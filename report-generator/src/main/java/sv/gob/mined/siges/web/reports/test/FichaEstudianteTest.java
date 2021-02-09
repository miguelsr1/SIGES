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

public class FichaEstudianteTest extends AbstractReportGenerator {

    /**
     * Default constructor for this sample report generator
     */
    public FichaEstudianteTest() {
    }

    public MasterReport getReportDefinition() {
        try {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = classloader.getResource("ficha_estudiante.prpt");

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

        model.addColumn("familiares", TypedTableModel.class);
        model.addColumn("matriculas", TypedTableModel.class);
        model.addColumn("titulos", TypedTableModel.class);

        TypedTableModel familiares = new TypedTableModel();

        familiares.addColumn("identificaciones", String.class);
        familiares.addColumn("primer_nombre", String.class);
        familiares.addColumn("segundo_nombre", String.class);
        familiares.addColumn("primer_apellido", String.class);
        familiares.addColumn("segundo_apellido", String.class);
        familiares.addColumn("parentesco", String.class);
        familiares.addColumn("vive_con", String.class);
        familiares.addColumn("referente", String.class);
        familiares.addColumn("estado", String.class);
        familiares.addColumn("correo_electronico", String.class);
        familiares.addColumn("telefonos", String.class);
        familiares.addColumn("zona", String.class);
        familiares.addColumn("departamento", String.class);
        familiares.addColumn("municipio", String.class);
        familiares.addColumn("direccion", String.class);
        familiares.addColumn("profesion", String.class);
        familiares.addColumn("escolaridad", String.class);
        familiares.addColumn("ocupacion", String.class);
        familiares.addColumn("familiar_index", String.class);

        familiares.addRow(
                "DUI: 878989",
                "Violeta",
                "Del Carmen",
                "García",
                "Pérez",
                "Madre",
                "Sí",
                "Sí",
                "Vive",
                "madre@hotmail.com",
                "+598665645",
                "Rural",
                "SS",
                "SS",
                "Dirección",
                "Profesión",
                "Escolaridad",
                "Ocupación",
                "1"
        );

        familiares.addRow(
                "DUI: 878989",
                "Roberto",
                "José",
                "García",
                "Pérez",
                "Padre",
                "Sí",
                "Sí",
                "Vive",
                "padre@hotmail.com",
                "+598665645",
                "Rural",
                "SS",
                "SS",
                "Dirección",
                "Profesión",
                "Escolaridad",
                "Ocupación",
                "2"
        );

        TypedTableModel matriculas = new TypedTableModel();
        matriculas.addColumn("mat_centro", String.class);
        matriculas.addColumn("mat_anio", String.class);
        matriculas.addColumn("mat_nivel", String.class);
        matriculas.addColumn("mat_grado", String.class);
        matriculas.addColumn("mat_seccion", String.class);
        matriculas.addColumn("mat_promocion", String.class);

        matriculas.addRow(
                "CENTRO ESCOLAR CATOLICO \" SANTA CATALINA\"",
                "2012",
                "Educación Básica",
                "NOVENO GRADO",
                "B",
                ""
        );
        
        matriculas.addRow(
                "CENTRO ESCOLAR CATOLICO \" SANTA CATALINA\"",
                "2013",
                "Educación Media",
                "PRIMER AÑO",
                "B",
                "Promovido"
        );
        
        TypedTableModel titulos = new TypedTableModel();
        titulos.addColumn("tit_centro", String.class);
        titulos.addColumn("tit_anio", String.class);
        titulos.addColumn("tit_fecha_emision", String.class);
        titulos.addColumn("tit_fecha_validez", String.class);
        titulos.addColumn("tit_titulo_obtenido", String.class);
        titulos.addColumn("tit_nro_registro", String.class);
        titulos.addColumn("tit_reposicion", String.class);

        titulos.addRow(
                "CENTRO ESCOLAR CATOLICO \" SANTA CATALINA\"",
                "2012",
                "25/04/2019",
                "25/04/2050",
                "BACHILLER GENERAL",
                "45648",
                "Repos"
        );
        
        titulos.addRow(
                "CENTRO ESCOLAR CATOLICO",
                "2013",
                "25/04/2019",
                "25/04/2050",
                "BACHILLER GENERAL",
                "45648",
                "Repos"
        );
        

        model.addRow(familiares, matriculas, titulos);

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    public Map<String, Object> getReportParameters() {
        final Map parameters = new HashMap<String, Object>();
        parameters.put("codigo_sede", "88057");
        parameters.put("nombre_sede", "CENTRO ESCOLAR CATOLICO \" SANTA CATALINA\"");
        parameters.put("identificaciones", "NIE: 4208370, DUI: 4545544,  Pasaporte: 45645644 Andorra");
        parameters.put("primer_nombre", "Violeta");
        parameters.put("segundo_nombre", "Alejandra");
        parameters.put("tercer_nombre", "Tercer nombre");
        parameters.put("primer_apellido", "Girón");
        parameters.put("segundo_apellido", "García");
        parameters.put("tercer_apellido", "Tercer apellido");
        parameters.put("nombre_partida", "Violeta Alejandra Girón García");
        parameters.put("nacionalidad", "Salvadoreña");
        parameters.put("fecha_nacimiento", "25/04/1995");
        parameters.put("naturalizado", "Sí");
        parameters.put("estado_familiar", "Soltero");
        parameters.put("discapacidades", "No tiene");
        parameters.put("estado", "Vive");
        parameters.put("sexo", "Femenino");
        parameters.put("correo_electronico", "hotmail@hotmail.com");
        parameters.put("telefonos", "+588224 45457");
        parameters.put("zona", "Rural");
        parameters.put("departamento", "San Salvador");
        parameters.put("municipio", "San Salvador");
        parameters.put("direccion", "COLONIA MIRAFLORES, PJE. 2 CASA 45");
        parameters.put("trabaja", "Sí");
        parameters.put("dependencia_economica", "Sí");
        parameters.put("ocupacion", "Estudiante");
        parameters.put("cantidad_hijos", "0");
        parameters.put("medio_transporte", "Auto");
        parameters.put("distancia_sede", "10km");

        return parameters;
    }

    public static void main(String[] args) throws IOException, ReportProcessingException {
        // Create an output filename
        final File outputFilename = new File(FichaEstudianteTest.class.getSimpleName() + ".pdf");

        // Generate the report
        new FichaEstudianteTest().generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
    }
}
