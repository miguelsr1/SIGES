/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
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
import sv.gob.mined.siges.web.dto.SgCanton;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgZona;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.simple.SgCambioDomicilio;


/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class CambioDomicilioGenerator {
    
    @Inject
    private SessionBean sessionBean;

    @Inject
    private CatalogosRestClient catalogoRest;

    @Inject
    private SedeRestClient sedeRest;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath; 
   
    
    public MasterReport generarReporte(SgCambioDomicilio cd) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (cd.getDepartamentoNuevoPk() == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.DEPARTAMENTO_VACIO));
                throw be;
            }else if(cd.getMunicipioNuevoPk() == null){
                be.addError(Mensajes.obtenerMensaje(Mensajes.MUNICIPIO_VACIO));
                throw be;            
            }else if(cd.getCantonNuevoPk() == null){
                be.addError(Mensajes.obtenerMensaje(Mensajes.CANTON_VACIO));
                throw be;                            
            }
            if(cd.getDireccionNuevo() == null && !cd.getDireccionNuevo().equals("")){
                be.addError(Mensajes.obtenerMensaje(Mensajes.DIRECCION_VACIO));
                throw be;                                        
            }
            if(cd.getSedePk() == null){
                be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                throw be;                                                    
            }            
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CAMBIO_DOMICILIO);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CAMBIO_DOMICILIO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("rpt_sl_cambioDomicilio.prpt");
            }                       

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();


            report.setDataFactory(this.getDataFactory(cd));           
            
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgCambioDomicilio cd) throws Exception {
        
        SgSede sede = sedeRest.obtenerPorId(cd.getSedePk());        
        SgCanton canton = catalogoRest.obtenerPorIdCanton(cd.getCantonNuevoPk());
        SgZona zona = catalogoRest.obtenerPorIdZona(cd.getZonaPk());

        TypedTableModel model = new TypedTableModel();
        model.addColumn("departamento", String.class);
        model.addColumn("municipio",String.class);
        model.addColumn("canton", String.class);
        model.addColumn("caserio", String.class);
        model.addColumn("direccion", String.class);
        model.addColumn("zona",String.class);
        model.addColumn("departamentoOld",String.class);
        model.addColumn("municipioOld",String.class);
        model.addColumn("cantonOld",String.class); 
        model.addColumn("caserioOld",String.class); 
        model.addColumn("codigoSede", String.class);
        model.addColumn("nombreSede", String.class);
        model.addColumn("direccionOld", String.class);
        model.addColumn("zonaOld", String.class);
        model.addColumn("nombreFirma", String.class);
        model.addColumn("cargoFirma", String.class);
        model.addColumn("nombreDirector", String.class);
        model.addColumn("documentoDirector", String.class);
                
        model.addRow(
            canton.getCanMunicipio().getMunDepartamento().getDepNombre()
            ,canton.getCanMunicipio().getMunNombre()
            ,canton.getCanNombre()
            ,cd.getCaserioNuevo()
            ,cd.getDireccionNuevo()
            ,zona.getZonNombre()
            ,sede.getSedDireccion().getDirZona().getZonNombre()
            ,sede.getSedDireccion().getDirDepartamento().getDepNombre()
            ,sede.getSedDireccion().getDirMunicipio().getMunNombre()
            ,sede.getSedDireccion().getDirCanton().getCanNombre()
            ,sede.getSedDireccion().getDirCaserioTexto()
            ,sede.getSedCodigo()
            ,sede.getSedNombre()
            ,sede.getSedDireccion().getDirDireccion()
            ,sede.getSedDireccion().getDirZona().getZonNombre()
            ,"nombre"
            ,"cargo"
            ,cd.getNombreCompleto()
            ,cd.getNumero());

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);

        return dataFactory;
    }
}