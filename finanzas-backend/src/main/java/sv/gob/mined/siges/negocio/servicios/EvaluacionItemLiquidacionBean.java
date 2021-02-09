/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEvaluacionItemLiquidacion;
import sv.gob.mined.siges.negocio.validaciones.EvaluacionItemLiquidacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EvaluacionItemLiquidacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEvaluacionItemLiquidacion;

/**
 * Servicio que gestiona la evaluación items de liquidación
 * @author Sofis Solutions
 */
@Stateless
public class EvaluacionItemLiquidacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEvaluacionItemLiquidacion
     * @throws GeneralException
     */
    public SgEvaluacionItemLiquidacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEvaluacionItemLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionItemLiquidacion.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_EVALUACION_ITEM_LIQUIDACION);
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
                CodigueraDAO<SgEvaluacionItemLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionItemLiquidacion.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_EVALUACION_ITEM_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param eil SgEvaluacionItemLiquidacion
     * @return SgEvaluacionItemLiquidacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEvaluacionItemLiquidacion guardar(SgEvaluacionItemLiquidacion eil, Boolean dataSecurity) throws GeneralException {
        try {
            if (eil != null) {
                if (EvaluacionItemLiquidacionValidacionNegocio.validar(eil)) {
                    CodigueraDAO<SgEvaluacionItemLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionItemLiquidacion.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(eil,eil.getEilPk() == null ? ConstantesOperaciones.CREAR_EVALUACION_ITEM_LIQUIDACION : ConstantesOperaciones.ACTUALIZAR_EVALUACION_ITEM_LIQUIDACION);
                    } else {
                        return codDAO.guardar(eil, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eil;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEvaluacionItemLiquidacion filtro) throws GeneralException {
        try {
            EvaluacionItemLiquidacionDAO codDAO = new EvaluacionItemLiquidacionDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_EVALUACION_ITEM_LIQUIDACION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEvaluacionItemLiquidacion>
     * @throws GeneralException
     */
    public List<SgEvaluacionItemLiquidacion> obtenerPorFiltro(FiltroEvaluacionItemLiquidacion filtro) throws GeneralException {
        try {
            EvaluacionItemLiquidacionDAO codDAO = new EvaluacionItemLiquidacionDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_EVALUACION_ITEM_LIQUIDACION);
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
                CodigueraDAO<SgEvaluacionItemLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionItemLiquidacion.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_EVALUACION_ITEM_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
