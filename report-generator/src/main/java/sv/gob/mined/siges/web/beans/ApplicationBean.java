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
    @ConfigProperty(name = "project.name", defaultValue = "Sistema de Gestión de Información Educativa Salvadoreña")
    private String nombreAplicacion;

    @Inject
    @ConfigProperty(name = "project.stage")
    private String ambiente;

    private String version;
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
            version = atts.getValue("Implementation-Version");
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public String getAmbiente() {
        return ambiente;
    }

}
