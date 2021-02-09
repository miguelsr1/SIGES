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
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroMejora;
import sv.gob.mined.siges.negocio.validaciones.MejorasValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MejoraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMejoras;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class MejorasBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMejoras
     * @throws GeneralException
     */
    public SgMejoras obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMejoras> codDAO = new CodigueraDAO<>(em, SgMejoras.class);
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
                CodigueraDAO<SgMejoras> codDAO = new CodigueraDAO<>(em, SgMejoras.class);
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
     * @param mej SgMejoras
     * @return SgMejoras
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMejoras guardar(SgMejoras mej) throws GeneralException {
        try {
            if (mej != null) {
                if (MejorasValidacionNegocio.validar(mej)) {
                    CodigueraDAO<SgMejoras> codDAO = new CodigueraDAO<>(em, SgMejoras.class);
            
                        return codDAO.guardar(mej);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mej;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMejora filtro) throws GeneralException {
        try {
            MejoraDAO codDAO = new MejoraDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.BUSCAR_MEJORAS);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMejoras>
     * @throws GeneralException
     */
    public List<SgMejoras> obtenerPorFiltro(FiltroMejora filtro) throws GeneralException {
        try {
            MejoraDAO codDAO = new MejoraDAO(em);
            return codDAO.obtenerPorFiltro(filtro,ConstantesOperaciones.BUSCAR_MEJORAS);
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
                CodigueraDAO<SgMejoras> codDAO = new CodigueraDAO<>(em, SgMejoras.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
