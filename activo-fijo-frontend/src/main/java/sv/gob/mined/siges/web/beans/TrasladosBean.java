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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAfTraslado;
import sv.gob.mined.siges.web.dto.SolicitudTraslado;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosTraslado;
import sv.gob.mined.siges.web.enumerados.EnumEstados;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyTrasladosDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.TrasladosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.OperacionesGenerales;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class TrasladosBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(TrasladosBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;
    
    private SgAfTraslado entidadEnEdicion;
    private Integer paginado = 10;
    private Long totalResultados = 0L;
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
    private List<RevHistorico> historial = new ArrayList();
    private LazyTrasladosDataModel trasladosLazyModel;
    private SgAfEstadosBienes estadoExistente;
    private SgAfEstadosBienes estadoEnSolicitud;
    private SgAfEstadosBienes estadoRechazado;
    private FiltroCodiguera filtro;
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado;
    
    @Inject 
    private FiltroOrigenDestinoBean filtroOrigenDestinoBean;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private TrasladosRestClient trasladosRestClient;
    
    private SofisComboG<SgAfEstadosBienes> comboEstadosBienes;
    private SofisComboG<SgAfEstadosTraslado> comboTipoTraslado;
    
    private List<SgAfTraslado> solicitudesSeleccionadas= new ArrayList();
    
    @PostConstruct
    public void init() {
        try {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
            validarAcceso();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public void buscar() {
        try {
            filtroBienes.setIncluirCampos(new String[]{"traPk","traVersion","traCodigoTraslado","traFechaSolicitud","traFechaTraslado",
                "traEstadoFk.ebiNombre","traEstadoFk.ebiCodigo","traEstadoFk.ebiVersion","traUnidadDestino",
                "traTipoTrasladoFk.etrNombre","traTipoTrasladoFk.etrCodigo","traUnidadAdmOrigenFk.uadCodigo","traTipoTrasladoFk.etrVersion",
                "traUnidadAdmOrigenFk.uadPk","traUnidadAdmOrigenFk.uadCodigo","traUnidadAdmOrigenFk.uadNombre","traUnidadAdmOrigenFk.uadVersion",
                "traUnidadAdmDestinoFk.uadPk","traUnidadAdmDestinoFk.uadCodigo", "traUnidadAdmDestinoFk.uadNombre","traUnidadAdmDestinoFk.uadVersion",
                "traSedeOrigenFk.sedPk","traSedeOrigenFk.sedCodigo","traSedeOrigenFk.sedNombre","traSedeOrigenFk.sedTipo","traSedeOrigenFk.sedVersion",
                "traSedeDestinoFk.sedPk","traSedeDestinoFk.sedCodigo","traSedeDestinoFk.sedNombre","traSedeDestinoFk.sedTipo","traSedeDestinoFk.sedVersion"});
            filtroBienes.setMunicipioId(filtroOrigenDestinoBean != null && filtroOrigenDestinoBean.getComboMunicipiosOrigen() != null && filtroOrigenDestinoBean.getComboMunicipiosOrigen().getSelectedT() != null ? filtroOrigenDestinoBean.getComboMunicipiosOrigen().getSelectedT().getMunPk(): null);
            filtroBienes.setMunicipioDestinoId(filtroOrigenDestinoBean != null && filtroOrigenDestinoBean.getComboMunicipiosDestino() != null && filtroOrigenDestinoBean.getComboMunicipiosDestino().getSelectedT() != null ? filtroOrigenDestinoBean.getComboMunicipiosDestino().getSelectedT().getMunPk(): null);

            
            filtroBienes.setTipoUnidad(filtroOrigenDestinoBean != null ? filtroOrigenDestinoBean.getTipoUnidadOrigenSeleccionada() : null);
            filtroBienes.setTipoUnidadDestino(filtroOrigenDestinoBean.getTipoUnidadDestinoSeleccionada());
            
            filtroBienes.setEstadoId(comboEstadosBienes != null && comboEstadosBienes.getSelectedT() != null ? comboEstadosBienes.getSelectedT().getEbiPk() : null );
            
            filtroBienes.setTipoTraslado(comboTipoTraslado != null && comboTipoTraslado.getSelectedT() != null ? comboTipoTraslado.getSelectedT().getEtrPk() : null);
            
            
            
            if(filtroOrigenDestinoBean.getUnidadAFDestinoSeleccionada()!= null) {
                filtroBienes.setUnidadActivoFijoDestinoId(filtroOrigenDestinoBean.getUnidadAFDestinoSeleccionada().getUafPk());
                if(filtroOrigenDestinoBean.getUnidadAFDestinoSeleccionada().getUafDepartamento() != null 
                    && filtroOrigenDestinoBean.getUnidadAFDestinoSeleccionada().getUafDepartamento().getDepPk() != null) {
                    filtroBienes.setDepartamentoDestinoId(filtroOrigenDestinoBean.getUnidadAFDestinoSeleccionada().getUafDepartamento().getDepPk());
                }
            }
            
            if(sessionBean.getUnidadADDefecto() == null && sessionBean.getSedeDefecto() == null) { 
                if(filtroOrigenDestinoBean.getUnidadAFOrigenSeleccionada() != null) {
                    filtroBienes.setUnidadActivoFijoId(filtroOrigenDestinoBean.getUnidadAFOrigenSeleccionada().getUafPk());
                    if(filtroOrigenDestinoBean.getUnidadAFOrigenSeleccionada().getUafDepartamento() != null 
                        && filtroOrigenDestinoBean.getUnidadAFOrigenSeleccionada().getUafDepartamento().getDepPk() != null) {
                        filtroBienes.setDepartamentoId(filtroOrigenDestinoBean.getUnidadAFOrigenSeleccionada().getUafDepartamento().getDepPk());
                    }
                }
                if(filtroOrigenDestinoBean.getUnidadOrigenSeleccionada() != null) {
                    filtroBienes.setUnidadAdministrativaId(filtroOrigenDestinoBean.getUnidadOrigenSeleccionada().getUadPk());
                } else if(filtroOrigenDestinoBean.getSedeOrigenSeleccionada()!= null) {
                    filtroBienes.setUnidadAdministrativaId(filtroOrigenDestinoBean.getSedeOrigenSeleccionada().getSedPk());
                }
            }
            
            if(filtroOrigenDestinoBean.getUnidadDestinoSeleccionada()!= null) {
                filtroBienes.setUnidadAdministrativaDestinoId(filtroOrigenDestinoBean.getUnidadDestinoSeleccionada().getUadPk());
            } else if(filtroOrigenDestinoBean.getSedeDestinoSeleccionada()!= null) {
                filtroBienes.setUnidadAdministrativaDestinoId(filtroOrigenDestinoBean.getSedeDestinoSeleccionada().getSedPk());
            }
            
            totalResultados = trasladosRestClient.buscarTotal(filtroBienes);
            
            trasladosLazyModel = new LazyTrasladosDataModel(trasladosRestClient, filtroBienes, totalResultados, paginado);
            
           
        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarCombos() {
        try {
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"ebiCodigo"});
            filtro.setIncluirCampos(new String[]{"ebiNombre","ebiCodigo","ebiAplicaPara", "ebiVersion"});
            List<SgAfEstadosBienes> estadosBienes = catalogosRestClient.buscarEstadosBienes(filtro);
            
            List<SgAfEstadosBienes> estadosMostrar = new ArrayList();    
            
            if(estadosBienes != null && !estadosBienes.isEmpty()) {
                for(SgAfEstadosBienes estado : estadosBienes) { 
                    if(OperacionesGenerales.existeEnArray(estado.getEbiAplicaPara(), EnumEstados.TRASLADO.getText())) {
                        estadosMostrar.add(estado);
                        if(Constantes.CODIGO_ESTADO_SOLICITUD_TRASLADO.equals(estado.getEbiCodigo().trim())) {
                            estadoEnSolicitud = estado;
                        }
                    }
                    if(Constantes.CODIGO_ESTADO_EXISTENTE.equals(estado.getEbiCodigo().trim())) {
                        estadoExistente = estado;
                    } else if(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA.equals(estado.getEbiCodigo().trim())) {
                        estadoRechazado = estado;
                    }
                    
                } 
            }
 
            comboEstadosBienes = new SofisComboG(estadosMostrar, "ebiNombre");
            comboEstadosBienes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            
            filtro.setOrderBy(new String[]{"etrCodigo"});
            filtro.setIncluirCampos(new String[]{"etrNombre","etrCodigo","etrVersion"});
            List<SgAfEstadosTraslado> estadosTraslado = catalogosRestClient.buscarEstadosTraslado(filtro);
            comboTipoTraslado = new SofisComboG(estadosTraslado, "etrNombre");
            comboTipoTraslado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void limpiar() {
        filtroBienes = new FiltroBienesDepreciables();
        comboEstadosBienes.setSelected(-1);
        comboTipoTraslado.setSelected(-1);
        filtroOrigenDestinoBean.limpiar();
        trasladosLazyModel = null;
        totalResultados = null;
    }
    public void actualizar(SgAfTraslado var) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = (SgAfTraslado) SerializationUtils.clone(var);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eliminar() {
        try {
            SolicitudTraslado solicitud = new SolicitudTraslado();
            solicitud.setTraslado(entidadEnEdicion);
            solicitud.setEstado(estadoExistente);
            trasladosRestClient.eliminar(solicitud);
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
    
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_TRASLADO_BIENES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String cargarHistorial(Long id) {
        try {
            historial = trasladosRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public Boolean solicitudTrasladoPuedeEliminarse(SgAfTraslado traslado) {
       //Solo los dueños de las solicitudes pueden eliminarlas
        if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_TODAS_SOLICITUD_PROCESO_TRASLADO_BIENES)) { 
            if(traslado != null && traslado.getTraEstadoFk() != null && traslado.getTraEstadoFk().getEbiCodigo() != null 
                    && ((estadoEnSolicitud != null && estadoEnSolicitud.getEbiCodigo() != null && traslado.getTraEstadoFk().getEbiCodigo().equals(estadoEnSolicitud.getEbiCodigo())))
                    || (estadoRechazado != null && estadoRechazado.getEbiCodigo() != null && traslado.getTraEstadoFk().getEbiCodigo().equals(estadoRechazado.getEbiCodigo()))) {
                return Boolean.TRUE;
            }
        } else if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_SOLICITUD_PROCESO_TRASLADO_BIENES)) { 
            if(traslado != null && traslado.getTraEstadoFk() != null && traslado.getTraEstadoFk().getEbiCodigo() != null 
                    && ((estadoEnSolicitud != null && estadoEnSolicitud.getEbiCodigo() != null && traslado.getTraEstadoFk().getEbiCodigo().equals(estadoEnSolicitud.getEbiCodigo())))
                    || (estadoRechazado != null && estadoRechazado.getEbiCodigo() != null && traslado.getTraEstadoFk().getEbiCodigo().equals(estadoRechazado.getEbiCodigo()))) {
                if((sessionBean.getUnidadADDefecto() != null && traslado.getTraUnidadAdmOrigenFk() != null && sessionBean.getUnidadADDefecto().getUadPk().equals(traslado.getTraUnidadAdmOrigenFk().getUadPk()))
                    || (sessionBean.getSedeDefecto() != null && traslado.getTraSedeOrigenFk() != null && sessionBean.getSedeDefecto().getSedPk().equals(traslado.getTraSedeOrigenFk().getSedPk()))) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public void verPanelAvanzado() {
        if (panelAvanzado) {
            panelAvanzado = false;
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
        } else {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
            panelAvanzado = true;
            if(filtroOrigenDestinoBean.getCargarDefault() != null && !filtroOrigenDestinoBean.getCargarDefault()) {
                filtroOrigenDestinoBean.cargarUnidadesSinPermiso();
                filtroOrigenDestinoBean.setCargarDefault(Boolean.TRUE);
            }
        }
    }
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SgAfTraslado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfTraslado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public LazyTrasladosDataModel getTrasladosLazyModel() {
        return trasladosLazyModel;
    }

    public void setTrasladosLazyModel(LazyTrasladosDataModel trasladosLazyModel) {
        this.trasladosLazyModel = trasladosLazyModel;
    }

    public SgAfEstadosBienes getEstadoExistente() {
        return estadoExistente;
    }

    public void setEstadoExistente(SgAfEstadosBienes estadoExistente) {
        this.estadoExistente = estadoExistente;
    }

    public SgAfEstadosBienes getEstadoEnSolicitud() {
        return estadoEnSolicitud;
    }

    public void setEstadoEnSolicitud(SgAfEstadosBienes estadoEnSolicitud) {
        this.estadoEnSolicitud = estadoEnSolicitud;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public FiltroOrigenDestinoBean getFiltroOrigenDestinoBean() {
        return filtroOrigenDestinoBean;
    }

    public void setFiltroOrigenDestinoBean(FiltroOrigenDestinoBean filtroOrigenDestinoBean) {
        this.filtroOrigenDestinoBean = filtroOrigenDestinoBean;
    }

    public SofisComboG<SgAfEstadosBienes> getComboEstadosBienes() {
        return comboEstadosBienes;
    }

    public void setComboEstadosBienes(SofisComboG<SgAfEstadosBienes> comboEstadosBienes) {
        this.comboEstadosBienes = comboEstadosBienes;
    }

    public List<SgAfTraslado> getSolicitudesSeleccionadas() {
        return solicitudesSeleccionadas;
    }

    public void setSolicitudesSeleccionadas(List<SgAfTraslado> solicitudesSeleccionadas) {
        this.solicitudesSeleccionadas = solicitudesSeleccionadas;
    }

    public SofisComboG<SgAfEstadosTraslado> getComboTipoTraslado() {
        return comboTipoTraslado;
    }

    public void setComboTipoTraslado(SofisComboG<SgAfEstadosTraslado> comboTipoTraslado) {
        this.comboTipoTraslado = comboTipoTraslado;
    }

    public Boolean getPanelAvanzado() {
        return panelAvanzado;
    }

    public void setPanelAvanzado(Boolean panelAvanzado) {
        this.panelAvanzado = panelAvanzado;
    }

    public String getTxtFiltroAvanzado() {
        return txtFiltroAvanzado;
    }

    public void setTxtFiltroAvanzado(String txtFiltroAvanzado) {
        this.txtFiltroAvanzado = txtFiltroAvanzado;
    }

    public SgAfEstadosBienes getEstadoRechazado() {
        return estadoRechazado;
    }

    public void setEstadoRechazado(SgAfEstadosBienes estadoRechazado) {
        this.estadoRechazado = estadoRechazado;
    }
    
}
