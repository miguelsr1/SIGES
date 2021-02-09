/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCabezalEscolaridadEstudiante;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgConfirmacionPromocion;
import sv.gob.mined.siges.web.dto.SgDatosCalculoCalificacionesPromocion;
import sv.gob.mined.siges.web.dto.SgEscolaridadEstudiante;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPorcentajeAsistenciasResponse;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConfirmacionPromocion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ConfirmacionPromocionRestClient;
import sv.gob.mined.siges.web.restclient.EscolaridadEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PromocionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PromocionBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long calificacionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "id_transaccion")
    private String idTransaccionFirma;

    @Inject
    private CalificacionRestClient restClient;

    @Inject
    private CalificacionEstudianteRestClient calificacionEstudianteRestClient;

    @Inject
    private ConfirmacionPromocionRestClient confirmacionPromocionRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstudioRestClient;

    @Inject
    private MatriculaRestClient restMatricula;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private EscolaridadEstudianteRestClient restEscolaridadEstudiante;

    @Inject
    private CalendarioRestClient calendarioRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgCalificacionCE entidadEnEdicion = new SgCalificacionCE();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalificacionDataModel calificacionLazyModel;

    private SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudio;
    private SofisComboG<SgCalificacion>[] comboCalificacion;
    private SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion;
    private SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario;
    private SofisComboG<String> comboPeriodoCalendario;
    private SofisComboG<SgRangoFecha> comboRangoFecha;
    private List<SgEstudiante> listEstudiantes;
    private LocalDate fechaRegistro;
    private List<SgCalificacionEstudiante> calificacionesEstList;
    private SgEscalaCalificacion escalaCalificacion;
    private SgSeccion seccionEnEdicion;
    private Boolean escalaNumerica = Boolean.FALSE;
    private Boolean soloLectura = Boolean.FALSE;
    private List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList;
    private SgComponentePlanGrado componentePlanGrado;
    private Integer cantidadPruebaComponentePlanGrado = 0;
    private List<SelectItem> comboPeriodos;
    private Integer comboPeriodoSeleccionado;
    private List<SgPorcentajeAsistenciasResponse> listPorcentajeAsistencia;
    private SgRelGradoPlanEstudio relGradoPlanEstudio;
    private List<SgCalificacionEstudiante> listCalificacionEstGrado;
    private Boolean habilitadoCierreAnio = Boolean.FALSE;
    private EnumPromovidoCalificacion[] promocionesEstudiante;
    private String mensajeWarningPromocion = null;
    private String mensajeWarningCierreAnio = null;
    private SgCalendario calendarioSede;
    private Boolean existenPromocionesCalculadas = Boolean.FALSE;
    private Boolean ejecucionAsincronica = Boolean.FALSE;
    private HashMap<SgEstudiante, String> errorAlCalificar = new HashMap<>();
    private String errorAlCalificarDeEst;
    private SgCalificacionEstudiante calificacionEstudiantePasadoAPendiente;
    private HashMap<SgEstudiante, SgCalificacionEstudiante> estudiantePuedeModificarPendiente = new HashMap<>();
    
    //Firma
    private SgConfirmacionPromocion conf;
    private Boolean firmaPromocionActivada = Boolean.FALSE;

    public PromocionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            buscarConfiguracion();
            if (idTransaccionFirma != null) {
                this.confirmarFirma(idTransaccionFirma);
            } else {
                if (calificacionId != null && calificacionId > 0) {
                    this.actualizar(restClient.obtenerPorId(calificacionId));
                    soloLectura = editable != null ? !editable : soloLectura;

                } else {
                    this.agregar();
                }
            }
            validarAcceso();

        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, ce.getMessage(), ce);
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public LocalDate obtenerFechaCierreMatriculasPorDefecto(SgSeccion sec) {
        try {
            Boolean esCohorteInternacional = Boolean.FALSE;

            SgTipoCalendario tip = sec.getSecServicioEducativo().getSduSede().getSedTipoCalendario();
            Integer anio = sec.getSecAnioLectivo().getAleAnio();
            Boolean anual = Boolean.TRUE;

            if (sec.getSecTipoPeriodo() != null && EnumTipoPeriodoSeccion.COHORTE.equals(sec.getSecTipoPeriodo())
                    && sec.getSecNumeroPeriodo().equals(1)) {
                anual = Boolean.FALSE;
            }

            FiltroCodiguera fcn = new FiltroCodiguera();
            if (anual) {
                if (Constantes.CALENDARIO_INTERNACIONAL.equals(tip.getTceCodigo())) {
                    fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_INTERNACIONAL);
                    anio++;
                } else {
                    fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_NACIONAL);
                }
            } else {
                if (Constantes.CALENDARIO_INTERNACIONAL.equals(tip.getTceCodigo())) {
                    fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_INTERNACIONAL_PRIMER_COHORTE);
                    esCohorteInternacional = Boolean.TRUE;

                } else {
                    fcn.setCodigoExacto(Constantes.CONFIG_DIA_MES_CIERRE_MATRICULA_NACIONAL_PRIMER_COHORTE);
                }
            }
            List<SgConfiguracion> cnfs;

            cnfs = restCatalogo.buscarConfiguracion(fcn);

            if (cnfs.isEmpty()) {
                BusinessException be = new BusinessException();
                be.addError("NO_SE_ENCONTRO_CONFIG_DIA_MES_CIERRE_MATRICULA");
                throw be;
            }
            SgConfiguracion cnf = cnfs.get(0);
            String[] diaMes = cnf.getConValor().split("/");
            if (esCohorteInternacional) {
                LocalDate fecha = LocalDate.of(anio, Integer.parseInt(diaMes[1]), Integer.parseInt(diaMes[0]));

                FiltroCalendario fc = new FiltroCalendario();
                fc.setTipoCalendarioPk(tip.getTcePk());
                fc.setAnioLectivo(anio);
                fc.setIncluirCampos(new String[]{"calFechaInicio", "calFechaFin"});
                List<SgCalendario> listCal = calendarioRestClient.buscar(fc);
                if (!(fecha.compareTo(listCal.get(0).getCalFechaInicio()) > 0 && fecha.compareTo(listCal.get(0).getCalFechaFin()) < 0)) {
                    anio++;
                }
            }

            return LocalDate.of(anio, Integer.parseInt(diaMes[1]), Integer.parseInt(diaMes[0]));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return LocalDate.now();
    }

    public void buscarConfiguracion() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.MENSAJE_CIERRE_SECCION);
            List<SgConfiguracion> conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                mensajeWarningCierreAnio = conf.get(0).getConValor();
            }

            fc.setCodigo(Constantes.MENSAJE_PROMOCION);
            conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                mensajeWarningPromocion = conf.get(0).getConValor();
            }

            fc.setCodigo(Constantes.PROCESAR_PROMOCIONES_ASINC);
            conf = restCatalogo.buscarConfiguracion(fc);
            if (!conf.isEmpty()) {
                if (conf.get(0).getConValor().equals("1")) {
                    ejecucionAsincronica = Boolean.TRUE;
                }
            }
            
            SgConfiguracionFirmaElectronica cnf = restCatalogo.buscarConfiguracionFirmaElectronicaPorCodigo(Constantes.CONFIG_FIRMA_ELECTRONICA_CONFIRMACION_PROMOCION);
            if (cnf != null){
                firmaPromocionActivada = cnf.getConActivada();
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_CALIFICACIONES)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getCalPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CALIFICACIONES)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CALIFICACIONES)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public Boolean calcularNotaPromocion(Boolean mostrarMsjExito) {
        try {
            listPorcentajeAsistencia = new ArrayList();
            if (seccionEnEdicion != null) {
                if (seccionEnEdicion.getSecPlanEstudio() != null && seccionEnEdicion.getSecServicioEducativo().getSduGrado() != null) {
                    FiltroRelGradoPlanEstudio filtroRelGraPlan = new FiltroRelGradoPlanEstudio();
                    filtroRelGraPlan.setRgpGrado(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
                    filtroRelGraPlan.setRgpPlanEstudio(seccionEnEdicion.getSecPlanEstudio().getPesPk());
                    filtroRelGraPlan.setIncluirCampos(new String[]{
                        "rgpFormulaFk.fomPk",
                        "rgpFormulaFk.fomTextoLargo"
                    });
                    List<SgRelGradoPlanEstudio> listRelGra = relGradoPlanEstudioRestClient.buscar(filtroRelGraPlan);
                    if (listRelGra.isEmpty()) {
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GRADO_SIN_FORMULA), "");
                        return Boolean.FALSE;
                    }
                    if (listRelGra.get(0).getRgpFormulaFk() == null) {
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GRADO_SIN_FORMULA), "");
                        return Boolean.FALSE;
                    }

                    SgDatosCalculoCalificacionesPromocion calculoPromocion = new SgDatosCalculoCalificacionesPromocion();
                    calculoPromocion.setSeccion(seccionEnEdicion);
                    if (ejecucionAsincronica) {
                        calculoPromocion.setEsAsnicronica(Boolean.TRUE);
                    } else {
                        calculoPromocion.setEsAsnicronica(Boolean.FALSE);
                    }

                    Boolean respuesta = restClient.calcularPromocion(calculoPromocion);

                    if (ejecucionAsincronica) {
                        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.PROMOCION_EXITOSAMENTE_COLA), "");
                    } else {
                        if(BooleanUtils.isTrue(mostrarMsjExito)){
                            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                        }
                    }

                    cargarCalificacionSeccion(seccionEnEdicion);

                    relGradoPlanEstudio = listRelGra.get(0);

                    if (ejecucionAsincronica) {
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }

                } else {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCION_SIN_PLAN_ESTUDIO), "");
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return Boolean.FALSE;
    }

    public void buscarEstudiantes() throws Exception {
        if (seccionEnEdicion.getSecPk() != null) {
            FiltroMatricula filtroMat = new FiltroMatricula();
            filtroMat.setSecPk(seccionEnEdicion.getSecPk());
            filtroMat.setMatRetirada(Boolean.FALSE);
            filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
                "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
                "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
                "matValidacionAcademica", "matProvisional",
                "matEstudiante.estVersion"});
            filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido",
                "matEstudiante.estPersona.perPrimerNombre"});
            filtroMat.setAscending(new boolean[]{true, true, true});
            List<SgMatricula> matriculas = restMatricula.buscar(filtroMat);
            List<SgEstudiante> estudiantes = matriculas.stream()
                    .map(c -> {
                        c.getMatEstudiante().setEstUltimaMatricula(c);
                        return c.getMatEstudiante();
                    }).distinct()
                    .collect(Collectors.toList());
            listEstudiantes = estudiantes;

        }

    }

    public String colorRow(SgEstudiante est) {

        if (est != null) {
            String resultado = obtenerCalificacionEstudiante(est);

            if (resultado != null) {
                if (resultado.equals("Promovido")) {
                    return "aprobado";
                } else {
                    if (resultado.equals("No promovido")) {
                        return "reprobado";
                    }
                }
            }
        }
        return null;
    }

    public void cierreAnioHabilitado() {
        habilitadoCierreAnio = Boolean.FALSE;

        if (listCalificacionEstGrado != null && !listCalificacionEstGrado.isEmpty()) {
            List<SgCalificacionEstudiante> listCal = listCalificacionEstGrado.stream().filter(c -> EnumPromovidoCalificacion.PENDIENTE.equals(c.getCaePromovidoCalificacion())).collect(Collectors.toList());
            if (listCal.isEmpty()) {
                habilitadoCierreAnio = Boolean.TRUE;
            }
        }
    }

    public void confirmar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            if (conf == null) {
                conf = new SgConfirmacionPromocion();
            }
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
            conf.setCprTransactionReturnUrl(baseURL + "/pp/promocion");
            conf.setCprSeccion(this.seccionEnEdicion);
            conf.setCprCabezalCalificacion(entidadEnEdicion);
            conf = this.confirmacionPromocionRestClient.preconfirmar(conf);

            if (!StringUtils.isBlank(conf.getCprTransactionSignatureUrl())) {
                PrimeFaces.current().executeScript("window.location.replace(\"" + conf.getCprTransactionSignatureUrl() + "\");");
            } else {
                //Firma deshabilitada
                throw new IllegalStateException();
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
            conf = this.confirmacionPromocionRestClient.confirmar(transactionId);
            FiltroConfirmacionPromocion fil = new FiltroConfirmacionPromocion();
            fil.setIncluirCampos(new String[]{"cprCabezalCalificacion.calPk", "cprArchivoFirmado.achPk"});
            fil.setCprPk(conf.getCprPk());
            List<SgConfirmacionPromocion> confs = confirmacionPromocionRestClient.buscar(fil);
            if (!confs.isEmpty()) {
                SgConfirmacionPromocion conf = confs.get(0);
                this.actualizar(restClient.obtenerPorId(conf.getCprCabezalCalificacion().getCalPk()));
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cerrarAnioPromocion() {
        try {
            if (!calcularNotaPromocion(Boolean.FALSE)) {
                return;
            }
            if (listCalificacionEstGrado != null && !listCalificacionEstGrado.isEmpty()) {
                List<Long> estudiantesPk = listCalificacionEstGrado.stream().map(c -> c.getCaeEstudiante().getEstPk()).collect(Collectors.toList());

                FiltroEscolaridadEstudiante fee = new FiltroEscolaridadEstudiante();
                fee.setEstudiantesPk(estudiantesPk);
                fee.setAnioPk(seccionEnEdicion.getSecAnioLectivo().getAlePk());
                fee.setServicioEducativoPk(seccionEnEdicion.getSecServicioEducativo().getSduPk());
                fee.setIncluirCampos(new String[]{"escEstudiante.estPersona.perNie", "escEstudiante.estPk",
                    "escEstudiante.estPersona.perPrimerNombre", "escEstudiante.estPersona.perSegundoNombre", "escEstudiante.estPersona.perNombreBusqueda",
                    "escEstudiante.estPersona.perPrimerApellido", "escEstudiante.estPersona.perSegundoApellido", "escEstudiante.estPersona.perFechaNacimiento",
                    "escEstudiante.estUltimaMatricula.matPk", "escEstudiante.estUltimaMatricula.matVersion",
                    "escEstudiante.estVersion", "escServicioEducativo.sduPk", "escServicioEducativo.sduVersion", "escAnio.alePk", "escAnio.aleVersion", "escResultado",
                    "escMatriculaFk.matPk", "escMatriculaFk.matVersion",
                    "escVersion", "escPk", "escAsistencias", "escInasistencias"});
                List<SgEscolaridadEstudiante> escolaridadList = restEscolaridadEstudiante.buscar(fee);

                SgCabezalEscolaridadEstudiante cabEsc = new SgCabezalEscolaridadEstudiante();
                cabEsc.setSeccion(seccionEnEdicion);
                cabEsc.setCalificacion(listCalificacionEstGrado.get(0).getCaeCalificacion());
                cabEsc.setCerrarSeccion(Boolean.FALSE);

                List<SgCalificacionEstudiante> calEstPendiente = listCalificacionEstGrado.stream().filter(c -> EnumPromovidoCalificacion.PENDIENTE.equals(c.getCaePromovidoCalificacion())).collect(Collectors.toList());
                if (calEstPendiente.isEmpty() && Integer.compare(listEstudiantes.size(), listCalificacionEstGrado.size()) == 0) {
                    cabEsc.setCerrarSeccion(Boolean.TRUE);
                }

                List<SgEscolaridadEstudiante> listEscolaridad = new ArrayList();
                for (SgCalificacionEstudiante calEl : listCalificacionEstGrado) {
                    if (!EnumPromovidoCalificacion.PENDIENTE.equals(calEl.getCaePromovidoCalificacion())) {
                        List<SgEscolaridadEstudiante> escExistente = escolaridadList.stream().filter(c -> c.getEscEstudiante().getEstPk().equals(calEl.getCaeEstudiante().getEstPk())).collect(Collectors.toList());

                        if (escExistente.isEmpty()) {
                            SgEscolaridadEstudiante escolaridadEst = new SgEscolaridadEstudiante();
                            escolaridadEst.setEscEstudiante(calEl.getCaeEstudiante());
                            escolaridadEst.setEscAnio(seccionEnEdicion.getSecAnioLectivo());
                            escolaridadEst.setEscServicioEducativo(seccionEnEdicion.getSecServicioEducativo());
                            escolaridadEst.setEscResultado(calEl.getCaePromovidoCalificacion());
                            escolaridadEst.setEscMatriculaFk(calEl.getCaeEstudiante().getEstUltimaMatricula());
                            escolaridadEst.setEscCreadoCierre(Boolean.TRUE);
                            listEscolaridad.add(escolaridadEst);
                        } else {
                            SgEscolaridadEstudiante esc = escExistente.get(0);
                            esc.setEscResultado(calEl.getCaePromovidoCalificacion());
                            esc.setEscCreadoCierre(Boolean.TRUE);
                            listEscolaridad.add(esc);
                        }
                    }
                }

                if (!listEscolaridad.isEmpty()) {
                    cabEsc.setEscolaridadEstudianteList(listEscolaridad);
                    cabEsc = restEscolaridadEstudiante.guardarEscolaridades(cabEsc);
                    if (cabEsc.getCerrarSeccion()) {
                        seccionEnEdicion.setSecEstado(EnumSeccionEstado.CERRADA);
                        entidadEnEdicion.setCalCerrado(Boolean.TRUE);
                        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ANIO_CERRADO_EXITO), "");
                    } else {
                        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.MATRICULAS_CERRADAS_PROMOCION), "");
                    }

                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ESTUDIANTE_PROMOVIDO), "");
                }
                cargarCalificacionSeccion(seccionEnEdicion);
            }
        } catch (BusinessException be) {
    
            JSFUtils.agregarMensajesError(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgCalificacionCE();
    }

    public void actualizar(SgCalificacionCE cal) {
        try {
            if (cal == null) {
                agregar();
            } else {
                entidadEnEdicion = cal;
                seccionEnEdicion = cal.getCalSeccion();
                cargarCalificacionSeccion(seccionEnEdicion);
                
                if (this.conf == null){
                    FiltroConfirmacionPromocion fil = new FiltroConfirmacionPromocion();
                    fil.setSeccionFk(seccionEnEdicion.getSecPk());
                    
                    List<SgConfirmacionPromocion> confs = confirmacionPromocionRestClient.buscar(fil);
                    if (!confs.isEmpty()) {
                        conf = confs.get(0);      
                    }
                
                }

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCalificacionSeccion(SgSeccion sec) {
        try {
            errorAlCalificar = new HashMap<>();
            estudiantePuedeModificarPendiente = new HashMap<>();
            promocionesEstudiante = null;
            calendarioSede = null;
            if (sec != null) {

                seccionEnEdicion = sec;

                FiltroCalendario fc = new FiltroCalendario();
                fc.setTipoCalendarioPk(seccionEnEdicion.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());
                fc.setAnioLectivoFk(seccionEnEdicion.getSecAnioLectivo().getAlePk());
                fc.setHabilitado(Boolean.TRUE);
                fc.setAscending(new boolean[]{false});
                fc.setOrderBy(new String[]{"calAnioLectivo.aleAnio"});
                fc.setIncluirCampos(new String[]{"calAnioLectivo.aleAnio", "calPermiteMatricularSiguienteAnio", "calPermiteCierreAnio"});
                List<SgCalendario> cal = calendarioRestClient.buscarConCache(fc);
                if (!cal.isEmpty()) {
                    calendarioSede = cal.get(0);
                }

                buscarEstudiantes();
                listCalificacionEstGrado = new ArrayList();
                if (!listEstudiantes.isEmpty()) {
                    FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
                    filtroCalEst.setIncluirCampos(new String[]{
                        "caePk",
                        "caePromovidoCalificacion",
                        "caeVersion",
                        "caeCalificacion.calPk",
                        "caeCalificacion.calVersion",
                        "caeCalificacion.calSeccion.secPk",
                        "caeCalificacion.calSeccion.secVersion",
                        "caeEstudiante.estUltimaMatricula.matSeccion.secPk",
                        "caeEstudiante.estUltimaMatricula.matEstado",
                        "caeEstudiante.estPk",
                        "caeEstudiante.estVersion",
                        "caeEstudiante.estUltimaMatricula.matPk",
                        "caeEstudiante.estUltimaMatricula.matVersion",
                        "caeInfoProcesamientoCalificacionEstFk.ipePk",
                        "caeInfoProcesamientoCalificacionEstFk.ipeError",
                        "caeInfoProcesamientoCalificacionEstFk.ipeVersion",
                        "caeInfoProcesamientoCalificacionEstFk.ipePromocionPendiente"
                    });

                    filtroCalEst.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                        EnumTiposPeriodosCalificaciones.GRA}));
                    filtroCalEst.setCaeEstudiantesPk(listEstudiantes.stream().map(e -> e.getEstPk()).collect(Collectors.toList()));
                    filtroCalEst.setAnioLectivoPk(seccionEnEdicion.getSecAnioLectivo().getAlePk());
                    filtroCalEst.setCaeGradoFk(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
                    //Secciones pueden ser semestrales
                    filtroCalEst.setCaeTipoPeriodo(seccionEnEdicion.getSecTipoPeriodo());
                    filtroCalEst.setCaeNumeroPeriodo(seccionEnEdicion.getSecNumeroPeriodo());

                    listCalificacionEstGrado = calificacionEstudianteRestClient.buscar(filtroCalEst);

                    existenPromocionesCalculadas = Boolean.FALSE;
                    promocionesEstudiante = new EnumPromovidoCalificacion[listEstudiantes.size()];
                    for (SgEstudiante est : listEstudiantes) {
                        List<SgCalificacionEstudiante> calEst = listCalificacionEstGrado.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                        if (calEst.isEmpty()) {
                            promocionesEstudiante[listEstudiantes.indexOf(est)] = EnumPromovidoCalificacion.PENDIENTE;
                        } else {
                            promocionesEstudiante[listEstudiantes.indexOf(est)] = calEst.get(0).getCaePromovidoCalificacion();
                            if (!EnumPromovidoCalificacion.PENDIENTE.equals(calEst.get(0).getCaePromovidoCalificacion())) {

                                if (calEst.get(0).getCaeEstudiante().getEstUltimaMatricula().getMatSeccion().getSecPk().equals(seccionEnEdicion.getSecPk())
                                        && (calEst.get(0).getCaeEstudiante().getEstUltimaMatricula().getMatEstado().equals(EnumMatriculaEstado.ABIERTO)
                                        || (calEst.get(0).getCaeEstudiante().getEstUltimaMatricula().getMatEstado().equals(EnumMatriculaEstado.CERRADO)
                                        && calEst.get(0).getCaePromovidoCalificacion().equals(EnumPromovidoCalificacion.NO_PROMOVIDO)))) {
                                    //PUEDE 'MODIFICAR PENDIENTE' LOS ESTUDIANTES CON MATRICULA ABIERTA DE LA SECCIÓN ACTUAL
                                    //O CERRADA CON CALIFICACION REPROBADO
                                    //Y QUE TIENE NOTA DISTINTO DE PENDIENTE
                                    if (!estudiantePuedeModificarPendiente.containsKey(est)) {
                                        estudiantePuedeModificarPendiente.put(est, calEst.get(0));
                                    }
                                }
                                existenPromocionesCalculadas = Boolean.TRUE;
                            } else {
                                if (calEst.get(0).getCaeInfoProcesamientoCalificacionEstFk() != null && BooleanUtils.isTrue(calEst.get(0).getCaeInfoProcesamientoCalificacionEstFk().getIpePromocionPendiente())) {
                                    //LOS QUE SON PENDIENTES PORQUE SE LOS MARCÓ COMO PENDIENTES SE AGREGAN AL HASH PARA PODER DESMARCARLOS
                                    if (!estudiantePuedeModificarPendiente.containsKey(est)) {
                                        estudiantePuedeModificarPendiente.put(est, calEst.get(0));
                                    }
                                }
                            }
                            if (calEst.get(0).getCaeInfoProcesamientoCalificacionEstFk() != null) {

                                if (!StringUtils.isBlank(calEst.get(0).getCaeInfoProcesamientoCalificacionEstFk().getIpeError())) {
                                    if (!errorAlCalificar.containsKey(est)) {
                                        errorAlCalificar.put(est, calEst.get(0).getCaeInfoProcesamientoCalificacionEstFk().getIpeError());
                                    }
                                }
                            }

                        }

                    }
                }
                cierreAnioHabilitado();

                FiltroCalificacion fcal = new FiltroCalificacion();
                fcal.setSecPk(seccionEnEdicion.getSecPk());
                fcal.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.GRA);
                fcal.setIncluirCampos(new String[]{"calComponentePlanEstudio.cpePk",
                    "calComponentePlanEstudio.cpeTipo",
                    "calComponentePlanEstudio.cpeVersion",
                    "calSeccion.secPk",
                    "calSeccion.secVersion",
                    "calFecha",
                    "calVersion",
                    "calTipoPeriodoCalificacion",
                    "calCerrado",
                    "calEstadoProcesamientoPromocion",
                    "calInfoProcesamientoCalificacionFk.ipcPk",
                    "calInfoProcesamientoCalificacionFk.ipcError",
                    "calInfoProcesamientoCalificacionFk.ipcVersion"});
                List<SgCalificacionCE> listcal = restClient.buscar(fcal);

                if (!listcal.isEmpty()) {
                    entidadEnEdicion = listcal.get(0);
                    entidadEnEdicion.setCalSeccion(seccionEnEdicion);
                    entidadEnEdicion.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.GRA);

                    entidadEnEdicion.setCalFecha(LocalDate.now());

//                    if (!ejecucionAsincronica) {
//                        entidadEnEdicion.setCalEstadoProcesamientoPromocion(null);
//                    }
                }
                if (seccionEnEdicion.getSecFechaCierreSeccion() == null) {
                    seccionEnEdicion.setSecFechaCierreSeccion(obtenerFechaCierreMatriculasPorDefecto(seccionEnEdicion));
                }
            } else {
                listEstudiantes = new ArrayList();
                seccionEnEdicion = null;
                entidadEnEdicion = null;
                listCalificacionEstGrado = null;
                existenPromocionesCalculadas = Boolean.FALSE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String obtenerCalificacionEstudiante(SgEstudiante est) {
        try {
            if (promocionesEstudiante[listEstudiantes.indexOf(est)] != null) {
                return promocionesEstudiante[listEstudiantes.indexOf(est)].getText();
            } else {
                return null;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public String obtenerErrorAlCalificar(SgEstudiante est) {

        if (promocionesEstudiante[listEstudiantes.indexOf(est)] != null) {
            return promocionesEstudiante[listEstudiantes.indexOf(est)].getText();
        } else {
            return null;
        }

    }

    public Boolean estudianteTieneError(SgEstudiante est) {
        return errorAlCalificar.containsKey(est);
    }

    public Boolean renderReporte(SgEstudiante est) {
        Boolean retorno = Boolean.FALSE;
        if (promocionesEstudiante[listEstudiantes.indexOf(est)] != null) {
            if (promocionesEstudiante[listEstudiantes.indexOf(est)].equals(EnumPromovidoCalificacion.PROMOVIDO)) {
                return Boolean.TRUE;
            }
        }
        return retorno;
    }

    public Boolean estudiantePuedeModificarPendiente(SgEstudiante est) {
        return estudiantePuedeModificarPendiente.containsKey(est);
    }

    public String botonModificarPendiente(SgEstudiante est) {
        SgCalificacionEstudiante calest = estudiantePuedeModificarPendiente.get(est);

        if (!EnumPromovidoCalificacion.PENDIENTE.equals(calest.getCaePromovidoCalificacion())) {
            return "Marcar como pendiente";
        } else {
            return "Desmarcar como pendiente";
        }
    }

    public String iconoModificarPendiente(SgEstudiante est) {
        SgCalificacionEstudiante calest = estudiantePuedeModificarPendiente.get(est);

        if (!EnumPromovidoCalificacion.PENDIENTE.equals(calest.getCaePromovidoCalificacion())) {
            return "glyphicon glyphicon-ok";
        } else {
            return "glyphicon glyphicon-remove";
        }
    }

    public void buscarError(SgEstudiante est) {
        errorAlCalificarDeEst = errorAlCalificar.get(est);
    }

    public void prepararCalificacionAPendiente(SgEstudiante est) {
        List<SgCalificacionEstudiante> calEst = listCalificacionEstGrado.stream().filter(c -> c.getCaeEstudiante().equals(est)).collect(Collectors.toList());
        calificacionEstudiantePasadoAPendiente = null;
        if (!calEst.isEmpty()) {
            calEst.get(0).getCaeCalificacion().setCalSeccion(seccionEnEdicion);
            calificacionEstudiantePasadoAPendiente = calEst.get(0);
        }
    }

    public String obtenerMensajeCambioPendiente() {
        if (calificacionEstudiantePasadoAPendiente != null) {
            if (calificacionEstudiantePasadoAPendiente.getCaeInfoProcesamientoCalificacionEstFk() == null || BooleanUtils.isNotTrue(calificacionEstudiantePasadoAPendiente.getCaeInfoProcesamientoCalificacionEstFk().getIpePromocionPendiente())) {
                return "El estudiante quedará con promoción pendiente y no podrá ser modificada desde el botón Calcular promoción, ¿desea continuar?";
            } else {
                return "Al desmarcar al estudiante como pendiente, el sistema habilitará el cálculo de promoción para el mismo, ¿desea continuar?";
            }
        } else {
            return "El estudiante quedará con promoción pendiente y no podrá ser modificada desde el botón Calcular promoción, ¿desea continuar?";
        }
    }

    public void cambioAPendiente() {
        try {
            if (calificacionEstudiantePasadoAPendiente.getCaePk() != null) {
                calificacionEstudianteRestClient.convertirEstadoPendiente(calificacionEstudiantePasadoAPendiente);
                cargarCalificacionSeccion(seccionEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgCalificacionCE getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalificacionCE entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyCalificacionDataModel getCalificacionLazyModel() {
        return calificacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalificacionDataModel calificacionLazyModel) {
        this.calificacionLazyModel = calificacionLazyModel;
    }

    public List<SgEstudiante> getListEstudiantes() {
        return listEstudiantes;
    }

    public void setListEstudiantes(List<SgEstudiante> listEstudiantes) {
        this.listEstudiantes = listEstudiantes;
    }

    public SofisComboG<SgComponentePlanEstudio> getComboComponentePlanEstudio() {
        return comboComponentePlanEstudio;
    }

    public void setComboComponentePlanEstudio(SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudio) {
        this.comboComponentePlanEstudio = comboComponentePlanEstudio;
    }

    public SofisComboG<SgCalificacion>[] getComboCalificacion() {
        return comboCalificacion;
    }

    public void setComboCalificacion(SofisComboG<SgCalificacion>[] comboCalificacion) {
        this.comboCalificacion = comboCalificacion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<SgCalificacionEstudiante> getCalificacionesEstList() {
        return calificacionesEstList;
    }

    public void setCalificacionesEstList(List<SgCalificacionEstudiante> calificacionesEstList) {
        this.calificacionesEstList = calificacionesEstList;
    }

    public SofisComboG<SgPeriodoCalificacion> getComboPeriodoCalificacion() {
        return comboPeriodoCalificacion;
    }

    public void setComboPeriodoCalificacion(SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion) {
        this.comboPeriodoCalificacion = comboPeriodoCalificacion;
    }

    public SofisComboG<SgRangoFecha> getComboRangoFecha() {
        return comboRangoFecha;
    }

    public void setComboRangoFecha(SofisComboG<SgRangoFecha> comboRangoFecha) {
        this.comboRangoFecha = comboRangoFecha;
    }

    public SgEscalaCalificacion getEscalaCalificacion() {
        return escalaCalificacion;
    }

    public void setEscalaCalificacion(SgEscalaCalificacion escalaCalificacion) {
        this.escalaCalificacion = escalaCalificacion;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
    }

    public Boolean getEscalaNumerica() {
        return escalaNumerica;
    }

    public void setEscalaNumerica(Boolean escalaNumerica) {
        this.escalaNumerica = escalaNumerica;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<String> getComboPeriodoCalendario() {
        return comboPeriodoCalendario;
    }

    public void setComboPeriodoCalendario(SofisComboG<String> comboPeriodoCalendario) {
        this.comboPeriodoCalendario = comboPeriodoCalendario;
    }

    public SofisComboG<EnumCalendarioEscolar> getComboTipoPeriodoCalendario() {
        return comboTipoPeriodoCalendario;
    }

    public void setComboTipoPeriodoCalendario(SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario) {
        this.comboTipoPeriodoCalendario = comboTipoPeriodoCalendario;
    }

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }

    public Integer getCantidadPruebaComponentePlanGrado() {
        return cantidadPruebaComponentePlanGrado;
    }

    public void setCantidadPruebaComponentePlanGrado(Integer cantidadPruebaComponentePlanGrado) {
        this.cantidadPruebaComponentePlanGrado = cantidadPruebaComponentePlanGrado;
    }

    public List<SelectItem> getComboPeriodos() {
        return comboPeriodos;
    }

    public void setComboPeriodos(List<SelectItem> comboPeriodos) {
        this.comboPeriodos = comboPeriodos;
    }

    public Integer getComboPeriodoSeleccionado() {
        return comboPeriodoSeleccionado;
    }

    public void setComboPeriodoSeleccionado(Integer comboPeriodoSeleccionado) {
        this.comboPeriodoSeleccionado = comboPeriodoSeleccionado;
    }

    public List<EnumTiposPeriodosCalificaciones> getPeriodoOrdinarioList() {
        return periodoOrdinarioList;
    }

    public void setPeriodoOrdinarioList(List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList) {
        this.periodoOrdinarioList = periodoOrdinarioList;
    }

    public Long getCalificacionId() {
        return calificacionId;
    }

    public List<SgPorcentajeAsistenciasResponse> getListPorcentajeAsistencia() {
        return listPorcentajeAsistencia;
    }

    public void setListPorcentajeAsistencia(List<SgPorcentajeAsistenciasResponse> listPorcentajeAsistencia) {
        this.listPorcentajeAsistencia = listPorcentajeAsistencia;
    }

    public SgRelGradoPlanEstudio getRelGradoPlanEstudio() {
        return relGradoPlanEstudio;
    }

    public void setRelGradoPlanEstudio(SgRelGradoPlanEstudio relGradoPlanEstudio) {
        this.relGradoPlanEstudio = relGradoPlanEstudio;
    }

    public List<SgCalificacionEstudiante> getListCalificacionEstGrado() {
        return listCalificacionEstGrado;
    }

    public void setListCalificacionEstGrado(List<SgCalificacionEstudiante> listCalificacionEstGrado) {
        this.listCalificacionEstGrado = listCalificacionEstGrado;
    }

    public Boolean getHabilitadoCierreAnio() {
        return habilitadoCierreAnio;
    }

    public void setHabilitadoCierreAnio(Boolean habilitadoCierreAnio) {
        this.habilitadoCierreAnio = habilitadoCierreAnio;
    }

    public EnumPromovidoCalificacion[] getPromocionesEstudiante() {
        return promocionesEstudiante;
    }

    public void setPromocionesEstudiante(EnumPromovidoCalificacion[] promocionesEstudiante) {
        this.promocionesEstudiante = promocionesEstudiante;
    }

    public String getMensajeWarningPromocion() {
        return mensajeWarningPromocion;
    }

    public void setMensajeWarningPromocion(String mensajeWarningPromocion) {
        this.mensajeWarningPromocion = mensajeWarningPromocion;
    }

    public String getMensajeWarningCierreAnio() {
        return mensajeWarningCierreAnio;
    }

    public void setMensajeWarningCierreAnio(String mensajeWarningCierreAnio) {
        this.mensajeWarningCierreAnio = mensajeWarningCierreAnio;
    }

    public SgCalendario getCalendarioSede() {
        return calendarioSede;
    }

    public void setCalendarioSede(SgCalendario calendarioSede) {
        this.calendarioSede = calendarioSede;
    }

    public Boolean getExistenPromocionesCalculadas() {
        return existenPromocionesCalculadas;
    }

    public void setExistenPromocionesCalculadas(Boolean existenPromocionesCalculadas) {
        this.existenPromocionesCalculadas = existenPromocionesCalculadas;
    }

    public Boolean getEjecucionAsincronica() {
        return ejecucionAsincronica;
    }

    public void setEjecucionAsincronica(Boolean ejecucionAsincronica) {
        this.ejecucionAsincronica = ejecucionAsincronica;
    }

    public String getErrorAlCalificarDeEst() {
        return errorAlCalificarDeEst;
    }

    public void setErrorAlCalificarDeEst(String errorAlCalificarDeEst) {
        this.errorAlCalificarDeEst = errorAlCalificarDeEst;
    }

    public SgCalificacionEstudiante getCalificacionEstudiantePasadoAPendiente() {
        return calificacionEstudiantePasadoAPendiente;
    }

    public void setCalificacionEstudiantePasadoAPendiente(SgCalificacionEstudiante calificacionEstudiantePasadoAPendiente) {
        this.calificacionEstudiantePasadoAPendiente = calificacionEstudiantePasadoAPendiente;
    }

    public Boolean getFirmaPromocionActivada() {
        return firmaPromocionActivada;
    }

    public void setFirmaPromocionActivada(Boolean firmaPromocionActivada) {
        this.firmaPromocionActivada = firmaPromocionActivada;
    }

    public SgConfirmacionPromocion getConf() {
        return conf;
    }

    public void setConf(SgConfirmacionPromocion conf) {
        this.conf = conf;
    }
    
}
