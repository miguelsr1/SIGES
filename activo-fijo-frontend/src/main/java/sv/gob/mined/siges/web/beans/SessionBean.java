/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.centroseducativos.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
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
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;

    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;

    private String ambiente;
    private String piePagina = "";
    private String timeZone = "GMT-6";
    private Locale locale;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private Integer userTokenExpirationTimeMinutes = 20;
    private String userIp;
    private Principal user;
    private SgUsuario entidadUsuario;
    private Locale localeNumber;
    private Set<String> operaciones;
    private HashMap<String, List<OperationSecurity>> operacionesSeguridad = new HashMap<>();
    private HashMap<String, EnumAmbito> operacionesMayorAmbito = new HashMap<>();
    private HashMap<Long, SgDepartamento> departamentos = new HashMap<>();
    private HashMap<Long, SgSede> sedes = new HashMap<>();
    private HashMap<Long, SgAfUnidadesAdministrativas> unidades = new HashMap<>();
    private SgSede sedeDefecto;
    private SgAfUnidadesAdministrativas unidadADDefecto;
    private SgAfUnidadesActivoFijo unidadAFDefecto;
    private Boolean seCargoSedeDefecto = Boolean.FALSE;
    private Boolean seCargoUnidadAFDefecto = Boolean.FALSE;
    private Boolean seCargoUnidadADDefecto = Boolean.FALSE;

    private NumberFormat numberFormat;
    
    public SessionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            locale = new Locale("es");
            localeNumber = new Locale("es_SV");
            
            numberFormat = NumberFormat.getNumberInstance(locale);
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

    public void cargarUnidadActivoFijoPorDefecto() {
        try {
            List<SgAfUnidadesActivoFijo> unidadesAF = buscarUnidadesActivoFijo(null);
            if (unidadesAF != null && unidadesAF.size() == 1) {
                unidadAFDefecto = unidadesAF.get(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarUnidadAdministrativaPorDefecto() {
        try {
            List<SgAfUnidadesAdministrativas> unidadesAD = buscarUnidadesAdministrativas(null);
           // LOGGER.info("unidades obtenidas: " + unidadesAD.size());
            if (unidadesAD != null && unidadesAD.size() == 1) {
                unidadADDefecto = unidadesAD.get(0);
                this.unidades.put(unidadADDefecto.getUadPk(), unidadADDefecto);
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
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo",
                "sedNombre",
                "sedTipo",
                "sedVersion",
                "sedDireccion.dirDepartamento.depPk",
                "sedDireccion.dirDepartamento.depNombre",
                "sedDireccion.dirDepartamento.depVersion",
                "sedSedeAdscritaDe.sedPk",
                "sedSedeAdscritaDe.sedTipo"});
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

    public List<SgAfUnidadesAdministrativas> buscarUnidadesAdministrativas(Long unidadesPk) {
        try {
            FiltroUnidadesAdministrativas fil = new FiltroUnidadesAdministrativas();
            //fil.setUnidadActivoFijoId(unidadesPk);
           // fil.setUnidadesActivoFijoId(unidadesPk);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"uadNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setMaxResults(2L);
            fil.setIncluirCampos(new String[]{"uadCodigo",
                "uadNombre",
                "uadVersion",
                "uadUnidadActivoFijoFk.uafPk",
                "uadUnidadActivoFijoFk.uafNombre",
                "uadUnidadActivoFijoFk.uafVersion"});
            return unidadesAdministrativasRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgAfUnidadesActivoFijo> buscarUnidadesActivoFijo(List<Long> departamentosPk) {
        try {
            FiltroUnidadesActivoFijo fil = new FiltroUnidadesActivoFijo();
            if (departamentosPk != null){
                fil.setDepartamentosId(departamentosPk);
            }
            fil.setMaxResults(2L);
            fil.setHabilitado(Boolean.TRUE);
            fil.setOrderBy(new String[]{"uafNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"uafCodigo",
                "uafNombre",
                "uafVersion",
                "uafDepartamento.depPk",
                "uafDepartamento.depNombre",
                "uafDepartamento.depVersion"});
            return unidadesActivoFijoRestClient.buscar(fil);
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
            if (userTokenGeneratedDate == null || !userTokenGeneratedDate.plusMinutes(userTokenExpirationTimeMinutes - 2).isAfter(LocalDateTime.now())) {
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
        this.userToken = JWTUtils.generarToken(this.user.getName(), this.userIp, "/privateKey.pem", new ArrayList<>(operaciones), userTokenExpirationTimeMinutes);
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

    public void setUnidadADDefecto(SgAfUnidadesAdministrativas unidadADDefecto) {
        this.unidadADDefecto = unidadADDefecto;
    }

    public void setUnidadAFDefecto(SgAfUnidadesActivoFijo unidadAFDefecto) {
        this.unidadAFDefecto = unidadAFDefecto;
    }

    public Boolean getSeCargoSedeDefecto() {
        return seCargoSedeDefecto;
    }

    public void setSeCargoSedeDefecto(Boolean seCargoSedeDefecto) {
        this.seCargoSedeDefecto = seCargoSedeDefecto;
    }

    public Boolean getSeCargoUnidadAFDefecto() {
        return seCargoUnidadAFDefecto;
    }

    public void setSeCargoUnidadAFDefecto(Boolean seCargoUnidadAFDefecto) {
        this.seCargoUnidadAFDefecto = seCargoUnidadAFDefecto;
    }

    public Boolean getSeCargoUnidadADDefecto() {
        return seCargoUnidadADDefecto;
    }

    public void setSeCargoUnidadADDefecto(Boolean seCargoUnidadADDefecto) {
        this.seCargoUnidadADDefecto = seCargoUnidadADDefecto;
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
                if (EnumAmbito.SEDE.equals(ambitoOp) && contextoOp.equals(contexto)) {
                    return Boolean.TRUE;
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
        try {
            List<Long> categoriasOp = new ArrayList<>();
            categoriasOp.add(1L); //Centros
            categoriasOp.add(3L); //Seguridad
            categoriasOp.add(4L); //Acceso a servicios
            categoriasOp.add(5L); //Activo fijo
            List<OperationSecurity> ops = seguridadClient.obtenerOperacionesSeguridad(this.user.getName(), categoriasOp);
            for (OperationSecurity o : ops) {
                operacionesSeguridad.computeIfAbsent(o.getOperation(), s -> new ArrayList<>()).add(o);
            }
            operaciones.addAll(operacionesSeguridad.keySet());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.getUser(), ex), ex);
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

    public SgAfUnidadesActivoFijo getUnidadAFDefecto() {
        if (!this.seCargoUnidadAFDefecto) {
            cargarUnidadActivoFijoPorDefecto();
            seCargoUnidadAFDefecto = Boolean.TRUE;
        }
        return unidadAFDefecto;
    }

    public SgAfUnidadesAdministrativas getUnidadADDefecto() {
        if (!this.seCargoUnidadADDefecto) {
            cargarUnidadAdministrativaPorDefecto();
            seCargoUnidadADDefecto = Boolean.TRUE;
        }
        return unidadADDefecto;
    }

    public String getFormatNumber(BigDecimal number, Integer min, Integer max) {
        if(number == null) {
            return "0.00";
        }
        if(numberFormat != null) {
            numberFormat.setGroupingUsed(true);
            numberFormat.setMinimumFractionDigits(min != null ? min : 2);
            numberFormat.setMaximumFractionDigits(max != null ? max : 2);
            return numberFormat.format(number);
        } else {
            return number.setScale(2, RoundingMode.HALF_EVEN).toString();
        }

    }
    public Locale getLocaleNumber() {
        return localeNumber;
    }

    public void setLocaleNumber(Locale localeNumber) {
        this.localeNumber = localeNumber;
    }

    public HashMap<Long, SgAfUnidadesAdministrativas> getUnidades() {
        return unidades;
    }

    public void setUnidades(HashMap<Long, SgAfUnidadesAdministrativas> unidades) {
        this.unidades = unidades;
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

}
