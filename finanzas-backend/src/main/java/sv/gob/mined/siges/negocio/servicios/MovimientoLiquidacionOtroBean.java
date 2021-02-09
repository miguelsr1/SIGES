/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sv.gob.mined.siges.filtros.FiltroMovimientoLiquidacionOtro;
import sv.gob.mined.siges.negocio.validaciones.MovimientoLiquidacionOtroValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientoLiquidacionOtroDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoLiquidacionOtro;

/**
 * Servicio que gestiona los movimientos de liquidación de otros ingresos
 * @author Sofis Solutions
 */
@Stateless
public class MovimientoLiquidacionOtroBean {
    
    private static final Logger LOGGER = Logger.getLogger(MovimientoLiquidacionOtroBean.class.getName());
    
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
     * @return SgMovimientoLiquidacionOtro
     * @throws GeneralException
     */
    public SgMovimientoLiquidacionOtro obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientoLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacionOtro.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_LIQUIDACION_OTRO_INGRESO);
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
                CodigueraDAO<SgMovimientoLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacionOtro.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_LIQUIDACION_OTRO_INGRESO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param mlo SgMovimientoLiquidacionOtro
     * @return SgMovimientoLiquidacionOtro
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMovimientoLiquidacionOtro guardar(SgMovimientoLiquidacionOtro mlo,Boolean dataSecurity) throws GeneralException {
        try {
            if (mlo != null) {
                if (MovimientoLiquidacionOtroValidacionNegocio.validar(mlo)) {
                    CodigueraDAO<SgMovimientoLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacionOtro.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(mlo,mlo.getMloPk() == null ? ConstantesOperaciones.CREAR_MOVIMIENTOS_LIQUIDACION_OTROS : ConstantesOperaciones.ACTUALIZAR_MOVIMIENTOS_LIQUIDACION_OTROS);
                    } else {
                        return codDAO.guardar(mlo, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mlo;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMovimientoLiquidacionOtro filtro) throws GeneralException {
        try {
            MovimientoLiquidacionOtroDAO codDAO = new MovimientoLiquidacionOtroDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_LIQUIDACION_OTRO_INGRESO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoLiquidacionOtro>
     * @throws GeneralException
     */
    public List<SgMovimientoLiquidacionOtro> obtenerPorFiltro(FiltroMovimientoLiquidacionOtro filtro) throws GeneralException {
        try {
            MovimientoLiquidacionOtroDAO codDAO = new MovimientoLiquidacionOtroDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_LIQUIDACION_OTRO_INGRESO);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(),ex);
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
                CodigueraDAO<SgMovimientoLiquidacionOtro> codDAO = new CodigueraDAO<>(em, SgMovimientoLiquidacionOtro.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_LIQUIDACION_OTRO_INGRESO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
