/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPagina;
import sv.gob.mined.siges.negocio.validaciones.PaginaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PaginaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRhPagina;

@Stateless
public class PaginaBean {
    
    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;    
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRhPagina
     * @throws GeneralException
     */
    public SgRhPagina obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgRhPagina> codDAO = new CodigueraDAO<>(em, SgRhPagina.class);
                SgRhPagina c = codDAO.obtenerPorId(id, null);         
                return c;
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
                CodigueraDAO<SgRhPagina> codDAO = new CodigueraDAO<>(em, SgRhPagina.class);
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
     * @param entity SgRhPagina
     * @return SgRhPagina
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgRhPagina guardar(SgRhPagina entity) throws GeneralException {
        try {
            if (entity != null) {
                if (PaginaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgRhPagina> codDAO = new CodigueraDAO<>(em, SgRhPagina.class);
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
                CodigueraDAO<SgRhPagina> codDAO = new CodigueraDAO<>(em, SgRhPagina.class);
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
    public Long obtenerTotalPorFiltro(FiltroPagina filtro) throws GeneralException {
        try {
            PaginaDAO codDAO = new PaginaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgRhPagina
     * @throws GeneralException
     */
    public List<SgRhPagina> obtenerPorFiltro(FiltroPagina filtro) throws GeneralException {
        try {
            PaginaDAO codDAO = new PaginaDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
