/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.dto.catalogo.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosCalidad;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFormaAdquisicion;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.dto.catalogo.SgAfTipoBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumEstados;
import sv.gob.mined.siges.web.enumerados.EnumFuenteFinanciamiento;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessError;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEmpleados;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyecto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyBienesDepreciablesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TipoBienesRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.OperacionesGenerales;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class CargosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CargosBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private ApplicationBean apliApplicationBean;
    
    @Inject
    private TipoBienesRestClient tipoBienesRestClient;

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
    private SgAfEmpleados empleadoSeleccionado;
    private SgAfUnidadesAdministrativas unidadSeleccionada;
    private SgSede sedeSeleccionada;
    private SgAfUnidadesActivoFijo unidadAFSeleccionada;
    private TipoUnidad tipoUnidadSeleccionada;
    
    private SofisComboG<SgAfEstadosCalidad> comboEstadosCalidad;
    private SofisComboG<SgAfCategoriaBienes> comboCategoriaBienes;
    private SofisComboG<SgAfFormaAdquisicion> comboFormaAdquisicion;
    private SofisComboG<SgAfProyectos> comboProyectos;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgAfEstadosBienes> comboEstadosBienes;
    private SofisComboG<SgSede> comboSedes;

    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesActivoFijo;
    private SofisComboG<TipoUnidad> comboTiposUnidad;
    private SofisComboG<SgMunicipio> comboMunicipios;
    
    private SgAfTipoBienes tipoBienSeleccionado;
    private FiltroCodiguera filtro;
    private Integer paginado = 10;
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado;
    private Boolean esUnidadAdministrativa = Boolean.FALSE;
    private List<RevHistorico> historial = new ArrayList();
    private List<SgAfEmpleados> empleados = new ArrayList();
    private List<SgAfBienDepreciable> bienesSeleccionados= new ArrayList();
    private List<SgAfUnidadesAdministrativas> unidadesAdministrativas = new ArrayList();
    private List<SgSede> sedes = new ArrayList();
    private List<SgAfUnidadesActivoFijo> listaUnidadesAF = new ArrayList();
    private Boolean cargarDefecto= Boolean.FALSE;
    private final Map<String, Boolean> colVisibilityMap = new HashMap();
    private final Map<Integer, String> colIndexMap = new HashMap();
    private LocalDate fechaReporte;
    private String urlReporte="";
    private Boolean cargado = Boolean.FALSE;
    private String tipoUnidad="";
    @PostConstruct
    public void init() {
        try {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
            validarAcceso();
            cargarCombos();
            cargarEntidad();
            cargarColumnas();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
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
            this.cargarDefecto= Boolean.TRUE;
            this.esUnidadAdministrativa = Boolean.FALSE;
            
            sedeSeleccionada = sessionBean.getSedeDefecto();
            
            tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            
            List<TipoUnidad> tipoUnidad = new ArrayList();
            tipoUnidad.add(tipoUnidadSeleccionada);
            
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
            this.cargarDefecto= Boolean.TRUE;
            this.esUnidadAdministrativa = Boolean.TRUE;
            
            unidadSeleccionada = sessionBean.getUnidadADDefecto();

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
        if(table != null) {
            List<UIColumn> columns = table.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                final String columnId = this.getColumnId(columns.get(i).getClientId());
                colIndexMap.put(i, columnId);
                colVisibilityMap.put(columnId, true);
            }
        } 
    }
    
    public void onColumnReorder(AjaxBehaviorEvent e) {
        if(e.getSource() != null) {
            List<UIColumn> columns = ((DataTable) e.getSource()).getColumns();
            for (int i = 0; i < columns.size(); i++) {
                this.colIndexMap.put(i, this.getColumnId(columns.get(i).getClientId()));
            }
        }
        
    }

    public void onToggle(ToggleEvent e) {
        if(e != null) {
             this.colVisibilityMap.put(this.colIndexMap.get((Integer) e.getData()), e.getVisibility() == Visibility.VISIBLE);
        }
    }
    
    public void limpiar() {
        this.esUnidadAdministrativa = Boolean.FALSE;
        filtroBienes = new FiltroBienesDepreciables();
        comboEstadosCalidad.setSelected(-1);
        comboCategoriaBienes.setSelected(-1);
        comboFormaAdquisicion.setSelected(-1);
        comboProyectos.setSelected(-1);
        comboFuenteFinanciamiento.setSelected(-1);
        comboEstadosBienes.setSelected(-1);
        comboUnidadesActivoFijo.setSelected(-1);
        comboTiposUnidad.setSelected(-1);
        
        comboMunicipios = new SofisComboG();
        comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        tipoBienSeleccionado = null;
        tipoUnidadSeleccionada = null;
        
        sedes = new ArrayList();
        unidadesAdministrativas = new ArrayList();
        bienesDepreciablesLazyModel = null;
        totalResultados = null;
        cargarEntidad();
    }
    
    public void buscar() {
        try {
            
            filtroBienes.setUnidadAdministrativaId(null);
            filtroBienes.setUnidadActivoFijoId(null);
            filtroBienes.setDepartamentoId(null);
            filtroBienes.setObtenerDepreciacion(Boolean.TRUE);
            filtroBienes.setIncluirCampos(new String[]{"bdeCodigoInventario","bdeEstadosBienes.ebiNombre","bdeEstadosBienes.ebiVersion","bdeEstadosSolicitud.ebiNombre","bdeEstadosSolicitud.ebiVersion","bdeTipoBien.tbiCategoriaBienes.cabNombre","bdeUnidadesAdministrativas.uadCodigo",
                "bdeUnidadesAdministrativas.uadNombre","bdeSede.sedNombre","bdeSede.sedCodigo","bdeDescripcion","bdeMarca","bdeModelo","bdeNoSerie","bdeFechaAdquisicion","bdeValorAdquisicion","bdeAsignadoA",
                "bdeCategoriaFk.cabPk","bdeCategoriaFk.cabNombre","bdeCategoriaFk.cabVersion","bdeTipoBienCategoriaVinculada",
                "bdeEmpleadoFk.empPersonaFk.perPrimerNombre","bdeEmpleadoFk.empPersonaFk.perSegundoNombre","bdeEmpleadoFk.empPersonaFk.perTercerNombre",
                "bdeEmpleadoFk.empPersonaFk.perPrimerApellido","bdeEmpleadoFk.empPersonaFk.perSegundoApellido","bdeEmpleadoFk.empPersonaFk.perTercerApellido","bdeDepreciado",
                "bdeFuenteFinanciamiento.ffiNombre","bdeProyectos.proNombre","bdeVersion","bdeUltModUsuario","bdeProveedor","bdeFechaCreacion","bdeEstadoCalidad.ecaNombre"
            });
   
            filtroBienes.setFormaAdquisicionId(comboFormaAdquisicion != null && comboFormaAdquisicion.getSelectedT() != null ? comboFormaAdquisicion.getSelectedT().getFadPk() : null);
            filtroBienes.setCalidadId(comboEstadosCalidad != null && comboEstadosCalidad.getSelectedT() != null ? comboEstadosCalidad.getSelectedT().getEcaPk(): null);
            filtroBienes.setCategoriaId(comboCategoriaBienes != null && comboCategoriaBienes.getSelectedT() != null ? comboCategoriaBienes.getSelectedT().getCabPk(): null);
            filtroBienes.setEstadoId(comboEstadosBienes != null && comboEstadosBienes.getSelectedT() != null ? comboEstadosBienes.getSelectedT().getEbiPk(): null);
            filtroBienes.setFuenteId(comboFuenteFinanciamiento != null && comboFuenteFinanciamiento.getSelectedT() != null ? comboFuenteFinanciamiento.getSelectedT().getFfiPk(): null);
            filtroBienes.setMunicipioId(comboMunicipios != null && comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk(): null);
            filtroBienes.setProyectoId(comboProyectos != null && comboProyectos.getSelectedT() != null ? comboProyectos.getSelectedT().getProPk(): null);
            filtroBienes.setTipoBienId(tipoBienSeleccionado != null ? tipoBienSeleccionado.getTbiPk() : null);
          
            filtroBienes.setTipoUnidad(comboTiposUnidad != null ? comboTiposUnidad.getSelectedT() : null);
            filtroBienes.setEmpleadoId(empleadoSeleccionado != null ? empleadoSeleccionado.getEmpPk() : null);
            filtroBienes.setEstadoId(comboEstadosBienes != null && comboEstadosBienes.getSelectedT() != null ? comboEstadosBienes.getSelectedT().getEbiPk(): null);
            
            filtroBienes.setValidarEstadoSolicitud(Boolean.TRUE); //Para que filtre tambien por el estado de la solicitud, este filtro va de la mano con el estadoId
            
            if(unidadAFSeleccionada != null) {
                filtroBienes.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                if(unidadAFSeleccionada.getUafDepartamento() != null 
                    && unidadAFSeleccionada.getUafDepartamento().getDepPk() != null) {
                    filtroBienes.setDepartamentoId(unidadAFSeleccionada.getUafDepartamento().getDepPk());
                }
            }
            if(comboTiposUnidad != null) {
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(comboTiposUnidad.getSelectedT())) {
                    filtroBienes.setUnidadAdministrativaId(unidadSeleccionada != null ? unidadSeleccionada.getUadPk() : null);
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(comboTiposUnidad.getSelectedT())) {
                    filtroBienes.setUnidadAdministrativaId(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
                }
            }
            
            totalResultados = bienesRestClient.buscarTotal(filtroBienes);

            bienesDepreciablesLazyModel = new LazyBienesDepreciablesDataModel(bienesRestClient, filtroBienes, totalResultados, paginado);

        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void verPanelAvanzado() {
        try {
           empleados = new ArrayList();
           if (panelAvanzado) {
                panelAvanzado = false;
                txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM"); 
            } else {
                txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
                panelAvanzado = true;
                
                if(cargado != null && !cargado) {
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
                    
                    cargado = Boolean.TRUE;
                }
                if(unidadSeleccionada != null) {
                    seleccionarUnidadAdministrativa();
                }
            } 
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CARGO_BIENES)) {
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

            List<TipoUnidad> tipoUnidadLista = new ArrayList();
            if(sessionBean.getUnidadADDefecto() != null) {
                tipoUnidadLista.add(TipoUnidad.UNIDAD_ADMINISTRATIVA);
                comboTiposUnidad = new SofisComboG(tipoUnidadLista, "text");
                tipoUnidad ="ua";
                comboTiposUnidad.setSelectedT(TipoUnidad.UNIDAD_ADMINISTRATIVA);           
                tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
            } else if(sessionBean.getSedeDefecto() != null) {
                tipoUnidadLista.add(TipoUnidad.CENTRO_ESCOLAR);
                comboTiposUnidad = new SofisComboG(tipoUnidadLista, "text");
                tipoUnidad ="ce";
                comboTiposUnidad.setSelectedT(TipoUnidad.CENTRO_ESCOLAR);           
                tipoUnidadSeleccionada = TipoUnidad.CENTRO_ESCOLAR;
            } else {
                tipoUnidadLista = new ArrayList(Arrays.asList(TipoUnidad.values()));
                comboTiposUnidad = new SofisComboG(tipoUnidadLista, "text");
                comboTiposUnidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                /**
                comboTiposUnidad.setSelectedT(TipoUnidad.UNIDAD_ADMINISTRATIVA);           
                tipoUnidadSeleccionada = TipoUnidad.UNIDAD_ADMINISTRATIVA;
                tipoUnidad ="ua";**/
            }
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

            filtro.setOrderBy(new String[]{"fadNombre"});
            filtro.setIncluirCampos(new String[]{"fadNombre", "fadVersion"});
            List<SgAfFormaAdquisicion> formaAdquisicion = catalogosRestClient.buscarFormaAdquisicion(filtro);
            comboFormaAdquisicion = new SofisComboG(formaAdquisicion, "fadNombre");
            comboFormaAdquisicion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));       

            filtro.setOrderBy(new String[]{"ebiNombre"});
            filtro.setIncluirCampos(new String[]{"ebiNombre","ebiCodigo","ebiAplicaPara","ebiEstilo" ,"ebiVersion"});
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
            
            
            FiltroCategoriaBienes fcategoria = new FiltroCategoriaBienes();
            fcategoria.setHabilitado(Boolean.TRUE);
            fcategoria.setAscending(new boolean[]{true});
            fcategoria.setOrderBy(new String[]{"cabNombre"});
            fcategoria.setIncluirCampos(new String[]{"cabNombre", "cabVersion"});
            List<SgAfCategoriaBienes> categorias = catalogosRestClient.buscarCategoriaBienes(fcategoria);
            comboCategoriaBienes = new SofisComboG(categorias, "cabNombre");
            comboCategoriaBienes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboFuenteFinanciamiento = new SofisComboG();
            comboProyectos = new SofisComboG();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgAfTipoBienes> completeTipoBien(String query) {
        try {
            FiltroTipoBienes filtro = new FiltroTipoBienes();
            filtro.setHabilitado(Boolean.TRUE); 
            filtro.setCodigoONombre(query);
            filtro.setCategoriaId(comboCategoriaBienes.getSelectedT() != null ? comboCategoriaBienes.getSelectedT().getCabPk() : null);
            filtro.setMaxResults(11L);
            filtro.setOrderBy(new String[]{"tbiNombre"});
            filtro.setAscending(new boolean[]{false});
            filtro.setIncluirCampos(new String[]{"tbiCodigo", "tbiNombre", "tbiVersion"});
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
    
    public void verificarCategoriaTipoBien() {
        if (this.tipoBienSeleccionado != null && this.comboCategoriaBienes != null 
                && this.comboCategoriaBienes.getSelectedT() != null 
                && this.comboCategoriaBienes.getSelectedT().getCabPk() != this.tipoBienSeleccionado.getTbiCategoriaBienes().getCabPk()) {
            this.tipoBienSeleccionado = null;
        }
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
            
           if(tipoUnidadSeleccionada != null && unidadAFSeleccionada != null && esUnidadAdministrativa != null) {
               if(esUnidadAdministrativa) {
                    FiltroUnidadesAdministrativas fuad = new FiltroUnidadesAdministrativas();
                    fuad.setOrderBy(new String[]{"uadCodigo"});
                    fuad.setAscending(new boolean[]{true});
                    fuad.setIncluirCampos(new String[]{"uadCodigo", "uadNombre", "uadVersion"});
                    fuad.setUnidadActivoFijoId(unidadAFSeleccionada.getUafPk());
                    unidadesAdministrativas = unidadesAdministrativasRestClient.buscar(fuad);
                } else {
                    if( unidadAFSeleccionada.getUafPk()!= null 
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
    
    public void seleccionarUnidadAdministrativa(){
        try {
            empleados = new ArrayList();
            if(tipoUnidadSeleccionada != null && esUnidadAdministrativa != null && esUnidadAdministrativa && unidadSeleccionada != null && panelAvanzado != null && panelAvanzado) {
                FiltroEmpleados filtroEmp = new FiltroEmpleados();
                filtroEmp.setOrderBy(new String[]{"empPersonaFk.perPrimerNombre"});
                filtroEmp.setIncluirCampos(new String[]{"empPk","empVersion","empPersonaFk.perPk","empPersonaFk.perVersion",
                                                "empPersonaFk.perPrimerNombre","empPersonaFk.perSegundoNombre","empPersonaFk.perTercerNombre",
                                                "empPersonaFk.perPrimerApellido","empPersonaFk.perSegundoApellido","empPersonaFk.perTercerApellido","empPersonaFk.perDui"});

                
                filtroEmp.setUnidadAdministrativaId(unidadSeleccionada.getUadPk());
                empleados = catalogosRestClient.buscarEmpleados(filtroEmp);
            } else if(sedeSeleccionada != null) {
                filtroBienes.setTipoUnidad(TipoUnidad.CENTRO_ESCOLAR);
                filtroBienes.setUnidadAdministrativaId(sedeSeleccionada.getSedPk());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } 
    }
    
    
    public void actualizar(SgAfBienDepreciable var) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = (SgAfBienDepreciable) SerializationUtils.clone(var);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eliminar() {
        try {
            bienesRestClient.eliminar(entidadEnEdicion.getBdePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void eliminarLista() {
        try {
            if(bienesSeleccionados != null && !bienesSeleccionados.isEmpty() && bienesSeleccionados.size() > 0) {
                bienesRestClient.eliminarLista(bienesSeleccionados);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.LISTA_ELIMINADO_CORRECTO), "");
                buscar();
                entidadEnEdicion = null;
                bienesSeleccionados = new ArrayList();
            } else {
                List<BusinessError> errores = new ArrayList();
                errores.add(new BusinessError(Mensajes.ERROR_BIENES_SELECCIONADOS_VACIO));
               JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, errores); 
            }
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarTipoUnidadAF() {
        empleadoSeleccionado = null;
        esUnidadAdministrativa = null;
        LOGGER.info("tipoUnidadSeleccionada: " + comboTiposUnidad.getSelectedT());
        tipoUnidadSeleccionada = comboTiposUnidad.getSelectedT();
        this.esUnidadAdministrativa = Boolean.FALSE;
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
        //LOGGER.info("tipoUnidadSeleccionada DESPÚES: " + tipoUnidadSeleccionada);
        seleccionarUnidadActivoFijo();
    }
    
    public String cargarHistorial(Long id) {
        try {
            historial = bienesRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public boolean bienPuedeEliminarse(SgAfBienDepreciable bien) {
        if(bien.getBdeDepreciado() != null && bien.getBdeDepreciado()) {
            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_BIEN_DEPRECIABLE_CON_DEPRECIACION)) {
                if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_BIEN_DEPRECIABLE)) {
                    return true;
                } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_BIEN_VALOR_ADQUISICION_MENOR_LIMITE_AF)) { 
                    if(bien != null && bien.getBdeValorAdquisicion().compareTo(apliApplicationBean.getValorActivoFijoLimite()) < 0) {
                        return true;
                    }
                }
            }
        } else {
            if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_BIEN_DEPRECIABLE)) {
                return true;
            } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_BIEN_VALOR_ADQUISICION_MENOR_LIMITE_AF)) { 
                if(bien != null && bien.getBdeValorAdquisicion().compareTo(apliApplicationBean.getValorActivoFijoLimite()) < 0) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public void generarNuevo() {
        this.tipoUnidad="";
        if(filtroBienes.getTipoUnidad() != null) {
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtroBienes.getTipoUnidad())) {
                  this.tipoUnidad="ua";
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtroBienes.getTipoUnidad())) {
                  this.tipoUnidad="ce";
            }
        } 
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

    public SofisComboG<SgAfEstadosCalidad> getComboEstadosCalidad() {
        return comboEstadosCalidad;
    }

    public void setComboEstadosCalidad(SofisComboG<SgAfEstadosCalidad> comboEstadosCalidad) {
        this.comboEstadosCalidad = comboEstadosCalidad;
    }
    
    public SofisComboG<SgAfFormaAdquisicion> getComboFormaAdquisicion() {
        return comboFormaAdquisicion;
    }

    public void setComboFormaAdquisicion(SofisComboG<SgAfFormaAdquisicion> comboFormaAdquisicion) {
        this.comboFormaAdquisicion = comboFormaAdquisicion;
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

    public SgAfTipoBienes getTipoBienSeleccionado() {
        return tipoBienSeleccionado;
    }

    public void setTipoBienSeleccionado(SgAfTipoBienes tipoBienSeleccionado) {
        this.tipoBienSeleccionado = tipoBienSeleccionado;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<SgAfEstadosBienes> getComboEstadosBienes() {
        return comboEstadosBienes;
    }

    public void setComboEstadosBienes(SofisComboG<SgAfEstadosBienes> comboEstadosBienes) {
        this.comboEstadosBienes = comboEstadosBienes;
    }

    public SofisComboG<SgAfCategoriaBienes> getComboCategoriaBienes() {
        return comboCategoriaBienes;
    }

    public void setComboCategoriaBienes(SofisComboG<SgAfCategoriaBienes> comboCategoriaBienes) {
        this.comboCategoriaBienes = comboCategoriaBienes;
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

    public SofisComboG<SgSede> getComboSedes() {
        return comboSedes;
    }

    public void setComboSedes(SofisComboG<SgSede> comboSedes) {
        this.comboSedes = comboSedes;
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

    public SofisComboG<TipoUnidad> getComboTiposUnidad() {
        return comboTiposUnidad;
    }

    public void setComboTiposUnidad(SofisComboG<TipoUnidad> comboTiposUnidad) {
        this.comboTiposUnidad = comboTiposUnidad;
    }

    public SgAfBienDepreciable getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfBienDepreciable entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
    }

    public void setHistorial(List<RevHistorico> historial) {
        this.historial = historial;
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

    public SgAfEstadosBienes getEstadoExistente() {
        return estadoExistente;
    }

    public void setEstadoExistente(SgAfEstadosBienes estadoExistente) {
        this.estadoExistente = estadoExistente;
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

    public LocalDate getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDate fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getUrlReporte() {
        return urlReporte;
    }

    public void setUrlReporte(String urlReporte) {
        this.urlReporte = urlReporte;
    }

    public List<SgAfEmpleados> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<SgAfEmpleados> empleados) {
        this.empleados = empleados;
    }

    public SgAfEmpleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(SgAfEmpleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public Boolean getCargarDefecto() {
        return cargarDefecto;
    }

    public void setCargarDefecto(Boolean cargarDefecto) {
        this.cargarDefecto = cargarDefecto;
    }

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
}