/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgUltimaCalificacionComponenteEstudiante;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.filtros.FiltroComponentePlanGrado;
import sv.gob.mined.siges.filtros.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.CalificacionEstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CalificacionEstudianteDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgInfoProcesamientoCalificacionEst;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class CalificacionEstudianteBean {

    private static final Logger LOGGER = Logger.getLogger(CalificacionEstudianteBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private CalificacionBean calificacionBean;

    @Inject
    private ComponentePlanGradoBean componentePlanGradoBean;

    @Inject
    private EstudianteBean estudianteBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private EscolaridadEstudianteBean escolaridadEstudianteBean;

    @Inject
    private HorarioEscolarBean horarioEscolarBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalificacionEstudiante
     * @throws GeneralException
     */
    public SgCalificacionEstudiante obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                SgCalificacionEstudiante calEst = codDAO.obtenerPorId(id, null);
                return calEst;
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
                CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param cae SgCalificacionEstudiante
     * @return SgCalificacionEstudiante
     * @throws GeneralException
     */
    public SgCalificacionEstudiante guardar(SgCalificacionEstudiante cae, Boolean validarUsuarioDictaComponenteEnSeccion) throws GeneralException {
        try {
            if (cae != null) {
                if (CalificacionEstudianteValidacionNegocio.validar(cae, null)) {
                    CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);

//TODO
//                    SgCalificacionEstudiante no tiene seguridad para mejorar performance. Se consulta el estudiante
//                    Comentado por temas de performance. Verificar luego
//                    if (!estudianteBean.objetoExistePorId(cae.getCaeEstudiante().getEstPk(), Boolean.TRUE)) {
//                        throw new DataSecurityException();
//                    }
                    Boolean guardar = Boolean.TRUE;
                    if (cae.getCaePk() != null) {
                        FiltroCalificacionEstudiante fil = new FiltroCalificacionEstudiante();
                        fil.setCalificacionEstudiantePk(cae.getCaePk());
                        fil.setIncluirCampos(new String[]{"caeCalificacionNumericaEstudiante",
                            "caeCalificacionConceptualEstudiante.calValor",
                            "caeCalificacion.calSeccion.secPk",
                            "caeCalificacion.calComponentePlanEstudio.cpePk"});
                        List<SgCalificacionEstudiante> cals = this.obtenerPorFiltro(fil);
                        if (!cals.isEmpty()) {
                            SgCalificacionEstudiante c = cals.get(0);

                            if (c.getCaeCalificacionValor() != null && c.getCaeCalificacionValor().equals(cae.getCaeCalificacionValor())) {
                                guardar = Boolean.FALSE;
                            } else {

                                //Guardar = true
                                if (BooleanUtils.isTrue(validarUsuarioDictaComponenteEnSeccion)) {
                                    JsonWebToken jwt = Lookup.obtenerJWT();
                                    if (!jwt.getGroups().contains(ConstantesOperaciones.PERMITE_CALIFICAR_SIN_HORARIO_ESCOLAR_ASIGNADO)
                                            && !horarioEscolarBean.usuarioDictaComponenteEnSeccion(
                                                    jwt.getSubject(),
                                                    c.getCaeCalificacion().getCalSeccion().getSecPk(),
                                                    c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk())) {
                                        BusinessException be = new BusinessException();
                                        be.addError(Errores.ERROR_USUARIO_NO_TIENE_HORARIO_ESCOLAR_ASIGNADO_PARA_SECCION_COMPONENTE);
                                        throw be;
                                    }
                                }

                            }
                        }
                    }

                    //Solo guardar y recalcular si cambió la nota
                    if (guardar) {
                        cae = codDAO.guardar(cae, null);

                        this.calificacionBean.marcarPromedioYModaDesactualizados(cae.getCaeCalificacion().getCalPk());

                        if (cae.getCaeCalificacion().getCalComponentePlanEstudio().getCpeTipo().equals(TipoComponentePlanEstudio.PRU)
                                && (cae.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.PAES)
                                || cae.getCaeCalificacion().getCalRangoFecha().getRfePeriodoCalificacion().getPcaNumero().equals(1))) {

                            FiltroCalificacion fc = new FiltroCalificacion();
                            fc.setCalPk(cae.getCaeCalificacion().getCalPk());
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
                                "calSeccion.secAnioLectivo.aleAnio",
                                "calSeccion.secAnioLectivo.alePk",
                                "calSeccion.secAnioLectivo.aleVersion",
                                "calSeccion.secPlanEstudio.pesPk",
                                "calSeccion.secPlanEstudio.pesNombre",
                                "calSeccion.secPlanEstudio.pesVersion",
                                "calSeccion.secServicioEducativo.sduSede.sedNombre",
                                "calSeccion.secServicioEducativo.sduSede.sedCodigo",
                                "calSeccion.secServicioEducativo.sduSede.sedPk",
                                "calSeccion.secServicioEducativo.sduSede.sedVersion",
                                "calSeccion.secServicioEducativo.sduSede.sedTipo",
                                "calSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
                                "calSeccion.secServicioEducativo.sduGrado.graPk",
                                "calSeccion.secServicioEducativo.sduGrado.graNombre",
                                "calSeccion.secServicioEducativo.sduOpcion.opcNombre",
                                "calSeccion.secServicioEducativo.sduOpcion.opcPk",
                                "calSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                                "calSeccion.secServicioEducativo.sduProgramaEducativo.pedPk",
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
                                "calRangoFecha.rfeVersion",
                                "calRangoFecha.rfePeriodoCalificacion.pcaPk",
                                "calRangoFecha.rfePeriodoCalificacion.pcaNombre",
                                "calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                                "calRangoFecha.rfePeriodoCalificacion.pcaPermiteCalificarSinNie",
                                "calRangoFecha.rfePeriodoCalificacion.pcaVersion",
                                "calRangoFecha.rfePeriodoCalificacion.pcaEsPrueba"
                            });

                            List<SgCalificacionCE> cabezales = this.calificacionBean.obtenerPorFiltro(fc);

                            if (!cabezales.isEmpty()) {
                                if (cabezales.get(0).getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)
                                        && cabezales.get(0).getCalComponentePlanEstudio().getCpeTipo().equals(TipoComponentePlanEstudio.PRU)
                                        && cabezales.get(0).getCalRangoFecha().getRfePeriodoCalificacion().getPcaNumero().equals(1)) {
                                    calificacionBean.guardarNotaInstitucionalAprobacionDePrueba(cabezales.get(0));
                                }
                            }
                        }

                    }

                    return cae;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cae;
    }

    public List<SgUltimaCalificacionComponenteEstudiante> obtenerUltimasCalificacionesEstudiante(Integer anio, Long pkEstudiante) {

        List<SgUltimaCalificacionComponenteEstudiante> ultimasCalPorComponente = new ArrayList<>();

        FiltroCalificacionEstudiante filtro = new FiltroCalificacionEstudiante();
        filtro.setAnioLectivo(anio);
        filtro.setEstudiantePk(pkEstudiante);
        filtro.setIncluirCampos(new String[]{"caeCalificacion.calPk",
            "caeCalificacion.calComponentePlanEstudio.cpeNombre",
            "caeCalificacion.calComponentePlanEstudio.cpePk",
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
            "caeResolucion",
            "caeFechaRealizado",
            "caeEstudiante.estPk",
            "caeCalificacion.calSeccion.secServicioEducativo.sduGrado.graPk",
            "caeCalificacion.calSeccion.secPlanEstudio.pesPk",
            "caeCalificacion.calSeccion.secPk",});
        filtro.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
            EnumTiposPeriodosCalificaciones.ORD,
            EnumTiposPeriodosCalificaciones.EXORD,
            EnumTiposPeriodosCalificaciones.NOTIN,
            EnumTiposPeriodosCalificaciones.APR}));

        List<SgCalificacionEstudiante> calificaciones = this.obtenerPorFiltro(filtro);

        HashMap<SgComponentePlanEstudio, List<SgCalificacionEstudiante>> componenteCalificaciones = new HashMap<>();
        for (SgCalificacionEstudiante cal : calificaciones) {
            componenteCalificaciones.computeIfAbsent(cal.getCaeCalificacion().getCalComponentePlanEstudio(), s -> new ArrayList<>()).add(cal);
        }

        SgCalificacionEstudiante ultima = null;

        for (SgComponentePlanEstudio componentePlan : componenteCalificaciones.keySet()) {

            ultima = null;

            List<SgCalificacionEstudiante> calificOrd = new ArrayList<>();
            SgCalificacionEstudiante calificNotin = null;
            List<SgCalificacionEstudiante> calificExord = new ArrayList<>();
            SgCalificacionEstudiante calificApr = null;

            for (SgCalificacionEstudiante c : componenteCalificaciones.get(componentePlan)) {
                EnumTiposPeriodosCalificaciones tipoPeriodo = c.getCaeCalificacion().getCalTipoPeriodoCalificacion();
                if (EnumTiposPeriodosCalificaciones.ORD.equals(tipoPeriodo)) {
                    calificOrd.add(c);
                } else if (EnumTiposPeriodosCalificaciones.NOTIN.equals(tipoPeriodo)) {
                    calificNotin = c;
                } else if (EnumTiposPeriodosCalificaciones.EXORD.equals(tipoPeriodo)) {
                    calificExord.add(c);
                } else if (EnumTiposPeriodosCalificaciones.APR.equals(tipoPeriodo)) {
                    calificApr = c;
                }
            }

            if (calificApr != null) {
                ultima = calificApr;
            } else if (!calificExord.isEmpty()) {

                SgCalificacionEstudiante[] datosFijos = new SgCalificacionEstudiante[4];
                Integer[] datosFijosUltimoNumero = new Integer[4]; //Pueden haber varias PP, PPS, etc. Hay que quedarse con la última.

                for (SgCalificacionEstudiante calExord : calificExord) {

                    EnumCalendarioEscolar calendarioEscolar = calExord.getCaeCalificacion().getCalTipoCalendarioEscolar();
                    if (calendarioEscolar != null) {
                        switch (calendarioEscolar) {
                            case PREC:
                                if (datosFijosUltimoNumero[0] == null || datosFijosUltimoNumero[0] < calExord.getCaeCalificacion().getCalNumero()) {  //Nos quedamos con la última
                                    datosFijos[0] = calExord;
                                    datosFijosUltimoNumero[0] = calExord.getCaeCalificacion().getCalNumero();
                                }
                                break;
                            case PRECPS:
                                if (datosFijosUltimoNumero[1] == null || datosFijosUltimoNumero[1] < calExord.getCaeCalificacion().getCalNumero()) {
                                    datosFijos[1] = calExord;
                                    datosFijosUltimoNumero[1] = calExord.getCaeCalificacion().getCalNumero();
                                }
                                break;
                            case SREC:
                                if (datosFijosUltimoNumero[2] == null || datosFijosUltimoNumero[2] < calExord.getCaeCalificacion().getCalNumero()) {
                                    datosFijos[2] = calExord;
                                    datosFijosUltimoNumero[2] = calExord.getCaeCalificacion().getCalNumero();
                                }
                                break;
                            case SRECPS:
                                if (datosFijosUltimoNumero[3] == null || datosFijosUltimoNumero[3] < calExord.getCaeCalificacion().getCalNumero()) {
                                    datosFijos[3] = calExord;
                                    datosFijosUltimoNumero[3] = calExord.getCaeCalificacion().getCalNumero();
                                }
                                break;
                        }
                    }
                }

                if (datosFijosUltimoNumero[3] != null) {
                    ultima = datosFijos[3];
                } else if (datosFijosUltimoNumero[2] != null) {
                    ultima = datosFijos[2];
                } else if (datosFijosUltimoNumero[1] != null) {
                    ultima = datosFijos[1];
                } else if (datosFijosUltimoNumero[0] != null) {
                    ultima = datosFijos[0];
                }

            } else if (calificNotin != null) {
                ultima = calificNotin;
            } else if (!calificOrd.isEmpty()) {

                for (SgCalificacionEstudiante calOrd : calificOrd) {
                    if (ultima == null || calOrd.getCaeCalificacion().getCalRangoFecha().getRfeFechaHasta().isAfter(ultima.getCaeCalificacion().getCalRangoFecha().getRfeFechaHasta())) {
                        ultima = calOrd;
                    }
                }

            }

            SgUltimaCalificacionComponenteEstudiante ultCalificacionComponente = new SgUltimaCalificacionComponenteEstudiante();
            ultCalificacionComponente.setUccComponentePlanEstudio(componentePlan);
            if (ultima != null) {
                ultCalificacionComponente.setUccFechaRealizado(ultima.getCaeFechaRealizado());
                ultCalificacionComponente.setUccValor(ultima.getCaeCalificacionValor());
            }
            ultimasCalPorComponente.add(ultCalificacionComponente);

        }

        //Cargar escalas
        if (!ultimasCalPorComponente.isEmpty() && ultima != null) {

            //Todas las calificaciones tienen la misma sección, grado y plan estudio. Agarramos la calificación que está en la variable "ultima"
            FiltroComponentePlanGrado filtroCPG = new FiltroComponentePlanGrado();
            filtroCPG.setCpgPlanEstudioPk(ultima.getCaeCalificacion().getCalSeccion().getSecPlanEstudio().getPesPk());
            filtroCPG.setCpgGradoPk(ultima.getCaeCalificacion().getCalSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
            filtroCPG.setCpgAgregandoSeccionExclusiva(ultima.getCaeCalificacion().getCalSeccion().getSecPk());
            filtroCPG.setIncluirCampos(new String[]{"cpgComponentePlanEstudio.cpePk", "cpgEscalaCalificacion.ecaPk", "cpgEscalaCalificacion.ecaMaximo", "cpgEscalaCalificacion.ecaMinimoAprobacion"});
            List<SgComponentePlanGrado> cpgs = componentePlanGradoBean.obtenerPorFiltro(filtroCPG);

            //Agrupar componente plan grado por componente plan estudio
            HashMap<SgComponentePlanEstudio, List<SgComponentePlanGrado>> comPlanEstudioGrado = new HashMap<>();
            for (SgComponentePlanGrado cpg : cpgs) {
                comPlanEstudioGrado.computeIfAbsent(cpg.getCpgComponentePlanEstudio(), s -> new ArrayList<>()).add(cpg);
            }
            for (SgUltimaCalificacionComponenteEstudiante ucc : ultimasCalPorComponente) {
                List<SgComponentePlanGrado> componentes = comPlanEstudioGrado.get(ucc.getUccComponentePlanEstudio());
                if (componentes != null && !componentes.isEmpty()) {
                    SgEscalaCalificacion escala = componentes.get(0).getCpgEscalaCalificacion();
                    ucc.setUccValorMaximo(escala.getEcaMaximo());
                    ucc.setUccValorMinimoAprobacion(escala.getEcaMinimoAprobacion());
                }
            }
        }
        return ultimasCalPorComponente;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCalificacionEstudiante filtro) throws GeneralException {
        try {
            CalificacionEstudianteDAO dao = new CalificacionEstudianteDAO(em);
            return dao.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalificacionEstudiante>
     * @throws GeneralException
     */
    public List<SgCalificacionEstudiante> obtenerPorFiltro(FiltroCalificacionEstudiante filtro) throws GeneralException {
        try {
            CalificacionEstudianteDAO dao = new CalificacionEstudianteDAO(em);
            List<SgCalificacionEstudiante> calEst = dao.obtenerPorFiltro(filtro);
            return calEst;
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
                CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    //TODO: revisar
    public void guardarCalificacionesEstudiante(List<SgCalificacionEstudiante> cae) throws GeneralException {
        if (!cae.isEmpty()) {
            SgCalificacionEstudiante calEst = cae.get(0);

            //Solo hacer consulta si hay calificaciones estudiante con pk null, sino no se usa
            FiltroCalificacion fc = new FiltroCalificacion();
            //TODO: verificar filtros vienen seteados
            fc.setSecPk(calEst.getCaeEstudiante().getEstUltimaMatricula().getMatSeccion().getSecPk());
            fc.setCalTipoCalendarioEscolar(calEst.getCaeCalificacion().getCalTipoCalendarioEscolar());
            fc.setCalTipoPeriodoCalificacion(calEst.getCaeCalificacion().getCalTipoPeriodoCalificacion());
            fc.setCalNumero(calEst.getCaeCalificacion().getCalNumero());
            fc.setCalRangoFecha(calEst.getCaeCalificacion().getCalRangoFecha() != null ? calEst.getCaeCalificacion().getCalRangoFecha().getRfePk() : null);
            fc.setIncluirCampos(new String[]{"calPk", "calComponentePlanEstudio.cpePk",
                "calComponentePlanEstudio.cpeTipo",
                "calComponentePlanEstudio.cpeVersion",
                "calFecha",
                "calTipoPeriodoCalificacion",
                "calTipoCalendarioEscolar",
                "calNumero",
                "calRangoFecha.rfePk",
                "calRangoFecha.rfeVersion",
                "calRangoFecha.rfePeriodoCalificacion.pcaVersion",
                "calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                "calSeccion.secPk",
                "calSeccion.secVersion",
                "calVersion"});
            List<SgCalificacionCE> calList = calificacionBean.obtenerPorFiltro(fc);
            for (SgCalificacionEstudiante calEstIter : cae) {
                if (calEstIter.getCaePk() != null) {
                    if (StringUtils.isBlank(calEstIter.getCaeCalificacionNumericaEstudiante()) && calEstIter.getCaeCalificacionConceptualEstudiante() == null) {
                        eliminar(calEstIter.getCaePk());
                    } else {
                        guardar(calEstIter, Boolean.TRUE);
                    }
                } else {
                    //Nueva calificacion estudiante
                    List<SgCalificacionCE> calListDeComp = calList.stream().filter(x -> x.getCalComponentePlanEstudio().getCpePk().equals(calEstIter.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk())).collect(Collectors.toList());
                    if (!calListDeComp.isEmpty()) {
                        //Cabezal ya existe. Puede haber sido creado para otro estudiante. Lo reemplazo
                        calEstIter.setCaeCalificacion(calListDeComp.get(0));
                        guardar(calEstIter, Boolean.TRUE);
                    } else {
                        //Cabezal no existe en bd. Agrego la calificación y lo guardo
                        calEstIter.getCaeCalificacion().setCalFecha(LocalDate.now());
                        calEstIter.getCaeCalificacion().getCalCalificacionesEstudiante().add(calEstIter);
                        SgCalificacionCE ca = calificacionBean.guardar(calEstIter.getCaeCalificacion(), Boolean.FALSE, Boolean.TRUE);
                        calEstIter.setCaeCalificacion(ca);
                    }
                }
            }
        }
    }

    public SgCalificacionEstudiante convertirPromocionAPendiente(SgCalificacionEstudiante calEst) throws GeneralException {
        try {
            if (calEst.getCaePk() != null) {
                FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                fce.setCalificacionEstudiantePk(calEst.getCaePk());                
                List<SgCalificacionEstudiante> calEstList = this.obtenerPorFiltro(fce);

                if (!calEstList.isEmpty()) {
                    SgCalificacionEstudiante cal = calEstList.get(0);

                    if (!cal.getCaeEstudiante().getEstUltimaMatricula().getMatSeccion().getSecPk().equals(cal.getCaeCalificacion().getCalSeccion().getSecPk())) {
                        BusinessException ge = new BusinessException();
                        ge.addError(Errores.ERROR_MATRICULA_ESTUDIANTE_NO_PERTENECE_SECCION);
                        throw ge;
                    }

                    if (!cal.getCaeVersion().equals(calEst.getCaeVersion())) {
                        BusinessException ge = new BusinessException();
                        ge.addError(Errores.ERROR_OPTIMISTIC_LOCK);
                        throw ge;
                    }

                    String err = "<b>El estudiante fue marcado como pendiente.</b><br/>";
                    if (cal.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.GRA)) {
                        if (cal.getCaeInfoProcesamientoCalificacionEstFk() == null) {
                            matriculaAbiertaBorraEscolaridad(cal);
                            SgInfoProcesamientoCalificacionEst infoProces = new SgInfoProcesamientoCalificacionEst();
                            infoProces.setIpePromocionPendiente(Boolean.TRUE);
                            infoProces.setIpeError(err);
                            cal.setCaeInfoProcesamientoCalificacionEstFk(infoProces);
                            cal.setCaePromovidoCalificacion(EnumPromovidoCalificacion.PENDIENTE);

                        } else {

                            if (BooleanUtils.isTrue(cal.getCaeInfoProcesamientoCalificacionEstFk().getIpePromocionPendiente())) {
                                cal.getCaeInfoProcesamientoCalificacionEstFk().setIpePromocionPendiente(Boolean.FALSE);
                                cal.getCaeInfoProcesamientoCalificacionEstFk().setIpeError(null);
                            } else {
                                matriculaAbiertaBorraEscolaridad(cal);
                                cal.getCaeInfoProcesamientoCalificacionEstFk().setIpePromocionPendiente(Boolean.TRUE);
                                cal.setCaePromovidoCalificacion(EnumPromovidoCalificacion.PENDIENTE);
                                cal.getCaeInfoProcesamientoCalificacionEstFk().setIpeError(err);

                            }
                        }
                        
                        
                        em.createNativeQuery("UPDATE centros_educativos.sg_calificaciones SET cal_todas_promociones_grado_realizadas = :valor where cal_pk = :calPk")
                                .setParameter("valor", false)
                                .setParameter("calPk", cal.getCaeCalificacion().getCalPk())
                                .executeUpdate();
                        
                        em.createNativeQuery("UPDATE centros_educativos.sg_secciones SET sec_todas_promociones_grado_realizadas = :valor where sec_pk = :secPk")
                                .setParameter("valor", false)
                                .setParameter("secPk", cal.getCaeCalificacion().getCalSeccion().getSecPk())
                                .executeUpdate();
                        
                        
                        
                        CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                        cal = codDAO.guardar(cal, null);
                        return cal;
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return calEst;
    }

    //Este método se llama desde convertirPromocionAPendiente
    //Cuando en promociones se cierra la sección, si a un estudiante con escolaridad generada y matrícula cerrada
    //se lo pasa a pendiente hay que re-abrir la sección y borrar la escolaridad
    private void matriculaAbiertaBorraEscolaridad(SgCalificacionEstudiante calEst) {
        if (calEst.getCaeCalificacion().getCalPk() != null) {
            if (calEst.getCaeEstudiante().getEstUltimaMatricula().getMatSeccion().getSecPk().equals(calEst.getCaeCalificacion().getCalSeccion().getSecPk())) {
                FiltroMatricula fm = new FiltroMatricula();
                fm.setMatEstudiantePk(calEst.getCaeEstudiante().getEstPk());
                fm.setSecPk(calEst.getCaeCalificacion().getCalSeccion().getSecPk());      
                fm.setMaxResults(1L);
                fm.setSecurityOperation(null);
                List<SgMatricula> matList = matriculaBean.obtenerPorFiltro(fm);

                if (!matList.isEmpty()) {
                    SgMatricula mat = matList.get(0);
                    mat.setMatEstado(EnumMatriculaEstado.ABIERTO);
                    mat.setMatFechaHasta(null);
                    mat.setMatPromocionGrado(null);
                    mat.setMatCerradoPorCierreAnio(null);
                    mat = matriculaBean.guardar(mat);
                }
                FiltroEscolaridadEstudiante fee = new FiltroEscolaridadEstudiante();
                fee.setEstudiantePk(calEst.getCaeEstudiante().getEstPk());
                fee.setMatriculaPk(calEst.getCaeEstudiante().getEstUltimaMatricula().getMatPk());
                fee.setIncluirCampos(new String[]{"escPk"});
                List<SgEscolaridadEstudiante> escEstudiante = escolaridadEstudianteBean.obtenerPorFiltro(fee);

                if (!escEstudiante.isEmpty()) {
                    em.createQuery("DELETE FROM  SgEscolaridadEstudiante where escPk=:ids")
                            .setParameter("ids", escEstudiante.get(0).getEscPk())
                            .executeUpdate();
                }

            }
        }
    }

}
