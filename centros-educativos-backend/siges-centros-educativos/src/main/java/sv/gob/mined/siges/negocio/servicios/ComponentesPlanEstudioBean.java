/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ComponentePlanEstudioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;

/**
 *
 * @author usuario
 */
@Stateless
@Traced
public class ComponentesPlanEstudioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPlanEstudio
     * @throws GeneralException
     */
    public SgComponentePlanEstudio obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgComponentePlanEstudio> codDAO = new CodigueraDAO<>(em, SgComponentePlanEstudio.class);
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
                CodigueraDAO<SgComponentePlanEstudio> codDAO = new CodigueraDAO<>(em, SgComponentePlanEstudio.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroComponentePlanEstudio filtro) throws GeneralException {
        try {
            ComponentePlanEstudioDAO codDAO = new ComponentePlanEstudioDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPlanEstudio
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgComponentePlanEstudio> obtenerPorFiltro(FiltroComponentePlanEstudio filtro) throws GeneralException {
        try {
            ComponentePlanEstudioDAO codDAO = new ComponentePlanEstudioDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los componentes plan estudio numéricos que el estudiante cursó y
     * tienen nota de APR
     *
     * @param estudiantePk Long
     * @return Lista de SgPlanEstudio
     * @throws GeneralException
     */
    public List<SgComponentePlanEstudio> obtenerComponentesPlanEstudioNumericosCursadosEstudiante(Long estudiantePk) throws GeneralException {
        try {
            String query = "select cpe.cpe_pk, cpe.cpe_tipo, cpe.cpe_nombre "
                    + "from centros_educativos.sg_calificaciones_estudiante calest "
                    + "INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) "
                    + "INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                    + "INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                    + "INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                    + "INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                    + "INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                    + "where calest.cae_calificacion is not null and (cpg.cpg_aplica_grafica_evolucion) and cabezal.cal_tipo_periodo_calificacion = 'APR' "
                    + "AND calest.cae_estudiante_fk = :estPk  "
                    + "group by cpe.cpe_pk "
                    + "order by cpe_nombre";

            Query q = em.createNativeQuery(query).setParameter("estPk", estudiantePk);

            List<SgComponentePlanEstudio> ret = new ArrayList<>();
            List<Object[]> result = q.getResultList();

            for (Object[] o : result) {
                SgComponentePlanEstudio cpe = new SgComponentePlanEstudio();
                cpe.setCpePk(((BigInteger) o[0]).longValue());
                cpe.setCpeTipo(TipoComponentePlanEstudio.valueOf((String) o[1]));
                cpe.setCpeNombre((String) o[2]);
                ret.add(cpe);
            }

            return ret;

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los componentes plan estudio numéricos que el estudiante está
     * cursando y tienen nota de ORD
     *
     * @param estudiantePk Long
     * @return Lista de SgPlanEstudio
     * @throws GeneralException
     */
    public List<SgComponentePlanEstudio> obtenerComponentesPlanEstudioNumericosActualesEstudiante(Long estudiantePk) throws GeneralException {
        try {
            String query = "select cpe.cpe_pk, cpe.cpe_tipo, cpe.cpe_nombre, esc.eca_pk, esc.eca_minimo_aprobacion "
                    + "from centros_educativos.sg_calificaciones_estudiante calest "
                    + "INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) "
                    + "INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                    + "INNER JOIN centros_educativos.sg_rango_fecha rf ON (cabezal.cal_rango_fecha_fk = rf.rfe_pk) "
                    + "INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                    + "INNER JOIN centros_educativos.sg_sedes sed ON (sed.sed_pk = sdu.sdu_sede_fk) "
                    + "INNER JOIN catalogo.sg_tipos_calendario_escolar tce ON (sed.sed_tipo_calendario_fk = tce.tce_pk) "
                    + "INNER JOIN centros_educativos.sg_calendarios calendarios ON (calendarios.cal_pk = (select cal_pk from centros_educativos.sg_calendarios cal where cal.cal_tipo_calendario_fk = tce.tce_pk AND cal.cal_habilitado order by cal_fecha_inicio desc limit 1)) "
                    + "INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                    + "INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                    + "INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                    + "INNER JOIN catalogo.sg_escalas_calificacion esc ON (cpg.cpg_escala_calificacion_fk = esc.eca_pk) "
                    + "where calest.cae_calificacion is not null and (cpg.cpg_aplica_grafica_evolucion) and cabezal.cal_tipo_periodo_calificacion = 'ORD' "
                    + "and rf.rfe_fecha_desde >= calendarios.cal_fecha_inicio AND  rf.rfe_fecha_desde <= calendarios.cal_fecha_fin "
                    + "AND calest.cae_estudiante_fk = :estPk  "
                    + "group by cpe.cpe_pk, esc.eca_pk  "
                    + "order by cpe_nombre";

            Query q = em.createNativeQuery(query).setParameter("estPk", estudiantePk);

            List<SgComponentePlanEstudio> ret = new ArrayList<>();
            List<Object[]> result = q.getResultList();

            for (Object[] o : result) {
                SgComponentePlanEstudio cpe = new SgComponentePlanEstudio();
                cpe.setCpePk(((BigInteger) o[0]).longValue());
                cpe.setCpeTipo(TipoComponentePlanEstudio.valueOf((String) o[1]));
                cpe.setCpeNombre((String) o[2]);
                cpe.setCpeMinimoAprobacion(((BigDecimal)o[4]).doubleValue());
                ret.add(cpe);
            }

            return ret;

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los componentes plan estudio conceptuales que el estudiante
     * cursó y tienen nota de APR
     *
     * @param estudiantePk Long
     * @return Lista de SgPlanEstudio
     * @throws GeneralException
     */
    public List<SgComponentePlanEstudio> obtenerComponentesPlanEstudioConceptualesCursadosEstudiante(Long estudiantePk) throws GeneralException {
        try {
            String query = "select cpe.cpe_pk, cpe.cpe_tipo, cpe.cpe_nombre "
                    + "from centros_educativos.sg_calificaciones_estudiante calest "
                    + "INNER JOIN centros_educativos.sg_calificaciones cabezal ON (calest.cae_calificacion_fk = cabezal.cal_pk) "
                    + "INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk) "
                    + "INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                    + "INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk) "
                    + "INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) "
                    + "INNER JOIN centros_educativos.sg_componente_plan_grado cpg ON (cpg.cpg_componente_plan_estudio_fk = cpe.cpe_pk AND cpg.cpg_grado_fk = gra.gra_pk AND cpg.cpg_plan_estudio_fk = sec.sec_plan_estudio_fk and (cpg.cpg_exclusivo_seccion is null or cpg.cpg_exclusivo_seccion = sec.sec_pk)) "
                    + "where calest.cae_calificacion_conceptual_fk is not null and (cpg.cpg_aplica_grafica_evolucion) and cabezal.cal_tipo_periodo_calificacion = 'APR' "
                    + "AND calest.cae_estudiante_fk = :estPk  "
                    + "group by cpe.cpe_pk "
                    + "order by cpe_nombre";

            Query q = em.createNativeQuery(query).setParameter("estPk", estudiantePk);

            List<SgComponentePlanEstudio> ret = new ArrayList<>();
            List<Object[]> result = q.getResultList();

            for (Object[] o : result) {
                SgComponentePlanEstudio cpe = new SgComponentePlanEstudio();
                cpe.setCpePk(((BigInteger) o[0]).longValue());
                cpe.setCpeTipo(TipoComponentePlanEstudio.valueOf((String) o[1]));
                cpe.setCpeNombre((String) o[2]);
                ret.add(cpe);
            }

            return ret;

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgComponentePlanEstudio> codDAO = new CodigueraDAO<>(em, SgComponentePlanEstudio.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
