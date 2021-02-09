/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
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
import sv.gob.mined.siges.utils.ConvertidorNumeroAPalabraUtils;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.utils.SpanishConverter;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgConfiguracion;
import sv.gob.mined.siges.web.dto.SgEstudianteImpresion;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.dto.SgSelloFirmaTitular;
import sv.gob.mined.siges.web.dto.SgSolicitudImpresion;
import sv.gob.mined.siges.web.dto.SgTitulo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudianteImpresion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTitulo;
import sv.gob.mined.siges.web.restclient.CatalogoRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteImpresionRestClient;
import sv.gob.mined.siges.web.restclient.SolicitudImpresionRestClient;
import sv.gob.mined.siges.web.restclient.TituloRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class TituloEstudianteGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "service.titulos.baseUrl")
    private String titulosURL;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private EstudianteImpresionRestClient estudianteImpresionClient;

    @Inject
    private CatalogoRestClient catalogoRestClient;

    @Inject
    private TituloRestClient tituloRestClient;

    @Inject
    private SolicitudImpresionRestClient solicitudImpresionRestClient;

    private static final Logger LOGGER = Logger.getLogger(TituloEstudianteGenerator.class.getName());

    Boolean respetaOrden;

    public MasterReport generarReporte(String hash, String estudianteImp, String solicitudImp) throws Exception {

        try {
            BusinessException be = new BusinessException();

            if (StringUtils.isBlank(hash) && StringUtils.isBlank(estudianteImp) && StringUtils.isBlank(solicitudImp)) {
                be.addError("Parámetros vacios");
                throw be;
            }

            SgSolicitudImpresion sol = new SgSolicitudImpresion();
            SgTitulo titulo = new SgTitulo();
            List<SgTitulo> tituloList = new ArrayList();

            if (!StringUtils.isBlank(hash) || !StringUtils.isBlank(solicitudImp)) {
                FiltroTitulo ftit = new FiltroTitulo();
                ftit.setTitHash(hash);
                ftit.setTitSolicitudImpresion(!StringUtils.isBlank(solicitudImp) ? Long.parseLong(solicitudImp) : null);
                ftit.setIncluirCampos(new String[]{
                    "titSelloFirmaTitularAutenticaFk.sftFirmaSello.achPk",
                    "titSelloFirmaTitularAutenticaFk.sftPrimerNombre",
                    "titSelloFirmaTitularAutenticaFk.sftSegundoNombre",
                    "titSelloFirmaTitularAutenticaFk.sftPrimerApellido",
                    "titSelloFirmaTitularAutenticaFk.sftSegundoApellido",
                    "titSelloFirmaTitularMinistroFk.sftFirmaSello.achPk",
                    "titSelloFirmaTitularMinistroFk.sftPrimerNombre",
                    "titSelloFirmaTitularMinistroFk.sftSegundoNombre",
                    "titSelloFirmaTitularMinistroFk.sftPrimerApellido",
                    "titSelloFirmaTitularMinistroFk.sftSegundoApellido",
                    "titSelloFirmaDirectorFk.sfiFirmaSello.achPk",
                    "titSelloFirmaDirectorFk.sfiPersona.perPrimerNombre",
                    "titSelloFirmaDirectorFk.sfiPersona.perSegundoNombre",
                    "titSelloFirmaDirectorFk.sfiPersona.perPrimerApellido",
                    "titSelloFirmaDirectorFk.sfiPersona.perSegundoApellido",
                    "titFechaEmision",
                    "titNombreEstudiante",
                    "titEstudianteFk.estPersona.perNie",
                    "titSedeCodigo",
                    "titSedeNombre",
                    "titNombreCertificado",
                    "titSolicitudImpresionFk.simEstado",
                    "titSolicitudImpresionFk.simDefinicionTitulo.dtiPlantilla.plaArchivo.achPk",
                    "titFechaValidez",
                    "titDuiEstudiante",
                    "titFechaLegalizacionTitulo",
                    "titNumeroResolucion",
                    "titNumeroRegistro",
                    "titTituloAnterior2008",
                    "titEsAnterior2008",
                    "titNombreTituloPosterior2008"
                });

                tituloList = tituloRestClient.buscar(ftit);
                if (tituloList.isEmpty()) {
                    be.addError("Titulo no encontrado");
                    throw be;
                }
                titulo = tituloList.get(0);
                sol = titulo.getTitSolicitudImpresionFk();
            }

            if (!StringUtils.isBlank(estudianteImp)) {
                Long id = Long.parseLong(estudianteImp);
                FiltroEstudianteImpresion filei = new FiltroEstudianteImpresion();
                filei.setEimPk(id);
                filei.setIncluirCampos(new String[]{
                    "eimEstudiante.estPersona.perNombrePartidaNacimiento",
                    "eimEstudiante.estPersona.perPrimerNombre",
                    "eimEstudiante.estPersona.perSegundoNombre",
                    "eimEstudiante.estPersona.perTercerNombre",
                    "eimEstudiante.estPersona.perPrimerApellido",
                    "eimEstudiante.estPersona.perSegundoApellido",
                    "eimEstudiante.estPersona.perTercerApellido",
                    "eimEstudiante.estPersona.perNie",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduSede.sedTipo",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduSede.sedNombre",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedCodigo",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedTipo",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedNombre",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduOpcion.opcSectorEconomico.secNombre",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduOpcion.opcNombre",
                    "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "eimSolicitudImpresion.simSelloAutentica.sftFirmaSello.achPk",
                    "eimSolicitudImpresion.simSelloAutentica.sftPrimerNombre",
                    "eimSolicitudImpresion.simSelloAutentica.sftSegundoNombre",
                    "eimSolicitudImpresion.simSelloAutentica.sftPrimerApellido",
                    "eimSolicitudImpresion.simSelloAutentica.sftSegundoApellido",
                    "eimSolicitudImpresion.simSelloMinistro.sftFirmaSello.achPk",
                    "eimSolicitudImpresion.simSelloMinistro.sftPrimerNombre",
                    "eimSolicitudImpresion.simSelloMinistro.sftSegundoNombre",
                    "eimSolicitudImpresion.simSelloMinistro.sftPrimerApellido",
                    "eimSolicitudImpresion.simSelloMinistro.sftSegundoApellido",
                    "eimSolicitudImpresion.simSelloDirector.sfiFirmaSello.achPk",
                    "eimSolicitudImpresion.simSelloDirector.sfiPersona.perPrimerNombre",
                    "eimSolicitudImpresion.simSelloDirector.sfiPersona.perSegundoNombre",
                    "eimSolicitudImpresion.simSelloDirector.sfiPersona.perPrimerApellido",
                    "eimSolicitudImpresion.simSelloDirector.sfiPersona.perSegundoApellido",
                    "eimSolicitudImpresion.simEstado",
                    "eimSolicitudImpresion.simFechaEnviadoImprimir",
                    "eimSolicitudImpresion.simDefinicionTitulo.dtiNombreCertificado",
                    "eimSolicitudImpresion.simDefinicionTitulo.dtiPlantilla.plaArchivo.achPk",});

                List<SgEstudianteImpresion> imps = estudianteImpresionClient.buscar(filei);

                if (imps.isEmpty()) {
                    be.addError("Solicitudes estudiante no encontradas. ");
                    throw be;
                }

                SgEstudianteImpresion imp = imps.get(0);
                sol = imp.getEimSolicitudImpresion();

                SgSede sedeTitulo = sol.getSimSeccion().getSecServicioEducativo().getSduSede();
                if (sol.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe() != null) {
                    sedeTitulo = new SgSede(sol.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedPk(), sol.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedVersion());
                    sedeTitulo.setSedCodigo(sol.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedCodigo());
                    sedeTitulo.setSedNombre(sol.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedNombre());
                }
                titulo = new SgTitulo();
                titulo.setTitNombreEstudiantePartida(imp.getEimEstudiante().getEstPersona().getPerNombrePartidaNacimiento());
                titulo.setTitNombreEstudiante(imp.getEimEstudiante().getEstPersona().getPerNombreCompletoNP2());
                titulo.setTitEstudianteFk(imp.getEimEstudiante());
                titulo.setTitSedeCodigo(sedeTitulo.getSedCodigo());
                titulo.setTitSedeNombre(sedeTitulo.getSedNombre());

                titulo.setTitFechaEmision(imp.getEimSolicitudImpresion().getSimFechaEnviadoImprimir());
                titulo.setTitSelloFirmaTitularAutenticaFk(sol.getSimSelloAutentica());
                titulo.setTitSelloFirmaTitularMinistroFk(sol.getSimSelloMinistro());
                titulo.setTitSelloFirmaDirectorFk(sol.getSimSelloDirector());
                titulo.setTitSolicitudImpresionFk(sol);

                String nombreCerificado = imp.getEimSolicitudImpresion().getSimDefinicionTitulo().getDtiNombreCertificado();
                if (imp.getEimSolicitudImpresion().getSimDefinicionTitulo() != null && imp.getEimSolicitudImpresion().getSimSeccion() != null) {
                    if (imp.getEimSolicitudImpresion().getSimDefinicionTitulo().getDtiNombreCertificado().contains("{OPCION}")) {

                        nombreCerificado = nombreCerificado.replace("{OPCION}", imp.getEimSolicitudImpresion().getSimSeccion().getSecServicioEducativo().getSduOpcion() != null ? "" + imp.getEimSolicitudImpresion().getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcNombre() : "");
                    }

                    if (imp.getEimSolicitudImpresion().getSimDefinicionTitulo().getDtiNombreCertificado().contains("{PROGRAMA}")) {
                        nombreCerificado = nombreCerificado.replace("{PROGRAMA}", imp.getEimSolicitudImpresion().getSimSeccion().getSecServicioEducativo().getSduProgramaEducativo() != null ? "" + imp.getEimSolicitudImpresion().getSimSeccion().getSecServicioEducativo().getSduProgramaEducativo().getPedNombre() : "");
                    }

                    if (imp.getEimSolicitudImpresion().getSimDefinicionTitulo().getDtiNombreCertificado().contains("{SECTOR}")) {
                        nombreCerificado = nombreCerificado.replace("{SECTOR}", imp.getEimSolicitudImpresion().getSimSeccion().getSecServicioEducativo().getSduOpcion() != null && imp.getEimSolicitudImpresion().getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico() != null ? "" + imp.getEimSolicitudImpresion().getSimSeccion().getSecServicioEducativo().getSduOpcion().getOpcSectorEconomico().getSecNombre() : "");
                    }
                }
                titulo.setTitNombreCertificado(nombreCerificado);
                tituloList.add(titulo);
            }

            if (sol.getSimEstado() == null || EnumEstadoSolicitudImpresion.ENVIADA.equals(sol.getSimEstado())) {
                be.addError("Estado solicitud incorrecto");
                throw be;
            }

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = titulo.getTitSolicitudImpresionFk().getSimDefinicionTitulo().getDtiPlantilla();
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CALIFICACIONES_ESTUDIANTE_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                //reportDefinitionURL = classloader.getResource("titulo_solicitud.prpt");
                reportDefinitionURL = classloader.getResource("titulo_solicitud_01_F_fija_v8.prpt");
            }

            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.TITULO_FECHA_VALIDEZ_DESDE);
            List<SgConfiguracion> conf = catalogoRestClient.buscarConfiguracion(fc);
            if (conf.isEmpty()) {
                be.addError("No existe configuración de fecha validez");
                throw be;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
            LocalDate localDate = LocalDate.parse(conf.get(0).getConValor(), formatter);

            formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
            String fechaValidez = formatter.format(localDate);


            String fechaValidezEnLetras =convertirFechaFormatoLetras(localDate);

            
           
//            if(StringUtils.isBlank(titulo.getTitNombreEstudiantePartida())){
//                be.addError("El estudiante  no tiene nombre en partida de nacimiento");
//                throw be;
//            }
            SgSelloFirmaTitular sfAutentica = titulo.getTitSelloFirmaTitularAutenticaFk();
            if (sfAutentica != null) {
                if (sfAutentica.getSftFirmaSello() != null) {
                    report.getParameterValues().put("firma_sello_autentica_path", this.basePath + SofisFileUtils.getPathFromPk(sfAutentica.getSftFirmaSello().getAchPk()));
                }
                report.getParameterValues().put("nombre_autentica", sfAutentica.getSftNombreCompleto());
            }

            SgSelloFirma sfDirector = titulo.getTitSelloFirmaDirectorFk();
            if (sfDirector != null) {
                if (sfDirector.getSfiFirmaSello() != null) {
                    report.getParameterValues().put("firma_sello_director_path", this.basePath + SofisFileUtils.getPathFromPk(sfDirector.getSfiFirmaSello().getAchPk()));
                }
                report.getParameterValues().put("nombre_director", sfDirector.getSfiPersona().getPerNombreCompletoNP());
            }

            SgSelloFirmaTitular sfMinistro = titulo.getTitSelloFirmaTitularMinistroFk();
            if (sfMinistro != null) {
                if (sfMinistro.getSftFirmaSello() != null) {
                    report.getParameterValues().put("firma_sello_ministro_path", this.basePath + SofisFileUtils.getPathFromPk(sfMinistro.getSftFirmaSello().getAchPk()));
                }
                report.getParameterValues().put("nombre_ministro", sfMinistro.getSftNombreCompleto());
            }

            TypedTableModel model = new TypedTableModel();
            model.addColumn("nombre_persona", String.class);
            model.addColumn("nie_persona", String.class);
            model.addColumn("codigo_nombre_sede", String.class);
            model.addColumn("nombre_sede", String.class);
            model.addColumn("titulo_obtenido", String.class);
            model.addColumn("validez_desde", String.class);
            model.addColumn("fecha_emision", String.class);
            model.addColumn("validez_desde_letras", String.class);
            model.addColumn("fecha_emision_letras", String.class);
            model.addColumn("codigo_qr", String.class);

            //Datos exclusivos de reposición
            model.addColumn("rep_dui_persona", String.class);
            model.addColumn("rep_fecha_legalizacion", String.class);
            model.addColumn("rep_titulo_nombre", String.class);
            model.addColumn("rep_numero_resolucion", Integer.class);
            model.addColumn("rep_numero_registro", Integer.class);

            formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
            String fechaEmision = formatter.format(LocalDate.now());
            
            String fechaEmisionEnLetras = convertirFechaFormatoLetras(LocalDate.now());

            for (SgTitulo tit : tituloList) {
                String fechaValidezTitulo=fechaValidez;
                String fechaValidezTituloLetras=fechaValidezEnLetras;                
                if(tit.getTitFechaValidez()!=null){
                    DateTimeFormatter formatterValidez = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
                    fechaValidezTitulo=formatterValidez.format(tit.getTitFechaValidez());                   
                   fechaValidezTituloLetras=convertirFechaFormatoLetras(tit.getTitFechaValidez());
                }
                
                String fechaEmisionTitulo=fechaEmision;
                String fechaEmisionTituloLetras=fechaEmisionEnLetras;
                if(tit.getTitFechaEmision()!=null){
                    DateTimeFormatter formatterValidez = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
                    fechaEmisionTitulo=formatterValidez.format(tit.getTitFechaEmision());                   
                    fechaEmisionTituloLetras=convertirFechaFormatoLetras(tit.getTitFechaEmision());
                }
                
                String path = null;
                if (tit.getTitPk() != null) {
                    path = generarQR(tit.getTitPk().toString());
                }

                model.addRow(tit.getTitNombreEstudiante(),
                        tit.getTitEstudianteFk() != null ? tit.getTitEstudianteFk().getEstPersona().getPerNie() : null,
                        tit.getTitSedeCodigo() + " - " + tit.getTitSedeNombre(),
                        tit.getTitSedeNombre(),
                        tit.getTitNombreCertificado(),
                        fechaValidezTitulo,
                        fechaEmisionTitulo,
                        fechaValidezTituloLetras,
                        fechaEmisionTituloLetras,
                        path,
                        tit.getTitDuiEstudiante(),
                        convertirFechaFormatoLetras(tit.getTitFechaLegalizacionTitulo()),
                        tit.getTitNombreTituloReposicion(),
                        tit.getTitNumeroResolucion(),
                        tit.getTitNumeroRegistro()
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

    private String convertirFechaFormatoLetras(LocalDate fecha) {
        if(fecha==null){
            return null;
        }
        SpanishConverter sc = new SpanishConverter();
        String dia = sc.convert((Integer) fecha.getDayOfMonth());
        String anio = ConvertidorNumeroAPalabraUtils.convertirLetras(fecha.getYear());
        anio = anio.trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("es"));
        String mes = formatter.format(fecha);

        return dia + " de " + mes + " de " + anio;
    }

    public String generarQR(String estImpPk) throws WriterException, IOException {

        if (!titulosURL.endsWith("/")) {
            titulosURL = titulosURL.concat("/");
        }

        String content = titulosURL + "estudianteTitulo?estudianteTitulo=" + estImpPk;
        String filePath = "";
        String fileType = "png";
        int size = 125;
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        QRCodeWriter qrcode = new QRCodeWriter();
        BitMatrix matrix = qrcode.encode(content, BarcodeFormat.QR_CODE, size, size);
        File qrFile = new File(filePath + randomUUIDString + "." + fileType);
        int matrixWidth = matrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        graphics.setColor(Color.BLACK);

        for (int b = 0; b < matrixWidth; b++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (matrix.get(b, j)) {
                    graphics.fillRect(b, j, 1, 1);
                }
            }
        }
        Path folder = Paths.get(tmpBasePath);
        Path tmpFile = Files.createTempFile(folder, null, ".png");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        Files.copy(is, tmpFile, StandardCopyOption.REPLACE_EXISTING);
        return tmpFile.toString();

    }

}
