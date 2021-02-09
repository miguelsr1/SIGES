/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.SgSolicitudTraslado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSolicitudTraslado;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudTrasladoRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class TrasladoConfirmadoGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private SelloFirmaRestClient selloFirmaClient;

    @Inject
    private SolicitudTrasladoRestClient solicitudTrasladoClient;


    public MasterReport generarReporte(Long trasladoPk) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (trasladoPk == null) {
                be.addError("Debes ingresar un traslado.");
                throw be;
            }

            FiltroSolicitudTraslado fil = new FiltroSolicitudTraslado();
            fil.setSotPk(trasladoPk);
            fil.setIncluirCampos(new String[]{
                "sotSedeOrigen.sedTipo", "sotSedeOrigen.sedCodigo", "sotSedeOrigen.sedNombre",
                "sotSedeSolicitante.sedTipo", "sotSedeSolicitante.sedCodigo", "sotSedeSolicitante.sedNombre",
                "sotServicioEducativoOrigen.sduGrado.graNombre",
                "sotServicioEducativoSolicitado.sduGrado.graNombre",
                "sotSeccion.secNombre", "sotSeccion.secCodigo", "sotSeccion.secJornadaLaboral.jlaNombre",
                "sotEstudiante.estPersona.perNie", "sotEstudiante.estPk",
                "sotEstudiante.estPersona.perPrimerNombre", "sotEstudiante.estPersona.perSegundoNombre", "sotEstudiante.estPersona.perNombreBusqueda",
                "sotEstudiante.estPersona.perPrimerApellido", "sotEstudiante.estPersona.perSegundoApellido", "sotEstudiante.estPersona.perFechaNacimiento",
                "sotEstudiante.estPersona.perSexo.sexNombre",
                "sotFechaTraslado", "sotResolucion", "sotMotivoTraslado.motNombre",
                "sotConfirmacion.sotResolucion", 
                "sotConfirmacion.sotSeccion.secNombre", "sotConfirmacion.sotSeccion.secCodigo", "sotConfirmacion.sotSeccion.secJornadaLaboral.jlaNombre",
                "sotConfirmacion.sotFechaTraslado" });
            List<SgSolicitudTraslado> traslados = solicitudTrasladoClient.buscar(fil);
            if (traslados.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.TRASLADO_NO_ENCONTRADO));
                throw be;
            }

            SgSolicitudTraslado traslado = traslados.get(0);

            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_TRASLADO_CONFIRMADO_CODIGO);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_TRASLADO_CONFIRMADO_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("traslado_confirmado.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            FiltroSelloFirma filtroSelloFirma = new FiltroSelloFirma();
            filtroSelloFirma.setSfiHabilitado(Boolean.TRUE);
            filtroSelloFirma.setSedPk(traslado.getSotSedeSolicitante().getSedPk());
            filtroSelloFirma.setIncluirCampos(new String[]{
                "sfiTipoRepresentante.treCodigo",
                "sfiFirmaSello.achPk",
                "sfiPersona.perNie",
                "sfiPersona.perPrimerNombre", "sfiPersona.perSegundoNombre",
                "sfiPersona.perPrimerApellido", "sfiPersona.perSegundoApellido",
                "sfiPersona.perFechaNacimiento"
            });

            List<SgSelloFirma> sellos = selloFirmaClient.buscar(filtroSelloFirma);
            if (!sellos.isEmpty()) {
                for (SgSelloFirma sello : sellos) {
                    if (Constantes.TIPO_REPRESENTANTE_DIRECTOR_CODIGO.equals(sello.getSfiTipoRepresentante().getTreCodigo())) {
                        if (sello.getSfiFirmaSello() != null) {
                            report.getParameterValues().put("firma_director_path", this.basePath + SofisFileUtils.getPathFromPk(sello.getSfiFirmaSello().getAchPk()));
                        }
                        report.getParameterValues().put("nombre_director", sello.getSfiPersona().getPerNombreCompleto());
                    } else if (Constantes.TIPO_REPRESENTANTE_ENCARGADO_REGISTRO_ACADEMICO_CODIGO.equals(sello.getSfiTipoRepresentante().getTreCodigo())) {
                        if (sello.getSfiFirmaSello() != null) {
                            report.getParameterValues().put("firma_encargado_path", this.basePath + SofisFileUtils.getPathFromPk(sello.getSfiFirmaSello().getAchPk()));
                        }
                        report.getParameterValues().put("nombre_encargado", sello.getSfiPersona().getPerNombreCompleto());
                    }
                }
            }
            
            SgSeccion sec = traslado.getSotConfirmacion() != null ? traslado.getSotConfirmacion().getSotSeccion() :  traslado.getSotSeccion();
            String resolucion = traslado.getSotConfirmacion() != null ? traslado.getSotConfirmacion().getSotResolucion() : traslado.getSotResolucion();
            LocalDate fechaTraslado = traslado.getSotConfirmacion() != null ? traslado.getSotConfirmacion().getSotFechaTraslado() : traslado.getSotFechaTraslado();

            report.getParameterValues().put("sedeOrigen", traslado.getSotSedeOrigen().getSedCodigoNombre());
            report.getParameterValues().put("sedeDestino", traslado.getSotSedeSolicitante().getSedCodigoNombre());
            report.getParameterValues().put("gradoOrigen", traslado.getSotServicioEducativoOrigen().getSduGrado().getGraNombre());
            report.getParameterValues().put("gradoDestino", traslado.getSotServicioEducativoSolicitado().getSduGrado().getGraNombre());
            report.getParameterValues().put("seccionDestino", sec.getNombreSeccion());
            report.getParameterValues().put("nie", traslado.getSotEstudiante().getEstPersona().getPerNie() != null ? traslado.getSotEstudiante().getEstPersona().getPerNie().toString() : "");
            report.getParameterValues().put("nombre", traslado.getSotEstudiante().getEstPersona().getPerNombreCompleto());
            report.getParameterValues().put("sexo", traslado.getSotEstudiante().getEstPersona().getPerSexo().getSexNombre());
            report.getParameterValues().put("fechaNacimiento", Generales.getDateFormat(traslado.getSotEstudiante().getEstPersona().getPerFechaNacimiento()));
            report.getParameterValues().put("fechaTraslado", Generales.getDateFormat(fechaTraslado));
            report.getParameterValues().put("motivo", traslado.getSotMotivoTraslado() != null ? traslado.getSotMotivoTraslado().getMotNombre() : "");
            report.getParameterValues().put("observaciones", resolucion != null ? resolucion : "");

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

}
