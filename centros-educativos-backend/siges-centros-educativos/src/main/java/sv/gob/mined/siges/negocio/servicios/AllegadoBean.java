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
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAllegado;
import sv.gob.mined.siges.negocio.validaciones.AllegadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.AllegadoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.utilidades.InitializeObjectUtils;

@Stateless
@Traced
public class AllegadoBean {
   
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private PersonaBean personaBean;
    
    @Inject
    private ConfiguracionBean configuracionBean;
    
    @Inject
    @ConfigProperty(name = "email-valid-pattern")
    private String emailPattern;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAllegado
     * @throws GeneralException
     */
    public SgAllegado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAllegado> codDAO = new CodigueraDAO<>(em, SgAllegado.class);
                SgAllegado all = codDAO.obtenerPorId(id, null);
                if (all != null){
                    InitializeObjectUtils.inicializarPersona(all.getAllPersona());
                }
                return all;
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
                CodigueraDAO<SgAllegado> codDAO = new CodigueraDAO<>(em, SgAllegado.class);
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
     * @param entity SgAllegado
     * @return SgAllegado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAllegado guardar(SgAllegado entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (AllegadoValidacionNegocio.validar(entity, emailPattern, configuracionBean)) {
                    CodigueraDAO<SgAllegado> codDAO = new CodigueraDAO<>(em, SgAllegado.class);
                    SgPersona per = personaBean.guardar(entity.getAllPersona());
                    entity.setAllPersona(per);
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
    public Long obtenerTotalPorFiltro(FiltroAllegado filtro) throws GeneralException {
        try {
            AllegadoDAO codDAO = new AllegadoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgAllegado
     * @throws GeneralException
     */
    public List<SgAllegado> obtenerPorFiltro(FiltroAllegado filtro) throws GeneralException {
        try {
            AllegadoDAO codDAO = new AllegadoDAO(em);
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
                CodigueraDAO<SgAllegado> codDAO = new CodigueraDAO<>(em, SgAllegado.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
