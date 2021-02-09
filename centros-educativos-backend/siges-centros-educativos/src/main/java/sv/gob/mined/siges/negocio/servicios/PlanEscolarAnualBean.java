/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPlanEscolarAnual;
import sv.gob.mined.siges.negocio.validaciones.PlanEscolarAnualValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PlanEscolarAnualDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPlanEscolarAnual;

@Stateless
@Traced
public class PlanEscolarAnualBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private SeguridadBean seguridadBean;
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPlanEscolarAnual
     * @throws GeneralException
     */
    public SgPlanEscolarAnual obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPlanEscolarAnual> codDAO = new CodigueraDAO<>(em, SgPlanEscolarAnual.class);
                SgPlanEscolarAnual plan = codDAO.obtenerPorId(id, null);
                if (plan != null){
                    if (plan.getPeaDetallePlanEscolar() != null){
                        plan.getPeaDetallePlanEscolar().size();
                    }
                }
                return plan;
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPlanEscolarAnual> codDAO = new CodigueraDAO<>(em, SgPlanEscolarAnual.class);
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
     * @param entity SgPlanEscolarAnual
     * @return SgPlanEscolarAnual
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPlanEscolarAnual guardar(SgPlanEscolarAnual entity) throws GeneralException {
        try {
            if (entity != null) {
                if (PlanEscolarAnualValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgPlanEscolarAnual> codDAO = new CodigueraDAO<>(em, SgPlanEscolarAnual.class);
                    return codDAO.guardar(entity, entity.getPeaPk() == null ? ConstantesOperaciones.CREAR_PLAN_ESCOLAR_ANUAL : ConstantesOperaciones.ACTUALIZAR_PLAN_ESCOLAR_ANUAL);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPlanEscolarAnual> codDAO = new CodigueraDAO<>(em, SgPlanEscolarAnual.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_PLAN_ESCOLAR_ANUAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPlanEscolarAnual filtro) throws GeneralException {
        try {
            PlanEscolarAnualDAO DAO = new PlanEscolarAnualDAO(em, seguridadBean);
            return DAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_PLAN_ESCOLAR_ANUAL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPlanEscolarAnual
     * @throws GeneralException
     */
    public List<SgPlanEscolarAnual> obtenerPorFiltro(FiltroPlanEscolarAnual filtro) throws GeneralException {
        try {
            PlanEscolarAnualDAO DAO = new PlanEscolarAnualDAO(em, seguridadBean);
            return DAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_PLAN_ESCOLAR_ANUAL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
