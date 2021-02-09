/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalendario;
import sv.gob.mined.siges.negocio.validaciones.CalendarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CalendarioDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalendario;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class CalendarioBean {
    
    @PersistenceContext
    private EntityManager em;
    

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalendario
     * @throws GeneralException
     */
    public SgCalendario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCalendario> codDAO = new CodigueraDAO<>(em, SgCalendario.class);
                SgCalendario ret = codDAO.obtenerPorId(id, null);
                if (ret != null){
                    if (ret.getCalPeriodosCalificacion() != null){
                        for (SgPeriodoCalificacion p : ret.getCalPeriodosCalificacion()){
                            if (p.getPcaRangoFecha() != null){
                                p.getPcaRangoFecha().size();
                            }
                        }
                    }
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
                CodigueraDAO<SgCalendario> codDAO = new CodigueraDAO<>(em, SgCalendario.class);
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
     * @param cal SgCalendario
     * @return SgCalendario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCalendario guardar(SgCalendario cal) throws GeneralException {
        try {
            if (cal != null) {
                if (CalendarioValidacionNegocio.validar(cal)) {
                    CodigueraDAO<SgCalendario> codDAO = new CodigueraDAO<>(em, SgCalendario.class);
                    return codDAO.guardar(cal, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        
        return cal;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCalendario filtro) throws GeneralException {
        try {
            CalendarioDAO codDAO = new CalendarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalendario>
     * @throws GeneralException
     */
    public List<SgCalendario> obtenerPorFiltro(FiltroCalendario filtro) throws GeneralException {
        try {
            CalendarioDAO codDAO = new CalendarioDAO(em);
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
                CodigueraDAO<SgCalendario> codDAO = new CodigueraDAO<>(em, SgCalendario.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
}
