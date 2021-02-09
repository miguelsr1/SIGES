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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.EvaluacionLiquidacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEvaluacionLiquidacion;

/**
 * Servicio que gestiona evaluación de liquidación
 * @author Sofis Solutions
 */
@Stateless
public class EvaluacionLiquidacionBean {

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
     * @return SgEvaluacionLiquidacion
     * @throws GeneralException
     */
    public SgEvaluacionLiquidacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEvaluacionLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacion.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION);
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
                CodigueraDAO<SgEvaluacionLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacion.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param elq SgEvaluacionLiquidacion
     * @return SgEvaluacionLiquidacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEvaluacionLiquidacion guardar(SgEvaluacionLiquidacion elq, Boolean dataSecurity) throws GeneralException {
        try {
            if (elq != null) {
                if (EvaluacionLiquidacionValidacionNegocio.validar(elq)) {
                    CodigueraDAO<SgEvaluacionLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacion.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(elq,elq.getElqPk() == null ? ConstantesOperaciones.CREAR_EVALUACION_LIQUIDACION : ConstantesOperaciones.ACTUALIZAR_EVALUACION_LIQUIDACION);
                    } else {
                        return codDAO.guardar(elq, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return elq;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEvaluacionLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacion.class);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEvaluacionLiquidacion>
     * @throws GeneralException
     */
    public List<SgEvaluacionLiquidacion> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEvaluacionLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacion.class);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION);
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
                CodigueraDAO<SgEvaluacionLiquidacion> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacion.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_EVALUACION_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
