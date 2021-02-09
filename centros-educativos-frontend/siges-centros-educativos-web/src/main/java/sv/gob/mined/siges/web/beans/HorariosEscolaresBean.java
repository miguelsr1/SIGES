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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgHorarioEscolar;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgOrganizacionCurricular;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHorarioEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyHorarioEscolarDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.HorarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.OrganizacionCurricularRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class HorariosEscolaresBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HorarioEscolarBean.class.getName());

    @Inject
    private HorarioEscolarRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private OrganizacionCurricularRestClient organizacionCurricularRestClient;

    @Inject
    private NivelRestClient nivelRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroHorarioEscolar filtro = new FiltroHorarioEscolar();
    private SgHorarioEscolar entidadEnEdicion = new SgHorarioEscolar();
    private List<RevHistorico> historialHorarioEscolar = new ArrayList();
    private List<SgHorarioEscolar> listHorarioEscolar = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyHorarioEscolarDataModel horarioEscolarLazyModel;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgOrganizacionCurricular> comboOrganizacionCurricular;
    private SofisComboG<SgNivel> comboNivel;

    public HorariosEscolaresBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();          

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_HORARIOS_ESCOLARES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {

            filtro.setHesCentroEducativo(sedeSeleccionada != null ? sedeSeleccionada.getSedPk() : null);
            filtro.setHesOrganizacionCurricular(comboOrganizacionCurricular.getSelectedT() != null ? comboOrganizacionCurricular.getSelectedT().getOcuPk() : null);
            filtro.setHesNivel(comboNivel.getSelectedT() != null ? comboNivel.getSelectedT().getNivPk() : null);
            filtro.setIncluirCampos(new String[]{"hesSeccion.secNombre", "hesSeccion.secAnioLectivo.aleAnio", 
                "hesSeccion.secServicioEducativo.sduGrado.graNombre", 
                "hesSeccion.secPlanEstudio.pesRelacionModalidad.reaModalidadEducativa.modNombre",
                "hesSeccion.secPlanEstudio.pesRelacionModalidad.reaModalidadAtencion.matNombre",
                "hesSeccion.secPlanEstudio.pesRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "hesSeccion.secPlanEstudio.pesOpcion.opcNombre",
                "hesSeccion.secPlanEstudio.pesProgramaEducativo.pedNombre",
                "hesUltModFecha", "hesUltModUsuario"});

            totalResultados = restClient.buscarTotal(filtro);
            horarioEscolarLazyModel = new LazyHorarioEscolarDataModel(restClient, filtro, totalResultados, paginado);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            comboOrganizacionCurricular = new SofisComboG(organizacionCurricularRestClient.buscar(fc), "ocuNombre");
            comboOrganizacionCurricular.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboNivel = new SofisComboG();
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarOrganizacionCurricular() {
        try {
            if (comboOrganizacionCurricular.getSelectedT() != null) {
                FiltroNivel fn = new FiltroNivel();
                fn.setOrganizacionCurricularPk(comboOrganizacionCurricular.getSelectedT().getOcuPk());
                comboNivel = new SofisComboG(nivelRestClient.buscar(fn), "nivNombre");
                comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        sedeSeleccionada = null;
        comboOrganizacionCurricular.setSelected(0);
        comboNivel.setSelected(0);
        filtro = new FiltroHorarioEscolar();
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo","sedNombre", "sedVersion", "sedTipo"});
            return sedeRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void actualizar(SgHorarioEscolar var) {
        limpiarCombos();
        entidadEnEdicion = (SgHorarioEscolar) SerializationUtils.clone(var);

    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getHesPk());
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
            historialHorarioEscolar = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroHorarioEscolar getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroHorarioEscolar filtro) {
        this.filtro = filtro;
    }

    public SgHorarioEscolar getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgHorarioEscolar entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public HorarioEscolarRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(HorarioEscolarRestClient restClient) {
        this.restClient = restClient;
    }

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
    }

    public OrganizacionCurricularRestClient getOrganizacionCurricularRestClient() {
        return organizacionCurricularRestClient;
    }

    public void setOrganizacionCurricularRestClient(OrganizacionCurricularRestClient organizacionCurricularRestClient) {
        this.organizacionCurricularRestClient = organizacionCurricularRestClient;
    }

    public NivelRestClient getNivelRestClient() {
        return nivelRestClient;
    }

    public void setNivelRestClient(NivelRestClient nivelRestClient) {
        this.nivelRestClient = nivelRestClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public List<RevHistorico> getHistorialHorarioEscolar() {
        return historialHorarioEscolar;
    }

    public void setHistorialHorarioEscolar(List<RevHistorico> historialHorarioEscolar) {
        this.historialHorarioEscolar = historialHorarioEscolar;
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

    public LazyHorarioEscolarDataModel getHorarioEscolarLazyModel() {
        return horarioEscolarLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyHorarioEscolarDataModel horarioEscolarLazyModel) {
        this.horarioEscolarLazyModel = horarioEscolarLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<SgHorarioEscolar> getListHorarioEscolar() {
        return listHorarioEscolar;
    }

    public void setListHorarioEscolar(List<SgHorarioEscolar> listHorarioEscolar) {
        this.listHorarioEscolar = listHorarioEscolar;
    }

    public SofisComboG<SgOrganizacionCurricular> getComboOrganizacionCurricular() {
        return comboOrganizacionCurricular;
    }

    public void setComboOrganizacionCurricular(SofisComboG<SgOrganizacionCurricular> comboOrganizacionCurricular) {
        this.comboOrganizacionCurricular = comboOrganizacionCurricular;
    }

    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

}
