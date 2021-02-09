/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.SgAfClasificacionBienes;
import sv.gob.mined.siges.web.dto.SgAfSeccionCategoria;
import sv.gob.mined.siges.web.enumerados.EnumSeccionesCargoBienes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyCategoriaBienesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CategoriaBienesRestClient;
import sv.gob.mined.siges.web.restclient.ClasificacionBienesRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.OperacionesGenerales;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CategoriaBienesBean implements Serializable {
     private static final Logger LOGGER = Logger.getLogger(CategoriaBienesBean.class.getName());

    @Inject
    private CategoriaBienesRestClient restClient;
    
    @Inject
    private ClasificacionBienesRestClient clasificacionBienesRestClient;
    
    @Inject
    private ApplicationBean applicationBean;
            
    private FiltroCategoriaBienes filtro = new FiltroCategoriaBienes();
    private SgAfCategoriaBienes entidadEnEdicion = new SgAfCategoriaBienes();
    private List<SgAfCategoriaBienes> historialCategoriasBienes = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCategoriaBienesDataModel categoriasBienesLazyModel;
    
    private SofisComboG<SgAfClasificacionBienes> comboClasificacion;
    private SofisComboG<SgAfClasificacionBienes> comboClasificacionInsert;
    
    private List<EnumSeccionesCargoBienes> listadoSecciones = new ArrayList();
    private Boolean mostrarRuta = Boolean.FALSE;
    
    private List<SgAfSeccionCategoria> seccionesSeleccionadas = new ArrayList();
    private EnumSeccionesCargoBienes[] listadoSeccionesSellecionadas;
    public CategoriaBienesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setInicializarLista(Boolean.TRUE);
            filtro.setClasificacionId(comboClasificacion.getSelectedT() != null ? comboClasificacion.getSelectedT().getCbiPk() : null);
            totalResultados = restClient.buscarTotal(filtro);
            categoriasBienesLazyModel = new LazyCategoriaBienesDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            List<SgAfClasificacionBienes> lista = clasificacionBienesRestClient.buscar(fc);

            comboClasificacion = new SofisComboG(lista, "cbiNombre");
            comboClasificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboClasificacionInsert = new SofisComboG(lista, "cbiNombre");
            comboClasificacionInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            listadoSecciones = new ArrayList(Arrays.asList(EnumSeccionesCargoBienes.values()));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiar() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        filtro = new FiltroCategoriaBienes();
        cargarCombos();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgAfCategoriaBienes();
        entidadEnEdicion.setCabValorMaximo(new BigDecimal(applicationBean.getValorCategoriaMontobase()));
        comboClasificacionInsert.setSelected(-1);
    }

    public void actualizar(SgAfCategoriaBienes var) {
        JSFUtils.limpiarMensajesError();
        comboClasificacionInsert.setSelected(-1);
        entidadEnEdicion = (SgAfCategoriaBienes) SerializationUtils.clone(var);
        comboClasificacionInsert.setSelectedT(entidadEnEdicion.getCabClasificacionBienes());

        listadoSeccionesSellecionadas = new EnumSeccionesCargoBienes[0];
        
        if(entidadEnEdicion.getSgAfSeccionesCategoriaList() != null && !entidadEnEdicion.getSgAfSeccionesCategoriaList().isEmpty() && entidadEnEdicion.getSgAfSeccionesCategoriaList().size() > 0) {
            listadoSeccionesSellecionadas = new EnumSeccionesCargoBienes[entidadEnEdicion.getSgAfSeccionesCategoriaList().size()];
            int count = 0;
            for(SgAfSeccionCategoria categoria : entidadEnEdicion.getSgAfSeccionesCategoriaList()) {
                listadoSeccionesSellecionadas[count] = categoria.getScaSeccion();
                count ++;
            }
        }
        seleccionarSeccion();
    }

    public void guardar() {
        try {
            
            List<SgAfSeccionCategoria> secciones = new ArrayList();
            entidadEnEdicion.setCabClasificacionBienes(comboClasificacionInsert.getSelectedT() != null ? comboClasificacionInsert.getSelectedT() : null);
            if(listadoSeccionesSellecionadas.length > 0) {
                for(EnumSeccionesCargoBienes rop: listadoSeccionesSellecionadas){
                    SgAfSeccionCategoria s = new SgAfSeccionCategoria();
                    s.setScaSeccion(rop);
                    secciones.add(s);
                } 
            }
            entidadEnEdicion.setSgAfSeccionesCategoriaList(secciones);
            restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCabPk());
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

    public void historial(Long id) {
        try {
            historialCategoriasBienes = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    
    public void seleccionarSeccion() {
        if(OperacionesGenerales.existeEnArray(listadoSeccionesSellecionadas, EnumSeccionesCargoBienes.SECCION_ID_EXTERNO)) {
            this.mostrarRuta = Boolean.TRUE;
        } else {
            this.mostrarRuta = Boolean.FALSE;
            this.entidadEnEdicion.setCabPathCargar("");
        }
    }
    
    public FiltroCategoriaBienes getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCategoriaBienes filtro) {
        this.filtro = filtro;
    }

    public SgAfCategoriaBienes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfCategoriaBienes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAfCategoriaBienes> getHistorialCategoriasBienes() {
        return historialCategoriasBienes;
    }

    public void setHistorialCategoriasBienes(List<SgAfCategoriaBienes> historialCategoriasBienes) {
        this.historialCategoriasBienes = historialCategoriasBienes;
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

    public LazyCategoriaBienesDataModel getCategoriasBienesLazyModel() {
        return categoriasBienesLazyModel;
    }

    public void setCategoriasBienesLazyModel(LazyCategoriaBienesDataModel categoriasBienesLazyModel) {
        this.categoriasBienesLazyModel = categoriasBienesLazyModel;
    }

    public SofisComboG<SgAfClasificacionBienes> getComboClasificacion() {
        return comboClasificacion;
    }

    public void setComboClasificacion(SofisComboG<SgAfClasificacionBienes> comboClasificacion) {
        this.comboClasificacion = comboClasificacion;
    }

    public SofisComboG<SgAfClasificacionBienes> getComboClasificacionInsert() {
        return comboClasificacionInsert;
    }

    public void setComboClasificacionInsert(SofisComboG<SgAfClasificacionBienes> comboClasificacionInsert) {
        this.comboClasificacionInsert = comboClasificacionInsert;
    }

    public List<EnumSeccionesCargoBienes> getListadoSecciones() {
        return listadoSecciones;
    }

    public void setListadoSecciones(List<EnumSeccionesCargoBienes> listadoSecciones) {
        this.listadoSecciones = listadoSecciones;
    }

    public List<SgAfSeccionCategoria> getSeccionesSeleccionadas() {
        return seccionesSeleccionadas;
    }

    public void setSeccionesSeleccionadas(List<SgAfSeccionCategoria> seccionesSeleccionadas) {
        this.seccionesSeleccionadas = seccionesSeleccionadas;
    }

    public EnumSeccionesCargoBienes[] getListadoSeccionesSellecionadas() {
        return listadoSeccionesSellecionadas;
    }

    public void setListadoSeccionesSellecionadas(EnumSeccionesCargoBienes[] listadoSeccionesSellecionadas) {
        this.listadoSeccionesSellecionadas = listadoSeccionesSellecionadas;
    }
    public Boolean getMostrarRuta() {
        return mostrarRuta;
    }

    public void setMostrarRuta(Boolean mostrarRuta) {
        this.mostrarRuta = mostrarRuta;

    }
    
    
}
