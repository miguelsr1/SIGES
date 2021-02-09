/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesAyuda;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.DesembolsoDDE;
import sv.gob.mined.siges.dto.DesembolsoMovimiento;
import sv.gob.mined.siges.dto.RequerimientoPorRecurso;
import sv.gob.mined.siges.enumerados.EnumDesembolsoEstado;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDesembolso;
import sv.gob.mined.siges.filtros.FiltroDetalleDesembolso;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DesembolsosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDesembolso;
import sv.gob.mined.siges.persistencia.entidades.SgDetalleDesembolso;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoDireccionDepartamental;
import sv.gob.mined.siges.persistencia.entidades.SgRequerimientoFondo;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsFuenteRecursos;


/**
 * Servicio que gestiona los desembolsos
 *
 * @author Sofis Solutions
 */
@Stateless
public class DesembolsoBean {
    
    private static final Logger LOGGER = Logger.getLogger(DesembolsoBean.class.getName());
    
    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;
    
    @Inject 
    private DetalleDesembolsoBean detalleDesembolsoBean;
    
    @Inject 
    private MovimientoDireccionDepartamentalBean movimientoDDEBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientos
     * @throws GeneralException
     *
     */
    //@Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgDesembolso obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDesembolso> codDAO = new CodigueraDAO<>(em, SgDesembolso.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_DESEMBOLSO);
                } else {
                    return codDAO.obtenerPorId(id, null);
                }
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDesembolso> codDAO = new CodigueraDAO<>(em, SgDesembolso.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_DESEMBOLSO);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgDesembolso
     * @return SgDesembolso
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDesembolso guardar(SgDesembolso entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                CodigueraDAO<SgDesembolso> codDAO = new CodigueraDAO<>(em, SgDesembolso.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    if(entity.getDsbPk() == null){
                        return codDAO.guardar(entity,ConstantesOperaciones.CREAR_DESEMBOLSO);
                    }
                    else{
                        entity.setDsbDetalleDesembolso(new ArrayList<>());
                        return codDAO.guardar(entity,ConstantesOperaciones.ACTUALIZAR_DESEMBOLSO);
                    }
                    
                } else {
                    return codDAO.guardar(entity, null);
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
     * Guarda el objeto indicado con su detalle
     *
     * @param list List
     * @return SgDesembolso
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDesembolso guardarConDetalle(DesembolsoDDE des, Boolean dataSecurity) throws GeneralException {
        SgDesembolso entity = new SgDesembolso();
        try {
            
           
            
            entity.setDsbPk(null);
            entity.setDsbEstado(EnumDesembolsoEstado.DEPOSITADO);
            entity.setDsbVersion(0);
            
            entity=guardar(entity, dataSecurity);
            
            if (entity != null && des.getListReqsRecurso()!=null &&  !des.getListReqsRecurso().isEmpty()) {
                List<SgDetalleDesembolso> detallesDesembolso = new ArrayList();
                for(RequerimientoPorRecurso req: des.getListReqsRecurso()){
                    SgDetalleDesembolso detalle = new SgDetalleDesembolso();
                    detalle.setDedDesembolsoFk(entity);
                    detalle.setDedReqFondoFk(em.getReference(SgRequerimientoFondo.class, req.getReqPk()));
                    detalle.setDedFuenteRecursosFk(em.getReference(SsFuenteRecursos.class, req.getRecursoPk()));
                    detalle.setDedMonto(req.getMontoDesembolso());
                    detalle.setDedPorcentaje(req.getPorcentaje());
                    detalle = detalleDesembolsoBean.guardar(detalle, dataSecurity);
                    
                    
                    SgMovimientoDireccionDepartamental mov = new SgMovimientoDireccionDepartamental();
                    mov.setMddCuentaFK(detalle.getDedReqFondoFk().getStrCuentaBancDdFk());
                    mov.setMddDetDesembolsoFk(detalle);
                    String detalle1 = ConstantesAyuda.DESEMBOLSO.concat("# ").concat(entity.getDsbPk().toString()).concat(" - ").concat(ConstantesAyuda.REQUERIMIENTO).concat("# ").concat(detalle.getDedReqFondoFk().getStrPk().toString());
                    mov.setMddDetalle(detalle1);
                    mov.setMddMonto(detalle.getDedMonto());
                    mov.setMddTipoMovimiento(TipoMovimiento.HABER);
                    mov.setMddFecha(des.getFechaMov());
                    movimientoDDEBean.guardar(mov);
                    
                    detallesDesembolso.add(detalle);
                    
                }
                //entity.setDsbDetalleDesembolso(detallesDesembolso);
                entity.setDsbMonto(detallesDesembolso.stream().map(d-> d.getDedMonto()).reduce(BigDecimal.ZERO, BigDecimal::add));
                
                entity=guardar(entity, dataSecurity);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    
    /**
     * Guarda los movimientos a las cuentas bancarias de las DDE
     *
     * @param des DesembolsoMovimiento
     * @return SgDesembolso
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDesembolso generarMovimientos(DesembolsoMovimiento des, Boolean dataSecurity) throws GeneralException {
        SgDesembolso entity = new SgDesembolso();
        try {
//            
//            if(des.getDesembolsoPk()!=null){
//                entity = obtenerPorId(des.getDesembolsoPk(), dataSecurity);
//            }
            
            FiltroDetalleDesembolso filtroDet = new FiltroDetalleDesembolso();
            filtroDet.setIncluirCampos(new String[]{
                "dedPk","dedReqFondoFk.strPk","dedReqFondoFk.strVersion",
                "dedReqFondoFk.strCuentaBancDdFk.cbdPk",
                "dedReqFondoFk.strCuentaBancDdFk.cbdNumeroCuenta",
                "dedReqFondoFk.strCuentaBancDdFk.cbdVersion",
                "dedMonto",
                "dedVersion"});
            
            List<SgDetalleDesembolso> detallesDesembolso=detalleDesembolsoBean.obtenerPorFiltro(filtroDet);
            
            if (entity != null && detallesDesembolso!=null &&  !detallesDesembolso.isEmpty()) {
                for(SgDetalleDesembolso det: detallesDesembolso){
                    SgMovimientoDireccionDepartamental mov = new SgMovimientoDireccionDepartamental();
                    mov.setMddCuentaFK(det.getDedReqFondoFk().getStrCuentaBancDdFk());
                    mov.setMddDetDesembolsoFk(det);
                    String detalle = ConstantesAyuda.DESEMBOLSO.concat("# ").concat(entity.getDsbPk().toString()).concat(" - ").concat(ConstantesAyuda.REQUERIMIENTO).concat("# ").concat(det.getDedReqFondoFk().getStrPk().toString());
                    mov.setMddDetalle(detalle);
                    mov.setMddMonto(det.getDedMonto());
                    mov.setMddTipoMovimiento(TipoMovimiento.HABER);
                    mov.setMddFecha(des.getFechaMov());
                    movimientoDDEBean.guardar(mov);
                } 
            }
            
            entity.setDsbEstado(EnumDesembolsoEstado.DEPOSITADO);
            return guardar(entity, dataSecurity);
            
            
        } catch (BusinessException be) {
            throw be;
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
                CodigueraDAO<SgDesembolso> codDAO = new CodigueraDAO<>(em, SgDesembolso.class
                );
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param filtro
     * @param dataSecurity
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgDesembolso> obtenerPorFiltro(FiltroDesembolso filtro) throws GeneralException {
        try {
            DesembolsosDAO codDAO = new DesembolsosDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_DESEMBOLSO);
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
    public Long obtenerTotalPorFiltro(FiltroDesembolso filtro) throws GeneralException {
        try {
            DesembolsosDAO codDAO = new DesembolsosDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_DESEMBOLSO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
     /**
     * Eliminar los detalle de desembolso de un desembolso
     *
     * @param Long dsbPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarDetalleDesembolso(Long dsbPk) throws GeneralException {
        try {
            if (dsbPk != null) {
                em.createQuery("DELETE FROM SgDetalleDesembolso where dedDesembolsoFk.dsbPk = :dsbPk")
                        .setParameter("dsbPk", dsbPk)
                        .executeUpdate();
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    
}
