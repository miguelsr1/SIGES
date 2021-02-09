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
import sv.gob.mined.siges.negocio.validaciones.InfTipoAbastecimientoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgInfTipoAbastecimiento;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class InfTipoAbastecimientoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgInfTipoAbastecimiento
     * @throws GeneralException
     */
    public SgInfTipoAbastecimiento obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgInfTipoAbastecimiento> codDAO = new CodigueraDAO<>(em, SgInfTipoAbastecimiento.class);
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
                CodigueraDAO<SgInfTipoAbastecimiento> codDAO = new CodigueraDAO<>(em, SgInfTipoAbastecimiento.class);
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
     * @param tab SgInfTipoAbastecimiento
     * @return SgInfTipoAbastecimiento
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgInfTipoAbastecimiento guardar(SgInfTipoAbastecimiento tab) throws GeneralException {
        try {
            if (tab != null) {
                if (InfTipoAbastecimientoValidacionNegocio.validar(tab)) {
                    CodigueraDAO<SgInfTipoAbastecimiento> codDAO = new CodigueraDAO<>(em, SgInfTipoAbastecimiento.class);
                
                        return codDAO.guardar(tab);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tab;
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
            CodigueraDAO<SgInfTipoAbastecimiento> codDAO = new CodigueraDAO<>(em, SgInfTipoAbastecimiento.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgInfTipoAbastecimiento>
     * @throws GeneralException
     */
    public List<SgInfTipoAbastecimiento> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgInfTipoAbastecimiento> codDAO = new CodigueraDAO<>(em, SgInfTipoAbastecimiento.class);
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
                CodigueraDAO<SgInfTipoAbastecimiento> codDAO = new CodigueraDAO<>(em, SgInfTipoAbastecimiento.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
