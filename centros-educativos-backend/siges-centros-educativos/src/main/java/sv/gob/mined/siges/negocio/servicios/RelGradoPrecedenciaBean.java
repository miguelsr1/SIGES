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
import sv.gob.mined.siges.filtros.FiltroRelGradoPrecedencia;
import sv.gob.mined.siges.negocio.validaciones.RelGradoPrecedenciaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelGradoPrecedenciaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelGradoPrecedencia;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelGradoPrecedenciaBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelGradoPrecedencia
     * @throws GeneralException
     */
    public SgRelGradoPrecedencia obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelGradoPrecedencia> codDAO = new CodigueraDAO<>(em, SgRelGradoPrecedencia.class);
                return codDAO.obtenerPorId(id,null);
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
                CodigueraDAO<SgRelGradoPrecedencia> codDAO = new CodigueraDAO<>(em, SgRelGradoPrecedencia.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param rgp SgRelGradoPrecedencia
     * @return SgRelGradoPrecedencia
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelGradoPrecedencia guardar(SgRelGradoPrecedencia rgp) throws GeneralException {
        try {
            if (rgp != null) {
                if (RelGradoPrecedenciaValidacionNegocio.validar(rgp)) {
                    CodigueraDAO<SgRelGradoPrecedencia> codDAO = new CodigueraDAO<>(em, SgRelGradoPrecedencia.class);
                    return codDAO.guardar(rgp,null);                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rgp;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelGradoPrecedencia filtro) throws GeneralException {
        try {
            RelGradoPrecedenciaDAO codDAO = new RelGradoPrecedenciaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelGradoPrecedencia>
     * @throws GeneralException
     */
    public List<SgRelGradoPrecedencia> obtenerPorFiltro(FiltroRelGradoPrecedencia filtro) throws GeneralException {
        try {
            RelGradoPrecedenciaDAO codDAO = new RelGradoPrecedenciaDAO(em);
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
                CodigueraDAO<SgRelGradoPrecedencia> codDAO = new CodigueraDAO<>(em, SgRelGradoPrecedencia.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
