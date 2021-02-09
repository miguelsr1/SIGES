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
import sv.gob.mined.siges.negocio.validaciones.EvaluacionLiquidacionOtroValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEvaluacionLiquidacionOtro;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EvaluacionLiquidacionOtroBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEvaluacionLiquidacionOtro
     * @throws GeneralException
     */
    public SgEvaluacionLiquidacionOtro obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEvaluacionLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacionOtro.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION_OTRO);
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
                CodigueraDAO<SgEvaluacionLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacionOtro.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION_OTRO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param eli SgEvaluacionLiquidacionOtro
     * @return SgEvaluacionLiquidacionOtro
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEvaluacionLiquidacionOtro guardar(SgEvaluacionLiquidacionOtro eli, Boolean dataSecurity) throws GeneralException {
        try {
            if (eli != null) {
                if (EvaluacionLiquidacionOtroValidacionNegocio.validar(eli)) {
                    CodigueraDAO<SgEvaluacionLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacionOtro.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(eli,eli.getEliPk() == null ? ConstantesOperaciones.CREAR_EVALUACION_LIQUIDACION_OTRO : ConstantesOperaciones.ACTUALIZAR_EVALUACION_LIQUIDACION_OTRO);
                    } else {
                        return codDAO.guardar(eli, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eli;
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
            CodigueraDAO<SgEvaluacionLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacionOtro.class);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION_OTRO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEvaluacionLiquidacionOtro>
     * @throws GeneralException
     */
    public List<SgEvaluacionLiquidacionOtro> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEvaluacionLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacionOtro.class);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_EVALUACION_LIQUIDACION_OTRO);
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
                CodigueraDAO<SgEvaluacionLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgEvaluacionLiquidacionOtro.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_EVALUACION_LIQUIDACION_OTRO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
