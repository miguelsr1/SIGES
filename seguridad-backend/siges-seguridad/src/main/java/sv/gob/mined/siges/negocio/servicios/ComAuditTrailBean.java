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
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroComAuditTrail;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ComAuditTrailDAO;
import sv.gob.mined.siges.persistencia.entidades.ComAuditTrail;
import sv.gob.mined.siges.persistencia.utilidades.LoadLazyCollectionsViewInterceptor;

@Stateless
public class ComAuditTrailBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return ComAuditTrail
     * @throws GeneralException
     */
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public ComAuditTrail obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<ComAuditTrail> codDAO = new CodigueraDAO<>(em, ComAuditTrail.class);
                ComAuditTrail rol =  codDAO.obtenerPorId(id);
                return rol;
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
                CodigueraDAO<ComAuditTrail> codDAO = new CodigueraDAO<>(em, ComAuditTrail.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroComAuditTrail
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroComAuditTrail filtro) throws GeneralException {
        try {
            ComAuditTrailDAO codDAO = new ComAuditTrailDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroComAuditTrail
     * @return Lista de ComAuditTrail
     * @throws GeneralException
     */
    public List<ComAuditTrail> obtenerPorFiltro(FiltroComAuditTrail filtro) throws GeneralException {
        try {
            ComAuditTrailDAO codDAO = new ComAuditTrailDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
}
