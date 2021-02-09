/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroFuenteFinanciamientoAF;
import sv.gob.mined.siges.negocio.validaciones.FuenteFinanciamientoAFValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.FuenteFinanciamientoAFDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfFuenteFinanciamiento;

@Stateless
public class FuenteFinanciamientoAFBean {
    
    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfFuenteFinanciamiento
     * @throws GeneralException
     */
    public SgAfFuenteFinanciamiento obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgAfFuenteFinanciamiento.class);
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
                CodigueraDAO<SgAfFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgAfFuenteFinanciamiento.class);
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
     * @param ffi SgAfFuenteFinanciamiento
     * @return SgAfFuenteFinanciamiento
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAfFuenteFinanciamiento guardar(SgAfFuenteFinanciamiento ffi) throws GeneralException {
        try {
            if (ffi != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (FuenteFinanciamientoAFValidacionNegocio.validar(ffi)) {
                    CodigueraDAO<SgAfFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgAfFuenteFinanciamiento.class);
                    return codDAO.guardar(ffi);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ffi;
    }

    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroFuenteFinanciamientoAF filtro) throws GeneralException {
        try {
            FuenteFinanciamientoAFDAO coDAO = new FuenteFinanciamientoAFDAO(em);
            return coDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgAfFuenteFinanciamiento 
     * @throws GeneralException
     */
    public List<SgAfFuenteFinanciamiento> obtenerPorFiltro(FiltroFuenteFinanciamientoAF filtro) throws GeneralException {
        try {
            FuenteFinanciamientoAFDAO coDAO = new FuenteFinanciamientoAFDAO(em);
            return coDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgAfFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgAfFuenteFinanciamiento.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}