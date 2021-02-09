/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.web.dto.SgUsuario;
import sv.gob.mined.siges.web.restclient.SeguridadRestClient;
import sv.gob.mined.siges.web.restclient.UsuarioRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.OperationSecurity;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAlerta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AlertaRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());

    @Inject
    private SeguridadRestClient seguridadClient;

    @Inject
    private UsuarioRestClient usuarioClient;

    @Inject
    private CatalogosRestClient catalogoClient;

    @Inject
    private SedeRestClient sedesClient;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private AlertaRestClient alertasClient;

    private String ambiente;
    private String piePagina = "";
    private String timeZone = "GMT-6";
    private Locale locale;
    private Locale localeNumber;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private Integer userTokenExpirationTimeMinutes = 20;
    private String userIp;
    private Principal user;
    private SgUsuario entidadUsuario;

    private Set<String> operaciones;
    private Set<String> operacionesBackend;
    private HashMap<String, List<OperationSecurity>> operacionesSeguridad = new HashMap<>();
    private HashMap<String, EnumAmbito> operacionesMayorAmbito = new HashMap<>();
    private HashMap<Long, SgDepartamento> departamentos = new HashMap<>();
    private HashMap<Long, SgSede> sedes = new HashMap<>();
    private HashMap<Long, SgSeccion> secciones = new HashMap<>();
    private SgSede sedeDefecto;
    private Boolean seCargoSedeDefecto = Boolean.FALSE;

    private Long cantAlertasTempRiesgoMuyAlto = 0L;
    private Long cantAlertasTempRiesgoAlto = 0L;
    private Long cantAlertasTempRiesgoMedio = 0L;

    private HashMap<EnumAmbito, HashMap<Object, List<OperationSecurity>>> ambitoContextoOps = new HashMap<>();
    private List<EnumAmbito> ambitosSeleccionablesBusqueda;

    public SessionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            locale = new Locale("es");
            localeNumber = new Locale("es_SV");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void cargarSedePorDefecto() {
        try {
            List<SgSede> sedes = buscarSedes(null);
            if (sedes != null && sedes.size() == 1) {
                sedeDefecto = sedes.get(0);
                this.sedes.put(sedeDefecto.getSedPk(), sedeDefecto);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> buscarSedes(Long sedPk) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedPk(sedPk);
            fil.setMaxResults(2L);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo",
                "sedNombre",
                "sedTipo",
                "sedVersion",
                "sedTipoCalendario.tcePk",
                "sedTipoCalendario.tceVersion",
                "sedDireccion.dirDepartamento.depPk",
                "sedDireccion.dirDepartamento.depNombre",
                "sedDireccion.dirDepartamento.depVersion",
                "sedSedeAdscritaDe.sedPk",
                "sedSedeAdscritaDe.sedTipo"});
            fil.setIncluirAdscritas(Boolean.TRUE);
            return sedesClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgSeccion> buscarSecciones(Long secPk) {
        try {
            FiltroSeccion fil = new FiltroSeccion();
            fil.setSecPk(secPk);
            fil.setMaxResults(2L);
            fil.setIncluirCampos(new String[]{"secCodigo",
                "secNombre",
                "secVersion",
                "secServicioEducativo.sduSede.sedPk",
                "secServicioEducativo.sduSede.sedTipo",
                "secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk"
            });
            return seccionClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    @Produces
    @Named("userToken")
    public String getUserToken() {
        try {
            if (userTokenGeneratedDate == null || !userTokenGeneratedDate.plusMinutes(userTokenExpirationTimeMinutes - 2L).isAfter(LocalDateTime.now())) {
                LOGGER.log(Level.INFO, "Token cerca de expirar. Generando nuevo token. Usuario: " + this.user.getName());
                generarTokenUsuario();
            }
            return this.userToken;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
        return null;
    }

    @Produces
    @Named("user")
    public Principal getUser() {
        return user;
    }

    public void generarTokenUsuario() throws Exception {
        this.userToken = JWTUtils.generarToken(this.user.getName(), this.userIp, "/privateKey.pem", new ArrayList<>(operaciones), userTokenExpirationTimeMinutes, "SIGES", "SIGES");
        this.userTokenGeneratedDate = LocalDateTime.now();
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public void setUser(Principal user) throws Exception {
        this.user = user;
        cargarOperaciones();
        generarTokenUsuario();
        entidadUsuario = usuarioClient.obtenerPorCodigo(this.getUser().getName());
        cargarCantidadAlertasTempranas();
    }

    public void cargarCantidadAlertasTempranas() {
        try {
            if (this.operaciones.contains(ConstantesOperaciones.VER_NOTIFICACION_ALERTAS_TEMPRANAS)) {
                FiltroAlerta filtro = new FiltroAlerta();
                List<EnumRiesgoAlerta> riesgos = new ArrayList<>();
                riesgos.add(EnumRiesgoAlerta.MUY_ALTO);
                filtro.setAleRiesgos(riesgos);
                this.cantAlertasTempRiesgoMuyAlto = alertasClient.buscarTotal(filtro);

                riesgos = new ArrayList<>();
                riesgos.add(EnumRiesgoAlerta.ALTO);
                filtro.setAleRiesgos(riesgos);
                this.cantAlertasTempRiesgoAlto = alertasClient.buscarTotal(filtro);

                riesgos = new ArrayList<>();
                riesgos.add(EnumRiesgoAlerta.MEDIO);
                filtro.setAleRiesgos(riesgos);
                this.cantAlertasTempRiesgoMedio = alertasClient.buscarTotal(filtro);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String salir() {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    System.getProperty("casServerLoginUrl")
                            .replace("/login", "/logout")
                            .concat("?service=" + System.getProperty("service.welcome.baseUrl") + "/pp/inicio.xhtml"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
        return null;
    }

    public void cargarDepartamentos() {
        try {

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"depNombre"});
            fc.setIncluirCampos(new String[]{"depNombre", "depVersion"});
            List<SgDepartamento> deptos = catalogoClient.buscarDepartamento(fc);
            for (SgDepartamento d : deptos) {
                departamentos.put(d.getDepPk(), d);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
    }

    //No utilizar para ver si renderizar o no botones en una tabla ya que se llamaría muchas veces. Utilizar solamente cuando se ingresa al elemento.
    //TODO: si el usuario seleccionara el contexto con el que está trabajando, todo esto no sería necesario.
    public Boolean tieneOperacionEnContexto(String operacion, EnumAmbito ambito, Long contexto) {
        if (!operacionesSeguridad.containsKey(operacion)) {
            return Boolean.FALSE;
        }
        List<OperationSecurity> ops = operacionesSeguridad.get(operacion);

        for (OperationSecurity o : ops) {
            EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
            if (EnumAmbito.MINED.equals(ambitoOp)) {
                return Boolean.TRUE;
            }
        }

        //Contexto departamento
        if (EnumAmbito.DEPARTAMENTAL.equals(ambito)) {
            for (OperationSecurity o : ops) {
                EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
                Long contextoOp = Long.valueOf((Integer) o.getContext());
                if (EnumAmbito.DEPARTAMENTAL.equals(ambitoOp) && contextoOp.equals(contexto)) {
                    return Boolean.TRUE;
                }
            }
        }

        //Contexto sede
        if (EnumAmbito.SEDE.equals(ambito)) {
            for (OperationSecurity o : ops) {
                EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
                Long contextoOp = Long.valueOf((Integer) o.getContext());
                if (EnumAmbito.SEDE.equals(ambitoOp)) {
                    if (contextoOp.equals(contexto)) {
                        return Boolean.TRUE;
                    } else {
                        if (!this.sedes.containsKey(contexto)) {
                            List<SgSede> sedes = buscarSedes(contexto);
                            if (sedes != null && sedes.size() == 1) {
                                SgSede sede = sedes.get(0);
                                this.sedes.put(sede.getSedPk(), sede);
                            }
                        }
                        if (this.sedes.get(contexto) != null && this.sedes.get(contexto).getSedSedeAdscritaDe() != null) {
                            SgSede sedePadre = this.sedes.get(contexto).getSedSedeAdscritaDe();
                            if (sedePadre.getSedPk().equals(contextoOp)) {
                                return Boolean.TRUE;
                            }
                        }
                    }
                } else if (EnumAmbito.DEPARTAMENTAL.equals(ambitoOp)) {

                    if (!this.sedes.containsKey(contexto)) {
                        List<SgSede> sedes = buscarSedes(contexto);
                        if (sedes != null && sedes.size() == 1) {
                            SgSede sede = sedes.get(0);
                            this.sedes.put(sede.getSedPk(), sede);
                        }
                    }
                    SgDepartamento d = this.sedes.get(contexto).getSedDireccion().getDirDepartamento();
                    if (d.getDepPk().equals(contextoOp)) {
                        return Boolean.TRUE;
                    }
                } else if (EnumAmbito.SECCION.equals(ambitoOp)) {
                    //TODO: borrar esto, ya que permitiría editar estudiantes de misma sede pero de otra sección
                    //Lo correcto sería almacenar ultSecccion en el estudiante, y validar la sección.
                    if (!this.secciones.containsKey(contextoOp)) {
                        List<SgSeccion> secciones = buscarSecciones(contextoOp);
                        if (secciones != null && secciones.size() == 1) {
                            SgSeccion sec = secciones.get(0);
                            this.secciones.put(sec.getSecPk(), sec);
                        }
                    }

                    SgSede sed = this.secciones.get(contextoOp).getSecServicioEducativo().getSduSede();
                    if (sed.getSedPk().equals(contexto)) {
                        return Boolean.TRUE;
                    }

                }
            }
        }

        if (EnumAmbito.SECCION.equals(ambito)) {
            for (OperationSecurity o : ops) {
                EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
                Long contextoOp = Long.valueOf((Integer) o.getContext());
                if (EnumAmbito.SECCION.equals(ambitoOp) && contextoOp.equals(contexto)) {
                    return Boolean.TRUE;
                } else if (EnumAmbito.SEDE.equals(ambitoOp)) {

                    if (!this.secciones.containsKey(contexto)) {
                        List<SgSeccion> secciones = buscarSecciones(contexto);
                        if (secciones != null && secciones.size() == 1) {
                            SgSeccion sec = secciones.get(0);
                            this.secciones.put(sec.getSecPk(), sec);
                        }
                    }
                    SgSede sed = this.secciones.get(contexto).getSecServicioEducativo().getSduSede();
                    if (sed.getSedPk().equals(contextoOp)) {
                        return Boolean.TRUE;
                    }

                } else if (EnumAmbito.DEPARTAMENTAL.equals(ambitoOp)) {

                    if (!this.secciones.containsKey(contexto)) {
                        List<SgSeccion> secciones = buscarSecciones(contexto);
                        if (secciones != null && secciones.size() == 1) {
                            SgSeccion sec = secciones.get(0);
                            this.secciones.put(sec.getSecPk(), sec);
                        }
                    }

                    SgSede sed = this.secciones.get(contexto).getSecServicioEducativo().getSduSede();
                    SgDepartamento d = sed.getSedDireccion().getDirDepartamento();
                    if (d.getDepPk().equals(contextoOp)) {
                        return Boolean.TRUE;
                    }

                }
            }
        }

        if (EnumAmbito.PERSONA.equals(ambito)) {

            for (OperationSecurity o : ops) {
                EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
                Long contextoOp = Long.valueOf((Integer) o.getContext());
                if (EnumAmbito.PERSONA.equals(ambitoOp) && contextoOp.equals(contexto)) {
                    return Boolean.TRUE;
                } else if (EnumAmbito.SECCION.equals(ambitoOp)) {
                    return Boolean.TRUE; //TODO:
                } else if (EnumAmbito.SEDE.equals(ambitoOp)) {
                    return Boolean.TRUE; //TODO:
                } else if (EnumAmbito.DEPARTAMENTAL.equals(ambitoOp)) {
                    return Boolean.TRUE; //TODO:
                }
            }
        }
        return Boolean.FALSE;
    }

    public EnumAmbito obtenerMayorAmbitoOperacion(String operacion) {
        try {
            if (!operacionesSeguridad.containsKey(operacion)) {
                return null;
            }
            if (!operacionesMayorAmbito.containsKey(operacion)) {
                List<OperationSecurity> ops = operacionesSeguridad.get(operacion);
                EnumAmbito mayorAmbito = null;
                for (OperationSecurity o : ops) {
                    EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
                    if (mayorAmbito == null || ambitoOp.getOrden() < mayorAmbito.getOrden()) {
                        mayorAmbito = ambitoOp;
                    }
                }
                operacionesMayorAmbito.put(operacion, mayorAmbito);
            }
            return operacionesMayorAmbito.get(operacion);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
        return null;
    }

    public List<SgDepartamento> obtenerDepartamentosOperacion(String operacion) {
        try {
            if (!operacionesSeguridad.containsKey(operacion)) {
                return new ArrayList<>();
            }
            if (departamentos.isEmpty()) {
                cargarDepartamentos();
            }

            List<OperationSecurity> ops = operacionesSeguridad.get(operacion);
            Set<SgDepartamento> ret = new TreeSet<>();
            for (OperationSecurity o : ops) {
                EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
                if (EnumAmbito.MINED.equals(ambitoOp)) {
                    return new ArrayList<>(this.departamentos.values());
                }
            }
            for (OperationSecurity o : ops) {
                EnumAmbito ambitoOp = EnumAmbito.valueOf(o.getAmbit());
                if (EnumAmbito.DEPARTAMENTAL.equals(ambitoOp)) {
                    Integer pk = (Integer) o.getContext();
                    if (pk != null) {
                        ret.add(this.departamentos.get(Long.valueOf(pk)));
                    }
                } else if (EnumAmbito.SEDE.equals(ambitoOp)) {
                    Integer pk = (Integer) o.getContext();
                    if (pk != null) {
                        if (!this.sedes.containsKey(pk)) {
                            List<SgSede> sedes = buscarSedes(Long.valueOf(pk));
                            if (sedes != null && sedes.size() == 1) {
                                SgSede sede = sedes.get(0);
                                this.sedes.put(sede.getSedPk(), sede);
                            }
                        }
                        ret.add(this.sedes.get(Long.valueOf(pk)).getSedDireccion().getDirDepartamento());
                    }
                }
            }
            return new ArrayList<>(ret);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
        return null;

    }

    public void cargarOperaciones() {
        operaciones = new HashSet<>();
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        operacionesBackend = new HashSet<>();
        operacionesBackend.add(ConstantesOperaciones.AUTENTICADO);
        try {
            List<Long> categoriasOp = new ArrayList<>();
            categoriasOp.add(1L); //Centros
            categoriasOp.add(3L); //Seguridad
            categoriasOp.add(4L); //Acceso a servicios
            categoriasOp.add(5L); //Reportes
            categoriasOp.add(7L); //Infraestructura
            categoriasOp.add(13L); //Títulos
            List<OperationSecurity> ops = seguridadClient.obtenerOperacionesSeguridad(this.user.getName(), categoriasOp);
            for (OperationSecurity o : ops) {

                ambitoContextoOps.computeIfAbsent(EnumAmbito.valueOf(o.getAmbit()), s -> new HashMap<Object, List<OperationSecurity>>())
                        .computeIfAbsent(o.getContext(), s -> new ArrayList<>()).add(o);
                operacionesSeguridad.computeIfAbsent(o.getOperation(), s -> new ArrayList<>()).add(o);
            }
            operaciones.addAll(operacionesSeguridad.keySet());
            //Ignoramos las op del menu, Las de control de vista no porq hay algunas q se usan en backend. Esto lo tenemos q cambiar
            operacionesBackend.addAll(operacionesSeguridad.keySet().stream().filter(o -> !o.startsWith("EM")).collect(Collectors.toList()));

            cargarAmbitosBusquedaSeleccionable();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
        }
    }

    public void cargarAmbitosBusquedaSeleccionable() {

        List<EnumAmbito> ambitos = new ArrayList<>(ambitoContextoOps.keySet());

        Iterator<EnumAmbito> it = ambitos.iterator();
        while (it.hasNext()) {
            EnumAmbito am = it.next();
            if (am.equals(EnumAmbito.MINED)) {
                ambitosSeleccionablesBusqueda = new ArrayList<>();
                return;
            }
            if (am.equals(EnumAmbito.PERSONA)) {
                it.remove();
            }
        }

        if (ambitos.size() < 2) {
            ambitosSeleccionablesBusqueda = new ArrayList<>();
        } else {
            ambitos.sort(Comparator.comparing(EnumAmbito::getOrden));
            ambitosSeleccionablesBusqueda = new ArrayList<>(ambitos);
        }
    }

    public SgUsuario getEntidadUsuario() {
        return entidadUsuario;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Set<String> getOperaciones() {
        return operaciones;
    }

    public SgSede getSedeDefecto() {
        if (!this.seCargoSedeDefecto) {
            cargarSedePorDefecto();
            seCargoSedeDefecto = Boolean.TRUE;
        }
        return sedeDefecto;
    }

    public Locale getLocaleNumber() {
        return localeNumber;
    }

    public void setLocaleNumber(Locale localeNumber) {
        this.localeNumber = localeNumber;
    }

    public Long getCantAlertasTempRiesgoMuyAlto() {
        return cantAlertasTempRiesgoMuyAlto;
    }

    public Long getCantAlertasTempRiesgoAlto() {
        return cantAlertasTempRiesgoAlto;
    }

    public Long getCantAlertasTempRiesgoMedio() {
        return cantAlertasTempRiesgoMedio;
    }

    public List<EnumAmbito> getAmbitosSeleccionablesBusqueda() {
        return ambitosSeleccionablesBusqueda;
    }

}
