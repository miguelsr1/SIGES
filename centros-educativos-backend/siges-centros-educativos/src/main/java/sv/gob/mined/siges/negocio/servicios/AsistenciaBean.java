/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
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
import sv.gob.mined.siges.filtros.FiltroAsistencia;
import sv.gob.mined.siges.negocio.validaciones.AsistenciaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.AsistenciaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;

@Stateless
@Traced
public class AsistenciaBean {
   
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgAsistencia
     * @throws GeneralException
     */
    public SgAsistencia obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                AsistenciaDAO dao = new AsistenciaDAO(em);
                return dao.obtenerPorId(id);
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
                CodigueraDAO<SgAsistencia> codDAO = new CodigueraDAO<>(em, SgAsistencia.class);
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
     * @return SgAsistencia
     * @throws GeneralException
     */
   @Interceptors({AuditInterceptor.class})
    public SgAsistencia guardar(SgAsistencia entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (AsistenciaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgAsistencia> codDAO = new CodigueraDAO<>(em, SgAsistencia.class);
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
    public Long obtenerTotalPorFiltro(FiltroAsistencia filtro) throws GeneralException {
        try {
            AsistenciaDAO codDAO = new AsistenciaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
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
    public List<SgAsistencia> obtenerPorFiltro(FiltroAsistencia filtro) throws GeneralException {
        try {
            AsistenciaDAO codDAO = new AsistenciaDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgAsistencia> codDAO = new CodigueraDAO<>(em, SgAsistencia.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
