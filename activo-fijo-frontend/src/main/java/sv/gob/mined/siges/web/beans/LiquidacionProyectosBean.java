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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAfLiquidacionProyecto;
import sv.gob.mined.siges.web.dto.catalogo.SgAfFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgAfProyectos;
import sv.gob.mined.siges.web.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacionProyectos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyecto;
import sv.gob.mined.siges.web.lazymodels.LazyLiquidacionProyectoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionProyectoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class LiquidacionProyectosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LiquidacionProyectosBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private LiquidacionProyectoRestClient liquidacionProyectoRestClient;
    
    private FiltroCodiguera filtro;
    
    private SofisComboG<SgAfProyectos> comboProyectos;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgAfProyectos> comboProyectosInsert;
    private SofisComboG<SgAfFuenteFinanciamiento> comboFuenteFinanciamientoInsert;
    private SofisComboG<EnumEstadosProceso> comboEstados;
    private SgAfLiquidacionProyecto entidadEnEdicion;
    
    private List<SgAfProyectos> proyectos;
    private SgAfFuenteFinanciamiento fuenteSeleccionada;
    private SgAfFuenteFinanciamiento fuenteSeleccionadaInsert;
    private LazyLiquidacionProyectoDataModel liquidacionProyectoLazyModel;
    private Integer paginado = 10;
    private Long totalResultados = 0L;
    private FiltroLiquidacionProyectos filtroLiqProyectos = new FiltroLiquidacionProyectos();
    private List<RevHistorico> historial = new ArrayList();

    private SgAfFuenteFinanciamiento fondoGeneral;
    
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_LIQUIDACION_PROYECTOS)) {
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
            
            FiltroFuenteFinanciamientoAF filtroFuentes = new FiltroFuenteFinanciamientoAF();
            filtroFuentes.setHabilitado(Boolean.TRUE);
            filtroFuentes.setOrderBy(new String[]{"ffiNombre"});
            filtroFuentes.setIncluirCampos(new String[]{"ffiNombre","ffiCodigo","ffiRequiereProyecto","ffiAplicaPara", "ffiVersion"});
            List<SgAfFuenteFinanciamiento> listaFuentes = catalogosRestClient.buscarFuenteFinanciamiento(filtroFuentes);         
            
            List<SgAfFuenteFinanciamiento> fuentes = new ArrayList();
            if(listaFuentes != null && !listaFuentes.isEmpty()) {
                for(SgAfFuenteFinanciamiento fuente: listaFuentes) {
                    if(Constantes.CODIGO_FUENTE_FONDO_GENERAL.equals(fuente.getFfiCodigo().trim())) {
                        fondoGeneral = fuente;
                    } else if(!Constantes.CODIGO_FUENTE_SIN_FUENTE.equals(fuente.getFfiCodigo().trim()) 
                            && fuente.getFfiRequiereProyecto() != null && fuente.getFfiRequiereProyecto()) {
                        fuentes.add(fuente);
                    }
                }
            }
            
            comboFuenteFinanciamiento = new SofisComboG(fuentes, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboFuenteFinanciamientoInsert = new SofisComboG(fuentes, "ffiNombre");
            comboFuenteFinanciamientoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroProyecto fpro = new FiltroProyecto();
            fpro.setHabilitado(Boolean.TRUE);
            fpro.setLiquidado(Boolean.FALSE);
            fpro.setOrderBy(new String[]{"proNombre"});
            fpro.setIncluirCampos(new String[]{"proNombre", "proVersion"});
            proyectos = catalogosRestClient.buscarProyecto(fpro);
            
            comboProyectos = new SofisComboG(proyectos, "proNombre");
            comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                
            comboProyectosInsert = new SofisComboG(proyectos, "proNombre");
            comboProyectosInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscar() {
        try {
           filtroLiqProyectos.setIncluirCampos(new String[]{"lprFechaLiquidacion","lprUltModFecha","lprEstado","lprFechaInicioProcesamiento","lprFechaFinalProcesamiento",
                "lprFuenteFinanciamientoOrigenFk.ffiPk","lprFuenteFinanciamientoOrigenFk.ffiNombre",
                "lprFuenteFinanciamientoDestinoFk.ffiPk","lprFuenteFinanciamientoDestinoFk.ffiNombre" ,
                "lprProyectoFk.proPk","lprProyectoFk.proNombre","lprProyectoFk.proPk"
            });
           
           
           /** filtroLiqProyectos.setIncluirCampos(new String[]{"lprPk","lprFechaLiquidacion","lprUltModFecha","lprEstado","lprFechaInicioProcesamiento","lprFechaFinalProcesamiento",
                "lprFuenteFinanciamientoOrigenFk.ffiNombre",
                "lprFuenteFinanciamientoDestinoFk.ffiNombre",
                "lprProyectoFk.proNombre"
            });**/
            filtroLiqProyectos.setAscending(new boolean[]{true});
            filtroLiqProyectos.setOrderBy(new String[]{"lprFechaCreacion"});
            filtroLiqProyectos.setFuenteId(comboFuenteFinanciamiento != null && comboFuenteFinanciamiento.getSelectedT() != null ? comboFuenteFinanciamiento.getSelectedT().getFfiPk(): null);
            filtroLiqProyectos.setProyectoId(comboProyectos != null && comboProyectos.getSelectedT() != null ? comboProyectos.getSelectedT().getProPk(): null);
            filtroLiqProyectos.setEstado(comboEstados != null  ? comboEstados.getSelectedT() : null);
            
            totalResultados = liquidacionProyectoRestClient.buscarTotal(filtroLiqProyectos);

            liquidacionProyectoLazyModel = new LazyLiquidacionProyectoDataModel(liquidacionProyectoRestClient, filtroLiqProyectos, totalResultados, paginado);
        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void procesar() {
        try {
            liquidacionProyectoRestClient.procesarTodas();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.TAREAS_PROCESADAS_CORRECTAMENTE), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void procesarTarea(SgAfLiquidacionProyecto liqProyecto) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = (SgAfLiquidacionProyecto) SerializationUtils.clone(liqProyecto);
            liquidacionProyectoRestClient.procesarTarea(liqProyecto);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.TAREAS_PROCESADAS_CORRECTAMENTE), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void limpiarCombos() {
        comboFuenteFinanciamientoInsert.setSelected(-1);
        if(comboProyectosInsert != null) {
            comboProyectosInsert.setSelected(-1);
        }
    }
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAfLiquidacionProyecto();
        entidadEnEdicion.setLprFuenteFinanciamientoDestinoFk(fondoGeneral);
    }
    
    public void guardar() {
        try {
            if(entidadEnEdicion.getLprPk() == null) {
                entidadEnEdicion.setLprEstado(EnumEstadosProceso.PENDIENTE.getText());
                entidadEnEdicion.setLprFechaCreacion(LocalDateTime.now());
            }
            
            entidadEnEdicion.setLprFuenteFinanciamientoOrigenFk(comboFuenteFinanciamientoInsert != null ? comboFuenteFinanciamientoInsert.getSelectedT() : null);   
            entidadEnEdicion.setLprFuenteFinanciamientoDestinoFk(fondoGeneral);
            entidadEnEdicion.setLprProyectoFk(comboProyectosInsert != null ? comboProyectosInsert.getSelectedT() : null);
            
            entidadEnEdicion = liquidacionProyectoRestClient.guardar(entidadEnEdicion);
            
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
    
    public void actualizar(SgAfLiquidacionProyecto var) {
        try {
            //entidadEnEdicion = liquidacionProyectoRestClient.obtenerPorId(var.getLprPk(), Boolean.TRUE);
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = (SgAfLiquidacionProyecto) SerializationUtils.clone(var);
            comboFuenteFinanciamientoInsert.setSelectedT(entidadEnEdicion.getLprFuenteFinanciamientoOrigenFk());
            comboProyectosInsert.setSelectedT(entidadEnEdicion.getLprProyectoFk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eliminar() {
        try {
            liquidacionProyectoRestClient.eliminar(entidadEnEdicion.getLprPk());
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
            historial = liquidacionProyectoRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public boolean puedeActualizarse(SgAfLiquidacionProyecto liqProyecto) {
        if(liqProyecto != null && liqProyecto.getLprEstado() != null && liqProyecto.getLprEstado().equals(EnumEstadosProceso.PENDIENTE.getText()) &&
                sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_LIQUIDACION_PROYECTO)) {
            return true;
        }
        return false;
    }
    
    public boolean puedeEliminarse(SgAfLiquidacionProyecto liqProyecto) {
        if(liqProyecto != null && liqProyecto.getLprEstado() != null && liqProyecto.getLprEstado().equals(EnumEstadosProceso.PENDIENTE.getText()) &&
                sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_LIQUIDACION_PROYECTO)) {
            return true;
        }
        return false;
    }
    
    public void limpiar() {
        filtroLiqProyectos = new FiltroLiquidacionProyectos();
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

    public SofisComboG<EnumEstadosProceso> getComboEstados() {
        return comboEstados;
    }

    public void setComboEstados(SofisComboG<EnumEstadosProceso> comboEstados) {
        this.comboEstados = comboEstados;
    }

    public SgAfFuenteFinanciamiento getFuenteSeleccionadaInsert() {
        return fuenteSeleccionadaInsert;
    }

    public void setFuenteSeleccionadaInsert(SgAfFuenteFinanciamiento fuenteSeleccionadaInsert) {
        this.fuenteSeleccionadaInsert = fuenteSeleccionadaInsert;
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

    public FiltroLiquidacionProyectos getFiltroLiqProyectos() {
        return filtroLiqProyectos;
    }

    public void setFiltroLiqProyectos(FiltroLiquidacionProyectos filtroLiqProyectos) {
        this.filtroLiqProyectos = filtroLiqProyectos;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
    }

    public void setHistorial(List<RevHistorico> historial) {
        this.historial = historial;
    }

    public SgAfLiquidacionProyecto getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfLiquidacionProyecto entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public LazyLiquidacionProyectoDataModel getLiquidacionProyectoLazyModel() {
        return liquidacionProyectoLazyModel;
    }

    public void setLiquidacionProyectoLazyModel(LazyLiquidacionProyectoDataModel liquidacionProyectoLazyModel) {
        this.liquidacionProyectoLazyModel = liquidacionProyectoLazyModel;
    }

}
