/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ComponentePlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.ModalidadRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgConfirmacionAprobacion;
import sv.gob.mined.siges.web.dto.SgDatosCalculoCalificaciones;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacionEstCal;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.web.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumResolucionCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConfirmacionAprobacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacionEstCal;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.ConfirmacionAprobacionRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;

@Named
@ViewScoped
public class CalificacionPeriodoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionPeriodoBean.class.getName());

    @Inject
    private SedeRestClient restSede;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private CicloRestClient restCiclo;

    @Inject
    private ModalidadRestClient restModalidad;

    @Inject
    private GradoRestClient restGrado;

    @Inject
    private ComponentePlanEstudioRestClient componentePlanEstudioRestClient;

    @Inject
    private PeriodoCalificacionRestClient periodoCalificacionClient;

    @Inject
    private CalificacionRestClient calificacionRestClient;

    @Inject
    private CalendarioRestClient calendarioClient;

    @Inject
    private CatalogosRestClient catalogosClient;

    @Inject
    private ConfirmacionAprobacionRestClient confirmacionAprobacionClient;

    @Inject
    private ComponentePlanGradoRestClient componentePlanGradoRestClient;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private SeleccionarSeccionBean seleccionarSeccionBean;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "id_transaccion")
    private String idTransaccionFirma;

    private Boolean soloLectura = Boolean.FALSE;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudio;
    private List<SgEstudiante> listEstudiantes;
    private List<SgRangoFecha> listRangoFecha;
    private List<SgCalificacionEstudiante> calificaciones;
    private SgSeccion seccionEnEdicion;
    private List<Integer> listaNumeros;
    private Integer cantidadPeriodos;
    private List<Integer> listTituloRango;
    private SgComponentePlanGrado componentePlanGrado;
    private List<Integer> cantidadPrimeraPruebaList;
    private List<Integer> cantidadPrimeraRecuperacionPSList;
    private List<Integer> cantidadSegundaRecuperacionList;
    private List<Integer> cantidadSegundaRecuperacionPSList;
    private List<SgCalificacion> calificacionesConceptualesList = new ArrayList();
    private List<SgCalificacionEstudiante> calificacionesCalculoAprobacion;
    private Boolean renderNotaInstitucional = Boolean.FALSE;
    private Boolean renderNotaAprobacion = Boolean.FALSE;
    private SgSede sedeAnterior = null;
    private String[] formulasAuxiliares = new String[]{"maximo(a,b)=if(isNaN(a),if(isNaN(b),0,b),if(isNaN(b),a,max(a,b)))", "maxTres(a,b,c)=maximo(a,maximo(b,c))"};
    private String[] incluirCamposSeccion = new String[]{
        "secServicioEducativo.sduGrado.graPk",
        "secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
        "secServicioEducativo.sduSede.sedPk",
        "secServicioEducativo.sduSede.sedTipo",
        "secPlanEstudio.pesPk",
        "secAnioLectivo.alePk",
        "secTipoPeriodo",
        "secNumeroPeriodo",
        "secNombre",
        "secEstado",
        "secCodigo",
        "secVersion"};
    private SgPeriodoCalificacion periodoCalificacionComponenteEnSeccion;

    //Firma
    private SgConfirmacionAprobacion conf;
    private Boolean firmaAprobacionActivada = Boolean.FALSE;
    private Boolean notaAPRDeComponenteCalculada = Boolean.FALSE;

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();

            if (idTransaccionFirma != null) {
                this.confirmarFirma(idTransaccionFirma);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PERIODOS_CALIFICACION)) {
            LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.MENU_PERIODOS_CALIFICACION);
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        try {
            comboComponentePlanEstudio = new SofisComboG();
            comboComponentePlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            SgConfiguracionFirmaElectronica cnf = catalogosClient.buscarConfiguracionFirmaElectronicaPorCodigo(Constantes.CONFIG_FIRMA_ELECTRONICA_CONFIRMACION_APROBACION);
            if (cnf != null) {
                firmaAprobacionActivada = cnf.getConActivada();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void limpiarCombos() {
        comboComponentePlanEstudio = new SofisComboG();
        comboComponentePlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public Boolean renderCalcularNOTIN() {
        return this.comboComponentePlanEstudio.getSelectedT() != null
                && !TipoComponentePlanEstudio.PRU.equals(this.comboComponentePlanEstudio.getSelectedT().getCpeTipo())
                && EnumSeccionEstado.ABIERTA.equals(seccionEnEdicion.getSecEstado())
                && (conf == null || BooleanUtils.isNotTrue(conf.getCprFirmada()));
    }

    public Boolean renderCalcularAPR() {
        return this.comboComponentePlanEstudio.getSelectedT() != null
                && !TipoComponentePlanEstudio.PRU.equals(this.comboComponentePlanEstudio.getSelectedT().getCpeTipo())
                && EnumSeccionEstado.ABIERTA.equals(seccionEnEdicion.getSecEstado())
                && (conf == null || BooleanUtils.isNotTrue(conf.getCprFirmada()));
    }

    public void confirmar() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            if (conf == null) {
                conf = new SgConfirmacionAprobacion();
            }
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
            conf.setCprTransactionReturnUrl(baseURL + "/pp/" + ConstantesPaginas.CALIFICACION_PERIODO);
            conf.setCprSeccion(this.seccionEnEdicion);
            conf.setCprComponentePlanEstudio(comboComponentePlanEstudio.getSelectedT());
            conf = this.confirmacionAprobacionClient.preconfirmar(conf);

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
            conf = this.confirmacionAprobacionClient.confirmar(transactionId);
            FiltroConfirmacionAprobacion fil = new FiltroConfirmacionAprobacion();
            fil.setIncluirCampos(new String[]{"cprSeccion.secPk", "cprArchivoFirmado.achPk", "cprComponentePlanEstudio.cpePk",
                "cprComponentePlanEstudio.cpeTipo", "cprComponentePlanEstudio.cpeNombre", "cprComponentePlanEstudio.cpeVersion"});
            fil.setCprPk(conf.getCprPk());
            List<SgConfirmacionAprobacion> confs = confirmacionAprobacionClient.buscar(fil);
            if (!confs.isEmpty()) {
                SgConfirmacionAprobacion conf = confs.get(0);
                SgSeccion sec = seccionClient.obtenerPorId(conf.getCprSeccion().getSecPk());
                seleccionarSeccionBean.setRetornarSoloSeccionesAbiertas(Boolean.FALSE);
                seleccionarSeccionBean.setIncluirCamposSeccion(incluirCamposSeccion);
                seleccionarSeccionBean.setearCombosDadaSeccion(sec);
                seleccionarSeccion(sec);

                this.comboComponentePlanEstudio.setSelectedT(conf.getCprComponentePlanEstudio());
                cargarPeriodoCalificacion();
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void eliminarConfirmacion() {
        try {
            this.confirmacionAprobacionClient.eliminar(conf.getCprPk());
            conf = null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarSeccion(SgSeccion sec) {
        try {
            limpiarCombos();
            comboComponentePlanEstudio = new SofisComboG();
            comboComponentePlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            notaAPRDeComponenteCalculada = Boolean.FALSE;
            conf = null;
            seccionEnEdicion = sec;
            if (seccionEnEdicion != null && seccionEnEdicion.getSecPk() != null && seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk() != null
                    && seccionEnEdicion.getSecPlanEstudio().getPesPk() != null) {
                FiltroComponentePlanEstudio fcpg = new FiltroComponentePlanEstudio();
                fcpg.setCpeGrado(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
                fcpg.setCpePlanEstudio(seccionEnEdicion.getSecPlanEstudio().getPesPk());
                fcpg.setExculsivoDeSeccion(seccionEnEdicion.getSecPk());
                fcpg.setIncluirCampos(new String[]{"cpeTipo", "cpeNombre", "cpeVersion"});
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.VER_CALIFICACION_PERIODO_COMPONENTE_TIPO_PRUEBA)) {
                    fcpg.setCpeOmitirTipo(TipoComponentePlanEstudio.PRU);
                }

                List<SgComponentePlanEstudio> liscpe = componentePlanEstudioRestClient.buscar(fcpg);

                comboComponentePlanEstudio = new SofisComboG(liscpe, "cpeNombre");
                comboComponentePlanEstudio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarPeriodoCalificacion() {
        try {

            cantidadPrimeraPruebaList = new ArrayList();
            cantidadPrimeraRecuperacionPSList = new ArrayList();
            cantidadSegundaRecuperacionList = new ArrayList();
            cantidadSegundaRecuperacionPSList = new ArrayList();
            listEstudiantes = new ArrayList<>();
            calificaciones = new ArrayList<>();
            listRangoFecha = new ArrayList();
            notaAPRDeComponenteCalculada = Boolean.FALSE;
            conf = null;

            if (comboComponentePlanEstudio.getSelectedT() != null
                    && seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk() != null
                    && seccionEnEdicion.getSecPlanEstudio().getPesPk() != null) {
                consultarCalendarioSede();

                FiltroConfirmacionAprobacion fil = new FiltroConfirmacionAprobacion();
                fil.setSeccionFk(seccionEnEdicion.getSecPk());
                fil.setComponentePlanEstudioFk(comboComponentePlanEstudio.getSelectedT().getCpePk());
                List<SgConfirmacionAprobacion> confs = confirmacionAprobacionClient.buscar(fil);
                if (!confs.isEmpty()) {
                    conf = confs.get(0);
                }

                FiltroComponentePlanGrado fcp = new FiltroComponentePlanGrado();
                fcp.setCpgGradoPk(seccionEnEdicion.getSecServicioEducativo().getSduGrado().getGraPk());
                fcp.setCpgPlanEstudioPk(seccionEnEdicion.getSecPlanEstudio().getPesPk());
                fcp.setCpgComponentePlanEstudioPk(comboComponentePlanEstudio.getSelectedT().getCpePk());
                fcp.setCpgAgregandoSeccionExclusiva(seccionEnEdicion.getSecPk());
                fcp.setIncluirCampos(new String[]{
                    "cpgPeriodosCalificacion",
                    "cpgEscalaCalificacion.ecaPk",
                    "cpgEscalaCalificacion.ecaCodigo",
                    "cpgEscalaCalificacion.ecaHabilitado",
                    "cpgEscalaCalificacion.ecaNombre",
                    "cpgEscalaCalificacion.ecaTipoEscala",
                    "cpgEscalaCalificacion.ecaMinimo",
                    "cpgEscalaCalificacion.ecaMaximo",
                    "cpgEscalaCalificacion.ecaMinimoAprobacion",
                    "cpgEscalaCalificacion.ecaPrecision",
                    "cpgEscalaCalificacion.ecaValorMinimo.calValor",
                    "cpgEscalaCalificacion.ecaValorMinimo.calOrden",
                    "cpgComponentePlanEstudio.cpeTipo",
                    "cpgComponentePlanEstudio.cpePk",
                    "cpgComponentePlanEstudio.cpeVersion",
                    "cpgComponentePlanEstudio.cpeNombre",
                    "cpgCantidadPrimeraPrueba",
                    "cpgCantidadPrimeraSuficiencia",
                    "cpgCantidadSegundaPrueba",
                    "cpgCantidadSegundaSuficiencia",
                    "cpgCalculoNotaInstitucional",
                    "cpgFuncionRedondeo",
                    "cpgOrden",
                    "cpgCodigoExterno",
                    "cpgPrecision",
                    "cpgObjetoPromocion",
                    "cpgCalificacionIngresadaCE",
                    "cpgFormula.fomTextoLargo",
                    "cpgFormula.fomPk",
                    "cpgFormula.fomTipoFormula",
                    "cpgFormula.fomTienSubformula",
                    "cpgParametroFormulaAprobacion.cpgComponentePlanEstudio.cpePk",
                    "cpgParametroFormulaAprobacion.cpgComponentePlanEstudio.cpeTipo"});

                List<SgComponentePlanGrado> cpg = componentePlanGradoRestClient.buscarConCache(fcp);
                if (!cpg.isEmpty()) {
                    componentePlanGrado = cpg.get(0);
                    if (componentePlanGrado.getCpgCantidadPrimeraPrueba() != null && componentePlanGrado.getCpgCantidadPrimeraPrueba() > 0) {
                        for (int i = 1; i <= componentePlanGrado.getCpgCantidadPrimeraPrueba(); i++) {
                            cantidadPrimeraPruebaList.add(i);
                        }
                    }
                    if (componentePlanGrado.getCpgCantidadPrimeraSuficiencia() != null && componentePlanGrado.getCpgCantidadPrimeraSuficiencia() > 0) {
                        for (int i = 1; i <= componentePlanGrado.getCpgCantidadPrimeraSuficiencia(); i++) {
                            cantidadPrimeraRecuperacionPSList.add(i);
                        }
                    }
                    if (componentePlanGrado.getCpgCantidadSegundaPrueba() != null && componentePlanGrado.getCpgCantidadSegundaPrueba() > 0) {
                        for (int i = 1; i <= componentePlanGrado.getCpgCantidadSegundaPrueba(); i++) {
                            cantidadSegundaRecuperacionList.add(i);
                        }
                    }
                    if (componentePlanGrado.getCpgCantidadSegundaSuficiencia() != null && componentePlanGrado.getCpgCantidadSegundaSuficiencia() > 0) {
                        for (int i = 1; i <= componentePlanGrado.getCpgCantidadSegundaSuficiencia(); i++) {
                            cantidadSegundaRecuperacionPSList.add(i);
                        }
                    }
                }

                actualizarCalificaciones();

            }
            PrimeFaces.current().executeScript("PF('blocker').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarCalificaciones() throws Exception {

        notaAPRDeComponenteCalculada = Boolean.FALSE;

        FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
        fpc.setIncluirCampos(new String[]{
            "pcaNumero",
            "pcaPermiteCalificarSinNie",
            "pcaEsPrueba",
            "pcaNombre",
            "pcaVersion"});
        fpc.setPcaEsPrueba(Boolean.FALSE);
        fpc.setPcaModalidadEducativa(seleccionarSeccionBean.getComboModalidad().getSelectedT().getModPk());
        fpc.setPcaModalidadAtencion(seleccionarSeccionBean.getComboModalidadAtencion().getSelectedT().getMatPk());
        fpc.setPcaNumero(componentePlanGrado.getCpgPeriodosCalificacion());
        fpc.setPcaSubModalidadAtencion(seleccionarSeccionBean.getComboSubModalidadAtencion() != null && seleccionarSeccionBean.getComboSubModalidadAtencion().getSelectedT() != null ? seleccionarSeccionBean.getComboSubModalidadAtencion().getSelectedT().getSmoPk() : null);
        fpc.setPcaAnioLectivo(seccionEnEdicion.getSecAnioLectivo().getAlePk());
        fpc.setPcaTipoCalendario(seccionEnEdicion.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());

        //Filtra dependiendo si sección es anual o semestral
        fpc.setPcaTipoPeriodo(seccionEnEdicion.getSecTipoPeriodo());
        fpc.setPcaNumeroPeriodo(seccionEnEdicion.getSecNumeroPeriodo());

        List<SgPeriodoCalificacion> listPeriodoCalif = periodoCalificacionClient.buscar(fpc);

        if (!listPeriodoCalif.isEmpty()) {
            periodoCalificacionComponenteEnSeccion = listPeriodoCalif.get(0);
        } else {
            periodoCalificacionComponenteEnSeccion = null;
        }

        FiltroPeriodoCalificacionEstCal filtro = new FiltroPeriodoCalificacionEstCal();
        filtro.setSeccionPk(this.seccionEnEdicion.getSecPk());
        filtro.setAnioLectivoPk(this.seccionEnEdicion.getSecAnioLectivo().getAlePk());
        filtro.setComponentePlanEstudioPk(this.comboComponentePlanEstudio.getSelectedT().getCpePk());
        filtro.setPeriodoCalificacionPk(periodoCalificacionComponenteEnSeccion != null ? periodoCalificacionComponenteEnSeccion.getPcaPk() : null);

        //Toma en cuenta secciones semestrales
        //Filtra dependiendo si sección es anual o semestral
        filtro.setTipoPeriodo(seccionEnEdicion.getSecTipoPeriodo());
        filtro.setNumeroPeriodo(seccionEnEdicion.getSecNumeroPeriodo());

        SgPeriodoCalificacionEstCal ret = calificacionRestClient.buscarEstudiantesCalificacionesPeriodo(filtro);
        this.listEstudiantes = ret.getPecEstudiantes();
        this.calificaciones = ret.getPecCalificaciones();
        this.listRangoFecha = ret.getPecPeriodoRangosFecha();

        if (this.calificaciones != null) {
            for (SgCalificacionEstudiante c : this.calificaciones) {
                if (EnumTiposPeriodosCalificaciones.APR.equals(c.getCaeCalificacion().getCalTipoPeriodoCalificacion())) {
                    notaAPRDeComponenteCalculada = Boolean.TRUE;
                    break;
                }
            }
        }
    }

    public void consultarCalendarioSede() {
        try {

            if (!this.seleccionarSeccionBean.getSedeSeleccionada().equals(sedeAnterior)) {
                sedeAnterior = this.seleccionarSeccionBean.getSedeSeleccionada();
                FiltroCalendario fcal = new FiltroCalendario();
                fcal.setHabilitado(Boolean.TRUE);
                fcal.setIncluirCampos(new String[]{"calPermiteCalcularNotaAprobacion", "calPermiteCalcularNotaInstitucional"});
                fcal.setTipoCalendarioPk(sedeAnterior.getSedTipoCalendario().getTcePk());

                SgCalendario cal = calendarioClient.buscar(fcal).stream().findFirst().orElse(null);

                if (cal != null) {
                    renderNotaAprobacion = cal.getCalPermiteCalcularNotaAprobacion();
                    renderNotaInstitucional = cal.getCalPermiteCalcularNotaInstitucional();
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

//    public List<SgEstudiante> buscarEstudiantes(Long secPk) throws Exception {
//        FiltroMatricula filtroMat = new FiltroMatricula();
//        filtroMat.setSecPk(secPk);
//        filtroMat.setMatRetirada(Boolean.FALSE);
//        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
//            "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
//            "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
//            "matEstudiante.estVersion"});
//        filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellidoBusqueda", "matEstudiante.estPersona.perSegundoApellidoBusqueda",
//            "matEstudiante.estPersona.perPrimerNombreBusqueda"});
//        filtroMat.setAscending(new boolean[]{true, true, true});
//        List<SgMatricula> matriculas = matriculaClient.buscar(filtroMat);
//        List<SgEstudiante> estudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());
//        return estudiantes;
//    }
    public String obtenerCalificacion(SgEstudiante est, SgRangoFecha rango) {
        Integer precision = null;
        if (componentePlanGrado.getCpgEscalaCalificacion() != null && componentePlanGrado.getCpgEscalaCalificacion().getEcaPrecision() != null) {
            precision = componentePlanGrado.getCpgEscalaCalificacion().getEcaPrecision();
        } else {
            precision = componentePlanGrado.getCpgPrecision();
        }
        for (SgCalificacionEstudiante ces : calificaciones) {
            if (ces.getCaeCalificacion().getCalRangoFecha() != null && ces.getCaeCalificacion().getCalRangoFecha().equals(rango)) {
                if (est.equals(ces.getCaeEstudiante())) {
                    String resultado = null;
                    if (ces.getCaeCalificacionNumericaEstudiante() != null) {
                        resultado = ces.getCaeCalificacionNumerica(precision);
                    }
                    if (ces.getCaeCalificacionConceptualEstudiante() != null) {
                        resultado = ces.getCaeCalificacionConceptualEstudiante().getCalValor();
                    }
                    return resultado;
                }
            }
        }
        return null;

    }

    public String obtenerCalificacionRecuperacion(SgEstudiante est, EnumCalendarioEscolar enumCal, Integer numero) {
        Integer precision = null;
        if (componentePlanGrado.getCpgEscalaCalificacion() != null && componentePlanGrado.getCpgEscalaCalificacion().getEcaPrecision() != null) {
            precision = componentePlanGrado.getCpgEscalaCalificacion().getEcaPrecision();
        } else {
            precision = componentePlanGrado.getCpgPrecision();
        }
        if (numero != null && numero > 0) {
            for (SgCalificacionEstudiante ces : calificaciones) {
                if (enumCal.equals(ces.getCaeCalificacion().getCalTipoCalendarioEscolar()) && numero.equals(ces.getCaeCalificacion().getCalNumero())) {
                    if (est.equals(ces.getCaeEstudiante())) {
                        String resultado = null;
                        if (ces.getCaeCalificacionNumericaEstudiante() != null) {
                            resultado = ces.getCaeCalificacionNumerica(precision);
                        }
                        if (ces.getCaeCalificacionConceptualEstudiante() != null) {
                            resultado = ces.getCaeCalificacionConceptualEstudiante().getCalValor();
                        }
                        return resultado;
                    }
                }
            }
            return null;
        }
        return null;
    }

    public String obtenerCalificacionPrimeraRecuperacionPS(SgEstudiante est, Integer numeroPRPS) {
        Integer precision = null;
        if (componentePlanGrado.getCpgEscalaCalificacion() != null && componentePlanGrado.getCpgEscalaCalificacion().getEcaPrecision() != null) {
            precision = componentePlanGrado.getCpgEscalaCalificacion().getEcaPrecision();
        } else {
            precision = componentePlanGrado.getCpgPrecision();
        }
        if (numeroPRPS != null && numeroPRPS > 0) {
            for (SgCalificacionEstudiante ces : calificaciones) {
                if (numeroPRPS.equals(ces.getCaeCalificacion().getCalNumero())) {
                    if (est.equals(ces.getCaeEstudiante())) {
                        String resultado = null;
                        if (ces.getCaeCalificacionNumericaEstudiante() != null) {
                            resultado = ces.getCaeCalificacionNumerica(precision);
                        }
                        if (ces.getCaeCalificacionConceptualEstudiante() != null) {
                            resultado = ces.getCaeCalificacionConceptualEstudiante().getCalValor();
                        }
                        return resultado;
                    }
                }
            }
            return null;
        }
        return null;
    }

    public void actualizarNotaInstitucionalYRecargarCalificaciones() {
        try {
            actualizarNotaInstitucional();
            actualizarCalificaciones();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void actualizarNotaInstitucional() throws Exception {

        if (componentePlanGrado.getCpgCalculoNotaInstitucional() == null) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_COMPONENTE_PLAN_ESTUDIO_SIN_CALCULO_NOTA_INSTITUCIONAL), "");
            return;
        }
        if (EnumCalculoNotaInstitucional.PROM.equals(componentePlanGrado.getCpgCalculoNotaInstitucional())) {
            if (componentePlanGrado.getCpgFuncionRedondeo() == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_COMPONENTE_PLAN_ESTUDIO_SIN_FUNCION_REDONDEO), "");
                return;
            }
        }
        SgDatosCalculoCalificaciones dcc = new SgDatosCalculoCalificaciones();
        dcc.setComponentePlanGrado(componentePlanGrado);
        dcc.setSeccion(seccionEnEdicion);
        dcc.setPeriodoCalificacionPk(periodoCalificacionComponenteEnSeccion != null ? periodoCalificacionComponenteEnSeccion.getPcaPk() : null);
        Boolean resultado = calificacionRestClient.calcularNotaInstitucional(dcc);
        if (BooleanUtils.isTrue(resultado)) {
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.NOTA_INSTITUCIONAL_GENERADA_CORRECTAMENTE), "");
        } else {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String colorRow(SgEstudiante est) {
        String resultado = null;
        SgCalificacion calConceptual = null;
        for (SgCalificacionEstudiante calest : calificaciones) {
            if ((EnumTiposPeriodosCalificaciones.APR.equals(calest.getCaeCalificacion().getCalTipoPeriodoCalificacion())) && est.equals(calest.getCaeEstudiante())) {

                if (EnumTiposPeriodosCalificaciones.APR.equals(calest.getCaeCalificacion().getCalTipoPeriodoCalificacion())) {
                    if (TipoEscalaCalificacion.NUMERICA.equals(componentePlanGrado.getCpgEscalaCalificacion().getEcaTipoEscala())) {
                        resultado = calest.getCaeCalificacionNumericaEstudiante();
                    }
                    if (TipoEscalaCalificacion.CONCEPTUAL.equals(componentePlanGrado.getCpgEscalaCalificacion().getEcaTipoEscala())) {
                        calConceptual = calest.getCaeCalificacionConceptualEstudiante();
                    }
                    if (EnumResolucionCalificacion.PENDIENTE.equals(calest.getCaeResolucion())) {
                        resultado = null;
                        calConceptual = null;
                    }
                    break;
                }

            }
        }
        if (resultado != null) {
            Double res = Double.valueOf(resultado);
            if (componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion() != null) {
                if (Double.compare(res, componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion()) >= 0) {
                    return "aprobado";
                } else {
                    return "reprobado";
                }
            }
        }
        if (calConceptual != null) {
            if (calConceptual.getCalOrden() != null && componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo() != null && componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo().getCalOrden() != null) {
                if (calConceptual.getCalOrden() >= componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo().getCalOrden()) {
                    return "aprobado";
                } else {
                    return "reprobado";
                }
            }

        }
        return null;
    }

    public String buscarCalificacionEspecial(SgEstudiante est, EnumTiposPeriodosCalificaciones tipoPerCal) {
        for (SgCalificacionEstudiante calest : calificaciones) {
            if (tipoPerCal.equals(calest.getCaeCalificacion().getCalTipoPeriodoCalificacion()) && est.equals(calest.getCaeEstudiante())) {
                String resultado = null;
                if (calest.getCaeCalificacionNumericaEstudiante() != null) {
                    resultado = calest.getCaeCalificacionNumericaEstudiante(); //NOTIN y APR se guardar con decimales exactos en bd. No hay que formatear
                }
                if (calest.getCaeCalificacionConceptualEstudiante() != null) {
                    resultado = calest.getCaeCalificacionConceptualEstudiante().getCalValor();
                }
                if (EnumResolucionCalificacion.PENDIENTE.equals(calest.getCaeResolucion()) && EnumTiposPeriodosCalificaciones.APR.equals(tipoPerCal)) {
                    resultado = "Pendiente";
                }
                return resultado;
            }
        }
        return null;
    }

    public void actualizarNotaAprobacionYRecargarCalificaciones() {
        try {
            if (comboComponentePlanEstudio.getSelectedT() != null && seccionEnEdicion != null) {
                SgDatosCalculoCalificaciones dcc = new SgDatosCalculoCalificaciones();
                dcc.setComponentePlanGrado(componentePlanGrado);
                dcc.setSeccion(seccionEnEdicion);
                dcc.setPeriodoCalificacionPk(periodoCalificacionComponenteEnSeccion != null ? periodoCalificacionComponenteEnSeccion.getPcaPk() : null);
                Boolean resultado = calificacionRestClient.calcularAprobacion(dcc);
                actualizarCalificaciones();

                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.NOTA_INSTITUCIONAL_GENERADA_CORRECTAMENTE), "");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.NOTA_APROBACION_GENERADA_CORRECTAMENTE), "");

            }

            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.EXISTE_ESTUDIANTE_SIN_PERIODOS), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public NivelRestClient getRestNivel() {
        return restNivel;
    }

    public void setRestNivel(NivelRestClient restNivel) {
        this.restNivel = restNivel;
    }

    public CicloRestClient getRestCiclo() {
        return restCiclo;
    }

    public void setRestCiclo(CicloRestClient restCiclo) {
        this.restCiclo = restCiclo;
    }

    public ModalidadRestClient getRestModalidad() {
        return restModalidad;
    }

    public void setRestModalidad(ModalidadRestClient restModalidad) {
        this.restModalidad = restModalidad;
    }

    public GradoRestClient getRestGrado() {
        return restGrado;
    }

    public void setRestGrado(GradoRestClient restGrado) {
        this.restGrado = restGrado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<SgComponentePlanEstudio> getComboComponentePlanEstudio() {
        return comboComponentePlanEstudio;
    }

    public void setComboComponentePlanEstudio(SofisComboG<SgComponentePlanEstudio> comboComponentePlanEstudio) {
        this.comboComponentePlanEstudio = comboComponentePlanEstudio;
    }

    public List<SgEstudiante> getListEstudiantes() {
        return listEstudiantes;
    }

    public void setListEstudiantes(List<SgEstudiante> listEstudiantes) {
        this.listEstudiantes = listEstudiantes;
    }

    public List<SgRangoFecha> getListRangoFecha() {
        return listRangoFecha;
    }

    public void setListRangoFecha(List<SgRangoFecha> listRangoFecha) {
        this.listRangoFecha = listRangoFecha;
    }

    public List<Integer> getListaNumeros() {
        return listaNumeros;
    }

    public void setListaNumeros(List<Integer> listaNumeros) {
        this.listaNumeros = listaNumeros;
    }

    public Integer getCantidadPeriodos() {
        return cantidadPeriodos;
    }

    public void setCantidadPeriodos(Integer cantidadPeriodos) {
        this.cantidadPeriodos = cantidadPeriodos;
    }

    public List<Integer> getListTituloRango() {
        return listTituloRango;
    }

    public void setListTituloRango(List<Integer> listTituloRango) {
        this.listTituloRango = listTituloRango;
    }

    public List<Integer> getCantidadPrimeraPruebaList() {
        return cantidadPrimeraPruebaList;
    }

    public void setCantidadPrimeraPruebaList(List<Integer> cantidadPrimeraPruebaList) {
        this.cantidadPrimeraPruebaList = cantidadPrimeraPruebaList;
    }

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }

    public List<Integer> getCantidadPrimeraRecuperacionPSList() {
        return cantidadPrimeraRecuperacionPSList;
    }

    public void setCantidadPrimeraRecuperacionPSList(List<Integer> cantidadPrimeraRecuperacionPSList) {
        this.cantidadPrimeraRecuperacionPSList = cantidadPrimeraRecuperacionPSList;
    }

    public List<Integer> getCantidadSegundaRecuperacionList() {
        return cantidadSegundaRecuperacionList;
    }

    public void setCantidadSegundaRecuperacionList(List<Integer> cantidadSegundaRecuperacionList) {
        this.cantidadSegundaRecuperacionList = cantidadSegundaRecuperacionList;
    }

    public List<Integer> getCantidadSegundaRecuperacionPSList() {
        return cantidadSegundaRecuperacionPSList;
    }

    public void setCantidadSegundaRecuperacionPSList(List<Integer> cantidadSegundaRecuperacionPSList) {
        this.cantidadSegundaRecuperacionPSList = cantidadSegundaRecuperacionPSList;
    }

    public List<SgCalificacion> getCalificacionesConceptualesList() {
        return calificacionesConceptualesList;
    }

    public void setCalificacionesConceptualesList(List<SgCalificacion> calificacionesConceptualesList) {
        this.calificacionesConceptualesList = calificacionesConceptualesList;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
    }

    public List<SgCalificacionEstudiante> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<SgCalificacionEstudiante> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public List<SgCalificacionEstudiante> getCalificacionesCalculoAprobacion() {
        return calificacionesCalculoAprobacion;
    }

    public void setCalificacionesCalculoAprobacion(List<SgCalificacionEstudiante> calificacionesCalculoAprobacion) {
        this.calificacionesCalculoAprobacion = calificacionesCalculoAprobacion;
    }

    public Boolean getRenderNotaInstitucional() {
        return renderNotaInstitucional;
    }

    public void setRenderNotaInstitucional(Boolean renderNotaInstitucional) {
        this.renderNotaInstitucional = renderNotaInstitucional;
    }

    public Boolean getRenderNotaAprobacion() {
        return renderNotaAprobacion;
    }

    public void setRenderNotaAprobacion(Boolean renderNotaAprobacion) {
        this.renderNotaAprobacion = renderNotaAprobacion;
    }

    public String[] getIncluirCamposSeccion() {
        return incluirCamposSeccion;
    }

    public SgConfirmacionAprobacion getConf() {
        return conf;
    }

    public void setConf(SgConfirmacionAprobacion conf) {
        this.conf = conf;
    }

    public Boolean getFirmaAprobacionActivada() {
        return firmaAprobacionActivada;
    }

    public void setFirmaAprobacionActivada(Boolean firmaAprobacionActivada) {
        this.firmaAprobacionActivada = firmaAprobacionActivada;
    }

    public Boolean getNotaAPRDeComponenteCalculada() {
        return notaAPRDeComponenteCalculada;
    }

    public void setNotaAPRDeComponenteCalculada(Boolean notaAPRDeComponenteCalculada) {
        this.notaAPRDeComponenteCalculada = notaAPRDeComponenteCalculada;
    }

}
