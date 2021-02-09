/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgConfirmacionSolicitudTraslado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgGradoPlanOrigenDestino;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSolicitudTraslado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.catalogo.SgAccionSolicitudTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgMotivoTraslado;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudTraslado;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroAccion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.ReglaEquivalenciaDetalleRestClient;
import sv.gob.mined.siges.web.restclient.ReglasEquivalenciaRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudTrasladoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.ServicioEducativoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SolicitudTrasladoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudTrasladoBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SolicitudTrasladoRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private EstudianteRestClient estudianteRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private MatriculaRestClient matriculaRestClient;

    @Inject
    private ServicioEducativoRestClient restServicioEducativo;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private AnioLectivoRestClient restAnioLectivo;

    @Inject
    private ReglaEquivalenciaDetalleRestClient restEquivalenciaDetalle;

    @Inject
    private ReglasEquivalenciaRestClient reglaEquivalenciaClient;

    @Inject
    private SeleccionarServicioEducativoBean seleccionarServicioEducativo;

    @Inject
    @Param(name = "idSolTr")
    private Long solicitudId;

    @Inject
    @Param(name = "id_transaccion")
    private String idTransaccionFirma;

    private Long nieBuscar;
    private FiltroSolicitudTraslado filtro = new FiltroSolicitudTraslado();
    private SgSolicitudTraslado entidadEnEdicion;
    private List<SgSolicitudTraslado> listSolicitudTraslado = new ArrayList();
    private SgSede sedeSeleccionada;
    private SgServicioEducativo servicioEnEdicion;
    private SofisComboG<EnumEstadoSolicitudTraslado> comboEstado;
    private SofisComboG<SgMotivoTraslado> comboMotivos;
    private SofisComboG<SgAccionSolicitudTraslado> comboAcciones;
    private Boolean continuar = Boolean.TRUE;
    private SofisComboG<SgSeccion> comboSecciones;
    private Boolean opcAccion = Boolean.FALSE;
    private Boolean soloLecturaServicio = Boolean.FALSE;
    private Boolean permitirAcciones = Boolean.TRUE;
    private Boolean unicaSede = Boolean.FALSE;
    private SgAnioLectivo anioLectivoActual;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    SgMatricula matricula = new SgMatricula();

    public SolicitudTrasladoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            this.seleccionarServicioEducativo.setVerDeptoMun(Boolean.TRUE);
            validarAcceso();
            cargarCombos();

            //Buscar la sede asignada del usuario
            sedeSeleccionada = null;

            if (idTransaccionFirma != null) {
                this.confirmarFirma(idTransaccionFirma);
            } else {
                if (solicitudId != null && solicitudId > 0) {
                    this.actualizar(restClient.obtenerPorId(solicitudId));
                } else {
                    if (sessionBean.getSedeDefecto() != null) {
                        //Si el usuario tiene una unica sede, se selecciona por defecto.
                        sedeSeleccionada = sedeRestClient.obtenerPorId(sessionBean.getSedeDefecto().getSedPk());
                        unicaSede = Boolean.TRUE;
                    }
                }
            }
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SOLICITUD_TRASLADO)) {
            JSFUtils.redirectToIndex();
        }

    }

    public void buscarNIE() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.SOLICITAR_TRASLADO)) {
            JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_TRASLADO);
        }

        try {
            if (nieBuscar != null) {

                //Validar si  ya existe una solicitud no finalizada
                //Se considera que la solicitud está finalizada en los siguientes estados:CONFIRMADA, ANULADA, RECHAZADA
                FiltroSolicitudTraslado fsol = new FiltroSolicitudTraslado();
                fsol.setPerNie(nieBuscar);
                fsol.setSolicitudEnProceso(new EnumEstadoSolicitudTraslado[]{EnumEstadoSolicitudTraslado.CONFIRMADO,
                    EnumEstadoSolicitudTraslado.ANULADO,
                    EnumEstadoSolicitudTraslado.RECHAZADA});
                fsol.setIncluirCampos(new String[]{"sotPk", "sotVersion"});
                if (restClient.buscarTotal(fsol) == 0) {
                    FiltroMatricula fmat = new FiltroMatricula();
                    fmat.setNie(nieBuscar);
                    fmat.setIncluirCampos(new String[]{"matPk", "matVersion"});
                    fmat.setOrderBy(new String[]{"matPk"});
                    fmat.setAscending(new boolean[]{true});
                    fmat.setMatEstado(EnumMatriculaEstado.ABIERTO);
                    fmat.setSecurityOperation(null);
                    List<SgMatricula> matriculas = matriculaRestClient.buscar(fmat);

                    //Se obtiene la matricula actual del estudiante, para determinar el servicio educativo origen
                    if (matriculas.size() > 0) {
                        matricula = matriculaRestClient.obtenerPorId(matriculas.get(matriculas.size() - 1).getMatPk());

                        //Verificamos si hay una sede seleccionada por defecto.
                        if (sedeSeleccionada != null) {
                            if (matricula.getMatSeccion().getSecServicioEducativo().getSduSede().equals(sedeSeleccionada)) {
                                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SEDE_IGUAL_TRASLADO), "");
                                return;
                            }
                        }

                        entidadEnEdicion = new SgSolicitudTraslado();
                        entidadEnEdicion.setSotEstudiante(matricula.getMatEstudiante());
                        entidadEnEdicion.setSotSedeOrigen(matricula.getMatSeccion().getSecServicioEducativo().getSduSede());
                        entidadEnEdicion.setSotServicioEducativoOrigen(matricula.getMatSeccion().getSecServicioEducativo());
                        entidadEnEdicion.setSotUsuarioSolicitud(sessionBean.getEntidadUsuario());

                        this.seleccionarServicioEducativo.cargarSedePorDefecto();
                    } else {
                        FiltroEstudiante fe = new FiltroEstudiante();
                        fe.setNie(nieBuscar);
                        fe.setIncluirCampos(new String[]{"estVersion"});
                        fe.setMaxResults(1L);
                        List<SgEstudiante> est = estudianteRestClient.buscar(fe);

                        if (est.isEmpty()) {
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRARON_ESTUDIANTES), "");
                        } else {
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ESTUDIANTE_NO_TIENE_MATRICULA_ABIERTA), "");
                        }

                    }
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SOLICITUD_TRASLADO_ACTIVA), "");
                }
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_VACIO), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();

            comboMotivos = new SofisComboG(catalogoRestClient.buscarMotivoTraslado(fc), "motNombre");
            comboMotivos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumEstadoSolicitudTraslado> estados = new ArrayList(Arrays.asList(EnumEstadoSolicitudTraslado.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboSecciones = new SofisComboG();
            comboSecciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});

            List<SgAnioLectivo> listAnio = restAnioLectivo.buscar(fal);
            comboAnioLectivo = new SofisComboG(listAnio, "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (servicioEnEdicion != null) {
                if (entidadEnEdicion.getSotPk() == null) {
                    //Nueva solicitud

                    SgGradoPlanOrigenDestino obj = new SgGradoPlanOrigenDestino();
                    obj.getOrigen().setGrado(entidadEnEdicion.getSotServicioEducativoOrigen().getSduGrado().getGraPk());
                    obj.getOrigen().setPlanEstudio(matricula.getMatSeccion().getSecPlanEstudio().getPesPk());
                    obj.getOrigen().setProgramaEducativo(entidadEnEdicion.getSotServicioEducativoOrigen().getSduProgramaEducativo() != null ? entidadEnEdicion.getSotServicioEducativoOrigen().getSduProgramaEducativo().getPedPk() : null);
                    obj.getOrigen().setOpcion(entidadEnEdicion.getSotServicioEducativoOrigen().getSduOpcion() != null ? entidadEnEdicion.getSotServicioEducativoOrigen().getSduOpcion().getOpcPk() : null);

                    obj.getDestino().setGrado(servicioEnEdicion.getSduGrado().getGraPk());
                    obj.getDestino().setProgramaEducativo(servicioEnEdicion.getSduProgramaEducativo() != null ? servicioEnEdicion.getSduProgramaEducativo().getPedPk() : null);
                    obj.getDestino().setOpcion(servicioEnEdicion.getSduOpcion() != null ? servicioEnEdicion.getSduOpcion().getOpcPk() : null);

                    //En esta etapa no esta la seccion destino seleccionada. Por eso no tenemos plan estudio para comparar
                    obj.setValidarPlanEstudioDestino(Boolean.FALSE);

                    //Validar las equivalencias
                    if (!reglaEquivalenciaClient.cumple(obj)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_MATRICULA_NO_CUMPLE_EQUIVALENCIA), "");
                        return;
                    }

                    entidadEnEdicion.setSotSedeSolicitante(servicioEnEdicion.getSduSede());
                    entidadEnEdicion.setSotServicioEducativoSolicitado(servicioEnEdicion);
                    entidadEnEdicion.setSotMotivoTraslado(comboMotivos.getSelectedT());

                    entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_TRASLADO);

                } else {
                    //Seguimiento de la solicitud
                    if (comboAcciones.getSelectedT() == null) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ACCION_VACIO), "");
                        return;
                    }

                    SgAccionSolicitudTraslado accion = comboAcciones.getSelectedT();
                    if (EnumEstadoSolicitudTraslado.CONFIRMADO.equals(accion.getAccEstadoDestino())) {
                        confirmar();
                    } else {
                        entidadEnEdicion.setSotEstado(accion.getAccEstadoDestino());

                        entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_TRASLADO);
                    }
                }

            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SERVICIO_EDUCATIVO_VACIO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void confirmar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            SgConfirmacionSolicitudTraslado conf = new SgConfirmacionSolicitudTraslado();

            if (entidadEnEdicion.getSotConfirmacion() != null) {
                conf = entidadEnEdicion.getSotConfirmacion();
            }

            conf.setSotFechaTraslado(entidadEnEdicion.getSotFechaTraslado());
            if (comboSecciones.getSelectedT() != null) {
                conf.setSotSeccion(comboSecciones.getSelectedT());
            }
            conf.setSotSolicitudTrasladoPk(entidadEnEdicion.getSotPk());
            conf.setSotResolucion(entidadEnEdicion.getSotResolucion());

            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
            conf.setSotTransactionReturnUrl(baseURL + "/pp/" + ConstantesPaginas.SOLICITUD_TRASLADO);
            conf = this.restClient.preconfirmar(conf);

            if (!StringUtils.isBlank(conf.getSotTransactionSignatureUrl())) {
                PrimeFaces.current().executeScript("window.location.replace(\"" + conf.getSotTransactionSignatureUrl() + "\");");
            } else {
                //Firma deshabilitada
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_TRASLADO);
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void confirmarFirma(String transactionId) {
        try {
            SgConfirmacionSolicitudTraslado conf = this.restClient.confirmar(transactionId);
            solicitudId = conf.getSotSolicitudTrasladoPk();
            this.actualizar(restClient.obtenerPorId(solicitudId));
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void servicioEducativoSeleccionar(SgServicioEducativo ser) {
        try {
            servicioEnEdicion = ser;
            if (servicioEnEdicion != null) {
                //Validar si es la misma sede
                if (servicioEnEdicion.getSduSede().equals(entidadEnEdicion.getSotSedeOrigen())) {
                    servicioEnEdicion = null;
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SEDE_IGUAL_TRASLADO), "");
                }
            }
            PrimeFaces.current().ajax().update("form:btnGuardarSolicitud");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboEstado.setSelected(-1);
        comboMotivos.setSelected(-1);

    }

    public void limpiar() {
        filtro = new FiltroSolicitudTraslado();
        if (!unicaSede) {
            sedeSeleccionada = null;
        }
        limpiarCombos();
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedNombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedNombre", "sedVersion", "sedTipo"});
            return sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void actualizar(SgSolicitudTraslado var) {
        try {
            limpiarCombos();
            entidadEnEdicion = (SgSolicitudTraslado) SerializationUtils.clone(var);
            comboMotivos.setSelectedT(entidadEnEdicion.getSotMotivoTraslado());
            servicioEnEdicion = entidadEnEdicion.getSotServicioEducativoSolicitado();

            FiltroSedes filtroSed = new FiltroSedes();
            filtroSed.setSedHabilitado(Boolean.TRUE);

            //Validar si cuenta con los permisos necesarios para ejecutar una acción
            switch (entidadEnEdicion.getSotEstado()) {
                case PENDIENTE:
                    filtroSed.setSedPk(entidadEnEdicion.getSotSedeOrigen().getSedPk());
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_TRASLADO)
                            || sedeRestClient.buscarTotal(filtroSed) == 0) {
                        this.permitirAcciones = Boolean.FALSE;
                        this.continuar = Boolean.FALSE;
                    }
                    break;
                case PENDIENTE_RESOLUCION:
                    filtroSed.setSedPk(entidadEnEdicion.getSotSedeOrigen().getSedPk());
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_TRASLADO_ESPECIAL)
                            || sedeRestClient.buscarTotal(filtroSed) == 0) {
                        this.permitirAcciones = Boolean.FALSE;
                        this.continuar = Boolean.FALSE;
                    }
                    break;
                case AUTORIZADA:
                    filtroSed.setSedPk(entidadEnEdicion.getSotSedeSolicitante().getSedPk());
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CONFIRMAR_TRASLADO)
                            || sedeRestClient.buscarTotal(filtroSed) == 0) {
                        this.permitirAcciones = Boolean.FALSE;
                        this.continuar = Boolean.FALSE;
                    }
                    break;
            }

            //Consultar las acciones
            FiltroAccion facc = new FiltroAccion();
            facc.setAccEstadoOrigen(entidadEnEdicion.getSotEstado());
            List<SgAccionSolicitudTraslado> acciones = catalogoRestClient.buscarAcciones(facc);
            comboAcciones = new SofisComboG(acciones, "accNombreAccion");
            comboAcciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            soloLecturaServicio = Boolean.TRUE;

            if (acciones.size() == 1) {
                if (acciones.get(0).getAccEstadoOrigen().equals(acciones.get(0).getAccEstadoDestino())) {
                    continuar = Boolean.FALSE;
                }
                switch (acciones.get(0).getAccEstadoDestino()) {
                    case CONFIRMADO:
                        opcAccion = Boolean.TRUE;
                        comboSecciones.setSelectedT(entidadEnEdicion.getSotSeccion());
                        break;
                }
            }
            this.seleccionarServicioEducativo.cargarServicioEducativo(servicioEnEdicion);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarAniosLectivos() {
        try {
            //Cargar combo año lectivo
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});
            fal.setMaxResults(1L);

            List<SgAnioLectivo> listAnio = restAnioLectivo.buscar(fal);

            if (!listAnio.isEmpty()) {
                anioLectivoActual = listAnio.get(0);
                seleccionarAnioLectivo();
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_HAY_ANIO_LECTIVO_HABILITADO), "");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAnioLectivo() {
        try {
            comboSecciones = new SofisComboG();
            comboSecciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboAnioLectivo.getSelectedT() != null) {
                FiltroSeccion fSeccion = new FiltroSeccion();
                fSeccion.setSecServicioEducativoFk(servicioEnEdicion.getSduPk());
                fSeccion.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT().getAlePk());
                fSeccion.setSecEstado(EnumSeccionEstado.ABIERTA);

                fSeccion.setOrderBy(new String[]{"secNombre"});
                fSeccion.setAscending(new boolean[]{true});
                fSeccion.setIncluirCampos(new String[]{"secNombre", "secVersion", "secJornadaLaboral.jlaNombre"});

                List<SgSeccion> secciones = seccionClient.buscar(fSeccion);

                if (!secciones.isEmpty()) {
                    comboSecciones = new SofisComboG(secciones, "nombreSeccion");
                    comboSecciones.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                } else {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_HAY_SECCIONES_PARA_LOS_DATOS_SELECCIONADOS), "");
                }

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getSotPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAccion() {
        if (comboAcciones.getSelectedT() != null) {
            switch (comboAcciones.getSelectedT().getAccEstadoDestino()) {
                case CONFIRMADO:
                    this.cargarAniosLectivos();
                    entidadEnEdicion.setSotFechaTraslado(LocalDate.now());
                    opcAccion = Boolean.TRUE;
                    break;
                case ANULADO:
                    opcAccion = Boolean.FALSE;
                    break;
            }
        } else {
            opcAccion = Boolean.FALSE;
        }
    }

    public SofisComboG<SgSeccion> getComboSecciones() {
        return comboSecciones;
    }

    public void setComboSecciones(SofisComboG<SgSeccion> comboSecciones) {
        this.comboSecciones = comboSecciones;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SolicitudTrasladoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SolicitudTrasladoRestClient restClient) {
        this.restClient = restClient;
    }

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
    }

    public EstudianteRestClient getEstudianteRestClient() {
        return estudianteRestClient;
    }

    public void setEstudianteRestClient(EstudianteRestClient estudianteRestClient) {
        this.estudianteRestClient = estudianteRestClient;
    }

    public CatalogosRestClient getCatalogoRestClient() {
        return catalogoRestClient;
    }

    public void setCatalogoRestClient(CatalogosRestClient catalogoRestClient) {
        this.catalogoRestClient = catalogoRestClient;
    }

    public MatriculaRestClient getMatriculaRestClient() {
        return matriculaRestClient;
    }

    public void setMatriculaRestClient(MatriculaRestClient matriculaRestClient) {
        this.matriculaRestClient = matriculaRestClient;
    }

    public Long getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(Long solicitudId) {
        this.solicitudId = solicitudId;
    }

    public Long getNieBuscar() {
        return nieBuscar;
    }

    public void setNieBuscar(Long nieBuscar) {
        this.nieBuscar = nieBuscar;
    }

    public FiltroSolicitudTraslado getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSolicitudTraslado filtro) {
        this.filtro = filtro;
    }

    public SgSolicitudTraslado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudTraslado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgSolicitudTraslado> getListSolicitudTraslado() {
        return listSolicitudTraslado;
    }

    public void setListSolicitudTraslado(List<SgSolicitudTraslado> listSolicitudTraslado) {
        this.listSolicitudTraslado = listSolicitudTraslado;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SgServicioEducativo getServicioEnEdicion() {
        return servicioEnEdicion;
    }

    public void setServicioEnEdicion(SgServicioEducativo servicioEnEdicion) {
        this.servicioEnEdicion = servicioEnEdicion;
    }

    public SofisComboG<EnumEstadoSolicitudTraslado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoSolicitudTraslado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgMotivoTraslado> getComboMotivos() {
        return comboMotivos;
    }

    public void setComboMotivos(SofisComboG<SgMotivoTraslado> comboMotivos) {
        this.comboMotivos = comboMotivos;
    }

    public SofisComboG<SgAccionSolicitudTraslado> getComboAcciones() {
        return comboAcciones;
    }

    public void setComboAcciones(SofisComboG<SgAccionSolicitudTraslado> comboAcciones) {
        this.comboAcciones = comboAcciones;
    }

    public Boolean getContinuar() {
        return continuar;
    }

    public void setContinuar(Boolean continuar) {
        this.continuar = continuar;
    }

    public Boolean getOpcAccion() {
        return opcAccion;
    }

    public void setOpcAccion(Boolean opcAccion) {
        this.opcAccion = opcAccion;
    }

    public Boolean getSoloLecturaServicio() {
        return soloLecturaServicio;
    }

    public void setSoloLecturaServicio(Boolean soloLecturaServicio) {
        this.soloLecturaServicio = soloLecturaServicio;
    }

    public Boolean getPermitirAcciones() {
        return permitirAcciones;
    }

    public void setPermitirAcciones(Boolean permitirAcciones) {
        this.permitirAcciones = permitirAcciones;
    }

    public ServicioEducativoRestClient getRestServicioEducativo() {
        return restServicioEducativo;
    }

    public void setRestServicioEducativo(ServicioEducativoRestClient restServicioEducativo) {
        this.restServicioEducativo = restServicioEducativo;
    }

    public Boolean getUnicaSede() {
        return unicaSede;
    }

    public void setUnicaSede(Boolean unicaSede) {
        this.unicaSede = unicaSede;
    }

    public SgAnioLectivo getAnioLectivoActual() {
        return anioLectivoActual;
    }

    public void setAnioLectivoActual(SgAnioLectivo anioLectivoActual) {
        this.anioLectivoActual = anioLectivoActual;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }
}
