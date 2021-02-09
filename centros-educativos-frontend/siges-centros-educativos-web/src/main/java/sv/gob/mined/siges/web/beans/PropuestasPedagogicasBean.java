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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgPropuestaPedagogica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPropuestaPedagogica;
import sv.gob.mined.siges.web.lazymodels.LazyPropuestaPedagogicaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PropuestaPedagogicaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PropuestasPedagogicasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PropuestaPedagogicaBean.class.getName());

    @Inject
    private PropuestaPedagogicaRestClient restClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private SedeRestClient sedeClient;

    private FiltroPropuestaPedagogica filtro = new FiltroPropuestaPedagogica();
    private SgPropuestaPedagogica entidadEnEdicion = new SgPropuestaPedagogica();
    private List<RevHistorico> historialPropuestaPedagogica = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyPropuestaPedagogicaDataModel propuestaPedagogicaLazyModel;
    private SgSede sedeSeleccionada;

    public PropuestasPedagogicasBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PROPUESTA_PEDAGOGICA)) {
            JSFUtils.redirectToIndex();
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
            return sedeClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setPpeSede(sedeSeleccionada!=null?sedeSeleccionada.getSedPk():null);
            filtro.setIncluirCampos(new String[]{"ppePk","ppeNombre", "ppeVersion", "ppeFechaInicio", "ppeFechaFin", "ppeUltModFecha", "ppeUltModUsuario", 
                "ppeSede.sedCodigo", 
                "ppeSede.sedNombre",
                "ppeSede.sedTipo",});
            totalResultados = restClient.buscarTotal(filtro);
            propuestaPedagogicaLazyModel = new LazyPropuestaPedagogicaDataModel(restClient, filtro, totalResultados, paginado);
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

    private void limpiarCombos() {
        
    }

    public void limpiar() {
        filtro = new FiltroPropuestaPedagogica();
        sedeSeleccionada = null;
        buscar();
    }

    public void actualizar(SgPropuestaPedagogica var) {
        limpiarCombos();
        entidadEnEdicion = (SgPropuestaPedagogica) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPpePk());
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

    public void historial(Long id) {
        try {
            historialPropuestaPedagogica = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroPropuestaPedagogica getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPropuestaPedagogica filtro) {
        this.filtro = filtro;
    }

    public SgPropuestaPedagogica getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPropuestaPedagogica entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialPropuestaPedagogica() {
        return historialPropuestaPedagogica;
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

    public LazyPropuestaPedagogicaDataModel getPropuestaPedagogicaLazyModel() {
        return propuestaPedagogicaLazyModel;
    }

    public void setTiposPropuestaPedagogicaLazyModel(LazyPropuestaPedagogicaDataModel propuestaPedagogicaLazyModel) {
        this.propuestaPedagogicaLazyModel = propuestaPedagogicaLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

}
