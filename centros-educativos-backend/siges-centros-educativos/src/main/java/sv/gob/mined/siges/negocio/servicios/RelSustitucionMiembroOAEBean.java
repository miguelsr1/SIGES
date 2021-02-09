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
import sv.gob.mined.siges.filtros.FiltroRelSustitucionMiembroOAE;
import sv.gob.mined.siges.negocio.validaciones.RelSustitucionMiembroOAEValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.RelSustitucionMiembroOAEDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelSustitucionMiembroOAE;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelSustitucionMiembroOAEBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelSustitucionMiembroOAE
     * @throws GeneralException
     */
    public SgRelSustitucionMiembroOAE obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgRelSustitucionMiembroOAE.class);
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
                CodigueraDAO<SgRelSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgRelSustitucionMiembroOAE.class);
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
     * @param rsm SgRelSustitucionMiembroOAE
     * @return SgRelSustitucionMiembroOAE
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelSustitucionMiembroOAE guardar(SgRelSustitucionMiembroOAE rsm) throws GeneralException {
        try {
            if (rsm != null) {
                if (RelSustitucionMiembroOAEValidacionNegocio.validar(rsm)) {
                    CodigueraDAO<SgRelSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgRelSustitucionMiembroOAE.class);              
                    return codDAO.guardar(rsm,null);               
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rsm;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroRelSustitucionMiembroOAE
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelSustitucionMiembroOAE filtro) throws GeneralException {
        try {
            RelSustitucionMiembroOAEDAO dao = new RelSustitucionMiembroOAEDAO(em);
            return dao.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroRelSustitucionMiembroOAE
     * @return Lista de <SgRelSustitucionMiembroOAE>
     * @throws GeneralException
     */
    public List<SgRelSustitucionMiembroOAE> obtenerPorFiltro(FiltroRelSustitucionMiembroOAE filtro) throws GeneralException {
        try {
            RelSustitucionMiembroOAEDAO dao = new RelSustitucionMiembroOAEDAO(em);
            return dao.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgRelSustitucionMiembroOAE> codDAO = new CodigueraDAO<>(em, SgRelSustitucionMiembroOAE.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
