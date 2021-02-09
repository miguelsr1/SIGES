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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.web.enumerados.EnumMes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyecto;
import sv.gob.mined.siges.web.lazymodels.LazyDepreciacionMaestroDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DepreciacionMaestroRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DepreciacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DepreciacionBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private DepreciacionMaestroRestClient depreciacionRestClient;
    
    private FiltroCodiguera filtro;
    
    private SofisComboG<SgAfProyectos> comboProyectos;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgAfProyectos> comboProyectosInsert;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamientoInsert;
    private SofisComboG<EnumEstadosProceso> comboEstados;
    private SofisComboG<EnumMes> comboMeses;
    private SofisComboG<EnumMes> comboMesesInsert;
    
    private List<SgAfProyectos> proyectos;
    private SgAfFuenteFinanciamiento fuenteSeleccionada;
    private SgAfFuenteFinanciamiento fuenteSeleccionadaInsert;
    private SgAfDepreciacionMaestro entidadEnEdicion;
    private LazyDepreciacionMaestroDataModel depreciacionMaestroLazyModel;
    private Integer paginado = 10;
    private Long totalResultados = 0L;
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
    private List<RevHistorico> historial = new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALCULAR_DEPRECIACION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        try {
            List<EnumEstadosProceso> estados = new ArrayList(Arrays.asList(EnumEstadosProceso.values()));
            comboEstados = new SofisComboG(estados, "text");
            comboEstados.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboEstados.setSelectedT(EnumEstadosProceso.EN_PROCESO);
            
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            
            FiltroFuenteFinanciamientoAF fuentes = new FiltroFuenteFinanciamientoAF();
            fuentes.setHabilitado(Boolean.TRUE);
            fuentes.setOrderBy(new String[]{"ffiNombre"});
            fuentes.setIncluirCampos(new String[]{"ffiNombre","ffiAplicaPara", "ffiVersion"});
            List<SgAfFuenteFinanciamiento> listaFuentes = catalogosRestClient.buscarFuenteFinanciamiento(fuentes);         
            
            comboFuenteFinanciamiento = new SofisComboG(listaFuentes, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboFuenteFinanciamientoInsert = new SofisComboG(listaFuentes, "ffiNombre");
            comboFuenteFinanciamientoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroProyecto fpro = new FiltroProyecto();
            fpro.setHabilitado(Boolean.TRUE);
            fpro.setLiquidado(Boolean.FALSE);
            fpro.setOrderBy(new String[]{"proNombre"});
            fpro.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            proyectos = catalogosRestClient.buscarProyecto(fpro);
            
            comboProyectos = new SofisComboG();
            comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboProyectosInsert = new SofisComboG();
            comboProyectosInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboMeses = new SofisComboG(Arrays.asList(EnumMes.values()), "text");
            comboMeses.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboMesesInsert = new SofisComboG(Arrays.asList(EnumMes.values()), "text");
            comboMesesInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void buscar() {
         try {
            filtroBienes.setIncluirCampos(new String[]{"dmaAnioProceso","dmaMesProceso","dmaUltModFecha","dmaEstado","dmaFechaFinalProcesamiento",
                "dmaFechaInicioProcesamiento","dmaFechaCreacion","dmaCodigoInventario","dmaFuenteFinanciamientoFk.ffiNombre","dmaFuenteFinanciamientoFk.ffiPk","dmaFuenteFinanciamientoFk.ffiVersion","dmaProyectoFk.proNombre","dmaProyectoFk.proVersion","dmaProyectoFk.proPk","dmaVersion"
            });
            filtroBienes.setFuenteId(comboFuenteFinanciamiento != null && comboFuenteFinanciamiento.getSelectedT() != null ? comboFuenteFinanciamiento.getSelectedT().getFfiPk(): null);
            filtroBienes.setProyectoId(comboProyectos != null && comboProyectos.getSelectedT() != null ? comboProyectos.getSelectedT().getProPk(): null);
            filtroBienes.setEstado(comboEstados != null ? comboEstados.getSelectedT(): null);
            
            filtroBienes.setMes(comboMeses != null && comboMeses.getSelectedT() != null ? comboMeses.getSelectedT().getNumero().longValue() : null);
            
            totalResultados = depreciacionRestClient.buscarTotal(filtroBienes);

            depreciacionMaestroLazyModel = new LazyDepreciacionMaestroDataModel(depreciacionRestClient, filtroBienes, totalResultados, paginado);
        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarFuente(Boolean insert) { 
        if(insert != null && insert) {
            fuenteSeleccionadaInsert = comboFuenteFinanciamientoInsert.getSelectedT();
            if(fuenteSeleccionadaInsert != null && fuenteSeleccionadaInsert.getFfiRequiereProyecto() != null && fuenteSeleccionadaInsert.getFfiRequiereProyecto()) {
                comboProyectosInsert = new SofisComboG(proyectos, "proNombre");
                comboProyectosInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                
                if(entidadEnEdicion != null) {
                    comboProyectosInsert.setSelectedT(entidadEnEdicion.getDmaProyectoFk());
                }
            } else {
                comboProyectosInsert = new SofisComboG();
            }
        } else {
            fuenteSeleccionada = comboFuenteFinanciamiento.getSelectedT();
            if(fuenteSeleccionada != null && fuenteSeleccionada.getFfiRequiereProyecto() != null && fuenteSeleccionada.getFfiRequiereProyecto()) {
                comboProyectos = new SofisComboG(proyectos, "proNombre");
                comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            } else {
                comboProyectos = new SofisComboG();
            }
        }
            
    }
    
    public void procesar() {
        try {
            depreciacionRestClient.procesarTodas();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.TAREAS_PROCESADAS_CORRECTAMENTE), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void procesarTarea(SgAfDepreciacionMaestro maestro) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = (SgAfDepreciacionMaestro) SerializationUtils.clone(maestro);
            depreciacionRestClient.procesarTarea(maestro);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.TAREAS_PROCESADAS_CORRECTAMENTE), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiarCombos() {
        if(comboFuenteFinanciamientoInsert != null) {
            comboFuenteFinanciamientoInsert.setSelected(-1);
        }
        if(comboProyectosInsert != null) {
            comboProyectosInsert.setSelected(-1);
        }
        if(comboMesesInsert != null) {
             comboMesesInsert.setSelected(-1);
        }
        seleccionarFuente(Boolean.TRUE);
    }
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAfDepreciacionMaestro();
    }
    
    public void guardar() {
        try {
            if(entidadEnEdicion.getDmaPk() == null) {
                entidadEnEdicion.setDmaEstado(EnumEstadosProceso.PENDIENTE);
                entidadEnEdicion.setDmaFechaCreacion(LocalDateTime.now());
            }
            
            entidadEnEdicion.setDmaMesProceso(comboMesesInsert != null && comboMesesInsert.getSelectedT() != null ? comboMesesInsert.getSelectedT().getNumero() : null);
            entidadEnEdicion.setDmaFuenteFinanciamientoFk(fuenteSeleccionadaInsert);
            entidadEnEdicion.setDmaProyectoFk(comboProyectosInsert != null ? comboProyectosInsert.getSelectedT() : null);

            entidadEnEdicion = depreciacionRestClient.guardar(entidadEnEdicion);
            
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.TAREA_GUARDADO_CORRECTO), "");
            
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizar(SgAfDepreciacionMaestro var) {
        try {
            JSFUtils.limpiarMensajesError();
            comboMesesInsert.setSelected(-1);
            entidadEnEdicion = (SgAfDepreciacionMaestro) SerializationUtils.clone(var);
            comboFuenteFinanciamientoInsert.setSelectedT(entidadEnEdicion.getDmaFuenteFinanciamientoFk());
            comboMesesInsert.setSelectedT(EnumMes.getByValue(entidadEnEdicion.getDmaMesProceso()));
            seleccionarFuente(Boolean.TRUE);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eliminar() {
        try {
            depreciacionRestClient.eliminar(entidadEnEdicion.getDmaPk());
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
    
    public String cargarHistorial(Long id) {
        try {
            historial = depreciacionRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public boolean puedeActualizarse(SgAfDepreciacionMaestro maestro) {
        if(maestro != null && maestro.getDmaEstado() != null && maestro.getDmaEstado().equals(EnumEstadosProceso.PENDIENTE) &&
                sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_MAESTRO)) {
            return true;
        }
        return false;
    }
    
    public boolean puedeEliminarse(SgAfDepreciacionMaestro maestro) {
        if(maestro != null && maestro.getDmaEstado() != null && maestro.getDmaEstado().equals(EnumEstadosProceso.PENDIENTE) &&
                sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_DEPRECIACION_MAESTRO)) {
            return true;
        }
        return false;
    }
    
    public void limpiar() {
        filtroBienes = new FiltroBienesDepreciables();
        comboFuenteFinanciamiento.setSelected(-1);
        comboProyectos.setSelected(-1);
        comboEstados.setSelectedT(EnumEstadosProceso.PENDIENTE);
        fuenteSeleccionada = null;
        buscar();
    }
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<SgAfProyectos> getComboProyectos() {
        return comboProyectos;
    }

    public void setComboProyectos(SofisComboG<SgAfProyectos> comboProyectos) {
        this.comboProyectos = comboProyectos;
    }

    public SofisComboG<SgAfFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public List<SgAfProyectos> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<SgAfProyectos> proyectos) {
        this.proyectos = proyectos;
    }

    public SgAfFuenteFinanciamiento getFuenteSeleccionada() {
        return fuenteSeleccionada;
    }

    public void setFuenteSeleccionada(SgAfFuenteFinanciamiento fuenteSeleccionada) {
        this.fuenteSeleccionada = fuenteSeleccionada;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SgAfDepreciacionMaestro getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfDepreciacionMaestro entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public LazyDepreciacionMaestroDataModel getDepreciacionMaestroLazyModel() {
        return depreciacionMaestroLazyModel;
    }

    public void setDepreciacionMaestroLazyModel(LazyDepreciacionMaestroDataModel depreciacionMaestroLazyModel) {
        this.depreciacionMaestroLazyModel = depreciacionMaestroLazyModel;
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

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
    }

    public void setHistorial(List<RevHistorico> historial) {
        this.historial = historial;
    }

    public SofisComboG<SgAfProyectos> getComboProyectosInsert() {
        return comboProyectosInsert;
    }

    public void setComboProyectosInsert(SofisComboG<SgAfProyectos> comboProyectosInsert) {
        this.comboProyectosInsert = comboProyectosInsert;
    }

    public SofisComboG<SgAfFuenteFinanciamiento> getComboFuenteFinanciamientoInsert() {
        return comboFuenteFinanciamientoInsert;
    }

    public void setComboFuenteFinanciamientoInsert(SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamientoInsert) {
        this.comboFuenteFinanciamientoInsert = comboFuenteFinanciamientoInsert;
    }

    public SgAfFuenteFinanciamiento getFuenteSeleccionadaInsert() {
        return fuenteSeleccionadaInsert;
    }

    public void setFuenteSeleccionadaInsert(SgAfFuenteFinanciamiento fuenteSeleccionadaInsert) {
        this.fuenteSeleccionadaInsert = fuenteSeleccionadaInsert;
    }

    public SofisComboG<EnumEstadosProceso> getComboEstados() {
        return comboEstados;
    }

    public void setComboEstados(SofisComboG<EnumEstadosProceso> comboEstados) {
        this.comboEstados = comboEstados;
    }

    public CatalogosRestClient getCatalogosRestClient() {
        return catalogosRestClient;
    }

    public void setCatalogosRestClient(CatalogosRestClient catalogosRestClient) {
        this.catalogosRestClient = catalogosRestClient;
    }

    public DepreciacionMaestroRestClient getDepreciacionRestClient() {
        return depreciacionRestClient;
    }

    public void setDepreciacionRestClient(DepreciacionMaestroRestClient depreciacionRestClient) {
        this.depreciacionRestClient = depreciacionRestClient;
    }

    public SofisComboG<EnumMes> getComboMeses() {
        return comboMeses;
    }

    public void setComboMeses(SofisComboG<EnumMes> comboMeses) {
        this.comboMeses = comboMeses;
    }

    public SofisComboG<EnumMes> getComboMesesInsert() {
        return comboMesesInsert;
    }

    public void setComboMesesInsert(SofisComboG<EnumMes> comboMesesInsert) {
        this.comboMesesInsert = comboMesesInsert;
    }
}
