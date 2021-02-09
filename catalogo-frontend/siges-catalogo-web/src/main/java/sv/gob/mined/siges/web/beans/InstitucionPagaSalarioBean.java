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
import sv.gob.mined.siges.web.dto.SgInstitucionPagaSalario;
import sv.gob.mined.siges.web.dto.SgTipoInstitucionPaga;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyInstitucionPagaSalarioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.InstitucionPagaSalarioRestClient;
import sv.gob.mined.siges.web.restclient.TipoInstitucionPagaSalarioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class InstitucionPagaSalarioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(InstitucionPagaSalarioBean.class.getName());

    @Inject
    private InstitucionPagaSalarioRestClient restClient;
    
    @Inject
    private TipoInstitucionPagaSalarioRestClient tipoRestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgInstitucionPagaSalario entidadEnEdicion = new SgInstitucionPagaSalario();
    private List<SgInstitucionPagaSalario> historialInstitucionPagaSalario = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyInstitucionPagaSalarioDataModel institucionPagaSalarioLazyModel;
    private SofisComboG<SgTipoInstitucionPaga> comboTipoInstitucionPaga;
    
    public InstitucionPagaSalarioBean() {
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
            institucionPagaSalarioLazyModel = new LazyInstitucionPagaSalarioDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{    
            FiltroCodiguera filtroC = new FiltroCodiguera();
            filtroC.setHabilitado(Boolean.TRUE);
            filtroC.setOrderBy(new String[]{"tipNombre"});
            List<SgTipoInstitucionPaga> listaTipoInstitucionPaga = tipoRestClient.buscar(filtroC);
            comboTipoInstitucionPaga = new SofisComboG(listaTipoInstitucionPaga, "tipNombre");
            comboTipoInstitucionPaga.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
         } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
            comboTipoInstitucionPaga.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgInstitucionPagaSalario();
    }

    public void actualizar(SgInstitucionPagaSalario var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgInstitucionPagaSalario) SerializationUtils.clone(var);
        comboTipoInstitucionPaga.setSelectedT(entidadEnEdicion.getIpsTipoInstitucion());
    }

    public void guardar() {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion.setIpsTipoInstitucion(comboTipoInstitucionPaga.getSelectedT());
            restClient.guardar(entidadEnEdicion);
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
            restClient.eliminar(entidadEnEdicion.getIpsPk());
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
            historialInstitucionPagaSalario = restClient.obtenerHistorialPorId(id);
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

    public SgInstitucionPagaSalario getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInstitucionPagaSalario entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgInstitucionPagaSalario> getHistorialInstitucionPagaSalario() {
        return historialInstitucionPagaSalario;
    }

    public void setHistorialInstitucionPagaSalario(List<SgInstitucionPagaSalario> historialInstitucionPagaSalario) {
        this.historialInstitucionPagaSalario = historialInstitucionPagaSalario;
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

    public LazyInstitucionPagaSalarioDataModel getInstitucionPagaSalarioLazyModel() {
        return institucionPagaSalarioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyInstitucionPagaSalarioDataModel institucionPagaSalarioLazyModel) {
        this.institucionPagaSalarioLazyModel = institucionPagaSalarioLazyModel;
    }

    public SofisComboG<SgTipoInstitucionPaga> getComboTipoInstitucionPaga() {
        return comboTipoInstitucionPaga;
    }

    public void setComboTipoInstitucionPaga(SofisComboG<SgTipoInstitucionPaga> comboTipoInstitucionPaga) {
        this.comboTipoInstitucionPaga = comboTipoInstitucionPaga;
    }

}
