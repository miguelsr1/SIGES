package sv.gob.mined.siges.web.reports.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class ConfirmacionTrasladoTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public ConfirmacionTrasladoTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("traslado_confirmado.prpt");

            // Parse the report file
            final ResourceManager resourceManager = new ResourceManager();
            final Resource directly = resourceManager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) directly.getResource();
            return report;
        } catch (ResourceException e) {
        }
        return null;
    }

    public DataFactory getDataFactory() {
        return null;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("sedeOrigen", "CENTRO ESCOLAR CATOLICO \" SANTA CATALINA\"");
        parameters.put("sedeDestino", "CENTRO ESCOLAR CATOLICO \" SANTA CATALINA\"");
        parameters.put("gradoOrigen", "PRIMER GRADO");
        parameters.put("seccionDestino", "A - Matutino");
        parameters.put("gradoDestino", "PRIMER GRADO B");
        parameters.put("nie", "45465");
        parameters.put("nombre", "Juan Roberto");
        parameters.put("sexo", "Masculino");
        parameters.put("fechaNacimiento", "25/03/1992");
        parameters.put("fechaTraslado", "15/05/2020");
        parameters.put("motivo", "Motivo X");
        parameters.put("observaciones", "obs");

        
        
        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(ConfirmacionTrasladoTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new ConfirmacionTrasladoTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
