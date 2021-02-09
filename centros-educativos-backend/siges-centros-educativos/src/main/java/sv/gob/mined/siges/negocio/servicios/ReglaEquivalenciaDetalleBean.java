/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroReglaEquivalenciaDetalle;
import sv.gob.mined.siges.negocio.validaciones.ReglaEquivalenciaDetalleValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ReglaEquivalenciaDetalleDAO;
import sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalenciaDetalle;

@Stateless
@Traced
public class ReglaEquivalenciaDetalleBean {
    
@PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgReglaEquivalenciaDetalle
     * @throws GeneralException
     */
    public SgReglaEquivalenciaDetalle obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgReglaEquivalenciaDetalle> codDAO = new CodigueraDAO<>(em, SgReglaEquivalenciaDetalle.class);
                SgReglaEquivalenciaDetalle ret = codDAO.obtenerPorId(id, null);
                return ret;
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
                CodigueraDAO<SgReglaEquivalenciaDetalle> codDAO = new CodigueraDAO<>(em, SgReglaEquivalenciaDetalle.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgReglaEquivalenciaDetalle
     * @return SgReglaEquivalenciaDetalle
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgReglaEquivalenciaDetalle guardar(SgReglaEquivalenciaDetalle entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ReglaEquivalenciaDetalleValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgReglaEquivalenciaDetalle> codDAO = new CodigueraDAO<>(em, SgReglaEquivalenciaDetalle.class);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroReglaEquivalenciaDetalle
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroReglaEquivalenciaDetalle filtro) throws GeneralException {
        try {
            ReglaEquivalenciaDetalleDAO codDAO = new ReglaEquivalenciaDetalleDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroReglaEquivalenciaDetalle
     * @return Lista de SgReglaEquivalenciaDetalle
     * @throws GeneralException
     */
    public List<SgReglaEquivalenciaDetalle> obtenerPorFiltro(FiltroReglaEquivalenciaDetalle filtro) throws GeneralException {
        try {
            ReglaEquivalenciaDetalleDAO codDAO = new ReglaEquivalenciaDetalleDAO(em);
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
                CodigueraDAO<SgReglaEquivalenciaDetalle> codDAO = new CodigueraDAO<>(em, SgReglaEquivalenciaDetalle.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}

