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
import sv.gob.mined.siges.filtros.FiltroDefinicionTitulo;
import sv.gob.mined.siges.negocio.validaciones.DefinicionTituloValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DefinicionTituloDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDefinicionTitulo;
import sv.gob.mined.siges.persistencia.utilidades.LoadLazyCollectionsViewInterceptor;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class DefinicionTituloBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDefinicionTitulo
     * @throws GeneralException
     */
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgDefinicionTitulo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDefinicionTitulo> codDAO = new CodigueraDAO<>(em, SgDefinicionTitulo.class);
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
                CodigueraDAO<SgDefinicionTitulo> codDAO = new CodigueraDAO<>(em, SgDefinicionTitulo.class);
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
     * @param dti SgDefinicionTitulo
     * @return SgDefinicionTitulo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDefinicionTitulo guardar(SgDefinicionTitulo dti) throws GeneralException {
        try {
            if (dti != null) {
                if (DefinicionTituloValidacionNegocio.validar(dti)) {
                    CodigueraDAO<SgDefinicionTitulo> codDAO = new CodigueraDAO<>(em, SgDefinicionTitulo.class);
                    return codDAO.guardar(dti);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return dti;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDefinicionTitulo filtro) throws GeneralException {
        try {
            DefinicionTituloDAO codDAO = new DefinicionTituloDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgDefinicionTitulo>
     * @throws GeneralException
     */
    public List<SgDefinicionTitulo> obtenerPorFiltro(FiltroDefinicionTitulo filtro) throws GeneralException {
        try {
            DefinicionTituloDAO codDAO = new DefinicionTituloDAO(em);
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
                CodigueraDAO<SgDefinicionTitulo> codDAO = new CodigueraDAO<>(em, SgDefinicionTitulo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
