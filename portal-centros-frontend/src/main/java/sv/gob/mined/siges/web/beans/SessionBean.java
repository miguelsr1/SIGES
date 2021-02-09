/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import com.sun.security.auth.UserPrincipal;
import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.utils.JWTUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;

@Named
@SessionScoped
public class SessionBean implements Serializable {
    
    @Inject
    private CatalogosRestClient restCatalogo;

    private static final Logger LOGGER = Logger.getLogger(SessionBean.class.getName());

    private String ambiente;
    private String piePagina = "";
    private Locale locale;
    private String userToken;
    private LocalDateTime userTokenGeneratedDate;
    private Integer userTokenExpirationTimeMinutes = 20;
    private String userIp;
    private Locale localeNumber;

    private Set<String> operaciones;
    private Boolean mostrarBotonEstadisticas=Boolean.FALSE;
    private Boolean mostrarBotonDatosAbiertos=Boolean.FALSE;
    private Boolean mostrarBotonQGIS=Boolean.FALSE;
   
    private Principal user ;

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

    public void buscarConfiguracionBoton(){
        try{
            FiltroCodiguera fc=new FiltroCodiguera();
            fc.setCodigo(Constantes.BOTON_ESTADISTICAS_PORTAL);
            List<SgConfiguracion> conf=restCatalogo.buscarConfiguracion(fc);
            if(!conf.isEmpty()){
                if("1".equals(conf.get(0).getConValor())){
                    mostrarBotonEstadisticas=Boolean.TRUE;
                }
            }
            fc.setCodigo(Constantes.BOTON_DATOS_ABIERTOS_PORTAL);
            conf=restCatalogo.buscarConfiguracion(fc);
            if(!conf.isEmpty()){
                if("1".equals(conf.get(0).getConValor())){
                    mostrarBotonDatosAbiertos=Boolean.TRUE;
                }
            }
            
            fc.setCodigo(Constantes.BOTON_QGIS_PORTAL);
            conf=restCatalogo.buscarConfiguracion(fc);
            if(!conf.isEmpty()){
                if("1".equals(conf.get(0).getConValor())){
                    mostrarBotonQGIS=Boolean.TRUE;
                }
            }
            
            
        }catch(Exception e){
            
        }
    }

    @Produces
    @Named("userToken")
    public String getUserToken() {
        try {
            if (userTokenGeneratedDate == null || !userTokenGeneratedDate.plusMinutes(userTokenExpirationTimeMinutes - 2L).isAfter(LocalDateTime.now())) {
                LOGGER.log(Level.INFO, "Token cerca de expirar. Generando nuevo token. Portal centros.");
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
        this.userToken = JWTUtils.generarToken(user.getName(), this.userIp, "/privateKey.pem", new ArrayList<>(operaciones), userTokenExpirationTimeMinutes);
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


    public String getPiePagina() {
        return piePagina;
    }

    public String getUserIp() {
        return userIp;
    }
    
    public void setUserIp(String userIp) throws Exception {
        this.userIp = userIp;
        operaciones = new HashSet<>();
        operaciones.add(ConstantesOperaciones.AUTENTICADO);
        operaciones.add(ConstantesOperaciones.BUSCAR_SEDES);
        operaciones.add(ConstantesOperaciones.BUSCAR_SERVICIO_EDUCATIVO);
        operaciones.add(ConstantesOperaciones.BUSCAR_ORGANIZACION_CURRICULAR);
        operaciones.add(ConstantesOperaciones.BUSCAR_NIVEL);
        operaciones.add(ConstantesOperaciones.BUSCAR_MODALIDAD);
        operaciones.add(ConstantesOperaciones.BUSCAR_RELACION_MODALIDAD_EDUCATIVA_ATENCION);
        operaciones.add(ConstantesOperaciones.BUSCAR_CICLO);
        operaciones.add(ConstantesOperaciones.BUSCAR_GRADO);
        operaciones.add(ConstantesOperaciones.BUSCAR_OPCION);
        operaciones.add(ConstantesOperaciones.BUSCAR_REL_INMUEBLE_ARCHIVO);
        operaciones.add(ConstantesOperaciones.BUSCAR_SECCION);
        operaciones.add(ConstantesOperaciones.BUSCAR_SOLICITUD_IMPRESION);
        operaciones.add(ConstantesOperaciones.BUSCAR_TITULO);
        operaciones.add(ConstantesOperaciones.BUSCAR_DIPLOMAS); 
        operaciones.add(ConstantesOperaciones.BUSCAR_PROPUESTA_PEDAGOGICA); 
        user = new UserPrincipal("Portal centros " + userIp);
        generarTokenUsuario();
        buscarConfiguracionBoton();
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
    

    public Set<String> getOperaciones() {
        return operaciones;
    }

    public Locale getLocaleNumber() {
        return localeNumber;
    }

    public void setLocaleNumber(Locale localeNumber) {
        this.localeNumber = localeNumber;
    }

    public Boolean getMostrarBotonEstadisticas() {
        return mostrarBotonEstadisticas;
    }

    public void setMostrarBotonEstadisticas(Boolean mostrarBotonEstadisticas) {
        this.mostrarBotonEstadisticas = mostrarBotonEstadisticas;
    }

    public Boolean getMostrarBotonDatosAbiertos() {
        return mostrarBotonDatosAbiertos;
    }

    public void setMostrarBotonDatosAbiertos(Boolean mostrarBotonDatosAbiertos) {
        this.mostrarBotonDatosAbiertos = mostrarBotonDatosAbiertos;
    }

    public Boolean getMostrarBotonQGIS() {
        return mostrarBotonQGIS;
    }

    public void setMostrarBotonQGIS(Boolean mostrarBotonQGIS) {
        this.mostrarBotonQGIS = mostrarBotonQGIS;
    }
    
    

    

}
