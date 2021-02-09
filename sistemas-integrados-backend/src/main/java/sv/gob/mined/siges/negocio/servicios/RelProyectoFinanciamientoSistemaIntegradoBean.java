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
import sv.gob.mined.siges.filtros.FiltroRelProyectoFinanciamientoSistemaIntegrado;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelProyectoFinanciamientoSistemaIntegradoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelProyectoFinanciamientoSistemaIntegrado;

@Stateless
public class RelProyectoFinanciamientoSistemaIntegradoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelProyectoFinanciamientoSistemaIntegrado
     * @throws GeneralException
     */
    public SgRelProyectoFinanciamientoSistemaIntegrado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelProyectoFinanciamientoSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgRelProyectoFinanciamientoSistemaIntegrado.class);
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
                CodigueraDAO<SgRelProyectoFinanciamientoSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgRelProyectoFinanciamientoSistemaIntegrado.class);
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
     * @param entity SgRelProyectoFinanciamientoSistemaIntegrado
     * @return SgRelProyectoFinanciamientoSistemaIntegrado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public Boolean guardar(List<SgRelProyectoFinanciamientoSistemaIntegrado> entity) throws GeneralException {
        try {
            if (entity != null) {
                CodigueraDAO<SgRelProyectoFinanciamientoSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgRelProyectoFinanciamientoSistemaIntegrado.class);
                for (SgRelProyectoFinanciamientoSistemaIntegrado pr : entity) {
                    codDAO.guardar(pr);
                }
            }
            return Boolean.TRUE;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelProyectoFinanciamientoSistemaIntegrado filtro) throws GeneralException {
        try {
            RelProyectoFinanciamientoSistemaIntegradoDAO codDAO = new RelProyectoFinanciamientoSistemaIntegradoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgRelProyectoFinanciamientoSistemaIntegrado
     * @throws GeneralException
     */
    public List<SgRelProyectoFinanciamientoSistemaIntegrado> obtenerPorFiltro(FiltroRelProyectoFinanciamientoSistemaIntegrado filtro) throws GeneralException {
        try {
            RelProyectoFinanciamientoSistemaIntegradoDAO codDAO = new RelProyectoFinanciamientoSistemaIntegradoDAO(em);
            List<SgRelProyectoFinanciamientoSistemaIntegrado> list = codDAO.obtenerPorFiltro(filtro);
            return list;
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
                CodigueraDAO<SgRelProyectoFinanciamientoSistemaIntegrado> codDAO = new CodigueraDAO<>(em, SgRelProyectoFinanciamientoSistemaIntegrado.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
