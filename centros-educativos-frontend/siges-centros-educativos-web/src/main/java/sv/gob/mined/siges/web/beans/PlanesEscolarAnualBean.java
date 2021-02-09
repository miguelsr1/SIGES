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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgPlanEscolarAnual;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanEscolarAnual;
import sv.gob.mined.siges.web.lazymodels.LazyPlanEscolarAnualDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlanEscolarAnualRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPEA;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PlanesEscolarAnualBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PlanesEscolarAnualBean.class.getName());

    @Inject
    private PlanEscolarAnualRestClient restClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private AnioLectivoRestClient anioLectivoClient;

    private FiltroPlanEscolarAnual filtro = new FiltroPlanEscolarAnual();
    private SgPlanEscolarAnual entidadEnEdicion = new SgPlanEscolarAnual();
    private List<RevHistorico> historialPlanEscolarAnual = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyPlanEscolarAnualDataModel propuestaPedagogicaLazyModel;
    private SgSede sedeSeleccionada;
    private SofisComboG<EnumEstadoPEA> comboEstadoPEA;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;

    public PlanesEscolarAnualBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_PLAN_ESCOLAR_ANUAL)) {
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
            filtro.setPeaSede(sedeSeleccionada!=null?sedeSeleccionada.getSedPk():null);
            filtro.setEstadoPea(comboEstadoPEA!=null?comboEstadoPEA.getSelectedT():null);
            filtro.setPeaAnioLectivo(comboAnioLectivo!=null?comboAnioLectivo.getSelectedT()!=null?comboAnioLectivo.getSelectedT().getAlePk():null:null);
            filtro.setIncluirCampos(new String[]{"peaPk", "peaVersion", "peaUltModFecha", "peaUltModUsuario", 
                "peaSede.sedCodigo", 
                "peaSede.sedNombre",
                "peaSede.sedTipo",
                "peaEstado", 
                "peaPropuestaPedagogica.ppeNombre", 
                "peaAnioLectivo.aleAnio"});
            totalResultados = restClient.buscarTotal(filtro);
            propuestaPedagogicaLazyModel = new LazyPlanEscolarAnualDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {

            List<EnumEstadoPEA> estados = new ArrayList(Arrays.asList(EnumEstadoPEA.values()));
            comboEstadoPEA = new SofisComboG(estados, "text");
            comboEstadoPEA.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroAnioLectivo fc2 = new FiltroAnioLectivo();
            fc2.setOrderBy(new String[]{"aleAnio"});
            fc2.setAscending(new boolean[]{false});
            fc2.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            comboAnioLectivo = new SofisComboG(anioLectivoClient.buscar(fc2), "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        
    }

    public void limpiar() {
        filtro = new FiltroPlanEscolarAnual();
        comboAnioLectivo.setSelected(-1);
        comboEstadoPEA.setSelected(-1);
        sedeSeleccionada = null;
        buscar();
    }

    public void actualizar(SgPlanEscolarAnual var) {
        limpiarCombos();
        entidadEnEdicion = (SgPlanEscolarAnual) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPeaPk());
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
            historialPlanEscolarAnual = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroPlanEscolarAnual getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPlanEscolarAnual filtro) {
        this.filtro = filtro;
    }

    public SgPlanEscolarAnual getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPlanEscolarAnual entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialPlanEscolarAnual() {
        return historialPlanEscolarAnual;
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

    public LazyPlanEscolarAnualDataModel getPlanEscolarAnualLazyModel() {
        return propuestaPedagogicaLazyModel;
    }

    public void setTiposPlanEscolarAnualLazyModel(LazyPlanEscolarAnualDataModel propuestaPedagogicaLazyModel) {
        this.propuestaPedagogicaLazyModel = propuestaPedagogicaLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<EnumEstadoPEA> getComboEstadoPEA() {
        return comboEstadoPEA;
    }

    public void setComboEstadoPEA(SofisComboG<EnumEstadoPEA> comboEstadoPEA) {
        this.comboEstadoPEA = comboEstadoPEA;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

}
