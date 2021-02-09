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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleManejoDesechos;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleManejoDesechosValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleManejoDesechosDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleManejoDesechos;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleManejoDesechosBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleManejoDesechos
     * @throws GeneralException
     */
    public SgRelInmuebleManejoDesechos obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleManejoDesechos> codDAO = new CodigueraDAO<>(em, SgRelInmuebleManejoDesechos.class);
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
                CodigueraDAO<SgRelInmuebleManejoDesechos> codDAO = new CodigueraDAO<>(em, SgRelInmuebleManejoDesechos.class);
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
     * @param imd SgRelInmuebleManejoDesechos
     * @return SgRelInmuebleManejoDesechos
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleManejoDesechos guardar(SgRelInmuebleManejoDesechos imd) throws GeneralException {
        try {
            if (imd != null) {
                if (RelInmuebleManejoDesechosValidacionNegocio.validar(imd)) {
                    CodigueraDAO<SgRelInmuebleManejoDesechos> codDAO = new CodigueraDAO<>(em, SgRelInmuebleManejoDesechos.class);                   
                   
                        return codDAO.guardar(imd);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return imd;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleManejoDesechos filtro) throws GeneralException {
        try {
            RelInmuebleManejoDesechosDAO codDAO = new RelInmuebleManejoDesechosDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleManejoDesechos>
     * @throws GeneralException
     */
    public List<SgRelInmuebleManejoDesechos> obtenerPorFiltro(FiltroRelInmuebleManejoDesechos filtro) throws GeneralException {
        try {
            RelInmuebleManejoDesechosDAO codDAO = new RelInmuebleManejoDesechosDAO(em);
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
                CodigueraDAO<SgRelInmuebleManejoDesechos> codDAO = new CodigueraDAO<>(em, SgRelInmuebleManejoDesechos.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
