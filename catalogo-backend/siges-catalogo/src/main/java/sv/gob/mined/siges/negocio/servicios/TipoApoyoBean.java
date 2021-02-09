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
import sv.gob.mined.siges.filtros.FiltroTiposApoyo;
import sv.gob.mined.siges.negocio.validaciones.TipoApoyoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TipoApoyoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoApoyo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TipoApoyoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoApoyo
     * @throws GeneralException
     */
    public SgTipoApoyo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoApoyo> codDAO = new CodigueraDAO<>(em, SgTipoApoyo.class);
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
                CodigueraDAO<SgTipoApoyo> codDAO = new CodigueraDAO<>(em, SgTipoApoyo.class);
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
     * @param tap SgTipoApoyo
     * @return SgTipoApoyo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoApoyo guardar(SgTipoApoyo tap) throws GeneralException {
        try {
            if (tap != null) {
                if (TipoApoyoValidacionNegocio.validar(tap)) {
                    CodigueraDAO<SgTipoApoyo> codDAO = new CodigueraDAO<>(em, SgTipoApoyo.class);
                   
                    return codDAO.guardar(tap);
                    
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tap;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTiposApoyo filtro) throws GeneralException {
        try {
            TipoApoyoDAO codDAO = new TipoApoyoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTipoApoyo>
     * @throws GeneralException
     */
    public List<SgTipoApoyo> obtenerPorFiltro(FiltroTiposApoyo filtro) throws GeneralException {
        try {
            TipoApoyoDAO codDAO = new TipoApoyoDAO(em);
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
                CodigueraDAO<SgTipoApoyo> codDAO = new CodigueraDAO<>(em, SgTipoApoyo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
