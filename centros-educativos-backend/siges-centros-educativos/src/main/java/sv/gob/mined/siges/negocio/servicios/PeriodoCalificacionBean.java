/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.filtros.FiltroRelModEdModAten;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.PeriodoCalificacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PeriodoCalificacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class PeriodoCalificacionBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private RelModEdModAtenBean relModEdModAtenBean;


    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPeriodoCalificacion
     * @throws GeneralException
     */
    public SgPeriodoCalificacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgPeriodoCalificacion.class);
                SgPeriodoCalificacion ret = codDAO.obtenerPorId(id, null);
                if (ret.getPcaRangoFecha() != null){
                    ret.getPcaRangoFecha().size();
                }
                return ret;
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgPeriodoCalificacion.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param pca SgPeriodoCalificacion
     * @return SgPeriodoCalificacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgPeriodoCalificacion guardar(SgPeriodoCalificacion pca) throws GeneralException {
        try {
            if (pca != null) {
                if (PeriodoCalificacionValidacionNegocio.validar(pca)) {
                    FiltroRelModEdModAten fm=new FiltroRelModEdModAten();
                    fm.setReaModalidadEducativa(pca.getPcaModalidad().getModPk());
                    fm.setReaModalidadAtencion(pca.getPcaModalidadAtencion().getMatPk());
                    List<SgRelModEdModAten> list=relModEdModAtenBean.obtenerPorFiltro(fm);
                    if(!list.isEmpty()){
                        if(list.get(0).getReaSubModalidadAtencion()!=null && pca.getPcaSubModalidadAtencion()==null){
                            BusinessException ge = new BusinessException();
                            ge.addError(Errores.ERROR_SUB_MODALIDAD_VACIO);
                            throw ge;
                        }
                    }
                    CodigueraDAO<SgPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgPeriodoCalificacion.class);
                    return codDAO.guardar(pca, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pca;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroPeriodoCalificacion filtro) throws GeneralException {
        try {
            PeriodoCalificacionDAO codDAO = new PeriodoCalificacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgPeriodoCalificacion>
     * @throws GeneralException
     */
    public List<SgPeriodoCalificacion> obtenerPorFiltro(FiltroPeriodoCalificacion filtro) throws GeneralException {
        try {
            PeriodoCalificacionDAO codDAO = new PeriodoCalificacionDAO(em);
            List<SgPeriodoCalificacion> listPer = codDAO.obtenerPorFiltro(filtro);
            if (filtro.getInicializarRangoFecha()) {
                for (SgPeriodoCalificacion pc : listPer) {
                    pc.getPcaRangoFecha().size();
                }
            }
            //TODO: deberia haber un booleano para indicar si cargar o no
            if (filtro.getIncluirCampos() == null){
                for (SgPeriodoCalificacion pc : listPer) {
                    if(pc.getPcaCalendario().getCalActividadesCalendario()!=null){
                        pc.getPcaCalendario().getCalActividadesCalendario().size();
                    }                
                }
            }
            return listPer;
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
                CodigueraDAO<SgPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgPeriodoCalificacion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
