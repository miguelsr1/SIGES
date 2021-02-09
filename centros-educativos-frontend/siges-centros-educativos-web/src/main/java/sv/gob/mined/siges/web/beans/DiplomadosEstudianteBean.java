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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.dto.SgDiplomadosEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgRelSedeDiplomado;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomadoEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomadosEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelSedeDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyDiplomadosEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.DiplomadosEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.RelSedeDiplomadoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DiplomadosEstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DiplomadosEstudianteBean.class.getName());

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DiplomadosEstudianteRestClient diplomadosEstudianteRestClient;

    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;

    @Inject
    private EstudianteRestClient estudianteRestClient;

    @Inject
    private DiplomadoRestClient diplomadoRestClient;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private CalificacionDiplomadoEstudianteRestClient calificacionDplomadosEstudianteRestClient;

    @Inject
    private RelSedeDiplomadoRestClient relSedeDiplomadoRestClient;

    private SgDiplomadosEstudiante entidadEnEdicion = new SgDiplomadosEstudiante();
    private List<RevHistorico> historialDiplomadosEstudiante = new ArrayList();
    private SgEstudiante estudiante;
    private String nie;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private SofisComboG<SgDiplomado> comboDiplomado;
    private SofisComboG<SgAnioLectivo> comboAnioLectivoInsert;
    private SofisComboG<SgDiplomado> comboDiplomadoInsert;

    private FiltroDiplomadosEstudiante filtro = new FiltroDiplomadosEstudiante();
    private FiltroEstudiante filtroEstudiante = new FiltroEstudiante();
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyDiplomadosEstudianteDataModel diplomadosEstudianteLazyModel;
    private Long nieSeleccionado;
    private SofisComboG<SgDepartamento> comboDepartamentos;
    private SgSede sedeSeleccionadaFiltro;
    private Boolean editable;
    private SgSede sedeSeleccionadaDialog;

    public DiplomadosEstudianteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void validarAcceso() {
        try {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_ESTUDIANTES)) {
                JSFUtils.redirectToIndex();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() throws Exception {
        try {
            FiltroAnioLectivo filtroAnioLectivo = new FiltroAnioLectivo();
            filtroAnioLectivo.setAleEstado(EnumAnioLectivoEstado.ABIERTO);
            filtroAnioLectivo.setOrderBy(new String[]{"aleAnio"});
            filtroAnioLectivo.setAscending(new boolean[]{false});
            List<SgAnioLectivo> listAniosLectivos = anioLectivoRestClient.buscar(filtroAnioLectivo);
            comboAnioLectivo = new SofisComboG(listAniosLectivos, "aleDescripcion");
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboAnioLectivoInsert = new SofisComboG(listAniosLectivos, "aleDescripcion");
            comboAnioLectivoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroDiplomado filtroD = new FiltroDiplomado();
            filtroD.setHabilitado(Boolean.TRUE);
            filtroD.setIncluirCampos(new String[]{"dipNombre", "dipVersion"});
            List<SgDiplomado> listaDiplomados = diplomadoRestClient.buscar(filtroD);
            comboDiplomado = new SofisComboG(listaDiplomados, "dipNombre");
            comboDiplomado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboDiplomadoInsert = new SofisComboG();
            comboDiplomadoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> listaDepartamentos = restCatalogo.buscarDepartamento(fc);
            comboDepartamentos = new SofisComboG<>(listaDepartamentos, "depNombre");
            comboDepartamentos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        editable = Boolean.TRUE;
        entidadEnEdicion = new SgDiplomadosEstudiante();
        comboAnioLectivoInsert.setSelected(-1);
        nieSeleccionado = null;
        sedeSeleccionadaDialog=null;
        comboDiplomadoInsert = new SofisComboG();
        comboDiplomadoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        cargarSedePorDefecto();
        
    }

    public String buscar() {
        try {
            filtro.setDepartamentoPk(comboDepartamentos.getSelectedT() != null ? comboDepartamentos.getSelectedT().getDepPk() : null);
            filtro.setSedePk(sedeSeleccionadaFiltro != null ? sedeSeleccionadaFiltro.getSedPk() : null);
            filtro.setAnioLectivoId(comboAnioLectivo != null && comboAnioLectivo.getSelectedT() != null ? comboAnioLectivo.getSelectedT().getAlePk() : null);
            filtro.setDiplomadoId(comboDiplomado != null && comboDiplomado.getSelectedT() != null ? comboDiplomado.getSelectedT().getDipPk() : null);
            filtro.setIncluirCampos(new String[]{"depSedeFk.sedCodigo", "depSedeFk.sedNombre",
                "depSedeFk.sedTipo",
                "depSedeFk.sedTelefono",
                "depSedeFk.sedDireccion.dirDepartamento.depNombre",
                "depSedeFk.sedDireccion.dirMunicipio.munNombre",
                "depDiplomado.dipNombre",
                "depEstudiante.estPersona.perNie",
                "depEstudiante.estPersona.perPrimerNombre",
                "depEstudiante.estPersona.perSegundoNombre",
                "depEstudiante.estPersona.perPrimerApellido",
                "depEstudiante.estPersona.perSegundoApellido",
                "depAnioLectivo.aleAnio"
            });
            totalResultados = diplomadosEstudianteRestClient.buscarTotal(filtro);
            diplomadosEstudianteLazyModel = new LazyDiplomadosEstudianteDataModel(diplomadosEstudianteRestClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public String limpiar() {
        sedeSeleccionadaFiltro = null;

        limpiarCombos();
        filtro = new FiltroDiplomadosEstudiante();
        filtroNombreCompleto = new FiltroNombreCompleto();
        buscar();
        return null;
    }

    private void limpiarCombos() {
        comboAnioLectivo.setSelected(-1);
        comboDiplomado.setSelected(-1);
        comboDepartamentos.setSelected(-1);
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
//            if (securityOperation != null) {
//                fil.setSecurityOperation(securityOperation);
//            }

            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion", "sedTipoCalendario.tcePk"});
            return restSede.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarSede() {
        comboDiplomadoInsert = new SofisComboG();
        comboDiplomadoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        try {
            if (sedeSeleccionadaDialog != null) {
                FiltroRelSedeDiplomado frsd = new FiltroRelSedeDiplomado();
                frsd.setRsdSedePk(sedeSeleccionadaDialog.getSedPk());
                frsd.setRsdHabilitado(Boolean.TRUE);
                frsd.setIncluirCampos(new String[]{
                    "rsdDiplomadoFk.dipPk",
                    "rsdDiplomadoFk.dipVersion",
                    "rsdDiplomadoFk.dipCodigo",
                    "rsdDiplomadoFk.dipNombre",
                    "rsdHabilitado"
                });
                frsd.setOrderBy(new String[]{"rsdDiplomadoFk.dipNombre"});
                frsd.setAscending(new boolean[]{true});
                List<SgRelSedeDiplomado> sedeDip = relSedeDiplomadoRestClient.buscar(frsd);
                if (!sedeDip.isEmpty()) {
                    List<SgDiplomado> dip = sedeDip.stream().map(c -> c.getRsdDiplomadoFk()).collect(Collectors.toList());
                    comboDiplomadoInsert = new SofisComboG(dip,"dipNombre");
                    comboDiplomadoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                }
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsg", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsg", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void filtroNombreCompletoSeleccionar(FiltroNombreCompleto filtroNombre) {
        if (filtroNombre != null) {
            filtro.setPerPrimerNombre(filtroNombre.getPerPrimerNombre());
            filtro.setPerSegundoNombre(filtroNombre.getPerSegundoNombre());
            filtro.setPerTercerNombre(filtroNombre.getPerTercerNombre());
            filtro.setPerPrimerApellido(filtroNombre.getPerPrimerApellido());
            filtro.setPerSegundoApellido(filtroNombre.getPerSegundoApellido());
            filtro.setPerTercerApellido(filtroNombre.getPerTercerApellido());
            if (!StringUtils.isBlank(filtroNombre.getNombreCompleto())) {
                filtro.setPerNombreCompleto(null);
            }
        }
        PrimeFaces.current().ajax().update("form:pnlSearch");
    }

    public void guardar() throws BusinessException, Exception {
        try {
            entidadEnEdicion.setDepSedeFk(sedeSeleccionadaDialog);
            entidadEnEdicion.setDepAnioLectivo(comboAnioLectivoInsert != null ? comboAnioLectivoInsert.getSelectedT() : null);
            entidadEnEdicion.setDepDiplomado(comboDiplomadoInsert != null ? comboDiplomadoInsert.getSelectedT() : null);
            entidadEnEdicion.setDepNieSeleccionado(nieSeleccionado);

            diplomadosEstudianteRestClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            buscar();

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsg", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsg", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarSedePorDefecto()  {
        if (sessionBean.getSedeDefecto() != null) {
            sedeSeleccionadaDialog = sessionBean.getSedeDefecto();
            seleccionarSede();
        }
    }

    public void actualizar(SgDiplomadosEstudiante diplomadoEstudiante) {
        try {
            editable = Boolean.TRUE;
            entidadEnEdicion = diplomadosEstudianteRestClient.obtenerPorId(diplomadoEstudiante.getDepPk());
            comboAnioLectivoInsert.setSelectedT(entidadEnEdicion.getDepAnioLectivo());
            sedeSeleccionadaDialog=entidadEnEdicion.getDepSedeFk();
            if(sedeSeleccionadaDialog!=null){
                seleccionarSede();
                if(!comboDiplomadoInsert.isEmpty()){
                    comboDiplomadoInsert.setSelectedT(entidadEnEdicion.getDepDiplomado());
                }
            }
            nieSeleccionado = null;
            if (entidadEnEdicion.getDepEstudiante().getEstPersona() != null && entidadEnEdicion.getDepEstudiante().getEstPersona().getPerNie() != null) {
                nieSeleccionado = entidadEnEdicion.getDepEstudiante().getEstPersona().getPerNie();
            }
            if (entidadEnEdicion.getDepAnioLectivo() != null && entidadEnEdicion.getDepSedeFk() != null && entidadEnEdicion.getDepEstudiante() != null && entidadEnEdicion.getDepDiplomado() != null) {
                FiltroCalificacionDiplomadoEstudiante fcde = new FiltroCalificacionDiplomadoEstudiante();
                fcde.setAnioLectivoFk(entidadEnEdicion.getDepAnioLectivo().getAlePk());
                fcde.setSedeFk(entidadEnEdicion.getDepSedeFk().getSedPk());
                fcde.setDiplomadoFk(entidadEnEdicion.getDepDiplomado().getDipPk());
                fcde.setCdeEstudianteFk(entidadEnEdicion.getDepEstudiante().getEstPk());
                fcde.setIncluirCampos(new String[]{"cdePk"});
                fcde.setMaxResults(1L);
                List<SgCalificacionDiplomadoEstudiante> calDep = calificacionDplomadosEstudianteRestClient.buscar(fcde);
                if (!calDep.isEmpty()) {
                    editable = Boolean.FALSE;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            diplomadosEstudianteRestClient.eliminar(entidadEnEdicion.getDepPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String historial(Long id) {
        try {
            historialDiplomadosEstudiante = diplomadosEstudianteRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentos() {
        return comboDepartamentos;
    }

    public void setComboDepartamentos(SofisComboG<SgDepartamento> comboDepartamentos) {
        this.comboDepartamentos = comboDepartamentos;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SgDiplomadosEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDiplomadosEstudiante entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgEstudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(SgEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivoInsert() {
        return comboAnioLectivoInsert;
    }

    public void setComboAnioLectivoInsert(SofisComboG<SgAnioLectivo> comboAnioLectivoInsert) {
        this.comboAnioLectivoInsert = comboAnioLectivoInsert;
    }

    public SofisComboG<SgDiplomado> getComboDiplomadoInsert() {
        return comboDiplomadoInsert;
    }

    public void setComboDiplomadoInsert(SofisComboG<SgDiplomado> comboDiplomadoInsert) {
        this.comboDiplomadoInsert = comboDiplomadoInsert;
    }

    public FiltroDiplomadosEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDiplomadosEstudiante filtro) {
        this.filtro = filtro;
    }

    public FiltroEstudiante getFiltroEstudiante() {
        return filtroEstudiante;
    }

    public void setFiltroEstudiante(FiltroEstudiante filtroEstudiante) {
        this.filtroEstudiante = filtroEstudiante;
    }

    public LazyDiplomadosEstudianteDataModel getDiplomadosEstudianteLazyModel() {
        return diplomadosEstudianteLazyModel;
    }

    public void setDiplomadosEstudianteLazyModel(LazyDiplomadosEstudianteDataModel diplomadosEstudianteLazyModel) {
        this.diplomadosEstudianteLazyModel = diplomadosEstudianteLazyModel;
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

    public SofisComboG<SgDiplomado> getComboDiplomado() {
        return comboDiplomado;
    }

    public void setComboDiplomado(SofisComboG<SgDiplomado> comboDiplomado) {
        this.comboDiplomado = comboDiplomado;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public String getNie() {
        return nie;
    }

    public void setNie(String nie) {
        this.nie = nie;
    }

    public List<RevHistorico> getHistorialDiplomadosEstudiante() {
        return historialDiplomadosEstudiante;
    }

    public void setHistorialDiplomadosEstudiante(List<RevHistorico> historialDiplomadosEstudiante) {
        this.historialDiplomadosEstudiante = historialDiplomadosEstudiante;
    }

    public Long getNieSeleccionado() {
        return nieSeleccionado;
    }

    public void setNieSeleccionado(Long nieSeleccionado) {
        this.nieSeleccionado = nieSeleccionado;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public SgSede getSedeSeleccionadaDialog() {
        return sedeSeleccionadaDialog;
    }

    public void setSedeSeleccionadaDialog(SgSede sedeSeleccionadaDialog) {
        this.sedeSeleccionadaDialog = sedeSeleccionadaDialog;
    }

}
