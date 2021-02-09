/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgEscolaridadEstudiante;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgServicioEducativo;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTitulo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EscolaridadEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TituloRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EscolaridadEstudianteComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EscolaridadEstudianteComponenteBean.class.getName());

    @Inject
    private EstudianteRestClient restClient;

    @Inject
    private EscolaridadEstudianteRestClient escolaridadEstudianteRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private TituloRestClient tituloRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private AnioLectivoRestClient anioLectivoRestClient;

    @Inject
    private SeleccionarServicioEducativoBean servicioEducativoSeleccionarBean;

    private FiltroEscolaridadEstudiante filtro = new FiltroEscolaridadEstudiante();
    private FiltroTitulo filtroTitulo = new FiltroTitulo();
    private Integer paginado = 10;
    private Long totalResultados;
    private List<SgEscolaridadEstudiante> escolaridad;
    private SgEscolaridadEstudiante entidadEnEdicion;
    private SgEstudiante estudiante;
    private SgServicioEducativo servicioEducativo;
    private SofisComboG<SgAnioLectivo> comboAnioLectivo;
    private SofisComboG<EnumPromovidoCalificacion> comboPromovidoCalificacion;
    private Boolean soloLectura = Boolean.FALSE;
    private List<SgTitulo> listTitulo = new ArrayList();
    private SgEscolaridadEstudiante entidadBorrado;

    public EscolaridadEstudianteComponenteBean() {
    }

    @PostConstruct
    public void init() {
        try {
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void cargarCombos() {
        comboPromovidoCalificacion = new SofisComboG(new ArrayList(Arrays.asList(EnumPromovidoCalificacion.values())), "text");
        comboPromovidoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void redirectToIndex() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.redirect(externalContext.getRequestContextPath());
    }

    public void actualizar(SgEscolaridadEstudiante var) {
        try {
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            entidadEnEdicion = escolaridadEstudianteRestClient.obtenerPorId(var.getEscPk());
            servicioEducativo = entidadEnEdicion.getEscServicioEducativo();
            servicioEducativoSeleccionarBean.setEntidadEnEdicionCopia(servicioEducativo);
            servicioEducativoSeleccionarBean.cargarServicioEducativo();
            cargarAniosLectivos();
            comboPromovidoCalificacion.setSelectedT(entidadEnEdicion.getEscResultado());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void prepararParaEliminar(SgEscolaridadEstudiante var) {
        entidadBorrado = var;
    }

    public void eliminar() {
        try {
            escolaridadEstudianteRestClient.eliminar(entidadBorrado.getEscPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('confirmDialogEliminarEscolaridad').hide()");
            entidadBorrado = null;
            buscar(estudiante);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarCombos() {
        comboPromovidoCalificacion = new SofisComboG(new ArrayList(Arrays.asList(EnumPromovidoCalificacion.values())), "text");
        comboPromovidoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboAnioLectivo = new SofisComboG();
        comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void buscar(SgEstudiante est) {
        try {
            estudiante = est;
            if (estudiante.getEstPk() != null) {
                filtro.setEstudiantePk(estudiante.getEstPk());
                filtro.setIncluirCampos(new String[]{
                    "escServicioEducativo.sduSede.sedCodigo",
                    "escServicioEducativo.sduSede.sedNombre",
                    "escServicioEducativo.sduSede.sedTipo",
                    "escServicioEducativo.sduSede.sedTelefono",
                    "escServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                    "escServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                    "escServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                    "escServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "escServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "escServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "escServicioEducativo.sduGrado.graNombre",
                    "escServicioEducativo.sduOpcion.opcNombre",
                    "escServicioEducativo.sduProgramaEducativo.pedNombre",
                    "escAnio.aleAnio",
                    "escResultado",
                    "escMatriculaFk.matPk",
                    "escCreadoCierre",
                    "escEqNumeroTramite",
                    "escGeneradaPorEquivalencia",
                    "escEqNumeroTramite",
                    "escEqAnio",
                    "escEqGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                    "escEqGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "escEqGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "escEqGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "escEqGrado.graNombre",
                    "escEqOpcion.opcNombre",
                    "escEqProgramaEducativo.pedNombre",
                    "escPk",
                    "escNombreSede"});
                escolaridad = escolaridadEstudianteRestClient.buscar(filtro);

                Comparator<SgEscolaridadEstudiante> comparator = Comparator.comparing(esc -> esc.getAnio());
                comparator = comparator.thenComparing(Comparator.comparing(esc -> esc.getEscPk()));
                escolaridad = escolaridad.stream().sorted(comparator).collect(Collectors.toList());

            }

            if (estudiante.getEstPersona().getPerNie() != null) {
                FiltroTitulo ft = new FiltroTitulo();
                ft.setTitNie(estudiante.getEstPersona().getPerNie());
                ft.setTitNoAnulada(Boolean.TRUE);
                ft.setIncluirCampos(new String[]{
                    "titEstudianteFk.estPk",
                    "titEstudianteFk.estVersion",
                    "titNombreEstudiante",
                    "titNombreEstudiantePartida",
                    "titNombreCertificado",
                    "titFechaValidez",
                    "titFechaEmision",
                    "titNombreDirector",
                    "titNombreMinistro",
                    "titAnio",
                    "titSedeFk.sedPk",
                    "titSedeFk.sedVersion",
                    "titSedeFk.sedTipo",
                    "titSedeFk.sedCodigo",
                    "titSedeFk.sedNombre",
                    "titSedeFk.sedDireccion.dirDepartamento.depNombre",
                    "titSedeFk.sedDireccion.dirMunicipio.munNombre",
                    "titSedeFk.sedTelefono",
                    "titSedeCodigo",
                    "titSedeNombre",
                    "titUsuarioEnviaImprimir",
                    "titNumeroRegistro",
                    "titReposicion",
                    "titHash",
                    "titVersion",
                    "titAnio",
                    "titAnulada"});
                listTitulo = tituloRestClient.buscar(ft);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String obtenerNombreSede(SgEscolaridadEstudiante esc) {
        if (esc.getEscServicioEducativo() != null && esc.getEscServicioEducativo().getSduSede() != null) {
            return esc.getEscServicioEducativo().getSduSede().getSedCodigo();
        } else {
            return esc.getEscNombreSede();
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion.setEscResultado(comboPromovidoCalificacion.getSelectedT());
            entidadEnEdicion.setEscAnio(comboAnioLectivo.getSelectedT());
            entidadEnEdicion = escolaridadEstudianteRestClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialogesc').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void servicioEducativoSeleccionar(SgServicioEducativo ser) {
        try {
            entidadEnEdicion.setEscServicioEducativo(null);
            servicioEducativo = ser;
            if (ser != null) {
                //Validar si es la misma sede
                entidadEnEdicion.setEscServicioEducativo(ser);

            }
            cargarAniosLectivos();
            PrimeFaces.current().ajax().update("form:tabs:itemDetailesc");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarAniosLectivos() {
        try {
            comboAnioLectivo = new SofisComboG();
            comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (servicioEducativo != null) {
                FiltroAnioLectivo fal = new FiltroAnioLectivo();
                fal.setAleServicioEducativo(servicioEducativo.getSduPk());
                List<SgAnioLectivo> anioLec = anioLectivoRestClient.buscar(fal);
                comboAnioLectivo = new SofisComboG(anioLec, "aleAnio");
                comboAnioLectivo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (entidadEnEdicion.getEscAnio() != null) {
                    comboAnioLectivo.setSelectedT(entidadEnEdicion.getEscAnio());
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroTitulo getFiltroTitulo() {
        return filtroTitulo;
    }

    public void setFiltroTitulo(FiltroTitulo filtroTitulo) {
        this.filtroTitulo = filtroTitulo;
    }

    public void limpiar() {
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

    public EstudianteRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(EstudianteRestClient restClient) {
        this.restClient = restClient;
    }

    public EscolaridadEstudianteRestClient getEscolaridadEstudianteRestClient() {
        return escolaridadEstudianteRestClient;
    }

    public void setEscolaridadEstudianteRestClient(EscolaridadEstudianteRestClient escolaridadEstudianteRestClient) {
        this.escolaridadEstudianteRestClient = escolaridadEstudianteRestClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SedeRestClient getSedeRestClient() {
        return sedeRestClient;
    }

    public void setSedeRestClient(SedeRestClient sedeRestClient) {
        this.sedeRestClient = sedeRestClient;
    }

    public FiltroEscolaridadEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEscolaridadEstudiante filtro) {
        this.filtro = filtro;
    }

    public List<SgEscolaridadEstudiante> getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(List<SgEscolaridadEstudiante> escolaridad) {
        this.escolaridad = escolaridad;
    }

    public SgEscolaridadEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEscolaridadEstudiante entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgEstudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(SgEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    public SgServicioEducativo getServicioEducativo() {
        return servicioEducativo;
    }

    public void setServicioEducativo(SgServicioEducativo servicioEducativo) {
        this.servicioEducativo = servicioEducativo;
    }

    public SofisComboG<SgAnioLectivo> getComboAnioLectivo() {
        return comboAnioLectivo;
    }

    public void setComboAnioLectivo(SofisComboG<SgAnioLectivo> comboAnioLectivo) {
        this.comboAnioLectivo = comboAnioLectivo;
    }

    public SofisComboG<EnumPromovidoCalificacion> getComboPromovidoCalificacion() {
        return comboPromovidoCalificacion;
    }

    public void setComboPromovidoCalificacion(SofisComboG<EnumPromovidoCalificacion> comboPromovidoCalificacion) {
        this.comboPromovidoCalificacion = comboPromovidoCalificacion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgTitulo> getListTitulo() {
        return listTitulo;
    }

    public void setListTitulo(List<SgTitulo> listTitulo) {
        this.listTitulo = listTitulo;
    }

}
