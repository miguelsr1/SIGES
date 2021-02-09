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
import sv.gob.mined.siges.web.dto.SgDepartamento;
import sv.gob.mined.siges.web.dto.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyMunicipioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DepartamentoRestClient;
import sv.gob.mined.siges.web.restclient.MunicipioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MunicipioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MunicipioBean.class.getName());

    @Inject
    private MunicipioRestClient restClient;
    
     @Inject
    private DepartamentoRestClient depRestClient;

    private FiltroMunicipio filtro = new FiltroMunicipio();
    private SgMunicipio entidadEnEdicion = new SgMunicipio();
    private List<SgMunicipio> historialMunicipio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMunicipioDataModel municipioLazyModel;
    private SofisComboG<SgDepartamento> comboDepartamento;

    public MunicipioBean() {
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
            if(comboDepartamento.getSelectedT()!=null){
                filtro.setMunDepartamento(comboDepartamento.getSelectedT().getDepNombre());
            }
            totalResultados = restClient.buscarTotal(filtro);
            municipioLazyModel = new LazyMunicipioDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            FiltroCodiguera fc= new FiltroCodiguera();
            comboDepartamento=new SofisComboG(depRestClient.buscar(fc),"depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
         } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboDepartamento.setSelected(0);
    }

    public void limpiar() {
        comboDepartamento.setSelected(0);
        filtro = new FiltroMunicipio();
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgMunicipio();
    }

    public void actualizar(SgMunicipio var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgMunicipio) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
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
            restClient.eliminar(entidadEnEdicion.getMunPk());
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
            historialMunicipio = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgDepartamento> completeDepartamento(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setMaxResults(10L);
            return depRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public FiltroMunicipio getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMunicipio filtro) {
        this.filtro = filtro;
    }

    public SgMunicipio getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMunicipio entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMunicipio> getHistorialMunicipio() {
        return historialMunicipio;
    }

    public void setHistorialMunicipio(List<SgMunicipio> historialMunicipio) {
        this.historialMunicipio = historialMunicipio;
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

    public LazyMunicipioDataModel getMunicipioLazyModel() {
        return municipioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMunicipioDataModel municipioLazyModel) {
        this.municipioLazyModel = municipioLazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

}
