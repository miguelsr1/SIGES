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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleAccesibilidad;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleAccesibilidadValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleAccesibilidadDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAccesibilidad;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleAccesibilidadBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleAccesibilidad
     * @throws GeneralException
     */
    public SgRelInmuebleAccesibilidad obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleAccesibilidad> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAccesibilidad.class);
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
                CodigueraDAO<SgRelInmuebleAccesibilidad> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAccesibilidad.class);
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
     * @param iac SgRelInmuebleAccesibilidad
     * @return SgRelInmuebleAccesibilidad
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleAccesibilidad guardar(SgRelInmuebleAccesibilidad iac) throws GeneralException {
        try {
            if (iac != null) {
                if (RelInmuebleAccesibilidadValidacionNegocio.validar(iac)) {
                    CodigueraDAO<SgRelInmuebleAccesibilidad> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAccesibilidad.class);                   
                   
                        return codDAO.guardar(iac);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return iac;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleAccesibilidad filtro) throws GeneralException {
        try {
            RelInmuebleAccesibilidadDAO codDAO = new RelInmuebleAccesibilidadDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleAccesibilidad>
     * @throws GeneralException
     */
    public List<SgRelInmuebleAccesibilidad> obtenerPorFiltro(FiltroRelInmuebleAccesibilidad filtro) throws GeneralException {
        try {
            RelInmuebleAccesibilidadDAO codDAO = new RelInmuebleAccesibilidadDAO(em);
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
                CodigueraDAO<SgRelInmuebleAccesibilidad> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAccesibilidad.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
