/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.DepreciacionContable;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DepreciacionReportesRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class DepreciacionSubcuentasContablesGenerator {
    @Inject
    private DepreciacionReportesRestClient descargosRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;  
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
    public MasterReport generarReporte(FiltroBienesDepreciables filtro) throws Exception {
       try {
            BusinessException be = new BusinessException();
             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_SUBCUENTAS_CONTABLES);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_SUBCUENTAS_CONTABLES + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("depsubcuentascontables.prpt");
            }
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            SgAfUnidadesActivoFijo unidadAFijo = null;
            if(filtro.getUnidadActivoFijoId() != null) {
                FiltroUnidadesActivoFijo filtroUAF = new FiltroUnidadesActivoFijo();
                filtroUAF.setId(filtro.getUnidadActivoFijoId());
                filtroUAF.setMaxResults(1L);
                filtroUAF.setIncluirCampos(new String[] {"uafNombre","uafVersion"});

                List<SgAfUnidadesActivoFijo> unidadesAF = unidadesActivoFijoRestClient.buscar(filtroUAF);

                if(unidadesAF != null && !unidadesAF.isEmpty()) {
                    unidadAFijo = unidadesAF.get(0);
                }
                if (unidadAFijo == null) {
                    be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ACTIVO_FIJO_VACIA));
                    throw be;
                }
            }
            
            
            List<DepreciacionContable> depreciaciones = descargosRestClient.buscar(filtro);
            if (depreciaciones.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.BUSQUEDA_SIN_RESULTADOS));
                throw be;
            }

            report.setDataFactory(this.getDataFactory(depreciaciones, unidadAFijo));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(List<DepreciacionContable> listaDep, SgAfUnidadesActivoFijo unidadAFijo) throws Exception {
        String fecha = LocalDate.now().getDayOfMonth() + " de " + Generales.getMesDescripcion(LocalDate.now().getMonth().getValue()) + " de " + LocalDate.now().getYear();
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("fecha", String.class);
        model.addColumn("usuario", String.class);
        model.addColumn("departamental", String.class);
        model.addColumn("bienes", TypedTableModel.class);
        model.addRow(
            fecha,
            sessionBean.getUserCode(),
            unidadAFijo != null && StringUtils.isNotBlank(unidadAFijo.getUafNombre()) ? unidadAFijo.getUafNombre().toUpperCase() : "",
            getDetalleDescargoTableModel(listaDep));

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
    
    private TypedTableModel getDetalleDescargoTableModel(List<DepreciacionContable> depLista) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("categoria", String.class);
        model.addColumn("precio", String.class);
        model.addColumn("valorRes", String.class);
        model.addColumn("depreAcumulada", String.class);
        model.addColumn("pendienteDepre", String.class);

        for (DepreciacionContable d : depLista) {
           model.addRow(
                  d.getCatNombre() != null && StringUtils.isNotBlank(d.getCatNombre()) ? d.getCatNombre().toUpperCase() : "",
                  d.getTotalValor() != null ? d.getTotalValor() : "",
                  d.getTotalValorResidual() != null ? d.getTotalValorResidual() : "",
                  d.getDepreciacionAcumulada() != null ? d.getDepreciacionAcumulada(): "",
                  d.getPendienteDepreciar() != null ? d.getPendienteDepreciar() : ""
           );
        }
        return model;
    }
}
