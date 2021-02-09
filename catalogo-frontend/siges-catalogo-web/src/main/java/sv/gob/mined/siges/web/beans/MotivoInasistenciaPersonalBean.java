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
import sv.gob.mined.siges.web.dto.SgCargo;
import sv.gob.mined.siges.web.dto.SgMotivoInasistenciaPersonal;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyMotivoInasistenciaPersonalDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CargoRestClient;
import sv.gob.mined.siges.web.restclient.MotivoInasistenciaPersonalRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MotivoInasistenciaPersonalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MotivoInasistenciaPersonalBean.class.getName());

    @Inject
    private MotivoInasistenciaPersonalRestClient restClient;
    
    @Inject
    private CargoRestClient restCargo;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgMotivoInasistenciaPersonal entidadEnEdicion = new SgMotivoInasistenciaPersonal();
    private List<SgMotivoInasistenciaPersonal> historialMotivoInasistenciaPersonal = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMotivoInasistenciaPersonalDataModel motivoInasistenciaPersonalLazyModel;
    private List<SgCargo> cargoList;
    private List<SgCargo> cargoDatosList;
    
    public MotivoInasistenciaPersonalBean() {
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
            motivoInasistenciaPersonalLazyModel = new LazyMotivoInasistenciaPersonalDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"carNombre"});
            fil.setAscending(new boolean[]{true});
            cargoDatosList = restCargo.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
        entidadEnEdicion = new SgMotivoInasistenciaPersonal();
        cargoList = new ArrayList();
    }

    public void actualizar(Long id) {
        try{
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(id);
            cargoList = entidadEnEdicion.getMipCargos();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setMipCargos(cargoList);
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
            restClient.eliminar(entidadEnEdicion.getMipPk());
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
            historialMotivoInasistenciaPersonal = restClient.obtenerHistorialPorId(id);
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

    public SgMotivoInasistenciaPersonal getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMotivoInasistenciaPersonal entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMotivoInasistenciaPersonal> getHistorialMotivoInasistenciaPersonal() {
        return historialMotivoInasistenciaPersonal;
    }

    public void setHistorialMotivoInasistenciaPersonal(List<SgMotivoInasistenciaPersonal> historialMotivoInasistenciaPersonal) {
        this.historialMotivoInasistenciaPersonal = historialMotivoInasistenciaPersonal;
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

    public LazyMotivoInasistenciaPersonalDataModel getMotivoInasistenciaPersonalLazyModel() {
        return motivoInasistenciaPersonalLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMotivoInasistenciaPersonalDataModel motivoInasistenciaPersonalLazyModel) {
        this.motivoInasistenciaPersonalLazyModel = motivoInasistenciaPersonalLazyModel;
    }

    public List<SgCargo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<SgCargo> cargoList) {
        this.cargoList = cargoList;
    }

    public List<SgCargo> getCargoDatosList() {
        return cargoDatosList;
    }

    public void setCargoDatosList(List<SgCargo> cargoDatosList) {
        this.cargoDatosList = cargoDatosList;
    }

}
