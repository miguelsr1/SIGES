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
import sv.gob.mined.siges.web.dto.SgCategoriaViolencia;
import sv.gob.mined.siges.web.dto.SgTipoManifestacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTiposManifestacionViolencia;
import sv.gob.mined.siges.web.lazymodels.LazyTipoManifestacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.rest.CategoriaViolenciaRestClient;
import sv.gob.mined.siges.web.restclient.TipoManifestacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TipoManifestacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TipoManifestacionBean.class.getName());

    @Inject
    private TipoManifestacionRestClient restClient;
    
    @Inject
    private CategoriaViolenciaRestClient cateogriaRestClient;

    private FiltroTiposManifestacionViolencia filtro = new FiltroTiposManifestacionViolencia();
    private SgTipoManifestacion entidadEnEdicion = new SgTipoManifestacion();
    private List<SgTipoManifestacion> historialTipoManifestacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTipoManifestacionDataModel tipoManifestacionLazyModel;
    private SofisComboG<SgCategoriaViolencia> comboCategoriaViolencia;
    private SgCategoriaViolencia manifestacionSeleccionada;
    
    public TipoManifestacionBean() {
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
            if (comboCategoriaViolencia.getSelectedT() != null) {
                filtro.setTmaCategoriaViolenciaPk(comboCategoriaViolencia.getSelectedT().getCavPk());
            }
            totalResultados = restClient.buscarTotal(filtro);
            tipoManifestacionLazyModel = new LazyTipoManifestacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"cavNombre"});
            fc.setAscending(new boolean[]{true});
            comboCategoriaViolencia = new SofisComboG(cateogriaRestClient.buscar(fc), "cavNombre");
            comboCategoriaViolencia.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
         } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroTiposManifestacionViolencia();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgTipoManifestacion();
        manifestacionSeleccionada = null;
    }

    public void actualizar(SgTipoManifestacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgTipoManifestacion) SerializationUtils.clone(var);
        manifestacionSeleccionada = entidadEnEdicion.getTmaCategoriaViolencia();
       // comboCategoriaViolencia.setSelectedT(entidadEnEdicion.getTmaCategoriaViolencia());
    }

    public void guardar() {
        try {
            //entidadEnEdicion.setTmaCategoriaViolencia(comboCategoriaViolencia.getSelectedT());
            entidadEnEdicion.setTmaCategoriaViolencia(manifestacionSeleccionada);
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
            restClient.eliminar(entidadEnEdicion.getTmaPk());
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
            historialTipoManifestacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgCategoriaViolencia> completeCategoriaViolencia(String query) {
        try {
            FiltroCodiguera fil = new FiltroCodiguera();
            fil.setNombre(query);
            fil.setMaxResults(10L);
            return cateogriaRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public FiltroTiposManifestacionViolencia getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroTiposManifestacionViolencia filtro) {
        this.filtro = filtro;
    }

    public SgTipoManifestacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTipoManifestacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgTipoManifestacion> getHistorialTipoManifestacion() {
        return historialTipoManifestacion;
    }

    public void setHistorialTipoManifestacion(List<SgTipoManifestacion> historialTipoManifestacion) {
        this.historialTipoManifestacion = historialTipoManifestacion;
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

    public LazyTipoManifestacionDataModel getTipoManifestacionLazyModel() {
        return tipoManifestacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTipoManifestacionDataModel tipoManifestacionLazyModel) {
        this.tipoManifestacionLazyModel = tipoManifestacionLazyModel;
    }

    public TipoManifestacionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(TipoManifestacionRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<SgCategoriaViolencia> getComboCategoriaViolencia() {
        return comboCategoriaViolencia;
    }

    public void setComboCategoriaViolencia(SofisComboG<SgCategoriaViolencia> comboCategoriaViolencia) {
        this.comboCategoriaViolencia = comboCategoriaViolencia;
    }

    public SgCategoriaViolencia getManifestacionSeleccionada() {
        return manifestacionSeleccionada;
    }

    public void setManifestacionSeleccionada(SgCategoriaViolencia manifestacionSeleccionada) {
        this.manifestacionSeleccionada = manifestacionSeleccionada;
    }

}
