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
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RequerimientoPorRecurso;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDesembolso;
import sv.gob.mined.siges.web.dto.SgDetalleDesembolso;
import sv.gob.mined.siges.web.dto.SgRequerimientoFondo;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumDesembolsoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetalleDesembolso;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.lazymodels.LazyDesembolsoDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyDetalleDesembolsosDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyRequerimientoFondoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.DesembolsoRestClient;
import sv.gob.mined.siges.web.restclient.DetalleDesembolsosRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
/**
 * Bean para la gestión de detalles desembolsos.
 *
 * @author Sofis Solutions
 */
public class DetallesDesembolsoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DetallesDesembolsoBean.class.getName());

    @Inject
    private DesembolsoRestClient restClient;

    @Inject
    private DetalleDesembolsosRestClient restDetDesembolso;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "id")
    private Long desembolsoId;

    private SgDesembolso entidadEnEdicion = new SgDesembolso();
    private SgDetalleDesembolso detalleDesEnEdicion = new SgDetalleDesembolso();
    private FiltroDetalleDesembolso filtro = new FiltroDetalleDesembolso();
    private FiltroCodiguera filtroComponente = new FiltroCodiguera();
    private FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();

    private Long totalResultados;
    private Long totalResultadosReq;
    private Long totalResultadosDet;
    private Integer paginado = 25;
    private Integer paginadoDet = 10;
    private Boolean soloLectura = Boolean.FALSE;
    private LazyDetalleDesembolsosDataModel detalleDesembolsosLazyModel;

    private LazyDesembolsoDataModel desembolsoDataModel;
    private LazyRequerimientoFondoDataModel requerimientoFondoDataModel;

    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SofisComboG<EnumDesembolsoEstado> estadoDesCombo = new SofisComboG<>();
    private List<SgRequerimientoFondo> listaReqSeleccionados;
    private List<SgDetalleDesembolso> listDetDesembolsos = new ArrayList();
    private List<RevHistorico> historialMovimientos = new ArrayList();
    private List<RequerimientoPorRecurso> tablaReqsPorRecurso = new ArrayList();
    private List<SgDetalleDesembolso> listReqsRecurso = new ArrayList();

    public DetallesDesembolsoBean() {
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
     * Carga los combos de filtro
     */
    public void cargarCombos() {
        try {

            filtroComponente.setOrderBy(new String[]{"cpeNombre"});
            filtroComponente.setIncluirCampos(new String[]{"cpeId", "cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesId", "gesVersion", "gesNombre", "gesCod"});
                filtroSubComponente.setCpeId(comboComponente.getSelectedT().getCpeId());
                comboSubComponente = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubComponente), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Crea un nuevo objeto de desembolso
     */
    public void agregar() {

    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setOrderBy(new String[]{"dedPk"});
            filtro.setIncluirCampos(new String[]{
                "dedPk",
                "dedDesembolsoFk.dsbPk",
                "dedDesembolsoFk.dsbEstado",
                "dedReqFondoFk.strPk",
                "dedReqFondoFk.strCuentaBancDdFk.cbdPk",
                "dedReqFondoFk.strCuentaBancDdFk.cbdVersion",
                "dedReqFondoFk.strSacGOES",
                "dedReqFondoFk.strSacUFI",
                "dedReqFondoFk.strCompromisoPresupuestario",
                "dedReqFondoFk.strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeNombre",
                "dedReqFondoFk.strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesNombre",
                "dedReqFondoFk.strTransferenciaGDepFk.tgdDepartamentoFk.depCodigo",
                "dedReqFondoFk.strTransferenciaGDepFk.tgdDepartamentoFk.depNombre",
                "dedFuenteRecursosFk.nombre",
                "dedFuenteRecursosFk.fuenteFinanciamiento.nombre",
                "dedPorcentaje",
                "dedMonto",
                "dedVersion"});
            filtro.setAscending(new boolean[]{false});

            if (comboComponente.getSelectedT() != null) {
                filtro.setComponenteId(comboComponente.getSelectedT().getCpeId());
            }

            if (comboSubComponente.getSelectedT() != null) {
                filtro.setSubComponenteId(comboSubComponente.getSelectedT().getGesId());
            }

            totalResultadosDet = restDetDesembolso.buscarTotal(filtro);
            detalleDesembolsosLazyModel = new LazyDetalleDesembolsosDataModel(restDetDesembolso, filtro, totalResultadosDet, paginadoDet);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un desembolso para poder ser editados.
     *
     * @param req
     */
    public void actualizar(SgDesembolso req) {
        try {
            entidadEnEdicion = req;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
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
            historialMovimientos = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros seleccionados y busca de nuevo
     */
    public void limpiar() {
        filtro = new FiltroDetalleDesembolso();
        filtroComponente = new FiltroCodiguera();
        filtroSubComponente = new FiltroGesPresEs();
        comboComponente.setSelected(-1);
        comboSubComponente = new SofisComboG();
        buscar();
    }

    /**
     * Elimina un registro de detalle desembolso.
     */
    public void eliminar() {
        try {
            restDetDesembolso.eliminar(detalleDesEnEdicion.getDedPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            detalleDesEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de desembolso.
     */
    public void guardar() {
        try {

        } /*catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        }*/ catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getters y setters">
    public void setEntidadEnEdicion(SgDesembolso entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgDesembolso getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public FiltroDetalleDesembolso getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDetalleDesembolso filtro) {
        this.filtro = filtro;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public List<RevHistorico> getHistorialPresupuestoEscolar() {
        return historialMovimientos;
    }

    public void setHistorialPresupuestoEscolar(List<RevHistorico> historialMovimientos) {
        this.historialMovimientos = historialMovimientos;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public LazyDesembolsoDataModel getDesembolsoDataModel() {
        return desembolsoDataModel;
    }

    public void setDesembolsoDataModel(LazyDesembolsoDataModel desembolsoDataModel) {
        this.desembolsoDataModel = desembolsoDataModel;
    }

    public SofisComboG<EnumDesembolsoEstado> getEstadoDesCombo() {
        return estadoDesCombo;
    }

    public void setEstadoDesCombo(SofisComboG<EnumDesembolsoEstado> estadoDesCombo) {
        this.estadoDesCombo = estadoDesCombo;
    }

    public List<SgRequerimientoFondo> getListaReqSeleccionados() {
        return listaReqSeleccionados;
    }

    public void setListaReqSeleccionados(List<SgRequerimientoFondo> listaReqSeleccionados) {
        this.listaReqSeleccionados = listaReqSeleccionados;
    }

    public Long getTotalResultadosReq() {
        return totalResultadosReq;
    }

    public void setTotalResultadosReq(Long totalResultadosReq) {
        this.totalResultadosReq = totalResultadosReq;
    }

    public LazyRequerimientoFondoDataModel getRequerimientoFondoDataModel() {
        return requerimientoFondoDataModel;
    }

    public void setRequerimientoFondoDataModel(LazyRequerimientoFondoDataModel requerimientoFondoDataModel) {
        this.requerimientoFondoDataModel = requerimientoFondoDataModel;
    }

    public List<SgDetalleDesembolso> getListDetDesembolsos() {
        return listDetDesembolsos;
    }

    public void setListDetDesembolsos(List<SgDetalleDesembolso> listDetDesembolsos) {
        this.listDetDesembolsos = listDetDesembolsos;
    }

    public List<SgDetalleDesembolso> getListReqsRecurso() {
        return listReqsRecurso;
    }

    public void setListReqsRecurso(List<SgDetalleDesembolso> listReqsRecurso) {
        this.listReqsRecurso = listReqsRecurso;
    }

    public Long getTotalResultadosDet() {
        return totalResultadosDet;
    }

    public void setTotalResultadosDet(Long totalResultadosDet) {
        this.totalResultadosDet = totalResultadosDet;
    }

    public Integer getPaginadoDet() {
        return paginadoDet;
    }

    public void setPaginadoDet(Integer paginadoDet) {
        this.paginadoDet = paginadoDet;
    }

    public LazyDetalleDesembolsosDataModel getDetalleDesembolsosLazyModel() {
        return detalleDesembolsosLazyModel;
    }

    public void setDetalleDesembolsosLazyModel(LazyDetalleDesembolsosDataModel detalleDesembolsosLazyModel) {
        this.detalleDesembolsosLazyModel = detalleDesembolsosLazyModel;
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
