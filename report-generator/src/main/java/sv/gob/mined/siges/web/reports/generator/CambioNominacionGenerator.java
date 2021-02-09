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
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.simple.SgCambioNominacion;


/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class CambioNominacionGenerator {
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private SedeRestClient sedeRestClient;    
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;        
    
    public MasterReport generarReporte(SgCambioNominacion cn) throws Exception {

        try {
            SgSede sede = null;
            BusinessException be = new BusinessException();
            if(cn.getSedePk()== null) {
                sede = sedeRestClient.obtenerPorId(cn.getSedePk());
                if (sede == null) {
                    be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                    throw be;
                }
            }
            if(cn.getNombreAprobado()== null || cn.getNombreAprobado().equals("")){
                be.addError(Mensajes.obtenerMensaje(Mensajes.NOMBRE_APROBADO_VACIO));
                throw be;                
            }
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CAMBIO_NOMINACION);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CAMBIO_NOMINACION + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("rpt_sl_cambioNominacion.prpt");
            }       
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();


            report.setDataFactory(this.getDataFactory(cn));           
            
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgCambioNominacion cn) throws Exception {

        SgSede sede = sedeRestClient.obtenerPorId(cn.getSedePk());
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("codigoSede", String.class);
        model.addColumn("nombreSede", String.class);
        model.addColumn("nombreAprobado", String.class);
        model.addColumn("nombreFirma", String.class);
        model.addColumn("cargoFirma", String.class);
        model.addColumn("nombreDirector", String.class);
        model.addColumn("documentoDirector", String.class);
                
        model.addRow(
             sede.getSedCodigo()
            ,sede.getSedNombre()
            ,cn.getNombreAprobado()
            ,"nombre"
            ,"cargo"
            ,cn.getNombreCompleto()
            ,cn.getNumero());
                      
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);

        return dataFactory;
    }
}