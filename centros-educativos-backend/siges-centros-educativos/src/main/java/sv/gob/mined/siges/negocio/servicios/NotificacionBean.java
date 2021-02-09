/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroNotificacion;
import sv.gob.mined.siges.negocio.validaciones.NotificacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.NotificacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgNotificacion;

@Stateless
@Traced
public class NotificacionBean {
    
    private static final Logger LOGGER = Logger.getLogger(NotificacionBean.class.getName());
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SedeBean sedeBean;
    
    @Inject
    private SeccionBean seccionBean;
    
    @Inject
    private EstudianteBean estudianteBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgNotificacion
     * @throws GeneralException
     */
    public SgNotificacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgNotificacion> codDAO = new CodigueraDAO<>(em, SgNotificacion.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_NOTIFICACION);
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
                CodigueraDAO<SgNotificacion> codDAO = new CodigueraDAO<>(em, SgNotificacion.class);
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
     * @param entity SgNotificacion
     * @return SgNotificacion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgNotificacion guardar(SgNotificacion entity) throws GeneralException {
        try {
            if (entity != null) {
                    //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (NotificacionValidacionNegocio.validar(entity)) {                                        
                    
                    CodigueraDAO<SgNotificacion> codDAO = new CodigueraDAO<>(em, SgNotificacion.class);
                    return (SgNotificacion)codDAO.guardar(entity, null);
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
     * @param filtro FiltroNotificacion
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroNotificacion filtro) throws GeneralException {
        try {
            NotificacionDAO codDAO = new NotificacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgNotificacion
     * @throws GeneralException
     */
    public List<SgNotificacion> obtenerPorFiltro(FiltroNotificacion filtro) throws GeneralException {
        try {
            NotificacionDAO codDAO = new NotificacionDAO(em);
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
                CodigueraDAO<SgNotificacion> codDAO = new CodigueraDAO<>(em, SgNotificacion.class); 
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
