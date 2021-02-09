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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.lazymodels.LazyEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgMunicipio;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroMunicipio;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EstudiantesSinNIEBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudiantesBean.class.getName());

    @Inject
    private EstudianteRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;
    
    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    @Inject
    private CatalogosRestClient restCatalogo;

    private FiltroEstudiante filtro = new FiltroEstudiante();
    private LazyEstudianteDataModel estudianteLazyModel;
    private List<RevHistorico> historial = new ArrayList();
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private Integer paginado = 10;
    private Long totalResultados;
    private Boolean panelAvanzado = Boolean.FALSE;
    private String txtFiltroAvanzado;
    private Boolean buscarMatAbierta = Boolean.TRUE;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgMunicipio> comboMunicipio;

    public EstudiantesSinNIEBean() {
    }

    @PostConstruct
    public void init() {
        try {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public String buscar() {
        try {
            filtro.setMatAnulada(Boolean.FALSE);//20-12-2019 Rodrigo Perez pidió que filtre estudiantes sin nie con ult matrícula no anulada

            filtro.setMatAnulada(Boolean.FALSE);//20-12-2019 Rodrigo Perez pidió que filtre estudiantes sin nie con ult matrícula no anulada
            
            filtro.setSecAnioLectivoFk(comboAnioLectivo.getSelectedT() != null ? comboAnioLectivo.getSelectedT().getAlePk() : null);
            filtro.setEstEstadoMatricula(comboAnioLectivo.getSelectedT() != null ? EnumMatriculaEstado.ABIERTO : null);
            filtro.setEstSinNie(Boolean.TRUE);
            filtro.setBuscarSoloEnUltimaMatricula(Boolean.TRUE);
            filtro.setIncluirCampos(new String[]{"estPersona.perNie",
                "estPersona.perPrimerNombre", "estPersona.perSegundoNombre",
                "estPersona.perPrimerApellido", "estPersona.perSegundoApellido",
            "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTipo",
            "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedPk",
            "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedCodigo",
            "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedNombre",
            "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
            "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre"
            });
            filtro.setEstEstadoMatricula(buscarMatAbierta ? EnumMatriculaEstado.ABIERTO : null);
            filtro.setEstDepartamentoMatricula(comboDepartamento!=null && comboDepartamento.getSelectedT()!=null?comboDepartamento.getSelectedT().getDepPk():null);
            filtro.setEstMunicipioMatricula(comboMunicipio!=null && comboMunicipio.getSelectedT()!=null?comboMunicipio.getSelectedT().getMunPk():null);

            totalResultados = restClient.buscarTotal(filtro);
            estudianteLazyModel = new LazyEstudianteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroAnioLectivo fal = new FiltroAnioLectivo();
            fal.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            fal.setOrderBy(new String[]{"aleAnio"});
            fal.setAscending(new boolean[]{false});

            List<SgAnioLectivo> listAnio = anioLectivoRestClient.buscar(fal);
            comboAnioLectivo = new SofisComboG(listAnio, "aleAnio");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroCodiguera fCod = new FiltroCodiguera();
            fCod.setOrderBy(new String[]{"depNombre"});
            fCod.setAscending(new boolean[]{true});
            fCod.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> departamentos = restCatalogo.buscarDepartamento(fCod);
            comboDepartamento = new SofisComboG(new ArrayList(departamentos), "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarDepartamento() {
        try {
            comboMunicipio = new SofisComboG();
            comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboDepartamento.getSelectedT() != null) {
                FiltroMunicipio fCod = new FiltroMunicipio();
                fCod.setOrderBy(new String[]{"munNombre"});
                fCod.setAscending(new boolean[]{true});
                fCod.setIncluirCampos(new String[]{"munNombre", "munVersion"});
                fCod.setMunDepartamentoFk(comboDepartamento.getSelectedT().getDepPk());          
                List<SgMunicipio> municipios = restCatalogo.buscarMunicipio(fCod);
                comboMunicipio = new SofisComboG(municipios, "munNombre");
                comboMunicipio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboAnioLectivo.setSelected(-1);
        if (this.filtrosSeccion.getFiltro() != null){
            this.filtrosSeccion.limpiarCombos();
        }
        comboDepartamento.setSelected(-1);
        comboMunicipio.setSelected(-1);
    }

    public String limpiar() {
        limpiarCombos();
        filtro = new FiltroEstudiante();
        this.filtrosSeccion.setFiltro(filtro);
        this.filtrosSeccion.setSedeSeleccionada(null);
        buscarMatAbierta = Boolean.TRUE;
        totalResultados = null;
        estudianteLazyModel = null;
        return null;
    }

    public String historial(Long id) {
        try {
            historial = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public String generarLinkSede(Integer id, Boolean editar) {
        try {
            return "A";
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            return null;
        }
    }
    
    public void verPanelAvanzado() {
        if (panelAvanzado) {
            panelAvanzado = false;
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaM");
        } else {
            txtFiltroAvanzado = Etiquetas.getValue("busquedaAvanzadaL");
            panelAvanzado = true;
        }
        inicializarFiltrosSeccion();
    }
    
    public void inicializarFiltrosSeccion() {
        if (this.filtrosSeccion.getFiltro() == null) {
            this.filtrosSeccion.setFiltro(this.filtro);
            this.filtrosSeccion.cargarCombos();
        }
    }

    public FiltroEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEstudiante filtro) {
        this.filtro = filtro;
    }

    public List<RevHistorico> getHistorialTipoCalendario() {
        return historial;
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

    public LazyEstudianteDataModel getEstudianteLazyModel() {
        return estudianteLazyModel;
    }

    public void setEstudianteLazyModel(LazyEstudianteDataModel estudianteLazyModel) {
        this.estudianteLazyModel = estudianteLazyModel;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public Boolean getPanelAvanzado() {
        return panelAvanzado;
    }

    public void setPanelAvanzado(Boolean panelAvanzado) {
        this.panelAvanzado = panelAvanzado;
    }

    public String getTxtFiltroAvanzado() {
        return txtFiltroAvanzado;
    }

    public void setTxtFiltroAvanzado(String txtFiltroAvanzado) {
        this.txtFiltroAvanzado = txtFiltroAvanzado;
    }

    public Boolean getBuscarMatAbierta() {
        return buscarMatAbierta;
    }

    public void setBuscarMatAbierta(Boolean buscarMatAbierta) {
        this.buscarMatAbierta = buscarMatAbierta;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgMunicipio> getComboMunicipio() {
        return comboMunicipio;
    }

    public void setComboMunicipio(SofisComboG<SgMunicipio> comboMunicipio) {
        this.comboMunicipio = comboMunicipio;
    }

}
