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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroDatoEmpleado;
import sv.gob.mined.siges.negocio.validaciones.DatoEmpleadoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DatoEmpleadoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado;

@Stateless
@Traced
public class DatoEmpleadoBean {
    
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDatoEmpleado
     * @throws GeneralException
     */
    public SgDatoEmpleado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDatoEmpleado> codDAO = new CodigueraDAO<>(em, SgDatoEmpleado.class);
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
                CodigueraDAO<SgDatoEmpleado> codDAO = new CodigueraDAO<>(em, SgDatoEmpleado.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        
    
    public void permitirAplicarAPlaza(Long datoEmpleadoPk) {
        try {
            String query = "UPDATE SgDatoEmpleado pe SET demPuedeAplicarPlaza = true where pe.demPk = :demPk";
            em.createQuery(query).setParameter("demPk", datoEmpleadoPk).executeUpdate();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgDatoEmpleado
     * @return SgDatoEmpleado
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDatoEmpleado guardar(SgDatoEmpleado entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (DatoEmpleadoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgDatoEmpleado> codDAO = new CodigueraDAO<>(em, SgDatoEmpleado.class);
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
                CodigueraDAO<SgDatoEmpleado> codDAO = new CodigueraDAO<>(em, SgDatoEmpleado.class);
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
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
                CodigueraDAO<SgDatoEmpleado> codDAO = new CodigueraDAO<>(em, SgDatoEmpleado.class);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }  
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgDatoEmpleado
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgDatoEmpleado> obtenerPorFiltro(FiltroDatoEmpleado filtro) throws GeneralException {
        try {
                DatoEmpleadoDAO codDAO = new DatoEmpleadoDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
