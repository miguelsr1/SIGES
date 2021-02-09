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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCursoDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCursoDocente;
import sv.gob.mined.siges.web.lazymodels.LazyCursoDocenteDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CursoDocenteRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgModuloFormacionDocente;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaCurso;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.enumerados.EnumEstadoCurso;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloFormacionDocente;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.restclient.ModuloFormacionDocenteRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class CursosDocentesDisponiblesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CursosDocentesDisponiblesBean.class.getName());

    @Inject
    private CursoDocenteRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private ModuloFormacionDocenteRestClient restModulo;

    private FiltroCursoDocente filtro = new FiltroCursoDocente();
    private LazyCursoDocenteDataModel cursoDocenteLazyModel;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private List<RevHistorico> historialCursoDocente = new ArrayList();
    private SgCursoDocente entidadEnEdicion = new SgCursoDocente();
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgCategoriaCurso> comboCategoriaCurso;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgEspecialidad> comboEspecialidad;
    private SofisComboG<SgModuloFormacionDocente> comboModulo;

    public CursosDocentesDisponiblesBean() {
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
    
    public void forceInit() {
        //Utilizado para forzar init de CursosDocentesBean antes que FiltrosSeccionBean
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CURSOS_DOCENTES_DISPONIBLES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            filtro.setFirst(new Long(0));

            filtro.setCdsModulo(comboModulo.getSelectedT() != null ? comboModulo.getSelectedT().getMfdPk() : null);
            filtro.setCdsCategoria(comboCategoriaCurso.getSelectedT() != null ? comboCategoriaCurso.getSelectedT().getCcuPk() : null);
            filtro.setCdsNivel(comboNivel.getSelectedT() != null ? comboNivel.getSelectedT().getNivPk() : null);
            filtro.setCdsEspecialidad(comboEspecialidad.getSelectedT() != null ? comboEspecialidad.getSelectedT().getEspPk() : null);
            filtro.setCdsDepartamento(comboDepartamento.getSelectedT() != null ? comboDepartamento.getSelectedT().getDepPk() : null);
            filtro.setCdsEstado(EnumEstadoCurso.PUBLICADO);
            filtro.setIncluirCampos(new String[]{"cdsPk" ,
            "cdsSede.sedDireccion.dirDepartamento.depNombre",
            "cdsSede.sedDireccion.dirMunicipio.munNombre",
            "cdsSede.sedTipo",
            "cdsSede.sedNombre",
            "cdsFechaInicio",
            "cdsFechaFin",
            "cdsNombre",
            "cdsSedeNombre",
            "cdsOtraSede",
            "cdsSedeDireccion.dirDepartamento.depNombre",
            "cdsNivel.nivNombre",
            "cdsEspecialidad.espNombre", 
            "cdsEstado",
            "cdsUltModUsuario", "cdsVersion"});
            
            totalResultados = restClient.buscarTotal(filtro);
            cursoDocenteLazyModel = new LazyCursoDocenteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> deptos = restCatalogo.buscarDepartamento(fc);
            comboDepartamento = new SofisComboG(deptos, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            
            fc.setOrderBy(new String[]{"ccuNombre"});
            fc.setIncluirCampos(null);
            List<SgCategoriaCurso> lcc = restCatalogo.buscarCategoriaCurso(fc);
            comboCategoriaCurso = new SofisComboG(lcc, "ccuNombre");
            comboCategoriaCurso.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroModuloFormacionDocente fMod = new FiltroModuloFormacionDocente();
            fMod.setHabilitado(Boolean.TRUE);
            fMod.setAscending(new boolean[]{true});
            fMod.setOrderBy(new String[]{"mfdNombre"});
            List<SgModuloFormacionDocente> lmodulo = restModulo.buscar(fMod);
            comboModulo = new SofisComboG(lmodulo, "mfdNombre");
            comboModulo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroEspecialidad fesp = new FiltroEspecialidad();
            fesp.setHabilitado(Boolean.TRUE);
            fesp.setAscending(new boolean[]{true});
            fesp.setOrderBy(new String[]{"espNombre"});
            fc.setOrderBy(new String[]{"espNombre"});
            fc.setIncluirCampos(null);
            comboEspecialidad = new SofisComboG(restCatalogo.buscarEspecialidad(fesp), "espNombre");
            comboEspecialidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroNivel fNivel = new FiltroNivel();
            fNivel.setIncluirCampos(new String[]{"nivNombre", "nivVersion"});
            fNivel.setAscending(new boolean[]{true});
            fNivel.setNivHabilitado(Boolean.TRUE);
            fNivel.setOrderBy(new String[]{"nivNombre"});
            List<SgNivel> niveles = restNivel.buscar(fNivel);
            comboNivel = new SofisComboG(niveles, "nivNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

          
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }


    public void limpiar() {
        filtro = new FiltroCursoDocente();
        comboDepartamento.setSelected(-1);
        comboCategoriaCurso.setSelected(-1);
        comboEspecialidad.setSelected(-1);
        comboModulo.setSelected(-1);
        comboNivel.setSelected(-1);
        buscar();
    }

    public String historial(Long id) {
        try {
            historialCursoDocente = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public CursoDocenteRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(CursoDocenteRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroCursoDocente getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCursoDocente filtro) {
        this.filtro = filtro;
    }

    public LazyCursoDocenteDataModel getCursoDocenteLazyModel() {
        return cursoDocenteLazyModel;
    }

    public void setCursoDocenteLazyModel(LazyCursoDocenteDataModel cursoDocenteLazyModel) {
        this.cursoDocenteLazyModel = cursoDocenteLazyModel;
    }

    public SgCursoDocente getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCursoDocente entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCursoDocente() {
        return historialCursoDocente;
    }

    public void setHistorialCursoDocente(List<RevHistorico> historialCursoDocente) {
        this.historialCursoDocente = historialCursoDocente;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgCategoriaCurso> getComboCategoriaCurso() {
        return comboCategoriaCurso;
    }

    public void setComboCategoriaCurso(SofisComboG<SgCategoriaCurso> comboCategoriaCurso) {
        this.comboCategoriaCurso = comboCategoriaCurso;
    }

    public SofisComboG<SgNivel> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivel> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public SofisComboG<SgEspecialidad> getComboEspecialidad() {
        return comboEspecialidad;
    }

    public void setComboEspecialidad(SofisComboG<SgEspecialidad> comboEspecialidad) {
        this.comboEspecialidad = comboEspecialidad;
    }

    public SofisComboG<SgModuloFormacionDocente> getComboModulo() {
        return comboModulo;
    }

    public void setComboModulo(SofisComboG<SgModuloFormacionDocente> comboModulo) {
        this.comboModulo = comboModulo;
    }



}
