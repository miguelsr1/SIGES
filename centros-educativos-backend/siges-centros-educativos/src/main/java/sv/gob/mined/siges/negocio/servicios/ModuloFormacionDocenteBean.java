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
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroModuloFormacionDocente;
import sv.gob.mined.siges.negocio.validaciones.ModuloFormacionDocenteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.ModuloFormacionDocenteDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgModuloFormacionDocente;

@Stateless
@Traced
public class ModuloFormacionDocenteBean {
    
    @PersistenceContext
    private EntityManager em;
    

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgModuloFormacionDocente
     * @throws GeneralException
     */
    public SgModuloFormacionDocente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
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
                CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
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
     * @param entity SgModuloFormacionDocente
     * @return SgModuloFormacionDocente
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgModuloFormacionDocente guardar(SgModuloFormacionDocente entity)throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ModuloFormacionDocenteValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
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
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroModuloFormacionDocente filtro) throws GeneralException {
        try {
            ModuloFormacionDocenteDAO codDAO = new ModuloFormacionDocenteDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgModuloFormacionDocente
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgModuloFormacionDocente> obtenerPorFiltro(FiltroModuloFormacionDocente filtro) throws GeneralException {
        try {
            ModuloFormacionDocenteDAO codDAO = new ModuloFormacionDocenteDAO(em);
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
                CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO(em,SgModuloFormacionDocente.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
