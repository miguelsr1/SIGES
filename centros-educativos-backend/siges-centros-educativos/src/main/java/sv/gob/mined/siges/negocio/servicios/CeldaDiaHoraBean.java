/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCeldaDiaHora;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.CeldaDiaHoraValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CeldaDiaHoraDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class CeldaDiaHoraBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCeldaDiaHora
     * @throws GeneralException
     */
    public SgCeldaDiaHora obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCeldaDiaHora> codDAO = new CodigueraDAO<>(em, SgCeldaDiaHora.class);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCeldaDiaHora> codDAO = new CodigueraDAO<>(em, SgCeldaDiaHora.class);
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
     * @param cdh SgCeldaDiaHora
     * @return SgCeldaDiaHora
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCeldaDiaHora guardar(SgCeldaDiaHora cdh) throws GeneralException {
        try {
            if (cdh != null) {
                if (CeldaDiaHoraValidacionNegocio.validar(cdh)) {
                    CodigueraDAO<SgCeldaDiaHora> codDAO = new CodigueraDAO<>(em, SgCeldaDiaHora.class);
                    return codDAO.guardar(cdh, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cdh;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgCeldaDiaHora> codDAO = new CodigueraDAO<>(em, SgCeldaDiaHora.class);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCeldaDiaHora>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgCeldaDiaHora> obtenerPorFiltro(FiltroCeldaDiaHora filtro) throws GeneralException {
        try {
            CeldaDiaHoraDAO codDAO = new CeldaDiaHoraDAO(em);
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
                CodigueraDAO<SgCeldaDiaHora> codDAO = new CodigueraDAO<>(em, SgCeldaDiaHora.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
