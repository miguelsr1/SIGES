/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgAsistencia;
import sv.gob.mined.siges.web.dto.SgCalendario;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsistencia;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsistenciaRestClient;
import sv.gob.mined.siges.web.restclient.CalendarioRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class AsistenciasEstudiantesGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private AsistenciaRestClient asistenciasClient;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private SelloFirmaRestClient selloFirmaClient;

    @Inject
    private MatriculaRestClient matriculaClient;
    
    @Inject
    private CalendarioRestClient calendrioClient;

    private static final Logger LOGGER = Logger.getLogger(AsistenciasEstudiantesGenerator.class.getName());

    public MasterReport generarReporte(Long seccionPk, Long estudiantePk, Integer mes) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (seccionPk == null) {
                be.addError("Debes ingresar una sección.");
                throw be;
            }
            if (mes == null || mes < 1 || mes > 12) {
                be.addError("Debes ingresar un mes válido.");
                throw be;
            }

            FiltroSeccion filSec = new FiltroSeccion();

            FiltroAsistencia filtroAsis = new FiltroAsistencia();
            filtroAsis.setIncluirCampos(new String[]{
                "asiControl.cacFecha",
                "asiInasistencia",
                "asiMotivoInasistencia.minMotivoJustificado",
                "asiEstudiante.estPk"
            });
            filtroAsis.setOrderBy(new String[]{"asiEstudiante.estPersona.perPrimerApellidoBusqueda", "asiEstudiante.estPersona.perSegundoApellidoBusqueda",
                "asiEstudiante.estPersona.perPrimerNombreBusqueda", "asiEstudiante.estPersona.perSegundoNombreBusqueda"});
            filtroAsis.setAscending(new boolean[]{true, true, true, true});

            if (seccionPk != null) {
                filSec.setSecPk(seccionPk);
                filtroAsis.setAsiSeccion(seccionPk);
            } 

            List<SgSeccion> secs = seccionClient.buscar(filSec);
            if (secs.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_NO_ENCONTRADA));
                throw be;
            }
            
            SgSeccion sec = secs.get(0);
            
            
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_ASISTENCIAS_ESTUDIANTE_CODIGO,
                    sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_ASISTENCIAS_ESTUDIANTE_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("asistencias_estudiantes.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            
            
            FiltroCalendario fc=new FiltroCalendario();
            fc.setAnioLectivoFk(sec.getSecAnioLectivo().getAlePk());
            fc.setTipoCalendarioPk(sec.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());
            fc.setIncluirCampos(new String[]{"calTipoCalendario.tceCodigo","calFechaInicio","calFechaFin"});
            List<SgCalendario> cal=calendrioClient.buscar(fc);
            
            LocalDate fechaDesde=YearMonth.of(sec.getSecAnioLectivo().getAleAnio(), mes).atDay(1);
            LocalDate fechaHasta=YearMonth.of(sec.getSecAnioLectivo().getAleAnio(), mes).atEndOfMonth();
            Integer aleAnio=sec.getSecAnioLectivo().getAleAnio();
            if(!cal.isEmpty() && cal.get(0).getCalTipoCalendario().getTceCodigo().equals(Constantes.CALENDARIO_INTERNACIONAL)){
                if(YearMonth.from(fechaDesde).compareTo(YearMonth.from(cal.get(0).getCalFechaInicio()))<1){
                    
                    fechaDesde=YearMonth.of(sec.getSecAnioLectivo().getAleAnio() +1, mes).atDay(1);
                    fechaHasta=YearMonth.of(sec.getSecAnioLectivo().getAleAnio()+1, mes).atEndOfMonth();
                    aleAnio++;
                }
            }

            List<SgEstudiante> estudiantes = buscarEstudiantes(seccionPk, estudiantePk, mes, aleAnio);
            if (estudiantes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_SIN_ESTUDIANTES));
                throw be;
            }
            
            
            //Busco todas las asistencias de los estudiantes de la sección en el mes
            filtroAsis.setAsiFechaDesde(fechaDesde);
            filtroAsis.setAsiFechaHasta(fechaHasta);
            List<SgAsistencia> asistencias = asistenciasClient.buscar(filtroAsis);

            FiltroSelloFirma filtroSelloFirma = new FiltroSelloFirma();
            filtroSelloFirma.setSfiHabilitado(Boolean.TRUE);
            filtroSelloFirma.setSedPk(sec.getSecServicioEducativo().getSduSede().getSedPk());
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

            report.setDataFactory(this.getDataFactory(estudiantes, asistencias, sec, mes,aleAnio));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    public List<SgEstudiante> buscarEstudiantes(Long secPk, Long estPk, Integer mes, Integer anio) throws Exception {
        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(secPk);
        filtroMat.setMatEstudiantePk(estPk);
        filtroMat.setMatFechaDesdeSup(YearMonth.of(anio, mes).atDay(1));
        filtroMat.setMatFechaHastaSup(YearMonth.of(anio, mes).atEndOfMonth());
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
            "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
            "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
            "matEstudiante.estVersion"});
        filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellidoBusqueda", "matEstudiante.estPersona.perPrimerNombreBusqueda"});
        filtroMat.setAscending(new boolean[]{true, true});
        List<SgMatricula> matriculas = matriculaClient.buscar(filtroMat);
        List<SgEstudiante> estudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).collect(Collectors.toList());
        return estudiantes;
    }

    private DataFactory getDataFactory(List<SgEstudiante> estudiantes, List<SgAsistencia> asistencias, SgSeccion seccion, Integer mes,Integer anio) throws Exception {

        //Preprocesamiento. Agrupar asistencias por estudiante
        HashMap<SgEstudiante, List<SgAsistencia>> estAsis = new HashMap<>();

        for (int i = 0; i < estudiantes.size(); i++) {
            estAsis.put(estudiantes.get(i), new ArrayList<>());
        }

        for (SgAsistencia a : asistencias) {
            estAsis.computeIfAbsent(a.getAsiEstudiante(), s -> new ArrayList<>()).add(a);
        }

        //Se puede extender para realizar reporte para todos los meses del año. Se debería agregar una row por mes.
        YearMonth yearMonth = YearMonth.of(anio, mes);
        String mesTexto = yearMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));

        TypedTableModel model = new TypedTableModel();
        model.addColumn("mes", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("anio_lectivo", String.class);
        model.addColumn("grado", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("asistencias", TypedTableModel.class);
        model.addColumn("cant_dias_mes", Integer.class);
        model.addRow(
                mesTexto.substring(0, 1).toUpperCase() + mesTexto.substring(1) + "/" + yearMonth.getYear(),
                seccion.getSecServicioEducativo().getSduSede().getSedCodigoNombre(),
                seccion.getSecPlanEstudio().getPesNombre(),
                anio,
                seccion.getSecServicioEducativo().getSduGrado().getGraNombre(),
                seccion.getNombreSeccion(),
                seccion.getSecServicioEducativo().getSduNombre(),
                this.getAsistenciasTableModel(estudiantes, estAsis, yearMonth),
                yearMonth.lengthOfMonth()
        );

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    private TypedTableModel getAsistenciasTableModel(List<SgEstudiante> estudiantes, HashMap<SgEstudiante, List<SgAsistencia>> asistenciasEstudiante, YearMonth yearMonth) throws Exception {

        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie", String.class);
        model.addColumn("nombre", String.class);
        model.addColumn("1", String.class);
        model.addColumn("2", String.class);
        model.addColumn("3", String.class);
        model.addColumn("4", String.class);
        model.addColumn("5", String.class);
        model.addColumn("6", String.class);
        model.addColumn("7", String.class);
        model.addColumn("8", String.class);
        model.addColumn("9", String.class);
        model.addColumn("10", String.class);
        model.addColumn("11", String.class);
        model.addColumn("12", String.class);
        model.addColumn("13", String.class);
        model.addColumn("14", String.class);
        model.addColumn("15", String.class);
        model.addColumn("16", String.class);
        model.addColumn("17", String.class);
        model.addColumn("18", String.class);
        model.addColumn("19", String.class);
        model.addColumn("20", String.class);
        model.addColumn("21", String.class);
        model.addColumn("22", String.class);
        model.addColumn("23", String.class);
        model.addColumn("24", String.class);
        model.addColumn("25", String.class);
        model.addColumn("26", String.class);
        model.addColumn("27", String.class);
        model.addColumn("28", String.class);
        model.addColumn("29", String.class);
        model.addColumn("30", String.class);
        model.addColumn("31", String.class);
        model.addColumn("A", String.class);
        model.addColumn("II", String.class);
        model.addColumn("IJ", String.class);
        model.addColumn("IT", String.class);
        model.addColumn("T", String.class);

        //“+”=Asiste “-“=Falta “N”=No se pasó lista “/”=Día No lectivo
        for (SgEstudiante e : estudiantes) {

            Object[] asistencias = new Object[31];

            Integer cantAsistencias = yearMonth.lengthOfMonth();
            Integer cantInasistenciasJustificadas = 0;
            Integer cantInasistenciasInjustificadas = 0;

            Arrays.fill(asistencias, "N");
            for (int i = (yearMonth.lengthOfMonth()); i < 31; i++) {
                asistencias[i] = "";
            }

            for (SgAsistencia a : asistenciasEstudiante.get(e)) {
                asistencias[a.getAsiControl().getCacFecha().getDayOfMonth() - 1] = a.getAsiInasistencia() ? "-" : "+";
                if (a.getAsiInasistencia()) {
                    cantAsistencias--;
                    if (a.getAsiMotivoInasistencia().getMinMotivoJustificado()) {
                        cantInasistenciasJustificadas++;
                    } else {
                        cantInasistenciasInjustificadas++;
                    }
                }
            }

            model.addRow(
                    e.getEstPersona().getPerNie(),
                    e.getEstPersona().getPerNombreApellido(),
                    asistencias[0],
                    asistencias[1],
                    asistencias[2],
                    asistencias[3],
                    asistencias[4],
                    asistencias[5],
                    asistencias[6],
                    asistencias[7],
                    asistencias[8],
                    asistencias[9],
                    asistencias[10],
                    asistencias[11],
                    asistencias[12],
                    asistencias[13],
                    asistencias[14],
                    asistencias[15],
                    asistencias[16],
                    asistencias[17],
                    asistencias[18],
                    asistencias[19],
                    asistencias[20],
                    asistencias[21],
                    asistencias[22],
                    asistencias[23],
                    asistencias[24],
                    asistencias[25],
                    asistencias[26],
                    asistencias[27],
                    asistencias[28],
                    asistencias[29],
                    asistencias[30],
                    cantAsistencias,
                    cantInasistenciasInjustificadas,
                    cantInasistenciasJustificadas,
                    cantInasistenciasInjustificadas + cantInasistenciasJustificadas,
                    cantAsistencias + cantInasistenciasInjustificadas + cantInasistenciasJustificadas);
        }
        return model;
    }

}
