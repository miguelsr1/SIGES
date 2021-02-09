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
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroReporteDirector;
import sv.gob.mined.siges.negocio.validaciones.ReporteDirectorValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ReporteDirectorDAO;
import sv.gob.mined.siges.persistencia.entidades.SgReporteDirector;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class ReporteDirectorBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;    

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgReporteDirector
     * @throws GeneralException
     */
    public SgReporteDirector obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgReporteDirector> codDAO = new CodigueraDAO<>(em, SgReporteDirector.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_REPORTE_DIRECTOR);
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
                CodigueraDAO<SgReporteDirector> codDAO = new CodigueraDAO<>(em, SgReporteDirector.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_REPORTE_DIRECTOR);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param rdi SgReporteDirector
     * @return SgReporteDirector
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgReporteDirector guardar(SgReporteDirector rdi) throws GeneralException {
        try {
            if (rdi != null) {
                if (ReporteDirectorValidacionNegocio.validar(rdi)) {
                    CodigueraDAO<SgReporteDirector> codDAO = new CodigueraDAO<>(em, SgReporteDirector.class);
                     return codDAO.guardar(rdi, rdi.getRdiPk() == null ? ConstantesOperaciones.CREAR_REPORTE_DIRECTOR : ConstantesOperaciones.ACTUALIZAR_REPORTE_DIRECTOR);                
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rdi;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroReporteDirector filtro) throws GeneralException {
        try {
            ReporteDirectorDAO codDAO = new ReporteDirectorDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_REPORTE_DIRECTOR);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgReporteDirector>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgReporteDirector> obtenerPorFiltro(FiltroReporteDirector filtro) throws GeneralException {
        try {
            ReporteDirectorDAO codDAO = new ReporteDirectorDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_REPORTE_DIRECTOR);
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
                CodigueraDAO<SgReporteDirector> codDAO = new CodigueraDAO<>(em, SgReporteDirector.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_REPORTE_DIRECTOR);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
