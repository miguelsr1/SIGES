/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.DetalleDescargo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDescargosDetalle;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DescargosRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class DescargoBienesGenerator {

    @Inject
    private DescargosRestClient descargosRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;

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
     
    @Inject
    private PlantillaRestClient plantillaClient;
    
    public MasterReport generarReporte(FiltroDescargosDetalle filtro, Integer opcion) throws Exception {
       try {
            BusinessException be = new BusinessException();
            if (filtro == null) {
                be.addError("Debes ingresar el filtro.");
                throw be;
            }
            if(opcion != 1 && opcion != 2) {
                be.addError("La opción de generación de reporte es incorrecta.");
                throw be;
            }
            if(opcion == 1) {
                if (filtro.getUnidadActivoFijoId() == null) {
                    be.addError("Debes ingresar la unidad de activo fijo.");
                    throw be;
                }

                if (filtro.getUnidadAdministrativaId() == null) {
                    be.addError("Debes ingresar la unidad administrativa.");
                    throw be;
                }
                if (filtro.getDescargoId()== null) {
                    be.addError("Debes ingresar el número de descargo.");
                    throw be;
                }
            }
            
             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();
            

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                if(opcion == 1) {
                    SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_DESCARGO_BIENES);
                    if (plantilla == null || plantilla.getPlaArchivo() == null) {
                        be.addError("Plantilla " + Constantes.PLANTILLA_DESCARGO_BIENES + " inexistente.");
                        throw be;
                    }
                    reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
                } else if(opcion == 2) {
                    SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CONSOLIDADO_DESCARGO_BIENES);
                    if (plantilla == null || plantilla.getPlaArchivo() == null) {
                        be.addError("Plantilla " + Constantes.PLANTILLA_CONSOLIDADO_DESCARGO_BIENES + " inexistente.");
                        throw be;
                    }
                    reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
                }
                
            } else {
                if(opcion == 1) {
                    final ClassLoader classloader = this.getClass().getClassLoader();
                    reportDefinitionURL = classloader.getResource("descargoBienes.prpt");
                } else if(opcion == 2) {
                    final ClassLoader classloader = this.getClass().getClassLoader();
                    reportDefinitionURL = classloader.getResource("consolidadoDescargoBienes.prpt");
                }
                
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
                    filtroSede.setIncluirCampos(new String[]{"sedCodigo","sedNombre","sedTipo","sedDireccion.dirDireccion","sedVersion"});
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
                    filtroUAD.setIncluirCampos(new String[] {"uadNombre","uadCodigo","uadDireccion","uadVersion"});
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
            
            List<DetalleDescargo> bienes = buscarDescargoDetalle(filtro);
            if (bienes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.BUSQUEDA_SIN_RESULTADOS));
                throw be;
            }

            report.setDataFactory(this.getDataFactory(bienes, unidadAFijo, filtro.getDescargoId(), unidad, sede, opcion));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    public List<DetalleDescargo> buscarDescargoDetalle(FiltroDescargosDetalle filtro) throws Exception {
       List<DetalleDescargo> bienes = new ArrayList();
        if(filtro != null) {
           filtro.setAscending(new boolean[]{true});
           filtro.setOrderBy(new String[]{"codigoInventario"});
           bienes = descargosRestClient.buscar(filtro);
       }
        
       return bienes;
    }

    private DataFactory getDataFactory(List<DetalleDescargo> detalle, SgAfUnidadesActivoFijo unidadAFijo, Long descargoId, SgAfUnidadesAdministrativas unidad, SgSede sede, Integer opcion) throws Exception {
        String nombreUnidad = StringUtils.EMPTY;
        String codigoUnidad = StringUtils.EMPTY;
        String direccionUnidad = StringUtils.EMPTY;
        
        if(unidad != null) {
            nombreUnidad  = unidad.getUadNombre() != null ? unidad.getUadNombre().trim().toUpperCase() : "";
            codigoUnidad = unidad.getUadCodigo() != null ? unidad.getUadCodigo().trim().toUpperCase() : "";
            direccionUnidad = unidad.getUadDireccion() != null ? unidad.getUadDireccion().trim().toUpperCase() : "";
        } else if(sede != null) {
            nombreUnidad  = sede.getSedNombre() != null ? sede.getSedNombre().trim().toUpperCase() : "";
            codigoUnidad = sede.getSedCodigo() !=null ? sede.getSedCodigo().trim().toUpperCase() : "";
            direccionUnidad = sede.getSedDireccion() != null && sede.getSedDireccion().getDirDireccion() != null ? sede.getSedDireccion().getDirDireccion().toUpperCase() : "" ;
        }
        
        TypedTableModel model = new TypedTableModel();
        if(opcion == 1) {
            model.addColumn("nombreUnidad", String.class);
            model.addColumn("codigoUnidad", String.class);
            model.addColumn("direccion", String.class);
            model.addColumn("departamental", String.class);
            model.addColumn("fechaSolicitud", String.class);
            model.addColumn("fechaFallo", String.class);
            model.addColumn("codigoSolicitud", String.class);
            model.addColumn("bienes", TypedTableModel.class);
            if(detalle != null && !detalle.isEmpty()) {
                model.addRow(
                    nombreUnidad,
                    codigoUnidad,
                    direccionUnidad,
                    unidadAFijo != null && StringUtils.isNotBlank(unidadAFijo.getUafNombre()) ? unidadAFijo.getUafNombre().toUpperCase() : "",
                    descargoId != null ? (detalle.get(0).getFechaSolicitud() != null ? detalle.get(0).getFechaSolicitud() : "") : "",
                    descargoId != null ? (detalle.get(0).getFechaFallo() != null ? detalle.get(0).getFechaFallo() : "") : "",
                    descargoId != null ? (detalle.get(0).getCodigoSolicitud() != null ? detalle.get(0).getCodigoSolicitud() : "") : "",
                    getDetalleDescargoTableModel(detalle, opcion));
            }
        } else if(opcion == 2) {
            model.addColumn("nombreUnidad", String.class);
            model.addColumn("codigoUnidad", String.class);
            model.addColumn("departamental", String.class);
            model.addColumn("bienes", TypedTableModel.class);
            if(detalle != null && !detalle.isEmpty()) {
                model.addRow(
                    nombreUnidad,
                    codigoUnidad,
                    unidadAFijo != null && StringUtils.isNotBlank(unidadAFijo.getUafNombre()) ? unidadAFijo.getUafNombre().toUpperCase() : "",
                    getDetalleDescargoTableModel(detalle, opcion));
            }
        }
        

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }
    
    private TypedTableModel getDetalleDescargoTableModel(List<DetalleDescargo> detalle, Integer opcion) throws Exception {

        TypedTableModel model = new TypedTableModel();
        if(opcion == 1) {
            model.addColumn("tipoDescargo", String.class);
            model.addColumn("codigoInventario", String.class);
            model.addColumn("descripcion", String.class);
            model.addColumn("marcaNumero", String.class);
            model.addColumn("modeloNumero", String.class);
            model.addColumn("serieNchasis", String.class);
            model.addColumn("fechaAdquisicion", String.class);
            model.addColumn("valorAdquisicion", String.class);

            for (DetalleDescargo d : detalle) {
               model.addRow(
                       d.getTipoDecargadoNombre() != null && StringUtils.isNotBlank(d.getTipoDecargadoNombre()) ? d.getTipoDecargadoNombre().trim().toUpperCase() : "",
                       d.getCodigoInventario() != null && StringUtils.isNotBlank(d.getCodigoInventario()) ? d.getCodigoInventario().trim().toUpperCase() : "" ,
                       d.getDescripcion() != null && StringUtils.isNotBlank(d.getDescripcion()) ? d.getDescripcion().trim().toUpperCase() : "" ,
                       d.getMarca() != null && StringUtils.isNotBlank(d.getMarca()) ? d.getMarca().trim().toUpperCase() : "" ,
                       d.getModelo() != null && StringUtils.isNotBlank(d.getModelo()) ? d.getModelo().trim().toUpperCase() : "" ,
                       d.getNoserie() != null && StringUtils.isNotBlank(d.getNoserie()) ? d.getNoserie().trim().toUpperCase() : "" ,
                       d.getFechaAdquisicion(),
                       d.getValoradquisicion()
               );
            }
        } else if(opcion == 2) {
            model.addColumn("codigoInventario", String.class);
            model.addColumn("categoria", String.class);
            model.addColumn("descripcion", String.class);
            model.addColumn("marcaNumero", String.class);
            model.addColumn("modeloNumero", String.class);
            model.addColumn("serieNchasis", String.class);
            model.addColumn("fechaAdquisicion", String.class);
            model.addColumn("valorAdquisicion", String.class);
            model.addColumn("fechaDescargo", String.class);
            model.addColumn("codigoDescargo", String.class);
            model.addColumn("depreciacion", String.class);
            for (DetalleDescargo d : detalle) {
               model.addRow(
                       d.getCodigoInventario() != null && StringUtils.isNotBlank(d.getCodigoInventario()) ? d.getCodigoInventario().trim().toUpperCase() : "" ,
                       d.getNombreCategoria() != null && StringUtils.isNotBlank(d.getNombreCategoria()) ? d.getNombreCategoria().trim().toUpperCase() : "" ,
                       d.getDescripcion() != null && StringUtils.isNotBlank(d.getDescripcion()) ? d.getDescripcion().trim().toUpperCase() : "" ,
                       d.getMarca() != null && StringUtils.isNotBlank(d.getMarca()) ? d.getMarca().trim().toUpperCase() : "" ,
                       d.getModelo() != null && StringUtils.isNotBlank(d.getModelo()) ? d.getModelo().trim().toUpperCase() : "" ,
                       d.getNoserie() != null && StringUtils.isNotBlank(d.getNoserie()) ? d.getNoserie().trim().toUpperCase() : "" ,
                       d.getFechaAdquisicion(),
                       d.getValoradquisicion(),
                       d.getFechaDescargo(),
                       d.getCodigoSolicitud(),
                       d.getDepreciacion()
               );
            }
        }
        
        return model;
    }
}
