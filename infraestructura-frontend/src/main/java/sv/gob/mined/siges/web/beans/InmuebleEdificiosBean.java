/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEdificio;
import sv.gob.mined.siges.web.lazymodels.LazyEdificioDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InmuebleEdificiosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InmuebleEdificiosBean.class.getName());

    @Inject
    private EdificioRestClient restClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private SedeRestClient sedeRestClient;
    
    @Inject
    private SessionBean sessionBean;

    private FiltroEdificio filtro = new FiltroEdificio();
    private SgInmueblesSedes entidadEnEdicion = new SgInmueblesSedes();
    private List<RevHistorico> historialEdificio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados=0L;
    private LazyEdificioDataModel edificioLazyModel;
    private SgSede sedeSeleccionada;
    private Boolean esSedePorDefecto;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;
    private HashMap<SgDepartamento, List<SgMunicipio>> munCache = new HashMap<>();
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean beanIniciado=Boolean.FALSE;

    public InmuebleEdificiosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean) request.getAttribute("soloLectura")) : soloLectura;
             buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEntidad() {
        try {          
           if(!beanIniciado){
                buscar();
                beanIniciado=Boolean.TRUE;
           }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_EDIFICIO)&& !sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_EDIFICIOS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            if(entidadEnEdicion.getTisPk()!=null){
                filtro.setInmuebleId(entidadEnEdicion.getTisPk());
                filtro.setFirst(new Long(0));
                totalResultados = restClient.buscarTotal(filtro);
                edificioLazyModel = new LazyEdificioDataModel(restClient, filtro, totalResultados, paginado);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


 
   
    
    public void limpiar() {
        filtro = new FiltroEdificio();
    }





    public void historial(Long id) {
        try {
            historialEdificio = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroEdificio getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEdificio filtro) {
        this.filtro = filtro;
    }

    public SgInmueblesSedes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInmueblesSedes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
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

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Boolean getEsSedePorDefecto() {
        return esSedePorDefecto;
    }

    public void setEsSedePorDefecto(Boolean esSedePorDefecto) {
        this.esSedePorDefecto = esSedePorDefecto;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

    public HashMap<SgDepartamento, List<SgMunicipio>> getMunCache() {
        return munCache;
    }

    public void setMunCache(HashMap<SgDepartamento, List<SgMunicipio>> munCache) {
        this.munCache = munCache;
    }

}
