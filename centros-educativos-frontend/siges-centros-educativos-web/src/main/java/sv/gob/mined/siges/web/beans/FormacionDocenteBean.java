/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgFormacionDocente;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaFormacionDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgModuloFormacionDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelFormacionDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFormacionDocente;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyFormacionDocenteDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CapacitacionRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.FormacionDocenteRestClient;
import sv.gob.mined.siges.web.restclient.EstudioSuperiorRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class FormacionDocenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FormacionDocenteBean.class.getName());

    @Inject
    private FormacionDocenteRestClient restClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SedeRestClient restSedes;

    @Inject
    private CapacitacionRestClient restCapacitaciones;

    @Inject
    private EstudioSuperiorRestClient restEstudioSuperior;

    @Inject
    private SessionBean sessionBean;

    private SgFormacionDocente entidadEnEdicion = new SgFormacionDocente();
    private String formatoSeleccionado = "csv";
    private FiltroFormacionDocente filtro = new FiltroFormacionDocente();
    private Integer paginado = 10;
    private Long totalResultados;
    private SgSede sedeSelecionada;
    private LazyFormacionDocenteDataModel formacionDocenteLazyModel;
    private SofisComboG<SgNivelFormacionDocente> comboNivel;
    private SofisComboG<SgCategoriaFormacionDocente> comboCategoria;
    private SofisComboG<SgEspecialidad> comboEspecialidad;
    private SofisComboG<SgModuloFormacionDocente> comboModulo;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SgPersonalSedeEducativa personalSede;
    private Boolean soloLectura = Boolean.FALSE;

    public FormacionDocenteBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_FORMACIONES_DOCENTE)) {
            LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. "+ ConstantesOperaciones.BUSCAR_FORMACIONES_DOCENTE);
            JSFUtils.redirectToIndex();
        }
    }

    public void personalSedeEducativa(SgPersonalSedeEducativa var) {
        try {
            personalSede = var;
            FiltroFormacionDocente filtrof = new FiltroFormacionDocente();
            filtrof.setIncluirCampos(new String[]{
                "fdoNivel.nfdPk",
                "fdoNivel.nfdNombre",
                "fdoNivel.nfdVersion",
                "fdoCategoria.cfdPk",
                "fdoCategoria.cfdNombre",
                "fdoCategoria.cfdVersion",
                "fdoEspecialidad.espPk",
                "fdoEspecialidad.espNombre",
                "fdoEspecialidad.espVersion",
                "fdoModulo.mfdPk",
                "fdoModulo.mfdNombre",
                "fdoModulo.mfdVersion",
                "fdoDepartamento.depPk",
                "fdoDepartamento.depNombre",
                "fdoDepartamento.depVersion",
                "fdoFechaDesde",
                "fdoFechaHasta",
                "fdoSede.sedPk",
                "fdoSede.sedCodigo",
                "fdoSede.sedNombre",
                "fdoSede.sedTipo",
                "fdoAprobado",
                "fdoCalificacionFinal",
                "fdoVersion"
            });
            filtrof.setFmdPersonalSede(personalSede.getPsePk());
            var.setPseFormacionDocente(restClient.buscar(filtrof));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setFmdPersonalSede(personalSede.getPsePk());
            totalResultados = restClient.buscarTotal(filtro);
            formacionDocenteLazyModel = new LazyFormacionDocenteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try {

            FiltroCodiguera filtroCod = new FiltroCodiguera();
            filtroCod.setAscending(new boolean[]{true});
            filtroCod.setHabilitado(Boolean.TRUE);

            filtroCod.setOrderBy(new String[]{"nfdNombre"});
            filtroCod.setIncluirCampos(new String[]{"nfdNombre", "nfdVersion"});
            List<SgNivelFormacionDocente> listaNivel = restCatalogo.buscarNivelFormacionDocente(filtroCod);
            comboNivel = new SofisComboG(listaNivel, "nfdNombre");
            comboNivel.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroCod.setOrderBy(new String[]{"cfdNombre"});
            filtroCod.setIncluirCampos(new String[]{"cfdNombre", "cfdVersion"});
            List<SgCategoriaFormacionDocente> listaCategoria = restCatalogo.buscarCategoriaFormacionDocente(filtroCod);
            comboCategoria = new SofisComboG(listaCategoria, "cfdNombre");
            comboCategoria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroEspecialidad fesp = new FiltroEspecialidad();
            fesp.setHabilitado(Boolean.TRUE);
            fesp.setAscending(new boolean[]{true});
            fesp.setOrderBy(new String[]{"espNombre"});
            fesp.setIncluirCampos(new String[]{"espNombre", "espVersion"});
            List<SgEspecialidad> listaEspecialidad = restCatalogo.buscarEspecialidad(fesp);
            comboEspecialidad = new SofisComboG(listaEspecialidad, "espNombre");
            comboEspecialidad.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroCod.setOrderBy(new String[]{"mfdNombre"});
            filtroCod.setIncluirCampos(new String[]{"mfdNombre", "mfdVersion"});
            List<SgModuloFormacionDocente> listaModulo = restCatalogo.buscarModuloFormacionDocente(filtroCod);
            comboModulo = new SofisComboG(listaModulo, "mfdNombre");
            comboModulo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroCod.setOrderBy(new String[]{"depNombre"});
            filtroCod.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamento = restCatalogo.buscarDepartamento(filtroCod);
            comboDepartamento = new SofisComboG(listaDepartamento, "depNombre");
            comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboNivel.setSelected(-1);
        comboCategoria.setSelected(-1);
        comboEspecialidad.setSelected(-1);
        comboModulo.setSelected(-1);
        comboDepartamento.setSelected(-1);
        sedeSelecionada = null;
    }

    public String limpiar() {
        filtro = new FiltroFormacionDocente();
        buscar();
        return null;
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgFormacionDocente();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgFormacionDocente var) {
        limpiarCombos();
        entidadEnEdicion = (SgFormacionDocente) SerializationUtils.clone(var);
        comboNivel.setSelectedT(entidadEnEdicion.getFdoNivel());
        comboCategoria.setSelectedT(entidadEnEdicion.getFdoCategoria());
        comboEspecialidad.setSelectedT(entidadEnEdicion.getFdoEspecialidad());
        comboModulo.setSelectedT(entidadEnEdicion.getFdoModulo());
        comboDepartamento.setSelectedT(entidadEnEdicion.getFdoDepartamento());
        sedeSelecionada = entidadEnEdicion.getFdoSede();
        JSFUtils.limpiarMensajesError();
    }

    public void ver(SgFormacionDocente var) {
        limpiarCombos();
        entidadEnEdicion = (SgFormacionDocente) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion.setFdoNivel(comboNivel.getSelectedT());
            entidadEnEdicion.setFdoCategoria(comboCategoria.getSelectedT());
            entidadEnEdicion.setFdoEspecialidad(comboEspecialidad.getSelectedT());
            entidadEnEdicion.setFdoModulo(comboModulo.getSelectedT());
            entidadEnEdicion.setFdoDepartamento(comboDepartamento.getSelectedT());
            entidadEnEdicion.setFdoSede(sedeSelecionada);
            entidadEnEdicion.setFdoPersonalSede(new SgPersonalSedeEducativa(personalSede.getPsePk(), personalSede.getPseVersion()));
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);

            if (personalSede.getPseFormacionDocente().contains(entidadEnEdicion)) {
                personalSede.getPseFormacionDocente().set(personalSede.getPseFormacionDocente().indexOf(entidadEnEdicion), entidadEnEdicion);
            } else {
                personalSede.getPseFormacionDocente().add(entidadEnEdicion);
            }
            PrimeFaces.current().executeScript("PF('formacionDocenteDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgFormacionDocente", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgFormacionDocente", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            personalSede.getPseFormacionDocente().remove(entidadEnEdicion);
            restClient.eliminar(entidadEnEdicion.getFdoPk());
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

    public List<SgSede> completeSede(String query) {
        try {
            sedeSelecionada = null;
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            return restSedes.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public FiltroFormacionDocente getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroFormacionDocente filtro) {
        this.filtro = filtro;
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

    public SgFormacionDocente getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgFormacionDocente entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public FormacionDocenteRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(FormacionDocenteRestClient restClient) {
        this.restClient = restClient;
    }

    public SedeRestClient getRestSedes() {
        return restSedes;
    }

    public void setRestSedes(SedeRestClient restSedes) {
        this.restSedes = restSedes;
    }

    public CapacitacionRestClient getRestCapacitaciones() {
        return restCapacitaciones;
    }

    public void setRestCapacitaciones(CapacitacionRestClient restCapacitaciones) {
        this.restCapacitaciones = restCapacitaciones;
    }

    public EstudioSuperiorRestClient getRestEstudioSuperior() {
        return restEstudioSuperior;
    }

    public void setRestEstudioSuperior(EstudioSuperiorRestClient restEstudioSuperior) {
        this.restEstudioSuperior = restEstudioSuperior;
    }

    public SgSede getSedeSelecionada() {
        return sedeSelecionada;
    }

    public void setSedeSelecionada(SgSede sedeSelecionada) {
        this.sedeSelecionada = sedeSelecionada;
    }

    public LazyFormacionDocenteDataModel getFormacionDocenteLazyModel() {
        return formacionDocenteLazyModel;
    }

    public void setFormacionDocenteLazyModel(LazyFormacionDocenteDataModel formacionDocenteLazyModel) {
        this.formacionDocenteLazyModel = formacionDocenteLazyModel;
    }

    public SofisComboG<SgNivelFormacionDocente> getComboNivel() {
        return comboNivel;
    }

    public void setComboNivel(SofisComboG<SgNivelFormacionDocente> comboNivel) {
        this.comboNivel = comboNivel;
    }

    public SofisComboG<SgCategoriaFormacionDocente> getComboCategoria() {
        return comboCategoria;
    }

    public void setComboCategoria(SofisComboG<SgCategoriaFormacionDocente> comboCategoria) {
        this.comboCategoria = comboCategoria;
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

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SgPersonalSedeEducativa getPersonalSede() {
        return personalSede;
    }

    public void setPersonalSede(SgPersonalSedeEducativa personalSede) {
        this.personalSede = personalSede;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

}
