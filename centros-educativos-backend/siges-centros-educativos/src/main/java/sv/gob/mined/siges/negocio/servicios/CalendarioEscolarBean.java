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
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPeriodoCalendario;
import sv.gob.mined.siges.negocio.validaciones.CalendarioEscolarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PeriodoCalendarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalendario;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class CalendarioEscolarBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCalendarioEscolar
     * @throws GeneralException
     */
    public SgPeriodoCalendario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPeriodoCalendario> codDAO = new CodigueraDAO<>(em, SgPeriodoCalendario.class);
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
                CodigueraDAO<SgPeriodoCalendario> codDAO = new CodigueraDAO<>(em, SgPeriodoCalendario.class);
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
     * @param ces SgCalendarioEscolar
     * @return SgCalendarioEscolar
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPeriodoCalendario guardar(SgPeriodoCalendario ces) throws GeneralException {
        try {
            if (ces != null) {
                if (CalendarioEscolarValidacionNegocio.validar(ces)) {
                    CodigueraDAO<SgPeriodoCalendario> codDAO = new CodigueraDAO<>(em, SgPeriodoCalendario.class);
                    return codDAO.guardar(ces, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ces;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroPeriodoCalendario filtro) throws GeneralException {
        try {
            PeriodoCalendarioDAO codDAO = new PeriodoCalendarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalendarioEscolar>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgPeriodoCalendario> obtenerPorFiltro(FiltroPeriodoCalendario filtro) throws GeneralException {
        try {
            PeriodoCalendarioDAO codDAO = new PeriodoCalendarioDAO(em);
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
                CodigueraDAO<SgPeriodoCalendario> codDAO = new CodigueraDAO<>(em, SgPeriodoCalendario.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
