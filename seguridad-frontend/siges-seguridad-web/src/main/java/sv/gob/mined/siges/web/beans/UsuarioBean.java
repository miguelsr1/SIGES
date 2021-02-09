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
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgContexto;
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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRol;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSistemaIntegrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUsuarioRol;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroAsociacion;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroPersona;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyLucenePersonaDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyUsuarioDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsociacionRestClient;
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
public class UsuarioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UsuarioBean.class.getName());

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
    private ReglaContextoRestClient reglaContextoClient;
    
    @Inject
    private AsociacionRestClient asociacionClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SeleccionarSeccionBean seleccionarSeccionBean;

    @Inject
    @Param(name = "id")
    private Long usuId;

    @Inject
    @Param(name = "rev")
    private Long usuRev;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private SgUsuario entidadEnEdicion = new SgUsuario();
    private List<RevHistorico> historialUsuario = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyUsuarioDataModel usuarioLazyModel;
    private List<SgUsuarioRol> rolesUsuario = new ArrayList();
    private SgUsuarioRol rolUsuario = new SgUsuarioRol();
    private SofisComboG<SgRol> comboRol;
    private SofisComboG<EnumAmbito> comboAmbito;
    private Boolean soloLectura =Boolean.FALSE;
    private Map.Entry<Long, String> contextoSeleccionado;
    private EnumAmbito ambitoSeleccionado;
    private Boolean inputContexto = false;
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

    public UsuarioBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarAmbitos();
            cargarCombos();
            if (usuId != null && usuId > 0) {
                if (usuRev != null && usuRev > 0) {
                    //this.actualizar(restClient.obtenerEnRevision(estId, estRev));
                    //this.soloLectura = Boolean.TRUE;
                } else {
                    this.actualizar(restClient.obtenerPorId(usuId));
                    limpiarRolUsuario();
                    buscarRoles();
                    soloLectura = editable != null ? !editable : soloLectura;
                }
            } else {
                this.agregar();
            }
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

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarRolUsuario() {
        rolUsuario = new SgUsuarioRol();
        SgContexto sgContexto = new SgContexto();
        rolUsuario.setPurContexto(sgContexto);
        comboRol.setSelected(-1);
        comboAmbito.setSelected(-1);
        contextoSeleccionado = null;
        if (comboReglasContexto != null) {
            comboReglasContexto.setSelected(-1);
        }
    }

    public void buscarRoles() {
        try {           
            FiltroUsuarioRol fil = new FiltroUsuarioRol();
            fil.setUsuario(entidadEnEdicion.getUsuPk());
            rolesUsuario = restUsuarioRolClient.buscarFiltroUsuarioRol(fil);
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

            comboAmbito = new SofisComboG<>(ambitosPermitidos, "text");
            comboAmbito.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            FiltroCodiguera fcod = new FiltroCodiguera();
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

    public void seleccionarUnidadActivoFijo() {
        if (comboUnidadActivoFijo != null) {
            unidadSeleccionado = comboUnidadActivoFijo.getSelectedT();
        }
    }

    public List<Entry<Long, String>> completeContexto(String query) {
        try {

            HashMap<Long, String> contextoCache = new HashMap<Long, String>();

            switch (ambitoSeleccionado) {
                case MINED:
                    break;

                case DEPARTAMENTAL:
                    FiltroCodiguera fcod = new FiltroCodiguera();
                    fcod.setNombre(query);
                    fcod.setMaxResults(11L);
                    fcod.setOrderBy(new String[]{"depNombre"});
                    fcod.setAscending(new boolean[]{true});
                    List<SgDepartamento> resultadoDepto = restCatalogo.buscarDepartamento(fcod);
                    resultadoDepto.forEach((sgDepto) -> {
                        contextoCache.put(sgDepto.getDepPk(), sgDepto.getDepNombre());
                    });
                    break;

                case SEDE:
                    FiltroSedes fsed = new FiltroSedes();
                    fsed.setSedCodigoONombre(query);
                    fsed.setMaxResults(11L);
                    fsed.setOrderBy(new String[]{"sedNombre"});
                    fsed.setAscending(new boolean[]{true});
                    fsed.setIncluirCampos(new String[]{"sedPk", "sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
                    List<SgSede> resultadoSede = restSede.buscar(fsed);
                    resultadoSede.forEach((sgSede) -> {
                        contextoCache.put(sgSede.getSedPk(), sgSede.getSedCodigoNombre());
                    });
                    break;

                case SECCION:
                    FiltroSeccion fsec = new FiltroSeccion();
                    fsec.setSecNombre(query);
                    fsec.setMaxResults(11L);
                    fsec.setOrderBy(new String[]{"secNombre"});
                    fsec.setAscending(new boolean[]{true});
                    fsec.setIncluirCampos(new String[]{"secPk", "secNombre", "secJornadaLaboral.jlaNombre"});
                    List<SgSeccion> resultadoSeccion = restSeccion.buscar(fsec);
                    resultadoSeccion.forEach((sgSeccion) -> {
                        contextoCache.put(sgSeccion.getSecPk(), sgSeccion.getSecNombre());
                    });
                    break;

                case PERSONA:
                    FiltroPersona fper = new FiltroPersona();
                    fper.setPerNombreCompleto(query);
                    fper.setMaxResults(11L);
                    fper.setOrderBy(new String[]{"perNombreBusqueda"});
                    fper.setAscending(new boolean[]{true});
                    fper.setIncluirCampos(new String[]{"perPk", "perNombreBusqueda", "perPrimerNombre", "perSegundoNombre", "perPrimerApellido", "perSegundoApellido"});
                    List<SgPersona> resultadoPersona = restPersona.buscar(fper);
                    resultadoPersona.forEach((sgPersona) -> {
                        contextoCache.put(sgPersona.getPerPk(), sgPersona.getPerNombreCompleto());
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
                    FiltroSistemaIntegrado fsi = new FiltroSistemaIntegrado();
                    fsi.setCodigoNombre(query);
                    fsi.setMaxResults(11L);
                    fsi.setOrderBy(new String[]{"sinNombre"});
                    fsi.setAscending(new boolean[]{true});
                    fsi.setIncluirCampos(new String[]{"sinCodigo", "sinNombre", "sinVersion"});
                    List<SgSistemaIntegrado> resultadosi = restSistemaIntegrado.buscar(fsi);
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

    public String contextoTabla(SgContexto contexto) {
        try {
            String c = "";
            switch (contexto.getConAmbito()) {
                case MINED:
                case MODALIDADES_FLEXIBLES:
                    break;
                case DEPARTAMENTAL:
                    SgDepartamento departamento = restCatalogo.obtenerPorIdDepartamento(contexto.getContextoId());
                    if (departamento != null) {
                        c = departamento.getDepNombre();
                    }
                    break;

                case SEDE:
                    SgSede sede = restSede.obtenerPorIdLazy(contexto.getContextoId());
                    if (sede != null) {
                        c = sede.getSedCodigoNombre();
                    }
                    break;

                case SECCION:
                    FiltroSeccion fs = new FiltroSeccion();
                    fs.setSecPk(contexto.getContextoId());
                    fs.setIncluirCampos(new String[]{
                        "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                        "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                        "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                        "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                        "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                        "secServicioEducativo.sduOpcion.opcNombre",
                        "secServicioEducativo.sduProgramaEducativo.pedNombre",
                        "secServicioEducativo.sduGrado.graNombre",
                        "secServicioEducativo.sduSede.sedCodigo",
                        "secServicioEducativo.sduSede.sedNombre",
                        "secServicioEducativo.sduSede.sedTipo",
                        "secNombre",
                        "secCodigo",
                        "secJornadaLaboral.jlaNombre",
                        "secPk",
                        "secAnioLectivo.aleAnio",
                        "secVersion"});
                    List<SgSeccion> list = restSeccion.buscar(fs);
                    if (!list.isEmpty()) {
                        SgSeccion seccion = list.get(0);
                        if (seccion != null) {
                            c = seccion.getNombreSeccionCompleto();
                        }
                    }
                    break;

                case PERSONA:
                    SgPersona persona = restPersona.obtenerPorId(contexto.getContextoId());
                    if (persona != null) {
                        c = persona.getPerNombreCompleto() + " - " + persona.getPerIdentificacionesAsString();
                    }
                    break;

                case UNIDAD_ADMINISTRATIVA:
                    SgAfUnidadesAdministrativas admin = restCatalogo.obtenerPorIdUnidadesAdministrativas(contexto.getContextoId());
                    if (admin != null) {
                        c = admin.getCodigoNombre();
                    }
                    break;
                case SISTEMA_INTEGRADO:
                    SgSistemaIntegrado si = restSistemaIntegrado.obtenerPorId(contexto.getContextoId());
                    if (si != null) {
                        c = si.getSinCodigoNombre();
                    }
                    break;
                case IMPLEMENTADORA:
                    FiltroAsociacion filA = new FiltroAsociacion();
                    filA.setAsoPk(contexto.getContextoId());
                    filA.setIncluirCampos(new String[]{"asoCodigo", "asoNombre", "asoVersion"});
                    List<SgAsociacion> lista = asociacionClient.buscar(filA);
                    if (!lista.isEmpty()) {
                        SgAsociacion a = lista.get(0);
                        if (a != null) {
                            c = a.getAsoCodigo() + " - " + a.getAsoNombre();
                        }
                    }
                    break;
                case PARTICION_SEDE:
                    c = contexto.getConRegla().getRecNombre();
                    break;
                default:
                    c = "";
                    break;
            }

            return c;
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
            inputContexto = ambitoSeleccionado != null ? !(ambitoSeleccionado.equals(EnumAmbito.MINED) || ambitoSeleccionado.equals(EnumAmbito.MODALIDADES_FLEXIBLES) ||ambitoSeleccionado.equals(EnumAmbito.PARTICION_SEDE) || ambitoSeleccionado.equals(EnumAmbito.SECCION) || ambitoSeleccionado.equals(EnumAmbito.PERSONA)) || ambitoSeleccionado == null : false;
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

    public void seleccionarSeccion(SgSeccion var) {
        this.seccionSeleccionada = var;
    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public void limpiar() {
        limpiarCombos();
        limpiarRolUsuario();
    }

    public void limpiarPersona() {
        filtroPersona = new FiltroPersona();
        lazyDataModel = null;
        totalResultadosPersona = null;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgUsuario();
    }

    public void actualizar(SgUsuario var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgUsuario) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscarRoles();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void revocarCertificados() {
        try {
            restClient.revocarCertificado(entidadEnEdicion.getUsuCodigo());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    

    public void agregarRol() {
        JSFUtils.limpiarMensajesError();
        limpiarRolUsuario();
        verSeleccionarSeccion = Boolean.FALSE;
        inputContexto = Boolean.FALSE;
        verSeleccionarPersona = Boolean.FALSE;
        seccionSeleccionada = null;
        personaSeleccionada = null;
        this.seleccionarSeccionBean.setSedeSeleccionada(null);
        this.seleccionarSeccionBean.seleccionarSede();
        totalResultadosPersona = null;
        lazyDataModel = null;
        filtroPersona = new FiltroPersona();
    }

    public void guardarRol() {

        try {

            if (comboAmbito.getSelectedT() != null && comboRol.getSelectedT() != null) {
                rolUsuario.setPurRol(comboRol.getSelectedT());
                rolUsuario.getPurContexto().setConAmbito(comboAmbito.getSelectedT());
                switch (comboAmbito.getSelectedT()) {
                    case MINED:
                        rolUsuario.getPurContexto().setContextoId(0L);
                        break;
                    case MODALIDADES_FLEXIBLES:
                        rolUsuario.getPurContexto().setContextoId(0L);
                        break;
                    case DEPARTAMENTAL:
                    case SEDE:
                    case IMPLEMENTADORA:
                    case SISTEMA_INTEGRADO:
                    case UNIDAD_ADMINISTRATIVA:
                        if (contextoSeleccionado != null) {
                            rolUsuario.getPurContexto().setContextoId(contextoSeleccionado.getKey());
                        } else {
                            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP_ROL, Mensajes.obtenerMensaje(Mensajes.ERROR_CONTEXTO_VACIO), "");
                        }
                        break;
                    case SECCION:
                        if (seccionSeleccionada != null) {
                            rolUsuario.getPurContexto().setContextoId(seccionSeleccionada.getSecPk());
                        } else {
                            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP_ROL, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCION_VACIO), "");
                        }
                        break;
                    case PERSONA:
                        if (personaSeleccionada != null) {
                            rolUsuario.getPurContexto().setContextoId(personaSeleccionada.getPerPk());
                        } else {
                            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP_ROL, Mensajes.obtenerMensaje(Mensajes.ERROR_PERSONA_VACIO), "");
                        }
                        break;
                    case PARTICION_SEDE:
                        rolUsuario.getPurContexto().setConRegla(comboReglasContexto.getSelectedT());
                        break;
                }

                rolUsuario.setPurUsuario(entidadEnEdicion);
                restUsuarioRolClient.guardar(rolUsuario);

                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                PrimeFaces.current().executeScript("PF('formularioUsuarioRolDialog').hide()");
                buscarRoles();
                limpiarRolUsuario();
            } else {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_POPUP_ROL, Mensajes.obtenerMensaje(Mensajes.ERROR_ROL_AMBITO_VACIO), "");
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_ROL, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_ROL, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina() {
        if (entidadEnEdicion == null || entidadEnEdicion.getUsuPk() == null) {
            return Etiquetas.getValue("tituloNuevoUsuario");
        } else {
            return Etiquetas.getValue("tituloEditarUsuario");
        }
    }

    public void eliminarRol(SgUsuarioRol elem) {
        rolUsuario = elem;
    }

    public void eliminarRol() {
        try {
            restUsuarioRolClient.eliminar(rolUsuario.getPurPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscarRoles();
            limpiar();
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

    public String buscar() {
        try {
            filtroPersona.setIncluirCampos(new String[]{"perNie", "perPrimerNombre", "perSegundoNombre", "perPrimerApellido", "perSegundoApellido"});
            totalResultadosPersona = restPersona.buscarTotalLucene(filtroPersona);
            lazyDataModel = new LazyLucenePersonaDataModel(restPersona, filtroPersona, totalResultadosPersona, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgroles", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void historial(Long id) {
        try {
            historialUsuario = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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

    public List<SgUsuarioRol> getRolesUsuario() {
        return rolesUsuario;
    }

    public void setRolesUsuario(List<SgUsuarioRol> rolesUsuario) {
        this.rolesUsuario = rolesUsuario;
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

    public Long getUsuId() {
        return usuId;
    }

    public void setUsuId(Long usuId) {
        this.usuId = usuId;
    }

    public Long getUsuRev() {
        return usuRev;
    }

    public void setUsuRev(Long usuRev) {
        this.usuRev = usuRev;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
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

    public Entry<Long, String> getContextoSeleccionado() {
        return contextoSeleccionado;
    }

    public void setContextoSeleccionado(Entry<Long, String> contextoSeleccionado) {
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

}
