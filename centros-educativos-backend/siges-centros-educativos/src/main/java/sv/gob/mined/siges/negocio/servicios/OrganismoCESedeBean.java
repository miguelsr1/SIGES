/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroOrganismoCESede;
import sv.gob.mined.siges.negocio.validaciones.OrganismoCESedeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.OrganismoCESedeDAO;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoCESede;

@Stateless
@Traced
public class OrganismoCESedeBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgOrganismoCESede
     * @throws GeneralException
     */
    public SgOrganismoCESede obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgOrganismoCESede> codDAO = new CodigueraDAO<>(em, SgOrganismoCESede.class);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOrganismoCESede> codDAO = new CodigueraDAO<>(em, SgOrganismoCESede.class);
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
     * @param entity SgOrganismoCESede
     * @return SgOrganismoCESede
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgOrganismoCESede guardar(SgOrganismoCESede entity) throws GeneralException {
        try {
            if (entity != null) {
                if (OrganismoCESedeValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgOrganismoCESede> codDAO = new CodigueraDAO<>(em, SgOrganismoCESede.class);
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
                CodigueraDAO<SgOrganismoCESede> codDAO = new CodigueraDAO<>(em, SgOrganismoCESede.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroOrganismoCESede
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroOrganismoCESede filtro) throws GeneralException {
        try {
            OrganismoCESedeDAO codDAO = new OrganismoCESedeDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroOrganismoCESede
     * @return Lista de SgOrganismoCESede
     * @throws GeneralException
     */     
    public List<SgOrganismoCESede> obtenerPorFiltro(FiltroOrganismoCESede filtro) throws GeneralException {
        try {
            OrganismoCESedeDAO codDAO = new OrganismoCESedeDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
