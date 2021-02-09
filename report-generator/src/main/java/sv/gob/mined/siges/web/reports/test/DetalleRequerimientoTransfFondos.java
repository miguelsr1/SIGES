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

public class DetalleRequerimientoTransfFondos extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public DetalleRequerimientoTransfFondos() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("detalleRequerimientoTransfFondos_v4.prpt");

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

//    public DataFactory getDataFactory() {
//
//        TypedTableModel model = new TypedTableModel();
//
//        model.addColumn("codigo", String.class);
//        model.addColumn("nombre", String.class);
//        model.addColumn("monto", String.class);
//
//        model.addRow("4870", "CDE CENTRO ESCOLAR \"SANTA MARIA\"", "617.46");
//        model.addRow("14877", "CDE, CENTRO ESCOLAR \"JUAN PABLO RODRIGUEZ ALFARO\" ", "516.32");
//        model.addRow("60149", "CDE CENTRO ESCOLAR CANTÓN TEHUISTE ABAJO ", "733.04");
//        model.addRow("60230", "CDE CENTRO ESCOLAR \"BELEN VILLA PALESTINA\" ", "1,333.21");
//        model.addRow("4870", "CDE CENTRO ESCOLAR \"SANTA MARIA\"", "617.46");
//
//
//        TableDataFactory dataFactory = new TableDataFactory();
//        dataFactory.addTable("param-query", model);
//        return dataFactory;
//    }
    
    public DataFactory getDataFactory() {
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("detalle", TypedTableModel.class);
        
        TypedTableModel detalle = new TypedTableModel();
        detalle.addColumn("sed_codigo", String.class);
        detalle.addColumn("sed_nombre", String.class);
        detalle.addColumn("fondo", BigDecimal.class);
        detalle.addColumn("prestamo", BigDecimal.class);
        detalle.addColumn("donaciones", BigDecimal.class);
 
        detalle.addRow(
                "ddd",
                "CDE CENTRO ESCOLAR \"SANTA MARIA\"",
                5000.00,
                6.00,
                6000.00);
        
        detalle.addRow(
                "4871",
                "CDE CENTRO ESCOLAR \"SANTA MARIA 2\"",
                5000.00,
                7.00,
                6000.00);
        
        
        model.addRow(detalle);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("concepto", "OTRAS TRANSFERENCIAS AL CENTRO EDUCATIVO");
        parameters.put("num_transf", "18");
        parameters.put("num_requerimiento", "B08-01965-19");
        parameters.put("fecha_elaboracion", "28/11/2019");
        parameters.put("componente", "PROYECTO ADICIONAL");
        parameters.put("subcomponente", "PROYECTO ADICIONAL: MANTENIMIENTO DE MOBILIARIO-MEDIA FOMILENIO II 2019");
        parameters.put("convenio", "CONVENIO");
        parameters.put("proyecto", "PROYECTO");
        parameters.put("componente_convenio", "N/A");
        parameters.put("subcomponente_convenio", "N/A");
        parameters.put("cat_gasto_convenio", "N/A");
        parameters.put("unidad_linea", "06 01 62303 ATENCION A EDUCACION MEDIA");
        parameters.put("nombre_director", "CANDIDO ALBERTO PALACIOS");
        parameters.put("nombre_pagador", "CARLOS RUBEN CARCAMO");
        parameters.put("cargo_director", "cargo director");
        parameters.put("cargo_pagador", "cargo pagador");
        parameters.put("departamento", "SAN SALVADOR");
        parameters.put("nit_departamental", "0821-0301961018");
        parameters.put("num_oae", "2");
        parameters.put("datos_convenio", "SI");

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(DetalleRequerimientoTransfFondos.class.getSimpleName() + ".pdf");

        // Generate the report
        new DetalleRequerimientoTransfFondos().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
