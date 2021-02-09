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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAyuda;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyAyudaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AyudaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AyudaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AyudaBean.class.getName());

    @Inject
    private AyudaRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgAyuda entidadEnEdicion = new SgAyuda();
    private List<SgAyuda> historialAyuda = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyAyudaDataModel ayudaLazyModel;
    private Boolean soloLectura = Boolean.FALSE;
    
    @Inject
    @Param(name = "id")
    private Long ayudaId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    public AyudaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (ayudaId != null && ayudaId > 0) {
                    this.actualizar(restClient.obtenerPorId(ayudaId));
                    soloLectura = editable != null ? !editable : soloLectura;              
            }      
            else{
                this.agregar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }



    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

   

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAyuda();
    }

    public void actualizar(SgAyuda var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgAyuda) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
           
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina(){
        if(ayudaId==null){
            return Etiquetas.getValue("nuevoAyuda");
        }else{
            return Etiquetas.getValue("edicionAyuda")+" "+ entidadEnEdicion.getAyuNombre();
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgAyuda getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAyuda entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAyuda> getHistorialAyuda() {
        return historialAyuda;
    }

    public void setHistorialAyuda(List<SgAyuda> historialAyuda) {
        this.historialAyuda = historialAyuda;
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

    public LazyAyudaDataModel getAyudaLazyModel() {
        return ayudaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyAyudaDataModel ayudaLazyModel) {
        this.ayudaLazyModel = ayudaLazyModel;
    }

}
