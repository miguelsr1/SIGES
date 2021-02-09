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
import sv.gob.mined.siges.web.dto.SgAfComisionDescargo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.lazymodels.LazyComisionDescargoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ComisionUnidadActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.DepartamentoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ComisionDescargoUnidadActivoFijoBean implements Serializable {
  
    private static final Logger LOGGER = Logger.getLogger(ComisionDescargoUnidadActivoFijoBean.class.getName());

    @Inject
    private ComisionUnidadActivoFijoRestClient restClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private DepartamentoRestClient departamentoRestClient;
    
    private SgAfUnidadesActivoFijo unidadAF = null;
    
    private FiltroUnidadesActivoFijo filtro = new FiltroUnidadesActivoFijo();
    private SgAfComisionDescargo entidadEnEdicion = new SgAfComisionDescargo();
    private List<SgAfComisionDescargo> historialComisionAF = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyComisionDescargoDataModel comisionDescargoDataModel;

    @Inject
    @Param(name = "id")
    private Long uafId;

    @PostConstruct
    public void init() {
        try {
            if (uafId != null && uafId > 0) {
                unidadAF = unidadesActivoFijoRestClient.obtenerPorId(uafId);
                if(unidadAF == null) {
                     JSFUtils.redirectToIndex();
                }
                buscar();
            } else {
                JSFUtils.redirectToIndex();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setUnidadId(unidadAF  != null ? unidadAF.getUafPk() : null);
            totalResultados = restClient.buscarTotal(filtro);
            comisionDescargoDataModel = new LazyComisionDescargoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    
    public void agregarMiembroComision() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgAfComisionDescargo();
    }
    public void guardarMiembroComision() {

    }

    public void actualizar(SgAfComisionDescargo var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgAfComisionDescargo) SerializationUtils.clone(var);

    }

    public void guardar() {
        try {
            entidadEnEdicion.setCdeActivoFijoFk(unidadAF);
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
            restClient.eliminar(entidadEnEdicion.getCdePk());
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
            historialComisionAF = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroUnidadesActivoFijo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroUnidadesActivoFijo filtro) {
        this.filtro = filtro;
    }

    public ComisionUnidadActivoFijoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ComisionUnidadActivoFijoRestClient restClient) {
        this.restClient = restClient;
    }

    public DepartamentoRestClient getDepartamentoRestClient() {
        return departamentoRestClient;
    }

    public void setDepartamentoRestClient(DepartamentoRestClient departamentoRestClient) {
        this.departamentoRestClient = departamentoRestClient;
    }

    public SgAfComisionDescargo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfComisionDescargo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAfComisionDescargo> getHistorialComisionAF() {
        return historialComisionAF;
    }

    public void setHistorialComisionAF(List<SgAfComisionDescargo> historialComisionAF) {
        this.historialComisionAF = historialComisionAF;
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

    public LazyComisionDescargoDataModel getComisionDescargoDataModel() {
        return comisionDescargoDataModel;
    }

    public void setComisionDescargoDataModel(LazyComisionDescargoDataModel comisionDescargoDataModel) {
        this.comisionDescargoDataModel = comisionDescargoDataModel;
    }

    public SgAfUnidadesActivoFijo getUnidadAF() {
        return unidadAF;
    }

    public void setUnidadAF(SgAfUnidadesActivoFijo unidadAF) {
        this.unidadAF = unidadAF;
    }

}
