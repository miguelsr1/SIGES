/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.security.OperationSecurity;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.SQLQuery;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.type.IntegerType;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumEstadoImportado;
import sv.gob.mined.siges.enumerados.EnumEstadoPersona;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.MatriculaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MatriculaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroPeriodoCalendario;
import sv.gob.mined.siges.filtros.FiltroSolicitudTraslado;
import sv.gob.mined.siges.persistencia.daos.MatriculaParcialDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoBitMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;
import sv.gob.mined.siges.persistencia.entidades.SgMatriculaParcial;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.dto.SgConsultaMatriculasSede;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgConsultaMatriculasSeccionResponse;
import sv.gob.mined.siges.dto.SgConsultaMatriculasSeccionResponseSAT;
import sv.gob.mined.siges.dto.SgDatoDashboard;
import sv.gob.mined.siges.dto.SgGradoPlanOrigenDestino;
import sv.gob.mined.siges.dto.SgModificarFechasMatricula;
import sv.gob.mined.siges.filtros.FiltroMatriculasEnSede;
import sv.gob.mined.siges.dto.SgPromRepMatriculas;
import sv.gob.mined.siges.dto.SgRangoEdad;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumPromocionGradoMatricula;
import sv.gob.mined.siges.enumerados.EnumServicioEducativoEstado;
import sv.gob.mined.siges.filtros.FiltroAllegado;
import sv.gob.mined.siges.filtros.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgNie;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroEstudianteImpresion;
import sv.gob.mined.siges.filtros.FiltroMatriculasEnSeccion;
import sv.gob.mined.siges.filtros.FiltroMatriculasEnSeccionSAT;
import sv.gob.mined.siges.filtros.FiltroRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.filtros.FiltroTitulo;
import sv.gob.mined.siges.negocio.validaciones.ArchivoBitMatriculaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.SeguridadDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.persistencia.entidades.SgTitulo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracion;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Stateless
@Traced
public class MatriculaBean {

