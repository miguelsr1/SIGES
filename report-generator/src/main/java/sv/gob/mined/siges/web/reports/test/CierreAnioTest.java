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

public class CierreAnioTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public CierreAnioTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("cierre_anio.prpt");

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

        TypedTableModel model = new TypedTableModel();
        model.addColumn("secciones", TypedTableModel.class);
        model.addColumn("preguntas", TypedTableModel.class);

        TypedTableModel secciones = new TypedTableModel();

        secciones.addColumn("grado", String.class);
        secciones.addColumn("seccion", String.class);
        secciones.addColumn("opcion", String.class);
        secciones.addColumn("asistencias", Integer.class);
        secciones.addColumn("inasistencias", Integer.class);
        secciones.addColumn("pm", Integer.class);
        secciones.addColumn("pf", Integer.class);
        secciones.addColumn("pt", Integer.class);
        secciones.addColumn("nm", Integer.class);
        secciones.addColumn("nf", Integer.class);
        secciones.addColumn("nt", Integer.class);
        secciones.addColumn("ppm", Integer.class);
        secciones.addColumn("ppf", Integer.class);
        secciones.addColumn("ppt", Integer.class);
        secciones.addColumn("stm", Integer.class);
        secciones.addColumn("stf", Integer.class);
        secciones.addColumn("stt", Integer.class);

     
        secciones.addRow(
                "PARVULARIA 4",
                "A - Matutino",
                "",
                25,
                26,
                5,
                6,
                11,
                5,
                6,
                11,
                5,
                6,
                11,
                5,
                6,
                11
        );
        
        secciones.addRow(
                "SEGUNDO GRADO",
                "A - Matutino",
                "",
                25,
                26,
                5,
                6,
                11,
                5,
                6,
                11,
                5,
                6,
                11,
                5,
                6,
                11
        );
        
       

        TypedTableModel preguntas = new TypedTableModel();
        preguntas.addColumn("pregunta", String.class);
        preguntas.addColumn("respuesta", String.class);
       

        preguntas.addRow(
                "¿Ya realizó el cierre de todas las secciones?",
                "Sí"
        );
        
        preguntas.addRow(
                "¿Verificó que las cuentas de usuarios activas correspondan con el personal que está laborando actualmente en el Centro Educativo?",
                "No se sabe"
        );
        
        model.addRow(secciones, preguntas);

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("sede", "CENTRO ESCOLAR CATOLICO \" SANTA CATALINA\"");
        parameters.put("anio", 2019);
        parameters.put("total_asistencias", 45);
        parameters.put("total_inasistencias", 66);
        parameters.put("total_promovidos_m", 20);
        parameters.put("total_promovidos_f", 20);
        parameters.put("total_promovidos_t", 40);
        parameters.put("total_no_promovidos_m", 10);
        parameters.put("total_no_promovidos_f", 10);
        parameters.put("total_no_promovidos_t", 20);
        parameters.put("total_promovidos_pend_m", 5);
        parameters.put("total_promovidos_pend_f", 5);
        parameters.put("total_promovidos_pend_t", 10);
        parameters.put("total_sol_titulo_m", 1);
        parameters.put("total_sol_titulo_f", 1);
        parameters.put("total_sol_titulo_t", 2);
        
        
        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(CierreAnioTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new CierreAnioTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
