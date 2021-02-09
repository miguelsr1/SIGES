/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCategoriaOperacion;
import sv.gob.mined.siges.web.dto.SgContexto;
import sv.gob.mined.siges.web.dto.SgOperacion;
import sv.gob.mined.siges.web.dto.SgReglaContexto;
import sv.gob.mined.siges.web.dto.SgRol;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.dto.SgUsuarioRol;
import sv.gob.mined.siges.web.dto.activo_fijo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.activo_fijo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.catalogo.SgAsociacion;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.dto.centros_educativos.SgPersona;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSeccion;
import sv.gob.mined.siges.web.dto.centros_educativos.SgSede;
import sv.gob.mined.siges.web.dto.si.SgSistemaIntegrado;
import sv.gob.mined.siges.web.enumerados.centros_educativos.EnumAmbito;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOperacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRol;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaIntegrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuarioRol;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroAsociacion;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyLucenePersonaDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyUsuarioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsociacionRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaOperacionRestClient;
import sv.gob.mined.siges.web.restclient.OperacionRestClient;
import sv.gob.mined.siges.web.restclient.ReglaContextoRestClient;
import sv.gob.mined.siges.web.restclient.RolRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRolRestClient;
import sv.gob.mined.siges.web.restclient.centros_educativos.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.centros_educativos.PersonaRestClient;
import sv.gob.mined.siges.web.restclient.centros_educativos.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.centros_educativos.SedeRestClient;
import sv.gob.mined.siges.web.restclient.si.SistemaIntegradoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class UsuariosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UsuariosBean.class.getName());

    @Inject
    private UsuarioRestClient restClient;

    @Inject
    private RolRestClient restRolClient;

    @Inject
    private UsuarioRolRestClient restUsuarioRolClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private SistemaIntegradoRestClient restSistemaIntegrado;

    @Inject
    private SeccionRestClient restSeccion;

    @Inject
    private PersonaRestClient restPersona;

    @Inject
    private CategoriaOperacionRestClient restCategoriaOperacion;

    @Inject
    private OperacionRestClient restOperacion;

    @Inject
    private ReglaContextoRestClient reglaContextoClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SeleccionarSeccionBean seleccionarSeccionBean;

    @Inject
    private AsociacionRestClient asociacionClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroUsuario filtroUsuario = new FiltroUsuario();
    private SgUsuario entidadEnEdicion = new SgUsuario();
    private List<RevHistorico> historialUsuario = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyUsuarioDataModel usuarioLazyModel;
    private SgUsuarioRol rolUsuario = new SgUsuarioRol();
    private SofisComboG<SgRol> comboRol = new SofisComboG<>();
    private SofisComboG<EnumAmbito> comboAmbito = new SofisComboG<>();
    private List<SgUsuario> usuariosSeleccionados = new ArrayList();
    private Map.Entry<Long, String> contextoSeleccionado;
    private EnumAmbito ambitoSeleccionado;
    private Boolean inputContexto = false;
    private SofisComboG<SgCategoriaOperacion> comboCategoriaOperacion = new SofisComboG<>();
    private SofisComboG<SgRol> comboRolBuscar = new SofisComboG<>();
    private SgOperacion operacionSeleccionado;
    private List<EnumAmbito> ambitosPermitidos = new ArrayList();
    private SgSeccion seccionSeleccionada;
    private Boolean verSeleccionarSeccion = Boolean.FALSE;
    private Boolean verSeleccionarPersona = Boolean.FALSE;
    private Boolean verUnidadActivoFijo = Boolean.FALSE;
    private Boolean verRegla = Boolean.FALSE;
    private FiltroPersona filtroPersona = new FiltroPersona();
    private LazyLucenePersonaDataModel lazyDataModel;
    private Long totalResultadosPersona;
    private SgPersona personaSeleccionada = new SgPersona();
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadActivoFijo;
    private SgAfUnidadesActivoFijo unidadSeleccionado = new SgAfUnidadesActivoFijo();
    private SofisComboG<SgReglaContexto> comboReglasContexto;

    private SofisComboG<EnumAmbito> comboAmbitoBusqueda;
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadActivoFijoBusqueda;
    private Map.Entry<Long, String> contextoBusquedaSeleccionado;

    public UsuariosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarAmbitos();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarAmbitos() {
        try {
            FiltroUsuarioRol fur = new FiltroUsuarioRol();
            fur.setIncluirCampos(new String[]{"purPk", "purContexto", "purVersion"});
            fur.setUsuario(sessionBean.getEntidadUsuario().getUsuPk());
            fur.setOperacionCodigo(ConstantesOperaciones.CREAR_USUARIO);
            List<SgUsuarioRol> listusuRol = restUsuarioRolClient.buscarFiltroUsuarioRol(fur);
            List<EnumAmbito> ambitosUsuario = new ArrayList();
            for (SgUsuarioRol usuRol : listusuRol) {
                if (!ambitosUsuario.contains(usuRol.getPurContexto().getConAmbito())) {
                    ambitosUsuario.add(usuRol.getPurContexto().getConAmbito());
                }

            }
            ambitosPermitidos = new ArrayList(Arrays.asList(EnumAmbito.values()));
            if (!ambitosUsuario.contains(EnumAmbito.MINED)) {
                ambitosPermitidos.remove(EnumAmbito.MINED);
                if (!ambitosUsuario.contains(EnumAmbito.DEPARTAMENTAL)) {
                    ambitosPermitidos.remove(EnumAmbito.DEPARTAMENTAL);
                }
                if (!ambitosUsuario.contains(EnumAmbito.SISTEMA_INTEGRADO)) {
                    ambitosPermitidos.remove(EnumAmbito.SISTEMA_INTEGRADO);
                }
                if (!ambitosUsuario.contains(EnumAmbito.UNIDAD_ADMINISTRATIVA)) {
                    ambitosPermitidos.remove(EnumAmbito.UNIDAD_ADMINISTRATIVA);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscar() {
        try {

            EnumAmbito ambitoBusqueda = comboAmbitoBusqueda.getSelectedT();
            filtroUsuario.setAmbito(ambitoBusqueda);
            if (ambitoBusqueda != null) {
                filtroUsuario.setContexto(this.contextoBusquedaSeleccionado != null ? this.contextoBusquedaSeleccionado.getKey() : null);
            }
            filtroUsuario.setCategoria(comboCategoriaOperacion.getSelectedT() != null ? comboCategoriaOperacion.getSelectedT().getCopPk() : null);
            filtroUsuario.setOperacion(operacionSeleccionado != null ? operacionSeleccionado.getOpePk() : null);
            filtroUsuario.setRol(comboRolBuscar.getSelectedT() != null ? comboRolBuscar.getSelectedT().getRolPk() : null);

            totalResultados = restClient.buscarTotal(filtroUsuario);
            usuarioLazyModel = new LazyUsuarioDataModel(restClient, filtroUsuario, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }


    public void cargarCombos() {
        try {
            FiltroRol filtroRol = new FiltroRol();
            filtroRol.setHabilitado(Boolean.TRUE);
            if (!BooleanUtils.isTrue(sessionBean.getEntidadUsuario().getUsuSuperUsuario())) {
                filtroRol.setDelegable(Boolean.TRUE);
            }
            filtroRol.setAscending(new boolean[]{true});
            filtroRol.setOrderBy(new String[]{"rolNombre"});
            List<SgRol> listaRoles = restRolClient.buscar(filtroRol);
            comboRol = new SofisComboG<>(listaRoles, "rolNombre");
            comboRol.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboRolBuscar = new SofisComboG<>(listaRoles, "rolNombre");
            comboRolBuscar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fcod = new FiltroCodiguera();
            fcod.setHabilitado(Boolean.TRUE);
            fcod.setAscending(new boolean[]{true});
            fcod.setOrderBy(new String[]{"copNombre"});
            List<SgCategoriaOperacion> listaCategoriaOperaciones = restCategoriaOperacion.buscar(fcod);
            comboCategoriaOperacion = new SofisComboG<>(listaCategoriaOperaciones, "copNombre");
            comboCategoriaOperacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAmbito = new SofisComboG<>(ambitosPermitidos, "text");
            comboAmbito.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumAmbito> ambitosBusqueda = new ArrayList<>();
            ambitosBusqueda.add(EnumAmbito.MINED);
            ambitosBusqueda.add(EnumAmbito.DEPARTAMENTAL);
            ambitosBusqueda.add(EnumAmbito.UNIDAD_ADMINISTRATIVA);
            ambitosBusqueda.add(EnumAmbito.SISTEMA_INTEGRADO);
            ambitosBusqueda.add(EnumAmbito.SEDE);
            ambitosBusqueda.add(EnumAmbito.IMPLEMENTADORA);
            comboAmbitoBusqueda = new SofisComboG<>(ambitosBusqueda, "text");
            comboAmbitoBusqueda.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            fcod = new FiltroCodiguera();
            fcod.setHabilitado(true);
            fcod.setOrderBy(new String[]{"uafNombre"});
            fcod.setAscending(new boolean[]{true});
            fcod.setIncluirCampos(new String[]{"uafCodigo", "uafNombre", "uafVersion"});
            List<SgAfUnidadesActivoFijo> lUnidades = restCatalogo.buscarUnidadesActivoFijo(fcod);
            comboUnidadActivoFijo = new SofisComboG<>(lUnidades, "uafNombre");
            comboUnidadActivoFijo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarAmbitoBusqueda() {
        try {
            EnumAmbito ambito = comboAmbitoBusqueda.getSelectedT();
            contextoBusquedaSeleccionado = null;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
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

    public List<SgSistemaIntegrado> completeSistema(String query) {
        try {
            FiltroSistemaIntegrado fsi = new FiltroSistemaIntegrado();
            fsi.setCodigoNombre(query);
            fsi.setMaxResults(11L);
            fsi.setOrderBy(new String[]{"sinNombre"});
            fsi.setAscending(new boolean[]{true});
            fsi.setIncluirCampos(new String[]{"sinCodigo", "sinNombre", "sinVersion"});
            return restSistemaIntegrado.buscar(fsi);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public void limpiar() {
        filtroUsuario = new FiltroUsuario();
        comboRolBuscar.setSelected(-1);
        comboCategoriaOperacion.setSelected(-1);
        operacionSeleccionado = null;
        totalResultados = null;
        usuarioLazyModel = null;
        contextoBusquedaSeleccionado = null;
        if (comboAmbitoBusqueda != null) {
            comboAmbitoBusqueda.setSelected(-1);
        }
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgUsuario();
    }

    public String actualizar(SgUsuario var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgUsuario) SerializationUtils.clone(var);
        return "IR_A_usuario";
    }

    public void roles(SgUsuario var) {
        limpiarCombos();
        entidadEnEdicion = (SgUsuario) SerializationUtils.clone(var);
        rolUsuario = new SgUsuarioRol();
        SgContexto sgContexto = new SgContexto();
        sgContexto.setContextoId(new Long(0));
        rolUsuario.setPurContexto(sgContexto);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardarSeleccion() {
        try {
            if (comboRol.getSelectedT() != null && ambitoSeleccionado != null) {
                if (usuariosSeleccionados.size() > 0) {

                    SgContexto con;
                    FiltroUsuarioRol filtroUR = new FiltroUsuarioRol();
                    filtroUR.setRol(comboRol.getSelectedT().getRolPk());
                    filtroUR.setAmbito(ambitoSeleccionado);
                    SgReglaContexto regla = null;

                    switch (ambitoSeleccionado) {
                        case MINED:
                            filtroUR.setContexto(0L);
                            break;
                        case MODALIDADES_FLEXIBLES:
                            filtroUR.setContexto(0L);
                            break;
                        case DEPARTAMENTAL:
                        case SEDE:
                        case IMPLEMENTADORA:
                        case SISTEMA_INTEGRADO:
                        case UNIDAD_ADMINISTRATIVA:
                            if (contextoSeleccionado != null) {
                                filtroUR.setContexto(contextoSeleccionado.getKey());
                            } else {
                                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_CONTEXTO_VACIO), "");
                                return;
                            }
                            break;
                        case SECCION:
                            if (seccionSeleccionada != null) {
                                filtroUR.setContexto(seccionSeleccionada.getSecPk());
                            } else {
                                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCION_VACIO), "");
                                return;
                            }
                            break;
                        case PERSONA:
                            if (personaSeleccionada != null) {
                                filtroUR.setContexto(personaSeleccionada.getPerPk());
                            } else {
                                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONA_VACIO), "");
                                return;
                            }
                            break;
                        case PARTICION_SEDE:
                            regla = comboReglasContexto.getSelectedT();
                            break;
                    }

                    for (SgUsuario user : usuariosSeleccionados) {

                        //Validar si este rol ya se encuentra asignada
                        filtroUR.setUsuario(user.getUsuPk());
                        if (restUsuarioRolClient.buscarTotal(filtroUR) == 0) {
                            con = new SgContexto();
                            con.setConAmbito(ambitoSeleccionado);
                            con.setContextoId(filtroUR.getContexto());
                            con.setConRegla(regla);

                            rolUsuario = new SgUsuarioRol();
                            rolUsuario.setPurUsuario(user);
                            rolUsuario.setPurRol(comboRol.getSelectedT());
                            rolUsuario.setPurContexto(con);
                            restUsuarioRolClient.guardar(rolUsuario);
                        }
                    }

                    usuariosSeleccionados = new ArrayList();
                    comboRol.setSelected(-1);
                    comboAmbito.setSelected(-1);
                    contextoSeleccionado = null;
                    if (comboReglasContexto != null) {
                        comboReglasContexto.setSelected(-1);
                    }
                    this.seleccionarSeccionBean.setSedeSeleccionada(null);
                    this.seleccionarSeccionBean.seleccionarSede();
                    verSeleccionarSeccion = Boolean.FALSE;
                    inputContexto = Boolean.FALSE;
                    verSeleccionarPersona = Boolean.FALSE;
                    lazyDataModel = null;
                    filtroPersona = new FiltroPersona();
                    totalResultadosPersona = null;

                    buscar();

                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

                } else {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_USUARIOS_SELECCIONADOS_VACIO), "");
                }
            } else {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_ROL_AMBITO_VACIO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar(SgUsuario elem) {
        entidadEnEdicion = elem;
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getUsuPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            limpiar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean esSuperUsuario() {
        return BooleanUtils.isNotTrue(sessionBean.getEntidadUsuario().getUsuSuperUsuario());

    }

    public void guardarRol() {

        try {

            rolUsuario.setPurRol(comboRol.getSelectedT());
            rolUsuario.getPurContexto().setConAmbito(comboAmbito.getSelectedT());
            restUsuarioRolClient.guardar(rolUsuario);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            rolUsuario = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialUsuario = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarAPentaho(SgUsuario usu) {
        entidadEnEdicion = usu;
    }

    public void enviarAPentaho() {
        try {
            restClient.enviarAPentaho(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ENVIADO_CORRECTO), "");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarDePentaho() {
        try {
            restClient.eliminarDePentaho(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ENVIADO_CORRECTO), "");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarUnidadActivoFijo() {
        if (comboUnidadActivoFijo != null) {
            unidadSeleccionado = comboUnidadActivoFijo.getSelectedT();
        }
    }

    public List<Map.Entry<Long, String>> completeContexto(String query) {
        try {

            HashMap<Long, String> contextoCache = new HashMap<Long, String>();

            switch (ambitoSeleccionado) {
                case MINED: 
                case MODALIDADES_FLEXIBLES:
                    break;
                case DEPARTAMENTAL:
                    FiltroCodiguera fcod = new FiltroCodiguera();
                    fcod.setNombre(query);
                    fcod.setMaxResults(11L);
                    fcod.setIncluirCampos(new String[]{"depNombre", "depVersion"});
                    fcod.setOrderBy(new String[]{"depNombre"});
                    fcod.setAscending(new boolean[]{true});
                    fcod.setHabilitado(Boolean.TRUE);
                    List<SgDepartamento> resultadoDepto = restCatalogo.buscarDepartamento(fcod);
                    resultadoDepto.forEach((sgDepto) -> {
                        contextoCache.put(sgDepto.getDepPk(), sgDepto.getDepNombre());
                    });
                    break;
                case SEDE:
                    List<SgSede> resultadoSede = this.completeSede(query);
                    resultadoSede.forEach((sgSede) -> {
                        contextoCache.put(sgSede.getSedPk(), sgSede.getSedCodigoNombre());
                    });
                    break;
                case UNIDAD_ADMINISTRATIVA:
                    FiltroUnidadesAdministrativas funidades = new FiltroUnidadesAdministrativas();
                    funidades.setCodigoNombre(query);
                    funidades.setUnidadActivoFijoId(unidadSeleccionado != null ? unidadSeleccionado.getUafPk() : null);
                    funidades.setMaxResults(11L);
                    funidades.setOrderBy(new String[]{"uadNombreBusqueda"});
                    funidades.setAscending(new boolean[]{true});
                    funidades.setIncluirCampos(new String[]{"uadPk", "uadCodigo", "uadNombre", "uadVersion"});
                    List<SgAfUnidadesAdministrativas> resultadoUnidadesAdm = restCatalogo.buscarUnidadesAdministrativos(funidades);
                    resultadoUnidadesAdm.forEach((sgUnidadAdm) -> {
                        contextoCache.put(sgUnidadAdm.getUadPk(), sgUnidadAdm.getCodigoNombre());
                    });
                    break;
                case SISTEMA_INTEGRADO:
                    List<SgSistemaIntegrado> resultadosi = completeSistema(query);
                    resultadosi.forEach((si) -> {
                        contextoCache.put(si.getSinPk(), si.getSinCodigoNombre());
                    });
                    break;
                case IMPLEMENTADORA:
                    FiltroAsociacion fa = new FiltroAsociacion();
                    fa.setNombre(query);
                    fa.setMaxResults(11L);
                    fa.setOrderBy(new String[]{"asoNombre"});
                    fa.setAscending(new boolean[]{true});
                    fa.setIncluirCampos(new String[]{"asoCodigo", "asoNombre", "asoVersion"});
                    List<SgAsociacion> asoc = asociacionClient.buscar(fa);
                    asoc.forEach((as) -> {
                        contextoCache.put(as.getAsoPk(), as.getAsoNombre());
                    });
                    break;
                default:
                    contextoCache.put(null, "Seleccione un ámbito");
                    break;
            }

            return new ArrayList(contextoCache.entrySet());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<Map.Entry<Long, String>> completeContextoBusqueda(String query) {
        try {

            HashMap<Long, String> contextoCache = new HashMap<Long, String>();

            EnumAmbito ambitoBusquedaSeleccionado = this.comboAmbitoBusqueda.getSelectedT();
            switch (ambitoBusquedaSeleccionado) {
                case MINED:
                case MODALIDADES_FLEXIBLES:
                    break;
                case DEPARTAMENTAL:
                    FiltroCodiguera fcod = new FiltroCodiguera();
                    fcod.setNombre(query);
                    fcod.setMaxResults(11L);
                    fcod.setIncluirCampos(new String[]{"depNombre", "depVersion"});
                    fcod.setOrderBy(new String[]{"depNombre"});
                    fcod.setAscending(new boolean[]{true});
                    fcod.setHabilitado(Boolean.TRUE);
                    List<SgDepartamento> resultadoDepto = restCatalogo.buscarDepartamento(fcod);
                    resultadoDepto.forEach((sgDepto) -> {
                        contextoCache.put(sgDepto.getDepPk(), sgDepto.getDepNombre());
                    });
                    break;
                case SEDE:
                    List<SgSede> resultadoSede = this.completeSede(query);
                    resultadoSede.forEach((sgSede) -> {
                        contextoCache.put(sgSede.getSedPk(), sgSede.getSedCodigoNombre());
                    });
                    break;
                case UNIDAD_ADMINISTRATIVA:
                    FiltroUnidadesAdministrativas funidades = new FiltroUnidadesAdministrativas();
                    funidades.setCodigoNombre(query);
                    funidades.setMaxResults(11L);
                    funidades.setOrderBy(new String[]{"uadNombreBusqueda"});
                    funidades.setAscending(new boolean[]{true});
                    funidades.setIncluirCampos(new String[]{"uadPk", "uadCodigo", "uadNombre", "uadVersion"});
                    List<SgAfUnidadesAdministrativas> resultadoUnidadesAdm = restCatalogo.buscarUnidadesAdministrativos(funidades);
                    resultadoUnidadesAdm.forEach((sgUnidadAdm) -> {
                        contextoCache.put(sgUnidadAdm.getUadPk(), sgUnidadAdm.getCodigoNombre());
                    });
                    break;
                case SISTEMA_INTEGRADO:
                    List<SgSistemaIntegrado> resultadosi = completeSistema(query);
                    resultadosi.forEach((si) -> {
                        contextoCache.put(si.getSinPk(), si.getSinCodigoNombre());
                    });
                    break;
                case IMPLEMENTADORA:
                    FiltroAsociacion fa = new FiltroAsociacion();
                    fa.setNombre(query);
                    fa.setMaxResults(11L);
                    fa.setOrderBy(new String[]{"asoNombre"});
                    fa.setAscending(new boolean[]{true});
                    fa.setIncluirCampos(new String[]{"asoCodigo", "asoNombre", "asoVersion"});
                    List<SgAsociacion> asoc = asociacionClient.buscar(fa);
                    asoc.forEach((as) -> {
                        contextoCache.put(as.getAsoPk(), as.getAsoNombre());
                    });
                    break;

                default:
                    contextoCache.put(null, "Seleccione un ámbito");
                    break;
            }

            return new ArrayList(contextoCache.entrySet());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void seleccionarAmbito() {
        try {
            ambitoSeleccionado = comboAmbito.getSelectedT();
            contextoSeleccionado = null;
            filtroPersona = new FiltroPersona();
            lazyDataModel = null;
            personaSeleccionada = null;
            seccionSeleccionada = null;
            this.seleccionarSeccionBean.setSedeSeleccionada(null);
            this.seleccionarSeccionBean.seleccionarSede();
            inputContexto = ambitoSeleccionado != null ? !(ambitoSeleccionado.equals(EnumAmbito.MINED) || ambitoSeleccionado.equals(EnumAmbito.MODALIDADES_FLEXIBLES) || ambitoSeleccionado.equals(EnumAmbito.PARTICION_SEDE) || ambitoSeleccionado.equals(EnumAmbito.SECCION) || ambitoSeleccionado.equals(EnumAmbito.PERSONA)) || ambitoSeleccionado == null : false;
            verSeleccionarSeccion = ambitoSeleccionado != null ? ambitoSeleccionado.equals(EnumAmbito.SECCION) : false;
            verSeleccionarPersona = ambitoSeleccionado != null ? ambitoSeleccionado.equals(EnumAmbito.PERSONA) : false;
            verUnidadActivoFijo = ambitoSeleccionado != null ? ambitoSeleccionado.equals(EnumAmbito.UNIDAD_ADMINISTRATIVA) : false;
            verRegla = ambitoSeleccionado != null ? ambitoSeleccionado.equals(EnumAmbito.PARTICION_SEDE) : false;
            comboUnidadActivoFijo.setSelected(-1);

            if (EnumAmbito.PARTICION_SEDE.equals(ambitoSeleccionado) && comboReglasContexto == null) {
                FiltroCodiguera fcod = new FiltroCodiguera();
                fcod.setOrderBy(new String[]{"recNombre"});
                fcod.setAscending(new boolean[]{true});
                List<SgReglaContexto> reglas = reglaContextoClient.buscar(fcod);
                comboReglasContexto = new SofisComboG<>(reglas, "recNombre");
                comboReglasContexto.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void seleccionarCategoriaOperacion() {
        operacionSeleccionado = null;
    }

    public void seleccionarSeccion(SgSeccion var) {
        this.seccionSeleccionada = var;
    }

    public List<SgOperacion> completeOperacion(String query) {
        try {

            FiltroOperacion fop = new FiltroOperacion();
            fop.setNombre(query);
            fop.setHabilitado(Boolean.TRUE);
            fop.setCategoria(comboCategoriaOperacion.getSelectedT().getCopPk());
            fop.setOrderBy(new String[]{"opeNombre"});
            fop.setAscending(new boolean[]{true});
            return restOperacion.buscar(fop);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public Boolean ambitoEsMined() {
        Boolean respuesta = Boolean.FALSE;
        if (comboAmbito.getSelectedT() != null) {
            respuesta = comboAmbito.getSelectedT().equals(EnumAmbito.MINED);
        }
        return respuesta;
    }

    public String buscarPersona() {
        try {
            filtroPersona.setIncluirCampos(new String[]{"perNie", "perPrimerNombre", "perSegundoNombre", "perPrimerApellido", "perSegundoApellido"});
            totalResultadosPersona = restPersona.buscarTotalLucene(filtroPersona);
            lazyDataModel = new LazyLucenePersonaDataModel(restPersona, filtroPersona, totalResultadosPersona, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void limpiarPersona() {
        filtroPersona = new FiltroPersona();
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgUsuario getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgUsuario entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialUsuario() {
        return historialUsuario;
    }

    public void setHistorialUsuario(List<RevHistorico> historialUsuario) {
        this.historialUsuario = historialUsuario;
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

    public LazyUsuarioDataModel getUsuarioLazyModel() {
        return usuarioLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyUsuarioDataModel usuarioLazyModel) {
        this.usuarioLazyModel = usuarioLazyModel;
    }

    public UsuarioRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(UsuarioRestClient restClient) {
        this.restClient = restClient;
    }

    public SgUsuarioRol getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(SgUsuarioRol rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public SofisComboG<SgRol> getComboRol() {
        return comboRol;
    }

    public void setComboRol(SofisComboG<SgRol> comboRol) {
        this.comboRol = comboRol;
    }

    public SofisComboG<EnumAmbito> getComboAmbito() {
        return comboAmbito;
    }

    public void setComboAmbito(SofisComboG<EnumAmbito> comboAmbito) {
        this.comboAmbito = comboAmbito;
    }

    public RolRestClient getRestRolClient() {
        return restRolClient;
    }

    public void setRestRolClient(RolRestClient restRolClient) {
        this.restRolClient = restRolClient;
    }

    public UsuarioRolRestClient getRestUsuarioRolClient() {
        return restUsuarioRolClient;
    }

    public void setRestUsuarioRolClient(UsuarioRolRestClient restUsuarioRolClient) {
        this.restUsuarioRolClient = restUsuarioRolClient;
    }

    public List<SgUsuario> getUsuariosSeleccionados() {
        return usuariosSeleccionados;
    }

    public void setUsuariosSeleccionados(List<SgUsuario> usuariosSeleccionados) {
        this.usuariosSeleccionados = usuariosSeleccionados;
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

    public SeccionRestClient getRestSeccion() {
        return restSeccion;
    }

    public void setRestSeccion(SeccionRestClient restSeccion) {
        this.restSeccion = restSeccion;
    }

    public PersonaRestClient getRestPersona() {
        return restPersona;
    }

    public void setRestPersona(PersonaRestClient restPersona) {
        this.restPersona = restPersona;
    }

    public Map.Entry<Long, String> getContextoSeleccionado() {
        return contextoSeleccionado;
    }

    public void setContextoSeleccionado(Map.Entry<Long, String> contextoSeleccionado) {
        this.contextoSeleccionado = contextoSeleccionado;
    }

    public EnumAmbito getAmbitoSeleccionado() {
        return ambitoSeleccionado;
    }

    public void setAmbitoSeleccionado(EnumAmbito ambitoSeleccionado) {
        this.ambitoSeleccionado = ambitoSeleccionado;
    }

    public Boolean getInputContexto() {
        return inputContexto;
    }

    public void setInputContexto(Boolean inputContexto) {
        this.inputContexto = inputContexto;
    }

    public CategoriaOperacionRestClient getRestCategoriaOperacion() {
        return restCategoriaOperacion;
    }

    public void setRestCategoriaOperacion(CategoriaOperacionRestClient restCategoriaOperacion) {
        this.restCategoriaOperacion = restCategoriaOperacion;
    }

    public SofisComboG<SgCategoriaOperacion> getComboCategoriaOperacion() {
        return comboCategoriaOperacion;
    }

    public void setComboCategoriaOperacion(SofisComboG<SgCategoriaOperacion> comboCategoriaOperacion) {
        this.comboCategoriaOperacion = comboCategoriaOperacion;
    }

    public SofisComboG<SgRol> getComboRolBuscar() {
        return comboRolBuscar;
    }

    public void setComboRolBuscar(SofisComboG<SgRol> comboRolBuscar) {
        this.comboRolBuscar = comboRolBuscar;
    }

    public SgOperacion getOperacionSeleccionado() {
        return operacionSeleccionado;
    }

    public void setOperacionSeleccionado(SgOperacion operacionSeleccionado) {
        this.operacionSeleccionado = operacionSeleccionado;
    }

    public OperacionRestClient getRestOperacion() {
        return restOperacion;
    }

    public void setRestOperacion(OperacionRestClient restOperacion) {
        this.restOperacion = restOperacion;
    }

    public FiltroUsuario getFiltroUsuario() {
        return filtroUsuario;
    }

    public void setFiltroUsuario(FiltroUsuario filtroUsuario) {
        this.filtroUsuario = filtroUsuario;
    }

    public List<EnumAmbito> getAmbitosPermitidos() {
        return ambitosPermitidos;
    }

    public void setAmbitosPermitidos(List<EnumAmbito> ambitosPermitidos) {
        this.ambitosPermitidos = ambitosPermitidos;
    }

    public SgSeccion getSeccionSeleccionada() {
        return seccionSeleccionada;
    }

    public void setSeccionSeleccionada(SgSeccion seccionSeleccionada) {
        this.seccionSeleccionada = seccionSeleccionada;
    }

    public Boolean getVerSeleccionarSeccion() {
        return verSeleccionarSeccion;
    }

    public void setVerSeleccionarSeccion(Boolean verSeleccionarSeccion) {
        this.verSeleccionarSeccion = verSeleccionarSeccion;
    }

    public Boolean getVerSeleccionarPersona() {
        return verSeleccionarPersona;
    }

    public void setVerSeleccionarPersona(Boolean verSeleccionarPersona) {
        this.verSeleccionarPersona = verSeleccionarPersona;
    }

    public FiltroPersona getFiltroPersona() {
        return filtroPersona;
    }

    public void setFiltroPersona(FiltroPersona filtroPersona) {
        this.filtroPersona = filtroPersona;
    }

    public LazyLucenePersonaDataModel getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyLucenePersonaDataModel lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Long getTotalResultadosPersona() {
        return totalResultadosPersona;
    }

    public void setTotalResultadosPersona(Long totalResultadosPersona) {
        this.totalResultadosPersona = totalResultadosPersona;
    }

    public SgPersona getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(SgPersona personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }

    public Boolean getVerUnidadActivoFijo() {
        return verUnidadActivoFijo;
    }

    public void setVerUnidadActivoFijo(Boolean verUnidadActivoFijo) {
        this.verUnidadActivoFijo = verUnidadActivoFijo;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadActivoFijo() {
        return comboUnidadActivoFijo;
    }

    public void setComboUnidadActivoFijo(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadActivoFijo) {
        this.comboUnidadActivoFijo = comboUnidadActivoFijo;
    }

    public Boolean getVerRegla() {
        return verRegla;
    }

    public void setVerRegla(Boolean verRegla) {
        this.verRegla = verRegla;
    }

    public SofisComboG<SgReglaContexto> getComboReglasContexto() {
        return comboReglasContexto;
    }

    public void setComboReglasContexto(SofisComboG<SgReglaContexto> comboReglasContexto) {
        this.comboReglasContexto = comboReglasContexto;
    }

    public SofisComboG<EnumAmbito> getComboAmbitoBusqueda() {
        return comboAmbitoBusqueda;
    }

    public void setComboAmbitoBusqueda(SofisComboG<EnumAmbito> comboAmbitoBusqueda) {
        this.comboAmbitoBusqueda = comboAmbitoBusqueda;
    }

    public Map.Entry<Long, String> getContextoBusquedaSeleccionado() {
        return contextoBusquedaSeleccionado;
    }

    public void setContextoBusquedaSeleccionado(Map.Entry<Long, String> contextoBusquedaSeleccionado) {
        this.contextoBusquedaSeleccionado = contextoBusquedaSeleccionado;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadActivoFijoBusqueda() {
        return comboUnidadActivoFijoBusqueda;
    }

    public void setComboUnidadActivoFijoBusqueda(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadActivoFijoBusqueda) {
        this.comboUnidadActivoFijoBusqueda = comboUnidadActivoFijoBusqueda;
    }

}
