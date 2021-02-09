/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgDiploma;
import sv.gob.mined.siges.web.dto.SgDiplomaEstudiante;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomaEstudiante;
import sv.gob.mined.siges.web.restclient.CatalogoRestClient;
import sv.gob.mined.siges.web.restclient.DiplomaEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.DiplomaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudImpresionRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class DiplomaGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private DiplomaRestClient diplomaClient;

    @Inject
    private CatalogoRestClient catalogoRestClient;

    @Inject
    private DiplomaEstudianteRestClient diplomaEstudianteClient;

    @Inject
    private SolicitudImpresionRestClient solicitudImpresionRestClient;

    @Inject
    private PlantillaRestClient plantillaClient;

    private static final Logger LOGGER = Logger.getLogger(DiplomaGenerator.class.getName());

    Boolean respetaOrden;

    public MasterReport generarReporte(String diplomaPk,String estudiantePk) throws Exception {

        try {
            BusinessException be = new BusinessException();

            if (StringUtils.isBlank(diplomaPk)) {
                be.addError("Parámetros vacios");
                throw be;
            }

            Long estudiante=null;
            if(estudiantePk!=null){
                estudiante=Long.parseLong(estudiantePk);
            }
            
            FiltroDiplomaEstudiante fde = new FiltroDiplomaEstudiante();
            fde.setDiplomaFk(Long.parseLong(diplomaPk));
            fde.setEstudianteFk(estudiante);
            fde.setIncluirCampos(new String[]{"dieEstudianteFk.estPersona.perPrimerNombre",
                "dieEstudianteFk.estPersona.perPrimerApellido",
                "dieEstudianteFk.estPersona.perSegundoNombre",
                "dieEstudianteFk.estPersona.perSegundoApellido",
                "dieEstudianteFk.estPersona.perNie",
                "dieDiplomaFk.dilSedeFk.sedCodigo",
                "dieDiplomaFk.dilSedeFk.sedNombre",
                "dieDiplomaFk.dilSedeFk.sedTipo",
                "dieDiplomaFk.dilDiplomadoFk.dipNombre",
                "dieDiplomaFk.dilAnioLectivoFk.aleAnio",
                "dieDiplomaFk.dilSelloMinistro.sftFirmaSello.achPk",
                "dieDiplomaFk.dilSelloMinistro.sftPrimerNombre",
                "dieDiplomaFk.dilSelloMinistro.sftSegundoNombre",
                "dieDiplomaFk.dilSelloMinistro.sftPrimerApellido",
                "dieDiplomaFk.dilSelloMinistro.sftSegundoApellido",
                "dieDiplomaFk.dilSelloDirector.sfiFirmaSello.achPk",
                "dieDiplomaFk.dilSelloDirector.sfiPersona.perPrimerNombre",
                "dieDiplomaFk.dilSelloDirector.sfiPersona.perSegundoNombre",
                "dieDiplomaFk.dilSelloDirector.sfiPersona.perPrimerApellido",
                "dieDiplomaFk.dilSelloDirector.sfiPersona.perSegundoApellido"
            });
            List<SgDiplomaEstudiante> dipEst = diplomaEstudianteClient.buscar(fde);

            if (dipEst.isEmpty()) {
                be.addError("No existen diplomas. ");
                throw be;
            }

            //Todos apuntan al mismo diploma cabezal
            SgDiploma diploma=dipEst.get(0).getDieDiplomaFk();
            
            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_DIPLOMAS_CODIGO);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_DIPLOMAS_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("diploma.prpt");
            }
            
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            SgSelloFirma sfDirector = diploma.getDilSelloDirector();
            if (sfDirector != null) {
                if (sfDirector.getSfiFirmaSello() != null) {
                    report.getParameterValues().put("firma_sello_director_path", this.basePath + SofisFileUtils.getPathFromPk(sfDirector.getSfiFirmaSello().getAchPk()));
                }
                report.getParameterValues().put("nombre_director", sfDirector.getSfiPersona().getPerNombreCompletoNP());
            }

            SgSelloFirmaTitular sfMinistro = diploma.getDilSelloMinistro();
            if (sfMinistro != null) {
                if (sfMinistro.getSftFirmaSello() != null) {
                    report.getParameterValues().put("firma_sello_ministro_path", this.basePath + SofisFileUtils.getPathFromPk(sfMinistro.getSftFirmaSello().getAchPk()));
                }
                report.getParameterValues().put("nombre_ministro", sfMinistro.getSftNombreCompleto());
            }

            

// año, sede, diplomado, y estudiante.
            TypedTableModel model = new TypedTableModel();
            model.addColumn("nombre_persona", String.class);
            model.addColumn("nie_persona", String.class);
            model.addColumn("codigo_nombre_sede", String.class);
            model.addColumn("nombre_sede", String.class);
            model.addColumn("diplomado", String.class);
            model.addColumn("anio", String.class);

            for (SgDiplomaEstudiante dip : dipEst) {

                model.addRow(dip.getDieEstudianteFk().getEstPersona().getPerNombreCompleto(),
                        dip.getDieEstudianteFk().getEstPersona().getPerNie(),
                        dip.getDieDiplomaFk().getDilSedeFk().getSedCodigoNombre(),
                        dip.getDieDiplomaFk().getDilSedeFk().getSedNombre(),
                        dip.getDieDiplomaFk().getDilDiplomadoFk().getDipNombre(),
                        dip.getDieDiplomaFk().getDilAnioLectivoFk().getAleAnio().toString()
                );
            }

            TableDataFactory dataFactory = new TableDataFactory();
            dataFactory.addTable("param-query", model);
            report.setDataFactory(dataFactory);

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

}
