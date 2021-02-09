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
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgTransferenciaDireccionDep;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumTransferenciaEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaDireccionDep;
import sv.gob.mined.siges.web.lazymodels.LazyTransferenciaDireccionDepDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaDireccionDepRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean correspondiente a la gestion de transferencias a las Direcciones
 * Departamentales.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TransferenciaDireccionDepBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TransferenciaDireccionDepBean.class.getName());

    @Inject
    private RequerimientoFondoRestClient solTraClient;

    @Inject
    private TransferenciaDireccionDepRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    private FiltroTransferenciaDireccionDep filtro = new FiltroTransferenciaDireccionDep();
    private SgTransferenciaDireccionDep entidadEnEdicion = new SgTransferenciaDireccionDep();
    private List<SgTransferenciaDireccionDep> historialTransferenciaDireccionDep = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTransferenciaDireccionDepDataModel transferenciaDireccionDepLazyModel;

    private SofisComboG<EnumTransferenciaEstado> comboEstado;
    private SofisComboG<SgDepartamento> comboDepartamento;

    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();

    private List<EnumTransferenciaEstado> estados = new ArrayList();

    public TransferenciaDireccionDepBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
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
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"tddPk",
                "tddTransferenciaFk.tcId",
                "tddTransferenciaFk.componente.cpeId",
                "tddTransferenciaFk.componente.cpeNombre",
                "tddTransferenciaFk.componente.cpeVersion",
                "tddTransferenciaFk.subComponente.gesId",
                "tddTransferenciaFk.subComponente.gesNombre",
                "tddTransferenciaFk.subComponente.gesVersion",
                "tddTransferenciaFk.anioFiscal.aniPk",
                "tddTransferenciaFk.anioFiscal.aniAnio",
                "tddTransferenciaFk.anioFiscal.aniVersion",
                "tddTransferenciaFk.unidadPresupuestaria.cuNombre",
                "tddTransferenciaFk.unidadPresupuestaria.cuVersion",
                "tddTransferenciaFk.lineaPresupuestaria.cuNombre",
                "tddTransferenciaFk.lineaPresupuestaria.cuVersion",
                "tddTransferenciaFk.tcFuenteRecursosFk.nombre",
                "tddTransferenciaFk.tcFuenteRecursosFk.version",
                "tddTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.nombre",
                "tddTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.version",
                "tddTransferenciaFk.tcPorcentaje",
                "tddTransferenciaFk.tcVersion",
                "tddDireccionDepFk.dedPk",
                "tddDireccionDepFk.dedDepartamentoFk",
                "tddDireccionDepFk.dedVersion",
                "tddMontoAutorizado",
                "tddMontoSolicitado",
                "tddBeneficiarios",
                "tddEstado",
                "tddVersion"});
            filtro.setOrderBy(new String[]{"tddPk"});
            filtro.setAscending(new boolean[]{false});

            if (comboDepartamento.getSelectedT() != null) {
                filtro.setDepartamento(comboDepartamento.getSelectedT());
            }

            if (comboEstado.getSelectedT() != null) {
                filtro.setTddEstado(comboEstado.getSelectedT());
            }

            if (comboComponente.getSelectedT() != null) {
                filtro.setComponente(comboComponente.getSelectedT().getCpeId());
            }

            if (comboSubComponente.getSelectedT() != null) {
                filtro.setSubComponente(comboSubComponente.getSelectedT().getGesId());
            }

            totalResultados = restClient.buscarTotal(filtro);
            transferenciaDireccionDepLazyModel = new LazyTransferenciaDireccionDepDataModel(solTraClient, restClient, filtro, totalResultados, paginado);

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
            estados = new ArrayList(Arrays.asList(EnumTransferenciaEstado.values()));
            comboEstado = new SofisComboG(estados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(departamentos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera filtroComponente = new FiltroCodiguera();
            filtroComponente.setOrderBy(new String[]{"cpeNombre"});
            filtroComponente.setIncluirCampos(new String[]{"cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    /**
     * Carga el combo de subcomponente
     */
    public void cargarSubComponente() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesVersion", "gesNombre", "gesCod"});
                filtroSubComponente.setCpeId(comboComponente.getSelectedT().getCpeId());
                comboSubComponente = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubComponente), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Limpia los combos de la búsqueda
     */
    private void limpiarCombos() {
        estados = new ArrayList();
        comboEstado = new SofisComboG();
        comboDepartamento = new SofisComboG();
        comboComponente = new SofisComboG();
        comboSubComponente = new SofisComboG();
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        filtro = new FiltroTransferenciaDireccionDep();
        limpiarCombos();
        cargarCombos();
        buscar();
    }

    /**
     * Crea un nuevo objeto de transferencia a dirección dep
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgTransferenciaDireccionDep();
    }

    /**
     * Carga los datos de un transferencia a dirección dep para poder ser
     * editados.
     *
     * @param var
     */
    public void actualizar(SgTransferenciaDireccionDep var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgTransferenciaDireccionDep) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de transferencia a dirección dep
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
     * Elimina un registro de transferencia a dirección dep
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getTddPk());
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
            historialTransferenciaDireccionDep = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroTransferenciaDireccionDep getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroTransferenciaDireccionDep filtro) {
        this.filtro = filtro;
    }

    public SgTransferenciaDireccionDep getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgTransferenciaDireccionDep entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgTransferenciaDireccionDep> getHistorialTransferenciaDireccionDep() {
        return historialTransferenciaDireccionDep;
    }

    public void setHistorialTransferenciaDireccionDep(List<SgTransferenciaDireccionDep> historialTransferenciaDireccionDep) {
        this.historialTransferenciaDireccionDep = historialTransferenciaDireccionDep;
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

    public LazyTransferenciaDireccionDepDataModel getTransferenciaDireccionDepLazyModel() {
        return transferenciaDireccionDepLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyTransferenciaDireccionDepDataModel transferenciaDireccionDepLazyModel) {
        this.transferenciaDireccionDepLazyModel = transferenciaDireccionDepLazyModel;
    }

    public SofisComboG<EnumTransferenciaEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumTransferenciaEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponente() {
        return comboSubComponente;
    }

    public void setComboSubComponente(SofisComboG<SsGesPresEs> comboSubComponente) {
        this.comboSubComponente = comboSubComponente;
    }

    // </editor-fold>
}
