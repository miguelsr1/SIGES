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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCargoOAE;
import sv.gob.mined.siges.web.dto.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCargoOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyCargoOAEDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CargoOAERestClient;
import sv.gob.mined.siges.web.restclient.TipoOrganismoAdministrativoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CargoOAEBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CargoOAEBean.class.getName());

    @Inject
    private CargoOAERestClient restClient;
    
    @Inject
    private TipoOrganismoAdministrativoRestClient restTipoOAE;

    private FiltroCargoOAE filtro = new FiltroCargoOAE();
    private SgCargoOAE entidadEnEdicion = new SgCargoOAE();
    private List<SgCargoOAE> historialCargoOAE = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCargoOAEDataModel cargoOAELazyModel;
    private List<SgTipoOrganismoAdministrativo> listaTiposOAE;

    public CargoOAEBean() {
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
            cargoOAELazyModel = new LazyCargoOAEDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            FiltroCodiguera fcombos = new FiltroCodiguera();
            fcombos.setHabilitado(true);
            fcombos.setOrderBy(new String[]{"toaNombre"});
            fcombos.setAscending(new boolean[]{true});
            listaTiposOAE = restTipoOAE.buscar(fcombos);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCargoOAE();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCargoOAE();
    }

    public void actualizar(SgCargoOAE var) {
        try{
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getCoaPk());
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
            restClient.eliminar(entidadEnEdicion.getCoaPk());
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
            historialCargoOAE = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCargoOAE getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCargoOAE filtro) {
        this.filtro = filtro;
    }

    public SgCargoOAE getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCargoOAE entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgCargoOAE> getHistorialCargoOAE() {
        return historialCargoOAE;
    }

    public void setHistorialCargoOAE(List<SgCargoOAE> historialCargoOAE) {
        this.historialCargoOAE = historialCargoOAE;
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

    public LazyCargoOAEDataModel getCargoOAELazyModel() {
        return cargoOAELazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCargoOAEDataModel cargoOAELazyModel) {
        this.cargoOAELazyModel = cargoOAELazyModel;
    }

    public List<SgTipoOrganismoAdministrativo> getListaTiposOAE() {
        return listaTiposOAE;
    }

    public void setListaTiposOAE(List<SgTipoOrganismoAdministrativo> listaTiposOAE) {
        this.listaTiposOAE = listaTiposOAE;
    }

}
