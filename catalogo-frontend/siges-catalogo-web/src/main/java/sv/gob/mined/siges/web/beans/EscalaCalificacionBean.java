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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCalificacion;
import sv.gob.mined.siges.web.dto.SgEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscalaCalificacion;
import sv.gob.mined.siges.web.lazymodels.LazyEscalaCalificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.EscalaCalificacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EscalaCalificacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EscalaCalificacionBean.class.getName());

    @Inject
    private EscalaCalificacionRestClient restClient;

    @Inject
    private CalificacionRestClient calRestClient;

    private FiltroEscalaCalificacion filtro = new FiltroEscalaCalificacion();
    private SgEscalaCalificacion entidadEnEdicion = new SgEscalaCalificacion();
    private List<SgEscalaCalificacion> historialEscalaCalificacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEscalaCalificacionDataModel escalaCalificacionLazyModel;
    private SofisComboG<TipoEscalaCalificacion> comboTiposEscalas;
    private SofisComboG<TipoEscalaCalificacion> comboTiposEscalasBus;
    private SofisComboG<SgCalificacion> comboValorMinimo;
    private Boolean numerica=Boolean.FALSE;

    public EscalaCalificacionBean() {
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
            filtro.setEcaTipoEscala(comboTiposEscalasBus.getSelectedT() != null ? comboTiposEscalasBus.getSelectedT() : null);
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            escalaCalificacionLazyModel = new LazyEscalaCalificacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<TipoEscalaCalificacion> escalas = new ArrayList(Arrays.asList(TipoEscalaCalificacion.values()));
        comboTiposEscalas = new SofisComboG(escalas, "text");
        comboTiposEscalas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        comboTiposEscalasBus = new SofisComboG(escalas, "text");
        comboTiposEscalasBus.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

    }

    private void limpiarCombos() {
        comboTiposEscalas.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroEscalaCalificacion();
        comboTiposEscalasBus.setSelected(-1);
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        numerica=Boolean.FALSE;
        limpiarCombos();
        entidadEnEdicion = new SgEscalaCalificacion();
    }

    public void actualizar(SgEscalaCalificacion var) {
        try {
            JSFUtils.limpiarMensajesError();
            numerica=Boolean.FALSE;
            limpiarCombos();
            entidadEnEdicion = restClient.obtenerPorId(var.getEcaPk());
            comboTiposEscalas.setSelectedT(var.getEcaTipoEscala());
            if(comboTiposEscalas.getSelectedT()!=null){
             if (comboTiposEscalas.getSelectedT().equals(TipoEscalaCalificacion.NUMERICA)){
                 numerica=Boolean.TRUE;
             }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void esNumerica(){
        numerica=Boolean.FALSE;
        if(comboTiposEscalas.getSelectedT()!=null){
             if (comboTiposEscalas.getSelectedT().equals(TipoEscalaCalificacion.NUMERICA)){
                 numerica=Boolean.TRUE;
             }
        }
        
    }

    public void guardar() {
        try {
           if(comboTiposEscalas.getSelectedT()!=null){
            if (comboTiposEscalas.getSelectedT().equals(TipoEscalaCalificacion.NUMERICA)){
                 if(entidadEnEdicion.getEcaValorMinimo()!=null){
                     entidadEnEdicion.setEcaValorMinimo(null);
                 }
             }
            }
            entidadEnEdicion.setEcaTipoEscala(comboTiposEscalas.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEcaPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialEscalaCalificacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgCalificacion> completeCalificacion(String query) {
        try {
            if(entidadEnEdicion.getEcaPk()!=null){
                FiltroCalificacion fil = new FiltroCalificacion();
                fil.setCalValor(query);
                fil.setMaxResults(10L);
                fil.setOrderBy(new String[]{"calOrden"});
                fil.setAscending(new boolean[]{false});
                fil.setEcaCalPk(entidadEnEdicion.getEcaPk());
                return calRestClient.buscar(fil);
            }else{
                return null;
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public Boolean Numerica(SgEscalaCalificacion eca){
        return eca.getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA);
    }

    public SofisComboG<TipoEscalaCalificacion> getComboTiposEscalasBus() {
        return comboTiposEscalasBus;
    }

    public void setComboTiposEscalasBus(SofisComboG<TipoEscalaCalificacion> comboTiposEscalasBus) {
        this.comboTiposEscalasBus = comboTiposEscalasBus;
    }

    public SofisComboG<TipoEscalaCalificacion> getComboTiposEscalas() {
        return comboTiposEscalas;
    }

    public void setComboTiposEscalas(SofisComboG<TipoEscalaCalificacion> comboTiposEscalas) {
        this.comboTiposEscalas = comboTiposEscalas;
    }

    public FiltroEscalaCalificacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEscalaCalificacion filtro) {
        this.filtro = filtro;
    }

    public SgEscalaCalificacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEscalaCalificacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgEscalaCalificacion> getHistorialEscalaCalificacion() {
        return historialEscalaCalificacion;
    }

    public void setHistorialEscalaCalificacion(List<SgEscalaCalificacion> historialEscalaCalificacion) {
        this.historialEscalaCalificacion = historialEscalaCalificacion;
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

    public LazyEscalaCalificacionDataModel getEscalaCalificacionLazyModel() {
        return escalaCalificacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEscalaCalificacionDataModel escalaCalificacionLazyModel) {
        this.escalaCalificacionLazyModel = escalaCalificacionLazyModel;
    }

    public SofisComboG<SgCalificacion> getComboValorMinimo() {
        return comboValorMinimo;
    }

    public void setComboValorMinimo(SofisComboG<SgCalificacion> comboValorMinimo) {
        this.comboValorMinimo = comboValorMinimo;
    }

    public Boolean getNumerica() {
        return numerica;
    }

    public void setNumerica(Boolean numerica) {
        this.numerica = numerica;
    }

    

}
