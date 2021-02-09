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
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroTrasladoDetalle;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TrasladosDetalleDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfTrasladosDetalle;

@Stateless
@Traced
public class TrasladosDetalleBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfDescargosDetalle
     * @throws GeneralException
     * 
     */
    public SgAfTrasladosDetalle obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfTrasladosDetalle> codDAO = new CodigueraDAO<>(em, SgAfTrasladosDetalle.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
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
                CodigueraDAO<SgAfTrasladosDetalle> codDAO = new CodigueraDAO<>(em, SgAfTrasladosDetalle.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SOLICITUDES_TRASLADO_BIENES);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroTrasladoDetalle
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTrasladoDetalle filtro) throws GeneralException {
        try {
            TrasladosDetalleDAO codDAO = new TrasladosDetalleDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroTrasladoDetalle
     * @return Lista de <SgAfTrasladosDetalle>
     * @throws GeneralException
     */
    public List<SgAfTrasladosDetalle> obtenerPorFiltro(FiltroTrasladoDetalle filtro) throws GeneralException {
        try {
            TrasladosDetalleDAO codDAO = new TrasladosDetalleDAO(em);
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
                CodigueraDAO<SgAfTrasladosDetalle> codDAO = new CodigueraDAO<>(em, SgAfTrasladosDetalle.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    /**
     * Elimina el objeto indicado
     * @param det objeto a eliminar
     * @throws GeneralException 
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(SgAfTrasladosDetalle det) throws GeneralException {
        if (det != null) {
            try {
                CodigueraDAO<SgAfTrasladosDetalle> codDAO = new CodigueraDAO<>(em, SgAfTrasladosDetalle.class);
                codDAO.eliminar(det, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    /**
     * Elimina el o los bienes de un detalle de traslado
     * @param idBien 
     */
    public void eliminarPorIdBien(Long idBien) {
        if (idBien != null) {
            try {
                FiltroTrasladoDetalle filtroTraslado = new FiltroTrasladoDetalle();
                filtroTraslado.setBienId(idBien);
                List<SgAfTrasladosDetalle> listaTraslado = obtenerPorFiltro(filtroTraslado);//Obtendrá como maximo un registro
                if(listaTraslado != null && !listaTraslado.isEmpty()) {
                    for(SgAfTrasladosDetalle desDetalle : listaTraslado) {
                       eliminar(desDetalle);
                    }
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}