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
import sv.gob.mined.siges.filtros.FiltroRelOAEIdentificacionOAE;
import sv.gob.mined.siges.negocio.validaciones.RelOAEIdentificacionOAEValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelOAEIdentificacionOAEDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelOAEIdentificacionOAE;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelOAEIdentificacionOAEBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean segBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelOAEIdentificacionOAE
     * @throws GeneralException
     */
    public SgRelOAEIdentificacionOAE obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelOAEIdentificacionOAE> codDAO = new CodigueraDAO<>(em, SgRelOAEIdentificacionOAE.class);
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
                CodigueraDAO<SgRelOAEIdentificacionOAE> codDAO = new CodigueraDAO<>(em, SgRelOAEIdentificacionOAE.class);
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
     * @param rio SgRelOAEIdentificacionOAE
     * @return SgRelOAEIdentificacionOAE
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelOAEIdentificacionOAE guardar(SgRelOAEIdentificacionOAE rio) throws GeneralException {
        try {
            if (rio != null) {
                if (RelOAEIdentificacionOAEValidacionNegocio.validar(rio)) {                  
                    CodigueraDAO<SgRelOAEIdentificacionOAE> codDAO = new CodigueraDAO<>(em, SgRelOAEIdentificacionOAE.class);
             
                    return codDAO.guardar(rio,null);
                    
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rio;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelOAEIdentificacionOAE filtro) throws GeneralException {
        try {
            RelOAEIdentificacionOAEDAO codDAO = new RelOAEIdentificacionOAEDAO(em,segBean);
            return codDAO.obtenerTotalPorFiltro(filtro,null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelOAEIdentificacionOAE>
     * @throws GeneralException
     */
    public List<SgRelOAEIdentificacionOAE> obtenerPorFiltro(FiltroRelOAEIdentificacionOAE filtro) throws GeneralException {
        try {
            RelOAEIdentificacionOAEDAO codDAO = new RelOAEIdentificacionOAEDAO(em,segBean);
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
                CodigueraDAO<SgRelOAEIdentificacionOAE> codDAO = new CodigueraDAO<>(em, SgRelOAEIdentificacionOAE.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
