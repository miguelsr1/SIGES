/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
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
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.filtros.FiltroComponentePlanGrado;
import sv.gob.mined.siges.filtros.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ComponentePlanGradoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ComponentePlanGradoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgActividadEspecialSeccion;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class ComponentePlanGradoBean {

    private static final Logger LOGGER = Logger.getLogger(ComponentePlanGradoBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private ActividadEspecialSeccionBean actividadEspecialSeccionBean;

    @Inject
    private ComponentesPlanEstudioBean componentePlanEstudioBean;

    @Inject
    private CalificacionBean calificacionBean;

    @Inject
    private RelGradoPlanEstudioBean relGradoPlanEstudioBean;   
    

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgComponentePlanGrado
     * @throws GeneralException
     */
    public SgComponentePlanGrado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgComponentePlanGrado> codDAO = new CodigueraDAO<>(em, SgComponentePlanGrado.class);
                return codDAO.obtenerPorId(id, null);
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
                CodigueraDAO<SgComponentePlanGrado> codDAO = new CodigueraDAO<>(em, SgComponentePlanGrado.class);
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
     * @param entity SgComponentePlanGrado
     * @return SgComponentePlanGrado
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgComponentePlanGrado guardar(SgComponentePlanGrado entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ComponentePlanGradoValidacionNegocio.validar(entity)) {
                    if (entity.getCpgPk() != null) {
                        FiltroComponentePlanGrado cpg = new FiltroComponentePlanGrado();
                        cpg.setCpgPk(entity.getCpgPk());
                        cpg.setIncluirCampos(new String[]{"cpgEscalaCalificacion.ecaPk","cpgPeriodosCalificacion"});
                        if (entity.getCpgExclusivoSeccion() != null) {
                            cpg.setCpgAgregandoSeccionExclusiva(entity.getCpgExclusivoSeccion().getSecPk());
                        }
                        List<SgComponentePlanGrado> original = this.obtenerPorFiltro(cpg);
                        if (!original.isEmpty()) {
                            if (!original.get(0).getCpgEscalaCalificacion().getEcaPk().equals(entity.getCpgEscalaCalificacion().getEcaPk())
                                    || !original.get(0).getCpgPeriodosCalificacion().equals(entity.getCpgPeriodosCalificacion())) {
                                FiltroCalificacion fc = new FiltroCalificacion();
                                fc.setCalComponentePlanEstudio(entity.getCpgComponentePlanEstudio().getCpePk());
                                fc.setSecGradoFk(entity.getCpgGrado().getGraPk());
                                fc.setSecPlanEstudioFk(entity.getCpgPlanEstudio().getPesPk());
                                fc.setSecPk(entity.getCpgExclusivoSeccion()!=null?entity.getCpgExclusivoSeccion().getSecPk():null);
                                fc.setMaxResults(1L);
                                fc.setIncluirCampos(new String[]{"calPk"});
                                List<SgCalificacionCE> calCE = calificacionBean.obtenerPorFiltro(fc);
                                if (!calCE.isEmpty()) {
                                    BusinessException ge = new BusinessException();
                                    if(!original.get(0).getCpgEscalaCalificacion().getEcaPk().equals(entity.getCpgEscalaCalificacion().getEcaPk())){
                                        ge.addError("cpgPlanEstudio", Errores.ERROR_EXISTEN_CALIFICACIONES_CON_ESCALA);
                                    }
                                    if(!original.get(0).getCpgPeriodosCalificacion().equals(entity.getCpgPeriodosCalificacion())){
                                        ge.addError("cpgPlanEstudio", Errores.ERROR_EXISTEN_CALIFICACIONES_CON_PERIODO);
                                    }
                                    
                                    throw ge;
                                }
                            }
                        }
                    }

                    if (entity.getCpgComponentePlanEstudio().getCpeTipo().equals(TipoComponentePlanEstudio.AESS)) {
                        SgActividadEspecialSeccion ae = (SgActividadEspecialSeccion) entity.getCpgComponentePlanEstudio();
                        BusinessException ge = new BusinessException();
                        if (StringUtils.isBlank(ae.getCpeCodigo())) {
                            ge.addError("cpgPlanEstudio", Errores.ERROR_CODIGO_VACIO);
                        }
                        if (StringUtils.isBlank(ae.getCpeNombre())) {
                            ge.addError("cpgPlanEstudio", Errores.ERROR_NOMBRE_VACIO);
                        }
                        if (ge.getErrores().size() > 0) {
                            throw ge;
                        }
                        actividadEspecialSeccionBean.guardar(ae);
                        entity.setCpgComponentePlanEstudio(ae);
                    }

                    FiltroRelGradoPlanEstudio frg = new FiltroRelGradoPlanEstudio();
                    frg.setRgpGrado(entity.getCpgGrado().getGraPk());
                    frg.setRgpPlanEstudio(entity.getCpgPlanEstudio().getPesPk());
                    frg.setMaxResults(1L);
                    if (relGradoPlanEstudioBean.obtenerTotalPorFiltro(frg) == 0) {
                        SgRelGradoPlanEstudio relGradoPlan = new SgRelGradoPlanEstudio();
                        relGradoPlan.setRgpGradoFk(entity.getCpgGrado());
                        relGradoPlan.setRgpPlanEstudioFk(entity.getCpgPlanEstudio());
                        relGradoPlanEstudioBean.guardar(relGradoPlan);
                    }

                    CodigueraDAO<SgComponentePlanGrado> codDAO = new CodigueraDAO<>(em, SgComponentePlanGrado.class);
                    return codDAO.guardar(entity, null);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroComponentePlanGrado filtro) throws GeneralException {
        try {
            ComponentePlanGradoDAO codDAO = new ComponentePlanGradoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgComponentePlanGrado
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgComponentePlanGrado> obtenerPorFiltro(FiltroComponentePlanGrado filtro) throws GeneralException {
        try {
            ComponentePlanGradoDAO codDAO = new ComponentePlanGradoDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgComponentePlanGrado> codDAO = new CodigueraDAO<>(em, SgComponentePlanGrado.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarComponentePlanGrado(SgComponentePlanGrado entidad) throws GeneralException {
        if (entidad != null) {
            try {
              

                //Se borra las calificaciones asociadas al componente
                if (entidad.getCpgPk()!=null && entidad.getCpgGrado().getGraPk() != null && entidad.getCpgPlanEstudio().getPesPk() != null) {
                    FiltroCalificacion fcal = new FiltroCalificacion();
                    fcal.setSecGradoFk(entidad.getCpgGrado().getGraPk());
                    fcal.setSecPlanEstudioFk(entidad.getCpgPlanEstudio().getPesPk());
                    fcal.setCalComponentePlanEstudio(entidad.getCpgComponentePlanEstudio().getCpePk());
                    fcal.setSecPk(entidad.getCpgExclusivoSeccion() != null ? entidad.getCpgExclusivoSeccion().getSecPk() : null);
                    fcal.setIncluirCampos(new String[]{"calPk"});
                    List<SgCalificacionCE> calificaciones = calificacionBean.obtenerPorFiltro(fcal);
                    List<Long> calPk = calificaciones.stream().map(c -> c.getCalPk()).collect(Collectors.toList());
                    
                   

                    if (!calPk.isEmpty()) {
                        
                        for(Long cal:calPk){
                            Query sq = em.createNativeQuery("select nextval ('hibernate_sequence')");
                            Long seq = ((BigInteger) sq.getSingleResult()).longValue();
                            
                            Query est = em.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(:seq, extract(epoch from now()), :usuario)");
                            est.setParameter("usuario",Lookup.obtenerJWT().getSubject());
                            est.setParameter("seq", seq);
                            est.executeUpdate();
                            
                            Query rev = em.createNativeQuery("insert into centros_educativos.sg_calificaciones_aud "
                                    + "(rev, revtype,cal_ult_mod_fecha,cal_ult_mod_usuario,cal_pk,cal_rango_fecha_fk,cal_seccion_fk,cal_componente_plan_estudio_fk,cal_fecha,cal_tipo_periodo_calificacion,cal_tipo_calendario_escolar,cal_numero,cal_cerrado)\n" +
                                    " (select :seq,2,:ultfec,:usu,cal_pk,cal_rango_fecha_fk,cal_seccion_fk,cal_componente_plan_estudio_fk,cal_fecha,cal_tipo_periodo_calificacion,cal_tipo_calendario_escolar,cal_numero,cal_cerrado from centros_educativos.sg_calificaciones where cal_pk=:calPk)");
                            rev.setParameter("seq", seq);
                            rev.setParameter("calPk",cal);
                            rev.setParameter("usu",Lookup.obtenerJWT().getSubject());
                            rev.setParameter("ultfec",LocalDateTime.now());
                            rev.executeUpdate();
                        }
                        
                        Query q = em.createNativeQuery("delete from centros_educativos.sg_calificaciones_estudiante where cae_calificacion_fk in (:listCal)");
                        q.setParameter("listCal", calPk);
                        q.executeUpdate();

                        Query a = em.createNativeQuery("delete from centros_educativos.sg_calificaciones where cal_pk in (:listCal);");
                        a.setParameter("listCal", calPk);
                        a.executeUpdate();                       
                        
                    }
                    Query b = em.createNativeQuery("delete from centros_educativos.sg_celda_dia_hora where cdh_componente_plan_grado_fk=:cpgpk");
                    b.setParameter("cpgpk", entidad.getCpgPk());
                    b.executeUpdate();
                }

                
               this.eliminar(entidad.getCpgPk());
              //  componentePlanEstudioBean.eliminar(cpePk);
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
