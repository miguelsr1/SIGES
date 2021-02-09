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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgItemEvaluarOrganismo;
import sv.gob.mined.siges.web.dto.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyTipoOrganismoAdministrativoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.TipoOrganismoAdministrativoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TipoOrganismoAdministrativoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TipoOrganismoAdministrativoBean.class.getName());

    @Inject
    private TipoOrganismoAdministrativoRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgTipoOrganismoAdministrativo entidadEnEdicion = new SgTipoOrganismoAdministrativo();
    private List<SgTipoOrganismoAdministrativo> historialTipoOrganismoAdministrativo = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTipoOrganismoAdministrativoDataModel tipoOrganismoAdministrativoLazyModel;
    SgItemEvaluarOrganismo itemEdicion = new SgItemEvaluarOrganismo();

    public TipoOrganismoAdministrativoBean() {
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
            tipoOrganismoAdministrativoLazyModel = new LazyTipoOrganismoAdministrativoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void onAddNew() {
        SgItemEvaluarOrganismo nuevoItem = new SgItemEvaluarOrganismo();
        entidadEnEdicion.getToaItems().add(nuevoItem);
    }
    public void onRowEdit(RowEditEvent event) {
        //Ordenar la lista
    }
     
    public void onRowCancel(RowEditEvent event) {
        SgItemEvaluarOrganismo edit = (SgItemEvaluarOrganismo) event.getObject();
        if(StringUtils.isBlank(edit.getIeoCodigo()) && StringUtils.isBlank(edit.getIeoNombre()) && edit.getIeoOrden()==null){
            entidadEnEdicion.getToaItems().remove(edit);
        }
    }
    
    public void onRowDelete(SgItemEvaluarOrganismo items){
        entidadEnEdicion.getToaItems().remove(items);
    }
    
    public void actualizarItem(SgItemEvaluarOrganismo items){
        itemEdicion = items;
    }
    
    public void eliminarItem(){
        entidadEnEdicion.getToaItems().remove(itemEdicion);
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgTipoOrganismoAdministrativo();
    }

    public void actualizar(SgTipoOrganismoAdministrativo var) {
        try{
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getToaPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
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
            restClient.eliminar(entidadEnEdicion.getToaPk());
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
            historialTipoOrganismoAdministrativo = restClient.obtenerHistorialPorId(id);
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

    public SgTipoOrganismoAdministrativo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTipoOrganismoAdministrativo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgTipoOrganismoAdministrativo> getHistorialTipoOrganismoAdministrativo() {
        return historialTipoOrganismoAdministrativo;
    }

    public void setHistorialTipoOrganismoAdministrativo(List<SgTipoOrganismoAdministrativo> historialTipoOrganismoAdministrativo) {
        this.historialTipoOrganismoAdministrativo = historialTipoOrganismoAdministrativo;
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

    public LazyTipoOrganismoAdministrativoDataModel getTipoOrganismoAdministrativoLazyModel() {
        return tipoOrganismoAdministrativoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTipoOrganismoAdministrativoDataModel tipoOrganismoAdministrativoLazyModel) {
        this.tipoOrganismoAdministrativoLazyModel = tipoOrganismoAdministrativoLazyModel;
    }

    public SgItemEvaluarOrganismo getItemEdicion() {
        return itemEdicion;
    }

    public void setItemEdicion(SgItemEvaluarOrganismo itemEdicion) {
        this.itemEdicion = itemEdicion;
    }

}
