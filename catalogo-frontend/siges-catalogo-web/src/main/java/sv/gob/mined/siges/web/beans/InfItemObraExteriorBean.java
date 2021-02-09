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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgInfItemObraExterior;
import sv.gob.mined.siges.web.dto.SgInfObraExterior;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyInfItemObraExteriorDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.InfItemObraExteriorRestClient;
import sv.gob.mined.siges.web.restclient.InfObraExteriorRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InfItemObraExteriorBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InfItemObraExteriorBean.class.getName());

    @Inject
    private InfItemObraExteriorRestClient restClient;
    
    @Inject
    private InfObraExteriorRestClient obraExteriorRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgInfItemObraExterior entidadEnEdicion = new SgInfItemObraExterior();
    private List<SgInfItemObraExterior> historialInfItemObraExterior = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyInfItemObraExteriorDataModel infItemObraExteriorLazyModel;
    private SofisComboG<SgInfObraExterior> comboObraExterior;

    public InfItemObraExteriorBean() {
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
            infItemObraExteriorLazyModel = new LazyInfItemObraExteriorDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"oexNombre"});
            fc.setAscending(new boolean[]{true});
            List<SgInfObraExterior> listObra=obraExteriorRestClient.buscar(fc);
            
            comboObraExterior = new SofisComboG(listObra, "oexNombre");
            comboObraExterior.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboObraExterior.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgInfItemObraExterior();
    }

    public void actualizar(SgInfItemObraExterior var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgInfItemObraExterior) SerializationUtils.clone(var);
        comboObraExterior.setSelectedT(entidadEnEdicion.getIoeObraExterior());
    }

    public void guardar() {
        try {
            entidadEnEdicion.setIoeObraExterior(comboObraExterior.getSelectedT());
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
            restClient.eliminar(entidadEnEdicion.getIoePk());
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
            historialInfItemObraExterior = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgInfItemObraExterior getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInfItemObraExterior entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgInfItemObraExterior> getHistorialInfItemObraExterior() {
        return historialInfItemObraExterior;
    }

    public void setHistorialInfItemObraExterior(List<SgInfItemObraExterior> historialInfItemObraExterior) {
        this.historialInfItemObraExterior = historialInfItemObraExterior;
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

    public LazyInfItemObraExteriorDataModel getInfItemObraExteriorLazyModel() {
        return infItemObraExteriorLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyInfItemObraExteriorDataModel infItemObraExteriorLazyModel) {
        this.infItemObraExteriorLazyModel = infItemObraExteriorLazyModel;
    }

    public SofisComboG<SgInfObraExterior> getComboObraExterior() {
        return comboObraExterior;
    }

    public void setComboObraExterior(SofisComboG<SgInfObraExterior> comboObraExterior) {
        this.comboObraExterior = comboObraExterior;
    }

}
