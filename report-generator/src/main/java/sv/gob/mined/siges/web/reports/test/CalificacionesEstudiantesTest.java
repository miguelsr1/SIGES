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

public class CalificacionesEstudiantesTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public CalificacionesEstudiantesTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("calificaciones_estudiantes.prpt");

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
               
        
        TypedTableModel a3 = new TypedTableModel();
        a3.addColumn("componente_plan_estudio", String.class);
        a3.addColumn("p1", String.class);
        a3.addColumn("p2", String.class);
        a3.addColumn("p3", String.class);
        a3.addColumn("ni", String.class);
        a3.addColumn("pp", String.class);
        a3.addColumn("pps", String.class);
        a3.addColumn("sp", String.class);
        a3.addColumn("sps", String.class);
        a3.addColumn("nf", String.class);
        a3.addColumn("resultado", String.class);
        a3.addRow("Ciencias naturales", "6.1", "5", "6.1", "5", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a3.addRow("Matemática", "6.1", "5", "6.1", "5", "6.1", "6.1", "5", "6.1", "5", "ARPOBADO");
        a3.addRow("Cursos de habilitación laboral Cursos de habilitación laboral", "6.1", "5",  "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        
 TypedTableModel a4 = new TypedTableModel();
        a4.addColumn("componente_plan_estudio", String.class);
        a4.addColumn("p1", String.class);
        a4.addColumn("p2", String.class);
        a4.addColumn("p3", String.class);
        a4.addColumn("p4", String.class);
        a4.addColumn("ni", String.class);
        a4.addColumn("pp", String.class);
        a4.addColumn("pps", String.class);
        a4.addColumn("sp", String.class);
        a4.addColumn("sps", String.class);
        a4.addColumn("nf", String.class);
        a4.addColumn("resultado", String.class);
        a4.addRow("Ciencias naturales", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Matemática", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a4.addRow("Cursos de habilitación laboral", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        
        TypedTableModel a5 = new TypedTableModel();
        a5.addColumn("componente_plan_estudio", String.class);
        a5.addColumn("p1", String.class);
        a5.addColumn("p2", String.class);
        a5.addColumn("p3", String.class);
        a5.addColumn("p4", String.class);
        a5.addColumn("p5", String.class);
        a5.addColumn("ni", String.class);
        a5.addColumn("pp", String.class);
        a5.addColumn("pps", String.class);
        a5.addColumn("sp", String.class);
        a5.addColumn("sps", String.class);
        a5.addColumn("nf", String.class);
        a5.addColumn("resultado", String.class);
        a5.addRow("Ciencias naturales", "6.1", "5", "6.1", "5", "5", "MBB", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a5.addRow("Matemática", "6.1", "5", "6.1", "5", "6.1", "5", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        a5.addRow("Cursos de habilitación laboral", "6.1", "5", "5", "6.1", "5", "6.1", "5", "6.1", "5", "6.1", "5", "ARPOBADO");
        
        
        TypedTableModel calificaciones = new TypedTableModel();
        calificaciones.addColumn("cant_periodos", Integer.class);
        calificaciones.addColumn("cal_periodos", TypedTableModel.class);
        calificaciones.addRow(3, a3);
        calificaciones.addRow(4, a4);
        calificaciones.addRow(5, a5);

        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("grado", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("nombre", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("asistencias", Integer.class);
        model.addColumn("inasistencias_j", Integer.class);
        model.addColumn("inasistencias_sj", Integer.class);
        model.addColumn("render_calificaciones_header", Boolean.class);
        model.addColumn("calificaciones", TypedTableModel.class);        
        model.addRow("2", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR", "PRIMERO","A - Matutino", "Luis Juan Pérez", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA",1,2,3, true, calificaciones);
        model.addRow("33332", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR","PRIMERO", "A - Matutino", "Roberto González", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA",1,2,3, true, calificaciones);
        model.addRow("11111", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR", "PRIMERO","A - Matutino", "Federico Andrés Pérez", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA",1,2,3, true, calificaciones);
        
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        //parameters.put("Report Title", "Simple Embedded Report Example with Parameters");
        //parameters.put("Col Headers BG Color", "yellow"); 
        parameters.put("nombre_director", "Ismael Quijada Cardoza");
        parameters.put("nombre_encargado", "Evelyn Audelia Alemán");
        parameters.put("sello_director_path", "c:/sello");
        parameters.put("firma_director_path", "c:/indice");
        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(CalificacionesEstudiantesTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new CalificacionesEstudiantesTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
