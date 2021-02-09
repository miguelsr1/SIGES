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
import java.util.LinkedList;
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
import sv.gob.mined.siges.web.dto.SgAfActaDescargo;
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.dto.SgAfDescargo;
import sv.gob.mined.siges.web.dto.SgAfDescargoDetalle;
import sv.gob.mined.siges.web.dto.SgAfMotivoRechazoDescargo;
import sv.gob.mined.siges.web.dto.SolicitudDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.dto.catalogo.SgAfOpcionesDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfTipoBienes;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessError;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyBienesDepreciablesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DescargosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TipoBienesRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DescargoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DescargoBean.class.getName());
     
    @Inject
    @Param(name = "id")
    private Long descargoId;

    @Inject
    @Param(name = "rev")
    private Long estRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private BienesDepreciablesRestClient bienesRestClient;
    
    @Inject
    private DescargosRestClient descargosRestClient;
    
    @Inject
    private ApplicationBean applicationBean;
    
    @Inject
    private TipoBienesRestClient tipoBienesRestClient;
    
    private LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel;
    
    private SgAfDescargo entidadEnEdicion = new SgAfDescargo();
    private SgAfActaDescargo actaEnEdicion = new SgAfActaDescargo();
    
    private SofisComboG<TipoUnidad> comboTiposUnidad;
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo;
    private SofisComboG<SgMunicipio> comboMunicipios;
    private SofisComboG<SgAfEstadosDescargo> comboEstadosDescargo;
    private SofisComboG<SgAfOpcionesDescargo> comboOpcionesDescargo;
    
    private TipoUnidad tipoUnidadSeleccionada;
    private FiltroCodiguera filtro;
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
    private Integer paginado = 10;
    private Integer paginadoDestino = 10;
    private Boolean esUnidadAdministrativa = Boolean.FALSE;
    
    private SgAfUnidadesAdministrativas unidadSeleccionada;
    private SgSede sedeSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFSeleccionada;
    private SgAfTipoBienes tipoBienSeleccionado;
    
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativas = new ArrayList();
    private List<SgSede> sedes = new ArrayList();
    
    private SgAfEstadosBienes estadoExistente;
    private SgAfEstadosBienes estadoEnProcesoDescargo;
    private SgAfEstadosBienes estadoDescargado;
    private SgAfEstadosBienes estadoRechazado;
    private SgAfEstadosBienes estadoEnSolicitud;
    private SgAfEstadosBienes estadoTrasladado;
    
    private List<SgAfBienDepreciable> bienesDestino = new ArrayList();
    private List<SgAfMotivoRechazoDescargo> listaMotivosRechazo = new ArrayList();
    
    private Boolean soloLectura = Boolean.FALSE;
    
    private Integer totalBienesOrigen = 0;
    private Integer totalBienesDestino = 0;
    
    private List<SgAfUnidadesActivoFijo> listaUnidadesAF = new ArrayList();
    private List<SgMunicipio> listaMunicipios;
    private SolicitudDescargo solicitud;
    private Boolean activosFijos; 
    private Boolean crearSolicitud = Boolean.TRUE; 
    private Boolean enviarSolicitud = Boolean.FALSE; 
    private Boolean descargarSolicitud = Boolean.FALSE; 
    private Boolean permiteBusquedas = Boolean.TRUE;
    private Boolean descargado = Boolean.FALSE;
    
    private SgAfMotivoRechazoDescargo motivoRechazoDescargo;
    
    private String numeroAcuerdo;
    private LocalDate fechaAcuerdo;
    private String seAutoriza;
    private String urlReporte="";
    private Long totalResultados = 0L;
    private Long totalResultadosAnterior = 0L;
    
    private List<SgAfOpcionesDescargo> opcionesDescargo = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            if (descargoId != null && descargoId > 0) {
                if (estRev != null && estRev > 0) {
                    this.actualizar(descargosRestClient.obtenerEnRevision(descargoId, estRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.soloLectura = this.editable != null ? !this.editable : this.soloLectura;
                    this.actualizar(descargosRestClient.obtenerPorId(descargoId, Boolean.TRUE)); 
                }
            } else {
                actaEnEdicion = new SgAfActaDescargo();
                cargarEntidad();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarVisualizacion() {
        if(entidadEnEdicion != null) {
            permiteBusquedas = Boolean.FALSE;
            if((estadoEnSolicitud != null && estadoEnSolicitud.getEbiPk() != null && entidadEnEdicion.getDesEstadoFk().getEbiPk().equals(estadoEnSolicitud.getEbiPk()))
                    || (estadoRechazado != null && estadoRechazado.getEbiPk() != null && entidadEnEdicion.getDesEstadoFk().getEbiPk().equals(estadoRechazado.getEbiPk()))) {
                this.enviarSolicitud = Boolean.TRUE;
                this.crearSolicitud = Boolean.TRUE;
                this.permiteBusquedas = Boolean.TRUE;
                this.descargarSolicitud = Boolean.FALSE;
                this.soloLectura = Boolean.FALSE;
            } else if(estadoEnProcesoDescargo != null && estadoEnProcesoDescargo.getEbiPk() != null && entidadEnEdicion.getDesEstadoFk().getEbiPk().equals(estadoEnProcesoDescargo.getEbiPk())) {
                this.descargarSolicitud = Boolean.TRUE;
                this.enviarSolicitud = Boolean.FALSE;
                this.crearSolicitud = Boolean.FALSE;
                this.soloLectura = Boolean.TRUE;
            } else {
                if(estadoDescargado != null && estadoDescargado.getEbiPk() != null && entidadEnEdicion.getDesEstadoFk().getEbiPk().equals(estadoDescargado.getEbiPk())) {
                     this.descargado = Boolean.TRUE;
                }
                this.descargarSolicitud = Boolean.FALSE;
                this.enviarSolicitud = Boolean.FALSE;
                this.crearSolicitud = Boolean.FALSE;
                this.soloLectura = Boolean.TRUE;
            }
        }
    }
    
    public void buscar(Boolean validarConBienesDestino, Boolean inicializar) {
        try {
            if(inicializar != null && inicializar) {
                filtroBienes = new FiltroBienesDepreciables();
            }
            filtroBienes.setIncluirCampos(new String[]{"bdePk","bdeVersion","bdeCodigoInventario","bdeValorAdquisicion",
                "bdeFechaAdquisicion","bdeTipoBien.tbiPk","bdeTipoBien.tbiVersion","bdeTipoBien.tbiCodigo","bdeTipoBien.tbiNombre","bdeTipoBien.tbiCategoriaBienes.cabPk","bdeTipoBien.tbiCategoriaBienes.cabVersion",
                "bdeTipoBien.tbiCategoriaBienes.cabCodigo","bdeTipoBien.tbiCategoriaBienes.cabNombre","bdeUnidadesAdministrativas.uadPk","bdeUnidadesAdministrativas.uadCodigo",
                "bdeUnidadesAdministrativas.uadNombre","bdeSede.sedPk","bdeSede.sedNombre","bdeSede.sedCodigo","bdeSede.sedVersion", "bdeSede.sedTipo","bdeEstadosBienes.ebiPk","bdeEstadosBienes.ebiVersion","bdeFechaDescargo"});
            
            filtroBienes.setActivos(activosFijos);

            filtroBienes.setEstadoId(estadoExistente != null ? estadoExistente.getEbiPk() : null);

            filtroBienes.setTipoBienId(tipoBienSeleccionado != null ? tipoBienSeleccionado.getTbiPk() : null);
            
            filtroBienes.setValidarEstadoSolicitudVacio(Boolean.TRUE); //Para que filtre tambien por el estado de la solicitud vacio(NULL), es decir que no esté en ninguna solicitud, este filtro va de la mano con el estadoId
            filtroBienes.setTrasladado(Boolean.TRUE);
            filtroBienes.setEstadoCodigo(estadoTrasladado != null ? estadoTrasladado.getEbiCodigo() : "");
            
            if(unidadAFSeleccionada != null) {
                filtroBienes.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                if(unidadAFSeleccionada.getUafDepartamento() != null 
                    && unidadAFSeleccionada.getUafDepartamento().getDepPk() != null) {
                    filtroBienes.setDepartamentoId(unidadAFSeleccionada.getUafDepartamento().getDepPk());
                }
            }
            
            filtroBienes.setTipoUnidad(comboTiposUnidad.getSelectedT());
            
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(comboTiposUnidad.getSelectedT())) {
                filtroBienes.setUnidadAdministrativaId(unidadSeleccionada != null ? unidadSeleccionada.getUadPk() : null);
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(comboTiposUnidad.getSelectedT())) {
                filtroBienes.setUnidadAdministrativaId(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            }
            
            if(validar(filtroBienes)) {
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
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DESCARGO_BIENES)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public  Boolean validar(FiltroBienesDepreciables filtro) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (filtro == null) {
            ge.addError(Mensajes.ERROR_FILTRO_DATO_VACIO);
        } else {    
            if (filtro.getUnidadActivoFijoId()== null) {
               ge.addError("unidadActivoFijoId", Mensajes.ERROR_FILTRO_UNIDAD_ACTIVO_FIJO_VACIO);
            }
            
            if (filtro.getUnidadAdministrativaId()== null) {
               ge.addError("unidadAdministrativaId", Mensajes.ERROR_FILTRO_UNIDAD_ADMINISTRATIVA_VACIO);
            }
            
            if (filtro.getActivos()== null) {
               ge.addError("activos", Mensajes.ERROR_FILTRO_TIPO_ACTIVO);
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
            
            filtro.setOrderBy(new String[]{"tunCodigo"});
            filtro.setIncluirCampos(new String[]{"tunNombre", "tunEsUnidadAdministrativa","tunVersion"});
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            if(sessionBean.getUnidadADDefecto() != null) {
                tipoUnidad.add(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            } else if(sessionBean.getSedeDefecto() != null) {
                tipoUnidad.add(TipoUnidad.CENTRO_ESCOLAR);
                comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            } else {
                tipoUnidad = new ArrayList(Arrays.asList(TipoUnidad.values()));
                comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
                comboTiposUnidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            comboTiposUnidad.ordenar();
            
            FiltroUnidadesActivoFijo fua = new FiltroUnidadesActivoFijo();
            fua.setHabilitado(Boolean.TRUE);
            fua.setOrderBy(new String[]{"uafCodigo"});
            fua.setAscending(new boolean[]{true});
            fua.setIncluirCampos(new String[]{"uafCodigo","uafNombre", "uafDepartamento","uafVersion"});
            //listaUnidadesAF = unidadesActivoFijoRestClient.buscar(fua);
            
            List<SgAfUnidadesActivoFijo> unidades = unidadesActivoFijoRestClient.buscar(fua);
            if(sessionBean.getSedeDefecto() != null) {
                if(unidades != null && !unidades.isEmpty()) {
                    for(SgAfUnidadesActivoFijo unidadesAF : unidades) {
                        if(!unidadesAF.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            listaUnidadesAF.add(unidadesAF);//Agregamos las unidades que no son UNIDAD CENTRAL
                        }
                    }
                }
                
            } else {
                listaUnidadesAF = unidades;
            }
            
            comboUnidadesActivoFijo = new SofisComboG(listaUnidadesAF, "codigoNombre");
            comboUnidadesActivoFijo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            filtro.setOrderBy(new String[]{"edeCodigo"});
            filtro.setIncluirCampos(new String[]{"edeNombre","edeVersion"});
            List<SgAfEstadosDescargo> estadosDescargo = catalogosRestClient.buscarEstadosDescargo(filtro);
            comboEstadosDescargo = new SofisComboG(estadosDescargo, "edeNombre");
            comboEstadosDescargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            filtro.setOrderBy(new String[]{"odeCodigo"});
            filtro.setIncluirCampos(new String[]{"odeNombre","odeCodigo","odeVersion"});
            opcionesDescargo = catalogosRestClient.buscarOpcionesDescargo(filtro);
            comboOpcionesDescargo = new SofisComboG();
            comboOpcionesDescargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrderBy(new String[]{"ebiCodigo"});
            filtro.setIncluirCampos(new String[]{"ebiNombre","ebiCodigo","ebiVersion"});
            List<SgAfEstadosBienes> estadosBien = catalogosRestClient.buscarEstadosBienes(filtro);
            if(estadosBien != null && !estadosBien.isEmpty()) {
                for(SgAfEstadosBienes estado : estadosBien) {
                    if(Constantes.CODIGO_ESTADO_EXISTENTE.equals(estado.getEbiCodigo().trim())) {
                        estadoExistente = estado;
                    } else if(Constantes.CODIGO_ESTADO_PROCESO_DESCARGO.equals(estado.getEbiCodigo().trim())) {
                        estadoEnProcesoDescargo = estado;
                    } else if(Constantes.CODIGO_ESTADO_DESCARGADO.equals(estado.getEbiCodigo().trim())) {
                        estadoDescargado = estado;
                    } else if(Constantes.CODIGO_ESTADO_SOLICITUD_DESCARGO.equals(estado.getEbiCodigo().trim())) {
                        estadoEnSolicitud = estado;
                    } else if(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA.equals(estado.getEbiCodigo().trim())) {
                        estadoRechazado = estado;
                    } else if(Constantes.CODIGO_ESTADO_TRASLADADO.equals(estado.getEbiCodigo().trim())) {
                        estadoTrasladado = estado;
                    }
                }
                
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiar() {
        filtroBienes = new FiltroBienesDepreciables();
        comboTiposUnidad.setSelected(-1);
        comboUnidadesActivoFijo.setSelected(-1);
        comboEstadosDescargo.setSelected(-1);
        
        comboTiposUnidad.setSelected(-1);
        
        comboMunicipios = new SofisComboG();
        comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
 
        unidadAFSeleccionada = null;
        unidadSeleccionada = null;
        sedeSeleccionada = null;
        activosFijos = null;
        bienesDestino = new ArrayList();
        totalBienesOrigen = 0;
        totalBienesDestino = 0;
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
    
    
    public void seleccionarTipoUnidadAF() {
        esUnidadAdministrativa = null;
        tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
  
        if (tipoUnidadSeleccionada != null) {
            switch (tipoUnidadSeleccionada) {
                case UNIDAD_ADMINISTRATIVA:
                    this.esUnidadAdministrativa = Boolean.TRUE;
                    break;
                case CENTRO_ESCOLAR:
                    this.esUnidadAdministrativa = Boolean.FALSE;
                    break;
                default:
                    break;
            }
        }
        seleccionarUnidadActivoFijo();
    }
    
    public void seleccionarUnidadAdministrativa(){
       limpiarTablas();
    }
    public void cargarEntidad() {
        try {
            sedeSeleccionada = null;
            unidadSeleccionada = null;
            cargarSedePorDefecto();
            cargarUADPorDefecto();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarSedePorDefecto() throws Exception {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionada = sessionBean.getSedeDefecto();
            entidadEnEdicion.setDesSedeFk(sedeSeleccionada);
            this.esUnidadAdministrativa = Boolean.FALSE;
            tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadSeleccionada);
            
            comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
            
            if(sedeSeleccionada!= null) {
                if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                    for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                        if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                            unidadAFSeleccionada = unidadesAF;
                        }
                    }
                } 

            }

            comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
                    
            seleccionarUnidadActivoFijo();
        }
    }
    public void cargarUADPorDefecto() throws Exception {
        if (sessionBean.getUnidadADDefecto() != null) {
            unidadSeleccionada = sessionBean.getUnidadADDefecto();
            entidadEnEdicion.setDesUnidadAdministrativaFk(unidadSeleccionada);
            this.esUnidadAdministrativa = Boolean.TRUE;
            tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadSeleccionada);
            
            comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
            
            if(unidadSeleccionada != null) {
                unidadAFSeleccionada = unidadSeleccionada.getUadUnidadActivoFijoFk();
            } 

            comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
            
            seleccionarUnidadActivoFijo();
        }
    }
    
    public void seleccionarTipoActivo() {
        List<SgAfOpcionesDescargo> opcionesDescargoTemp = new ArrayList();
        if(this.getActivosFijos() != null && this.getActivosFijos()) {
            for(SgAfOpcionesDescargo op: opcionesDescargo) {
                if(!op.getOdeCodigo().equals(Constantes.CODIGO_TIPO_DESCARGO_PERMUTA)) {
                    opcionesDescargoTemp.add(op);
                }
            } 
        } else {
            opcionesDescargoTemp = opcionesDescargo;
        }
        comboOpcionesDescargo = new SofisComboG(opcionesDescargoTemp, "odeNombre");
        comboOpcionesDescargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        limpiarTablas();
    }
    
    public void agregaroEditar() {
        if(entidadEnEdicion.getDesActaDescargo() != null && entidadEnEdicion.getDesActaDescargo().getAdePk() != null) {
            actaEnEdicion = entidadEnEdicion.getDesActaDescargo();
        } else {
            actaEnEdicion = new SgAfActaDescargo();
        }
    }
    public void limpiarTablas() {
        bienesDepreciablesLazyModel = null;
        bienesDestino = new ArrayList();
        totalResultados = 0L;
        totalBienesDestino = 0;
        totalResultadosAnterior= 0L;  
    }
    public void seleccionarUnidadActivoFijo() {
        try {
            
            limpiarTablas();
            
            if(comboUnidadesActivoFijo.getSelectedT() != null) {
                unidadAFSeleccionada = comboUnidadesActivoFijo.getSelectedT();
            }
            
            unidadesAdministrativas = new ArrayList();
            sedes = new ArrayList();
            
            
            if(tipoUnidadSeleccionada == null) {
                tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
                if (tipoUnidadSeleccionada != null) {
                    switch (tipoUnidadSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            this.esUnidadAdministrativa = Boolean.TRUE;
                            break;
                        case CENTRO_ESCOLAR:
                            this.esUnidadAdministrativa = Boolean.FALSE;
                            break;
                        default:
                            break;
                    }
                }
            }
            if(unidadAFSeleccionada != null && tipoUnidadSeleccionada != null) { 
                 if(esUnidadAdministrativa != null && esUnidadAdministrativa) {
                    FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                    fuad.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                    fuad.setOrderBy(new String[]{"uadCodigo"});
                    fuad.setAscending(new boolean[]{true});
                    fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                    unidadesAdministrativas = unidadesAdministrativasRestClient.buscar(fuad);
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadSeleccionada)) {
                    if(!unidadAFSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                        FiltroSedes fil = new FiltroSedes();
                        fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
                        fil.setSedDepartamentoId(unidadAFSeleccionada.getUafDepartamento().getDepPk());
                        fil.setSedTipos(applicationBean.getTiposSedes());
                        fil.setPriSubvencionada(Boolean.TRUE);
                        fil.setOrderBy(new String[]{"sedCodigo"});
                        fil.setAscending(new boolean[]{true});
                        fil.setIncluirCampos(new String[]{"sedCodigo","sedTipo", "sedNombre", "sedVersion"});
                        sedes = sedeRestClient.buscar(fil);
                    }         
                }
                // seleccionarUnidadAdministrativa();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    } 
    
    public void guardarActaDescargo() {
        try {
            entidadEnEdicion = descargosRestClient.guardarActaDescargo(entidadEnEdicion.getDesPk(),actaEnEdicion);

            PrimeFaces.current().executeScript("PF('itemActaDialog').hide()");
            
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_3, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_3, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void guardar(Boolean crearSolicitud, Boolean enviarSolicitud, Boolean descargarSolicitud, Boolean rechazarSolicitud) {
        try {
            entidadEnEdicion.setDesOpcionDescargoFk(comboOpcionesDescargo != null ? comboOpcionesDescargo.getSelectedT() : null );
            entidadEnEdicion.setDesTipoDescargoFk(comboEstadosDescargo != null ? comboEstadosDescargo.getSelectedT() : null );
            if(crearSolicitud != null && crearSolicitud) {
                entidadEnEdicion.setDesEstadoFk(estadoEnSolicitud);
            } else if(enviarSolicitud != null && enviarSolicitud) {
                entidadEnEdicion.setDesEstadoFk(estadoEnProcesoDescargo);
                entidadEnEdicion.setDesFechaEnvioSolicitud(LocalDateTime.now());
            } else if(descargarSolicitud != null && descargarSolicitud) {
                entidadEnEdicion.setDesEstadoFk(estadoDescargado);
                entidadEnEdicion.setDesNombreAutoriza(sessionBean.getEntidadUsuario() != null ? sessionBean.getEntidadUsuario().getUsuNombre() : "");
            } else if(rechazarSolicitud != null && rechazarSolicitud) {
                entidadEnEdicion.setDesEstadoFk(estadoRechazado);
                listaMotivosRechazo = entidadEnEdicion.getSgAfMotivosRechazoDescargo();
                if(listaMotivosRechazo == null) {
                    listaMotivosRechazo = new ArrayList();
                }
                listaMotivosRechazo.add(motivoRechazoDescargo);
                entidadEnEdicion.setSgAfMotivosRechazoDescargo(listaMotivosRechazo);
            }
            
            
            entidadEnEdicion.setDesActivo(activosFijos);
            
            if(entidadEnEdicion.getDesPk() == null) {
                entidadEnEdicion.setDesFechaCreacion(LocalDateTime.now());
                entidadEnEdicion.setDesFechaSolicitud(LocalDate.now());
                entidadEnEdicion.setDesUsuarioCreacion(sessionBean.getUser().getName());
                if(bienesDestino == null || bienesDestino.isEmpty() || bienesDestino.size() == 0) {
                    List<BusinessError> errores = new ArrayList();
                    errores.add(new BusinessError(Mensajes.ERROR_BIENES_SELECCIONADOS_VACIO));
                    JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
                    return;
                }
                
            }

            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(tipoUnidadSeleccionada)) {
                entidadEnEdicion.setDesUnidadAdministrativaFk(unidadSeleccionada);
                entidadEnEdicion.setDesSedeFk(null);
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(tipoUnidadSeleccionada)) {
                entidadEnEdicion.setDesSedeFk(sedeSeleccionada);
                entidadEnEdicion.setDesUnidadAdministrativaFk(null);
            }

            
            List<SgAfDescargoDetalle> listaBienesDescargar = new ArrayList();
            if(bienesDestino != null && !bienesDestino.isEmpty() && bienesDestino.size() > 0) {
                for(SgAfBienDepreciable bien : bienesDestino) {
                    SgAfDescargoDetalle detalle = new SgAfDescargoDetalle();
                    detalle.setDdeBienesDepreciablesFk(bien);
                    listaBienesDescargar.add(detalle);
                }
            }
            entidadEnEdicion.setSgAfDescargosDetalle(listaBienesDescargar);
            
            solicitud = new SolicitudDescargo();
            solicitud.setDescargo(entidadEnEdicion);
            solicitud.setEstado(estadoExistente);

            entidadEnEdicion = descargosRestClient.guardar(solicitud);
            

            //bienesDestino = new ArrayList();
            validarVisualizacion();
            
            if(descargarSolicitud != null && descargarSolicitud) {
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            } else if(rechazarSolicitud != null && rechazarSolicitud) {
                PrimeFaces.current().executeScript("PF('motivoRechazoDialog').hide()");
            }
            
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
             
        } catch (BusinessException be) {
            if(descargarSolicitud != null && descargarSolicitud) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
            } else if(rechazarSolicitud != null && rechazarSolicitud) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
            }  else {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            if(descargarSolicitud != null && descargarSolicitud) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            } else if(rechazarSolicitud != null && rechazarSolicitud) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            } else {
               JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        }
    }
    
    public void actualizar(SgAfDescargo est) {
        try {
            if (est == null) {
                JSFUtils.redirectToIndex();
            } else {
                JSFUtils.limpiarMensajesError();
                entidadEnEdicion = est;
                bienesDestino = new ArrayList();
                if(entidadEnEdicion != null) {
                    actaEnEdicion = entidadEnEdicion.getDesActaDescargo();
                    activosFijos = entidadEnEdicion.getDesActivo();
                    if(activosFijos != null && activosFijos) {
                        List<SgAfOpcionesDescargo> opcionesDescargoTemp = new LinkedList();
                        for(SgAfOpcionesDescargo op: opcionesDescargo) {
                            if(!op.getOdeCodigo().equals(Constantes.CODIGO_TIPO_DESCARGO_PERMUTA)) {
                                opcionesDescargoTemp.add(op);
                            }
                        }
                        comboOpcionesDescargo = new SofisComboG(opcionesDescargoTemp, "odeNombre");
                        comboOpcionesDescargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    }
                    comboEstadosDescargo.setSelectedT(entidadEnEdicion.getDesTipoDescargoFk());
                    comboOpcionesDescargo.setSelectedT(entidadEnEdicion.getDesOpcionDescargoFk());
                    sedeSeleccionada = entidadEnEdicion.getDesSedeFk();
                    unidadSeleccionada = entidadEnEdicion.getDesUnidadAdministrativaFk();
           
                    if(unidadSeleccionada != null) {
                        esUnidadAdministrativa = Boolean.TRUE;
                        tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
                        unidadAFSeleccionada = entidadEnEdicion.getDesUnidadAdministrativaFk().getUadUnidadActivoFijoFk();
                        comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
                    } else if(sedeSeleccionada!= null) {
                        tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
                        comboTiposUnidad.setSelectedT(tipoUnidadSeleccionada);
                        if(listaUnidadesAF != null && !listaUnidadesAF.isEmpty()) {
                            for (SgAfUnidadesActivoFijo unidadesAF: listaUnidadesAF) {
                                if(unidadesAF.getUafDepartamento().getDepPk().equals(sedeSeleccionada.getSedDireccion().getDirDepartamento().getDepPk())
                                        && !unidadesAF.getUafCodigo().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                                    unidadAFSeleccionada = unidadesAF;
                                }
                            }
                        } 
                        
                    }
                    comboUnidadesActivoFijo.setSelectedT(unidadAFSeleccionada);
                    seleccionarUnidadActivoFijo();
                    
                    if(entidadEnEdicion.getSgAfDescargosDetalle() != null && !entidadEnEdicion.getSgAfDescargosDetalle().isEmpty() && entidadEnEdicion.getSgAfDescargosDetalle().size() > 0) {
                        for(SgAfDescargoDetalle desDetalle : entidadEnEdicion.getSgAfDescargosDetalle()) {
                            bienesDestino.add(desDetalle.getDdeBienesDepreciablesFk());
                        }
                    }
                    totalBienesDestino = bienesDestino.size();
                    validarVisualizacion();
                    buscar(true, true);
                }
            }  
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void crearNuevoMotivoRechazo() {
        motivoRechazoDescargo = new SgAfMotivoRechazoDescargo();
    }
    
    public void generarURL() throws Exception {
        this.urlReporte = "uaf=" + (unidadAFSeleccionada != null ? unidadAFSeleccionada.getUafPk() : "") 
                + "&uad=" + (unidadSeleccionada != null ? unidadSeleccionada.getUadPk() : "")
                + "&sed=" + (sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : "")
                + "&desId=" + (entidadEnEdicion != null ? entidadEnEdicion.getDesPk() : "")
                + "&fAcuerdo=" + (this.fechaAcuerdo != null ? this.fechaAcuerdo : "")
                + "&nAcuerdo=" + (this.numeroAcuerdo != null ? this.numeroAcuerdo.trim() : "")
                + "&sAutoriza=" + (this.seAutoriza != null ? this.seAutoriza.trim() : "");
             
    }

    public Boolean puedeAutorizarDescargo() {
        if(entidadEnEdicion != null) {
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_DESCARGO_BIENES)) {
                return Boolean.TRUE;
            } else if (sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTORIZAR_DESCARGO_BIENES_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                if(entidadEnEdicion.getDesActivo() != null && !entidadEnEdicion.getDesActivo()) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;       
    }
    public Boolean puedeRechzarDescargo() {
        if(entidadEnEdicion != null) {
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.RECHAZAR_DESCARGO_BIENES)) {
                return Boolean.TRUE;
            } else if (sessionBean.getOperaciones().contains(ConstantesOperaciones.RECHAZAR_DESCARGO_BIENES_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                if(entidadEnEdicion.getDesActivo() != null && !entidadEnEdicion.getDesActivo()) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;       
    }
    
    public Boolean puedeGenerarActaDescargo() {
        if(entidadEnEdicion != null && descargado != null && descargado) {
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_ACTA_DESCARGO)) {
                return Boolean.TRUE;
            } else if (sessionBean.getOperaciones().contains(ConstantesOperaciones.GENERAR_REPORTE_ACTA_DESCARGO_VALOR_ADQUISICION_MENOR_LIMITE_AF)) {
                if(entidadEnEdicion.getDesActivo() != null && !entidadEnEdicion.getDesActivo()) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE; 
        
    }
    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
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

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
    }

    public CatalogosRestClient getCatalogosRestClient() {
        return catalogosRestClient;
    }

    public void setCatalogosRestClient(CatalogosRestClient catalogosRestClient) {
        this.catalogosRestClient = catalogosRestClient;
    }

    public SofisComboG<TipoUnidad> getComboTiposUnidad() {
        return comboTiposUnidad;
    }

    public void setComboTiposUnidad(SofisComboG<TipoUnidad> comboTiposUnidad) {
        this.comboTiposUnidad = comboTiposUnidad;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijo() {
        return comboUnidadesActivoFijo;
    }

    public void setComboUnidadesActivoFijo(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo) {
        this.comboUnidadesActivoFijo = comboUnidadesActivoFijo;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public SofisComboG<SgAfEstadosDescargo> getComboEstadosDescargo() {
        return comboEstadosDescargo;
    }

    public void setComboEstadosDescargo(SofisComboG<SgAfEstadosDescargo> comboEstadosDescargo) {
        this.comboEstadosDescargo = comboEstadosDescargo;
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

    public TipoUnidad getTipoUnidadSeleccionada() {
        return tipoUnidadSeleccionada;
    }

    public void setTipoUnidadSeleccionada(TipoUnidad tipoUnidadSeleccionada) {
        this.tipoUnidadSeleccionada = tipoUnidadSeleccionada;
    }

    public Long getDescargoId() {
        return descargoId;
    }

    public void setDescargoId(Long descargoId) {
        this.descargoId = descargoId;
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
    
    public SgAfDescargo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfDescargo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgAfUnidadesAdministrativas getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(SgAfUnidadesAdministrativas unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SgAfUnidadesActivoFijo getUnidadAFSeleccionada() {
        return unidadAFSeleccionada;
    }

    public void setUnidadAFSeleccionada(SgAfUnidadesActivoFijo unidadAFSeleccionada) {
        this.unidadAFSeleccionada = unidadAFSeleccionada;
    }

    public List<SgAfUnidadesActivoFijo> getListaUnidadesAF() {
        return listaUnidadesAF;
    }

    public void setListaUnidadesAF(List<SgAfUnidadesActivoFijo> listaUnidadesAF) {
        this.listaUnidadesAF = listaUnidadesAF;
    }

    public List<SgMunicipio> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SgMunicipio> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public SgAfEstadosBienes getEstadoExistente() {
        return estadoExistente;
    }

    public void setEstadoExistente(SgAfEstadosBienes estadoExistente) {
        this.estadoExistente = estadoExistente;
    }

    public SgAfEstadosBienes getEstadoEnProcesoDescargo() {
        return estadoEnProcesoDescargo;
    }

    public void setEstadoEnProcesoDescargo(SgAfEstadosBienes estadoEnProcesoDescargo) {
        this.estadoEnProcesoDescargo = estadoEnProcesoDescargo;
    }

    public SgAfEstadosBienes getEstadoDescargado() {
        return estadoDescargado;
    }

    public void setEstadoDescargado(SgAfEstadosBienes estadoDescargado) {
        this.estadoDescargado = estadoDescargado;
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

    public Boolean getDescargarSolicitud() {
        return descargarSolicitud;
    }

    public void setDescargarSolicitud(Boolean descargarSolicitud) {
        this.descargarSolicitud = descargarSolicitud;
    }

    public Boolean getPermiteBusquedas() {
        return permiteBusquedas;
    }

    public void setPermiteBusquedas(Boolean permiteBusquedas) {
        this.permiteBusquedas = permiteBusquedas;
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

    public SolicitudDescargo getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudDescargo solicitud) {
        this.solicitud = solicitud;
    }

    public SgAfMotivoRechazoDescargo getMotivoRechazoDescargo() {
        return motivoRechazoDescargo;
    }

    public void setMotivoRechazoDescargo(SgAfMotivoRechazoDescargo motivoRechazoDescargo) {
        this.motivoRechazoDescargo = motivoRechazoDescargo;
    }

    public List<SgAfMotivoRechazoDescargo> getListaMotivosRechazo() {
        return listaMotivosRechazo;
    }

    public void setListaMotivosRechazo(List<SgAfMotivoRechazoDescargo> listaMotivosRechazo) {
        this.listaMotivosRechazo = listaMotivosRechazo;
    }

    public Boolean getDescargado() {
        return descargado;
    }

    public void setDescargado(Boolean descargado) {
        this.descargado = descargado;
    }

    public SofisComboG<SgAfOpcionesDescargo> getComboOpcionesDescargo() {
        return comboOpcionesDescargo;
    }

    public void setComboOpcionesDescargo(SofisComboG<SgAfOpcionesDescargo> comboOpcionesDescargo) {
        this.comboOpcionesDescargo = comboOpcionesDescargo;
    }

    public SgAfEstadosBienes getEstadoRechazado() {
        return estadoRechazado;
    }

    public void setEstadoRechazado(SgAfEstadosBienes estadoRechazado) {
        this.estadoRechazado = estadoRechazado;
    }

    public SgAfEstadosBienes getEstadoTrasladado() {
        return estadoTrasladado;
    }

    public void setEstadoTrasladado(SgAfEstadosBienes estadoTrasladado) {
        this.estadoTrasladado = estadoTrasladado;
    }

    public String getNumeroAcuerdo() {
        return numeroAcuerdo;
    }

    public void setNumeroAcuerdo(String numeroAcuerdo) {
        this.numeroAcuerdo = numeroAcuerdo;
    }

    public LocalDate getFechaAcuerdo() {
        return fechaAcuerdo;
    }

    public void setFechaAcuerdo(LocalDate fechaAcuerdo) {
        this.fechaAcuerdo = fechaAcuerdo;
    }

    public String getSeAutoriza() {
        return seAutoriza;
    }

    public void setSeAutoriza(String seAutoriza) {
        this.seAutoriza = seAutoriza;
    }

    public String getUrlReporte() {
        return urlReporte;
    }

    public void setUrlReporte(String urlReporte) {
        this.urlReporte = urlReporte;
    }

    public List<SgAfUnidadesAdministrativas> getUnidadesAdministrativas() {
        return unidadesAdministrativas;
    }

    public void setUnidadesAdministrativas(List<SgAfUnidadesAdministrativas> unidadesAdministrativas) {
        this.unidadesAdministrativas = unidadesAdministrativas;
    }

    public List<SgSede> getSedes() {
        return sedes;
    }

    public void setSedes(List<SgSede> sedes) {
        this.sedes = sedes;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyBienesDepreciablesDataModel getBienesDepreciablesLazyModel() {
        return bienesDepreciablesLazyModel;
    }

    public void setBienesDepreciablesLazyModel(LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel) {
        this.bienesDepreciablesLazyModel = bienesDepreciablesLazyModel;
    }

    public Integer getPaginadoDestino() {
        return paginadoDestino;
    }

    public void setPaginadoDestino(Integer paginadoDestino) {
        this.paginadoDestino = paginadoDestino;
    }

    public SgAfTipoBienes getTipoBienSeleccionado() {
        return tipoBienSeleccionado;
    }

    public void setTipoBienSeleccionado(SgAfTipoBienes tipoBienSeleccionado) {
        this.tipoBienSeleccionado = tipoBienSeleccionado;
    }

    public List<SgAfOpcionesDescargo> getOpcionesDescargo() {
        return opcionesDescargo;
    }

    public void setOpcionesDescargo(List<SgAfOpcionesDescargo> opcionesDescargo) {
        this.opcionesDescargo = opcionesDescargo;
    }

    public SgAfActaDescargo getActaEnEdicion() {
        return actaEnEdicion;
    }

    public void setActaEnEdicion(SgAfActaDescargo actaEnEdicion) {
        this.actaEnEdicion = actaEnEdicion;
    }
}
