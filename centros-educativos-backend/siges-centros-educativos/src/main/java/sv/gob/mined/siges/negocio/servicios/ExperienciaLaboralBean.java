/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroExperienciaLaboral;
import sv.gob.mined.siges.negocio.validaciones.ExperienciaLaboralValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ExperienciaLaboralDAO;
import sv.gob.mined.siges.persistencia.entidades.SgExperienciaLaboral;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class ExperienciaLaboralBean {
    
    
    @PersistenceContext
    private EntityManager em;
    
    private static final Logger LOGGER = Logger.getLogger(ExperienciaLaboralBean.class.getName());
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgExperienciaLaboral
     * @throws GeneralException
     */
    public SgExperienciaLaboral obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgExperienciaLaboral> codDAO = new CodigueraDAO<>(em, SgExperienciaLaboral.class);
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
                CodigueraDAO<SgExperienciaLaboral> codDAO = new CodigueraDAO<>(em, SgExperienciaLaboral.class);
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
     * @param entity SgExperienciaLaboral
     * @return SgExperienciaLaboral
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgExperienciaLaboral guardar(SgExperienciaLaboral entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ExperienciaLaboralValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgExperienciaLaboral> codDAO = new CodigueraDAO<>(em, SgExperienciaLaboral.class);
                    
                    if (entity.getElaPk() != null) {
                        SgExperienciaLaboral comp = codDAO.obtenerPorId(entity.getElaPk(), null);
                        if (!ExperienciaLaboralValidacionNegocio.compararParaGrabar(entity, comp)) {
                            entity.setElaValidada(Boolean.FALSE);
                            entity.setElaUltValidacionFecha(null);
                            entity.setElaUltValidacionUsuario(null);
                        }
                    }
                    
                    
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
     * Valida la experiencia indicada
     *
     * @param Long expPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgExperienciaLaboral validarRealizado(Long expPk) throws GeneralException {
        try {
            if (expPk != null) {
                CodigueraDAO<SgExperienciaLaboral> codDAO = new CodigueraDAO<>(em, SgExperienciaLaboral.class);          
                SgExperienciaLaboral exp = codDAO.obtenerPorId(expPk, null);
                exp.setElaValidada(Boolean.TRUE);
                exp.setElaUltValidacionFecha(LocalDateTime.now());
                exp.setElaUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
                return codDAO.guardar(exp, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }
    
    /**
     * Invalida la experiencia indicada
     *
     * @param Long expPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgExperienciaLaboral invalidarRealizado(Long expPk) throws GeneralException {
        try {
            if (expPk != null) {
                CodigueraDAO<SgExperienciaLaboral> codDAO = new CodigueraDAO<>(em, SgExperienciaLaboral.class);          
                SgExperienciaLaboral exp = codDAO.obtenerPorId(expPk, null);
                exp.setElaValidada(Boolean.FALSE);
                exp.setElaUltValidacionFecha(LocalDateTime.now());
                exp.setElaUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
                return codDAO.guardar(exp, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
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
                CodigueraDAO<SgExperienciaLaboral> codDAO = new CodigueraDAO<>(em, SgExperienciaLaboral.class);
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
    public Long obtenerTotalPorFiltro(FiltroExperienciaLaboral filtro) throws GeneralException {
        try {
            ExperienciaLaboralDAO codDAO = new ExperienciaLaboralDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }  
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgExperienciaLaboral
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgExperienciaLaboral> obtenerPorFiltro(FiltroExperienciaLaboral filtro) throws GeneralException {
        try {
            ExperienciaLaboralDAO codDAO = new ExperienciaLaboralDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
