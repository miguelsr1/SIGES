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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleServicioSanitario;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleServicioSanitarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleServicioSanitarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleServicioSanitario;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleServicioSanitarioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleServicioSanitario
     * @throws GeneralException
     */
    public SgRelInmuebleServicioSanitario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioSanitario.class);
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
                CodigueraDAO<SgRelInmuebleServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioSanitario.class);
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
     * @param rit SgRelInmuebleServicioSanitario
     * @return SgRelInmuebleServicioSanitario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleServicioSanitario guardar(SgRelInmuebleServicioSanitario rit) throws GeneralException {
        try {
            if (rit != null) {
                if (RelInmuebleServicioSanitarioValidacionNegocio.validar(rit)) {
                    CodigueraDAO<SgRelInmuebleServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioSanitario.class);
                 
                        return codDAO.guardar(rit);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rit;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleServicioSanitario filtro) throws GeneralException {
        try {
            RelInmuebleServicioSanitarioDAO codDAO = new RelInmuebleServicioSanitarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleServicioSanitario>
     * @throws GeneralException
     */
    public List<SgRelInmuebleServicioSanitario> obtenerPorFiltro(FiltroRelInmuebleServicioSanitario filtro) throws GeneralException {
        try {
            RelInmuebleServicioSanitarioDAO codDAO = new RelInmuebleServicioSanitarioDAO(em);
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
                CodigueraDAO<SgRelInmuebleServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelInmuebleServicioSanitario.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
