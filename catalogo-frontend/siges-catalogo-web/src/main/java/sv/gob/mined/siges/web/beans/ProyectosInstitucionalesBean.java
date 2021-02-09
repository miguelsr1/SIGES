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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgProgramaInstitucional;
import sv.gob.mined.siges.web.dto.SgProyectoInstitucional;
import sv.gob.mined.siges.web.enumerados.EnumTipoBeneficio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstitucional;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyProyectoInstitucionalDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ProgramaInstitucionalRestClient;
import sv.gob.mined.siges.web.restclient.ProyectoInstitucionalRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ProyectosInstitucionalesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProyectosInstitucionalesBean.class.getName());

    @Inject
    private ProyectoInstitucionalRestClient restClient;
    
    @Inject
    private ProgramaInstitucionalRestClient restPorgramaInstitucional;

    private FiltroProyectoInstitucional filtro = new FiltroProyectoInstitucional();
    private SgProyectoInstitucional entidadEnEdicion = new SgProyectoInstitucional();
    private List<SgProyectoInstitucional> historialProyectoInstitucional = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyProyectoInstitucionalDataModel proyectoInstitucionalLazyModel;
    private SofisComboG<EnumTipoBeneficio> comboTipoBeneficio;
    private SofisComboG<SgProgramaInstitucional> comboProgramaInstitucional;

    public ProyectosInstitucionalesBean() {
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
            filtro.setInicializarBeneficios(Boolean.TRUE);
            filtro.setTipoBeneficio(comboTipoBeneficio.getSelectedT());
            filtro.setProgramaInstitucional(comboProgramaInstitucional.getSelectedT()!=null?comboProgramaInstitucional.getSelectedT().getPinPk():null);
            totalResultados = restClient.buscarTotal(filtro);
            proyectoInstitucionalLazyModel = new LazyProyectoInstitucionalDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
            comboTipoBeneficio = new SofisComboG(new ArrayList(Arrays.asList(EnumTipoBeneficio.values())), "text");
            comboTipoBeneficio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));


            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"pinNombre"});
            comboProgramaInstitucional = new SofisComboG(restPorgramaInstitucional.buscar(fc), "pinNombre");
            comboProgramaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroProyectoInstitucional();
        comboTipoBeneficio.setSelected(-1);
        comboProgramaInstitucional.setSelected(-1);
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgProyectoInstitucional();
    }

    public void actualizar(SgProyectoInstitucional var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgProyectoInstitucional) SerializationUtils.clone(var);
    }

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

    public void eliminar() {
        try {
            
            restClient.eliminar(entidadEnEdicion.getPinPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialProyectoInstitucional = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroProyectoInstitucional getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroProyectoInstitucional filtro) {
        this.filtro = filtro;
    }

    public SgProyectoInstitucional getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgProyectoInstitucional entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgProyectoInstitucional> getHistorialProyectoInstitucional() {
        return historialProyectoInstitucional;
    }

    public void setHistorialProyectoInstitucional(List<SgProyectoInstitucional> historialProyectoInstitucional) {
        this.historialProyectoInstitucional = historialProyectoInstitucional;
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

    public LazyProyectoInstitucionalDataModel getProyectoInstitucionalLazyModel() {
        return proyectoInstitucionalLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyProyectoInstitucionalDataModel proyectoInstitucionalLazyModel) {
        this.proyectoInstitucionalLazyModel = proyectoInstitucionalLazyModel;
    }

    public ProyectoInstitucionalRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ProyectoInstitucionalRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<EnumTipoBeneficio> getComboTipoBeneficio() {
        return comboTipoBeneficio;
    }

    public void setComboTipoBeneficio(SofisComboG<EnumTipoBeneficio> comboTipoBeneficio) {
        this.comboTipoBeneficio = comboTipoBeneficio;
    }

    public SofisComboG<SgProgramaInstitucional> getComboProgramaInstitucional() {
        return comboProgramaInstitucional;
    }

    public void setComboProgramaInstitucional(SofisComboG<SgProgramaInstitucional> comboProgramaInstitucional) {
        this.comboProgramaInstitucional = comboProgramaInstitucional;
    }

}
