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
import sv.gob.mined.siges.negocio.validaciones.TipoOrganismoAdministrativoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoOrganismoAdministrativo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class TipoOrganismoAdministrativoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoOrganismoAdministrativo
     * @throws GeneralException
     */
    public SgTipoOrganismoAdministrativo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoOrganismoAdministrativo> codDAO = new CodigueraDAO<>(em, SgTipoOrganismoAdministrativo.class);
                SgTipoOrganismoAdministrativo ret = codDAO.obtenerPorId(id);
                if (ret.getToaItems() != null){
                    ret.getToaItems().size();
                }
                return ret;
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
                CodigueraDAO<SgTipoOrganismoAdministrativo> codDAO = new CodigueraDAO<>(em, SgTipoOrganismoAdministrativo.class);
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
     * @param toa SgTipoOrganismoAdministrativo
     * @return SgTipoOrganismoAdministrativo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoOrganismoAdministrativo guardar(SgTipoOrganismoAdministrativo toa) throws GeneralException {
        try {
            if (toa != null) {
                if (TipoOrganismoAdministrativoValidacionNegocio.validar(toa)) {
                    CodigueraDAO<SgTipoOrganismoAdministrativo> codDAO = new CodigueraDAO<>(em, SgTipoOrganismoAdministrativo.class);
                    return codDAO.guardar(toa);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return toa;
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
            CodigueraDAO<SgTipoOrganismoAdministrativo> codDAO = new CodigueraDAO<>(em, SgTipoOrganismoAdministrativo.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTipoOrganismoAdministrativo>
     * @throws GeneralException
     */
    public List<SgTipoOrganismoAdministrativo> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgTipoOrganismoAdministrativo> codDAO = new CodigueraDAO<>(em, SgTipoOrganismoAdministrativo.class);
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
                CodigueraDAO<SgTipoOrganismoAdministrativo> codDAO = new CodigueraDAO<>(em, SgTipoOrganismoAdministrativo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
