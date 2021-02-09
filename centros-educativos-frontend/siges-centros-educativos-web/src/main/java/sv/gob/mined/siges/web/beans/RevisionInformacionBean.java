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
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgReporteDirector;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReporteDirector;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.lazymodels.LazyReporteDirectorDataModel;
import sv.gob.mined.siges.web.lazymodels.LazySedesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ReporteDirectorRestClient;
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
public class RevisionInformacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RevisionInformacionBean.class.getName());

    @Inject
    private ReporteDirectorRestClient restClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private CatalogosRestClient catalogoClient;
    
    @Inject
    private SessionBean sessionBean;

    private FiltroReporteDirector filtro = new FiltroReporteDirector();
    private SgReporteDirector entidadEnEdicion = new SgReporteDirector();
    private List<SgReporteDirector> reporteDirectorList = new ArrayList();
    private Integer paginado = 10;
    private Integer paginadoSede = 10;
    private Long totalResultados;
    private Long totalResultadosSede;
    private SofisComboG<SgDepartamento> comboDepartamentos = new SofisComboG<>();
    private SofisComboG<SgMunicipio> comboMunicipios = new SofisComboG<>();
    private List<SgSede> sedeSinRevisar = new ArrayList();
    private Boolean realizadaRevision = Boolean.TRUE;
    private Boolean revisadoSede = Boolean.FALSE;
    private Boolean revisadoEstudiante = Boolean.FALSE;
    private Boolean revisadoPersonal = Boolean.FALSE;
    private LazySedesDataModel sedesLazyModel;
    private LazyReporteDirectorDataModel reporteLazyModel;

    public RevisionInformacionBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REVISION_INFORMACION)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setSedDepartamentoFk(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setSedMunicipioFk(comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
            if (revisadoEstudiante.equals(Boolean.FALSE) && revisadoSede.equals(Boolean.FALSE) && revisadoPersonal.equals(Boolean.FALSE)) {
                filtro.setRdiDatosGeneral(realizadaRevision);
            }
            filtro.setRdiDatosEstudiante(revisadoEstudiante ? realizadaRevision : null);
            filtro.setRdiDatosSede(revisadoSede ? realizadaRevision : null);
            filtro.setRdiDatosPersonal(revisadoPersonal ? realizadaRevision : null);
            filtro.setIncluirCampos(new String[]{
                "rdiSede.sedCodigo",
                "rdiSede.sedNombre",
                "rdiSede.sedDireccion.dirDepartamento.depNombre",
                "rdiSede.sedDireccion.dirMunicipio.munNombre",
                "rdiSede.sedVersion",
                "rdiSede.sedPk",
                "rdiSede.sedTipo",
                "rdiDatosSede",
                "rdiConObservacionesSede",
                "rdiDatosEstudiante",
                "rdiConObservacionesEstudiante",
                "rdiDatosPersonal",
                "rdiConObservacionesPersonal"});

            totalResultados = restClient.buscarTotal(filtro);
            reporteLazyModel = new LazyReporteDirectorDataModel(restClient, filtro, totalResultados, paginado);

            if (!realizadaRevision) {
                buscarSedeSinRevisar();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarSedeSinRevisar() {
        try {
            FiltroSedes fs = new FiltroSedes();
            fs.setSedRevisado(Boolean.FALSE);
            fs.setSedDepartamentoId(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            fs.setSedMunicipioId(comboMunicipios.getSelectedT() != null ? comboMunicipios.getSelectedT().getMunPk() : null);
            fs.setSedCodigo(filtro.getSedCodigo());
            fs.setSedNombre(filtro.getSedNombre());
            totalResultadosSede = sedeRestClient.buscarTotal(fs);
            sedesLazyModel = new LazySedesDataModel(sedeRestClient, fs, totalResultadosSede, paginadoSede);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = catalogoClient.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipios = new SofisComboG<>();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboDepartamentos.setSelected(-1);
        comboMunicipios = new SofisComboG<>();
        comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void limpiar() {
        limpiarCombos();
        filtro = new FiltroReporteDirector();
        revisadoSede = Boolean.FALSE;
        revisadoEstudiante = Boolean.FALSE;
        revisadoPersonal = Boolean.FALSE;
        realizadaRevision = Boolean.TRUE;
        buscar();
    }

    public void agregar() {
        limpiarCombos();
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
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarDepartamento() {
        try {
            comboMunicipios = new SofisComboG();
            comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (comboDepartamentos.getSelectedT() != null) {
                FiltroMunicipio filtro = new FiltroMunicipio();
                filtro.setOrderBy(new String[]{"munNombre"});
                filtro.setAscending(new boolean[]{true});
                filtro.setMunDepartamentoFk(comboDepartamentos.getSelectedT().getDepPk());
                filtro.setIncluirCampos(new String[]{"munNombre", "munVersion"});       
                List<SgMunicipio> municipios = catalogoClient.buscarMunicipio(filtro);
                comboMunicipios = new SofisComboG(municipios, "munNombre");
                comboMunicipios.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public FiltroReporteDirector getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroReporteDirector filtro) {
        this.filtro = filtro;
    }

    public SgReporteDirector getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgReporteDirector entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgReporteDirector> getReporteDirectorList() {
        return reporteDirectorList;
    }

    public void setReporteDirectorList(List<SgReporteDirector> reporteDirectorList) {
        this.reporteDirectorList = reporteDirectorList;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SofisComboG<SgMunicipio> getComboMunicipios() {
        return comboMunicipios;
    }

    public void setComboMunicipios(SofisComboG<SgMunicipio> comboMunicipios) {
        this.comboMunicipios = comboMunicipios;
    }

    public List<SgSede> getSedeSinRevisar() {
        return sedeSinRevisar;
    }

    public void setSedeSinRevisar(List<SgSede> sedeSinRevisar) {
        this.sedeSinRevisar = sedeSinRevisar;
    }

    public Boolean getRealizadaRevision() {
        return realizadaRevision;
    }

    public void setRealizadaRevision(Boolean realizadaRevision) {
        this.realizadaRevision = realizadaRevision;
    }

    public Boolean getRevisadoSede() {
        return revisadoSede;
    }

    public void setRevisadoSede(Boolean revisadoSede) {
        this.revisadoSede = revisadoSede;
    }

    public Boolean getRevisadoEstudiante() {
        return revisadoEstudiante;
    }

    public void setRevisadoEstudiante(Boolean revisadoEstudiante) {
        this.revisadoEstudiante = revisadoEstudiante;
    }

    public Boolean getRevisadoPersonal() {
        return revisadoPersonal;
    }

    public void setRevisadoPersonal(Boolean revisadoPersonal) {
        this.revisadoPersonal = revisadoPersonal;
    }

    public LazySedesDataModel getSedesLazyModel() {
        return sedesLazyModel;
    }

    public void setSedesLazyModel(LazySedesDataModel sedesLazyModel) {
        this.sedesLazyModel = sedesLazyModel;
    }

    public Long getTotalResultadosSede() {
        return totalResultadosSede;
    }

    public void setTotalResultadosSede(Long totalResultadosSede) {
        this.totalResultadosSede = totalResultadosSede;
    }

    public LazyReporteDirectorDataModel getReporteLazyModel() {
        return reporteLazyModel;
    }

    public void setReporteLazyModel(LazyReporteDirectorDataModel reporteLazyModel) {
        this.reporteLazyModel = reporteLazyModel;
    }

    public Integer getPaginadoSede() {
        return paginadoSede;
    }

    public void setPaginadoSede(Integer paginadoSede) {
        this.paginadoSede = paginadoSede;
    }

}
