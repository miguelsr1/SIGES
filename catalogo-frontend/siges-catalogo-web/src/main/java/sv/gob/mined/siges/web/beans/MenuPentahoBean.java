/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import sv.gob.mined.siges.web.dto.SgMenuPentaho;
import sv.gob.mined.siges.web.dto.SgOperacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoComponentePentaho;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMenuPentaho;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOperacion;
import sv.gob.mined.siges.web.lazymodels.LazyMenuPentahoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MenuPentahoRestClient;
import sv.gob.mined.siges.web.restclient.OperacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;


/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MenuPentahoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MenuPentahoBean.class.getName());

    @Inject
    private MenuPentahoRestClient restClient;
    
    @Inject
    private OperacionRestClient restOperacion;



    private FiltroMenuPentaho filtro = new FiltroMenuPentaho();
    private FiltroOperacion fop = new FiltroOperacion();
    private SgMenuPentaho entidadEnEdicion = new SgMenuPentaho();
    private List<SgMenuPentaho> historialPentaho= new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;

   
    private LazyMenuPentahoDataModel pentahoLazyModel;
    private SofisComboG<EnumTipoComponentePentaho> comboTipoComponente;
    private SofisComboG<EnumTipoComponentePentaho> comboTipoComponenteFiltro;
    private SofisComboG<SgOperacion> comboOperacion;
    private SofisComboG<SgOperacion> comboOperacionFiltro;


    public MenuPentahoBean() {
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
            filtro.setTipo(comboTipoComponenteFiltro.getSelectedT());
            filtro.setOperacionFk(comboOperacionFiltro.getSelectedT());

            totalResultados = restClient.buscarTotal(filtro);
            pentahoLazyModel = new LazyMenuPentahoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() throws HttpServerException, HttpClientException {
        try {
            List<EnumTipoComponentePentaho> estados = new ArrayList(Arrays.asList(EnumTipoComponentePentaho.values()));
            comboTipoComponente = new SofisComboG(estados, "text");
            comboTipoComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboTipoComponenteFiltro = new SofisComboG(estados, "text");
            comboTipoComponenteFiltro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            fop.setHabilitado(Boolean.TRUE);
            fop.setCategoria(ConstantesComponentesId.ID_CATEGORIA_OPERACION);
            fop.setOrderBy(new String[]{"opeNombre"});
            fop.setAscending(new boolean[]{true});
            List<SgOperacion> listaOperaciones = restOperacion.buscar(fop);
            comboOperacion = new SofisComboG<>(listaOperaciones, "opeNombre");
            comboOperacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboOperacionFiltro = new SofisComboG<>(listaOperaciones,"opeNombre");
            comboOperacionFiltro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboTipoComponente.setSelected(-1);
        comboOperacion.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroMenuPentaho();
        comboTipoComponenteFiltro.setSelected(-1);
        comboOperacionFiltro.setSelected(-1);
        buscar();
    }

    public List<SgMenuPentaho> completeMenuPentaho(String query) {
        try {
            FiltroMenuPentaho fil = new FiltroMenuPentaho();
            fil.setNombre(query);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"mpeNombre"});
            fil.setAscending(new boolean[]{false});
            return restClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgMenuPentaho();
    }

    public void actualizar(SgMenuPentaho var) {
        try {
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = (SgMenuPentaho) SerializationUtils.clone(var);
            comboTipoComponente.setSelectedT(entidadEnEdicion.getMpeTipo());
            comboOperacion.setSelectedT(entidadEnEdicion.getMpeOperacionFk());
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    

    public void guardar() {
        try {
            entidadEnEdicion.setMpeTipo(comboTipoComponente.getSelectedT());
            entidadEnEdicion.setMpeOperacionFk(comboOperacion.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
            restClient.eliminar(entidadEnEdicion.getMpePk());
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
            historialPentaho = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroMenuPentaho getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMenuPentaho filtro) {
        this.filtro = filtro;
    }

    public SgMenuPentaho getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMenuPentaho entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMenuPentaho> getHistorialPentaho() {
        return historialPentaho;
    }

    public void setHistorialPentaho(List<SgMenuPentaho> historialPentaho) {
        this.historialPentaho = historialPentaho;
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

    public LazyMenuPentahoDataModel getPentahoLazyModel() {
        return pentahoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMenuPentahoDataModel pentahoLazyModel) {
        this.pentahoLazyModel = pentahoLazyModel;
    }

    public SofisComboG<EnumTipoComponentePentaho> getComboTipoComponente() {
        return comboTipoComponente;
    }

    public void setComboTipoComponente(SofisComboG<EnumTipoComponentePentaho> comboTipoComponente) {
        this.comboTipoComponente = comboTipoComponente;
    }

    public MenuPentahoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(MenuPentahoRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<EnumTipoComponentePentaho> getComboTipoComponenteFiltro() {
        return comboTipoComponenteFiltro;
    }

    public void setComboTipoComponenteFiltro(SofisComboG<EnumTipoComponentePentaho> comboTipoComponenteFiltro) {
        this.comboTipoComponenteFiltro = comboTipoComponenteFiltro;
    }
    
     public SofisComboG<SgOperacion> getComboOperacion() {
        return comboOperacion;
    }

    public void setComboOperacion(SofisComboG<SgOperacion> comboOperacion) {
        this.comboOperacion = comboOperacion;
    }
    
    public SofisComboG<SgOperacion> getComboOperacionFiltro() {
        return comboOperacionFiltro;
    }

    public void setComboOperacionFiltro(SofisComboG<SgOperacion> comboOperacionFiltro) {
        this.comboOperacionFiltro = comboOperacionFiltro;
    }
    
    public FiltroOperacion getFop() {
        return fop;
    }

    public void setFop(FiltroOperacion fop) {
        this.fop = fop;
    }

}
