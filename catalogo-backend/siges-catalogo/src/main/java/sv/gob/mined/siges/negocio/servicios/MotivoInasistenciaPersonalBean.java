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
import sv.gob.mined.siges.negocio.validaciones.MotivoInasistenciaPersonalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoInasistenciaPersonal;
import sv.gob.mined.siges.persistencia.utilidades.LoadLazyCollectionsViewInterceptor;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class MotivoInasistenciaPersonalBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMotivoInasistenciaPersonal
     * @throws GeneralException
     */
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgMotivoInasistenciaPersonal obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMotivoInasistenciaPersonal> codDAO = new CodigueraDAO<>(em, SgMotivoInasistenciaPersonal.class);
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
                CodigueraDAO<SgMotivoInasistenciaPersonal> codDAO = new CodigueraDAO<>(em, SgMotivoInasistenciaPersonal.class);
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
     * @param mip SgMotivoInasistenciaPersonal
     * @return SgMotivoInasistenciaPersonal
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class, LoadLazyCollectionsViewInterceptor.class})
    public SgMotivoInasistenciaPersonal guardar(SgMotivoInasistenciaPersonal mip) throws GeneralException {
        try {
            if (mip != null) {
                if (MotivoInasistenciaPersonalValidacionNegocio.validar(mip)) {
                    CodigueraDAO<SgMotivoInasistenciaPersonal> codDAO = new CodigueraDAO<>(em, SgMotivoInasistenciaPersonal.class);
                    return codDAO.guardar(mip);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mip;
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
            CodigueraDAO<SgMotivoInasistenciaPersonal> codDAO = new CodigueraDAO<>(em, SgMotivoInasistenciaPersonal.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgMotivoInasistenciaPersonal
     * @throws GeneralException
     */
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public List<SgMotivoInasistenciaPersonal> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgMotivoInasistenciaPersonal> codDAO = new CodigueraDAO<>(em, SgMotivoInasistenciaPersonal.class);
            
            List<SgMotivoInasistenciaPersonal> ret = codDAO.obtenerPorFiltro(filtro);
            for (SgMotivoInasistenciaPersonal m : ret){
                    m.getMipCargos().size();
                }
            return ret;
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
                CodigueraDAO<SgMotivoInasistenciaPersonal> codDAO = new CodigueraDAO<>(em, SgMotivoInasistenciaPersonal.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
