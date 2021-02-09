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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAsignacionPerfil;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsignacionPerfil;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyAsignacionPerfilDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsignacionPerfilRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AsignacionesPerfilBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AsignacionesPerfilBean.class.getName());

    @Inject
    private AsignacionPerfilRestClient restClient;
    
     @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroAsignacionPerfil filtro = new FiltroAsignacionPerfil();
    private SgAsignacionPerfil entidadEnEdicion = new SgAsignacionPerfil();
    private List<RevHistorico> historialAsignacionPerfil = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyAsignacionPerfilDataModel asignacionPerfilLazyModel;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SgSede sedeActividadSeleccionadaBusqueda;

    public AsignacionesPerfilBean() {
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
            
            filtro.setApeDepartamentoFk(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setApeSedeFk(sedeActividadSeleccionadaBusqueda != null ? sedeActividadSeleccionadaBusqueda.getSedPk() : null);
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"apeSedeFk.sedCodigo","apeSedeFk.sedNombre","apeSedeFk.sedTipo"});
            totalResultados = restClient.buscarTotal(filtro);
            asignacionPerfilLazyModel = new LazyAsignacionPerfilDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogoRestClient.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));


        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }



    private void limpiarCombos() {
        comboDepartamento.setSelected(-1);
    }

  

    public void limpiar() {
        sedeActividadSeleccionadaBusqueda = null;
        limpiarCombos();
        filtro = new FiltroAsignacionPerfil();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAsignacionPerfil();
    }

    public void actualizar(SgAsignacionPerfil var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgAsignacionPerfil) SerializationUtils.clone(var);
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
            restClient.eliminar(entidadEnEdicion.getApePk());
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
            historialAsignacionPerfil = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroAsignacionPerfil getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAsignacionPerfil filtro) {
        this.filtro = filtro;
    }

    public SgAsignacionPerfil getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAsignacionPerfil entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialAsignacionPerfil() {
        return historialAsignacionPerfil;
    }

    public void setHistorialAsignacionPerfil(List<RevHistorico> historialAsignacionPerfil) {
        this.historialAsignacionPerfil = historialAsignacionPerfil;
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

    public LazyAsignacionPerfilDataModel getAsignacionPerfilLazyModel() {
        return asignacionPerfilLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyAsignacionPerfilDataModel asignacionPerfilLazyModel) {
        this.asignacionPerfilLazyModel = asignacionPerfilLazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SgSede getSedeActividadSeleccionadaBusqueda() {
        return sedeActividadSeleccionadaBusqueda;
    }

    public void setSedeActividadSeleccionadaBusqueda(SgSede sedeActividadSeleccionadaBusqueda) {
        this.sedeActividadSeleccionadaBusqueda = sedeActividadSeleccionadaBusqueda;
    }

}
