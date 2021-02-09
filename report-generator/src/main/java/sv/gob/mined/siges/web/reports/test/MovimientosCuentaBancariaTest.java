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

public class MovimientosCuentaBancariaTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public MovimientosCuentaBancariaTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("libroDeBanco.prpt");

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
        model.addColumn("libroBanco", TypedTableModel.class);

        TypedTableModel libroBanco = new TypedTableModel();
        libroBanco.addColumn("libroBancoMes", TypedTableModel.class);

        TypedTableModel libroBancoMes = new TypedTableModel();

        libroBancoMes.addColumn("fecha", String.class);
        libroBancoMes.addColumn("chequeNo", String.class);
        libroBancoMes.addColumn("aNombreDe", String.class);
        libroBancoMes.addColumn("enConceptoDe", String.class);
        libroBancoMes.addColumn("ingreso", BigDecimal.class);
        libroBancoMes.addColumn("gasto", BigDecimal.class);
        libroBancoMes.addColumn("saldo", BigDecimal.class);

        TypedTableModel libroBancoMes2 = new TypedTableModel();

        libroBancoMes2.addColumn("fecha", String.class);
        libroBancoMes2.addColumn("chequeNo", String.class);
        libroBancoMes2.addColumn("aNombreDe", String.class);
        libroBancoMes2.addColumn("enConceptoDe", String.class);
        libroBancoMes2.addColumn("ingreso", BigDecimal.class);
        libroBancoMes2.addColumn("gasto", BigDecimal.class);
        libroBancoMes2.addColumn("saldo", BigDecimal.class);

        TypedTableModel libroBancoMes3 = new TypedTableModel();

        libroBancoMes3.addColumn("fecha", String.class);
        libroBancoMes3.addColumn("chequeNo", String.class);
        libroBancoMes3.addColumn("aNombreDe", String.class);
        libroBancoMes3.addColumn("enConceptoDe", String.class);
        libroBancoMes3.addColumn("ingreso", BigDecimal.class);
        libroBancoMes3.addColumn("gasto", BigDecimal.class);
        libroBancoMes3.addColumn("saldo", BigDecimal.class);

        TypedTableModel libroBancoMes4 = new TypedTableModel();

        libroBancoMes4.addColumn("fecha", String.class);
        libroBancoMes4.addColumn("chequeNo", String.class);
        libroBancoMes4.addColumn("aNombreDe", String.class);
        libroBancoMes4.addColumn("enConceptoDe", String.class);
        libroBancoMes4.addColumn("ingreso", BigDecimal.class);
        libroBancoMes4.addColumn("gasto", BigDecimal.class);
        libroBancoMes4.addColumn("saldo", BigDecimal.class);

        //ENERO
        libroBancoMes.addRow("01/02/2020", "1206141719022", "CENTRO ESCOLAR \"1° DE JULIO DE 1823\"", "Transferencia MINED", 900.00, "", 900.00);
        libroBancoMes.addRow("20/02/2020", "4736568734474", "Museo Tin Marin", "Visita 5to Grado", "", 325.47, 574.53);
        libroBancoMes.addRow("28/02/2020", "8582577933856", "Ferreteria EL Triunfo", "Compra de lamparas", "", 147.44, 427.09);

        //FEBRERO
        libroBancoMes2.addRow("22/03/2020", "4022171912061", "Almacenes Vidri", "Pintura  Azul", "", 45.50, 381.59);

        //MARZO
        libroBancoMes3.addRow("10/04/2020", "3962689337400", "Transportes Gacela", "Transporte visita Termos", "", 300.10, 81.49);

        //ABRIL
        libroBancoMes4.addRow("10/05/2020", "9004955515040", "Industrias Alimenticias S.A. de C.V", "Alimentacion dia de la madre", "", 81.00, 0.49);

        libroBanco.addRow(libroBancoMes);

        libroBanco.addRow(libroBancoMes2);

        libroBanco.addRow(libroBancoMes3);

        libroBanco.addRow(libroBancoMes4);

        model.addRow(libroBanco);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("NoCuenta", "0001234567");
        parameters.put("banco", "Banco Agricola");
        parameters.put("anio", "2020");

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(MovimientosCuentaBancariaTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new MovimientosCuentaBancariaTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
