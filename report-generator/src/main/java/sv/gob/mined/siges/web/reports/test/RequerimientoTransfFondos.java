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
import sv.gob.mined.siges.web.utilidades.NumberToLetterConverter;

public class RequerimientoTransfFondos extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public RequerimientoTransfFondos() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("requerimientoTransfFondos.prpt");

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
        model.addColumn("detalle", TypedTableModel.class);
        //model.addColumn("datos_convenio", TypedTableModel.class);
        
        TypedTableModel detalle = new TypedTableModel();
        //TypedTableModel datos_convenio = new TypedTableModel();
        
        detalle.addColumn("unidad", String.class);
        detalle.addColumn("linea", String.class);
        detalle.addColumn("fuente_financiamiento", String.class);
        detalle.addColumn("monto_autorizado", BigDecimal.class);
        
        detalle.addRow("04","0402-62303 ATENCION A EDUCACIÓN PARVULARIA","Fondo General",685.00);
        detalle.addRow("04","0402-62303 ATENCION A EDUCACIÓN PARVULARIA","Préstamo",665.00);
        
        model.addRow(detalle);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("concepto", "OTRAS TRANSFERENCIAS AL CENTRO EDUCATIVO 2019L");
        parameters.put("num_requerimiento", "B08-01965-19");
        parameters.put("fecha_elaboracion", "28/11/2019 06:00");
        parameters.put("cantidad_solicitada", NumberToLetterConverter.convertNumberToLetter(new BigDecimal("68995.07"), Boolean.TRUE).trim().toUpperCase());
        parameters.put("componente", "PROYECTO ADICIONAL");
        parameters.put("subcomponente", "PROYECTO ADICIONAL: MANTENIMIENTO DE MOBILIARIO-MEDIA\n"+ "FOMILENIO II 2019");
//        parameters.put("convenio", "CONVENIO");
//        parameters.put("proyecto", "PROYECTO");
//        parameters.put("componente_convenio", "N/A");
//        parameters.put("subcomponente_convenio", "N/A");
//        parameters.put("cat_gasto_convenio", "N/A");
        //parameters.put("cantidad_solicitada", "SESENTA Y OCHO MIL NOVECIENTOS NOVENTA Y CINCO 07/100 DOLARES");
        parameters.put("datos_convenio", "SI");
        parameters.put("banco", "BANCO AGRICOLA S.A.");
        parameters.put("num_cuenta", "590-055547-5");
        parameters.put("nombre_director", "CANDIDO ALBERTO PALACIOS");
        parameters.put("nombre_pagador", "CARLOS RUBEN CARCAMO");
        parameters.put("cargo_director", "cargo director");
        parameters.put("cargo_pagador", "cargo pagador");
        parameters.put("departamento", "SAN SALVADOR");
        parameters.put("nit_departamental", "0821-0301961018");

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(RequerimientoTransfFondos.class.getSimpleName() + ".pdf");

        // Generate the report
        new RequerimientoTransfFondos().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
