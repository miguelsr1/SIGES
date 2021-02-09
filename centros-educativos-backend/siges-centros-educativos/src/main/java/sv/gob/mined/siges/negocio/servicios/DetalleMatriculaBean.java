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
import sv.gob.mined.siges.filtros.FiltroDetalleMatricula;
import sv.gob.mined.siges.negocio.validaciones.DetalleMatriculaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DetalleMatriculaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDetalleMatricula;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class DetalleMatriculaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDetalleMatricula
     * @throws GeneralException
     */
    public SgDetalleMatricula obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDetalleMatricula> codDAO = new CodigueraDAO<>(em, SgDetalleMatricula.class);
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
                CodigueraDAO<SgDetalleMatricula> codDAO = new CodigueraDAO<>(em, SgDetalleMatricula.class);
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
     * @param dem SgDetalleMatricula
     * @return SgDetalleMatricula
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDetalleMatricula guardar(SgDetalleMatricula dem) throws GeneralException {
        try {
            if (dem != null) {
                if (DetalleMatriculaValidacionNegocio.validar(dem)) {
                    CodigueraDAO<SgDetalleMatricula> codDAO = new CodigueraDAO<>(em, SgDetalleMatricula.class);
                
                        return codDAO.guardar(dem,null);
                    
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return dem;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDetalleMatricula filtro) throws GeneralException {
        try {
            DetalleMatriculaDAO codDAO = new DetalleMatriculaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgDetalleMatricula>
     * @throws GeneralException
     */
    public List<SgDetalleMatricula> obtenerPorFiltro(FiltroDetalleMatricula filtro) throws GeneralException {
        try {
            DetalleMatriculaDAO codDAO = new DetalleMatriculaDAO(em);
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
                CodigueraDAO<SgDetalleMatricula> codDAO = new CodigueraDAO<>(em, SgDetalleMatricula.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
