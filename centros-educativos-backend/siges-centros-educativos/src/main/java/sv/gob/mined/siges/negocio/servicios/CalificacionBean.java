/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.security.DataSecurityException;
import com.sofis.security.OperationSecurity;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgCalificacionEstudiantePSNG;
import sv.gob.mined.siges.dto.SgCalificacionPSNG;
import sv.gob.mined.siges.dto.SgComponenteArchivoInfo;
import sv.gob.mined.siges.dto.SgConsultaCalificacionesPendientesSedesEnNivel;
import sv.gob.mined.siges.dto.SgDatosCalculoCalificaciones;
import sv.gob.mined.siges.dto.SgDatosCalculoCalificacionesPromocion;
import sv.gob.mined.siges.dto.SgPeriodoCalificacionEstCal;
import sv.gob.mined.siges.dto.SgPorcentajeAsistenciasRequest;
import sv.gob.mined.siges.dto.SgPorcentajeAsistenciasResponse;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumEstadoImportado;
import sv.gob.mined.siges.enumerados.EnumEstadoProcesamientoCalificacionPromocion;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.enumerados.EnumProcesoCreacion;
import sv.gob.mined.siges.enumerados.EnumPromocionGradoMatricula;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.enumerados.EnumResolucionCalificacion;
import sv.gob.mined.siges.enumerados.EnumTipoFormula;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.filtros.FiltroCalificacionesPendientesSedeEnNivel;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroComponentePlanGrado;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.filtros.FiltroPeriodoCalificacionEstCal;
import sv.gob.mined.siges.filtros.FiltroRangoFecha;
import sv.gob.mined.siges.filtros.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.filtros.catalogo.FiltroCalificacionCa;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.CalificacionEstudianteValidacionNegocio;
import sv.gob.mined.siges.negocio.validaciones.CalificacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CalificacionDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SeguridadDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoCalificaciones;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCELite;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgInfoProcesamientoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.SgInfoProcesamientoCalificacionEst;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgRangoFecha;
import sv.gob.mined.siges.persistencia.entidades.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFormula;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.NumberFormatUtils;
import sv.gob.mined.siges.utils.MathFunctionsUtils;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class CalificacionBean {

    private static final Logger LOGGER = Logger.getLogger(CalificacionBean.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private CalificacionEstudianteBean calificacionEstudianteBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private CalificacionCatalogosBean calificacionCatalogosBean;

    @Inject
    private ComponentePlanGradoBean componentePlanGradoBean;

    @Inject
    private RelGradoPlanEstudioBean relGradoPlanEstudioBean;

    @Inject
    private ArchivoCalificacionesBean archivoCalificacionesBean;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private RangoFechaBean rangoFechaBean;

    @Inject
    private AsistenciaBean asistenciaBean;

    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    private PorcentajeAsistenciasBean porcentajeAsistenciasBean;

    @Inject
    private PeriodoCalificacionBean periodoCalificacionBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private EstudianteBean estudianteBean;

    @Inject
    private HorarioEscolarBean horarioEscolarBean;

    @Inject
    private GradoBean gradoBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalificacion
     * @throws GeneralException
     */
    public SgCalificacionCE obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                SgCalificacionCE cal = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CALIFICACIONES);
                if (cal.getCalComponentePlanEstudio() != null) {
                    cal.getCalComponentePlanEstudio().getCpePk();
                }
                if (cal.getCalRangoFecha() != null) {
                    cal.getCalRangoFecha().getRfePk();
                }
                if (cal.getCalSeccion() != null) {
                    cal.getCalSeccion().getSecPk();
                }
                return cal;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CALIFICACIONES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    @Interceptors(AuditInterceptor.class)
    public Boolean actualizarNotaInstitucional(SgDatosCalculoCalificaciones datoCalificaciones) throws Exception {

        if (datoCalificaciones.getPeriodoCalificacionPk() == null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_PERIODO_CALIFICACON_VACIO);
            throw be;
        }

        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(datoCalificaciones.getSeccion().getSecPk());
        filtroMat.setMatRetirada(Boolean.FALSE);
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPk",
            "matEstudiante.estVersion"});
        List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(filtroMat);
        List<SgEstudiante> listEstudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());

        FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
        filtroCalEst.setIncluirCampos(new String[]{
            "caeCalificacion.calPk",
            "caeCalificacion.calComponentePlanEstudio.cpeNombre",
            "caeCalificacion.calComponentePlanEstudio.cpePk",
            "caeCalificacion.calComponentePlanEstudio.cpeTipo",
            "caeCalificacion.calComponentePlanEstudio.cpeVersion",
            "caeCalificacion.calTipoCalendarioEscolar",
            "caeCalificacion.calTipoPeriodoCalificacion",
            "caeCalificacion.calNumero",
            "caeCalificacion.calSeccion.secPk",
            "caeCalificacion.calSeccion.secVersion",
            "caeCalificacion.calFecha",
            "caeCalificacion.calUltModFecha",
            "caeCalificacion.calUltModUsuario",
            "caeCalificacion.calVersion",
            "caeCalificacion.calCerrado",
            "caeCalificacion.calRangoFecha.rfePk",
            "caeCalificacion.calRangoFecha.rfeFechaHasta",
            "caeCalificacion.calRangoFecha.rfeCodigo",
            "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
            "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
            "caeCalificacion.calRangoFecha.rfeVersion",
            "caeCalificacionNumericaEstudiante",
            "caeCalificacionConceptualEstudiante.calPk",
            "caeCalificacionConceptualEstudiante.calCodigo",
            "caeCalificacionConceptualEstudiante.calValor",
            "caeCalificacionConceptualEstudiante.calOrden",
            "caeCalificacionConceptualEstudiante.calVersion",
            "caeVersion",
            "caeResolucion",
            "caeEstudiante.estPk",
            "caeEstudiante.estVersion"
        });
        filtroCalEst.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
            EnumTiposPeriodosCalificaciones.ORD,
            EnumTiposPeriodosCalificaciones.NOTIN}));
        //FIXME: Se agrega filtro por grado para secciones semestrales, esto no contempla el caso de repetición
        filtroCalEst.setCaeGradoFk(datoCalificaciones.getSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
        //filtroCalEst.setSeccionActualEstudiantesPk(datoCalificaciones.getSeccion().getSecPk());
        filtroCalEst.setCaeEstudiantesPk(listEstudiantes.stream().map(e -> e.getEstPk()).collect(Collectors.toList()));
        filtroCalEst.setAnioLectivoPk(datoCalificaciones.getSeccion().getSecAnioLectivo().getAlePk());
        filtroCalEst.setCaeComponentePlanEstudio(datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio().getCpePk());

        //Filtra dependiendo si sección es anual o semestral
        filtroCalEst.setCaeTipoPeriodo(datoCalificaciones.getSeccion().getSecTipoPeriodo());
        filtroCalEst.setCaeNumeroPeriodo(datoCalificaciones.getSeccion().getSecNumeroPeriodo());

        List<SgCalificacionEstudiante> calificaciones = calificacionEstudianteBean.obtenerPorFiltro(filtroCalEst);

        //Removemos calificaciones que sean de otros períodos
        Iterator<SgCalificacionEstudiante> it = calificaciones.iterator();
        while (it.hasNext()) {
            SgCalificacionEstudiante cal = it.next();
            if (cal.getCaeCalificacion().getCalRangoFecha() != null
                    && !cal.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion().getPcaPk().equals(datoCalificaciones.getPeriodoCalificacionPk())) {
                it.remove();
            }
        }

        calcularNotaInstitucional(datoCalificaciones, listEstudiantes, calificaciones);
        return Boolean.TRUE;
    }

    public SgCalificacionCE calcularNotaInstitucional(SgDatosCalculoCalificaciones datoCalificaciones, List<SgEstudiante> listEstudiantes, List<SgCalificacionEstudiante> calificaciones) throws Exception {
        if (datoCalificaciones.getSeccion() == null
                || datoCalificaciones.getComponentePlanGrado() == null
                || datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio() == null
                || datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio().getCpePk() == null
                || TipoComponentePlanEstudio.PRU.equals(datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio().getCpeTipo()) // Las pruebas se calculan mediante procesar PAES
                || listEstudiantes == null
                || listEstudiantes.isEmpty()) {
            return null;
        }

        SgSeccion seccion = datoCalificaciones.getSeccion();
        SgComponentePlanGrado componentePlanGrado = datoCalificaciones.getComponentePlanGrado();

        SgComponentePlanEstudio componentePlanEstudio = componentePlanGrado.getCpgComponentePlanEstudio();

        SgCalificacionCE calificacionNotaInstitucional = null;
        List<SgCalificacionEstudiante> calEstudiantes = calificaciones.stream().filter(c -> EnumTiposPeriodosCalificaciones.NOTIN.equals(c.getCaeCalificacion().getCalTipoPeriodoCalificacion())).collect(Collectors.toList());
        if (!calEstudiantes.isEmpty()) {
            //Me quedo con el cabezal cuya sec se corresponda con la sec que estoy calificando
            //Si hay calificaciones NOTIN de otros cabezales porque fueron trasladados, se les actualiza el cabezal
            calificacionNotaInstitucional = calEstudiantes.stream()
                    .map(ce -> ce.getCaeCalificacion())
                    .filter(c -> c.getCalSeccion().getSecPk().equals(seccion.getSecPk()))
                    .findFirst()
                    .orElse(null);
        }
        List<SgCalificacionEstudiante> calEstudiantesoOrd = calificaciones.stream().filter(c -> EnumTiposPeriodosCalificaciones.ORD.equals(c.getCaeCalificacion().getCalTipoPeriodoCalificacion())).collect(Collectors.toList());

        if (calificacionNotaInstitucional == null) {
            //En algunos casos se da de que existe el cabezal pero ningún estudiante está asociado, 
            //se hace búsqueda de cabezal, en caso de que sea vacio se crea uno nuevo
            FiltroCalificacion fc = new FiltroCalificacion();
            fc.setSecPk(seccion.getSecPk());
            fc.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.NOTIN);
            fc.setCalComponentePlanEstudio(componentePlanEstudio.getCpePk());
            fc.setIncluirCampos(new String[]{"calPk",
                "calComponentePlanEstudio.cpePk",
                "calComponentePlanEstudio.cpeVersion",
                "calComponentePlanEstudio.cpeTipo",
                "calSeccion.secPk",
                "calSeccion.secVersion",
                "calFecha",
                "calTipoPeriodoCalificacion",
                "calTipoCalendarioEscolar",
                "calNumero",
                "calVersion",
                "calCerrado",
                "calEstadoProcesamientoPromocion",
                "calInfoProcesamientoCalificacionFk.ipcPk",
                "calInfoProcesamientoCalificacionFk.ipcError",
                "calInfoProcesamientoCalificacionFk.ipcVersion"});
            List<SgCalificacionCE> calList = this.obtenerPorFiltro(fc);

            if (calList.isEmpty()) {
                calificacionNotaInstitucional = new SgCalificacionCE();
                calificacionNotaInstitucional.setCalComponentePlanEstudio(componentePlanEstudio);
                calificacionNotaInstitucional.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.NOTIN);
                calificacionNotaInstitucional.setCalSeccion(seccion);
            } else {
                calificacionNotaInstitucional = calList.get(0);
                //EN CASO DE QUE EL CABEZAL EXISTIRIA Y TUVIERA OTRAS CALIFICACIONES, NO SE BORRAN LAS CALIFICACIONES ANTERIORES          
            }
        }
        calificacionNotaInstitucional.setCalFecha(LocalDate.now());

        for (SgEstudiante est : listEstudiantes) {
            SgCalificacionEstudiante calificacionEst = new SgCalificacionEstudiante();
            List<SgCalificacionEstudiante> calEstList = calEstudiantes.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
            if (!calEstList.isEmpty()) {
                calificacionEst = calEstList.get(0);
            }
            Object resultadoObjeto = calcularNotaInstitucionalPorEstudiante(est, componentePlanGrado, calEstudiantesoOrd);
            if (resultadoObjeto != null) {
                if (resultadoObjeto instanceof String) {
                    calificacionEst.setCaeCalificacionNumericaEstudiante((String) resultadoObjeto);
                }
                if (resultadoObjeto instanceof SgCalificacion) {
                    calificacionEst.setCaeCalificacionConceptualEstudiante((SgCalificacion) resultadoObjeto);
                }
                calificacionEst.setCaeFechaRealizado(LocalDate.now());
                calificacionEst.setCaeEstudiante(est);

                if (calificacionEst.getCaePk() == null) {
                    calEstudiantes.add(calificacionEst);
                }
            }

        }

        //cuando datoCalificaciones.getEstudiantePk() es distinto de NULL significa que el calculo se hace para un solo estudiante
        //AL EDITAR LA NOTA DE UN SOLO ESTUDIANTE HAY QUE ASEGURAR QUE LAS DE LOS DEMAS NO SE BORREN CON ORPHANREMOVAL
        if (calificacionNotaInstitucional.getCalPk() != null && datoCalificaciones.getEstudiantePk() != null) {
            FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
            fce.setCabezalPk(calificacionNotaInstitucional.getCalPk());
            fce.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                "caeCalificacion.calComponentePlanEstudio.cpePk",
                "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                "caeCalificacion.calComponentePlanEstudio.cpeVersion",
                "caeCalificacion.calTipoCalendarioEscolar",
                "caeCalificacion.calTipoPeriodoCalificacion",
                "caeCalificacion.calNumero",
                "caeCalificacion.calSeccion.secPk",
                "caeCalificacion.calSeccion.secVersion",
                "caeCalificacion.calFecha",
                "caeCalificacion.calUltModFecha",
                "caeCalificacion.calUltModUsuario",
                "caeCalificacion.calVersion",
                "caeCalificacion.calCerrado",
                "caeCalificacion.calRangoFecha.rfePk",
                "caeCalificacion.calRangoFecha.rfeFechaHasta",
                "caeCalificacion.calRangoFecha.rfeCodigo",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                "caeCalificacion.calRangoFecha.rfeVersion",
                "caeCalificacionNumericaEstudiante",
                "caeCalificacionConceptualEstudiante.calPk",
                "caeCalificacionConceptualEstudiante.calCodigo",
                "caeCalificacionConceptualEstudiante.calValor",
                "caeCalificacionConceptualEstudiante.calOrden",
                "caeCalificacionConceptualEstudiante.calVersion",
                "caeVersion",
                "caeResolucion",
                "caeEstudiante.estPk",
                "caeEstudiante.estVersion"});
            List<SgCalificacionEstudiante> calCE = calificacionEstudianteBean.obtenerPorFiltro(fce);
            calCE = calCE.stream().filter(c -> !c.getCaeEstudiante().getEstPk().equals(datoCalificaciones.getEstudiantePk())).collect(Collectors.toList());
            calCE.addAll(calEstudiantes);
            calEstudiantes = calCE;
        }

        calificacionNotaInstitucional.setCalCalificacionesEstudiante(calEstudiantes);

        for (SgCalificacionEstudiante calel : calificacionNotaInstitucional.getCalCalificacionesEstudiante()) {
            //Actualizamos cabezal de todas las NOTIN
            calel.setCaeCalificacion(calificacionNotaInstitucional);
        }

        //Si el cabezal es nuevo, todas las calificaciones existentes que tengan cabezal incorrecto y deben apuntar al nuevo se regeneran
        //Se hace de este modo para evitar detached entity passed to persist
        List<Long> calificacionesExistentes = new ArrayList<>();
        if (calificacionNotaInstitucional.getCalPk() == null) {
            Iterator<SgCalificacionEstudiante> calEstIterator = calificacionNotaInstitucional.getCalCalificacionesEstudiante().iterator();
            while (calEstIterator.hasNext()) {
                SgCalificacionEstudiante ce = calEstIterator.next();
                if (ce.getCaePk() != null) {
                    calificacionesExistentes.add(ce.getCaePk());
                }
                ce.setCaePk(null);
                ce.setCaeVersion(null);
            }
        }

        SgCalificacionCE result = this.guardar(calificacionNotaInstitucional, null, Boolean.FALSE);

        if (!calificacionesExistentes.isEmpty()) {
            //Eliminar calificaciones incorrectas
            em.createQuery("DELETE FROM  SgCalificacionEstudiante where caePk IN (:ids)")
                    .setParameter("ids", calificacionesExistentes)
                    .executeUpdate();
        }

        return calificacionNotaInstitucional;
    }

    public Object calcularNotaInstitucionalPorEstudiante(SgEstudiante est, SgComponentePlanGrado componentePlanGrado, List<SgCalificacionEstudiante> calificaciones) {
        List<BigDecimal> calificacionesNumericas = new ArrayList();
        List<Double> calificacionesNumericasDouble = new ArrayList();
        List<SgCalificacion> calificacionesConceptuales = new ArrayList();
        if (componentePlanGrado != null) {
            if (componentePlanGrado.getCpgEscalaCalificacion() != null) {

                LocalDate ultimaFecha = null;
                String ultimaNotaNumerica = null;
                SgCalificacion ultimaNotaConceptual = null;

                for (SgCalificacionEstudiante calEst : calificaciones) {
                    if (calEst.getCaeCalificacion().getCalRangoFecha() != null && calEst.getCaeEstudiante().getEstPk().equals(est.getEstPk())) {

                        if (calEst.getCaeCalificacionNumericaEstudiante() != null) {
                            calificacionesNumericas.add(new BigDecimal(calEst.getCaeCalificacionNumericaEstudiante()));
                            calificacionesNumericasDouble.add(Double.valueOf(calEst.getCaeCalificacionNumericaEstudiante()));
                        } else {
                            if (calEst.getCaeCalificacionConceptualEstudiante() != null) {
                                calificacionesConceptuales.add(calEst.getCaeCalificacionConceptualEstudiante());
                            }
                        }
                        if (ultimaFecha == null) {
                            ultimaFecha = calEst.getCaeCalificacion().getCalRangoFecha().getRfeFechaHasta();
                            ultimaNotaNumerica = calEst.getCaeCalificacionNumericaEstudiante();
                            ultimaNotaConceptual = calEst.getCaeCalificacionConceptualEstudiante();
                        } else {

                            if (ultimaFecha.isBefore(calEst.getCaeCalificacion().getCalRangoFecha().getRfeFechaHasta())) {
                                ultimaFecha = calEst.getCaeCalificacion().getCalRangoFecha().getRfeFechaHasta();
                                ultimaNotaNumerica = calEst.getCaeCalificacionNumericaEstudiante();
                                ultimaNotaConceptual = calEst.getCaeCalificacionConceptualEstudiante();
                            }
                        }
                    }
                }
                if (calificacionesNumericas.size() > 0) {
                    if (TipoEscalaCalificacion.NUMERICA.equals(componentePlanGrado.getCpgEscalaCalificacion().getEcaTipoEscala())) {
                        Integer precision = null;
                        if (componentePlanGrado.getCpgPrecision() != null) {
                            precision = componentePlanGrado.getCpgPrecision();
                        } else {
                            precision = componentePlanGrado.getCpgEscalaCalificacion().getEcaPrecision();
                        }
                        Double resultado = null;
                        switch (componentePlanGrado.getCpgCalculoNotaInstitucional()) {
                            case PROM:
                                //  resultado = MathFunctionsUtils.promedio(calificacionesNumericas);
                                switch (componentePlanGrado.getCpgFuncionRedondeo()) {
                                    case RED:
                                        resultado = MathFunctionsUtils.promedio(calificacionesNumericas, precision != null ? precision : 0, RoundingMode.HALF_UP);
                                        break;
                                    case PIS:
                                        resultado = MathFunctionsUtils.promedio(calificacionesNumericas, precision != null ? precision : 0, RoundingMode.FLOOR);
                                        break;
                                    case TEC:
                                        resultado = MathFunctionsUtils.promedio(calificacionesNumericas, precision != null ? precision : 0, RoundingMode.CEILING);
                                        break;
                                    default:// nada
                                }
                                break;
                            case MAY:
                                resultado = MathFunctionsUtils.mayor(calificacionesNumericasDouble);
                                break;
                            case ULT:
                                resultado = Double.parseDouble(ultimaNotaNumerica);
                                break;
                            case MED:
                                resultado = MathFunctionsUtils.mediana(calificacionesNumericasDouble);
                                break;
                            default:

                        }
                        if (resultado != null) {
                            return NumberFormatUtils.formatDouble(resultado, precision);
                        }

                        return resultado != null ? resultado.toString() : null;
                    }
                }
                if (TipoEscalaCalificacion.CONCEPTUAL.equals(componentePlanGrado.getCpgEscalaCalificacion().getEcaTipoEscala())) {
                    SgCalificacion resultadoConceptual = null;
                    switch (componentePlanGrado.getCpgCalculoNotaInstitucional()) {
                        case MAY:
                            for (SgCalificacion calConceptual : calificacionesConceptuales) {
                                if (resultadoConceptual == null) {
                                    resultadoConceptual = calConceptual;
                                } else {
                                    if (calConceptual.getCalOrden() != null) {
                                        if (resultadoConceptual.getCalOrden() < calConceptual.getCalOrden()) {
                                            resultadoConceptual = calConceptual;
                                        }
                                    }
                                }
                            }
                            break;
                        case ULT:
                            resultadoConceptual = ultimaNotaConceptual;
                            break;
                        default:

                    }

                    return resultadoConceptual != null ? resultadoConceptual : null;
                }

            }
        }
        return null;
    }

    private Function cargarSubformula(SgFormula formula) {
        Function f = new Function(formula.getFomTextoLargo());
        if (formula.getFomTienSubformula()) {
            for (SgFormula fr : formula.getFomSubFormula()) {
                f.addDefinitions(cargarSubformula(fr));
            }
        }
        return f;
    }

    @Interceptors(AuditInterceptor.class)
    public Boolean calcularNotaAprobacion(SgDatosCalculoCalificaciones datoCalificaciones) throws Exception {
  
        if (datoCalificaciones.getPeriodoCalificacionPk() == null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_PERIODO_CALIFICACON_VACIO);
            throw be;
        }

        if (datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio() == null
                || datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio().getCpePk() == null
                || datoCalificaciones.getSeccion() == null
                || TipoComponentePlanEstudio.PRU.equals(datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio().getCpeTipo())) {
            // Las pruebas se calculan mediante procesar PAES
            return Boolean.FALSE;
        }

        SgComponentePlanGrado componentePlanGrado = datoCalificaciones.getComponentePlanGrado();
        SgSeccion seccionEnEdicion = datoCalificaciones.getSeccion();

        FiltroMatricula filtroMat = new FiltroMatricula();
        filtroMat.setSecPk(datoCalificaciones.getSeccion().getSecPk());

        //FILTRO PARA EL CASO DE QUE SEA UN ÚNICO ALUMNO, SE UTILIZA POR EJEMPLO EN SOLICITUD_CALIFICACIÓN
        if (datoCalificaciones.getEstudiantePk() != null) {
            filtroMat.setMatEstudiantePk(datoCalificaciones.getEstudiantePk());
        }

        filtroMat.setMatRetirada(Boolean.FALSE);
        filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPk",
            "matEstudiante.estVersion"});
        List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(filtroMat);
        List<SgEstudiante> listEstudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());

        if (!listEstudiantes.isEmpty()) {
            FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
            filtroCalEst.setIncluirCampos(new String[]{
                "caeCalificacion.calPk",
                "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                "caeCalificacion.calComponentePlanEstudio.cpePk",
                "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                "caeCalificacion.calComponentePlanEstudio.cpeVersion",
                "caeCalificacion.calTipoCalendarioEscolar",
                "caeCalificacion.calTipoPeriodoCalificacion",
                "caeCalificacion.calNumero",
                "caeCalificacion.calSeccion.secPk",
                "caeCalificacion.calSeccion.secVersion",
                "caeCalificacion.calFecha",
                "caeCalificacion.calUltModFecha",
                "caeCalificacion.calUltModUsuario",
                "caeCalificacion.calVersion",
                "caeCalificacion.calCerrado",
                "caeCalificacion.calRangoFecha.rfePk",
                "caeCalificacion.calRangoFecha.rfeFechaHasta",
                "caeCalificacion.calRangoFecha.rfeCodigo",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                "caeCalificacion.calRangoFecha.rfeVersion",
                "caeCalificacionNumericaEstudiante",
                "caeCalificacionConceptualEstudiante.calPk",
                "caeCalificacionConceptualEstudiante.calCodigo",
                "caeCalificacionConceptualEstudiante.calValor",
                "caeCalificacionConceptualEstudiante.calOrden",
                "caeCalificacionConceptualEstudiante.calVersion",
                "caeVersion",
                "caeResolucion",
                "caeEstudiante.estPk",
                "caeEstudiante.estVersion"
            });
            filtroCalEst.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                EnumTiposPeriodosCalificaciones.ORD,
                EnumTiposPeriodosCalificaciones.EXORD,
                EnumTiposPeriodosCalificaciones.NOTIN,
                EnumTiposPeriodosCalificaciones.APR}));
            filtroCalEst.setCaeGradoFk(datoCalificaciones.getSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
            filtroCalEst.setCaeEstudiantesPk(listEstudiantes.stream().map(e -> e.getEstPk()).collect(Collectors.toList()));
            filtroCalEst.setAnioLectivoPk(datoCalificaciones.getSeccion().getSecAnioLectivo().getAlePk());
            filtroCalEst.setCaeComponentePlanEstudio(datoCalificaciones.getComponentePlanGrado().getCpgComponentePlanEstudio().getCpePk());

            //Filtra dependiendo si sección es anual o semestral
            filtroCalEst.setCaeTipoPeriodo(datoCalificaciones.getSeccion().getSecTipoPeriodo());
            filtroCalEst.setCaeNumeroPeriodo(datoCalificaciones.getSeccion().getSecNumeroPeriodo());

            List<SgCalificacionEstudiante> calificaciones = calificacionEstudianteBean.obtenerPorFiltro(filtroCalEst);

            //Removemos calificaciones que sean de otros períodos
            Iterator<SgCalificacionEstudiante> it = calificaciones.iterator();
            while (it.hasNext()) {
                SgCalificacionEstudiante cal = it.next();
                if (cal.getCaeCalificacion().getCalRangoFecha() != null
                        && !cal.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion().getPcaPk().equals(datoCalificaciones.getPeriodoCalificacionPk())) {
                    it.remove();
                }
            }

            SgCalificacionCE calNotaInst = calcularNotaInstitucional(datoCalificaciones, listEstudiantes, calificaciones);
            //Se remplazan las notas insitucionales nuevas
            calificaciones = calificaciones.stream().filter(c -> !c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());
            calificaciones.addAll(calNotaInst.getCalCalificacionesEstudiante());

            //Para calcular nota de aprobación, los estudiantes tienen que tener calificación en todos los períodos ordinarios
            List<SgCalificacionEstudiante> calificacionesOrdinarias = calificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)).collect(Collectors.toList());
            Boolean[] permiteCalculoAprobacionEstudiante = new Boolean[listEstudiantes.size()];

            for (SgEstudiante est : listEstudiantes) {
                List<SgCalificacionEstudiante> calificacionesDePeriodos = calificacionesOrdinarias.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());

                permiteCalculoAprobacionEstudiante[listEstudiantes.indexOf(est)] = calificacionesDePeriodos.size() >= componentePlanGrado.getCpgPeriodosCalificacion();

            }

            if (TipoEscalaCalificacion.NUMERICA.equals(componentePlanGrado.getCpgEscalaCalificacion().getEcaTipoEscala())) {
                if (componentePlanGrado.getCpgFormula() == null || StringUtils.isBlank(componentePlanGrado.getCpgFormula().getFomTextoLargo())) {
                    BusinessException ge = new BusinessException();
                    ge.addError("asiEstudiante", Errores.ERROR_COMPONENTE_SIN_FORMULA);
                    throw ge;

                }
                if (componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion() == null) {
                    BusinessException ge = new BusinessException();
                    ge.addError("asiEstudiante", Errores.ERROR_ESCALA_SIN_MINIMO);
                    throw ge;
                }

                SgCalificacionCE calificacionAprobacionComponente = new SgCalificacionCE();
                List<SgCalificacionEstudiante> calEstudiantes = calificaciones.stream().filter(c -> EnumTiposPeriodosCalificaciones.APR.equals(c.getCaeCalificacion().getCalTipoPeriodoCalificacion())).collect(Collectors.toList());
                if (!calEstudiantes.isEmpty()) {
                    //Me quedo con el cabezal cuya sec se corresponda con la sec que estoy calificando
                    //Si hay calificaciones APR de otros cabezales porque fueron trasladados, se les actualiza el cabezal
                    calificacionAprobacionComponente = calEstudiantes.stream()
                            .map(ce -> ce.getCaeCalificacion())
                            .filter(c -> c.getCalSeccion().getSecPk().equals(seccionEnEdicion.getSecPk()))
                            .findFirst()
                            .orElse(new SgCalificacionCE());
                    //Cargo calificaciones existentes al cabezal
                    calificacionAprobacionComponente.setCalCalificacionesEstudiante(calEstudiantes);
                }

                if (calificacionAprobacionComponente.getCalPk() == null) {

                    //En algunos casos se da de que existe el cabezal pero ningún estudiante está asociado, 
                    //se hace búsqueda de cabezal, en caso de que sea vacio se crea uno nuevo
                    FiltroCalificacion fc = new FiltroCalificacion();
                    fc.setSecPk(seccionEnEdicion.getSecPk());
                    fc.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                    fc.setCalComponentePlanEstudio(componentePlanGrado.getCpgComponentePlanEstudio().getCpePk());
                    fc.setIncluirCampos(new String[]{"calPk",
                        "calComponentePlanEstudio.cpePk",
                        "calComponentePlanEstudio.cpeVersion",
                        "calComponentePlanEstudio.cpeTipo",
                        "calSeccion.secPk",
                        "calSeccion.secVersion",
                        "calFecha",
                        "calTipoPeriodoCalificacion",
                        "calTipoCalendarioEscolar",
                        "calNumero",
                        "calVersion",
                        "calCerrado",
                        "calEstadoProcesamientoPromocion",
                        "calInfoProcesamientoCalificacionFk.ipcPk",
                        "calInfoProcesamientoCalificacionFk.ipcError",
                        "calInfoProcesamientoCalificacionFk.ipcVersion"});
                    List<SgCalificacionCE> calList = this.obtenerPorFiltro(fc);

                    if (!calList.isEmpty()) {
                        calificacionAprobacionComponente = calList.get(0);
                        //Cargo calificaciones existentes al cabezal
                        calificacionAprobacionComponente.setCalCalificacionesEstudiante(calEstudiantes);
                    }

                    calificacionAprobacionComponente.setCalComponentePlanEstudio(componentePlanGrado.getCpgComponentePlanEstudio());
                    calificacionAprobacionComponente.setCalSeccion(seccionEnEdicion);
                    calificacionAprobacionComponente.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                }
                calificacionAprobacionComponente.setCalFecha(LocalDate.now());

                Function formula = new Function(componentePlanGrado.getCpgFormula().getFomTextoLargo());

                if (BooleanUtils.isTrue(componentePlanGrado.getCpgFormula().getFomTienSubformula())) {
                    //SE UTILIZA OBTENERPORID A MODO DE OBTENER LA LISTA DE SUBFORMULAS
                    CodigueraDAO<SgFormula> codDAOFormula = new CodigueraDAO<>(em, SgFormula.class);
                    SgFormula form = codDAOFormula.obtenerPorId(componentePlanGrado.getCpgFormula().getFomPk(), null);

                    formula = cargarSubformula(form);

                }
                String[] formulasAuxiliares = new String[]{"maximo(a,b)=if(isNaN(a),if(isNaN(b),0,b),if(isNaN(b),a,max(a,b)))", "maxTres(a,b,c)=maximo(a,maximo(b,c))"};
                Function formulaAx0 = new Function(formulasAuxiliares[0]);
                Function formulaAx1 = new Function(formulasAuxiliares[1]);
                formulaAx1.addFunctions(formulaAx0);
                formula.addDefinitions(formulaAx0);
                formula.addDefinitions(formulaAx1);
                /*  for(String formulaAux:formulasAuxiliares){
                            Function formulaAx = new Function(formulaAux);
                            formula.addFunctions(formulaAx);
                        }*/

                List<SgCalificacionEstudiante> calificacionesCalculoAprobacion = new ArrayList();

                calificacionesCalculoAprobacion.addAll(calificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)
                        || c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD)).collect(Collectors.toList()));

                String parteDefinicion;
                String[] formulaSeparada = componentePlanGrado.getCpgFormula().getFomTextoLargo().split("=", 2);
                parteDefinicion = formulaSeparada[0];
                parteDefinicion = parteDefinicion.trim();

                //BÚSQUEDA DE CALIFICACIONES PARA PARÁMETRO NOTAEVAL
                List<SgCalificacionEstudiante> calEstNOTAEVAL = new ArrayList();
                if (EnumTipoFormula.COMPONENTE_PLAN_ESTUDIO_PARAM.equals(componentePlanGrado.getCpgFormula().getFomTipoFormula())
                        && componentePlanGrado.getCpgParametroFormulaAprobacion() != null) {
                    FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                    fce.setCaeEstudiantesPk(listEstudiantes.stream().map(c -> c.getEstPk()).collect(Collectors.toList()));
                    fce.setCaeTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.NOTIN);
                    fce.setCaeComponentePlanEstudio(componentePlanGrado.getCpgParametroFormulaAprobacion().getCpgComponentePlanEstudio().getCpePk());

                    fce.setCaeGradoFk(datoCalificaciones.getSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                    //Filtra dependiendo si sección es anual o semestral
                    fce.setCaeTipoPeriodo(datoCalificaciones.getSeccion().getSecTipoPeriodo());
                    fce.setCaeNumeroPeriodo(datoCalificaciones.getSeccion().getSecNumeroPeriodo());

                    fce.setIncluirCampos(new String[]{"caeEstudiante.estPk", "caeCalificacionNumericaEstudiante"});
                    calEstNOTAEVAL = calificacionEstudianteBean.obtenerPorFiltro(fce);
                }

                for (SgEstudiante est : listEstudiantes) {

                    String ni = null, ppe = null, ppeps = null, spe = null, speps = null;
                    Expression expresion = new Expression(parteDefinicion, formula);

                    List<SgCalificacionEstudiante> listCales = calificacionesCalculoAprobacion.stream().filter(s -> s.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                    Argument arg_ni = new Argument("ni", Double.NaN);
                    expresion.addArguments(arg_ni);
                    Argument arg_ppe = new Argument("ppe", Double.NaN);
                    expresion.addArguments(arg_ppe);
                    Argument arg_ppeps = new Argument("ppeps", Double.NaN);
                    expresion.addArguments(arg_ppeps);
                    Argument arg_spe = new Argument("spe", Double.NaN);
                    expresion.addArguments(arg_spe);
                    Argument arg_sppe = new Argument("sppe", Double.NaN);
                    expresion.addArguments(arg_sppe);
                    Argument arg_notaeval = new Argument("NOTAEVAL", Double.NaN);
                    expresion.addArguments(arg_notaeval);
                    Argument arg_mayor_nota = new Argument("mayorNota", Double.NaN);
                    expresion.addArguments(arg_mayor_nota);

                    Double mayorNota = 0.0d;

                    if (!listCales.isEmpty() && permiteCalculoAprobacionEstudiante[listEstudiantes.indexOf(est)]) {

                        Integer[] datosFijosUltimoNumero = new Integer[4]; //Pueden haber varias PP, PPS, etc. Hay que quedarse con la última.
                        //Se agregad Double.NaN para tener null

                        //    Argument arg_ppeps = new Argument("ppeps",Double.NaN);
                        //   Argument arg_spe = new Argument("spe",Double.NaN);
                        for (SgCalificacionEstudiante calest : listCales) {

                            if (!StringUtils.isBlank(calest.getCaeCalificacionNumericaEstudiante())) {
                                if (mayorNota.compareTo(Double.valueOf(calest.getCaeCalificacionNumericaEstudiante())) < 0) {
                                    mayorNota = Double.valueOf(calest.getCaeCalificacionNumericaEstudiante());
                                }
                            }

                            //SgCalificacionEstudiante calest = listCales.get(0);
                            if (calest.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)) {

                                ni = calest.getCaeCalificacionNumericaEstudiante();
                                if (!StringUtils.isBlank(ni)) {
                                    //   Argument arg_ni = new Argument("ni=" + ni);
                                    //    arg_ni.setArgumentValue(Double.valueOf(ni));
                                    //   expresion.addArguments(arg_ni);
                                    expresion.setArgumentValue("ni", Double.valueOf(ni));
                                }

                            } else {
                                if (calest.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.PREC)) {

                                    if (datosFijosUltimoNumero[0] == null || datosFijosUltimoNumero[0] < calest.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                        datosFijosUltimoNumero[0] = calest.getCaeCalificacion().getCalNumero();
                                        ppe = calest.getCaeCalificacionNumericaEstudiante();
                                    }

                                    if (!StringUtils.isBlank(ppe)) {
                                        //Argument arg_ppe = new Argument("ppe=" + ppe);
                                        // arg_ppe.setArgumentValue(Double.valueOf(ppe));
                                        expresion.setArgumentValue("ppe", Double.valueOf(ppe));
                                    }

                                }
                                if (calest.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.PRECPS)) {

                                    if (datosFijosUltimoNumero[1] == null || datosFijosUltimoNumero[1] < calest.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                        datosFijosUltimoNumero[1] = calest.getCaeCalificacion().getCalNumero();
                                        ppeps = calest.getCaeCalificacionNumericaEstudiante();
                                    }

                                    if (!StringUtils.isBlank(ppeps)) {
                                        //  Argument arg_ppeps = new Argument("ppeps=" + ppeps);
                                        // expresion.addArguments(arg_ppeps);
                                        expresion.setArgumentValue("ppeps", Double.valueOf(ppeps));
                                    }
                                }
                                if (calest.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.SREC)) {

                                    if (datosFijosUltimoNumero[2] == null || datosFijosUltimoNumero[2] < calest.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                        datosFijosUltimoNumero[2] = calest.getCaeCalificacion().getCalNumero();
                                        spe = calest.getCaeCalificacionNumericaEstudiante();
                                    }

                                    if (!StringUtils.isBlank(spe)) {
                                        //  Argument arg_spe = new Argument("spe=" + spe);
                                        // expresion.addArguments(arg_spe);
                                        expresion.setArgumentValue("spe", Double.valueOf(spe));
                                    }
                                }
                                if (calest.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.SRECPS)) {

                                    if (datosFijosUltimoNumero[3] == null || datosFijosUltimoNumero[3] < calest.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                        datosFijosUltimoNumero[3] = calest.getCaeCalificacion().getCalNumero();
                                        speps = calest.getCaeCalificacionNumericaEstudiante();
                                    }

                                    if (!StringUtils.isBlank(speps)) {
                                        //Argument arg_speps = new Argument("sppe=" + speps);
                                        //expresion.addArguments(arg_speps);
                                        expresion.setArgumentValue("sppe", Double.valueOf(speps));
                                    }
                                }
                            }
                        }
                    }

                    List<SgCalificacionEstudiante> calEstudianteNOTAEVAL = calEstNOTAEVAL.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                    if (!calEstudianteNOTAEVAL.isEmpty()) {
                        if (!StringUtils.isBlank(calEstudianteNOTAEVAL.get(0).getCaeCalificacionNumericaEstudiante())) {
                            expresion.setArgumentValue("NOTAEVAL", Double.valueOf(calEstudianteNOTAEVAL.get(0).getCaeCalificacionNumericaEstudiante()));
                        }
                    }

                    if (mayorNota.compareTo(0.0) > 0) {
                        expresion.setArgumentValue("mayorNota", mayorNota);
                    }

                    if (componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion() != null) {
                        Argument arg_speps = new Argument("ma=" + componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion().toString());
                        expresion.addArguments(arg_speps);
                    }

                    SgComponentePlanEstudio cpl = componentePlanGrado.getCpgComponentePlanEstudio();
                    SgEscalaCalificacion escala = componentePlanGrado.getCpgEscalaCalificacion();
                    Integer precision = null;
                    if (componentePlanGrado.getCpgPrecision() != null) {
                        precision = componentePlanGrado.getCpgPrecision();
                    } else {
                        precision = escala.getEcaPrecision();
                    }

                    Double res = expresion.calculate();

                    if (TipoComponentePlanEstudio.PRU.equals(cpl.getCpeTipo()) && precision != null && precision > 0) {
                        res = Double.isNaN(res) ? null : MathFunctionsUtils.redondear(res, precision, RoundingMode.HALF_UP);
                    } else {
                        res = Double.isNaN(res) ? null : MathFunctionsUtils.redondear(res, 0, RoundingMode.HALF_UP);
                    }

                    String resultado = res == null ? null : String.valueOf(res);
                    if (res != null) {
                        Double d = Double.parseDouble(res.toString());
                        if (TipoComponentePlanEstudio.PRU.equals(cpl.getCpeTipo()) && precision != null && precision > 0) {
                            resultado = NumberFormatUtils.formatDouble(d, precision);
                        } else {
                            resultado = NumberFormatUtils.formatDouble(d, null);
                        }
                    }

                    EnumResolucionCalificacion enumRes = EnumResolucionCalificacion.PENDIENTE;
                    if (componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimo() != null && res != null) {
                        if (res >= componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion()) {
                            enumRes = EnumResolucionCalificacion.APROBADO;
                        } else {
                            enumRes = EnumResolucionCalificacion.NO_APROBADO;
                        }

                    }

                    Boolean encontro = Boolean.FALSE;
                    aprobacion:
                    for (SgCalificacionEstudiante calEstudiante : calificacionAprobacionComponente.getCalCalificacionesEstudiante()) {
                        if (calEstudiante.getCaeEstudiante().getEstPk().equals(est.getEstPk())) {
                            calEstudiante.setCaeCalificacionNumericaEstudiante(resultado);
                            calEstudiante.setCaeFechaRealizado(LocalDate.now());
                            calEstudiante.setCaeResolucion(enumRes);
                            encontro = Boolean.TRUE;
                            break aprobacion;
                        }
                    }
                    if (!encontro) {
                        SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();
                        calEst.setCaeCalificacionNumericaEstudiante(resultado);
                        calEst.setCaeEstudiante(est);
                        calEst.setCaeFechaRealizado(LocalDate.now());
                        calEst.setCaeResolucion(enumRes);
                        calificacionAprobacionComponente.getCalCalificacionesEstudiante().add(calEst);
                    }

                }
                for (SgCalificacionEstudiante calEst : calificacionAprobacionComponente.getCalCalificacionesEstudiante()) {
                    calEst.setCaeCalificacion(calificacionAprobacionComponente);
                }

                //cuando datoCalificaciones.getEstudiantePk() es distinto de NULL significa que el calculo se hace para un solo estudiante
                //AL EDITAR LA NOTA DE UN SOLO ESTUDIANTE HAY QUE ASEGURAR QUE LAS DE LOS DEMAS NO SE BORREN CON ORPHANREMOVAL
                if (calificacionAprobacionComponente.getCalPk() != null && datoCalificaciones.getEstudiantePk() != null) {
                    FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                    fce.setCabezalPk(calificacionAprobacionComponente.getCalPk());
                    fce.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                        "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                        "caeCalificacion.calComponentePlanEstudio.cpePk",
                        "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                        "caeCalificacion.calComponentePlanEstudio.cpeVersion",
                        "caeCalificacion.calTipoCalendarioEscolar",
                        "caeCalificacion.calTipoPeriodoCalificacion",
                        "caeCalificacion.calNumero",
                        "caeCalificacion.calSeccion.secPk",
                        "caeCalificacion.calSeccion.secVersion",
                        "caeCalificacion.calFecha",
                        "caeCalificacion.calUltModFecha",
                        "caeCalificacion.calUltModUsuario",
                        "caeCalificacion.calVersion",
                        "caeCalificacion.calCerrado",
                        "caeCalificacion.calRangoFecha.rfePk",
                        "caeCalificacion.calRangoFecha.rfeFechaHasta",
                        "caeCalificacion.calRangoFecha.rfeCodigo",
                        "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                        "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                        "caeCalificacion.calRangoFecha.rfeVersion",
                        "caeCalificacionNumericaEstudiante",
                        "caeCalificacionConceptualEstudiante.calPk",
                        "caeCalificacionConceptualEstudiante.calCodigo",
                        "caeCalificacionConceptualEstudiante.calValor",
                        "caeCalificacionConceptualEstudiante.calOrden",
                        "caeCalificacionConceptualEstudiante.calVersion",
                        "caeVersion",
                        "caeResolucion",
                        "caeEstudiante.estPk",
                        "caeEstudiante.estVersion"});
                    List<SgCalificacionEstudiante> calCE = calificacionEstudianteBean.obtenerPorFiltro(fce);
                    calCE = calCE.stream().filter(c -> !c.getCaeEstudiante().getEstPk().equals(datoCalificaciones.getEstudiantePk())).collect(Collectors.toList());
                    calificacionAprobacionComponente.getCalCalificacionesEstudiante().addAll(calCE);

                }

                //Si el cabezal es nuevo, todas las calificaciones existentes que tengan cabezal incorrecto y deben apuntar al nuevo se regeneran
                //Se hace de este modo para evitar detached entity passed to persist
                List<Long> calificacionesExistentes = new ArrayList<>();
                if (calificacionAprobacionComponente.getCalPk() == null) {
                    Iterator<SgCalificacionEstudiante> calEstIterator = calificacionAprobacionComponente.getCalCalificacionesEstudiante().iterator();
                    while (calEstIterator.hasNext()) {
                        SgCalificacionEstudiante ce = calEstIterator.next();
                        if (ce.getCaePk() != null) {
                            calificacionesExistentes.add(ce.getCaePk());
                        }
                        ce.setCaePk(null);
                        ce.setCaeVersion(null);
                    }
                }

                this.guardar(calificacionAprobacionComponente, null, Boolean.FALSE);

                if (!calificacionesExistentes.isEmpty()) {
                    //Eliminar calificaciones incorrectas
                    em.createQuery("DELETE FROM  SgCalificacionEstudiante where caePk IN (:ids)")
                            .setParameter("ids", calificacionesExistentes)
                            .executeUpdate();

                }

            } else {
                List<SgCalificacionEstudiante> calificacionesCalculoAprobacion = new ArrayList();

                calificacionesCalculoAprobacion.addAll(calificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList()));

                SgCalificacionCE calificacionAprobacionComponente = new SgCalificacionCE();
                List<SgCalificacionEstudiante> calEstudiantes = calificaciones.stream().filter(c -> EnumTiposPeriodosCalificaciones.APR.equals(c.getCaeCalificacion().getCalTipoPeriodoCalificacion())).collect(Collectors.toList());
                if (!calEstudiantes.isEmpty()) {
                    //Me quedo con el cabezal cuya sec se corresponda con la sec que estoy calificando
                    //Si hay calificaciones APR de otros cabezales porque fueron trasladados, se les actualiza el cabezal
                    calificacionAprobacionComponente = calEstudiantes.stream()
                            .map(ce -> ce.getCaeCalificacion())
                            .filter(c -> c.getCalSeccion().getSecPk().equals(seccionEnEdicion.getSecPk()))
                            .findFirst()
                            .orElse(new SgCalificacionCE());
                    //Cargo calificaciones existentes al cabezal
                    calificacionAprobacionComponente.setCalCalificacionesEstudiante(calEstudiantes);
                }
                if (calificacionAprobacionComponente.getCalPk() == null) {

                    //En algunos casos se da de que existe el cabezal pero ningún estudiante está asociado, 
                    //se hace búsqueda de cabezal, en caso de que sea vacio se crea uno nuevo
                    FiltroCalificacion fc = new FiltroCalificacion();
                    fc.setSecPk(seccionEnEdicion.getSecPk());
                    fc.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                    fc.setCalComponentePlanEstudio(componentePlanGrado.getCpgComponentePlanEstudio().getCpePk());
                    fc.setIncluirCampos(new String[]{"calPk",
                        "calComponentePlanEstudio.cpePk",
                        "calComponentePlanEstudio.cpeVersion",
                        "calComponentePlanEstudio.cpeTipo",
                        "calSeccion.secPk",
                        "calSeccion.secVersion",
                        "calFecha",
                        "calTipoPeriodoCalificacion",
                        "calTipoCalendarioEscolar",
                        "calNumero",
                        "calVersion",
                        "calCerrado",
                        "calEstadoProcesamientoPromocion",
                        "calInfoProcesamientoCalificacionFk.ipcPk",
                        "calInfoProcesamientoCalificacionFk.ipcError",
                        "calInfoProcesamientoCalificacionFk.ipcVersion"});
                    List<SgCalificacionCE> calList = this.obtenerPorFiltro(fc);

                    if (!calList.isEmpty()) {
                        calificacionAprobacionComponente = calList.get(0);
                        //Cargo calificaciones existentes al cabezal
                        calificacionAprobacionComponente.setCalCalificacionesEstudiante(calEstudiantes);
                    }

                    calificacionAprobacionComponente.setCalComponentePlanEstudio(componentePlanGrado.getCpgComponentePlanEstudio());
                    calificacionAprobacionComponente.setCalSeccion(seccionEnEdicion);
                    calificacionAprobacionComponente.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                }

                if (calificacionAprobacionComponente.getCalPk() == null) {
                    calificacionAprobacionComponente.setCalComponentePlanEstudio(componentePlanGrado.getCpgComponentePlanEstudio());
                    calificacionAprobacionComponente.setCalSeccion(seccionEnEdicion);
                    calificacionAprobacionComponente.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                }
                calificacionAprobacionComponente.setCalFecha(LocalDate.now());

                for (SgEstudiante est : listEstudiantes) {

                    List<SgCalificacionEstudiante> calNotaInstitucional = calificacionesCalculoAprobacion.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());

                    List<SgCalificacionEstudiante> listCalificacionAprobacionEstudiante = calificacionAprobacionComponente.getCalCalificacionesEstudiante().stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());

                    SgCalificacionEstudiante calEstudiante = new SgCalificacionEstudiante();

                    if (!listCalificacionAprobacionEstudiante.isEmpty()) {
                        calEstudiante = listCalificacionAprobacionEstudiante.get(0);
                    }
                    calEstudiante.setCaeEstudiante(est);
                    calEstudiante.setCaeFechaRealizado(LocalDate.now());
                    calEstudiante.setCaeResolucion(EnumResolucionCalificacion.PENDIENTE);
                    if (!calNotaInstitucional.isEmpty() && permiteCalculoAprobacionEstudiante[listEstudiantes.indexOf(est)]) {
                        calEstudiante.setCaeCalificacionConceptualEstudiante(calNotaInstitucional.get(0).getCaeCalificacionConceptualEstudiante());
                        if (componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo() != null && componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo().getCalOrden() != null) {
                            if (calEstudiante.getCaeCalificacionConceptualEstudiante().getCalOrden() >= componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo().getCalOrden()) {
                                calEstudiante.setCaeResolucion(EnumResolucionCalificacion.APROBADO);
                            } else {
                                calEstudiante.setCaeResolucion(EnumResolucionCalificacion.NO_APROBADO);
                            }
                        }
                    }

                    if (calEstudiante.getCaePk() == null) {
                        calificacionAprobacionComponente.getCalCalificacionesEstudiante().add(calEstudiante);
                    }
                }
                for (SgCalificacionEstudiante calEst : calificacionAprobacionComponente.getCalCalificacionesEstudiante()) {
                    calEst.setCaeCalificacion(calificacionAprobacionComponente);
                }

                //cuando datoCalificaciones.getEstudiantePk() es distinto de NULL significa que el calculo se hace para un solo estudiante
                //AL EDITAR LA NOTA DE UN SOLO ESTUDIANTE HAY QUE ASEGURAR QUE LAS DE LOS DEMAS NO SE BORREN CON ORPHANREMOVAL
                if (calificacionAprobacionComponente.getCalPk() != null && datoCalificaciones.getEstudiantePk() != null) {
                    FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                    fce.setCabezalPk(calificacionAprobacionComponente.getCalPk());
                    fce.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                        "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                        "caeCalificacion.calComponentePlanEstudio.cpePk",
                        "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                        "caeCalificacion.calComponentePlanEstudio.cpeVersion",
                        "caeCalificacion.calTipoCalendarioEscolar",
                        "caeCalificacion.calTipoPeriodoCalificacion",
                        "caeCalificacion.calNumero",
                        "caeCalificacion.calSeccion.secPk",
                        "caeCalificacion.calSeccion.secVersion",
                        "caeCalificacion.calFecha",
                        "caeCalificacion.calUltModFecha",
                        "caeCalificacion.calUltModUsuario",
                        "caeCalificacion.calVersion",
                        "caeCalificacion.calCerrado",
                        "caeCalificacion.calRangoFecha.rfePk",
                        "caeCalificacion.calRangoFecha.rfeFechaHasta",
                        "caeCalificacion.calRangoFecha.rfeCodigo",
                        "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                        "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                        "caeCalificacion.calRangoFecha.rfeVersion",
                        "caeCalificacionNumericaEstudiante",
                        "caeCalificacionConceptualEstudiante.calPk",
                        "caeCalificacionConceptualEstudiante.calCodigo",
                        "caeCalificacionConceptualEstudiante.calValor",
                        "caeCalificacionConceptualEstudiante.calOrden",
                        "caeCalificacionConceptualEstudiante.calVersion",
                        "caeVersion",
                        "caeResolucion",
                        "caeEstudiante.estPk",
                        "caeEstudiante.estVersion"});
                    List<SgCalificacionEstudiante> calCE = calificacionEstudianteBean.obtenerPorFiltro(fce);
                    calCE = calCE.stream().filter(c -> !c.getCaeEstudiante().getEstPk().equals(datoCalificaciones.getEstudiantePk())).collect(Collectors.toList());
                    calificacionAprobacionComponente.getCalCalificacionesEstudiante().addAll(calCE);

                }

                //Si el cabezal es nuevo, todas las calificaciones existentes que tengan cabezal incorrecto y deben apuntar al nuevo se regeneran
                //Se hace de este modo para evitar detached entity passed to persist
                List<Long> calificacionesExistentes = new ArrayList<>();
                if (calificacionAprobacionComponente.getCalPk() == null) {
                    Iterator<SgCalificacionEstudiante> calEstIterator = calificacionAprobacionComponente.getCalCalificacionesEstudiante().iterator();
                    while (calEstIterator.hasNext()) {
                        SgCalificacionEstudiante ce = calEstIterator.next();
                        if (ce.getCaePk() != null) {
                            calificacionesExistentes.add(ce.getCaePk());
                        }
                        ce.setCaePk(null);
                        ce.setCaeVersion(null);
                    }
                }
                this.guardar(calificacionAprobacionComponente, null, Boolean.FALSE);

                if (!calificacionesExistentes.isEmpty()) {
                    //Eliminar calificaciones incorrectas
                    em.createQuery("DELETE FROM  SgCalificacionEstudiante where caePk IN (:ids)")
                            .setParameter("ids", calificacionesExistentes)
                            .executeUpdate();

                }

            }
        }

        return Boolean.TRUE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param cal SgCalificacion
     * @param dataSecurity
     * @return SgCalificacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionCE guardar(SgCalificacionCE cal, Boolean dataSecurity, Boolean validarUsuarioDictaComponenteEnSeccion) throws GeneralException {
        try {
            if (cal != null) {
                //TODO: Validar seccion tenga plan estudio y servicio educativo? o cargarlas
                if (CalificacionValidacionNegocio.validar(cal)) {
                    if (!EnumTiposPeriodosCalificaciones.GRA.equals(cal.getCalTipoPeriodoCalificacion())) {

                        FiltroSeccion fs = new FiltroSeccion();
                        fs.setIncluirCampos(new String[]{"secServicioEducativo.sduGrado.graPk", "secPlanEstudio.pesPk"});
                        fs.setSecPk(cal.getCalSeccion().getSecPk());
                        fs.setSecurityOperation(null);
                        List<SgSeccion> secciones = seccionBean.obtenerPorFiltro(fs);
                        if (secciones.isEmpty()) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_SECCION_VACIO);
                            throw be;
                        }
                        SgSeccion sec = secciones.get(0);

                        FiltroComponentePlanGrado fcp = new FiltroComponentePlanGrado();
                        fcp.setCpgGradoPk(sec.getSecServicioEducativo().getSduGrado().getGraPk());
                        fcp.setCpgPlanEstudioPk(sec.getSecPlanEstudio().getPesPk());
                        fcp.setCpgComponentePlanEstudioPk(cal.getCalComponentePlanEstudio().getCpePk());
                        fcp.setCpgAgregandoSeccionExclusiva(cal.getCalSeccion().getSecPk());
                        fcp.setMaxResults(1L);
                        fcp.setIncluirCampos(new String[]{"cpgEscalaCalificacion"});
                        List<SgComponentePlanGrado> cpg = componentePlanGradoBean.obtenerPorFiltro(fcp);

                        SgEscalaCalificacion escala = null;
                        if (!cpg.isEmpty()) {
                            escala = cpg.get(0).getCpgEscalaCalificacion();
                        }
                        for (SgCalificacionEstudiante calEst : cal.getCalCalificacionesEstudiante()) {
                            CalificacionEstudianteValidacionNegocio.validar(calEst, escala);
                        }

                        if (BooleanUtils.isTrue(validarUsuarioDictaComponenteEnSeccion)) {
                            JsonWebToken jwt = Lookup.obtenerJWT();
                            if (!jwt.getGroups().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)
                                    && !horarioEscolarBean.usuarioDictaComponenteEnSeccion(
                                            jwt.getSubject(),
                                            cal.getCalSeccion().getSecPk(),
                                            cal.getCalComponentePlanEstudio().getCpePk())) {
                                BusinessException be = new BusinessException();
                                be.addError(Errores.ERROR_USUARIO_NO_TIENE_HORARIO_ESCOLAR_ASIGNADO_PARA_SECCION_COMPONENTE);
                                throw be;
                            }
                        }

                        //Si pk == null y no tiene dato, no se guarda
                        //Si pk != null y no tiene dato, se borra por orphanRemoval
                        if (cal.getCalCalificacionesEstudiante() != null) {
                            if (escala.getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                                cal.getCalCalificacionesEstudiante().forEach(c -> c.setCaeCalificacionConceptualEstudiante(null));
                            } else if (escala.getEcaTipoEscala().equals(TipoEscalaCalificacion.CONCEPTUAL)) {
                                cal.getCalCalificacionesEstudiante().forEach(c -> c.setCaeCalificacionNumericaEstudiante(null));
                            }
                            cal.getCalCalificacionesEstudiante()
                                    .removeIf(c -> StringUtils.isBlank(c.getCaeCalificacionNumericaEstudiante()) && c.getCaeCalificacionConceptualEstudiante() == null);
                        }
                    }

                    CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                    if (EnumTiposPeriodosCalificaciones.GRA.equals(cal.getCalTipoPeriodoCalificacion())) {

                        Boolean todasRealizadas = Boolean.TRUE;

                        for (SgCalificacionEstudiante calEst : cal.getCalCalificacionesEstudiante()) {

                            if (calEst.getCaePromovidoCalificacion() == null || EnumPromovidoCalificacion.PENDIENTE.equals(calEst.getCaePromovidoCalificacion())) {
                                todasRealizadas = Boolean.FALSE;
                            }

                            //TODO hace una query sola que traiga todas las matriculas
                            //ARREGLAR
                            SgEstudiante est = calEst.getCaeEstudiante();
                            FiltroMatricula fmat = new FiltroMatricula();
                            fmat.setSecPk(cal.getCalSeccion().getSecPk());
                            fmat.setMatEstudiantePk(est.getEstPk());
                            fmat.setMatEstado(EnumMatriculaEstado.ABIERTO);
                            List<SgMatricula> listmat = matriculaBean.obtenerPorFiltro(fmat);
                            if (!listmat.isEmpty()) {
                                SgMatricula mat = listmat.get(0);
                                if (EnumPromovidoCalificacion.NO_PROMOVIDO.equals(calEst.getCaePromovidoCalificacion())) {
                                    mat.setMatPromocionGrado(EnumPromocionGradoMatricula.NO_PROMOVIDO);
                                }

                                if (EnumPromovidoCalificacion.PROMOVIDO.equals(calEst.getCaePromovidoCalificacion())) {
                                    mat.setMatPromocionGrado(EnumPromocionGradoMatricula.PROMOVIDO);
                                }
                                if (EnumPromovidoCalificacion.PENDIENTE.equals(calEst.getCaePromovidoCalificacion())) {
                                    mat.setMatPromocionGrado(null);
                                }
                                matriculaBean.guardar(mat);
                            }
                        }

                        cal.setCalTodasPromocionesGradoRealizadas(todasRealizadas);

                        em.createNativeQuery("UPDATE centros_educativos.sg_secciones SET sec_todas_promociones_grado_realizadas = :valor where sec_pk = :secPk")
                                .setParameter("valor", todasRealizadas)
                                .setParameter("secPk", cal.getCalSeccion().getSecPk())
                                .executeUpdate();

                    }

                    //Cargar sexo en persona estudiantes
                    //Utilizado para calcular promedio y moda por sexo
                    List<Long> estPks = cal.getCalCalificacionesEstudiante().stream().map(e -> e.getCaeEstudiante().getEstPk()).collect(Collectors.toList());
                    if (!estPks.isEmpty()) {
                        FiltroEstudiante filtroEst = new FiltroEstudiante();
                        filtroEst.setEstudiantesPk(estPks);
                        filtroEst.setSecurityOperation(null);
                        filtroEst.setIncluirCampos(new String[]{"estPk", "estVersion", "estPersona.perSexo.sexPk", "estPersona.perSexo.sexCodigo", "estPersona.perSexo.sexVersion"});
                        List<SgEstudiante> estudiantes = estudianteBean.obtenerPorFiltro(filtroEst);
                        HashMap<Long, SgEstudiante> hashEstudiantes = new HashMap<>();
                        for (SgEstudiante e : estudiantes) {
                            hashEstudiantes.put(e.getEstPk(), e);
                        }

                        for (SgCalificacionEstudiante cae : cal.getCalCalificacionesEstudiante()) {
                            if (cae.getCaeEstudiante().getEstPersona() == null) {
                                cae.getCaeEstudiante().setEstPersona(new SgPersona());
                            }
                            cae.getCaeEstudiante().getEstPersona().setPerSexo(hashEstudiantes.get(cae.getCaeEstudiante().getEstPk()).getEstPersona().getPerSexo());
                        }
                    }
                    //Fin cargar sexo en persona estudiantes

                    //Seteamos cantidad de estudiantes calificados en el cabezal
                    cal.setCalCantEstudiantesCalificados(cal.getCalCalificacionesEstudiante().size());

                    //Seteamos promedios y modas
                    cal.setCalPromedioCalificaciones(cal.calcularPromedioCalificaciones(null));
                    cal.setCalPromedioCalificacionesMasculino(cal.calcularPromedioCalificaciones(Constantes.SEXO_MASCULINO_PK));
                    cal.setCalPromedioCalificacionesFemenino(cal.calcularPromedioCalificaciones(Constantes.SEXO_FEMENINO_PK));
                    cal.setCalModaCalificacionesConceptuales(cal.calcularModaCalificacionesConceptuales(null));
                    cal.setCalModaCalificacionesConceptualesMasculino(cal.calcularModaCalificacionesConceptuales(Constantes.SEXO_MASCULINO_PK));
                    cal.setCalModaCalificacionesConceptualesFemenino(cal.calcularModaCalificacionesConceptuales(Constantes.SEXO_FEMENINO_PK));
                    cal.setCalPromedioModaDesactualizados(Boolean.FALSE);

                    //En caso de que la calificación sea de prueba, periodo ordinario y tenga un único período se genera automáticamenta nota institucional y aprobacion
                    if (cal.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)
                            && cal.getCalComponentePlanEstudio().getCpeTipo().equals(TipoComponentePlanEstudio.PRU)
                            && cal.getCalRangoFecha().getRfePeriodoCalificacion().getPcaNumero().equals(1)) {
                        guardarNotaInstitucionalAprobacionDePrueba(cal);
                    }

                    if (!EnumTiposPeriodosCalificaciones.GRA.equals(cal.getCalTipoPeriodoCalificacion())) {

                        if (!cal.getCalCalificacionesEstudiante().isEmpty()) {
                            //No guardar cabezales sin calificaciones
                            if (BooleanUtils.isTrue(dataSecurity)) {
                                return codDAO.guardar(cal, cal.getCalPk() == null ? ConstantesOperaciones.CREAR_CALIFICACIONES : ConstantesOperaciones.ACTUALIZAR_CALIFICACIONES);
                            } else {
                                return codDAO.guardar(cal, null);
                            }
                        } else {
                            if (cal.getCalPk() != null) {
                                codDAO.eliminarPorId(cal.getCalPk(), null);
                                cal.setCalPk(null);
                            }
                            return cal;
                        }

                    } else {
                        //Los cabezales de tipo GRADO se pueden guardar sin tener calificaciones (cuando estan pendientes de procesamiento)
                        //Los mismos luego se obtienen haciendo una query de cabezales para procesarlos.
                        if (BooleanUtils.isTrue(dataSecurity)) {
                            return codDAO.guardar(cal, cal.getCalPk() == null ? ConstantesOperaciones.CREAR_CALIFICACIONES : ConstantesOperaciones.ACTUALIZAR_CALIFICACIONES);
                        } else {
                            return codDAO.guardar(cal, null);
                        }
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cal;
    }

    //TODO: recibir pk como param. Recalcular en background
    @Interceptors(AuditInterceptor.class)
    public void guardarNotaInstitucionalAprobacionDePrueba(SgCalificacionCE calificacion) throws GeneralException {
        try {
            //TODO: validar que el rangofecha q viene tenga el periodo de calificacion con pca numero, ? sino ir a buscarlo
            if (calificacion.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)
                    && calificacion.getCalComponentePlanEstudio().getCpeTipo().equals(TipoComponentePlanEstudio.PRU)
                    && calificacion.getCalRangoFecha().getRfePeriodoCalificacion().getPcaNumero().equals(1)) {
                //Busco calificacion del tipo nota institucional y aprobacion

                FiltroComponentePlanGrado fcp = new FiltroComponentePlanGrado();
                fcp.setCpgGradoPk(calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                fcp.setCpgPlanEstudioPk(calificacion.getCalSeccion().getSecPlanEstudio().getPesPk());
                fcp.setCpgComponentePlanEstudioPk(calificacion.getCalComponentePlanEstudio().getCpePk());
                List<SgComponentePlanGrado> cpg = componentePlanGradoBean.obtenerPorFiltro(fcp);
                SgComponentePlanGrado componentePlanGrado = cpg.get(0);

                SgEscalaCalificacion escala = componentePlanGrado.getCpgEscalaCalificacion();
                Integer precision = null;
                if (componentePlanGrado.getCpgPrecision() != null) {
                    precision = componentePlanGrado.getCpgPrecision();
                } else {
                    precision = escala.getEcaPrecision();
                }
                FiltroCalificacion fc = new FiltroCalificacion();
                List<EnumTiposPeriodosCalificaciones> tiposPeriodo = new ArrayList();
                tiposPeriodo.add(EnumTiposPeriodosCalificaciones.NOTIN);
                tiposPeriodo.add(EnumTiposPeriodosCalificaciones.APR);

                fc.setCalTiposPeriodoCalificacion(tiposPeriodo);
                fc.setSecPk(calificacion.getCalSeccion().getSecPk());
                fc.setCalComponentePlanEstudio(calificacion.getCalComponentePlanEstudio().getCpePk());
                fc.setIncluirCalificacionesEstudiantes(Boolean.TRUE);
                fc.setIncluirCampos(new String[]{
                    "calFecha",
                    "calUltModFecha",
                    "calUltModUsuario",
                    "calVersion",
                    "calTipoPeriodoCalificacion",
                    "calTipoCalendarioEscolar",
                    "calNumero",
                    "calCerrado",
                    "calSeccion.secNombre",
                    "calSeccion.secPk",
                    "calSeccion.secVersion",
                    "calComponentePlanEstudio.cpePk",
                    "calComponentePlanEstudio.cpeCodigo",
                    "calComponentePlanEstudio.cpeNombre",
                    "calComponentePlanEstudio.cpeTipo",
                    "calComponentePlanEstudio.cpeVersion",
                    "calComponentePlanEstudio.cpeCodigoExterno",
                    "calComponentePlanEstudio.cpeNombrePublicable",
                    "calRangoFecha.rfePk",
                    "calRangoFecha.rfeCodigo",
                    "calRangoFecha.rfeFechaDesde",
                    "calRangoFecha.rfeHabilitado",
                    "calRangoFecha.rfeFechaHasta",
                    "calRangoFecha.rfeVersion",});

                List<SgCalificacionCE> calificaciones = this.obtenerPorFiltro(fc);

                //Busco si ya tiene NOTA INSTITUCIONAL
                List<SgCalificacionCE> calNotaInstitucionalList = calificaciones.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());

                SgCalificacionCE calNotaIns = new SgCalificacionCE();
                if (calNotaInstitucionalList.isEmpty()) {
                    calNotaIns.setCalComponentePlanEstudio(calificacion.getCalComponentePlanEstudio());
                    calNotaIns.setCalFecha(LocalDate.now());
                    calNotaIns.setCalSeccion(calificacion.getCalSeccion());
                    calNotaIns.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.NOTIN);
                    calNotaIns.setCalCalificacionesEstudiante(new ArrayList());
                } else {
                    calNotaIns = calNotaInstitucionalList.get(0);
                }

                //Busco si ya tiene APROBACION
                List<SgCalificacionCE> calAprList = calificaciones.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.APR)).collect(Collectors.toList());

                SgCalificacionCE calApr = new SgCalificacionCE();
                if (calAprList.isEmpty()) {
                    calApr.setCalComponentePlanEstudio(calificacion.getCalComponentePlanEstudio());
                    calApr.setCalFecha(LocalDate.now());
                    calApr.setCalSeccion(calificacion.getCalSeccion());
                    calApr.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                    calApr.setCalCalificacionesEstudiante(new ArrayList());
                } else {
                    calApr = calAprList.get(0);
                }

                if (calNotaIns.getCalPk() == null) {
                    List<SgCalificacionEstudiante> calEstNotaInstList = new ArrayList();
                    for (SgCalificacionEstudiante calEst : calificacion.getCalCalificacionesEstudiante()) {
                        SgCalificacionEstudiante calEstNotaInst = new SgCalificacionEstudiante();
                        calEstNotaInst.setCaeCalificacionConceptualEstudiante(calEst.getCaeCalificacionConceptualEstudiante());
                        if (calEst.getCaeCalificacionNumericaEstudiante() != null) {
                            calEstNotaInst.setCaeCalificacionNumericaEstudiante(NumberFormatUtils.formatDouble(Double.parseDouble(calEst.getCaeCalificacionNumericaEstudiante()), precision));
                        }
                        calEstNotaInst.setCaeEstudiante(calEst.getCaeEstudiante());
                        calEstNotaInst.setCaeFechaRealizado(calEst.getCaeFechaRealizado());
                        calEstNotaInst.setCaeCalificacion(calNotaIns);
                        calEstNotaInstList.add(calEstNotaInst);
                    }
                    calNotaIns.getCalCalificacionesEstudiante().addAll(calEstNotaInstList);

                } else {

                    for (SgCalificacionEstudiante calEst : calNotaIns.getCalCalificacionesEstudiante()) {
                        List<SgCalificacionEstudiante> calEstudianteNueva = calificacion.getCalCalificacionesEstudiante().stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());
                        if (!calEstudianteNueva.isEmpty()) {
                            calEst.setCaeCalificacionConceptualEstudiante(calEstudianteNueva.get(0).getCaeCalificacionConceptualEstudiante());
                            if (calEstudianteNueva.get(0) != null) {
                                calEst.setCaeCalificacionNumericaEstudiante(NumberFormatUtils.formatDouble(Double.parseDouble(calEstudianteNueva.get(0).getCaeCalificacionNumericaEstudiante()), precision));
                            }
                        }
                    }

                    for (SgCalificacionEstudiante calEst : calificacion.getCalCalificacionesEstudiante()) {
                        List<SgCalificacionEstudiante> calEstudianteNueva = calNotaIns.getCalCalificacionesEstudiante().stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());
                        if (calEstudianteNueva.isEmpty()) {
                            SgCalificacionEstudiante calEstNotaInst = new SgCalificacionEstudiante();
                            calEstNotaInst.setCaeCalificacionConceptualEstudiante(calEst.getCaeCalificacionConceptualEstudiante());
                            if (calEst.getCaeCalificacionNumericaEstudiante() != null) {
                                calEstNotaInst.setCaeCalificacionNumericaEstudiante(NumberFormatUtils.formatDouble(Double.parseDouble(calEst.getCaeCalificacionNumericaEstudiante()), precision));
                            }
                            calEstNotaInst.setCaeEstudiante(calEst.getCaeEstudiante());
                            calEstNotaInst.setCaeFechaRealizado(calEst.getCaeFechaRealizado());
                            calEstNotaInst.setCaeCalificacion(calNotaIns);
                            calNotaIns.getCalCalificacionesEstudiante().add(calEstNotaInst);
                        }
                    }

                }

                if (calApr.getCalPk() == null) {
                    List<SgCalificacionEstudiante> calEstAprList = new ArrayList();
                    for (SgCalificacionEstudiante calEst : calificacion.getCalCalificacionesEstudiante()) {
                        SgCalificacionEstudiante calEstApr = new SgCalificacionEstudiante();
                        calEstApr.setCaeCalificacionConceptualEstudiante(calEst.getCaeCalificacionConceptualEstudiante());
                        if (calEst.getCaeCalificacionNumericaEstudiante() != null) {
                            calEstApr.setCaeCalificacionNumericaEstudiante(NumberFormatUtils.formatDouble(Double.parseDouble(calEst.getCaeCalificacionNumericaEstudiante()), precision));
                        }
                        calEstApr.setCaeEstudiante(calEst.getCaeEstudiante());
                        calEstApr.setCaeFechaRealizado(calEst.getCaeFechaRealizado());
                        calEstApr.setCaeCalificacion(calApr);
                        calEstApr.setCaeResolucion(resolucionDeCalificacion(calEst, componentePlanGrado));
                        calEstAprList.add(calEstApr);
                    }
                    calApr.getCalCalificacionesEstudiante().addAll(calEstAprList);

                } else {

                    for (SgCalificacionEstudiante calEst : calApr.getCalCalificacionesEstudiante()) {
                        List<SgCalificacionEstudiante> calEstudianteNueva = calificacion.getCalCalificacionesEstudiante().stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());
                        if (!calEstudianteNueva.isEmpty()) {
                            calEst.setCaeCalificacionConceptualEstudiante(calEstudianteNueva.get(0).getCaeCalificacionConceptualEstudiante());
                            if (calEstudianteNueva.get(0) != null) {
                                calEst.setCaeCalificacionNumericaEstudiante(NumberFormatUtils.formatDouble(Double.parseDouble(calEstudianteNueva.get(0).getCaeCalificacionNumericaEstudiante()), precision));
                            }
                            calEst.setCaeResolucion(resolucionDeCalificacion(calEst, componentePlanGrado));
                        }
                    }

                    for (SgCalificacionEstudiante calEst : calificacion.getCalCalificacionesEstudiante()) {
                        List<SgCalificacionEstudiante> calEstudianteNueva = calApr.getCalCalificacionesEstudiante().stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());
                        if (calEstudianteNueva.isEmpty()) {
                            SgCalificacionEstudiante calEstApr = new SgCalificacionEstudiante();
                            calEstApr.setCaeCalificacionConceptualEstudiante(calEst.getCaeCalificacionConceptualEstudiante());
                            if (calEst.getCaeCalificacionNumericaEstudiante() != null) {
                                calEstApr.setCaeCalificacionNumericaEstudiante(NumberFormatUtils.formatDouble(Double.parseDouble(calEst.getCaeCalificacionNumericaEstudiante()), precision));
                            }
                            calEstApr.setCaeEstudiante(calEst.getCaeEstudiante());
                            calEstApr.setCaeFechaRealizado(calEst.getCaeFechaRealizado());

                            calEstApr.setCaeResolucion(resolucionDeCalificacion(calEstApr, componentePlanGrado));
                            calEstApr.setCaeCalificacion(calApr);
                            calApr.getCalCalificacionesEstudiante().add(calEstApr);
                        }
                    }

                }

                this.guardar(calNotaIns, null, Boolean.FALSE);
                this.guardar(calApr, null, Boolean.FALSE);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public EnumResolucionCalificacion resolucionDeCalificacion(SgCalificacionEstudiante calEst, SgComponentePlanGrado componentePlanGrado) {
        EnumResolucionCalificacion enumRes = EnumResolucionCalificacion.PENDIENTE;
        if (!StringUtils.isBlank(calEst.getCaeCalificacionNumericaEstudiante())) {
            Double calific = Double.parseDouble(calEst.getCaeCalificacionNumericaEstudiante());
            if (componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimo() != null && calific != null) {
                Integer resultado = calific.intValue();
                if (resultado >= componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion()) {
                    enumRes = EnumResolucionCalificacion.APROBADO;
                } else {
                    enumRes = EnumResolucionCalificacion.NO_APROBADO;
                }

            }
        } else {
            if (componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo() != null && componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo().getCalOrden() != null) {
                if (calEst.getCaeCalificacionConceptualEstudiante().getCalOrden() >= componentePlanGrado.getCpgEscalaCalificacion().getEcaValorMinimo().getCalOrden()) {
                    enumRes = EnumResolucionCalificacion.APROBADO;
                } else {
                    enumRes = EnumResolucionCalificacion.NO_APROBADO;
                }
            }
        }
        return enumRes;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCalificacion filtro) throws GeneralException {
        try {
            CalificacionDAO codDAO = new CalificacionDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACIONES);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalificacion>
     * @throws GeneralException
     */
    public SgPeriodoCalificacionEstCal buscarPeriodoEstudiantesCalificaciones(FiltroPeriodoCalificacionEstCal filtro) throws GeneralException {

        try {

            BusinessException be = new BusinessException();

            if (filtro.getAnioLectivoPk() == null) {
                be.addError(Errores.ERROR_ANIO_VACIO);
                throw be;
            }

            if (filtro.getSeccionPk() == null) {
                be.addError(Errores.ERROR_SECCION_VACIO);
                throw be;
            }

            if (filtro.getComponentePlanEstudioPk() == null) {
                be.addError(Errores.ERROR_COMPONENTE_PLAN_ESTUDIO_VACIO);
                throw be;
            }

            SgPeriodoCalificacionEstCal ret = new SgPeriodoCalificacionEstCal();
            FiltroMatricula filtroMat = new FiltroMatricula();
            filtroMat.setSecPk(filtro.getSeccionPk());
            filtroMat.setMatRetirada(Boolean.FALSE);
            filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
                "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
                "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
                "matEstudiante.estVersion"});
            filtroMat.setOrderBy(new String[]{"matEstudiante.estPersona.perPrimerApellidoBusqueda", "matEstudiante.estPersona.perSegundoApellidoBusqueda",
                "matEstudiante.estPersona.perPrimerNombreBusqueda"});
            filtroMat.setAscending(new boolean[]{true, true, true});
            List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(filtroMat);
            List<SgEstudiante> estudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());
            ret.setPecEstudiantes(estudiantes);

            if (!estudiantes.isEmpty()) {

                FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
                filtroCalEst.setIncluirCampos(new String[]{
                    "caeCalificacion.calPk",
                    "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                    "caeCalificacion.calComponentePlanEstudio.cpePk",
                    "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                    "caeCalificacion.calComponentePlanEstudio.cpeVersion",
                    "caeCalificacion.calTipoCalendarioEscolar",
                    "caeCalificacion.calTipoPeriodoCalificacion",
                    "caeCalificacion.calNumero",
                    "caeCalificacion.calSeccion.secPk",
                    "caeCalificacion.calSeccion.secVersion",
                    "caeCalificacion.calFecha",
                    "caeCalificacion.calUltModFecha",
                    "caeCalificacion.calUltModUsuario",
                    "caeCalificacion.calVersion",
                    "caeCalificacion.calCerrado",
                    "caeCalificacion.calRangoFecha.rfePk",
                    "caeCalificacion.calRangoFecha.rfeFechaHasta",
                    "caeCalificacion.calRangoFecha.rfeCodigo",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                    "caeCalificacion.calRangoFecha.rfeVersion",
                    "caeCalificacionNumericaEstudiante",
                    "caeCalificacionConceptualEstudiante.calPk",
                    "caeCalificacionConceptualEstudiante.calCodigo",
                    "caeCalificacionConceptualEstudiante.calValor",
                    "caeCalificacionConceptualEstudiante.calOrden",
                    "caeCalificacionConceptualEstudiante.calVersion",
                    "caeVersion",
                    "caeResolucion",
                    "caeEstudiante.estPk",
                    "caeEstudiante.estVersion"
                });
                filtroCalEst.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                    EnumTiposPeriodosCalificaciones.EXORD,
                    EnumTiposPeriodosCalificaciones.ORD,
                    EnumTiposPeriodosCalificaciones.NOTIN,
                    EnumTiposPeriodosCalificaciones.APR}));
                filtroCalEst.setCaeEstudiantesPk(estudiantes.stream().map(e -> e.getEstPk()).collect(Collectors.toList()));
                //FIXME: Se agrega filtro por grado para secciones semestrales, esto no contempla el caso de repetición
                filtroCalEst.setCaeGradoFk(filtro.getGradoPk());
                filtroCalEst.setAnioLectivoPk(filtro.getAnioLectivoPk());
                filtroCalEst.setCaeComponentePlanEstudio(filtro.getComponentePlanEstudioPk());

                //Se agrega filtro para determinar si son calificaciones de secciones semestrales o anuales
                filtroCalEst.setCaeTipoPeriodo(filtro.getTipoPeriodo());
                filtroCalEst.setCaeNumeroPeriodo(filtro.getNumeroPeriodo());

                List<SgCalificacionEstudiante> cals = calificacionEstudianteBean.obtenerPorFiltro(filtroCalEst);

                if (filtro.getPeriodoCalificacionPk() != null) {
                    Iterator<SgCalificacionEstudiante> it = cals.iterator();
                    while (it.hasNext()) {
                        SgCalificacionEstudiante cal = it.next();
                        if (cal.getCaeCalificacion().getCalRangoFecha() != null
                                && !cal.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion().getPcaPk().equals(filtro.getPeriodoCalificacionPk())) {
                            it.remove();
                        }
                    }
                }

                ret.setPecCalificaciones(cals);

                if (filtro.getPeriodoCalificacionPk() != null) {
                    FiltroRangoFecha fre = new FiltroRangoFecha();
                    fre.setPeriodoCalificacionPk(filtro.getPeriodoCalificacionPk());
                    fre.setIncluirCampos(new String[]{
                        "rfeCodigo",
                        "rfeFechaDesde",
                        "rfeFechaDesde",
                        "rfeFechaHasta",
                        "rfeHabilitado",
                        "rfeVersion"});
                    fre.setOrderBy(new String[]{"rfeFechaDesde"});
                    fre.setAscending(new boolean[]{true});
                    ret.setPecPeriodoRangosFecha(rangoFechaBean.obtenerPorFiltroConCache(fre));
                }

            }

            return ret;

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalificacion>
     * @throws GeneralException
     */
    public List<SgCalificacionCE> obtenerPorFiltro(FiltroCalificacion filtro) throws GeneralException {
        try {
            CalificacionDAO codDAO = new CalificacionDAO(em, seguridadBean);
            List<SgCalificacionCE> list = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CALIFICACIONES);

            if (filtro.getIncluirCampos() == null && BooleanUtils.isTrue(filtro.getInicializarLazyToOne())) {
                for (SgCalificacionCE cal : list) {
                    if (cal.getCalSeccion() != null) {
                        cal.getCalSeccion().getSecPk();
                    }
                    if (cal.getCalRangoFecha() != null) {
                        cal.getCalRangoFecha().getRfePk();
                    }
                    if (cal.getCalComponentePlanEstudio() != null) {
                        cal.getCalComponentePlanEstudio().getCpePk();
                    }
                }
            }

            if (BooleanUtils.isTrue(filtro.getIncluirCalificacionesEstudiantes())) {
                for (SgCalificacionCE cal : list) {
                    em.detach(cal);
                    FiltroCalificacionEstudiante filCalEs = new FiltroCalificacionEstudiante();
                    filCalEs.setCabezalPk(cal.getCalPk());

                    filCalEs.setIncluirCampos(new String[]{
                        "caeCalificacion.calPk",
                        "caeCalificacion.calVersion",
                        "caeResolucion",
                        "caeEstudiante.estPk",
                        "caeEstudiante.estPersona.perNie",
                        "caeEstudiante.estPersona.perPrimerNombre", "caeEstudiante.estPersona.perSegundoNombre", "caeEstudiante.estPersona.perNombreBusqueda",
                        "caeEstudiante.estPersona.perPrimerApellido", "caeEstudiante.estPersona.perSegundoApellido", "caeEstudiante.estPersona.perFechaNacimiento",
                        "caeEstudiante.estVersion",
                        "caePromovidoCalificacion",
                        "caeCalificacionNumericaEstudiante",
                        "caeCalificacionConceptualEstudiante.calPk",
                        "caeCalificacionConceptualEstudiante.calValor",
                        "caeCalificacionConceptualEstudiante.calVersion",
                        "caeCalificacionConceptualEstudiante.calOrden",
                        "caeObservacion",
                        "caeProcesoDeCreacion",
                        "caeInfoProcesamientoCalificacionEstFk.ipePk",
                        "caeInfoProcesamientoCalificacionEstFk.ipeError",
                        "caeInfoProcesamientoCalificacionEstFk.ipeVersion",
                        "caeVersion"});
                    filCalEs.setOrderBy(new String[]{"caeEstudiante.estPersona.perNombreBusqueda"});
                    filCalEs.setAscending(new boolean[]{true});
                    cal.setCalCalificacionesEstudiante(calificacionEstudianteBean.obtenerPorFiltro(filCalEs));
                }
            }

            if (BooleanUtils.isTrue(filtro.getCalTieneEstudiantePendiente())) {
                if (!list.isEmpty()) {
                    FiltroCalificacionEstudiante filCalEs = new FiltroCalificacionEstudiante();
                    filCalEs.setCaePromovido(EnumPromovidoCalificacion.PENDIENTE);
                    filCalEs.setCalficacionesPk(list.stream().map(c -> c.getCalPk()).collect(Collectors.toList()));
                    filCalEs.setIncluirCampos(new String[]{"caeVersion", "caeCalificacion.calPk"});
                    List<SgCalificacionEstudiante> calestList = calificacionEstudianteBean.obtenerPorFiltro(filCalEs);
                    for (SgCalificacionCE cal : list) {
                        List<SgCalificacionEstudiante> calE = calestList.stream().filter(c -> c.getCaeCalificacion().getCalPk().equals(cal.getCalPk())).collect(Collectors.toList());
                        if (calE.isEmpty()) {
                            cal.setCalTieneEstudiantesPendientes(Boolean.FALSE);
                        } else {
                            cal.setCalTieneEstudiantesPendientes(Boolean.TRUE);
                        }
                    }

                }
            }
            return list;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public SgCalificacionCELite guardarCabezal(SgCalificacionCELite cal) throws GeneralException, Exception {
        try {
            CodigueraDAO<SgCalificacionCELite> codDAO = new CodigueraDAO<>(em, SgCalificacionCELite.class);
            return codDAO.guardar(cal, null);

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CALIFICACIONES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    //IMPORTACIÓN DE CALIFICACIONES
    //-----------------------------
    //Existen dos métodos para importar calificaciones
    //Cualquiera de los dos se llama desde calificarEstudiantesConArchivo
    //calificarEstudiantesConArchivoUnSoloComponente califica la lista de estudiantes en un único componente
    //calificarEstudiantesConArchivoMuchosComponentes califica la lista de estudiantes en más de un componente
    //El sistema utiliza un único método a la vez, depende de una configuración IMPORTACION_CALIFICACION_SELECCION_METODO (0-calificarEstudiantesConArchivoUnSoloComponente,1-calificarEstudiantesConArchivoMuchosComponentes)
    
    /**
     * Guarda el objeto indicado
     *
     * @param archCal
     * @throws GeneralException
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionCE calificarEstudiantesConArchivo(SgArchivoCalificaciones archCal) throws GeneralException, FileNotFoundException, IOException, InvalidFormatException {
        
        if(archCal.getAccPk()!=null && !StringUtils.isBlank(archCal.getAccMetodoImportacion())){
            if(archCal.getAccMetodoImportacion().equals("0")){
                return calificarEstudiantesConArchivoUnSoloComponente(archCal);
            }
            if(archCal.getAccMetodoImportacion().equals("1")){
                return calificarEstudiantesConArchivoMuchosComponentes(archCal);
            }
        }
        
        FiltroCodiguera fcodiguera = new FiltroCodiguera();
        fcodiguera.setCodigo(Constantes.IMPORTACION_CALIFICACION_VARIOS_COMPONENTES);
        List<SgConfiguracion> conf = configuracionBean.obtenerPorFiltro(fcodiguera);
        
        if (!conf.isEmpty() && conf.get(0).getConValor().equals("1")) {
            return calificarEstudiantesConArchivoMuchosComponentes(archCal);            
        }else{
            return calificarEstudiantesConArchivoUnSoloComponente(archCal);
        }
  
        
        
    }
    /**
     * Guarda el objeto indicado
     *
     * @param archCal
     * @throws GeneralException
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionCE calificarEstudiantesConArchivoMuchosComponentes(SgArchivoCalificaciones archCal) throws GeneralException, FileNotFoundException, IOException, InvalidFormatException {
        InputStream stream = null;
        Workbook myWorkBook = null;
        try {
            BusinessException ge = new BusinessException();
            ge.addError("estNie", "Se encontraron los siguientes errores en el archivo:");
            String errores = "";
            List<Long> listaNie = new ArrayList();
            if (archCal != null) {
                String respuesta = "";
                SgCalificacionCE calReturn = null;

                String path = (EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado()) || (EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado()))) ? tmpBasePath : basePath;

                if (archCal.getAchPk() != null) {
                    stream = new BufferedInputStream(new FileInputStream(path + SofisFileUtils.getPathFromPk(archCal.getAccArchivo().getAchPk())));
                } else {
                    stream = new BufferedInputStream(new FileInputStream(path + archCal.getAccArchivo().getAchTmpPath()));
                }

                myWorkBook = WorkbookFactory.create(stream);
                Sheet mySheet = myWorkBook.getSheetAt(0);

                //VALIDAR CANT FILAS
                FiltroCodiguera fcodiguera = new FiltroCodiguera();
                fcodiguera.setCodigoExacto(Constantes.CANT_FILAS_ARCHIVO_IMPORT);
                List<SgConfiguracion> conf = configuracionBean.obtenerPorFiltro(fcodiguera);
                Integer filasCantMax = 500;
                if (!conf.isEmpty()) {
                    filasCantMax = Integer.parseInt(conf.get(0).getConValor());
                }

                Iterator<Row> rowIterator1 = mySheet.iterator();
                Integer cantRows = 0;
                while (rowIterator1.hasNext()) {
                    Row row = rowIterator1.next();
                    if (row.getCell(0, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(1, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(3, Row.RETURN_BLANK_AS_NULL) == null) {
                        continue;
                    }
                    cantRows++;
                }
                if (cantRows <= 1) {
                    ge.addError("filas", "El archivo está vacío.");
                    throw ge;
                }

                if (cantRows > filasCantMax) {
                    ge.addError("filas", "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".");
                    throw ge;
                }
                //FIN VALIDAR CANT FILAS

                //VALIDACIÓN COLUMNAS
                Iterator<Row> rowIterator2 = mySheet.iterator();
                List<String> componentes = new ArrayList();
                List<String> componentesRepetidos = new ArrayList();
                HashMap<Integer, SgComponenteArchivoInfo> filaComponente = new HashMap<>();
                if (rowIterator2.hasNext()) {
                    //Me paro en la fila q tiene los nombres de los componentes
                    Row rowComponentes = rowIterator2.next();
                    Iterator<Cell> cellIterator = rowComponentes.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell != null && cell.getColumnIndex() != 0 && !StringUtils.isBlank(cell.getStringCellValue())) {
                            String nombreNormalizado = SofisStringUtils.normalizarParaBusqueda(cell.getStringCellValue());
                            if (componentes.contains(nombreNormalizado)) {
                                componentesRepetidos.add(cell.getStringCellValue());
                            } else {
                                componentes.add(nombreNormalizado);
                                SgComponenteArchivoInfo compInf = new SgComponenteArchivoInfo();
                                compInf.setNombreNormalizadoComponente(nombreNormalizado);
                                compInf.setNombreComponente(cell.getStringCellValue());
                                if (!filaComponente.containsKey(cell.getColumnIndex())) {
                                    filaComponente.put(cell.getColumnIndex(), compInf);
                                }
                            }
                        }
                    }
                }

                if (!componentesRepetidos.isEmpty()) {
                    List<String> cpgDistinct = componentesRepetidos.stream().distinct().collect(Collectors.toList());
                    for (String componenteRepetido : cpgDistinct) {
                        ge.addError("filas", "El componente " + componenteRepetido + " aparece más de una vez en la lista.");
                    }
                    throw ge;
                }
                if (filaComponente.values().isEmpty()) {
                    ge.addError("filas", "La lista de componentes es vacía.");
                    throw ge;
                }

                Iterator<Row> rowIterator = mySheet.iterator();

                

                FiltroSeccion fsec = new FiltroSeccion();
                fsec.setSecPk(archCal.getAccSeccionPk());
                fsec.setIncluirCampos(new String[]{"secPk", "secVersion", "secServicioEducativo.sduGrado.graPk", "secPlanEstudio.pesPk", "secTipoPeriodo", "secNumeroPeriodo"});
                List<SgSeccion> listSeccion = seccionBean.obtenerPorFiltro(fsec);
                if (listSeccion.isEmpty()) {
                    respuesta = "No existe la sección.";
                    errores = errores + " - " + respuesta;
                    if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado()) && !EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado())) {
                        archCal.setAccEstado(EnumEstadoImportado.PROCESADO_ERROR);
                        archCal.setAccDescripcion(errores);
                        archivoCalificacionesBean.guardar(archCal);
                        return new SgCalificacionCE();
                    } else {
                        ge.addError("estNie", respuesta);
                        throw ge;
                    }

                }
                SgSeccion seccion = listSeccion.get(0);

                FiltroRelGradoPlanEstudio filtro = new FiltroRelGradoPlanEstudio();
                filtro.setRgpGrado(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
                filtro.setRgpPlanEstudio(seccion.getSecPlanEstudio().getPesPk());
                filtro.setIncluirCampos(new String[]{"rgpPermiteCalificarSinMatValidada",
                    "rgpPermiteCalificarConMatProvisional",
                    "rgpRequiereValidacionAcademica"});
                List<SgRelGradoPlanEstudio> list = relGradoPlanEstudioBean.obtenerPorFiltro(filtro);
                Boolean calificarSinMatriculaValidada = Boolean.TRUE;
                Boolean calificarConMatriculaProvisional = Boolean.TRUE;
                if (!list.isEmpty()) {
                    SgRelGradoPlanEstudio relGradoPlanEstudio = list.get(0);
                    if (BooleanUtils.isTrue(relGradoPlanEstudio.getRgpRequiereValidacionAcademica()) && BooleanUtils.isFalse(relGradoPlanEstudio.getRgpPermiteCalificarSinMatValidada())) {
                        calificarSinMatriculaValidada = Boolean.FALSE;
                    }
                    calificarConMatriculaProvisional = relGradoPlanEstudio.getRgpPermiteCalificarConMatProvisional();
                }

                FiltroMatricula fmat = new FiltroMatricula();
                fmat.setSecPk(seccion.getSecPk());
                fmat.setMatEstado(EnumMatriculaEstado.ABIERTO);
                fmat.setMatFechaEntreIngresoHasta(LocalDate.now());
                fmat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
                    "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
                    "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
                    "matEstudiante.estVersion", "matValidacionAcademica", "matProvisional"});
                List<SgMatricula> listmat = matriculaBean.obtenerPorFiltro(fmat);
                
                if (EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado()) || EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado())) {

                    JsonWebToken jwt = Lookup.obtenerJWT();
                    if (!jwt.getGroups().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)
                            && !horarioEscolarBean.usuarioDictaComponenteEnSeccion(
                                    jwt.getSubject(),
                                    seccion.getSecPk(),
                                    archCal.getAccComponentePlanEstudio().getCpePk())) {
                        respuesta = "Usuario no tiene horarios escolares asignados en la sección para el componente seleccionado.";
                        errores = errores + " - " + respuesta;
                        ge.addError("estNie", respuesta);     
                        throw ge;
                    }

                }

                FiltroComponentePlanGrado fcp = new FiltroComponentePlanGrado();
                fcp.setCpgGradoPk(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
                fcp.setCpgPlanEstudioPk(seccion.getSecPlanEstudio().getPesPk());
                fcp.setCpgAgregandoSeccionExclusiva(seccion.getSecPk());
                fcp.setCpeNombreBusquedaList(filaComponente.values().stream().map(c -> c.getNombreNormalizadoComponente()).collect(Collectors.toList()));
                fcp.setIncluirCampos(new String[]{
                    "cpgPeriodosCalificacion",
                    "cpgEscalaCalificacion.ecaPk",
                    "cpgEscalaCalificacion.ecaCodigo",
                    "cpgEscalaCalificacion.ecaHabilitado",
                    "cpgEscalaCalificacion.ecaNombre",
                    "cpgEscalaCalificacion.ecaTipoEscala",
                    "cpgEscalaCalificacion.ecaMinimo",
                    "cpgEscalaCalificacion.ecaMaximo",
                    "cpgEscalaCalificacion.ecaMinimoAprobacion",
                    "cpgEscalaCalificacion.ecaPrecision",
                    "cpgEscalaCalificacion.ecaValorMinimo.calValor",
                    "cpgEscalaCalificacion.ecaValorMinimo.calOrden",
                    "cpgComponentePlanEstudio.cpePk",
                    "cpgComponentePlanEstudio.cpeVersion",
                    "cpgComponentePlanEstudio.cpeNombre",
                    "cpgComponentePlanEstudio.cpeNombreBusqueda",
                    "cpgComponentePlanEstudio.cpeCodigo",
                    "cpgComponentePlanEstudio.cpeTipo",
                    "cpgVersion",
                    "cpgPk",
                    "cpgEscalaCalificacion.ecaVersion"});

                List<SgComponentePlanGrado> componentesCalificar = componentePlanGradoBean.obtenerPorFiltro(fcp);

                if (componentesCalificar.isEmpty()) {
                    ge.addError("estNie", "No existen componentes para los datos ingresados.");
                    throw ge;
                }

                List<String> componentesNoExisten = new ArrayList();
                List<String> componentesNoPertenecenRango = new ArrayList();
                List<Integer> columnasEliminar = new ArrayList();
                for (Integer columns : filaComponente.keySet()) {
                    List<SgComponentePlanGrado> busquedaComponente = componentesCalificar.stream().filter(c -> c.getCpgComponentePlanEstudio().getCpeNombreBusqueda().equals(filaComponente.get(columns).getNombreNormalizadoComponente())).collect(Collectors.toList());
                    if (busquedaComponente.isEmpty()) {
                        componentesNoExisten.add(filaComponente.get(columns).getNombreComponente());
                        columnasEliminar.add(columns);
                    } else {
                        SgComponentePlanGrado cpg = busquedaComponente.get(0);
                        if (!cpg.getCpgPeriodosCalificacion().equals(archCal.getAccRangoFecha().getRfePeriodoCalificacion().getPcaNumero())) {
                            componentesNoPertenecenRango.add(filaComponente.get(columns).getNombreComponente());
                            columnasEliminar.add(columns);
                        } else {
                            filaComponente.get(columns).setComponentePlanGrado(cpg);
                        }
                    }
                }

                if (!columnasEliminar.isEmpty()) {
                    for (Integer col : columnasEliminar) {
                        filaComponente.remove(col);
                    }
                }
                Boolean seEncontroError = Boolean.FALSE;
                if (!componentesNoExisten.isEmpty()) {
                    for (String componenteRepetido : componentesNoExisten) {
                        respuesta = "El componente " + componenteRepetido + " no pertenece a la sección.";
                        errores = errores + " - " + respuesta;
                        ge.addError("estNie", respuesta);
                    }
                    seEncontroError = Boolean.TRUE;
                }

                if (!componentesNoPertenecenRango.isEmpty()) {
                    for (String componenteRepetido : componentesNoPertenecenRango) {
                        respuesta = "El componente " + componenteRepetido + " no pertenece al período de calificación seleccionado.";
                        errores = errores + " - " + respuesta;
                        ge.addError("estNie", respuesta);
                    }
                    seEncontroError = Boolean.TRUE;
                }
           /*     if (seEncontroError) {
                    throw ge;
                }*/

                HashMap<Integer, SgEstudiante> filaEstudiante = new HashMap<>();
                //Validación estudiantes
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Integer fila = row.getRowNum() + 1;
                    Cell cell;

                    if (row.getRowNum() > 0) {
                        //Se extraen los demas datos
                        String[] datos = new String[6];

                        //Extraemos el valor de NIE
                        cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            Iterator<Cell> cellIterator = row.cellIterator();
                            Boolean existeElementoDistintoVacio = Boolean.FALSE;

                            iterCeldas:
                            while (cellIterator.hasNext()) {
                                Cell cellEst = cellIterator.next();
                                if (cellEst != null) {
                                    existeElementoDistintoVacio = Boolean.TRUE;
                                    break iterCeldas;
                                }
                            }
                            if (existeElementoDistintoVacio) {
                                respuesta = "Fila " + fila + ": NIE vacío.";
                                errores = errores + " - " + respuesta;
                                ge.addError("estNie", respuesta);
                            }
                            continue;
                        }
                        if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                            respuesta = "Fila " + fila + ": NIE incorrecto.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }

                        datos[0] = String.valueOf(Math.round(cell.getNumericCellValue()));

                        if (listaNie.contains(Long.parseLong(datos[0]))) {
                            respuesta = "Fila " + fila + ": NIE aparece más de una vez.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        listaNie.add(Long.parseLong(datos[0]));

                        List<SgMatricula> listEst = new ArrayList();
                        for (SgMatricula m : listmat) {
                            if (m.getMatEstudiante().getEstPersona().getPerNie() != null) {
                                if (m.getMatEstudiante().getEstPersona().getPerNie().equals(Long.parseLong(datos[0]))) {
                                    listEst.add(m);
                                }
                            }
                        }
                        if (listEst.isEmpty()) {
                            respuesta = "Fila " + fila + ": NIE incorrecto.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        SgMatricula mat = listEst.get(0);

                        if (!calificarSinMatriculaValidada && BooleanUtils.isNotTrue(mat.getMatValidacionAcademica())) {
                            respuesta = "Fila " + fila + ": No se permite calificar matrículas sin validar.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }

                        if (!calificarConMatriculaProvisional && BooleanUtils.isTrue(mat.getMatProvisional())) {
                            respuesta = "Fila " + fila + ": No se permite calificar matrículas provisionales.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }

                        SgEstudiante est = mat.getMatEstudiante();

                        if (!filaEstudiante.containsKey(row.getRowNum())) {
                            filaEstudiante.put(row.getRowNum(), est);
                        }
                    }
                }

                for (Integer colIndex : filaComponente.keySet()) {
                    filaComponente.get(colIndex).setCalificacionEstudianteList(new ArrayList());

                    if (EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado())) {

                        JsonWebToken jwt = Lookup.obtenerJWT();
                        if (!jwt.getGroups().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)
                                && !horarioEscolarBean.usuarioDictaComponenteEnSeccion(
                                        jwt.getSubject(),
                                        seccion.getSecPk(),
                                        filaComponente.get(colIndex).getComponentePlanGrado().getCpgComponentePlanEstudio().getCpePk())) {
                            respuesta = "Usuario no tiene horarios escolares asignados en la sección para el componente " + filaComponente.get(colIndex).getComponentePlanGrado().getCpgComponentePlanEstudio().getCpeNombre() + ".";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }

                    }

                    iterEstudiante:
                    for (Integer rowIndex : filaEstudiante.keySet()) {

                        SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();
                        calEst.setCaeEstudiante(filaEstudiante.get(rowIndex));

                        Row rowEst = mySheet.getRow(rowIndex);

                        Cell cell = rowEst.getCell(colIndex, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            respuesta = "Fila " + (rowIndex + 1) + " y columna " + (colIndex + 1) + ": Calificación vacía.";
                            errores = errores + " - " + respuesta;
                            ge.addError("cal", respuesta);
                            continue iterEstudiante;
                        }
                        String datoCalificacion = cell.toString();

                        SgCalificacion calificacionConceptual = new SgCalificacion();
                        if (TipoEscalaCalificacion.NUMERICA.equals(filaComponente.get(colIndex).getComponentePlanGrado().getCpgEscalaCalificacion().getEcaTipoEscala())) {
                            String calificacion = null;
                            try {
                                Double num = Double.parseDouble(datoCalificacion);

                                if ((filaComponente.get(colIndex).getComponentePlanGrado().getCpgEscalaCalificacion().getEcaMaximo() < num) || (filaComponente.get(colIndex).getComponentePlanGrado().getCpgEscalaCalificacion().getEcaMinimo() > num)) {
                                    respuesta = "Fila " + (rowIndex + 1) + " y columna " + (colIndex + 1) + ": La calificación no corresponde al rango de la escala.";
                                    errores = errores + " - " + respuesta;
                                    ge.addError("cal", respuesta);
                                }
                                Integer precision = filaComponente.get(colIndex).getComponentePlanGrado().getCpgEscalaCalificacion().getEcaPrecision();
                                DecimalFormat df = new DecimalFormat("#");
                                if (precision != null && precision.equals(1)) {
                                    df = new DecimalFormat("#.0");
                                }
                                if (precision != null && precision.equals(2)) {
                                    df = new DecimalFormat("#.00");
                                }

                                calificacion = df.format(num);

                            } catch (NumberFormatException e) {
                                respuesta = "Fila " + (rowIndex + 1) + " y columna " + (colIndex + 1) + ":  La calificación no corresponde con escala.";
                                errores = errores + " - " + respuesta;
                                ge.addError("cal", respuesta);
                            }
                            calEst.setCaeCalificacionNumericaEstudiante(calificacion);
                        } else {

                            FiltroCalificacionCa fc = new FiltroCalificacionCa();
                            fc.setCalEscalaCalificacionPk(filaComponente.get(colIndex).getComponentePlanGrado().getCpgEscalaCalificacion().getEcaPk());
                            fc.setCalValor(datoCalificacion);

                            List<SgCalificacion> calList = calificacionCatalogosBean.obtenerPorFiltro(fc); //Tiene cache
                            if (calList.isEmpty()) {
                                respuesta = "Fila " + (rowIndex + 1) + " y columna " + (colIndex + 1) + ": La calificación no corresponde al rango de la escala.";
                                errores = errores + " - " + respuesta;
                                ge.addError("cal", respuesta);
                                continue;
                            }
                            calificacionConceptual = calList.get(0);
                            calEst.setCaeCalificacionConceptualEstudiante(calificacionConceptual);
                        }

                        calEst.setCaeFechaRealizado(archCal.getAccFechaCalificado() != null ? archCal.getAccFechaCalificado() : null);
                        calEst.setCaeProcesoDeCreacion(EnumProcesoCreacion.IMP);
                        /*    cell = row.getCell(3, Row.RETURN_BLANK_AS_NULL);
                        datos[3] = cell != null ? cell.getStringCellValue() : null;
                        calEst.setCaeObservacion(datos[3]);*/

                        filaComponente.get(colIndex).getCalificacionEstudianteList().add(calEst);
                    }
                }

                if (StringUtils.isBlank(respuesta)) {
                    FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                    fce.setComponentePlanEstudioPk(filaComponente.values().stream().map(c -> c.getComponentePlanGrado().getCpgComponentePlanEstudio().getCpePk()).collect(Collectors.toList()));
                    fce.setCaeRangoFechaPk(archCal.getAccRangoFecha().getRfePk());
                    fce.setCaeTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                    List<Long> listEstudiantePk = filaEstudiante.values().stream().map(c -> c.getEstPk()).collect(Collectors.toList());
                    fce.setCaeEstudiantesPk(listEstudiantePk);
                    fce.setDescartandoSeccion(archCal.getAccSeccionPk());
                    //FIXME: Se agrego filtro por grado para secciones semestrales, pero esto no abarca el caso de repetidores en semestre, hay que ver mejor arreglo
                    fce.setCaeGradoFk(seccion.getSecServicioEducativo().getSduGrado().getGraPk());

                    //Filtra dependiendo si sección es anual o semestral
                    fce.setCaeTipoPeriodo(seccion.getSecTipoPeriodo());
                    fce.setCaeNumeroPeriodo(seccion.getSecNumeroPeriodo());

                    fce.setMaxResults(5L);
                    fce.setIncluirCampos(new String[]{"caeCalificacion.calSeccion.secPk", "caeEstudiante.estPk",
                        "caeCalificacion.calComponentePlanEstudio.cpeNombre", "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                        "caeEstudiante.estPersona.perNie"});
                    List<SgCalificacionEstudiante> listCalEs = calificacionEstudianteBean.obtenerPorFiltro(fce);
                    for (SgCalificacionEstudiante cal : listCalEs) {
                        if (!cal.getCaeCalificacion().getCalSeccion().getSecPk().equals(archCal.getAccSeccionPk())) {
                            respuesta = "Estudiante con NIE " + cal.getCaeEstudiante().getEstPersona().getPerNie() + " ya tiene una calificación ingresada para el componente " + cal.getCaeCalificacion().getCalComponentePlanEstudio().getCpeNombre() + " período seleccionado en otra sección.";
                            errores = errores + " - " + respuesta;
                            ge.addError("fecha", respuesta);
                        }
                    }
                }

                if (StringUtils.isBlank(respuesta)) {
                    if (!(EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado()))) {

                        guardarCalificacion:
                        for (Integer colIndex : filaComponente.keySet()) {
                            List<SgCalificacionEstudiante> listCalEst = new ArrayList();

                            if (filaComponente.get(colIndex).getCalificacionEstudianteList().isEmpty()) {
                                continue guardarCalificacion;
                            }
                            listCalEst = filaComponente.get(colIndex).getCalificacionEstudianteList();

                            FiltroCalificacion fca = new FiltroCalificacion();
                            fca.setSecPk(archCal.getAccSeccionPk());
                            fca.setCalComponentePlanEstudio(filaComponente.get(colIndex).getComponentePlanGrado().getCpgComponentePlanEstudio().getCpePk());
                            fca.setCalRangoFecha(archCal.getAccRangoFecha().getRfePk());
                            fca.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                            fca.setIncluirCalificacionesEstudiantes(Boolean.TRUE);

                            //TODO: incluirCampos
                            List<SgCalificacionCE> calOriginalList = this.obtenerPorFiltro(fca);

                            if (calOriginalList.isEmpty()) {

                                SgCalificacionCE calCE = new SgCalificacionCE();
                                calCE.setCalComponentePlanEstudio(filaComponente.get(colIndex).getComponentePlanGrado().getCpgComponentePlanEstudio());
                                calCE.setCalSeccion(seccion);
                                calCE.setCalRangoFecha(archCal.getAccRangoFecha());
                                calCE.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                                calCE.setCalFecha(LocalDate.now());

                                listCalEst.stream().forEach(c -> c.setCaeCalificacion(calCE));

                                calCE.setCalCalificacionesEstudiante(listCalEst);
                                calReturn = calCE;

                                this.guardar(calReturn, Boolean.FALSE, Boolean.FALSE);
                            } else {

                                SgCalificacionCE calOriginal = calOriginalList.get(0);
                                List<SgCalificacionEstudiante> listCalFinal = new ArrayList();

                                for (SgCalificacionEstudiante calEst : calOriginal.getCalCalificacionesEstudiante()) {
                                    List<SgCalificacionEstudiante> calificacionExistente = listCalEst.stream().filter(elem -> elem.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());

                                    if (!calificacionExistente.isEmpty()) {
                                        calEst.setCaeCalificacionConceptualEstudiante(calificacionExistente.get(0).getCaeCalificacionConceptualEstudiante());
                                        calEst.setCaeCalificacionNumericaEstudiante(calificacionExistente.get(0).getCaeCalificacionNumericaEstudiante());
                                        calEst.setCaeFechaRealizado(calificacionExistente.get(0).getCaeFechaRealizado());
                                        calEst.setCaeObservacion(calificacionExistente.get(0).getCaeObservacion());
                                        calEst.setCaeProcesoDeCreacion(EnumProcesoCreacion.IMP);
                                    }
                                    listCalFinal.add(calEst);

                                }

                                for (SgCalificacionEstudiante calEst : listCalEst) {
                                    List<SgCalificacionEstudiante> calificacionExistente = listCalFinal.stream().filter(elem -> elem.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());
                                    if (calificacionExistente.isEmpty()) {
                                        calEst.setCaeCalificacion(calOriginal);
                                        calEst.setCaeProcesoDeCreacion(EnumProcesoCreacion.IMP);
                                        listCalFinal.add(calEst);
                                    }

                                }
                                calOriginal.setCalCalificacionesEstudiante(listCalFinal);

                                this.guardar(calOriginal, Boolean.FALSE, Boolean.FALSE);
                            }
                        }
                        if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado())) {
                            archCal.setAccEstado(EnumEstadoImportado.PROCESADO_EXITO);
                            archivoCalificacionesBean.guardar(archCal);
                        }
                        return new SgCalificacionCE();

                    }
                } else {
                    if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado()) && !EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado())) {
                        archCal.setAccEstado(EnumEstadoImportado.PROCESADO_ERROR);
                        archCal.setAccDescripcion(errores);

                        archivoCalificacionesBean.guardar(archCal);
                    } else {

                        if (ge.getErrores().size() > 1) {
                            throw ge;
                        }
                        return null;
                    }

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ex) {
                }
            }
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                }
            }
        }
        return new SgCalificacionCE();
    }
    
    
    //IMPORTACIÓN DE CALIFICACIONES
    //-----------------------------
    //Existen dos métodos para importar calificaciones
    //Cualquiera de los dos se llama desde calificarEstudiantesConArchivo
    //calificarEstudiantesConArchivoUnSoloComponente califica la lista de estudiantes en un único componente
    //calificarEstudiantesConArchivoMuchosComponentes califica la lista de estudiantes en más de un componente
    //El sistema utiliza un único método a la vez, depende de una configuración IMPORTACION_CALIFICACION_SELECCION_METODO (0-calificarEstudiantesConArchivoUnSoloComponente,1-calificarEstudiantesConArchivoMuchosComponentes)
    
    
     /**
     * Guarda el objeto indicado
     *
     * @param archCal
     * @throws GeneralException
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalificacionCE calificarEstudiantesConArchivoUnSoloComponente(SgArchivoCalificaciones archCal) throws GeneralException, FileNotFoundException, IOException, InvalidFormatException {
        InputStream stream = null;
        Workbook myWorkBook = null;
        try {
            BusinessException ge = new BusinessException();
            ge.addError("estNie", "Se encontraron los siguientes errores en el archivo:");
            String errores = "";
            List<Long> listaNie = new ArrayList();
            if (archCal != null) {
                SgCalificacionCE calReturn = null;

                String path = (EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado()) || (EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado()))) ? tmpBasePath : basePath;

                if (archCal.getAchPk() != null) {
                    stream = new BufferedInputStream(new FileInputStream(path + SofisFileUtils.getPathFromPk(archCal.getAccArchivo().getAchPk())));
                } else {
                    stream = new BufferedInputStream(new FileInputStream(path + archCal.getAccArchivo().getAchTmpPath()));
                }

                myWorkBook = WorkbookFactory.create(stream);
                Sheet mySheet = myWorkBook.getSheetAt(0);

                //VALIDAR CANT FILAS
                FiltroCodiguera fcodiguera = new FiltroCodiguera();
                fcodiguera.setCodigoExacto(Constantes.CANT_FILAS_ARCHIVO_IMPORT);
                List<SgConfiguracion> conf = configuracionBean.obtenerPorFiltro(fcodiguera);
                Integer filasCantMax = 500;
                if (!conf.isEmpty()) {
                    filasCantMax = Integer.parseInt(conf.get(0).getConValor());
                }

                Iterator<Row> rowIterator1 = mySheet.iterator();
                Integer cantRows = 0;
                while (rowIterator1.hasNext()) {
                    Row row = rowIterator1.next();
                    if (row.getCell(0, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(1, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null
                            && row.getCell(3, Row.RETURN_BLANK_AS_NULL) == null) {
                        continue;
                    }
                    cantRows++;
                }
                if (cantRows <= 1) {
                    ge.addError("filas", "El archivo está vacío.");
                    throw ge;
                }

                if (cantRows > filasCantMax) {
                    ge.addError("filas", "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".");
                    throw ge;
                }
                //FIN VALIDAR CANT FILAS

                Iterator<Row> rowIterator = mySheet.iterator();

                List<SgCalificacionEstudiante> listCalEst = new ArrayList();
                String respuesta = "";

                FiltroSeccion fsec = new FiltroSeccion();
                fsec.setSecPk(archCal.getAccSeccionPk());
                fsec.setIncluirCampos(new String[]{"secPk", "secVersion", "secServicioEducativo.sduGrado.graPk", "secPlanEstudio.pesPk", "secTipoPeriodo", "secNumeroPeriodo"});
                List<SgSeccion> listSeccion = seccionBean.obtenerPorFiltro(fsec);
                if (listSeccion.isEmpty()) {
                    respuesta = "No existe la sección.";
                    errores = errores + " - " + respuesta;
                    if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado()) && !EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado())) {
                        archCal.setAccEstado(EnumEstadoImportado.PROCESADO_ERROR);
                        archCal.setAccDescripcion(errores);
                        archivoCalificacionesBean.guardar(archCal);
                        return new SgCalificacionCE();
                    } else {
                        ge.addError("estNie", respuesta);
                        throw ge;
                    }

                }
                SgSeccion seccion = listSeccion.get(0);

                FiltroRelGradoPlanEstudio filtro = new FiltroRelGradoPlanEstudio();
                filtro.setRgpGrado(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
                filtro.setRgpPlanEstudio(seccion.getSecPlanEstudio().getPesPk());
                filtro.setIncluirCampos(new String[]{"rgpPermiteCalificarSinMatValidada",
                    "rgpPermiteCalificarConMatProvisional",
                    "rgpRequiereValidacionAcademica"});
                List<SgRelGradoPlanEstudio> list = relGradoPlanEstudioBean.obtenerPorFiltro(filtro);
                Boolean calificarSinMatriculaValidada = Boolean.TRUE;
                Boolean calificarConMatriculaProvisional = Boolean.TRUE;
                if (!list.isEmpty()) {
                    SgRelGradoPlanEstudio relGradoPlanEstudio = list.get(0);
                    if (BooleanUtils.isTrue(relGradoPlanEstudio.getRgpRequiereValidacionAcademica()) && BooleanUtils.isFalse(relGradoPlanEstudio.getRgpPermiteCalificarSinMatValidada())) {
                        calificarSinMatriculaValidada = Boolean.FALSE;
                    }
                    calificarConMatriculaProvisional = relGradoPlanEstudio.getRgpPermiteCalificarConMatProvisional();
                }

                FiltroMatricula fmat = new FiltroMatricula();
                fmat.setSecPk(seccion.getSecPk());
                fmat.setMatEstado(EnumMatriculaEstado.ABIERTO);
                fmat.setMatFechaEntreIngresoHasta(LocalDate.now());
                fmat.setIncluirCampos(new String[]{"matEstudiante.estPersona.perNie", "matEstudiante.estPk",
                    "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
                    "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
                    "matEstudiante.estVersion", "matValidacionAcademica", "matProvisional"});
                List<SgMatricula> listmat = matriculaBean.obtenerPorFiltro(fmat);
                
                if (EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado()) || EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado())) {

                    JsonWebToken jwt = Lookup.obtenerJWT();
                    if (!jwt.getGroups().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)
                            && !horarioEscolarBean.usuarioDictaComponenteEnSeccion(
                                    jwt.getSubject(),
                                    seccion.getSecPk(),
                                    archCal.getAccComponentePlanEstudio().getCpePk())) {
                        respuesta = "Usuario no tiene horarios escolares asignados en la sección para el componente seleccionado.";
                        errores = errores + " - " + respuesta;
                        ge.addError("estNie", respuesta);     
                        throw ge;
                    }

                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Integer fila = row.getRowNum() + 1;
                    Cell cell;

                    SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();

                    if (row.getRowNum() > 0) {
                        //Se extraen los demas datos
                        String[] datos = new String[6];

                        //Extraemos el valor de NIE
                        cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            if (row.getCell(1, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(2, Row.RETURN_BLANK_AS_NULL) == null
                                    && row.getCell(3, Row.RETURN_BLANK_AS_NULL) == null) {
                                continue;
                            }
                            respuesta = "fila " + fila + ": NIE vacío.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        if (!CellType.NUMERIC.equals(cell.getCellTypeEnum())) {
                            respuesta = "fila " + fila + ": NIE incorrecto.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }

                        datos[0] = String.valueOf(Math.round(cell.getNumericCellValue()));

                        if (listaNie.contains(Long.parseLong(datos[0]))) {
                            respuesta = "fila " + fila + ": NIE aparece más de una vez.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        listaNie.add(Long.parseLong(datos[0]));

                        List<SgMatricula> listEst = new ArrayList();
                        for (SgMatricula m : listmat) {
                            if (m.getMatEstudiante().getEstPersona().getPerNie() != null) {
                                if (m.getMatEstudiante().getEstPersona().getPerNie().equals(Long.parseLong(datos[0]))) {
                                    listEst.add(m);
                                }
                            }
                        }
                        if (listEst.isEmpty()) {
                            respuesta = "fila " + fila + ": NIE incorrecto.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }
                        SgMatricula mat = listEst.get(0);

                        if (!calificarSinMatriculaValidada && BooleanUtils.isNotTrue(mat.getMatValidacionAcademica())) {
                            respuesta = "fila " + fila + ": No se permite calificar matrículas sin validar.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }

                        if (!calificarConMatriculaProvisional && BooleanUtils.isTrue(mat.getMatProvisional())) {
                            respuesta = "fila " + fila + ": No se permite calificar matrículas provisionales.";
                            errores = errores + " - " + respuesta;
                            ge.addError("estNie", respuesta);
                            continue;
                        }

                        SgEstudiante est = mat.getMatEstudiante();
                        calEst.setCaeEstudiante(est);

                        //calificacion
                        cell = row.getCell(1, Row.RETURN_BLANK_AS_NULL);
                        if (cell == null) {
                            respuesta = "fila " + fila + ": Calificación vacía.";
                            errores = errores + " - " + respuesta;
                            ge.addError("cal", respuesta);
                            continue;
                        }
                        datos[1] = cell.toString();

                        SgCalificacion calificacionConceptual = new SgCalificacion();
                        if (TipoEscalaCalificacion.NUMERICA.equals(archCal.getAccEscalaCalificacion().getEcaTipoEscala())) {
                            String calificacion = null;
                            try {
                                Double num = Double.parseDouble(datos[1]);

                                if ((archCal.getAccEscalaCalificacion().getEcaMaximo() < num) || (archCal.getAccEscalaCalificacion().getEcaMinimo() > num)) {
                                    respuesta = "fila " + fila + ": La calificación no corresponde al rango de la escala.";
                                    errores = errores + " - " + respuesta;
                                    ge.addError("cal", respuesta);
                                }
                                Integer precision = archCal.getAccEscalaCalificacion().getEcaPrecision();
                                DecimalFormat df = new DecimalFormat("#");
                                if (precision != null && precision.equals(1)) {
                                    df = new DecimalFormat("#.0");
                                }
                                if (precision != null && precision.equals(2)) {
                                    df = new DecimalFormat("#.00");
                                }

                                calificacion = df.format(num);

                            } catch (NumberFormatException e) {
                                respuesta = "fila " + fila + ": La calificación no corresponde con escala.";
                                errores = errores + " - " + respuesta;
                                ge.addError("cal", respuesta);
                            }
                            calEst.setCaeCalificacionNumericaEstudiante(calificacion);
                        } else {

                            FiltroCalificacionCa fc = new FiltroCalificacionCa();
                            fc.setCalEscalaCalificacionPk(archCal.getAccEscalaCalificacion().getEcaPk());
                            fc.setCalValor(datos[1]);

                            List<SgCalificacion> calList = calificacionCatalogosBean.obtenerPorFiltro(fc); //Tiene cache
                            if (calList.isEmpty()) {
                                respuesta = "fila " + fila + ": La calificación no corresponde al rango de la escala.";
                                errores = errores + " - " + respuesta;
                                ge.addError("cal", respuesta);
                                continue;
                            }
                            calificacionConceptual = calList.get(0);
                            calEst.setCaeCalificacionConceptualEstudiante(calificacionConceptual);
                        }

                        cell = row.getCell(2, Row.RETURN_BLANK_AS_NULL);
                        datos[2] = null;
                        if (cell != null) {
                            if (CellType.NUMERIC.equals(cell.getCellTypeEnum()) && DateUtil.isCellDateFormatted(cell)) {
                                datos[2] = cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                            } else {
                                respuesta = "fila " + fila + ": Formato de fecha incorrecto.";
                                errores = errores + " - " + respuesta;
                                ge.addError("fecha", respuesta);
                            }
                        }
                        calEst.setCaeFechaRealizado(datos[2] != null ? LocalDate.parse(datos[2]) : null);
                        calEst.setCaeProcesoDeCreacion(EnumProcesoCreacion.IMP);
                        cell = row.getCell(3, Row.RETURN_BLANK_AS_NULL);
                        datos[3] = cell != null ? cell.getStringCellValue() : null;
                        calEst.setCaeObservacion(datos[3]);

                        listCalEst.add(calEst);

                    }
                }
                if (StringUtils.isBlank(respuesta)) {
                    FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                    fce.setCaeComponentePlanEstudio(archCal.getAccComponentePlanEstudio().getCpePk());
                    fce.setCaeRangoFechaPk(archCal.getAccRangoFecha().getRfePk());
                    fce.setCaeTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                    List<Long> listEstudiantePk = listCalEst.stream().map(c -> c.getCaeEstudiante().getEstPk()).collect(Collectors.toList());
                    fce.setCaeEstudiantesPk(listEstudiantePk);
                    fce.setDescartandoSeccion(archCal.getAccSeccionPk());
                    //FIXME: Se agrego filtro por grado para secciones semestrales, pero esto no abarca el caso de repetidores en semestre, hay que ver mejor arreglo
                    fce.setCaeGradoFk(seccion.getSecServicioEducativo().getSduGrado().getGraPk());

                    //Filtra dependiendo si sección es anual o semestral
                    fce.setCaeTipoPeriodo(seccion.getSecTipoPeriodo());
                    fce.setCaeNumeroPeriodo(seccion.getSecNumeroPeriodo());

                    fce.setMaxResults(5L);
                    fce.setIncluirCampos(new String[]{"caeCalificacion.calSeccion.secPk", "caeEstudiante.estPk", "caeEstudiante.estPersona.perNie"});
                    List<SgCalificacionEstudiante> listCalEs = calificacionEstudianteBean.obtenerPorFiltro(fce);
                    for (SgCalificacionEstudiante cal : listCalEs) {
                        if (!cal.getCaeCalificacion().getCalSeccion().getSecPk().equals(archCal.getAccSeccionPk())) {
                            respuesta = "Estudiante con NIE " + cal.getCaeEstudiante().getEstPersona().getPerNie() + " ya tiene una calificación ingresada para el período seleccionado en otra sección.";
                            errores = errores + " - " + respuesta;
                            ge.addError("fecha", respuesta);
                        }
                    }
                }

                if (StringUtils.isBlank(respuesta)) {
                    if (!(EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado()))) {

                        FiltroCalificacion fca = new FiltroCalificacion();
                        fca.setSecPk(archCal.getAccSeccionPk());
                        fca.setCalComponentePlanEstudio(archCal.getAccComponentePlanEstudio().getCpePk());
                        fca.setCalRangoFecha(archCal.getAccRangoFecha().getRfePk());
                        fca.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                        fca.setIncluirCalificacionesEstudiantes(Boolean.TRUE);

                        //TODO: incluirCampos
                        List<SgCalificacionCE> calOriginalList = this.obtenerPorFiltro(fca);

                        if (calOriginalList.isEmpty()) {
                            SgCalificacionCE calCE = new SgCalificacionCE();
                            calCE.setCalComponentePlanEstudio(archCal.getAccComponentePlanEstudio());
                            calCE.setCalSeccion(seccion);
                            calCE.setCalRangoFecha(archCal.getAccRangoFecha());
                            calCE.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                            calCE.setCalFecha(LocalDate.now());
                            if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado())) {
                                listCalEst.stream().forEach(c -> c.setCaeCalificacion(calCE));
                            }
                            calCE.setCalCalificacionesEstudiante(listCalEst);
                            calReturn = calCE;
                            if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado())) {
                                this.guardar(calReturn, Boolean.FALSE, Boolean.FALSE);
                            }

                        } else {
                            SgCalificacionCE calOriginal = calOriginalList.get(0);
                            List<SgCalificacionEstudiante> listCalFinal = new ArrayList();

                            for (SgCalificacionEstudiante calEst : calOriginal.getCalCalificacionesEstudiante()) {
                                List<SgCalificacionEstudiante> calificacionExistente = listCalEst.stream().filter(elem -> elem.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());

                                if (!calificacionExistente.isEmpty()) {
                                    calEst.setCaeCalificacionConceptualEstudiante(calificacionExistente.get(0).getCaeCalificacionConceptualEstudiante());
                                    calEst.setCaeCalificacionNumericaEstudiante(calificacionExistente.get(0).getCaeCalificacionNumericaEstudiante());
                                    calEst.setCaeFechaRealizado(calificacionExistente.get(0).getCaeFechaRealizado());
                                    calEst.setCaeObservacion(calificacionExistente.get(0).getCaeObservacion());
                                    calEst.setCaeProcesoDeCreacion(EnumProcesoCreacion.IMP);
                                }
                                listCalFinal.add(calEst);

                            }

                            for (SgCalificacionEstudiante calEst : listCalEst) {
                                List<SgCalificacionEstudiante> calificacionExistente = listCalFinal.stream().filter(elem -> elem.getCaeEstudiante().getEstPk().equals(calEst.getCaeEstudiante().getEstPk())).collect(Collectors.toList());
                                if (calificacionExistente.isEmpty()) {
                                    calEst.setCaeCalificacion(calOriginal);
                                    calEst.setCaeProcesoDeCreacion(EnumProcesoCreacion.IMP);
                                    listCalFinal.add(calEst);
                                }

                            }
                            calOriginal.setCalCalificacionesEstudiante(listCalFinal);
                            if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado())) {
                                this.guardar(calOriginal, Boolean.FALSE, Boolean.FALSE);
                            } else {
                                calOriginal.getCalSeccion().getSecPk(); //init
                                calOriginal.getCalComponentePlanEstudio().getCpePk(); //init
                                calOriginal.getCalRangoFecha().getRfePk(); //init
                                calReturn = calOriginal;
                            }

                        }

                        if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado())) {
                            archCal.setAccEstado(EnumEstadoImportado.PROCESADO_EXITO);
                            archivoCalificacionesBean.guardar(archCal);
                        }
                        return calReturn;
                    }
                } else {
                    if (!EnumEstadoImportado.PROCESAMIENTO_DIRECTO.equals(archCal.getAccEstado()) && !EnumEstadoImportado.VALIDACION.equals(archCal.getAccEstado())) {
                        archCal.setAccEstado(EnumEstadoImportado.PROCESADO_ERROR);
                        archCal.setAccDescripcion(errores);

                        archivoCalificacionesBean.guardar(archCal);
                    } else {

                        if (ge.getErrores().size() > 1) {
                            throw ge;
                        }
                        return null;
                    }

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ex) {
                }
            }
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                }
            }
        }
        return new SgCalificacionCE();
    }
    

    public List<SgPorcentajeAsistenciasResponse> obtenerDatosHabilitacionPeriodoExtraordinario(SgPorcentajeAsistenciasRequest porcentajeRequest) {
        try {

            if (porcentajeRequest.getPinSeccion() == null
                    || porcentajeRequest.getComponentePlanEstudio() == null || porcentajeRequest.getComponentePlanGrado() == null) {
                BusinessException be = new BusinessException();
                be.addError("ERROR_OBTENER_DATOS_HABILITACION_PERIODO_EXTRAORDINARIO_INCOMPLETO");
                throw be;
            }

            List<SgPorcentajeAsistenciasResponse> listPorcentaje = new ArrayList();
            FiltroSeccion filtroSec = new FiltroSeccion();
            filtroSec.setSecPk(porcentajeRequest.getPinSeccion());
            filtroSec.setIncluirCampos(new String[]{"secAnioLectivo.alePk", "secServicioEducativo.sduGrado.graPk", "secPlanEstudio.pesPk", "secTipoPeriodo", "secNumeroPeriodo"});
            List<SgSeccion> listSeccion = seccionBean.obtenerPorFiltro(filtroSec);
            if (!listSeccion.isEmpty() && !porcentajeRequest.getPinEstudiantes().isEmpty()) {
                SgSeccion sec = listSeccion.get(0);

                if (porcentajeRequest.getPinEstudiantes() != null) {
                    List<Long> listEstudiantesPk = porcentajeRequest.getPinEstudiantes().stream().map(e -> e.getEstPk()).collect(Collectors.toList());

                    FiltroCalificacionEstudiante filtroCalOrdinarios = new FiltroCalificacionEstudiante();
                    filtroCalOrdinarios.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                        "caeCalificacion.calVersion",
                        "caeEstudiante.estPk",
                        "caeCalificacion.calTipoPeriodoCalificacion",
                        "caeCalificacion.calTipoCalendarioEscolar",
                        "caeCalificacion.calNumero",
                        "caeCalificacionNumericaEstudiante"
                    });
                    filtroCalOrdinarios.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{EnumTiposPeriodosCalificaciones.ORD}));
                    filtroCalOrdinarios.setCaeEstudiantesPk(porcentajeRequest.getPinEstudiantes().stream().map(c -> c.getEstPk()).collect(Collectors.toList()));
                    filtroCalOrdinarios.setAnioLectivoPk(sec.getSecAnioLectivo().getAlePk());
                    filtroCalOrdinarios.setCaeComponentePlanEstudio(porcentajeRequest.getComponentePlanEstudio());

                    //Filtra dependiendo si sección es anual o semestral
                    filtroCalOrdinarios.setCaeTipoPeriodo(sec.getSecTipoPeriodo());
                    filtroCalOrdinarios.setCaeNumeroPeriodo(sec.getSecNumeroPeriodo());

                    List<SgCalificacionEstudiante> calPeriodoOrdinario = calificacionEstudianteBean.obtenerPorFiltro(filtroCalOrdinarios);

                    List<Long> estudiantesDeshabilitados = new ArrayList();
                    for (Long estudiante : listEstudiantesPk) {
                        List<SgCalificacionEstudiante> calPeriodosCalificados = calPeriodoOrdinario.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(estudiante)).collect(Collectors.toList());
                        if (calPeriodosCalificados.size() < porcentajeRequest.getComponentePlanGrado().getCpgPeriodosCalificacion()) {
                            estudiantesDeshabilitados.add(estudiante);
                            SgPorcentajeAsistenciasResponse asisResp = new SgPorcentajeAsistenciasResponse();
                            asisResp.setPinEstudiante(new SgEstudiante(estudiante, 0));
                            asisResp.setPinEstudianteConTodosLosPeriodosCalificados(Boolean.FALSE);
                            listPorcentaje.add(asisResp);
                        }
                    }
                    if (!estudiantesDeshabilitados.isEmpty()) {
                        listEstudiantesPk.removeAll(estudiantesDeshabilitados);
                    }

                    if (listEstudiantesPk.isEmpty()) {
                        return listPorcentaje;
                    }

//                    FiltroAsistencia fa = new FiltroAsistencia();
//                    fa.setIncluirCampos(new String[]{
//                        "asiEstudiante.estPk",
//                        "asiEstudiante.estVersion",
//                        "asiInasistencia",
//                        "asiVersion"});
//                    fa.setAsiAnioLectivo(sec.getSecAnioLectivo().getAlePk());
//                    fa.setCaeEstudiantesPk(listEstudiantesPk);
//                    List<SgAsistencia> listAsis = asistenciaBean.obtenerPorFiltro(fa);
                    for (SgEstudiante est : porcentajeRequest.getPinEstudiantes()) {
                        if (!listEstudiantesPk.contains(est.getEstPk())) {
                            continue;
                        }
                        //List<SgAsistencia> listAsistenciaEstudiante = listAsis.stream().filter(c -> c.getAsiEstudiante().getEstPk().equals(est.getEstPk()) && c.getAsiInasistencia().equals(Boolean.FALSE)).collect(Collectors.toList());
                        SgPorcentajeAsistenciasResponse asisResp = new SgPorcentajeAsistenciasResponse();
                        asisResp.setPinEstudiante(est);
                        asisResp.setPinCantidadAsistencias(0);
                        asisResp.setPinEstudianteConTodosLosPeriodosCalificados(Boolean.TRUE);
                        listPorcentaje.add(asisResp);
                    }

                    //cantidad de componentes no aprobados
                    FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
                    fpg.setCpgGradoPk(sec.getSecServicioEducativo().getSduGrado().getGraPk());
                    fpg.setCpgPlanEstudioPk(sec.getSecPlanEstudio().getPesPk());
                    if (porcentajeRequest.getComponentePlanEstudioPrueba() == null) {
                        fpg.setCpgObjetoPromocion(Boolean.TRUE);
                    } else {
                        fpg.setCpgObjetoPromocionOPrueba(porcentajeRequest.getComponentePlanEstudioPrueba());
                    }
                    fpg.setCpgSeccionPk(sec.getSecPk());
                    fpg.setIncluirCampos(new String[]{"cpgPk", "cpgVersion",
                        "cpgComponentePlanEstudio.cpePk", "cpgComponentePlanEstudio.cpeVersion",
                        "cpgComponentePlanEstudio.cpeTipo", "cpgEscalaCalificacion.ecaPk", "cpgEscalaCalificacion.ecaPk",
                        "cpgEscalaCalificacion.ecaTipoEscala", "cpgEscalaCalificacion.ecaMinimoAprobacion",
                        "cpgObjetoPromocion",
                        "cpgPeriodosCalificacion"});
                    List<SgComponentePlanGrado> listCpg = componentePlanGradoBean.obtenerPorFiltro(fpg);
                    List<Long> listCpePk = listCpg.stream().map(c -> c.getCpgComponentePlanEstudio().getCpePk()).collect(Collectors.toList());

                    if (!listCpg.isEmpty()) {
                        FiltroCalificacionEstudiante filtroCalMayorNota = new FiltroCalificacionEstudiante();
                        filtroCalMayorNota.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                            "caeCalificacion.calComponentePlanEstudio.cpePk",
                            "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                            "caeCalificacion.calVersion",
                            "caeCalificacion.calNumero",
                            "caeCalificacion.calTipoPeriodoCalificacion",
                            "caeCalificacion.calTipoCalendarioEscolar",
                            "caeCalificacionNumericaEstudiante",
                            "caeCalificacionConceptualEstudiante.calOrden",
                            "caeVersion",
                            "caeResolucion",
                            "caeEstudiante.estPk"
                        });
                        filtroCalMayorNota.setOrderBy(new String[]{"caeEstudiante.estPersona.perPrimerApellido", "caeEstudiante.estPersona.perSegundoApellido",
                            "caeEstudiante.estPersona.perPrimerNombre", "caeEstudiante.estPersona.perSegundoNombre"});
                        filtroCalMayorNota.setAscending(new boolean[]{true, true, true, true});
                        filtroCalMayorNota.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.EXORD}));
                        filtroCalMayorNota.setCaeEstudiantesPk(listEstudiantesPk);
                        //filtroCalMayorNota.setSeccionActualEstudiantesPk(porcentajeRequest.getPinSeccion());
                        filtroCalMayorNota.setAnioLectivoPk(sec.getSecAnioLectivo().getAlePk());
                        filtroCalMayorNota.setCaeComponentePlanEstudio(porcentajeRequest.getComponentePlanEstudio());

                        //Filtra dependiendo si sección es anual o semestral
                        filtroCalMayorNota.setCaeTipoPeriodo(sec.getSecTipoPeriodo());
                        filtroCalMayorNota.setCaeNumeroPeriodo(sec.getSecNumeroPeriodo());

                        List<SgCalificacionEstudiante> listCalificacionesMayorNota = calificacionEstudianteBean.obtenerPorFiltro(filtroCalMayorNota);

                        FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
                        filtroCalEst.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                            "caeCalificacion.calComponentePlanEstudio.cpePk",
                            "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                            "caeCalificacion.calVersion",
                            "caeCalificacion.calTipoPeriodoCalificacion",
                            "caeCalificacionNumericaEstudiante",
                            "caeCalificacionConceptualEstudiante.calOrden",
                            "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                            "caeVersion",
                            "caeResolucion",
                            "caeEstudiante.estPk"
                        });
                        filtroCalEst.setOrderBy(new String[]{"caeEstudiante.estPersona.perPrimerApellido", "caeEstudiante.estPersona.perSegundoApellido",
                            "caeEstudiante.estPersona.perPrimerNombre", "caeEstudiante.estPersona.perSegundoNombre"});
                        filtroCalEst.setAscending(new boolean[]{true, true, true, true});
                        filtroCalEst.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.ORD}));
                        filtroCalEst.setCaeEstudiantesPk(listEstudiantesPk);
                        //filtroCalEst.setSeccionActualEstudiantesPk(porcentajeRequest.getPinSeccion());
                        filtroCalEst.setAnioLectivoPk(sec.getSecAnioLectivo().getAlePk());

                        //Filtra dependiendo si sección es anual o semestral
                        filtroCalEst.setCaeTipoPeriodo(sec.getSecTipoPeriodo());
                        filtroCalEst.setCaeNumeroPeriodo(sec.getSecNumeroPeriodo());

                        List<SgCalificacionEstudiante> listCalificacionesEstudiantes = calificacionEstudianteBean.obtenerPorFiltro(filtroCalEst);

                        List<SgCalificacionEstudiante> listCalificacionesEstudiantesOrdinarias = listCalificacionesEstudiantes.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)).collect(Collectors.toList());
                        listCalificacionesEstudiantes = listCalificacionesEstudiantes.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());

                        HashMap<Long, SgComponentePlanGrado> componentesPlanGradoHash = new HashMap<>();
                        for (SgComponentePlanGrado cpg : listCpg) {
                            if (!componentesPlanGradoHash.containsKey(cpg.getCpgComponentePlanEstudio().getCpePk())) {
                                componentesPlanGradoHash.put(cpg.getCpgComponentePlanEstudio().getCpePk(), cpg);
                            }
                        }

                        for (SgEstudiante est : porcentajeRequest.getPinEstudiantes()) {
                            if (!listEstudiantesPk.contains(est.getEstPk())) {
                                continue;
                            }
                            List<SgCalificacionEstudiante> listCal = listCalificacionesEstudiantes.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk()) && listCpePk.contains(c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk())).collect(Collectors.toList());
                            Integer materiasReprobadas = 0;
                            String calificacionNotaInstitucional = "0";
                            String calificacionNotaInstitucionalPAES = "0";
                            for (SgCalificacionEstudiante calEst : listCal) {
                                SgComponentePlanGrado cpg = componentesPlanGradoHash.get(calEst.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk());
                                if (cpg != null) {

                                    if (cpg.getCpgEscalaCalificacion().getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA) && BooleanUtils.isTrue(cpg.getCpgObjetoPromocion())) {
                                        //UN COMPONENTE SE TOMA COMO REPROBADO SI NO TIENE TODOS LOS PERIODOS ORDINARIOS CALIFICADOS
                                        //O SI LA NOTA INSTITUCIONAL ES MENOR AL MINIMO DE APROBACION
                                        List<SgCalificacionEstudiante> cantidadCalificacionesOrd = listCalificacionesEstudiantesOrdinarias.stream().filter(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(cpg.getCpgComponentePlanEstudio().getCpePk()) && c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());

                                        if (cpg.getCpgPeriodosCalificacion() < cantidadCalificacionesOrd.size()) {
                                            materiasReprobadas++;
                                        } else {
                                            if (calEst.getCaeCalificacionNumericaEstudiante() != null
                                                    && cpg.getCpgEscalaCalificacion().getEcaMinimoAprobacion() != null) {
                                                Double calific = Double.parseDouble(calEst.getCaeCalificacionNumericaEstudiante());
                                                calific = MathFunctionsUtils.redondear(calific, 0, RoundingMode.HALF_UP);
                                                if (calific.compareTo(cpg.getCpgEscalaCalificacion().getEcaMinimoAprobacion()) < 0) {
                                                    materiasReprobadas++;
                                                }
                                            }
                                        }
                                    }
                                    if (calEst.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)
                                            && calEst.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(porcentajeRequest.getComponentePlanEstudio())) {
                                        if (calEst.getCaeCalificacionNumericaEstudiante() != null) {
                                            calificacionNotaInstitucional = calEst.getCaeCalificacionNumericaEstudiante();
                                        }
                                    }
                                    if (porcentajeRequest.getComponentePlanEstudioPrueba() != null) {
                                        if (calEst.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)
                                                && calEst.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(porcentajeRequest.getComponentePlanEstudioPrueba())) {
                                            if (calEst.getCaeCalificacionNumericaEstudiante() != null) {
                                                calificacionNotaInstitucionalPAES = calEst.getCaeCalificacionNumericaEstudiante();
                                            }
                                        }
                                    }
                                }
                            }

                            List<SgCalificacionEstudiante> calMayorNota = listCalificacionesMayorNota.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                            Double mayorNota = 0.0;
                            Integer[] datosFijosUltimoNumero = new Integer[4];
                            String ppe = null, ppeps = null, spe = null, speps = null;
                            for (SgCalificacionEstudiante calEs : calMayorNota) {

                                //MAYOR NOTA ENTRE NI, PPE, PPEPS, SPE, SPPE
                                if (!StringUtils.isBlank(calEs.getCaeCalificacionNumericaEstudiante())) {
                                    if (mayorNota.compareTo(Double.valueOf(calEs.getCaeCalificacionNumericaEstudiante())) < 0) {
                                        mayorNota = Double.valueOf(calEs.getCaeCalificacionNumericaEstudiante());
                                    }
                                }

                                //ÚLTIMA NOTA PARA PPE, PPEPS, SPE, SPPE
                                if (calEs.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD)) {
                                    if (calEs.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.PREC)) {

                                        if (datosFijosUltimoNumero[0] == null || datosFijosUltimoNumero[0] < calEs.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                            datosFijosUltimoNumero[0] = calEs.getCaeCalificacion().getCalNumero();
                                            if (!StringUtils.isBlank(calEs.getCaeCalificacionNumericaEstudiante())) {
                                                ppe = calEs.getCaeCalificacionNumericaEstudiante();
                                            }
                                        }

                                    }
                                    if (calEs.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.PRECPS)) {

                                        if (datosFijosUltimoNumero[1] == null || datosFijosUltimoNumero[1] < calEs.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                            datosFijosUltimoNumero[1] = calEs.getCaeCalificacion().getCalNumero();
                                            if (!StringUtils.isBlank(calEs.getCaeCalificacionNumericaEstudiante())) {
                                                ppeps = calEs.getCaeCalificacionNumericaEstudiante();
                                            }
                                        }

                                    }
                                    if (calEs.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.SREC)) {

                                        if (datosFijosUltimoNumero[2] == null || datosFijosUltimoNumero[2] < calEs.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                            datosFijosUltimoNumero[2] = calEs.getCaeCalificacion().getCalNumero();
                                            if (!StringUtils.isBlank(calEs.getCaeCalificacionNumericaEstudiante())) {
                                                spe = calEs.getCaeCalificacionNumericaEstudiante();
                                            }
                                        }
                                    }
                                    if (calEs.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(EnumCalendarioEscolar.SRECPS)) {

                                        if (datosFijosUltimoNumero[3] == null || datosFijosUltimoNumero[3] < calEs.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                            datosFijosUltimoNumero[3] = calEs.getCaeCalificacion().getCalNumero();
                                            if (!StringUtils.isBlank(speps)) {
                                                speps = calEs.getCaeCalificacionNumericaEstudiante();
                                            }
                                        }
                                    }
                                }

                            }

                            porcentaje:
                            for (SgPorcentajeAsistenciasResponse porcentaje : listPorcentaje) {
                                if (porcentaje.getPinEstudiante().getEstPk().equals(est.getEstPk())) {
                                    porcentaje.setPinCantidadNoAprobado(materiasReprobadas);
                                    porcentaje.setPinNotaInstitucional(calificacionNotaInstitucional);
                                    porcentaje.setPinNotaInstitucionalPrueba(calificacionNotaInstitucionalPAES);
                                    porcentaje.setPinMayorNota(mayorNota.compareTo(0.0) != 0 ? mayorNota.toString() : null);
                                    porcentaje.setPinPpe(ppe != null ? Double.valueOf(ppe) : null);
                                    porcentaje.setPinPpeps(ppeps != null ? Double.valueOf(ppeps) : null);
                                    porcentaje.setPinSpe(spe != null ? Double.valueOf(spe) : null);
                                    porcentaje.setPinSppe(speps != null ? Double.valueOf(speps) : null);
                                    break porcentaje;
                                }
                            }
                        }
                    }
                }
            }
            return listPorcentaje;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgConsultaCalificacionesPendientesSedesEnNivel> obtenerCalificacionesPendientesPorSedeEnNivel(FiltroCalificacionesPendientesSedeEnNivel filtro) throws GeneralException {
        try {
            //Contabiliza solamente calificaciones ORD

            if (filtro.getCalNacionalAnioLectivoPk() == null || filtro.getCalInternacionalAnioLectivoPk() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_VACIO);
                throw be;
            }

            Session session = em.unwrap(Session.class);
            SeguridadDAO segDAO = new SeguridadDAO(em);

            String offset = "";
            String limit = "";
            String whereCabezal = "";
            String whereSede = "";

            String whereDataSecurity = "";
            List<OperationSecurity> ops = segDAO.obtenerOperacionesSeguridad(ConstantesOperaciones.BUSCAR_CALIFICACIONES_PENDIENTES_POR_SEDE, Lookup.obtenerJWT().getSubject());
            if (ops != null && ops.isEmpty()) {
                throw new DataSecurityException();
            }
            for (OperationSecurity o : ops) {
                if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
                    whereDataSecurity = "";
                    break;
                }
                if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
                    whereDataSecurity += " or sed.sed_pk = " + o.getContext() + " or sed.sed_centro_adscrito = " + o.getContext();
                } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
                    whereDataSecurity += " or dir.dir_departamento = " + o.getContext();
                } else {
                    //Sin acceso
                    whereDataSecurity += " or sed.sed_pk = -1 ";
                }
            }

            if (!StringUtils.isBlank(whereDataSecurity)) {
                whereDataSecurity = " and (" + whereDataSecurity.replaceFirst("or", "") + ")";
            }

            if (filtro.getFirst() != null) {
                offset = " offset :offset ";

            }

            if (filtro.getMaxResults() != null) {
                limit = " limit :limit ";
            }

            if (filtro.getSedNivel() != null) {
                whereCabezal += " and niv.niv_pk = :nivPk";

                whereSede += " and exists (select 1 from centros_educativos.sg_servicio_educativo sdu"
                        + "		INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                        + "		INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                        + "		INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)"
                        + "		INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)"
                        + "		INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk) where niv_pk = :nivPk and sdu.sdu_sede_fk = sed.sed_pk)";
            }

            if (BooleanUtils.isFalse(filtro.getIncluirAdscritas())) {
                whereSede += " and sed.sed_centro_adscrito is null";
            }

            if (filtro.getSedDepartamentoId() != null || filtro.getSedMunicipioId() != null) {
                if (filtro.getSedDepartamentoId() != null) {
                    whereSede += " and dir.dir_departamento = :depPk";
                }
                if (filtro.getSedMunicipioId() != null) {
                    whereSede += " and dir.dir_municipio = :munPk";
                }
            }

            if (filtro.getSedPk() != null) {
                whereSede += " and sed.sed_pk = :sedPk";
            }

            whereSede += whereDataSecurity;
            if (!StringUtils.isBlank(whereSede)) {
                whereSede = " where " + whereSede.replaceFirst("and", "");
            }

            SQLQuery q = session.createSQLQuery("select f1.departamento as departamento, f1.municipio as municipio, f1.sedeNombre as sedeNombre, f1.sedeCodigo as sedeCodigo, f1.tipoCalendarioNombre as tipoCalendarioNombre, f1.nivNombre as desagregacion, f1.cantidad as posibles, f2.cantidad as realizadas, (f1.cantidad - COALESCE(f2.cantidad, 0)) as pendientes from"
                    + "	"
                    + "	(select sedes.sed_departamento as departamento, sedes.sed_municipio as municipio, sedes.sed_pk as sedePk, sedes.sed_nombre as sedeNombre, sedes.sed_codigo as sedeCodigo, sedes.tce_nombre as tipoCalendarioNombre, h1.niv_pk as nivPk, h1.niv_nombre as nivNombre, SUM(sec_cantidad_estudiantes_no_retirados)  as cantidad  from "
                    + "	((select sed.sed_pk as sed_pk, sed.sed_nombre as sed_nombre, sed.sed_codigo as sed_codigo, tce.tce_nombre, mun.mun_nombre as sed_municipio, dep.dep_nombre as sed_departamento, calendarios.cal_fecha_inicio, calendarios.cal_fecha_fin, calendarios.cal_anio_lectivo_fk from centros_educativos.sg_sedes sed "
                    + "                 INNER JOIN centros_educativos.sg_direcciones dir ON (dir.dir_pk = sed.sed_direccion_fk)  "
                    + "                 INNER JOIN catalogo.sg_departamentos dep ON (dir.dir_departamento = dep.dep_pk) "
                    + "                 INNER JOIN catalogo.sg_municipios mun ON (dir.dir_municipio = mun.mun_pk) "
                    + "	  		INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)"
                    + "			INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND ((cal.cal_tipo_calendario_fk = " + Constantes.TIPO_CALENDARIO_NACIONAL_PK + " AND cal.cal_anio_lectivo_fk = :anioLectivoNacional) OR (cal.cal_tipo_calendario_fk = " + Constantes.TIPO_CALENDARIO_INTERNACIONAL_PK + " AND cal.cal_anio_lectivo_fk = :anioLectivoInternacional)) limit 1))"
                    + whereSede
                    + "                 order by sed_departamento asc, sed_municipio asc, sed_nombre asc"
                    + offset + " " + limit
                    + "                  ) as sedes "
                    + "     LEFT OUTER JOIN										"
                    + "		(select * from centros_educativos.sg_secciones sec "
                    + "		INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)"
                    + "		INNER JOIN centros_educativos.sg_sedes sede ON (sdu.sdu_sede_fk = sede.sed_pk)"
                    + "		INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                    + "		INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk))"
                    + "    	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                    + "    	INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)"
                    + "    	INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)"
                    + "    	INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)"
                    + "		INNER JOIN centros_educativos.sg_periodos_calificacion periodo ON "
                    + "		((periodo.pca_modalidad_educativa_fk = relmodaten.rea_modalidad_educativa_fk and periodo.pca_modalidad_atencion_fk = relmodaten.rea_modalidad_atencion_fk and relmodaten.rea_sub_modalidad_atencion_fk is null and cpg.cpg_periodos_calificacion = periodo.pca_numero ) or "
                    + "		(periodo.pca_modalidad_educativa_fk = relmodaten.rea_modalidad_educativa_fk and periodo.pca_modalidad_atencion_fk = relmodaten.rea_modalidad_atencion_fk and relmodaten.rea_sub_modalidad_atencion_fk is not null and periodo.pca_sub_modalidad_educativa_fk = relmodaten.rea_sub_modalidad_atencion_fk and cpg.cpg_periodos_calificacion = periodo.pca_numero))"
                    + "		INNER JOIN centros_educativos.sg_rango_fecha rf ON (periodo.pca_pk = rf.rfe_periodo_calificacion_fk and rf.rfe_fecha_desde < now())"
                    + "		where sec.sec_estado = 'ABIERTA' and sec.sec_cantidad_estudiantes_no_retirados > 0 " + whereCabezal + ") as h1"
                    + "	ON ( h1.sdu_sede_fk = sedes.sed_pk AND h1.rfe_fecha_desde >= sedes.cal_fecha_inicio AND h1.rfe_fecha_desde <= sedes.cal_fecha_fin AND h1.sec_anio_lectivo_fk = sedes.cal_anio_lectivo_fk))"
                    + "	group by sedes.sed_departamento, sedes.sed_municipio, sedes.sed_pk, sedes.sed_nombre, sedes.sed_codigo, sedes.tce_nombre, h1.niv_pk, h1.niv_nombre) f1"
                    + "	"
                    + "	LEFT OUTER JOIN"
                    + "	"
                    + "	(select sedes1.sed_departamento as departamento, sedes1.sed_municipio as municipio, sedes1.sed_pk as sedePk, sedes1.sed_nombre as sedeNombre, sedes1.sed_codigo as sedeCodigo, h2.niv_pk as nivPk, h2.niv_nombre as nivNombre,  SUM(h2.cal_cant_estudiantes_calificados) as cantidad  from"
                    + "	"
                    + "	(select sed.sed_pk as sed_pk, sed.sed_nombre as sed_nombre, sed.sed_codigo as sed_codigo, mun.mun_nombre as sed_municipio, dep.dep_nombre as sed_departamento, calendarios.cal_fecha_inicio, calendarios.cal_fecha_fin, calendarios.cal_anio_lectivo_fk  from centros_educativos.sg_sedes sed "
                    + "                   INNER JOIN centros_educativos.sg_direcciones dir ON (dir.dir_pk = sed.sed_direccion_fk)  "
                    + "                   INNER JOIN catalogo.sg_departamentos dep ON (dir.dir_departamento = dep.dep_pk) "
                    + "                   INNER JOIN catalogo.sg_municipios mun ON (dir.dir_municipio = mun.mun_pk) "
                    + "                   INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk)"
                    + "			  INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND ((cal.cal_tipo_calendario_fk = " + Constantes.TIPO_CALENDARIO_NACIONAL_PK + " AND cal.cal_anio_lectivo_fk = :anioLectivoNacional) OR (cal.cal_tipo_calendario_fk = " + Constantes.TIPO_CALENDARIO_INTERNACIONAL_PK + " AND cal.cal_anio_lectivo_fk = :anioLectivoInternacional)) limit 1))"
                    + whereSede
                    + "                   order by sed_departamento asc, sed_municipio asc, sed_nombre asc"
                    + offset + " " + limit
                    + "                   ) as sedes1 "
                    + "     LEFT OUTER JOIN"
                    + "         (select * from centros_educativos.sg_calificaciones cabezal"
                    + "         INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk)"
                    + "         INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)"
                    + "         INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                    + "         INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                    + "         INNER JOIN centros_educativos.sg_modalidades modedu ON (relmodaten.rea_modalidad_educativa_fk = modedu.mod_pk)"
                    + "         INNER JOIN centros_educativos.sg_ciclos cic ON (modedu.mod_ciclo = cic.cic_pk)"
                    + "         INNER JOIN centros_educativos.sg_niveles niv ON (cic.cic_nivel = niv.niv_pk)"
                    + "         INNER JOIN centros_educativos.sg_rango_fecha rf ON (cabezal.cal_rango_fecha_fk = rf.rfe_pk and rf.rfe_fecha_desde < now())"
                    + "         where cabezal.cal_cant_estudiantes_calificados > 0 and cabezal.cal_tipo_periodo_calificacion = 'ORD' " + whereCabezal + ") h2"
                    + "	ON ( h2.sdu_sede_fk = sedes1.sed_pk AND h2.rfe_fecha_desde >= sedes1.cal_fecha_inicio AND  h2.rfe_fecha_desde <= sedes1.cal_fecha_fin  and h2.sec_anio_lectivo_fk = sedes1.cal_anio_lectivo_fk )"
                    + "	group by sedes1.sed_departamento, sedes1.sed_municipio, sedes1.sed_pk, sedes1.sed_nombre, sedes1.sed_codigo, h2.niv_pk, h2.niv_nombre) f2"
                    + "	"
                    + ""
                    + "	ON (f1.departamento = f2.departamento AND f1.municipio = f2.municipio AND f1.sedePk = f2.sedePk AND f1.nivPk = f2.nivPk)"
                    + "	order by departamento asc, municipio asc, sedeNombre asc");

            q.addScalar("departamento", new StringType());
            q.addScalar("municipio", new StringType());
            q.addScalar("sedeCodigo", new StringType());
            q.addScalar("sedeNombre", new StringType());
            q.addScalar("tipoCalendarioNombre", new StringType());
            q.addScalar("desagregacion", new StringType());
            q.addScalar("posibles", new LongType());
            q.addScalar("realizadas", new LongType());
            q.addScalar("pendientes", new LongType());

            if (filtro.getSedDepartamentoId() != null) {
                q.setParameter("depPk", filtro.getSedDepartamentoId());
            }
            if (filtro.getSedMunicipioId() != null) {
                q.setParameter("munPk", filtro.getSedMunicipioId());
            }
            if (filtro.getSedNivel() != null) {
                q.setParameter("nivPk", filtro.getSedNivel());
            }
            if (filtro.getSedPk() != null) {
                q.setParameter("sedPk", filtro.getSedPk());
            }

            q.setParameter("anioLectivoNacional", filtro.getCalNacionalAnioLectivoPk());
            q.setParameter("anioLectivoInternacional", filtro.getCalInternacionalAnioLectivoPk());

            if (filtro.getFirst() != null) {
                q.setParameter("offset", filtro.getFirst());
            }
            if (filtro.getMaxResults() != null) {
                q.setParameter("limit", filtro.getMaxResults());
            }
            q.setResultTransformer(Transformers.aliasToBean(SgConsultaCalificacionesPendientesSedesEnNivel.class));
            return q.list();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    //Este método se llama desde frontend, si es asincrónica se guarda calificación con estado sin_procesar
    //Si es sincrónica se ejecuta el metodo calcularPromocion
    @Interceptors(AuditInterceptor.class)
    public Boolean actualizarPromocion(SgDatosCalculoCalificacionesPromocion datoCalculoPromocion) throws Exception {
        if (datoCalculoPromocion.getSeccion() == null
                || datoCalculoPromocion.getSeccion().getSecPlanEstudio() == null
                || datoCalculoPromocion.getSeccion().getSecServicioEducativo().getSduGrado() == null) {
            return Boolean.FALSE;
        }
        FiltroCalificacion fc = new FiltroCalificacion();
        fc.setSecPk(datoCalculoPromocion.getSeccion().getSecPk());
        fc.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.GRA);
        fc.setIncluirCampos(new String[]{"calFecha",
            "calUltModFecha",
            "calUltModUsuario",
            "calVersion",
            "calTipoPeriodoCalificacion",
            "calCerrado",
            "calSeccion.secAnioLectivo.aleAnio",
            "calSeccion.secAnioLectivo.alePk",
            "calSeccion.secAnioLectivo.aleVersion",
            "calSeccion.secPlanEstudio.pesPk",
            "calSeccion.secPlanEstudio.pesNombre",
            "calSeccion.secPlanEstudio.pesVersion",
            "calSeccion.secServicioEducativo.sduGrado.graPk",
            "calSeccion.secServicioEducativo.sduGrado.graNombre",
            "calSeccion.secServicioEducativo.sduOpcion.opcNombre",
            "calSeccion.secServicioEducativo.sduOpcion.opcPk",
            "calSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
            "calSeccion.secServicioEducativo.sduProgramaEducativo.pedPk",
            "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk",
            "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk",
            "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk",
            "calSeccion.secServicioEducativo.sduSede.sedPk",
            "calSeccion.secServicioEducativo.sduSede.sedTipo",
            "calSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
            "calSeccion.secNombre",
            "calSeccion.secPk",
            "calSeccion.secTipoPeriodo",
            "calSeccion.secNumeroPeriodo",
            "calSeccion.secVersion",
            "calEstadoProcesamientoPromocion",
            "calInfoProcesamientoCalificacionFk.ipcPk",
            "calInfoProcesamientoCalificacionFk.ipcError",
            "calInfoProcesamientoCalificacionFk.ipcVersion"});
        List<SgCalificacionCE> calificaciones = this.obtenerPorFiltro(fc);

        if (BooleanUtils.isTrue(datoCalculoPromocion.getEsAsnicronica())) {

            if (calificaciones.isEmpty()) {
                SgCalificacionCE calNueva = new SgCalificacionCE();
                calNueva.setCalSeccion(datoCalculoPromocion.getSeccion());
                calNueva.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.GRA);
                calNueva.setCalFecha(LocalDate.now());
                calNueva.setCalEstadoProcesamientoPromocion(EnumEstadoProcesamientoCalificacionPromocion.SIN_PROCESAR);
                CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                codDAO.guardar(calNueva, null);
            } else {
                SgCalificacionCELite calLite = new SgCalificacionCELite();
                calLite.setCalEstadoProcesamientoPromocion(EnumEstadoProcesamientoCalificacionPromocion.SIN_PROCESAR);
                calLite.setCalPk(calificaciones.get(0).getCalPk());
                calLite.setCalVersion(calificaciones.get(0).getCalVersion());
                calLite.setCalFecha(LocalDate.now());
                this.guardarCabezal(calLite);
            }
        } else {

            SgCalificacionCE calificacion = new SgCalificacionCE();
            if (!calificaciones.isEmpty()) {
                calificacion = calificaciones.get(0);
            } else {
                calificacion.setCalSeccion(datoCalculoPromocion.getSeccion());
                calificacion.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.GRA);
            }
            calificacion.setCalEstadoProcesamientoPromocion(null);
            calcularPromocion(calificacion, null);
        }

        return Boolean.TRUE;
    }

    @Interceptors(AuditInterceptor.class)
    public SgCalificacionCE calcularPromocion(SgCalificacionCE calificacion, Long estudiantePk) throws Exception {
        try {

            HashMap<SgEstudiante, String> estudianteError = new HashMap<>();

            if (calificacion.getCalInfoProcesamientoCalificacionFk() != null) {
                calificacion.getCalInfoProcesamientoCalificacionFk().getIpcPk();
            } else {
                SgInfoProcesamientoCalificacion infoProc = new SgInfoProcesamientoCalificacion();
                calificacion.setCalInfoProcesamientoCalificacionFk(infoProc);
            }

            FiltroRelGradoPlanEstudio filtroRelGraPlan = new FiltroRelGradoPlanEstudio();
            filtroRelGraPlan.setRgpGrado(calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
            filtroRelGraPlan.setRgpPlanEstudio(calificacion.getCalSeccion().getSecPlanEstudio().getPesPk());
            filtroRelGraPlan.setIncluirCampos(new String[]{"rgpFormulaFk.fomTextoLargo",
                "rgpRequiereValidacionAcademica"});
            List<SgRelGradoPlanEstudio> listRelGra = relGradoPlanEstudioBean.obtenerPorFiltro(filtroRelGraPlan);

            String funcion = null;
            if (listRelGra.isEmpty()) {

                SgCalificacionCELite calCELite = new SgCalificacionCELite();
                calCELite.setCalPk(calificacion.getCalPk());
                calCELite.setCalVersion(calificacion.getCalVersion());
                calCELite.setCalInfoProcesamientoCalificacionFk(calificacion.getCalInfoProcesamientoCalificacionFk());
                calCELite.setCalEstadoProcesamientoPromocion(EnumEstadoProcesamientoCalificacionPromocion.PROCESADO_ERROR);
                if (calCELite.getCalInfoProcesamientoCalificacionFk() != null) {
                    calCELite.getCalInfoProcesamientoCalificacionFk().setIpcError("No tiene fórmula asociada");
                } else {
                    SgInfoProcesamientoCalificacion proCal = new SgInfoProcesamientoCalificacion();
                    proCal.setIpcError("No tiene fórmula asociada");
                    calCELite.setCalInfoProcesamientoCalificacionFk(proCal);
                }
                this.guardarCabezal(calCELite);
                return null;

            } else {
                funcion = listRelGra.get(0).getRgpFormulaFk().getFomTextoLargo();
            }

            FiltroComponentePlanGrado fcg = new FiltroComponentePlanGrado();
            fcg.setCpgGradoPk(calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
            fcg.setCpgPlanEstudioPk(calificacion.getCalSeccion().getSecPlanEstudio().getPesPk());
            //  PARA EL CALCULO DE PROMOCION NO HAY QUE AGREGAR SECCIÓN EXCLUSIVA
            fcg.setIncluirCampos(new String[]{
                "cpgPeriodosCalificacion",
                "cpgEscalaCalificacion.ecaPk",
                "cpgEscalaCalificacion.ecaCodigo",
                "cpgEscalaCalificacion.ecaHabilitado",
                "cpgEscalaCalificacion.ecaNombre",
                "cpgEscalaCalificacion.ecaTipoEscala",
                "cpgEscalaCalificacion.ecaMinimo",
                "cpgEscalaCalificacion.ecaMaximo",
                "cpgEscalaCalificacion.ecaMinimoAprobacion",
                "cpgEscalaCalificacion.ecaPrecision",
                "cpgEscalaCalificacion.ecaValorMinimo.calValor",
                "cpgEscalaCalificacion.ecaValorMinimo.calOrden",
                "cpgComponentePlanEstudio.cpeTipo",
                "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeVersion",
                "cpgComponentePlanEstudio.cpeNombre",
                "cpgCantidadPrimeraPrueba",
                "cpgCantidadPrimeraSuficiencia",
                "cpgCantidadSegundaPrueba",
                "cpgCantidadSegundaSuficiencia",
                "cpgCalculoNotaInstitucional",
                "cpgFuncionRedondeo",
                "cpgOrden",
                "cpgCodigoExterno",
                "cpgPrecision",
                "cpgObjetoPromocion",
                "cpgCalificacionIngresadaCE",
                "cpgFormula.fomTextoLargo",
                "cpgFormula.fomPk",
                "cpgFormula.fomTipoFormula",
                "cpgFormula.fomTienSubformula",
                "cpgParametroFormulaAprobacion.cpgComponentePlanEstudio.cpePk",
                "cpgParametroFormulaAprobacion.cpgComponentePlanEstudio.cpeTipo"});
            List<SgComponentePlanGrado> listCpg = componentePlanGradoBean.obtenerPorFiltro(fcg);

            List<SgComponentePlanGrado> listCpgActualizar = listCpg.stream().filter(c -> !c.getCpgComponentePlanEstudio().getCpeTipo().equals(TipoComponentePlanEstudio.PRU)).collect(Collectors.toList());

            FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
            fpc.setIncluirCampos(new String[]{
                "pcaNumero",
                "pcaPermiteCalificarSinNie",
                "pcaEsPrueba",
                "pcaNombre",
                "pcaTipoPeriodo",
                "pcaNumeroPeriodo",
                "pcaVersion"});
            fpc.setPcaEsPrueba(Boolean.FALSE);
            fpc.setPcaModalidadEducativa(calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModPk());
            fpc.setPcaModalidadAtencion(calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
            fpc.setPcaSubModalidadAtencion(calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
            fpc.setPcaAnioLectivo(calificacion.getCalSeccion().getSecAnioLectivo().getAlePk());
            fpc.setPcaTipoCalendario(calificacion.getCalSeccion().getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());

            //Se busca por periodo semestral o anual
            fpc.setPcaTipoPeriodo(calificacion.getCalSeccion().getSecTipoPeriodo());
            fpc.setPcaNumeroPeriodo(calificacion.getCalSeccion().getSecNumeroPeriodo());

            List<SgPeriodoCalificacion> listPeriodoCalif = periodoCalificacionBean.obtenerPorFiltro(fpc);

            for (SgComponentePlanGrado cpg : listCpgActualizar) {
                //Componentes que no coincidan con ningun periodo se ignoran
                //TODO: revisar
                Optional<SgPeriodoCalificacion> periodo = listPeriodoCalif.stream().filter(p -> p.getPcaNumero().equals(cpg.getCpgPeriodosCalificacion())).findAny();
                if (periodo.isPresent()) {
                    SgDatosCalculoCalificaciones calculoCalificaciones = new SgDatosCalculoCalificaciones();
                    calculoCalificaciones.setComponentePlanGrado(cpg);
                    calculoCalificaciones.setPeriodoCalificacionPk(periodo.get().getPcaPk());
                    calculoCalificaciones.setSeccion(calificacion.getCalSeccion());

                    //EL CÁLCULO PUEDE SER PARA UN ÚNICO ESTUDIANTE
                    if (estudiantePk != null) {
                        calculoCalificaciones.setEstudiantePk(estudiantePk);
                    }

                    calcularNotaAprobacion(calculoCalificaciones);
                }
            }

            FiltroMatricula filtroMat = new FiltroMatricula();
            filtroMat.setSecPk(calificacion.getCalSeccion().getSecPk());

            //EN CASO DE QUE SE CALCULE PROMOCIÓN PARA UN ÚNICO ESTUDIANTE
            filtroMat.setMatEstudiantePk(estudiantePk);

            filtroMat.setMatRetirada(Boolean.FALSE);

            filtroMat.setIncluirCampos(new String[]{"matEstudiante.estPk", "matEstudiante.estPersona.perNie", "matEstudiante.estPersona.perPk", "matValidacionAcademica", "matEstudiante.estVersion"});
            List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(filtroMat);
            List<SgEstudiante> listEstudiantes = matriculas.stream().map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());

            List<SgEstudiante> listEstudiantesSinValAcademica = new ArrayList();
            //En caso de que requiera validación académica, se filtran los estudiantes que no tengan validación académica y se agregan al hash de errores
            if (BooleanUtils.isTrue(listRelGra.get(0).getRgpRequiereValidacionAcademica())) {
                listEstudiantesSinValAcademica = matriculas.stream().filter(c -> BooleanUtils.isNotTrue(c.getMatValidacionAcademica())).map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());
                for (SgEstudiante est : listEstudiantesSinValAcademica) {
                    String err = "<li><b>La matrícula del estudiante no está validada académicamente.</b></li><br/>";
                    if (!estudianteError.containsKey(est)) {
                        estudianteError.put(est, err);
                    } else {
                        estudianteError.put(est, estudianteError.get(est) + " " + err);
                    }
                }
            }

            //Se filtran los estudiantes que tengan nie null y se agregan al hash de errores
            List<SgEstudiante> estudiantesSinNie = matriculas.stream().filter(c -> c.getMatEstudiante().getEstPersona().getPerNie() == null).map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());
            for (SgEstudiante est : estudiantesSinNie) {
                String err = "<li><b>Estudiante no tiene NIE.</b></li><br/>";
                if (!estudianteError.containsKey(est)) {
                    estudianteError.put(est, err);
                } else {
                    estudianteError.put(est, estudianteError.get(est) + " " + err);
                }
            }

            if (!listEstudiantes.isEmpty()) {
                SgPorcentajeAsistenciasRequest fa = new SgPorcentajeAsistenciasRequest();
                fa.setPinSeccion(calificacion.getCalSeccion().getSecPk());
                fa.setPinEstudiantes(listEstudiantes);
                List<SgPorcentajeAsistenciasResponse> listPorcentajeAsistencia = porcentajeAsistenciasBean.obtenerPorcentajeListaAsistencias(fa);
                listPorcentajeAsistencia.stream().forEach(s -> s.setPinCantidadNoAprobado(0));
                List<Long> listCpePk = listCpg.stream().map(c -> c.getCpgComponentePlanEstudio().getCpePk()).collect(Collectors.toList());
                List<String> listCpeNombre = listCpg.stream().map(c -> c.getCpgComponentePlanEstudio().getCpeNombre()).collect(Collectors.toList());

                List<SgComponentePlanGrado> listCpgObjetoPromocion = listCpg.stream().filter(c -> BooleanUtils.isTrue(c.getCpgObjetoPromocion())).collect(Collectors.toList());
                List<Long> listCpePkObjetoPromocion = listCpgObjetoPromocion.stream().map(c -> c.getCpgComponentePlanEstudio().getCpePk()).collect(Collectors.toList());

                if (!listCpg.isEmpty()) {

                    FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
                    filtroCalEst.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                        "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                        "caeCalificacion.calComponentePlanEstudio.cpePk",
                        "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                        "caeCalificacion.calTipoCalendarioEscolar",
                        "caeCalificacion.calTipoPeriodoCalificacion",
                        "caeCalificacion.calNumero",
                        "caeCalificacion.calVersion",
                        "caeCalificacionNumericaEstudiante",
                        "caeCalificacionConceptualEstudiante.calPk",
                        "caeCalificacionConceptualEstudiante.calCodigo",
                        "caeCalificacionConceptualEstudiante.calValor",
                        "caeCalificacionConceptualEstudiante.calOrden",
                        "caeCalificacionConceptualEstudiante.calVersion",
                        "caeVersion",
                        "caeResolucion",
                        "caeEstudiante.estPk"

                    });
                    filtroCalEst.setCaeGradoFk(calificacion.getCalSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                    filtroCalEst.setOrderBy(new String[]{"caeEstudiante.estPersona.perPrimerApellido", "caeEstudiante.estPersona.perSegundoApellido",
                        "caeEstudiante.estPersona.perPrimerNombre", "caeEstudiante.estPersona.perSegundoNombre"});
                    filtroCalEst.setAscending(new boolean[]{true, true, true, true});
                    filtroCalEst.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                        EnumTiposPeriodosCalificaciones.APR}));
                    filtroCalEst.setCaeEstudiantesPk(listEstudiantes.stream().map(e -> e.getEstPk()).collect(Collectors.toList()));
                    filtroCalEst.setAnioLectivoPk(calificacion.getCalSeccion().getSecAnioLectivo().getAlePk());
                    //FILTRA SEGUN SI SECCIÓN ES ANUAL O SEMESTRAL
                    filtroCalEst.setCaeTipoPeriodo(calificacion.getCalSeccion().getSecTipoPeriodo());
                    filtroCalEst.setCaeNumeroPeriodo(calificacion.getCalSeccion().getSecNumeroPeriodo());

                    List<SgCalificacionEstudiante> listCalificacionesEstudiantes = calificacionEstudianteBean.obtenerPorFiltro(filtroCalEst);

                    for (SgEstudiante est : listEstudiantes) {

                        List<SgCalificacionEstudiante> listCal = listCalificacionesEstudiantes.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())
                                && !EnumResolucionCalificacion.PENDIENTE.equals(c.getCaeResolucion())
                                && listCpePk.contains(c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk())).collect(Collectors.toList());
                        List<String> componenteCalificadoNombre = listCal.stream().map(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().getCpeNombre()).collect(Collectors.toList());

                        porcentaje:
                        for (SgPorcentajeAsistenciasResponse porcentaje : listPorcentajeAsistencia) {
                            if (porcentaje.getPinEstudiante().getEstPk().equals(est.getEstPk())) {

                                porcentaje.setPinCantidadNoAprobado(null);

                                //La cantidad de no aprobados se ingresa solo si la cantidad de calificaciones es igual a la cantidad de materias
                                //Con esto aseguramos que no se promueva a un alumno que no tiene todas las calificaciones
                                //Las componentes exclusivas de la sección no son tomadas en cuenta
                                if (listCal.size() == listCpePk.size()) {
                                    List<SgCalificacionEstudiante> listCalNoAprobados = listCal.stream().filter(c -> EnumResolucionCalificacion.NO_APROBADO.equals(c.getCaeResolucion())
                                            && listCpePkObjetoPromocion.contains(c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk())).collect(Collectors.toList());

                                    porcentaje.setPinCantidadNoAprobado(listCalNoAprobados.size());
                                } else {
                                    List<String> componentesSinCalificar = new ArrayList(listCpeNombre);
                                    componentesSinCalificar.removeAll(componenteCalificadoNombre);
                                    if (!componentesSinCalificar.isEmpty()) {
                                        Collections.sort(componentesSinCalificar);
                                    }
                                    String er1 = "<li><b>El estudiante tiene los siguientes componentes sin nota de aprobación:</b></li><br/>";
                                    String listCpe = "<ul style=\"list-style-type:disc;\">";
                                    for (String cpe : componentesSinCalificar) {
                                        listCpe += "<li>" + cpe + "</li>";
                                    }
                                    listCpe += "</ul>";
                                    er1 += listCpe;
                                    if (!estudianteError.containsKey(est)) {
                                        estudianteError.put(est, er1);
                                    } else {
                                        estudianteError.put(est, estudianteError.get(est) + " " + er1);
                                    }
                                }
                                break porcentaje;
                            }
                        }
                    }
                }

                List<SgCalificacionEstudiante> listCalificacionEstGrado = new ArrayList();
                if (calificacion.getCalPk() != null) {
                    FiltroCalificacionEstudiante filtroCalEst = new FiltroCalificacionEstudiante();
                    filtroCalEst.setCabezalPk(calificacion.getCalPk());
                    filtroCalEst.setIncluirCampos(new String[]{
                        "caePk",
                        "caeVersion",
                        "caeEstudiante.estPk",
                        "caeEstudiante.estVersion",
                        "caeResolucion",
                        "caeCalificacionNumericaEstudiante",
                        "caeCalificacionConceptualEstudiante.calPk",
                        "caeCalificacionConceptualEstudiante.calVersion",
                        "caePromovidoCalificacion",
                        "caeObservacion",
                        "caeFechaRealizado",
                        "caeProcesoDeCreacion",
                        "caeInfoProcesamientoCalificacionEstFk.ipePk",
                        "caeInfoProcesamientoCalificacionEstFk.ipeError",
                        "caeInfoProcesamientoCalificacionEstFk.ipeVersion",
                        "caeInfoProcesamientoCalificacionEstFk.ipePromocionPendiente"
                    });
                    //FILTRA SEGUN SI SECCIÓN ES ANUAL O SEMESTRAL
                    filtroCalEst.setCaeTipoPeriodo(calificacion.getCalSeccion().getSecTipoPeriodo());
                    filtroCalEst.setCaeNumeroPeriodo(calificacion.getCalSeccion().getSecNumeroPeriodo());

                    listCalificacionEstGrado = calificacionEstudianteBean.obtenerPorFiltro(filtroCalEst);

                }

                for (SgPorcentajeAsistenciasResponse porcentajeAsistencia : listPorcentajeAsistencia) {

                    Function formula = new Function(funcion);

                    String parteDefinicion;

                    String[] formulaSeparada = funcion.split("=", 2);
                    parteDefinicion = formulaSeparada[0];
                    parteDefinicion = parteDefinicion.trim();

                    Expression expresion = new Expression(parteDefinicion, formula);
                    Argument arg_asis = new Argument("asis=" + porcentajeAsistencia.getPinPorcentajeAsistencias());
                    expresion.addArguments(arg_asis);
                    Argument arg_noaprob = new Argument("noaprob=" + porcentajeAsistencia.getPinCantidadNoAprobado());
                    expresion.addArguments(arg_noaprob);
                    Double res = expresion.calculate();
                    res = Double.isNaN(res) ? null : MathFunctionsUtils.redondear(res, 0, RoundingMode.HALF_UP);
                    String resultado = res == null ? null : String.valueOf(res);

                    EnumPromovidoCalificacion promovidoCalif = null;
                    if (res != null) {
                        if (Double.compare(res, 1) == 0) {
                            promovidoCalif = EnumPromovidoCalificacion.PROMOVIDO;
                        } else {
                            promovidoCalif = EnumPromovidoCalificacion.NO_PROMOVIDO;
                        }
                    } else {

                        promovidoCalif = EnumPromovidoCalificacion.PENDIENTE;
                    }

                    //Si hay algún error para el estudiante se setea en Pendiente
                    if (estudianteError.get(porcentajeAsistencia.getPinEstudiante()) != null) {
                        promovidoCalif = EnumPromovidoCalificacion.PENDIENTE;
                    }

                    List<SgCalificacionEstudiante> calList = listCalificacionEstGrado.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(porcentajeAsistencia.getPinEstudiante().getEstPk())).collect(Collectors.toList());

                    if (calList.isEmpty()) {
                        SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();
                        calEst.setCaeEstudiante(porcentajeAsistencia.getPinEstudiante());
                        calEst.setCaeCalificacionNumericaEstudiante(resultado);
                        calEst.setCaeFechaRealizado(LocalDate.now());
                        calEst.setCaePromovidoCalificacion(promovidoCalif);
                        calEst.setCaeCalificacion(calificacion);
                        if (estudianteError.get(porcentajeAsistencia.getPinEstudiante()) != null) {
                            String errorFinal = "<ol>" + estudianteError.get(porcentajeAsistencia.getPinEstudiante()) + "</ol> ";
                            if (calEst.getCaeInfoProcesamientoCalificacionEstFk() != null) {
                                calEst.getCaeInfoProcesamientoCalificacionEstFk().setIpeError(errorFinal);
                            } else {
                                SgInfoProcesamientoCalificacionEst infoProcesamiento = new SgInfoProcesamientoCalificacionEst();
                                infoProcesamiento.setIpeError(errorFinal);
                                calEst.setCaeInfoProcesamientoCalificacionEstFk(infoProcesamiento);
                            }
                        } else {
                            if (calEst.getCaeInfoProcesamientoCalificacionEstFk() != null) {
                                calEst.getCaeInfoProcesamientoCalificacionEstFk().setIpeError(null);
                            }
                        }
                        listCalificacionEstGrado.add(calEst);
                    } else {
                        SgCalificacionEstudiante calNuevo = calList.get(0);
                        calNuevo.setCaeCalificacionNumericaEstudiante(resultado);
                        calNuevo.setCaeFechaRealizado(LocalDate.now());
                        calNuevo.setCaePromovidoCalificacion(promovidoCalif);

                        //EN CASO DE QUE PROMOCION_PENDIENTE ESTÉ EN TRUE SIEMPRE QUEDA EN PENDIENTE                        
                        if (calNuevo.getCaeInfoProcesamientoCalificacionEstFk() != null && BooleanUtils.isTrue(calNuevo.getCaeInfoProcesamientoCalificacionEstFk().getIpePromocionPendiente())) {
                            calNuevo.setCaePromovidoCalificacion(EnumPromovidoCalificacion.PENDIENTE);
                        } else {

                            if (estudianteError.get(porcentajeAsistencia.getPinEstudiante()) != null) {
                                String errorFinal = "<ol>" + estudianteError.get(porcentajeAsistencia.getPinEstudiante()) + "</ol> ";
                                if (calNuevo.getCaeInfoProcesamientoCalificacionEstFk() != null) {
                                    calNuevo.getCaeInfoProcesamientoCalificacionEstFk().setIpeError(errorFinal);
                                } else {
                                    SgInfoProcesamientoCalificacionEst infoProcesamiento = new SgInfoProcesamientoCalificacionEst();
                                    infoProcesamiento.setIpeError(errorFinal);
                                    calNuevo.setCaeInfoProcesamientoCalificacionEstFk(infoProcesamiento);
                                }
                            } else {
                                if (calNuevo.getCaeInfoProcesamientoCalificacionEstFk() != null && BooleanUtils.isNotTrue(calNuevo.getCaeInfoProcesamientoCalificacionEstFk().getIpePromocionPendiente())) {
                                    calNuevo.getCaeInfoProcesamientoCalificacionEstFk().setIpeError(null);
                                }
                            }
                        }
                        calNuevo.setCaeCalificacion(calificacion);
                    }

                }
                calificacion.setCalCalificacionesEstudiante(listCalificacionEstGrado);
            }

            //cuando estudiantePk es distinto de NULL significa que el calculo se hace para un solo estudiante
            //AL EDITAR LA NOTA DE UN SOLO ESTUDIANTE HAY QUE ASEGURAR QUE LAS DE LOS DEMAS NO SE BORREN CON ORPHANREMOVAL
            if (calificacion.getCalPk() != null && estudiantePk != null) {
                for (SgCalificacionEstudiante cal : calificacion.getCalCalificacionesEstudiante()) {

                    cal.setCaeCalificacion(calificacion);
                }

            }

            calificacion.setCalEstadoProcesamientoPromocion(EnumEstadoProcesamientoCalificacionPromocion.PROCESADO);
            calificacion.setCalInfoProcesamientoCalificacionFk(null);

            calificacion = this.guardar(calificacion, Boolean.TRUE, Boolean.FALSE);

            return calificacion;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public void guardarConError(Long cabezalPk, String stackTrace) throws GeneralException {
        try {
            if (cabezalPk != null) {
                String query = "update SgCalificacionCE set calEstadoProcesamientoPromocion= :estado where calPk= :pk";
                Query qda = em.createQuery(query)
                        .setParameter("estado", EnumEstadoProcesamientoCalificacionPromocion.PROCESADO_ERROR)
                        .setParameter("pk", cabezalPk);
                qda.executeUpdate();
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Marca que se debe actualizar la moda y promedios del cabezal Los mismos
     * son actualizados por un cron
     *
     * @param id Long
     * @throws GeneralException
     */
    public void marcarPromedioYModaDesactualizados(Long cabezalPk) throws GeneralException {
        try {
            if (cabezalPk != null) {
                String query = "update SgCalificacionCE set calPromedioModaDesactualizados = true where calPk= :pk";
                em.createQuery(query)
                        .setParameter("pk", cabezalPk)
                        .executeUpdate();
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    //Utilizado para cargar calificaciones desde SIGO
    public void cargarCalificacionesDePruebaSuficienciaPorNivelGrado(List<SgCalificacionPSNG> calificacionesSIGO) throws GeneralException {
        try {

            BusinessException be = new BusinessException();
            String respuesta = null;
            for (SgCalificacionPSNG calSIGO : calificacionesSIGO) {
                
                if (calSIGO.getEstudianteFk() == null || calSIGO.getEstudianteFk().getEstPk() == null){
                    String error = "Uno de los registros a calificar no cuenta con estudiante.";
                    respuesta += error;
                    be.addError(error);
                    continue;
                }
                if (calSIGO.getGradoFk() == null){
                    String error = "El estudiante de NIE "+calSIGO.getEstudianteFk().getEstPersona().getPerNie()+" no presenta grado.";
                    respuesta += error;
                    be.addError(error);
                    continue;
                }
                if(calSIGO.getCalificacionesEstudiante() == null || calSIGO.getCalificacionesEstudiante().isEmpty()){
                    String error = "El estudiante de NIE "+calSIGO.getEstudianteFk().getEstPersona().getPerNie()+" tiene lista de calificaciones vacía.";
                    respuesta += error;
                    be.addError(error);
                    continue;
                }
                if (calSIGO.getPlanEstudioFk()== null){
                    String error = "El estudiante de NIE "+calSIGO.getEstudianteFk().getEstPersona().getPerNie()+" no presenta plan estudio.";
                    respuesta += error;
                    be.addError(error);
                    continue;
                }
                
                
                if (calSIGO.getEstudianteFk() != null && calSIGO.getEstudianteFk().getEstPk() != null && calSIGO.getGradoFk() != null && calSIGO.getCalificacionesEstudiante() != null && calSIGO.getCalificacionesEstudiante().size() > 0) {
                    Query q = em.createNativeQuery(" \n"
                            + " select \n"
                            + "  mat.mat_pk,"
                            + "sec.sec_pk,"
                            + "sec.sec_version,"
                            + "plaest.pes_pk,"
                            + "ale.ale_pk,"
                            + "sec.sec_tipo_periodo,"
                            + "sec.sec_numero_periodo,"
                            + "rel.rea_modalidad_educativa_fk,"
                            + "rel.rea_modalidad_atencion_fk,"
                            + "rel.rea_sub_modalidad_atencion_fk," 
                            + "sed.sed_tipo_calendario_fk\n"
                            + "from \n"
                            + "  centros_educativos.sg_matriculas mat \n"
                            + "  inner join centros_educativos.sg_secciones sec on mat.mat_seccion_fk = sec.sec_pk \n"
                            + "  inner join centros_educativos.sg_servicio_educativo sered on sered.sdu_pk=sec.sec_servicio_educativo_fk \n"
                            + "  inner join centros_educativos.sg_planes_estudio plaest on plaest.pes_pk=sec.sec_plan_estudio_fk  \n"
                            + "  inner join centros_educativos.sg_anio_lectivo ale on ale.ale_pk=sec.sec_anio_lectivo_fk \n"
                            + "  inner join centros_educativos.sg_grados gra on gra.gra_pk=sered.sdu_grado_fk\n"
                            + "  inner join centros_educativos.sg_rel_mod_ed_mod_aten rel on rel.rea_pk=gra.gra_relacion_modalidad_fk\n"
                            + "  inner join centros_educativos.sg_sedes sed on sed.sed_pk=sered.sdu_sede_fk "
                            + "where \n"
                            + "  mat.mat_estudiante_fk=:estudiantePk\n"
                            + "  and \n"
                            + "  sered.sdu_grado_fk=:gradoPk\n"
                            + "  and\n"
                            + "  sec.sec_plan_estudio_fk=:planEstudioFk\n"
                            + "  and mat.mat_anulada=false\n"
                            + "  and mat.mat_retirada=false\n"
                            + " order by ale.ale_anio desc, mat.mat_pk desc \n"
                            + " limit 1 ");
                    q.setParameter("estudiantePk", calSIGO.getEstudianteFk().getEstPk());
                    q.setParameter("planEstudioFk", calSIGO.getPlanEstudioFk());
                    q.setParameter("gradoPk", calSIGO.getGradoFk());

                    List<Object[]> objs = (List<Object[]>) q.getResultList();

                    if (!objs.isEmpty()) {

                        Object[] obj = objs.get(0);
                        
                        EnumTipoPeriodoSeccion tipoPeriodo=EnumTipoPeriodoSeccion.ANUAL;
                        Integer cantidad=null;
                        if(obj[5]!=null && obj[5].toString().equals("COHORTE")){
                            tipoPeriodo=EnumTipoPeriodoSeccion.COHORTE;
                            cantidad=((Integer) obj[6]);
                        }
                        
                        FiltroRangoFecha frf = new FiltroRangoFecha();
                        frf.setModalidadEducativaPk(((BigInteger) obj[7]).longValue());
                        frf.setModalidadAtencionPk(((BigInteger) obj[8]).longValue());
                        frf.setSubModalidadAtencionPk(obj[9] != null ? ((BigInteger) obj[9]).longValue() : null);
                        frf.setPcaAnioLectivo(((BigInteger) obj[4]).longValue());
                        frf.setPcaTipoPeriodo(tipoPeriodo);                        
                        frf.setPcaNumeroPeriodo(cantidad);
                        frf.setPcaTipoCalendario(((BigInteger) obj[10]).longValue());
                        frf.setIncluirCampos(new String[]{"rfeVersion"});
                        List<SgRangoFecha> rfes = rangoFechaBean.obtenerPorFiltroConCache(frf);

                        if (rfes.isEmpty()) {
                            String error = "No existe período para los datos seleccionados para estudiante de NIE " + calSIGO.getEstudianteFk().getEstPersona().getPerNie();
                            respuesta += error;
                            be.addError(error);
                            continue;
                        }
                        SgRangoFecha rangoFecha = rfes.get(0);

                        List<Long> componetePk=new ArrayList();
                        
                        Boolean componeteVacia=Boolean.FALSE;
                        for(SgCalificacionEstudiantePSNG cpg: calSIGO.getCalificacionesEstudiante()){
                            if(cpg.getComponetePlanGradoPk()==null && !componeteVacia){
                                String error = "Existen componentes vacías para estudiante de NIE " + calSIGO.getEstudianteFk().getEstPersona().getPerNie();
                                respuesta += error;
                                be.addError(error);
                                componeteVacia=Boolean.TRUE;
                            }else{
                                componetePk.add(cpg.getComponetePlanGradoPk());
                            }
                            
                        }
                        if(componetePk.isEmpty()){
                            continue;
                        }
                        
                        SgSeccion sec = new SgSeccion(((BigInteger) obj[1]).longValue(), ((Integer) obj[2]));
                        FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
                        fpg.setCpgGradoPk(calSIGO.getGradoFk());
                        fpg.setCpgPlanEstudioPk(calSIGO.getPlanEstudioFk());
                        fpg.setCpgPkList(componetePk);
                        fpg.setIncluirCampos(new String[]{"cpgEscalaCalificacion.ecaTipoEscala", "cpgEscalaCalificacion.ecaPk",
                            "cpgEscalaCalificacion.ecaValorMinimo.calOrden",
                            "cpgEscalaCalificacion.ecaMinimo",
                            "cpgEscalaCalificacion.ecaMaximo",
                            "cpgEscalaCalificacion.ecaMinimoAprobacion",
                            "cpgPeriodosCalificacion",
                            "cpgNombrePublicable",
                            "cpgComponentePlanEstudio.cpeNombre",
                            "cpgComponentePlanEstudio.cpePk", "cpgComponentePlanEstudio.cpeTipo", "cpgComponentePlanEstudio.cpeVersion"});
                        List<SgComponentePlanGrado> componentesPlanGradoList = componentePlanGradoBean.obtenerPorFiltro(fpg);

                        if (!componentesPlanGradoList.isEmpty()) {

                            recorridoComponentes:
                            for (SgCalificacionEstudiantePSNG calEst : calSIGO.getCalificacionesEstudiante()) {

                                if (calEst.getComponetePlanGradoPk() != null && !StringUtils.isBlank(calEst.getCalificacion())) {

                                    String calificacion = calEst.getCalificacion();
                                    List<SgComponentePlanGrado> cpgList = componentesPlanGradoList.stream().filter(c -> c.getCpgPk().equals(calEst.getComponetePlanGradoPk())).collect(Collectors.toList());

                                    if (cpgList.isEmpty()) {
                                        String error = "No existe componente plan grado con pk " + calEst.getComponetePlanGradoPk() + " para el estudiante " + calSIGO.getEstudianteFk().getEstPersona().getPerNie();
                                        respuesta += error;
                                        be.addError(error);
                                        continue recorridoComponentes;
                                    }
                                    SgComponentePlanGrado cpg = cpgList.get(0);

                                    if (cpg.getCpgPeriodosCalificacion() == null || cpg.getCpgPeriodosCalificacion() != 1) {
                                        String error = "El componente" +  cpg.getCpgNombrePublicable() + " para el estudiante " + calSIGO.getEstudianteFk().getEstPersona().getPerNie() + " tiene una cantidad de periodos superior a 1";
                                        respuesta += error;
                                        be.addError(error);
                                        continue recorridoComponentes;
                                    }

                                    SgEscalaCalificacion escCalificacion = cpg.getCpgEscalaCalificacion();
                                    Double num = null;
                                    SgCalificacion cal = null;
                                    if (TipoEscalaCalificacion.NUMERICA.equals(cpg.getCpgEscalaCalificacion().getEcaTipoEscala())) {

                                        try {
                                            num = Double.parseDouble(calificacion);
                                            if ((cpg.getCpgEscalaCalificacion().getEcaMaximo() < num) || (cpg.getCpgEscalaCalificacion().getEcaMinimo() > num)) {
                                                String error = "Calificación " + calificacion + " no cumple con escala para el estudiante " + calSIGO.getEstudianteFk().getEstPersona().getPerNie() + " en componente " + cpg.getCpgComponentePlanEstudio().getCpeNombre();
                                                respuesta += error;
                                                be.addError(error);
                                                continue;
                                            }
                                        } catch (NumberFormatException e) {
                                            String error = "Calificación " + calificacion + " no cumple con escala para el estudiante " + calSIGO.getEstudianteFk().getEstPersona().getPerNie() + " en componente " + cpg.getCpgComponentePlanEstudio().getCpeNombre();
                                            respuesta += error;
                                            be.addError(error);
                                            continue;
                                        }

                                    } else {

                                        FiltroCalificacionCa fc = new FiltroCalificacionCa();
                                        fc.setCalEscalaCalificacionPk(cpg.getCpgEscalaCalificacion().getEcaPk());
                                        fc.setCalValor(calificacion);

                                        List<SgCalificacion> calList = calificacionCatalogosBean.obtenerPorFiltro(fc); //Tiene cache
                                        if (calList.isEmpty()) {
                                            String error = "Calificación " + calificacion + " no cumple con escala para el estudiante " + calSIGO.getEstudianteFk().getEstPersona().getPerNie() + " en componente " + cpg.getCpgComponentePlanEstudio().getCpeNombre();
                                            respuesta += error;
                                            be.addError(error);
                                            continue;
                                        } else {
                                            cal = calList.get(0);
                                        }

                                    }

                                    FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                                    fce.setCaeEstudiantePk(calSIGO.getEstudianteFk().getEstPk());
                                    fce.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{EnumTiposPeriodosCalificaciones.ORD, EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.APR}));
                                    fce.setAnioLectivoPk(((BigInteger) obj[4]).longValue());
                                    fce.setCaeComponentePlanEstudio(cpg.getCpgComponentePlanEstudio().getCpePk());
                                    fce.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                                        "caeCalificacion.calPk",
                                        "caeCalificacion.calVersion",
                                        "caeCalificacion.calTipoPeriodoCalificacion",
                                        "caeEstudiante.estPk",
                                        "caeEstudiante.estVersion",
                                        "caeResolucion",
                                        "caePromovidoCalificacion",
                                        "caeCalificacionNumericaEstudiante",
                                        "caeCalificacionConceptualEstudiante.calPk",
                                        "caeCalificacionConceptualEstudiante.calVersion",
                                        "caeCalificacion.calRangoFecha.rfePk",
                                        "caeCalificacion.calRangoFecha.rfeVersion",
                                        "caeObservacion",
                                        "caeFechaRealizado",
                                        "caeVersion",
                                        "caeProcesoDeCreacion"});
                                    List<SgCalificacionEstudiante> listCalificaciones = calificacionEstudianteBean.obtenerPorFiltro(fce);
                                    List<SgCalificacionEstudiante> listCalEs = listCalificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD) 
                                    && c.getCaeCalificacion().getCalRangoFecha().getRfePk().equals(rangoFecha.getRfePk())).collect(Collectors.toList());
                                    listCalificaciones = listCalificaciones.stream().filter(c -> !c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)).collect(Collectors.toList());

                                    FiltroCalificacion fc = new FiltroCalificacion();
                                    fc.setSecPk(sec.getSecPk());
                                    fc.setSecAnioLectivoFk(((BigInteger) obj[4]).longValue());
                                    fc.setCalComponentePlanEstudio(cpg.getCpgComponentePlanEstudio().getCpePk());
                                    fc.setCalTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{EnumTiposPeriodosCalificaciones.ORD, EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.APR}));
                                    fc.setIncluirCampos(new String[]{"calPk", "calComponentePlanEstudio.cpePk",
                                        "calComponentePlanEstudio.cpeTipo",
                                        "calComponentePlanEstudio.cpeVersion",
                                        "calSeccion.secPk",
                                        "calSeccion.secVersion",
                                        "calFecha",
                                        "calRangoFecha.rfePk",
                                        "calRangoFecha.rfeVersion",
                                        "calTipoPeriodoCalificacion",
                                        "calRangoFecha.rfePk",
                                        "calRangoFecha.rfeVersion",
                                        "calVersion"});
                                    CalificacionDAO codDAOCal = new CalificacionDAO(em, null);
                                    List<SgCalificacionCE> listCalAux = codDAOCal.obtenerPorFiltro(fc, null);

                                    SgCalificacionEstudiante calEstudiante = new SgCalificacionEstudiante();
                                    SgCalificacionCE calificacionPaes = new SgCalificacionCE();

                                    SgCalificacionEstudiante calEstudianteNOTIN = new SgCalificacionEstudiante();
                                    SgCalificacionCE calificacionNOTIN = new SgCalificacionCE();

                                    SgCalificacionEstudiante calEstudianteAPR = new SgCalificacionEstudiante();
                                    SgCalificacionCE calificacionAPR = new SgCalificacionCE();

                                    SgEstudiante est = calSIGO.getEstudianteFk();

                                    if (!listCalEs.isEmpty()) {
                                        calEstudiante = listCalEs.get(0);

                                    } else {

                                        List<SgCalificacionCE> listCal = listCalAux.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)&& c.getCalRangoFecha().getRfePk().equals(rangoFecha.getRfePk())).collect(Collectors.toList());
                                        if (!listCal.isEmpty()) {
                                            calificacionPaes = listCal.get(0);

                                        } else {
                                            calificacionPaes.setCalComponentePlanEstudio(cpg.getCpgComponentePlanEstudio());
                                            calificacionPaes.setCalFecha(calEst.getFechaCalificacion());
                                            calificacionPaes.setCalSeccion(sec);
                                            calificacionPaes.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);
                                            calificacionPaes.setCalRangoFecha(rangoFecha);
                                        }

                                        calEstudiante.setCaeEstudiante(est);
                                        calEstudiante.setCaeCalificacion(calificacionPaes);

                                    }

                                    //CREAR O EDITAR NOTA INSTITUCIONAL                   
                                    List<SgCalificacionEstudiante> calEstNOTIN = listCalificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());
                                    if (!calEstNOTIN.isEmpty()) {
                                        calEstudianteNOTIN = calEstNOTIN.get(0);
                                    } else {
                                        //Nota institucional
                                        List<SgCalificacionCE> listCalNotin = listCalAux.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());
                                        if (!listCalNotin.isEmpty()) {
                                            calificacionNOTIN = listCalNotin.get(0);
                                        } else {
                                            calificacionNOTIN.setCalComponentePlanEstudio(cpg.getCpgComponentePlanEstudio());
                                            calificacionNOTIN.setCalFecha(LocalDate.now());
                                            calificacionNOTIN.setCalSeccion(sec);
                                            calificacionNOTIN.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.NOTIN);
                                        }

                                        calEstudianteNOTIN.setCaeEstudiante(est);
                                        calEstudianteNOTIN.setCaeCalificacion(calificacionNOTIN);
                                    }

                                    //CREAR O EDITAR APROBACION               
                                    List<SgCalificacionEstudiante> calEstAPR = listCalificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.APR)).collect(Collectors.toList());
                                    if (!calEstAPR.isEmpty()) {
                                        calEstudianteAPR = calEstAPR.get(0);
                                    } else {
                                        //Aprobacion
                                        List<SgCalificacionCE> listCalApr = listCalAux.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.APR)).collect(Collectors.toList());
                                        if (!listCalApr.isEmpty()) {
                                            calificacionAPR = listCalApr.get(0);
                                        } else {
                                            calificacionAPR.setCalComponentePlanEstudio(cpg.getCpgComponentePlanEstudio());
                                            calificacionAPR.setCalFecha(LocalDate.now());
                                            calificacionAPR.setCalSeccion(sec);
                                            calificacionAPR.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                                        }

                                        calEstudianteAPR.setCaeEstudiante(est);
                                        calEstudianteAPR.setCaeCalificacion(calificacionAPR);
                                    }

                                    //ASIGNAR VALOR CALIFICACION
                                    if (num != null) {
                                        calEstudiante.setCaeCalificacionNumericaEstudiante(calificacion);

                                        calEstudianteNOTIN.setCaeCalificacionNumericaEstudiante(calificacion);

                                        calEstudianteAPR.setCaeCalificacionNumericaEstudiante(calificacion);
                                        calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.PENDIENTE);
                                        if (escCalificacion.getEcaMinimoAprobacion().compareTo(num) > 0) {
                                            calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.NO_APROBADO);
                                        } else {
                                            calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.APROBADO);
                                        }
                                    } else {
                                        if (cal != null) {
                                            calEstudiante.setCaeCalificacionConceptualEstudiante(cal);

                                            calEstudianteNOTIN.setCaeCalificacionConceptualEstudiante(cal);

                                            calEstudianteAPR.setCaeCalificacionConceptualEstudiante(cal);
                                            calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.PENDIENTE);
                                            if (escCalificacion.getEcaValorMinimo() != null && escCalificacion.getEcaValorMinimo().getCalOrden() != null) {
                                                if (cal.getCalOrden() >= escCalificacion.getEcaValorMinimo().getCalOrden()) {
                                                    calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.APROBADO);
                                                } else {
                                                    calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.NO_APROBADO);
                                                }
                                            }
                                        } else {
                                            String error = "Calificación " + calificacion + " no cumple con escala para el estudiante " + calSIGO.getEstudianteFk().getEstPersona().getPerNie() + " en componente " + cpg.getCpgComponentePlanEstudio().getCpeNombre();
                                            respuesta += error;
                                            be.addError(error);
                                            continue;
                                        }
                                    }

                                    //GUARDAR
                                    //Si existe error para cualquiera de las notas no se manda a guardar ninguna
                                    if (StringUtils.isBlank(respuesta)) {
                                        if (calEstudiante.getCaePk() != null || calEstudiante.getCaeCalificacion().getCalPk() != null) {
                                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                            calEstudiante = codDAO.guardar(calEstudiante, null);
                                            this.marcarPromedioYModaDesactualizados(calEstudiante.getCaeCalificacion().getCalPk());
                                        } else {
                                            List<SgCalificacionEstudiante> calEstList = new ArrayList();
                                            calEstList.add(calEstudiante);
                                            calificacionPaes.setCalCalificacionesEstudiante(calEstList);

                                            CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                                            codDAO.guardar(calificacionPaes, null);
                                            this.marcarPromedioYModaDesactualizados(calificacionPaes.getCalPk());
                                        }
                                        //Guardar Nota institucional
                                        if (calEstudianteNOTIN.getCaePk() != null || calEstudianteNOTIN.getCaeCalificacion().getCalPk() != null) {
                                            //cabezal existente o calificacion existente
                                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                            codDAO.guardar(calEstudianteNOTIN, null);
                                            this.marcarPromedioYModaDesactualizados(calEstudianteNOTIN.getCaeCalificacion().getCalPk());
                                        } else {
                                            //Si ambos son null, entonces cabezal no existe en la base. Se crea
                                            List<SgCalificacionEstudiante> calEstList = new ArrayList();
                                            calEstList.add(calEstudianteNOTIN);
                                            calificacionNOTIN.setCalCalificacionesEstudiante(calEstList);

                                            CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                                            codDAO.guardar(calificacionNOTIN, null);
                                            this.marcarPromedioYModaDesactualizados(calificacionNOTIN.getCalPk());
                                        }

                                        //Guardar Aprobacion
                                        if (calEstudianteAPR.getCaePk() != null || calEstudianteAPR.getCaeCalificacion().getCalPk() != null) {
                                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                            codDAO.guardar(calEstudianteAPR, null);
                                            this.marcarPromedioYModaDesactualizados(calEstudianteAPR.getCaeCalificacion().getCalPk());
                                        } else {
                                            List<SgCalificacionEstudiante> calEstList = new ArrayList();
                                            calEstList.add(calEstudianteAPR);
                                            calificacionAPR.setCalCalificacionesEstudiante(calEstList);

                                            CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                                            codDAO.guardar(calificacionAPR, null);
                                            this.marcarPromedioYModaDesactualizados(calificacionAPR.getCalPk());
                                        }
                                        em.flush();
                                    }
                                } else {
                                    if (!StringUtils.isBlank(calEst.getCalificacion())) {
                                        String error = "La calificación es vacía para el componente de pk " + calEst.getComponetePlanGradoPk() + " para el estudiante " + calSIGO.getEstudianteFk().getEstPersona().getPerNie();
                                        respuesta += error;
                                        be.addError(error);
                                    } else {
                                        String error = "Uno de los componentes del estudiante de NIE  " + calSIGO.getEstudianteFk().getEstPersona().getPerNie() + " es vacío.";
                                        respuesta += error;
                                        be.addError(error);
                                    }

                                }
                            }

                        } else {
                            String error = "No existe componente plan grado especificados para el estudiante de NIE " + calSIGO.getEstudianteFk().getEstPersona().getPerNie();
                            respuesta += error;
                            be.addError(error);
                        }
                    } else {
                        String error = "No existe matrículas para el estudiante de NIE " + calSIGO.getEstudianteFk().getEstPersona().getPerNie();
                        respuesta += error;
                        be.addError(error);
                    }
                }
            }
            if (be.getErrores().size() > 0) {
                throw be;
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
