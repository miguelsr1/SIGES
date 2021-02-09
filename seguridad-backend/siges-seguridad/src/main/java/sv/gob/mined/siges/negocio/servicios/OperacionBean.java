/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroOperacion;
import sv.gob.mined.siges.negocio.validaciones.OperacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.OperacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgOperacion;

@Stateless
public class OperacionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgOperacion
     * @throws GeneralException
     */
    public SgOperacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOperacion> codDAO = new CodigueraDAO<>(em, SgOperacion.class);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOperacion> codDAO = new CodigueraDAO<>(em, SgOperacion.class);
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
     * @param entity SgOperacion
     * @return SgOperacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgOperacion guardar(SgOperacion entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (OperacionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgOperacion> codDAO = new CodigueraDAO<>(em, SgOperacion.class);
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
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroOperacion filtro) throws GeneralException {
        try {
            OperacionDAO codDAO = new OperacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgOperacion
     * @throws GeneralException
     */
    public List<SgOperacion> obtenerPorFiltro(FiltroOperacion filtro) throws GeneralException {
        try {
            OperacionDAO codDAO = new OperacionDAO(em);
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
                CodigueraDAO<SgOperacion> codDAO = new CodigueraDAO<>(em, SgOperacion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
