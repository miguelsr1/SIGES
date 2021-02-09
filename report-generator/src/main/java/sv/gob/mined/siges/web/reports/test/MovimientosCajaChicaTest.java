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

public class MovimientosCajaChicaTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public MovimientosCajaChicaTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("libroCajaChica.prpt");

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
        model.addColumn("libroCaja", TypedTableModel.class);

        TypedTableModel cajaMes = new TypedTableModel();
        cajaMes.addColumn("cajaMes", TypedTableModel.class);

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
        libroBancoMes.addRow("01/02/2020", "4736568734344", "CDE", "Colaboracion padres de familia", 500.00, "", 500.00);
        libroBancoMes.addRow("20/02/2020", "4736568734474", "Museo Tin Marin", "Visita 5to Grado", "", 325.47, 174.53);
        libroBancoMes.addRow("28/02/2020", "8582577933856", "Ferreteria EL Triunfo", "Compra de lamparas", "", 147.44, 27.09);

        //FEBRERO
        libroBancoMes2.addRow("01/03/2020", "11365687432347", "Direccion Escoñar", "Venta de Libros", 710.50, "", 737.59);
        libroBancoMes2.addRow("22/03/2020", "4022171912061", "Almacenes Vidri", "Pintura  Azul", "", 45.50, 692.09);

        //MARZO
        libroBancoMes3.addRow("10/04/2020", "3962689337400", "Transportes Gacela", "Trasnsporte visita Termos", "", 445.50, 246.59);

        //ABRIL
        libroBancoMes4.addRow("10/05/2020", "9004955515040", "Industrias Alimenticias S.A. de C.V", "Alimentacion dia de la madre", "", 174.54, 72.05);

        cajaMes.addRow(libroBancoMes);

        cajaMes.addRow(libroBancoMes2);

        cajaMes.addRow(libroBancoMes3);

        cajaMes.addRow(libroBancoMes4);

        model.addRow(cajaMes);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("NoCajaChica", "0001234567");
        parameters.put("banco", "Banco Agricola");
        parameters.put("anio", "2020");

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(MovimientosCajaChicaTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new MovimientosCajaChicaTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
