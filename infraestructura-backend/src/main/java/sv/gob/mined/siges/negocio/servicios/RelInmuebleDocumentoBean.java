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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleDocumento;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleDocumentoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleDocumentoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleDocumento;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleDocumentoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleDocumento
     * @throws GeneralException
     */
    public SgRelInmuebleDocumento obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleDocumento> codDAO = new CodigueraDAO<>(em, SgRelInmuebleDocumento.class);
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
                CodigueraDAO<SgRelInmuebleDocumento> codDAO = new CodigueraDAO<>(em, SgRelInmuebleDocumento.class);
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
     * @param ria SgRelInmuebleDocumento
     * @return SgRelInmuebleDocumento
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleDocumento guardar(SgRelInmuebleDocumento ria) throws GeneralException {
        try {
            if (ria != null) {
                if (RelInmuebleDocumentoValidacionNegocio.validar(ria)) {
                    CodigueraDAO<SgRelInmuebleDocumento> codDAO = new CodigueraDAO<>(em, SgRelInmuebleDocumento.class);
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
     * @param filtro FiltroRelInmuebleDocumento
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleDocumento filtro) throws GeneralException {
        try {
            RelInmuebleDocumentoDAO codDAO = new RelInmuebleDocumentoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroRelInmuebleDocumento
     * @return Lista de <SgRelInmuebleDocumento>
     * @throws GeneralException
     */
    public List<SgRelInmuebleDocumento> obtenerPorFiltro(FiltroRelInmuebleDocumento filtro) throws GeneralException {
        try {
            RelInmuebleDocumentoDAO codDAO = new RelInmuebleDocumentoDAO(em);
            return codDAO.obtenerPorFiltro(filtro, null);
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
                CodigueraDAO<SgRelInmuebleDocumento> codDAO = new CodigueraDAO<>(em, SgRelInmuebleDocumento.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
