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
import javax.persistence.Query;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRelInmuebleUnidadResp;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleUnidadRespValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleUnidadRespDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleUnidadResp;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleUnidadRespBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleUnidadResp
     * @throws GeneralException
     */
    public SgRelInmuebleUnidadResp obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleUnidadResp> codDAO = new CodigueraDAO<>(em, SgRelInmuebleUnidadResp.class);
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
                CodigueraDAO<SgRelInmuebleUnidadResp> codDAO = new CodigueraDAO<>(em, SgRelInmuebleUnidadResp.class);
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
     * @param riu SgRelInmuebleUnidadResp
     * @return SgRelInmuebleUnidadResp
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleUnidadResp guardar(SgRelInmuebleUnidadResp riu) throws GeneralException {
        try {
            if (riu != null) {
                if (RelInmuebleUnidadRespValidacionNegocio.validar(riu)) {
                    CodigueraDAO<SgRelInmuebleUnidadResp> codDAO = new CodigueraDAO<>(em, SgRelInmuebleUnidadResp.class);
                    return codDAO.guardar(riu);
              
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return riu;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleUnidadResp filtro) throws GeneralException {
        try {
            RelInmuebleUnidadRespDAO codDAO = new RelInmuebleUnidadRespDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleUnidadResp>
     * @throws GeneralException
     */
    public List<SgRelInmuebleUnidadResp> obtenerPorFiltro(FiltroRelInmuebleUnidadResp filtro) throws GeneralException {
        try {
            RelInmuebleUnidadRespDAO codDAO = new RelInmuebleUnidadRespDAO(em);
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
                Query q = em.createNativeQuery("delete from infraestructuras.sg_edificio_rel_inmueble_unidad_resp where riu_pk = :pk");
                        q.setParameter("pk", id);
                        q.executeUpdate();
                CodigueraDAO<SgRelInmuebleUnidadResp> codDAO = new CodigueraDAO<>(em, SgRelInmuebleUnidadResp.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
