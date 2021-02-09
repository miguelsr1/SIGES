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
import sv.gob.mined.siges.filtros.FiltroCargoOAE;
import sv.gob.mined.siges.negocio.validaciones.CargoOAEValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CargoOAEDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCargoOAE;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CargoOAEBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCargoOAE
     * @throws GeneralException
     */
    public SgCargoOAE obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCargoOAE> codDAO = new CodigueraDAO<>(em, SgCargoOAE.class);
                SgCargoOAE ret= codDAO.obtenerPorId(id);
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
                CodigueraDAO<SgCargoOAE> codDAO = new CodigueraDAO<>(em, SgCargoOAE.class);
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
     * @param coa SgCargoOAE
     * @return SgCargoOAE
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCargoOAE guardar(SgCargoOAE coa) throws GeneralException {
        try {
            if (coa != null) {
                if (CargoOAEValidacionNegocio.validar(coa)) {
                    CodigueraDAO<SgCargoOAE> codDAO = new CodigueraDAO<>(em, SgCargoOAE.class);                    
                    return codDAO.guardar(coa);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return coa;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCargoOAE filtro) throws GeneralException {
        try {
            CargoOAEDAO dao = new CargoOAEDAO(em);
            return dao.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCargoOAE>
     * @throws GeneralException
     */
    public List<SgCargoOAE> obtenerPorFiltro(FiltroCargoOAE filtro) throws GeneralException {
        try {
            CargoOAEDAO dao = new CargoOAEDAO(em);
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
                CodigueraDAO<SgCargoOAE> codDAO = new CodigueraDAO<>(em, SgCargoOAE.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
