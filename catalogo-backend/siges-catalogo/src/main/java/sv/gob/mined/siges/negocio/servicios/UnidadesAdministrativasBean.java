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
import sv.gob.mined.siges.filtros.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.negocio.validaciones.UnidadesAdministrativasValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.UnidadesAdministrativasDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfUnidadesAdministrativas;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class UnidadesAdministrativasBean {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfUnidadesAdministrativas
     * @throws GeneralException
     */
    public SgAfUnidadesAdministrativas obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfUnidadesAdministrativas> codDAO = new CodigueraDAO<>(em, SgAfUnidadesAdministrativas.class);
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
                CodigueraDAO<SgAfUnidadesAdministrativas> codDAO = new CodigueraDAO<>(em, SgAfUnidadesAdministrativas.class);
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
     * @param tri SgAfUnidadesAdministrativas
     * @return SgAfUnidadesAdministrativas
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAfUnidadesAdministrativas guardar(SgAfUnidadesAdministrativas tri) throws GeneralException {
        try {
            if (tri != null) {
                if (UnidadesAdministrativasValidacionNegocio.validar(tri)) {
                    CodigueraDAO<SgAfUnidadesAdministrativas> codDAO = new CodigueraDAO<>(em, SgAfUnidadesAdministrativas.class);
                    return codDAO.guardar(tri);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tri;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroUnidadesAdministrativas
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroUnidadesAdministrativas filtro) throws GeneralException {
        try {
            UnidadesAdministrativasDAO codDAO = new UnidadesAdministrativasDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroUnidadesAdministrativas
     * @return Lista de <SgAfUnidadesActivoFijo>
     * @throws GeneralException
     */
    public List<SgAfUnidadesAdministrativas> obtenerPorFiltro(FiltroUnidadesAdministrativas filtro) throws GeneralException {
        try {
            UnidadesAdministrativasDAO codDAO = new UnidadesAdministrativasDAO(em);
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
                CodigueraDAO<SgAfUnidadesAdministrativas> codDAO = new CodigueraDAO<>(em, SgAfUnidadesAdministrativas.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
