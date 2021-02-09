/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PromocionesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PromocionesBean.class.getName());

    @Inject
    private CalificacionRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    private FiltroCalificacion filtro = new FiltroCalificacion();
    private SgCalificacionCE entidadEnEdicion = new SgCalificacionCE();
    private List<RevHistorico> historialCalificacionEstudiante = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalificacionEstudianteDataModel calificacionEstudianteLazyModel;

    public PromocionesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            inicializarFiltrosSeccion();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void forceInit(){
        //Utilizado para forzar init de CalificacionesBean antes que FiltrosSeccionBean
    }

    public void inicializarFiltrosSeccion() {
        this.filtrosSeccion.setFiltro(this.filtro);
        this.filtrosSeccion.cargarCombos();
        this.filtrosSeccion.seleccionarUltimoAnio();
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PROMOCIONES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            //No devolver NOTIN
            filtro.setCalTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                EnumTiposPeriodosCalificaciones.GRA}));
            filtro.setIncluirCampos(new String[]{
                "calVersion",
                "calSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "calSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "calSeccion.secServicioEducativo.sduSede.sedNombre",
                "calSeccion.secServicioEducativo.sduSede.sedCodigo", 
                "calSeccion.secServicioEducativo.sduSede.sedTipo",
                "calSeccion.secServicioEducativo.sduGrado.graNombre",
                "calSeccion.secServicioEducativo.sduOpcion.opcNombre",
                "calSeccion.secNombre",
                "calEstadoProcesamientoPromocion",
                "calUltModFecha"
            });
            filtro.setCalTieneEstudiantePendiente(Boolean.TRUE);

            totalResultados = restClient.buscarTotal(filtro);
            calificacionEstudianteLazyModel = new LazyCalificacionEstudianteDataModel(restClient, filtro, totalResultados, paginado);
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
    
    public String obtenerEstado(SgCalificacionCE cal){
        String retorno="";
        if(cal.getCalEstadoProcesamientoPromocion()!=null){
            retorno =cal.getCalEstadoProcesamientoPromocion().getText();
            if(cal.getCalEstadoProcesamientoPromocion().equals(EnumEstadoProcesamientoCalificacionPromocion.PROCESADO)){
                retorno=cal.getCalTieneEstudiantesPendientes()?"Procesado con estudiantes pendientes":"Procesado";
            }

           
        }else{
            retorno=cal.getCalTieneEstudiantesPendientes()?"Procesado con estudiantes pendientes":"Procesado";
        }
        return retorno;
    }
    public void procesarArchivo() {
        try {
            restClient.ejecutarProcesamientoPromociones();
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ARCHIVOS_PENDIENTES_PROCESADOS), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroCalificacion();
        this.filtrosSeccion.limpiarCombos();
      
    }

    public void agregar() {
        entidadEnEdicion = new SgCalificacionCE();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgCalificacionCE var) {
        entidadEnEdicion = (SgCalificacionCE) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void historial(Long id) {
        try {
            historialCalificacionEstudiante = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCalificacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCalificacion filtro) {
        this.filtro = filtro;
    }

    public SgCalificacionCE getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalificacionCE entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCalificacionEstudiante() {
        return historialCalificacionEstudiante;
    }

    public void setHistorialCalificacionEstudiante(List<RevHistorico> historialCalificacionEstudiante) {
        this.historialCalificacionEstudiante = historialCalificacionEstudiante;
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

    public LazyCalificacionEstudianteDataModel getCalificacionEstudianteLazyModel() {
        return calificacionEstudianteLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalificacionEstudianteDataModel calificacionEstudianteLazyModel) {
        this.calificacionEstudianteLazyModel = calificacionEstudianteLazyModel;
    }

    public CalificacionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(CalificacionRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }
}
