/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCursoDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsistenciaPersonalRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.CursoDocenteRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgCeldaDiaHoraCurso;
import sv.gob.mined.siges.web.dto.SgModuloFormacionDocente;
import sv.gob.mined.siges.web.dto.SgNivel;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSolicitudCursoDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaCurso;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoModulo;
import sv.gob.mined.siges.web.enumerados.EnumCeldaDiaHora;
import sv.gob.mined.siges.web.enumerados.EnumEstadoCurso;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudCurso;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloFormacionDocente;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNivel;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudCursoDocente;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroEspecialidad;
import sv.gob.mined.siges.web.restclient.DatoContratacionRestClient;
import sv.gob.mined.siges.web.restclient.ModuloFormacionDocenteRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudCursoDocenteRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class CursoDocenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CursoDocenteBean.class.getName());

    @Inject
    private CursoDocenteRestClient restClient;

    @Inject
    private PersonalSedeEducativaRestClient restPersonal;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private AsistenciaPersonalRestClient asistenciaClient;

    @Inject
    private DatoContratacionRestClient restContratos;

    @Inject
    private NivelRestClient restNivel;

    @Inject
    private ModuloFormacionDocenteRestClient restModulo;

    @Inject
    @Param(name = "id")
    private Long controlAsisId;

    @Inject
    @Param(name = "rev")
    private Long controlAsisRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "publish")
    private Boolean publicar;

    @Inject
    @Param(name = "close")
    private Boolean cerrar;

    @Inject
    @Param(name = "aplicar")
    private Boolean solicitud;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DireccionComponenteBean direccion;

    @Inject
    private SolicitudCursoDocenteRestClient restSolicitud;

    private Boolean soloLectura = Boolean.FALSE;
    private SgCursoDocente entidadEnEdicion;
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private SgSede sedeSeleccionada;
    private SofisComboG<SgTipoModulo> comboTipoModulo;
    private SofisComboG<SgCategoriaCurso> comboCategoriaCurso;
    private SofisComboG<SgNivel> comboNivel;
    private SofisComboG<SgEspecialidad> comboEspecialidad;
    private SofisComboG<SgModuloFormacionDocente> comboModulo;
    private Integer cantidadFilas = 2;
    private List<Integer> listaCantidad;
    private List<EnumCeldaDiaHora> listaDias;
    private LocalTime[][] matrizHoras;
    private Boolean[] checkDia;
    private Boolean verMensaje = Boolean.FALSE;

    public CursoDocenteBean() {
    }

    @PostConstruct
    public void init() {
        try {

            this.direccion.setVerMapa(Boolean.FALSE);
            this.direccion.setVerZona(Boolean.FALSE);
            this.direccion.setVerCanton(Boolean.FALSE);
            this.direccion.setVerCaserio(Boolean.FALSE);
            this.direccion.setVerTipoCalle(Boolean.FALSE);

            cargarCombos();
            if (controlAsisId != null && controlAsisId > 0) {
                if (controlAsisRev != null && controlAsisRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(controlAsisId, controlAsisRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(controlAsisId));
                    soloLectura = editable != null ? (editable ? (entidadEnEdicion.getCdsEstado().equals(EnumEstadoCurso.CREADO) ? !editable : !soloLectura) : !soloLectura) : soloLectura;
                    if (publicar != null) {
                        publicar = sessionBean.getOperaciones().contains(ConstantesOperaciones.PUBLICAR_CURSOS_DOCENTES) && entidadEnEdicion.getCdsEstado().equals(EnumEstadoCurso.CREADO);
                        soloLectura = Boolean.TRUE;
                    } else {
                        publicar = false;
                    }
                    if (cerrar != null) {
                        cerrar = sessionBean.getOperaciones().contains(ConstantesOperaciones.CERRAR_CURSOS_DOCENTES) && entidadEnEdicion.getCdsEstado().equals(EnumEstadoCurso.PUBLICADO);
                        soloLectura = Boolean.TRUE;
                    } else {
                        cerrar = false;
                    }
                    if (solicitud != null) {
                        solicitud = entidadEnEdicion.getCdsEstado().equals(EnumEstadoCurso.PUBLICADO);
                        if (solicitudPrevia()) {
                            solicitud = Boolean.FALSE;
                            verMensaje = Boolean.TRUE;
                        }
                        soloLectura = Boolean.TRUE;
                    } else {
                        solicitud = false;
                    }

                }
            } else {
                this.agregar();
            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }

    public Boolean solicitudPrevia() {
        try {
            String dui = String.valueOf(Long.valueOf(sessionBean.getEntidadUsuario().getUsuCodigo()));
            FiltroSolicitudCursoDocente fs = new FiltroSolicitudCursoDocente();
            fs.setScdCurso(entidadEnEdicion.getCdsPk());
            fs.setScdEstados(Arrays.asList(EnumEstadoSolicitudCurso.CREADA, EnumEstadoSolicitudCurso.APROBADA));
            fs.setScdDui(dui);
            return (restSolicitud.buscarTotal(fs) > 0);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_CURSOS_DOCENTES)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getCdsPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_CURSOS_DOCENTES)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_CURSOS_DOCENTES)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public String getTituloPagina() {
        if (this.controlAsisId == null) {
            return Etiquetas.getValue("agregarCursoDocente");
        } else if (this.controlAsisRev != null) {
            return Etiquetas.getValue("historialCursoDocente");
        } else if (this.solicitud) {
            return Etiquetas.getValue("aplicar") + " " + entidadEnEdicion.getCdsNombre();
        } else if (this.publicar) {
            return Etiquetas.getValue("publicar") + " " + entidadEnEdicion.getCdsNombre();
        } else if (this.cerrar) {
            return Etiquetas.getValue("cerrar") + " " + entidadEnEdicion.getCdsNombre();
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verCursoDocente") + " " + entidadEnEdicion.getCdsNombre();
        } else {
            return Etiquetas.getValue("edicionCursoDocente") + " " + entidadEnEdicion.getCdsNombre();
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});

            fc.setOrderBy(new String[]{"ccuNombre"});
            List<SgCategoriaCurso> lcc = restCatalogo.buscarCategoriaCurso(fc);
            comboCategoriaCurso = new SofisComboG(lcc, "ccuNombre");
            comboCategoriaCurso.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fc.setOrderBy(new String[]{"tmoNombre"});
            List<SgTipoModulo> ltm = restCatalogo.buscarTipoModulo(fc);
            comboTipoModulo = new SofisComboG(ltm, "tmoNombre");
            comboTipoModulo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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

            listaDias = new ArrayList(Arrays.asList(EnumCeldaDiaHora.values()));
            listaCantidad = new ArrayList();
            for (Integer i = 0; i < cantidadFilas; i++) {
                listaCantidad.add(i);
            }
            matrizHoras = new LocalTime[cantidadFilas][7];

            checkDia = new Boolean[7];
            Arrays.fill(checkDia, false);

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgCursoDocente();
    }

    public void actualizarCheck() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> param = context.getExternalContext().getRequestParameterMap();
        Integer col = param.get("col") != null ? Integer.valueOf(param.get("col")) : null;
        if (col != null) {
            this.matrizHoras[0][col] = null;
            this.matrizHoras[1][col] = null;
        }
    }

    public void actualizar(SgCursoDocente cac) {
        try {
            entidadEnEdicion = cac;
            sedeSeleccionada = entidadEnEdicion.getCdsSede();
            comboModulo.setSelectedT(entidadEnEdicion.getCdsModulo());
            comboCategoriaCurso.setSelectedT(entidadEnEdicion.getCdsCategoria());
            comboTipoModulo.setSelectedT(entidadEnEdicion.getCdsTipoModulo());
            comboNivel.setSelectedT(entidadEnEdicion.getCdsNivel());
            comboEspecialidad.setSelectedT(entidadEnEdicion.getCdsEspecialidad());

            List<SgCeldaDiaHoraCurso> listCeldas = entidadEnEdicion.getCdsCeldasDiaHora();
            for (SgCeldaDiaHoraCurso celda : listCeldas) {
                matrizHoras[celda.getCdcFila()][celda.getCdcDia().ordinal()] = celda.getCdcHora();
                checkDia[celda.getCdcDia().ordinal()] = Boolean.TRUE;
            }

            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getCdsPk() == null) {
                entidadEnEdicion.setCdsEstado(EnumEstadoCurso.CREADO);
            }
            entidadEnEdicion.setCdsModulo(comboModulo != null ? comboModulo.getSelectedT() : null);
            entidadEnEdicion.setCdsCategoria(comboCategoriaCurso != null ? comboCategoriaCurso.getSelectedT() : null);
            entidadEnEdicion.setCdsTipoModulo(comboTipoModulo != null ? comboTipoModulo.getSelectedT() : null);
            entidadEnEdicion.setCdsNivel(comboNivel != null ? comboNivel.getSelectedT() : null);
            entidadEnEdicion.setCdsEspecialidad(comboEspecialidad != null ? comboEspecialidad.getSelectedT() : null);
            entidadEnEdicion.setCdsSede(sedeSeleccionada);
            if (!entidadEnEdicion.getCdsOtraSede()) {
                entidadEnEdicion.setCdsSedeNombre(null);
                entidadEnEdicion.setCdsSedeDireccion(null);
            } else {
                sedeSeleccionada = null;
                entidadEnEdicion.setCdsSede(null);
            }

            List<SgCeldaDiaHoraCurso> celdas = new ArrayList();

            for (int i = 0; i < 7; i++) {
                if (this.checkDia[i]) {
                    for (int j = 0; j < this.cantidadFilas; j++) {
                        if (this.matrizHoras[j][i] != null) {
                            SgCeldaDiaHoraCurso nuevaCelda = new SgCeldaDiaHoraCurso();
                            nuevaCelda.setCdcDia(listaDias.get(i));
                            nuevaCelda.setCdcFila(j);
                            nuevaCelda.setCdcHora(matrizHoras[j][i]);

                            celdas.add(nuevaCelda);

                        }
                    }
                }
            }
            entidadEnEdicion.setCdsCeldasDiaHora(celdas);

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void publicar() {
        try {
            entidadEnEdicion = restClient.publicar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.CURSOS_DOCENTES);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cerrar() {
        try {
            entidadEnEdicion = restClient.cerrar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.CURSOS_DOCENTES);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void aplicarCurso() {
        try {
            SgPersonalSedeEducativa personal = obtenerPersonal();
            if (personal != null) {
                SgSolicitudCursoDocente entidadSolicitud = new SgSolicitudCursoDocente();

                entidadSolicitud.setScdCurso(entidadEnEdicion);
                entidadSolicitud.setScdEstado(EnumEstadoSolicitudCurso.CREADA);
                entidadSolicitud.setScdPersonal(personal);
                restSolicitud.guardar(entidadSolicitud);

                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                JSFUtils.redirectToPage(ConstantesPaginas.CURSOS_DOCENTES_DISPONIBLES);
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public SgPersonalSedeEducativa obtenerPersonal() {
        try {
            FiltroPersonalSedeEducativa fper = new FiltroPersonalSedeEducativa();
            fper.setPerDui(String.valueOf(Long.valueOf(sessionBean.getEntidadUsuario().getUsuCodigo())));

            SgPersonalSedeEducativa resultado = restPersonal.buscar(fper)
                    .stream()
                    .findFirst()
                    .orElse(null);
            if (resultado == null) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
            return resultado;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setMaxResults(11L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public CursoDocenteRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(CursoDocenteRestClient restClient) {
        this.restClient = restClient;
    }

    public PersonalSedeEducativaRestClient getRestPersonal() {
        return restPersonal;
    }

    public void setRestPersonal(PersonalSedeEducativaRestClient restPersonal) {
        this.restPersonal = restPersonal;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public AsistenciaPersonalRestClient getAsistenciaClient() {
        return asistenciaClient;
    }

    public void setAsistenciaClient(AsistenciaPersonalRestClient asistenciaClient) {
        this.asistenciaClient = asistenciaClient;
    }

    public Long getControlAsisId() {
        return controlAsisId;
    }

    public void setControlAsisId(Long controlAsisId) {
        this.controlAsisId = controlAsisId;
    }

    public Long getControlAsisRev() {
        return controlAsisRev;
    }

    public void setControlAsisRev(Long controlAsisRev) {
        this.controlAsisRev = controlAsisRev;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgCursoDocente getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCursoDocente entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<SgTipoModulo> getComboTipoModulo() {
        return comboTipoModulo;
    }

    public void setComboTipoModulo(SofisComboG<SgTipoModulo> comboTipoModulo) {
        this.comboTipoModulo = comboTipoModulo;
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

    public Integer getCantidadFilas() {
        return cantidadFilas;
    }

    public void setCantidadFilas(Integer cantidadFilas) {
        this.cantidadFilas = cantidadFilas;
    }

    public List<EnumCeldaDiaHora> getListaDias() {
        return listaDias;
    }

    public void setListaDias(List<EnumCeldaDiaHora> listaDias) {
        this.listaDias = listaDias;
    }

    public LocalTime[][] getMatrizHoras() {
        return matrizHoras;
    }

    public void setMatrizHoras(LocalTime[][] matrizHoras) {
        this.matrizHoras = matrizHoras;
    }

    public List<Integer> getListaCantidad() {
        return listaCantidad;
    }

    public void setListaCantidad(List<Integer> listaCantidad) {
        this.listaCantidad = listaCantidad;
    }

    public Boolean[] getCheckDia() {
        return checkDia;
    }

    public void setCheckDia(Boolean[] checkDia) {
        this.checkDia = checkDia;
    }

    public SofisComboG<SgModuloFormacionDocente> getComboModulo() {
        return comboModulo;
    }

    public void setComboModulo(SofisComboG<SgModuloFormacionDocente> comboModulo) {
        this.comboModulo = comboModulo;
    }

    public Boolean getPublicar() {
        return publicar;
    }

    public void setPublicar(Boolean publicar) {
        this.publicar = publicar;
    }

    public Boolean getCerrar() {
        return cerrar;
    }

    public void setCerrar(Boolean cerrar) {
        this.cerrar = cerrar;
    }

    public Boolean getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Boolean solicitud) {
        this.solicitud = solicitud;
    }

    public Boolean getVerMensaje() {
        return verMensaje;
    }

    public void setVerMensaje(Boolean verMensaje) {
        this.verMensaje = verMensaje;
    }

}
