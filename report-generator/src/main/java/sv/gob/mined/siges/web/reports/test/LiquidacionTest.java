package sv.gob.mined.siges.web.reports.test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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

public class LiquidacionTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public LiquidacionTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("autoLiquidacion.prpt");

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
        model.addColumn("tablaMontoTransferido1", TypedTableModel.class);
        model.addColumn("tablaMontoTransferido2", TypedTableModel.class);
        
        TypedTableModel tablaMontoTransferido1 = new TypedTableModel();
        tablaMontoTransferido1.addColumn("conceptoDepositado", String.class);
        tablaMontoTransferido1.addColumn("cantidadDepositado", BigDecimal.class);
//        
        TypedTableModel tablaMontoTransferido2 = new TypedTableModel();
        tablaMontoTransferido2.addColumn("conceptoInvertido", String.class);
        tablaMontoTransferido2.addColumn("cantidadInvertido", BigDecimal.class);
        
        tablaMontoTransferido1.addRow("DEPOSITO 1",5000.00);
        tablaMontoTransferido1.addRow("DEPOSITO 2",1000.00);
        tablaMontoTransferido2.addRow("PAGO 1", 5000.00);
        
        
        model.addRow(tablaMontoTransferido1,tablaMontoTransferido2);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("anio", "2021");
        parameters.put("componente", "Gratuidad de la Educación Media");
        parameters.put("subcomponente", "Funcionamiento");
        parameters.put("fuenteFinanciamiento", "GOES,BID");
        parameters.put("tipoOrganismo", "Tipo");
        parameters.put("codigoInfraestructura", "10027");
        parameters.put("nombreCentroEducativo", "COMPLEJO EDUCATIVO \"DOCTOR RAFAEL MEZA DELGADO\"");
        parameters.put("nombreOAE", "PROYECTO");
        parameters.put("direccion", "N/A");
        parameters.put("departamento", "N/A");
        parameters.put("canton", "N/A");
        parameters.put("caserio", "06 01 62303 ATENCION A EDUCACION MEDIA");
        parameters.put("telefono", "CANDIDO ALBERTO PALACIOS");
        parameters.put("nit", "CARLOS RUBEN CARCAMO");
        parameters.put("isss", "cargo director");
        parameters.put("nombreBanco", "cargo pagador");
        parameters.put("NoCuenta", "SAN SALVADOR");
        parameters.put("nombreCuenta", "0821-0301961018");
        
        parameters.put("nombrePresidente", "2");
        parameters.put("nombreTesorero", "2");
        parameters.put("nombreConsejal", "2");
        parameters.put("observaciones", "2");
        parameters.put("componenteLiquidado", "2");
        parameters.put("fechaLiquidacion", "2");

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(LiquidacionTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new LiquidacionTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
