/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyEdificioDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EdificioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EdificioBean.class.getName());

    @Inject
    private EdificioRestClient restClient;
    
    
    @Inject
    private RelEdificioEspacioBean relEdificioEspacioBean;
    
    @Inject
    private RelEdificioServicioBean relEdificioServicioBean;
    
    @Inject
    @Param(name = "id")
    private Long edificioId;


    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgEdificio entidadEnEdicion = new SgEdificio();
    private List<RevHistorico> historialEdificio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEdificioDataModel edificioLazyModel;
    private String tabActiveId;
    private Boolean soloLectura = Boolean.FALSE;
    private String urlMapa;
    
    public EdificioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (edificioId != null && edificioId > 0) {
                this.actualizar(restClient.obtenerPorId(edificioId));
                soloLectura = editable != null ? !editable : soloLectura;
                mostrarMapa();
            }else{
                agregar();
            }
            mostrarMapa();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

     public String getNombreEntidadEdicion(){
        String nombre=entidadEnEdicion.getEdiCodigo();
        if(!StringUtils.isBlank(entidadEnEdicion.getEdiNombre())){
            nombre=nombre+" - "+entidadEnEdicion.getEdiNombre();
        }
        return nombre;
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
       
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgEdificio();
    }

    public void actualizar(SgEdificio var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEdificio) SerializationUtils.clone(var);
    }

    
     public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();       
             
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }
     
     public void mostrarMapa() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath).append("/mapa/mapa.html?");

        urlMapa = url.toString();
        if (entidadEnEdicion != null) {
            if (entidadEnEdicion.getEdiLatitud() != null && entidadEnEdicion.getEdiLongitud() != null) {
                urlMapa = urlMapa + "lat=" + entidadEnEdicion.getEdiLatitud() + "&lon=" + entidadEnEdicion.getEdiLongitud() + "&sl=" + this.soloLectura;
            } else {
                urlMapa = urlMapa + "sl=" + this.soloLectura;
            }
        }
    }

    public String getUrlMapa() {
        if (urlMapa == null) {
            mostrarMapa();
        }
        return urlMapa;
    }

    public void updateAddressCoordinates() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String lat = params.get("latitude");
        String lng = params.get("longitude");

        if (lat != null) {
            entidadEnEdicion.setEdiLatitud(Double.parseDouble(lat));
        }
        if (lng != null) {
            entidadEnEdicion.setEdiLongitud(Double.parseDouble(lng));
        }
    }
    
    public void guardar() {
        try {           
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            mostrarMapa();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void historial(Long id) {
        try {
            historialEdificio = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
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

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

}
