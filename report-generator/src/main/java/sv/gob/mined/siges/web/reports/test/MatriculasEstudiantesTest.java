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

public class MatriculasEstudiantesTest extends AbstractReportGenerator {

    public MatriculasEstudiantesTest() {
    }


    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("matriculas_seccion.prpt");

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
               
        
        TypedTableModel a = new TypedTableModel();
        a.addColumn("nro", String.class);
        a.addColumn("nie", String.class);
        a.addColumn("nombre_estudiante", String.class);
        a.addColumn("estado", String.class);
        a.addColumn("observacion", String.class);
        a.addRow("1", "6131245", "AGREDA CAMPOS, ROCÍO ALEJANDRA", "PROVISIONAL", "PENDIENTE VALIDACIÓN");
        a.addRow("2", "456456788", "AGUILAR MARROQUÍN, ADRIANA MICHELLE", "PROVISIONAL", "PENDIENTE PARTIDA DE NACIMIENTO");
        a.addRow("3", "123455", "BARILLAS MENDOZA, JACQUELINNE TATIANA", "MATRICULADO", "");

        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("direccion", String.class);
        model.addColumn("telefono", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("nombre", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("estudiantes", TypedTableModel.class);
        model.addRow("2132131", "1001 - Centro Escolar Isidro Menéndez", "CALLE AL VOLCAN, FRENTE AL HOSPITAL NACIONAL SANTA TERESA, BARRIO EL CALVARIO, ZACATECOLUCA, LA PAZ", "45454654-8", "2018", "PL2015 - GENERAL - REGULAR", "A - Matutino", "Luis Juan Pérez", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a);
        model.addRow("33332", "1001 - Centro Escolar Isidro Menéndez", "CALLE AL VOLCAN, FRENTE AL HOSPITAL NACIONAL SANTA TERESA, BARRIO EL CALVARIO, ZACATECOLUCA, LA PAZ", "45454654-8", "2018", "PL2015 - GENERAL - REGULAR", "A - Matutino", "Roberto González", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a);
        model.addRow("11111", "1001 - Centro Escolar Isidro Menéndez", "CALLE AL VOLCAN, FRENTE AL HOSPITAL NACIONAL SANTA TERESA, BARRIO EL CALVARIO, ZACATECOLUCA, LA PAZ", "45454654-8", "2018", "PL2015 - GENERAL - REGULAR", "A - Matutino", "Federico Andrés Pérez", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a);
        
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
        final File outputFilename = new File(MatriculasEstudiantesTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new MatriculasEstudiantesTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
