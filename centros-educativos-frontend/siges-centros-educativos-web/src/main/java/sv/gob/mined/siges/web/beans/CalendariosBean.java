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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgTipoCalendario;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyCalendarioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.TipoCalendarioRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalendariosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalendarioBean.class.getName());

    @Inject
    private CalendarioRestClient restClient;

    @Inject
    private AnioLectivoRestClient anioLectRestClient;

    @Inject
    private TipoCalendarioRestClient tipoCalendarioRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroCalendario filtro = new FiltroCalendario();
    private SgCalendario entidadEnEdicion = new SgCalendario();
    private List<RevHistorico> historialCalendario = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalendarioDataModel calendarioLazyModel;
    private SofisComboG<SgAnioLectivo> AnioLectivoCombo;
    private SofisComboG<SgTipoCalendario> TipoCalendarioCombo;

    public CalendariosBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALENDARIOS)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            calendarioLazyModel = new LazyCalendarioDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAscending(new boolean[]{false});
            fal.setOrderBy(new String[]{"aleAnio"});
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(fal);
            AnioLectivoCombo = new SofisComboG(listAnio, "aleAnio");
            AnioLectivoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setIncluirCampos(new String[]{"tcePk", "tceNombre", "tceVersion"});
            List<SgTipoCalendario> listCalendario = tipoCalendarioRestClient.buscar(fc);
            TipoCalendarioCombo = new SofisComboG(listCalendario, "tceNombre");
            TipoCalendarioCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        AnioLectivoCombo.setSelected(-1);
        TipoCalendarioCombo.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroCalendario();
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgCalendario();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgCalendario var) {
        limpiarCombos();
        entidadEnEdicion = (SgCalendario) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void guardar() {
        try {
            if (AnioLectivoCombo.getSelectedT() != null) {
                entidadEnEdicion.setCalAnioLectivo(AnioLectivoCombo.getSelectedT());
            }
            if (TipoCalendarioCombo.getSelectedT() != null) {
                entidadEnEdicion.setCalTipoCalendario(TipoCalendarioCombo.getSelectedT());
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
            restClient.eliminar(entidadEnEdicion.getCalPk());
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
            historialCalendario = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCalendario getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCalendario filtro) {
        this.filtro = filtro;
    }

    public SgCalendario getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalendario entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCalendario() {
        return historialCalendario;
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

    public LazyCalendarioDataModel getCalendarioLazyModel() {
        return calendarioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalendarioDataModel calendarioLazyModel) {
        this.calendarioLazyModel = calendarioLazyModel;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return AnioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> AnioLectivoCombo) {
        this.AnioLectivoCombo = AnioLectivoCombo;
    }

    public SofisComboG<SgTipoCalendario> getTipoCalendarioCombo() {
        return TipoCalendarioCombo;
    }

    public void setTipoCalendarioCombo(SofisComboG<SgTipoCalendario> TipoCalendarioCombo) {
        this.TipoCalendarioCombo = TipoCalendarioCombo;
    }

}
