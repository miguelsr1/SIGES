/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;


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
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroLiquidacion;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.LiquidacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.LiquidacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgLiquidacion;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoLiquidacion;

/**
 * Servicio que gestiona liquidación
 * @author Sofis Solutions
 */
@Stateless
public class LiquidacionBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private MovimientoLiquidacionBean movLiquidacionBean;
    
    @Inject
    private MovimientoCuentaBancariaBean movCuentaBancBean;
    
    @Inject
    private FacturaBean facturaBean;
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgLiquidacion
     * @throws GeneralException
     */
    public SgLiquidacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgLiquidacion> codDAO = new CodigueraDAO<>(em, SgLiquidacion.class);
                return codDAO.obtenerPorId(id,ConstantesOperaciones.BUSCAR_LIQUIDACION);
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
                CodigueraDAO<SgLiquidacion> codDAO = new CodigueraDAO<>(em, SgLiquidacion.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param liq SgLiquidacion
     * @return SgLiquidacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgLiquidacion guardar(SgLiquidacion liq,Boolean dataSecurity) throws GeneralException {
        try {
            if (liq != null) {
                if (LiquidacionValidacionNegocio.validar(liq)) {
                    CodigueraDAO<SgLiquidacion> codDAO = new CodigueraDAO<>(em, SgLiquidacion.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(liq,liq.getLiqPk() == null ? ConstantesOperaciones.CREAR_LIQUIDACION : ConstantesOperaciones.ACTUALIZAR_LIQUIDACION);
                    } else {
                        return codDAO.guardar(liq, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return liq;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroLiquidacion filtro) throws GeneralException {
        try {
            LiquidacionDAO codDAO = new LiquidacionDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_LIQUIDACION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgLiquidacion>
     * @throws GeneralException
     */
    public List<SgLiquidacion> obtenerPorFiltro(FiltroLiquidacion filtro) throws GeneralException {
        try {
            LiquidacionDAO codDAO = new LiquidacionDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_LIQUIDACION);
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
                CodigueraDAO<SgLiquidacion> codDAO = new CodigueraDAO<>(em, SgLiquidacion.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.ELIMINAR_LIQUIDACION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Guarda el objeto indicado
     *
     * @param liq SgLiquidacion
     * @return SgLiquidacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgLiquidacion guardarConMovimientos(SgLiquidacion liq,Boolean dataSecurity) throws GeneralException {
        try {
            Long ingresos = 0L;
            BusinessException be = new BusinessException();
            SgLiquidacion liquidacion = new SgLiquidacion();

            if(liq.getMovimientosLiquidacion()!=null && !liq.getMovimientosLiquidacion().isEmpty()){
                ingresos = liq.getMovimientosLiquidacion().stream().filter(m-> m.getMlqTipoMovimiento().equals(EnumMovimientosTipo.I)).count();
                if(ingresos.equals(0L)){
                    be.addError(Errores.ERROR_LIQUIDACION_INGRESOS_GUARDAR);
                    throw be;
                }
                liquidacion.setLiqSedePk(liq.getLiqSedePk());
                liquidacion.setLiqSubComponenteFk(liq.getLiqSubComponenteFk());
                liquidacion.setLiqComponentePk(liq.getLiqComponentePk());
                liquidacion.setLiqAnioPk(liq.getLiqAnioPk());
                liquidacion.setLiqEstado(liq.getLiqEstado());
                liquidacion = guardar(liquidacion,dataSecurity);

                for(SgMovimientoLiquidacion movLiq: liq.getMovimientosLiquidacion().stream().filter(m-> m.getMlqTipoMovimiento().equals(EnumMovimientosTipo.I)).collect(Collectors.toList())){
                    
                    movLiq.setMlqLiquidacionPk(liquidacion);
                    movLiq.getMlqMovimientoPk().setMcbEstadoLiquidacion(EnumEstadoLiquidacion.CONFIRMADA);
                    movLiquidacionBean.guardar(movLiq, dataSecurity);
                    
                    SgMovimientoCuentaBancaria mov = em.getReference(SgMovimientoCuentaBancaria.class, movLiq.getMlqMovimientoPk().getMcbPk());
                    mov.setMcbEstadoLiquidacion(EnumEstadoLiquidacion.CONFIRMADA);
                    movCuentaBancBean.cobrar(mov,dataSecurity);
                }
                
                List<SgMovimientoLiquidacion> egresosMovs = liq.getMovimientosLiquidacion().stream().filter(m-> m.getMlqTipoMovimiento().equals(EnumMovimientosTipo.E)).collect(Collectors.toList());
                if(egresosMovs!=null && !egresosMovs.isEmpty() && liquidacion.getLiqPk()!=null){
                    for(SgMovimientoLiquidacion movLiq: egresosMovs){
                        movLiq.setMlqLiquidacionPk(liquidacion);
                        movLiq.getMlqMovimientoPk().setMcbEstadoLiquidacion(EnumEstadoLiquidacion.CONFIRMADA);
                        movLiquidacionBean.guardar(movLiq, dataSecurity);
                        
                        SgMovimientoCuentaBancaria mov = em.getReference(SgMovimientoCuentaBancaria.class, movLiq.getMlqMovimientoPk().getMcbPk());
                        mov.setMcbEstadoLiquidacion(EnumEstadoLiquidacion.CONFIRMADA);
                        movCuentaBancBean.cobrar(mov,dataSecurity);
                    }
                }
            }
            else{
                be.addError(Errores.ERROR_LIQUIDACION_INGRESOS_GUARDAR);
                throw be;
            }

           
            return liquidacion;
            
            
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }


}
