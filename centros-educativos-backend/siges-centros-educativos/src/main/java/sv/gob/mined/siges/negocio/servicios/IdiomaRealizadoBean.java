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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.IdiomaRealizadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgIdiomaRealizado;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class IdiomaRealizadoBean {
    
    @PersistenceContext
    private EntityManager em;
    
    private static final Logger LOGGER = Logger.getLogger(IdiomaRealizadoBean.class.getName());

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgIdiomaRealizado
     * @throws GeneralException
     */
    public SgIdiomaRealizado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class);
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
                CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
    
    /**
     * Valida el idioma indicado
     *
     * @param Long idiomaPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgIdiomaRealizado validarRealizado(Long idiomaPk) throws GeneralException {
        try {
            if (idiomaPk != null) {
                CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class);          
                SgIdiomaRealizado idioma = codDAO.obtenerPorId(idiomaPk, null);
                idioma.setIreValidado(Boolean.TRUE);
                idioma.setIreUltValidacionFecha(LocalDateTime.now());
                idioma.setIreUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
                return codDAO.guardar(idioma, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }
    
    /**
     * Invalida el idioma indicado
     *
     * @param Long idiomaPk
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgIdiomaRealizado invalidarRealizado(Long idiomaPk) throws GeneralException {
        try {
            if (idiomaPk != null) {
                CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class);          
                SgIdiomaRealizado idioma = codDAO.obtenerPorId(idiomaPk, null);
                idioma.setIreValidado(Boolean.FALSE);
                idioma.setIreUltValidacionFecha(LocalDateTime.now());
                idioma.setIreUltValidacionUsuario(Lookup.obtenerJWT().getSubject());
                return codDAO.guardar(idioma, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }
    
    /**
     * Guarda el objeto indicado
     *
     * @param entity SgIdiomaRealizado
     * @return SgIdiomaRealizado
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgIdiomaRealizado guardar(SgIdiomaRealizado entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (IdiomaRealizadoValidacionNegocio.validar(entity)) {                                        
                    CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class);
                    
                    if (entity.getIrePk() != null) {
                        SgIdiomaRealizado comp = codDAO.obtenerPorId(entity.getIrePk(), null);
                        if (!IdiomaRealizadoValidacionNegocio.compararParaGrabar(entity, comp)) {
                            entity.setIreValidado(Boolean.FALSE);
                            entity.setIreUltValidacionFecha(null);
                            entity.setIreUltValidacionUsuario(null);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgIdiomaRealizado
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgIdiomaRealizado> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class);        
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
                CodigueraDAO<SgIdiomaRealizado> codDAO = new CodigueraDAO<>(em, SgIdiomaRealizado.class); 
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
