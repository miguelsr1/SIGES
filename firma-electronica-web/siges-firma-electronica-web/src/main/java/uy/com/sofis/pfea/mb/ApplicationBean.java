/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package uy.com.sofis.pfea.mb;

import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@ApplicationScoped
public class ApplicationBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ApplicationBean.class.getName());

    private String ambiente;

    private String welcomeWebUrl;
    
    private String version;

    private String nombreAplicacion = "Sistema de Información para la Gestión Educativa Salvadoreña";


    private String timestamp;
    private final String paginatorTemplate = "{RowsPerPageDropdown} {CurrentPageReport} {primerPagina} {anterior} {PageLinks} {siguiente} {ultimaPagina}";
    private final String rowsPerPageTemplate = "1,5,10,20";
    private final String rowsPerPageTemplate2 = "1,5,10,25,50,100";
    private final String patternFecha = "dd/MM/yyyy";
    private final String patternHora = "HH:mm";
    private final String patternFechaHora = "dd/MM/yyyy HH:mm";
    private final DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern(patternFecha);
    private final DateTimeFormatter dateTimeFormater = DateTimeFormatter.ofPattern(patternFechaHora);
    private final DateTimeFormatter timeFormater = DateTimeFormatter.ofPattern(patternHora);
    private final String encodingDataExport = "iso-8859-1";
    private final String patternMail = "(^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$)?";
    private String formatValoresDecimales = "#,##0.00";
    private String formatPorcentaje = "###,###,##0.00";
    private final String rowsPerPageTemplate3 = "3,6,12,18"; 
    
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
            version = atts.getValue("Implementation-Version");
            timestamp = atts.getValue("build-time");
            
            welcomeWebUrl = System.getProperty("service.welcome.baseUrl");
            ambiente = System.getProperty("ambiente");

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


    public String getAmbiente() {
        return ambiente;
    }

    public String getPaginatorTemplate() {
        return paginatorTemplate;
    }

    public String getRowsPerPageTemplate() {
        return rowsPerPageTemplate;
    }

    public String getPatternFecha() {
        return patternFecha;
    }

    public String getEncodingDataExport() {
        return encodingDataExport;
    }

    public String getPatternMail() {
        return patternMail;
    }

    public DateTimeFormatter getDateFormater() {
        return dateFormater;
    }

    public DateTimeFormatter getDateTimeFormater() {
        return dateTimeFormater;
    }

    public String getFormatValoresDecimales() {
        return formatValoresDecimales;
    }

    public String getPatternFechaHora() {
        return patternFechaHora;
    }

    public String getFormatPorcentaje() {
        return formatPorcentaje;
    }

    public String getWelcomeWebUrl() {
        return welcomeWebUrl;
    }

    public String getPatternHora() {
        return patternHora;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    

  
    public LocalDate today() {
        return LocalDate.now();
    }

    public String getRowsPerPageTemplate2() {
        return rowsPerPageTemplate2;
    }

    public String getRowsPerPageTemplate3() {
        return rowsPerPageTemplate3;
    }


  
    
}
