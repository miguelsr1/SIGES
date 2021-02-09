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

public class BienesActivoFijoTest extends AbstractReportGenerator {
    
    /**
     * Default constructor for this sample report generator
     */
    public BienesActivoFijoTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("listadoBienes.prpt");

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
               
        
        //TypedTableModel a3 = new TypedTableModel();
        TypedTableModel a3 = new TypedTableModel();
        a3.addColumn("codigo_inventario", String.class);
        a3.addColumn("descripcion", String.class);
        a3.addColumn("marca", String.class);
        a3.addColumn("modelo", String.class);
        a3.addColumn("serie", String.class);
        a3.addColumn("fecha_adquisicion", String.class);
        a3.addColumn("valor_adquisicion", String.class);
        a3.addColumn("calidad", String.class);
        a3.addColumn("asignado_a", String.class);
        a3.addRow("01-1130-001", "GAVETERO DE MADERACOLOR CAFÉ, 6 GAVETAS, 2","N/A", "S/M", "S/S", "07/10/2004", "$ 937.14", "BUENO", "");
        a3.addRow("01-1131-001", "COMPUTADORA DE MADERA CON FORMICA COLOR CAFE", "N/A", "", "", "03/02/1999", "$ 57.14", "BUENO", "");
        a3.addRow("01-1138-002", "MESA DE MADERA COLOR CAFÉ, CON BASE DE VIDRIO", "N/A",  "", "S/S", "03/11/2004", "$ 1,000.00 ", "BUENO", "LIC. GALVEZ");
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("bienes", TypedTableModel.class); 
        //model.addRow("Agosto", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR","PRIMERO", "A - Matutino", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a4, 28);
        //model.addRow("Agosto", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR","PRIMERO", "A - Matutino",  "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a4, 29);
        //model.addRow("Agosto", "1001 - Centro Escolar Isidro Menéndez", "2018", "PL2015 - GENERAL - REGULAR","PRIMERO", "A - Matutino", "EDUCACIÓN MEDIA - TÉCNICO VOCACIONAL - ACUICULTURA - MEGATEC - FELXIBLE - NOCTURNA", a4, 30);
        model.addRow(a3, 1);
        
        
        
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("unidad_AF", "MINED CENTRAL");
        parameters.put("unidad_AD", "01 - DESPACHO MINISTERIAL");
        parameters.put("firma_director_path", "");
        parameters.put("nombre_director", "DIRECTOR MINED CENTRAL");
        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(BienesActivoFijoTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new BienesActivoFijoTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
