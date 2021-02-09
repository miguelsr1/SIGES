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
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroConsultaGuardada;
import sv.gob.mined.siges.negocio.validaciones.ConsultaGuardadaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ConsultaGuardadaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgConsultaGuardada;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ConsultaGuardadaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgConsultaGuardada
     * @throws GeneralException
     */
    public SgConsultaGuardada obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgConsultaGuardada> codDAO = new CodigueraDAO<>(em, SgConsultaGuardada.class);
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgConsultaGuardada> codDAO = new CodigueraDAO<>(em, SgConsultaGuardada.class);
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
     * @param cgr SgConsultaGuardada
     * @return SgConsultaGuardada
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgConsultaGuardada guardar(SgConsultaGuardada cgr) throws GeneralException {
        try {
            if (cgr != null) {
                if (ConsultaGuardadaValidacionNegocio.validar(cgr)) {
                    CodigueraDAO<SgConsultaGuardada> codDAO = new CodigueraDAO<>(em, SgConsultaGuardada.class);                 
                    return codDAO.guardar(cgr, null);
             
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cgr;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroConsultaGuardada filtro) throws GeneralException {
        try {
            ConsultaGuardadaDAO codDAO = new ConsultaGuardadaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgConsultaGuardada>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgConsultaGuardada> obtenerPorFiltro(FiltroConsultaGuardada filtro) throws GeneralException {
        try {
            ConsultaGuardadaDAO codDAO = new ConsultaGuardadaDAO(em);
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
                CodigueraDAO<SgConsultaGuardada> codDAO = new CodigueraDAO<>(em, SgConsultaGuardada.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
