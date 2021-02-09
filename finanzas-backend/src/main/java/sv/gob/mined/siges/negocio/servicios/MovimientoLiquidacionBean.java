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
import sv.gob.mined.siges.filtros.FiltroMovimientoLiquidacion;
import sv.gob.mined.siges.negocio.validaciones.MovimientoLiquidacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientoLiquidacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoLiquidacion;

/**
 * Servicio que gestiona los movimientos de liquidación
 * @author Sofis Solutions
 */
@Stateless
public class MovimientoLiquidacionBean {

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
     * @return SgMovimientoLiquidacion
     * @throws GeneralException
     */
    public SgMovimientoLiquidacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientoLiquidacion> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacion.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_MOVIMIENTOS_LIQUIDACION);
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
                CodigueraDAO<SgMovimientoLiquidacion> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacion.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_MOVIMIENTOS_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param mlq SgMovimientoLiquidacion
     * @return SgMovimientoLiquidacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMovimientoLiquidacion guardar(SgMovimientoLiquidacion mlq,Boolean dataSecurity) throws GeneralException {
        try {
            if (mlq != null) {
                if (MovimientoLiquidacionValidacionNegocio.validar(mlq)) {
                    CodigueraDAO<SgMovimientoLiquidacion> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacion.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(mlq,mlq.getMlqPk() == null ? ConstantesOperaciones.CREAR_MOVIMIENTOS_LIQUIDACION : ConstantesOperaciones.ACTUALIZAR_MOVIMIENTOS_LIQUIDACION);
                    } else {
                        return codDAO.guardar(mlq, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mlq;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMovimientoLiquidacion filtro) throws GeneralException {
        try {
            MovimientoLiquidacionDAO codDAO = new MovimientoLiquidacionDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_MOVIMIENTOS_LIQUIDACION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoLiquidacion>
     * @throws GeneralException
     */
    public List<SgMovimientoLiquidacion> obtenerPorFiltro(FiltroMovimientoLiquidacion filtro) throws GeneralException {
        try {
            MovimientoLiquidacionDAO codDAO = new MovimientoLiquidacionDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_MOVIMIENTOS_LIQUIDACION);
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
                CodigueraDAO<SgMovimientoLiquidacion> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacion.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_MOVIMIENTOS_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
