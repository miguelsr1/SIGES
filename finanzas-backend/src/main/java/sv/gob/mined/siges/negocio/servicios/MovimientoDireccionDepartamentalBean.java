/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroMovimientoDireccionDepartamental;
import sv.gob.mined.siges.negocio.validaciones.MovimientoDireccionDepartamentalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MovimientoDireccionDepartamentalDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancariasDD;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoDireccionDepartamental;

/**
 * Servicio que gestiona los movimientos de las cuentsa bancarias de las
 * direcciones departamentales
 *
 * @author Sofis Solutions
 */
@Stateless
public class MovimientoDireccionDepartamentalBean {

    private static final Logger LOGGER = Logger.getLogger(MovimientoDireccionDepartamentalBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientoDireccionDepartamental
     * @throws GeneralException
     */
    public SgMovimientoDireccionDepartamental obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMovimientoDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgMovimientoDireccionDepartamental.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_CUENTA_BANCARIA);
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
                CodigueraDAO<SgMovimientoDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgMovimientoDireccionDepartamental.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_CUENTA_BANCARIA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param mdd SgMovimientoDireccionDepartamental
     * @return SgMovimientoDireccionDepartamental
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMovimientoDireccionDepartamental guardar(SgMovimientoDireccionDepartamental mdd) throws GeneralException {
        try {
            if (mdd != null) {
                if (MovimientoDireccionDepartamentalValidacionNegocio.validar(mdd)) {
                    //Código encargado de realizar la mutua exclusión de los movimientos para la cuenta bancaria en edición
                    em.find(SgCuentasBancariasDD.class, mdd.getMddCuentaFK().getCbdPk(), LockModeType.PESSIMISTIC_WRITE);
                    CodigueraDAO<SgMovimientoDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgMovimientoDireccionDepartamental.class);
                    mdd = calcularSaldosMovimientos(mdd);
                    return mdd;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mdd;
    }

    /**
     * Realiza el cálculo para todos los movimientos existentes.
     * @param movimiento SgMovimientoDireccionDepartamental
     * @return SgMovimientoDireccionDepartamental
     */
    public SgMovimientoDireccionDepartamental calcularSaldosMovimientos(SgMovimientoDireccionDepartamental movimiento) {
        try {
            CodigueraDAO<SgMovimientoDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgMovimientoDireccionDepartamental.class);
            LocalTime time = LocalTime.now();
            movimiento.setMddFecha(movimiento.getMddFecha().with(time));
            //Se obtienen los demás movimientos para esta cuenta bancaria
            FiltroMovimientoDireccionDepartamental filtro = new FiltroMovimientoDireccionDepartamental();
            filtro.setMddCuentaFK(movimiento.getMddCuentaFK().getCbdPk());
            filtro.setFirst(new Long(0));
            filtro.setOrderBy(new String[]{"mddFecha"});
            filtro.setAscending(new boolean[]{true});
            List<SgMovimientoDireccionDepartamental> listado = obtenerPorFiltro(filtro);
            movimiento = SaldoMovimientoActual(listado, movimiento);
            //Se guarda el movimiento en edición
            codDAO.guardar(movimiento, null);
            //Se verifican los saldos para los demás movimientos
            saldoMovimientosPosteriores(listado, movimiento);
            return movimiento;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Se realizan las operaciones para el movimiento actual.
     * @param listado List<SgMovimientoDireccionDepartamental>
     * @param movimiento SgMovimientoDireccionDepartamental
     * @return SgMovimientoDireccionDepartamental
     */
    public SgMovimientoDireccionDepartamental SaldoMovimientoActual(List<SgMovimientoDireccionDepartamental> listado, SgMovimientoDireccionDepartamental movimiento) {
        BigDecimal montoDebe = BigDecimal.ZERO;
        BigDecimal montoHaber = BigDecimal.ZERO;
        if (!listado.isEmpty()) {
            List<SgMovimientoDireccionDepartamental> debe = listado.stream().filter(m -> m.getMddTipoMovimiento().equals(TipoMovimiento.DEBE) && (m.getMddFecha().isBefore(movimiento.getMddFecha()))).collect(Collectors.toList());
            List<SgMovimientoDireccionDepartamental> haber = listado.stream().filter(m -> m.getMddTipoMovimiento().equals(TipoMovimiento.HABER) && (m.getMddFecha().isBefore(movimiento.getMddFecha()))).collect(Collectors.toList());
            if (!debe.isEmpty()) {
                montoDebe = debe.stream().map(m -> m.getMddMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);//BigDecimal.valueOf();
                //LOGGER.info("Debe: ---- "+debe.size()+" ---- "+montoDebe);
            }
            if (!haber.isEmpty()) {
                //montoHaber = haber.stream().mapToDouble(m -> m.getMddMonto()).sum();
                montoHaber = haber.stream().map(m -> m.getMddMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                //LOGGER.info("Haber: ---- "+haber.size()+" ---- "+montoHaber);
            }
        }
        if (movimiento.getMddTipoMovimiento().equals(TipoMovimiento.DEBE)) {
            montoDebe = montoDebe.add(movimiento.getMddMonto());
        } else {
            montoHaber = montoHaber.add(movimiento.getMddMonto());
        }
        movimiento.setMddSaldo(montoHaber.subtract(montoDebe));
        return movimiento;
    }

    /**
     * Se actualizan los saldos para los movimientos más actuales.
     * @param listado List<SgMovimientoDireccionDepartamental>
     * @param mov SgMovimientoDireccionDepartamental
     */
    public void saldoMovimientosPosteriores(List<SgMovimientoDireccionDepartamental> listado, SgMovimientoDireccionDepartamental mov) {
        try {
            listado = listado.stream().filter(m -> m.getMddFecha().isAfter(mov.getMddFecha())).collect(Collectors.toList());
            if (!listado.isEmpty()) {
                BigDecimal montoHaber = BigDecimal.ZERO;
                BigDecimal montoDebe = BigDecimal.ZERO;
                CodigueraDAO<SgMovimientoDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgMovimientoDireccionDepartamental.class);
                for (SgMovimientoDireccionDepartamental movimiento : listado) {
                    if (mov.getMddTipoMovimiento().equals(TipoMovimiento.DEBE)) {
                        montoDebe = mov.getMddMonto();
                    } else {
                        montoHaber = mov.getMddMonto();
                    }
                    movimiento.setMddSaldo(movimiento.getMddSaldo().add(montoHaber.subtract(montoDebe)));
                    codDAO.guardar(movimiento, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMovimientoDireccionDepartamental filtro) throws GeneralException {
        try {
            MovimientoDireccionDepartamentalDAO codDAO = new MovimientoDireccionDepartamentalDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMovimientoDireccionDepartamental>
     * @throws GeneralException
     */
    public List<SgMovimientoDireccionDepartamental> obtenerPorFiltro(FiltroMovimientoDireccionDepartamental filtro) throws GeneralException {
        try {
            MovimientoDireccionDepartamentalDAO codDAO = new MovimientoDireccionDepartamentalDAO(em);
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
                CodigueraDAO<SgMovimientoDireccionDepartamental> codDAO = new CodigueraDAO<>(em, SgMovimientoDireccionDepartamental.class);
                codDAO.eliminarPorId(id, "");
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
