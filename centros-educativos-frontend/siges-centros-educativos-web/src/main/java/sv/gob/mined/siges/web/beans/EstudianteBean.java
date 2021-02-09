/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAllegado;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.AllegadoRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;

import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgIdentificacion;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgResponsable;
import sv.gob.mined.siges.web.dto.SgUnirEstudiantes;
import sv.gob.mined.siges.web.dto.SgUsuarioRol;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroIdentificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuarioRol;
import sv.gob.mined.siges.web.lazymodels.LazyLucenePersonaDataModel;
import sv.gob.mined.siges.web.restclient.IdentificacionRestClient;
import sv.gob.mined.siges.web.restclient.NieRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRolRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class EstudianteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstudianteBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long estId;

    @Inject
    @Param(name = "rev")
    private Long estRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "tab")
    private String tab;

    @Inject
    private EstudianteRestClient restClient;

    @Inject
    private AllegadoRestClient allegadoClient;

    @Inject
    private PersonaRestClient personaRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private NieRestClient nieRestClient;

    @Inject
    private IdentificacionComponenteBean identificacionComponenteBean;

    @Inject
    private PersonalSedeEducativaRestClient personalSedeClient;

    @Inject
    private MatriculaComponenteBean matriculaComponenteBean;

    @Inject
    private EstudianteValoracionBean estudianteValoracionBean;

    @Inject
    private ManifestacionViolenciaBean manifestacionViolenciaBean;

    @Inject
    private EscolaridadEstudianteComponenteBean escolaridadEstudianteComponenteBean;

    @Inject
    private DatosSaludEstudianteBean datosSaludEstudianteBean;

    @Inject
    private AlertasEstudianteComponenteBean alertasEstudianteComponenteBean;

    @Inject
    private EstudianteEvolucionBean estudianteEvolucionBean;

    @Inject
    private IdentificacionRestClient identificacionRestClient;

    @Inject
    private UsuarioRolRestClient usuarioRolRestClient;

    private Boolean luceneEnabled = Boolean.FALSE;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean renderOpcionTieneIdentificacion = Boolean.TRUE;
    private SgEstudiante entidadEnEdicion;
    private String tabActiveId;
    private Integer tabActiveIndex;
    private SgAllegado allegadoEnEdicion;
    private SofisComboG<SgTipoParentesco> comboTipoParentesco;
    private List<SgAllegado> listAllegados;
    private Long completarNIE;
    private SgEstudiante estudianteUnir;
    private Long totalResultadosPersonas;
    private LazyLucenePersonaDataModel lazyPersonaDataModel;
    private Integer paginado = 10;
    private Boolean estudiaNivelMedia = Boolean.FALSE;

    public EstudianteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (estId != null && estId > 0) {
                if (estRev != null && estRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(estId, estRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(estId, Boolean.TRUE));
                    soloLectura = editable != null ? !editable : soloLectura;
                }
                if (tab != null) {
                    if (tab.equals("f")) {
                        tabActiveIndex = 1;
                        this.changeTab("allegados");
                    }
                }
            } else {
                JSFUtils.redirectToIndex();
            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public String getTituloPagina() {
        if (this.entidadEnEdicion.getEstPk() != null) {
            if (this.soloLectura) {
                return Etiquetas.getValue("visualizarEstudiante") + " " + entidadEnEdicion.getEstPersona().getPerPrimerNombre() + " " + entidadEnEdicion.getEstPersona().getPerPrimerApellido();
            } else {
                return Etiquetas.getValue("edicionEstudiante") + " " + entidadEnEdicion.getEstPersona().getPerPrimerNombre() + " " + entidadEnEdicion.getEstPersona().getPerPrimerApellido();
            }
        } else {
            return "";
        }
    }

    public void validarAcceso() {
        try {
            if (soloLectura) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_ESTUDIANTES)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (estId == null) {
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_ESTUDIANTE)) {
                        JSFUtils.redirectToIndex();
                    }
                } else {
                    if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_ESTUDIANTE)) {
                        JSFUtils.redirectToIndex();
                    }
                    if (!this.entidadEnEdicion.getEstPersona().getPerHabilitado()) {
                        soloLectura = Boolean.TRUE;
                    } else if (!sessionBean.tieneOperacionEnContexto(ConstantesOperaciones.ACTUALIZAR_ESTUDIANTE, EnumAmbito.SEDE, this.entidadEnEdicion.getEstUltimaSedePk())) {
                        //Validar ult matrícula
                        soloLectura = Boolean.TRUE;
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_ESTUDIANTE_SOLO_LECTURA_PERTENECE_OTRA_INSTITUCION), "");
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public void updateAddressCoordinates() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String lat = params.get("latitude");
        String lng = params.get("longitude");
        SgDireccion dir = this.getObtenerDireccionEnEdicion();
        if (lat != null) {
            dir.setDirLatitud(Double.parseDouble(lat));
        }
        if (lng != null) {
            dir.setDirLongitud(Double.parseDouble(lng));
        }
    }

    public void cargarFamiliares() {
        try {
            if (entidadEnEdicion != null && entidadEnEdicion.getEstPersona().getPerPk() != null) {
                FiltroAllegado ff = new FiltroAllegado();
                ff.setIncluirCampos(new String[]{"allTipoParentesco.tpaNombre",
                    "allPersona.perPrimerNombre",
                    "allPersona.perSegundoNombre",
                    "allPersona.perPk",
                    "allPersona.perPrimerApellido",
                    "allPersona.perSegundoApellido",
                    "allPersona.perUsuarioPk",
                    "allReferente",
                    "allViveConAllegado",
                    "allVersion"});
                ff.setAllPersonaReferenciadaPk(entidadEnEdicion.getEstPersona().getPerPk());
                listAllegados = allegadoClient.buscar(ff);

                for (SgAllegado all : listAllegados) {
                    if (all.getAllPersona().getPerUsuarioPk() != null) {
                        FiltroUsuarioRol filtroUsuarioRol = new FiltroUsuarioRol();
                        filtroUsuarioRol.setIncluirCampos(new String[]{"purPk"});
                        filtroUsuarioRol.setUsuario(all.getAllPersona().getPerUsuarioPk());
                        filtroUsuarioRol.setRolCodigo(Constantes.CODIGO_ROL_PADRE);
                        filtroUsuarioRol.setAmbito(EnumAmbito.PERSONA);
                        filtroUsuarioRol.setContexto(entidadEnEdicion.getEstPersona().getPerPk());
                        List<SgUsuarioRol> ret = usuarioRolRestClient.buscar(filtroUsuarioRol);

                        if (!ret.isEmpty()) {
                            all.setAllTieneRolPadreEnEstudiante(Boolean.TRUE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setIncluirCampos(new String[]{"tpaNombre", "tpaVersion"});
            List<SgTipoParentesco> list = catalogoRestClient.buscarTipoParentesco(fc);
            comboTipoParentesco = new SofisComboG(list, "tpaNombre");
            comboTipoParentesco.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            SgConfiguracion c = catalogoRestClient.buscarConfiguracionPorCodigo(Constantes.CONFIG_BUSQUEDA_PERSONAS_SIMILARES_LUCENE_HABILITADA);
            if (c != null && !StringUtils.isBlank(c.getConValor())) {
                luceneEnabled = Boolean.parseBoolean(c.getConValor());
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgEstudiante est) {
        try {
            if (est == null) {
                JSFUtils.redirectToIndex();
            } else {
                entidadEnEdicion = est;
                if (entidadEnEdicion.getEstPersona().getPerDireccion() == null) {
                    entidadEnEdicion.getEstPersona().setPerDireccion(new SgDireccion());
                }
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarCombo() {
        this.comboTipoParentesco.setSelected(-1);
    }

    public void agregarFamiliar() {
        allegadoEnEdicion = new SgAllegado();
        comboTipoParentesco.setSelected(-1);
        allegadoEnEdicion.setAllEsFamiliar(Boolean.TRUE);
        allegadoEnEdicion.getAllPersona().setPerTieneIdentificacion(Boolean.TRUE);
        JSFUtils.limpiarMensajesError();
    }

    public void editarFamiliar(SgAllegado familiar) {
        try {
            allegadoEnEdicion = allegadoClient.obtenerPorId(familiar.getAllPk());
            this.comboTipoParentesco.setSelectedT(allegadoEnEdicion.getAllTipoParentesco());

            //Validar si responsable es personal de sede
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_EDITAR_RESPONSABLE_SI_ES_PERSONAL_SEDE)) {
                FiltroPersonalSedeEducativa filtroPersonal = new FiltroPersonalSedeEducativa();
                filtroPersonal.setPerPk(familiar.getAllPersona().getPerPk());
                this.allegadoEnEdicion.setAllSoloLectura(personalSedeClient.buscarTotal(filtroPersonal) > 0L);
                if (this.allegadoEnEdicion.getAllSoloLectura()) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.WARNING_RESPONSABLE_ES_PERSONAL_DE_SEDE), "");
                }
            }
        } catch (BusinessException be) {
            this.allegadoEnEdicion.setAllSoloLectura(Boolean.TRUE);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            this.allegadoEnEdicion.setAllSoloLectura(Boolean.TRUE);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarFamiliar() {
        try {
            allegadoClient.eliminar(allegadoEnEdicion.getAllPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            cargarFamiliares();
            allegadoEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            Boolean tieneIdentificacion = entidadEnEdicion.getEstPersona().getPerTieneIdentificacion();
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            entidadEnEdicion.getEstPersona().setPerTieneIdentificacion(tieneIdentificacion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarDatosTabla(SgAllegado elem) {
        try {
            //TODO: cambiar por metodos en el backend para no tener que traer todo el allegado al frontend
            allegadoEnEdicion = allegadoClient.obtenerPorId(elem.getAllPk());
            allegadoEnEdicion.setAllReferente(elem.getAllReferente());
            allegadoEnEdicion.setAllViveConAllegado(elem.getAllViveConAllegado());
            allegadoEnEdicion.setAllPersonaReferenciada(entidadEnEdicion.getEstPersona());
            allegadoClient.guardar(allegadoEnEdicion);
            allegadoEnEdicion = null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarFamiliar() {
        try {
            allegadoEnEdicion.setAllPersonaReferenciada(entidadEnEdicion.getEstPersona());
            allegadoEnEdicion = allegadoClient.guardar(allegadoEnEdicion);
            cargarFamiliares();
            allegadoEnEdicion = null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void crearUsuarioResponsable(SgAllegado familiar) {
        try {
            SgAllegado allegado = allegadoClient.obtenerPorId(familiar.getAllPk());
            String documento = obtenerDocumento(allegado);
            if (StringUtils.isNotBlank(documento)) {
                SgResponsable responsable = new SgResponsable();
                responsable.setDocumento(documento);
                responsable.setUsuPk(allegado.getAllPersona().getPerUsuarioPk()); //Para los casos en que el usuario ya existe y solo hay que asociar el rol
                responsable.setNombreCompleto(allegado.getAllPersona().getPerNombreCompleto());
                responsable.setEmail(allegado.getAllPersona().getPerEmail());
                responsable.setPersonaEstudianteId(entidadEnEdicion.getEstPersona().getPerPk());
                responsable.setPersonaResponsableId(allegado.getAllPersona().getPerPk());

                usuarioRolRestClient.guardarUsuarioRolResponsable(responsable);
                cargarFamiliares();
            } else {
                throw new BusinessException(Mensajes.ERROR_CREACION_USUARIO_ALLEGADO);
            }

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.USUARIO_CREADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String obtenerDocumento(SgAllegado allegado) throws Exception {
        String resultado = StringUtils.EMPTY;
        if (allegado != null && allegado.getAllPersona() != null) {
            if (StringUtils.isBlank(allegado.getAllPersona().getPerDui())) {
                FiltroIdentificacion filtro = new FiltroIdentificacion();
                filtro.setIdePersona(allegado.getAllPersona().getPerPk());
                List<SgIdentificacion> listaIdentificacionesPersona = identificacionRestClient.buscar(filtro);
                if (listaIdentificacionesPersona != null && !listaIdentificacionesPersona.isEmpty()) {
                    for (SgIdentificacion iden : listaIdentificacionesPersona) {
                        if (Constantes.CODIGO_IDENTIFICACION_PASAPORTE.equals(iden.getIdeTipoDocumento().getTinCodigo())) {
                            resultado = iden.getIdeNumeroDocumento();
                            break;
                        }
                    }
                }
            } else {
                resultado = allegado.getAllPersona().getPerDui();
            }
        }
        return resultado;
    }

    public void buscarRemplazoEstudiante() {
        try {
            if (completarNIE != null) {
                FiltroEstudiante fest = new FiltroEstudiante();
                fest.setNie(completarNIE);
                fest.setSecurityOperation(null); //Desactivamos seguridad
                fest.setIncluirCampos(new String[]{"estPersona.perNie",
                    "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perNombreBusqueda",
                    "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perFechaNacimiento",
                    "estVersion"});
                List<SgEstudiante> listEst = restClient.buscar(fest);
                if (!listEst.isEmpty()) {
                    estudianteUnir = listEst.get(0);
                } else {
                    estudianteUnir = null;
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRARON_ESTUDIANTES), "");
                }
                PrimeFaces.current().ajax().update("form:itemDetailEstudianteUnir");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void unirEstudiante() {
        try {
            if (estudianteUnir != null) {
                SgUnirEstudiantes dto = new SgUnirEstudiantes();
                dto.setEstudianteOrigenPk(entidadEnEdicion.getEstPk());
                dto.setEstudianteDestinoPk(estudianteUnir.getEstPk());
                restClient.unirEstudiantes(dto);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                JSFUtils.redirectToPage(ConstantesPaginas.ESTUDIANTE + "?id=" + estudianteUnir.getEstPk());
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_ESTUDIANTE_VACIO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgPersona getObtenerPersonaEnEdicion() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            if (this.allegadoEnEdicion != null) {
                this.allegadoEnEdicion.getAllPersona().setPerVisualizadaComoEstudiante(Boolean.FALSE);
                return this.allegadoEnEdicion.getAllPersona();
            }
        } else if (this.entidadEnEdicion != null) {
            this.entidadEnEdicion.getEstPersona().setPerVisualizadaComoEstudiante(Boolean.TRUE);
            return this.entidadEnEdicion.getEstPersona();
        }
        return new SgPersona();
    }

    public Boolean getRenderPartidaNacimiento() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public SgDireccion getObtenerDireccionEnEdicion() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            if (this.allegadoEnEdicion != null) {
                return this.allegadoEnEdicion.getAllPersona().getPerDireccion();
            }
        } else if (this.entidadEnEdicion != null) {
            return this.entidadEnEdicion.getEstPersona().getPerDireccion();
        }
        return null;
    }

    public Boolean getPersonaSoloLectura() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            return this.soloLectura || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_RESPONSABLE_Y_FAMILIARES_ESTUDIANTE)
                    || (this.allegadoEnEdicion != null && this.allegadoEnEdicion.getAllSoloLectura());
        } else {
            return this.soloLectura || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_DATOS_PERSONALES_ESTUDIANTE);
        }
    }

    public Boolean getVerTrastornosAprendizaje() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean getDireccionSoloLectura() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            if (allegadoEnEdicion != null && allegadoEnEdicion.getAllPersona() != null && allegadoEnEdicion.getAllPersona().getPerPk() == null) {
                return Boolean.FALSE;
            }
            return this.soloLectura || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_RESPONSABLE_Y_FAMILIARES_ESTUDIANTE)
                    || (this.allegadoEnEdicion != null && this.allegadoEnEdicion.getAllSoloLectura());
        } else {
            return this.soloLectura || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_DATOS_PERSONALES_ESTUDIANTE);
        }
    }

    public void restablecerAllegado() {
        this.allegadoEnEdicion = new SgAllegado();
    }

    public void changeTab(TabChangeEvent event) {
        this.changeTab(event.getTab().getId());
    }

    public void changeTab(String tabId) {
        this.tabActiveId = tabId;
        if (tabActiveId.equals("datos_personales")) {
            PrimeFaces.current().ajax().update("form:tabs:formularioEstudiante");
        } else if (tabActiveId.equals("allegados")) {
            if (listAllegados == null) {
                cargarFamiliares();
            }
            PrimeFaces.current().ajax().update("form:tabs:allegadosContainer");
        } else if (tabActiveId.equals("tabBitMatricula")) {
            if (matriculaComponenteBean.getEstEdicion() == null) {
                matriculaComponenteBean.actualizar(entidadEnEdicion);
            }
            //Buscar la matricula no anulada de educación media para habilitar la edición del servicio social
            SgMatricula matriculaMedia = matriculaComponenteBean.getListaMatricula()
                    .stream()
                    .filter(
                            (o) -> o.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivCodigo().equals(Constantes.CODIGO_NIVEL_EDUCACION_MEDIA)
                            && BooleanUtils.isNotTrue(o.getMatAnulada())
                    )
                    .findFirst()
                    .orElse(null);
            if (matriculaMedia != null) {
                estudiaNivelMedia = Boolean.TRUE;
            }
            PrimeFaces.current().ajax().update("form:tabs:tabBitMatriculaContainer");
        } else if (tabActiveId.equals("tabManifestacionViolencia")) {
            if (manifestacionViolenciaBean.getEntidadEnEdicion() == null) {
                manifestacionViolenciaBean.actualizar(entidadEnEdicion);
            }
            PrimeFaces.current().ajax().update("form:tabs:tabManifestacionViolenciaContainer");
        } else if (tabActiveId.equals("tabValoracionesDocente")) {
            if (estudianteValoracionBean.getEntidadEnEdicion() == null) {
                estudianteValoracionBean.actualizar(entidadEnEdicion);
            }
            PrimeFaces.current().ajax().update("form:tabs:tabValoracionesDocenteContainer");
        } else if (tabActiveId.equals("tabEscolaridad")) {
            if (escolaridadEstudianteComponenteBean.getEstudiante() == null) {
                escolaridadEstudianteComponenteBean.buscar(entidadEnEdicion);
            }
            PrimeFaces.current().ajax().update("form:tabs:tabEscolaridadContainer");
        } else if (tabActiveId.equals("tabDatosSalud")) {
            if (datosSaludEstudianteBean.getEntidadEnEdicion() == null) {
                datosSaludEstudianteBean.setEntidadEnEdicion(entidadEnEdicion);
                datosSaludEstudianteBean.inicializarFicha();
            }
            PrimeFaces.current().ajax().update("form:tabs:tabDatosSaludContainer");
        } else if (tabActiveId.equals("tabAlertas")) {
            if (alertasEstudianteComponenteBean.getEntidadEnEdicion() == null) {
                alertasEstudianteComponenteBean.setEntidadEnEdicion(entidadEnEdicion);
                alertasEstudianteComponenteBean.buscarAlerta();
            }
            PrimeFaces.current().ajax().update("form:tabs:tabAlertasContainer");
        } else if (tabActiveId.equals("tabEvolucion")) {
            if (estudianteEvolucionBean.getEntidadEnEdicion() == null) {
                estudianteEvolucionBean.actualizar(entidadEnEdicion);
            }
            PrimeFaces.current().ajax().update("form:tabs:tabEvolucionContainer");
        }
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }

    public String getPersonaViewAction() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            return "intercambiarPersonaFamiliar";
        }
        return "intercambiarEstudiante";
    }

    public void intercambiarEstudiante(SgPersona p) throws Exception {
        FiltroEstudiante fe = new FiltroEstudiante();
        fe.setEstPersona(p.getPerPk());
        fe.setIncluirCampos(new String[]{"estPk", "estVersion"});
        List<SgEstudiante> estudiantes = this.restClient.buscar(fe);
        if (!estudiantes.isEmpty()) {
            entidadEnEdicion = this.restClient.obtenerPorId(estudiantes.get(0).getEstPk(), Boolean.FALSE);
        }
    }

    public void intercambiarPersonaFamiliar(SgPersona p) throws Exception {

        SgPersona personaIngresada = allegadoEnEdicion.getAllPersona();

        if (allegadoEnEdicion.getAllPk() == null && p.getPerPk() == null) {
            //Nuevo caso persona consultada RNPN
            allegadoEnEdicion.setAllPersona(p);
            allegadoEnEdicion.getAllPersona().normalizarDatos();
            allegadoEnEdicion.getAllPersona().actualizarIdentificaciones(personaIngresada);
            allegadoEnEdicion.getAllPersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            allegadoEnEdicion.getAllPersona().setPerSeBuscoEnBd(Boolean.TRUE);
            return;
        }

        //Buscamos si persona ya existe como familiar
        Boolean resultado = Boolean.FALSE;
        FiltroAllegado fe = new FiltroAllegado();
        fe.setAllPersonaReferenciadaPk(this.entidadEnEdicion.getEstPersona().getPerPk());
        fe.setAllPersonaPk(p.getPerPk());
        fe.setIncluirCampos(new String[]{"allVersion", "allPersona.perHabilitado"});
        List<SgAllegado> familiares = this.allegadoClient.buscar(fe);
        if (!familiares.isEmpty()) {
            if (BooleanUtils.isTrue(familiares.get(0).getAllPersona().getPerHabilitado())) {
                //Familiar existente
                resultado = Boolean.TRUE;
                this.allegadoEnEdicion = this.allegadoClient.obtenerPorId(familiares.get(0).getAllPk());
            }
        } else {
            //Seteamos persona existente a nuevo familiar
            SgPersona persona = personaRestClient.obtenerPorId(p.getPerPk());
            if (BooleanUtils.isTrue(persona.getPerHabilitado())) {
                resultado = Boolean.TRUE;
                this.allegadoEnEdicion.setAllPersona(persona);
            }
        }
        if (resultado) {
            allegadoEnEdicion.getAllPersona().normalizarDatos();
            allegadoEnEdicion.getAllPersona().actualizarIdentificaciones(personaIngresada);
            allegadoEnEdicion.getAllPersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            allegadoEnEdicion.getAllPersona().setPerSeBuscoEnBd(Boolean.TRUE);
        } else {
            allegadoEnEdicion.getAllPersona().setPerSeEncontroIdentificacion(Boolean.FALSE);
            allegadoEnEdicion.getAllPersona().setPerSeBuscoEnBd(Boolean.FALSE);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONA_RETIRADO), "");
        }
    }

    public String generarNIE() {
        try {
            if (entidadEnEdicion != null) {
                SgPersona per = nieRestClient.generarNie(entidadEnEdicion.getEstPersona());
                entidadEnEdicion.setEstPersona(per);
                entidadEnEdicion.getEstPersona().setPerTieneIdentificacion(Boolean.TRUE);
                identificacionComponenteBean.setTieneNIE(Boolean.TRUE);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.NIE_GENERADO), "");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public Long personasSimilares() throws Exception {
        if (!luceneEnabled) {
            totalResultadosPersonas = 0L;
            lazyPersonaDataModel = null;
            return 0L;
        }
        SgPersona p = entidadEnEdicion.getEstPersona();
        FiltroPersona fp = new FiltroPersona();
        fp.setPerPrimerNombre(p.getPerPrimerNombre());
        fp.setPerPrimerApellido(p.getPerPrimerApellido());
        fp.setPerSegundoNombre(p.getPerSegundoNombre());
        fp.setPerSegundoApellido(p.getPerSegundoApellido());
        fp.setPerTercerNombre(p.getPerTercerNombre());
        fp.setPerTercerApellido(p.getPerTercerApellido());
        fp.setPerSexoPk(p.getPerSexo() != null ? p.getPerSexo().getSexPk() : null);
        List<Long> pkIgnorar = new ArrayList();
        pkIgnorar.add(entidadEnEdicion.getEstPersona().getPerPk());
        fp.setIgnorarPersonasPk(pkIgnorar);
        if (p.getPerFechaNacimiento() != null) {
            fp.setPerFechaNacimientoDesde(p.getPerFechaNacimiento().minusDays(30));
            fp.setPerFechaNacimientoHasta(p.getPerFechaNacimiento().plusDays(30));
        }
        fp.setIncluirCampos(new String[]{
            "perNie",
            "perDui",
            "perPrimerNombre",
            "perSegundoNombre",
            "perTercerNombre",
            "perPrimerApellido",
            "perSegundoApellido",
            "perTercerApellido",
            "perFechaNacimiento",
            "perSexo.sexNombre",
            "perDireccion.dirDepartamento.depNombre",
            "perDireccion.dirMunicipio.munNombre",
            "perEmail",
            "perEstadoCivil.eciNombre",
            "perEstudiante.estUltimaMatricula.matEstado",
            "perEstudiante.estUltimaMatricula.matVersion",
            "perEstudiante.estUltimaMatricula.matSeccion.secAnioLectivo.aleAnio",
            "perEstudiante.estUltimaMatricula.matSeccion.secAnioLectivo.aleAnio"});
        totalResultadosPersonas = personaRestClient.buscarTotalLucene(fp);
        if (totalResultadosPersonas <= 0) {
            lazyPersonaDataModel = null;
        } else {
            lazyPersonaDataModel = new LazyLucenePersonaDataModel(personaRestClient, fp, totalResultadosPersonas, paginado);
            lazyPersonaDataModel.setLuceneEnabled(luceneEnabled);
        }

        return totalResultadosPersonas;
    }

    public void seleccionadoServicioSocial() {
        if (!entidadEnEdicion.getEstRealizoServicioSocial()) {
            entidadEnEdicion.setEstCantidadHorasServicioSocial(null);
            entidadEnEdicion.setEstDescripcionServicioSocial(null);
            entidadEnEdicion.setEstFechaServicioSocial(null);
        }
    }

    public Boolean getSoloLecturaServicioSocial() {
        return !soloLectura && sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_SERVICIO_SOCIAL) && estudiaNivelMedia;
    }

    public void comboParentescoSelected() {
        allegadoEnEdicion.setAllTipoParentesco(comboTipoParentesco.getSelectedT());
    }

    public SgEstudiante getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstudiante entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Boolean getEditable() {
        return editable;
    }

    public SgAllegado getAllegadoEnEdicion() {
        return allegadoEnEdicion;
    }

    public void setAllegadoEnEdicion(SgAllegado allegadoEnEdicion) {
        this.allegadoEnEdicion = allegadoEnEdicion;
    }

    public SofisComboG<SgTipoParentesco> getComboTipoParentesco() {
        return comboTipoParentesco;
    }

    public void setComboTipoParentesco(SofisComboG<SgTipoParentesco> comboTipoParentesco) {
        this.comboTipoParentesco = comboTipoParentesco;
    }

    public List<SgAllegado> getListFamiliares() {
        return listAllegados;
    }

    public void setListFamiliares(List<SgAllegado> listFamiliares) {
        this.listAllegados = listFamiliares;
    }

    public Long getEstRev() {
        return estRev;
    }

    public Boolean getRenderOpcionTieneIdentificacion() {
        return renderOpcionTieneIdentificacion;
    }

    public void setRenderOpcionTieneIdentificacion(Boolean renderOpcionTieneIdentificacion) {
        this.renderOpcionTieneIdentificacion = renderOpcionTieneIdentificacion;
    }

    public SgEstudiante getEstudianteUnir() {
        return estudianteUnir;
    }

    public void setEstudianteUnir(SgEstudiante estudianteUnir) {
        this.estudianteUnir = estudianteUnir;
    }

    public Long getCompletarNIE() {
        return completarNIE;
    }

    public void setCompletarNIE(Long completarNIE) {
        this.completarNIE = completarNIE;
    }

    public Boolean getLuceneEnabled() {
        return luceneEnabled;
    }

    public void setLuceneEnabled(Boolean luceneEnabled) {
        this.luceneEnabled = luceneEnabled;
    }

    public Long getTotalResultadosPersonas() {
        return totalResultadosPersonas;
    }

    public void setTotalResultadosPersonas(Long totalResultadosPersonas) {
        this.totalResultadosPersonas = totalResultadosPersonas;
    }

    public LazyLucenePersonaDataModel getLazyPersonaDataModel() {
        return lazyPersonaDataModel;
    }

    public void setLazyPersonaDataModel(LazyLucenePersonaDataModel lazyPersonaDataModel) {
        this.lazyPersonaDataModel = lazyPersonaDataModel;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Boolean getEstudiaNivelMedia() {
        return estudiaNivelMedia;
    }

    public void setEstudiaNivelMedia(Boolean estudiaNivelMedia) {
        this.estudiaNivelMedia = estudiaNivelMedia;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public Integer getTabActiveIndex() {
        return tabActiveIndex;
    }

    public void setTabActiveIndex(Integer tabActiveIndex) {
        this.tabActiveIndex = tabActiveIndex;
    }

}
