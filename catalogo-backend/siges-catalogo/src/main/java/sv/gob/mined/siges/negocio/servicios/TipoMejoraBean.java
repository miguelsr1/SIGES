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
import sv.gob.mined.siges.filtros.FiltroTipoMejora;
import sv.gob.mined.siges.negocio.validaciones.TipoMejoraValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TipoMejoraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoMejora;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TipoMejoraBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoMejora
     * @throws GeneralException
     */
    public SgTipoMejora obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoMejora> codDAO = new CodigueraDAO<>(em, SgTipoMejora.class);
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
                CodigueraDAO<SgTipoMejora> codDAO = new CodigueraDAO<>(em, SgTipoMejora.class);
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
     * @param tme SgTipoMejora
     * @return SgTipoMejora
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoMejora guardar(SgTipoMejora tme) throws GeneralException {
        try {
            if (tme != null) {
                if (TipoMejoraValidacionNegocio.validar(tme)) {
                    CodigueraDAO<SgTipoMejora> codDAO = new CodigueraDAO<>(em, SgTipoMejora.class);
               
                        return codDAO.guardar(tme);
                
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tme;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTipoMejora filtro) throws GeneralException {
        try {
            TipoMejoraDAO codDAO = new TipoMejoraDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTipoMejora>
     * @throws GeneralException
     */
    public List<SgTipoMejora> obtenerPorFiltro(FiltroTipoMejora filtro) throws GeneralException {
        try {
            TipoMejoraDAO codDAO = new TipoMejoraDAO(em);
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
                CodigueraDAO<SgTipoMejora> codDAO = new CodigueraDAO<>(em, SgTipoMejora.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
