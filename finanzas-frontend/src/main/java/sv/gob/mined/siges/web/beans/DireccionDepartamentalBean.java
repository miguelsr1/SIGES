/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDireccionDepartamental;
import sv.gob.mined.siges.web.dto.SgProfesion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDireccionDepartamental;
import sv.gob.mined.siges.web.lazymodels.LazyDireccionDepartamentalDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DireccionDepartamentalRestClient;
import sv.gob.mined.siges.web.restclient.ProfesionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de las pagadurías en las direcciones departamentales.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DireccionDepartamentalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DireccionDepartamentalBean.class.getName());

    @Inject
    private DireccionDepartamentalRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private ProfesionRestClient profesionRestClient;

    @Inject
    private SessionBean sessionBean;

    private SofisComboG<SgDepartamento> comboDepartamento = new SofisComboG<>();
    private SofisComboG<SgDepartamento> comboDepartamentoNew = new SofisComboG<>();
    private SofisComboG<SgProfesion> comboProfesion = new SofisComboG<>();
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroDireccionDepartamental filtroDD = new FiltroDireccionDepartamental();
    private SgDireccionDepartamental entidadEnEdicion = new SgDireccionDepartamental();
    private List<RevHistorico> historialDireccionDepartamental = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private String tituloPagina = "";
    private LazyDireccionDepartamentalDataModel direccionDepartamentalLazyModel;
    private boolean historial = false;

    public DireccionDepartamentalBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
            setTituloPagina();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtroDD.setFirst(new Long(0));
            if (comboDepartamento.getSelectedT() != null) {
                filtroDD.setDepartamentoPk(comboDepartamento.getSelectedT().getDepPk());
            }

            totalResultados = restClient.buscarTotal(filtroDD);
            direccionDepartamentalLazyModel = new LazyDireccionDepartamentalDataModel(restClient, filtroDD, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de la busqueda
     */
    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depPk", "depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), be), be);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de departamento
     */
    private void cargarDepartamento() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depPk", "depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentoNew = new SofisComboG(departamentos, "depNombre");
            comboDepartamentoNew.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combo de profesiones
     */
    private void cargarProfesiones() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"proNombre"});
            fc.setIncluirCampos(new String[]{"proPk", "proCodigo", "proNombre", "proVersion"});
            List<SgProfesion> profesiones = profesionRestClient.buscar(fc);
            comboProfesion = new SofisComboG(profesiones, "proNombre");
            comboProfesion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (Optional.ofNullable(entidadEnEdicion.getDedDirectorProfesionFk()).isPresent()) {
                comboProfesion.setSelectedT(entidadEnEdicion.getDedDirectorProfesionFk());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Limpia los combos de la busqueda
     */
    private void limpiarCombos() {
        comboDepartamento.setSelected(-1);
    }

    /**
     * Limpia los filtros y busca de nuevo
     */
    public void limpiar() {
        filtroDD = new FiltroDireccionDepartamental();
        limpiarCombos();
        buscar();
    }

    /**
     * Crea un nuevo objeto de desembolso
     */
    public void agregar() {
        entidadEnEdicion = new SgDireccionDepartamental();
        setTituloPagina();
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        cargarDepartamento();
        cargarProfesiones();
    }

    /**
     * Carga los datos de una dirección departamental para poder ser editados.
     *
     * @param var
     */
    public void actualizar(SgDireccionDepartamental var) {
        historial = false;
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgDireccionDepartamental) SerializationUtils.clone(var);
        setTituloPagina();
        cargarDepartamento();
        cargarProfesiones();
        if (var.getDedDepartamentoFk() != null) {
            comboDepartamentoNew.setSelectedT(entidadEnEdicion.getDedDepartamentoFk());
        }

    }

    /**
     * Crea o actualiza un registro de dirección departamental.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setDedDepartamentoFk(comboDepartamentoNew.getSelectedT());
            if (Optional.ofNullable(comboProfesion.getSelectedT()).isPresent()) {
                entidadEnEdicion.setDedDirectorProfesionFk(comboProfesion.getSelectedT());
            }
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
     * Elimina un registro de dirección departamental
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDedPk());
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
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialDireccionDepartamental = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra los datos de auditoría.
     *
     * @param estId Long: Id del registro
     * @param estRev Long: Revisión del registro
     */
    public void mostrarDatos(Long estId, Long estRev) {
        try {
            limpiarCombos();
            if (estId != null && estId > 0) {
                if (estRev != null && estRev > 0) {
                    entidadEnEdicion = restClient.obtenerEnRevision(estId, estRev);
                } else {
                    entidadEnEdicion = restClient.obtenerPorId(estId);
                }
            }
            historial = true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public String getTituloPagina() {
        return this.tituloPagina;
    }

    public void setTituloPagina() {
        if (entidadEnEdicion.getDedPk() != null) {
            tituloPagina = Etiquetas.getValue("edicionPagaduria");
        } else {
            tituloPagina = Etiquetas.getValue("agregarPagaduria");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public FiltroDireccionDepartamental getFiltroDD() {
        return filtroDD;
    }

    public void setFiltroDD(FiltroDireccionDepartamental filtroDD) {
        this.filtroDD = filtroDD;
    }

    public SgDireccionDepartamental getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDireccionDepartamental entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialDireccionDepartamental() {
        return historialDireccionDepartamental;
    }

    public void setHistorialDireccionDepartamental(List<RevHistorico> historialDireccionDepartamental) {
        this.historialDireccionDepartamental = historialDireccionDepartamental;
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

    public LazyDireccionDepartamentalDataModel getDireccionDepartamentalLazyModel() {
        return direccionDepartamentalLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyDireccionDepartamentalDataModel direccionDepartamentalLazyModel) {
        this.direccionDepartamentalLazyModel = direccionDepartamentalLazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoNew() {
        return comboDepartamentoNew;
    }

    public void setComboDepartamentoNew(SofisComboG<SgDepartamento> comboDepartamentoNew) {
        this.comboDepartamentoNew = comboDepartamentoNew;
    }

    public boolean isHistorial() {
        return historial;
    }

    public void setHistorial(boolean historial) {
        this.historial = historial;
    }

    public SofisComboG<SgProfesion> getComboProfesion() {
        return comboProfesion;
    }

    public void setComboProfesion(SofisComboG<SgProfesion> comboProfesion) {
        this.comboProfesion = comboProfesion;
    }
    // </editor-fold>

}
