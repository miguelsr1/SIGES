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
import org.pentaho.reporting.engine.classic.core.wizard.RelationalAutoGeneratorPreProcessor;

public class MatriculasEnSedeGroupingGeneratorTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public MatriculasEnSedeGroupingGeneratorTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("matriculas_en_sede.prpt");

            // Parse the report file
            final ResourceManager resourceManager = new ResourceManager();
            final Resource directly = resourceManager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) directly.getResource();
            report.addPreProcessor(new RelationalAutoGeneratorPreProcessor());
            return report;
        } catch (ResourceException e) {
        }
        return null;
    }

    public DataFactory getDataFactory() {
               
               
        TypedTableModel grados = new TypedTableModel();
        grados.addColumn("nivel", String.class);
        grados.addColumn("ciclo", String.class);
        grados.addColumn("modalidad_educativa", String.class);
        grados.addColumn("modalidad_atencion", String.class);
        grados.addColumn("grado", String.class);
        grados.addColumn("cant_masculino", Long.class);
        grados.addColumn("cant_femenino", Long.class);
        grados.addColumn("total", Long.class);
        
        grados.addRow("Nivel1", "Ciclo1", "ME", "Regular", "Primer Grado", 10L, 11L, 5L);
        grados.addRow("Nivel1", "Ciclo2", "ME", "Regular", "Primer Grado", 10L, 11L, 8L);
        grados.addRow("Nivel1", "Ciclo2","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel2", "Ciclo1","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("Nivel3", "Ciclo3","ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        
       
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", grados);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("sede", "1001 - Centro Escolar Isidro Menéndez");
        parameters.put("departamento", "San Salvador");
        parameters.put("municipio", "San Salvador");
        parameters.put("anio_lectivo", "2019");
        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(MatriculasEnSedeGroupingGeneratorTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new MatriculasEnSedeGroupingGeneratorTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
