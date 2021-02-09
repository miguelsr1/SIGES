/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import javax.faces.context.FacesContext;
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
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgCentroEducativo;
import sv.gob.mined.siges.web.dto.SgOAEyMiembros;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgPersona;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracionLite;
import sv.gob.mined.siges.web.dto.SgRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSolDeshabilitarPerJur;
import sv.gob.mined.siges.web.dto.catalogo.SgCargoOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgIdentificacionOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgSexo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
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
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCargoOAE;
import sv.gob.mined.siges.web.lazymodels.LazyOrganismoAdministracionEscolarDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyPersonaOrganismoAdministracionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DeshabilitarPersonaJuridicaRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.RelOAEIdentificacionOAERestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class OaeRenovarMiembroBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OaeRenovarMiembroBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long orgPadreId;

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

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private SgOrganismoAdministracionEscolar oaePadre = new SgOrganismoAdministracionEscolar();
    private SgOrganismoAdministracionEscolar entidadEnEdicion = new SgOrganismoAdministracionEscolar();
    private SgPersonaOrganismoAdministracion miembroReemplazar;
    private List<SgOrganismoAdministracionEscolar> historialOrganismoAdministracionEscolar = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyOrganismoAdministracionEscolarDataModel organismoAdministracionEscolarLazyModel;
    private SgPersonaOrganismoAdministracion personaEnEdicion;
    private List<SgCargoOAE> listaCargos;
    private SofisComboG<SgCargoOAE> comboCargoOAE;
    private SofisComboG<SgSexo> comboSexo;
    private LazyPersonaOrganismoAdministracionDataModel personaOrganismoAdministracionLazyModel;
    private FiltroPersonaOrganismoAdministrativo filtroPersona = new FiltroPersonaOrganismoAdministrativo();
    private FiltroNombreCompleto filtroNombreCompleto = new FiltroNombreCompleto();
    private List<SgPersonaOrganismoAdministracionLite> miembrosHistorico = new ArrayList<>();
    private SgSolDeshabilitarPerJur solicitudDpJ = new SgSolDeshabilitarPerJur();
    private Boolean existeSolicitud = Boolean.FALSE;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(pdf)$/";
    private Set<SgIdentificacionOAE> listIdentificacionOAE;
    private List<SgRelOAEIdentificacionOAE> listRelOAEIdentificacionOAE;
    private List<SgPersonaOrganismoAdministracion> listMiembros;
    private Long identificadorMiembro=0L;

    private Locale local;
    private String titulo;
    private String buscarDUI;
    private String buscarNIE;
    private String buscarNIP;
    private String buscarNIT;

    public OaeRenovarMiembroBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscarIdentificacionOAE();
            if (orgPadreId != null && orgPadreId > 0) {
                this.cargarRenovarMiembro(orgPadreId);

            }
            validarAcceso();
            prepararIdentificacionOAE();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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

    public void cargarRenovarMiembro(Long oaePadreId) {
        try {
            listMiembros = new ArrayList();
            if (oaePadreId != null) {
                FiltroOrganismoAdministrativoEscolar filtroPadre = new FiltroOrganismoAdministrativoEscolar();
                filtroPadre.setOaePk(oaePadreId);
                filtroPadre.setIncluirCampos(new String[]{"oaeVersion","oaeNombre","oaeFechaAcuerdo","oaeTipoOrganismoAdministrativo.toaPk","oaeTipoOrganismoAdministrativo.toaVersion","oaeMiembrosVigentes","oaeNumeroAcuerdo","oaeFechaLegalizacion", "oaeSede.sedPk","oaeSede.sedTipo","oaeSede.sedVersion","oaeSede.sedNombre","oaeSede.sedCodigo"});
                List<SgOrganismoAdministracionEscolar> listPadre = restClient.buscar(filtroPadre);
                if (listPadre != null && !listPadre.isEmpty()) {
                    oaePadre=listPadre.get(0);
                    FiltroPersonaOrganismoAdministrativo fpa = new FiltroPersonaOrganismoAdministrativo();
             
                    FiltroOrganismoAdministrativoEscolar filtro = new FiltroOrganismoAdministrativoEscolar();
                    filtro.setOaeRenovarMiembroPadre(oaePadreId);
                    filtro.setIncluirCampos(new String[]{"oaeVersion"});
                    List<SgOrganismoAdministracionEscolar> list = restClient.buscar(filtro);
                    if (!list.isEmpty()) {
                        JSFUtils.redirectToIndex();
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, "Ya existe oae", "");
                    } else {
                        entidadEnEdicion = (SgOrganismoAdministracionEscolar) SerializationUtils.clone(oaePadre);
                        entidadEnEdicion.setOaePk(null);
                        entidadEnEdicion.setOaeVersion(0);
                        entidadEnEdicion.setOaeActaIntegracion(null);
                        entidadEnEdicion.setOaeFechaActaIntegracion(null);                        
                        entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.ELABORACION);
                        sedeSeleccionada =restSede.obtenerPorIdLazy(oaePadre.getOaeSede().getSedPk());
                        cargarListaCargosSegunSede();

                        fpa.setPoaOrganismoAdministrativoEscolar(oaePadre.getOaePk());
                        listMiembros =new ArrayList(personaOrganismoRestClient.buscar(fpa));
                        identificadorMiembro=0L;
                        for(SgPersonaOrganismoAdministracion per:listMiembros){
                            if(per.getPoaPk()>identificadorMiembro){
                                identificadorMiembro=per.getPoaPk();
                            }
                        }
                        identificadorMiembro++;
                        
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
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

    public void cargarCombos() {
        try {

            comboCargoOAE = new SofisComboG();
            comboCargoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setOrderBy(new String[]{"sexNombre"});
            fc.setAscending(new boolean[]{true});
            fc.setIncluirCampos(new String[]{"sexNombre","sexCodigo", "sexVersion"});
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

    }

    public void confirmar() {
        try {
            if (listRelOAEIdentificacionOAE != null && !listRelOAEIdentificacionOAE.isEmpty()) {
                entidadEnEdicion.setOaeIdentificaciones(listRelOAEIdentificacionOAE);
            }
            entidadEnEdicion.setOaeSede(sedeSeleccionada);
            entidadEnEdicion.setOaeMiembrosRenovadoPadreFk(oaePadre);
            entidadEnEdicion.setOaeEsMiembroRenovado(Boolean.TRUE);
          //  entidadEnEdicion.setOaePersonasOrganismoAdministriativo(listMiembros);
            SgOAEyMiembros oaeMiembros=new SgOAEyMiembros();
            oaeMiembros.setOrganismo(entidadEnEdicion);
            listMiembros.forEach(c->c.setPoaOrganismoAdministrativoEscolar(null));
            oaeMiembros.setListMiembros(listMiembros);
            entidadEnEdicion = restClient.guardarRenovarMiembro(oaeMiembros);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
         
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            JSFUtils.redirectToPage(ConstantesPaginas.ORGANISMO_ADMINISTRACION_ESCOLAR + "?id="+this.entidadEnEdicion.getOaePk()+"&edit=true" );
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

    public void agregarPersona() {
        limpiarCombos();
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

    public void actualizarPersona(SgPersonaOrganismoAdministracion var) {
        try {
            JSFUtils.limpiarMensajesError();
            miembroReemplazar = null;
            limpiarCombos();
            personaEnEdicion = var;
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
            JSFUtils.limpiarMensajesError();
            limpiarCombos();
            agregarPersona();
            miembroReemplazar = personaOrganismoRestClient.obtenerPorId(var.getPoaPk());
            personaEnEdicion.setMiembroReemplazar(miembroReemplazar);
            setTitulo();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public String setTitulo() {
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

            if (personaOrganismoRestClient.validar(personaEnEdicion)) {
                if (personaEnEdicion.getPoaPk() != null) {
                    listMiembros.removeIf(c -> personaEnEdicion.getPoaPk().equals(c.getPoaPk()));
                    listMiembros.add(personaEnEdicion);
                } else {
                    personaEnEdicion.setPoaPk(identificadorMiembro);
                    identificadorMiembro++;
                    listMiembros.add(personaEnEdicion);
                }

                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                limpiarBusquedaPersonas();
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarPersona() {
        listMiembros.remove(personaEnEdicion);
        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        personaEnEdicion = null;
        //buscarMiembros();
        limpiarBusquedaPersonas();

    }

    public String limpiarBusquedaPersonas() {
        filtroPersona = new FiltroPersonaOrganismoAdministrativo();
        //filtroPersona.setPoaHabilitado(Boolean.TRUE);
        filtroNombreCompleto = new FiltroNombreCompleto();

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

    public LazyPersonaOrganismoAdministracionDataModel getPersonaOrganismoAdministracionLazyModel() {
        return personaOrganismoAdministracionLazyModel;
    }

    public void setPersonaOrganismoAdministracionLazyModel(LazyPersonaOrganismoAdministracionDataModel personaOrganismoAdministracionLazyModel) {
        this.personaOrganismoAdministracionLazyModel = personaOrganismoAdministracionLazyModel;
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

    public List<SgPersonaOrganismoAdministracion> getListMiembros() {
        return listMiembros;
    }

    public void setListMiembros(List<SgPersonaOrganismoAdministracion> listMiembros) {
        this.listMiembros = listMiembros;
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

}
