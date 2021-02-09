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
import sv.gob.mined.siges.filtros.FiltroMedidasExamenFisico;
import sv.gob.mined.siges.negocio.validaciones.MedidasExamenFisicoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MedidasExamenFisicoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMedidasExamenFisico;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class MedidasExamenFisicoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMedidasExamenFisico
     * @throws GeneralException
     */
    public SgMedidasExamenFisico obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMedidasExamenFisico> codDAO = new CodigueraDAO<>(em, SgMedidasExamenFisico.class);
                return codDAO.obtenerPorId(id,null);
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
                CodigueraDAO<SgMedidasExamenFisico> codDAO = new CodigueraDAO<>(em, SgMedidasExamenFisico.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param mef SgMedidasExamenFisico
     * @return SgMedidasExamenFisico
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMedidasExamenFisico guardar(SgMedidasExamenFisico mef) throws GeneralException {
        try {
            if (mef != null) {
                if (MedidasExamenFisicoValidacionNegocio.validar(mef)) {
                    CodigueraDAO<SgMedidasExamenFisico> codDAO = new CodigueraDAO<>(em, SgMedidasExamenFisico.class);              
                    return codDAO.guardar(mef,null);               
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mef;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMedidasExamenFisico filtro) throws GeneralException {
        try {
            MedidasExamenFisicoDAO codDAO = new MedidasExamenFisicoDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMedidasExamenFisico>
     * @throws GeneralException
     */
    public List<SgMedidasExamenFisico> obtenerPorFiltro(FiltroMedidasExamenFisico filtro) throws GeneralException {
        try {
            MedidasExamenFisicoDAO codDAO = new MedidasExamenFisicoDAO(em);
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
                CodigueraDAO<SgMedidasExamenFisico> codDAO = new CodigueraDAO<>(em, SgMedidasExamenFisico.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
