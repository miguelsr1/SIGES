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
import sv.gob.mined.siges.filtros.FiltroRelPersonalEspecialidad;
import sv.gob.mined.siges.negocio.validaciones.RelPersonalEspecialidadValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.RelPersonalEspecialidadDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad;

@Stateless
@Traced
public class RelPersonalEspecialidadBean {
    
    @PersistenceContext
    private EntityManager em;
    

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelPersonalEspecialidad
     * @throws GeneralException
     */
    public SgRelPersonalEspecialidad obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgRelPersonalEspecialidad> codDAO = new CodigueraDAO<>(em, SgRelPersonalEspecialidad.class);
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
                CodigueraDAO<SgRelPersonalEspecialidad> codDAO = new CodigueraDAO<>(em, SgRelPersonalEspecialidad.class);
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
     * @param entity SgRelPersonalEspecialidad
     * @return SgRelPersonalEspecialidad
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgRelPersonalEspecialidad guardar(SgRelPersonalEspecialidad entity)throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (RelPersonalEspecialidadValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgRelPersonalEspecialidad> codDAO = new CodigueraDAO<>(em, SgRelPersonalEspecialidad.class);
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
    public Long obtenerTotalPorFiltro(FiltroRelPersonalEspecialidad filtro) throws GeneralException {
        try {
            RelPersonalEspecialidadDAO codDAO = new RelPersonalEspecialidadDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgRelPersonalEspecialidad
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgRelPersonalEspecialidad> obtenerPorFiltro(FiltroRelPersonalEspecialidad filtro) throws GeneralException {
        try {
            RelPersonalEspecialidadDAO codDAO = new RelPersonalEspecialidadDAO(em);
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
                CodigueraDAO<SgRelPersonalEspecialidad> codDAO = new CodigueraDAO(em,SgRelPersonalEspecialidad.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
