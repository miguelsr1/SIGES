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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleArchivo;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleArchivoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleArchivoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleArchivo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleArchivoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleArchivo
     * @throws GeneralException
     */
    public SgRelInmuebleArchivo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleArchivo> codDAO = new CodigueraDAO<>(em, SgRelInmuebleArchivo.class);
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
                CodigueraDAO<SgRelInmuebleArchivo> codDAO = new CodigueraDAO<>(em, SgRelInmuebleArchivo.class);
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
     * @param ria SgRelInmuebleArchivo
     * @return SgRelInmuebleArchivo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleArchivo guardar(SgRelInmuebleArchivo ria) throws GeneralException {
        try {
            if (ria != null) {
                if (RelInmuebleArchivoValidacionNegocio.validar(ria)) {
                    CodigueraDAO<SgRelInmuebleArchivo> codDAO = new CodigueraDAO<>(em, SgRelInmuebleArchivo.class);
                    boolean guardar = true;
                  
                        return codDAO.guardar(ria);
                
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ria;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleArchivo filtro) throws GeneralException {
        try {
            RelInmuebleArchivoDAO codDAO = new RelInmuebleArchivoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleArchivo>
     * @throws GeneralException
     */
    public List<SgRelInmuebleArchivo> obtenerPorFiltro(FiltroRelInmuebleArchivo filtro) throws GeneralException {
        try {
            RelInmuebleArchivoDAO codDAO = new RelInmuebleArchivoDAO(em);
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
                CodigueraDAO<SgRelInmuebleArchivo> codDAO = new CodigueraDAO<>(em, SgRelInmuebleArchivo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
