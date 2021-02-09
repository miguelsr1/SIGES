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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleAlmacenamientoAgua;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleAlmacenamientoAguaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleAlmacenimientoAguaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAlmacenamientoAgua;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleAlmacenamientoAguaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleAlmacenamientoAgua
     * @throws GeneralException
     */
    public SgRelInmuebleAlmacenamientoAgua obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleAlmacenamientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAlmacenamientoAgua.class);
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
                CodigueraDAO<SgRelInmuebleAlmacenamientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAlmacenamientoAgua.class);
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
     * @param ial SgRelInmuebleAlmacenamientoAgua
     * @return SgRelInmuebleAlmacenamientoAgua
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleAlmacenamientoAgua guardar(SgRelInmuebleAlmacenamientoAgua ial) throws GeneralException {
        try {
            if (ial != null) {
                if (RelInmuebleAlmacenamientoAguaValidacionNegocio.validar(ial)) {
                    CodigueraDAO<SgRelInmuebleAlmacenamientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAlmacenamientoAgua.class);                   
                   
                        return codDAO.guardar(ial);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ial;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleAlmacenamientoAgua filtro) throws GeneralException {
        try {
            RelInmuebleAlmacenimientoAguaDAO codDAO = new RelInmuebleAlmacenimientoAguaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleAlmacenamientoAgua>
     * @throws GeneralException
     */
    public List<SgRelInmuebleAlmacenamientoAgua> obtenerPorFiltro(FiltroRelInmuebleAlmacenamientoAgua filtro) throws GeneralException {
        try {
            RelInmuebleAlmacenimientoAguaDAO codDAO = new RelInmuebleAlmacenimientoAguaDAO(em);
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
                CodigueraDAO<SgRelInmuebleAlmacenamientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAlmacenamientoAgua.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
