package sv.gob.mined.siges.web.reports.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;

public class PromocionesEstudiantesTest extends AbstractReportGenerator {

    public PromocionesEstudiantesTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("promociones_estudiantes.prpt");

            // Parse the report file
            final ResourceManager resourceManager = new ResourceManager();
            final Resource directly = resourceManager.createDirectly(reportDefinitionURL, MasterReport.class);
            return (MasterReport) directly.getResource();
        } catch (ResourceException e) {
        }
        return null;
    }

    public DataFactory getDataFactory() {

        TypedTableModel a = new TypedTableModel();
        a.addColumn("componente_plan_estudio", String.class);
        a.addColumn("horas_semanales", String.class);
        a.addColumn("calificacion_final", String.class);
        a.addColumn("resultado", String.class);
        a.addRow("Ciencias naturales", "6", "7.0", "ARPOBADO");
        a.addRow("Matemática", "4", "5.0", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        a.addRow("Cursos de habilitación laboral", "2", "MB", "ARPOBADO");
        

        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("nombre", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("calificaciones", TypedTableModel.class);
        model.addRow("2132131", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR", "A - Matutino", "Luis Juan Pérez", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a);
        model.addRow("33332", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR", "A - Matutino", "Roberto González", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a);
        model.addRow("11111", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR", "A - Matutino", "Federico Andrés Pérez", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a);

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("nombre_director", "Ismael Quijada Cardoza");
        parameters.put("nombre_encargado", "Evelyn Audelia Alemán");
        parameters.put("departamento", "Santa Ana");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
        parameters.put("fecha_emision", formatter.format(LocalDate.now()));

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(PromocionesEstudiantesTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new PromocionesEstudiantesTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
