/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class ApplicationBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ApplicationBean.class.getName());
    
    @Inject
    @ConfigProperty(name = "service.manual.baseUrl")
    private String manualWebUrl;

    @Inject
    @ConfigProperty(name = "service.catalogo-web.baseUrl")
    private String catalogoWebUrl;

    @Inject
    @ConfigProperty(name = "service.centros-educativos-web.baseUrl")
    private String centrosEducativosWebUrl;

    @Inject
    @ConfigProperty(name = "service.activo-fijo-web.baseUrl")
    private String activoFijoWebUrl;

    @Inject
    @ConfigProperty(name = "service.estadisticas-web.baseUrl")
    private String estadisticasWebUrl;

    @Inject
    @ConfigProperty(name = "service.simple.baseUrl")
    private String simpleWebUrl;

    @Inject
    @ConfigProperty(name = "service.seguridad-web.baseUrl")
    private String seguridadWebUrl;

    @Inject
    @ConfigProperty(name = "service.autogestion-web.baseUrl")
    private String autogestionWebUrl;

    @Inject
    @ConfigProperty(name = "service.infraestructura-web.baseUrl")
    private String infraestructuraWebUrl;

    @Inject
    @ConfigProperty(name = "service.registro-historico-web.baseUrl")
    private String registroHistoricoWebUrl;    
    
    @Inject
    @ConfigProperty(name = "service.sistemas-integrados-web.baseUrl")
    private String sistemasIntegradosWebUrl;  
    
    @Inject
    @ConfigProperty(name = "service.finanzas.baseUrl")
    private String finanzasWebUrl;  

    @Inject
    @ConfigProperty(name = "service.pentaho.baseUrl")
    private String pentahoWebUrl;
    
    @Inject
    @ConfigProperty(name = "service.sistema-informacion-gerencial.baseUrl")
    private String sistemaInformacionGerencialWebUrl;
    
    @Inject
    @ConfigProperty(name = "service.gis.baseUrl")
    private String gisWebUrl;

    @Inject
    @ConfigProperty(name = "project.name", defaultValue = "Sistema de Información para la Gestión Educativa Salvadoreña")
    private String nombreAplicacion;

    @Inject
    @ConfigProperty(name = "project.stage")
    private String ambiente;

    private String timestamp;
    private final String patternFecha = "dd/MM/yyyy";
    private final String patternFechaHora = "dd/MM/yyyy HH:mm";

    /**
     * Creates a new instance of AplicacionManagedBean
     */
    public ApplicationBean() {

    }

    @PostConstruct
    private void init() {
        Manifest manifest;
        try {
            InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/META-INF/MANIFEST.MF");
            manifest = new Manifest();
            manifest.read(is);

            Attributes atts = manifest.getMainAttributes();
            for (Iterator it = atts.keySet().iterator(); it.hasNext();) {
                Attributes.Name attrName = (Attributes.Name) it.next();
                String attrValue = atts.getValue(attrName);
                LOGGER.log(Level.INFO, "*attr: " + attrValue);
            }
            timestamp = atts.getValue("build-time");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPatternFecha() {
        return patternFecha;
    }

    public DateTimeFormatter getFormatterFechaHora() {
        return DateTimeFormatter.ofPattern(patternFechaHora);
    }

    public DateTimeFormatter getFormatterFecha() {
        return DateTimeFormatter.ofPattern(patternFecha);
    }

    public String getPatternFechaHora() {
        return patternFechaHora;
    }

    public String getCatalogoWebUrl() {
        return catalogoWebUrl;
    }

    public String getCentrosEducativosWebUrl() {
        return centrosEducativosWebUrl;
    }

    public String getSimpleWebUrl() {
        return simpleWebUrl;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public String getSeguridadWebUrl() {
        return seguridadWebUrl;
    }

    public String getAutogestionWebUrl() {
        return autogestionWebUrl;
    }

    public String getActivoFijoWebUrl() {
        return activoFijoWebUrl;
    }

    public void setActivoFijoWebUrl(String activoFijoWebUrl) {
        this.activoFijoWebUrl = activoFijoWebUrl;
    }

    public String getEstadisticasWebUrl() {
        return estadisticasWebUrl;
    }

    public void setEstadisticasWebUrl(String estadisticasWebUrl) {
        this.estadisticasWebUrl = estadisticasWebUrl;
    }

    public String getInfraestructuraWebUrl() {
        return infraestructuraWebUrl;
    }

    public void setInfraestructuraWebUrl(String infraestructuraWebUrl) {
        this.infraestructuraWebUrl = infraestructuraWebUrl;
    }

    public String getRegistroHistoricoWebUrl() {
        return registroHistoricoWebUrl;
    }

    public void setRegistroHistoricoWebUrl(String registroHistoricoWebUrl) {
        this.registroHistoricoWebUrl = registroHistoricoWebUrl;
    }

    public String getSistemasIntegradosWebUrl() {
        return sistemasIntegradosWebUrl;
    }

    public void setSistemasIntegradosWebUrl(String sistemasIntegradosWebUrl) {
        this.sistemasIntegradosWebUrl = sistemasIntegradosWebUrl;
    }
    
    public String getPentahoWebUrl() {
        return pentahoWebUrl;
    }

    public void setPentahoWebUrl(String pentahoWebUrl) {
        this.pentahoWebUrl = pentahoWebUrl;
    }

    public String getGisWebUrl() {
        return gisWebUrl;
    }

    public void setGisWebUrl(String gisWebUrl) {
        this.gisWebUrl = gisWebUrl;
    }    

    public String getManualWebUrl() {
        return manualWebUrl;
    }

    public String getFinanzasWebUrl() {
        return finanzasWebUrl;
    }

    public void setFinanzasWebUrl(String finanzasWebUrl) {
        this.finanzasWebUrl = finanzasWebUrl;
    }

    public String getSistemaInformacionGerencialWebUrl() {
        return sistemaInformacionGerencialWebUrl;
    }

    public void setSistemaInformacionGerencialWebUrl(String sistemaInformacionGerencialWebUrl) {
        this.sistemaInformacionGerencialWebUrl = sistemaInformacionGerencialWebUrl;
    }
    
    

}
