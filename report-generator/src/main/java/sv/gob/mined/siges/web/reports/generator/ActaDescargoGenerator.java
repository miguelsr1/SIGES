/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.dto.SgAfComisionDescargo;
import sv.gob.mined.siges.web.dto.SgAfDescargo;
import sv.gob.mined.siges.web.dto.SgAfDescargoDetalle;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDescargosDetalle;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DescargosRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;
import sv.gob.mined.siges.web.utilidades.NumberToLetterConverter;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class ActaDescargoGenerator {
    private static final Logger LOGGER = Logger.getLogger(ActaDescargoGenerator.class.getName());
    
    @Inject
    private SedeRestClient sedeRestClient;

    @Inject
    private UnidadesAdministrativasRestClient unidadesAdministrativasRestClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    @Inject
    private DescargosRestClient descargosRestClient;
    
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath; 
    
    public MasterReport generarReporte(FiltroDescargosDetalle filtro) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (filtro.getTipoUnidad() == null) {
                be.addError("Debes ingresar el tipo de unidad.");
                throw be;
            }
            
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
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_ACTA_DESCARGO_BIENES);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_ACTA_DESCARGO_BIENES + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("actaDescargo.prpt");
            } 
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            
            SgAfUnidadesActivoFijo unidadAFijo = unidadesActivoFijoRestClient.obtenerPorId(filtro.getUnidadActivoFijoId());
            if (unidadAFijo == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ACTIVO_FIJO_VACIA));
                throw be;
            }
            SgSede sede = null;
            SgAfUnidadesAdministrativas unidad = null;
            if(filtro.getTipoUnidad().equals(TipoUnidad.CENTRO_ESCOLAR)) {
                sede = sedeRestClient.obtenerPorId(filtro.getUnidadAdministrativaId());
                if (sede == null) {
                    be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                    throw be;
                }
            } else if(filtro.getTipoUnidad().equals(TipoUnidad.UNIDAD_ADMINISTRATIVA)) {
                unidad = unidadesAdministrativasRestClient.obtenerPorId(filtro.getUnidadAdministrativaId());
                if (unidad == null) {
                    be.addError(Mensajes.obtenerMensaje(Mensajes.UNIDAD_ADMINISTRATIVA_VACIA));
                    throw be;
                }
            }
            
            SgAfDescargo descargo = descargosRestClient.obtenerPorId(filtro.getDescargoId(), Boolean.TRUE);
            if (descargo == null) {
                be.addError("No se ha encontrado el descargo solicitado.");
                throw be;
            }
            BigDecimal montos = calcularMontos(descargo);
            report.setDataFactory(this.getDataFactory(unidadAFijo, sede, unidad, descargo,montos));

            return report;
        } catch (BusinessException ge) {
            LOGGER.log(Level.SEVERE, ge.getMessage(), ge);
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgAfUnidadesActivoFijo unidadAF, SgSede sede, 
            SgAfUnidadesAdministrativas unidad, 
            SgAfDescargo descargo, BigDecimal montoBienes
            ) throws Exception {
        String fechaDescargo = StringUtils.EMPTY;
        String fechaActa = StringUtils.EMPTY;
        String numeroAcuerdo = StringUtils.EMPTY;
        String seAutoriza = StringUtils.EMPTY;
        

        
        String montoDescripcion =  NumberToLetterConverter.convertNumberToLetter(montoBienes.toString(), Boolean.TRUE).trim().toUpperCase() + " dólares de los Estados Unidos de Norte América " + Generales.getValorMonetario("$", montoBienes);

        String fecha = NumberToLetterConverter.convertNumberToLetter(LocalDate.now().getDayOfMonth(), Boolean.FALSE) + " de " +
               Generales.getMesDescripcion(LocalDate.now().getMonthValue()) + " de " + 
                NumberToLetterConverter.convertNumberToLetter(LocalDate.now().getYear(), Boolean.FALSE);
        
        //Fecha de descargo de la solicitud
        if(descargo.getDesActaDescargo() != null) {
            fechaDescargo = NumberToLetterConverter.convertNumberToLetter(descargo.getDesFechaDescargo().getDayOfMonth(), Boolean.FALSE) + " de " +
               Generales.getMesDescripcion(descargo.getDesFechaDescargo().getMonthValue()) + " de " + 
                NumberToLetterConverter.convertNumberToLetter(descargo.getDesFechaDescargo().getYear(), Boolean.FALSE);
            //Fecha de acuerdo
            fechaActa = NumberToLetterConverter.convertNumberToLetter(descargo.getDesActaDescargo().getAdeFechaAcuerdo().getDayOfMonth(), Boolean.FALSE) + " de " +
                Generales.getMesDescripcion(descargo.getDesActaDescargo().getAdeFechaAcuerdo().getMonthValue()) + " de " + 
                NumberToLetterConverter.convertNumberToLetter(descargo.getDesActaDescargo().getAdeFechaAcuerdo().getYear(), Boolean.FALSE);
            
            numeroAcuerdo = descargo.getDesActaDescargo().getAdeNumeroAcuerdo();
            seAutoriza = descargo.getDesActaDescargo().getAdeSeAutoriza();
        }
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("nombreUnidad", String.class);
        model.addColumn("codigoActa", String.class);
        model.addColumn("ubicacion", String.class);
        model.addColumn("departamental", String.class);
        model.addColumn("fecha", String.class);
        model.addColumn("fechaDescargo", String.class);
        model.addColumn("fechaReporte", String.class);
        model.addColumn("numeroAcuerdo", String.class);
        model.addColumn("seAutoriza", String.class);
        model.addColumn("montoDescripcion", String.class);
        model.addColumn("nombre1", String.class);
        model.addColumn("nombre2", String.class);
        model.addColumn("nombre3", String.class);
        model.addColumn("cargo1", String.class);
        model.addColumn("cargo2", String.class);
        model.addColumn("cargo3", String.class);
        model.addColumn("ubicacion1", String.class);
        model.addColumn("ubicacion2", String.class);
        model.addColumn("ubicacion3", String.class);
        String ubicacion = (unidadAF != null && unidadAF.getUafDepartamento() != null ? unidadAF.getUafDepartamento().getDepNombre().trim().toUpperCase() : "");
        String unidadAFijo = (unidadAF != null ? unidadAF.getUafNombre().trim().toUpperCase() : "");
        
        String nombre1 = "";
        String nombre2 = "";
        String nombre3 = "";
        String cargo1 = "";
        String cargo2 = "";
        String cargo3 = "";
        String ubicacion1 = "";
        String ubicacion2 = "";
        String ubicacion3 = "";
        Integer count = 1;
        //Solo se toman en cuenta 3 miebros de comision
        if( unidadAF.getSgAfComisionDescargoList() != null && !unidadAF.getSgAfComisionDescargoList().isEmpty()) {
            for(SgAfComisionDescargo comision : unidadAF.getSgAfComisionDescargoList()) {
                if(comision.getCdeHabilitado() != null && comision.getCdeHabilitado()) {
                    if(count == 1) {
                        nombre1 = comision.getCdeNombreRepresentante().trim().toUpperCase();
                        cargo1 = comision.getCdeCargoRepresentante().trim().toUpperCase();
                        ubicacion1 = unidadAFijo;
                    } else if(count == 2) {
                        nombre2 = comision.getCdeNombreRepresentante().trim().toUpperCase();
                        cargo2 = comision.getCdeCargoRepresentante().trim().toUpperCase();
                        ubicacion2 = unidadAFijo;
                    } else if(count == 3) {
                        nombre3 = comision.getCdeNombreRepresentante().trim().toUpperCase();
                        cargo3 = comision.getCdeCargoRepresentante().trim().toUpperCase();
                        ubicacion3 = unidadAFijo;
                        break;
                    }
                    count ++;
                }
            }
        }
        if(sede != null) {
            model.addRow(
                sede.getSedNombre().trim().toUpperCase(),
                descargo.getDesCodigoDescargo() != null && StringUtils.isNotBlank(descargo.getDesCodigoDescargo()) ? descargo.getDesCodigoDescargo().trim().toUpperCase() : "",
                ubicacion.trim(),
                unidadAFijo,
                fecha.toLowerCase(),
                fechaDescargo.toLowerCase(),
                fechaActa.toLowerCase(),
                numeroAcuerdo,
                seAutoriza,
                montoDescripcion,
                nombre1,
                nombre2,
                nombre3,
                cargo1,
                cargo2,
                cargo3,
                ubicacion1,
                ubicacion2,
                ubicacion3
            );
        } else if(unidad != null) {
            model.addRow(
                unidad.getUadNombre().trim().toUpperCase(),
                descargo.getDesCodigoDescargo() != null && StringUtils.isNotBlank(descargo.getDesCodigoDescargo()) ? descargo.getDesCodigoDescargo().trim().toUpperCase() : "",
                ubicacion, 
                unidadAFijo,
                fecha.toLowerCase(),
                fechaDescargo.toLowerCase(),
                fechaActa.toLowerCase(),
                numeroAcuerdo,
                seAutoriza,
                montoDescripcion.toUpperCase(),
                nombre1,
                nombre2,
                nombre3,
                cargo1,
                cargo2,
                cargo3,
                ubicacion1,
                ubicacion2,
                ubicacion3
            );
        }

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);

        return dataFactory;
    }
    
    private BigDecimal calcularMontos(SgAfDescargo descargo) {
        BigDecimal resultado = new BigDecimal(0);
        if(descargo != null && descargo.getSgAfDescargosDetalle() != null && !descargo.getSgAfDescargosDetalle().isEmpty()) {
            for(SgAfDescargoDetalle det : descargo.getSgAfDescargosDetalle()) {
                if(det.getDdeBienesDepreciablesFk() != null && det.getDdeBienesDepreciablesFk().getBdeValorAdquisicion() != null) {
                    resultado = resultado.add(det.getDdeBienesDepreciablesFk().getBdeValorAdquisicion());
                }
            }
        }
        return resultado;
    }
}