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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgTipoCuentaBancaria;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyTipoCuentaBancariaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.TipoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de tipos de cuenta bancaria.
 * 
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TipoCuentaBancariaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TipoCuentaBancariaBean.class.getName());

    @Inject
    private TipoCuentaBancariaRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgTipoCuentaBancaria entidadEnEdicion = new SgTipoCuentaBancaria();
    private List<SgTipoCuentaBancaria> historialTipoCuentaBancaria = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTipoCuentaBancariaDataModel tipoCuentaBancariaLazyModel;

    /**
     * Constructor de la clase.
     */
    public TipoCuentaBancariaBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los tipos de cuenta bancaria según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            tipoCuentaBancariaLazyModel = new LazyTipoCuentaBancariaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos del formulario.
     */
    public void cargarCombos() {

    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de tipo de cuenta bancaria.
     */
    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgTipoCuentaBancaria();
    }

    /**
     * Carga los datos de un tipo de cuenta bancaria para poder ser editados.
     * @param var SgTipoCuentaBancaria: Elemento del grid seleccionado.
     */
    public void actualizar(SgTipoCuentaBancaria var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgTipoCuentaBancaria) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de tipo de cuenta bancaria.
     */
    public void guardar() {
        try {
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

    /**
     * Elimina un registro de tipo de cuenta bancaria.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getTcbPk());
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

    /**
     * Muestra el historial del registro.
     * @param id Long
     */
    public void historial(Long id) {
        try {
            historialTipoCuentaBancaria = restClient.obtenerHistorialPorId(id);
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

    public SgTipoCuentaBancaria getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTipoCuentaBancaria entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgTipoCuentaBancaria> getHistorialTipoCuentaBancaria() {
        return historialTipoCuentaBancaria;
    }

    public void setHistorialTipoCuentaBancaria(List<SgTipoCuentaBancaria> historialTipoCuentaBancaria) {
        this.historialTipoCuentaBancaria = historialTipoCuentaBancaria;
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

    public LazyTipoCuentaBancariaDataModel getTipoCuentaBancariaLazyModel() {
        return tipoCuentaBancariaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTipoCuentaBancariaDataModel tipoCuentaBancariaLazyModel) {
        this.tipoCuentaBancariaLazyModel = tipoCuentaBancariaLazyModel;
    }

}
