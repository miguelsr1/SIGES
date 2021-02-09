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
import sv.gob.mined.siges.filtros.FiltroRelEdificioServicio;
import sv.gob.mined.siges.negocio.validaciones.RelEdificioServicioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelEdificioServicioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioServicio;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelEdificioServicioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelEdificioServicio
     * @throws GeneralException
     */
    public SgRelEdificioServicio obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelEdificioServicio> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicio.class);
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
                CodigueraDAO<SgRelEdificioServicio> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicio.class);
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
     * @param res SgRelEdificioServicio
     * @return SgRelEdificioServicio
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelEdificioServicio guardar(SgRelEdificioServicio res) throws GeneralException {
        try {
            if (res != null) {
                if (RelEdificioServicioValidacionNegocio.validar(res)) {
                    CodigueraDAO<SgRelEdificioServicio> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicio.class);
              
                        return codDAO.guardar(res);
              
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return res;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelEdificioServicio filtro) throws GeneralException {
        try {
            RelEdificioServicioDAO codDAO = new RelEdificioServicioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelEdificioServicio>
     * @throws GeneralException
     */
    public List<SgRelEdificioServicio> obtenerPorFiltro(FiltroRelEdificioServicio filtro) throws GeneralException {
        try {
            RelEdificioServicioDAO codDAO = new RelEdificioServicioDAO(em);
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
                CodigueraDAO<SgRelEdificioServicio> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicio.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
