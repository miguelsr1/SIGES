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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleTipoDrenaje;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleTipoDrenajeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleTipoDrenajeDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleTipoDrenaje;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleTipoDrenajeBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleTipoDrenaje
     * @throws GeneralException
     */
    public SgRelInmuebleTipoDrenaje obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleTipoDrenaje> codDAO = new CodigueraDAO<>(em, SgRelInmuebleTipoDrenaje.class);
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
                CodigueraDAO<SgRelInmuebleTipoDrenaje> codDAO = new CodigueraDAO<>(em, SgRelInmuebleTipoDrenaje.class);
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
     * @param itd SgRelInmuebleTipoDrenaje
     * @return SgRelInmuebleTipoDrenaje
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleTipoDrenaje guardar(SgRelInmuebleTipoDrenaje itd) throws GeneralException {
        try {
            if (itd != null) {
                if (RelInmuebleTipoDrenajeValidacionNegocio.validar(itd)) {
                    CodigueraDAO<SgRelInmuebleTipoDrenaje> codDAO = new CodigueraDAO<>(em, SgRelInmuebleTipoDrenaje.class);                   
                   
                        return codDAO.guardar(itd);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return itd;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleTipoDrenaje filtro) throws GeneralException {
        try {
            RelInmuebleTipoDrenajeDAO codDAO = new RelInmuebleTipoDrenajeDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleTipoDrenaje>
     * @throws GeneralException
     */
    public List<SgRelInmuebleTipoDrenaje> obtenerPorFiltro(FiltroRelInmuebleTipoDrenaje filtro) throws GeneralException {
        try {
            RelInmuebleTipoDrenajeDAO codDAO = new RelInmuebleTipoDrenajeDAO(em);
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
                CodigueraDAO<SgRelInmuebleTipoDrenaje> codDAO = new CodigueraDAO<>(em, SgRelInmuebleTipoDrenaje.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
