/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import sv.gob.mined.siges.web.dto.SgEjecucionProcesoNocturno;
import sv.gob.mined.siges.web.dto.SgProcesoNocturno;
import sv.gob.mined.siges.web.enumerados.EnumServiciosRest;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyProcesoNocturnoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EjecucionProcesoNocturnoRestClient;
import sv.gob.mined.siges.web.restclient.ProcesoNocturnoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ProcesoNocturnoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProcesoNocturnoBean.class.getName());

    @Inject
    private ProcesoNocturnoRestClient restClient;

    @Inject
    private EjecucionProcesoNocturnoRestClient ejecucionRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgProcesoNocturno entidadEnEdicion = new SgProcesoNocturno();
    private SgProcesoNocturno entidadProcesar = new SgProcesoNocturno();
    private List<SgProcesoNocturno> historialProcesoNocturno = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyProcesoNocturnoDataModel procesoNocturnoLazyModel;
    private SofisComboG<EnumServiciosRest> comboServicioRest;
    private Integer cantidadElementos=0;

    public ProcesoNocturnoBean() {
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
            procesoNocturnoLazyModel = new LazyProcesoNocturnoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<EnumServiciosRest> estados = new ArrayList(Arrays.asList(EnumServiciosRest.values()));

        comboServicioRest = new SofisComboG(estados, "text");
        comboServicioRest.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

    }

    private void limpiarCombos() {
        comboServicioRest.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgProcesoNocturno();
    }

    public void actualizar(SgProcesoNocturno var) {
        try {
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getPrnPk());
            entidadProcesar =(SgProcesoNocturno) SerializationUtils.clone(var);
            if (entidadEnEdicion.getPrnServicio() != null) {
                comboServicioRest.setSelectedT(entidadEnEdicion.getPrnServicio());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizarListaEjecuciones(SgProcesoNocturno var) {
        try {
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getPrnPk());
            cantidadElementos=entidadEnEdicion.getEjecucionesProcesoNocturno()!=null?entidadEnEdicion.getEjecucionesProcesoNocturno().size():0;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setPrnServicio(comboServicioRest.getSelectedT());
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
            restClient.eliminar(entidadEnEdicion.getPrnPk());
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
            historialProcesoNocturno = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void procesar() {
        SgEjecucionProcesoNocturno ejecucion = new SgEjecucionProcesoNocturno();
        try {
            if (entidadProcesar != null) {
                ejecucion.setEprProcesoNocturnoFk(entidadProcesar);
                LocalDateTime comienzo = LocalDateTime.now();
                ejecucion.setEprComienzoEjecucion(comienzo);    
                Long startTime = System.currentTimeMillis();
                Boolean respuesta=restClient.ejecutarProcesamiento(entidadProcesar);
                if(respuesta){
                    LocalDateTime fin = LocalDateTime.now();
                    ejecucion.setEprFinEjecucion(fin);
                    ejecucion.setEprEjecucionCorrecta(Boolean.TRUE);
                }
                Long tiempo = System.currentTimeMillis() - startTime;
                LOGGER.log(Level.SEVERE, "tiempo "+tiempo);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "Procesamiento correcto", "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            ejecucion.setEprEjecucionCorrecta(Boolean.FALSE);
        } finally {
            try {
                ejecucionRestClient.guardar(ejecucion);
            } catch (Exception ex) {

            }
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgProcesoNocturno getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgProcesoNocturno entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgProcesoNocturno> getHistorialProcesoNocturno() {
        return historialProcesoNocturno;
    }

    public void setHistorialProcesoNocturno(List<SgProcesoNocturno> historialProcesoNocturno) {
        this.historialProcesoNocturno = historialProcesoNocturno;
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

    public LazyProcesoNocturnoDataModel getProcesoNocturnoLazyModel() {
        return procesoNocturnoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyProcesoNocturnoDataModel procesoNocturnoLazyModel) {
        this.procesoNocturnoLazyModel = procesoNocturnoLazyModel;
    }

    public SofisComboG<EnumServiciosRest> getComboServicioRest() {
        return comboServicioRest;
    }

    public void setComboServicioRest(SofisComboG<EnumServiciosRest> comboServicioRest) {
        this.comboServicioRest = comboServicioRest;
    }

    public SgProcesoNocturno getEntidadProcesar() {
        return entidadProcesar;
    }

    public void setEntidadProcesar(SgProcesoNocturno entidadProcesar) {
        this.entidadProcesar = entidadProcesar;
    }

    public Integer getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(Integer cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }

   

}
