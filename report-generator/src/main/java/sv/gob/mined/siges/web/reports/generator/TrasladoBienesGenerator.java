/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
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
import sv.gob.mined.siges.web.dto.SgAfTraslado;
import sv.gob.mined.siges.web.dto.SgAfTrasladoDetalle;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.TrasladosRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class TrasladoBienesGenerator {
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private TrasladosRestClient trasladosRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient activoFijoRestClient;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;  
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
    public MasterReport generarReporte(Long unidasAF, Long idTraslado) throws Exception {
       try {
            BusinessException be = new BusinessException();
            if (idTraslado == null) {
                be.addError("Debes ingresar el el del traslado.");
                throw be;
            }

            if (unidasAF == null) {
                be.addError("Debes ingresar la unidad de activo fijo.");
                throw be;
            }

             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();
            

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_TRASLADO_BIENES);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_TRASLADO_BIENES + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("trasladoBienes.prpt");
            }
            
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            SgAfUnidadesActivoFijo unidadAFijo = activoFijoRestClient.obtenerPorId(unidasAF);
            if (unidadAFijo == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ACTIVO_FIJO_VACIA));
                throw be;
            }
            
            SgAfTraslado traslado = trasladosRestClient.obtenerPorId(idTraslado, Boolean.TRUE);
            if (traslado == null || traslado.getSgAfTrasladosDetalle().isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.BUSQUEDA_SIN_RESULTADOS));
                throw be;
            }
            report.setDataFactory(this.getDataFactory(traslado, unidadAFijo));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgAfTraslado traslado, SgAfUnidadesActivoFijo unidadAFijo) throws Exception {
        TypedTableModel model = new TypedTableModel();
        model.addColumn("nombreUnidad1", String.class);
        model.addColumn("codigoUnidad2", String.class);
        model.addColumn("nombreUnidad2", String.class);
        model.addColumn("tipoTraslado", String.class);
        model.addColumn("numeroTraslado", String.class);
        model.addColumn("nombreDepartamental", String.class);
        model.addColumn("fecha", String.class);
        model.addColumn("usuario", String.class);
        model.addColumn("nombreAutoriza", String.class);
        model.addColumn("cargoAutoriza", String.class);
        model.addColumn("nombreRecibe", String.class);
        model.addColumn("cargoRecibe", String.class);
        model.addColumn("bienes", TypedTableModel.class);
        
        String codigoTraslado = StringUtils.EMPTY;
        if(traslado.getTraTipoTrasladoFk() != null && traslado.getTraTipoTrasladoFk().getEtrCodigo() != null 
                   && traslado.getTraTipoTrasladoFk().getEtrCodigo().trim().equals(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO)) {
            codigoTraslado = traslado.getTraCodigoTraslado();
        }
        
        model.addRow(
                StringUtils.isNotBlank(traslado.getTraCodigoNombreUnidadAdministrativaOrigen()) ? traslado.getTraCodigoNombreUnidadAdministrativaOrigen() : "",
                StringUtils.isNotBlank(traslado.getTraCodigoUnidadAdministrativaDestino())? traslado.getTraCodigoUnidadAdministrativaDestino() : "",
                StringUtils.isNotBlank(traslado.getTraNombreUnidadAdministrativaDestino())? traslado.getTraNombreUnidadAdministrativaDestino() : "",
                traslado.getTraTipoTrasladoFk() != null && StringUtils.isNotBlank(traslado.getTraTipoTrasladoFk().getEtrNombre()) ? traslado.getTraTipoTrasladoFk().getEtrNombre().trim().toUpperCase() : "",
                codigoTraslado,
                StringUtils.isNotBlank(unidadAFijo.getUafNombre()) ? unidadAFijo.getUafNombre().toUpperCase() : "",
                traslado.getTraFechaTraslado(), 
                sessionBean.getUserCode(),
                traslado.getTraNombreAutoriza() != null && StringUtils.isNotBlank(traslado.getTraNombreAutoriza())? traslado.getTraNombreAutoriza().trim().toUpperCase() : "",
                traslado.getTraCargoAutoriza() != null && StringUtils.isNotBlank(traslado.getTraCargoAutoriza())? traslado.getTraCargoAutoriza().trim().toUpperCase() : "",
                traslado.getTraNombreRecibe() != null && StringUtils.isNotBlank(traslado.getTraNombreRecibe())? traslado.getTraNombreRecibe().trim().toUpperCase() : "",
                traslado.getTraCargoRecibe() != null && StringUtils.isNotBlank(traslado.getTraCargoRecibe())? traslado.getTraCargoRecibe().trim().toUpperCase() : "",
                getDetalleTrasladoTableModel(traslado.getSgAfTrasladosDetalle()));

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
    
    private TypedTableModel getDetalleTrasladoTableModel(List<SgAfTrasladoDetalle> detalle) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("codigoInventario", String.class);
        model.addColumn("descripcionBien", String.class);
        model.addColumn("marcaBien", String.class);
        model.addColumn("modeloBien", String.class);
        model.addColumn("serieBien", String.class);
        model.addColumn("fechaAdquisicion", String.class);
        model.addColumn("valorAdquisicion", String.class);
                
        for (SgAfTrasladoDetalle d : detalle) {
           model.addRow(
                  d.getTdeBienesDepreciablesFk() != null ? d.getTdeBienesDepreciablesFk().getBdeCodigoInventario() : "",
                  d.getTdeBienesDepreciablesFk() != null && StringUtils.isNotBlank(d.getTdeBienesDepreciablesFk().getBdeDescripcion()) ? d.getTdeBienesDepreciablesFk().getBdeDescripcion().toUpperCase(): "",
                  d.getTdeBienesDepreciablesFk() != null && StringUtils.isNotBlank(d.getTdeBienesDepreciablesFk().getBdeMarca()) ? d.getTdeBienesDepreciablesFk().getBdeMarca().toUpperCase() : "",
                  d.getTdeBienesDepreciablesFk() != null && StringUtils.isNotBlank(d.getTdeBienesDepreciablesFk().getBdeModelo()) ? d.getTdeBienesDepreciablesFk().getBdeModelo().toUpperCase() : "",
                  d.getTdeBienesDepreciablesFk() != null && StringUtils.isNotBlank(d.getTdeBienesDepreciablesFk().getBdeNoSerie()) ? d.getTdeBienesDepreciablesFk().getBdeNoSerie().toUpperCase() : "",
                  d.getTdeBienesDepreciablesFk() != null ? Generales.getDateFormat(d.getTdeBienesDepreciablesFk().getBdeFechaAdquisicion(), "dd-MM-yyyy") : "",
                  d.getTdeBienesDepreciablesFk() != null ? d.getTdeBienesDepreciablesFk().getBdeValorAdquisicion() : "0.00"
           );
        }
        return model;
    }
}
