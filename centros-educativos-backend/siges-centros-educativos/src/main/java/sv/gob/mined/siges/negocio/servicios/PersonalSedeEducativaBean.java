/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgNominaDocente;
import sv.gob.mined.siges.dto.SgNominaGradoMaterias;
import sv.gob.mined.siges.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAllegado;
import sv.gob.mined.siges.filtros.FiltroComponenteDocente;
import sv.gob.mined.siges.filtros.FiltroDatoContratacion;
import sv.gob.mined.siges.filtros.FiltroHorarioEscolar;
import sv.gob.mined.siges.filtros.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.filtros.FiltroRelPersonalEspecialidad;
import sv.gob.mined.siges.filtros.FiltroSolicitudCursoDocente;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.PersonalSedeEducativoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PersonalSedeEducativaDAO;
import sv.gob.mined.siges.persistencia.daos.PersonalSedeEducativaLiteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.entidades.SgCapacitacion;
import sv.gob.mined.siges.persistencia.entidades.SgComponenteDocente;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado;
import sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleadoLite;
import sv.gob.mined.siges.persistencia.entidades.SgEstudioRealizado;
import sv.gob.mined.siges.persistencia.entidades.SgEstudioSuperior;
import sv.gob.mined.siges.persistencia.entidades.SgExperienciaLaboral;
import sv.gob.mined.siges.persistencia.entidades.SgHorarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgIdiomaRealizado;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaLite;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativaLite;
import sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudCursoDocente;
import sv.gob.mined.siges.persistencia.entidades.SgTelefono;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.persistencia.utilidades.InitializeObjectUtils;

