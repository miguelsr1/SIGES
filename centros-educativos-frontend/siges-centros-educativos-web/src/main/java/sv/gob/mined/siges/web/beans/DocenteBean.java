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
import sv.gob.mined.siges.web.dto.SgAllegado;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgIdentificacion;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgTelefono;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoParentesco;
import sv.gob.mined.siges.web.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAllegado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AllegadoRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgUnirPersonal;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DocenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PersonalSedeEducativaBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long pseId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "rev")
    private Long pseRev;

    @Inject
    private PersonalSedeEducativaRestClient restClient;

    @Inject
    private PersonaRestClient personaClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ApplicationBean appBean;

    @Inject
    private AllegadoRestClient allegadoClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private FormacionDocenteBean formacionDocenteBean;

    @Inject
    private DocenteContratosBean docenteContratosBean;

    @Inject
    private DocenteEmpleadoBean docenteEmpleadoBean;

    @Inject
    private DocenteDocumentoBean docenteDocumentoBean;

    @Inject
    private DocenteExperienciaBean docenteExperienciaBean;

    @Inject
    private IdentificacionComponenteBean identificacionComponenteBean;

    private SgPersonalSedeEducativa entidadEnEdicion;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<TipoPersonalSedeEducativa> comboTiposPersonalSedeEducativa;
    private SgAllegado allegadoEnEdicion;
    private SofisComboG<SgTipoParentesco> comboTipoParentesco;
    private List<SgAllegado> listAllegados;
    private Boolean renderizarPanelFamilia = Boolean.FALSE;
    private String tabActiveId;
    private String completarDUIoNIP;
    private SgPersonalSedeEducativa personalUnir;
    private Boolean peronalUnirHabilitado = Boolean.FALSE;

    public DocenteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (pseId != null && pseId > 0) {
                if (pseRev != null && pseRev > 0) {
                    this.actualizar(restClient.obtenerEnRevision(pseId, pseRev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(pseId, Boolean.TRUE));
                    soloLectura = editable != null ? !editable : soloLectura;
                }
            } else {
                comboTiposPersonalSedeEducativa.setSelectedT(TipoPersonalSedeEducativa.DOC);
                agregar();
            }
            soloLectura = editable != null ? !editable : soloLectura;
            if (entidadEnEdicion != null) {
                if (!entidadEnEdicion.getPsePersona().getPerHabilitado()) {
                    soloLectura = Boolean.TRUE;
                }

                if (entidadEnEdicion.getPsePk() == null) {
                    //identificacionComponenteBean.setVerNIP(Boolean.FALSE);
                }

            }
            validarAcceso();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_PERSONAL_SEDE_EDUCATIVA)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (pseId == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_PERSONAL_SEDE_EDUCATIVA)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_PERSONAL_SEDE_EDUCATIVA)) {
                    LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.ACTUALIZAR_PERSONAL_SEDE_EDUCATIVA);
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void buscarRemplazoPersonal() {
        try {
            peronalUnirHabilitado = Boolean.FALSE;
            personalUnir = null;
            FiltroPersonalSedeEducativa fps = new FiltroPersonalSedeEducativa();
            fps.setIncluirCampos(new String[]{"psePersona.perDui", "psePersona.perNip",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perNombreBusqueda",
                "psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perFechaNacimiento",
                "pseVersion"});
            if (!StringUtils.isBlank(completarDUIoNIP)) {
                if ((entidadEnEdicion.getPsePersona().getPerDui() == null && entidadEnEdicion.getPsePersona().getPerNip() != null) || (entidadEnEdicion.getPsePersona().getPerDui() != null && entidadEnEdicion.getPsePersona().getPerNip() == null)) {
                    if (entidadEnEdicion.getPsePersona().getPerDui() == null && entidadEnEdicion.getPsePersona().getPerNip() != null) {
                        fps.setPerDui(completarDUIoNIP);
                    }
                    if (entidadEnEdicion.getPsePersona().getPerDui() != null && entidadEnEdicion.getPsePersona().getPerNip() == null) {
                        fps.setPerNip(completarDUIoNIP);
                    }
                    List<SgPersonalSedeEducativa> per = restClient.buscar(fps);
                    if (!per.isEmpty()) {
                        personalUnir = per.get(0);
                        if (personalUnir.getPsePersona().getPerDui() != null && personalUnir.getPsePersona().getPerNip() != null) {
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "Personal seleccionado inválido debido a que tiene NIP y DUI.", "");
                        } else {
                            peronalUnirHabilitado = Boolean.TRUE;
                        }
                    } else {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "No existe personal para los datos seleccionados.", "");
                    }
                    PrimeFaces.current().ajax().update("form:itemDetailEstudianteUnir");
                    return;
                }
            }

            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "No existe personal para los datos seleccionados", "");
            PrimeFaces.current().ajax().update("form:itemDetailEstudianteUnir");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void unirPersonal() {
        try {
            if (personalUnir != null) {
                SgUnirPersonal dto = new SgUnirPersonal();
                dto.setPersonalDestinoPk(entidadEnEdicion.getPsePk());
                dto.setPersonalOrigenPk(personalUnir.getPsePk());
                restClient.unirPersonal(dto);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                PrimeFaces.current().executeScript("PF('itemDialogEstudianteUnir').hide()");

                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                JSFUtils.redirectToPage(ConstantesPaginas.DOCENTE + "?id=" + entidadEnEdicion.getPsePk());
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONAL_VACIO), "");
                PrimeFaces.current().ajax().update("form:itemDetailEstudianteUnir");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void updateAddressCoordinates() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String lat = params.get("latitude");
        String lng = params.get("longitude");
        SgDireccion dir = this.getDireccionEnEdicion();
        if (lat != null) {
            dir.setDirLatitud(Double.parseDouble(lat));
        }
        if (lng != null) {
            dir.setDirLongitud(Double.parseDouble(lng));
        }
    }

    public void cargarFamiliares() {
        try {
            if (entidadEnEdicion != null && entidadEnEdicion.getPsePersona().getPerPk() != null) {
                FiltroAllegado ff = new FiltroAllegado();
                ff.setIncluirCampos(new String[]{"allTipoParentesco.tpaNombre",
                    "allPersona.perPrimerNombre",
                    "allPersona.perSegundoNombre",
                    "allPersona.perPrimerApellido",
                    "allPersona.perSegundoApellido",
                    "allVersion"});
                ff.setAllPersonaReferenciadaPk(entidadEnEdicion.getPsePersona().getPerPk());
                listAllegados = allegadoClient.buscar(ff);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiarCombos() {
        comboTiposPersonalSedeEducativa.setSelected(-1);
        comboTipoParentesco.setSelected(-1);
    }

    public void cargarCombos() {
        try {
            List<TipoPersonalSedeEducativa> listaTipoPersonas = new ArrayList();

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_DOCENTE)) {
                listaTipoPersonas.add(TipoPersonalSedeEducativa.DOC);
            }

            comboTiposPersonalSedeEducativa = new SofisComboG(listaTipoPersonas, "text");
            comboTiposPersonalSedeEducativa.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setIncluirCampos(new String[]{"tpaNombre", "tpaVersion"});
            List<SgTipoParentesco> list = catalogoRestClient.buscarTipoParentesco(fc);
            comboTipoParentesco = new SofisComboG(list, "tpaNombre");
            comboTipoParentesco.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgPersonalSedeEducativa();
        entidadEnEdicion.setPsePuedeAplicarPlaza(Boolean.TRUE);
        entidadEnEdicion.getPsePersona().setPerTieneIdentificacion(Boolean.TRUE);
        soloLectura = Boolean.FALSE;
    }

    public void actualizar(SgPersonalSedeEducativa personal) {
        try {
            entidadEnEdicion = personal;
            if (entidadEnEdicion.getPsePersona().getPerDireccion() == null) {
                this.entidadEnEdicion.getPsePersona().setPerDireccion(new SgDireccion());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {

            if (entidadEnEdicion.getPsePersona().getPerIngresoAlgunaIdentificacion()) {

                entidadEnEdicion = restClient.guardar(entidadEnEdicion, Boolean.FALSE);

                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.IDENTIFICACION_NO_INGRESADA), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina() {
        if (this.pseId == null) {
            return Etiquetas.getValue("agregarDocente");
        } else if (this.pseRev != null) {
            return Etiquetas.getValue("historialDocente") + " (" + entidadEnEdicion.getPseUltModUsuario() + (entidadEnEdicion.getPseUltModFecha() != null ? (" " + this.appBean.getDateTimeFormater().format(entidadEnEdicion.getPseUltModFecha())) : "") + ")";
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verDocente");
        } else {
            return Etiquetas.getValue("edicionDocente");
        }
    }

    public void agregarFamiliar() {
        allegadoEnEdicion = new SgAllegado();
        allegadoEnEdicion.setAllPersonaReferenciada(entidadEnEdicion.getPsePersona());
        allegadoEnEdicion.setAllEsFamiliar(Boolean.TRUE);
        comboTipoParentesco.setSelected(-1);
        renderizarPanelFamilia = Boolean.TRUE;
        allegadoEnEdicion.getAllPersona().setPerTieneIdentificacion(Boolean.TRUE);

    }

    public void editarFamiliar(SgAllegado familiar) {
        try {
            allegadoEnEdicion = allegadoClient.obtenerPorId(familiar.getAllPk());
            this.comboTipoParentesco.setSelectedT(allegadoEnEdicion.getAllTipoParentesco());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarFamiliar() {
        try {
            allegadoEnEdicion.setAllPersonaReferenciada(entidadEnEdicion.getPsePersona());
            allegadoEnEdicion = allegadoClient.guardar(allegadoEnEdicion);
            renderizarPanelFamilia = Boolean.FALSE;
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

    public void comboParentescoSelected() {
        allegadoEnEdicion.setAllTipoParentesco(comboTipoParentesco.getSelectedT());
    }

    public SgPersona getPersonaEnEdicion() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            if (this.allegadoEnEdicion != null) {
                return this.allegadoEnEdicion.getAllPersona();
            }
        } else if (this.entidadEnEdicion != null) {
            return entidadEnEdicion.getPsePersona();
        }
        return new SgPersona();
    }

    public SgDireccion getDireccionEnEdicion() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            if (this.allegadoEnEdicion != null) {
                return this.allegadoEnEdicion.getAllPersona().getPerDireccion();
            }
        } else if (this.entidadEnEdicion != null) {
            return this.entidadEnEdicion.getPsePersona().getPerDireccion();
        }
        return null;
    }

    public Boolean getRenderRestablecerIdentificacion() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            if (this.allegadoEnEdicion != null) {
                return (!this.soloLectura && BooleanUtils.isTrue(this.allegadoEnEdicion.getAllPersona().getPerSeBuscoEnBd()));
            }
        } else {
            return (!this.soloLectura && BooleanUtils.isTrue(this.entidadEnEdicion.getPsePersona().getPerSeBuscoEnBd()));
        }
        return Boolean.FALSE;
    }

    public Boolean getPersonaSoloLectura() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            return this.soloLectura || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_FAMILIARES_PERSONAL_SEDE_TAB);
        }
        return this.soloLectura || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_PERSONA_PERSONAL_SEDE);
    }

    public Boolean getRenderIdentPersonalSede() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean getDireccionSoloLectura() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            if (allegadoEnEdicion != null) {
                if (allegadoEnEdicion.getAllPersona() != null && allegadoEnEdicion.getAllPersona().getPerPk() == null) {
                    return Boolean.FALSE;
                }
                return (this.soloLectura
                        || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_FAMILIARES_PERSONAL_SEDE_TAB)
                        || BooleanUtils.isTrue(this.allegadoEnEdicion.getAllPersona().getPerSeEncontroIdentificacion()));
            }
        } else {
            if (this.entidadEnEdicion != null) {
                if (entidadEnEdicion.getPsePersona() != null && entidadEnEdicion.getPsePersona().getPerPk() == null) {
                    return Boolean.FALSE;
                }
                return (this.soloLectura
                        || !sessionBean.getOperaciones().contains(ConstantesOperaciones.EDITAR_PERSONA_PERSONAL_SEDE)
                        || BooleanUtils.isTrue(this.entidadEnEdicion.getPsePersona().getPerSeEncontroIdentificacion()));
            }
        }
        return Boolean.TRUE;
    }

    public void restablecerPersonal() {
        this.agregar();
    }

    public String getPersonaViewAction() {
        if (this.tabActiveId != null && this.tabActiveId.equals("allegados")) {
            return "intercambiarPersonaFamiliar";
        }
        return "intercambiarPersona";
    }

    public void intercambiarPersona(SgPersona p) throws Exception {
        SgPersona personaIngresada = entidadEnEdicion.getPsePersona();

        if (entidadEnEdicion.getPsePk() == null && p.getPerPk() == null) {
            //Nuevo caso persona consultada RNPN
            entidadEnEdicion.setPsePersona(p);
            entidadEnEdicion.getPsePersona().normalizarDatos();
            entidadEnEdicion.getPsePersona().actualizarIdentificaciones(personaIngresada);
            entidadEnEdicion.getPsePersona().actualizarIdentificaciones(p);
            entidadEnEdicion.getPsePersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            entidadEnEdicion.getPsePersona().setPerSeBuscoEnBd(Boolean.TRUE);
            entidadEnEdicion.getPsePersona().setPerSePermiteModificarIdentificacion(Boolean.TRUE);
            return;
        }

        Boolean resultado = Boolean.FALSE;

        FiltroPersonalSedeEducativa fe = new FiltroPersonalSedeEducativa();
        fe.setPerPk(p.getPerPk());
        fe.setIncluirCampos(new String[]{"pseVersion", "psePersona.perHabilitado"});
        List<SgPersonalSedeEducativa> personal = this.restClient.buscar(fe);
        if (!personal.isEmpty()) {
            if (BooleanUtils.isTrue(personal.get(0).getPsePersona().getPerHabilitado())) {
                //Personal existente
                resultado = Boolean.TRUE;
                entidadEnEdicion = this.restClient.obtenerPorId(personal.get(0).getPsePk(), Boolean.FALSE);
            }
        } else {
            //Seteamos persona existente a nuevo personal
            SgPersona persona = personaClient.obtenerPorId(p.getPerPk());
            if (BooleanUtils.isTrue(persona.getPerHabilitado())) {
                resultado = Boolean.TRUE;
                entidadEnEdicion.setPsePersona(persona);
            }
        }
        if (resultado) {
            entidadEnEdicion.getPsePersona().normalizarDatos();
            entidadEnEdicion.getPsePersona().actualizarIdentificaciones(personaIngresada);
            entidadEnEdicion.getPsePersona().setPerSeEncontroIdentificacion(Boolean.TRUE);
            entidadEnEdicion.getPsePersona().setPerSeBuscoEnBd(Boolean.TRUE);
            entidadEnEdicion.getPsePersona().setPerSePermiteModificarIdentificacion(Boolean.TRUE);
        } else {
            entidadEnEdicion.getPsePersona().setPerSeEncontroIdentificacion(Boolean.FALSE);
            entidadEnEdicion.getPsePersona().setPerSeBuscoEnBd(Boolean.FALSE);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONA_RETIRADO), "");
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
        fe.setAllPersonaReferenciadaPk(this.entidadEnEdicion.getPsePersona().getPerPk());
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
            SgPersona persona = personaClient.obtenerPorId(p.getPerPk());
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

    public void changeTab(TabChangeEvent event) {
        this.tabActiveId = event.getTab().getId();
        if (tabActiveId.equals("allegados") && listAllegados == null) {
            cargarFamiliares();
        } else if (tabActiveId.equals("tab_formacion_docente")) {
            //Cargar formacion
            if (formacionDocenteBean.getPersonalSede() == null) {
                formacionDocenteBean.personalSedeEducativa(entidadEnEdicion);
            }
        } else if (tabActiveId.equals("tab_docente_contratos")) {
            //Cargar contratos
            if (docenteContratosBean.getPersonalSede() == null) {
                docenteContratosBean.personalSedeEducativa(entidadEnEdicion);
            }
        } else if (tabActiveId.equals("tab_empleado")) {
            //Cargar datos empleado
            if (docenteEmpleadoBean.getPersonalSede() == null) {
                docenteEmpleadoBean.personalSedeEducativa(entidadEnEdicion);
            }
        } else if (tabActiveId.equals("tab_experiencia_laboral")) {
            if (docenteExperienciaBean.getPersonalSede() == null) {
                docenteExperienciaBean.personalSedeEducativa(entidadEnEdicion);
            }
        } else if (tabActiveId.equals("tab_docente_documentos")) {
            //Cargar datos empleado
            if (docenteDocumentoBean.getPersonalSede() == null) {
                docenteDocumentoBean.personalSedeEducativa(entidadEnEdicion);
            }
        }
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }

    public SgPersonalSedeEducativa getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPersonalSedeEducativa entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public void eliminarIdentificacion(SgIdentificacion identificacion) {
        this.entidadEnEdicion.getPsePersona().getPerIdentificaciones().remove(identificacion);
    }

    public void eliminarTelefono(SgTelefono telefono) {
        this.entidadEnEdicion.getPsePersona().getPerTelefonos().remove(telefono);
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SofisComboG<TipoPersonalSedeEducativa> getComboTiposPersonalSedeEducativa() {
        return comboTiposPersonalSedeEducativa;
    }

    public void setComboTiposPersonalSedeEducativa(SofisComboG<TipoPersonalSedeEducativa> comboTiposPersonalSedeEducativa) {
        this.comboTiposPersonalSedeEducativa = comboTiposPersonalSedeEducativa;
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

    public List<SgAllegado> getListAllegados() {
        return listAllegados;
    }

    public void setListAllegados(List<SgAllegado> listAllegados) {
        this.listAllegados = listAllegados;
    }

    public Boolean getRenderizarPanelFamilia() {
        return renderizarPanelFamilia;
    }

    public void setRenderizarPanelFamilia(Boolean renderizarPanelFamilia) {
        this.renderizarPanelFamilia = renderizarPanelFamilia;
    }

    public String getCompletarDUIoNIP() {
        return completarDUIoNIP;
    }

    public void setCompletarDUIoNIP(String completarDUIoNIP) {
        this.completarDUIoNIP = completarDUIoNIP;
    }

    public SgPersonalSedeEducativa getPersonalUnir() {
        return personalUnir;
    }

    public void setPersonalUnir(SgPersonalSedeEducativa personalUnir) {
        this.personalUnir = personalUnir;
    }

    public Boolean getPeronalUnirHabilitado() {
        return peronalUnirHabilitado;
    }

    public void setPeronalUnirHabilitado(Boolean peronalUnirHabilitado) {
        this.peronalUnirHabilitado = peronalUnirHabilitado;
    }

}
