/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyeccionPoblacion;
import sv.gob.mined.siges.web.lazymodels.LazyProyeccionesPoblacionDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ProyeccionesPoblacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ProyeccionesPoblacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProyeccionesPoblacionBean.class.getName());

    @Inject
    private ProyeccionesPoblacionRestClient restClient;
    
    @Inject
    private CatalogosRestClient catalogosClient;
    
    @Inject
    private SessionBean sessionBean;

    private FiltroProyeccionPoblacion filtro = new FiltroProyeccionPoblacion();
    private Integer paginado = 20;
    private Long totalResultados;
    private LazyProyeccionesPoblacionDataModel proyeccionesLazyModel;
   

    public ProyeccionesPoblacionBean() {
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
            totalResultados = restClient.buscarTotal(filtro);
            proyeccionesLazyModel = new LazyProyeccionesPoblacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
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
        filtro = new FiltroProyeccionPoblacion();
        buscar();
    }


    public FiltroProyeccionPoblacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroProyeccionPoblacion filtro) {
        this.filtro = filtro;
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

    public LazyProyeccionesPoblacionDataModel getProyeccionesLazyModel() {
        return proyeccionesLazyModel;
    }

    public void setProyeccionesLazyModel(LazyProyeccionesPoblacionDataModel proyeccionesLazyModel) {
        this.proyeccionesLazyModel = proyeccionesLazyModel;
    }

    
    
}
