/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Named
@ApplicationScoped
public class ApplicationBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ApplicationBean.class.getName());

    @Inject
    @ConfigProperty(name = "project.stage")
    private String ambiente;

    @Inject
    @ConfigProperty(name = "service.welcome.baseUrl")
    private String welcomeWebUrl;

    @Inject
    @ConfigProperty(name = "service.autogestion-web.baseUrl")
    private String autogestionWebUrl;
    
    @Inject
    @ConfigProperty(name = "service.report-generator.baseUrl")
    private String reportGeneratorUrl;

    @Inject
    @ConfigProperty(name = "project.version")
    private String version;

    @Inject
    @ConfigProperty(name = "project.name", defaultValue = "Sistema de Información para la Gestión Educativa Salvadoreña")
    private String nombreAplicacion;

    @Inject
    @ConfigProperty(name = "files.allowed-size.documents", defaultValue = "10485760") //10MB
    private String documentosTamPermitido;

    @Inject
    @ConfigProperty(name = "files.allowed-size.images", defaultValue = "5242880") //5MB
    private String fotosTamPermitido;

    private String timestamp;
    private final String paginatorTemplate = "{RowsPerPageDropdown} {CurrentPageReport} {primerPagina} {anterior} {PageLinks} {siguiente} {ultimaPagina}";
    private final String rowsPerPageTemplate = "1,5,10,20";
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
            //version = atts.getValue("Implementation-Version");
            timestamp = atts.getValue("build-time");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public SelectItem[] getBooleanValues() {
        return new SelectItem[]{
            new SelectItem(Boolean.TRUE, "Sí"),
            new SelectItem(Boolean.FALSE, "No")
        };
    }

    public SelectItem[] getEstadoValues() {
        return new SelectItem[]{
            new SelectItem(Boolean.TRUE, "Activo"),
            new SelectItem(Boolean.FALSE, "Inactivo")
        };
    }
    
    public SelectItem[] getHabilitadoValues() {
        return new SelectItem[]{
            new SelectItem(Boolean.TRUE, "Sí"),
            new SelectItem(Boolean.FALSE, "No")
        };
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
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

    public String getAutogestionWebUrl() {
        return autogestionWebUrl;
    }

    public LocalDate today() {
        return LocalDate.now();
    }

    public String getDocumentosTamPermitido() {
        return documentosTamPermitido;
    }

    public String getFotosTamPermitido() {
        return fotosTamPermitido;
    }

    public String getReportGeneratorUrl() {
        return reportGeneratorUrl;
    }

    public void setReportGeneratorUrl(String reportGeneratorUrl) {
        this.reportGeneratorUrl = reportGeneratorUrl;
    }
    
}
