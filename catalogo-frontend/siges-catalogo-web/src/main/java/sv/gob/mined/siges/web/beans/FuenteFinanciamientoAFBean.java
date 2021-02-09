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
import sv.gob.mined.siges.web.dto.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.enumerados.EnumFuenteFinanciamiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.lazymodels.LazyFuenteFinanciamientoAFDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.FuenteFinanciamientoAFRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class FuenteFinanciamientoAFBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FuenteFinanciamientoAFBean.class.getName());

    @Inject
    private FuenteFinanciamientoAFRestClient restClient;

    private FiltroFuenteFinanciamientoAF filtro = new FiltroFuenteFinanciamientoAF();
    private SgAfFuenteFinanciamiento entidadEnEdicion = new SgAfFuenteFinanciamiento();
    private List<SgAfFuenteFinanciamiento> historialFuenteFinanciamiento = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyFuenteFinanciamientoAFDataModel fuenteFinanciamientoLazyModel;
    
    private List<EnumFuenteFinanciamiento> listaOpciones;
    
    public FuenteFinanciamientoAFBean() {
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
            totalResultados = restClient.buscarTotal(filtro);
            fuenteFinanciamientoLazyModel = new LazyFuenteFinanciamientoAFDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            listaOpciones = new ArrayList(Arrays.asList(EnumFuenteFinanciamiento.values()));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroFuenteFinanciamientoAF();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAfFuenteFinanciamiento();
    }

    public void actualizar(SgAfFuenteFinanciamiento var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion =(SgAfFuenteFinanciamiento) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {     
            //entidadEnEdicion.setFfiAmbito(comboAmbitoInsert.getSelectedT());
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
            restClient.eliminar(entidadEnEdicion.getFfiPk());
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
            historialFuenteFinanciamiento = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroFuenteFinanciamientoAF getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroFuenteFinanciamientoAF filtro) {
        this.filtro = filtro;
    }

    public SgAfFuenteFinanciamiento getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfFuenteFinanciamiento entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAfFuenteFinanciamiento> getHistorialFuenteFinanciamiento() {
        return historialFuenteFinanciamiento;
    }

    public void setHistorialFuenteFinanciamiento(List<SgAfFuenteFinanciamiento> historialFuenteFinanciamiento) {
        this.historialFuenteFinanciamiento = historialFuenteFinanciamiento;
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

    public LazyFuenteFinanciamientoAFDataModel getFuenteFinanciamientoLazyModel() {
        return fuenteFinanciamientoLazyModel;
    }

    public void setFuenteFinanciamientoLazyModel(LazyFuenteFinanciamientoAFDataModel fuenteFinanciamientoLazyModel) {
        this.fuenteFinanciamientoLazyModel = fuenteFinanciamientoLazyModel;
    }

    public List<EnumFuenteFinanciamiento> getListaOpciones() {
        return listaOpciones;
    }

    public void setListaOpciones(List<EnumFuenteFinanciamiento> listaOpciones) {
        this.listaOpciones = listaOpciones;
    }
    

}
