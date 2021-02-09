/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.simple.SgAmpliacionServicio;


/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class AmpliacionServicioGenerator {
    
    @Inject
    private SessionBean sessionBean;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;    
    
    private static final Logger LOGGER = Logger.getLogger(AmpliacionServicioGenerator.class.getName());
    
    public MasterReport generarReporte(SgAmpliacionServicio ampliacionServicio) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (ampliacionServicio.getSedId() == null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SEDE_VACIA));
                throw be;
            }
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_AMPLIACION_SERVICIO);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_AMPLIACION_SERVICIO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("rpt_sl_ampliacionServicio.prpt");
            }            
            
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            report.setDataFactory(this.getDataFactory(ampliacionServicio));                       
            return report;
        } catch (BusinessException ge) {
            LOGGER.log(Level.SEVERE, ge.getMessage());
            throw ge;
        }
    }

    private DataFactory getDataFactory(SgAmpliacionServicio a) throws Exception {

        
        TypedTableModel model = new TypedTableModel();        
       
        model.addColumn("numero", String.class);
        model.addColumn("nombre_completo", String.class);        
        model.addColumn("telefono_de_contacto", String.class);
        model.addColumn("estadoSolicitud", String.class);        
        model.addColumn("sedPk", Long.class);
        
        model.addRow(a.getNumero(),a.getNombreCompleto(),a.getTelefonoContacto(),a.getEstadoSolicitud(),a.getSedId());        
        
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
                
        return dataFactory;
    }
}