/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.filtros.FiltroConfirmacionPromocion;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.SeccionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SeccionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAnioLectivo;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionPromocion;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;

@Stateless
@Traced
public class SeccionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private EstudianteBean estudiantesBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private CalificacionBean calificacionBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ConfirmacionPromocionBean confirmacionPromocionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSeccion
     * @throws GeneralException
     */
    public SgSeccion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSeccion> codDAO = new CodigueraDAO<>(em, SgSeccion.class);
                SgSeccion sec = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SECCION);
                if (sec.getSecServicioEducativo().getSduSede().getSedJornadasLaborales() != null) {
                    sec.getSecServicioEducativo().getSduSede().getSedJornadasLaborales().size();
                }
                return sec;
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
                CodigueraDAO<SgSeccion> codDAO = new CodigueraDAO<>(em, SgSeccion.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SECCION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgSeccion
     * @return SgSeccion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgSeccion guardar(SgSeccion entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SeccionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgSeccion> codDAO = new CodigueraDAO<>(em, SgSeccion.class);

                    //No se debe permitir cambiar plan de estudio, anio lectivo, si la sección tiene estudiantes matriculados
                    if (entity.getSecPk() != null) {                                          
                        FiltroSeccion filSec = new FiltroSeccion();
                        filSec.setSecPk(entity.getSecPk());
                        filSec.setIncluirCampos(new String[]{"secPlanEstudio.pesPk", "secAnioLectivo.alePk", "secEstado"});
                        List<SgSeccion> secActual = this.obtenerPorFiltro(filSec);
                        SgSeccion actual = secActual.get(0);
                        
                        if (((actual.getSecPlanEstudio() != null) && (!actual.getSecPlanEstudio().equals(entity.getSecPlanEstudio())))
                                || !actual.getSecAnioLectivo().equals(entity.getSecAnioLectivo())) {
                            FiltroEstudiante filEst = new FiltroEstudiante();
                            filEst.setSecPk(entity.getSecPk());
                            if (estudiantesBean.obtenerTotalPorFiltro(filEst) > 0L) {
                                BusinessException be = new BusinessException();
                                if (!actual.getSecPlanEstudio().equals(entity.getSecPlanEstudio())) {
                                    be.addError(Errores.ERROR_CAMBIAR_PLAN_ESTUDIO_SECCION_CON_ESTUDIANTES);
                                }
                                if (!actual.getSecAnioLectivo().equals(entity.getSecAnioLectivo())) {
                                    be.addError(Errores.ERROR_CAMBIAR_ANIO_LECTIVO_SECCION_CON_ESTUDIANTES);
                                }
                                throw be;
                            }
                        }                        
                        if (EnumSeccionEstado.ABIERTA.equals(entity.getSecEstado()) && EnumSeccionEstado.CERRADA.equals(actual.getSecEstado())) {
                            realizarAccionesPostAbrirSeccion(entity.getSecPk());
                        }
                    }
                    return codDAO.guardar(entity, entity.getSecPk() == null ? ConstantesOperaciones.CREAR_SECCION : ConstantesOperaciones.ACTUALIZAR_SECCION);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    
    /**
     * Abre una sección si está cerrada y realiza acciones luego de abierta
     *
     * @param seccionPk Long
     * @throws GeneralException
     */
    public void abrirSeccionSiCerrada(Long seccionPk) throws GeneralException {
        try {
            FiltroSeccion fs = new FiltroSeccion();
            fs.setSecPk(seccionPk);
            List<SgSeccion> secciones = this.obtenerPorFiltro(fs);
            if (secciones.isEmpty()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_NO_EXISTE_SECCION);
                throw be;
            }

            SgSeccion sec = secciones.get(0);
            if (sec.getSecEstado().equals(EnumSeccionEstado.CERRADA)) {
                sec.setSecEstado(EnumSeccionEstado.ABIERTA);
                em.merge(sec);
                realizarAccionesPostAbrirSeccion(sec.getSecPk());
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Realiza acciones luego de abrir una sección
     *
     * @param seccionPk Long
     * @throws GeneralException
     */
    
    public void realizarAccionesPostAbrirSeccion(Long seccionPk) throws GeneralException {
        try {
            //En caso de que la sección se vuelva a abrir hay que ver si existe una calificación de promoción cerrada, la misma se tiene q re abrir
            FiltroCalificacion fc = new FiltroCalificacion();
            fc.setSecPk(seccionPk);
            fc.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.GRA);
            List<SgCalificacionCE> calCeList = calificacionBean.obtenerPorFiltro(fc);
            if (!calCeList.isEmpty()) {
                SgCalificacionCE calCe = calCeList.get(0);
                if (BooleanUtils.isTrue(calCe.getCalCerrado())) {
                    calCe.setCalCerrado(null);
                    CodigueraDAO<SgCalificacionCE> codDAOCal = new CodigueraDAO<>(em, SgCalificacionCE.class);
                    codDAOCal.guardar(calCe, null);
                }
            }

            //Eliminar la confirmación de promoción
            FiltroConfirmacionPromocion fil = new FiltroConfirmacionPromocion();
            fil.setSeccionFk(seccionPk);
            fil.setIncluirCampos(new String[]{"cprPk"});

            List<SgConfirmacionPromocion> confs = confirmacionPromocionBean.obtenerPorFiltro(fil);

            if (!confs.isEmpty()) {
                Long confPk = confs.get(0).getCprPk();
                confirmacionPromocionBean.eliminar(confPk);
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroSeccion
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroSeccion filtro) throws GeneralException {
        try {
            SeccionDAO codDAO = new SeccionDAO(em, seguridadBean);
            return codDAO.cantidadTotal(filtro, ConstantesOperaciones.BUSCAR_SECCION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroSeccion
     * @return Lista de SgSeccion
     * @throws GeneralException
     */
    public List<SgSeccion> obtenerPorFiltro(FiltroSeccion filtro) throws GeneralException {
        try {
            SeccionDAO codDAO = new SeccionDAO(em, seguridadBean);
            List<SgSeccion> secs = codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            if (!secs.isEmpty() && filtro.getSecIncluirMatriculasCampos() != null) {

                FiltroMatricula fm = new FiltroMatricula();
                fm.setMatSeccionesPk(secs.stream().map(s -> s.getSecPk()).collect(Collectors.toList()));
                fm.setMatRetirada(Boolean.FALSE);
                if (filtro.getSecIncluirMatriculasCampos() != null) {
                    fm.setIncluirCampos(ArrayUtils.add(filtro.getSecIncluirMatriculasCampos(), "matSeccion.secPk"));
                }
                fm.setSecurityOperation(null);

                List<SgMatricula> matriculas = matriculaBean.obtenerPorFiltro(fm);
                HashMap<Long, List<SgMatricula>> matriculasAgrupadas = new HashMap<>();
                for (SgMatricula mat : matriculas) {
                    matriculasAgrupadas.computeIfAbsent(mat.getMatSeccion().getSecPk(), s -> new ArrayList<>()).add(mat);
                }

                for (SgSeccion sec : secs) {
                    sec.setSecMatricula(matriculasAgrupadas.get(sec.getSecPk()) != null ? matriculasAgrupadas.get(sec.getSecPk()) : new ArrayList<>());
                }
            }
            return secs;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Copia secciones a un nuevo año
     *
     *
     * @param listServicio
     * @return Bolean
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public Boolean copiarSecciones(List<SgSeccion> listaSecciones, SgAnioLectivo nuevo) throws GeneralException {
        try {
            CodigueraDAO<SgSeccion> codDAO = new CodigueraDAO<>(em, SgSeccion.class);
            List<Long> pks = listaSecciones.stream().map(s -> s.getSecPk()).collect(Collectors.toList());

            FiltroSeccion fcopiar = new FiltroSeccion();
            fcopiar.setSeccionesPk(pks);
            List<SgSeccion> copiar = this.obtenerPorFiltro(fcopiar);

            FiltroSeccion fSeccion = new FiltroSeccion();
            fSeccion.setSecAnioLectivoFk(nuevo.getAlePk());
            fSeccion.setSecServiciosEducativoFk(copiar.stream().map(c -> c.getSecServicioEducativo().getSduPk()).distinct().collect(Collectors.toList()));
            fSeccion.setIncluirCampos(new String[]{"secCodigo",
                "secServicioEducativo.sduPk"});
            List<SgSeccion> seccionesExistentes = this.obtenerPorFiltro(fSeccion);

            for (SgSeccion sec : copiar) {
                em.detach(sec);
                if (BooleanUtils.isTrue(sec.getSecCopiadaAnioSiguiente())) {
                    if (!seccionesExistentes.stream().filter(c -> c.getSecCodigo().equals(sec.getSecCodigo()) && c.getSecServicioEducativo().getSduPk().equals(sec.getSecServicioEducativo().getSduPk())).collect(Collectors.toList()).isEmpty()) {
                        continue;
                    }
                }
                //Nueva copia
                sec.setSecPk(null);
                sec.setSecVersion(null);
                sec.setSecCopiadaAnioSiguiente(Boolean.FALSE);
                sec.setSecAnioLectivo(nuevo);
                sec.setSecEstado(EnumSeccionEstado.ABIERTA);
                codDAO.guardar(sec, null);
            }
            em.createNativeQuery("UPDATE centros_educativos.sg_secciones SET sec_copiada_anio_siguiente = true where sec_pk IN (:ids)")
                    .setParameter("ids", pks)
                    .executeUpdate();
            return Boolean.TRUE;
        } catch (BusinessException be) {
            throw be;
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
                CodigueraDAO<SgSeccion> codDAO = new CodigueraDAO(em, SgSeccion.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SECCION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    @Interceptors({AuditInterceptor.class})
    public void guardarSeccionAula(List<SgSeccion> list) {
        try {
            for (SgSeccion sec : list) {
                CodigueraDAO<SgSeccion> codDAO = new CodigueraDAO<>(em, SgSeccion.class);
                codDAO.guardar(sec, ConstantesOperaciones.ACTUALIZAR_SECCION);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
