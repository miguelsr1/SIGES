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

public class MatriculasEnSedeGeneratorTestOld extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public MatriculasEnSedeGeneratorTestOld() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("matriculas_en_sede_old.prpt");

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
        grados.addColumn("modalidad_educativa", String.class);
        grados.addColumn("modalidad_atencion", String.class);
        grados.addColumn("grado", String.class);
        grados.addColumn("cant_masculino", Long.class);
        grados.addColumn("cant_femenino", Long.class);
        grados.addColumn("total", Long.class);
        
        grados.addRow("ME", "Regular", "Primer Grado", 10L, 11L, 5L);
        grados.addRow("ME", "Regular", "Primer Grado", 10L, 11L, 8L);
        grados.addRow("ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        grados.addRow("ME", "Regular", "Primer Grado", 10L, 11L, 10L);
        
        
        TypedTableModel ciclos = new TypedTableModel();
        ciclos.addColumn("ciclo", String.class);
        ciclos.addColumn("cant_masculino", Long.class);
        ciclos.addColumn("cant_femenino", Long.class);
        ciclos.addColumn("total", Long.class);
        ciclos.addColumn("grados", TypedTableModel.class);        
        ciclos.addRow("Ciclo 1", 50L, 40L, 90L, grados);
        ciclos.addRow("Ciclo 2", 50L, 40L, 90L, grados);
        ciclos.addRow("Ciclo 3", 50L, 40L, 90L, grados);
        
        TypedTableModel ciclos2 = new TypedTableModel();
        ciclos2.addColumn("ciclo", String.class);
        ciclos2.addColumn("cant_masculino", Long.class);
        ciclos2.addColumn("cant_femenino", Long.class);
        ciclos2.addColumn("total", Long.class);
        ciclos2.addColumn("grados", TypedTableModel.class);        
        ciclos2.addRow("Ciclo 4", 50L, 40L, 90L, grados);

        TypedTableModel niveles = new TypedTableModel();
        niveles.addColumn("nivel", String.class);
        niveles.addColumn("cant_masculino", Long.class);
        niveles.addColumn("cant_femenino", Long.class);
        niveles.addColumn("total", Long.class);
        niveles.addColumn("ciclos", TypedTableModel.class);
        niveles.addRow("Nivel 2", 50L, 40L, 90L, ciclos2);        
        niveles.addRow("Nivel 1", 50L, 40L, 90L, ciclos);
        niveles.addRow("Nivel 3", 50L, 40L, 90L, ciclos);
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("sede", String.class);
        model.addColumn("departamento", String.class);
        model.addColumn("municipio", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("niveles", TypedTableModel.class);    
        model.addRow("1001 - Centro Escolar Isidro Menéndez", "San Salvador", "San Salvador", "2019", niveles);
        
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
        final File outputFilename = new File(MatriculasEnSedeGeneratorTestOld.class.getSimpleName() + ".pdf");

        // Generate the report
        new MatriculasEnSedeGeneratorTestOld().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
