/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAllegado;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.AllegadoRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgMatriculaParcial;
import sv.gob.mined.siges.web.dto.SgRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumEstadoHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.web.enumerados.EnumServicioEducativoEstado;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.web.lazymodels.LazyLucenePersonaDataModel;
import sv.gob.mined.siges.web.restclient.CalendarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.RelHabilitacionMatriculaNivelRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.ObjectMapperContextResolver;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class MatriculaEstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MatriculaEstudianteBean.class.getName());

    @Inject
    @Param(name = "seccionId")
    private Long seccionId;

    @Inject
    private MatriculaRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private PersonaRestClient personaClient;

    @Inject
    private EstudianteRestClient estudianteClient;

    @Inject
    private AllegadoRestClient allegadoClient;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private IdentificacionComponenteBean identificacionComponenteBean;

    @Inject
    private CalendarioEscolarRestClient periodoCalendarioRestClient;

    @Inject
    private RelHabilitacionMatriculaNivelRestClient relHabilitacionMatriculaNivelRestClient;

    private SgSeccion seccionEnEdicion;
    private SgMatricula entidadEnEdicion;
    private SgMatriculaParcial matriculaParcialEdicion;
    private SgEstudiante entidadEnEdicionEstudiante;
    private SgAllegado estudianteReferente;
    private FiltroMatricula filtro = new FiltroMatricula();
    private List<RevHistorico> historialMatricula = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultadosPersonas;
    private LazyLucenePersonaDataModel lazyPersonaDataModel;
    private LocalDate fechaRegistro;
    private SofisComboG<SgTipoParentesco> comboTipoParentesco;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean soloLecturaProvisional = Boolean.FALSE;
    private Boolean avanzarWizardDatosPersonales = Boolean.FALSE;
    private String wizardStep = "identificacion";
    private List<SgMatriculaParcial> matPendientes = new ArrayList<>();
    private Boolean ingresarResponsableEstudianteMayorEdad = Boolean.FALSE;
    private Boolean nieGenaradoAutomatico = Boolean.FALSE;
    private Boolean nieGenaradoAutomaticoGradoRequiereNie = Boolean.FALSE;
    private Boolean consumoServicioDUIRNPNHabilitado = Boolean.FALSE;
    private Boolean consumoServicioCUNRNPNHabilitado = Boolean.FALSE;
    private Boolean luceneEnabled = Boolean.FALSE;

    public MatriculaEstudianteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            agregar();
            validarAcceso();
            consultarMatriculaParcial();
            if (seccionId != null) {
                this.seccionEnEdicion = seccionClient.obtenerPorId(seccionId);
            }
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MATRICULA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void updateAddressCoordinates() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String lat = params.get("latitude");
        String lng = params.get("longitude");
        SgDireccion dir = this.getDireccionEnEdicion();
        if (lat != null) {
            dir.setDirLatitud(Double.parseDouble(lat));
        }
        if (lng != null) {
            dir.setDirLongitud(Double.parseDouble(lng));
        }
    }

    public void consultarMatriculaParcial() {
        try {
            filtro.setCodigoUsuarioCreador(this.sessionBean.getUser().getName());
            filtro.setIncluirCampos(new String[]{"matEstNie",
                "matEstPrimerNombre", "matEstSegundoNombre", "matEstPrimerApellido",
                "matEstSegundoApellido"
            });
            matPendientes = restClient.buscarMatriculasParciales(filtro);
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void cargarMatriculaParcial(Long matPk) {
        try {
            matriculaParcialEdicion = restClient.obtenerMatriculaParcialPorId(matPk);
            ObjectMapperContextResolver contextResolver = new ObjectMapperContextResolver();
            this.entidadEnEdicion = contextResolver.getDefaultMapper().readValue(matriculaParcialEdicion.getMatJson(), SgMatricula.class);
            this.entidadEnEdicionEstudiante = this.entidadEnEdicion.getMatEstudiante();
            if (this.entidadEnEdicionEstudiante.getEstPersona().getPerResponsable() != null) {
                this.estudianteReferente = this.entidadEnEdicionEstudiante.getEstPersona().getPerResponsable();
                responsableEsFamiliarSelected();
                if (this.estudianteReferente.getAllTipoParentesco() != null) {
                    this.comboTipoParentesco.setSelectedT(this.estudianteReferente.getAllTipoParentesco());
                }
            }
            this.matPendientes = new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarMatriculaParcial(SgMatriculaParcial mat) {
        try {
            restClient.eliminarMatriculaParcial(mat.getMatPk());
            matPendientes.remove(mat);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            fechaRegistro = LocalDate.now();
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            SgConfiguracion ccun = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_CUN_RNPN_HABILITADO);
            SgConfiguracion cdui = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_CONSUMO_SERVICIO_DUI_RNPN_HABILITADO);

            if (ccun != null && ccun.getConValor() != null) {
                consumoServicioCUNRNPNHabilitado = Boolean.parseBoolean(ccun.getConValor());
            }

            if (cdui != null && cdui.getConValor() != null) {
                consumoServicioDUIRNPNHabilitado = Boolean.parseBoolean(cdui.getConValor());
            }

            SgConfiguracion c = restCatalogo.buscarConfiguracionPorCodigo(Constantes.CONFIG_BUSQUEDA_PERSONAS_SIMILARES_LUCENE_HABILITADA);
            if (c != null && !StringUtils.isBlank(c.getConValor())) {
                luceneEnabled = Boolean.parseBoolean(c.getConValor());
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    private void limpiarCombos() {
        fechaRegistro = LocalDate.now();
    }

    public void agregar() {
        entidadEnEdicion = new SgMatricula();
        restablecerEstudiante();
        restablecerResponsable();
    }

    public void restablecerEstudiante() {
        entidadEnEdicionEstudiante = new SgEstudiante();
        entidadEnEdicionEstudiante.getEstPersona().setPerTieneIdentificacion(Boolean.TRUE);
    }

    public void restablecerResponsable() {
        estudianteReferente = new SgAllegado();
        estudianteReferente.setAllReferente(Boolean.TRUE);
    }

    public void actualizar(SgMatricula var, SgEstudiante var2) {
        limpiarCombos();
        entidadEnEdicion = (SgMatricula) SerializationUtils.clone(var);
        entidadEnEdicionEstudiante = (SgEstudiante) SerializationUtils.clone(var2);
    }

    public void guardarYSalir() {
        Boolean guardoOk = this.confirmar();
        if (guardoOk) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToIndex();
        }
    }

    public void guardarYNuevo() {
        Boolean guardoOk = this.confirmar();
        if (guardoOk) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.refreshCurrentPage();
        }
    }

    public void guardarYNuevoEnMismaSeccion() {
        Boolean guardoOk = this.confirmar();
        if (guardoOk) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.MATRICULA + "?seccionId=" + this.entidadEnEdicion.getMatSeccion().getSecPk());
        }
    }

    public Boolean confirmar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "Código constancia: " + entidadEnEdicion.getMatCodigoConstancia(), "");
            if (nieGenaradoAutomatico || nieGenaradoAutomaticoGradoRequiereNie) {
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "NIE: " + entidadEnEdicion.getMatEstudiante().getEstPersona().getPerNie(), "");
            }

            return Boolean.TRUE;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return Boolean.FALSE;
    }

    public Boolean guardadoParcial() {
        try {
            if (this.matriculaParcialEdicion == null) {
                matriculaParcialEdicion = new SgMatriculaParcial();
            }
            matriculaParcialEdicion.actualizar(entidadEnEdicion);
            matriculaParcialEdicion = restClient.guardarParcial(matriculaParcialEdicion);
            return Boolean.TRUE;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return Boolean.FALSE;
    }

    public SgPersona getPersonaEnEdicion() {
        identificacionComponenteBean.setRenderOpcionTieneIdentificacion(null);
        if (this.wizardStep.equals("responsable") || this.wizardStep.equals("identificacionResponsable")) {
            if (estudianteReferente.getAllPersona().getPerPk() == null) {
                estudianteReferente.getAllPersona().setPerTieneIdentificacion(Boolean.TRUE);
                identificacionComponenteBean.setRenderOpcionTieneIdentificacion(Boolean.FALSE);
            }
            return this.estudianteReferente.getAllPersona();
        }
        return this.entidadEnEdicionEstudiante.getEstPersona();
    }

    public SgDireccion getDireccionEnEdicion() {
        if (this.wizardStep.equals("responsable") || this.wizardStep.equals("identificacionResponsable")) {
            return this.estudianteReferente.getAllPersona().getPerDireccion();
        }
        return this.entidadEnEdicionEstudiante.getEstPersona().getPerDireccion();
    }

    public Boolean getIdentificacionSoloLectura() {
        try {
            if (this.wizardStep.equals("datosPersonales") || this.wizardStep.equals("responsable")) {
                return Boolean.TRUE;
            }
            if (this.wizardStep.equals("identificacion")) {
                return (this.soloLectura || BooleanUtils.isTrue(this.entidadEnEdicionEstudiante.getEstPersona().getPerSeBuscoEnBd()));
            }
            if (this.wizardStep.equals("identificacionResponsable")) {
                return (this.soloLectura || BooleanUtils.isTrue(this.estudianteReferente.getAllPersona().getPerSeBuscoEnBd()));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return this.soloLectura;
    }

    public Boolean getRenderPartidaNacimiento() {
        if (this.wizardStep.equals("datosPersonales")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getPersonaSoloLectura() {
        return this.soloLectura;
    }

    public Boolean getDireccionSoloLectura() {
        if (this.wizardStep.equals("responsable") || this.wizardStep.equals("identificacionResponsable")) {
            if (this.estudianteReferente != null && this.estudianteReferente.getAllPersona() != null
                    && this.estudianteReferente.getAllPersona().getPerPk() == null) {
                return Boolean.FALSE;
            }
            return (this.soloLectura || BooleanUtils.isTrue(this.estudianteReferente.getAllPersona().getPerSeEncontroIdentificacion()));
        } else {
            if (this.entidadEnEdicionEstudiante != null && this.entidadEnEdicionEstudiante.getEstPersona() != null
                    && this.entidadEnEdicionEstudiante.getEstPersona().getPerPk() == null) {
                return Boolean.FALSE;
            }
            return (this.soloLectura || BooleanUtils.isTrue(this.entidadEnEdicionEstudiante.getEstPersona().getPerSeEncontroIdentificacion()));
        }
    }

    public void responsableEsFamiliarSelected() {
        try {
            if (comboTipoParentesco == null) {
                FiltroCodiguera fc = new FiltroCodiguera();
                fc.setIncluirCampos(new String[]{"tpaNombre", "tpaVersion"});
                List<SgTipoParentesco> list = restCatalogo.buscarTipoParentesco(fc);
                comboTipoParentesco = new SofisComboG(list, "tpaNombre");
                comboTipoParentesco.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String onFlowProcess(FlowEvent event) {
        this.wizardStep = event.getNewStep();
        try {
            JSFUtils.setFocus("tmsg");
            if (event.getOldStep() == null) {
                return this.wizardStep;
            }
            switch (event.getOldStep()) {
                case "identificacion":
                    try {
                    if (BooleanUtils.isTrue(entidadEnEdicionEstudiante.getEstPersona().getPerSeBuscoEnBd())) {
                        //Si ya se buscó, no lo volvemos a buscar
                        return wizardStep;
                    }
                    this.matPendientes = new ArrayList<>();
                    PrimeFaces.current().ajax().update("form:matPendientes");
                    entidadEnEdicionEstudiante.getEstPersona().setPerSeEncontroIdentificacion(Boolean.FALSE);
                    if (this.entidadEnEdicionEstudiante.getEstPersona().getPerTieneIdentificacion()) {
                        if (this.entidadEnEdicionEstudiante.getEstPersona().getPerIngresoAlgunaIdentificacion()) {
                            this.personaClient.validarIdentificaciones(this.entidadEnEdicionEstudiante.getEstPersona());
                            entidadEnEdicionEstudiante.getEstPersona().setPerSeBuscoEnBd(Boolean.TRUE);
                            List<SgPersona> listaPersonas = this.personaClient.obtenerPersonasPorIdentificacion(this.entidadEnEdicionEstudiante.getEstPersona());

                            if (StringUtils.isNotBlank(this.entidadEnEdicionEstudiante.getEstPersona().getPerDui())
                                    && BooleanUtils.isTrue(consumoServicioDUIRNPNHabilitado)
                                    && listaPersonas.isEmpty()) {
                                try {
                                    SgPersona p = this.personaClient.obtenerPersonaDesdeRNPNPorDUI(this.entidadEnEdicionEstudiante.getEstPersona().getPerDui());
                                    listaPersonas.add(p);
                                } catch (BusinessException be) {
                                    JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
                                    this.wizardStep = event.getOldStep();
                                    return this.wizardStep;
                                } catch (Exception ex) {
                                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                                    //Error de consumo 
                                }
                            }

                            if (this.entidadEnEdicionEstudiante.getEstPersona().getPerCun() != null
                                    && BooleanUtils.isTrue(consumoServicioCUNRNPNHabilitado)
                                    && listaPersonas.isEmpty()) {
                                try {
                                    SgPersona p = this.personaClient.obtenerPersonaDesdeRNPNPorCUN(this.entidadEnEdicionEstudiante.getEstPersona().getPerCun());
                                    listaPersonas.add(p);
                                } catch (BusinessException be) {
                                    JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
                                    this.wizardStep = event.getOldStep();
                                    return this.wizardStep;
                                } catch (Exception ex) {
                                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                                    //Error de consumo 
                                }
                            }

                            if (listaPersonas.isEmpty()) {
                                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.PERSONA_NO_ENCONTRADA), "");
                                if (this.entidadEnEdicionEstudiante.getEstPersona().getPerNie() != null || this.entidadEnEdicionEstudiante.getEstPersona().getPerNip() != null) {
                                    entidadEnEdicionEstudiante.getEstPersona().setPerSeBuscoEnBd(Boolean.FALSE); // En estos casos no se deja avanzar
                                    this.wizardStep = event.getOldStep();
                                }
                            } else if (listaPersonas.size() > 1) {
                                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.MULTIPLES_PERSONAS_CON_MISMA_IDENTIFICACION), "");
                                this.wizardStep = event.getOldStep();
                            } else if (listaPersonas.size() == 1) {
                                //Buscamos si persona ya existe como estudiante
                                if (!intercambiarEstudiante(listaPersonas.get(0))) {
                                    this.wizardStep = event.getOldStep();
                                }
                            }
                            return this.wizardStep;
                        } else {
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.IDENTIFICACION_NO_INGRESADA), "");
                            this.wizardStep = event.getOldStep();
                            return wizardStep;
                        }
                    }
                } catch (Exception ex) {
                    entidadEnEdicionEstudiante.getEstPersona().setPerSeEncontroIdentificacion(Boolean.FALSE);
                    entidadEnEdicionEstudiante.getEstPersona().setPerSeBuscoEnBd(Boolean.FALSE);
                    throw ex;
                }
                return this.wizardStep;
                case "datosPersonales":
                    if (event.getNewStep().equals("identificacion")) {
                        return this.wizardStep;
                    }

                    //Validaciones
                    estudianteClient.validar(this.entidadEnEdicionEstudiante); //TODO: sacar

                    if (avanzarWizardDatosPersonales) {
                        avanzarWizardDatosPersonales = Boolean.FALSE;
                        return this.wizardStep;
                    }
                    //Verificar persona similar
                    if (this.entidadEnEdicionEstudiante.getEstPersona().getPerPk() == null && this.personasSimilares(this.entidadEnEdicionEstudiante.getEstPersona()) > 0L) {
                        PrimeFaces.current().ajax().update("form:itemDetailCandidatos");
                        PrimeFaces.current().executeScript("PF('itemDialogCandidatos').show()");
                        this.wizardStep = event.getOldStep();
                        return this.wizardStep;
                    }
                    if (this.wizardStep.equals(event.getNewStep())) {
                        entidadEnEdicion.setMatEstudiante(entidadEnEdicionEstudiante);
                        if (this.seccionEnEdicion != null) {
                            //Si secc está definida, entonces la selecciono 
                            seleccionarSeccion(this.seccionEnEdicion);
                        }
                        if (!this.guardadoParcial()) {
                            this.wizardStep = event.getOldStep();
                        }
                    }
                    return this.wizardStep;
                case "identificacionResponsable":
                    if (event.getNewStep().equals("datosPersonales")) {
                        return this.wizardStep;
                    }
                    if (this.entidadEnEdicionEstudiante.getEstPersona().getPerEsMayorDeEdad() && !this.ingresarResponsableEstudianteMayorEdad) {
                        this.wizardStep = "matricula";
                        restablecerResponsable();

                        return this.wizardStep;
                    }
                    try {
                        if (BooleanUtils.isTrue(estudianteReferente.getAllPersona().getPerSeBuscoEnBd())) {
                            //Si ya se buscó, no lo volvemos a buscar
                            return wizardStep;
                        }
                        estudianteReferente.getAllPersona().setPerSeEncontroIdentificacion(Boolean.FALSE);
                        if (this.estudianteReferente.getAllPersona().getPerTieneIdentificacion()) {
                            if (this.estudianteReferente.getAllPersona().getPerIngresoAlgunaIdentificacion()) {
                                this.personaClient.validarIdentificaciones(this.estudianteReferente.getAllPersona());
                                estudianteReferente.getAllPersona().setPerSeBuscoEnBd(Boolean.TRUE);
                                List<SgPersona> listaPersonas = this.personaClient.obtenerPersonasPorIdentificacion(this.estudianteReferente.getAllPersona());

                                if (StringUtils.isNotBlank(this.estudianteReferente.getAllPersona().getPerDui())
                                        && BooleanUtils.isTrue(consumoServicioDUIRNPNHabilitado)
                                        && listaPersonas.isEmpty()) {
                                    try {
                                        SgPersona p = this.personaClient.obtenerPersonaDesdeRNPNPorDUI(this.estudianteReferente.getAllPersona().getPerDui());
                                        listaPersonas.add(p);
                                    } catch (BusinessException be) {
                                        JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
                                        this.wizardStep = event.getOldStep();
                                        return this.wizardStep;
                                    } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                                        //Error de consumo 
                                    }
                                }

                                if (this.estudianteReferente.getAllPersona().getPerCun() != null
                                        && BooleanUtils.isTrue(consumoServicioCUNRNPNHabilitado)
                                        && listaPersonas.isEmpty()) {
                                    try {
                                        SgPersona p = this.personaClient.obtenerPersonaDesdeRNPNPorCUN(this.estudianteReferente.getAllPersona().getPerCun());
                                        listaPersonas.add(p);
                                    } catch (BusinessException be) {
                                        JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
                                        this.wizardStep = event.getOldStep();
                                        return this.wizardStep;
                                    } catch (Exception ex) {
                                        LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                                        //Error de consumo 
                                    }
                                }

                                if (listaPersonas.isEmpty()) {
                                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.PERSONA_NO_ENCONTRADA), "");
                                    if (this.estudianteReferente.getAllPersona().getPerNie() != null || this.estudianteReferente.getAllPersona().getPerNip() != null) {
                                        estudianteReferente.getAllPersona().setPerSeBuscoEnBd(Boolean.FALSE);
                                        this.wizardStep = event.getOldStep();
                                    }
                                } else if (listaPersonas.size() > 1) {
                                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.MULTIPLES_PERSONAS_CON_MISMA_IDENTIFICACION), "");
                                    this.wizardStep = event.getOldStep();
                                } else if (listaPersonas.size() == 1) {
                                    if (!intercambiarResponsable(listaPersonas.get(0))) {
                                        this.wizardStep = event.getOldStep();
                                    }
                                }
                                return this.wizardStep;
                            } else {
                                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.IDENTIFICACION_NO_INGRESADA), "");
                                this.wizardStep = event.getOldStep();
                                return wizardStep;
                            }
                        }
                    } catch (Exception ex) {
                        estudianteReferente.getAllPersona().setPerSeEncontroIdentificacion(Boolean.FALSE);
                        estudianteReferente.getAllPersona().setPerSeBuscoEnBd(Boolean.FALSE);
                        throw ex;
                    }
                    return this.wizardStep;
                case "responsable":
                    if (event.getNewStep().equals("identificacionResponsable")) {
                        return this.wizardStep;
                    }

                    if (this.comboTipoParentesco != null) {
                        this.estudianteReferente.setAllTipoParentesco(this.comboTipoParentesco.getSelectedT());
                    }

                    if (this.estudianteReferente.getAllPersona().getPerPk() != null && this.entidadEnEdicionEstudiante.getEstPersona().getPerPk() != null) {
                        estudianteReferente.setAllPersonaReferenciada(this.entidadEnEdicionEstudiante.getEstPersona());
                    }
                    allegadoClient.validar(this.estudianteReferente);

                    if (avanzarWizardDatosPersonales) {
                        avanzarWizardDatosPersonales = Boolean.FALSE;
                        return this.wizardStep;
                    }

                    if (this.estudianteReferente.getAllPersona().getPerPk() == null && this.personasSimilares(this.estudianteReferente.getAllPersona()) > 0L) {
                        PrimeFaces.current().ajax().update("form:itemDetailCandidatos");
                        PrimeFaces.current().executeScript("PF('itemDialogCandidatos').show()");
                        this.wizardStep = event.getOldStep();
                        return this.wizardStep;
                    }

                    entidadEnEdicionEstudiante.getEstPersona().setPerResponsable(estudianteReferente);
                    entidadEnEdicion.setMatEstudiante(entidadEnEdicionEstudiante);
                    if (!this.guardadoParcial()) {
                        this.wizardStep = event.getOldStep();
                        return this.wizardStep;
                    }

                    return this.wizardStep;
                case "matricula":
                    //validarGradoNuevaMatricula(entidadEnEdicion);
                    if (event.getNewStep().equals("responsable")) {
                        if (this.entidadEnEdicionEstudiante.getEstPersona().getPerEsMayorDeEdad() && !this.ingresarResponsableEstudianteMayorEdad) {
                            this.wizardStep = "datosPersonales";
                        }
                        return this.wizardStep;
                    }
                    entidadEnEdicion.setMatEstado(EnumMatriculaEstado.ABIERTO);
                    this.restClient.validar(entidadEnEdicion);
                    return this.wizardStep;
                case "confirmacion":
                    if (event.getNewStep().equals("matricula")) {
                        return this.wizardStep;
                    }
                    return this.wizardStep;
                default:
                    return this.wizardStep;
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            this.wizardStep = event.getOldStep();
            return this.wizardStep;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            this.wizardStep = event.getOldStep();
            return this.wizardStep;
        }
    }

