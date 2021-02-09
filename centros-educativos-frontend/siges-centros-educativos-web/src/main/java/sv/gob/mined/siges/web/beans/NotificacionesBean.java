/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgNotificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNotificacion;
import sv.gob.mined.siges.web.lazymodels.LazyNotificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.NotificacionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumTipoNotificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class NotificacionesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(NotificacionesBean.class.getName());

    @Inject
    private NotificacionRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SeleccionarSeccionBean seccion;

    @Inject
    private EstudianteRestClient restEstudiante;

    private SgNotificacion entidadEnEdicion = new SgNotificacion();
    private FiltroNotificacion filtro = new FiltroNotificacion();
    private LazyNotificacionDataModel notificacionLazyModel;
    private List<RevHistorico> historialNotificacion = new ArrayList();
    private SofisComboG<EnumTipoNotificacion> comboTipoNotificacion;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private SgSede sedeSeleccionada;
    private SgSeccion seccionSeleccionada;
    private Long nieIngresado;

    public NotificacionesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_NOTIFICACION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setIncluirCampos(new String[]{"nfsFecha","nfsUsuario.usuNombre", "nfsTipo", 
                "nfsSede.sedTipo",
                "nfsSede.sedNombre",
                "nfsSede.sedCodigo",
                "nfsSeccion.secNombre",
                "nfsSeccion.secJornadaLaboral.jlaNombre",
                "nfsEstudiante.estPersona.perNie",
                "nfsUltModFecha",
                "nfsUltModUsuario"});
            totalResultados = restClient.buscarTotal(filtro);
            notificacionLazyModel = new LazyNotificacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public void seleccionarSeccion(SgSeccion var){
        this.seccionSeleccionada = var;
    }

    public void cargarCombos() {
        List<EnumTipoNotificacion> tipoNotif = new ArrayList(Arrays.asList(EnumTipoNotificacion.values()));
        comboTipoNotificacion = new SofisComboG(tipoNotif, "text");
        comboTipoNotificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {
    }

    public String limpiar() {
        comboTipoNotificacion.setSelected(-1);
        filtro = new FiltroNotificacion();
        buscar();
        return null;
    }

    public void agregar() {
        limpiarCombos();
        comboTipoNotificacion.setSelected(-1);
        entidadEnEdicion = new SgNotificacion();
        this.seccion.limpiarCombos();
        sedeSeleccionada = null;
    }

    public void actualizar(SgNotificacion var) {
        try{
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getNfsPk());
            comboTipoNotificacion.setSelectedT(entidadEnEdicion.getNfsTipo());
            sedeSeleccionada = entidadEnEdicion.getNfsSede();
            seccionSeleccionada = entidadEnEdicion.getNfsSeccion();
            nieIngresado = entidadEnEdicion.getNfsEstudiante()!=null?entidadEnEdicion.getNfsEstudiante().getEstPersona().getPerNie():null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if(entidadEnEdicion.getNfsPk()==null){
                entidadEnEdicion.setNfsUsuario(sessionBean.getEntidadUsuario());
                entidadEnEdicion.setNfsFecha(LocalDate.now());
            }
            entidadEnEdicion.setNfsTipo(comboTipoNotificacion.getSelectedT());
            switch(entidadEnEdicion.getNfsTipo()){
                case CENTRO:
                    entidadEnEdicion.setNfsSede(sedeSeleccionada);
                    entidadEnEdicion.setNfsEstudiante(null);
                    entidadEnEdicion.setNfsSeccion(null);
                    break;
                case ESTUDIANTE:
                    FiltroEstudiante fe = new FiltroEstudiante();
                    fe.setNie(nieIngresado);
                    List<SgEstudiante> lest = restEstudiante.buscar(fe);
                    SgEstudiante estudianteM = lest.size()==1?lest.get(0):null;
                    if(estudianteM==null){
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRO_ESTUDIANTE), "");
                        return;
                    }
                    entidadEnEdicion.setNfsEstudiante(estudianteM);
                    entidadEnEdicion.setNfsSede(null);
                    entidadEnEdicion.setNfsSeccion(null);
                    break;
                case SECCION:
                    entidadEnEdicion.setNfsSeccion(seccionSeleccionada);
                    entidadEnEdicion.setNfsSede(null);
                    entidadEnEdicion.setNfsEstudiante(null);
                    break;
            }
            
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
            restClient.eliminar(entidadEnEdicion.getNfsPk());
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

    public String historial(Long id) {
        try {
            historialNotificacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public NotificacionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(NotificacionRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroNotificacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroNotificacion filtro) {
        this.filtro = filtro;
    }

    public LazyNotificacionDataModel getNotificacionLazyModel() {
        return notificacionLazyModel;
    }

    public void setNotificacionLazyModel(LazyNotificacionDataModel notificacionLazyModel) {
        this.notificacionLazyModel = notificacionLazyModel;
    }

    public List<RevHistorico> getHistorialNotificacion() {
        return historialNotificacion;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public SgNotificacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgNotificacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SofisComboG<EnumTipoNotificacion> getComboTipoNotificacion() {
        return comboTipoNotificacion;
    }

    public void setComboTipoNotificacion(SofisComboG<EnumTipoNotificacion> comboTipoNotificacion) {
        this.comboTipoNotificacion = comboTipoNotificacion;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SgSeccion getSeccionSeleccionada() {
        return seccionSeleccionada;
    }

    public void setSeccionSeleccionada(SgSeccion seccionSeleccionada) {
        this.seccionSeleccionada = seccionSeleccionada;
    }

    public Long getNieIngresado() {
        return nieIngresado;
    }

    public void setNieIngresado(Long nieIngresado) {
        this.nieIngresado = nieIngresado;
    }

}
