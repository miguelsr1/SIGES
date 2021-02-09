/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRelOpcionProgEd;
import sv.gob.mined.siges.negocio.validaciones.RelOpcionProgEdValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelOpcionProgEdDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelOpcionProgEd;

@Stateless
@Traced
public class RelOpcionProgEdBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelOpcionProgEd
     * @throws GeneralException
     */
    public SgRelOpcionProgEd obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgRelOpcionProgEd> codDAO = new CodigueraDAO<>(em,SgRelOpcionProgEd.class);
                return codDAO.obtenerPorId(id, null);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelOpcionProgEd> codDAO = new CodigueraDAO<>(em, SgRelOpcionProgEd.class);
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
     * @param entity SgRelOpcionProgEd
     * @return SgRelOpcionProgEd
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelOpcionProgEd guardar(SgRelOpcionProgEd entity)throws GeneralException {
        try {
            if (entity != null) {
                if (RelOpcionProgEdValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgRelOpcionProgEd> codDAO = new CodigueraDAO<>(em, SgRelOpcionProgEd.class);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelOpcionProgEd> codDAO = new CodigueraDAO<>(em, SgRelOpcionProgEd.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
   
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroRelOpcionProgEd filtro) throws GeneralException {
        try {
            RelOpcionProgEdDAO dao = new RelOpcionProgEdDAO(em);
            return dao.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgRelOpcionProgEd
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgRelOpcionProgEd> obtenerPorFiltro(FiltroRelOpcionProgEd filtro) throws GeneralException {
        try {
            RelOpcionProgEdDAO dao = new RelOpcionProgEdDAO(em);
            return dao.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
