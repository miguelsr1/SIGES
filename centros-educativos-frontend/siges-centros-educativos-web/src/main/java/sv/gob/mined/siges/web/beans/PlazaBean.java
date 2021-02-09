/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSolicitudPlaza;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudPlazaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgAplicacionPlaza;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgDatoContratacion;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgPlaza;
import sv.gob.mined.siges.web.dto.catalogo.SgCalidadIngresoAplicantes;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.enumerados.EnumCaracterizacionPlaza;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAplicacionPlaza;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPlaza;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudPlaza;
import sv.gob.mined.siges.web.enumerados.EnumModeloContrato;
import sv.gob.mined.siges.web.enumerados.EnumTipoPlaza;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAplicacionPlaza;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDatoContratacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlaza;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.restclient.AplicacionPlazaRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.DatoContratacionRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.PlazaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class PlazaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PlazaBean.class.getName());

    @Inject
    private SolicitudPlazaRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ComponentePlanEstudioRestClient restComponente;

    @Inject
    private AplicacionPlazaRestClient restAplicacion;

    @Inject
    private PersonalSedeEducativaRestClient restPersonal;

    @Inject
    private PlazaRestClient restPlaza;
    
    @Inject
    private DatoContratacionRestClient datoContratacionRestClient;

    @Inject
    @Param(name = "id")
    private Long plazaId;

    @Inject
    @Param(name = "rev")
    private Long plazaRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "aprob")
    private Boolean aprobacion;

    @Inject
    @Param(name = "src")
    private Integer origen;

    @Inject
    private SeleccionarServicioEducativoBean servicio;

    @Inject
    private ApplicationBean appBean;

    private Boolean soloLectura = Boolean.FALSE;
    private SgSolicitudPlaza entidadEnEdicion;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;

    private SofisComboG<EnumTipoPlaza> comboTipoPlaza;
    private SofisComboG<SgEspecialidad> comboEspecialidad;
    private SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgJornadaLaboral> comboJornada;
    private SofisComboG<SgCargo> comboTipoNombramiento;
    private SgComponentePlanEstudio componenteSeleccionado;
    private String valor1;
    private String valor2;

    private List<EnumCaracterizacionPlaza> listCaracterizacion;
    private List<EnumEstadoPlaza> listEstadoPlaza;
    private Boolean aplicarPlaza = Boolean.FALSE;
    private String textoAplico;
    private SgPersonalSedeEducativa entidadPersonal;
    private SgPlaza plazaSeleccionada;
    private List<SgPlaza> listaPlazas;
    private List<SgPlaza> listaPlazasAux;
    private String aplicoEnCalidad;
    
    private SofisComboG<SgCalidadIngresoAplicantes> comboAplicaCalidad;

    public PlazaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            servicio.setEjecutarAction(Boolean.FALSE);
            servicio.setVerGrado(Boolean.FALSE);
            servicio.setVerCiclo(Boolean.FALSE);
            servicio.setSoloSede(Boolean.TRUE);
            servicio.setVerProgramaEducativo(Boolean.FALSE);
            entidadPersonal = restPersonal.obtenerPorId(sessionBean.getEntidadUsuario().getUsuPersonalPk(), false);
            cargarCombos();
            if (plazaId != null && plazaId > 0) {
                if (plazaRev != null && plazaRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(plazaId, plazaRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(plazaId));
                    soloLectura = editable != null ? !editable : soloLectura;
                 /*   if (!entidadEnEdicion.getSplEstado().equals(EnumEstadoSolicitudPlaza.EN_CREACION) && !soloLectura) {
                        soloLectura = Boolean.TRUE;
                    }*/
                    if (entidadEnEdicion.getSplEstado().equals(EnumEstadoSolicitudPlaza.APROBADA) && origen.equals(3)) {
                        LocalDate hoy = LocalDate.now();
                        if (appBean.estaEnRango(entidadEnEdicion.getSplPostulacionInicio(), entidadEnEdicion.getSplPostulacionFin(), hoy)) {
                            
                            if (sessionBean.getEntidadUsuario().getUsuPersonalPk() != null) {

                                if (entidadPersonal != null && BooleanUtils.isTrue(entidadPersonal.getPseDatoEmpleado().getDemPuedeAplicarPlaza()) && entidadEnEdicion.getSplTipoPlaza().equals(EnumTipoPlaza.D)) {
                                    if (aplicoPreviamente() > 0) {
                                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP_VALORACION, Etiquetas.getValue("aplicoPlaza"), "");
                                    } else {
                                        aplicarPlaza = Boolean.TRUE;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                this.agregar();
            }

            if (BooleanUtils.isTrue(aprobacion)) {

                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.APROBAR_PLAZA)) {
                    aprobacion = Boolean.FALSE;
                } else if (entidadEnEdicion.getSplEstado().equals(EnumEstadoSolicitudPlaza.APROBADA) || entidadEnEdicion.getSplEstado().equals(EnumEstadoSolicitudPlaza.RECHAZADA)) {
                    aprobacion = Boolean.FALSE;
                }
            }

            validarAcceso();
            cargarListaPlazas();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public Boolean habilitadoEdicionDatosPrincipales(){
        return BooleanUtils.isTrue(soloLectura) || BooleanUtils.isTrue(aprobacion);
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_SOLICITUD_PLAZA)) {
                JSFUtils.redirectToIndex();
            }
            if (BooleanUtils.isTrue(aprobacion)) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.APROBAR_PLAZA)) {
                    aprobacion = Boolean.FALSE;
                } else if (entidadEnEdicion.getSplEstado().equals(EnumEstadoSolicitudPlaza.APROBADA) || entidadEnEdicion.getSplEstado().equals(EnumEstadoSolicitudPlaza.RECHAZADA)) {
                    aprobacion = Boolean.FALSE;
                }
            }
        } else {
            if (entidadEnEdicion.getSplPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_SOLICITUD_PLAZA)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_SOLICITUD_PLAZA)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public String getTituloPagina() {
        if (this.plazaId == null) {
            return Etiquetas.getValue("agregarPlaza");
        } else if (this.plazaRev != null) {
            return Etiquetas.getValue("historialPlaza");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verPlaza") + " " + entidadEnEdicion.getSplTipoPlaza().getText();
        } else {
            return Etiquetas.getValue("edicionPlaza") + " " + entidadEnEdicion.getSplTipoPlaza().getText();
        }
    }

    public void cargarListaPlazas() {
        try {
            if (servicio.getSedeSeleccionada() != null) {
                FiltroPlaza fil = new FiltroPlaza();                
                fil.setSedeFk(servicio.getSedeSeleccionada().getSedPk());
                fil.setOrderBy(new String[]{"plaNombre"});
                fil.setAscending(new boolean[]{false});
                fil.setIncluirCampos(new String[]{"plaPk", "plaPartida", "plaAnioPartida","plaSubpartida", "plaVersion", "plaNombre", "plaSedeFk.sedPk", "plaSedeFk.sedTipo","plaIdPuesto"});
                listaPlazas = restPlaza.buscar(fil);
                listaPlazasAux=new ArrayList(listaPlazas);
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizarPlaza(){
        entidadEnEdicion.setSplPlazaFk(plazaSeleccionada);
        PrimeFaces.current().executeScript("PF('itemDialog').hide()");
    }

    public void seleccionarTipoPlaza() {
        this.entidadEnEdicion.setSplTipoPlaza(comboTipoPlaza != null ? comboTipoPlaza.getSelectedT() : null);
        if (entidadEnEdicion.getSplTipoPlaza() != null && entidadEnEdicion.getSplTipoPlaza().equals(EnumTipoPlaza.D)) {
            this.servicio.setSoloSede(Boolean.FALSE);
        } else {
            this.servicio.setSoloSede(Boolean.TRUE);
        }
    }
    
    public Boolean renderizarBotonSeleccionarPlaza(){
        if(BooleanUtils.isFalse(soloLectura) && (BooleanUtils.isTrue(entidadEnEdicion.getSplPlazaExistente()) && (servicio.getSedeSeleccionada() != null || entidadEnEdicion.getSplSedeSolicitante()!=null))){
            return Boolean.TRUE;
        }
        
       
        return Boolean.FALSE;
    }
    
    public Boolean habilitarSeleccionPlazaExistente(){
        
        return (((BooleanUtils.isTrue(aprobacion) || EnumEstadoSolicitudPlaza.EN_CREACION.equals( entidadEnEdicion.getSplEstado())) && BooleanUtils.isNotTrue(soloLectura)) || (entidadEnEdicion.getSplPk()==null))?Boolean.TRUE:Boolean.FALSE;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera filtroCod = new FiltroCodiguera();
            filtroCod.setHabilitado(Boolean.TRUE);
            filtroCod.setAscending(new boolean[]{true});

            List<EnumTipoPlaza> tipoPlaza = new ArrayList(Arrays.asList(EnumTipoPlaza.values()));
            comboTipoPlaza = new SofisComboG(tipoPlaza, "text");
            comboTipoPlaza.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroEspecialidad fesp = new FiltroEspecialidad();
            fesp.setHabilitado(Boolean.TRUE);
            fesp.setAscending(new boolean[]{true});
            fesp.setOrderBy(new String[]{"espNombre"});
            fesp.setIncluirCampos(new String[]{"espNombre", "espVersion"});
            List<SgEspecialidad> listaEspecialidad = restCatalogo.buscarEspecialidad(fesp);
            comboEspecialidad = new SofisComboG(listaEspecialidad, "espNombre");
            comboEspecialidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroCod.setOrderBy(new String[]{"ffiNombre"});
            filtroCod.setIncluirCampos(new String[]{"ffiNombre", "ffiVersion"});
            List<SgFuenteFinanciamiento> listaFuente = restCatalogo.buscarFuenteFinanciamiento(filtroCod);
            comboFuenteFinanciamiento = new SofisComboG(listaFuente, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroCod.setOrderBy(new String[]{"jlaNombre"});
            filtroCod.setIncluirCampos(new String[]{"jlaNombre", "jlaVersion"});
            List<SgJornadaLaboral> listaJornada = restCatalogo.buscarJornadasLaborales(filtroCod);
            comboJornada = new SofisComboG(listaJornada, "jlaNombre");
            comboJornada.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroCod.setOrderBy(new String[]{"carNombre"});
            filtroCod.setIncluirCampos(new String[]{"carNombre", "carVersion"});
            List<SgCargo> listaCargos = restCatalogo.buscarCargo(filtroCod);
            comboTipoNombramiento = new SofisComboG(listaCargos, "carNombre");
            comboTipoNombramiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            listCaracterizacion = new ArrayList(Arrays.asList(EnumCaracterizacionPlaza.values()));

            listEstadoPlaza = new ArrayList(Arrays.asList(EnumEstadoPlaza.values()));
            
            filtroCod.setOrderBy(new String[]{"ciaNombre"});
            filtroCod.setIncluirCampos(new String[]{"ciaCodigo","ciaNombre", "ciaVersion"});
            List<SgCalidadIngresoAplicantes> calidadIngreso = restCatalogo.buscarIngresoAplicantePlaza(filtroCod);
            comboAplicaCalidad = new SofisComboG(calidadIngreso, "ciaNombre");
            comboAplicaCalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            Integer valor = setValorDefecto();
            if(valor != null){
                switch(valor){
                    case 1: 
                        comboAplicaCalidad.setSelectedT(calidadIngreso.stream().filter(x -> x.getCiaCodigo().equals("001")).findFirst().get());
                        break;
                    case 2: 
                        comboAplicaCalidad.setSelectedT(calidadIngreso.stream().filter(x -> x.getCiaCodigo().equals("002")).findFirst().get());
                        break;
                    case 3: 
                        comboAplicaCalidad.setSelectedT(calidadIngreso.stream().filter(x -> x.getCiaCodigo().equals("003")).findFirst().get());
                        break;
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Integer setValorDefecto(){
        try{
 
            if(entidadPersonal != null && entidadPersonal.getPseDatoEmpleado() != null){            
                FiltroDatoContratacion fdc = new FiltroDatoContratacion();
                fdc.setDcoDatoEmpleado(entidadPersonal.getPseDatoEmpleado().getDemPk());
                fdc.setIncluirCampos(new String[]{"dcoVersion","dcoModeloContrato","dcoDesde","dcoHasta"});
                List<SgDatoContratacion> dc = datoContratacionRestClient.buscar(fdc);
                if(!dc.isEmpty()){
                    for(SgDatoContratacion x : dc){
                        if(x.getDcoModeloContrato().equals(EnumModeloContrato.ACUERDO)){
                            if(x.getDcoHasta() == null || x.getDcoHasta().isAfter(LocalDate.now())){ //Traslado
                                return 1;
                            }else{//Reingreso
                                return 2;
                            }
                        }else{//Ingreso
                            return 3;
                        }
                    }
                }else{//Ingreso
                    return 3;
                }
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void agregar() {
        entidadEnEdicion = new SgSolicitudPlaza();
    }

    public void actualizar(SgSolicitudPlaza cac) {
        try {
            if (cac == null) {
                this.agregar();
            } else {
                entidadEnEdicion = cac;
            }
            this.servicio.setSedeSeleccionada(entidadEnEdicion.getSplSedeSolicitante());
            this.servicio.setSoloLecturaCombos(Boolean.TRUE);

            comboJornada.setSelectedT(entidadEnEdicion.getSplJornada());

            plazaSeleccionada = entidadEnEdicion.getSplPlazaFk();

            if (entidadEnEdicion.getSplTipoPlaza().equals(EnumTipoPlaza.D)) {
                this.servicio.setSoloSede(Boolean.FALSE);
                this.servicio.seleccionarSede();

                this.servicio.getComboNivel().setSelectedT(entidadEnEdicion.getSplNivel());
                this.servicio.seleccionarNivel();

                this.servicio.getComboModalidad().setSelectedT(entidadEnEdicion.getSplModalidadEducativa());
                this.servicio.seleccionarModalidad();

                this.servicio.getComboOpcion().setSelectedT(entidadEnEdicion.getSplOpcion());
                this.servicio.seleccionarOpcion();

                this.servicio.getComboModalidadAtencion().setSelectedT(entidadEnEdicion.getSplModalidadAtencion());
                this.servicio.seleccionarModalidadAtencion();

                componenteSeleccionado = entidadEnEdicion.getSplComponentePlanEstudio();
                comboEspecialidad.setSelectedT(entidadEnEdicion.getSplEspecialidad());
                comboFuenteFinanciamiento.setSelectedT(entidadEnEdicion.getSplFuenteFinanciamiento());
                
                //aplicoEnCalidad = entidadEnEdicion.get
            } else {
                comboTipoNombramiento.setSelectedT(entidadEnEdicion.getSplTipoNombramiento());
            }

            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (BooleanUtils.isTrue(entidadEnEdicion.getSplPlazaExistente())) {
                entidadEnEdicion.setSplPlazaFk(plazaSeleccionada);
            } else {
                entidadEnEdicion.setSplPlazaFk(null);
            }
            entidadEnEdicion.setSplSedeSolicitante(servicio.getSedeSeleccionada());

            entidadEnEdicion.setSplNivel(servicio.getComboNivel() != null ? servicio.getComboNivel().getSelectedT() : null);
            entidadEnEdicion.setSplModalidadEducativa(servicio.getComboModalidad() != null ? servicio.getComboModalidad().getSelectedT() : null);
            entidadEnEdicion.setSplModalidadAtencion(servicio.getComboModalidadAtencion() != null ? servicio.getComboModalidadAtencion().getSelectedT() : null);
            entidadEnEdicion.setSplOpcion(servicio.getComboOpcion() != null ? servicio.getComboOpcion().getSelectedT() : null);
            entidadEnEdicion.setSplComponentePlanEstudio(componenteSeleccionado);
            entidadEnEdicion.setSplEspecialidad(comboEspecialidad != null ? comboEspecialidad.getSelectedT() : null);
            entidadEnEdicion.setSplFuenteFinanciamiento(comboFuenteFinanciamiento != null ? comboFuenteFinanciamiento.getSelectedT() : null);
            entidadEnEdicion.setSplJornada(comboJornada != null ? comboJornada.getSelectedT() : null);
            entidadEnEdicion.setSplTipoNombramiento(comboTipoNombramiento != null ? comboTipoNombramiento.getSelectedT() : null);

            if (entidadEnEdicion.getSplPk() == null) {
                entidadEnEdicion.setSplEstado(EnumEstadoSolicitudPlaza.EN_CREACION);
            }

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_PLAZAS);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgPlaza> completePlaza(String query) {
        try {

            if (servicio.getSedeSeleccionada() != null) {
                FiltroPlaza fil = new FiltroPlaza();
                //            if (securityOperation != null) {
                //                fil.setSecurityOperation(securityOperation);
                //            }
                fil.setSedeFk(servicio.getSedeSeleccionada().getSedPk());
                fil.setCodigoOnombre(query);
                fil.setMaxResults(11L);
                fil.setOrderBy(new String[]{"plaNombre"});
                fil.setAscending(new boolean[]{false});
                fil.setIncluirCampos(new String[]{"plaPk", "plaPartida", "plaSubpartida", "plaVersion", "plaNombre", "plaSedeFk.sedPk", "plaSedeFk.sedTipo","plaAnioPartida"});
                return restPlaza.buscar(fil);
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionadoSede() {
        plazaSeleccionada = null;
        entidadEnEdicion.setSplPlazaFk(null);
        cargarListaPlazas();
    }
    
    public void seleccionarPlazaExistente(){
        plazaSeleccionada = null;
        entidadEnEdicion.setSplPlazaFk(null);
    }

    public void enviar() {
        try {
            if (BooleanUtils.isTrue(entidadEnEdicion.getSplPlazaExistente())) {
                entidadEnEdicion.setSplPlazaFk(plazaSeleccionada);
            } else {
                entidadEnEdicion.setSplPlazaFk(null);
            }

            entidadEnEdicion.setSplSedeSolicitante(servicio.getSedeSeleccionada());

            entidadEnEdicion.setSplNivel(servicio.getComboNivel() != null ? servicio.getComboNivel().getSelectedT() : null);
            entidadEnEdicion.setSplModalidadEducativa(servicio.getComboModalidad() != null ? servicio.getComboModalidad().getSelectedT() : null);
            entidadEnEdicion.setSplModalidadAtencion(servicio.getComboModalidadAtencion() != null ? servicio.getComboModalidadAtencion().getSelectedT() : null);
            entidadEnEdicion.setSplOpcion(servicio.getComboOpcion() != null ? servicio.getComboOpcion().getSelectedT() : null);
            entidadEnEdicion.setSplComponentePlanEstudio(componenteSeleccionado);
            entidadEnEdicion.setSplEspecialidad(comboEspecialidad != null ? comboEspecialidad.getSelectedT() : null);
            entidadEnEdicion.setSplFuenteFinanciamiento(comboFuenteFinanciamiento != null ? comboFuenteFinanciamiento.getSelectedT() : null);
            entidadEnEdicion.setSplJornada(comboJornada != null ? comboJornada.getSelectedT() : null);
            entidadEnEdicion.setSplTipoNombramiento(comboTipoNombramiento != null ? comboTipoNombramiento.getSelectedT() : null);

            entidadEnEdicion.setSplEstado(EnumEstadoSolicitudPlaza.ENVIADA);

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_PLAZAS);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void aprobar() {
        try {
            if (BooleanUtils.isFalse(entidadEnEdicion.getSplPlazaExistente())) {
                entidadEnEdicion.setSplPlazaFk(plazaSeleccionada);                
            } 
            String codigo = "";
            if (StringUtils.isNotBlank(valor1)) {
                codigo += valor1;
            }
            if (StringUtils.isNotBlank(valor2)) {
                if (StringUtils.isNotBlank(valor1)) {
                    codigo += "-";
                }
                codigo += valor2;
            }
            entidadEnEdicion.setSplCodigo(codigo);
            entidadEnEdicion.setSplEstado(EnumEstadoSolicitudPlaza.APROBADA);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_PLAZAS);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void aplicar() {
        try {
            SgAplicacionPlaza aplicacion = new SgAplicacionPlaza();
            aplicacion.setAplCodigoUsuario(sessionBean.getEntidadUsuario().getUsuCodigo());
            aplicacion.setAplEstado(EnumEstadoAplicacionPlaza.APLICADO);
            aplicacion.setAplFechaAplico(LocalDateTime.now());
            aplicacion.setAplPersonal(entidadPersonal);
            aplicacion.setAplPlaza(entidadEnEdicion);
            aplicacion.setAplCalidadAplicantes(comboAplicaCalidad.getSelectedT());
            restAplicacion.guardar(aplicacion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.PLAZAS_DISPONIBLES);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void rechazar() {
        try {
            entidadEnEdicion.setSplEstado(EnumEstadoSolicitudPlaza.RECHAZADA);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.SOLICITUDES_PLAZAS);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgComponentePlanEstudio> completeComponente(String query) {
        try {
            FiltroComponentePlanEstudio fil = new FiltroComponentePlanEstudio();
            fil.setCpeNombre(query);
            fil.setCpeHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setIncluirCampos(new String[]{"cpeNombre", "cpeTipo", "cpeVersion"});
            fil.setOrderBy(new String[]{"cpeNombre"});
            fil.setAscending(new boolean[]{false});
            return restComponente.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public Long aplicoPreviamente() {
        try {
            FiltroAplicacionPlaza fap = new FiltroAplicacionPlaza();
            fap.setAplPlaza(entidadEnEdicion.getSplPk());
            fap.setAplPersonal(sessionBean.getEntidadUsuario().getUsuPersonalPk());
            fap.setIncluirCampos(new String[]{"aplPk","aplVersion","aplCalidadAplicantes.ciaPk","aplCalidadAplicantes.ciaCodigo","aplCalidadAplicantes.ciaNombre","aplCalidadAplicantes.ciaVersion"});
            List<SgAplicacionPlaza> aplicaciones = restAplicacion.buscar(fap);
            if(!aplicaciones.isEmpty()){
                for(SgAplicacionPlaza apl : aplicaciones){
                    if(apl.getAplCalidadAplicantes() != null){
                        aplicoEnCalidad = apl.getAplCalidadAplicantes().getCiaNombre();
                        break;
                    }
                }
                return 1L;
            }
            
            //return restAplicacion.buscarTotal(fap);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return 0L;
    }
    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public SgPlaza getPlazaSeleccionada() {
        return plazaSeleccionada;
    }

    public void setPlazaSeleccionada(SgPlaza plazaSeleccionada) {
        this.plazaSeleccionada = plazaSeleccionada;
    }

    public SeleccionarServicioEducativoBean getServicio() {
        return servicio;
    }

    public void setServicio(SeleccionarServicioEducativoBean servicio) {
        this.servicio = servicio;
    }

    public List<SgPlaza> getListaPlazas() {
        return listaPlazas;
    }

    public void setListaPlazas(List<SgPlaza> listaPlazas) {
        this.listaPlazas = listaPlazas;
    }

    public List<SgPlaza> getListaPlazasAux() {
        return listaPlazasAux;
    }

    public void setListaPlazasAux(List<SgPlaza> listaPlazasAux) {
        this.listaPlazasAux = listaPlazasAux;
    }

    public Long getPlazaId() {
        return plazaId;
    }

    public void setPlazaId(Long plazaId) {
        this.plazaId = plazaId;
    }

    public Long getPlazaRev() {
        return plazaRev;
    }

    public void setPlazaRev(Long plazaRev) {
        this.plazaRev = plazaRev;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(Boolean aprobacion) {
        this.aprobacion = aprobacion;
    }

    public Integer getOrigen() {
        return origen;
    }

    public void setOrigen(Integer origen) {
        this.origen = origen;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSolicitudPlaza getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudPlaza entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public SofisComboG<EnumTipoPlaza> getComboTipoPlaza() {
        return comboTipoPlaza;
    }

    public void setComboTipoPlaza(SofisComboG<EnumTipoPlaza> comboTipoPlaza) {
        this.comboTipoPlaza = comboTipoPlaza;
    }

    public SofisComboG<SgEspecialidad> getComboEspecialidad() {
        return comboEspecialidad;
    }

    public void setComboEspecialidad(SofisComboG<SgEspecialidad> comboEspecialidad) {
        this.comboEspecialidad = comboEspecialidad;
    }

    public SofisComboG<SgFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SofisComboG<SgJornadaLaboral> getComboJornada() {
        return comboJornada;
    }

    public void setComboJornada(SofisComboG<SgJornadaLaboral> comboJornada) {
        this.comboJornada = comboJornada;
    }

    public SofisComboG<SgCargo> getComboTipoNombramiento() {
        return comboTipoNombramiento;
    }

    public void setComboTipoNombramiento(SofisComboG<SgCargo> comboTipoNombramiento) {
        this.comboTipoNombramiento = comboTipoNombramiento;
    }

    public SgComponentePlanEstudio getComponenteSeleccionado() {
        return componenteSeleccionado;
    }

    public void setComponenteSeleccionado(SgComponentePlanEstudio componenteSeleccionado) {
        this.componenteSeleccionado = componenteSeleccionado;
    }

    public String getValor1() {
        return valor1;
    }

    public void setValor1(String valor1) {
        this.valor1 = valor1;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public List<EnumCaracterizacionPlaza> getListCaracterizacion() {
        return listCaracterizacion;
    }

    public void setListCaracterizacion(List<EnumCaracterizacionPlaza> listCaracterizacion) {
        this.listCaracterizacion = listCaracterizacion;
    }

    public List<EnumEstadoPlaza> getListEstadoPlaza() {
        return listEstadoPlaza;
    }

    public void setListEstadoPlaza(List<EnumEstadoPlaza> listEstadoPlaza) {
        this.listEstadoPlaza = listEstadoPlaza;
    }

    public Boolean getAplicarPlaza() {
        return aplicarPlaza;
    }

    public void setAplicarPlaza(Boolean aplicarPlaza) {
        this.aplicarPlaza = aplicarPlaza;
    }

    public String getTextoAplico() {
        return textoAplico;
    }

    public void setTextoAplico(String textoAplico) {
        this.textoAplico = textoAplico;
    }

    public SgPersonalSedeEducativa getEntidadPersonal() {
        return entidadPersonal;
    }

    public void setEntidadPersonal(SgPersonalSedeEducativa entidadPersonal) {
        this.entidadPersonal = entidadPersonal;
    }

    public SofisComboG<SgCalidadIngresoAplicantes> getComboAplicaCalidad() {
        return comboAplicaCalidad;
    }

    public void setComboAplicaCalidad(SofisComboG<SgCalidadIngresoAplicantes> comboAplicaCalidad) {
        this.comboAplicaCalidad = comboAplicaCalidad;
    }
    
    public String getAplicoEnCalidad() {
        return aplicoEnCalidad;
    }

    public void setAplicoEnCalidad(String aplicoEnCalidad) {
        this.aplicoEnCalidad = aplicoEnCalidad;
    }
    
    //</editor-fold>

}
