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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleEspacioComun;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleEspacioComunValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleEspacioComunDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleEspacioComun;



@Stateless
public class RelInmuebleEspacioComunBean {
    
    @PersistenceContext
    private EntityManager em;
   
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleEspacioComun
     * @throws GeneralException
     */
    
    public SgRelInmuebleEspacioComun obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleEspacioComun> codDAO = new CodigueraDAO<>(em, SgRelInmuebleEspacioComun.class);
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
                CodigueraDAO<SgRelInmuebleEspacioComun> codDAO = new CodigueraDAO<>(em, SgRelInmuebleEspacioComun.class);
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
     * @param entity SgRelInmuebleEspacioComun
     * @return SgRelInmuebleEspacioComun
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgRelInmuebleEspacioComun guardar(SgRelInmuebleEspacioComun entity) throws GeneralException {
        try {
            if (entity != null) {
                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (RelInmuebleEspacioComunValidacionNegocio.validar(entity)) {                 
                    CodigueraDAO<SgRelInmuebleEspacioComun> codDAO = new CodigueraDAO<>(em, SgRelInmuebleEspacioComun.class);
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
     * @param filtro FiltroRelInmuebleEspacioComun
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleEspacioComun filtro) throws GeneralException {
        try {
            RelInmuebleEspacioComunDAO codDAO = new RelInmuebleEspacioComunDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroRelInmuebleEspacioComun
     * @return Lista de SgRelInmuebleEspacioComun
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgRelInmuebleEspacioComun> obtenerPorFiltro(FiltroRelInmuebleEspacioComun filtro) throws GeneralException {
        try {
            RelInmuebleEspacioComunDAO codDAO = new RelInmuebleEspacioComunDAO(em);        
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
                CodigueraDAO<SgRelInmuebleEspacioComun> codDAO = new CodigueraDAO<>(em, SgRelInmuebleEspacioComun.class); 
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
