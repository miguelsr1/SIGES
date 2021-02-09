/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRangoFecha;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.RangoFechaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RangoFechaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.SgRangoFecha;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class RangoFechaBean {

    @PersistenceContext
    private EntityManager em;


    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRangoFecha
     * @throws GeneralException
     */
    public SgRangoFecha obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRangoFecha> codDAO = new CodigueraDAO<>(em, SgRangoFecha.class);
                return codDAO.obtenerPorId(id, null);
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
                CodigueraDAO<SgRangoFecha> codDAO = new CodigueraDAO<>(em, SgRangoFecha.class);
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
     * @param rfe SgRangoFecha
     * @return SgRangoFecha
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRangoFecha guardar(SgRangoFecha rfe) throws GeneralException {
        try {
            if (rfe != null) {
                if (RangoFechaValidacionNegocio.validar(rfe)) {
                    CodigueraDAO<SgRangoFecha> codDAO = new CodigueraDAO<>(em, SgRangoFecha.class);

                    SgPeriodoCalificacion periodo = em.find(SgPeriodoCalificacion.class, rfe.getRfePeriodoCalificacion().getPcaPk());

                    BusinessException ge = new BusinessException();
                            
                    FiltroRangoFecha filtro = new FiltroRangoFecha();
                    filtro.setPeriodoCalificacionPk(periodo.getPcaPk());
                    if (rfe.getRfePk() == null) {
                        if (this.obtenerTotalPorFiltro(filtro) >= periodo.getPcaNumero()) {
                            ge.addError(Errores.ERROR_CANTIDAD_DE_RANGOS_SUPERA_PERMITIDOS_EN_PERIODO);
                            throw ge;
                        }
                    }
                    
                    filtro.setDesde(rfe.getRfeFechaDesde());
                    filtro.setHasta(rfe.getRfeFechaHasta());
                    filtro.setExcluirRangoPk(rfe.getRfePk());
                    if(this.obtenerTotalPorFiltro(filtro) > 0){
                        ge.addError(Errores.ERROR_FECHA_SUPERPONE);
                        throw ge;
                    }

                    return codDAO.guardar(rfe, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rfe;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroRangoFecha filtro) throws GeneralException {
        try {
            RangoFechaDAO codDAO = new RangoFechaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRangoFecha>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgRangoFecha> obtenerPorFiltro(FiltroRangoFecha filtro) throws GeneralException {
        try {
            RangoFechaDAO codDAO = new RangoFechaDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
        
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @CacheResult(cacheName = Constantes.RANGO_FECHA_CACHE)
    public List<SgRangoFecha> obtenerPorFiltroConCache(FiltroRangoFecha filtro) throws GeneralException {
        try {
            RangoFechaDAO codDAO = new RangoFechaDAO(em);
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
                CodigueraDAO<SgRangoFecha> codDAO = new CodigueraDAO<>(em, SgRangoFecha.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
