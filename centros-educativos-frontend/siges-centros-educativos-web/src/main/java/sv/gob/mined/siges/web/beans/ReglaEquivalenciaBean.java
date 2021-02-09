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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgReglaEquivalencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReglaEquivalencia;
import sv.gob.mined.siges.web.lazymodels.LazyReglasEquivalenciaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ReglasEquivalenciaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ReglaEquivalenciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ReglaEquivalenciaBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private ReglasEquivalenciaRestClient reglasNormativaRestClient;
    
    
    private SgReglaEquivalencia entidadEnEdicion = new SgReglaEquivalencia();
    private LazyReglasEquivalenciaDataModel reglasEquivalenciaDataModel;
    private FiltroReglaEquivalencia filtro = new FiltroReglaEquivalencia();
    private List<RevHistorico> historialReglasEquivalencia = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    
    public ReglaEquivalenciaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REGLAS_EQUIVALENCIAS)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = reglasNormativaRestClient.buscarTotal(filtro);
            reglasEquivalenciaDataModel = new LazyReglasEquivalenciaDataModel(reglasNormativaRestClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiar() {
        filtro = new FiltroReglaEquivalencia();
        buscar();
    }
    public void agregar() {
        entidadEnEdicion = new SgReglaEquivalencia();
    }
    
    public void actualizar(SgReglaEquivalencia req) {
        try {
            entidadEnEdicion = reglasNormativaRestClient.obtenerPorId(req.getReqPk());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void guardar() {
        try {
            entidadEnEdicion = reglasNormativaRestClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();  
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            reglasNormativaRestClient.eliminar(entidadEnEdicion.getReqPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialReglasEquivalencia = reglasNormativaRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public SgReglaEquivalencia getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgReglaEquivalencia entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public LazyReglasEquivalenciaDataModel getReglasEquivalenciDataModel() {
        return reglasEquivalenciaDataModel;
    }

    public void setReglasEquivalenciDataModel(LazyReglasEquivalenciaDataModel reglasEquivalenciDataModel) {
        this.reglasEquivalenciaDataModel = reglasEquivalenciDataModel;
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

    public FiltroReglaEquivalencia getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroReglaEquivalencia filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorialReglasEquivalencia() {
        return historialReglasEquivalencia;
    }

    public void setHistorialReglasEquivalencia(List<RevHistorico> historialReglasEquivalencia) {
        this.historialReglasEquivalencia = historialReglasEquivalencia;
    }

}
