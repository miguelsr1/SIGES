/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TipoCalendarioBean implements Serializable {

//    private static final Logger LOGGER = Logger.getLogger(TipoCalendarioBean.class.getName());
//
//    @Inject
//    private TipoCalendarioRestClient restClient;
//
//    private FiltroCodiguera filtro = new FiltroCodiguera();
//    private SgTipoCalendario entidadEnEdicion = new SgTipoCalendario();
//    private List<RevHistorico> historialTipoCalendario = new ArrayList();
//    private Integer paginado = 10;
//    private Long totalResultados;
//    private LazyTipoCalendarioDataModel tipoCalendarioLazyModel;
//
//    public TipoCalendarioBean() {
//    }
//
//    @PostConstruct
//    public void init() {
//        try {
//            cargarCombos();
//            buscar();
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void buscar() {
//        try {
//            filtro.setFirst(new Long(0));
//            totalResultados = restClient.buscarTotal(filtro);
//            tipoCalendarioLazyModel = new LazyTipoCalendarioDataModel(restClient, filtro, totalResultados, paginado);
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
//        }
//    }
//
//    public void cargarCombos() {
//
//    }
//
//    private void limpiarCombos() {
//
//    }
//
//    public void limpiar() {
//        filtro = new FiltroCodiguera();
//        buscar();
//    }
//
//    public void agregar() {
//        limpiarCombos();
//        entidadEnEdicion = new SgTipoCalendario();
//    }
//
//    public void actualizar(SgTipoCalendario var) {
//        limpiarCombos();
//        entidadEnEdicion = (SgTipoCalendario) SerializationUtils.clone(var);
//    }
//
//    public void guardar() {
//        try {
//            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
//            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
//            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
//            buscar();
//        } catch (BusinessException be) {
//            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
//        }
//    }
//
//    public void eliminar() {
//        try {
//            restClient.eliminar(entidadEnEdicion.getTcaPk());
//            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
//            buscar();
//            entidadEnEdicion = null;
//        } catch (BusinessException be) {
//            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
//        }
//    }
//
//    public void historial(Long id) {
//        try {
//            historialTipoCalendario = restClient.obtenerHistorialPorId(id);
//        } catch (Exception ex) {
//            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
//            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
//        }
//    }
//
//    public FiltroCodiguera getFiltro() {
//        return filtro;
//    }
//
//    public void setFiltro(FiltroCodiguera filtro) {
//        this.filtro = filtro;
//    }
//
//    public SgTipoCalendario getEntidadEnEdicion() {
//        return entidadEnEdicion;
//    }
//
//    public void setEntidadEnEdicion(SgTipoCalendario entidadEnEdicion) {
//        this.entidadEnEdicion = entidadEnEdicion;
//    }
//
//    public List<RevHistorico> getHistorialTipoCalendario() {
//        return historialTipoCalendario;
//    }
//
//    public Integer getPaginado() {
//        return paginado;
//    }
//
//    public void setPaginado(Integer paginado) {
//        this.paginado = paginado;
//    }
//
//    public Long getTotalResultados() {
//        return totalResultados;
//    }
//
//    public void setTotalResultados(Long totalResultados) {
//        this.totalResultados = totalResultados;
//    }
//
//    public LazyTipoCalendarioDataModel getTipoCalendarioLazyModel() {
//        return tipoCalendarioLazyModel;
//    }
//
//    public void setTiposCalendarioLazyModel(LazyTipoCalendarioDataModel tipoCalendarioLazyModel) {
//        this.tipoCalendarioLazyModel = tipoCalendarioLazyModel;
//    }
}
