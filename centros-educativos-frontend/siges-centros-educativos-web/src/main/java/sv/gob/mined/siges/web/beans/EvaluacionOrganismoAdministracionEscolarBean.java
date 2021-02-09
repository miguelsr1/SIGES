/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCentroEducativo;
import sv.gob.mined.siges.web.dto.SgItemsEvaluarOAE;
import sv.gob.mined.siges.web.dto.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCargoOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgIdentificacionOAE;
import sv.gob.mined.siges.web.dto.catalogo.SgItemEvaluarOrganismo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelOAEIdentificacionOAE;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCargoOAE;
import sv.gob.mined.siges.web.lazymodels.LazyOrganismoAdministracionEscolarDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.OrganismoAdministracionEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
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
public class EvaluacionOrganismoAdministracionEscolarBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EvaluacionOrganismoAdministracionEscolarBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long orgId;

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
    private RelOAEIdentificacionOAERestClient RelOAEIdentificacionOAERestClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private SgOrganismoAdministracionEscolar entidadEnEdicion = new SgOrganismoAdministracionEscolar();
    private List<SgOrganismoAdministracionEscolar> historialOrganismoAdministracionEscolar = new ArrayList();
    private Integer paginado = 10;
    private Integer paginadoI = 10;
    private Long totalResultados;
    private Long totalResultadosI;
    private LazyOrganismoAdministracionEscolarDataModel organismoAdministracionEscolarLazyModel;
    private SgPersonaOrganismoAdministracion personaEnEdicion;
    private SofisComboG<SgCargoOAE> comboCargoOAE;
    private SgPersonaOrganismoAdministracion miembroReemplazo = new SgPersonaOrganismoAdministracion();

    private List<SgPersonaOrganismoAdministracion> listPersonasHabilitadas;
    private List<SgPersonaOrganismoAdministracion> listPersonasNoHabilitadas;
    private FiltroPersonaOrganismoAdministrativo filtroPersona;
    private SelectItem[] acciones;
    private Integer resultado;
    private List<SgItemEvaluarOrganismo> itemsSeleccionados = new ArrayList<>();
    private List<SgItemEvaluarOrganismo> itemsEvaluar = new ArrayList<>();
    private List<SgRelOAEIdentificacionOAE> listRelOAEIdentificacionOAE;
    private Set<SgIdentificacionOAE> listIdentificacionOAE;

    public EvaluacionOrganismoAdministracionEscolarBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscarIdentificacionOAE();
            if (orgId != null && orgId > 0) {
                this.actualizar(restClient.obtenerPorId(orgId));
                soloLectura = editable != null ? !editable : soloLectura;
                estadoLectura();
            } else {
                this.agregar();
            }
            validarAcceso();
            prepararIdentificacionOAE();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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

    public void actualizarPersona(SgPersonaOrganismoAdministracion var) {
        try {
            JSFUtils.limpiarMensajesError();
            personaEnEdicion = personaOrganismoRestClient.obtenerPorId(var.getPoaPk());

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
            if (!EnumEstadoOrganismoAdministrativo.ENVIADO.equals(entidadEnEdicion.getOaeEstado())) {
                soloLectura = Boolean.TRUE;
            }
        }
    }

    public void cargarCombos() {
        try {
            FiltroCargoOAE fc = new FiltroCargoOAE();
            fc.setHabilitado(Boolean.TRUE);

            List<SgCargoOAE> listaCargo = catalogosRestClient.buscarCargoOAE(fc);
            comboCargoOAE = new SofisComboG(listaCargo, "coaNombre");
            comboCargoOAE.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            acciones = new SelectItem[]{
                new SelectItem(1, "Aprobar"),
                new SelectItem(2, "Ampliar datos"),
                new SelectItem(3, "Rechazar")
            };

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
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

    private void limpiarCombos() {
        comboCargoOAE.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgOrganismoAdministracionEscolar();
    }

    public void cargarItemsEvaluar() {
        try {
            if (entidadEnEdicion != null && entidadEnEdicion.getOaeSede() != null && entidadEnEdicion.getOaeSede().getSedTipo() != null) {
                SgCentroEducativo centro = (SgCentroEducativo) entidadEnEdicion.getOaeSede();
                SgTipoOrganismoAdministrativo tipo = catalogosRestClient.obtenerPorIdTipoOrganismo(centro.getCedTipoOrganismoAdministrativo().getToaPk());
                if (tipo != null) {
                    tipo.getToaItems().forEach(x -> {
                        itemsEvaluar.add(x);
                    });
                }
                if (!itemsEvaluar.isEmpty()) {
                    itemsEvaluar.sort(Comparator.comparing(SgItemEvaluarOrganismo::getIeoOrden));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(EvaluacionOrganismoAdministracionEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizar(SgOrganismoAdministracionEscolar var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgOrganismoAdministracionEscolar) SerializationUtils.clone(var);
        sedeSeleccionada = entidadEnEdicion.getOaeSede();
        entidadEnEdicion.setItemsEvaluarOAE(var.getItemsEvaluarOAE());
        cargarItemsEvaluar();
        buscarPersona();
        buscarPersonaI();
        itemsSeleccionados = new ArrayList<>();
        entidadEnEdicion.getItemsEvaluarOAE().forEach(i -> {
            itemsSeleccionados.add(i.getOaiItemFk());
        });
    }

    public void guardar() {
        try {
            if (entidadEnEdicion.getOaePk() == null) {
                entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.ELABORACION);
            }
            entidadEnEdicion.setOaeSede(sedeSeleccionada);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getOaePk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");

            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void nuevaEvaluacion() {
        resultado = null;
        //Aprobacion
        entidadEnEdicion.setOaeNombre(null);
        entidadEnEdicion.setOaeMiembrosVigentes(Boolean.FALSE);
        entidadEnEdicion.setOaeNumeroAcuerdo(null);
        entidadEnEdicion.setOaeNumeroResolucion(null);
        entidadEnEdicion.setOaeFechaLegalizacion(null);
        entidadEnEdicion.setOaeFechaResolucion(null);
        itemsSeleccionados = new ArrayList<>();
        //Ampliar datos
        entidadEnEdicion.setOaeAmpliarDatos(null);
        //Rechazar
        entidadEnEdicion.setOaeMotivoRechazo(null);
    }

    public void evaluar() {
        if (resultado == null) {
            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_ACCION_VACIO), "");
        } else {
            switch (resultado) {
                case 1:
                    //Se pasan a null los datos que no tienen q ver con aprobar
                    entidadEnEdicion.setOaeAmpliarDatos(null);
                    entidadEnEdicion.setOaeMotivoRechazo(null);
                    aceptar();
                    break;
                case 2:
                    //Se pasan a null los datos que no tienen que vero con pendiente aprobacion
                    entidadEnEdicion.setOaeNombre(null);
                    entidadEnEdicion.setOaeMiembrosVigentes(Boolean.FALSE);
                    entidadEnEdicion.setOaeNumeroAcuerdo(null);
                    entidadEnEdicion.setOaeNumeroResolucion(null);
                    entidadEnEdicion.setOaeFechaLegalizacion(null);
                    entidadEnEdicion.setOaeFechaResolucion(null);
                    itemsSeleccionados = new ArrayList<>();
                    entidadEnEdicion.setOaeMotivoRechazo(null);
                    ampliarDatos();
                    break;
                case 3:
                    //Se pasa a null lo que no tiene que ver con rechazo
                    entidadEnEdicion.setOaeNombre(null);
                    entidadEnEdicion.setOaeMiembrosVigentes(Boolean.FALSE);
                    entidadEnEdicion.setOaeNumeroAcuerdo(null);
                    entidadEnEdicion.setOaeNumeroResolucion(null);
                    entidadEnEdicion.setOaeFechaLegalizacion(null);
                    entidadEnEdicion.setOaeFechaResolucion(null);
                    itemsSeleccionados = new ArrayList<>();
                    entidadEnEdicion.setOaeAmpliarDatos(null);
                    rechazar();
                    break;
            }
        }
    }

    public void aceptar() {
        try {
            //calcular fecha de vencimiento

            //TODO: pasar al backend
            entidadEnEdicion.setOaeFechaAcuerdo(entidadEnEdicion.getOaeFechaLegalizacion());
            if (entidadEnEdicion.getOaeEsMiembroRenovado() == null || BooleanUtils.isNotTrue(entidadEnEdicion.getOaeEsMiembroRenovado())) {

                if (entidadEnEdicion.getOaeSede() instanceof SgCentroEducativo) {
                    SgCentroEducativo centro = (SgCentroEducativo) entidadEnEdicion.getOaeSede();

                    entidadEnEdicion.setOaeTipoOrganismoAdministrativo(centro.getCedTipoOrganismoAdministrativo());

                    Integer plazo = centro.getCedTipoOrganismoAdministrativo().getToaPlazo();
                    if (plazo != null && entidadEnEdicion.getOaeFechaAcuerdo() != null) {
                        LocalDate fechaVencimiento = entidadEnEdicion.getOaeFechaAcuerdo().plusYears(Long.valueOf(plazo.toString()));
                        entidadEnEdicion.setOaeFechaVencimiento(fechaVencimiento);
                    }

                }
                if (!itemsSeleccionados.isEmpty()) {
                    List<SgItemsEvaluarOAE> items = new ArrayList<>();
                    for (SgItemEvaluarOrganismo i : itemsSeleccionados) {
                        SgItemsEvaluarOAE item = new SgItemsEvaluarOAE();
                        item.setOaiItemFk(i);
                        item.setOaiOrganismoFk(entidadEnEdicion);
                        items.add(item);
                    }
                    entidadEnEdicion.setItemsEvaluarOAE(items);
                }

            }
            if (entidadEnEdicion.getOaeEsMiembroRenovado() != null && BooleanUtils.isTrue(entidadEnEdicion.getOaeEsMiembroRenovado())) {
                if (entidadEnEdicion.getOaeSede() instanceof SgCentroEducativo) {
                    SgCentroEducativo centro = (SgCentroEducativo) entidadEnEdicion.getOaeSede();

                    Integer plazo = centro.getCedTipoOrganismoAdministrativo().getToaPlazo();
                    if (plazo != null && entidadEnEdicion.getOaeFechaResolucion() != null) {
                        LocalDate fechaVencimiento = entidadEnEdicion.getOaeFechaResolucion().plusYears(Long.valueOf(plazo.toString()));
                        entidadEnEdicion.setOaeFechaVencimiento(fechaVencimiento);
                    }

                }
            }
            if (listRelOAEIdentificacionOAE != null && !listRelOAEIdentificacionOAE.isEmpty()) {
                entidadEnEdicion.setOaeIdentificaciones(listRelOAEIdentificacionOAE);
            }
            entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.APROBADO);

            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            soloLectura = Boolean.TRUE;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.CAMBIO_ESTADO_APROBADO), "");
            PrimeFaces.current().executeScript("PF('confirmDialogEnviar').hide()");
        } catch (BusinessException be) {
            entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.ENVIADO);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.ENVIADO);
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void ampliarDatos() {
        try {
            entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.AMPLIAR_DATOS);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            soloLectura = Boolean.TRUE;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.CAMBIO_ESTADO_AMPLIAR_DATOS), "");
            PrimeFaces.current().executeScript("PF('confirmDialogEnviar').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void rechazar() {
        try {
            entidadEnEdicion.setOaeEstado(EnumEstadoOrganismoAdministrativo.RECHAZADO);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            soloLectura = Boolean.TRUE;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.CAMBIO_ESTADO_RECHAZADO), "");
            PrimeFaces.current().executeScript("PF('confirmDialogEnviar').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarPersona() {
        try {
            if (entidadEnEdicion.getOaePk() != null) {
                filtroPersona = new FiltroPersonaOrganismoAdministrativo();
                filtroPersona.setIncluirCampos(new String[]{
                    "poaPersona.perDui",
                    "poaDui",
                    "poaSexo",
                    "poaPersona.perNit",
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
                filtroPersona.setPoaHabilitado(Boolean.TRUE);
                filtroPersona.setPoaOrganismoAdministrativoEscolar(entidadEnEdicion.getOaePk());
                listPersonasHabilitadas = personaOrganismoRestClient.buscar(filtroPersona);

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarPersonaI() {
        try {
            
            if (entidadEnEdicion.getOaePk() != null) {
                
                filtroPersona = new FiltroPersonaOrganismoAdministrativo();
                filtroPersona.setIncluirCampos(new String[]{
                    "poaPersona.perDui",
                    "miembroReemplazo",
                    "poaDui",
                    "poaSexo",
                    "poaPersona.perNit",
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
                filtroPersona.setPoaHabilitado(Boolean.FALSE);
                filtroPersona.setPoaOrganismoAdministrativoEscolar(entidadEnEdicion.getOaePk());
                listPersonasNoHabilitadas = personaOrganismoRestClient.buscar(filtroPersona);

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String obtenerMiembroReemplazo(SgPersonaOrganismoAdministracion miembro) {
        try {
           
            if (miembro != null && miembro.getMiembroReemplazo() != null) {
                for (SgPersonaOrganismoAdministracion per : listPersonasHabilitadas) {
                    if (per.getPoaPk().equals(miembro.getMiembroReemplazo())) {
                        if (per.getPoaPrerregistro()) {
                            return per.getPoaPersona().getPerDuiNombreCompleto();
                        } else {
                            return per.getPoaDui() != null ? per.getPoaDui() + " - " + per.getPoaNombre() : per.getPoaNombre();
                        }
                    }
                }
                for (SgPersonaOrganismoAdministracion per : listPersonasNoHabilitadas) {
                    if (per.getPoaPk().equals(miembro.getMiembroReemplazo())) {
                        if (per.getPoaPrerregistro()) {
                            return per.getPoaPersona().getPerDuiNombreCompleto();
                        } else {
                            return per.getPoaDui() != null ? per.getPoaDui() + " - " + per.getPoaNombre() : per.getPoaNombre();
                        }
                    }
                }
            }
   
        } catch (Exception ex) {
            Logger.getLogger(EvaluacionOrganismoAdministracionEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
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

    public FiltroPersonaOrganismoAdministrativo getFiltroPersona() {
        return filtroPersona;
    }

    public void setFiltroPersona(FiltroPersonaOrganismoAdministrativo filtroPersona) {
        this.filtroPersona = filtroPersona;
    }

    public SelectItem[] getAcciones() {
        return acciones;
    }

    public void setAcciones(SelectItem[] acciones) {
        this.acciones = acciones;
    }

    public Integer getResultado() {
        return resultado;
    }

    public void setResultado(Integer resultado) {
        this.resultado = resultado;
    }

    public List<SgPersonaOrganismoAdministracion> getListPersonasHabilitadas() {
        return listPersonasHabilitadas;
    }

    public void setListPersonasHabilitadas(List<SgPersonaOrganismoAdministracion> listPersonasHabilitadas) {
        this.listPersonasHabilitadas = listPersonasHabilitadas;
    }

    public List<SgPersonaOrganismoAdministracion> getListPersonasNoHabilitadas() {
        return listPersonasNoHabilitadas;
    }

    public void setListPersonasNoHabilitadas(List<SgPersonaOrganismoAdministracion> listPersonasNoHabilitadas) {
        this.listPersonasNoHabilitadas = listPersonasNoHabilitadas;
    }

    public Long getTotalResultadosI() {
        return totalResultadosI;
    }

    public void setTotalResultadosI(Long totalResultadosI) {
        this.totalResultadosI = totalResultadosI;
    }

    public Integer getPaginadoI() {
        return paginadoI;
    }

    public void setPaginadoI(Integer paginadoI) {
        this.paginadoI = paginadoI;
    }

    public SgPersonaOrganismoAdministracion getMiembroReemplazo() {
        return miembroReemplazo;
    }

    public void setMiembroReemplazo(SgPersonaOrganismoAdministracion miembroReemplazo) {
        this.miembroReemplazo = miembroReemplazo;
    }

    public List<SgItemEvaluarOrganismo> getItemsSeleccionados() {
        return itemsSeleccionados;
    }

    public void setItemsSeleccionados(List<SgItemEvaluarOrganismo> itemsSeleccionados) {
        this.itemsSeleccionados = itemsSeleccionados;
    }

    public List<SgItemEvaluarOrganismo> getItemsEvaluar() {
        return itemsEvaluar;
    }

    public void setItemsEvaluar(List<SgItemEvaluarOrganismo> itemsEvaluar) {
        this.itemsEvaluar = itemsEvaluar;
    }

    public List<SgRelOAEIdentificacionOAE> getListRelOAEIdentificacionOAE() {
        return listRelOAEIdentificacionOAE;
    }

    public void setListRelOAEIdentificacionOAE(List<SgRelOAEIdentificacionOAE> listRelOAEIdentificacionOAE) {
        this.listRelOAEIdentificacionOAE = listRelOAEIdentificacionOAE;
    }

}
