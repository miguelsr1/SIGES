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
import sv.gob.mined.siges.filtros.FiltroCargoTitular;
import sv.gob.mined.siges.negocio.validaciones.CargoTitularValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CargoTitularDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCargoTitular;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class CargoTitularBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCargoTitular
     * @throws GeneralException
     */
    public SgCargoTitular obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCargoTitular> codDAO = new CodigueraDAO<>(em, SgCargoTitular.class);
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
                CodigueraDAO<SgCargoTitular> codDAO = new CodigueraDAO<>(em, SgCargoTitular.class);
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
     * @param cti SgCargoTitular
     * @return SgCargoTitular
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCargoTitular guardar(SgCargoTitular cti) throws GeneralException {
        try {
            if (cti != null) {
                if (CargoTitularValidacionNegocio.validar(cti)) {
                    CodigueraDAO<SgCargoTitular> codDAO = new CodigueraDAO<>(em, SgCargoTitular.class);
                                 
                    return codDAO.guardar(cti);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cti;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCargoTitular filtro) throws GeneralException {
        try {
            CargoTitularDAO codDAO = new CargoTitularDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCargoTitular>
     * @throws GeneralException
     */
    public List<SgCargoTitular> obtenerPorFiltro(FiltroCargoTitular filtro) throws GeneralException {
        try {
            CargoTitularDAO codDAO = new CargoTitularDAO(em);
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
                CodigueraDAO<SgCargoTitular> codDAO = new CodigueraDAO<>(em, SgCargoTitular.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
