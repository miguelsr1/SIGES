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
import sv.gob.mined.siges.filtros.FiltroManifestacionViolencia;
import sv.gob.mined.siges.negocio.validaciones.ManifestacionViolenciaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ManifestacionViolenciaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgManifestacionViolencia;

@Stateless
@Traced
public class ManifestacionViolenciaBean {
   
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgManifestacionViolencia
     * @throws GeneralException
     */
    public SgManifestacionViolencia obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgManifestacionViolencia> codDAO = new CodigueraDAO<>(em, SgManifestacionViolencia.class);
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
     * @param id
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgManifestacionViolencia> codDAO = new CodigueraDAO<>(em, SgManifestacionViolencia.class);
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
     * @param entity
     * @return SgManifestacionViolencia
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgManifestacionViolencia guardar(SgManifestacionViolencia entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ManifestacionViolenciaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgManifestacionViolencia> codDAO = new CodigueraDAO<>(em, SgManifestacionViolencia.class);
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
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroManifestacionViolencia filtro) throws GeneralException {
        try {
            ManifestacionViolenciaDAO dao = new ManifestacionViolenciaDAO(em);
            return dao.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgManifestacionViolencia> obtenerPorFiltro(FiltroManifestacionViolencia filtro) throws GeneralException {
        try {
            ManifestacionViolenciaDAO dao = new ManifestacionViolenciaDAO(em);
            return dao.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgManifestacionViolencia> codDAO = new CodigueraDAO<>(em, SgManifestacionViolencia.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
