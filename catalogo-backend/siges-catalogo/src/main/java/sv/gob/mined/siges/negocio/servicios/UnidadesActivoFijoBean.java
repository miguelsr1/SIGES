/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.negocio.validaciones.UnidadesActivoFijoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.UnidadesActivoFijoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfUnidadesActivoFijo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class UnidadesActivoFijoBean {
    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfUnidadesActivoFijo
     * @throws GeneralException
     */
    public SgAfUnidadesActivoFijo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfUnidadesActivoFijo> codDAO = new CodigueraDAO<>(em, SgAfUnidadesActivoFijo.class);
                SgAfUnidadesActivoFijo unidadAF = codDAO.obtenerPorId(id);
                if(unidadAF != null && unidadAF.getSgAfComisionDescargoList() != null){
                    unidadAF.getSgAfComisionDescargoList().size();
                }
                return unidadAF;
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
                CodigueraDAO<SgAfUnidadesActivoFijo> codDAO = new CodigueraDAO<>(em, SgAfUnidadesActivoFijo.class);
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
     * @param tri SgAfUnidadesActivoFijo
     * @return SgAfUnidadesActivoFijo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAfUnidadesActivoFijo guardar(SgAfUnidadesActivoFijo tri) throws GeneralException {
        try {
            if (tri != null) {
                if (UnidadesActivoFijoValidacionNegocio.validar(tri)) {
                    CodigueraDAO<SgAfUnidadesActivoFijo> codDAO = new CodigueraDAO<>(em, SgAfUnidadesActivoFijo.class);
                    return codDAO.guardar(tri);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tri;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroUnidadesActivoFijo
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroUnidadesActivoFijo filtro) throws GeneralException {
        try {
            UnidadesActivoFijoDAO codDAO = new UnidadesActivoFijoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroUnidadesActivoFijo
     * @return Lista de <SgAfUnidadesActivoFijo>
     * @throws GeneralException
     */
    public List<SgAfUnidadesActivoFijo> obtenerPorFiltro(FiltroUnidadesActivoFijo filtro) throws GeneralException {
        try {
            UnidadesActivoFijoDAO codDAO = new UnidadesActivoFijoDAO(em);
            List<SgAfUnidadesActivoFijo> unidades = codDAO.obtenerPorFiltro(filtro);
            if(unidades != null && !unidades.isEmpty()) {
                for(SgAfUnidadesActivoFijo uaf : unidades) {
                    if(uaf.getSgAfComisionDescargoList() != null) {
                        uaf.getSgAfComisionDescargoList().size();
                    }
                }
            }
            return unidades;
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
                CodigueraDAO<SgAfUnidadesActivoFijo> codDAO = new CodigueraDAO<>(em, SgAfUnidadesActivoFijo.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
