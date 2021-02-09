/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroFactura;
import sv.gob.mined.siges.filtros.FiltroLiquidacionOtroIngreso;
import sv.gob.mined.siges.filtros.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.LiquidacionOtroIngresoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.LiquidacionOtroIngresoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgFactura;
import sv.gob.mined.siges.persistencia.entidades.SgLiquidacionOtroIngreso;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoLiquidacionOtro;

/**
 * Servicio que gestiona liquidacion de otros ingresos
 * @author Sofis Solutions
 */
@Stateless
public class LiquidacionOtroIngresoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;
    
    @Inject
    private MovimientoLiquidacionOtroBean movLiquidacionOtroBean;
    
    @Inject
    private MovimientoCuentaBancariaBean movCuentaBancBean;
    
    @Inject
    private FacturaBean facturaBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgLiquidacionOtroIngreso
     * @throws GeneralException
     */
    public SgLiquidacionOtroIngreso obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgLiquidacionOtroIngreso> codDAO = new CodigueraDAO<>(em, SgLiquidacionOtroIngreso.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_LIQUIDACION_OTRO_INGRESO);
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
                CodigueraDAO<SgLiquidacionOtroIngreso> codDAO = new CodigueraDAO<>(em, SgLiquidacionOtroIngreso.class);
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
     * @param loi SgLiquidacionOtroIngreso
     * @return SgLiquidacionOtroIngreso
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgLiquidacionOtroIngreso guardar(SgLiquidacionOtroIngreso loi, Boolean dataSecurity) throws GeneralException {
        try {
            if (loi != null) {
                if (LiquidacionOtroIngresoValidacionNegocio.validar(loi)) {
                    CodigueraDAO<SgLiquidacionOtroIngreso> codDAO = new CodigueraDAO<>(em, SgLiquidacionOtroIngreso.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(loi,loi.getLoiPk() == null ? ConstantesOperaciones.CREAR_LIQUIDACION_OTRO_INGRESO : ConstantesOperaciones.ACTUALIZAR_LIQUIDACION_OTRO_INGRESO);
                    } else {
                        return codDAO.guardar(loi, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return loi;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroLiquidacionOtroIngreso filtro) throws GeneralException {
        try {
            LiquidacionOtroIngresoDAO codDAO = new LiquidacionOtroIngresoDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_LIQUIDACION_OTRO_INGRESO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgLiquidacionOtroIngreso>
     * @throws GeneralException
     */
    public List<SgLiquidacionOtroIngreso> obtenerPorFiltro(FiltroLiquidacionOtroIngreso filtro) throws GeneralException {
        try {
            LiquidacionOtroIngresoDAO codDAO = new LiquidacionOtroIngresoDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_LIQUIDACION_OTRO_INGRESO);
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
                CodigueraDAO<SgLiquidacionOtroIngreso> codDAO = new CodigueraDAO<>(em, SgLiquidacionOtroIngreso.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_LIQUIDACION_OTRO_INGRESO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Guarda la liquidación con sus movimientos
     *
     * @param liq SgLiquidacion
     * @return SgLiquidacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgLiquidacionOtroIngreso guardarConMovimientos(SgLiquidacionOtroIngreso liq,Boolean dataSecurity) throws GeneralException {
        try {
            BusinessException be = new BusinessException();
            SgLiquidacionOtroIngreso liquidacion = new SgLiquidacionOtroIngreso();
            FiltroMovimientoCuentaBancaria filtroMovCB = new FiltroMovimientoCuentaBancaria();
                filtroMovCB.setIncluirCampos(new String[]{
                    "mcbPk","mcbDetalle","mcbFecha","mcbMonto","mcbSaldo","mcbVersion","mcbCuentaFK.cbcPk",
                    "mcbCuentaFK.cbcVersion","mcbTipoMovimiento","mcbMovFuenteIngresosFk.movPk","mcbMovFuenteIngresosFk.movVersion",
                    "mcbChequeCobrado","mcbTransaccion","mcbEstadoLiquidacion"
                });
                filtroMovCB.setSedePresupuesto(liq.getLoiSedePk().getSedPk());
                filtroMovCB.setAnioFiscal(liq.getLoiAnioPk().getAleAnio());
                filtroMovCB.setMcbTipoMovimiento(TipoMovimiento.HABER);
                List<SgMovimientoCuentaBancaria> ingresosMovs = movCuentaBancBean.obtenerPorFiltro(filtroMovCB);
                
                List<SgMovimientoCuentaBancaria> egresosMovs = new ArrayList();
                FiltroFactura filtroFac = new FiltroFactura();
                filtroFac.setSedePk(liq.getLoiSedePk().getSedPk());
                filtroFac.setAnioFiscal(liq.getLoiAnioPk().getAleAnio());
                filtroFac.setMovimientoOrigen(EnumMovimientosOrigen.P);
                filtroFac.setIncluirCampos(new String[]{"facPk",
                    "pago.pgsMovimientoCBFk.mcbPk","pago.pgsMovimientoCBFk.mcbDetalle","pago.pgsMovimientoCBFk.mcbFecha",
                    "pago.pgsMovimientoCBFk.mcbCuentaFK.cbcPk","pago.pgsMovimientoCBFk.mcbCuentaFK.cbcVersion",
                    "pago.pgsMovimientoCBFk.mcbMonto","pago.pgsMovimientoCBFk.mcbSaldo",
                    "pago.pgsMovimientoCBFk.mcbTipoMovimiento","pago.pgsMovimientoCBFk.mcbEstadoLiquidacion",
                    "pago.pgsMovimientoCBFk.mcbMovFuenteIngresosFk.movPk","pago.pgsMovimientoCBFk.mcbMovFuenteIngresosFk.movVersion",
                    "pago.pgsMovimientoCBFk.mcbTransaccion","pago.pgsMovimientoCBFk.mcbVersion",
                    "facVersion"});
                
                List<SgFactura> listFacturas = facturaBean.obtenerPorFiltro(filtroFac);
                if(!listFacturas.isEmpty()){
                    egresosMovs = listFacturas.stream().filter(f->f.getPago()!=null).filter(f->f.getPago().getPgsMovimientoCBFk()!=null).map(f-> f.getPago().getPgsMovimientoCBFk()).collect(Collectors.toList());
                }


            if(ingresosMovs!=null && !ingresosMovs.isEmpty()){
                liquidacion = guardar(liq, dataSecurity);

                for(SgMovimientoCuentaBancaria mov: ingresosMovs){
                    SgMovimientoLiquidacionOtro movLiq = new SgMovimientoLiquidacionOtro();
                    movLiq.setMloLiquidacionOtroFk(liquidacion);
                    movLiq.setMloMovimientoFk(mov);
                    movLiq.setMloTipoMovimiento(EnumMovimientosTipo.I);
                    movLiquidacionOtroBean.guardar(movLiq, dataSecurity);

                    mov.setMcbEstadoLiquidacion(EnumEstadoLiquidacion.CONFIRMADA);
                    movCuentaBancBean.cobrar(mov,dataSecurity);
                }
            }
            else{
                be.addError(Errores.ERROR_LIQUIDACION_INGRESOS_GUARDAR);
                throw be;
            }

            if(egresosMovs!=null && !egresosMovs.isEmpty()){
                for(SgMovimientoCuentaBancaria mov: egresosMovs){
                    SgMovimientoLiquidacionOtro movLiq = new SgMovimientoLiquidacionOtro();
                    movLiq.setMloLiquidacionOtroFk(liquidacion);
                    movLiq.setMloMovimientoFk(mov);
                    movLiq.setMloTipoMovimiento(EnumMovimientosTipo.E);
                    movLiquidacionOtroBean.guardar(movLiq, dataSecurity);

                    mov.setMcbEstadoLiquidacion(EnumEstadoLiquidacion.CONFIRMADA);
                    movCuentaBancBean.cobrar(mov,dataSecurity);
                }
            }

            return liquidacion;

            
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }


}   
