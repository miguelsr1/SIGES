/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.security.DataSecurityException;
import com.sofis.security.OperationSecurity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.caux.KeyControlAsistencia;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgConsultaAsistenciasSedesEnNivel;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumProcesoCreacion;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAsistencia;
import sv.gob.mined.siges.filtros.FiltroAsistenciasSedeEnNivel;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroControlAsistenciaCabezal;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ControlAsistenciaCabezalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ControlAsistenciaCabezalDAO;
import sv.gob.mined.siges.persistencia.daos.SeguridadDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;
import sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaCabezal;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoInasistencia;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Stateless
@Traced
public class ControlAsistenciaCabezalBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private AsistenciaBean asistenciaBean;

    @Inject
    private CatalogoBean catalogoBean;

     @Inject
    private SeguridadBean seguridadBean;   
    private static final Logger LOGGER = Logger.getLogger(ControlAsistenciaCabezalBean.class.getName());

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgControlAsistenciaCabezal
     * @throws GeneralException
     */
    public SgControlAsistenciaCabezal obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgControlAsistenciaCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaCabezal.class);
                SgControlAsistenciaCabezal ret = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL);
                ret.getCacSeccion().getSecPk();
                return ret;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgControlAsistenciaCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaCabezal.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @return SgControlAsistenciaCabezal
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgControlAsistenciaCabezal guardar(SgControlAsistenciaCabezal entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ControlAsistenciaCabezalValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgControlAsistenciaCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaCabezal.class);

                    entity.setCacEstudiantesPresentes((int) entity.getCacAsistencia().stream().filter(a -> !a.getAsiInasistencia()).count());
                    entity.setCacEstudiantesAusentesJustificados((int) entity.getCacAsistencia().stream().filter(a -> BooleanUtils.isTrue(a.getAsiInasistencia()) && (a.getAsiMotivoInasistencia() != null ? a.getAsiMotivoInasistencia().getMinMotivoJustificado() : Boolean.FALSE)).count());
                    entity.setCacEstudiantesAusentesSinJustificar((int) entity.getCacAsistencia().stream().filter(a -> BooleanUtils.isTrue(a.getAsiInasistencia()) && (a.getAsiMotivoInasistencia() != null ? (!a.getAsiMotivoInasistencia().getMinMotivoJustificado()) : Boolean.FALSE)).count());
                    entity.setCacEstudiantesEnLista(entity.getCacAsistencia().size());
                    return codDAO.guardar(entity, entity.getCacPk() == null ? ConstantesOperaciones.CREAR_CONTROL_ASISTENCIA_CABEZAL : ConstantesOperaciones.ACTUALIZAR_CONTROL_ASISTENCIA_CABEZAL);
                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroControlAsistenciaCabezal filtro) throws GeneralException {
        try {
            ControlAsistenciaCabezalDAO codDAO = new ControlAsistenciaCabezalDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgControlAsistenciaCabezal> obtenerPorFiltro(FiltroControlAsistenciaCabezal filtro) throws GeneralException {
        try {
            ControlAsistenciaCabezalDAO codDAO = new ControlAsistenciaCabezalDAO(em, seguridadBean);
            List<SgControlAsistenciaCabezal> cabezales = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_CABEZAL);

            if (!cabezales.isEmpty() && filtro.getIncluirCampos() == null && BooleanUtils.isTrue(filtro.getIncluirSeccion())) {
                for (SgControlAsistenciaCabezal cab : cabezales) {
                    cab.getCacSeccion().getSecPk();
                }
            }

            if (!cabezales.isEmpty() && BooleanUtils.isTrue(filtro.getIncluirAsistencias())) {

                FiltroAsistencia fil = new FiltroAsistencia();
                fil.setAsiCabezalesPk(cabezales.stream().map(c -> c.getCacPk()).collect(Collectors.toList()));
                fil.setIncluirCampos(new String[]{
                    "asiEstudiante.estPk",
                    "asiEstudiante.estPersona.perNie",
                    "asiEstudiante.estPersona.perPrimerNombre", "asiEstudiante.estPersona.perSegundoNombre", "asiEstudiante.estPersona.perNombreBusqueda",
                    "asiEstudiante.estPersona.perPrimerApellido", "asiEstudiante.estPersona.perSegundoApellido", "asiEstudiante.estPersona.perFechaNacimiento",
                    "asiEstudiante.estVersion",
                    "asiControl.cacPk",
                    "asiControl.cacVersion",
                    "asiObservacion",
                    "asiInasistencia",
                    "asiMotivoInasistencia.minPk",
                    "asiMotivoInasistencia.minNombre",
                    "asiMotivoInasistencia.minCodigo",
                    "asiMotivoInasistencia.minMotivoJustificado",
                    "asiMotivoInasistencia.minVersion",
                    "asiVersion"});
                fil.setOrderBy(new String[]{"asiEstudiante.estPersona.perPrimerApellido", "asiEstudiante.estPersona.perSegundoApellido", "asiEstudiante.estPersona.perPrimerNombre", "asiEstudiante.estPersona.perSegundoNombre"});
                fil.setAscending(new boolean[]{true, true, true, true});

                List<SgAsistencia> asistencias = asistenciaBean.obtenerPorFiltro(fil);

                HashMap<Long, List<SgAsistencia>> asistenciasAgrupadas = new HashMap<>();

                for (SgAsistencia a : asistencias) {
                    asistenciasAgrupadas.computeIfAbsent(a.getAsiControl().getCacPk(), s -> new ArrayList<>()).add(a);
                }
                for (SgControlAsistenciaCabezal cab : cabezales) {
                    if (em.contains(cab)) {
                        em.detach(cab);
                    }
                    cab.setCacAsistencia(asistenciasAgrupadas.get(cab.getCacPk()) != null ? asistenciasAgrupadas.get(cab.getCacPk()) : new ArrayList<>());
                }
            }

            return cabezales;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgControlAsistenciaCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaCabezal.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CONTROL_ASISTENCIA_CABEZAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public void importar(String[][] entity) throws GeneralException {
        try {
            if (entity != null) {

                //Se obtienen todos los motivo de inasistencia
                FiltroCodiguera fcod = new FiltroCodiguera();
                fcod.setHabilitado(Boolean.TRUE);
                List<SgMotivoInasistencia> motivos = catalogoBean.buscarMotivoInasistencia(fcod);

                HashMap<Long, SgSeccion> secciones = new HashMap<>();
                HashMap<KeyControlAsistencia, List<String[]>> excel = new HashMap<>();
                for (String[] fila : entity) {
                    KeyControlAsistencia key = new KeyControlAsistencia(Long.valueOf(fila[0]), LocalDate.parse(fila[1]));

                    String[] datos = new String[5];
                    datos[0] = fila[2]; //código
                    datos[1] = fila[3]; //NIE
                    datos[2] = fila[4]; //Fálto
                    datos[3] = fila[5]; //Motivo
                    datos[4] = fila[6]; //Observación

                    if (excel.containsKey(key)) {
                        //Se agrega un nuevo registro esta clave
                        List<String[]> actual = excel.get(key);
                        actual.add(datos);
                        excel.put(key, actual);
                    } else {
                        //Se crea la llave y se agrega el nuevo registro
                        List<String[]> nuevaLista = new ArrayList<>();
                        nuevaLista.add(datos);
                        excel.put(key, nuevaLista);
                    }
                }

                for (Map.Entry<KeyControlAsistencia, List<String[]>> entry : excel.entrySet()) {

                    List<String[]> registros = entry.getValue();

                    //Validar que la sección exista.
                    if (!secciones.containsKey(entry.getKey().seccion)) {
                        FiltroSeccion fsec = new FiltroSeccion();
                        fsec.setSecPk(entry.getKey().seccion);
                        fsec.setIncluirCampos(new String[]{"secPk", "secEstado", "secVersion"});
                        List<SgSeccion> lseccion = this.seccionBean.obtenerPorFiltro(fsec);
                        if (lseccion.isEmpty()) {
                            BusinessException ge = new BusinessException();
                            ge.addError("La sección " + entry.getKey().seccion + " no se encontró", Errores.ERROR_SECCION_VACIO);
                            throw ge;
                        } else {
                            if (EnumSeccionEstado.CERRADA.equals(lseccion.get(0).getSecEstado())) {
                                BusinessException ge = new BusinessException();
                                ge.addError("La sección " + entry.getKey().seccion + " tiene estado cerrado", Errores.ERROR_SECCION_VACIO);
                                throw ge;
                            }
                            secciones.put(entry.getKey().seccion, lseccion.get(0));
                        }
                    }

                    SgSeccion seccion = secciones.get(entry.getKey().seccion);

                    //Se obtienen los NIE matriculados en la sección.
                    FiltroMatricula filtroMat = new FiltroMatricula();
                    filtroMat.setSecPk(seccion.getSecPk());
                    filtroMat.setMatFechaEntreIngresoHasta(entry.getKey().fecha);
                    filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPersona.perPk",
                        "matEstudiante.estPersona.perVersion", "matEstudiante.estPk",
                        "matEstudiante.estVersion"});
                    filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perNombreBusqueda"});
                    filtroMat.setAscending(new boolean[]{true});
                    List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(filtroMat);
                    List<SgEstudiante> listaEstudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).collect(Collectors.toList());

                    if (listaEstudiantes != null && registros != null && listaEstudiantes.size() == registros.size()) {

                        //Se valida si existe el control
                        FiltroControlAsistenciaCabezal fCabezal = new FiltroControlAsistenciaCabezal();
                        fCabezal.setSecPk(seccion.getSecPk());
                        fCabezal.setCacFecha(entry.getKey().fecha);
                        List<SgControlAsistenciaCabezal> listControl = this.obtenerPorFiltro(fCabezal);
                        SgControlAsistenciaCabezal entidadEnEdicion = null;
                        Boolean existeControl = Boolean.FALSE;

                        if (!listControl.isEmpty()) {
                            entidadEnEdicion = listControl.get(0);
                            em.detach(entidadEnEdicion);

                            FiltroAsistencia fil = new FiltroAsistencia();
                            fil.setAsiCabezalPk(entidadEnEdicion.getCacPk());
                            fil.setIncluirCampos(new String[]{
                                "asiEstudiante.estPk",
                                "asiEstudiante.estPersona.perNie",
                                "asiEstudiante.estVersion",
                                "asiControl.cacPk",
                                "asiControl.cacVersion",
                                "asiObservacion",
                                "asiInasistencia",
                                "asiMotivoInasistencia.minPk",
                                "asiMotivoInasistencia.minNombre",
                                "asiMotivoInasistencia.minCodigo",
                                "asiMotivoInasistencia.minMotivoJustificado",
                                "asiMotivoInasistencia.minVersion",
                                "asiVersion"});
                            fil.setOrderBy(new String[]{"asiEstudiante.estPersona.perNombreBusqueda"});
                            fil.setAscending(new boolean[]{true});
                            entidadEnEdicion.setCacAsistencia(asistenciaBean.obtenerPorFiltro(fil));
                            existeControl = Boolean.TRUE;
                        } else {
                            //Se crea el cabezal
                            entidadEnEdicion = new SgControlAsistenciaCabezal();
                            entidadEnEdicion.setCacSeccion(seccion);
                            entidadEnEdicion.setCacFecha(entry.getKey().fecha);
                            existeControl = Boolean.FALSE;
                        }

                        if (existeControl) {
                            //Se actualiza el control
                            for (SgAsistencia sgAsi : entidadEnEdicion.getCacAsistencia()) {
                                String[] estudiante = obtenerEstudiante(sgAsi.getAsiEstudiante().getEstPersona().getPerNie(), registros);

                                if (estudiante != null) {
                                    sgAsi.setAsiInasistencia(SofisStringUtils.normalizarParaBusqueda(estudiante[2]).equals("si"));
                                    if (sgAsi.getAsiInasistencia()) {
                                        //Se busca si existe el motivo de inasistencia
                                        SgMotivoInasistencia motivo = motivos
                                                .stream()
                                                .filter(m -> m.getMinNombreBusqueda().equals(SofisStringUtils.normalizarParaBusqueda(estudiante[3])))
                                                .findFirst().orElse(null);
                                        if (motivo != null) {
                                            sgAsi.setAsiMotivoInasistencia(motivo);
                                            sgAsi.setAsiObservacion(estudiante[4]);
                                        } else {
                                            BusinessException ge = new BusinessException();
                                            ge.addError("El motivo de inasistencia: " + estudiante[3] + " no existe.", Errores.ERROR_MOTIVO_INASISTENCIA_VACIO);
                                            throw ge;
                                        }
                                    } else {
                                        sgAsi.setAsiMotivoInasistencia(null);
                                        sgAsi.setAsiObservacion(null);
                                    }
                                } else {
                                    BusinessException ge = new BusinessException();
                                    ge.addError("Falta el registro del estudiante con NIE " + sgAsi.getAsiEstudiante().getEstPersona().getPerNie(), Errores.ERROR_ESTUDIANTE_VACIO);
                                    throw ge;
                                }
                            }

                        } else {
                            //Se crea uno nuevo
                            for (SgEstudiante est : listaEstudiantes) {
                                //Valida si el NIE esta matriculado en la sección.
                                String[] estudiante = obtenerEstudiante(est.getEstPersona().getPerNie(), registros);

                                if (estudiante != null) {
                                    SgAsistencia sgAsi = new SgAsistencia();
                                    sgAsi.setAsiControl(entidadEnEdicion);
                                    sgAsi.setAsiEstudiante(est);

                                    sgAsi.setAsiInasistencia(SofisStringUtils.normalizarParaBusqueda(estudiante[2]).equals("si"));
                                    if (BooleanUtils.isTrue(sgAsi.getAsiInasistencia())) {
                                        //Se obtiene el motivo de inasistencia
                                        SgMotivoInasistencia motivo = motivos != null && !motivos.isEmpty() ? motivos
                                                .stream()
                                                .filter((m) -> m.getMinNombreBusqueda().equals(SofisStringUtils.normalizarParaBusqueda(estudiante[3])))
                                                .findFirst()
                                                .orElse(null) : null;
                                        if (motivo != null) {
                                            sgAsi.setAsiMotivoInasistencia(motivo);
                                            sgAsi.setAsiObservacion(estudiante[4]);
                                        } else {
                                            BusinessException ge = new BusinessException();
                                            ge.addError("El motivo de inasistencia: " + estudiante[3] + " no existe.", Errores.ERROR_MOTIVO_INASISTENCIA_VACIO);
                                            throw ge;
                                        }
                                    }
                                    entidadEnEdicion.getCacAsistencia().add(sgAsi);
                                } else {
                                    BusinessException ge = new BusinessException();
                                    ge.addError("Falta el registro del estudiante con NIE " + est.getEstPersona().getPerNie(), Errores.ERROR_ESTUDIANTE_VACIO);
                                    throw ge;
                                }
                            }
                        }
                        entidadEnEdicion.setCacProcesoDeCreacion(EnumProcesoCreacion.IMP);
                        this.guardar(entidadEnEdicion);
                    } else {
                        BusinessException ge = new BusinessException();
                        ge.addError("La cantidad de estudiantes de la sección " + entry.getKey().seccion + " está incompleta para la fecha " + entry.getKey().fecha, Errores.ERROR_CANTIDAD_ESTUDIANTES_DIFERENTES_REGISTRADOS);
                        throw ge;
                    }

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    private Boolean buscarEstudiante(Long nie, List<String[]> listArray) {
        if (listArray.stream().anyMatch((fila) -> (Long.valueOf(fila[1]).equals(nie)))) {
            return true;
        }
        return false;
    }

    private String[] obtenerEstudiante(Long nie, List<String[]> listArray) {
        for (String[] fila : listArray) {
            if (Long.valueOf(fila[1]).equals(nie)) {
                return fila;
            }
        }
        return null;
    }

    public List<SgConsultaAsistenciasSedesEnNivel> obtenerAsistenciasPorSedeEnNivel(FiltroAsistenciasSedeEnNivel filtro) throws GeneralException {
        try {

            if (filtro.getCalNacionalAnioLectivoPk() == null || filtro.getCalInternacionalAnioLectivoPk() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_VACIO);
                throw be;
            }

            Session session = em.unwrap(Session.class);
            SeguridadDAO segDAO = new SeguridadDAO(em);

            String offset = "";
            String limit = "";
            String whereCabezal = "";
            String whereSede = "";

            String whereDataSecurity = "";
            List<OperationSecurity> ops = segDAO.obtenerOperacionesSeguridad(ConstantesOperaciones.BUSCAR_ASISTENCIAS_POR_SEDE, Lookup.obtenerJWT().getSubject());
            if (ops != null && ops.isEmpty()) {
                throw new DataSecurityException();
            }
            for (OperationSecurity o : ops) {
                if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
                    whereDataSecurity = "";
                    break;
                }
                if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
                    whereDataSecurity += " or sed.sed_pk = " + o.getContext() + " or sed.sed_centro_adscrito = " + o.getContext();
                } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
                    whereDataSecurity += " or dir.dir_departamento = " + o.getContext();
                } else {
                    //Sin acceso
                    whereDataSecurity += " or sed.sed_pk = -1 ";
                }
            }

            if (!StringUtils.isBlank(whereDataSecurity)) {
                whereDataSecurity = " and (" + whereDataSecurity.replaceFirst("or", "") + ")";
            }

            if (filtro.getFirst() != null) {
                offset = " offset :offset ";

            }

            if (filtro.getMaxResults() != null) {
                limit = " limit :limit ";
            }

            if (filtro.getSedNivel() != null) {
                whereCabezal += " and niv.niv_pk = :nivPk";

                whereSede += " and exists (select 1 from centros_educativos.sg_servicio_educativo sdu"
                        + "		INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                        + "		INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                        + "		INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)"
                        + "		INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)"
                        + "		INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk) where niv_pk = :nivPk and sdu.sdu_sede_fk = sed.sed_pk)";
            }

            if (BooleanUtils.isFalse(filtro.getIncluirAdscritas())) {
                whereSede += " and sed.sed_centro_adscrito is null";
            }

            if (filtro.getSedDepartamentoId() != null || filtro.getSedMunicipioId() != null) {
                if (filtro.getSedDepartamentoId() != null) {
                    whereSede += " and dir.dir_departamento = :depPk";
                }
                if (filtro.getSedMunicipioId() != null) {
                    whereSede += " and dir.dir_municipio = :munPk";
                }
            }

            if (filtro.getSedPk() != null) {
                whereSede += " and sed.sed_pk = :sedPk";
            }

            whereSede += whereDataSecurity;

            if (!StringUtils.isBlank(whereSede)) {
                whereSede = " where " + whereSede.replaceFirst("and", "");
            }

            SQLQuery q = session.createSQLQuery("select sedes.sed_departamento as departamento, sedes.sed_municipio as municipio, sedes.sed_nombre as sedeNombre, sedes.sed_pk as sedePk, sedes.sed_codigo as sedeCodigo, sedes.tce_nombre as tipoCalendarioNombre, h1.niv_nombre as desagregacion, sum(h1.cac_estudiantes_presentes) as asistencias, (sum(h1.cac_estudiantes_en_lista) - sum(h1.cac_estudiantes_presentes)) as inasistencias  from "
                    + " ("
                    + "(select sed.sed_pk as sed_pk, sed.sed_nombre as sed_nombre, sed.sed_codigo as sed_codigo, mun.mun_nombre as sed_municipio, dep.dep_nombre as sed_departamento, tce.tce_nombre, calendarios.cal_fecha_inicio, calendarios.cal_fecha_fin, calendarios.cal_anio_lectivo_fk from centros_educativos.sg_sedes sed "
                    + " INNER JOIN centros_educativos.sg_direcciones dir ON (dir.dir_pk = sed.sed_direccion_fk) "
                    + " INNER JOIN catalogo.sg_departamentos dep ON (dir.dir_departamento = dep.dep_pk) "
                    + " INNER JOIN catalogo.sg_municipios mun ON (dir.dir_municipio = mun.mun_pk) "
                    + " INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)"
                    + "	INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND ((cal.cal_tipo_calendario_fk = " + Constantes.TIPO_CALENDARIO_NACIONAL_PK + " AND cal.cal_anio_lectivo_fk = :anioLectivoNacional) OR (cal.cal_tipo_calendario_fk = " + Constantes.TIPO_CALENDARIO_INTERNACIONAL_PK + " AND cal.cal_anio_lectivo_fk = :anioLectivoInternacional)) limit 1))"
                    + whereSede
                    + " order by sed_departamento, sed_municipio, sed_nombre"
                    + offset + " " + limit + ") as sedes"
                    + " LEFT OUTER JOIN"
                    + " "
                    + " (select sdu.sdu_sede_fk, niv.niv_pk, niv.niv_nombre, sec.sec_anio_lectivo_fk, cac.cac_fecha, cac.cac_estudiantes_presentes, cac.cac_estudiantes_en_lista from centros_educativos.sg_control_asistencia_cabezal cac"
                    + " INNER JOIN centros_educativos.sg_secciones sec ON (cac.cac_seccion_fk = sec.sec_pk)"
                    + " INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)"
                    + " INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                    + " INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                    + " INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)"
                    + " INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)"
                    + " INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)"
                    + " where cac.cac_fecha <= now() " + whereCabezal + ") as h1"
                    + " ON (h1.sdu_sede_fk = sedes.sed_pk AND h1.cac_fecha >= sedes.cal_fecha_inicio AND h1.cac_fecha <= sedes.cal_fecha_fin AND h1.sec_anio_lectivo_fk = sedes.cal_anio_lectivo_fk))"
                    + " group by sedes.sed_departamento, sedes.sed_municipio, sedes.sed_pk, sedes.sed_nombre, sedes.sed_codigo, sedes.tce_nombre, h1.niv_pk, h1.niv_nombre"
                    + " ");

            q.addScalar("departamento", new StringType());
            q.addScalar("municipio", new StringType());
            q.addScalar("sedeCodigo", new StringType());
            q.addScalar("sedePk", new LongType());
            q.addScalar("sedeNombre", new StringType());
            q.addScalar("tipoCalendarioNombre", new StringType());
            q.addScalar("desagregacion", new StringType());
            q.addScalar("inasistencias", new LongType());
            q.addScalar("asistencias", new LongType());

            if (filtro.getSedDepartamentoId() != null) {
                q.setParameter("depPk", filtro.getSedDepartamentoId());
            }
            if (filtro.getSedMunicipioId() != null) {
                q.setParameter("munPk", filtro.getSedMunicipioId());
            }
            if (filtro.getSedNivel() != null) {
                q.setParameter("nivPk", filtro.getSedNivel());
            }
            if (filtro.getSedPk() != null) {
                q.setParameter("sedPk", filtro.getSedPk());
            }

            q.setParameter("anioLectivoNacional", filtro.getCalNacionalAnioLectivoPk());
            q.setParameter("anioLectivoInternacional", filtro.getCalInternacionalAnioLectivoPk());

            if (filtro.getFirst() != null) {
                q.setParameter("offset", filtro.getFirst());
            }
            if (filtro.getMaxResults() != null) {
                q.setParameter("limit", filtro.getMaxResults());
            }
            q.setResultTransformer(Transformers.aliasToBean(SgConsultaAsistenciasSedesEnNivel.class));
            return q.list();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
