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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.EjecucionProcesoNocturnoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEjecucionProcesoNocturno;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EjecucionProcesoNocturnoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEjecucionProcesoNocturno
     * @throws GeneralException
     */
    public SgEjecucionProcesoNocturno obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEjecucionProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgEjecucionProcesoNocturno.class);
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
                CodigueraDAO<SgEjecucionProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgEjecucionProcesoNocturno.class);
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
     * @param epr SgEjecucionProcesoNocturno
     * @return SgEjecucionProcesoNocturno
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEjecucionProcesoNocturno guardar(SgEjecucionProcesoNocturno epr) throws GeneralException {
        try {
            if (epr != null) {
                if (EjecucionProcesoNocturnoValidacionNegocio.validar(epr)) {
                    CodigueraDAO<SgEjecucionProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgEjecucionProcesoNocturno.class);
                    return codDAO.guardar(epr);
                
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return epr;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEjecucionProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgEjecucionProcesoNocturno.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEjecucionProcesoNocturno>
     * @throws GeneralException
     */
    public List<SgEjecucionProcesoNocturno> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgEjecucionProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgEjecucionProcesoNocturno.class);
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
                CodigueraDAO<SgEjecucionProcesoNocturno> codDAO = new CodigueraDAO<>(em, SgEjecucionProcesoNocturno.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
