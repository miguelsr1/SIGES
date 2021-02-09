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
import sv.gob.mined.siges.negocio.validaciones.ItemEvaluarOrganismoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgItemEvaluarOrganismo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ItemEvaluarOrganismoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgItemEvaluarOrganismo
     * @throws GeneralException
     */
    public SgItemEvaluarOrganismo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgItemEvaluarOrganismo> codDAO = new CodigueraDAO<>(em, SgItemEvaluarOrganismo.class);
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
                CodigueraDAO<SgItemEvaluarOrganismo> codDAO = new CodigueraDAO<>(em, SgItemEvaluarOrganismo.class);
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
     * @param ieo SgItemEvaluarOrganismo
     * @return SgItemEvaluarOrganismo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgItemEvaluarOrganismo guardar(SgItemEvaluarOrganismo ieo) throws GeneralException {
        try {
            if (ieo != null) {
                if (ItemEvaluarOrganismoValidacionNegocio.validar(ieo)) {
                    CodigueraDAO<SgItemEvaluarOrganismo> codDAO = new CodigueraDAO<>(em, SgItemEvaluarOrganismo.class);
                    return codDAO.guardar(ieo);
                    
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ieo;
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
            CodigueraDAO<SgItemEvaluarOrganismo> codDAO = new CodigueraDAO<>(em, SgItemEvaluarOrganismo.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgItemEvaluarOrganismo>
     * @throws GeneralException
     */
    public List<SgItemEvaluarOrganismo> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgItemEvaluarOrganismo> codDAO = new CodigueraDAO<>(em, SgItemEvaluarOrganismo.class);
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
                CodigueraDAO<SgItemEvaluarOrganismo> codDAO = new CodigueraDAO<>(em, SgItemEvaluarOrganismo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
