/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgCentroEducativo;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracionLite;
import sv.gob.mined.siges.web.dto.SgRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.dto.SgRelSustitucionMiembroOAE;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSolDeshabilitarPerJur;
import sv.gob.mined.siges.web.dto.SgSustitucionMiembroOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgCargoOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgIdentificacionOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudOAE;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSustitucionMiembroOAE;
import sv.gob.mined.siges.web.enumerados.TipoSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNombreCompleto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudesOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSustitucionMiembroOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCargoOAE;
import sv.gob.mined.siges.web.lazymodels.LazyOrganismoAdministracionEscolarDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DeshabilitarPersonaJuridicaRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.RelOAEIdentificacionOAERestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.SustitucionMiembroOAERestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class OrganismoAdministracionEscolarBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OrganismoAdministracionEscolarBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long orgId;

    @Inject
    @Param(name = "rev")
    private Long rev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private OrganismoAdministracionEscolarRestClient restClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private PersonaOrganismoAdministracionRestClient personaOrganismoRestClient;

    @Inject
    private CatalogosRestClient catalogosRestClient;

    @Inject
    private PersonaRestClient restPersona;

    @Inject
    private HandleArchivoBean handleArchivoBean;

    @Inject
    private DeshabilitarPersonaJuridicaRestClient deshabilitarPerJurCLient;

    @Inject
    private RelOAEIdentificacionOAERestClient RelOAEIdentificacionOAERestClient;
    
    @Inject
    private SustitucionMiembroOAERestClient sustitucionMiembroOAERestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private SgOrganismoAdministracionEscolar entidadEnEdicion = new SgOrganismoAdministracionEscolar();
    
    private List<SgOrganismoAdministracionEscolar> historialOrganismoAdministracionEscolar = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyOrganismoAdministracionEscolarDataModel organismoAdministracionEscolarLazyModel;
    
    private List<SgCargoOAE> listaCargos;
    private SofisComboG<SgCargoOAE> comboCargoOAE;
    private SofisComboG<SgSexo> comboSexo;
    private List<SgPersonaOrganismoAdministracion> personaOrganismoList;
    private FiltroPersonaOrganismoAdministrativo filtroPersona = new FiltroPersonaOrganismoAdministrativo();
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private List<SgPersonaOrganismoAdministracionLite> miembrosHistorico = new ArrayList<>();
    private SgSolDeshabilitarPerJur solicitudDpJ = new SgSolDeshabilitarPerJur();
    private Boolean existeSolicitud = Boolean.FALSE;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(pdf)$/";
    private Set<SgIdentificacionOAE> listIdentificacionOAE;
    private List<SgRelOAEIdentificacionOAE> listRelOAEIdentificacionOAE;
    private SgOrganismoAdministracionEscolar miembroRenovadoAsociado;
    
    private SgPersonaOrganismoAdministracion personaEnEdicion;
    private List<SgRelSustitucionMiembroOAE> relSustitucionMiembrosList=new ArrayList();
    private SgPersonaOrganismoAdministracion miembroReemplazar;
    private Boolean soloLecturaDialog;
    private Boolean existenSolicitudesRemplazo;
   

    private Locale local;
    private String titulo;
    private String buscarDUI;
    private String buscarNIE;
    private String buscarNIP;
    private String buscarNIT;

    public OrganismoAdministracionEscolarBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscarIdentificacionOAE();
            if (orgId != null && orgId > 0) {
                if (rev != null && rev > 0) {
                    this.actualizarRev(restClient.obtenerEnRevision(orgId, rev));
                    this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(orgId));
                    soloLectura = editable != null ? !editable : soloLectura;
                   
                    obtenerSolicitudOAE();
                }
            } else {
                this.agregar();
            }
            validarAcceso();
            prepararIdentificacionOAE();
            buscarMiembroRenovadoAsociado();
            buscarSiExistenSolicitudesRemplazoMiembros();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscarMiembroRenovadoAsociado() {
        try {
            if (entidadEnEdicion.getOaePk() != null) {
                FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
                filtro.setOaeRenovarMiembroPadre(entidadEnEdicion.getOaePk());
                filtro.setIncluirCampos(new String[]{"oaePk"});
                List<SgOrganismoAdministracionEscolar> list = restClient.buscar(filtro);
                if(list!=null && !list.isEmpty()){
                    miembroRenovadoAsociado=list.get(0);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    
    public void buscarSiExistenSolicitudesRemplazoMiembros(){
        try {
            existenSolicitudesRemplazo=Boolean.FALSE;
            if (entidadEnEdicion.getOaePk() != null) {
                FiltroSustitucionMiembroOAE filtro = new FiltroSustitucionMiembroOAE();
                filtro.setSmoOaeFk(entidadEnEdicion.getOaePk());
                filtro.setIncluirCampos(new String[]{"smoVersion"});
                List<SgSustitucionMiembroOAE> list = sustitucionMiembroOAERestClient.buscar(filtro);
                if(list!=null && !list.isEmpty()){
                    existenSolicitudesRemplazo=Boolean.TRUE;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_ORGANISMO_ADMINISTRACION_ESCOLAR)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getOaePk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_ORGANISMO_ADMINISTRACION_ESCOLAR)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_ORGANISMO_ADMINISTRACION_ESCOLAR)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void buscarIdentificacionOAE() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            listIdentificacionOAE = new HashSet<SgIdentificacionOAE>(catalogosRestClient.buscarIdentificacionOAE(fc));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void prepararIdentificacionOAE() {
        try {
            Set<SgIdentificacionOAE> elementosSinAgregar = new HashSet<SgIdentificacionOAE>(listIdentificacionOAE);
            listRelOAEIdentificacionOAE = new ArrayList();
            if (entidadEnEdicion.getOaePk() != null) {
                FiltroRelOAEIdentificacionOAE filtroRel = new FiltroRelOAEIdentificacionOAE();
                filtroRel.setIdentificacionHabilitada(Boolean.TRUE);
                filtroRel.setRioOrganismoAdministracionEscolarFk(entidadEnEdicion.getOaePk());
                listRelOAEIdentificacionOAE = new ArrayList(RelOAEIdentificacionOAERestClient.buscar(filtroRel));
                Set<SgIdentificacionOAE> elementos = new HashSet<SgIdentificacionOAE>(listRelOAEIdentificacionOAE.stream().map(c -> c.getRioIdentificacionOAEfk()).collect(Collectors.toList()));
                elementosSinAgregar.removeAll(elementos);
            }
            for (SgIdentificacionOAE id : elementosSinAgregar) {
                SgRelOAEIdentificacionOAE rel = new SgRelOAEIdentificacionOAE();
                rel.setRioIdentificacionOAEfk(id);
                listRelOAEIdentificacionOAE.add(rel);
            }
            Collections.sort(listRelOAEIdentificacionOAE, (o1, o2) -> o1.getRioIdentificacionOAEfk().getIoaPk().compareTo(o2.getRioIdentificacionOAEfk().getIoaPk()));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void estadoLectura() {
        if (entidadEnEdicion.getOaePk() != null) {
            if (!EnumEstadoOrganismoAdministrativo.ELABORACION.equals(entidadEnEdicion.getOaeEstado())
                    && !EnumEstadoOrganismoAdministrativo.AMPLIAR_DATOS.equals(entidadEnEdicion.getOaeEstado()) ) {
                soloLectura = Boolean.TRUE;
            }
        }
    }

    public void cargarCombos() {
        try {

            comboCargoOAE = new SofisComboG();
            comboCargoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"sexNombre"});
            fc.setAscending(new boolean[]{true});
            fc.setIncluirCampos(new String[]{"sexNombre","sexCodigo" ,"sexVersion"});
            comboSexo = new SofisComboG(catalogosRestClient.buscarSexo(fc), "sexNombre");
            comboSexo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarListaCargosSegunSede() {
        try {
            FiltroCargoOAE fco = new FiltroCargoOAE();
            fco.setHabilitado(Boolean.TRUE);
            fco.setOrderBy(new String[]{"coaOrden"});
            fco.setAscending(new boolean[]{true});
            fco.setIncluirCampos(new String[]{"coaOrden", "coaVersion", "coaNombre", "coaNombreMasculino"});

            SgCentroEducativo centro = (SgCentroEducativo) sedeSeleccionada;
            fco.setTipoOrganismoAdministrativoPk(centro.getCedTipoOrganismoAdministrativo().getToaPk());

            listaCargos = catalogosRestClient.buscarCargoOAE(fco);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarComboCargos() {
        if (BooleanUtils.isTrue(personaEnEdicion.getPoaPrerregistro())) {
            if (personaEnEdicion.getPoaPersona().getPerPk() != null) {
                if (personaEnEdicion.getPoaPersona().getPerSexo().getSexPk().equals(Constantes.PK_SEXO_MASCULINO)) {
                    comboCargoOAE = new SofisComboG(listaCargos, "coaNombreMasculino");
                } else {
                    comboCargoOAE = new SofisComboG(listaCargos, "coaNombre");
                }
            } else {
                comboCargoOAE = new SofisComboG();
            }
            comboCargoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } else {
            if (comboSexo.getSelectedT() != null) {
                if (comboSexo.getSelectedT().getSexPk().equals(Constantes.PK_SEXO_MASCULINO)) {
                    comboCargoOAE = new SofisComboG(listaCargos, "coaNombreMasculino");
                } else {
                    comboCargoOAE = new SofisComboG(listaCargos, "coaNombre");
                }
            } else {
                comboCargoOAE = new SofisComboG();
            }
            comboCargoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }

    }

    public String mostrarCargo(SgPersonaOrganismoAdministracion org) {
        if (org.getPoaSexo() != null) {
            if (org.getPoaSexo().getSexCodigo().equals(Constantes.CODIGO_SEXO_MASCULINO)) {
                return org.getPoaCargo().getCoaNombreMasculino();
            } else {
                return org.getPoaCargo().getCoaNombre();
            }
        }
        return null;

    }

    public String mostrarCargo(SgPersonaOrganismoAdministracionLite org) {
        if (org.getPoaSexo() != null) {
            if (org.getPoaSexo().getSexCodigo().equals(Constantes.CODIGO_SEXO_MASCULINO)) {
                return org.getPoaCargo().getCoaNombreMasculino();
            } else {
                return org.getPoaCargo().getCoaNombre();
            }
        }
        return null;

    }

    private void limpiarCombos() {
        if (comboSexo != null) {
            comboSexo.setSelected(-1);
        }
        comboCargoOAE = new SofisComboG();
        comboCargoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void limpiar() {
        filtroPersona = new FiltroPersonaOrganismoAdministrativo();
        buscarMiembros();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgOrganismoAdministracionEscolar();
        entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.ELABORACION);
    }

    public void actualizar(SgOrganismoAdministracionEscolar var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgOrganismoAdministracionEscolar) SerializationUtils.clone(var);
        sedeSeleccionada = entidadEnEdicion.getOaeSede();
        cargarListaCargosSegunSede();
        buscarMiembros();
    }

    public void obtenerSolicitudOAE() {
        try {
            FiltroSolicitudesOAE filtroOae = new FiltroSolicitudesOAE();
            filtroOae.setDpjOaeFk(orgId);
            Long conteo = deshabilitarPerJurCLient.buscarTotal(filtroOae);
            if (conteo > 0) {
                existeSolicitud = Boolean.TRUE;
            }
        } catch (BusinessException ex) {
            Logger.getLogger(OrganismoAdministracionEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OrganismoAdministracionEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarRev(SgOrganismoAdministracionEscolar var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgOrganismoAdministracionEscolar) SerializationUtils.clone(var);
        if (entidadEnEdicion.getOaePersonasOrganismoAdministriativoLite() != null) {
            miembrosHistorico.addAll(entidadEnEdicion.getOaePersonasOrganismoAdministriativoLite());
            totalResultados = Long.parseLong(String.valueOf(miembrosHistorico.size()));
        }
        sedeSeleccionada = entidadEnEdicion.getOaeSede();
        //buscarMiembros();
    }

    public void guardar() {
        try {
            if (listRelOAEIdentificacionOAE != null && !listRelOAEIdentificacionOAE.isEmpty()) {
                entidadEnEdicion.setOaeIdentificaciones(listRelOAEIdentificacionOAE);
            }
            entidadEnEdicion.setOaeSede(sedeSeleccionada);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            cargarListaCargosSegunSede();
            limpiar();
            prepararIdentificacionOAE();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getOaePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");

            entidadEnEdicion = null;
            limpiar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setSedTipos(Arrays.asList(new TipoSede[]{TipoSede.CED_OFI, TipoSede.CED_PRI}));
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
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

    public void buscarMiembros() {
        try {
            filtroPersona.setIncluirCampos(new String[]{
                "poaPersona.perDui",
                "poaPersona.perPk",
                "poaPersona.perVersion",
                "poaDui",
                "poaPersona.perNit",
                "poaSexo",
                "poaNit",
                "poaPersona.perNie",
                "poaPersona.perPrimerNombre",
                "poaPersona.perSegundoNombre",
                "poaPersona.perPrimerApellido",
                "poaPersona.perSegundoApellido",
                "poaNombre",
                "poaPrerregistro",
                "poaCargo.coaNombre",
                "poaCargo.coaNombreMasculino",
                "poaCargo.coaPk",
                "poaCargo.coaVersion",
                "poaDesde",
                "poaHasta",
                "poaHabilitado"});

            
            filtroPersona.setPoaOrganismoAdministrativoEscolar(entidadEnEdicion.getOaePk());
            filtroPersona.setIncluirDatoHabiltadoRemplazo(Boolean.TRUE);
            
            personaOrganismoList =personaOrganismoRestClient.buscar(filtroPersona);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarPersona() {
        limpiarCombos();
        soloLecturaDialog=Boolean.FALSE;
        miembroReemplazar = null;
        personaEnEdicion = new SgPersonaOrganismoAdministracion();
        buscarDUI = null;
        buscarNIE = null;
        buscarNIP = null;
        buscarNIT = null;
        setTitulo();
    }

    public void agregarSolicitud() {
        solicitudDpJ = new SgSolDeshabilitarPerJur();
    }

    //Crea una solicitud para dejar sin efecto a la persona jurídica
    public void solicitudDeshabilitarPerJuridica() {
        try {
            solicitudDpJ.setDpjOaeFk(entidadEnEdicion);
            solicitudDpJ.setDpjEstado(EnumEstadoSolicitudOAE.ENVIADA);
            deshabilitarPerJurCLient.guardar(solicitudDpJ);
            existeSolicitud = Boolean.TRUE;
            PrimeFaces.current().executeScript("PF('itemDialogSol').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Sube acta de OAE
     *
     * @param event
     */
    public void subirActaOae(FileUploadEvent event) {
        try {
            solicitudDpJ.setDpjActa(new SgArchivo());
            handleArchivoBean.subirArchivoTmp(event.getFile(), solicitudDpJ.getDpjActa());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarPersona(SgPersonaOrganismoAdministracion var,Boolean soloLec) {
        try {
            soloLecturaDialog=soloLec;
            JSFUtils.limpiarMensajesError();
            miembroReemplazar = null;
            limpiarCombos();
            personaEnEdicion = personaOrganismoRestClient.obtenerPorId(var.getPoaPk());
            personaEnEdicion.setMiembroReemplazar(null);
            comboSexo.setSelectedT(personaEnEdicion.getPoaSexo());
            cargarComboCargos();
            comboCargoOAE.setSelectedT(personaEnEdicion.getPoaCargo());
            if (personaEnEdicion.getPoaPersona() != null) {
                buscarDUI = personaEnEdicion.getPoaPersona().getPerDui();
                buscarNIT = personaEnEdicion.getPoaPersona().getPerNit();
                buscarNIE = personaEnEdicion.getPoaPersona().getPerNie() != null ? personaEnEdicion.getPoaPersona().getPerNie().toString() : "";
                buscarNIP = personaEnEdicion.getPoaPersona().getPerNip() != null ? personaEnEdicion.getPoaPersona().getPerNip().toString() : "";
            }
            setTitulo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public void reemplazarPersona(SgPersonaOrganismoAdministracion var) {
        try {
            soloLecturaDialog=Boolean.FALSE;
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            agregarPersona();
            miembroReemplazar = personaOrganismoRestClient.obtenerPorId(var.getPoaPk());   
            personaEnEdicion.setPoaHabilitado(Boolean.FALSE);
            setTitulo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public String setTitulo() {
        if(BooleanUtils.isTrue(soloLecturaDialog)){
            titulo= "Ver miembro";
            return titulo;
        }
        titulo = Etiquetas.getValue("hagregarMiembro", local);
        if (miembroReemplazar != null && miembroReemplazar.getPoaPk() != null) {
            titulo = Etiquetas.getValue("hreemplazarMiembro", local);
        } else {
            if (personaEnEdicion != null && personaEnEdicion.getPoaPk() != null) {
                titulo = Etiquetas.getValue("heditarMiembro", local);
            } else {
                titulo = Etiquetas.getValue("hagregarMiembro", local);
            }
        }
        return titulo;
    }

    public void buscarPersona() {
        try {
            if (StringUtils.isBlank(buscarDUI) && StringUtils.isBlank(buscarNIE) && StringUtils.isBlank(buscarNIP) && StringUtils.isBlank(buscarNIT)) {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.IDENTIFICACION_NO_INGRESADA), "");
                return;
            }
            //Buscar a la persona por cualquiera de las identificaciones
            FiltroPersona fper = new FiltroPersona();
            fper.setDui(buscarDUI);
            fper.setNie(!StringUtils.isBlank(buscarNIE) ? Long.valueOf(buscarNIE) : null);
            fper.setNip(!StringUtils.isBlank(buscarNIP) ? buscarNIP : null);
            fper.setNit(!StringUtils.isBlank(buscarNIT) ? buscarNIT : null);

            fper.setIncluirCampos(new String[]{"perPk", "perVersion",
                "perSexo",
                "perPrimerNombre",
                "perSegundoNombre",
                "perPrimerApellido",
                "perSegundoApellido",
                "perDui",
                "perNie",
                "perNip",
                "perNit"});

            List<SgPersona> lper = restPersona.buscar(fper);
            if (!lper.isEmpty()) {
                if (lper.size() == 1) {
                    personaEnEdicion.setPoaPersona(lper.get(0));
                    if (personaEnEdicion.getPoaPersona().getPerNie() != null) {
                        buscarNIE = personaEnEdicion.getPoaPersona().getPerNie().toString();
                    }
                    buscarNIP = personaEnEdicion.getPoaPersona().getPerNip();
                    buscarNIT = personaEnEdicion.getPoaPersona().getPerNit();
                    buscarDUI = personaEnEdicion.getPoaPersona().getPerDui();

                    cargarComboCargos();
                } else {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.MULTIPLES_PERSONAS_CON_MISMA_IDENTIFICACION), "");
                    return;
                }
            } else {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.PERSONA_NO_ENCONTRADA), "");
                return;
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarPersona() {
        try {
            
            if (BooleanUtils.isFalse(personaEnEdicion.getPoaPrerregistro())) {

                if (StringUtils.isBlank(personaEnEdicion.getPoaDui()) && StringUtils.isBlank(personaEnEdicion.getPoaNit())) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.IDENTIFICACION_NO_INGRESADA), "");
                    return;
                }
                //Validar que el DUI ingresado no le pertenezca a otra persona
                FiltroPersona fper = new FiltroPersona();
                fper.setDui(personaEnEdicion.getPoaDui());
                fper.setNit(personaEnEdicion.getPoaNit());
                fper.setIncluirCampos(new String[]{"perPk", "perVersion"});

                List<SgPersona> lper = restPersona.buscar(fper);
                if (!lper.isEmpty()) {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP,
                            Mensajes.obtenerMensaje(Mensajes.ERROR_IDENTIFICACION_DUPLICADA_OAE), "");
                    return;
                }
                personaEnEdicion.setPoaSexo(comboSexo.getSelectedT());

            } else {
                if (personaEnEdicion.getPoaPersona() != null) {
                    personaEnEdicion.setPoaSexo(personaEnEdicion.getPoaPersona().getPerSexo());
                }
            }
            personaEnEdicion.setPoaCargo(comboCargoOAE.getSelectedT());
            
            if(miembroReemplazar!=null && miembroReemplazar.getPoaPk()!=null){
                if(personaOrganismoRestClient.validar(personaEnEdicion)){
                    personaEnEdicion.setPoaHabilitado(Boolean.FALSE);
                    List<SgRelSustitucionMiembroOAE> list=relSustitucionMiembrosList.stream().filter(c->c.getRsmMiembroaSustituirFk().equals(miembroReemplazar)).collect(Collectors.toList());
                    if(list.isEmpty()){
                        SgRelSustitucionMiembroOAE rel=new SgRelSustitucionMiembroOAE();
                        rel.setRsmMiembroaSustituirFk(miembroReemplazar);                        
                        rel.setRsmMiembroSustituyenteFk(personaEnEdicion);
                        relSustitucionMiembrosList.add(rel);
                    }else{
                        list.get(0).setRsmMiembroSustituyenteFk(personaEnEdicion);
                    }
                     PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "Se agregó solicitud de reemplazo a la lista.", "");
                    miembroReemplazar=null;
                    personaEnEdicion=null;
                    return;
                }else{
                    return;
                }
                
                
            }else{
                personaEnEdicion.setPoaOrganismoAdministrativoEscolar(entidadEnEdicion);
                
                personaEnEdicion = personaOrganismoRestClient.guardar(personaEnEdicion);
            }          
            

         
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            //buscarMiembros();
            limpiarBusquedaPersonas();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void confirmarSustitucion(){
        try{
            SgSustitucionMiembroOAE sust=new SgSustitucionMiembroOAE();
            sust.setSmoEstado(EnumEstadoSustitucionMiembroOAE.PENDIENTE);
            sust.setSmoFecha(LocalDate.now());
            sust.setSmoOaeFk(entidadEnEdicion);
            sust.setSmoRelSustitucionMiembroOAE(relSustitucionMiembrosList);
            sust=sustitucionMiembroOAERestClient.guardar(sust);
            relSustitucionMiembrosList=new ArrayList();
            buscarMiembros();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarPersona() {
        try {
            personaOrganismoRestClient.eliminar(personaEnEdicion.getPoaPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            personaEnEdicion = null;
            //buscarMiembros();
            limpiarBusquedaPersonas();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String limpiarBusquedaPersonas() {
        filtroPersona = new FiltroPersonaOrganismoAdministrativo();
        //filtroPersona.setPoaHabilitado(Boolean.TRUE);
        filtroNombreCompleto = new FiltroNombreCompleto();
        buscarMiembros();
        return null;
    }

    public void filtroNombreCompletoSeleccionar(FiltroNombreCompleto filtroNombre) {
        if (filtroNombre != null) {
            filtroPersona.setPerPrimerNombre(filtroNombre.getPerPrimerNombre());
            filtroPersona.setPerSegundoNombre(filtroNombre.getPerSegundoNombre());
            filtroPersona.setPerTercerNombre(filtroNombre.getPerTercerNombre());
            filtroPersona.setPerPrimerApellido(filtroNombre.getPerPrimerApellido());
            filtroPersona.setPerSegundoApellido(filtroNombre.getPerSegundoApellido());
            filtroPersona.setPerTercerApellido(filtroNombre.getPerTercerApellido());
            if (!StringUtils.isBlank(filtroNombre.getNombreCompleto())) {
                filtroPersona.setPerNombreCompleto(null);
            }
        }
        PrimeFaces.current().ajax().update("form:pnlSearch");
    }
    
    public void enviar() {
        try {
            restClient.enviar(entidadEnEdicion.getOaePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            this.actualizar(restClient.obtenerPorId(entidadEnEdicion.getOaePk()));
            estadoLectura();
            obtenerSolicitudOAE();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void agregarRenovacionMiembros(){
        try {
            FiltroSustitucionMiembroOAE fsm=new FiltroSustitucionMiembroOAE();
            fsm.setSmoEstado(EnumEstadoSustitucionMiembroOAE.PENDIENTE);
            fsm.setSmoOaeFk(entidadEnEdicion.getOaePk());
            fsm.setIncluirCampos(new String[]{"smoVersion"});
            fsm.setMaxResults(1L);
            List<SgSustitucionMiembroOAE> listSust=sustitucionMiembroOAERestClient.buscar(fsm);
            if(listSust!=null && !listSust.isEmpty()){
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE,"No se puede renovar miembros ya que existen solicitudes de sustitución de miembros pendientes.", "");
            }else{
                PrimeFaces.current().executeScript("PF('confirmDialogRenovarMiembros').show()");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgOrganismoAdministracionEscolar getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgOrganismoAdministracionEscolar entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgOrganismoAdministracionEscolar> getHistorialOrganismoAdministracionEscolar() {
        return historialOrganismoAdministracionEscolar;
    }

    public void setHistorialOrganismoAdministracionEscolar(List<SgOrganismoAdministracionEscolar> historialOrganismoAdministracionEscolar) {
        this.historialOrganismoAdministracionEscolar = historialOrganismoAdministracionEscolar;
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

    public LazyOrganismoAdministracionEscolarDataModel getOrganismoAdministracionEscolarLazyModel() {
        return organismoAdministracionEscolarLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyOrganismoAdministracionEscolarDataModel organismoAdministracionEscolarLazyModel) {
        this.organismoAdministracionEscolarLazyModel = organismoAdministracionEscolarLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgPersonaOrganismoAdministracion getPersonaEnEdicion() {
        return personaEnEdicion;
    }

    public void setPersonaEnEdicion(SgPersonaOrganismoAdministracion personaEnEdicion) {
        this.personaEnEdicion = personaEnEdicion;
    }

    public SofisComboG<SgCargoOAE> getComboCargoOAE() {
        return comboCargoOAE;
    }

    public void setComboCargoOAE(SofisComboG<SgCargoOAE> comboCargoOAE) {
        this.comboCargoOAE = comboCargoOAE;
    }

    public List<SgPersonaOrganismoAdministracion> getPersonaOrganismoList() {
        return personaOrganismoList;
    }

    public void setPersonaOrganismoList(List<SgPersonaOrganismoAdministracion> personaOrganismoList) {
        this.personaOrganismoList = personaOrganismoList;
    }

    public FiltroPersonaOrganismoAdministrativo getFiltroPersona() {
        return filtroPersona;
    }

    public void setFiltroPersona(FiltroPersonaOrganismoAdministrativo filtroPersona) {
        this.filtroPersona = filtroPersona;
    }

    public String getBuscarDUI() {
        return buscarDUI;
    }

    public void setBuscarDUI(String buscarDUI) {
        if (!StringUtils.isEmpty(buscarDUI)) {
            buscarDUI = StringUtils.leftPad(buscarDUI, 9, "0");
        }
        this.buscarDUI = buscarDUI;
    }

    public String getBuscarNIE() {
        return buscarNIE;
    }

    public void setBuscarNIE(String buscarNIE) {
        this.buscarNIE = buscarNIE;
    }

    public String getBuscarNIP() {
        return buscarNIP;
    }

    public void setBuscarNIP(String buscarNIP) {
        this.buscarNIP = buscarNIP;
    }

    public String getBuscarNIT() {
        return buscarNIT;
    }

    public void setBuscarNIT(String buscarNIT) {
        this.buscarNIT = buscarNIT;
    }

    public FiltroNombreCompleto getFiltroNombreCompleto() {
        return filtroNombreCompleto;
    }

    public void setFiltroNombreCompleto(FiltroNombreCompleto filtroNombreCompleto) {
        this.filtroNombreCompleto = filtroNombreCompleto;
    }

    public SofisComboG<SgSexo> getComboSexo() {
        return comboSexo;
    }

    public void setComboSexo(SofisComboG<SgSexo> comboSexo) {
        this.comboSexo = comboSexo;
    }

    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    public List<SgPersonaOrganismoAdministracionLite> getMiembrosHistorico() {
        return miembrosHistorico;
    }

    public void setMiembrosHistorico(List<SgPersonaOrganismoAdministracionLite> miembrosHistorico) {
        this.miembrosHistorico = miembrosHistorico;
    }

    public SgSolDeshabilitarPerJur getSolicitudDpJ() {
        return solicitudDpJ;
    }

    public void setSolicitudDpJ(SgSolDeshabilitarPerJur solicitudDpJ) {
        this.solicitudDpJ = solicitudDpJ;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public Boolean getExisteSolicitud() {
        return existeSolicitud;
    }

    public void setExisteSolicitud(Boolean existeSolicitud) {
        this.existeSolicitud = existeSolicitud;
    }

    public SgPersonaOrganismoAdministracion getMiembroReemplazar() {
        return miembroReemplazar;
    }

    public void setMiembroReemplazar(SgPersonaOrganismoAdministracion miembroReemplazar) {
        this.miembroReemplazar = miembroReemplazar;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Set<SgIdentificacionOAE> getListIdentificacionOAE() {
        return listIdentificacionOAE;
    }

    public void setListIdentificacionOAE(Set<SgIdentificacionOAE> listIdentificacionOAE) {
        this.listIdentificacionOAE = listIdentificacionOAE;
    }

    public List<SgRelOAEIdentificacionOAE> getListRelOAEIdentificacionOAE() {
        return listRelOAEIdentificacionOAE;
    }

    public void setListRelOAEIdentificacionOAE(List<SgRelOAEIdentificacionOAE> listRelOAEIdentificacionOAE) {
        this.listRelOAEIdentificacionOAE = listRelOAEIdentificacionOAE;
    }

    public SgOrganismoAdministracionEscolar getMiembroRenovadoAsociado() {
        return miembroRenovadoAsociado;
    }

    public void setMiembroRenovadoAsociado(SgOrganismoAdministracionEscolar miembroRenovadoAsociado) {
        this.miembroRenovadoAsociado = miembroRenovadoAsociado;
    }

    public List<SgRelSustitucionMiembroOAE> getRelSustitucionMiembrosList() {
        return relSustitucionMiembrosList;
    }

    public void setRelSustitucionMiembrosList(List<SgRelSustitucionMiembroOAE> relSustitucionMiembrosList) {
        this.relSustitucionMiembrosList = relSustitucionMiembrosList;
    }

    public Boolean getSoloLecturaDialog() {
        return soloLecturaDialog;
    }

    public void setSoloLecturaDialog(Boolean soloLecturaDialog) {
        this.soloLecturaDialog = soloLecturaDialog;
    }

    public Boolean getExistenSolicitudesRemplazo() {
        return existenSolicitudesRemplazo;
    }

    public void setExistenSolicitudesRemplazo(Boolean existenSolicitudesRemplazo) {
        this.existenSolicitudesRemplazo = existenSolicitudesRemplazo;
    }

}
