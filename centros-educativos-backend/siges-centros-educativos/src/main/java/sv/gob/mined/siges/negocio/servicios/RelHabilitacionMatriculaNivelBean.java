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
import sv.gob.mined.siges.filtros.FiltroRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.negocio.validaciones.RelHabilitacionMatriculaNivelValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelHabilitacionMatriculaNivelDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelHabilitacionMatriculaNivel;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelHabilitacionMatriculaNivelBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelHabilitacionMatriculaNivel
     * @throws GeneralException
     */
    public SgRelHabilitacionMatriculaNivel obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelHabilitacionMatriculaNivel> codDAO = new CodigueraDAO<>(em, SgRelHabilitacionMatriculaNivel.class);
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
                CodigueraDAO<SgRelHabilitacionMatriculaNivel> codDAO = new CodigueraDAO<>(em, SgRelHabilitacionMatriculaNivel.class);
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
     * @param rhn SgRelHabilitacionMatriculaNivel
     * @return SgRelHabilitacionMatriculaNivel
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelHabilitacionMatriculaNivel guardar(SgRelHabilitacionMatriculaNivel rhn) throws GeneralException {
        try {
            if (rhn != null) {
                if (RelHabilitacionMatriculaNivelValidacionNegocio.validar(rhn)) {
                    CodigueraDAO<SgRelHabilitacionMatriculaNivel> codDAO = new CodigueraDAO<>(em, SgRelHabilitacionMatriculaNivel.class);

                    return codDAO.guardar(rhn,null);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rhn;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelHabilitacionMatriculaNivel filtro) throws GeneralException {
        try {
            RelHabilitacionMatriculaNivelDAO codDAO = new RelHabilitacionMatriculaNivelDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelHabilitacionMatriculaNivel>
     * @throws GeneralException
     */
    public List<SgRelHabilitacionMatriculaNivel> obtenerPorFiltro(FiltroRelHabilitacionMatriculaNivel filtro) throws GeneralException {
        try {
            RelHabilitacionMatriculaNivelDAO codDAO = new RelHabilitacionMatriculaNivelDAO(em,seguridadBean);
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
                CodigueraDAO<SgRelHabilitacionMatriculaNivel> codDAO = new CodigueraDAO<>(em, SgRelHabilitacionMatriculaNivel.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
