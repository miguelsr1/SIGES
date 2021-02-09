/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroNotificacionCumplimiento;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.NotificacionCumplimientoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfNotificacionCumplimiento;

@Stateless
@Traced
public class NotificacionCumplimientoBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfLoteBienes
     * @throws GeneralException
     * 
     */
    public SgAfNotificacionCumplimiento obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfNotificacionCumplimiento> codDAO = new CodigueraDAO<>(em, SgAfNotificacionCumplimiento.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_NOTIFICACIONES_CUMPLIMIENTO);
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
                CodigueraDAO<SgAfNotificacionCumplimiento> codDAO = new CodigueraDAO<>(em, SgAfNotificacionCumplimiento.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_NOTIFICACIONES_CUMPLIMIENTO);
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
     * @param entity SgAfNotificacionCumplimiento
     * @return SgAfNotificacionCumplimiento
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfNotificacionCumplimiento guardar(SgAfNotificacionCumplimiento entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                //if (LoteBienesValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgAfNotificacionCumplimiento> codDAO = new CodigueraDAO<>(em, SgAfNotificacionCumplimiento.class);
                    if (BooleanUtils.isTrue(dataSecurity)){
                        entity = codDAO.guardar(entity, entity.getNcuId()== null ? ConstantesOperaciones.CREAR_NOTIFICACION_CUMPLIMIENTO : ConstantesOperaciones.ACTUALIZAR_NOTIFICACION_CUMPLIMIENTO);
                    } else {
                        entity = codDAO.guardar(entity, null);
                    }
                //}
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }
    
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro SgAfNotificacionCumplimiento
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroNotificacionCumplimiento filtro) throws GeneralException {
        try {
            NotificacionCumplimientoDAO codDAO = new NotificacionCumplimientoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_NOTIFICACIONES_CUMPLIMIENTO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroNotificacionCumplimiento
     * @return Lista de <SgAfNotificacionCumplimiento>
     * @throws GeneralException
     */
    public List<SgAfNotificacionCumplimiento> obtenerPorFiltro(FiltroNotificacionCumplimiento filtro) throws GeneralException {
        try {
            NotificacionCumplimientoDAO codDAO = new NotificacionCumplimientoDAO(em);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_NOTIFICACIONES_CUMPLIMIENTO);
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
    public void eliminar(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfNotificacionCumplimiento> codDAO = new CodigueraDAO<>(em, SgAfNotificacionCumplimiento.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_NOTIFICACION_CUMPLIMIENTO);
                } else {
                    eliminar(id, null);
                }
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
}
