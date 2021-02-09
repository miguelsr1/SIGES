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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgActividadEspecial;
import sv.gob.mined.siges.web.dto.SgActividadTiempoExtendido;
import sv.gob.mined.siges.web.dto.SgArea;
import sv.gob.mined.siges.web.dto.SgAsignatura;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgModulo;
import sv.gob.mined.siges.web.dto.SgPrueba;
import sv.gob.mined.siges.web.dto.catalogo.SgAreaAsignaturaModulo;
import sv.gob.mined.siges.web.dto.catalogo.SgAreaIndicador;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.web.lazymodels.LazyComponentePlanEstudioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ActividadEspecialRestClient;
import sv.gob.mined.siges.web.restclient.ActividadTiempoExtendidoRestClient;
import sv.gob.mined.siges.web.restclient.AreaRestClient;
import sv.gob.mined.siges.web.restclient.AsignaturaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.ModuloRestClient;
import sv.gob.mined.siges.web.restclient.PruebaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.enumerados.EnumTipoPruebaPaes;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ComponentePlanEstudioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ComponentePlanEstudioBean.class.getName());

    @Inject
    private ComponentePlanEstudioRestClient restClient;
    @Inject
    private ActividadEspecialRestClient actEsprestClient;
    @Inject
    private ActividadTiempoExtendidoRestClient actTiemprestClient;
    @Inject
    private AreaRestClient arearestClient;
    @Inject
    private AsignaturaRestClient asigrestClient;
    @Inject
    private ModuloRestClient modrestClient;
    @Inject
    private PruebaRestClient pruebrestClient;
    @Inject
    private CatalogosRestClient catalogorestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroComponentePlanEstudio filtro = new FiltroComponentePlanEstudio();
    private SgComponentePlanEstudio entidadEnEdicion = new SgComponentePlanEstudio();
    private List<RevHistorico> historialComponentePlanEstudio = new ArrayList();
    private List<SgComponentePlanEstudio> listaComponentePlanEstudio = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyComponentePlanEstudioDataModel componentePlanEstudioLazyModel;
    private SofisComboG<TipoComponentePlanEstudio> comboTiposCompPlanEst;
    private SofisComboG<EnumTipoPruebaPaes> comboTiposPaes;
    private SofisComboG<TipoComponentePlanEstudio> comboFiltroTiposCompPlanEst;
    private SofisComboG<SgAreaAsignaturaModulo> comboAreaAsigMod;
    private SofisComboG<SgAreaIndicador> comboAreaIndicador;
    private Boolean esIndicador = Boolean.FALSE;
    private Boolean esAsignaturaModulo = Boolean.FALSE;

    public ComponentePlanEstudioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            cargarCombosArea();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLANES_ESTUDIO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    public void buscar() {
        try {
            filtro.setCpeTipo(comboFiltroTiposCompPlanEst.getSelectedT());
            filtro.setOrderBy(new String[]{"cpeCodigo", "cpeNombre"});
            filtro.setAscending(new boolean[]{true, true});
            filtro.setIncluirCampos(new String[]{
                "cpeCodigo",
                "cpeNombre",
                "cpeTipo",
                "cpeHabilitado",
                "cpeUltModUsuario",
                "cpeUltModFecha",
                "cpeVersion",
                "asigAreaAsignaturaModulo.aamNombre",
                "modAreaAsignaturaModulo.aamNombre",
                "indAreaIndicador.ariNombre"
            });
            totalResultados = restClient.buscarTotal(filtro);
            componentePlanEstudioLazyModel = new LazyComponentePlanEstudioDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<TipoComponentePlanEstudio> escalas = new ArrayList(Arrays.asList(TipoComponentePlanEstudio.values()));
        escalas.remove(TipoComponentePlanEstudio.AESS);
        comboTiposCompPlanEst = new SofisComboG(escalas, "text");
        comboTiposCompPlanEst.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboFiltroTiposCompPlanEst = new SofisComboG(escalas, "text");
        comboFiltroTiposCompPlanEst.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        comboTiposPaes = new SofisComboG(Arrays.asList(EnumTipoPruebaPaes.values()), "text");
        comboTiposPaes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        

    }

    public void cargarCombosArea() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            List<SgAreaAsignaturaModulo> listaam = catalogorestClient.buscarAreaAsignaturaModulo(fc);

            List<SgAreaIndicador> listai = catalogorestClient.buscarAreaIndicador(fc);

            comboAreaAsigMod = new SofisComboG(listaam, "aamNombre");
            comboAreaAsigMod.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAreaIndicador = new SofisComboG(listai, "ariNombre");
            comboAreaIndicador.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarCombos() {
        comboTiposCompPlanEst.setSelected(0);
        comboAreaAsigMod.setSelected(0);
        comboAreaIndicador.setSelected(0);
        entidadEnEdicion = null;
    }

    public void limpiar() {
        filtro = new FiltroComponentePlanEstudio();
        comboFiltroTiposCompPlanEst.setSelected(0);
        buscar();
    }

    public void agregar() {
        esIndicador = Boolean.FALSE;
        esAsignaturaModulo = Boolean.FALSE;
        if (null == comboTiposCompPlanEst.getSelectedT()) {
            entidadEnEdicion = null;
        } else {
            switch (comboTiposCompPlanEst.getSelectedT()) {
                case ASI:
                    entidadEnEdicion = new SgAsignatura();
                    entidadEnEdicion.setCpeTipo(TipoComponentePlanEstudio.ASI);
                    esAsignaturaModulo = Boolean.TRUE;
                    break;
                case AES:
                    entidadEnEdicion = new SgActividadEspecial();
                    entidadEnEdicion.setCpeTipo(TipoComponentePlanEstudio.AES);
                    break;
                case ATE:
                    entidadEnEdicion = new SgActividadTiempoExtendido();
                    entidadEnEdicion.setCpeTipo(TipoComponentePlanEstudio.ATE);
                    break;
                case IND:
                    entidadEnEdicion = new SgArea();
                    entidadEnEdicion.setCpeTipo(TipoComponentePlanEstudio.IND);
                    esIndicador = Boolean.TRUE;
                    break;
                case MOD:
                    entidadEnEdicion = new SgModulo();
                    entidadEnEdicion.setCpeTipo(TipoComponentePlanEstudio.MOD);
                    esAsignaturaModulo = Boolean.TRUE;
                    break;
                case PRU:
                    entidadEnEdicion = new SgPrueba();
                    entidadEnEdicion.setCpeTipo(TipoComponentePlanEstudio.PRU);
                    break;
                default:
                    entidadEnEdicion = null;
                    break;
            }
        }
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgComponentePlanEstudio var) {
        try {
            esIndicador = Boolean.FALSE;
            esAsignaturaModulo = Boolean.FALSE;
            if (TipoComponentePlanEstudio.ASI.equals(var.getCpeTipo())) {
                SgAsignatura asig = asigrestClient.obtenerPorId(var.getCpePk());
                comboAreaAsigMod.setSelectedT(asig.getAsigAreaAsignaturaModulo());
                entidadEnEdicion = asig;
                esAsignaturaModulo = Boolean.TRUE;
            } else if (TipoComponentePlanEstudio.IND.equals(var.getCpeTipo())) {
                SgArea indicador = arearestClient.obtenerPorId(var.getCpePk());
                comboAreaIndicador.setSelectedT(indicador.getIndAreaIndicador());
                entidadEnEdicion = indicador;
                esIndicador = Boolean.TRUE;
            } else if (TipoComponentePlanEstudio.MOD.equals(var.getCpeTipo())) {
                SgModulo mod = modrestClient.obtenerPorId(var.getCpePk());
                comboAreaAsigMod.setSelectedT(mod.getModAreaAsignaturaModulo());
                entidadEnEdicion = mod;
                esAsignaturaModulo = Boolean.TRUE;
            } else {
                entidadEnEdicion = this.restClient.obtenerPorId(var.getCpePk());
                comboTiposPaes.setSelectedT(entidadEnEdicion.getCpeTipoPaes());
            }
            comboTiposCompPlanEst.setSelectedT(entidadEnEdicion.getCpeTipo());
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (null != comboTiposCompPlanEst.getSelectedT()) {
                switch (comboTiposCompPlanEst.getSelectedT()) {
                    case ASI:
                        SgAsignatura Asign = new SgAsignatura();
                        Asign = (SgAsignatura) entidadEnEdicion;
                        Asign.setAsigAreaAsignaturaModulo(comboAreaAsigMod.getSelectedT());
                        entidadEnEdicion = asigrestClient.guardar(Asign);
                        break;
                    case AES:
                        entidadEnEdicion = actEsprestClient.guardar(entidadEnEdicion);
                        break;
                    case ATE:
                        entidadEnEdicion = actTiemprestClient.guardar(entidadEnEdicion);
                        break;
                    case IND:
                        SgArea Indicador = new SgArea();
                        Indicador = (SgArea) entidadEnEdicion;
                        Indicador.setIndAreaIndicador(comboAreaIndicador.getSelectedT());
                        entidadEnEdicion = arearestClient.guardar(Indicador);
                        break;
                    case MOD:
                        SgModulo mod = new SgModulo();
                        mod = (SgModulo) entidadEnEdicion;
                        mod.setModAreaAsignaturaModulo(comboAreaAsigMod.getSelectedT());
                        entidadEnEdicion = modrestClient.guardar(mod);
                        break;
                    case PRU:
                        entidadEnEdicion.setCpeTipoPaes(this.comboTiposPaes.getSelectedT());
                        entidadEnEdicion = pruebrestClient.guardar(entidadEnEdicion);
                        break;
                    default:
                        break;
                }
            }

            buscar();
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            this.restClient.eliminar(entidadEnEdicion.getCpePk());
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
            historialComponentePlanEstudio = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroComponentePlanEstudio getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroComponentePlanEstudio filtro) {
        this.filtro = filtro;
    }

    public SgComponentePlanEstudio getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgComponentePlanEstudio entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialComponentePlanEstudio() {
        return historialComponentePlanEstudio;
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

    public LazyComponentePlanEstudioDataModel getComponentePlanEstudioLazyModel() {
        return componentePlanEstudioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyComponentePlanEstudioDataModel componentePlanEstudioLazyModel) {
        this.componentePlanEstudioLazyModel = componentePlanEstudioLazyModel;
    }

    public SofisComboG<TipoComponentePlanEstudio> getComboTiposCompPlanEst() {
        return comboTiposCompPlanEst;
    }

    public void setComboTiposCompPlanEst(SofisComboG<TipoComponentePlanEstudio> comboTiposCompPlanEst) {
        this.comboTiposCompPlanEst = comboTiposCompPlanEst;
    }

    public List<SgComponentePlanEstudio> getListaComponentePlanEstudio() {
        return listaComponentePlanEstudio;
    }

    public void setListaComponentePlanEstudio(List<SgComponentePlanEstudio> listaComponentePlanEstudio) {
        this.listaComponentePlanEstudio = listaComponentePlanEstudio;
    }

    public SofisComboG<TipoComponentePlanEstudio> getComboFiltroTiposCompPlanEst() {
        return comboFiltroTiposCompPlanEst;
    }

    public void setComboFiltroTiposCompPlanEst(SofisComboG<TipoComponentePlanEstudio> comboFiltroTiposCompPlanEst) {
        this.comboFiltroTiposCompPlanEst = comboFiltroTiposCompPlanEst;
    }

    public SofisComboG<SgAreaAsignaturaModulo> getComboAreaAsigMod() {
        return comboAreaAsigMod;
    }

    public void setComboAreaAsigMod(SofisComboG<SgAreaAsignaturaModulo> comboAreaAsigMod) {
        this.comboAreaAsigMod = comboAreaAsigMod;
    }

    public SofisComboG<SgAreaIndicador> getComboAreaIndicador() {
        return comboAreaIndicador;
    }

    public void setComboAreaIndicador(SofisComboG<SgAreaIndicador> comboAreaIndicador) {
        this.comboAreaIndicador = comboAreaIndicador;
    }

    public Boolean getEsIndicador() {
        return esIndicador;
    }

    public void setEsIndicador(Boolean esIndicador) {
        this.esIndicador = esIndicador;
    }

    public Boolean getEsAsignaturaModulo() {
        return esAsignaturaModulo;
    }

    public void setEsAsignaturaModulo(Boolean esAsignaturaModulo) {
        this.esAsignaturaModulo = esAsignaturaModulo;
    }

    public SofisComboG<EnumTipoPruebaPaes> getComboTiposPaes() {
        return comboTiposPaes;
    }

    public void setComboTiposPaes(SofisComboG<EnumTipoPruebaPaes> comboTiposPaes) {
        this.comboTiposPaes = comboTiposPaes;
    }
    
}
