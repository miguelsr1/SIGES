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
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRelInmuebleServicio;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleServicioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleServicioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleServicioBasico;



@Stateless
public class RelInmuebleServicioBean {
    
    @PersistenceContext
    private EntityManager em;
   
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleServicioBasico
     * @throws GeneralException
     */
    
    public SgRelInmuebleServicioBasico obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleServicioBasico> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioBasico.class);
                return codDAO.obtenerPorId(id);
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
                CodigueraDAO<SgRelInmuebleServicioBasico> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioBasico.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgRelInmuebleServicioBasico
     * @return SgRelInmuebleServicioBasico
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgRelInmuebleServicioBasico guardar(SgRelInmuebleServicioBasico entity) throws GeneralException {
        try {
            if (entity != null) {
                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (RelInmuebleServicioValidacionNegocio.validar(entity)) {                 
                    CodigueraDAO<SgRelInmuebleServicioBasico> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioBasico.class);
                    return codDAO.guardar(entity);
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
     * @param filtro FiltroRelInmuebleServicio
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleServicio filtro) throws GeneralException {
        try {
            RelInmuebleServicioDAO codDAO = new RelInmuebleServicioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroRelInmuebleServicio
     * @return Lista de SgRelInmuebleServicioBasico
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgRelInmuebleServicioBasico> obtenerPorFiltro(FiltroRelInmuebleServicio filtro) throws GeneralException {
        try {
            RelInmuebleServicioDAO codDAO = new RelInmuebleServicioDAO(em);        
            return codDAO.obtenerPorFiltro(filtro,null);

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
                CodigueraDAO<SgRelInmuebleServicioBasico> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioBasico.class); 
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
