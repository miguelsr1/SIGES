/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgMatricula;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgPorcentajeAsistenciasRequest;
import sv.gob.mined.siges.web.dto.SgPorcentajeAsistenciasResponse;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMatricula;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.MatriculaRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.PorcentajeAsistenciaRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class CalificacionesEstudiantesGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private CalificacionEstudianteRestClient calEstudianteClient;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private PorcentajeAsistenciaRestClient porcentajeAsistenciaClient;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private PeriodoCalificacionRestClient peroidoCalificacionRestClient;

    @Inject
    private SelloFirmaRestClient selloFirmaClient;

    @Inject
    private MatriculaRestClient matriculaClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstudioRestClient;

    @Inject
    private ComponentePlanGradoRestClient componentePlanGradoRestClient;

    private static final Logger LOGGER = Logger.getLogger(CalificacionesEstudiantesGenerator.class.getName());

    Boolean respetaOrden;

    public MasterReport generarReporte(Long seccionPk, Long estudiantePk, Long componentePlanEstudioPk) throws Exception {

        try {
            BusinessException be = new BusinessException();
            if (seccionPk == null) {
                be.addError("Debes ingresar una sección.");
                throw be;
            }

            FiltroSeccion filSec = new FiltroSeccion();
            filSec.setSecPk(seccionPk);

            FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
            filtroCalEst.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                "caeCalificacion.calComponentePlanEstudio.cpePk",
                "caeCalificacion.calComponentePlanEstudio.cpeEsPaes",
                "caeCalificacion.calTipoCalendarioEscolar",
                "caeCalificacion.calTipoPeriodoCalificacion",
                "caeCalificacion.calNumero",
                "caeCalificacion.calRangoFecha.rfePk",
                "caeCalificacion.calRangoFecha.rfeFechaHasta",
                "caeCalificacion.calRangoFecha.rfeCodigo",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                "caeCalificacionNumericaEstudiante",
                "caeCalificacionConceptualEstudiante.calCodigo",
                "caeCalificacionConceptualEstudiante.calValor",
                "caeCalificacionConceptualEstudiante.calValorEnCertificado",
                "caeResolucion",
                "caeEstudiante.estPk"
            });
            filtroCalEst.setOrderBy(new String[]{"caeEstudiante.estPersona.perPrimerApellidoBusqueda", "caeEstudiante.estPersona.perSegundoApellidoBusqueda",
                "caeEstudiante.estPersona.perPrimerNombreBusqueda", "caeEstudiante.estPersona.perSegundoNombreBusqueda"});
            filtroCalEst.setAscending(new boolean[]{true, true, true, true});
            filtroCalEst.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                EnumTiposPeriodosCalificaciones.EXORD,
                EnumTiposPeriodosCalificaciones.ORD,
                EnumTiposPeriodosCalificaciones.NOTIN,
                EnumTiposPeriodosCalificaciones.APR})); 
            filtroCalEst.setEstudiantePk(estudiantePk);
            filtroCalEst.setCaeComponentePlanEstudio(componentePlanEstudioPk);
                        
            List<SgSeccion> sec = seccionClient.buscar(filSec);
            if (sec.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_NO_ENCONTRADA));
                throw be;
            }
            SgSeccion seccion = sec.get(0);

            List<SgEstudiante> estudiantes = buscarEstudiantes(seccionPk, estudiantePk);
            if (estudiantes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_SIN_ESTUDIANTES));
                throw be;
            }
            
            
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_CALIFICACIONES_ESTUDIANTE_CODIGO,
                        seccion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_CALIFICACIONES_ESTUDIANTE_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("calificaciones_estudiantes_en_prod_13_12_2019.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();

            respetaOrden = Boolean.FALSE;
            List<SgComponentePlanGrado> cpgList = new ArrayList();

            FiltroRelGradoPlanEstudio frpe = new FiltroRelGradoPlanEstudio();
            frpe.setRgpPlanEstudio(seccion.getSecPlanEstudio().getPesPk());
            frpe.setRgpGrado(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
            frpe.setIncluirCampos(new String[]{"rgpConsiderarOrden"});
            List<SgRelGradoPlanEstudio> relGraList = relGradoPlanEstudioRestClient.buscar(frpe);
            if (!relGraList.isEmpty()) {
                if (BooleanUtils.isTrue(relGraList.get(0).getRgpConsiderarOrden())) {
                    respetaOrden = Boolean.TRUE;
                }
            }

            FiltroComponentePlanGrado fcp = new FiltroComponentePlanGrado();
            fcp.setCpgGradoPk(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
            fcp.setCpgPlanEstudioPk(seccion.getSecPlanEstudio().getPesPk());
            fcp.setCpgComponentePlanEstudioPk(componentePlanEstudioPk);
            fcp.setCpgAgregandoSeccionExclusiva(seccion.getSecPk());
            fcp.setIncluirCampos(new String[]{"cpgOrden", "cpgPrecision",
                "cpgEscalaCalificacion.ecaTipoEscala",
                "cpgEscalaCalificacion.ecaPrecision", "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeNombre", "cpgComponentePlanEstudio.cpeVersion",
                "cpgComponentePlanEstudio.cpeEsPaes",
                "cpgComponentePlanEstudio.cpeTipo"});

            cpgList = componentePlanGradoRestClient.buscar(fcp);

            FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
            fpc.setIncluirCampos(new String[]{
                "pcaNumero",
                "pcaPermiteCalificarSinNie",
                "pcaEsPrueba",
                "pcaNombre",
                "pcaVersion"});
            fpc.setPcaEsPrueba(Boolean.FALSE);
            fpc.setPcaModalidadEducativa(seccion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModPk());
            fpc.setPcaModalidadAtencion(seccion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
            fpc.setPcaSubModalidadAtencion(seccion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? seccion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
            fpc.setPcaAnioLectivo(seccion.getSecAnioLectivo().getAlePk());
            fpc.setPcaTipoCalendario(seccion.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());
            
            //En caso de que la sección sea semestral hay que buscar periodo del semestre correcto
            fpc.setPcaTipoPeriodo(seccion.getSecTipoPeriodo());
            fpc.setPcaNumeroPeriodo(seccion.getSecNumeroPeriodo());
            
            List<SgPeriodoCalificacion> listPeriodoCalif = peroidoCalificacionRestClient.buscar(fpc);

            //Busco todas las calificaciones de los estudiantes de la sección en el año lectivo de la misma
            filtroCalEst.setCaeEstudiantesPk(estudiantes.stream().map(e -> e.getEstPk()).collect(Collectors.toList()));
            filtroCalEst.setAnioLectivoPk(sec.get(0).getSecAnioLectivo().getAlePk());
            
 
            filtroCalEst.setCaeGradoFk(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
            //Filtra dependiendo si sección es anual o semestral
            filtroCalEst.setCaeTipoPeriodo(seccion.getSecTipoPeriodo());
            filtroCalEst.setCaeNumeroPeriodo(seccion.getSecNumeroPeriodo());
            

            List<SgCalificacionEstudiante> calificaciones = calEstudianteClient.buscar(filtroCalEst);
            if (calificaciones.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_SIN_CALIFICACIONES));
                throw be;
            }
            if (!cpgList.isEmpty()) {
                AsignarCpg:
                for (SgComponentePlanGrado cpg : cpgList) {
                    for (SgCalificacionEstudiante calel : calificaciones) {
                        if (calel.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(cpg.getCpgComponentePlanEstudio().getCpePk())) {
                            //Si tiene calificaciones en otros componente plan de estudio que no estan en la seccion queda null
                            //Esto se puede dar si tuvo calificaciones y luego se lo traslado a otra seccion
                            calel.getCaeCalificacion().getCalComponentePlanEstudio().setCpeComponentePlanGrado(cpg);
                        }
                    }
                }
            }

            //Sacamos las calificaciones que sean de otro periodo distinto al de la seccion. 
            //Puede haber pasado que el estudiante haya sido calificado en otro periodo en otra seccion
            Iterator<SgCalificacionEstudiante> it = calificaciones.iterator();
            while (it.hasNext()) {
                SgCalificacionEstudiante cal = it.next();

                if (cal.getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado() == null) {
                    // Se ignoran calificaciones de otros componentes plan grado de otros planes de estudio
                    it.remove();
                    continue;
                }

                //Hay que verificar que el periodo de las calificaciones sea uno de los periodos soportados por la seccion
                //puede pasar que haya sido calificado para matematica en un periodo X
                //luego lo cambiaron de seccion y lo volvieron a calificar en matematica en un periodo Y
                //tenemos que ignorar las calificaciones de x
                if (cal.getCaeCalificacion().getCalRangoFecha() != null) {           
                    if (!listPeriodoCalif.contains(cal.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion())){
                        it.remove();
                    }
                }
            }
            
            FiltroSelloFirma filtroSelloFirma = new FiltroSelloFirma();
            filtroSelloFirma.setSfiHabilitado(Boolean.TRUE);
            filtroSelloFirma.setSedPk(sec.get(0).getSecServicioEducativo().getSduSede().getSedPk());
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

            report.setDataFactory(this.getDataFactory(estudiantes, calificaciones, sec.get(0)));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }

    public List<SgEstudiante> buscarEstudiantes(Long secPk, Long estPk) throws Exception {
        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(secPk);
        filtroMat.setMatEstudiantePk(estPk);
        if (estPk == null) {
            filtroMat.setMatRetirada(Boolean.FALSE);
        }
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
            "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
            "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
            "matEstudiante.estVersion"});
        filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellidoBusqueda", "matEstudiante.estPersona.perSegundoApellidoBusqueda",
            "matEstudiante.estPersona.perPrimerNombreBusqueda"});
        filtroMat.setAscending(new boolean[]{true, true, true});
        List<SgMatricula> matriculas = matriculaClient.buscar(filtroMat);
        List<SgEstudiante> estudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).collect(Collectors.toList());
        return estudiantes;
    }

    private DataFactory getDataFactory(List<SgEstudiante> estudiantes, List<SgCalificacionEstudiante> calificaciones, SgSeccion seccion) throws Exception {

        //Preprocesamiento. Agrupar calificaciones por componente plan estudio
        HashMap<SgEstudiante, HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>>> estCal = new HashMap<>();

        for (int i = 0; i < estudiantes.size(); i++) {
            estCal.put(estudiantes.get(i), new HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>>());
        }

        for (SgCalificacionEstudiante cal : calificaciones) {
            estCal.computeIfAbsent(cal.getCaeEstudiante(), s -> new HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>>())
                    .computeIfAbsent(cal.getCaeCalificacion().getCalComponentePlanEstudio(), s -> new ArrayList<>()).add(cal);
        }

        SgPorcentajeAsistenciasRequest pareq = new SgPorcentajeAsistenciasRequest();
        List<SgEstudiante> listEst = new ArrayList(estCal.keySet());
        pareq.setPinEstudiantes(listEst);
        pareq.setPinAnioLectivo(seccion.getSecAnioLectivo().getAlePk());
        List<SgPorcentajeAsistenciasResponse> paresList = porcentajeAsistenciaClient.buscarListaPorcentaje(pareq);

        HashMap<SgPeriodoCalificacion, List<SgRangoFecha>> periodoRangos = new HashMap<>();
        HashMap<SgPeriodoCalificacion, Integer> periodosCantRangosMax = new HashMap<>();

        Integer cantRangosMax = null;
        for (SgCalificacionEstudiante c : calificaciones) {
            if (c.getCaeCalificacion().getCalRangoFecha() != null) {
                SgPeriodoCalificacion periodo = c.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion();
                if (periodoRangos.containsKey(periodo)) {
                    continue;
                }
                List<SgRangoFecha> rangos = peroidoCalificacionRestClient.obtenerPorId(periodo.getPcaPk()).getPcaRangoFecha();
                rangos.sort(Comparator.comparing(o -> o.getRfeFechaHasta()));
                periodoRangos.put(periodo, rangos);
                periodosCantRangosMax.put(periodo, periodo.getPcaNumero());
            }
        }

        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie_nombre", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("grado", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("anio", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("asistencias", Integer.class);
        model.addColumn("inasistencias_j", Integer.class);
        model.addColumn("inasistencias_sj", Integer.class);
        model.addColumn("cant_periodos", Integer.class);
        model.addColumn("render_calificaciones_header", Boolean.class);
        model.addColumn("calificaciones", TypedTableModel.class);

        for (SgEstudiante est : estCal.keySet()) {
            SgPorcentajeAsistenciasResponse porcentajeAsis = new SgPorcentajeAsistenciasResponse();
            for (SgPorcentajeAsistenciasResponse pasis : paresList) {
                if (pasis.getPinEstudiante().getEstPk().equals(est.getEstPk())) {
                    porcentajeAsis = pasis;
                    break;
                }
            }

            model.addRow(
                    est.getEstPersona().getPerNie() + " - " + est.getEstPersona().getPerNombreCompleto(),
                    seccion.getSecServicioEducativo().getSduSede().getSedCodigoNombre(),
                    seccion.getSecPlanEstudio().getPesNombre(),
                    seccion.getSecServicioEducativo().getSduGrado().getGraNombre(),
                    seccion.getNombreSeccion(),
                    seccion.getSecAnioLectivo().getAleAnio(),
                    seccion.getSecServicioEducativo().getSduNombre(),
                    porcentajeAsis.getPinCantidadAsistencias(),
                    porcentajeAsis.getPinCantidadInasistenciasJustificadas(),
                    porcentajeAsis.getPinCantidadInasistenciasSinJustificar(),
                    cantRangosMax,
                    estCal.get(est).size() > 0,
                    this.getEstudianteCalificationsTableModel(estCal.get(est), periodoRangos, periodosCantRangosMax)
            );
        }
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    private TypedTableModel getEstudianteCalificationsTableModel(HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>> calificComponente, HashMap<SgPeriodoCalificacion, List<SgRangoFecha>> periodosRangos, HashMap<SgPeriodoCalificacion, Integer> periodosCantRangosMax) throws Exception {

        TypedTableModel calt = new TypedTableModel();
        calt.addColumn("cant_periodos", Integer.class);
        calt.addColumn("cal_periodos", TypedTableModel.class);

        //Agrupar calificaciones de componente por período
        HashMap<SgPeriodoCalificacion, HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>>> calificaciones = new HashMap<>();

        List<SgCalificacionEstudiante> calificacionAprobacionPAES = new ArrayList();

        for (SgComponentePlanEstudio c : calificComponente.keySet()) {

            List<SgCalificacionEstudiante> cals = calificComponente.get(c);
            if (cals != null) {
                if (BooleanUtils.isTrue(c.getCpeEsPaes())) {
                    calificacionAprobacionPAES.addAll(cals);

                } else {
                    forCalificaciones:
                    for (SgCalificacionEstudiante ce : cals) {
                        if (ce.getCaeCalificacion().getCalRangoFecha() != null) {
                            if (!calificaciones.containsKey(ce.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion())) {
                                calificaciones.put(ce.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion(), new HashMap<>());
                            }
                            calificaciones.get(ce.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion()).put(c, cals);
                            break forCalificaciones;
                        }
                    }
                }

            }
        }

        List<SgPeriodoCalificacion> periodos = new ArrayList<>(calificaciones.keySet());

        periodos.sort(Comparator.comparing(o -> o.getPcaNumero()));

        for (int i = 0; i < periodos.size(); i++) {
            SgPeriodoCalificacion periodo = periodos.get(i);
            calt.addRow(periodo.getPcaNumero(),
                    this.getCalificationsTableModel(calificaciones.get(periodo),
                            periodosRangos.get(periodo),
                            periodosCantRangosMax.get(periodo)));
        }
        if (!calificacionAprobacionPAES.isEmpty()) {
            calt.addRow(0, getCalificationsPAESTableModel(calificacionAprobacionPAES));
        }

        return calt;
    }

    private TypedTableModel getCalificationsTableModel(HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>> planEstCal, List<SgRangoFecha> rangos, Integer cantRangosMax) {

        TypedTableModel a = new TypedTableModel();
        a.addColumn("componente_plan_estudio", String.class);
        for (int f = 1; f <= cantRangosMax; f++) {
            a.addColumn("p" + f, String.class);
        }
        a.addColumn("ni", String.class);
        a.addColumn("pp", String.class);
        a.addColumn("pps", String.class);
        a.addColumn("sp", String.class);
        a.addColumn("sps", String.class);
        a.addColumn("nf", String.class);
        a.addColumn("resultado", String.class);

        List<SgComponentePlanEstudio> componentes = new ArrayList<>(planEstCal.keySet());
        if (respetaOrden) {
            Collections.sort(componentes, (o1, o2) -> (o1.getCpeComponentePlanGrado() != null && o1.getCpeComponentePlanGrado().getCpgOrden() != null) && (o2.getCpeComponentePlanGrado() != null && o2.getCpeComponentePlanGrado().getCpgOrden() != null)
                    ? o1.getCpeComponentePlanGrado().getCpgOrden().compareTo(o2.getCpeComponentePlanGrado().getCpgOrden())
                    : o1.getCpeComponentePlanGrado() == null && o2.getCpeComponentePlanGrado() == null
                    ? o1.getCpeNombre().compareTo(o2.getCpeNombre())
                    : o1.getCpeComponentePlanGrado() == null ? 1 : -1);
        } else {
            Collections.sort(componentes, (o1, o2) -> o1.getCpeComponentePlanGrado() != null && o2.getCpeComponentePlanGrado() != null
                    ? (o1.getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala().equals(o2.getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala())
                    ? o1.getCpeComponentePlanGrado().getCpgComponentePlanEstudio().getCpeNombre().compareTo(o2.getCpeComponentePlanGrado().getCpgComponentePlanEstudio().getCpeNombre())
                    : o1.getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala().equals(TipoEscalaCalificacion.CONCEPTUAL) ? -1 : 1)
                    : o1.getCpeNombre().compareTo(o2.getCpeNombre()));
        }

        for (SgComponentePlanEstudio componentePlan : componentes) {

            HashMap<SgRangoFecha, String> periodosValor = new HashMap<>();
            Object[] datosFijos = new Object[7];
            Integer[] datosFijosUltimoNumero = new Integer[7]; //Pueden haber varias PP, PPS, etc. Hay que quedarse con la última.
            Arrays.fill(datosFijos, "");

            for (SgCalificacionEstudiante c : planEstCal.get(componentePlan)) {

                Integer precisionComponente = null;
                Integer precisionEscala = null;
                if (c.getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado() != null) {
                    precisionComponente = c.getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado().getCpgPrecision();
                    precisionEscala = c.getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaPrecision();
                }
                if (precisionEscala == null && precisionComponente != null) {
                    precisionEscala = precisionComponente;
                }

                if (precisionComponente == null && precisionEscala != null) {
                    precisionComponente = precisionEscala;
                }

                EnumTiposPeriodosCalificaciones tipoPeriodo = c.getCaeCalificacion().getCalTipoPeriodoCalificacion();

                //Ordinaria
                if (EnumTiposPeriodosCalificaciones.ORD.equals(tipoPeriodo)) {
                    periodosValor.put(c.getCaeCalificacion().getCalRangoFecha(), c.getCaeCalificacionValor(precisionEscala));
                } else if (EnumTiposPeriodosCalificaciones.EXORD.equals(tipoPeriodo)) {

                    EnumCalendarioEscolar calendarioEscolar = c.getCaeCalificacion().getCalTipoCalendarioEscolar();
                    if (calendarioEscolar != null) {
                        switch (calendarioEscolar) {
                            case PREC:
                                if (datosFijosUltimoNumero[1] == null || datosFijosUltimoNumero[1] < c.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                    datosFijos[1] = c.getCaeCalificacionValor(precisionEscala);
                                    datosFijosUltimoNumero[1] = c.getCaeCalificacion().getCalNumero();
                                }
                                break;
                            case PRECPS:
                                if (datosFijosUltimoNumero[2] == null || datosFijosUltimoNumero[2] < c.getCaeCalificacion().getCalNumero()) {
                                    datosFijos[2] = c.getCaeCalificacionValor(precisionEscala);
                                    datosFijosUltimoNumero[2] = c.getCaeCalificacion().getCalNumero();
                                }
                                break;
                            case SREC:
                                if (datosFijosUltimoNumero[3] == null || datosFijosUltimoNumero[3] < c.getCaeCalificacion().getCalNumero()) {
                                    datosFijos[3] = c.getCaeCalificacionValor(precisionEscala);
                                    datosFijosUltimoNumero[3] = c.getCaeCalificacion().getCalNumero();
                                }
                                break;
                            case SRECPS:
                                if (datosFijosUltimoNumero[4] == null || datosFijosUltimoNumero[4] < c.getCaeCalificacion().getCalNumero()) {
                                    datosFijos[4] = c.getCaeCalificacionValor(precisionEscala);
                                    datosFijosUltimoNumero[4] = c.getCaeCalificacion().getCalNumero();
                                }
                                break;
                        }
                    }

                } else if (EnumTiposPeriodosCalificaciones.NOTIN.equals(tipoPeriodo)) {
                    datosFijos[0] = c.getCaeCalificacionValor(precisionComponente);
                } else if (EnumTiposPeriodosCalificaciones.APR.equals(tipoPeriodo)) {

                    if (c.getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado() != null && TipoEscalaCalificacion.CONCEPTUAL.equals(c.getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala())) {
                        datosFijos[5] = c.getCaeCalificacionValor(null);
                        datosFijos[6] = "-----";

                    } else {

                        if (TipoComponentePlanEstudio.PRU.equals(c.getCaeCalificacion().getCalComponentePlanEstudio().getCpeTipo())) {
                            datosFijos[5] = c.getCaeCalificacionValor(precisionComponente);
                            datosFijos[6] = "-----";
                        } else {
                            datosFijos[5] = c.getCaeCalificacionValor(null); // distinto de prueba no llevan prec en APR
                            datosFijos[6] = c.getCaeResolucion() != null ? c.getCaeResolucion().getText() : "";
                        }

                    }

                }

            }

            Object[] row = new Object[14];
            Integer i = 0;
            row[i] = componentePlan.getCpeNombre();

            Integer cantRangos = rangos.size();
            //cantRangosMax puede ser >= a rangos.size()
            for (int f = 0; f < cantRangosMax; f++) {
                if (cantRangos > f) {
                    SgRangoFecha r = rangos.get(f);
                    String valor = periodosValor.get(r);
                    row[++i] = valor != null ? valor : "";
                } else {
                    row[++i] = "";
                }
            }

            row[++i] = datosFijos[0];
            row[++i] = datosFijos[1];
            row[++i] = datosFijos[2];
            row[++i] = datosFijos[3];
            row[++i] = datosFijos[4];
            row[++i] = datosFijos[5];
            row[++i] = datosFijos[6];
            a.addRow(row);
        }
        return a;
    }

    private TypedTableModel getCalificationsPAESTableModel(List<SgCalificacionEstudiante> planEstCal) {
        List<SgCalificacionEstudiante> calificacionEstudiantePAESapr = planEstCal.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.APR)).collect(Collectors.toList());

        TypedTableModel a = new TypedTableModel();
        a.addColumn("componente_plan_estudio", String.class);
        a.addColumn("nf", String.class);
        a.addColumn("resultado", String.class);

        List<SgComponentePlanEstudio> componentes = calificacionEstudiantePAESapr.stream().map(c -> c.getCaeCalificacion().getCalComponentePlanEstudio()).collect(Collectors.toList());
        if (respetaOrden) {
            Collections.sort(componentes, (o1, o2) -> (o1.getCpeComponentePlanGrado() != null && o1.getCpeComponentePlanGrado().getCpgOrden() != null) && (o2.getCpeComponentePlanGrado() != null && o2.getCpeComponentePlanGrado().getCpgOrden() != null)
                    ? o1.getCpeComponentePlanGrado().getCpgOrden().compareTo(o2.getCpeComponentePlanGrado().getCpgOrden())
                    : o1.getCpeComponentePlanGrado() == null && o2.getCpeComponentePlanGrado() == null
                    ? o1.getCpeNombre().compareTo(o2.getCpeNombre())
                    : o1.getCpeComponentePlanGrado() == null ? 1 : -1);
        } else {
            Collections.sort(componentes, (o1, o2) -> o1.getCpeComponentePlanGrado() != null && o2.getCpeComponentePlanGrado() != null
                    ? (o1.getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala().equals(o2.getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala())
                    ? o1.getCpeComponentePlanGrado().getCpgComponentePlanEstudio().getCpeNombre().compareTo(o2.getCpeComponentePlanGrado().getCpgComponentePlanEstudio().getCpeNombre())
                    : o1.getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala().equals(TipoEscalaCalificacion.CONCEPTUAL) ? -1 : 1)
                    : o1.getCpeNombre().compareTo(o2.getCpeNombre()));
        }

        for (SgComponentePlanEstudio componentePlan : componentes) {

            List<SgCalificacionEstudiante> calEstList = calificacionEstudiantePAESapr.stream().filter(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(componentePlan.getCpePk())).collect(Collectors.toList());

            if (!calEstList.isEmpty()) {
                Integer precisionEscala = null;
                if (calEstList.get(0).getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado() != null) {

                    precisionEscala = calEstList.get(0).getCaeCalificacion().getCalComponentePlanEstudio().getCpeComponentePlanGrado().getCpgEscalaCalificacion().getEcaPrecision();
                }
                Object[] row = new Object[3];
                row[0] = componentePlan.getCpeNombre();
                row[1] = calEstList.get(0).getCaeCalificacionValor(precisionEscala);
                row[2] = "-----";
                a.addRow(row);
            }
        }

        return a;
    }

}
