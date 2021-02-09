/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgArea;
import sv.gob.mined.siges.web.dto.SgAreaAsignaturaModulo;
import sv.gob.mined.siges.web.dto.SgAreaIndicador;
import sv.gob.mined.siges.web.dto.SgAsignatura;
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgDireccion;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgModulo;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSelloFirma;
import sv.gob.mined.siges.web.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCiclo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSeccion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSelloFirma;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.CicloRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.web.restclient.SelloFirmaRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class PromocionesEstudiantesGenerator {

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private CalificacionEstudianteRestClient calEstudianteClient;

    @Inject
    private SeccionRestClient seccionClient;

    @Inject
    private PlantillaRestClient plantillaClient;

    @Inject
    private ComponentePlanGradoRestClient comPlanGradoClient;

    @Inject
    private SelloFirmaRestClient selloFirmaClient;

    @Inject
    private CicloRestClient cicloRestClient;

    public MasterReport generarReporte(Long seccionPk, Long estudiantePk) throws Exception {

        try {
            
            BusinessException be = new BusinessException();
            if (seccionPk == null) {
                be.addError("Debes ingresar una sección.");
                throw be;
            }

            FiltroSeccion filSec = new FiltroSeccion();

            FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
            filtroCalEst.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                "caeCalificacion.calComponentePlanEstudio.cpePk",
                "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                "caeCalificacion.calComponentePlanEstudio.cpeEsPaes",
                "caeCalificacion.calTipoCalendarioEscolar",
                "caeCalificacion.calTipoPeriodoCalificacion",
                "caeCalificacion.calNumero",
                "caePromovidoCalificacion",
                "caeCalificacion.calRangoFecha.rfePk",
                "caeCalificacion.calRangoFecha.rfeFechaHasta",
                "caeCalificacion.calRangoFecha.rfeCodigo",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                "caeResolucion",
                "caeCalificacionNumericaEstudiante",
                "caeCalificacionConceptualEstudiante.calCodigo",
                "caeCalificacionConceptualEstudiante.calValor",
                "caeCalificacionConceptualEstudiante.calValorEnCertificado",
                "caeEstudiante.estPk",
                "caeEstudiante.estPersona.perNie",
                "caeEstudiante.estPersona.perPk",
                "caeEstudiante.estPersona.perPrimerNombre", "caeEstudiante.estPersona.perSegundoNombre","caeEstudiante.estPersona.perTercerNombre",  "caeEstudiante.estPersona.perNombreBusqueda",
                "caeEstudiante.estPersona.perPrimerApellido", "caeEstudiante.estPersona.perSegundoApellido", "caeEstudiante.estPersona.perFechaNacimiento",
                "caeEstudiante.estVersion"});
            filtroCalEst.setOrderBy(new String[]{"caeEstudiante.estPersona.perPrimerApellidoBusqueda", "caeEstudiante.estPersona.perSegundoApellidoBusqueda",
                "caeEstudiante.estPersona.perPrimerNombreBusqueda", "caeEstudiante.estPersona.perSegundoNombreBusqueda"});
            filtroCalEst.setAscending(new boolean[]{true, true, true, true});
            List<EnumTiposPeriodosCalificaciones> listTipo = new ArrayList();
            listTipo.add(EnumTiposPeriodosCalificaciones.APR);
            listTipo.add(EnumTiposPeriodosCalificaciones.GRA);
            filtroCalEst.setCaeTiposPeriodoCalificacion(listTipo);
            
            
            if (seccionPk != null) {
                filtroCalEst.setSeccionPk(seccionPk);
                filSec.setSecPk(seccionPk);
            }
            
            if (estudiantePk != null) {
                filtroCalEst.setEstudiantePk(estudiantePk);
            }

            List<SgSeccion> secciones = seccionClient.buscar(filSec);
            if (secciones.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_NO_ENCONTRADA));
                throw be;
            }
            SgSeccion seccion = secciones.get(0);  
            
            
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_PROMOCION_ESTUDIANTE_CODIGO,
                        seccion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuPk());
                
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_PROMOCION_ESTUDIANTE_CODIGO + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("promociones_estudiantes.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
                   
            List<SgCalificacionEstudiante> calificacionesApr = calEstudianteClient.buscar(filtroCalEst);
            List<SgCalificacionEstudiante> calificacionPromociones = calificacionesApr.stream().filter(c -> EnumTiposPeriodosCalificaciones.GRA.equals(c.getCaeCalificacion().getCalTipoPeriodoCalificacion()) && EnumPromovidoCalificacion.PROMOVIDO.equals(c.getCaePromovidoCalificacion())).collect(Collectors.toList());
            List<SgEstudiante> listEstudiantes = calificacionPromociones.stream().map(c -> c.getCaeEstudiante()).collect(Collectors.toList());
            List<SgCalificacionEstudiante> calificaciones = calificacionesApr.stream()
                    .filter(c -> EnumTiposPeriodosCalificaciones.APR.equals(c.getCaeCalificacion().getCalTipoPeriodoCalificacion()) && listEstudiantes.contains(c.getCaeEstudiante()))
                    .collect(Collectors.toList());

            if (listEstudiantes.isEmpty()) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SECCION_SIN_ESTUDIANTES_PROMOVIDOS));
                throw be;
            }

            FiltroComponentePlanGrado filCompGrado = new FiltroComponentePlanGrado();
            filCompGrado.setCpgGradoPk(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
            filCompGrado.setCpgPlanEstudioPk(seccion.getSecPlanEstudio().getPesPk());
            filCompGrado.setCpgEsTipoPaes(Boolean.FALSE);
            filCompGrado.setOrderBy(new String[]{"cpgOrden"});
            filCompGrado.setAscending(new boolean[]{true});
            filCompGrado.setIncluirCampos(new String[]{
                "cpgPeriodosCalificacion",
                "cpgComponentePlanEstudio.indAreaIndicador.ariPk",
                 "cpgComponentePlanEstudio.indAreaIndicador.ariNombre",
                "cpgComponentePlanEstudio.asigAreaAsignaturaModulo.aamPk",
                "cpgComponentePlanEstudio.asigAreaAsignaturaModulo.aamNombre",
                "cpgComponentePlanEstudio.modAreaAsignaturaModulo.aamPk",
                "cpgComponentePlanEstudio.modAreaAsignaturaModulo.aamNombre",
                "cpgComponentePlanEstudio.cpeTipo",
                "cpgComponentePlanEstudio.cpeNombre",
                "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeVersion",
                "cpgCantidadHorasSemanales",
                "cpgCalculoNotaInstitucional",
                "cpgFuncionRedondeo",
                "cpgOrden",
                "cpgCodigoExterno",
                "cpgPrecision",
                "cpgObjetoPromocion",
                "cpgCalificacionIngresadaCE"});

            List<SgComponentePlanGrado> componentesGrado = comPlanGradoClient.buscar(filCompGrado);

            Collections.sort(componentesGrado, (o1, o2) -> (o1.getCpgOrden() != null ? o1.getCpgOrden() : 0) - (o2.getCpgOrden() != null ? o2.getCpgOrden() : 0));

            //Agrupar componente plan grado por componente plan estudio
            //  HashMap<SgComponentePlanEstudio, SgComponentePlanGrado> compPlanEstudioGrado = new HashMap<>();
            HashMap<Object, HashMap<SgComponentePlanEstudio, SgComponentePlanGrado>> areaComponente = new HashMap<>();
            for (SgComponentePlanGrado cpg : componentesGrado) {
                if (cpg.getCpgComponentePlanEstudio() instanceof SgAsignatura) {
                    SgAsignatura asig = (SgAsignatura) cpg.getCpgComponentePlanEstudio();
                    areaComponente.computeIfAbsent(asig.getAsigAreaAsignaturaModulo(), s -> new HashMap<SgComponentePlanEstudio, SgComponentePlanGrado>())
                            .computeIfAbsent(cpg.getCpgComponentePlanEstudio(), s -> cpg);
                } else {
                    if (cpg.getCpgComponentePlanEstudio() instanceof SgModulo) {
                        SgModulo asig = (SgModulo) cpg.getCpgComponentePlanEstudio();
                        areaComponente.computeIfAbsent(asig.getModAreaAsignaturaModulo(), s -> new HashMap<SgComponentePlanEstudio, SgComponentePlanGrado>())
                                .computeIfAbsent(cpg.getCpgComponentePlanEstudio(), s -> cpg);

                    } else if (cpg.getCpgComponentePlanEstudio() instanceof SgArea) {
                        SgArea asig = (SgArea) cpg.getCpgComponentePlanEstudio();
                        areaComponente.computeIfAbsent(asig.getIndAreaIndicador(), s -> new HashMap<SgComponentePlanEstudio, SgComponentePlanGrado>())
                                .computeIfAbsent(cpg.getCpgComponentePlanEstudio(), s -> cpg);
                    } else {
                        String st = "";
                        areaComponente.computeIfAbsent(st, s -> new HashMap<SgComponentePlanEstudio, SgComponentePlanGrado>())
                                .computeIfAbsent(cpg.getCpgComponentePlanEstudio(), s -> cpg);
                    }

                }

                //  compPlanEstudioGrado.put(cpg.getCpgComponentePlanEstudio(), cpg);
            }

            //Set report parameters
            FiltroSelloFirma filtroSelloFirma = new FiltroSelloFirma();
            filtroSelloFirma.setSfiHabilitado(Boolean.TRUE);
            filtroSelloFirma.setSedPk(seccion.getSecServicioEducativo().getSduSede().getSedPk());
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
            SgDireccion dirSede = seccion.getSecServicioEducativo().getSduSede().getSedDireccion();
            report.getParameterValues().put("departamento", dirSede != null ? dirSede.getDirDepartamento().getDepNombre() : "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es"));
            report.getParameterValues().put("fecha_emision", formatter.format(LocalDate.now()));
            //End Set report parameters

            report.setDataFactory(this.getDataFactory(calificaciones, seccion, areaComponente, listEstudiantes));
            
            return report;


        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw e;
        }
    }
    
    private DataFactory getDataFactory(List<SgCalificacionEstudiante> calificaciones, SgSeccion seccion, HashMap<Object, HashMap<SgComponentePlanEstudio, SgComponentePlanGrado>> areaComponente, List<SgEstudiante> listEstudiantes) throws Exception {

        //Se obtiene la cantidad de ciclo ya que en caso de que sea único no hay q imprimir el nombre
        FiltroCiclo fc = new FiltroCiclo();
        fc.setNivPk(seccion.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
        Long totalCiclo = cicloRestClient.buscarTotal(fc);

        //Preprocesamiento. Agrupar calificaciones por estudiante
        // HashMap<SgEstudiante, List<SgCalificacionEstudiante>> estCal = new HashMap<>();
        HashMap<SgEstudiante, HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>>> estCal = new HashMap<>();

        for (SgEstudiante est : listEstudiantes) {
            estCal.put(est, new HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>>());
        }

        for (SgCalificacionEstudiante cal : calificaciones) {
            estCal.computeIfAbsent(cal.getCaeEstudiante(), s -> new HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>>())
                    .computeIfAbsent(cal.getCaeCalificacion().getCalComponentePlanEstudio(), s -> new ArrayList<>()).add(cal);
        }

        TypedTableModel model = new TypedTableModel();
        model.addColumn("nie_nombre", String.class);
        model.addColumn("nie", String.class);
        model.addColumn("sede", String.class);
        model.addColumn("plan_estudio", String.class);
        model.addColumn("grado", String.class);
        model.addColumn("anio", String.class);
        model.addColumn("seccion", String.class);
        model.addColumn("nombre", String.class);
        model.addColumn("servicio", String.class);
        model.addColumn("servicio_promocion", String.class);
        model.addColumn("calificaciones", TypedTableModel.class);

        for (SgEstudiante est : estCal.keySet()) {
            model.addRow(est.getEstPersona().getPerNie() + " - " + est.getEstPersona().getPerNombreCompleto(),
                    est.getEstPersona().getPerNie(),
                    seccion.getSecServicioEducativo().getSduSede().getSedCodigoNombre(),
                    seccion.getSecPlanEstudio().getPesNombre(),
                    seccion.getSecServicioEducativo().getSduGrado().getGraNombre(),
                    seccion.getSecAnioLectivo().getAleAnio(),
                    seccion.getNombreSeccion(),
                    est.getEstPersona().getPerNombreCompletoNP(),
                    seccion.getSecServicioEducativo().getSduNombre(totalCiclo),
                    seccion.getSecServicioEducativo().getSduNombrePromocion(),
                    this.getPromocionTableModel(estCal.get(est), areaComponente)
            );
        }

        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    }

    private TypedTableModel getPromocionTableModel(HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>> planEstCal, HashMap<Object, HashMap<SgComponentePlanEstudio, SgComponentePlanGrado>> areaComponente) throws Exception {

        //Preprocesamiento. 
        //Agrupar calificaciones por componente plan estudio
        TypedTableModel a = new TypedTableModel();
        a.addColumn("componente_plan_estudio", String.class);
        a.addColumn("horas_semanales", String.class);
        a.addColumn("calificacion_final", String.class);
        a.addColumn("resultado", String.class);
        a.addColumn("es_area", Integer.class);
        a.addColumn("calificacion_letra", String.class);

        for (Object area : areaComponente.keySet()) {
            Object[] rowArea = new Object[6];
            Arrays.fill(rowArea, "");
            if (area instanceof SgAreaAsignaturaModulo) {
                rowArea[0] = ((SgAreaAsignaturaModulo) area).getAamNombre().toUpperCase();
            }
            if (area instanceof SgAreaIndicador) {
                rowArea[0] = ((SgAreaIndicador) area).getAriNombre().toUpperCase();
            }
            if (area instanceof String) {
                rowArea[0] = "SIN ÁREA";
            }
            rowArea[4] = 2;
            a.addRow(rowArea);

            HashMap<SgComponentePlanEstudio, SgComponentePlanGrado> compPlanEstudioGrado = areaComponente.get(area);
            List<SgComponentePlanGrado> cpList = new ArrayList<>(compPlanEstudioGrado.values());
            Collections.sort(cpList, (o1, o2) -> (o1.getCpgOrden() != null ? o1.getCpgOrden() : 0) - (o2.getCpgOrden() != null ? o2.getCpgOrden() : 0));

            for (SgComponentePlanGrado componentePlan : cpList) {

                Object[] row = new Object[6];
                Arrays.fill(row, "");

                row[0] = componentePlan.getCpgComponentePlanEstudio().getCpeNombre();
                row[1] = componentePlan.getCpgCantidadHorasSemanales() != null ? componentePlan.getCpgCantidadHorasSemanales() : "";

                List<SgCalificacionEstudiante> calEl = planEstCal.get(componentePlan.getCpgComponentePlanEstudio());
                String calificacionFinal = " ";
                String calificacionEscrita = " ";
                String resultado = "Pendiente";
                if (calEl != null) {
                    if (!calEl.isEmpty() && calEl.size() == 1) {
                        if (!StringUtils.isEmpty(calEl.get(0).getCaeCalificacionValorPromocion())) {
                            calificacionFinal = calEl.get(0).getCaeCalificacionValorPromocion();
                            calificacionEscrita = calEl.get(0).getCaeCalificacionFormatoEscrito();
                        }
                        if (calEl.get(0).getCaeResolucion() != null) {
                            SgComponentePlanGrado cpg = compPlanEstudioGrado.get(calEl.get(0).getCaeCalificacion().getCalComponentePlanEstudio());
                            if (cpg.getCpgObjetoPromocion() != null && cpg.getCpgObjetoPromocion()) {
                                resultado = calEl.get(0).getCaeResolucion().getText();
                            } else {
                                resultado = "";
                            }
                        }
                    }
                }
                row[2] = calificacionFinal;
                row[3] = resultado;
                row[4] = 0;
                row[5] = StringUtils.isEmpty(calificacionEscrita) ? " " : calificacionEscrita;

                a.addRow(row);
            }
        }
        return a;
    }

}