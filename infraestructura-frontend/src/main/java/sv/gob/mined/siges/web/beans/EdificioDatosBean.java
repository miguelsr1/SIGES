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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgRelInmuebleUnidadResp;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoConstruccion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtros.FiltroRelInmuebleUnidadResp;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEdificio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroInmueblesSedes;
import sv.gob.mined.siges.web.lazymodels.LazyEdificioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleUnidadRespRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EdificioDatosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EdificioDatosBean.class.getName());

    @Inject
    private EdificioRestClient restClient;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private InmueblesSedesRestClient inmueblesSedesRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private EdificioBean edificioBean;

    @Inject
    private RelInmuebleUnidadRespRestClient relInmuebleUnidadRespRestClient;

    private FiltroEdificio filtro = new FiltroEdificio();
    private SgEdificio entidadEnEdicion = new SgEdificio();
    private List<RevHistorico> historialEdificio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEdificioDataModel edificioLazyModel;
    private Boolean esSedePorDefecto;
    private SofisComboG<SgTipoConstruccion> comboTipoConstruccion;
    private SgInmueblesSedes inmuebleSeleccionado;
    private List<SgRelInmuebleUnidadResp> relInmuebleUnidadRespList;
    private List<SgRelInmuebleUnidadResp> relInmuebleUnidadRespCompletaList;

    private String securityOperation;
    private Boolean soloLectura = Boolean.FALSE;

    public EdificioDatosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgEdificio) request.getAttribute("edificio");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
            cargarEntidad();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public List<SgRelInmuebleUnidadResp> completeRelInmuebleUnidadResp(String query) {
     
        return relInmuebleUnidadRespList.stream().filter(c -> c.getNombreElemento().toLowerCase().startsWith(query.toLowerCase()) && !entidadEnEdicion.getEdiRelInmuebleUnidadResp().contains(c)).collect(Collectors.toList());

    }

    public void cargarCombos() {
        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"tcoNombre"});
            fc.setIncluirCampos(new String[]{"tcoNombre", "tcoVersion"});
            List<SgTipoConstruccion> departamentos = catalogosRestClient.buscarTipoConstruccion(fc);
            comboTipoConstruccion = new SofisComboG(departamentos, "tcoNombre");
            comboTipoConstruccion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizarLista(){
        entidadEnEdicion.getEdiRelInmuebleUnidadResp().size();
       
    }

    public void cargarEntidad() {
        relInmuebleUnidadRespList = new ArrayList();
        if (entidadEnEdicion.getEdiTipoConstruccion() != null) {
            comboTipoConstruccion.setSelectedT(entidadEnEdicion.getEdiTipoConstruccion());
        }
        if (entidadEnEdicion.getEdiInmuebleSedeFk() != null) {
            inmuebleSeleccionado = entidadEnEdicion.getEdiInmuebleSedeFk();
        }
        if (entidadEnEdicion.getEdiRelInmuebleUnidadResp() != null) {
            relInmuebleUnidadRespList = entidadEnEdicion.getEdiRelInmuebleUnidadResp();
        }
        if(BooleanUtils.isTrue(entidadEnEdicion.getEdiInmuebleSedeFk().getTisExisteOtrasSedesUnidadAdmi())){
            buscarListRelInmuebleUnidad(entidadEnEdicion.getEdiInmuebleSedeFk());
            if(entidadEnEdicion.getEdiRelInmuebleUnidadResp()==null){
                entidadEnEdicion.setEdiRelInmuebleUnidadResp(new ArrayList());
            }
        }
    }

    public void guardar() {
        try {

            entidadEnEdicion.setEdiTipoConstruccion(comboTipoConstruccion.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            edificioBean.setEntidadEnEdicion(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgEdificio var) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = restClient.obtenerPorId(var.getEdiPk());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgInmueblesSedes> completeInmueble(String query) {
        try {
            FiltroInmueblesSedes fil = new FiltroInmueblesSedes();
            fil.setCodigo(query);
            fil.setMaxResults(11L);
            fil.setIncluirCampos(new String[]{"tisPk", "tisCodigo", "tisVersion", "tisExisteOtrasSedesUnidadAdmi"});
            return inmueblesSedesRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void buscarListRelInmuebleUnidad(SgInmueblesSedes inmu) {
        try {
            FiltroRelInmuebleUnidadResp fri = new FiltroRelInmuebleUnidadResp();
            fri.setInmuebleFk(inmu.getTisPk());
            relInmuebleUnidadRespList = relInmuebleUnidadRespRestClient.buscar(fri);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarInmueble() {

        entidadEnEdicion.setEdiRelInmuebleUnidadResp(new ArrayList());
        relInmuebleUnidadRespList = new ArrayList();
        entidadEnEdicion.setEdiInmuebleSedeFk(inmuebleSeleccionado);
        if (BooleanUtils.isTrue(inmuebleSeleccionado.getTisExisteOtrasSedesUnidadAdmi())) {
            buscarListRelInmuebleUnidad(inmuebleSeleccionado);
            entidadEnEdicion.setEdiRelInmuebleUnidadResp(relInmuebleUnidadRespList);
        }

    }

    public SofisComboG<SgTipoConstruccion> getComboTipoConstruccion() {
        return comboTipoConstruccion;
    }

    public void setComboTipoConstruccion(SofisComboG<SgTipoConstruccion> comboTipoConstruccion) {
        this.comboTipoConstruccion = comboTipoConstruccion;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public FiltroEdificio getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEdificio filtro) {
        this.filtro = filtro;
    }

    public SgEdificio getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEdificio entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialEdificio() {
        return historialEdificio;
    }

    public void setHistorialEdificio(List<RevHistorico> historialEdificio) {
        this.historialEdificio = historialEdificio;
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

    public LazyEdificioDataModel getEdificioLazyModel() {
        return edificioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEdificioDataModel edificioLazyModel) {
        this.edificioLazyModel = edificioLazyModel;
    }

    public Boolean getEsSedePorDefecto() {
        return esSedePorDefecto;
    }

    public void setEsSedePorDefecto(Boolean esSedePorDefecto) {
        this.esSedePorDefecto = esSedePorDefecto;
    }

    public SgInmueblesSedes getInmuebleSeleccionado() {
        return inmuebleSeleccionado;
    }

    public void setInmuebleSeleccionado(SgInmueblesSedes inmuebleSeleccionado) {
        this.inmuebleSeleccionado = inmuebleSeleccionado;
    }

    public List<SgRelInmuebleUnidadResp> getRelInmuebleUnidadRespList() {
        return relInmuebleUnidadRespList;
    }

    public void setRelInmuebleUnidadRespList(List<SgRelInmuebleUnidadResp> relInmuebleUnidadRespList) {
        this.relInmuebleUnidadRespList = relInmuebleUnidadRespList;
    }

    public List<SgRelInmuebleUnidadResp> getRelInmuebleUnidadRespCompletaList() {
        return relInmuebleUnidadRespCompletaList;
    }

    public void setRelInmuebleUnidadRespCompletaList(List<SgRelInmuebleUnidadResp> relInmuebleUnidadRespCompletaList) {
        this.relInmuebleUnidadRespCompletaList = relInmuebleUnidadRespCompletaList;
    }

}
