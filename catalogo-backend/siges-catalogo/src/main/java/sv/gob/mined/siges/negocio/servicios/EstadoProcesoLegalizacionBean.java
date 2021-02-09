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
import sv.gob.mined.siges.filtros.FiltroEstadoProcesoLegalizacion;
import sv.gob.mined.siges.negocio.validaciones.EstadoProcesoLegalizacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EstadoProcesoLegalizacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstadoProcesoLegalizacion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EstadoProcesoLegalizacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstadoProcesoLegalizacion
     * @throws GeneralException
     */
    public SgEstadoProcesoLegalizacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstadoProcesoLegalizacion> codDAO = new CodigueraDAO<>(em, SgEstadoProcesoLegalizacion.class);
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
                CodigueraDAO<SgEstadoProcesoLegalizacion> codDAO = new CodigueraDAO<>(em, SgEstadoProcesoLegalizacion.class);
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
     * @param epl SgEstadoProcesoLegalizacion
     * @return SgEstadoProcesoLegalizacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstadoProcesoLegalizacion guardar(SgEstadoProcesoLegalizacion epl) throws GeneralException {
        try {
            if (epl != null) {
                if (EstadoProcesoLegalizacionValidacionNegocio.validar(epl)) {
                    CodigueraDAO<SgEstadoProcesoLegalizacion> codDAO = new CodigueraDAO<>(em, SgEstadoProcesoLegalizacion.class);
      
                        return codDAO.guardar(epl);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return epl;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEstadoProcesoLegalizacion filtro) throws GeneralException {
        try {
            EstadoProcesoLegalizacionDAO codDAO = new EstadoProcesoLegalizacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEstadoProcesoLegalizacion>
     * @throws GeneralException
     */
    public List<SgEstadoProcesoLegalizacion> obtenerPorFiltro(FiltroEstadoProcesoLegalizacion filtro) throws GeneralException {
        try {
            EstadoProcesoLegalizacionDAO codDAO = new EstadoProcesoLegalizacionDAO(em);
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
                CodigueraDAO<SgEstadoProcesoLegalizacion> codDAO = new CodigueraDAO<>(em, SgEstadoProcesoLegalizacion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
