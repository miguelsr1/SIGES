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
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgConfiguracionFirmaElectronica;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ConfiguracionFirmaElectronicaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgConfiguracionFirmaElectronica
     * @throws GeneralException
     */
    public SgConfiguracionFirmaElectronica obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgConfiguracionFirmaElectronica> codDAO = new CodigueraDAO<>(em, SgConfiguracionFirmaElectronica.class);
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
                CodigueraDAO<SgConfiguracionFirmaElectronica> codDAO = new CodigueraDAO<>(em, SgConfiguracionFirmaElectronica.class);
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
     * @param con SgConfiguracionFirmaElectronica
     * @return SgConfiguracionFirmaElectronica
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConfiguracionFirmaElectronica guardar(SgConfiguracionFirmaElectronica con) throws GeneralException {
        try {
            if (con != null) {
                CodigueraDAO<SgConfiguracionFirmaElectronica> codDAO = new CodigueraDAO<>(em, SgConfiguracionFirmaElectronica.class);
                return codDAO.guardar(con);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return con;
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
            CodigueraDAO<SgConfiguracionFirmaElectronica> codDAO = new CodigueraDAO<>(em, SgConfiguracionFirmaElectronica.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgConfiguracionFirmaElectronica>
     * @throws GeneralException
     */
    public List<SgConfiguracionFirmaElectronica> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgConfiguracionFirmaElectronica> codDAO = new CodigueraDAO<>(em, SgConfiguracionFirmaElectronica.class);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }


}
