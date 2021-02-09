/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
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
import sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class CargosBienesGenerator {
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private BienesDepreciablesRestClient bienesRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient activoFijoRestClient;

    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath; 
    
    public MasterReport generarReporte(FiltroBienesDepreciables filtro, LocalDate fechaReporte) throws Exception {
       String unidadAdministrativa = StringUtils.EMPTY;
       String unidadActivoFijo = StringUtils.EMPTY;
        try {
            BusinessException be = new BusinessException();

            if(fechaReporte == null) {
                be.addError("Debes ingresar la fecha del reporte.");
                throw be;
            }
             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();
            

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CARGO_BIENES);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CARGO_BIENES + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("cargoBienes.prpt");
            } 
            
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            SgAfUnidadesActivoFijo unidadAFijo = null;
            if(filtro.getUnidadActivoFijoId() != null) {
                unidadAFijo = activoFijoRestClient.obtenerPorId(filtro.getUnidadActivoFijoId());
                if(unidadAFijo == null) {
                    be.addError(Mensajes.obtenerMensaje("La unidad de Activo Fijo no se ha encontrado."));
                    throw be;
                }
                unidadActivoFijo = unidadAFijo.getUafNombre() != null && StringUtils.isNotBlank(unidadAFijo.getUafNombre().trim()) ? unidadAFijo.getUafNombre().trim().toUpperCase() : "";
            }
            SgSede sede = null;
            SgAfUnidadesAdministrativas unidad = null;
            
            if(filtro.getTipoUnidad() != null && filtro.getUnidadAdministrativaId() != null) {
                if(filtro.getTipoUnidad().equals(TipoUnidad.CENTRO_ESCOLAR)) {
                    sede = sedeRestClient.obtenerPorId(filtro.getUnidadAdministrativaId());
                    if (sede == null) {
                        be.addError(Mensajes.obtenerMensaje("La Unidad Administrativa no se ha encontrado"));
                        throw be;
                    }
                    unidadAdministrativa = sede.getSedCodigoNombre() != null && StringUtils.isNotBlank(sede.getSedCodigoNombre()) ? sede.getSedCodigoNombre().trim().toUpperCase() : "";
                } else if(filtro.getTipoUnidad().equals(TipoUnidad.UNIDAD_ADMINISTRATIVA)) {
                    unidad = unidadesAdministrativasRestClient.obtenerPorId(filtro.getUnidadAdministrativaId());
                    if (unidad == null) {
                        be.addError(Mensajes.obtenerMensaje("La Unidad Administrativa no se ha encontrado"));
                        throw be;
                    }
                    unidadAdministrativa = unidad.getCodigoNombre() != null && StringUtils.isNotBlank(unidad.getCodigoNombre()) ? unidad.getCodigoNombre().trim().toUpperCase() : "";
                }
            }
            
            List<SgAfBienDepreciable> bienes = buscarBienes(filtro);
            if (bienes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.BUSQUEDA_SIN_RESULTADOS));
                throw be;
            }

            report.setDataFactory(this.getDataFactory(bienes, unidadActivoFijo, fechaReporte, unidadAdministrativa));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    public List<SgAfBienDepreciable> buscarBienes(FiltroBienesDepreciables filtro) throws Exception {
       List<SgAfBienDepreciable> bienes = new ArrayList();
        if(filtro != null) {
           filtro.setIncluirCampos(new String[]{"bdeCodigoInventario","bdeUnidadesAdministrativas.uadCodigo","bdeUnidadesAdministrativas.uadNombre","bdeSede.sedNombre","bdeSede.sedTipo",
               "bdeSede.sedCodigo","bdeDescripcion","bdeMarca","bdeModelo","bdeNoSerie","bdeFechaAdquisicion","bdeValorAdquisicion","bdeAsignadoA","bdeEmpleadoFk.empPersonaFk.perPrimerNombre",
               "bdeEmpleadoFk.empPersonaFk.perSegundoNombre","bdeEmpleadoFk.empPersonaFk.perTercerNombre",
               "bdeEmpleadoFk.empPersonaFk.perPrimerApellido","bdeEmpleadoFk.empPersonaFk.perSegundoApellido","bdeEmpleadoFk.empPersonaFk.perTercerApellido","bdeEstadoCalidad.ecaNombre","bdeVersion"
           });
           filtro.setOrderBy(new String[]{"bdeCodigoInventario"});
           filtro.setAscending(new boolean[]{true});
           bienes = bienesRestClient.buscar(filtro);
       }
        
       return bienes;
    }

    private DataFactory getDataFactory(List<SgAfBienDepreciable> bienes, String unidadAFijo, LocalDate fechaReporte, String unidadAdministrativa) throws Exception {
        String fecha = fechaReporte.getDayOfMonth() + " de " + Generales.getMesDescripcion(fechaReporte.getMonth().getValue()) + " de " + fechaReporte.getYear();
        TypedTableModel model = new TypedTableModel();
        model.addColumn("nombreUnidad", String.class);
        model.addColumn("departamental", String.class);
        model.addColumn("fechaInventario", String.class);
        model.addColumn("usuario", String.class);
        model.addColumn("bienes", TypedTableModel.class);
        
        model.addRow(
            unidadAdministrativa,
            unidadAFijo,
            fecha, 
            sessionBean.getUserCode(),
            getBienesTableModel(bienes));

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
    
    private TypedTableModel getBienesTableModel(List<SgAfBienDepreciable> bienes) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("codigoInventario", String.class);
        model.addColumn("descripcion", String.class);
        model.addColumn("marca", String.class);
        model.addColumn("modelo", String.class);
        model.addColumn("serie", String.class);
        model.addColumn("fechaAdquisicion", String.class);
        model.addColumn("valorAdquisicion", String.class);
        model.addColumn("calidad", String.class);
        model.addColumn("asignadoA", String.class);
                
        for (SgAfBienDepreciable bien : bienes) {
           model.addRow(
                   bien.getBdeCodigoInventario(),
                   bien.getBdeDescripcion() != null && StringUtils.isNotBlank(bien.getBdeDescripcion()) ? bien.getBdeDescripcion().trim().toUpperCase() : "",
                   bien.getBdeMarca() != null && StringUtils.isNotBlank(bien.getBdeMarca()) ? bien.getBdeMarca().trim().toUpperCase() : "",
                   bien.getBdeModelo() != null && StringUtils.isNotBlank(bien.getBdeModelo()) ? bien.getBdeModelo().trim().toUpperCase() : "",
                   bien.getBdeNoSerie() != null && StringUtils.isNotBlank(bien.getBdeNoSerie()) ? bien.getBdeNoSerie().trim().toUpperCase() : "",
                   Generales.getDateFormat(bien.getBdeFechaAdquisicion(), "dd/MM/yyyy"),
                   bien.getBdeValorAdquisicion(),
                   bien.getBdeEstadoCalidad() != null && bien.getBdeEstadoCalidad().getEcaNombre() != null && StringUtils.isNotBlank(bien.getBdeEstadoCalidad().getEcaNombre()) ? bien.getBdeEstadoCalidad().getEcaNombre().trim().toUpperCase() : "",
                   bien.getBdeBienAsignadoA() != null && StringUtils.isNotBlank(bien.getBdeBienAsignadoA()) ? bien.getBdeBienAsignadoA().trim().toUpperCase() : ""
           );
        }
        return model;
    }
}
