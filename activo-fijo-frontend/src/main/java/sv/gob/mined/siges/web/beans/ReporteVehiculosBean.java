/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosCalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumEstados;
import sv.gob.mined.siges.web.enumerados.EnumFuenteFinanciamiento;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyecto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyBienesDepreciablesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.OperacionesGenerales;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ReporteVehiculosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CargosBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private BienesDepreciablesRestClient bienesRestClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private ApplicationBean applicationBean;
    
    private SgAfBienDepreciable entidadEnEdicion;
    
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
    
    private LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel;
    private Long totalResultados = 0L;
    
    private SgAfEstadosBienes estadoExistente;
    
    private SgAfUnidadesAdministrativas unidadSeleccionada;
    private SgSede sedeSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFSeleccionada;
    private TipoUnidad tipoUnidadSeleccionada;
    
    private SofisComboG<SgAfEstadosCalidad> comboEstadosCalidad;
    private SofisComboG<SgAfProyectos> comboProyectos;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgAfEstadosBienes> comboEstadosBienes;
    
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo;
    private SofisComboG<TipoUnidad> comboTiposUnidad;
    private SofisComboG<SgMunicipio> comboMunicipios;
    private FiltroCodiguera filtro;
    private Integer paginado = 10;
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado;
    private Boolean esUnidadAdministrativa = Boolean.FALSE;
    private List<SgAfUnidadesActivoFijo> listaUnidadesAF;
    private List<SgAfBienDepreciable> bienesSeleccionados= new ArrayList();
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativas = new ArrayList();
    private List<SgSede> sedes = new ArrayList();
    private final Map<String, Boolean> colVisibilityMap = new HashMap();
    private final Map<Integer, String> colIndexMap = new HashMap();
    private Boolean cargarDefecto= Boolean.FALSE;
    private String urlReporte="";
    private String tipoUnidad;
    @PostConstruct
    public void init() {
        try {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
            validarAcceso();
            cargarCombos();
            cargarColumnas();
            cargarEntidad();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public Map<String, Boolean> getColVisibilityMap() {
        return Collections.unmodifiableMap(colVisibilityMap);
    }

    private String getColumnId(String fullId) {
        String[] idParts = fullId.split(":");
        return idParts[idParts.length - 1];
    }
    
    public void cargarColumnas() {
        FacesContext context = FacesContext.getCurrentInstance();
        DataTable table = (DataTable) context.getViewRoot().findComponent(":form:basicDT");
        List<UIColumn> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            final String columnId = this.getColumnId(columns.get(i).getClientId());
            colIndexMap.put(i, columnId);
            colVisibilityMap.put(columnId, true);
        }
    }
    
    public void onColumnReorder(AjaxBehaviorEvent e) {
        List<UIColumn> columns = ((DataTable) e.getSource()).getColumns();
        for (int i = 0; i < columns.size(); i++) {
            this.colIndexMap.put(i, this.getColumnId(columns.get(i).getClientId()));
        }
    }

    public void onToggle(ToggleEvent e) {
        this.colVisibilityMap.put(this.colIndexMap.get((Integer) e.getData()), e.getVisibility() == Visibility.VISIBLE);
    }
    
    public void limpiar() {
        esUnidadAdministrativa = Boolean.FALSE;
        filtroBienes = new FiltroBienesDepreciables();
        comboEstadosCalidad.setSelected(-1);
        comboProyectos.setSelected(-1);
        comboFuenteFinanciamiento.setSelected(-1);
        comboEstadosBienes.setSelected(-1);
        comboUnidadesActivoFijo.setSelected(-1);
        comboTiposUnidad.setSelected(-1);
        
        comboMunicipios = new SofisComboG();
        comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        tipoUnidadSeleccionada = null;
        bienesDepreciablesLazyModel = null;
        totalResultados = 0L;
        unidadSeleccionada = null;
        sedeSeleccionada = null;
        cargarEntidad();
    }
    
    public void generarReporte() {
        this.urlReporte = applicationBean.getReportGeneratorUrl() + ConstantesPaginas.REPORTE_VEHICULOS_R + "?";
        try {
            this.urlReporte += "uaf=" + (filtroBienes.getUnidadActivoFijoId() != null ? filtroBienes.getUnidadActivoFijoId() : "");
        
            if(filtroBienes.getTipoUnidad() != null) {
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtroBienes.getTipoUnidad())) {
                     this.urlReporte += "&uad=" + (filtroBienes.getUnidadAdministrativaId() != null ? filtroBienes.getUnidadAdministrativaId() : "") + "&sed=" ;
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtroBienes.getTipoUnidad())) {
                     this.urlReporte += "&uad=&sed=" + (filtroBienes.getUnidadAdministrativaId() != null ? filtroBienes.getUnidadAdministrativaId() : "");
                }
            } else {
                 this.urlReporte += "&uad=&sed=";
            }
            this.urlReporte += "&depId=" + (filtroBienes.getDepartamentoId() != null ? filtroBienes.getDepartamentoId() : "")
                    + "&munId=" + (filtroBienes.getMunicipioId() != null ? filtroBienes.getMunicipioId() : "")
                    + "&estadoId=" + (filtroBienes.getEstadoId() != null ? filtroBienes.getEstadoId() : "")
                    + "&fuenteId=" + (filtroBienes.getFuenteId() != null ? filtroBienes.getFuenteId() : "")
                    + "&proyectoId=" + (filtroBienes.getProyectoId() != null ? filtroBienes.getProyectoId() : "")
                    + "&codInventario=" + (filtroBienes.getCodigoInventario() != null ? filtroBienes.getCodigoInventario().trim() : "")
                    + "&fechaAdqDesde=" + (filtroBienes.getFechaAdquisicionDesde() != null ? filtroBienes.getFechaAdquisicionDesde() : "")
                    + "&fechaAdqHasta=" + (filtroBienes.getFechaAdquisicionHasta() != null ? filtroBienes.getFechaAdquisicionHasta() : "")
                    + "&fechaCreaDesde=" + (filtroBienes.getFechaCreacionDesde() != null ? filtroBienes.getFechaCreacionDesde() : "")
                    + "&fechaCreaHasta=" + (filtroBienes.getFechaCreacionHasta() != null ? filtroBienes.getFechaCreacionHasta() : "")
                    + "&motor=" + (filtroBienes.getMotor() != null ? filtroBienes.getMotor().trim() : "")
                    + "&chasis=" + (filtroBienes.getChasis() != null ? filtroBienes.getChasis().trim() : "")
                    + "&placa=" + (filtroBienes.getPlaca() != null ? filtroBienes.getPlaca().trim() : "");

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(this.urlReporte);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public void buscar() {
        try {
            filtroBienes.setIncluirCampos(new String[]{"bdeCodigoInventario","bdeEstadosBienes.ebiNombre","bdeEstadosBienes.ebiVersion","bdeUnidadesAdministrativas.uadCodigo","bdeUnidadesAdministrativas.uadNombre","bdeSede.sedNombre",
               "bdeSede.sedCodigo","bdeDescripcion","bdeMarca","bdeNoPlaca","bdeNoMotor","bdeNoChasis","bdeColorCarroceria","bdeFechaAdquisicion","bdeValorAdquisicion","bdeAsignadoA",
               "bdeEmpleadoFk.empPersonaFk.perPrimerNombre","bdeEmpleadoFk.empPersonaFk.perSegundoNombre","bdeEmpleadoFk.empPersonaFk.perTercerNombre",
               "bdeEmpleadoFk.empPersonaFk.perPrimerApellido","bdeEmpleadoFk.empPersonaFk.perSegundoApellido","bdeEmpleadoFk.empPersonaFk.perTercerApellido",
               "bdeEstadoCalidad.ecaNombre","bdeVersion"
            });
   
            filtroBienes.setSoloVehiculos(Boolean.TRUE);
            filtroBienes.setAscending(new boolean[]{true});
            filtroBienes.setOrderBy(new String[]{"bdeCodigoInventario"});
            filtroBienes.setEstadoId(comboEstadosBienes != null && comboEstadosBienes.getSelectedT() != null ? comboEstadosBienes.getSelectedT().getEbiPk(): null);
            filtroBienes.setFuenteId(comboFuenteFinanciamiento != null && comboFuenteFinanciamiento.getSelectedT() != null ? comboFuenteFinanciamiento.getSelectedT().getFfiPk(): null);
            filtroBienes.setMunicipioId(comboMunicipios != null && comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk(): null);
            filtroBienes.setProyectoId(comboProyectos != null && comboProyectos.getSelectedT() != null ? comboProyectos.getSelectedT().getProPk(): null);
            
            filtroBienes.setTipoUnidad(comboTiposUnidad.getSelectedT());
     
            filtroBienes.setEstadoId(comboEstadosBienes != null && comboEstadosBienes.getSelectedT() != null ? comboEstadosBienes.getSelectedT().getEbiPk(): null);
            
            filtroBienes.setValidarEstadoSolicitud(Boolean.TRUE); //Para que filtre tambien por el estado de la solicitud, este filtro va de la mano con el estadoId
            
            if(unidadAFSeleccionada != null) {
                filtroBienes.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                if(unidadAFSeleccionada.getUafDepartamento() != null 
                    && unidadAFSeleccionada.getUafDepartamento().getDepPk() != null) {
                    filtroBienes.setDepartamentoId(unidadAFSeleccionada.getUafDepartamento().getDepPk());
                }
            }
            
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(comboTiposUnidad.getSelectedT())) {
                filtroBienes.setUnidadAdministrativaId(unidadSeleccionada != null ? unidadSeleccionada.getUadPk() : null);
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(comboTiposUnidad.getSelectedT())) {
                filtroBienes.setUnidadAdministrativaId(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            }
            
            totalResultados = bienesRestClient.buscarTotal(filtroBienes);

            bienesDepreciablesLazyModel = new LazyBienesDepreciablesDataModel(bienesRestClient, filtroBienes, totalResultados, paginado);
        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void verPanelAvanzado() {
        if (panelAvanzado) {
            panelAvanzado = false;
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
        } else {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
            panelAvanzado = true;
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_VEHICULOS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        try {
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            
            filtro.setOrderBy(new String[]{"tunCodigo"});
            filtro.setIncluirCampos(new String[]{"tunNombre", "tunEsUnidadAdministrativa","tunVersion"});

            List<TipoUnidad> tipoUnidad = new ArrayList(Arrays.asList(TipoUnidad.values()));
            comboTiposUnidad = new SofisComboG(tipoUnidad, "text");
            comboTiposUnidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTiposUnidad.ordenar();
            
            FiltroUnidadesActivoFijo fua = new FiltroUnidadesActivoFijo();
            fua.setHabilitado(Boolean.TRUE);
            fua.setOrderBy(new String[]{"uafCodigo"});
            fua.setAscending(new boolean[]{true});
            fua.setIncluirCampos(new String[]{"uafCodigo","uafNombre", "uafDepartamento","uafVersion"});
            listaUnidadesAF = unidadesActivoFijoRestClient.buscar(fua);
            comboUnidadesActivoFijo = new SofisComboG(listaUnidadesAF, "codigoNombre");
            comboUnidadesActivoFijo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            filtro.setOrderBy(new String[]{"ecaNombre"});
            filtro.setIncluirCampos(new String[]{"ecaNombre", "ecaVersion"});
            fua.setAscending(new boolean[]{true});
            List<SgAfEstadosCalidad> estadosCalidad = catalogosRestClient.buscarEstadosCalidad(filtro);
            comboEstadosCalidad = new SofisComboG(estadosCalidad, "ecaNombre");
            comboEstadosCalidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));


            
            FiltroFuenteFinanciamientoAF fuentes = new FiltroFuenteFinanciamientoAF();
            fuentes.setHabilitado(Boolean.TRUE);
            fuentes.setOrderBy(new String[]{"ffiNombre"});
            fuentes.setIncluirCampos(new String[]{"ffiNombre","ffiAplicaPara", "ffiVersion"});
            List<SgAfFuenteFinanciamiento> listaFuentes = catalogosRestClient.buscarFuenteFinanciamiento(fuentes);
            
            List<SgAfFuenteFinanciamiento> fuentesMostrar = new ArrayList();
            EnumFuenteFinanciamiento fuentePara = null;
            
            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.FUENTE_MINED)) {
                fuentePara = EnumFuenteFinanciamiento.MINED;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.FUENTE_DEPARTAMENTAL)) {
                fuentePara = EnumFuenteFinanciamiento.DEPARTAMENTAL; 
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.FUENTE_CENTRO_EDUCATIVO)) {
                fuentePara = EnumFuenteFinanciamiento.CENTRO_EDUCATIVO;
            }
            
            if(listaFuentes != null && !listaFuentes.isEmpty()) {
                for(SgAfFuenteFinanciamiento fuente : listaFuentes) { 
                    if(OperacionesGenerales.existeEnArray(fuente.getFfiAplicaPara(), fuentePara.getText())) {
                        fuentesMostrar.add(fuente);
                    }
                } 
            }           
            
            comboFuenteFinanciamiento = new SofisComboG(fuentesMostrar, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroProyecto fpro = new FiltroProyecto();
            fpro.setHabilitado(Boolean.TRUE);
            fpro.setOrderBy(new String[]{"proNombre"});
            fpro.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            List<SgAfProyectos> proyectos = catalogosRestClient.buscarProyecto(fpro);
            comboProyectos = new SofisComboG(proyectos, "proNombre");
            comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtro.setOrderBy(new String[]{"ebiNombre"});
            filtro.setIncluirCampos(new String[]{"ebiNombre","ebiCodigo","ebiAplicaPara", "ebiVersion"});
            List<SgAfEstadosBienes> estadosBienes = catalogosRestClient.buscarEstadosBienes(filtro);
            
            List<SgAfEstadosBienes> estadosMostrar = new ArrayList();    
            
            if(estadosBienes != null && !estadosBienes.isEmpty()) {
                for(SgAfEstadosBienes estado : estadosBienes) {
                    
                    if(OperacionesGenerales.existeEnArray(estado.getEbiAplicaPara(), EnumEstados.CARGO.getText())) {
                        estadosMostrar.add(estado);
                        
                        if(Constantes.CODIGO_ESTADO_EXISTENTE.equals(estado.getEbiCodigo().trim())) {
                            estadoExistente = estado;
                        }
                    }
                    
                } 
            }
            
            comboEstadosBienes = new SofisComboG(estadosMostrar, "ebiNombre");
            comboEstadosBienes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboEstadosBienes.setSelectedT(estadoExistente);
            
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
            this.tipoUnidad = "ce";
            sedeSeleccionada = sessionBean.getSedeDefecto();
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
            this.tipoUnidad = "ua";
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
    
    public void seleccionarTipoUnidadAF() {
        esUnidadAdministrativa = null;
        tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
        this.tipoUnidad = "";
        if (tipoUnidadSeleccionada != null) {
            switch (tipoUnidadSeleccionada) {
                case UNIDAD_ADMINISTRATIVA:
                    esUnidadAdministrativa = Boolean.TRUE;
                    this.tipoUnidad = "ua";
                    break;
                case CENTRO_ESCOLAR:
                    esUnidadAdministrativa = Boolean.FALSE;
                    this.tipoUnidad = "ce";
                    break;
                default:
                    break;
            }
        }
        seleccionarUnidadActivoFijo();
    }
    
    public void seleccionarUnidadActivoFijo() {
        try {
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            sedes = new ArrayList();
            unidadesAdministrativas = new ArrayList();
            
            if(comboUnidadesActivoFijo.getSelectedT() != null) {
                unidadAFSeleccionada = comboUnidadesActivoFijo.getSelectedT();
            } else {
                unidadAFSeleccionada = null;
            }
            
            if(cargarDefecto == null || !cargarDefecto) {
                unidadSeleccionada = null;
                sedeSeleccionada = null;
            }
            this.tipoUnidad = "";
            if(tipoUnidadSeleccionada == null) {
                tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
                if (tipoUnidadSeleccionada != null) {
                    switch (tipoUnidadSeleccionada) {
                        case UNIDAD_ADMINISTRATIVA:
                            esUnidadAdministrativa = Boolean.TRUE;
                            this.tipoUnidad = "ua";
                            break;
                        case CENTRO_ESCOLAR:
                            esUnidadAdministrativa = Boolean.FALSE;
                            this.tipoUnidad = "ce";
                            break;
                        default:
                            break;
                    }
                }
            }
            
           if(unidadAFSeleccionada != null && esUnidadAdministrativa != null && tipoUnidadSeleccionada != null) {
               if(esUnidadAdministrativa) {
                    FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                    fuad.setOrderBy(new String[]{"uadCodigo"});
                    fuad.setAscending(new boolean[]{true});
                    fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                    fuad.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                    unidadesAdministrativas = unidadesAdministrativasRestClient.buscar(fuad);
                } else {
                    if(unidadAFSeleccionada.getUafPk()!= null 
                                && unidadAFSeleccionada.getUafDepartamento() != null
                                && !unidadAFSeleccionada.getUafCodigo().trim().equals(Constantes.CODIGO_UNIDAD_ACTIVO_FIJO_CENTRAL)) {
                        FiltroMunicipio fm = new FiltroMunicipio();
                        fm.setMunHabilitado(Boolean.TRUE);
                        fm.setMunDepartamentoFk(unidadAFSeleccionada.getUafDepartamento().getDepPk());
                        fm.setAscending(new boolean[]{true});
                        fm.setOrderBy(new String[]{"munNombre"});
                        fm.setIncluirCampos(new String[]{"munNombre", "munVersion"});

                        List<SgMunicipio> municipios = catalogosRestClient.buscarMunicipio(fm);
                        comboMunicipios = new SofisComboG(municipios, "munNombre");
                        comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        
                        seleccionarMunicipio();
                    }
                    
                } 
           } else {
               comboMunicipios = new SofisComboG();
               comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
           }
           this.cargarDefecto = Boolean.FALSE;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    
    public void seleccionarMunicipio() {
        try {
                FiltroSedes fil = new FiltroSedes();
                fil.setSecurityOperation(ConstantesOperaciones.BUSCAR_SEDES);
                fil.setSedTipos(applicationBean.getTiposSedes());
                fil.setPriSubvencionada(Boolean.TRUE);
                fil.setSedDepartamentoId(comboUnidadesActivoFijo != null && comboUnidadesActivoFijo.getSelectedT() != null ? comboUnidadesActivoFijo.getSelectedT().getUafDepartamento().getDepPk(): null);
                fil.setSedMunicipioId(comboMunicipios != null && comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
                fil.setOrderBy(new String[]{"sedCodigo"});
                fil.setAscending(new boolean[]{true});
                fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedVersion"});
                sedes = sedeRestClient.buscar(fil);         
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
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

    public SgAfBienDepreciable getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfBienDepreciable entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }

    public LazyBienesDepreciablesDataModel getBienesDepreciablesLazyModel() {
        return bienesDepreciablesLazyModel;
    }

    public void setBienesDepreciablesLazyModel(LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel) {
        this.bienesDepreciablesLazyModel = bienesDepreciablesLazyModel;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public SgAfEstadosBienes getEstadoExistente() {
        return estadoExistente;
    }

    public void setEstadoExistente(SgAfEstadosBienes estadoExistente) {
        this.estadoExistente = estadoExistente;
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

    public TipoUnidad getTipoUnidadSeleccionada() {
        return tipoUnidadSeleccionada;
    }

    public void setTipoUnidadSeleccionada(TipoUnidad tipoUnidadSeleccionada) {
        this.tipoUnidadSeleccionada = tipoUnidadSeleccionada;
    }

    public SofisComboG<SgAfEstadosCalidad> getComboEstadosCalidad() {
        return comboEstadosCalidad;
    }

    public void setComboEstadosCalidad(SofisComboG<SgAfEstadosCalidad> comboEstadosCalidad) {
        this.comboEstadosCalidad = comboEstadosCalidad;
    }

    public SofisComboG<SgAfProyectos> getComboProyectos() {
        return comboProyectos;
    }

    public void setComboProyectos(SofisComboG<SgAfProyectos> comboProyectos) {
        this.comboProyectos = comboProyectos;
    }

    public SofisComboG<SgAfFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SofisComboG<SgAfEstadosBienes> getComboEstadosBienes() {
        return comboEstadosBienes;
    }

    public void setComboEstadosBienes(SofisComboG<SgAfEstadosBienes> comboEstadosBienes) {
        this.comboEstadosBienes = comboEstadosBienes;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesActivoFijo() {
        return comboUnidadesActivoFijo;
    }

    public void setComboUnidadesActivoFijo(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo) {
        this.comboUnidadesActivoFijo = comboUnidadesActivoFijo;
    }

    public SofisComboG<TipoUnidad> getComboTiposUnidad() {
        return comboTiposUnidad;
    }

    public void setComboTiposUnidad(SofisComboG<TipoUnidad> comboTiposUnidad) {
        this.comboTiposUnidad = comboTiposUnidad;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Boolean getPanelAvanzado() {
        return panelAvanzado;
    }

    public void setPanelAvanzado(Boolean panelAvanzado) {
        this.panelAvanzado = panelAvanzado;
    }

    public String getTxtFiltroAvanzado() {
        return txtFiltroAvanzado;
    }

    public void setTxtFiltroAvanzado(String txtFiltroAvanzado) {
        this.txtFiltroAvanzado = txtFiltroAvanzado;
    }

    public Boolean getEsUnidadAdministrativa() {
        return esUnidadAdministrativa;
    }

    public void setEsUnidadAdministrativa(Boolean esUnidadAdministrativa) {
        this.esUnidadAdministrativa = esUnidadAdministrativa;
    }

    public List<SgAfBienDepreciable> getBienesSeleccionados() {
        return bienesSeleccionados;
    }

    public void setBienesSeleccionados(List<SgAfBienDepreciable> bienesSeleccionados) {
        this.bienesSeleccionados = bienesSeleccionados;
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

    public List<SgAfUnidadesActivoFijo> getListaUnidadesAF() {
        return listaUnidadesAF;
    }

    public void setListaUnidadesAF(List<SgAfUnidadesActivoFijo> listaUnidadesAF) {
        this.listaUnidadesAF = listaUnidadesAF;
    }

    public String getUrlReporte() {
        return urlReporte;
    }

    public void setUrlReporte(String urlReporte) {
        this.urlReporte = urlReporte;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
}
