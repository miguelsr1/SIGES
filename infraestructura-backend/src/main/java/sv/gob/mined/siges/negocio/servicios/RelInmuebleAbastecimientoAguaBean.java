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
import sv.gob.mined.siges.filtros.FiltroRelInmuebleAbastecimientoAgua;
import sv.gob.mined.siges.negocio.validaciones.RelInmuebleAbastecimientoAguaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelInmuebleAbastecimientoAguaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAbastecimientoAgua;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelInmuebleAbastecimientoAguaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelInmuebleAbastecimientoAgua
     * @throws GeneralException
     */
    public SgRelInmuebleAbastecimientoAgua obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelInmuebleAbastecimientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAbastecimientoAgua.class);
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
                CodigueraDAO<SgRelInmuebleAbastecimientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAbastecimientoAgua.class);
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
     * @param iaa SgRelInmuebleAbastecimientoAgua
     * @return SgRelInmuebleAbastecimientoAgua
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelInmuebleAbastecimientoAgua guardar(SgRelInmuebleAbastecimientoAgua iaa) throws GeneralException {
        try {
            if (iaa != null) {
                if (RelInmuebleAbastecimientoAguaValidacionNegocio.validar(iaa)) {
                    CodigueraDAO<SgRelInmuebleAbastecimientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAbastecimientoAgua.class);                   
                   
                        return codDAO.guardar(iaa);
                 
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return iaa;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelInmuebleAbastecimientoAgua filtro) throws GeneralException {
        try {
            RelInmuebleAbastecimientoAguaDAO codDAO = new RelInmuebleAbastecimientoAguaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelInmuebleAbastecimientoAgua>
     * @throws GeneralException
     */
    public List<SgRelInmuebleAbastecimientoAgua> obtenerPorFiltro(FiltroRelInmuebleAbastecimientoAgua filtro) throws GeneralException {
        try {
            RelInmuebleAbastecimientoAguaDAO codDAO = new RelInmuebleAbastecimientoAguaDAO(em);
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
                CodigueraDAO<SgRelInmuebleAbastecimientoAgua> codDAO = new CodigueraDAO<>(em, SgRelInmuebleAbastecimientoAgua.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
