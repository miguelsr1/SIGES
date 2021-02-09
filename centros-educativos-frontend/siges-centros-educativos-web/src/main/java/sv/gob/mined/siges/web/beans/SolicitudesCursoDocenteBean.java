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
import sv.gob.mined.siges.web.dto.SgSolicitudCursoDocente;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudCursoDocente;
import sv.gob.mined.siges.web.lazymodels.LazySolicitudCursoDocenteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SolicitudCursoDocenteRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCursoDocente;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudCurso;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.restclient.CursoDocenteRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class SolicitudesCursoDocenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolicitudesCursoDocenteBean.class.getName());

    @Inject
    private SolicitudCursoDocenteRestClient restClient;
    
    @Inject
    private CursoDocenteRestClient restCurso;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "id")
    private Long idCurso;

    private FiltroSolicitudCursoDocente filtro = new FiltroSolicitudCursoDocente();
    private SgSolicitudCursoDocente entidadEnEdicion = new SgSolicitudCursoDocente();
    private SgCursoDocente entidadCurso = new SgCursoDocente();
    private List<RevHistorico> historialSolicitudCursoDocente = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazySolicitudCursoDocenteDataModel solicitudCursoDocenteLazyModel;
    private List<SgSolicitudCursoDocente> listaSolicitudes = new ArrayList();
    private Long cantidadAprobadas = 0L;

    public SolicitudesCursoDocenteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            if(idCurso!=null && idCurso>0){
                actualizar(restCurso.obtenerPorId(idCurso));
            }
            cargarCombos();
            buscar();
            obtenerAprobadas();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void forceInit(){
        //Utilizado para forzar init de SolicitudCursoDocenteesBean antes que FiltrosSeccionBean
    }

    public void validarAcceso() {
        if (!(sessionBean.getOperaciones().contains(ConstantesOperaciones.APROBAR_SOLICITUD_CURSO_DOCENTE) ||  
                sessionBean.getOperaciones().contains(ConstantesOperaciones.RECHAZAR_SOLICITUD_CURSO_DOCENTE))) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setScdCurso(idCurso);
            filtro.setIncluirCampos(new String[]{
                "scdVersion",
                "scdPersonal.psePersona.perPrimerNombre",
                "scdPersonal.psePersona.perSegundoNombre",
                "scdPersonal.psePersona.perTercerNombre",
                "scdPersonal.psePersona.perPrimerApellido",
                "scdPersonal.psePersona.perSegundoApellido",
                "scdPersonal.psePersona.perTercerApellido",
                "scdEstado"
            });

            totalResultados = restClient.buscarTotal(filtro);
            solicitudCursoDocenteLazyModel = new LazySolicitudCursoDocenteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroSolicitudCursoDocente();
        buscar();
        obtenerAprobadas();
    }
    
    public void obtenerAprobadas(){
        try {
            FiltroSolicitudCursoDocente fs = new FiltroSolicitudCursoDocente();
            fs.setScdCurso(idCurso);
            fs.setScdEstado(EnumEstadoSolicitudCurso.APROBADA);
            fs.setIncluirCampos(new String[]{
                "scdVersion",
                "scdEstado"
            });

            cantidadAprobadas = restClient.buscarTotal(fs);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgSolicitudCursoDocente();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgCursoDocente var) {
        entidadCurso = (SgCursoDocente) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }
    
    public void seleccionarSolicitud(SgSolicitudCursoDocente var){
        try{
            entidadEnEdicion = restClient.obtenerPorId(var.getScdPk());
            JSFUtils.limpiarMensajesError();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void aprobar() {
        try {
            restClient.aprobar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            obtenerAprobadas();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void rechazar() {
        try {
            restClient.rechazar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            obtenerAprobadas();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } 
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialSolicitudCursoDocente = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroSolicitudCursoDocente getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroSolicitudCursoDocente filtro) {
        this.filtro = filtro;
    }

    public SgSolicitudCursoDocente getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgSolicitudCursoDocente entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialSolicitudCursoDocente() {
        return historialSolicitudCursoDocente;
    }

    public void setHistorialSolicitudCursoDocente(List<RevHistorico> historialSolicitudCursoDocente) {
        this.historialSolicitudCursoDocente = historialSolicitudCursoDocente;
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

    public LazySolicitudCursoDocenteDataModel getSolicitudCursoDocenteLazyModel() {
        return solicitudCursoDocenteLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazySolicitudCursoDocenteDataModel solicitudCursoDocenteLazyModel) {
        this.solicitudCursoDocenteLazyModel = solicitudCursoDocenteLazyModel;
    }

    public SolicitudCursoDocenteRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(SolicitudCursoDocenteRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SgCursoDocente getEntidadCurso() {
        return entidadCurso;
    }

    public void setEntidadCurso(SgCursoDocente entidadCurso) {
        this.entidadCurso = entidadCurso;
    }

    public Long getCantidadAprobadas() {
        return cantidadAprobadas;
    }

    public void setCantidadAprobadas(Long cantidadAprobadas) {
        this.cantidadAprobadas = cantidadAprobadas;
    }

}
