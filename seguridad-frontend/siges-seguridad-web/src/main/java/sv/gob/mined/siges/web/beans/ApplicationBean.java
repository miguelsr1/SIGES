/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import sv.gob.mined.siges.web.dto.catalogo.SgAyuda;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.AyudaRestClient;

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
    @ConfigProperty(name = "project.version")
    private String version;

    @Inject
    private AyudaRestClient ayudaRestClient;

    private String timestamp;
    private final String paginatorTemplate = "{RowsPerPageDropdown} {CurrentPageReport} {anterior} {PageLinks} {siguiente}";
    private final String rowsPerPageTemplate = "1,5,10,25,50,100";
    private final String patternFecha = "dd/MM/yyyy";
    private final String patternFechaHora = "dd/MM/yyyy HH:mm";
    private final String encodingDataExport = "iso-8859-1";
    private final String patternMail = "(^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$)?";
    private String formatValoresDecimales = "#,##0.00";
    private String formatPorcentaje = "###,###,##0.00";
     
    private Map<String, SgAyuda> ayudas = new HashMap<>();

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

    public SelectItem[] getHabilitadoValues() {
        return new SelectItem[]{
            new SelectItem(Boolean.TRUE, "Sí"),
            new SelectItem(Boolean.FALSE, "No")
        };
    }

    //TODO: cambiar cuando se utilice kafka
    public SgAyuda getAyuda(String codigo) {
        try {
//            if (this.ayudas.containsKey(codigo)) {
//                return this.ayudas.get(codigo);
//            } else {
                FiltroCodiguera filtro = new FiltroCodiguera();
                filtro.setCodigoExacto(codigo);
                filtro.setHabilitado(Boolean.TRUE);
                List<SgAyuda> ayudas = ayudaRestClient.buscar(filtro);
                if (!ayudas.isEmpty()) {
                    this.ayudas.put(codigo, ayudas.get(0));
    }
                return this.ayudas.get(codigo);
//            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public String obtenerAyuda(String codigo) { //TODO: borrar
        return codigo;
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

    public DateTimeFormatter getFormatterFechaHora() {
        return DateTimeFormatter.ofPattern(patternFechaHora);
    }

    public DateTimeFormatter getFormatterFecha() {
        return DateTimeFormatter.ofPattern(patternFecha);
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

    public String getAmbiente() {
        return ambiente;
    }

    public String getWelcomeWebUrl() {
        return welcomeWebUrl;
    }

    public String getAutogestionWebUrl() {
        return autogestionWebUrl;
    }

    }
