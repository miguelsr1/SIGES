/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

 
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.web.delegates.ConfiguracionDelegate;
import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;


/**
 * Contiene datos genéricos de la aplicación.
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class AplicacionMB {

    
    @Inject
    private ConfiguracionDelegate confDelegate;
   
    private  static String timeZone="America/El Salvador";
    private  static String locale="es";
    private  static String patronFechas="dd-MM-yyyy";
    private static String patronFechaHora="dd-MM-yyyy HH:mm:ss";
    private static final Long ULT_MOD_CONF = 0L;
    private static final long MAX_PERIODO_ACTUALIZACION = 300000;
    private String version;  
    private HashMap<String, Configuracion> configuracion = new HashMap();


    /**
     * Creates a new instance of AplicacionMB
     */
    public AplicacionMB() {
    }

    @PostConstruct
    private void init() {
        Manifest manifest;
        try {
            InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/META-INF/MANIFEST.MF");
            manifest = new Manifest();
            manifest.read(is);

            Attributes atts = manifest.getMainAttributes();

            version = atts.getValue("Implementation-Build");

        } catch (IOException ex) {
            Logger.getLogger(AplicacionMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga todas las configuraciones del sistema en un MAP
     */
    private void cargarConfiguracion() {
        try {
            List<Configuracion> listaConf = confDelegate.obtenerTodos();
            configuracion = new HashMap();
            for (Configuracion c : listaConf) {
                configuracion.put(c.getCnfCodigo(), c);
            }
        } catch (Exception ex) {
            Logger.getLogger(AplicacionMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * Obtiene la configuración a partir del código.
     * @param codigo
     * @return 
     */
    public Configuracion obtenerConfiguracionPorCodigo(String codigo) {
        if (GregorianCalendar.getInstance().getTimeInMillis() - ULT_MOD_CONF > MAX_PERIODO_ACTUALIZACION) {
            cargarConfiguracion();
        }
        return configuracion.get(codigo);
    }


    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZoneParam) {
         timeZone = timeZoneParam;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String localeParam) {
        locale = localeParam;
    }

    public String getPatronFechas() {
        return patronFechas;
    }

    public void setPatronFechas(String patronFechasParam) {
         patronFechas = patronFechasParam;
    } 

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public   String getPatronFechaHora() {
        return patronFechaHora;
    }

    public   void setPatronFechaHora(String patronFechaHoraParam) {
         patronFechaHora = patronFechaHoraParam;
    }
    
    
     
    
    
    
}
