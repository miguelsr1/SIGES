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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.dto.SgAfMotivoRechazoTraslado;
import sv.gob.mined.siges.web.dto.SgAfTraslado;
import sv.gob.mined.siges.web.dto.SgAfTrasladoDetalle;
import sv.gob.mined.siges.web.dto.SolicitudTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgAfTipoBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessError;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEmpleados;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTrasladoDetalle;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyBienesDepreciablesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TipoBienesRestClient;
import sv.gob.mined.siges.web.restclient.TrasladosRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class TrasladoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TrasladoBean.class.getName());
     
    @Inject
    @Param(name = "id")
    private Long trasladoId;

    @Inject
    @Param(name = "rev")
    private Long estRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private SedeRestClient sedeRestClient;
     
    @Inject
    private BienesDepreciablesRestClient bienesRestClient;
    
    @Inject
    private TrasladosRestClient trasladosRestClient;

    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private ApplicationBean applicationBean;
    
    @Inject
    private TipoBienesRestClient tipoBienesRestClient;
    
    private LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel;
    
    private SgAfTipoBienes tipoBienSeleccionado;
    
    private SgAfTraslado entidadEnEdicion = new SgAfTraslado();
    private SofisComboG<SgAfEstadosTraslado> comboTipoTraslado;
    
    private SgAfUnidadesAdministrativas unidadOrigenSeleccionada;
    private SgSede sedeOrigenSeleccionada;
    
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativasOrigen = new ArrayList();
    private List<SgSede> sedesOrigen = new ArrayList();
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativasDestino = new ArrayList();
    private List<SgSede> sedesDestino = new ArrayList();
    
    private SgAfUnidadesActivoFijo unidadAFOrigenSeleccionada;
    private TipoUnidad tipoUnidadOrigenSeleccionada;
    
    private SgAfUnidadesAdministrativas unidadDestinoSeleccionada;
    private SgSede sedeDestinoSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFDestinoSeleccionada;
    private TipoUnidad tipoUnidadDestinoSeleccionada;
    private SgAfEstadosTraslado tipoTrasladoSeleccionado;
    
    private FiltroCodiguera filtro;
    private SofisComboG<SgSede> comboSedesOrigen;
    private SofisComboG comboUnidadAdministrativaOrigen;
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoOrigen;
    private SofisComboG<TipoUnidad> comboTiposUnidadOrigen;
    private SofisComboG<SgMunicipio> comboMunicipiosOrigen;
    
    private SofisComboG<SgSede> comboSedesDestino;
    private SofisComboG comboUnidadAdministrativaDestino;
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoDestino;
    private SofisComboG<TipoUnidad> comboTiposUnidadDestino;
    private SofisComboG<SgMunicipio> comboMunicipiosDestino;
    
    private Boolean esUnidadAdministrativaOrigen;
    private Boolean esUnidadAdministrativaDestino;
    
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
    
    private SgAfEstadosBienes estadoExistente;
    private SgAfEstadosBienes estadoEnviado;
    private SgAfEstadosBienes estadoTrasladado;
    private SgAfEstadosBienes estadoEnSolicitud;
    private SgAfEstadosBienes estadoRechazado;
    private List<SgAfTrasladoDetalle> listaDetalleRechazo  = new ArrayList();
    
    private Boolean esUnidadAdministrativa = Boolean.FALSE;
    
    private List<SgAfBienDepreciable> bienesDestino = new ArrayList();
    private List<SgAfMotivoRechazoTraslado> listaMotivosRechazo = new ArrayList();
    private List<SgAfUnidadesActivoFijo> listaUnidadesAF;
    private SgAfMotivoRechazoTraslado motivoRechazoTraslado;
    
    private Boolean soloLectura = Boolean.FALSE;
    
    private SolicitudTraslado solicitud;
    private Boolean activosFijos; 
    private Boolean crearSolicitud = Boolean.TRUE; 
    private Boolean enviarSolicitud = Boolean.FALSE; 
    private Boolean guardarCambios = Boolean.FALSE; 
    
    private Boolean esTrasladoDefinitivo = Boolean.FALSE;
    private Boolean confirmarTraslado = Boolean.FALSE;
    private Boolean confirmarRecibido = Boolean.FALSE; 
    private Boolean permiteBusquedas = Boolean.TRUE;
    private Boolean renderUnidadDestino = Boolean.FALSE;
    private Boolean renderSeccionDestino = Boolean.FALSE;
    private Boolean editarFecha = Boolean.TRUE;
    private Integer totalBienesOrigen = 0;
    private Integer totalBienesDestino = 0;
    
    private Integer proceso = 0;
    private Integer paginado = 10;
    private Integer paginadoDestino = 10;
    private Long totalResultados = 0L;
    private Long totalResultadosAnterior = 0L;
    private FiltroTrasladoDetalle filtroTrasladoDetalle;
    private Boolean editarEmpleado = Boolean.FALSE;
    //private List<SgAfEmpleados> empleados = new ArrayList();
    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (trasladoId != null && trasladoId > 0) {
                if (estRev != null && estRev > 0) {
                    this.soloLectura = Boolean.TRUE;
                    this.actualizar(trasladosRestClient.obtenerEnRevision(trasladoId, estRev));
                } else {
                    this.soloLectura = this.editable != null ? !this.editable : this.soloLectura;
                    this.actualizar(trasladosRestClient.obtenerPorId(trasladoId, Boolean.TRUE)); 
                }
            } else {
                cargarEntidad();
            }
            
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarVisualizacion() {
        Boolean esOrigen =  Boolean.FALSE;
        Boolean esDestino =  Boolean.FALSE;
        this.editarEmpleado = Boolean.FALSE;
        if(entidadEnEdicion != null) {
            permiteBusquedas = Boolean.FALSE;
            if((sessionBean.getUnidadADDefecto() != null && entidadEnEdicion.getTraUnidadAdmOrigenFk() != null && sessionBean.getUnidadADDefecto().getUadPk().equals(entidadEnEdicion.getTraUnidadAdmOrigenFk().getUadPk()))
                        || sessionBean.getSedeDefecto() != null && entidadEnEdicion.getTraSedeOrigenFk() != null && sessionBean.getSedeDefecto().getSedPk().equals(entidadEnEdicion.getTraSedeOrigenFk().getSedPk())) {
                esOrigen = Boolean.TRUE;
            } else if((sessionBean.getUnidadADDefecto() != null && entidadEnEdicion.getTraUnidadAdmDestinoFk() != null && sessionBean.getUnidadADDefecto().getUadPk().equals(entidadEnEdicion.getTraUnidadAdmDestinoFk().getUadPk()))
                || sessionBean.getSedeDefecto() != null && entidadEnEdicion.getTraSedeDestinoFk() != null && sessionBean.getSedeDefecto().getSedPk().equals(entidadEnEdicion.getTraSedeDestinoFk().getSedPk())) {
                esDestino = Boolean.TRUE;
            }
            
            if(estadoEnSolicitud != null && estadoEnSolicitud.getEbiPk() != null && entidadEnEdicion.getTraEstadoFk().getEbiPk().equals(estadoEnSolicitud.getEbiPk())) {
                this.enviarSolicitud = Boolean.FALSE;
                this.permiteBusquedas = Boolean.FALSE;
                this.crearSolicitud = Boolean.FALSE;
                this.guardarCambios = Boolean.FALSE;
                this.confirmarRecibido = Boolean.FALSE;
                this.confirmarTraslado = Boolean.FALSE;
                this.soloLectura = Boolean.TRUE;
                
                if(esOrigen || sessionBean.getOperaciones().contains(ConstantesOperaciones.ENVIAR_TODOS_TRASLADO_BIENES)) {
                    this.enviarSolicitud = Boolean.TRUE;
                    this.permiteBusquedas = Boolean.TRUE;
                    this.crearSolicitud = Boolean.TRUE;
                    this.soloLectura = Boolean.FALSE;
                }
            } else if(estadoRechazado != null && estadoRechazado.getEbiPk() != null && entidadEnEdicion.getTraEstadoFk().getEbiPk().equals(estadoRechazado.getEbiPk())) {
                this.enviarSolicitud = Boolean.FALSE;
                this.permiteBusquedas = Boolean.FALSE;
                this.crearSolicitud = Boolean.FALSE;
                this.guardarCambios = Boolean.FALSE;
                this.confirmarRecibido = Boolean.FALSE;
                this.confirmarTraslado = Boolean.FALSE;
                this.soloLectura = Boolean.TRUE;
                
                if(esOrigen || sessionBean.getOperaciones().contains(ConstantesOperaciones.RECHAZAR_TODOS_TRASLADO_BIENES)) {
                    this.guardarCambios = Boolean.TRUE;
                    this.enviarSolicitud = Boolean.TRUE;
                    this.permiteBusquedas = Boolean.TRUE;
                    this.soloLectura = Boolean.FALSE;
                }
            } else if(estadoEnviado != null && estadoEnviado.getEbiPk() != null && entidadEnEdicion.getTraEstadoFk().getEbiPk().equals(estadoEnviado.getEbiPk())) {
                this.enviarSolicitud = Boolean.FALSE;
                this.soloLectura = Boolean.TRUE;
                this.editarFecha = Boolean.TRUE;
                if(tipoTrasladoSeleccionado != null) {
                    if(tipoTrasladoSeleccionado.getEtrCodigo().equals(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO)) {
                        this.confirmarTraslado = Boolean.FALSE;
                        this.crearSolicitud = Boolean.FALSE; 
                        this.guardarCambios = Boolean.FALSE;
                        this.soloLectura = Boolean.TRUE;
                        if((esDestino && sessionBean.getUnidadADDefecto() != null) 
                                || (sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_TODOS_TRASLADO_BIENES) && entidadEnEdicion.getTraUnidadAdmDestinoFk() != null)) {
                            this.confirmarTraslado = Boolean.TRUE;
                            this.editarEmpleado = Boolean.TRUE;
                        }
                        
                    } else {
                        this.confirmarRecibido = Boolean.FALSE;
                        this.crearSolicitud = Boolean.FALSE;
                        this.guardarCambios = Boolean.FALSE;
                        this.soloLectura = Boolean.TRUE;
                        if(esOrigen || sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_TODOS_TRASLADO_BIENES)) {
                            this.confirmarRecibido = Boolean.TRUE;
                            this.guardarCambios = Boolean.TRUE;
                            this.soloLectura = Boolean.FALSE;
                        }
                    }
                } else {
                    this.crearSolicitud = Boolean.TRUE;
                    this.guardarCambios = Boolean.FALSE;
                }
                
            } else {
                this.confirmarTraslado = Boolean.FALSE;
                this.enviarSolicitud = Boolean.FALSE;
                this.crearSolicitud = Boolean.FALSE;
                this.confirmarRecibido = Boolean.FALSE;
                this.guardarCambios = Boolean.FALSE;
                this.soloLectura = Boolean.TRUE;
                this.editarFecha = Boolean.FALSE;
            }
        }
    }
    
    public void buscar(Boolean validarConBienesDestino, Boolean inicializar) throws  BusinessException{
        try {
            if(inicializar != null && inicializar) {
                filtroBienes = new FiltroBienesDepreciables();
            }
            filtroBienes.setIncluirCampos(new String[]{"bdePk","bdeVersion","bdeCodigoInventario","bdeValorAdquisicion",
                "bdeFechaAdquisicion","bdeTipoBien.tbiPk","bdeTipoBien.tbiVersion","bdeTipoBien.tbiCodigo","bdeTipoBien.tbiNombre",
                "bdeTipoBien.tbiCategoriaBienes.cabPk","bdeTipoBien.tbiCategoriaBienes.cabVersion",
                "bdeTipoBien.tbiCategoriaBienes.cabCodigo","bdeTipoBien.tbiCategoriaBienes.cabNombre","bdeUnidadesAdministrativas.uadPk",
                "bdeUnidadesAdministrativas.uadCodigo","bdeUnidadesAdministrativas.uadNombre","bdeSede.sedPk","bdeSede.sedVersion", 
                "bdeSede.sedTipo","bdeEstadosBienes.ebiPk","bdeEstadosBienes.ebiVersion","bdeFechaDescargo"});
            
            filtroBienes.setSecurityOperation(null);
            
            filtroBienes.setTipoUnidad(tipoUnidadOrigenSeleccionada != null ? tipoUnidadOrigenSeleccionada : (comboTiposUnidadOrigen != null ? comboTiposUnidadOrigen.getSelectedT() : null));
            
            filtroBienes.setEstadoId(estadoExistente != null ? estadoExistente.getEbiPk() : null);

            filtroBienes.setTipoBienId(tipoBienSeleccionado != null ? tipoBienSeleccionado.getTbiPk() : null);
            filtroBienes.setValidarEstadoSolicitudVacio(Boolean.TRUE); //Para que filtre tambien por el estado de la solicitud vacio(NULL), es decir que no esté en ninguna solicitud, este filtro va de la mano con el estadoId
            filtroBienes.setTrasladado(Boolean.TRUE);
            filtroBienes.setEstadoCodigo(estadoTrasladado != null ? estadoTrasladado.getEbiCodigo() : "");
            
            if(unidadAFOrigenSeleccionada != null) {
                filtroBienes.setUnidadActivoFijoId(unidadAFOrigenSeleccionada.getUafPk());
                if(unidadAFOrigenSeleccionada.getUafDepartamento() != null 
                    && unidadAFOrigenSeleccionada.getUafDepartamento().getDepPk() != null) {
                    filtroBienes.setDepartamentoId(unidadAFOrigenSeleccionada.getUafDepartamento().getDepPk());
                }
            }
            filtroBienes.setMunicipioId(comboMunicipiosOrigen != null && comboMunicipiosOrigen.getSelectedT() != null ? comboMunicipiosOrigen.getSelectedT().getMunPk() : null);
            
            if(tipoUnidadOrigenSeleccionada != null) {
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadOrigenSeleccionada)) {
                    filtroBienes.setUnidadAdministrativaId(unidadOrigenSeleccionada != null ? unidadOrigenSeleccionada.getUadPk() : null);
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadOrigenSeleccionada)) {
                    filtroBienes.setUnidadAdministrativaId(sedeOrigenSeleccionada != null ? sedeOrigenSeleccionada.getSedPk() : null);
                }
            }
           
            if(validarFiltro(filtroBienes)) {
               totalResultados = bienesRestClient.buscarTotal(filtroBienes);
               totalResultadosAnterior = totalResultados;
               bienesDepreciablesLazyModel = new LazyBienesDepreciablesDataModel(bienesRestClient, filtroBienes, totalResultados, paginado);
            }
            
        } catch (BusinessException be) {
             LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_TRASLADO_BIENES)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public  Boolean validarFiltro(FiltroBienesDepreciables filtro) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (filtro == null) {
            ge.addError(Mensajes.ERROR_FILTRO_DATO_VACIO);
        } else {    
            if (filtro.getTipoUnidad() == null) {
               ge.addError("tipoUnidad", Mensajes.ERROR_FILTRO_TIPO_UNIDAD_ORIGEN_VACIO);
            }
            
            if (filtro.getUnidadActivoFijoId()== null) {
               ge.addError("unidadActivoFijoId", Mensajes.ERROR_FILTRO_UNIDAD_ACTIVO_FIJO_ORIGEN_VACIO);
            }
            
            if (filtro.getUnidadAdministrativaId()== null) {
               ge.addError("unidadAdministrativaId", Mensajes.ERROR_FILTRO_UNIDAD_ADMINISTRATIVA_ORIGEN_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    public void cargarCombos() {
        try {
            
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            
            filtro.setOrderBy(new String[]{"etrCodigo"});
            filtro.setIncluirCampos(new String[]{"etrNombre","etrCodigo","etrVersion"});
            List<SgAfEstadosTraslado> estadosTraslado = catalogosRestClient.buscarEstadosTraslado(filtro);
            comboTipoTraslado = new SofisComboG(estadosTraslado, "etrNombre");
            comboTipoTraslado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            if(sessionBean.getUnidadADDefecto() != null) {
                tipoUnidad.add(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            } else if(sessionBean.getSedeDefecto() != null) {
                tipoUnidad.add(TipoUnidad.CENTRO_ESCOLAR);
                comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            } else {
                tipoUnidad = new ArrayList(Arrays.asList(TipoUnidad.values()));
                comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
                comboTiposUnidadOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            comboTiposUnidadOrigen.ordenar();
            
            List<TipoUnidad> tipoUnidadDestino = new ArrayList(Arrays.asList(TipoUnidad.values()));
            comboTiposUnidadDestino = new SofisComboG(tipoUnidadDestino, "text");
            comboTiposUnidadDestino.ordenar();
            
            
            FiltroUnidadesActivoFijo fua = new FiltroUnidadesActivoFijo();
            fua.setHabilitado(Boolean.TRUE);
            fua.setOrderBy(new String[]{"uafCodigo"});
            fua.setAscending(new boolean[]{true});
            fua.setIncluirCampos(new String[]{"uafCodigo","uafNombre", "uafDepartamento","uafVersion"});
            
            List<SgAfUnidadesActivoFijo> listaUnidades = new ArrayList();
            
            List<SgAfUnidadesActivoFijo> unidades = unidadesActivoFijoRestClient.buscar(fua);
            if(sessionBean.getSedeDefecto() != null) {
                if(unidades != null && !unidades.isEmpty()) {
                    for(SgAfUnidadesActivoFijo unidadesAF : unidades) {
                        if(!unidadesAF.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            listaUnidades.add(unidadesAF);//Agregamos las unidades que no son UNIDAD CENTRAL
                        }
                    }
                }
                
            } else {
                listaUnidades = unidades;
            }
            
            comboUnidadesActivoFijoOrigen = new SofisComboG(listaUnidades, "codigoNombre");
            comboUnidadesActivoFijoOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            listaUnidadesAF = unidadesActivoFijoRestClient.buscarSinPermisos(fua);
            comboUnidadesActivoFijoDestino= new SofisComboG(listaUnidadesAF, "codigoNombre");
            comboUnidadesActivoFijoDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            comboMunicipiosOrigen = new SofisComboG();
            comboMunicipiosOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboMunicipiosDestino = new SofisComboG();
            comboMunicipiosDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboUnidadAdministrativaOrigen = new SofisComboG();
            comboUnidadAdministrativaOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboUnidadAdministrativaDestino = new SofisComboG();
            comboUnidadAdministrativaDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboSedesOrigen = new SofisComboG();
            comboSedesOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboSedesDestino= new SofisComboG();
            
            
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrderBy(new String[]{"ebiCodigo"});
            filtro.setIncluirCampos(new String[]{"ebiPk","ebiNombre","ebiCodigo","ebiVersion"});
            List<SgAfEstadosBienes> estadosBien = catalogosRestClient.buscarEstadosBienes(filtro);
            if(estadosBien != null && !estadosBien.isEmpty()) {
                for(SgAfEstadosBienes estado : estadosBien) {
                    if(Constantes.CODIGO_ESTADO_EXISTENTE.equals(estado.getEbiCodigo().trim())) {
                        estadoExistente = estado;
                    } else if(Constantes.CODIGO_ESTADO_PROCESO_TRASLADADO.equals(estado.getEbiCodigo().trim())) {
                        estadoEnviado= estado;
                    } else if(Constantes.CODIGO_ESTADO_TRASLADADO.equals(estado.getEbiCodigo().trim())) {
                        estadoTrasladado = estado;
                    } else if(Constantes.CODIGO_ESTADO_SOLICITUD_TRASLADO.equals(estado.getEbiCodigo().trim())) {
                        estadoEnSolicitud = estado;
                    } else if(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA.equals(estado.getEbiCodigo().trim())) {
                        estadoRechazado = estado;
                    }
                }
                
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void completeCodigoCorrelativo() {
        entidadEnEdicion.setTraCodigoTraslado("");
        if(tipoUnidadOrigenSeleccionada != null && (unidadOrigenSeleccionada != null || sedeOrigenSeleccionada != null) && entidadEnEdicion.getTraCorrelativo() != null) {
            entidadEnEdicion.setTraCodigoTraslado(entidadEnEdicion.getTraCorrelativo() + "-" + LocalDate.now().getYear());
            entidadEnEdicion.setTraAnio(LocalDate.now().getYear());
        }
    }
    public void cargarEntidad() {
        try {
            sedeOrigenSeleccionada = null;
            unidadOrigenSeleccionada = null;
            cargarSedePorDefecto();
            cargarUADPorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
   
    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            this.esUnidadAdministrativaOrigen = Boolean.FALSE;
            sedeOrigenSeleccionada = sessionBean.getSedeDefecto();
            entidadEnEdicion.setTraSedeOrigenFk(sedeOrigenSeleccionada);
            
            tipoUnidadOrigenSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadOrigenSeleccionada);
            
            comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidadOrigen.setSelectedT(tipoUnidadOrigenSeleccionada);
            
            if(sedeOrigenSeleccionada!= null) {
                if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                    for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                        if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeOrigenSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            unidadAFOrigenSeleccionada = unidadesAF;
                        }
                    }
                } 

            }

            comboUnidadesActivoFijoOrigen.setSelectedT(unidadAFOrigenSeleccionada);
                    
            seleccionarUnidadActivoFijo(0);
        }
    }
    public void cargarUADPorDefecto() throws Exception {
        if (sessionBean.getUnidadADDefecto() != null) {
            this.esUnidadAdministrativaOrigen = Boolean.TRUE;
            unidadOrigenSeleccionada = sessionBean.getUnidadADDefecto();
            entidadEnEdicion.setTraUnidadAdmOrigenFk(unidadOrigenSeleccionada);
            
            tipoUnidadOrigenSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadOrigenSeleccionada);
            
            comboTiposUnidadOrigen = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidadOrigen.setSelectedT(tipoUnidadOrigenSeleccionada);
            
            if(unidadOrigenSeleccionada != null) {
                unidadAFOrigenSeleccionada = unidadOrigenSeleccionada.getUadUnidadActivoFijoFk();
            } 

            comboUnidadesActivoFijoOrigen.setSelectedT(unidadAFOrigenSeleccionada);
            
            seleccionarUnidadActivoFijo(0);
        }
    }

    public void cargarEmpleados() {
        try {
           // empleados = new ArrayList();
            if(this.editarEmpleado != null && this.editarEmpleado) {
                FiltroEmpleados femp = new FiltroEmpleados();
                filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                femp.setOrderBy(new String[]{"empPersonaFk.perPrimerNombre"});
                femp.setAscending(new boolean[]{true});
                femp.setIncluirCampos(new String[]{"empPk","empVersion","empPersonaFk.perPk","empPersonaFk.perVersion",
                                                "empPersonaFk.perPrimerNombre","empPersonaFk.perSegundoNombre","empPersonaFk.perTercerNombre",
                                                "empPersonaFk.perPrimerApellido","empPersonaFk.perSegundoApellido","empPersonaFk.perTercerApellido","empPersonaFk.perDui"});

                
                femp.setUnidadAdministrativaId(sessionBean.getUnidadADDefecto().getUadPk());
               // empleados = catalogosRestClient.buscarEmpleados(femp);
                
                filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                filtroBienes.setUnidadAdministrativaId(femp.getUnidadAdministrativaId());
               
                //empleados = catalogosRestClient.buscarEmpleados(femp);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    public void guardar(Boolean guardarSolicitud, Boolean enviarSolicitud, Boolean trasladarSolicitud, Boolean tieneCambios, Boolean rechazarSolicitud) {
        try {  

            entidadEnEdicion.setTraTipoTrasladoFk(comboTipoTraslado != null ? comboTipoTraslado.getSelectedT():null);
            
            if((tieneCambios ==null || !tieneCambios )) {
                if(guardarSolicitud != null && guardarSolicitud && (tieneCambios ==null || !tieneCambios )) {
                    entidadEnEdicion.setTraEstadoFk(estadoEnSolicitud);
                } else if(enviarSolicitud != null && enviarSolicitud) {
                    entidadEnEdicion.setTraEstadoFk(estadoEnviado);
                } else if(trasladarSolicitud != null && trasladarSolicitud) {
                    entidadEnEdicion.setTraEstadoFk(estadoTrasladado);
                    entidadEnEdicion.setTraFechaAutoriza(LocalDateTime.now());
                } else if(rechazarSolicitud != null && rechazarSolicitud) {
                    entidadEnEdicion.setTraEstadoFk(estadoRechazado);
                    listaMotivosRechazo = entidadEnEdicion.getSgAfMotivosRechazoTraslado();
                    if(listaMotivosRechazo == null) {
                        listaMotivosRechazo = new ArrayList();
                    }
                    listaMotivosRechazo.add(motivoRechazoTraslado);
                    entidadEnEdicion.setSgAfMotivosRechazoTraslado(listaMotivosRechazo);
                }
            }
            
            if(entidadEnEdicion.getTraPk() == null) {
                entidadEnEdicion.setTraFechaCreacion(LocalDateTime.now());
                entidadEnEdicion.setTraFechaSolicitud(LocalDate.now());
                entidadEnEdicion.setTraUsuarioCreacion(sessionBean.getUser().getName());
                if(bienesDestino == null || bienesDestino.isEmpty() || bienesDestino.size() == 0) {
                    List<BusinessError> errores = new ArrayList();
                    errores.add(new BusinessError(Mensajes.ERROR_BIENES_SELECCIONADOS_VACIO));
                    JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                    return;
                }
                
            }
 
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadOrigenSeleccionada)) {
                entidadEnEdicion.setTraUnidadAdmOrigenFk(unidadOrigenSeleccionada);
                entidadEnEdicion.setTraSedeOrigenFk(null);
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadOrigenSeleccionada)) {
                entidadEnEdicion.setTraSedeOrigenFk(sedeOrigenSeleccionada);
                entidadEnEdicion.setTraUnidadAdmOrigenFk(null);
            }
            
            if(entidadEnEdicion.getTraTipoTrasladoFk() != null && entidadEnEdicion.getTraTipoTrasladoFk().getEtrCodigo().equals(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO)) {
                entidadEnEdicion.setTraUnidadDestino("");
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadDestinoSeleccionada)) {
                    entidadEnEdicion.setTraUnidadAdmDestinoFk(unidadDestinoSeleccionada);
                    entidadEnEdicion.setTraSedeDestinoFk(null);
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadDestinoSeleccionada)) {
                    entidadEnEdicion.setTraSedeDestinoFk(sedeDestinoSeleccionada);
                    entidadEnEdicion.setTraUnidadAdmDestinoFk(null);
                }
            } else {
                entidadEnEdicion.setTraSedeDestinoFk(null);
                entidadEnEdicion.setTraUnidadAdmDestinoFk(null);
            }
            
            
            
            if(rechazarSolicitud == null || !rechazarSolicitud) {
                List<SgAfTrasladoDetalle> listaBienesTrasladar = new ArrayList();
                if(bienesDestino != null && !bienesDestino.isEmpty() && bienesDestino.size() > 0) {
                    for(SgAfBienDepreciable bien : bienesDestino) {
                        bien.setBdeEstadosBienes(estadoEnSolicitud);
                        SgAfTrasladoDetalle detalle = new SgAfTrasladoDetalle();
                        if(trasladarSolicitud != null && trasladarSolicitud) {//Se pasa el bien a la unidad de destino
                            bien.setBdeEstadosBienes(estadoTrasladado);
                        }
                        detalle.setTdeBienesDepreciablesFk(bien);
                        listaBienesTrasladar.add(detalle);
                    }
                }
                entidadEnEdicion.setSgAfTrasladosDetalle(listaBienesTrasladar);
            }

            solicitud = new SolicitudTraslado();
            solicitud.setTraslado(entidadEnEdicion);
            solicitud.setEstado(estadoExistente);
            entidadEnEdicion = trasladosRestClient.guardar(solicitud);
            if(entidadEnEdicion.getTraEstadoFk().getEbiCodigo().equals(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA)) {
                if(entidadEnEdicion.getSgAfTrasladosDetalle()!= null && !entidadEnEdicion.getSgAfTrasladosDetalle().isEmpty() && entidadEnEdicion.getSgAfTrasladosDetalle().size() > 0) {
                    if(listaDetalleRechazo != null) {
                        listaDetalleRechazo.clear();
                    } else {
                        listaDetalleRechazo = new ArrayList();
                    }
                    bienesDestino = new ArrayList();
                    for(SgAfTrasladoDetalle desDetalle : entidadEnEdicion.getSgAfTrasladosDetalle()) {
                        bienesDestino.add(desDetalle.getTdeBienesDepreciablesFk());
                        if(desDetalle.getTdeRechazado() != null && desDetalle.getTdeRechazado()) {
                            listaDetalleRechazo.add(desDetalle);
                        }
                    }
                }
            }
            
            validarVisualizacion();
            
            if(enviarSolicitud != null && enviarSolicitud) {
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            } else if(rechazarSolicitud != null && rechazarSolicitud) {
                PrimeFaces.current().executeScript("PF('motivoRechazoDialog').hide()");
            } else if(trasladarSolicitud != null && trasladarSolicitud) {
                PrimeFaces.current().executeScript("PF('itemTrasladoDialog').hide()");
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
             
        } catch (BusinessException be) {
            
            if(enviarSolicitud != null && enviarSolicitud) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
            } else if(trasladarSolicitud != null && trasladarSolicitud) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
            } else if(rechazarSolicitud != null && rechazarSolicitud) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_3, FacesMessage.SEVERITY_ERROR, be.getErrores());
            } else {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            }
           // LOGGER.log(Level.SEVERE, be.getMessage(), be);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            if(enviarSolicitud != null && enviarSolicitud) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            } else if(trasladarSolicitud != null && trasladarSolicitud) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            } else if(rechazarSolicitud != null && rechazarSolicitud) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_3, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            } else {
               JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        }
    }
    
    public void actualizar(SgAfTraslado est) {
        try {
            if (est == null) {
                JSFUtils.redirectToIndex();
            } else {
                JSFUtils.limpiarMensajesError();
                entidadEnEdicion = est;
                bienesDestino = new ArrayList();
                if(entidadEnEdicion != null) {
                    
                    comboTipoTraslado.setSelectedT(entidadEnEdicion.getTraTipoTrasladoFk());
                    
                    seleccionarTipoTraslado();
                    
                    unidadOrigenSeleccionada = entidadEnEdicion.getTraUnidadAdmOrigenFk();
                    unidadDestinoSeleccionada = entidadEnEdicion.getTraUnidadAdmDestinoFk();
                    sedeOrigenSeleccionada = entidadEnEdicion.getTraSedeOrigenFk();
                    sedeDestinoSeleccionada = entidadEnEdicion.getTraSedeDestinoFk();
                        
                    //Carga de datos de origen
                    if(unidadOrigenSeleccionada != null) {
                        tipoUnidadOrigenSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
                        unidadAFOrigenSeleccionada = unidadOrigenSeleccionada.getUadUnidadActivoFijoFk();
                        comboTiposUnidadOrigen.setSelectedT(tipoUnidadOrigenSeleccionada);
                    } else if(sedeOrigenSeleccionada!= null) {
                        tipoUnidadOrigenSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
                        comboTiposUnidadOrigen.setSelectedT(tipoUnidadOrigenSeleccionada);
                        if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                            for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                                if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeOrigenSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                        && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                    unidadAFOrigenSeleccionada = unidadesAF;
                                }
                            }
                        } 
                        
                    }

                    comboUnidadesActivoFijoOrigen.setSelectedT(unidadAFOrigenSeleccionada);
                    seleccionarUnidadActivoFijo(0);
          
                    
                    if(entidadEnEdicion.getSgAfTrasladosDetalle()!= null && !entidadEnEdicion.getSgAfTrasladosDetalle().isEmpty() && entidadEnEdicion.getSgAfTrasladosDetalle().size() > 0) {
                        for(SgAfTrasladoDetalle desDetalle : entidadEnEdicion.getSgAfTrasladosDetalle()) {
                            bienesDestino.add(desDetalle.getTdeBienesDepreciablesFk());
                            if(desDetalle.getTdeRechazado() != null && desDetalle.getTdeRechazado()) {
                                listaDetalleRechazo.add(desDetalle);
                            }
                        }
                    }
                    totalBienesDestino = bienesDestino.size();
                    //Carga de datos de destino
                    if(unidadDestinoSeleccionada != null) {
                        tipoUnidadDestinoSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
                        unidadAFDestinoSeleccionada = unidadDestinoSeleccionada.getUadUnidadActivoFijoFk();
                        comboTiposUnidadOrigen.setSelectedT(tipoUnidadOrigenSeleccionada);
                    } else if(sedeDestinoSeleccionada!= null) {
                        tipoUnidadDestinoSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
                        comboTiposUnidadDestino.setSelectedT(tipoUnidadDestinoSeleccionada);
                        if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                            for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                                if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeDestinoSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                        && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                    unidadAFDestinoSeleccionada = unidadesAF;
                                }
                            }
                        } 
                        
                    }
                    comboUnidadesActivoFijoDestino.setSelectedT(unidadAFDestinoSeleccionada);
                    seleccionarUnidadActivoFijo(1);
                    
                    validarVisualizacion();
                    buscar(true, true);
                }
            }  
        }  catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgAfTipoBienes> completeTipoBien(String query) {
        try {
            FiltroTipoBienes filtro = new FiltroTipoBienes();
            filtro.setHabilitado(Boolean.TRUE); 
            filtro.setCodigoONombre(query);
            filtro.setMaxResults(11L);
            filtro.setOrderBy(new String[]{"tbiNombre"});
            filtro.setAscending(new boolean[]{false});
            filtro.setIncluirCampos(new String[]{"tbiPk","tbiCodigo", "tbiNombre","tbiVersion"});
            return tipoBienesRestClient.buscar(filtro);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    public void limpiarTablas() {
        bienesDepreciablesLazyModel = null;
        bienesDestino = new ArrayList();
        totalResultados = 0L;
        totalBienesDestino = 0;
        totalResultadosAnterior= 0L;  
    }
    
    public void rowSelectCheckbox(SelectEvent event) {
        SgAfBienDepreciable bien = (SgAfBienDepreciable) event.getObject();
        if(!bienesDestino.contains(bien)) {
            bienesDestino.add(bien);
        }
        this.totalBienesDestino = bienesDestino.size();
        this.totalResultados = this.totalResultados - 1;
    }

    public void rowUnSelectCheckboxSeleccionados(UnselectEvent event) {
        SgAfBienDepreciable bien = (SgAfBienDepreciable) event.getObject();
        bienesDestino.remove(bien);
        this.totalBienesDestino = bienesDestino.size();
        this.totalResultados = this.totalResultados + 1;
    }
    
    public void rowSelectCheckboxSeleccionados(SelectEvent event) {
        SgAfBienDepreciable bien = (SgAfBienDepreciable) event.getObject();
        bienesDestino.remove(bien);
        this.totalBienesDestino = bienesDestino.size();
        this.totalResultados = this.totalResultados + 1;
    }
    
    public void toggleSelected(ToggleSelectEvent tse) {
        if(tse.isSelected()) {
            List<SgAfBienDepreciable> totalBienes = bienesDepreciablesLazyModel.getWrappedData();
            for(SgAfBienDepreciable bien :totalBienes) {
                if(!bienesDestino.contains(bien)) {
                    bienesDestino.add(bien);
                }
            }
            this.totalBienesDestino = bienesDestino.size();
            this.totalResultados = totalResultados - this.paginado ;
        } else {
            this.totalBienesDestino = this.totalBienesDestino - this.paginado ;
            this.totalResultados = totalResultadosAnterior;
        }
    }
    public void toggleSelectedSeleccionado(ToggleSelectEvent tse) {
        this.totalBienesDestino = this.bienesDestino.size() ;
        this.totalResultados = totalResultadosAnterior;
    }
    
    public boolean bienPuedeAgregarse(SgAfBienDepreciable bien) {
        if(bienesDestino.contains(bien)) {
            return false;
        }
        return true;
    }
    
    public void seleccionarTipoUnidadAF(Integer opcion) {
        if(opcion != null) {
            if(Constantes.OPCION_CERO.equals(opcion)) {
                tipoUnidadOrigenSeleccionada = comboTiposUnidadOrigen.getSelectedT();
                tipoUnidadOrigenSeleccionada = comboTiposUnidadOrigen.getSelectedT();
                 if (tipoUnidadOrigenSeleccionada != null) {
                    switch (tipoUnidadOrigenSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            this.esUnidadAdministrativaOrigen = Boolean.TRUE;
                            break;
                        case CENTRO_ESCOLAR:
                            this.esUnidadAdministrativaOrigen = Boolean.FALSE;
                            break;
                        default:
                            break;
                    }
                }
            } else if(Constantes.OPCION_UNO.equals(opcion)) {
                tipoUnidadDestinoSeleccionada = comboTiposUnidadDestino.getSelectedT();
                if (tipoUnidadDestinoSeleccionada != null) {
                    switch (tipoUnidadDestinoSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            this.esUnidadAdministrativaDestino = Boolean.TRUE;
                            break;
                        case CENTRO_ESCOLAR:
                            this.esUnidadAdministrativaDestino = Boolean.FALSE;
                            break;
                        default:
                            break;
                    }
                }
            }
            seleccionarUnidadActivoFijo(opcion);
        } 
    }
    
    public void seleccionarUnidadActivoFijo(Integer opcion) {
        try {
            if(opcion != null) {
                if(Constantes.OPCION_CERO.equals(opcion)) {
                    comboMunicipiosOrigen = new SofisComboG();
                    comboMunicipiosOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    unidadesAdministrativasOrigen = new ArrayList();
                    sedesOrigen = new ArrayList();
                   
                    if(comboUnidadesActivoFijoOrigen.getSelectedT() != null) {
                        unidadAFOrigenSeleccionada = comboUnidadesActivoFijoOrigen.getSelectedT();
                    }
                    
                    if(comboTiposUnidadOrigen.getSelectedT() != null) {
                        tipoUnidadOrigenSeleccionada = comboTiposUnidadOrigen.getSelectedT();
                        if (tipoUnidadOrigenSeleccionada != null) {
                            switch (tipoUnidadOrigenSeleccionada) {
                                case UNIDAD_ADMINISTRATIVA:
                                    this.esUnidadAdministrativaOrigen = Boolean.TRUE;
                                    break;
                                case CENTRO_ESCOLAR:
                                    this.esUnidadAdministrativaOrigen = Boolean.FALSE;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    
                    if(unidadAFOrigenSeleccionada != null && this.esUnidadAdministrativaOrigen != null) {
                        if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadOrigenSeleccionada)) {
                            FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                            fuad.setOrderBy(new String[]{"uadCodigo"});
                            fuad.setAscending(new boolean[]{true});
                            fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                            fuad.setUnidadActivoFijoId(unidadAFOrigenSeleccionada != null ? unidadAFOrigenSeleccionada.getUafPk() : null);
                            unidadesAdministrativasOrigen = unidadesAdministrativasRestClient.buscar(fuad);
                            
                        } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadOrigenSeleccionada)) {
                            if(unidadAFOrigenSeleccionada.getUafPk()!= null 
                                        && unidadAFOrigenSeleccionada.getUafDepartamento() != null
                                        && !unidadAFOrigenSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                FiltroMunicipio fm = new FiltroMunicipio();
                                fm.setMunHabilitado(Boolean.TRUE);
                                fm.setMunDepartamentoFk(unidadAFOrigenSeleccionada != null ? unidadAFOrigenSeleccionada.getUafDepartamento().getDepPk() : null);
                                fm.setAscending(new boolean[]{true});
                                fm.setOrderBy(new String[]{"munNombre"});
                                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                                
                                List<SgMunicipio> municipios = catalogosRestClient.buscarMunicipio(fm);
                                comboMunicipiosOrigen = new SofisComboG(municipios, "munNombre");
                                comboMunicipiosOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                                if(sedeOrigenSeleccionada != null && sedeOrigenSeleccionada.getSedDireccion().getDirMunicipio() != null) {
                                    comboMunicipiosOrigen.setSelectedT(sedeOrigenSeleccionada.getSedDireccion().getDirMunicipio()); 
                                }
                                
                                seleccionarMunicipio(0);
                            }
                       }
                        obtenerUltimoCorrelativo();
                   } 
                } else if(Constantes.OPCION_UNO.equals(opcion)) {
                    comboMunicipiosDestino = new SofisComboG();
                    comboMunicipiosDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                    unidadesAdministrativasDestino = new ArrayList();
                    sedesDestino = new ArrayList();

                    unidadAFDestinoSeleccionada = comboUnidadesActivoFijoDestino.getSelectedT();

                    if(comboTiposUnidadDestino.getSelectedT() != null) {
                        tipoUnidadDestinoSeleccionada = comboTiposUnidadDestino.getSelectedT();
                        
                        if (tipoUnidadDestinoSeleccionada != null) {
                            switch (tipoUnidadDestinoSeleccionada) {
                                case UNIDAD_ADMINISTRATIVA:
                                    this.esUnidadAdministrativaDestino = Boolean.TRUE;
                                    break;
                                case CENTRO_ESCOLAR:
                                    this.esUnidadAdministrativaDestino = Boolean.FALSE;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    
                    if(unidadAFDestinoSeleccionada != null && this.esUnidadAdministrativaDestino != null) {
                        if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadDestinoSeleccionada)) {
                            FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                            fuad.setOrderBy(new String[]{"uadCodigo"});
                            fuad.setAscending(new boolean[]{true});
                            fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                            fuad.setUnidadActivoFijoId(unidadAFDestinoSeleccionada != null ? unidadAFDestinoSeleccionada.getUafPk() : null);
                            unidadesAdministrativasDestino = unidadesAdministrativasRestClient.buscarSinPermisos(fuad);
                            
                        } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadDestinoSeleccionada)) {
                            if(unidadAFDestinoSeleccionada.getUafPk()!= null 
                                        && unidadAFDestinoSeleccionada.getUafDepartamento() != null
                                        && !unidadAFDestinoSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                FiltroMunicipio fm = new FiltroMunicipio();
                                fm.setMunHabilitado(Boolean.TRUE);
                                fm.setMunDepartamentoFk(unidadAFDestinoSeleccionada.getUafDepartamento().getDepPk());
                                fm.setAscending(new boolean[]{true});
                                fm.setOrderBy(new String[]{"munNombre"});
                                fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});

                                List<SgMunicipio> municipios = catalogosRestClient.buscarMunicipio(fm);
                                comboMunicipiosDestino = new SofisComboG(municipios, "munNombre");
                                comboMunicipiosDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                                
                                if(sedeDestinoSeleccionada != null && sedeDestinoSeleccionada.getSedDireccion().getDirMunicipio() != null) {
                                    comboMunicipiosDestino.setSelectedT(sedeDestinoSeleccionada.getSedDireccion().getDirMunicipio()); 
                                }
                                
                                seleccionarMunicipio(1);
                            }
                       }
                    }                 
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    public void seleccionarMunicipio(Integer opcion) {
        try {
            if(opcion != null) {
                FiltroSedes fil = new FiltroSedes();
                fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
                fil.setOrderBy(new String[]{"sedCodigo"});
                fil.setAscending(new boolean[]{true});
                fil.setIncluirCampos(new String[]{"sedCodigo","sedTipo", "sedNombre", "sedVersion"});   
                fil.setSedTipos(applicationBean.getTiposSedes());
                fil.setPriSubvencionada(Boolean.TRUE);
                if(Constantes.OPCION_CERO.equals(opcion)) {
                    if(comboUnidadesActivoFijoOrigen != null && comboUnidadesActivoFijoOrigen.getSelectedT() != null && comboUnidadesActivoFijoOrigen.getSelectedT() != null
                            && comboUnidadesActivoFijoOrigen.getSelectedT().getUafDepartamento() != null) {  
                        fil.setSedMunicipioId(comboMunicipiosOrigen != null && comboMunicipiosOrigen.getSelectedT() != null? comboMunicipiosOrigen.getSelectedT().getMunPk() : null);
                        fil.setSedDepartamentoId(comboUnidadesActivoFijoOrigen.getSelectedT().getUafDepartamento().getDepPk());
                        sedesOrigen= sedeRestClient.buscar(fil);
                        
                   } else {
                        sedesOrigen = new ArrayList();
                   }
                } else if(Constantes.OPCION_UNO.equals(opcion)) {
                    if(comboUnidadesActivoFijoDestino != null && comboUnidadesActivoFijoDestino.getSelectedT() != null && comboUnidadesActivoFijoDestino.getSelectedT() != null
                            && comboUnidadesActivoFijoDestino.getSelectedT().getUafDepartamento() != null) {  
                        fil.setSedMunicipioId(comboMunicipiosDestino != null && comboMunicipiosDestino.getSelectedT() != null? comboMunicipiosDestino.getSelectedT().getMunPk() : null);
                        fil.setSedDepartamentoId(comboUnidadesActivoFijoDestino.getSelectedT().getUafDepartamento().getDepPk());
                        fil.setSecurityOperation(null);
                        sedesDestino= sedeRestClient.buscar(fil);
                        
                    } else {
                        sedesDestino = new ArrayList();
                    }
                }
            } else {
                sedesOrigen = new ArrayList();
                sedesDestino = new ArrayList();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    public void seleccionarUnidadAdministrativa(Integer opcion){
        if(opcion != null) {
            if(Constantes.OPCION_CERO.equals(opcion)) {
                obtenerUltimoCorrelativo();
            }
        }
    }
    
    
    public void obtenerUltimoCorrelativo() {
        Integer correlativo = 0;       
        try {
            if(trasladoId == null) {
                entidadEnEdicion.setTraCodigoTraslado("");
                entidadEnEdicion.setTraCorrelativo(null);
                
                FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
                filtro.setMaxResults(1L);
                filtro.setOrderBy(new String[]{"traCorrelativo"});
                filtro.setAscending(new boolean[]{false});
                filtro.setTipoUnidad(tipoUnidadOrigenSeleccionada);
                filtro.setAnio(Long.valueOf(LocalDateTime.now().getYear()));
                if(tipoUnidadOrigenSeleccionada != null) {
                    if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadOrigenSeleccionada)) {
                        filtro.setUnidadAdministrativaId(unidadOrigenSeleccionada != null ? unidadOrigenSeleccionada.getUadPk() : null);
                    } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadOrigenSeleccionada)) {
                        filtro.setUnidadAdministrativaId(sedeOrigenSeleccionada != null ? sedeOrigenSeleccionada.getSedPk() : null);
                    }
                }

                if(filtro.getUnidadAdministrativaId() != null) {
                    List<SgAfTraslado> ultimoTraslado =trasladosRestClient.buscar(filtro);
                    if(ultimoTraslado != null && !ultimoTraslado.isEmpty()) {
                        if(ultimoTraslado.get(0).getTraCorrelativo() != null) {
                            correlativo = ultimoTraslado.get(0).getTraCorrelativo();
                        }
                    }
                    correlativo = correlativo + 1;
                    entidadEnEdicion.setTraCorrelativo(correlativo);
                    entidadEnEdicion.setTraCodigoTraslado(correlativo + "-" + LocalDateTime.now().getYear());
                    entidadEnEdicion.setTraAnio(LocalDateTime.now().getYear());
                }
            } 
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
            
    public void seleccionarTipoTraslado(){
        this.renderSeccionDestino = Boolean.FALSE;
        this.renderUnidadDestino = Boolean.FALSE;
        this.esTrasladoDefinitivo = Boolean.FALSE;
        
        tipoTrasladoSeleccionado = comboTipoTraslado.getSelectedT();
        if(tipoTrasladoSeleccionado != null && tipoTrasladoSeleccionado.getEtrCodigo().equals(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO)) {
            //Si no es traslado definitivo, que muestre la seccion destino.
            this.renderUnidadDestino = Boolean.TRUE;
            this.esTrasladoDefinitivo = Boolean.TRUE;
        } else {
            this.renderSeccionDestino = Boolean.TRUE;
        }
    }
    
    public List<SgAfEmpleados> completeEmpleado(String query) {
        try {
            if(unidadDestinoSeleccionada != null) {
                FiltroEmpleados femp = new FiltroEmpleados();
                femp.setMaxResults(10L);
                femp.setPerDUINombreCompleto(query);
                femp.setUnidadAdministrativaId(unidadDestinoSeleccionada.getUadPk());
                femp.setAscending(new boolean[]{true});
                femp.setOrderBy(new String[]{ "empPersonaFk.perNombreBusqueda","empPersonaFk.perDui"});

                femp.setIncluirCampos(new String[]{"empPk","empVersion","empPersonaFk.perPk","empPersonaFk.perVersion",
                                                "empPersonaFk.perPrimerNombre","empPersonaFk.perSegundoNombre","empPersonaFk.perTercerNombre",
                                                "empPersonaFk.perPrimerApellido","empPersonaFk.perSegundoApellido","empPersonaFk.perTercerApellido","empPersonaFk.perDui"});



                return catalogosRestClient.buscarEmpleados(femp);
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
    
    public void limpiar() {
        filtroBienes = new FiltroBienesDepreciables();
        
        sedeOrigenSeleccionada = null;
        sedeDestinoSeleccionada = null;
        unidadOrigenSeleccionada = null;
        unidadDestinoSeleccionada = null;
        tipoUnidadOrigenSeleccionada = null;
        tipoUnidadDestinoSeleccionada = null;
        comboTiposUnidadOrigen.setSelected(-1);
        
        comboMunicipiosOrigen.setSelected(-1);
        comboMunicipiosDestino.setSelected(-1);
        
        comboTiposUnidadOrigen.setSelected(-1);
        comboTiposUnidadDestino.setSelected(-1);
        
        comboUnidadesActivoFijoOrigen.setSelected(-1);
        comboUnidadesActivoFijoDestino.setSelected(-1);
        
        comboUnidadAdministrativaOrigen.setSelected(-1);
        comboUnidadAdministrativaDestino.setSelected(-1);

        bienesDestino = new ArrayList();
        totalBienesOrigen = 0;
        totalBienesDestino = 0;
    }
    
    public void crearNuevoMotivoRechazo() {
        try {
            motivoRechazoTraslado = new SgAfMotivoRechazoTraslado();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_3, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void nuevoEnvioAprobacion() {
        if(entidadEnEdicion != null) {
            entidadEnEdicion.setTraNombreAutoriza("");
            entidadEnEdicion.setTraNombreRecibe("");
            entidadEnEdicion.setTraCargoAutoriza("");
            entidadEnEdicion.setTraCargoRecibe("");
            entidadEnEdicion.setTraObservacion("");
        }
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
 
    public CatalogosRestClient getCatalogosRestClient() {
        return catalogosRestClient;
    }

    public void setCatalogosRestClient(CatalogosRestClient catalogosRestClient) {
        this.catalogosRestClient = catalogosRestClient;
    }

    

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }

    public Boolean getEsUnidadAdministrativa() {
        return esUnidadAdministrativa;
    }

    public void setEsUnidadAdministrativa(Boolean esUnidadAdministrativa) {
        this.esUnidadAdministrativa = esUnidadAdministrativa;
    }

    public Long getEstRev() {
        return estRev;
    }

    public void setEstRev(Long estRev) {
        this.estRev = estRev;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public BienesDepreciablesRestClient getBienesRestClient() {
        return bienesRestClient;
    }

    public void setBienesRestClient(BienesDepreciablesRestClient bienesRestClient) {
        this.bienesRestClient = bienesRestClient;
    }

    public List<SgAfBienDepreciable> getBienesDestino() {
        return bienesDestino;
    }

    public void setBienesDestino(List<SgAfBienDepreciable> bienesDestino) {
        this.bienesDestino = bienesDestino;
    }
    
    public SgAfTraslado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfTraslado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }
    
    public SgAfEstadosBienes getEstadoExistente() {
        return estadoExistente;
    }

    public void setEstadoExistente(SgAfEstadosBienes estadoExistente) {
        this.estadoExistente = estadoExistente;
    }

    public Boolean getConfirmarTraslado() {
        return confirmarTraslado;
    }

    public void setConfirmarTraslado(Boolean confirmarTraslado) {
        this.confirmarTraslado = confirmarTraslado;
    }

    public Boolean getConfirmarRecibido() {
        return confirmarRecibido;
    }

    public void setConfirmarRecibido(Boolean confirmarRecibido) {
        this.confirmarRecibido = confirmarRecibido;
    }

    public SgAfEstadosBienes getEstadoEnSolicitud() {
        return estadoEnSolicitud;
    }

    public void setEstadoEnSolicitud(SgAfEstadosBienes estadoEnSolicitud) {
        this.estadoEnSolicitud = estadoEnSolicitud;
    }

    public Boolean getActivosFijos() {
        return activosFijos;
    }

    public void setActivosFijos(Boolean activosFijos) {
        this.activosFijos = activosFijos;
    }

    public Boolean getCrearSolicitud() {
        return crearSolicitud;
    }

    public void setCrearSolicitud(Boolean crearSolicitud) {
        this.crearSolicitud = crearSolicitud;
    }

    public Boolean getEnviarSolicitud() {
        return enviarSolicitud;
    }

    public void setEnviarSolicitud(Boolean enviarSolicitud) {
        this.enviarSolicitud = enviarSolicitud;
    }

    public Boolean getPermiteBusquedas() {
        return permiteBusquedas;
    }

    public void setPermiteBusquedas(Boolean permiteBusquedas) {
        this.permiteBusquedas = permiteBusquedas;
    }

    public SolicitudTraslado getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudTraslado solicitud) {
        this.solicitud = solicitud;
    }

    public SgAfEstadosBienes getEstadoEnviado() {
        return estadoEnviado;
    }

    public void setEstadoEnviado(SgAfEstadosBienes estadoEnviado) {
        this.estadoEnviado = estadoEnviado;
    }

    public SgAfEstadosBienes getEstadoTrasladado() {
        return estadoTrasladado;
    }

    public void setEstadoTrasladado(SgAfEstadosBienes estadoTrasladado) {
        this.estadoTrasladado = estadoTrasladado;
    }

    public SofisComboG<SgAfEstadosTraslado> getComboTipoTraslado() {
        return comboTipoTraslado;
    }

    public void setComboTipoTraslado(SofisComboG<SgAfEstadosTraslado> comboTipoTraslado) {
        this.comboTipoTraslado = comboTipoTraslado;
    }

    public Long getTrasladoId() {
        return trasladoId;
    }

    public void setTrasladoId(Long trasladoId) {
        this.trasladoId = trasladoId;
    }

    public TrasladosRestClient getTrasladosRestClient() {
        return trasladosRestClient;
    }

    public void setTrasladosRestClient(TrasladosRestClient trasladosRestClient) {
        this.trasladosRestClient = trasladosRestClient;
    }

    public UnidadesActivoFijoRestClient getUnidadesActivoFijoRestClient() {
        return unidadesActivoFijoRestClient;
    }

    public void setUnidadesActivoFijoRestClient(UnidadesActivoFijoRestClient unidadesActivoFijoRestClient) {
        this.unidadesActivoFijoRestClient = unidadesActivoFijoRestClient;
    }

    public UnidadesAdministrativasRestClient getUnidadesAdministrativasRestClient() {
        return unidadesAdministrativasRestClient;
    }

    public void setUnidadesAdministrativasRestClient(UnidadesAdministrativasRestClient unidadesAdministrativasRestClient) {
        this.unidadesAdministrativasRestClient = unidadesAdministrativasRestClient;
    }

    public SgAfUnidadesAdministrativas getUnidadOrigenSeleccionada() {
        return unidadOrigenSeleccionada;
    }

    public void setUnidadOrigenSeleccionada(SgAfUnidadesAdministrativas unidadOrigenSeleccionada) {
        this.unidadOrigenSeleccionada = unidadOrigenSeleccionada;
    }

    public SgSede getSedeOrigenSeleccionada() {
        return sedeOrigenSeleccionada;
    }

    public void setSedeOrigenSeleccionada(SgSede sedeOrigenSeleccionada) {
        this.sedeOrigenSeleccionada = sedeOrigenSeleccionada;
    }

    public SgAfUnidadesActivoFijo getUnidadAFOrigenSeleccionada() {
        return unidadAFOrigenSeleccionada;
    }

    public void setUnidadAFOrigenSeleccionada(SgAfUnidadesActivoFijo unidadAFOrigenSeleccionada) {
        this.unidadAFOrigenSeleccionada = unidadAFOrigenSeleccionada;
    }

    public TipoUnidad getTipoUnidadOrigenSeleccionada() {
        return tipoUnidadOrigenSeleccionada;
    }

    public void setTipoUnidadOrigenSeleccionada(TipoUnidad tipoUnidadOrigenSeleccionada) {
        this.tipoUnidadOrigenSeleccionada = tipoUnidadOrigenSeleccionada;
    }

    public SgAfUnidadesAdministrativas getUnidadDestinoSeleccionada() {
        return unidadDestinoSeleccionada;
    }

    public void setUnidadDestinoSeleccionada(SgAfUnidadesAdministrativas unidadDestinoSeleccionada) {
        this.unidadDestinoSeleccionada = unidadDestinoSeleccionada;
    }

    public SgSede getSedeDestinoSeleccionada() {
        return sedeDestinoSeleccionada;
    }

    public void setSedeDestinoSeleccionada(SgSede sedeDestinoSeleccionada) {
        this.sedeDestinoSeleccionada = sedeDestinoSeleccionada;
    }

    public SgAfUnidadesActivoFijo getUnidadAFDestinoSeleccionada() {
        return unidadAFDestinoSeleccionada;
    }

    public void setUnidadAFDestinoSeleccionada(SgAfUnidadesActivoFijo unidadAFDestinoSeleccionada) {
        this.unidadAFDestinoSeleccionada = unidadAFDestinoSeleccionada;
    }

    public TipoUnidad getTipoUnidadDestinoSeleccionada() {
        return tipoUnidadDestinoSeleccionada;
    }

    public void setTipoUnidadDestinoSeleccionada(TipoUnidad tipoUnidadDestinoSeleccionada) {
        this.tipoUnidadDestinoSeleccionada = tipoUnidadDestinoSeleccionada;
    }

    public SofisComboG<SgSede> getComboSedesOrigen() {
        return comboSedesOrigen;
    }

    public void setComboSedesOrigen(SofisComboG<SgSede> comboSedesOrigen) {
        this.comboSedesOrigen = comboSedesOrigen;
    }

    public SofisComboG getComboUnidadAdministrativaOrigen() {
        return comboUnidadAdministrativaOrigen;
    }

    public void setComboUnidadAdministrativaOrigen(SofisComboG comboUnidadAdministrativaOrigen) {
        this.comboUnidadAdministrativaOrigen = comboUnidadAdministrativaOrigen;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijoOrigen() {
        return comboUnidadesActivoFijoOrigen;
    }

    public void setComboUnidadesActivoFijoOrigen(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoOrigen) {
        this.comboUnidadesActivoFijoOrigen = comboUnidadesActivoFijoOrigen;
    }

    public SofisComboG<TipoUnidad> getComboTiposUnidadOrigen() {
        return comboTiposUnidadOrigen;
    }

    public void setComboTiposUnidadOrigen(SofisComboG<TipoUnidad> comboTiposUnidadOrigen) {
        this.comboTiposUnidadOrigen = comboTiposUnidadOrigen;
    }

    public SofisComboG<SgMunicipio> getComboMunicipiosOrigen() {
        return comboMunicipiosOrigen;
    }

    public void setComboMunicipiosOrigen(SofisComboG<SgMunicipio> comboMunicipiosOrigen) {
        this.comboMunicipiosOrigen = comboMunicipiosOrigen;
    }

    public SofisComboG<SgSede> getComboSedesDestino() {
        return comboSedesDestino;
    }

    public void setComboSedesDestino(SofisComboG<SgSede> comboSedesDestino) {
        this.comboSedesDestino = comboSedesDestino;
    }

    public SofisComboG getComboUnidadAdministrativaDestino() {
        return comboUnidadAdministrativaDestino;
    }

    public void setComboUnidadAdministrativaDestino(SofisComboG comboUnidadAdministrativaDestino) {
        this.comboUnidadAdministrativaDestino = comboUnidadAdministrativaDestino;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijoDestino() {
        return comboUnidadesActivoFijoDestino;
    }

    public void setComboUnidadesActivoFijoDestino(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijoDestino) {
        this.comboUnidadesActivoFijoDestino = comboUnidadesActivoFijoDestino;
    }

    public SofisComboG<TipoUnidad> getComboTiposUnidadDestino() {
        return comboTiposUnidadDestino;
    }

    public void setComboTiposUnidadDestino(SofisComboG<TipoUnidad> comboTiposUnidadDestino) {
        this.comboTiposUnidadDestino = comboTiposUnidadDestino;
    }

    public SofisComboG<SgMunicipio> getComboMunicipiosDestino() {
        return comboMunicipiosDestino;
    }

    public void setComboMunicipiosDestino(SofisComboG<SgMunicipio> comboMunicipiosDestino) {
        this.comboMunicipiosDestino = comboMunicipiosDestino;
    }

    public Boolean getEsUnidadAdministrativaOrigen() {
        return esUnidadAdministrativaOrigen;
    }

    public void setEsUnidadAdministrativaOrigen(Boolean esUnidadAdministrativaOrigen) {
        this.esUnidadAdministrativaOrigen = esUnidadAdministrativaOrigen;
    }

    public Boolean getEsUnidadAdministrativaDestino() {
        return esUnidadAdministrativaDestino;
    }

    public void setEsUnidadAdministrativaDestino(Boolean esUnidadAdministrativaDestino) {
        this.esUnidadAdministrativaDestino = esUnidadAdministrativaDestino;
    }

    public Integer getTotalBienesOrigen() {
        return totalBienesOrigen;
    }

    public void setTotalBienesOrigen(Integer totalBienesOrigen) {
        this.totalBienesOrigen = totalBienesOrigen;
    }

    public Integer getTotalBienesDestino() {
        return totalBienesDestino;
    }

    public void setTotalBienesDestino(Integer totalBienesDestino) {
        this.totalBienesDestino = totalBienesDestino;
    }

    public List<SgAfUnidadesActivoFijo> getListaUnidadesAF() {
        return listaUnidadesAF;
    }

    public void setListaUnidadesAF(List<SgAfUnidadesActivoFijo> listaUnidadesAF) {
        this.listaUnidadesAF = listaUnidadesAF;
    }

    public SgAfEstadosTraslado getTipoTrasladoSeleccionado() {
        return tipoTrasladoSeleccionado;
    }

    public void setTipoTrasladoSeleccionado(SgAfEstadosTraslado tipoTrasladoSeleccionado) {
        this.tipoTrasladoSeleccionado = tipoTrasladoSeleccionado;
    }

    public Boolean getRenderUnidadDestino() {
        return renderUnidadDestino;
    }

    public void setRenderUnidadDestino(Boolean renderUnidadDestino) {
        this.renderUnidadDestino = renderUnidadDestino;
    }

    public Boolean getRenderSeccionDestino() {
        return renderSeccionDestino;
    }

    public void setRenderSeccionDestino(Boolean renderSeccionDestino) {
        this.renderSeccionDestino = renderSeccionDestino;
    }

    public Integer getProceso() {
        return proceso;
    }

    public void setProceso(Integer proceso) {
        this.proceso = proceso;
    }

    public Boolean getEsTrasladoDefinitivo() {
        return esTrasladoDefinitivo;
    }

    public void setEsTrasladoDefinitivo(Boolean esTrasladoDefinitivo) {
        this.esTrasladoDefinitivo = esTrasladoDefinitivo;
    }

    public Boolean getGuardarCambios() {
        return guardarCambios;
    }

    public void setGuardarCambios(Boolean guardarCambios) {
        this.guardarCambios = guardarCambios;
    }

    public Boolean getEditarFecha() {
        return editarFecha;
    }

    public void setEditarFecha(Boolean editarFecha) {
        this.editarFecha = editarFecha;
    }

    public SgAfEstadosBienes getEstadoRechazado() {
        return estadoRechazado;
    }

    public void setEstadoRechazado(SgAfEstadosBienes estadoRechazado) {
        this.estadoRechazado = estadoRechazado;
    }

    public List<SgAfMotivoRechazoTraslado> getListaMotivosRechazo() {
        return listaMotivosRechazo;
    }

    public void setListaMotivosRechazo(List<SgAfMotivoRechazoTraslado> listaMotivosRechazo) {
        this.listaMotivosRechazo = listaMotivosRechazo;
    }

    public SgAfMotivoRechazoTraslado getMotivoRechazoTraslado() {
        return motivoRechazoTraslado;
    }

    public void setMotivoRechazoTraslado(SgAfMotivoRechazoTraslado motivoRechazoTraslado) {
        this.motivoRechazoTraslado = motivoRechazoTraslado;
    }

    public List<SgAfTrasladoDetalle> getListaDetalleRechazo() {
        return listaDetalleRechazo;
    }

    public void setListaDetalleRechazo(List<SgAfTrasladoDetalle> listaDetalleRechazo) {
        this.listaDetalleRechazo = listaDetalleRechazo;
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

    public FiltroTrasladoDetalle getFiltroTrasladoDetalle() {
        return filtroTrasladoDetalle;
    }

    public void setFiltroTrasladoDetalle(FiltroTrasladoDetalle filtroTrasladoDetalle) {
        this.filtroTrasladoDetalle = filtroTrasladoDetalle;
    }

    public Boolean getEditarEmpleado() {
        return editarEmpleado;
    }

    public void setEditarEmpleado(Boolean editarEmpleado) {
        this.editarEmpleado = editarEmpleado;
    }

    public Integer getPaginadoDestino() {
        return paginadoDestino;
    }

    public void setPaginadoDestino(Integer paginadoDestino) {
        this.paginadoDestino = paginadoDestino;
    }

    public Long getTotalResultadosAnterior() {
        return totalResultadosAnterior;
    }

    public void setTotalResultadosAnterior(Long totalResultadosAnterior) {
        this.totalResultadosAnterior = totalResultadosAnterior;
    }

    public SgAfTipoBienes getTipoBienSeleccionado() {
        return tipoBienSeleccionado;
    }

    public void setTipoBienSeleccionado(SgAfTipoBienes tipoBienSeleccionado) {
        this.tipoBienSeleccionado = tipoBienSeleccionado;
    }

    public LazyBienesDepreciablesDataModel getBienesDepreciablesLazyModel() {
        return bienesDepreciablesLazyModel;
    }

    public void setBienesDepreciablesLazyModel(LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel) {
        this.bienesDepreciablesLazyModel = bienesDepreciablesLazyModel;
    }

    public List<SgAfUnidadesAdministrativas> getUnidadesAdministrativasOrigen() {
        return unidadesAdministrativasOrigen;
    }

    public void setUnidadesAdministrativasOrigen(List<SgAfUnidadesAdministrativas> unidadesAdministrativasOrigen) {
        this.unidadesAdministrativasOrigen = unidadesAdministrativasOrigen;
    }

    public List<SgSede> getSedesOrigen() {
        return sedesOrigen;
    }

    public void setSedesOrigen(List<SgSede> sedesOrigen) {
        this.sedesOrigen = sedesOrigen;
    }

    public List<SgAfUnidadesAdministrativas> getUnidadesAdministrativasDestino() {
        return unidadesAdministrativasDestino;
    }

    public void setUnidadesAdministrativasDestino(List<SgAfUnidadesAdministrativas> unidadesAdministrativasDestino) {
        this.unidadesAdministrativasDestino = unidadesAdministrativasDestino;
    }

    public List<SgSede> getSedesDestino() {
        return sedesDestino;
    }

    public void setSedesDestino(List<SgSede> sedesDestino) {
        this.sedesDestino = sedesDestino;
    }
}
