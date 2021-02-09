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
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
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
public class VehiculosCargosGenerator {
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private BienesDepreciablesRestClient bienesRestClient;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;  
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;

    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    public MasterReport generarReporte(FiltroBienesDepreciables filtro) throws Exception {
       try {
            BusinessException be = new BusinessException();
            if (filtro == null) {
                be.addError("Debes ingresar el filtro.");
                throw be;
            }

             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();
            

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CARGO_VEHICULOS_BIENES);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CARGO_VEHICULOS_BIENES + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("bienesVehiculos.prpt");
            }
            
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            SgSede sede = null;
            SgAfUnidadesAdministrativas unidad = null;
            SgAfUnidadesActivoFijo unidadAFijo = null;

            if (filtro.getUnidadActivoFijoId() != null) {
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
            if(filtro.getTipoUnidad() != null && filtro.getUnidadAdministrativaId() != null){
                if(filtro.getTipoUnidad().equals(TipoUnidad.CENTRO_ESCOLAR)) {
                    FiltroSedes filtroSede = new FiltroSedes();
                    filtroSede.setSedPk(filtro.getUnidadAdministrativaId());
                    filtroSede.setMaxResults(1L);
                    filtroSede.setIncluirCampos(new String[]{"sedCodigo","sedNombre","sedTipo","sedVersion"});
                    List<SgSede> sedes = sedeRestClient.buscar(filtroSede);
                    if(sedes != null && !sedes.isEmpty()) {
                        sede = sedes.get(0);
                    }
                    
                    if (sede == null) {
                        be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                        throw be;
                    }
                } else if(filtro.getTipoUnidad().equals(TipoUnidad.UNIDAD_ADMINISTRATIVA)) {
                    FiltroUnidadesAdministrativas filtroUAD = new FiltroUnidadesAdministrativas();
                    filtroUAD.setId(filtro.getUnidadAdministrativaId());
                    filtroUAD.setMaxResults(1L);
                    filtroUAD.setIncluirCampos(new String[] {"uadNombre","uadCodigo","uadVersion"});
                    List<SgAfUnidadesAdministrativas> unidadesAD = unidadesAdministrativasRestClient.buscar(filtroUAD);
                    if(unidadesAD != null && !unidadesAD.isEmpty()) {
                        unidad = unidadesAD.get(0);
                    }
                    if (unidad == null) {
                        be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ADMINISTRATIVA_VACIA));
                        throw be;
                    }
                }
            }
            
            List<SgAfBienDepreciable> bienes = buscarBienes(filtro);
            if (bienes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.BUSQUEDA_SIN_RESULTADOS));
                throw be;
            }
            
            report.setDataFactory(this.getDataFactory(bienes, unidadAFijo, unidad, sede));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    public List<SgAfBienDepreciable> buscarBienes(FiltroBienesDepreciables filtro) throws Exception {
       List<SgAfBienDepreciable> bienes = new ArrayList();
        if(filtro != null) {
           filtro.setIncluirCampos(new String[]{"bdeCodigoInventario","bdeUnidadesAdministrativas.uadCodigo","bdeUnidadesAdministrativas.uadNombre","bdeSede.sedNombre","bdeSede.sedTipo",
               "bdeSede.sedCodigo","bdeDescripcion","bdeMarca","bdeNoPlaca","bdeNoMotor","bdeNoChasis","bdeColorCarroceria","bdeFechaAdquisicion","bdeValorAdquisicion","bdeAsignadoA",
               "bdeEmpleadoFk.empPersonaFk.perPrimerNombre","bdeEmpleadoFk.empPersonaFk.perSegundoNombre","bdeEmpleadoFk.empPersonaFk.perTercerNombre",
               "bdeEmpleadoFk.empPersonaFk.perPrimerApellido","bdeEmpleadoFk.empPersonaFk.perSegundoApellido","bdeEmpleadoFk.empPersonaFk.perTercerApellido",
               "bdeEstadoCalidad.ecaNombre","bdeVersion"
           });
           
           bienes = bienesRestClient.buscar(filtro);
       }
        
       return bienes;
    }

    private DataFactory getDataFactory(List<SgAfBienDepreciable> bienes, SgAfUnidadesActivoFijo unidadAFijo, SgAfUnidadesAdministrativas unidad, SgSede sede) throws Exception {
        String fecha = LocalDate.now().getDayOfMonth() + " de " + Generales.getMesDescripcion(LocalDate.now().getMonth().getValue()) + " de " + LocalDate.now().getYear();
        
        String nombreUnidad = StringUtils.EMPTY;
        String nombreDepartamental = StringUtils.EMPTY;
        if(unidad != null) {
            nombreUnidad = unidad.getCodigoNombre() != null && StringUtils.isNotEmpty(unidad.getCodigoNombre()) ? unidad.getCodigoNombre().trim().toUpperCase() : "";
        } else if(sede != null) {
            nombreUnidad = sede.getSedCodigoNombre() != null && StringUtils.isNotEmpty(sede.getSedCodigoNombre()) ? sede.getSedCodigoNombre().trim().toUpperCase() : "";
        }
        
        if(unidadAFijo != null) {
            nombreDepartamental = unidadAFijo.getUafNombre() != null && StringUtils.isNotBlank(unidadAFijo.getUafNombre()) ? unidadAFijo.getUafNombre().trim().toUpperCase() : "";
        }
        TypedTableModel model = new TypedTableModel();
        model.addColumn("fecha", String.class);
        model.addColumn("usuario", String.class);
        model.addColumn("departamental", String.class);
        model.addColumn("unidad", String.class);
        model.addColumn("bienes", TypedTableModel.class);

        model.addRow(
            fecha, 
            sessionBean.getUserCode(),
            nombreDepartamental,
            nombreUnidad,
            getBienesTableModel(bienes));

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
    
    private TypedTableModel getBienesTableModel(List<SgAfBienDepreciable> bienes) throws Exception {
        TypedTableModel model = new TypedTableModel();
        model.addColumn("codigoInventario", String.class);
        model.addColumn("descripcionBien", String.class);
        model.addColumn("marca", String.class);
        model.addColumn("numero_placa", String.class);
        model.addColumn("numeroMotor", String.class);
        model.addColumn("numeroChasis", String.class);
        model.addColumn("color", String.class);
        model.addColumn("calidad", String.class);
        model.addColumn("fechaAdquisicion", String.class);
        model.addColumn("valorAdquisicion", String.class);
        model.addColumn("nombreUnidad", String.class);
        model.addColumn("asignadoA", String.class);
                
        for (SgAfBienDepreciable bien : bienes) {
           model.addRow(
                   bien.getBdeCodigoInventario(),
                   bien.getBdeDescripcion() != null && StringUtils.isNotBlank(bien.getBdeDescripcion()) ? bien.getBdeDescripcion().trim().toUpperCase() : "",
                   bien.getBdeMarca() != null && StringUtils.isNotBlank(bien.getBdeMarca()) ? bien.getBdeMarca().trim().toUpperCase() : "",
                   bien.getBdeNoPlaca() != null && StringUtils.isNotBlank(bien.getBdeNoPlaca()) ? bien.getBdeNoPlaca().trim().toUpperCase() : "",
                   bien.getBdeNoMotor() != null && StringUtils.isNotBlank(bien.getBdeNoMotor()) ? bien.getBdeNoMotor().trim().toUpperCase() : "",
                   bien.getBdeNoChasis()!= null && StringUtils.isNotBlank(bien.getBdeNoChasis()) ? bien.getBdeNoChasis().trim().toUpperCase() : "",
                   bien.getBdeColorCarroceria()!= null && StringUtils.isNotBlank(bien.getBdeColorCarroceria()) ? bien.getBdeColorCarroceria().trim().toUpperCase() : "",
                   bien.getBdeEstadoCalidad() != null && bien.getBdeEstadoCalidad().getEcaNombre() != null && StringUtils.isNotBlank(bien.getBdeEstadoCalidad().getEcaNombre()) ? bien.getBdeEstadoCalidad().getEcaNombre().trim().toUpperCase() : "",
                   Generales.getDateFormat(bien.getBdeFechaAdquisicion(), "dd/MM/yyyy"),
                   bien.getBdeValorAdquisicion(),
                   bien.getBdeUnidadesAdministrativas() != null && StringUtils.isNotBlank(bien.getBdeUnidadesAdministrativas().getUadNombre())? bien.getBdeUnidadesAdministrativas().getUadNombre().trim().toUpperCase() : "",
                   bien.getBdeBienAsignadoA() != null && StringUtils.isNotBlank(bien.getBdeBienAsignadoA()) ? bien.getBdeBienAsignadoA().trim().toUpperCase() : ""
           );
        }
        return model;
    }
}
