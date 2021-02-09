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

public class AsistenciasEstudiantesTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public AsistenciasEstudiantesTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("asistencias_estudiantes.prpt");

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
               
        
        
        TypedTableModel a4 = new TypedTableModel();
        a4.addColumn("nie", String.class);
        a4.addColumn("nombre", String.class);
        a4.addColumn("1", String.class);
        a4.addColumn("2", String.class);
        a4.addColumn("3", String.class);
        a4.addColumn("4", String.class);
        a4.addColumn("5", String.class);
        a4.addColumn("6", String.class);
        a4.addColumn("7", String.class);
        a4.addColumn("8", String.class);
        a4.addColumn("9", String.class);
        a4.addColumn("10", String.class);
        a4.addColumn("11", String.class);
        a4.addColumn("12", String.class);
        a4.addColumn("13", String.class);
        a4.addColumn("14", String.class);
        a4.addColumn("15", String.class);
        a4.addColumn("16", String.class);
        a4.addColumn("17", String.class);
        a4.addColumn("18", String.class);
        a4.addColumn("19", String.class);
        a4.addColumn("20", String.class);
        a4.addColumn("21", String.class);
        a4.addColumn("22", String.class);
        a4.addColumn("23", String.class);
        a4.addColumn("24", String.class);
        a4.addColumn("25", String.class);
        a4.addColumn("26", String.class);
        a4.addColumn("27", String.class);
        a4.addColumn("28", String.class);
        a4.addColumn("29", String.class);
        a4.addColumn("30", String.class);
        a4.addColumn("31", String.class);
        a4.addColumn("A", String.class);
        a4.addColumn("II", String.class);
        a4.addColumn("IJ", String.class);
        a4.addColumn("IT", String.class);
        a4.addColumn("T", String.class);
        a4.addRow("332112", "LÓPEZ NICOLÁS", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");

        
        
        TypedTableModel a5 = new TypedTableModel();
        a5.addColumn("nie", String.class);
        a5.addColumn("nombre", String.class);
        a5.addColumn("1", String.class);
        a5.addColumn("2", String.class);
        a5.addColumn("3", String.class);
        a5.addColumn("4", String.class);
        a5.addColumn("5", String.class);
        a5.addColumn("6", String.class);
        a5.addColumn("7", String.class);
        a5.addColumn("8", String.class);
        a5.addColumn("9", String.class);
        a5.addColumn("10", String.class);
        a5.addColumn("11", String.class);
        a5.addColumn("12", String.class);
        a5.addColumn("13", String.class);
        a5.addColumn("14", String.class);
        a5.addColumn("15", String.class);
        a5.addColumn("16", String.class);
        a5.addColumn("17", String.class);
        a5.addColumn("18", String.class);
        a5.addColumn("19", String.class);
        a5.addColumn("20", String.class);
        a5.addColumn("21", String.class);
        a5.addColumn("22", String.class);
        a5.addColumn("23", String.class);
        a5.addColumn("24", String.class);
        a5.addColumn("25", String.class);
        a5.addColumn("26", String.class);
        a5.addColumn("27", String.class);
        a5.addColumn("28", String.class);
        a5.addColumn("29", String.class);
        a5.addColumn("30", String.class);
        a5.addColumn("31", String.class);
        a5.addColumn("A", String.class);
        a5.addColumn("II", String.class);
        a5.addColumn("IJ", String.class);
        a5.addColumn("IT", String.class);
        a5.addColumn("T", String.class);
        a5.addRow("332112", "LÓPEZ NICOLÁS", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");
        a5.addRow("20004264", "BOSWORTH, BENJAMIN", "-", "-", "N", "N", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "+", "+", "+", "+", "/", "+", "+", "+", "+", "+", "+", "24", "2", "0", "2", "26");


        TypedTableModel model = new TypedTableModel();
        model.addColumn("mes", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("grado", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("asistencias", TypedTableModel.class);    
        model.addColumn("cant_dias_mes", Integer.class);
        //model.addRow("Agosto", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR","PRIMERO", "A - Matutino", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a4, 28);
        //model.addRow("Agosto", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR","PRIMERO", "A - Matutino",  "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a4, 29);
        //model.addRow("Agosto", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR","PRIMERO", "A - Matutino", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a4, 30);
        model.addRow("Septiembre", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR", "PRIMERO","A - Matutino", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a5, 31);
        
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
        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(AsistenciasEstudiantesTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new AsistenciasEstudiantesTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
