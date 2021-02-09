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
import sv.gob.mined.siges.filtros.FiltroAsociacion;
import sv.gob.mined.siges.negocio.validaciones.AsociacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.AsociacionDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAsociacion;
import sv.gob.mined.siges.persistencia.utilidades.LoadLazyCollectionsViewInterceptor;

@Stateless
public class AsociacionBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAsociacion
     * @throws GeneralException
     */
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgAsociacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAsociacion> codDAO = new CodigueraDAO<>(em, SgAsociacion.class);
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
                CodigueraDAO<SgAsociacion> codDAO = new CodigueraDAO<>(em, SgAsociacion.class);
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
     * @param cla SgAsociacion
     * @return SgAsociacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAsociacion guardar(SgAsociacion cla) throws GeneralException {
        try {
            if (cla != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (AsociacionValidacionNegocio.validar(cla)) {
                    CodigueraDAO<SgAsociacion> codDAO = new CodigueraDAO<>(em, SgAsociacion.class);
                    return codDAO.guardar(cla);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cla;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAsociacion filtro) throws GeneralException {
        try {
            AsociacionDAO codDAO = new AsociacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgAsociacion 
     * @throws GeneralException
     */
    public List<SgAsociacion> obtenerPorFiltro(FiltroAsociacion filtro) throws GeneralException {
        try {
            AsociacionDAO codDAO = new AsociacionDAO(em);
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
                CodigueraDAO<SgAsociacion> codDAO = new CodigueraDAO<>(em, SgAsociacion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}

