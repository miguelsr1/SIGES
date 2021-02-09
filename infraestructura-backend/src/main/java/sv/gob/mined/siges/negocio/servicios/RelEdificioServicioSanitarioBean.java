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
import sv.gob.mined.siges.filtros.FiltroRelEdificioServicioSanitario;
import sv.gob.mined.siges.negocio.validaciones.RelEdificioServicioSanitarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelEdificioServicioSanitarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioServicioSanitario;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelEdificioServicioSanitarioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelEdificioServicioSanitario
     * @throws GeneralException
     */
    public SgRelEdificioServicioSanitario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelEdificioServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicioSanitario.class);
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
                CodigueraDAO<SgRelEdificioServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicioSanitario.class);
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
     * @param ret SgRelEdificioServicioSanitario
     * @return SgRelEdificioServicioSanitario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelEdificioServicioSanitario guardar(SgRelEdificioServicioSanitario ret) throws GeneralException {
        try {
            if (ret != null) {
                if (RelEdificioServicioSanitarioValidacionNegocio.validar(ret)) {
                    CodigueraDAO<SgRelEdificioServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicioSanitario.class);
                 
                        return codDAO.guardar(ret);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ret;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelEdificioServicioSanitario filtro) throws GeneralException {
        try {
            RelEdificioServicioSanitarioDAO codDAO = new RelEdificioServicioSanitarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelEdificioServicioSanitario>
     * @throws GeneralException
     */
    public List<SgRelEdificioServicioSanitario> obtenerPorFiltro(FiltroRelEdificioServicioSanitario filtro) throws GeneralException {
        try {
            RelEdificioServicioSanitarioDAO codDAO = new RelEdificioServicioSanitarioDAO(em);
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
                CodigueraDAO<SgRelEdificioServicioSanitario> codDAO = new CodigueraDAO<>(em, SgRelEdificioServicioSanitario.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
