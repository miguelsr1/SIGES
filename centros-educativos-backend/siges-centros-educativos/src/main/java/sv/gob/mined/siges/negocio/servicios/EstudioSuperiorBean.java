/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDateTime;
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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.EstudioSuperiorValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstudioSuperior;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class EstudioSuperiorBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstudioSuperior
     * @throws GeneralException
     */
    public SgEstudioSuperior obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class);
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
                CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class);
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
     * @param entity SgEstudioSuperior
     * @return SgEstudioSuperior
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstudioSuperior guardar(SgEstudioSuperior entity) throws GeneralException {
        try {
            if (entity != null) {
                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (EstudioSuperiorValidacionNegocio.validar(entity)) {                                        
                    CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class);
                    
                    if (entity.getEsuPk() != null) {
                        SgEstudioSuperior comp = codDAO.obtenerPorId(entity.getEsuPk(), null);
                        if (!EstudioSuperiorValidacionNegocio.compararParaGrabar(entity, comp)) {
                            entity.setEsuValidado(Boolean.FALSE);
                            entity.setEsuUltValidacionFecha(null);
                            entity.setEsuUltValidacionUsuario(null);
                        }
                    }
                    
                    return (SgEstudioSuperior)codDAO.guardar(entity, null);
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
     * Valida el estudio indicado
     *
     * @param Long estPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstudioSuperior validarRealizado(Long estPk) throws GeneralException {
        try {
            if (estPk != null) {
                CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class);          
                SgEstudioSuperior exp = codDAO.obtenerPorId(estPk, null);
                exp.setEsuValidado(Boolean.TRUE);
                exp.setEsuUltValidacionFecha(LocalDateTime.now());
                exp.setEsuUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
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
     * Invalida el estudio indicado
     *
     * @param Long estPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstudioSuperior invalidarRealizado(Long estPk) throws GeneralException {
        try {
            if (estPk != null) {
                CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class);          
                SgEstudioSuperior exp = codDAO.obtenerPorId(estPk, null);
                exp.setEsuValidado(Boolean.FALSE);
                exp.setEsuUltValidacionFecha(LocalDateTime.now());
                exp.setEsuUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEstudioSuperior
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgEstudioSuperior> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class);        
            return codDAO.obtenerPorFiltro(filtro, null);
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
                CodigueraDAO<SgEstudioSuperior> codDAO = new CodigueraDAO<>(em, SgEstudioSuperior.class); 
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
