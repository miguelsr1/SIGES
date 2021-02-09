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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgReporteDirector;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReporteDirector;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyReporteDirectorDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ReporteDirectorRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ReporteDirectorBean implements Serializable {

    @Inject
    @Param(name = "id")
    private Long reporteId;

    private static final Logger LOGGER = Logger.getLogger(ReporteDirectorBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private ReporteDirectorRestClient restClient;

    private FiltroReporteDirector filtro = new FiltroReporteDirector();
    private SgReporteDirector entidadEnEdicion = new SgReporteDirector();
    private List<SgReporteDirector> historialReporteDirector = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyReporteDirectorDataModel reporteDirectorLazyModel;
    private SgSede sedSeleccionado;
    private Boolean soloLectura = Boolean.FALSE;

    public ReporteDirectorBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            if (reporteId != null && reporteId > 0) {
                entidadEnEdicion = restClient.obtenerPorId(reporteId);
                soloLectura = Boolean.TRUE;
            }
            cargarCombos();
            seleccionarSedeAsignada();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REVISION_DATOS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void seleccionarSedeAsignada() {
        List<SgSede> sedes = completeSede("");
        if (sedes != null) {
            if (sedes.size() == 1) {
                sedSeleccionado = sedes.get(0);
                seleccionarSede();
            }
        }
    }

    public void seleccionarSede() {
        try {
            entidadEnEdicion = new SgReporteDirector();
            entidadEnEdicion.setRdiSede(sedSeleccionado);
            FiltroReporteDirector frd = new FiltroReporteDirector();
            frd.setRidSede(sedSeleccionado.getSedPk());
            List<SgReporteDirector> listRepDir = restClient.buscar(frd);
            if (listRepDir.size() > 0) {
                entidadEnEdicion = listRepDir.get(0);
            }
            soloLectura = Boolean.FALSE;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean renderizarGuardar() {
        return !this.soloLectura
                && sedSeleccionado != null
                && ((this.entidadEnEdicion.getRdiPk() == null && sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_REPORTE_DIRECTOR))
                || (this.entidadEnEdicion.getRdiPk() != null && sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_REPORTE_DIRECTOR)));
    }

    public void seleccionadoObservacionPersonal() {
        if (!entidadEnEdicion.getRdiConObservacionesPersonal()) {
            entidadEnEdicion.setRdiObservacionPersonal(null);
        }
    }

    public void seleccionadoObservacionSede() {
        if (!entidadEnEdicion.getRdiConObservacionesSede()) {
            entidadEnEdicion.setRdiObservacionSede(null);
        }
    }

    public void seleccionadoObservacionEstudiante() {
        if (!entidadEnEdicion.getRdiConObservacionesEstudiante()) {
            entidadEnEdicion.setRdiObservacionEstudiante(null);
        }
    }

    public void cargarCombos() {
        
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroReporteDirector();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgReporteDirector();
    }

    public void actualizar(SgReporteDirector var) {
        limpiarCombos();
        entidadEnEdicion = (SgReporteDirector) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void historial(Long id) {
        try {
            historialReporteDirector = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroReporteDirector getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroReporteDirector filtro) {
        this.filtro = filtro;
    }

    public SgReporteDirector getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgReporteDirector entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgReporteDirector> getHistorialReporteDirector() {
        return historialReporteDirector;
    }

    public void setHistorialReporteDirector(List<SgReporteDirector> historialReporteDirector) {
        this.historialReporteDirector = historialReporteDirector;
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

    public LazyReporteDirectorDataModel getReporteDirectorLazyModel() {
        return reporteDirectorLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyReporteDirectorDataModel reporteDirectorLazyModel) {
        this.reporteDirectorLazyModel = reporteDirectorLazyModel;
    }

    public SgSede getSedSeleccionado() {
        return sedSeleccionado;
    }

    public void setSedSeleccionado(SgSede sedSeleccionado) {
        this.sedSeleccionado = sedSeleccionado;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

}
