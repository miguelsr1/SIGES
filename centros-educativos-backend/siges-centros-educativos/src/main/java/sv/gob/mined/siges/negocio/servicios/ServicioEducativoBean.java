/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgRegistroServiciosEducativos;
import sv.gob.mined.siges.enumerados.EnumServicioEducativoEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroGrado;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.filtros.FiltroServicioEducativo;
import sv.gob.mined.siges.filtros.catalogo.FiltroTipoAcuerdo;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuario;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ServicioEducativoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ServicioEducativoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSede;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;
import sv.gob.mined.siges.persistencia.entidades.SgOpcion;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.entidades.SgServicioEducativo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.persistencia.utilidades.SimpleUtils;

@Stateless
@Traced
public class ServicioEducativoBean {

    private static final Logger LOGGER = Logger.getLogger(ServicioEducativoBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private GradoBean gradoBean;

    @Inject
    private CatalogoBean catalogoBean;

    @Inject
    private AcuerdoSedeBean acuerdoSedeBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgServicioEducativo
     * @throws GeneralException
     */
    public SgServicioEducativo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
                SgServicioEducativo serv = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SERVICIO_EDUCATIVO);
                if (serv.getSduSede() != null) {
                    serv.getSduSede().getSedPk();
                    if (serv.getSduSede().getSedJornadasLaborales() != null) {
                        serv.getSduSede().getSedJornadasLaborales().size();
                    }
                }
                return serv;
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
                CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SERVICIO_EDUCATIVO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgServicioEducativo
     * @return SgServicioEducativo
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgServicioEducativo guardar(SgServicioEducativo entity) throws GeneralException {
        try {
            if (entity != null) {
                if (ServicioEducativoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
                    if (BooleanUtils.isTrue(entity.getSduOrigenExterno())) {
                        SimpleUtils.cargarEntidadesSegunId(entity, em, seguridadBean);
                    }
                    return codDAO.guardar(entity, entity.getSduPk() == null ? ConstantesOperaciones.CREAR_SERVICIO_EDUCATIVO : ConstantesOperaciones.ACTUALIZAR_SERVICIO_EDUCATIVO);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    @Interceptors(AuditInterceptor.class)
    public void habilitarServiciosEducativos(SgRegistroServiciosEducativos se, Boolean generarAcuerdo) throws GeneralException {
        try {
            if (se != null) {
                if (ServicioEducativoValidacionNegocio.validar(se)) {
                    CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
                    BusinessException ge = new BusinessException();
                    for (List<String> servicio : se.getListadoServicios()) {

                        Long nivelPk = servicio.get(0) != null ? Long.parseLong(servicio.get(0)) : -1L;
                        Long cicloPk = servicio.get(1) != null ? Long.parseLong(servicio.get(1)) : -1L;
                        Long modalidadEducativaPk = servicio.get(2) != null ? Long.parseLong(servicio.get(2)) : -1L;
                        Long modalidadAtencionPk = servicio.get(3) != null ? Long.parseLong(servicio.get(3)) : -1L;
                        Long subModalidadAtencionPk = servicio.get(4) != null ? Long.parseLong(servicio.get(4)) : -1L;
                        Long opcionPk = servicio.get(5) != null ? Long.parseLong(servicio.get(5)) : -1L;
                        Long programaPk = servicio.get(6) != null ? Long.parseLong(servicio.get(6)) : -1L;
                        Long gradoPk = servicio.get(7) != null ? Long.parseLong(servicio.get(7)) : -1L;

                        if (nivelPk < 1) {
                            nivelPk = null;
                        }
                        if (cicloPk < 1) {
                            cicloPk = null;
                        }
                        if (modalidadEducativaPk < 1) {
                            modalidadEducativaPk = null;
                        }
                        if (modalidadAtencionPk < 1) {
                            modalidadAtencionPk = null;
                        }
                        if (subModalidadAtencionPk < 1) {
                            subModalidadAtencionPk = null;
                        }
                        if (opcionPk < 1) {
                            opcionPk = null;
                        }
                        if (programaPk < 1) {
                            programaPk = null;
                        }
                        if (gradoPk < 1) {
                            gradoPk = null;
                        }

                        if (nivelPk == null) {
                            ge.addError("nivelPk", Errores.ERROR_NIVEL_VACIO);
                            throw ge;
                        }
                        if (cicloPk == null) {
                            ge.addError("cicloPk", Errores.ERROR_CICLO_VACIO);
                            throw ge;
                        }
                        if (modalidadEducativaPk == null) {
                            ge.addError("modalidadEducativaPk", Errores.ERROR_MODALIDAD_EDUCATIVA_VACIO);
                            throw ge;
                        }
                        if (modalidadAtencionPk == null) {
                            ge.addError("modalidadAtencionPk", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
                            throw ge;
                        }

                        FiltroGrado filg = new FiltroGrado();
                        filg.setModPk(modalidadEducativaPk);
                        filg.setModAtencionPk(modalidadAtencionPk);
                        filg.setSubModAtencionPk(subModalidadAtencionPk);
                        filg.setGraPk(gradoPk);
                        filg.setIncluirCampos(new String[]{"graPk"});
                        List<SgGrado> grados = gradoBean.obtenerPorFiltro(filg);

                        for (SgGrado g : grados) {

                            //Verificar si servicio existente
                            FiltroServicioEducativo filtro = new FiltroServicioEducativo();
                            filtro.setSecurityOperation(null);
                            if (opcionPk == null) {
                                filtro.setTieneOpcion(Boolean.FALSE);
                            } else {
                                filtro.setSecOpcionFk(opcionPk);
                            }
                            if (programaPk == null) {
                                filtro.setTienePrograma(Boolean.FALSE);
                            } else {
                                filtro.setSecProgramaEducativoFk(programaPk);
                            }
                            filtro.setSecGradoFk(g.getGraPk());
                            filtro.setSecSedeFk(se.getSedePk());

                            List<SgServicioEducativo> servicios = this.obtenerPorFiltro(filtro);

                            if (!servicios.isEmpty()) {
                                SgServicioEducativo s = servicios.get(0);
                                if (EnumServicioEducativoEstado.NO_HABILITADO.equals(s.getSduEstado())) {
                                    s.setSduFechaHabilitado(LocalDate.now());
                                    s.setSduFechaSolicitada(LocalDate.now());
                                }
                                s.setSduEstado(EnumServicioEducativoEstado.HABILITADO);
                                s.setSduNumeroTramite(se.getNumTramite());
                                codDAO.guardar(s, null);
                            } else {

                                SgServicioEducativo serv = new SgServicioEducativo();
                                serv.setSduEstado(EnumServicioEducativoEstado.HABILITADO);
                                serv.setSduFechaHabilitado(LocalDate.now());
                                serv.setSduFechaSolicitada(LocalDate.now());
                                serv.setSduNumeroTramite(se.getNumTramite());
                                if (opcionPk != null) {
                                    serv.setSduOpcion(em.getReference(SgOpcion.class, opcionPk));
                                }
                                if (programaPk != null) {
                                    serv.setSduProgramaEducativo(em.getReference(SgProgramaEducativo.class, programaPk));
                                }
                                serv.setSduGrado(em.getReference(SgGrado.class, g.getGraPk()));
                                serv.setSduSede(em.getReference(SgSede.class, se.getSedePk()));
                                codDAO.guardar(serv, null);
                            }
                        }

                    }
                    if (BooleanUtils.isTrue(generarAcuerdo)) {

                        FiltroTipoAcuerdo filtroTipoAcuerdo = new FiltroTipoAcuerdo();
                        filtroTipoAcuerdo.setHabilitado(Boolean.TRUE);
                        filtroTipoAcuerdo.setTaoTramiteAmpliacionServicios(Boolean.TRUE);
                        List<SgTipoAcuerdo> tiposAcuerdo = catalogoBean.buscarTipoAcuerdo(filtroTipoAcuerdo);

                        if (!tiposAcuerdo.isEmpty()) {
                            String usuario = Lookup.obtenerJWT().getSubject();

                            if (usuario != null && !StringUtils.isBlank(usuario)) {
                                FiltroUsuario fu = new FiltroUsuario();
                                fu.setCodigo(usuario);
                                List<SgUsuario> list = seguridadBean.obtenerUsuarios(fu);
                                if (list != null && !list.isEmpty()) {
                                    usuario = list.get(0).getUsuCodigo() + " - " + list.get(0).getUsuNombre();
                                }
                            }
                            SgAcuerdoSede acuerdo = new SgAcuerdoSede();
                            acuerdo.setAseOrigenExterno(Boolean.TRUE);
                            acuerdo.setAseSede(new SgSede(se.getSedePk(), 0));
                            acuerdo.setAseTipoAcuerdo(tiposAcuerdo.get(0));
                            acuerdo.setAseNumeroSolicitud(se.getNumTramite());
                            acuerdo.setAseFechaRegistro(LocalDate.now());
                            acuerdo.setAseNombreResponsable(usuario);
                            acuerdo.setAseSoloLectura(Boolean.TRUE);
                            acuerdo.setAseObservacion(se.getObservacion());
                            acuerdo.setAseFechaGeneracion(se.getFechaGeneracion());
                            acuerdo.setAseNumeroAcuerdo(se.getNumAcuerdo());
                            acuerdo.setAseNumeroResolucion(se.getNumResolucion() );
                            acuerdo.setAseFechaResolucion(se.getFechaResolucion());
                            acuerdoSedeBean.guardar(acuerdo, Boolean.FALSE);
                        }

                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @Interceptors(AuditInterceptor.class)
    public void deshabilitarServiciosEducativos(SgRegistroServiciosEducativos se, Boolean generarAcuerdo) throws GeneralException {
        try {
            if (se != null) {
                if (ServicioEducativoValidacionNegocio.validar(se)) {
                    CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
                    BusinessException ge = new BusinessException();
                    for (List<String> servicio : se.getListadoServicios()) {

                        Long nivelPk = servicio.get(0) != null ? Long.parseLong(servicio.get(0)) : -1L;
                        Long cicloPk = servicio.get(1) != null ? Long.parseLong(servicio.get(1)) : -1L;
                        Long modalidadEducativaPk = servicio.get(2) != null ? Long.parseLong(servicio.get(2)) : -1L;
                        Long modalidadAtencionPk = servicio.get(3) != null ? Long.parseLong(servicio.get(3)) : -1L;
                        Long subModalidadAtencionPk = servicio.get(4) != null ? Long.parseLong(servicio.get(4)) : -1L;
                        Long opcionPk = servicio.get(5) != null ? Long.parseLong(servicio.get(5)) : -1L;
                        Long programaPk = servicio.get(6) != null ? Long.parseLong(servicio.get(6)) : -1L;
                        Long gradoPk = servicio.get(7) != null ? Long.parseLong(servicio.get(7)) : -1L;

                        if (nivelPk < 1) {
                            nivelPk = null;
                        }
                        if (cicloPk < 1) {
                            cicloPk = null;
                        }
                        if (modalidadEducativaPk < 1) {
                            modalidadEducativaPk = null;
                        }
                        if (modalidadAtencionPk < 1) {
                            modalidadAtencionPk = null;
                        }
                        if (subModalidadAtencionPk < 1) {
                            subModalidadAtencionPk = null;
                        }
                        if (opcionPk < 1) {
                            opcionPk = null;
                        }
                        if (programaPk < 1) {
                            programaPk = null;
                        }
                        if (gradoPk < 1) {
                            gradoPk = null;
                        }

                        if (nivelPk == null) {
                            ge.addError("nivelPk", Errores.ERROR_NIVEL_VACIO);
                            throw ge;
                        }
                        if (cicloPk == null) {
                            ge.addError("cicloPk", Errores.ERROR_CICLO_VACIO);
                            throw ge;
                        }
                        if (modalidadEducativaPk == null) {
                            ge.addError("modalidadEducativaPk", Errores.ERROR_MODALIDAD_EDUCATIVA_VACIO);
                            throw ge;
                        }
                        if (modalidadAtencionPk == null) {
                            ge.addError("modalidadAtencionPk", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
                            throw ge;
                        }

                        FiltroServicioEducativo filtro = new FiltroServicioEducativo();

                        filtro.setSecModalidadEducativaPk(modalidadEducativaPk);
                        filtro.setSecModalidadAtencionPk(modalidadAtencionPk);
                        filtro.setSecSubModalidadAtencionFk(subModalidadAtencionPk);
//                        if (opcionPk == null) {
//                            filtro.setTieneOpcion(Boolean.FALSE);
//                        } else {
//                            filtro.setSecOpcionFk(opcionPk);
//                        }
//                        if (programaPk == null) {
//                            filtro.setTienePrograma(Boolean.FALSE);
//                        } else {
//                            filtro.setSecProgramaEducativoFk(programaPk);
//                        }
                        filtro.setSecOpcionFk(opcionPk);
                        filtro.setSecProgramaEducativoFk(programaPk);
                        filtro.setSecGradoFk(gradoPk);
                        filtro.setSecSedeFk(se.getSedePk());
                        filtro.setSecurityOperation(null);

                        List<SgServicioEducativo> servicios = this.obtenerPorFiltro(filtro);

                        for (SgServicioEducativo s : servicios) {
                            s.setSduEstado(EnumServicioEducativoEstado.NO_HABILITADO);
                            s.setSduNumeroTramite(se.getNumTramite());
                            codDAO.guardar(s, null);
                        }
                    }
                    if (BooleanUtils.isTrue(generarAcuerdo)) {

                        FiltroTipoAcuerdo filtroTipoAcuerdo = new FiltroTipoAcuerdo();
                        filtroTipoAcuerdo.setHabilitado(Boolean.TRUE);
                        filtroTipoAcuerdo.setTaoTramiteDisminucionServicios(Boolean.TRUE);
                        List<SgTipoAcuerdo> tiposAcuerdo = catalogoBean.buscarTipoAcuerdo(filtroTipoAcuerdo);

                        if (!tiposAcuerdo.isEmpty()) {
                            String usuario = Lookup.obtenerJWT().getSubject();

                            if (usuario != null && !StringUtils.isBlank(usuario)) {
                                FiltroUsuario fu = new FiltroUsuario();
                                fu.setCodigo(usuario);
                                List<SgUsuario> list = seguridadBean.obtenerUsuarios(fu);
                                if (list != null && !list.isEmpty()) {
                                    usuario = list.get(0).getUsuCodigo() + " - " + list.get(0).getUsuNombre();
                                }
                            }

                            SgAcuerdoSede acuerdo = new SgAcuerdoSede();
                            acuerdo.setAseOrigenExterno(Boolean.TRUE);
                            acuerdo.setAseSede(new SgSede(se.getSedePk(), 0));
                            acuerdo.setAseTipoAcuerdo(tiposAcuerdo.get(0));
                            acuerdo.setAseNumeroSolicitud(se.getNumTramite());
                            acuerdo.setAseFechaRegistro(LocalDate.now());
                            acuerdo.setAseNombreResponsable(usuario);
                            acuerdo.setAseSoloLectura(Boolean.TRUE);
                            acuerdo.setAseObservacion(se.getObservacion());
                            acuerdo.setAseFechaGeneracion(se.getFechaGeneracion());
                            acuerdo.setAseNumeroAcuerdo(se.getNumAcuerdo());
                            acuerdo.setAseNumeroResolucion(se.getNumResolucion() );
                            acuerdo.setAseFechaResolucion(se.getFechaResolucion());
                            acuerdoSedeBean.guardar(acuerdo, Boolean.FALSE);
                        }

                    }
                }
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
                CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SERVICIO_EDUCATIVO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroServicioEducativo filtro) throws GeneralException {
        try {
            ServicioEducativoDAO DAO = new ServicioEducativoDAO(em, seguridadBean);
            return DAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgServicioEducativo
     * @throws GeneralException
     */
    public List<SgServicioEducativo> obtenerPorFiltro(FiltroServicioEducativo filtro) throws GeneralException {
        try {
            ServicioEducativoDAO DAO = new ServicioEducativoDAO(em, seguridadBean);
            List<SgServicioEducativo> servicios = DAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            if (!servicios.isEmpty() && BooleanUtils.isTrue(filtro.getIncluirSecciones())) {
                FiltroSeccion fSeccion = new FiltroSeccion();
                fSeccion.setOrderBy(new String[]{"secAnioLectivo.aleAnio", "secNombre"});
                fSeccion.setAscending(new boolean[]{true, true});

                fSeccion.setEstadoSeccion(filtro.getEstadoSeccion());
                fSeccion.setEstadoPromocion(filtro.getEstadoPromocion());
                fSeccion.setSecAnioLectivoFk(filtro.getSecAnioLectivoFk());
                fSeccion.setSecIntegrada(filtro.getSecIntegrada());

                fSeccion.setIncluirCampos(new String[]{"secNombre", "secVersion", "secServicioEducativo.sduPk",
                    "secJornadaLaboral.jlaNombre", "secAnioLectivo.alePk", "secAnioLectivo.aleAnio"});
                fSeccion.setSecServiciosEducativoFk(servicios.stream().map(s -> s.getSduPk()).collect(Collectors.toList()));

                List<SgSeccion> secciones = seccionBean.obtenerPorFiltro(fSeccion);

                HashMap<Long, List<SgSeccion>> seccionesAgrupadas = new HashMap<>();
                for (SgSeccion sec : secciones) {
                    seccionesAgrupadas.computeIfAbsent(sec.getSecServicioEducativo().getSduPk(), s -> new ArrayList<>()).add(sec);
                }
                for (SgServicioEducativo serv : servicios) {
                    serv.setSduSeccion(seccionesAgrupadas.get(serv.getSduPk()) != null ? seccionesAgrupadas.get(serv.getSduPk()) : new ArrayList<>());
                }
            }
            return servicios;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Copia los servicios del objeto indicado
     *
     *
     * @param sedeHija
     * @param sedePadre
     * @return Bolean
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public Boolean copiarServicios(Long sedeHija, Long sedePadre) throws GeneralException {
        try {

            //Buscar los servicios educativos de la sede padre
            FiltroServicioEducativo fser = new FiltroServicioEducativo();
            fser.setIncluirCampos(new String[]{
                "sduFechaHabilitado",
                "sduFechaSolicitada",
                "sduEstado",
                "sduSede.sedTipo",
                "sduSede.sedPk",
                "sduSede.sedVersion",
                "sduGrado.graPk",
                "sduGrado.graVersion",
                "sduOpcion.opcPk",
                "sduOpcion.opcVersion",
                "sduProgramaEducativo.pedPk",
                "sduProgramaEducativo.pedVersion"});
            fser.setSecSedeFk(sedePadre);
            fser.setSduEstado(EnumServicioEducativoEstado.HABILITADO);
            fser.setSecurityOperation(null);
            List<SgServicioEducativo> serviciosPadre = this.obtenerPorFiltro(fser);

            //Buscar los servicios de la sede adscrita, para no agregar los mismos
            fser.setSecSedeFk(sedeHija);
            List<SgServicioEducativo> serviciosActuales = this.obtenerPorFiltro(fser);

            CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
            for (SgServicioEducativo ser : serviciosPadre) {
                //Verificar que el servicio no se encuentre asociado 
                SgServicioEducativo servicioActual = serviciosActuales.stream().filter(
                        (e) -> e.getSduGrado().getGraPk().equals(ser.getSduGrado().getGraPk())
                ).findAny()
                        .orElse(null);
                if (servicioActual == null) {
                    servicioActual = ser;
                    servicioActual.setSduPk(null);
                    servicioActual.setSduSede(new SgSede(sedeHija, 0));
                    codDAO.guardar(servicioActual, ConstantesOperaciones.CREAR_SERVICIO_EDUCATIVO);
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return Boolean.TRUE;
    }

    /**
     * Copia los servicios del objeto indicado
     *
     *
     * @param listServicio
     * @return Bolean
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public Boolean copiarServiciosSelectos(List<SgServicioEducativo> listServicio) throws GeneralException {
        try {
            CodigueraDAO<SgServicioEducativo> codDAO = new CodigueraDAO<>(em, SgServicioEducativo.class);
            for (SgServicioEducativo serv : listServicio) {
                serv.setSduEstado(EnumServicioEducativoEstado.HABILITADO);
                codDAO.guardar(serv, ConstantesOperaciones.CREAR_SERVICIO_EDUCATIVO);
            }
            return Boolean.TRUE;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgServicioEducativo> obtenerServiciosSedePadre(Long sedeHija, Long sedePadre) throws GeneralException {
        try {

            //Buscar los servicios educativos de la sede padre
            FiltroServicioEducativo fser = new FiltroServicioEducativo();
            fser.setIncluirCampos(new String[]{
                "sduVersion",
                "sduGrado.graNombre",
                "sduOpcion.opcNombre",
                "sduProgramaEducativo.pedNombre",
                "sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                "sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "sduFechaHabilitado",
                "sduFechaSolicitada",
                "sduEstado",
                "sduSede.sedTipo",
                "sduSede.sedPk",
                "sduSede.sedVersion",
                "sduGrado.graPk",
                "sduGrado.graVersion",
                "sduOpcion.opcPk",
                "sduOpcion.opcVersion",
                "sduProgramaEducativo.pedPk",
                "sduProgramaEducativo.pedVersion"});
            fser.setSecSedeFk(sedePadre);
            fser.setSduEstado(EnumServicioEducativoEstado.HABILITADO);
            fser.setSecurityOperation(null);
            List<SgServicioEducativo> serviciosPadre = this.obtenerPorFiltro(fser);

            //Buscar los servicios de la sede adscrita, para no agregar los mismos
            fser.setSecSedeFk(sedeHija);
            List<SgServicioEducativo> serviciosActuales = this.obtenerPorFiltro(fser);

            List<SgServicioEducativo> serviciosPadreSinHijo = new ArrayList();
            for (SgServicioEducativo ser : serviciosPadre) {
                //Verificar que el servicio no se encuentre asociado 
                SgServicioEducativo servicioActual = serviciosActuales.stream().filter(
                        (e) -> e.getSduGrado().getGraPk().equals(ser.getSduGrado().getGraPk())
                ).findAny()
                        .orElse(null);
                if (servicioActual == null) {
                    serviciosPadreSinHijo.add(ser);

                }
            }
            return serviciosPadreSinHijo;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}
