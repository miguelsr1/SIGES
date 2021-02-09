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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.FuenteFinanciamientoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgFuenteFinanciamiento;

@Stateless
public class FuenteFinanciamientoBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgFuenteFinanciamiento
     * @throws GeneralException
     */
    public SgFuenteFinanciamiento obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgFuenteFinanciamiento.class);
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
                CodigueraDAO<SgFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgFuenteFinanciamiento.class);
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
     * @param ffi SgFuenteFinanciamiento
     * @return SgFuenteFinanciamiento
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgFuenteFinanciamiento guardar(SgFuenteFinanciamiento ffi) throws GeneralException {
        try {
            if (ffi != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (FuenteFinanciamientoValidacionNegocio.validar(ffi)) {
                    CodigueraDAO<SgFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgFuenteFinanciamiento.class);
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
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgFuenteFinanciamiento.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgFuenteFinanciamiento 
     * @throws GeneralException
     */
    public List<SgFuenteFinanciamiento> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgFuenteFinanciamiento.class);
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
                CodigueraDAO<SgFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SgFuenteFinanciamiento.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