@Stateless
@Traced
public class PersonalSedeEducativaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private DatoContratacionBean datoContratacionBean;

    @Inject
    private ComponenteDocenteBean componenteDocenteBean;

    @Inject
    private HorarioEscolarBean horarioEscolarBean;

    @Inject
    private RelPersonalEspecialidadBean relPersonalEspecialidadBean;

    @Inject
    private SolicitudCursoDocenteBean solicitudCursoDocenteBean;

    @Inject
    private PersonaBean personaBean;

    @Inject
    private AllegadoBean allegadoBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    @ConfigProperty(name = "email-valid-pattern")
    private String emailPattern;

    @Inject
    private UsuarioBean usuarioBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPersonalSedeEducativa
     * @throws GeneralException
     */
    public SgPersonalSedeEducativa obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPersonalSedeEducativa> codDAO = new CodigueraDAO<>(em, SgPersonalSedeEducativa.class);
                SgPersonalSedeEducativa ret = null;
                if (BooleanUtils.isTrue(dataSecurity)) {
                    ret = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_PERSONAL_SEDE_EDUCATIVA);
                } else {
                    ret = codDAO.obtenerPorId(id, null);
                }
                if (ret != null) {
                    InitializeObjectUtils.inicializarPersona(ret.getPsePersona());
                }
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
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPersonalSedeEducativa> codDAO = new CodigueraDAO<>(em, SgPersonalSedeEducativa.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_PERSONAL_SEDE_EDUCATIVA);
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
    public Long obtenerTotalPorFiltro(FiltroPersonalSedeEducativa filtro) throws GeneralException {
        try {
            PersonalSedeEducativaDAO dao = new PersonalSedeEducativaDAO(em, seguridadBean);
            String securityOperation = ConstantesOperaciones.BUSCAR_PERSONAL_SEDE_EDUCATIVA;
            if (filtro.getPerPk() != null) {
                securityOperation = null;
            }
            return dao.obtenerTotalPorFiltro(filtro, securityOperation);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersonalSedeEducativa
     * @throws GeneralException
     */
    public List<SgPersonalSedeEducativaLite> obtenerPorFiltroLite(FiltroPersonalSedeEducativa filtro) throws GeneralException {
        try {
            PersonalSedeEducativaLiteDAO dao = new PersonalSedeEducativaLiteDAO(em, seguridadBean);
            String securityOperation = ConstantesOperaciones.BUSCAR_PERSONAL_SEDE_EDUCATIVA;
            if (filtro.getPerPk() != null) {
                securityOperation = null;
            }
            List<SgPersonalSedeEducativaLite> personal = dao.obtenerPorFiltro(filtro, securityOperation);
            return personal;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersonalSedeEducativa
     * @throws GeneralException
     */
    public List<SgPersonalSedeEducativa> obtenerPorFiltro(FiltroPersonalSedeEducativa filtro) throws GeneralException {
        try {
            PersonalSedeEducativaDAO dao = new PersonalSedeEducativaDAO(em, seguridadBean);
            String securityOperation = ConstantesOperaciones.BUSCAR_PERSONAL_SEDE_EDUCATIVA;
            if (filtro.getPerPk() != null) {
                securityOperation = null;
            }
            List<SgPersonalSedeEducativa> personal = dao.obtenerPorFiltro(filtro, securityOperation);

            if (!personal.isEmpty() && BooleanUtils.isTrue(filtro.getIncluirSedes())) {
                FiltroDatoContratacion fDato = new FiltroDatoContratacion();
                fDato.setOrderBy(new String[]{"dcoCentroEducativo.sedNombre"});
                fDato.setAscending(new boolean[]{true});
                fDato.setIncluirCampos(new String[]{"dcoCentroEducativo.sedPk",
                    "dcoCentroEducativo.sedCodigo",
                    "dcoCentroEducativo.sedNombre",
                    "dcoCentroEducativo.sedTipo",
                    "dcoDatoEmpleado.demPersonalSede.psePk",
                    "dcoCentroEducativo.sedDireccion.dirDepartamento.depNombre",
                    "dcoCentroEducativo.sedDireccion.dirMunicipio.munNombre",
                    "dcoDesde", "dcoHasta", "dcoCargo.carNombre", "dcoModeloContrato"});
                fDato.setDcoPersonalesPk(personal.stream().map(p -> p.getPsePk()).collect(Collectors.toList()));

                List<SgDatoContratacion> datos = datoContratacionBean.obtenerPorFiltro(fDato);
                HashMap<Long, List<SgDatoContratacion>> datosContratAgrupados = new HashMap<>();
                for (SgDatoContratacion dc : datos) {
                    if (dc.getDcoDatoEmpleado() != null && dc.getDcoDatoEmpleado().getDemPersonalSede() != null && dc.getDcoDatoEmpleado().getDemPersonalSede().getPsePk() != null) {
                        datosContratAgrupados.computeIfAbsent(dc.getDcoDatoEmpleado().getDemPersonalSede().getPsePk(), s -> new ArrayList<>()).add(dc);
                    }
                }
                for (SgPersonalSedeEducativa per : personal) {
                    per.setPseDatoEmpleado(new SgDatoEmpleado());
                    per.getPseDatoEmpleado().setDemDatoContratacion(datosContratAgrupados.get(per.getPsePk()) != null ? datosContratAgrupados.get(per.getPsePk()) : new ArrayList<>());
                }
            }
            return personal;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public Set<TipoPersonalSedeEducativa> obtenerTiposPersonal(SgPersonalSedeEducativa pers) {
        if (pers.getPsePk() == null) {
            HashSet ret = new HashSet<>();
            if (pers.getPseCrearConDatoContratacion() != null) {
                ret.add(pers.getPseCrearConDatoContratacion().getDcoTipo());
            }
            return ret;
        } else {
            List<TipoPersonalSedeEducativa> ret = em.createQuery("Select dat.dcoTipo from SgPersonalSedeEducativa pe INNER JOIN SgDatoEmpleado de ON (pe.pseDatoEmpleado = de.demPk) "
                    + " INNER JOIN SgDatoContratacion dat ON (dat.dcoDatoEmpleado = de.demPk) where pe.psePk = :psePk")
                    .setParameter("psePk", pers.getPsePk())
                    .getResultList();
            return new HashSet<>(ret);
        }
    }

//    public Boolean personalEsTipo(Long personalPk, TipoPersonalSedeEducativa tipo) {
//        try {
//            
//            if (personalPk == null || tipo == null){
//                throw new IllegalStateException();
//            }
//            
//            BigInteger count = (BigInteger) em.createQuery("Select count(*) from SgPersonalSedeEducativa pe INNER JOIN SgDatoEmpleado de ON (pe.pseDatoEmpleado = de.demPk) "
//                    + " INNER JOIN SgDatoContratacion dat ON (dat.dcoDatoEmpleado = de.demPk) where pe.psePk = :psePk AND dat.dcoTipo = :tipo")
//                    .setParameter("psePK", personalPk)
//                    .setParameter("tipo", tipo)
//                    .getSingleResult();
//
//            return (count.longValue() > 0L);
//
//        } catch (Exception ex) {
//            throw new TechnicalException(ex);
//        }
//    }
    @Interceptors({AuditInterceptor.class})
    public SgPersonalSedeEducativa guardar(SgPersonalSedeEducativa entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                Set<TipoPersonalSedeEducativa> tipos = this.obtenerTiposPersonal(entity);

                if (PersonalSedeEducativoValidacionNegocio.validar(entity, emailPattern, configuracionBean, tipos)) {

                    SgDatoContratacion crearConDato = entity.getPseCrearConDatoContratacion();

                    if (entity.getPsePk() == null) {
                        entity.setPseDatoEmpleado(new SgDatoEmpleado());
                        if (entity.getPsePuedeAplicarPlaza() != null) {
                            entity.getPseDatoEmpleado().setDemPuedeAplicarPlaza(entity.getPsePuedeAplicarPlaza());
                        } else {
                            entity.getPseDatoEmpleado().setDemPuedeAplicarPlaza(Boolean.FALSE);
                        }
                        entity.setPseEstudioRealizado(new SgEstudioRealizado());
                        entity.getPseEstudioRealizado().setErePersonalSede(entity);
                    }

                    if (tipos.contains(TipoPersonalSedeEducativa.DOC) || BooleanUtils.isTrue(entity.getPseDatoEmpleado().getDemPuedeAplicarPlaza())) {
                        if (StringUtils.isBlank(entity.getPsePersona().getPerNip())) {
                            entity.getPsePersona().setPerNip(entity.getPsePersona().calcularNIP());
                        }
                    }

                    //Actualizar los datos de seguridad si cambio Nombres o Correo.
                    if (entity.getPsePersona() != null && entity.getPsePersona().getPerPk() != null) {
                        personaBean.actualizarSeguridad(entity.getPsePersona());
                    }

                    CodigueraDAO<SgPersonalSedeEducativa> codDAO = new CodigueraDAO(em, SgPersonalSedeEducativa.class);

                    SgPersona per = personaBean.guardar(entity.getPsePersona());
                    entity.setPsePersona(per);

                    SgPersonalSedeEducativa docente;
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        docente = codDAO.guardar(entity, entity.getPsePk() == null ? ConstantesOperaciones.CREAR_PERSONAL_SEDE_EDUCATIVA : ConstantesOperaciones.ACTUALIZAR_PERSONAL_SEDE_EDUCATIVA);
                    } else {
                        docente = codDAO.guardar(entity, null);
                    }

                    if (per.getPerUsuarioPk() == null) {
                        usuarioBean.crearObtenerUsuarioPersona(per);
                    }

                    if (crearConDato != null) {
                        docente.getPseDatoEmpleado().setDemPersonalSede(docente);
                        crearConDato.setDcoDatoEmpleado(docente.getPseDatoEmpleado());
                        datoContratacionBean.guardar(crearConDato);
                    }

                    return docente;
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
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersonalSedeEducativa
     * @throws GeneralException
     */
    public List<SgNominaDocente> obtenerNominaDocentePorFiltro(FiltroPersonalSedeEducativa filtro) throws GeneralException {
        try {

            if (filtro.getIncluirCampos() == null) {
                BusinessException be = new BusinessException();
                be.addError("INCLUIR_CAMPOS_VACIO");
                throw be;
            }

            if (filtro.getPerCentroEducativo() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_SEDE_VACIO);
                throw be;
            }

            PersonalSedeEducativaDAO dao = new PersonalSedeEducativaDAO(em, seguridadBean);
            String securityOperation = ConstantesOperaciones.BUSCAR_PERSONAL_SEDE_EDUCATIVA;
            List<SgPersonalSedeEducativa> personal = dao.obtenerPorFiltro(filtro, securityOperation);

            List<SgNominaDocente> ret = new ArrayList<>();
            HashMap<Long, SgNominaDocente> ndhm = new HashMap<>();

            if (!personal.isEmpty()) {

                List<Long> personalPks = personal.stream().map(p -> p.getPsePk()).collect(Collectors.toList());

                FiltroDatoContratacion fDato = new FiltroDatoContratacion();
                fDato.setDcoPersonalesPk(personalPks);
                fDato.setDcoCentroEducativo(filtro.getPerCentroEducativo());
                fDato.setBuscarEnSedeAdscrita(filtro.getBuscarEnSedeAdscrita());
                fDato.setIncluirCampos(new String[]{
                    "dcoDatoEmpleado.demPersonalSede.psePk",
                    "dcoInstitucionPagaSalario.ipsPk",
                    "dcoInstitucionPagaSalario.ipsNombre",
                    "dcoTipoNombramiento.tnoPk",
                    "dcoTipoNombramiento.tnoNombre",
                    "dcoTipoContrato.tcoPk",
                    "dcoTipoContrato.tcoNombre"
                });

                List<SgDatoContratacion> datos = datoContratacionBean.obtenerPorFiltro(fDato);
                HashMap<Long, List<SgDatoContratacion>> datosContratAgrupados = new HashMap<>();
                for (SgDatoContratacion dc : datos) {
                    if (dc.getDcoDatoEmpleado() != null && dc.getDcoDatoEmpleado().getDemPersonalSede() != null && dc.getDcoDatoEmpleado().getDemPersonalSede().getPsePk() != null) {
                        datosContratAgrupados.computeIfAbsent(dc.getDcoDatoEmpleado().getDemPersonalSede().getPsePk(), s -> new ArrayList<>()).add(dc);
                    }
                }

                for (SgPersonalSedeEducativa per : personal) {

                    per.setPseDatoEmpleado(new SgDatoEmpleado());
                    per.getPseDatoEmpleado().setDemDatoContratacion(datosContratAgrupados.get(per.getPsePk()) != null ? datosContratAgrupados.get(per.getPsePk()) : new ArrayList<>());
                    per.setPseRelEspecialidades(new ArrayList<>());

                    SgNominaDocente nd = new SgNominaDocente();
                    nd.setNomPersonal(per);
                    ret.add(nd);
                    ndhm.put(per.getPsePk(), nd);
                }

                //obtener materias donde es unico docente
                FiltroHorarioEscolar fil = new FiltroHorarioEscolar();
                fil.setHesAnio(LocalDate.now().getYear());
                fil.setHesUnicoDocentePks(personalPks);
                fil.setHesCentroEducativo(filtro.getPerCentroEducativo());
                fil.setBuscarEnSedeAdscrita(filtro.getBuscarEnSedeAdscrita());
                fil.setIncluirCampos(new String[]{"hesDocente.psePk",
                    "hesSeccion.secServicioEducativo.sduGrado.graPk",
                    "hesSeccion.secServicioEducativo.sduGrado.graNombre",
                    "hesSeccion.secServicioEducativo.sduGrado.graOrden"});

                List<SgHorarioEscolar> horarios = horarioEscolarBean.obtenerPorFiltro(fil);
                for (SgHorarioEscolar h : horarios) {
                    SgNominaDocente nd = ndhm.get(h.getHesDocente().getPsePk());
                    nd.getNomGradoUnicoDocente().add(h.getHesSeccion().getSecServicioEducativo().getSduGrado());
                }

                //obtener resto
                FiltroComponenteDocente fcd = new FiltroComponenteDocente();
                fcd.setCdoAnio(LocalDate.now().getYear());
                fcd.setCdoDocentes(personalPks);
                fcd.setCdoSede(filtro.getPerCentroEducativo());
                fcd.setBuscarEnSedeAdscrita(filtro.getBuscarEnSedeAdscrita());
                fcd.setIncluirCampos(new String[]{
                    "cdoDocente.psePk",
                    "cdoComponente.cpePk",
                    "cdoComponente.cpeTipo",
                    "cdoComponente.cpeNombre",
                    "cdoHorarioEscolar.hesSeccion.secServicioEducativo.sduGrado.graPk",
                    "cdoHorarioEscolar.hesSeccion.secServicioEducativo.sduGrado.graNombre",
                    "cdoHorarioEscolar.hesSeccion.secServicioEducativo.sduGrado.graOrden",});

                List<SgComponenteDocente> componentes = componenteDocenteBean.obtenerPorFiltro(fcd);

                for (SgComponenteDocente cd : componentes) {
                    SgNominaDocente nd = ndhm.get(cd.getCdoDocente().getPsePk());

                    Optional<SgNominaGradoMaterias> op = nd.getNomMateriasPorGrado().stream()
                            .filter(m -> m.getNomGrado().equals(cd.getCdoHorarioEscolar().getHesSeccion().getSecServicioEducativo().getSduGrado()))
                            .findAny();
                    if (op.isPresent()) {
                        op.get().getNomMaterias().add(cd.getCdoComponente());
                    } else {
                        SgNominaGradoMaterias ng = new SgNominaGradoMaterias();
                        ng.setNomGrado(cd.getCdoHorarioEscolar().getHesSeccion().getSecServicioEducativo().getSduGrado());
                        ng.getNomMaterias().add(cd.getCdoComponente());
                        nd.getNomMateriasPorGrado().add(ng);
                    }
                }

                //especialidades 
                FiltroRelPersonalEspecialidad files = new FiltroRelPersonalEspecialidad();
                files.setPersonalPks(personalPks);
                files.setOrderBy(new String[]{"rpeEspecialidad.espNombre"});
                files.setAscending(new boolean[]{false});
                files.setIncluirCampos(new String[]{
                    "rpePersonal.psePk",
                    "rpeEspecialidad.espCodigo",
                    "rpeEspecialidad.espNombre",
                    "rpeEspecialidad.espVersion"});
                List<SgRelPersonalEspecialidad> relEspecialidades = relPersonalEspecialidadBean.obtenerPorFiltro(files);
                for (SgRelPersonalEspecialidad e : relEspecialidades) {
                    SgNominaDocente nd = ndhm.get(e.getRpePersonal().getPsePk());
                    nd.getNomPersonal().getPseRelEspecialidades().add(e);
                }

            }
            return ret;
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
                CodigueraDAO<SgPersonalSedeEducativa> codDAO = new CodigueraDAO<>(em, SgPersonalSedeEducativa.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_PERSONAL_SEDE_EDUCATIVA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    //Destino queda fijo, origen es eliminado, todas las relaciones de origen se pasan a destino
    public void unirPersonal(Long personalOrigenPk, Long personalDestinoPk) {

        try {
            SgPersonalSedeEducativa origen = em.find(SgPersonalSedeEducativa.class, personalOrigenPk);
            SgPersonalSedeEducativa destino = em.find(SgPersonalSedeEducativa.class, personalDestinoPk);
            SgPersona perOrigen = origen.getPsePersona();
            SgPersona perDestino = destino.getPsePersona();

            //Datos personales
            //Copiar campos simples estudiante que no sean null
            PropertyUtils.describe(destino).entrySet().stream()
                    .filter(e -> e.getValue() == null
                    && !e.getKey().equals("class")
                    && !e.getKey().equals("psePk")
                    && !e.getKey().equals("pseCodigo")
                    && !e.getKey().equals("psePersona")
                    && !e.getKey().equals("pseVersion")
                    && !e.getKey().equals("pseDatoEmpleado")
                    && !e.getKey().equals("pseEstudioRealizado"))
                    .forEach(e -> {
                        try {
                            PropertyUtils.describe(origen).entrySet().stream().
                                    filter(k -> k.getValue() != null && k.getKey().equals(e.getKey()) && !k.getKey().equals("class"))
                                    .forEach(
                                            k -> {
                                                try {
                                                    PropertyUtils.setProperty(destino, k.getKey(), k.getValue());
                                                } catch (Exception ex) {
                                                }
                                            }
                                    );

                        } catch (Exception ex) {
                        }
                    });

            //Copiar campos simples persona que no sean null
            PropertyUtils.describe(perOrigen).entrySet().stream()
                    .filter(e -> e.getValue() != null
                    && !e.getKey().equals("class")
                    && !e.getKey().equals("perPk")
                    && !e.getKey().equals("perDui")
                    && !e.getKey().equals("perNip")
                    && !e.getKey().equals("perEstudiante")
                    && !e.getKey().equals("perVersion"))
                    .forEach(e -> {
                        try {
                            if (PropertyUtils.getProperty(perDestino, e.getKey()) == null) {
                                PropertyUtils.setProperty(perDestino, e.getKey(), e.getValue());
                            }
                        } catch (Exception ex) {
                        }
                    });
            if (StringUtils.isBlank(perDestino.getPerDui())) {
                perDestino.setPerDui(perOrigen.getPerDui());
                origen.getPsePersona().setPerDui(null);
                em.flush();
            }
            if (StringUtils.isBlank(perDestino.getPerNip())) {
                perDestino.setPerNip(perOrigen.getPerNip());
            }

            //EMPLEADO
            //Convertir todos los SgHorarioEscolar de origen a destino
            if (origen.getPseRelEspecialidades() != null) {
                //Hibernate autosave
                String query = "update from SgRelPersonalEspecialidad SET rpePersonal=:destino where rpePersonal=:origen";
                Query qda = em.createQuery(query).setParameter("destino", destino).setParameter("origen", origen);
                qda.executeUpdate();
            }

            //NOMBRAMIENTOS Y CONTRATOS
            //Esto se hace a modo de actualizar los roles del dato de contratación en caso de que no tuviera por no tener DUI
            if (destino.getPseDatoEmpleado() != null) {
                if (destino.getPseDatoEmpleado().getDemDatoContratacion() != null) {
                    for (SgDatoContratacion datoContrat : destino.getPseDatoEmpleado().getDemDatoContratacion()) {
                        try {
                            datoContratacionBean.guardar(datoContrat);
                        } catch (BusinessException be) {
                            be.addError("dcoCargo", Errores.ERRORES_DADOS_PERTENECEN_PESTANIA_NOMBRAMIENTOS);
                            throw be;
                        }
                    }
                }
            }

            if (origen.getPseDatoEmpleado() != null) {
                if (origen.getPseDatoEmpleado().getDemDatoContratacion() != null) {
                    for (SgDatoContratacion datoContrat : origen.getPseDatoEmpleado().getDemDatoContratacion()) {
                        datoContrat.setDcoDatoEmpleado(destino.getPseDatoEmpleado());
                        try {
                            datoContratacionBean.guardar(datoContrat);
                        } catch (BusinessException be) {
                            be.addError("dcoCargo", Errores.ERRORES_DADOS_PERTENECEN_PESTANIA_NOMBRAMIENTOS);
                            throw be;
                        }
                    }
                }
            }

            //EXPERIENCIA LABORAL
            if (origen.getPseDatoEmpleado() != null) {
                if (origen.getPseDatoEmpleado().getDemExperienciaLaboral() != null) {
                    for (SgExperienciaLaboral experienciaLaboral : origen.getPseDatoEmpleado().getDemExperienciaLaboral()) {
                        experienciaLaboral.setElaDatoEmpleado(new SgDatoEmpleadoLite(destino.getPseDatoEmpleado().getDemPk(), destino.getPseDatoEmpleado().getDemVersion()));
                    }
                }

            }

            //ESTUDIOS REALIZADOS
            if (origen.getPseEstudioRealizado() != null) {
                if (origen.getPseEstudioRealizado() != null) {
                    if (destino.getPseEstudioRealizado() == null) {
                        origen.getPseEstudioRealizado().setErePersonalSede(destino);
                    } else {
                        for (SgIdiomaRealizado idioma : origen.getPseEstudioRealizado().getEreIdiomas()) {
                            idioma.setIreEstudiosRealizados(destino.getPseEstudioRealizado());
                        }
                        for (SgEstudioSuperior estSuperior : origen.getPseEstudioRealizado().getEreEstudiosSuperiores()) {
                            estSuperior.setEsuEstudios(destino.getPseEstudioRealizado());
                        }
                        for (SgCapacitacion capacitacion : origen.getPseEstudioRealizado().getEreCapacitaciones()) {
                            capacitacion.setCapEstudios(destino.getPseEstudioRealizado());
                        }
                    }

                }

            }

            //FAMILIARES
            //Modificar colecciones persona
            if (perOrigen.getPerAllegados() != null) {
                //Hibernate autosave
                Boolean origenTieneReferente = Boolean.FALSE;
                for (SgAllegado a : perOrigen.getPerAllegados()) {
                    if (a.getAllReferente()) {
                        origenTieneReferente = Boolean.TRUE;
                        break;
                    }
                }
                if (origenTieneReferente) {
                    for (SgAllegado a : perDestino.getPerAllegados()) {
                        a.setAllReferente(Boolean.FALSE);
                    }
                }
                for (SgAllegado a : perOrigen.getPerAllegados()) {
                    a.setAllPersonaReferenciada(perDestino);
                }
            }

            //ORIGEN ES FAMILIAR DE
            FiltroAllegado fa = new FiltroAllegado();
            fa.setAllPersonaPk(perOrigen.getPerPk());
            List<SgAllegado> origenEsFamiliar = allegadoBean.obtenerPorFiltro(fa);

            if (!origenEsFamiliar.isEmpty()) {
                String query = "update from SgAllegado SET allPersona=:perDestino where allPersona=:perOrigen";
                Query qda = em.createQuery(query).setParameter("perDestino", perDestino).setParameter("perOrigen", perOrigen);
                qda.executeUpdate();

            }

            if (perOrigen.getPerDiscapacidades() != null) {
                //Guardado por guardar estudiante cascada
                if (perDestino.getPerDiscapacidades() == null) {
                    perDestino.setPerDiscapacidades(new ArrayList<>());
                }
                for (SgDiscapacidad d : perOrigen.getPerDiscapacidades()) {
                    if (!perDestino.getPerDiscapacidades().contains(d)) {
                        perDestino.getPerDiscapacidades().add(d);
                    }
                }
            }

            if (perOrigen.getPerTelefonos() != null) {
                //Guardado por guardar estudiante cascada
                if (perDestino.getPerTelefonos() == null) {
                    perDestino.setPerTelefonos(new ArrayList<>());
                }
                for (SgTelefono t : perOrigen.getPerTelefonos()) {
                    t.setTelPersona(perDestino);
                    if (!perDestino.getPerTelefonos().contains(t)) {
                        perDestino.getPerTelefonos().add(t);
                    }
                }
            }

            //HORARIO ESCOLAR
            FiltroComponenteDocente fcd = new FiltroComponenteDocente();
            fcd.setCdoDocente(personalOrigenPk);
            List<SgComponenteDocente> componentesOrigen = componenteDocenteBean.obtenerPorFiltro(fcd);
            SgPersonalSedeEducativaLite personalLiteDestino = new SgPersonalSedeEducativaLite(destino.getPsePk(), destino.getPseVersion());
            SgPersonaLite perLite = new SgPersonaLite(perDestino.getPerPk(), perDestino.getPerVersion());
            perLite.setPerDui(perDestino.getPerDui());
            personalLiteDestino.setPsePersona(perLite);
            SgDatoEmpleadoLite datoEmpLite = new SgDatoEmpleadoLite(destino.getPseDatoEmpleado().getDemPk(), destino.getPseDatoEmpleado().getDemVersion());
            personalLiteDestino.setPseDatoEmpleado(datoEmpLite);
            componentesOrigen.forEach(c -> {
                c.setCdoDocente(personalLiteDestino);
                componenteDocenteBean.guardar(c);
            });

            //Convertir horarios escolares con unico docente origen a destino
            FiltroHorarioEscolar fhe = new FiltroHorarioEscolar();
            fhe.setHesUnicoDocente(personalOrigenPk);
            List<SgHorarioEscolar> horarioEscolarOrigen = horarioEscolarBean.obtenerPorFiltro(fhe);

            horarioEscolarOrigen.forEach(c -> {
                c.setHesDocente(personalLiteDestino);
                horarioEscolarBean.guardar(c);
            });

            //Convertir todos los SgSolicitudCursoDocente de origen a destino
            FiltroSolicitudCursoDocente fsd = new FiltroSolicitudCursoDocente();
            fsd.setScdPersonal(personalOrigenPk);
            List<SgSolicitudCursoDocente> solicitudCursoOrigen = solicitudCursoDocenteBean.obtenerPorFiltro(fsd);

            solicitudCursoOrigen.forEach(j -> {
                j.setScdPersonal(destino);
                solicitudCursoDocenteBean.guardar(j);
            });
            //FORMACION DOCENTE
            //Convertir todos los SgFormacionDocente de origen a destino

            if (origen.getPseFormacionDocente() != null) {
                String query = "update from SgFormacionDocente SET fdoPersonalSede=:destino where fdoPersonalSede=:origen";
                Query qda = em.createQuery(query).setParameter("destino", destino).setParameter("origen", origen);
                qda.executeUpdate();

            }

            destino.getPsePersona().setPerUsuarioPk(null);
            this.guardar(destino, Boolean.FALSE);

            this.eliminar(origen.getPsePk());

            personaBean.eliminar(perOrigen.getPerPk());

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    public void eliminarRelacionesDePersonal(Long personalPk) {
        try {

            if (personalPk != null) {

                //Eliminar asistencias personal t
                em.createQuery("DELETE FROM SgAsistenciaPersonal where apePersonal.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Eliminar componentes docentes t
                em.createQuery("DELETE FROM SgComponenteDocente where cdoDocente.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Documentos docentes t
                em.createQuery("DELETE FROM SgDocenteDocumento where ddoPersonal.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Formacionm docente t
                em.createQuery("DELETE FROM SgFormacionDocente where fdoPersonalSede.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Rel personal especialidad
                em.createQuery("DELETE FROM SgRelPersonalEspecialidad where rpePersonal.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Solicitud curso docente
                em.createQuery("DELETE FROM SgSolicitudCursoDocente where scdPersonal.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //sg_asignacion_perfil_personal
                em.createQuery("DELETE FROM SgAsignacionPerfilPersonal where appPersonalFk.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //pasaro horario escolar a null
                em.createNativeQuery("update centros_educativos.sg_horarios_escolares set hes_docente_fk=null where hes_docente_fk=:psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                /*
            //Relacionados con estudios realizados
                 */
                //Elimiar capacitaciones
                em.createNativeQuery("delete from centros_educativos.sg_capacitaciones where cap_estudios_fk=\n"
                        + "(select est_r.ere_pk from centros_educativos.sg_estudios_realizados est_r where est_r.ere_personal_sede_fk=:psePk)")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Elimiar estudios superiores
                em.createNativeQuery("delete from centros_educativos.sg_estudios_superiores where esu_estudios_fk=\n"
                        + "(select est_r.ere_pk from centros_educativos.sg_estudios_realizados est_r where est_r.ere_personal_sede_fk=:psePk)")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Elimiar idiomas realizados
                em.createNativeQuery("delete from centros_educativos.sg_idiomas_realizados where ire_estudios_realizados_fk=\n"
                        + "(select est_r.ere_pk from centros_educativos.sg_estudios_realizados est_r where est_r.ere_personal_sede_fk=:psePk)")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Elimiar Estudio realizado
                em.createQuery("DELETE FROM SgEstudioRealizado where erePersonalSede.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Aplicacion plaza especialidades al aplicar
                em.createNativeQuery(" delete from centros_educativos.sg_especialidades_personal_al_aplicar where epa_aplicacion_plaza_fk=\n"
                        + "(select est_r.apl_pk from centros_educativos.sg_aplicaciones_plaza est_r where est_r.apl_personal_fk=:psePk)")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Motivo aplicacion plaza
                em.createNativeQuery("delete from centros_educativos.sg_motivo_aplicacion_plaza where apl_pk =\n"
                        + "(select est_r.apl_pk from centros_educativos.sg_aplicaciones_plaza est_r where est_r.apl_personal_fk=:psePk)")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                //Aplicación plaza
                em.createQuery("DELETE FROM SgAplicacionPlaza where aplPersonal.psePk = :psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

                em.createNativeQuery("delete from centros_educativos.sg_docente where pse_pk=:psePk")
                        .setParameter("psePk", personalPk)
                        .executeUpdate();

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
