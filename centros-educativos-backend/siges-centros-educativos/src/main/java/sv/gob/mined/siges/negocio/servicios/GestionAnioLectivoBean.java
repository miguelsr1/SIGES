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
import sv.gob.mined.siges.filtros.FiltroGestionAnioLectivo;
import sv.gob.mined.siges.negocio.validaciones.GestionAnioLectivoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.GestionAnioLectivoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgGestionAnioLectivo;

@Stateless
@Traced
public class GestionAnioLectivoBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgGestionAnioLectivo
     * @throws GeneralException
     */
    public SgGestionAnioLectivo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgGestionAnioLectivo> codDAO = new CodigueraDAO<>(em, SgGestionAnioLectivo.class);
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
                CodigueraDAO<SgGestionAnioLectivo> codDAO = new CodigueraDAO<>(em, SgGestionAnioLectivo.class);
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
     * @param entity SgGestionAnioLectivo
     * @return SgGestionAnioLectivo
     * @throws GeneralException
     */
   @Interceptors(AuditInterceptor.class)
    public SgGestionAnioLectivo guardar(SgGestionAnioLectivo entity) throws GeneralException {
        try {
            if (entity != null) {
                if (GestionAnioLectivoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgGestionAnioLectivo> codDAO = new CodigueraDAO<>(em, SgGestionAnioLectivo.class);
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
                CodigueraDAO<SgGestionAnioLectivo> codDAO = new CodigueraDAO<>(em, SgGestionAnioLectivo.class);
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
    public Long obtenerTotalPorFiltro(FiltroGestionAnioLectivo filtro) throws GeneralException {
        try {
            GestionAnioLectivoDAO DAO = new GestionAnioLectivoDAO(em);
            return DAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgGestionAnioLectivo
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgGestionAnioLectivo> obtenerPorFiltro(FiltroGestionAnioLectivo filtro) throws GeneralException {
        try {
            GestionAnioLectivoDAO DAO = new GestionAnioLectivoDAO(em);
            return DAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}

