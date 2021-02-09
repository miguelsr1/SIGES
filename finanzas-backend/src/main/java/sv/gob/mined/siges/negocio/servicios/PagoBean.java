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
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.enumerados.EnumModoPago;
import sv.gob.mined.siges.enumerados.EnumTipoMovimientoCajaChica;
import sv.gob.mined.siges.enumerados.EnumTipoRetiroMovimientoCB;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPago;
import sv.gob.mined.siges.negocio.validaciones.PagoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PagoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgFactura;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCajaChica;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.persistencia.entidades.SgPago;

/**
 * Servicio que gestiona los pagos
 *
 * @author Sofis Solutions
 */
@Stateless
public class PagoBean {

    private static final Logger LOGGER = Logger.getLogger(PagoBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private MovimientoCajaChicaBean cajaChicaBean;

    @Inject
    private MovimientoCuentaBancariaBean movCuentaBancariaBean;

    @Inject
    private FacturaBean facturaBean;

    @Inject
    private PagoBean pagoBean;

    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPago
     * @throws GeneralException
     */
    public SgPago obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPago> codDAO = new CodigueraDAO<>(em, SgPago.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_PAGOS);
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
                CodigueraDAO<SgPago> codDAO = new CodigueraDAO<>(em, SgPago.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_PAGOS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param pgs SgPago
     * @return SgPago
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgPago guardar(SgPago pgs) throws GeneralException {
        try {
            if (pgs != null) {
                if (PagoValidacionNegocio.validar(pgs)) {

                    //Se evalúa si aplica cancelar el movimiento anterior
                    boolean cancelarMovimiento = true;
                    if (pgs.getPgsPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(pgs.getClass(), pgs.getPgsPk(), pgs.getPgsVersion());
                        SgPago valorAnterior = (SgPago) valorAnt;
                        cancelarMovimiento = !PagoValidacionNegocio.compararParaGrabar(valorAnterior, pgs);
                        if (cancelarMovimiento) {
                            //Se cancela el movimiento anterior
                            cancelarMovimiento(pgs);
                            //Se guarda el movimiento actual
                            return pgs;
                        } else {
                            //Se actualiza sin crear movimientos
                            CodigueraDAO<SgPago> codDAO = new CodigueraDAO<>(em, SgPago.class);
                            codDAO.guardar(pgs, ConstantesOperaciones.CREAR_PAGOS);
                        }
                    } else {
                        //Se guarda el movimiento actual
                        return generarMovimiento(pgs);
                    }

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pgs;
    }

    /**
     * Se genera un movimiento en la cuenta bancaria o de caja chica
     * seleccionada.
     *
     * @param pago
     * @return
     */
    public SgPago generarMovimiento(SgPago pago) {
        try {
            CodigueraDAO<SgPago> codDAO = new CodigueraDAO<>(em, SgPago.class);

            SgFactura factura = facturaBean.obtenerPorId(pago.getPgsFactura().getFacPk());

            FiltroPago pago1 = new FiltroPago();
            pago1.setPgsNumeroCheque(pago.getPgsNumeroCheque());
            pago1.setIncluirCampos(new String[]{
                "pgsPk",
                "pgsNumeroCheque",
                "pgsMovimientoCBFk.mcbPk",
                "pgsMovimientoCBFk.mcbVersion",
                "pgsVersion"});
            List<SgPago> listMov = pagoBean.obtenerPorFiltro(pago1);
            if (!listMov.isEmpty()) {
                SgPago pagoOriginal = listMov.get(0);
                SgMovimientoCuentaBancaria movOriginal = movCuentaBancariaBean.obtenerPorId(pagoOriginal.getPgsMovimientoCBFk().getMcbPk());

                if (movOriginal != null) {
                    if (pago.getPgsModoPago().equals(EnumModoPago.CHEQUE)) {
                        SgMovimientoCuentaBancaria movimiento = movOriginal;
                        movimiento.setMcbCuentaFK(pago.getPgsCuentaBancaria());
                        movimiento.setMcbMonto(movOriginal.getMcbMonto().add(pago.getPgsImporte()));
                        movimiento.setMcbFecha(pago.getPgsFechaPago());
                        movimiento.setMcbTipoMovimiento(TipoMovimiento.DEBE);
                        if (factura.getFacItemMovimiento() != null) {
                            movimiento.setMcbDetalle(movOriginal.getMcbDetalle().concat(", ".concat(factura.getFacItemMovimiento().getMovAreaInversionPk().getAdiNombre())));
                            movimiento.setMcbProveedor(movOriginal.getMcbProveedor().concat(", ").concat(factura.getFacProveedorFk().getProNombre()));
                        }
                        if (factura.getFacItemPlanCompra() != null) {
                            movimiento.setMcbDetalle(movOriginal.getMcbDetalle().concat(", ").concat(factura.getFacItemPlanCompra().getComMovimientosFk().getMovAreaInversionPk().getAdiNombre()));
                            movimiento.setMcbProveedor(movOriginal.getMcbProveedor().concat(", ").concat(factura.getFacProveedorFk().getProNombre()));
                        }
                        movimiento.setMcbTipoRetiro(EnumTipoRetiroMovimientoCB.CHEQUE);
                        movimiento.setMcbChequeCb(pago.getPgsNumeroCheque());
                        movimiento.setMcbChequeCobrado(Boolean.FALSE);
                        movimiento.setMcbAplicaConciliacion(Boolean.TRUE);
                        movimiento = movCuentaBancariaBean.guardar(movimiento);
                        if (movimiento.getMcbPk() != null) {
                            pago.setPgsMovimientoCBFk(movimiento);
                            pago = codDAO.guardar(pago, ConstantesOperaciones.CREAR_PAGOS);
                            factura.setFacEstado(EnumFacturaEstado.PAGADO);
                            factura = facturaBean.guardar(factura, true);
                        }
                    } else {
                        SgMovimientoCajaChica movimiento = new SgMovimientoCajaChica();
                        movimiento.setMccCuentaFK(pago.getPgsCuentaBancariaCC());
                        movimiento.setMccMonto(pago.getPgsImporte());
                        movimiento.setMccFecha(pago.getPgsFechaPago());
                        movimiento.setMccTipoMovimiento(TipoMovimiento.DEBE);
                        if (factura.getFacItemMovimiento() != null) {
                            movimiento.setMccDetalle(factura.getFacItemMovimiento().getMovAreaInversionPk().getAdiNombre());
                        }
                        if (factura.getFacItemPlanCompra() != null) {
                            movimiento.setMccDetalle(factura.getFacItemPlanCompra().getComMovimientosFk().getMovAreaInversionPk().getAdiNombre());
                        }
                        movimiento = cajaChicaBean.guardar(movimiento);
                        if (movimiento.getMccPk() != null) {
                            pago.setPgsMovimientoCCFk(movimiento);
                            codDAO.guardar(pago, ConstantesOperaciones.CREAR_PAGOS);
                            factura.setFacEstado(EnumFacturaEstado.PAGADO);
                            factura = facturaBean.guardar(factura, true);
                        }
                    }

                }
            } else {
                if (pago.getPgsModoPago().equals(EnumModoPago.CHEQUE)) {
                    SgMovimientoCuentaBancaria movimiento = new SgMovimientoCuentaBancaria();
                    movimiento.setMcbCuentaFK(pago.getPgsCuentaBancaria());
                    movimiento.setMcbMonto(pago.getPgsImporte());
                    movimiento.setMcbFecha(pago.getPgsFechaPago());
                    movimiento.setMcbTipoMovimiento(TipoMovimiento.DEBE);
                    if (factura.getFacItemMovimiento() != null) {
                        movimiento.setMcbDetalle(factura.getFacItemMovimiento().getMovAreaInversionPk().getAdiNombre());
                        movimiento.setMcbProveedor(factura.getFacProveedorFk().getProNombre());
                    }
                    if (factura.getFacItemPlanCompra() != null) {
                        movimiento.setMcbDetalle(factura.getFacItemPlanCompra().getComMovimientosFk().getMovAreaInversionPk().getAdiNombre());
                        movimiento.setMcbProveedor(factura.getFacProveedorFk().getProNombre());
                    }
                    movimiento.setMcbTipoRetiro(EnumTipoRetiroMovimientoCB.CHEQUE);
                    movimiento.setMcbChequeCb(pago.getPgsNumeroCheque());
                    movimiento.setMcbChequeCobrado(Boolean.FALSE);
                    movimiento.setMcbAplicaConciliacion(Boolean.TRUE);
                    movimiento = movCuentaBancariaBean.guardar(movimiento);
                    if (movimiento.getMcbPk() != null) {
                        pago.setPgsMovimientoCBFk(movimiento);
                        pago = codDAO.guardar(pago, ConstantesOperaciones.CREAR_PAGOS);
                        factura.setFacEstado(EnumFacturaEstado.PAGADO);
                        factura = facturaBean.guardar(factura, true);
                    }
                } else {
                    SgMovimientoCajaChica movimiento = new SgMovimientoCajaChica();
                    movimiento.setMccCuentaFK(pago.getPgsCuentaBancariaCC());
                    movimiento.setMccMonto(pago.getPgsImporte());
                    movimiento.setMccFecha(pago.getPgsFechaPago());
                    movimiento.setMccTipoMovimiento(TipoMovimiento.DEBE);
                    if (factura.getFacItemMovimiento() != null) {
                        movimiento.setMccDetalle(factura.getFacItemMovimiento().getMovAreaInversionPk().getAdiNombre());
                    }
                    if (factura.getFacItemPlanCompra() != null) {
                        movimiento.setMccDetalle(factura.getFacItemPlanCompra().getComMovimientosFk().getMovAreaInversionPk().getAdiNombre());
                    }
                    movimiento = cajaChicaBean.guardar(movimiento);
                    if (movimiento.getMccPk() != null) {
                        pago.setPgsMovimientoCCFk(movimiento);
                        codDAO.guardar(pago, ConstantesOperaciones.CREAR_PAGOS);
                        factura.setFacEstado(EnumFacturaEstado.PAGADO);
                        factura = facturaBean.guardar(factura, true);
                    }
                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            Logger.getLogger(PagoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pago;
    }

    /**
     * Se genera un movimiento para cancelar el movimiento anterior.
     *
     * @param pagoActual SgPago
     */
    public void cancelarMovimiento(SgPago pagoActual) {
        try {
            CodigueraDAO<SgPago> codDAO = new CodigueraDAO<>(em, SgPago.class);
            SgPago pago = obtenerPorId(pagoActual.getPgsPk());
            if (pago.getPgsModoPago().equals(EnumModoPago.CHEQUE)) {
                SgMovimientoCuentaBancaria movimiento = new SgMovimientoCuentaBancaria();
                movimiento.setMcbCuentaFK(pago.getPgsMovimientoCBFk().getMcbCuentaFK());
                movimiento.setMcbMonto(pago.getPgsImporte());
                movimiento.setMcbFecha(pago.getPgsFechaPago());
                movimiento.setMcbTipoMovimiento(TipoMovimiento.HABER);
                movimiento.setMcbDetalle(pago.getPgsComentario());
                movCuentaBancariaBean.guardar(movimiento);
                generarMovimiento(pagoActual);
            } else {
                SgMovimientoCajaChica movimiento = new SgMovimientoCajaChica();
                movimiento.setMccCuentaFK(pago.getPgsMovimientoCCFk().getMccCuentaFK());
                movimiento.setMccMonto(pago.getPgsImporte());
                movimiento.setMccFecha(pago.getPgsFechaPago());
                movimiento.setMccTipoMovimiento(TipoMovimiento.HABER);
                movimiento.setMccDetalle(pago.getPgsComentario());
                cajaChicaBean.guardar(movimiento);
                generarMovimiento(pagoActual);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            Logger.getLogger(PagoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se genera un movimiento para cancelar el movimiento anterior.
     *
     * @param id Long
     */
    public void cancelarMovimientoPorId(Long id, String razon) {
        try {
            CodigueraDAO<SgPago> codDAO = new CodigueraDAO<>(em, SgPago.class);
            SgPago pago = obtenerPorId(id);

            SgFactura factura = facturaBean.obtenerPorId(pago.getPgsFactura().getFacPk());

            if (pago.getPgsModoPago().equals(EnumModoPago.CHEQUE)) {
                SgMovimientoCuentaBancaria movimiento1 = pago.getPgsMovimientoCBFk();
                movimiento1.setMcbAplicaConciliacion(Boolean.FALSE);
                movimiento1.setMcbChequeAnulado(Boolean.TRUE);
                movCuentaBancariaBean.guardar(movimiento1);

                SgMovimientoCuentaBancaria movimiento = new SgMovimientoCuentaBancaria();
                movimiento.setMcbCuentaFK(pago.getPgsMovimientoCBFk().getMcbCuentaFK());
                movimiento.setMcbMonto(pago.getPgsImporte());
                movimiento.setMcbFecha(pago.getPgsFechaPago());
                movimiento.setMcbTipoMovimiento(TipoMovimiento.HABER);
                movimiento.setMcbDetalle(razon);
                movimiento.setMcbAplicaConciliacion(Boolean.FALSE);
                movimiento.setMcbChequeCb(pago.getPgsNumeroCheque());

                movCuentaBancariaBean.guardar(movimiento);
            } else {
                SgMovimientoCajaChica movimiento = new SgMovimientoCajaChica();
                movimiento.setMccCuentaFK(pago.getPgsMovimientoCCFk().getMccCuentaFK());
                movimiento.setMccMonto(pago.getPgsImporte());
                movimiento.setMccFecha(pago.getPgsFechaPago());
                movimiento.setMccTipoMovimiento(TipoMovimiento.HABER);
                movimiento.setMccTipoIngreso(EnumTipoMovimientoCajaChica.PAGO);
                movimiento.setMccDetalle(razon);
                cajaChicaBean.guardar(movimiento);
            }

            factura.setFacEstado(EnumFacturaEstado.EN_PROCESO);
            factura = facturaBean.guardar(factura, true);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            Logger.getLogger(PagoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPago filtro) throws GeneralException {
        try {
            PagoDAO codDAO = new PagoDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgPago>
     * @throws GeneralException
     */
    public List<SgPago> obtenerPorFiltro(FiltroPago filtro) throws GeneralException {
        try {
            PagoDAO codDAO = new PagoDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
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
    public void eliminar(Long id, String razon) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPago> codDAO = new CodigueraDAO<>(em, SgPago.class);
                cancelarMovimientoPorId(id, razon);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_PAGOS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
