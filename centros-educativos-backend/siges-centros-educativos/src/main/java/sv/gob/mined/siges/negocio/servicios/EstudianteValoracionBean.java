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
import sv.gob.mined.siges.filtros.FiltroEstudianteValoracion;
import sv.gob.mined.siges.negocio.validaciones.EstudianteValoracionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EstudianteValoracionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteValoracion;

@Stateless
@Traced
public class EstudianteValoracionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstudianteValoracion
     * @throws GeneralException
     */
    public SgEstudianteValoracion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstudianteValoracion> dao = new CodigueraDAO<>(em,SgEstudianteValoracion.class);
                return dao.obtenerPorId(id, null);
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
                CodigueraDAO<SgEstudianteValoracion> codDAO = new CodigueraDAO<>(em, SgEstudianteValoracion.class);
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
     * @param entity SgEstudianteValoracion
     * @return SgEstudianteValoracion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgEstudianteValoracion guardar(SgEstudianteValoracion entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (EstudianteValoracionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgEstudianteValoracion> codDAO = new CodigueraDAO<>(em, SgEstudianteValoracion.class);
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
    public Long obtenerTotalPorFiltro(FiltroEstudianteValoracion filtro) throws GeneralException {
        try {
            EstudianteValoracionDAO dao = new EstudianteValoracionDAO(em);
            return dao.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEstudianteValoracion
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgEstudianteValoracion> obtenerPorFiltro(FiltroEstudianteValoracion filtro) throws GeneralException {
        try {
            EstudianteValoracionDAO dao = new EstudianteValoracionDAO(em);
            return dao.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgEstudianteValoracion> codDAO = new CodigueraDAO<>(em, SgEstudianteValoracion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}