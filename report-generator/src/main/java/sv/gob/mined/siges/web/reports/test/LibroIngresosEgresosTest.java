package sv.gob.mined.siges.web.reports.test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.engine.classic.core.wizard.RelationalAutoGeneratorPreProcessor;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class LibroIngresosEgresosTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public LibroIngresosEgresosTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("libroIngresoEgreso.prpt");

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
        model.addColumn("libro", TypedTableModel.class);

        TypedTableModel libroMes = new TypedTableModel();
        libroMes.addColumn("libroMes", TypedTableModel.class);

        TypedTableModel libroBancoMes2 = new TypedTableModel();

        libroBancoMes2.addColumn("fecha", String.class);
        libroBancoMes2.addColumn("referencia", String.class);
        libroBancoMes2.addColumn("concepto", String.class);
        libroBancoMes2.addColumn("ingreso", BigDecimal.class);
        libroBancoMes2.addColumn("gasto", BigDecimal.class);
        libroBancoMes2.addColumn("saldo", BigDecimal.class);

        TypedTableModel libroBancoMes3 = new TypedTableModel();

        libroBancoMes3.addColumn("fecha", String.class);
        libroBancoMes3.addColumn("referencia", String.class);
        libroBancoMes3.addColumn("concepto", String.class);
        libroBancoMes3.addColumn("ingreso", BigDecimal.class);
        libroBancoMes3.addColumn("gasto", BigDecimal.class);
        libroBancoMes3.addColumn("saldo", BigDecimal.class);

        TypedTableModel libroBancoMes4 = new TypedTableModel();

        libroBancoMes4.addColumn("fecha", String.class);
        libroBancoMes4.addColumn("referencia", String.class);
        libroBancoMes4.addColumn("concepto", String.class);
        libroBancoMes4.addColumn("ingreso", BigDecimal.class);
        libroBancoMes4.addColumn("gasto", BigDecimal.class);
        libroBancoMes4.addColumn("saldo", BigDecimal.class);

        //ENERO
        //FEBRERO
        libroBancoMes2.addRow("01/03/2020", "4022171912061", "Transferencia Subcomponente", 900.00, "", 900.00);
        libroBancoMes2.addRow("22/03/2020", "4022171912061", "Pintura Aceite Azul", "", 45.50, 854.50);
        libroBancoMes2.addRow("28/03/2020", "8582577933856", "Compra de lamparas", "", 147.44, 707.06);

        //MARZO
        libroBancoMes3.addRow("10/04/2020", "3962689337400", "Trasnsporte Excursion Termos", "", 445.50, 261.56);

        //ABRIL
        libroBancoMes4.addRow("10/05/2020", "9004955515040", "Alimentacion dia de la madre", "", 150.41, 111.15);

        libroMes.addRow(libroBancoMes2);

        libroMes.addRow(libroBancoMes3);

        libroMes.addRow(libroBancoMes4);

        model.addRow(libroMes);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("nombreTransferencia", "Funcionamiento");
        parameters.put("fuenteFinanciamiento", "GOES/PEIS");
        parameters.put("anio", "2020");
        parameters.put("ingresoMes", "2020");

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(LibroIngresosEgresosTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new LibroIngresosEgresosTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
