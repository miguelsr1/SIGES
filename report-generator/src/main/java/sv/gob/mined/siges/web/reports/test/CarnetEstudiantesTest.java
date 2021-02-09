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
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

public class CarnetEstudiantesTest extends AbstractReportGenerator {


    public CarnetEstudiantesTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("carnet_estudiante.prpt");

            // Parse the report file
            final ResourceManager resourceManager = new ResourceManager();
            final Resource directly = resourceManager.createDirectly(reportDefinitionURL, MasterReport.class);
            return (MasterReport) directly.getResource();
        } catch (ResourceException e) {
        }
        return null;
    }

    public DataFactory getDataFactory() {
                  
        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie", String.class);
        model.addColumn("nombre", String.class);
        model.addColumn("responsable", String.class);
        model.addColumn("contacto_emergencia", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("foto_estudiante_path", String.class);
        model.addRow("One", "<b>Jos&eacute; Juan</b>", "Resp", null, "2018");
        model.addRow("Two", "José Juan", "Andrés Santos Fernández Pérez Pérez Gómez", null, "2018",  "c:/sello");
        model.addRow("Three", "Roberto Juan Andrés Pérez Gómez", null, "Andrés Santos Fernández Pérez Pérez Gómez", "2018",  "c:/sello");
        model.addRow("Four", "Fourth Row", null, "Andrés Santos Fernández Pérez Pérez Gómez", "2018",  "c:/sello");

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        //parameters.put("Report Title", "Simple Embedded Report Example with Parameters");
        //parameters.put("Col Headers BG Color", "yellow"); 
        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(CarnetEstudiantesTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new CarnetEstudiantesTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