    private static final Logger LOGGER = Logger.getLogger(MatriculaBean.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private EstudianteBean estudianteBean;

    @Inject
    private AllegadoBean allegadoBean;

    @Inject
    private NieBean nieBean;

    @Inject
    private CalendarioEscolarBean calendarioEscolarBean;

    @Inject
    private SolicitudTrasladoBean solicitudTrasladoBean;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private ArchivoBitMatriculaBean archivoBitMatriculaBean;

    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ReglaEquivalenciaBean reglaEquivalenciaBean;

    @Inject
    private EscolaridadEstudianteBean escolaridadEstudianteBean;

    @Inject
    private EstudianteImpresionBean estudianteImpresionBean;

    @Inject
    private TituloBean tituloBean;

    @Inject
    private RelHabilitacionMatriculaNivelBean relHabilitacionMatriculaNivelBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMatricula
     * @throws GeneralException
     */
    public SgMatricula obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                SgMatricula mat = codDAO.obtenerPorId(id, null);
                mat.getMatSeccion().getSecPk();
                mat.getMatEstudiante().getEstPk();
                return mat;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroMatricula filtro) throws GeneralException {
        try {
            MatriculaDAO codDAO = new MatriculaDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgMatricula
     * @throws GeneralException
     */
    public List<SgMatricula> obtenerPorFiltro(FiltroMatricula filtro) throws GeneralException {
        try {
            MatriculaDAO codDAO = new MatriculaDAO(em, seguridadBean);
            if (filtro.getMatEstudiantePk() != null) {
                filtro.setSecurityOperation(null);
            }

            if (filtro.getSecGradoSiguienteFk() != null) {
                //Al buscar matriculas del grado anterior hay que ver si la modalidad de dicho grado tiene opción y programa educativo
                //En caso de que no tengan se sacan de los filtros
                //Ejemplo: primer grado de técnico vocacional tiene opciones, pero la precedencia (noveno de ciclo 3) no
                if (!gradoTieneOpcion(filtro.getSecGradoSiguienteFk())) {
                    filtro.setSecOpcionFk(null);
                    filtro.setSecProgramaEducativoFk(null);
                }
            }
            List<SgMatricula> matriculas = codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());

            if (!matriculas.isEmpty() && BooleanUtils.isTrue(filtro.getIncluirResponsableOContactoEmergencia())) {
                FiltroAllegado filtroAll = new FiltroAllegado();
                filtroAll.setAllPersonasReferenciadasPk(matriculas.stream().map(s -> s.getMatEstudiante().getEstPersona().getPerPk()).collect(Collectors.toList()));
                filtroAll.setAllEsReferenteOContactoEmergencia(Boolean.TRUE);
                filtroAll.setIncluirCampos(new String[]{"allPersona.perNie", "allPersona.perPk", "allPersonaReferenciada.perPk",
                    "allPersona.perPrimerNombre", "allPersona.perSegundoNombre", "allPersona.perNombreBusqueda",
                    "allPersona.perPrimerApellido", "allPersona.perSegundoApellido", "allPersona.perFechaNacimiento",
                    "allVersion", "allReferente", "allContactoEmergencia"});

                List<SgAllegado> allegados = allegadoBean.obtenerPorFiltro(filtroAll);
                HashMap<Long, List<SgAllegado>> allegadosAgrupados = new HashMap<>();
                for (SgAllegado all : allegados) {
                    allegadosAgrupados.computeIfAbsent(all.getAllPersonaReferenciada().getPerPk(), s -> new ArrayList<>()).add(all);
                }
                for (SgMatricula mat : matriculas) {
                    mat.getMatEstudiante().getEstPersona().setPerAllegados(allegadosAgrupados.get(mat.getMatEstudiante().getEstPersona().getPerPk()) != null ? allegadosAgrupados.get(mat.getMatEstudiante().getEstPersona().getPerPk()) : new ArrayList<>());
                }
            }

            return matriculas;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    private Boolean gradoTieneOpcion(Long gradoSiguiente) {
        Query q = em.createNativeQuery("select opc_pk from centros_educativos.sg_opciones o inner join centros_educativos.sg_modalidades m on m.mod_pk=o.opc_modalidad_fk\n"
                + "where exists (select 1 from centros_educativos.sg_rel_grado_precedente rg inner join centros_educativos.sg_grados g \n"
                + "on g.gra_pk=rg.rgp_grado_origen_fk\n"
                + "inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on g.gra_relacion_modalidad_fk=rel.rea_pk\n"
                + "inner join centros_educativos.sg_modalidades m_aux on m_aux.mod_pk=rel.rea_modalidad_educativa_fk\n"
                + "where m_aux.mod_pk=m.mod_pk and rg.rgp_grado_destino_fk=:dest)");
        q.setParameter("dest", gradoSiguiente);

        List<Object[]> objs = (List<Object[]>) q.getResultList();
        if (objs.isEmpty()) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public SgMatricula guardar(SgMatricula entity) throws GeneralException {
        try {
            if (entity != null) {
                if (entity.getMatPk() == null) {
                    return this.crear(entity);
                } else {
                    return this.actualizar(entity);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return entity;
    }

    @Interceptors({AuditInterceptor.class})
    public SgMatriculaParcial guardarMatriculaParcial(SgMatriculaParcial entity) throws GeneralException {
        try {
            if (entity != null) {
                CodigueraDAO<SgMatriculaParcial> matDAO = new CodigueraDAO<>(em, SgMatriculaParcial.class);
                entity.setMatCreacionUsuario(Lookup.obtenerJWT().getSubject());
                return matDAO.guardar(entity, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Crea el objeto indicado
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula crear(SgMatricula entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (MatriculaValidacionNegocio.validarCrear(entity)) {
                    CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                    SgSeccion seccion = em.find(SgSeccion.class, entity.getMatSeccion().getSecPk());
                    if (seccion.getSecEstado().equals(EnumSeccionEstado.CERRADA)) {
                        BusinessException ge = new BusinessException();
                        ge.addError("matEstudiante", Errores.ERROR_SECCION_CERRADA);
                        throw ge;
                    }
                    if (!seccion.getSecServicioEducativo().getSduEstado().equals(EnumServicioEducativoEstado.HABILITADO)) {
                        BusinessException ge = new BusinessException();
                        ge.addError("matEstudiante", Errores.ERROR_SERVICIO_EDUCATIVO_NO_HABILITADO);
                        throw ge;
                    }
                    if (entity.getMatEstudiante().getEstPk() != null) {

                        FiltroMatricula fmat = new FiltroMatricula();
                        fmat.setMatEstudiantePk(entity.getMatEstudiante().getEstPk());
                        fmat.setMatEstado(EnumMatriculaEstado.ABIERTO);
                        fmat.setSecurityOperation(null);
                        Long cant = this.obtenerTotalPorFiltro(fmat);
                        if (cant > 0) {
                            BusinessException ge = new BusinessException();
                            ge.addError("matEstudiante", Errores.ERROR_MATRICULA_ABIERTA_ESTUDIANTE);
                            throw ge;
                        }

                        //Validar la fecha de ingreso no se intercepte con otro registro
                        fmat = new FiltroMatricula();
                        fmat.setMatEstudiantePk(entity.getMatEstudiante().getEstPk());
                        fmat.setMatFechaDesdeSup(entity.getMatFechaIngreso());
                        fmat.setSecurityOperation(null);
                        cant = this.obtenerTotalPorFiltro(fmat);

                        if (cant > 0) {
                            BusinessException ge = new BusinessException();
                            ge.addError("matFechaIngreso", Errores.ERROR_FECHA_INGRESO_SUPERPONE);
                            throw ge;
                        }

                    }

                    FiltroPeriodoCalendario fpc = new FiltroPeriodoCalendario();
                    fpc.setCesModalidadAtencionFk(entity.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                    fpc.setCesNivelFk(entity.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                    fpc.setCesSubModalidadAtencionFk(entity.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? entity.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                    fpc.setCesTipo(EnumCalendarioEscolar.MAT);
                    fpc.setCesHabilitado(Boolean.TRUE);
                    fpc.setFechaCalificacion(entity.getMatFechaIngreso());

                    Long cantidad = calendarioEscolarBean.obtenerTotalPorFiltro(fpc);
                    if (cantidad.equals(0L)) {
                        FiltroRelHabilitacionMatriculaNivel frhm = new FiltroRelHabilitacionMatriculaNivel();
                        frhm.setNivelAndNullOrNivelAndModAtSubMod(Boolean.TRUE);
                        frhm.setRhnNivelFk(entity.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                        frhm.setRhnModalidadAtencionFk(entity.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        frhm.setRhnSubmodalidadFk(entity.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? entity.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                        frhm.setRhnFecha(LocalDate.now());
                        frhm.setRhnSedeFk(entity.getMatSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                        frhm.setRhnEstadoHabilitacion(EnumEstadoHabilitacionPeriodoMatricula.APROBADA);
                        frhm.setIncluirCampos(new String[]{"rhnVersion"});
                        frhm.setMaxResults(1L);

                        List<SgRelHabilitacionMatriculaNivel> rel = relHabilitacionMatriculaNivelBean.obtenerPorFiltro(frhm);

                        if (rel.isEmpty()) {
                            BusinessException ge = new BusinessException();
                            ge.addError("matEstudiante", Errores.ERROR_PERIODO_MATRICULA_CERRADO);
                            throw ge;
                        }
                    }

                    Boolean matValidadaAcademicamente = Boolean.FALSE;
                    entity.setMatRepetidor(Boolean.FALSE);

                    if (entity.getMatEstudiante().getEstPk() != null) {
                        //Verificar si el estudiante es repetidor
                        FiltroMatricula fmat = new FiltroMatricula();
                        fmat.setMatEstudiantePk(entity.getMatEstudiante().getEstPk());
                        fmat.setOrderBy(new String[]{"matFechaIngreso"});
                        fmat.setAscending(new boolean[]{false});
                        fmat.setMaxResults(1L);
                        fmat.setIncluirCampos(new String[]{"matSeccion.secServicioEducativo.sduGrado.graPk", "matSeccion.secPlanEstudio.pesPk",
                            "matSeccion.secServicioEducativo.sduProgramaEducativo.pedPk",
                            "matSeccion.secServicioEducativo.sduOpcion.opcPk", "matRepetidor",
                            "matSeccion.secServicioEducativo.sduGrado.graRequiereNIE",
                            "matRetirada", "matValidacionAcademica",
                            "matValidacionAcademicaFecha",
                            "matPromocionGrado",
                            "matValidacionAcademicaUsuario"});
                        fmat.setSecurityOperation(null);

                        List<SgMatricula> mat = this.obtenerPorFiltro(fmat);
                        if (mat.isEmpty()) {

                            if (BooleanUtils.isNotFalse(seccion.getSecServicioEducativo().getSduGrado().getGraRequiereNIE())) {
                                if (entity.getMatEstudiante().getEstPk() != null) {

                                    if (!reglaEquivalenciaBean.gradoCumplePrecedenciaOEquivalenciaPrecedenciaParaEstudiante(seccion.getSecServicioEducativo().getSduGrado().getGraPk(), entity.getMatEstudiante().getEstPk())) {
                                        BusinessException ge = new BusinessException();
                                        ge.addError("matEstudiante", Errores.ERROR_NO_CUMPLE_PRECEDENCIA_REGLA_EQUIVALENCIA);
                                        throw ge;
                                    }
                                }
                            }

                        } else {
                            //Anterior siempre es cerrada
                            SgMatricula anterior = mat.get(0);

                            if (BooleanUtils.isFalse(anterior.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRequiereNIE())
                                    && BooleanUtils.isFalse(seccion.getSecServicioEducativo().getSduGrado().getGraRequiereNIE())) {
                                // no hay validaciones de equivalencia
                            } else {

                                if (entity.getMatEstudiante().getEstPk() != null) {

                                    if (!reglaEquivalenciaBean.gradoCumplePrecedenciaOEquivalenciaPrecedenciaParaEstudiante(seccion.getSecServicioEducativo().getSduGrado().getGraPk(), entity.getMatEstudiante().getEstPk())) {

                                        if (BooleanUtils.isTrue(anterior.getMatRetirada()) || EnumPromocionGradoMatricula.NO_PROMOVIDO.equals(anterior.getMatPromocionGrado())) {

                                            SgGradoPlanOrigenDestino od = new SgGradoPlanOrigenDestino();

                                            od.getOrigen().setGrado(anterior.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                                            od.getOrigen().setPlanEstudio(anterior.getMatSeccion().getSecPlanEstudio().getPesPk());
                                            od.getOrigen().setProgramaEducativo(anterior.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? anterior.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
                                            od.getOrigen().setOpcion(anterior.getMatSeccion().getSecServicioEducativo().getSduOpcion() != null ? anterior.getMatSeccion().getSecServicioEducativo().getSduOpcion().getOpcPk() : null);

                                            od.getDestino().setGrado(entity.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                                            od.getDestino().setPlanEstudio(entity.getMatSeccion().getSecPlanEstudio().getPesPk());
                                            od.getDestino().setProgramaEducativo(entity.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? entity.getMatSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedPk() : null);
                                            od.getDestino().setOpcion(entity.getMatSeccion().getSecServicioEducativo().getSduOpcion() != null ? entity.getMatSeccion().getSecServicioEducativo().getSduOpcion().getOpcPk() : null);

                                            //Cuando la matrícula es retirada hay que validar que el grado sea igual o equivalente al anterior
                                            if (!reglaEquivalenciaBean.cumpleReglaEquivalencia(od)) {
                                                BusinessException ge = new BusinessException();
                                                ge.addError("matEstudiante", Errores.ERROR_NO_CUMPLE_PRECEDENCIA_REGLA_EQUIVALENCIA);
                                                throw ge;
                                            }
                                            if (EnumPromocionGradoMatricula.NO_PROMOVIDO.equals(anterior.getMatPromocionGrado())) {
                                                entity.setMatRepetidor(Boolean.TRUE);
                                            } else {
                                                entity.setMatRepetidor(anterior.getMatRepetidor());
                                            }

                                        } else {
                                            BusinessException ge = new BusinessException();
                                            ge.addError("matEstudiante", Errores.ERROR_NO_CUMPLE_PRECEDENCIA_REGLA_EQUIVALENCIA);
                                            throw ge;
                                        }
                                    } else {

                                        if (BooleanUtils.isTrue(anterior.getMatRetirada()) || EnumPromocionGradoMatricula.NO_PROMOVIDO.equals(anterior.getMatPromocionGrado())) {

                                            if (EnumPromocionGradoMatricula.NO_PROMOVIDO.equals(anterior.getMatPromocionGrado())) {
                                                entity.setMatRepetidor(Boolean.TRUE);
                                            } else {
                                                entity.setMatRepetidor(anterior.getMatRepetidor());
                                            }

                                        }

                                    }

                                }
                            }

                            if (BooleanUtils.isTrue(anterior.getMatValidacionAcademica())) {
                                matValidadaAcademicamente = Boolean.TRUE;
                                entity.setMatValidacionAcademicaFecha(anterior.getMatValidacionAcademicaFecha());
                                entity.setMatValidacionAcademicaUsuario(anterior.getMatValidacionAcademicaUsuario());
                            }
                        }
                    }
                    String userCode = Lookup.obtenerJWT().getSubject();
                    this.eliminarMatriculasParcialesDeUsuario(userCode);
                    SgAllegado all = entity.getMatEstudiante().getEstPersona().getPerResponsable();

                    SgGrado grado = entity.getMatSeccion().getSecServicioEducativo().getSduGrado();
                    //Grado no requiere nie, entonces se lo generamos. Si grado requiere NIE, la persona ya debería tener NIE asignado.
                    if (grado != null && !grado.getGraRequiereNIE() && entity.getMatEstudiante().getEstPersona().getPerNie() == null) {
                        SgNie nie = nieBean.generarNie();
                        entity.getMatEstudiante().getEstPersona().setPerNie(nie.getNiePk());
                    }

                    //Grado requiere NIE y el estudiante no tiene, vemos que tenga operacion de PERMIT_MATRICULAR_SIN_NIE
                    //en caso que no la tenga manda error
                    //en caso que la tenga -> si tiene operacion CREAR_NIE lo crea, sino lo deja vacío
                    if (grado != null && grado.getGraRequiereNIE() && entity.getMatEstudiante().getEstPersona().getPerNie() == null) {

                        SeguridadDAO segDAO = new SeguridadDAO(em);
                        List<OperationSecurity> ops = segDAO.obtenerOperacionesSeguridad(ConstantesOperaciones.PERMITE_MATRICULAR_SIN_NIE, Lookup.obtenerJWT().getSubject());
                        if (ops.isEmpty()) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_GRADO_REQUIERE_NIE);
                            throw be;
                        } else {
                            ops = segDAO.obtenerOperacionesSeguridad(ConstantesOperaciones.CREAR_NIE, Lookup.obtenerJWT().getSubject());
                            if (!ops.isEmpty()) {
                                SgNie nie = nieBean.generarNie();
                                entity.getMatEstudiante().getEstPersona().setPerNie(nie.getNiePk());
                            }
                        }

                    }

                    entity.getMatEstudiante().setEstHabilitado(Boolean.TRUE);

                    SgEstudiante est = estudianteBean.guardar(entity.getMatEstudiante(), Boolean.FALSE);


                    if (all != null) {
                        all.setAllPersonaReferenciada(est.getEstPersona());
                        allegadoBean.guardar(all);
                    }

                    entity.setMatEstudiante(est);
                    entity.setMatValidacionAcademica(matValidadaAcademicamente);
                    entity.setMatEstado(EnumMatriculaEstado.ABIERTO);
                    entity.setMatRetirada(Boolean.FALSE);
                    entity.setMatAnulada(Boolean.FALSE);

// Verificar
//                    String queryda = "DELETE FROM SgAsistencia WHERE asiPk IN "
//                            + "(SELECT a.asiPk FROM SgAsistencia a "
//                            + "INNER JOIN a.asiEstudiante est "
//                            + "INNER JOIN a.asiControl c "
//                            + "where est.estPk = :estPk "
//                            + "and c.cacFecha >= :matFechaIngreso) ";
//                    Query qda = em.createQuery(queryda)
//                            .setParameter("estPk", entity.getMatEstudiante().getEstPk())
//                            .setParameter("matFechaHasta", entity.getMatFechaIngreso());
//                    qda.executeUpdate();
                    return codDAO.guardar(entity, ConstantesOperaciones.CREAR_MATRICULA);
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
     * Crea las matriculas que recibe como param. Se asume que son matrículas
     * válidas ya que provienen de una existente.
     *
     * @param entity List<SgMatricula>
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public void generarPromocionesYRepeticiones(SgPromRepMatriculas promMat) throws GeneralException {
        try {
            BusinessException ge = new BusinessException();
            CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);

            if (MatriculaValidacionNegocio.validarPromociones(promMat)) {

                if (promMat.getNuevaSeccion().getSecEstado().equals(EnumSeccionEstado.CERRADA)) {
                    ge.addError("matEstudiante", Errores.ERROR_SECCION_CERRADA);
                    throw ge;
                }
                if (!promMat.getNuevaSeccion().getSecServicioEducativo().getSduEstado().equals(EnumServicioEducativoEstado.HABILITADO)) {
                    ge.addError("matEstudiante", Errores.ERROR_SERVICIO_EDUCATIVO_NO_HABILITADO);
                    throw ge;
                }

                //Validar la fecha de ingreso no se intercepte con otro registro
                FiltroMatricula fmat = new FiltroMatricula();
                if (promMat.getMatriculasPromovidas() != null) {
                    fmat.setMatEstudiantesPk(promMat.getMatriculasPromovidas().stream().map(m -> m.getMatEstudiante().getEstPk()).collect(Collectors.toList()));
                }
                if (promMat.getMatriculasRepetidoras() != null) {
                    fmat.getMatEstudiantesPk().addAll(promMat.getMatriculasRepetidoras().stream().map(m -> m.getMatEstudiante().getEstPk()).collect(Collectors.toList()));
                }
                fmat.setMatFechaDesdeSup(promMat.getFechaIngreso());
                fmat.setSecurityOperation(null);
                Long cant = this.obtenerTotalPorFiltro(fmat);
                if (cant > 0) {
                    ge.addError("matFechaIngreso", Errores.ERROR_FECHA_INGRESO_SUPERPONE);
                    throw ge;
                }

                FiltroPeriodoCalendario fpc = new FiltroPeriodoCalendario();
                fpc.setCesModalidadAtencionFk(promMat.getNuevaSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                fpc.setCesNivelFk(promMat.getNuevaSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                fpc.setCesSubModalidadAtencionFk(promMat.getNuevaSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? promMat.getNuevaSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                fpc.setCesTipo(EnumCalendarioEscolar.MAT);
                fpc.setFechaCalificacion(promMat.getFechaIngreso());
                Long cantidad = calendarioEscolarBean.obtenerTotalPorFiltro(fpc);
                if (cantidad.equals(0L)) {
                    ge.addError("matEstudiante", Errores.ERROR_PERIODO_MATRICULA_CERRADO);
                    throw ge;
                }

                for (SgMatricula m : promMat.getMatriculasPromovidas()) {
                    SgMatricula nueva = new SgMatricula();
                    nueva.setMatSeccion(promMat.getNuevaSeccion());
                    nueva.setMatEstudiante(m.getMatEstudiante());
                    nueva.setMatFechaIngreso(promMat.getFechaIngreso());
                    nueva.setMatProvisional(m.getMatProvisional());
                    nueva.setMatValidacionAcademica(m.getMatValidacionAcademica());
                    codDAO.guardar(nueva, null);
                }

                for (SgMatricula m : promMat.getMatriculasRepetidoras()) {
                    SgMatricula nueva = new SgMatricula();
                    nueva.setMatSeccion(promMat.getNuevaSeccion());
                    nueva.setMatEstudiante(m.getMatEstudiante());
                    nueva.setMatFechaIngreso(promMat.getFechaIngreso());
                    nueva.setMatProvisional(m.getMatProvisional());
                    nueva.setMatValidacionAcademica(m.getMatValidacionAcademica());
                    nueva.setMatRepetidor(Boolean.TRUE);
                    codDAO.guardar(nueva, null);
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Actualiza el objeto indicado
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula actualizar(SgMatricula entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (MatriculaValidacionNegocio.validarActualizar(entity)) {
                    CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                    return codDAO.guardar(entity, ConstantesOperaciones.ACTUALIZAR_MATRICULA);
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
     * Actualiza fechas del objeto indicado
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula modificarFechas(SgModificarFechasMatricula mod) throws GeneralException {
        try {
            if (mod != null) {
                if (mod.getMatPk() == null) {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_MATRICULA_VACIO);
                    throw be;
                }

                if (mod.getEstudiantePk() == null) {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_ESTUDIANTE_VACIO);
                    throw be;
                }

                if (mod.getFechaIngreso() == null) {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_FECHA_INGRESO_VACIO);
                    throw be;
                }

                SgMatricula entity = em.find(SgMatricula.class, mod.getMatPk());

                //Validar la fecha de ingreso no se intercepte con otro registro
                FiltroMatricula fmat = new FiltroMatricula();
                fmat.setMatEstudiantePk(mod.getEstudiantePk());
                fmat.setMatExcluirPk(Arrays.asList(mod.getMatPk()));
                fmat.setMatFechaDesdeSup(mod.getFechaIngreso());

                if (entity.getMatEstado().equals(EnumMatriculaEstado.CERRADO)) {
                    if (mod.getFechaHasta() == null) {
                        BusinessException be = new BusinessException();
                        be.addError(Errores.ERROR_FECHA_HASTA_VACIO);
                        throw be;
                    }
                    if (!MatriculaValidacionNegocio.validarHastaDesde(mod.getFechaIngreso(), mod.getFechaHasta())) {
                        BusinessException ge = new BusinessException();
                        ge.addError("matFechaIngreso", Errores.ERROR_FECHA_RETIRO_MENOR_INGRESO);
                        throw ge;
                    }
                    //Intersección
                    fmat.setMatFechaHastaSup(mod.getFechaHasta());
                }

                fmat.setSecurityOperation(null);
                Long cant = this.obtenerTotalPorFiltro(fmat);

                if (cant <= 0) {
                    CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);

                    entity.setMatFechaIngreso(mod.getFechaIngreso());
                    entity.setMatFechaHasta(mod.getFechaHasta());

                    return codDAO.guardar(entity, ConstantesOperaciones.ACTUALIZAR_MATRICULA);
                } else {
                    BusinessException ge = new BusinessException();
                    ge.addError("matFechaIngreso", Errores.ERROR_FECHA_INGRESO_SUPERPONE);
                    throw ge;
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Anular matricula con la id indicada
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula anularMatricula(Long matPk) throws GeneralException {
        SgMatricula entity = null;
        try {
            if (matPk != null) {

                CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                entity = em.find(SgMatricula.class, matPk);

                //Verificar si estudiante tiene más de una matrícula para la misma sección.
                //Si tiene más de una, entonces la fecha de las calificaciones y asistencias a borrar,
                //deben ser mayores a la fecha de ingreso de la matrícula que se está anulando
                String queryCantMats = "SELECT COUNT(*) FROM SgMatricula m WHERE "
                        + " m.matEstudiante.estPk = :estPk and m.matSeccion.secPk = :secPk and matAnulada = false";

                Long cant = (Long) em.createQuery(queryCantMats)
                        .setParameter("estPk", entity.getMatEstudiante().getEstPk())
                        .setParameter("secPk", entity.getMatSeccion().getSecPk())
                        .getSingleResult();

                //Borrar calificaciones
                String querydc = "DELETE FROM SgCalificacionEstudiante WHERE caePk IN "
                        + "(SELECT ce.caePk FROM SgCalificacionEstudiante ce "
                        + "INNER JOIN ce.caeEstudiante est "
                        + "INNER JOIN ce.caeCalificacion c "
                        + "INNER JOIN c.calSeccion s "
                        + "where est.estPk = :estPk and s.secPk = :secPk ";

                if (cant > 1) {
                    querydc += "and c.calFecha >= :matFechaIngreso ";
                    if (entity.getMatFechaHasta() != null) {
                        querydc += "and c.calFecha <= :matFechaHasta ";
                    }
                }
                querydc += ")";

                Query qdc = em.createQuery(querydc)
                        .setParameter("estPk", entity.getMatEstudiante().getEstPk())
                        .setParameter("secPk", entity.getMatSeccion().getSecPk());
                if (cant > 1) {
                    qdc = qdc.setParameter("matFechaIngreso", entity.getMatFechaIngreso());
                    if (entity.getMatFechaHasta() != null) {
                        qdc = qdc.setParameter("matFechaHasta", entity.getMatFechaHasta());
                    }
                }
                qdc.executeUpdate();

                //Borrar asistencias
                String queryda = "DELETE FROM SgAsistencia WHERE asiPk IN "
                        + "(SELECT a.asiPk FROM SgAsistencia a "
                        + "INNER JOIN a.asiEstudiante est "
                        + "INNER JOIN a.asiControl c "
                        + "INNER JOIN c.cacSeccion s "
                        + "where est.estPk = :estPk and s.secPk = :secPk ";

                if (cant > 1) {
                    queryda += "and c.cacFecha >= :matFechaIngreso ";
                    if (entity.getMatFechaHasta() != null) {
                        queryda += "and c.cacFecha <= :matFechaHasta ";
                    }
                }
                queryda += ")";

                Query qda = em.createQuery(queryda)
                        .setParameter("estPk", entity.getMatEstudiante().getEstPk())
                        .setParameter("secPk", entity.getMatSeccion().getSecPk());
                if (cant > 1) {
                    qda = qda.setParameter("matFechaIngreso", entity.getMatFechaIngreso());
                    if (entity.getMatFechaHasta() != null) {
                        qda = qda.setParameter("matFechaHasta", entity.getMatFechaHasta());
                    }
                }
                qda.executeUpdate();

                entity.setMatAnulada(Boolean.TRUE);
                entity.setMatEstado(EnumMatriculaEstado.CERRADO);
                entity.setMatFechaHasta(entity.getMatFechaIngreso());
                return codDAO.guardar(entity, ConstantesOperaciones.ANULAR_MATRICULA);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return entity;
    }

    /**
     * Validar matricula provisional del objeto con la id indicada
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula validarMatriculaProvisional(Long matPk) throws GeneralException {
        SgMatricula entity = null;
        try {
            if (matPk != null) {
                CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                entity = em.find(SgMatricula.class, matPk);
                entity.setMatProvisional(Boolean.FALSE);
                return codDAO.guardar(entity, ConstantesOperaciones.VALIDAR_MATRICULA_PROVISIONAL);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return entity;
    }

    /**
     * Retira la matricula
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula retirar(SgMatricula entity) throws GeneralException {
        try {
            if (entity != null) {

                SgMatricula mat = this.obtenerPorId(entity.getMatPk());

                //Validar que no tenga un traslado activo
                FiltroSolicitudTraslado fsol = new FiltroSolicitudTraslado();
                fsol.setPerPersonaPk(mat.getMatEstudiante().getEstPersona().getPerPk());
                fsol.setPerNie(mat.getMatEstudiante().getEstPersona().getPerNie());
                fsol.setSotEstados(Arrays.asList(EnumEstadoSolicitudTraslado.PENDIENTE,
                        EnumEstadoSolicitudTraslado.PENDIENTE_RESOLUCION,
                        EnumEstadoSolicitudTraslado.AUTORIZADA));
                fsol.setIncluirCampos(new String[]{"sotPk", "sotVersion"});

                if (solicitudTrasladoBean.obtenerTotalPorFiltro(fsol) > 0) {
                    BusinessException ge = new BusinessException();
                    ge.addError("matEstado", Errores.ERROR_RETIRO_TRASLADO_EN_PROCESO);
                    throw ge;
                }
                
                //Validar que no tenga promoción calculada
                if (mat.getMatPromocionGrado() != null){
                    BusinessException ge = new BusinessException();
                    ge.addError("matEstado", Errores.ERROR_MATRICULA_CON_PROMOCION_CALCULADA);
                    throw ge;
                }

                mat.setMatObservaciones(entity.getMatObservaciones());
                mat.setMatMotivoRetiro(entity.getMatMotivoRetiro());
                mat.setMatFechaHasta(entity.getMatFechaHasta());
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (MatriculaValidacionNegocio.validarRetiro(mat)) {
                    CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);

                    mat.setMatEstado(EnumMatriculaEstado.CERRADO);
                    mat.setMatRetirada(Boolean.TRUE);
                    SgMatricula resultado = codDAO.guardar(mat, ConstantesOperaciones.RETIRAR_MATRICLA);
                    if (resultado != null) {
                        SgEstudiante estudiante = resultado.getMatEstudiante();
                        estudiante.setEstHabilitado(Boolean.FALSE); //Hibernate autosave

                        if (resultado.getMatMotivoRetiro().getMreDefinitivo()) {
                            SgPersona persona = estudiante.getEstPersona(); //Hibernate autosave
                            persona.setPerHabilitado(Boolean.FALSE);
                            persona.setPerEstado(EnumEstadoPersona.FALLECIDO);
                            persona.setPerFechaFallecimiento(entity.getMatFechaHasta());
                        }

                    }
                    return resultado;
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
     * Anular el retiro de la matricula
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula anularRetiro(SgMatricula entity) throws GeneralException {
        try {
            if (entity != null) {
                SgMatricula mat = this.obtenerPorId(entity.getMatPk());
                mat.setMatObservaciones(entity.getMatObservaciones());
                mat.setMatMotivoRetiro(entity.getMatMotivoRetiro());
                mat.setMatFechaHasta(entity.getMatFechaHasta());
                mat.setMatObsAnuRetiro(entity.getMatObsAnuRetiro());
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                if (MatriculaValidacionNegocio.validarAnularRetiro(mat)) {

                    CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                    SgMatricula resultado = codDAO.guardar(mat, ConstantesOperaciones.ANULAR_RETIRO_MATRICULA);

                    SgEstudiante estudiante = resultado.getMatEstudiante();
                    SgPersona persona = estudiante.getEstPersona();
                    estudiante.setEstHabilitado(Boolean.TRUE);
                    persona.setPerHabilitado(Boolean.TRUE);
                    
                    CodigueraDAO<SgPersona> codDAOPer = new CodigueraDAO<>(em, SgPersona.class);
                    SgPersona p = codDAOPer.guardar(persona, null);
                    estudiante.setEstPersona(p);
                    CodigueraDAO<SgEstudiante> codDAOEst = new CodigueraDAO<>(em, SgEstudiante.class);
                    codDAOEst.guardar(estudiante, null);


                    return resultado;
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
     * Validar matrícula academicamente
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula validarAcademicamente(SgMatricula entity) throws GeneralException {
        try {
            if (entity != null) {
                CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                CodigueraDAO<SgPersona> perDAO = new CodigueraDAO<>(em, SgPersona.class);

                SgMatricula mat = this.obtenerPorId(entity.getMatPk());
                mat.setMatValidacionAcademica(Boolean.TRUE);
                mat.setMatValidacionAcademicaFecha(LocalDateTime.now());
                mat.setMatValidacionAcademicaUsuario(Lookup.obtenerJWT().getSubject());

                SgEstudiante est = mat.getMatEstudiante();
                est.getEstPersona().setPerPrimerNombre(entity.getMatEstudiante().getEstPersona().getPerPrimerNombre());
                est.getEstPersona().setPerSegundoNombre(entity.getMatEstudiante().getEstPersona().getPerSegundoNombre());
                est.getEstPersona().setPerTercerNombre(entity.getMatEstudiante().getEstPersona().getPerTercerNombre());
                est.getEstPersona().setPerPrimerApellido(entity.getMatEstudiante().getEstPersona().getPerPrimerApellido());
                est.getEstPersona().setPerSegundoApellido(entity.getMatEstudiante().getEstPersona().getPerSegundoApellido());
                est.getEstPersona().setPerTercerApellido(entity.getMatEstudiante().getEstPersona().getPerTercerApellido());

                est.getEstPersona().setPerPartidaNacimiento(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimiento());
                est.getEstPersona().setPerPartidaNacimientoFolio(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoFolio());
                est.getEstPersona().setPerPartidaNacimientoLibro(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoLibro());
                est.getEstPersona().setPerPartidaNacimientoComplemento(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoComplemento());
                est.getEstPersona().setPerPartidaNacimientoAnio(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoAnio());
                est.getEstPersona().setPerPartidaDepartamento(entity.getMatEstudiante().getEstPersona().getPerPartidaDepartamento());
                est.getEstPersona().setPerPartidaMunicipio(entity.getMatEstudiante().getEstPersona().getPerPartidaMunicipio());

                perDAO.guardar(est.getEstPersona(), null);

                return codDAO.guardar(mat, ConstantesOperaciones.VALIDAR_ACADEMICAMENTE_MATRICULA);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return entity;
    }

    /**
     * Anular la validación de la matrícula
     *
     * @param entity SgMatricula
     * @return SgMatricula
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgMatricula anularValidacionAcademica(SgMatricula entity) throws GeneralException {
        try {
            if (entity != null) {
                CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                CodigueraDAO<SgPersona> perDAO = new CodigueraDAO<>(em, SgPersona.class);

                SgMatricula mat = this.obtenerPorId(entity.getMatPk());
                mat.setMatValidacionAcademica(Boolean.FALSE);
                mat.setMatValidacionAcademicaFecha(null);
                mat.setMatValidacionAcademicaUsuario(null);

                SgEstudiante est = mat.getMatEstudiante();
                est.getEstPersona().setPerPrimerNombre(entity.getMatEstudiante().getEstPersona().getPerPrimerNombre());
                est.getEstPersona().setPerSegundoNombre(entity.getMatEstudiante().getEstPersona().getPerSegundoNombre());
                est.getEstPersona().setPerTercerNombre(entity.getMatEstudiante().getEstPersona().getPerTercerNombre());
                est.getEstPersona().setPerPrimerApellido(entity.getMatEstudiante().getEstPersona().getPerPrimerApellido());
                est.getEstPersona().setPerSegundoApellido(entity.getMatEstudiante().getEstPersona().getPerSegundoApellido());
                est.getEstPersona().setPerTercerApellido(entity.getMatEstudiante().getEstPersona().getPerTercerApellido());

                est.getEstPersona().setPerPartidaNacimiento(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimiento());
                est.getEstPersona().setPerPartidaNacimientoFolio(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoFolio());
                est.getEstPersona().setPerPartidaNacimientoLibro(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoLibro());
                est.getEstPersona().setPerPartidaNacimientoComplemento(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoComplemento());
                est.getEstPersona().setPerPartidaNacimientoAnio(entity.getMatEstudiante().getEstPersona().getPerPartidaNacimientoAnio());
                est.getEstPersona().setPerPartidaDepartamento(entity.getMatEstudiante().getEstPersona().getPerPartidaDepartamento());
                est.getEstPersona().setPerPartidaMunicipio(entity.getMatEstudiante().getEstPersona().getPerPartidaMunicipio());

                perDAO.guardar(est.getEstPersona(), null);

                return codDAO.guardar(mat, ConstantesOperaciones.ANULAR_VALIDACION_MATRICULA);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return entity;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param matriculas
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public void cambiarSeccion(SgMatricula[][] matriculas) throws GeneralException {
        try {
            CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
            for (int i = 0; i < matriculas.length; i++) {
                if (MatriculaValidacionNegocio.validarHastaDesde(matriculas[i][0].getMatFechaIngreso(), matriculas[i][0].getMatFechaHasta())) {
                    codDAO.guardar(matriculas[i][0], null);
                    codDAO.guardar(matriculas[i][1], ConstantesOperaciones.CREAR_MATRICULA);

                    //Borrar asistencias realizadas luego de fechaHasta
                    String queryda = "DELETE FROM SgAsistencia WHERE asiPk IN "
                            + "(SELECT a.asiPk FROM SgAsistencia a "
                            + "INNER JOIN a.asiEstudiante est "
                            + "INNER JOIN a.asiControl c "
                            + "INNER JOIN c.cacSeccion s "
                            + "where est.estPk = :estPk and s.secPk = :secPk "
                            + "and c.cacFecha > :matFechaHasta) ";
                    Query qda = em.createQuery(queryda)
                            .setParameter("estPk", matriculas[i][0].getMatEstudiante().getEstPk())
                            .setParameter("secPk", matriculas[i][0].getMatSeccion().getSecPk())
                            .setParameter("matFechaHasta", matriculas[i][0].getMatFechaHasta());
                    qda.executeUpdate();

                } else {
                    BusinessException ge = new BusinessException();
                    ge.addError("matFechaIngreso", Errores.ERROR_FECHA_RETIRO_MENOR_INGRESO);
                    throw ge;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param matriculas
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public void cambioMasivoSeccion(SgMatricula[] matriculas) throws GeneralException {
        try {
            CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
            for (int i = 0; i < matriculas.length; i++) {
                codDAO.guardar(matriculas[i], ConstantesOperaciones.ACTUALIZAR_MATRICULA);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_MATRICULA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    //// MATRICULA PARCIAL

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgMatricula
     * @throws GeneralException
     */
    public List<SgMatriculaParcial> obtenerMatriculaParcialPorFiltro(FiltroMatricula filtro) throws GeneralException {
        try {
            MatriculaParcialDAO codDAO = new MatriculaParcialDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean matriculaParcialExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMatriculaParcial> codDAO = new CodigueraDAO<>(em, SgMatriculaParcial.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MATRICULA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarMatriculaParcial(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMatriculaParcial> codDAO = new CodigueraDAO<>(em, SgMatriculaParcial.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_MATRICULA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Elimina las matriculas parciales del usuario
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarMatriculasParcialesDeUsuario(String userCode) throws GeneralException {
        if (userCode != null) {
            try {
                String query = "DELETE FROM SgMatriculaParcial where matCreacionUsuario = :userCode";
                em.createQuery(query).setParameter("userCode", userCode).executeUpdate();
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMatriculaParcial
     * @throws GeneralException
     */
    public SgMatriculaParcial obtenerMatriculaParcialPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMatriculaParcial> codDAO = new CodigueraDAO<>(em, SgMatriculaParcial.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @return
     * @throws GeneralException
     * @throws java.io.FileNotFoundException
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException
     */
    public SgArchivoBitMatricula procesarArchivos(SgArchivoBitMatricula entity) throws GeneralException, FileNotFoundException, IOException, InvalidFormatException {
        Workbook myWorkBook = null;
        InputStream stream = null;
        try {
            BusinessException ge = new BusinessException();
            ge.addError("estNie", "Se encontraron los siguientes errores en el archivo:");
            String errores = "";

            ArchivoBitMatriculaValidacionNegocio.validar(entity);

            if (entity != null) {

                String path = (entity.getAbmEstado().equals(EnumEstadoImportado.PROCESAMIENTO_DIRECTO) || entity.getAbmEstado().equals(EnumEstadoImportado.VALIDACION)) ? tmpBasePath : basePath;

                if (entity.getAbmPk() != null) {
                    stream = new BufferedInputStream(new FileInputStream(path + SofisFileUtils.getPathFromPk(entity.getAbmArchivo().getAchPk())));
                } else {
                    stream = new BufferedInputStream(new FileInputStream(path + entity.getAbmArchivo().getAchTmpPath()));
                }

                myWorkBook = WorkbookFactory.create(stream);
                Sheet mySheet = myWorkBook.getSheetAt(0);

                //VALIDAR CANT FILAS
                FiltroCodiguera fcodiguera = new FiltroCodiguera();
                fcodiguera.setCodigoExacto(Constantes.CANT_FILAS_ARCHIVO_IMPORT);
                List<SgConfiguracion> conf = configuracionBean.obtenerPorFiltro(fcodiguera);
                Integer filasCantMax = 500;
                if (!conf.isEmpty()) {
                    filasCantMax = Integer.parseInt(conf.get(0).getConValor());
                }

                Set<Long> seccionesPk = new HashSet<>();
                Iterator<Row> rowIterator1 = mySheet.iterator();
                Integer cantRows = 0;
                while (rowIterator1.hasNext()) {
                    Row row = rowIterator1.next();
                    if (row.getCell(0, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(1, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null) {
                        continue;
                    }
                    if (row.getRowNum() > 0) {
                        Cell cell = row.getCell(2, Row.RETURN_BLANK_AS_NULL);
                        if (cell != null && CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                            Long secPk = Math.round(cell.getNumericCellValue());
                            seccionesPk.add(secPk);
                        }
                    }
                    cantRows++;
                }
                if (cantRows <= 1) {
                    ge.addError("filas", "El archivo está vacío.");
                    throw ge;
                }

                if (seccionesPk.isEmpty()) {
                    ge.addError("filas", "No se ingresaron secciones.");
                    throw ge;
                }

                if (cantRows > filasCantMax) {
                    ge.addError("filas", "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".");
                    throw ge;
                }
                //FIN VALIDAR CANT FILAS

                Iterator<Row> rowIterator = mySheet.iterator();

                String respuesta = "";

                FiltroSeccion fsec = new FiltroSeccion();
                fsec.setSecSedeFk(entity.getAbmSede().getSedPk());
                fsec.setSeccionesPk(new ArrayList<>(seccionesPk));
                fsec.setIncluirCampos(new String[]{"secPk", "secVersion", "secAnioLectivo.aleAnio"});
                List<SgSeccion> listSeccion = seccionBean.obtenerPorFiltro(fsec);

                if (listSeccion.isEmpty()) {
                    respuesta = "La sede no tiene ninguna de las secciones ingresadas.";
                    errores = errores + " - " + respuesta;
                    if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(entity.getAbmEstado()) && !EnumEstadoImportado.VALIDACION.equals(entity.getAbmEstado())) {
                        entity.setAbmEstado(EnumEstadoImportado.PROCESADO_ERROR);
                        entity.setAbmDescripcion(errores);
                        archivoBitMatriculaBean.guardar(entity);
                        return new SgArchivoBitMatricula();
                    } else {
                        ge.addError("estNie", respuesta);
                        throw ge;
                    }

                }

                List<Object[]> datosIngresar = new ArrayList<>();
                List<Long> estudiantesNie = new ArrayList<>();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Integer fila = row.getRowNum() + 1;
                    Cell cell;

                    if (row.getRowNum() > 0) {
                        //Se extraen los demas datos
                        Object[] datos = new Object[6];

                        //Año lectivo
                        cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            if (row.getCell(1, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null) {
                                continue;
                            }
                            respuesta = "Fila " + fila + ": Año lectivo vacío.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                            respuesta = "Fila " + fila + ": Año lectivo incorrecto.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        } else {
                            datos[0] = String.valueOf(Math.round(cell.getNumericCellValue()));
                        }

                        //NIE
                        cell = row.getCell(1, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            respuesta = "fila " + fila + ": NIE vacío.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                            respuesta = "Fila " + fila + ": NIE incorrecto.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        } else {
                            Long nieLong = Math.round(cell.getNumericCellValue());
                            datos[1] = String.valueOf(nieLong);
                            if (!StringUtils.isBlank((String) datos[1])) {
                                if (!estudiantesNie.contains(nieLong)) {
                                    estudiantesNie.add(nieLong);
                                }
                            }
                        }

                        //Id sección
                        cell = row.getCell(2, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            respuesta = "Fila " + fila + ": Id de la sección vacío.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                            respuesta = "Fila " + fila + ": Id de la sección incorrecto.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        } else {
                            datos[2] = String.valueOf(Math.round(cell.getNumericCellValue()));
                        }

                        //promovido
                        cell = row.getCell(3, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            respuesta = "Fila " + fila + ": Celda promovido incorrecta.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        if (!CellType.STRING.equals(cell.getCellTypeEnum())) {
                            respuesta = "Fila " + fila + ": Celda promovido incorrecta.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        } else {
                            String celdaPromovido = SofisStringUtils.normalizarParaBusqueda((String) cell.getStringCellValue());
                            if (celdaPromovido.equals("si") || celdaPromovido.equals("no")) {
                                datos[3] = celdaPromovido.equals("si");
                            } else {
                                respuesta = "Fila " + fila + ": Celda promovido incorrecta.";
                                errores = errores + " - " + respuesta;
                                ge.addError("estNie", respuesta);
                                continue;
                            }
                        }

                        //Validar si la seccion existe en la sede seleccionada
                        Boolean evaluacion = Boolean.FALSE;
                        if (datos[2] != null && !StringUtils.isBlank((String) datos[2])) {
                            evaluacion = listSeccion.stream().filter(
                                    o -> o.getSecPk().toString().equals(datos[2])
                            ).findFirst().isPresent();
                        }

                        if (evaluacion) {
                            datosIngresar.add(datos);
                        } else {
                            respuesta = "Fila " + fila + ": La sección no se encuentra en la sede seleccionada.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                        }
                    }
                }

                if (estudiantesNie.isEmpty()) {
                    respuesta = "Los NIE ingresados son incorrectos.";
                    errores = errores + " - " + respuesta;
                    ge.addError("estNie", respuesta);
                    throw ge;
                }

                List<SgMatricula> listaMatriculas = new ArrayList<>();

                if (StringUtils.isBlank(respuesta)) {
                    if (!(EnumEstadoImportado.VALIDACION.equals(entity.getAbmEstado()))) {
                        //No existen errores previos, se procesa el archivo
                        respuesta = "";

                        //Se obtienen todos los estudiantes
                        FiltroEstudiante fest = new FiltroEstudiante();
                        fest.setEstudiantesNie(estudiantesNie);
                        fest.setIncluirCampos(new String[]{"estPk", "estVersion", "estPersona.perNie"});
                        List<SgEstudiante> estudiantes = estudianteBean.obtenerPorFiltro(fest);

                        //Se procesa cada estudiante
                        for (Object[] datos : datosIngresar) {

                            Integer anio = Integer.valueOf((String) datos[0]);

                            SgSeccion sec = listSeccion.stream().filter(
                                    o -> o.getSecPk().toString().equals(datos[2])
                            ).findFirst().orElse(null);

                            //Validar que el año de la sección corresponda al año de la matricula
                            if (sec.getSecAnioLectivo().getAleAnio().equals(anio)) {
                                //Se busca si el estudiante esta registrado
                                SgEstudiante est = estudiantes.stream().filter(
                                        o -> o.getEstPersona().getPerNie().toString().equals(datos[1])
                                ).findFirst().orElse(null);

                                if (est != null) {
                                    //Se crea la matricula
                                    //Validar que el día corresponda al mes
                                    if (diaCorrespondeMes(entity.getAbmDiaIngreso(), entity.getAbmMesIngreso(), anio)) {

                                        if (diaCorrespondeMes(entity.getAbmDiaEgreso(), entity.getAbmMesEgreso(), anio)) {
                                            LocalDate fechaIngreso = LocalDate.of(anio, entity.getAbmMesIngreso(), entity.getAbmDiaIngreso());
                                            LocalDate fechaHasta = LocalDate.of(anio, entity.getAbmMesEgreso(), entity.getAbmDiaEgreso());

                                            SgMatricula mat = new SgMatricula();
                                            mat.setMatEstudiante(est);
                                            mat.setMatImportado(Boolean.TRUE);
                                            mat.setMatEstado(EnumMatriculaEstado.CERRADO);
                                            mat.setMatSeccion(sec);
                                            mat.setMatFechaIngreso(fechaIngreso);
                                            mat.setMatFechaHasta(fechaHasta);
                                            mat.setMatPromocionGrado((Boolean) datos[3] ? EnumPromocionGradoMatricula.PROMOVIDO : EnumPromocionGradoMatricula.NO_PROMOVIDO);

                                            if (!MatriculaValidacionNegocio.validarHastaDesde(mat.getMatFechaIngreso(), mat.getMatFechaHasta())) {
                                                respuesta = "Para el NIE " + datos[1] + " la fecha hasta debe ser mayor o igual a la fecha de ingreso.";
                                                errores = errores + " - " + respuesta;
                                                ge.addError("matFechaIngreso", respuesta);
                                            } else {
                                                listaMatriculas.add(mat);

//                                                //Validar la fecha de ingreso no se intercepte con otro registro
//                                                FiltroMatricula fmat = new FiltroMatricula();
//                                                fmat.setMatEstudiantePk(mat.getMatEstudiante().getEstPk());
//                                                fmat.setMatFechaDesdeSup(mat.getMatFechaIngreso());
//                                                fmat.setMatFechaHastaSup(mat.getMatFechaHasta());
//                                                fmat.setSecurityOperation(null);
//                                                Long cant = this.obtenerTotalPorFiltro(fmat);
//
//                                                if (cant <= 0) {
//                                                    
//                                                } else {
//                                                    respuesta = "para el NIE " + datos[1] + " la fecha de ingreso se superpone con otro registro ya existente.";
//                                                    errores = errores + " - " + respuesta;
//                                                    ge.addError("matFechaIngreso", respuesta);
//                                                }
                                            }
                                        } else {
                                            respuesta = "Para el NIE " + datos[1] + " el día de egreso no corresponde al mes.";
                                            errores = errores + " - " + respuesta;
                                            ge.addError("matFechaHasta", respuesta);
                                        }
                                    } else {
                                        respuesta = "Para el NIE " + datos[1] + " el día de ingreso no corresponde al mes.";
                                        errores = errores + " - " + respuesta;
                                        ge.addError("matFechaIngreso", respuesta);
                                    }
                                } else {
                                    respuesta = "El NIE " + datos[1] + " no existe.";
                                    errores = errores + " - " + respuesta;
                                    ge.addError("estNie", respuesta);
                                }
                            } else {
                                respuesta = "El año de la matrícula del estudiante " + datos[1] + " no corresponde al año de la sección " + datos[2] + ".";
                                errores = errores + " - " + respuesta;
                                ge.addError("estNie", respuesta);
                            }
                        }

                        if (StringUtils.isBlank(respuesta)) {

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                            //No hubo ningun erro al procesar el archivo, se guardan todas las matriculas
                            CodigueraDAO<SgMatricula> codDAO = new CodigueraDAO<>(em, SgMatricula.class);
                            for (SgMatricula mat : listaMatriculas) {

                                //Validar matricula no se intercepte con otro registro
                                FiltroMatricula fmat = new FiltroMatricula();
                                fmat.setMatEstudiantePk(mat.getMatEstudiante().getEstPk());
                                fmat.setMatFechaDesdeSup(mat.getMatFechaIngreso());
                                fmat.setMatFechaHastaSup(mat.getMatFechaHasta());
                                fmat.setSecurityOperation(null);
                                Long cant = this.obtenerTotalPorFiltro(fmat);

                                if (cant > 0) {
                                    respuesta = "La matrícula del estudiante con NIE " + mat.getMatEstudiante().getEstPersona().getPerNie() + ", fecha ingreso " + formatter.format(mat.getMatFechaIngreso()) + ", fecha egreso " + formatter.format(mat.getMatFechaHasta()) + "  se superpone con otro registro existente.";
                                    errores = errores + " - " + respuesta;
                                    ge.addError("estNie", respuesta);
                                    throw ge;
                                }

                                SgMatricula guardada = codDAO.guardar(mat, null);

                                escolaridadEstudianteBean.generarEscolaridad(guardada.getMatPk());
                            }

                            entity.setAbmEstado(EnumEstadoImportado.PROCESADO_EXITO);
                            archivoBitMatriculaBean.guardar(entity);

                        } else {
                            //Se proceso con errores
                            //Mostrar los errores en pantalla
                            if (ge.getErrores().size() > 1) {
                                throw ge;
                            }
                            return null;

                        }

                        return entity;
                    } else {
                        //Solo se guarda la entidad para ser procesada luego
                        return archivoBitMatriculaBean.guardar(entity);
                    }
                } else {
                    if (ge.getErrores().size() > 1) {
                        throw ge;
                    }
                    return null;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ex) {
                }
            }
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                }
            }
        }
        return new SgArchivoBitMatricula();
    }

    public boolean diaCorrespondeMes(int diaComparar, int mes, int anio) {
        try {
            Integer dias = YearMonth.of(anio, mes).lengthOfMonth();

            return dias > 0 && diaComparar <= dias;

        } catch (Exception ex) {
            return false;
        }
    }

    public List<SgConsultaMatriculasSede> obtenerConfirmacionMatriculaEnSede(FiltroMatriculasEnSede filtro) throws GeneralException {
        try {

            Session session = em.unwrap(Session.class);

            SQLQuery q = session.createSQLQuery(" select niv.niv_nombre as nivelNombre, cic.cic_nombre as cicloNombre,"
                    + " modedu.mod_nombre as modalidadEduNombre, modaten.mat_nombre as modalidadAtenNombre, gra.gra_nombre as gradoNombre,"
                    + "	   sum(case when per.per_sexo_fk  = 1 then 1 end) as cantMatMasculino,"
                    + "    sum(case when per.per_sexo_fk  = 2 then 1 end) as cantMatFemenino "
                    + "	   from centros_educativos.sg_matriculas mat"
                    + "	   INNER JOIN centros_educativos.sg_estudiantes est ON (mat.mat_estudiante_fk = est.est_pk)"
                    + "	   INNER JOIN centros_educativos.sg_personas per ON (per.per_pk = est.est_persona)"
                    + "	   INNER JOIN centros_educativos.sg_secciones sec ON (mat.mat_seccion_fk = sec.sec_pk)"
                    + "	   INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sdu.sdu_pk = sec.sec_servicio_educativo_fk)"
                    + "	   INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                    + "	   INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                    + "	   INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)"
                    + "	   INNER JOIN catalogo.sg_modalidades_atencion modaten ON (relmodaten.rea_modalidad_atencion_fk = modaten.mat_pk)"
                    + "	   INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)	"
                    + "	   INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)"
                    + "    INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk)"
                    + "	   where mat.mat_retirada = false and mat.mat_anulada = false and ale.ale_anio = :anio and sdu.sdu_sede_fk = :sedPk"
                    + "	   group by niv.niv_pk, niv.niv_nombre, cic.cic_pk, cic.cic_nombre, modedu.mod_pk, modedu.mod_nombre, modaten.mat_pk, modaten.mat_nombre, gra.gra_pk, gra.gra_nombre"
                    + "	   order by niv.niv_orden, cic.cic_orden, modedu.mod_orden, gra.gra_orden");

            q.addScalar("nivelNombre", new StringType());
            q.addScalar("cicloNombre", new StringType());
            q.addScalar("gradoNombre", new StringType());
            q.addScalar("modalidadEduNombre", new StringType());
            q.addScalar("modalidadAtenNombre", new StringType());
            q.addScalar("cantMatMasculino", new LongType());
            q.addScalar("cantMatFemenino", new LongType());

            if (filtro.getAnio() != null) {
                q.setParameter("anio", filtro.getAnio());
            }
            if (filtro.getSedPk() != null) {
                q.setParameter("sedPk", filtro.getSedPk());
            }

            q.setResultTransformer(Transformers.aliasToBean(SgConsultaMatriculasSede.class));
            return q.list();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Consulta utilizada por SIGO
     *
     * @param FiltroMatriculasEnSeccion
     * @return List<SgConsultaMatriculasSeccionResponse>
     * @throws GeneralException
     */
    public List<SgConsultaMatriculasSeccionResponse> obtenerMatriculasEnSeccion(FiltroMatriculasEnSeccion filtro) throws GeneralException {
        try {

            Session session = em.unwrap(Session.class);

            if (filtro.getImplementadoraCodigo() == null && filtro.getImplementadoraPk() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DEBE_FILTRAR_POR_CODIGO_O_PK_IMPLEMENTADORA);
                throw be;
            }

            String where = "";

            if (filtro.getImplementadoraPk() != null) {
                where += " and asoc.aso_pk = :asoPk ";
            }

            if (!StringUtils.isBlank(filtro.getImplementadoraCodigo())) {
                where += " and asoc.aso_codigo = :asoCodigo";
            }

            if (filtro.getMatriculasFecha() != null) {
                where += " and mat.mat_fecha_ingreso <= :fechaMat and (mat.mat_fecha_hasta is null or mat.mat_fecha_hasta >= :fechaMat)";
            }

            SQLQuery q = session.createSQLQuery(" select sede.sed_pk as sedePk, sede.sed_codigo as sedeCodigo,"
                    + "     sec.sec_pk as seccionPk, sec.sec_codigo as seccionCodigo, sec.sec_regimen as seccionRegimen,"
                    + "     sum(case when per.per_sexo_fk  = 1 then 1 end) as cantMatMasculino,"
                    + "     sum(case when per.per_sexo_fk  = 2 then 1 end) as cantMatFemenino "
                    + "     from centros_educativos.sg_matriculas mat"
                    + "     INNER JOIN centros_educativos.sg_estudiantes est ON (mat.mat_estudiante_fk = est.est_pk)"
                    + "     INNER JOIN centros_educativos.sg_personas per ON (per.per_pk = est.est_persona)"
                    + "     INNER JOIN centros_educativos.sg_secciones sec ON (mat.mat_seccion_fk = sec.sec_pk)"
                    + "     INNER JOIN catalogo.sg_asociaciones asoc ON (asoc.aso_pk = sec.sec_asociacion_fk)"
                    + "     INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sdu.sdu_pk = sec.sec_servicio_educativo_fk)"
                    + "     INNER JOIN centros_educativos.sg_sedes sede ON (sdu.sdu_sede_fk = sede.sed_pk)"
                    + "     where mat.mat_retirada = false and mat.mat_anulada = false " + where
                    + "     group by sede.sed_pk, sede.sed_codigo, sec.sec_pk, sec.sec_codigo, sec.sec_regimen"
                    + "     order by sede.sed_pk, sec.sec_pk");

            q.addScalar("sedeCodigo", new StringType());
            q.addScalar("sedePk", new LongType());
            q.addScalar("seccionCodigo", new StringType());
            q.addScalar("seccionPk", new LongType());
            q.addScalar("seccionRegimen", new StringType());
            q.addScalar("cantMatMasculino", new LongType());
            q.addScalar("cantMatFemenino", new LongType());

            if (filtro.getImplementadoraPk() != null) {
                q.setParameter("asoPk", filtro.getImplementadoraPk());
            }

            if (!StringUtils.isBlank(filtro.getImplementadoraCodigo())) {
                q.setParameter("asoCodigo", filtro.getImplementadoraCodigo());
            }

            if (filtro.getMatriculasFecha() != null) {
                q.setParameter("fechaMat", filtro.getMatriculasFecha());
            }

            q.setResultTransformer(Transformers.aliasToBean(SgConsultaMatriculasSeccionResponse.class));
            return q.list();

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Consulta utilizada por SAT
     *
     * @param FiltroMatriculasEnSeccion2
     * @return List<SgConsultaMatriculasSeccionResponse2>
     * @throws GeneralException
     */
    public List<SgConsultaMatriculasSeccionResponseSAT> obtenerMatriculasEnSeccionSAT(FiltroMatriculasEnSeccionSAT filtro) throws GeneralException {
        try {

            if (StringUtils.isBlank(filtro.getCodigoCentro())) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }
            if (filtro.getFecha() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_FECHA_VACIO);
                throw be;
            }

            String where = "";

            if (BooleanUtils.isTrue(filtro.getSoloRepetidores())) {
                where += " and mat.mat_repetidor = true ";
            }

            if (BooleanUtils.isTrue(filtro.getSoloSobreedad())) {
                where += " and gra.gra_edad_maxima is not null and per.per_fecha_nacimiento <= (now() - (interval '1 year' * (gra.gra_edad_maxima)))";
            }

            where += " and mat.mat_fecha_ingreso <= :fechaMat and (mat.mat_fecha_hasta is null or mat.mat_fecha_hasta >= :fechaMat)";

            Session session = em.unwrap(Session.class);

            SQLQuery q = session.createSQLQuery("select sede.sed_pk as sedePk, sede.sed_codigo as sedeCodigo,"
                    + "     sec.sec_pk as seccionPk, sec.sec_codigo as seccionCodigo, ale.ale_anio as anio,"
                    + "     sum(case when per.per_sexo_fk  = 1 then 1 end) as cantMatMasculino,"
                    + "     sum(case when per.per_sexo_fk  = 2 then 1 end) as cantMatFemenino "
                    + "     from centros_educativos.sg_matriculas mat"
                    + "     INNER JOIN centros_educativos.sg_estudiantes est ON (mat.mat_estudiante_fk = est.est_pk)"
                    + "     INNER JOIN centros_educativos.sg_personas per ON (per.per_pk = est.est_persona)"
                    + "     INNER JOIN centros_educativos.sg_secciones sec ON (mat.mat_seccion_fk = sec.sec_pk)"
                    + "     INNER JOIN centros_educativos.sg_anio_lectivo ale ON (ale.ale_pk = sec.sec_anio_lectivo_fk)"
                    + "     INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sdu.sdu_pk = sec.sec_servicio_educativo_fk)"
                    + "     INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                    + "     INNER JOIN centros_educativos.sg_sedes sede ON (sdu.sdu_sede_fk = sede.sed_pk)"
                    + "     where mat.mat_retirada = false and mat.mat_anulada = false and sede.sed_codigo = :sedCodigo " + where
                    + "     group by sede.sed_pk, sede.sed_codigo, sec.sec_pk, sec.sec_codigo, ale.ale_anio"
                    + "     order by sede.sed_pk, sec.sec_pk");

            q.addScalar("sedeCodigo", new StringType());
            q.addScalar("sedePk", new LongType());
            q.addScalar("seccionCodigo", new StringType());
            q.addScalar("seccionPk", new LongType());
            q.addScalar("anio", new IntegerType());
            q.addScalar("cantMatMasculino", new LongType());
            q.addScalar("cantMatFemenino", new LongType());

            q.setParameter("sedCodigo", filtro.getCodigoCentro());
            q.setParameter("fechaMat", filtro.getFecha());

            q.setResultTransformer(Transformers.aliasToBean(SgConsultaMatriculasSeccionResponseSAT.class));
            return q.list();

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public SgMatricula reaperturaMatricula(SgMatricula entity) {
        try {
            //Para re abrir la matrícula debe cumplir:
            //-Tiene que estar en estado cerrado
            //-El cierre debe ser por Cierre de seccion (matCerradoPorCierreAnio=true)
            //-Estudiante no debe tener título o solicitud de título asociada
            //-Debe ser la última matricula del estudiante
            //-No debe tener matricula abierta

            BusinessException be = new BusinessException();

            FiltroMatricula fm = new FiltroMatricula();
            fm.setMatEstado(EnumMatriculaEstado.ABIERTO);
            fm.setMatEstudiantePk(entity.getMatEstudiante().getEstPk());
            fm.setIncluirCampos(new String[]{"matPk"});
            fm.setSecurityOperation(null);
            fm.setMaxResults(1L);
            List<SgMatricula> matriculas = this.obtenerPorFiltro(fm);

            if (!matriculas.isEmpty()) {
                be.addError(Errores.ERROR_ESTUDIANTE_TIENE_MATRICULA_ABIERTA);
                throw be;
            }

            fm = new FiltroMatricula();
            fm.setMatEstudiantePk(entity.getMatEstudiante().getEstPk());
            fm.setOrderBy(new String[]{"matFechaIngreso"});
            fm.setAscending(new boolean[]{false});
            fm.setMaxResults(1L);
            fm.setSecurityOperation(null);
           
            matriculas = this.obtenerPorFiltro(fm);
            if (matriculas.isEmpty()) {
                be.addError(Errores.ERROR_ESTUDIANTE_NO_TIENE_MATRICULA);
                throw be;
            }
            SgMatricula matricula = matriculas.get(0);
            if (matricula.getMatPk().compareTo(entity.getMatPk()) != 0) {
                be.addError(Errores.ERROR_MATRICULA_SELECCIONADA_NO_ES_ULTIMA);
                throw be;
            }

            if (BooleanUtils.isNotTrue(matricula.getMatCerradoPorCierreAnio())) {
                be.addError(Errores.ERROR_MATRICULA_CERRADA_CIERRE_ANIO);
                throw be;
            }

            if (!matricula.getMatEstado().equals(EnumMatriculaEstado.CERRADO)) {
                be.addError(Errores.ERROR_MATRICULA_ESTADO_CERRADO);
                throw be;
            }

            FiltroEstudianteImpresion fei = new FiltroEstudianteImpresion();
            fei.setEimEstudiantePk(matricula.getMatEstudiante().getEstPk());
            fei.setEimSeccion(matricula.getMatSeccion().getSecPk());
            fei.setEimNoAnulada(Boolean.TRUE);
            List<EnumEstadoSolicitudImpresion> estado = new ArrayList();
            estado.add(EnumEstadoSolicitudImpresion.ENVIADA);
            estado.add(EnumEstadoSolicitudImpresion.CONFIRMADA);
            estado.add(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
            // estado.add(EnumEstadoSolicitudImpresion.IMPRESA);
            // estado.add(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
            fei.setEimEstadosSolicitud(estado);
            fei.setIncluirCampos(new String[]{"eimPk"});
            fei.setMaxResults(1L);

            List<SgEstudianteImpresion> estImp = estudianteImpresionBean.obtenerPorFiltro(fei);
            if (!estImp.isEmpty()) {
                be.addError(Errores.ERROR_ESTUDIANTE_TIENE_TITULO_O_SOLICITUD);
                throw be;
            } else {
                FiltroTitulo ft = new FiltroTitulo();
                ft.setTitEstudiante(matricula.getMatEstudiante().getEstPk());
                ft.setTitNoAnulada(Boolean.TRUE);
                ft.setTitSeccionFk(matricula.getMatSeccion().getSecPk());
                ft.setIncluirCampos(new String[]{"titPk"});
                ft.setMaxResults(1L);
                List<SgTitulo> tit = tituloBean.obtenerPorFiltro(ft);
                if (!tit.isEmpty()) {
                    be.addError(Errores.ERROR_ESTUDIANTE_TIENE_TITULO_O_SOLICITUD);
                    throw be;
                }
            }

            matricula.setMatFechaHasta(null);
            matricula.setMatEstado(EnumMatriculaEstado.ABIERTO);
            matricula.setMatPromocionGrado(null);
            matricula.setMatCerradoPorCierreAnio(null);

            seccionBean.abrirSeccionSiCerrada(matricula.getMatSeccion().getSecPk());

            FiltroEscolaridadEstudiante fee = new FiltroEscolaridadEstudiante();
            fee.setEstudiantePk(matricula.getMatEstudiante().getEstPk());
            fee.setMatriculaPk(matricula.getMatPk());
            fee.setIncluirCampos(new String[]{"escPk"});
            List<SgEscolaridadEstudiante> escolaridades = escolaridadEstudianteBean.obtenerPorFiltro(fee);
            if (!escolaridades.isEmpty()) {
                for (SgEscolaridadEstudiante esc : escolaridades) {
                    escolaridadEstudianteBean.eliminar(esc.getEscPk());
                }
            }

            matricula = this.actualizar(matricula);

            return matricula;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    //abarca solo modalidades flexibles
    public List<SgDatoDashboard> matriculasActivasPorSexo(FiltroMatricula filtro) {

        String query = "select per_sexo_fk, sum(cantidad) from centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac ";

        if (filtro.getMatDepartamentoFk() != null || filtro.getMatSexoFk() != null) {
            query += "where ";
            if (filtro.getMatDepartamentoFk() != null) {
                query += "dir_departamento=:dep  ";

                if (filtro.getMatSexoFk() != null) {
                    query += "and per_sexo_fk=:sex ";
                }
            } else {
                if (filtro.getMatSexoFk() != null) {
                    query += "per_sexo_fk=:sex ";
                }
            }
        }

        query += "group by per_sexo_fk";
        Query a = em.createNativeQuery(query);

        if (filtro.getMatDepartamentoFk() != null) {
            a.setParameter("dep", filtro.getMatDepartamentoFk());
        }
        if (filtro.getMatSexoFk() != null) {
            a.setParameter("sex", filtro.getMatSexoFk());
        }

        List<Object[]> resqsolicitudesimp = a.getResultList();

        List<SgDatoDashboard> datoList = new ArrayList();
        for (Object[] o : resqsolicitudesimp) {
            if (o[0] != null && o[1] != null) {
                SgDatoDashboard dato = new SgDatoDashboard();
                dato.setElementoPk(((BigInteger) o[0]).longValue());
                dato.setCantidad(((BigDecimal) o[1]).longValue());
                datoList.add(dato);
            }
        }
        return datoList;
    }

    //abarca solo modalidades flexibles
    public List<SgDatoDashboard> matriculasActivasPorDepartamento(FiltroMatricula filtro) {

        String query = "select dir_departamento, sum(cantidad) from centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac ";

        if (filtro.getMatDepartamentoFk() != null || filtro.getMatSexoFk() != null) {
            query += "where ";
            if (filtro.getMatDepartamentoFk() != null) {
                query += "dir_departamento=:dep  ";

                if (filtro.getMatSexoFk() != null) {
                    query += "and per_sexo_fk=:sex ";
                }
            } else {
                if (filtro.getMatSexoFk() != null) {
                    query += "per_sexo_fk=:sex ";
                }
            }
        }

        query += "group by dir_departamento";
        Query a = em.createNativeQuery(query);

        if (filtro.getMatDepartamentoFk() != null) {
            a.setParameter("dep", filtro.getMatDepartamentoFk());
        }
        if (filtro.getMatSexoFk() != null) {
            a.setParameter("sex", filtro.getMatSexoFk());
        }

        List<Object[]> resqsolicitudesimp = a.getResultList();

        List<SgDatoDashboard> datoList = new ArrayList();
        for (Object[] o : resqsolicitudesimp) {
            if (o[0] != null && o[1] != null) {
                SgDatoDashboard dato = new SgDatoDashboard();
                dato.setElementoPk(((BigInteger) o[0]).longValue());
                dato.setCantidad(((BigDecimal) o[1]).longValue());
                datoList.add(dato);
            }
        }
        return datoList;
    }

    //abarca solo modalidades flexibles
    public List<SgDatoDashboard> matriculasActivasPorMunicipio(FiltroMatricula filtro) {

        String query = "select dir_municipio, sum(cantidad) from centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac ";

        if (filtro.getMatMunicipioFk() != null || filtro.getMatSexoFk() != null) {
            query += "where ";
            if (filtro.getMatMunicipioFk() != null) {
                query += "dir_municipio=:dep  ";

                if (filtro.getMatSexoFk() != null) {
                    query += "and per_sexo_fk=:sex ";
                }
            } else {
                if (filtro.getMatSexoFk() != null) {
                    query += "per_sexo_fk=:sex ";
                }
            }
        }

        query += "group by dir_municipio";
        Query a = em.createNativeQuery(query);

        if (filtro.getMatMunicipioFk() != null) {
            a.setParameter("dep", filtro.getMatMunicipioFk());
        }
        if (filtro.getMatSexoFk() != null) {
            a.setParameter("sex", filtro.getMatSexoFk());
        }

        List<Object[]> resqsolicitudesimp = a.getResultList();

        List<SgDatoDashboard> datoList = new ArrayList();
        for (Object[] o : resqsolicitudesimp) {
            if (o[0] != null && o[1] != null) {
                SgDatoDashboard dato = new SgDatoDashboard();
                dato.setElementoPk(((BigInteger) o[0]).longValue());
                dato.setCantidad(((BigDecimal) o[1]).longValue());
                datoList.add(dato);
            }
        }
        return datoList;
    }

    //abarca solo modalidades flexibles
    public List<SgDatoDashboard> matriculasActivasPorEstadoCivil(FiltroMatricula filtro) {

        String query = "select per_estado_civil_fk, sum(cantidad) from centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac ";

        if (filtro.getMatDepartamentoFk() != null || filtro.getMatSexoFk() != null) {
            query += "where ";
            if (filtro.getMatDepartamentoFk() != null) {
                query += "dir_departamento=:dep  ";

                if (filtro.getMatSexoFk() != null) {
                    query += "and per_sexo_fk=:sex ";
                }
            } else {
                if (filtro.getMatSexoFk() != null) {
                    query += "per_sexo_fk=:sex ";
                }
            }
        }

        query += "group by per_estado_civil_fk";
        Query a = em.createNativeQuery(query);

        if (filtro.getMatDepartamentoFk() != null) {
            a.setParameter("dep", filtro.getMatDepartamentoFk());
        }
        if (filtro.getMatSexoFk() != null) {
            a.setParameter("sex", filtro.getMatSexoFk());
        }

        List<Object[]> resqsolicitudesimp = a.getResultList();

        List<SgDatoDashboard> datoList = new ArrayList();
        for (Object[] o : resqsolicitudesimp) {
            if (o[0] != null && o[1] != null) {
                SgDatoDashboard dato = new SgDatoDashboard();
                dato.setElementoPk(((BigInteger) o[0]).longValue());
                dato.setCantidad(((BigDecimal) o[1]).longValue());
                datoList.add(dato);
            }
        }
        return datoList;
    }

    //abarca solo modalidades flexibles
    public List<SgDatoDashboard> matriculasActivasPorSubModalidad(FiltroMatricula filtro) {

        String query = "select rea_sub_modalidad_atencion_fk, sum(cantidad) from centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac where rea_sub_modalidad_atencion_fk is not null ";

        if (filtro.getMatDepartamentoFk() != null) {
            query += "and dir_departamento=:dep  ";

        }

        if (filtro.getMatSexoFk() != null) {
            query += "and per_sexo_fk=:sex ";
        }

        query += "group by rea_sub_modalidad_atencion_fk";
        Query a = em.createNativeQuery(query);

        if (filtro.getMatDepartamentoFk() != null) {
            a.setParameter("dep", filtro.getMatDepartamentoFk());
        }
        if (filtro.getMatSexoFk() != null) {
            a.setParameter("sex", filtro.getMatSexoFk());
        }

        List<Object[]> resqsolicitudesimp = a.getResultList();

        List<SgDatoDashboard> datoList = new ArrayList();
        for (Object[] o : resqsolicitudesimp) {
            if (o[0] != null && o[1] != null) {
                SgDatoDashboard dato = new SgDatoDashboard();
                dato.setElementoPk(((BigInteger) o[0]).longValue());
                dato.setCantidad(((BigDecimal) o[1]).longValue());
                datoList.add(dato);
            }
        }
        return datoList;
    }

    //abarca solo modalidades flexibles
    public List<SgRangoEdad> matriculasActivasPorRangoEdad(FiltroMatricula filtro) {

        if (filtro.getMatRangosEdad() != null && !filtro.getMatRangosEdad().isEmpty()) {
            for (SgRangoEdad re : filtro.getMatRangosEdad()) {

                String query = "select sum(cantidad) from centros_educativos.sg_matriculas_por_dep_sexo_estado_civil_sub_mod_fecha_nac ";

                if (re.getEdadDesde() != null) {
                    query += "where per_fecha_nacimiento <= :fechaHasta ";
                } else {
                    continue;
                }

                if (re.getEdadHasta() != null) {
                    query += "and per_fecha_nacimiento >= :fechaDesde ";
                }

                if (filtro.getMatDepartamentoFk() != null) {
                    query += "and dir_departamento=:dep  ";

                }

                if (filtro.getMatSexoFk() != null) {
                    query += "and per_sexo_fk=:sex ";
                }

                Query a = em.createNativeQuery(query);

                if (re.getEdadDesde() != null) {
                    LocalDate fechaHasta = LocalDate.now().minusYears(re.getEdadDesde());
                    a.setParameter("fechaHasta", fechaHasta);
                } else {
                    continue;
                }

                if (re.getEdadHasta() != null) {
                    LocalDate fechaDesde = LocalDate.now().minusYears(re.getEdadHasta());
                    a.setParameter("fechaDesde", fechaDesde);
                }
                if (filtro.getMatDepartamentoFk() != null) {
                    a.setParameter("dep", filtro.getMatDepartamentoFk());
                }
                if (filtro.getMatSexoFk() != null) {
                    a.setParameter("sex", filtro.getMatSexoFk());
                }
                Long resultado = 0L;
                if (a.getSingleResult() != null) {
                    resultado = ((BigDecimal) a.getSingleResult()).longValue();
                }
                re.setCantidad(resultado);

            }
            return filtro.getMatRangosEdad();
        }
        return null;
    }

}