//    private void validarGradoNuevaMatricula(SgMatricula entity) {
//        try {
//            if (entidadEnEdicionEstudiante.getEstPk() != null) {
//
//                FiltroMatricula fMatricula = new FiltroMatricula();
//                fMatricula.setMatEstudiantePk(entidadEnEdicionEstudiante.getEstPk());
//                fMatricula.setOrderBy(new String[]{"matFechaIngreso"});
//                fMatricula.setAscending(new boolean[]{false});
//                fMatricula.setMaxResults(1L);
//                fMatricula.setIncluirCampos(new String[]{"matEstado",
//                    "matSeccion",
//                    "matVersion"});
//                List<SgMatricula> matriculas = restClient.buscar(fMatricula);
//                SgMatricula ultimaMatriculaCerrada = matriculas.isEmpty() ? null : matriculas.get(0);
//                if (ultimaMatriculaCerrada != null) {
//                    if (!ultimaMatriculaCerrada.getMatEstado().equals(EnumMatriculaEstado.ABIERTO)) {
//                        SgGrado ultimoGradoCursado = ultimaMatriculaCerrada.getMatSeccion().getSecServicioEducativo().getSduGrado();
//                        SgGrado gradoActual = entity.getMatSeccion().getSecServicioEducativo().getSduGrado();
//
//                        if (ultimoGradoCursado.getGraRelacionModalidad().getReaPk().equals(gradoActual.getGraRelacionModalidad().getReaPk())) {
//                            if (ultimoGradoCursado.getGraOrden() > gradoActual.getGraOrden()) {
//                                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Etiquetas.getValue("gradoNuevoMatriculaMenor"), "");
//                            }
//                        } else {
//                            SgModalidad modalidadCursada = ultimoGradoCursado.getGraRelacionModalidad().getReaModalidadEducativa();
//                            SgModalidad modalidadActual = gradoActual.getGraRelacionModalidad().getReaModalidadEducativa();
//
//                            if (modalidadCursada.getModCiclo().getCicNivel().getNivOrden() != null && modalidadActual.getModCiclo().getCicNivel().getNivOrden() != null) {
//                                if (modalidadCursada.getModCiclo().getCicNivel().getNivOrden()
//                                        > modalidadActual.getModCiclo().getCicNivel().getNivOrden()) {
//                                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Etiquetas.getValue("gradoNuevoMatriculaMenor"), "");
//                                } else {
//                                    if (modalidadCursada.getModCiclo().getCicNivel().getNivOrden().equals(
//                                            modalidadActual.getModCiclo().getCicNivel().getNivOrden())) {
//
//                                        if (modalidadCursada.getModCiclo().getCicOrden() != null && modalidadActual.getModCiclo().getCicOrden() != null) {
//                                            if (modalidadCursada.getModCiclo().getCicOrden()
//                                                    > modalidadActual.getModCiclo().getCicOrden()) {
//                                                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Etiquetas.getValue("gradoNuevoMatriculaMenor"), "");
//                                            } else {
//                                                if (modalidadCursada.getModCiclo().getCicOrden().equals(
//                                                        modalidadActual.getModCiclo().getCicOrden())) {
//                                                    if (modalidadCursada.getModOrden() != null && modalidadActual.getModOrden() != null) {
//                                                        if (modalidadCursada.getModOrden() > modalidadActual.getModOrden()) {
//                                                            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Etiquetas.getValue("gradoNuevoMatriculaMenor"), "");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//
//                            }
//
//                        }
//                    }
//
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, null, ex);
//        }
//
//    }
    private Boolean intercambiarEstudiante(SgPersona p) throws Exception {

        SgPersona personaIngresada = entidadEnEdicionEstudiante.getEstPersona();

        if (entidadEnEdicionEstudiante.getEstPk() == null && p.getPerPk() == null) {
            //Nuevo caso persona consultada RNPN
            entidadEnEdicionEstudiante.setEstPersona(p);
            entidadEnEdicionEstudiante.getEstPersona().normalizarDatos();
            entidadEnEdicionEstudiante.getEstPersona().actualizarIdentificaciones(personaIngresada);
            entidadEnEdicionEstudiante.getEstPersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            entidadEnEdicionEstudiante.getEstPersona().setPerSeBuscoEnBd(Boolean.TRUE);
            return Boolean.TRUE;
        }

        Boolean resultado = Boolean.FALSE;
        FiltroEstudiante fe = new FiltroEstudiante();
        fe.setEstPersona(p.getPerPk());
        fe.setIncluirCampos(new String[]{"estVersion", "estPersona.perHabilitado"});
        List<SgEstudiante> estudiantes = this.estudianteClient.buscar(fe);
        if (!estudiantes.isEmpty()) {
            if (BooleanUtils.isTrue(estudiantes.get(0).getEstPersona().getPerHabilitado())) {
                //Estudiante existente
                resultado = Boolean.TRUE;
                entidadEnEdicionEstudiante = this.estudianteClient.obtenerPorId(estudiantes.get(0).getEstPk(), Boolean.FALSE);

                if (entidadEnEdicionEstudiante.getEstPk() != null) {
                    FiltroMatricula fm = new FiltroMatricula();
                    fm.setMatEstado(EnumMatriculaEstado.ABIERTO);
                    fm.setMatEstudiantePk(entidadEnEdicionEstudiante.getEstPk());
                    Long cantidad = restClient.buscarTotal(fm);
                    if (cantidad > 0) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_MATRICULA_ABIERTA_ESTUDIANTE), "");
                        return Boolean.FALSE;
                    }
                }
            }
        } else {
            //Seteamos persona existente a nuevo estudiante
            SgPersona persona = personaClient.obtenerPorId(p.getPerPk());
            if (BooleanUtils.isTrue(persona.getPerHabilitado())) {
                resultado = Boolean.TRUE;
                entidadEnEdicionEstudiante.setEstPersona(persona);
            }
        }
        if (resultado) {
            this.entidadEnEdicionEstudiante.getEstPersona().normalizarDatos();
            this.entidadEnEdicionEstudiante.getEstPersona().actualizarIdentificaciones(personaIngresada);
            this.entidadEnEdicionEstudiante.getEstPersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            this.entidadEnEdicionEstudiante.getEstPersona().setPerSeBuscoEnBd(Boolean.TRUE);
        } else {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONA_RETIRADO), "");
        }
        return resultado;
    }

    public Boolean intercambiarResponsable(SgPersona p) throws Exception {

        SgPersona personaIngresada = estudianteReferente.getAllPersona();

        if (estudianteReferente.getAllPk() == null && p.getPerPk() == null) {
            //Nuevo caso persona consultada RNPN
            estudianteReferente.setAllPersona(p);
            estudianteReferente.getAllPersona().normalizarDatos();
            estudianteReferente.getAllPersona().actualizarIdentificaciones(personaIngresada);
            estudianteReferente.getAllPersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            estudianteReferente.getAllPersona().setPerSeBuscoEnBd(Boolean.TRUE);
            return Boolean.TRUE;
        }

        Boolean resultado = Boolean.FALSE;
        //Si estudiante ya existente, buscamos si persona ya existe como familiar
        if (this.entidadEnEdicionEstudiante.getEstPk() != null) {
            FiltroAllegado fe = new FiltroAllegado();
            fe.setAllPersonaReferenciadaPk(this.entidadEnEdicionEstudiante.getEstPersona().getPerPk());
            fe.setAllPersonaPk(p.getPerPk());
            fe.setIncluirCampos(new String[]{"allVersion", "allPersona.perHabilitado"});
            List<SgAllegado> familiares = this.allegadoClient.buscar(fe);
            if (!familiares.isEmpty()) {
                if (BooleanUtils.isTrue(familiares.get(0).getAllPersona().getPerHabilitado())) {
                    //Familiar existente
                    resultado = Boolean.TRUE;
                    estudianteReferente = this.allegadoClient.obtenerPorId(familiares.get(0).getAllPk());
                }
            } else {
                //Seteamos persona existente a nuevo familiar
                SgPersona persona = personaClient.obtenerPorId(p.getPerPk());
                if (BooleanUtils.isTrue(persona.getPerHabilitado())) {
                    resultado = Boolean.TRUE;
                    estudianteReferente.setAllPersona(persona);
                }
            }
        } else {
            //Seteamos persona existente a nuevo familiar
            SgPersona persona = personaClient.obtenerPorId(p.getPerPk());
            if (BooleanUtils.isTrue(persona.getPerHabilitado())) {
                resultado = Boolean.TRUE;
                estudianteReferente.setAllPersona(persona);
            }
        }
        if (resultado) {
            responsableEsFamiliarSelected();
            if (this.estudianteReferente.getAllTipoParentesco() != null) {
                this.comboTipoParentesco.setSelectedT(this.estudianteReferente.getAllTipoParentesco());
            }
            this.estudianteReferente.getAllPersona().normalizarDatos();
            this.estudianteReferente.getAllPersona().actualizarIdentificaciones(personaIngresada);
            this.estudianteReferente.getAllPersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            this.estudianteReferente.getAllPersona().setPerSeBuscoEnBd(Boolean.TRUE);
        } else {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONA_RETIRADO), "");
        }
        return resultado;
    }

    private Long personasSimilares(SgPersona p) throws Exception {
        if (!luceneEnabled) {
            return 0L;
        }
        FiltroPersona fp = new FiltroPersona();
        fp.setPerPrimerNombre(p.getPerPrimerNombre());
        fp.setPerPrimerApellido(p.getPerPrimerApellido());
        fp.setPerSegundoNombre(p.getPerSegundoNombre());
        fp.setPerSegundoApellido(p.getPerSegundoApellido());
        fp.setPerTercerNombre(p.getPerTercerNombre());
        fp.setPerTercerApellido(p.getPerTercerApellido());
        fp.setPerSexoPk(p.getPerSexo() != null ? p.getPerSexo().getSexPk() : null);
        if (p.getPerFechaNacimiento() != null) {
            fp.setPerFechaNacimientoDesde(p.getPerFechaNacimiento().minusDays(30));
            fp.setPerFechaNacimientoHasta(p.getPerFechaNacimiento().plusDays(30));
        }
        fp.setIncluirCampos(new String[]{
            "perNie",
            "perDui",
            "perPrimerNombre",
            "perSegundoNombre",
            "perTercerNombre",
            "perPrimerApellido",
            "perSegundoApellido",
            "perTercerApellido",
            "perFechaNacimiento",
            "perSexo.sexNombre",
            "perDireccion.dirDepartamento.depNombre",
            "perDireccion.dirMunicipio.munNombre",
            "perEmail",
            "perEstadoCivil.eciNombre"});
        totalResultadosPersonas = personaClient.buscarTotalLucene(fp);
        lazyPersonaDataModel = new LazyLucenePersonaDataModel(personaClient, fp, totalResultadosPersonas, paginado);
        lazyPersonaDataModel.setLuceneEnabled(luceneEnabled);
        if (totalResultadosPersonas <= 0) {
            lazyPersonaDataModel = null;
        }
        return totalResultadosPersonas;
    }

    public String noEsNinguno() {
        entidadEnEdicionEstudiante.getEstPersona().setPerResponsable(estudianteReferente);
        entidadEnEdicion.setMatEstudiante(entidadEnEdicionEstudiante);
        if (this.seccionEnEdicion != null) {
            //Si secc está definida, entonces la selecciono 
            seleccionarSeccion(this.seccionEnEdicion);
        }
        if (!this.guardadoParcial()) {
            return null;
        }
        lazyPersonaDataModel = null;
        avanzarWizardDatosPersonales = Boolean.TRUE;
        PrimeFaces.current().ajax().update("form:itemDetailCandidatos");
        PrimeFaces.current().executeScript("PF('itemDialogCandidatos').hide()");
        if (this.wizardStep.equals("datosPersonales")) {
            PrimeFaces.current().executeScript("PF('matriculaWizard').loadStep('identificacionResponsable', false)");
        } else if (this.wizardStep.equals("responsable")) {
            PrimeFaces.current().executeScript("PF('matriculaWizard').loadStep('matricula', false)");
        }
        return null;
    }

    public String intercambiarPersona(SgPersona per) {
        try {
            if (this.wizardStep.equals("datosPersonales")) {
                intercambiarEstudiante(per);
            } else if (this.wizardStep.equals("responsable")) {
                intercambiarResponsable(per);
            }
            lazyPersonaDataModel = null;
            PrimeFaces.current().ajax().update("form:wizMatricula");
            PrimeFaces.current().ajax().update("form:itemDetailCandidatos");
            PrimeFaces.current().executeScript("PF('itemDialogCandidatos').hide()");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void seleccionarSeccion(SgSeccion sec) {
        try {
            nieGenaradoAutomatico = Boolean.FALSE;
            nieGenaradoAutomaticoGradoRequiereNie = Boolean.FALSE;
            entidadEnEdicion.setMatSeccion(sec);
            entidadEnEdicion.setMatProvisional(Boolean.FALSE);
            entidadEnEdicion.setMatObservacioneProvisional(null);
            entidadEnEdicion.setMatFechaIngreso(null);
            if (sec != null) {

                if (!sec.getSecServicioEducativo().getSduEstado().equals(EnumServicioEducativoEstado.HABILITADO)) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SERVICIO_EDUCATIVO_NO_HABILITADO), "");

                }

                entidadEnEdicion.setMatFechaIngreso(restCatalogo.obtenerFechaIngresoMatriculasPorDefecto(
                        sec.getSecAnioLectivo().getAleAnio(), sec.getSecServicioEducativo().getSduSede().getSedTipoCalendario()));

                if ((this.entidadEnEdicionEstudiante.getEstPersona().getPerNie() == null && sec.getSecServicioEducativo().getSduGrado().getGraRequiereNIE())
                        && !sessionBean.getOperaciones().contains(ConstantesOperaciones.PERMITE_MATRICULAR_SIN_NIE)) {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GRADO_REQUIERE_NIE), "");
                    return;
                }

                if ((this.entidadEnEdicionEstudiante.getEstPersona().getPerNie() == null && sec.getSecServicioEducativo().getSduGrado().getGraRequiereNIE()
                        && sessionBean.getOperaciones().contains(ConstantesOperaciones.PERMITE_MATRICULAR_SIN_NIE)
                        && sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_NIE))
                        && (BooleanUtils.isFalse(this.entidadEnEdicionEstudiante.getEstPersona().getPerPartidaNacimientoPresenta()))) {
                    nieGenaradoAutomaticoGradoRequiereNie = Boolean.TRUE;
                    this.entidadEnEdicion.setMatProvisional(Boolean.TRUE);
                    this.soloLecturaProvisional = Boolean.TRUE;
                    this.entidadEnEdicion.setMatObservacioneProvisional(Etiquetas.getValue("observacionProvisionalPartidaNacimiento"));
                } else {

                    if ((this.entidadEnEdicionEstudiante.getEstPersona().getPerNie() == null && sec.getSecServicioEducativo().getSduGrado().getGraRequiereNIE())
                            && (BooleanUtils.isFalse(this.entidadEnEdicionEstudiante.getEstPersona().getPerPartidaNacimientoPresenta()))) {
                        this.entidadEnEdicion.setMatProvisional(Boolean.TRUE);
                        this.soloLecturaProvisional = Boolean.TRUE;
                        this.entidadEnEdicion.setMatObservacioneProvisional(Etiquetas.getValue("observacionProvisional"));
                    }
                }
                if (this.entidadEnEdicionEstudiante.getEstPersona().getPerNie() == null && !sec.getSecServicioEducativo().getSduGrado().getGraRequiereNIE()) {
                    nieGenaradoAutomatico = Boolean.TRUE;
                }

                if (entidadEnEdicion.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion() != null
                        && entidadEnEdicion.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel() != null) {
                    FiltroPeriodoCalendario fpc = new FiltroPeriodoCalendario();
                    fpc.setCesModalidadAtencionFk(entidadEnEdicion.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                    fpc.setCesNivelFk(entidadEnEdicion.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                    fpc.setCesSubModalidadAtencionFk(entidadEnEdicion.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? entidadEnEdicion.getMatSeccion().getSecPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                    fpc.setCesHabilitado(Boolean.TRUE);
                    fpc.setCesTipo(EnumCalendarioEscolar.MAT);
                    fpc.setFechaCalificacion(LocalDate.now());
                    Long cantidad = periodoCalendarioRestClient.buscarTotal(fpc);
                    if (cantidad.equals(0L)) {
                        FiltroRelHabilitacionMatriculaNivel frhm = new FiltroRelHabilitacionMatriculaNivel();
                        frhm.setNivelAndNullOrNivelAndModAtSubMod(Boolean.TRUE);
                        frhm.setRhnNivelFk(entidadEnEdicion.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                        frhm.setRhnModalidadAtencionFk(entidadEnEdicion.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        frhm.setRhnSubmodalidadFk(entidadEnEdicion.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? entidadEnEdicion.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                        frhm.setRhnFecha(LocalDate.now());
                        frhm.setRhnSedeFk(entidadEnEdicion.getMatSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                        frhm.setRhnEstadoHabilitacion(EnumEstadoHabilitacionPeriodoMatricula.APROBADA);
                        frhm.setIncluirCampos(new String[]{"rhnVersion"});
                        frhm.setMaxResults(1L);

                        List<SgRelHabilitacionMatriculaNivel> rel = relHabilitacionMatriculaNivelRestClient.buscar(frhm);
                        if (rel.isEmpty()) {
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PERIODO_MATRICULA_CERRADO), "");
                        }

                    }
                }
            }
            PrimeFaces.current().ajax().update("form:fila_input_estudiante_ingreso_provisional", "form:fila_input_estudiante_ingreso_observatorio");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public Boolean getSoloLecturaProvisional() {
        return soloLecturaProvisional;
    }

    public FiltroMatricula getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMatricula filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorialMatricula() {
        return historialMatricula;
    }

    public void setHistorialMatricula(List<RevHistorico> historialMatricula) {
        this.historialMatricula = historialMatricula;
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

    public Long getTotalResultadosPersonas() {
        return totalResultadosPersonas;
    }

    public void setTotalResultadosPersonas(Long totalResultadosPersonas) {
        this.totalResultadosPersonas = totalResultadosPersonas;
    }

    public SgMatricula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMatricula entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgEstudiante getEntidadEnEdicionEstudiante() {
        return entidadEnEdicionEstudiante;
    }

    public void setEntidadEnEdicionEstudiante(SgEstudiante entidadEnEdicionEstudiante) {
        this.entidadEnEdicionEstudiante = entidadEnEdicionEstudiante;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgAllegado getEstudianteReferente() {
        return estudianteReferente;
    }

    public void setEstudianteReferente(SgAllegado estudianteReferente) {
        this.estudianteReferente = estudianteReferente;
    }

    public String getWizardStep() {
        return wizardStep;
    }

    public void setWizardStep(String wizardStep) {
        this.wizardStep = wizardStep;
    }

    public SofisComboG<SgTipoParentesco> getComboTipoParentesco() {
        return comboTipoParentesco;
    }

    public void setComboTipoParentesco(SofisComboG<SgTipoParentesco> comboTipoParentesco) {
        this.comboTipoParentesco = comboTipoParentesco;
    }

    public LazyLucenePersonaDataModel getLazyPersonaDataModel() {
        return lazyPersonaDataModel;
    }

    public void setLazyPersonaDataModel(LazyLucenePersonaDataModel lazyPersonaDataModel) {
        this.lazyPersonaDataModel = lazyPersonaDataModel;
    }

    public Boolean getAvanzarWizardDatosPersonales() {
        return avanzarWizardDatosPersonales;
    }

    public void setAvanzarWizardDatosPersonales(Boolean avanzarWizardDatosPersonales) {
        this.avanzarWizardDatosPersonales = avanzarWizardDatosPersonales;
    }

    public List<SgMatriculaParcial> getMatPendientes() {
        return matPendientes;
    }

    public void setMatPendientes(List<SgMatriculaParcial> matPendientes) {
        this.matPendientes = matPendientes;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
    }

    public Boolean getIngresarResponsableEstudianteMayorEdad() {
        return ingresarResponsableEstudianteMayorEdad;
    }

    public void setIngresarResponsableEstudianteMayorEdad(Boolean ingresarResponsableEstudianteMayorEdad) {
        this.ingresarResponsableEstudianteMayorEdad = ingresarResponsableEstudianteMayorEdad;
    }

    public Boolean getNieGenaradoAutomatico() {
        return nieGenaradoAutomatico;
    }

    public void setNieGenaradoAutomatico(Boolean nieGenaradoAutomatico) {
        this.nieGenaradoAutomatico = nieGenaradoAutomatico;
    }

}
