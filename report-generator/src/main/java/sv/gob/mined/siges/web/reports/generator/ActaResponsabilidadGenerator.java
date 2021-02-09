/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class ActaResponsabilidadGenerator {

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private BienesDepreciablesRestClient bienesRestClient;

    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;  
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
    public MasterReport generarReporte(Long unidadAF, Long unidadAD, Long empleadoId) throws Exception {
        String responsable = StringUtils.EMPTY;
        String cargo = StringUtils.EMPTY;
        try {
            BusinessException be = new BusinessException();
            if (unidadAF == null) {
                be.addError("Debes ingresar la unidad de activo fijo.");
                throw be;
            }

            if (unidadAD == null) {
                be.addError("Debes ingresar la unidad administrativa.");
                throw be;
            }
            
            if (empleadoId == null) {
                be.addError("Debes ingresar el empleado.");
                throw be;
            }
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_ACTA_RESPONSABILIDAD_BIENES);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_ACTA_RESPONSABILIDAD_BIENES + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("acta_responsabilidad.prpt");
            } 

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            SgAfUnidadesActivoFijo unidadAFijo = null;
            
            FiltroUnidadesActivoFijo filtroUAF = new FiltroUnidadesActivoFijo();
            filtroUAF.setId(unidadAF);
            filtroUAF.setMaxResults(1L);
            filtroUAF.setIncluirCampos(new String[] {"uafResponsableAF","uafCargoResponsableAF","uafVersion"});
            
            List<SgAfUnidadesActivoFijo> unidadesAF = unidadesActivoFijoRestClient.buscar(filtroUAF);
                    
            if(unidadesAF != null && !unidadesAF.isEmpty()) {
                unidadAFijo = unidadesAF.get(0);
            }
            if (unidadAFijo == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ACTIVO_FIJO_VACIA));
                throw be;
            }
            
            responsable = unidadAFijo.getUafResponsableAF()!= null && StringUtils.isNotBlank(unidadAFijo.getUafResponsableAF()) ? unidadAFijo.getUafResponsableAF().trim().toUpperCase() : "";
            cargo = unidadAFijo.getUafCargoResponsableAF()!= null && StringUtils.isNotBlank(unidadAFijo.getUafCargoResponsableAF()) ? unidadAFijo.getUafCargoResponsableAF().trim().toUpperCase() : "";
            
            if (StringUtils.isBlank(responsable.trim())) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.RESPONSABLE_AF_NO_CONFIGURADO));
                throw be;
            }
            
            if (StringUtils.isBlank(cargo.trim())) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.CARGO_RESPONSABLE_AF_NO_CONFIGURADO));
                throw be;
            }
            
            List<SgAfBienDepreciable> bienes = buscarBienes(unidadAF, unidadAD, empleadoId);
            if (bienes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.EMPLEADO_SIN_BIENES));
                throw be;
            }
            report.setDataFactory(this.getDataFactory(bienes, responsable, cargo));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    public List<SgAfBienDepreciable> buscarBienes(Long unidadAF, Long unidadAD, Long empleadoId) throws Exception {
        FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
        filtroBienes.setAscending(new boolean[]{true});
        filtroBienes.setOrderBy(new String[]{"bdeCodigoInventario"});
        filtroBienes.setIncluirCampos(new String[]{"bdeCodigoInventario","bdeDescripcion","bdeMarca","bdeNoSerie","bdeEmpleadoFk.empPersonaFk.perOcupacion.ocuNombre","bdeFechaAdquisicion","bdeValorAdquisicion","bdeVersion",
                                            "bdeEmpleadoFk.empPersonaFk.perPrimerNombre","bdeEmpleadoFk.empPersonaFk.perSegundoNombre","bdeEmpleadoFk.empPersonaFk.perTercerNombre",
                                            "bdeEmpleadoFk.empPersonaFk.perPrimerApellido","bdeEmpleadoFk.empPersonaFk.perSegundoApellido","bdeEmpleadoFk.empPersonaFk.perTercerApellido"});
        filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);
        filtroBienes.setUnidadActivoFijoId(unidadAF);
        filtroBienes.setUnidadAdministrativaId(unidadAD);
        filtroBienes.setEmpleadoId(empleadoId);

        return  bienesRestClient.buscar(filtroBienes);
    }

    private DataFactory getDataFactory(List<SgAfBienDepreciable> bienes, String nombreResponsableAF, String cargoResponsableAF) throws Exception {
        
        String fecha = LocalDate.now().getDayOfMonth() + " de " + Generales.getMesDescripcion(LocalDate.now().getMonth().getValue()) + " de " + LocalDate.now().getYear();
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("nombreEncargadoAF", String.class);
        model.addColumn("cargoResponsable", String.class);
        model.addColumn("nombreEmpleado", String.class);
        model.addColumn("cargoEmpleado", String.class);
        model.addColumn("fecha", String.class);
        model.addColumn("hora", String.class);
        model.addColumn("usuario", String.class);
        model.addColumn("bienes", TypedTableModel.class);
        model.addRow(
                nombreResponsableAF.toUpperCase(),
                cargoResponsableAF.toUpperCase(),
                bienes.get(0).getBdeEmpleadoFk() != null && bienes.get(0).getBdeEmpleadoFk().getNombresApellidos()!= null ? bienes.get(0).getBdeEmpleadoFk().getNombresApellidos().trim().toUpperCase() : "",
                bienes.get(0).getBdeEmpleadoFk() != null && bienes.get(0).getBdeEmpleadoFk().getOcupacionNombre() != null ? bienes.get(0).getBdeEmpleadoFk().getOcupacionNombre().toUpperCase() : "", 
                fecha, 
                LocalDateTime.now().getHour(),
                sessionBean.getUserCode(), 
                getBienesTableModel(bienes)
        );
        
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);

        return dataFactory;
    }

    private TypedTableModel getBienesTableModel(List<SgAfBienDepreciable> bienes) throws Exception {
        TypedTableModel model = new TypedTableModel();
        model.addColumn("numeroInventario", String.class);
        model.addColumn("descripcionBien", String.class);
        model.addColumn("marca", String.class);
        model.addColumn("serie", String.class);
        model.addColumn("fechaAdquisicion", String.class);
        model.addColumn("valorAdquisicion", BigDecimal.class);

        for (SgAfBienDepreciable bien : bienes) {
           model.addRow(
                   bien.getBdeCodigoInventario(),
                   bien.getBdeDescripcion(),
                   bien.getBdeMarca(),
                   bien.getBdeSede(),
                   Generales.getDateFormat(bien.getBdeFechaAdquisicion(), "dd/MM/yyyy"),
                   bien.getBdeValorAdquisicion());
        }
        return model;
    }
}