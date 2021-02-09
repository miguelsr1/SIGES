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
import sv.gob.mined.siges.filtros.FiltroCasoViolacion;
import sv.gob.mined.siges.negocio.validaciones.CasoViolacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.CasoViolacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCasoViolacion;

@Stateless
@Traced
public class CasoViolacionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCasoViolacion
     * @throws GeneralException
     */
    public SgCasoViolacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgCasoViolacion> codDAO = new CodigueraDAO<>(em, SgCasoViolacion.class);
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
                CodigueraDAO<SgCasoViolacion> codDAO = new CodigueraDAO<>(em, SgCasoViolacion.class);
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
     * @param entity SgCasoViolacion
     * @return SgCasoViolacion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgCasoViolacion guardar(SgCasoViolacion entity) throws GeneralException {
        try {
            if (entity != null) {
                if (CasoViolacionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgCasoViolacion> codDAO = new CodigueraDAO<>(em, SgCasoViolacion.class);
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
                CodigueraDAO<SgCasoViolacion> codDAO = new CodigueraDAO<>(em, SgCasoViolacion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCasoViolacion
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCasoViolacion filtro) throws GeneralException {
        try {
            CasoViolacionDAO codDAO = new CasoViolacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCasoViolacion
     * @return Lista de SgCasoViolacion
     * @throws GeneralException
     */     
    public List<SgCasoViolacion> obtenerPorFiltro(FiltroCasoViolacion filtro) throws GeneralException {
        try {
            CasoViolacionDAO codDAO = new CasoViolacionDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
