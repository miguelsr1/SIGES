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
import sv.gob.mined.siges.filtros.FiltroRelModEdModAten;
import sv.gob.mined.siges.negocio.validaciones.RelModEdModAtenValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelModEdModAtenDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten;

@Stateless
@Traced
public class RelModEdModAtenBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelModEdModAten
     * @throws GeneralException
     */
    public SgRelModEdModAten obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelModEdModAten> codDAO = new CodigueraDAO<>(em, SgRelModEdModAten.class);
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
                CodigueraDAO<SgRelModEdModAten> codDAO = new CodigueraDAO<>(em, SgRelModEdModAten.class);
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
     * @param entity SgRelModEdModAten
     * @return SgRelModEdModAten
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelModEdModAten guardar(SgRelModEdModAten entity) throws GeneralException {
        try {
            if (entity != null) {
                if (RelModEdModAtenValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgRelModEdModAten> codDAO = new CodigueraDAO<>(em, SgRelModEdModAten.class);
                    return codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
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
                CodigueraDAO<SgRelModEdModAten> codDAO = new CodigueraDAO<>(em, SgRelModEdModAten.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroRelModEdModAten
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroRelModEdModAten filtro) throws GeneralException {
        try {
            RelModEdModAtenDAO DAO = new RelModEdModAtenDAO(em);
            return DAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroRelModEdModAten
     * @return Lista de SgRelModEdModAten
     * @throws GeneralException
     */
    public List<SgRelModEdModAten> obtenerPorFiltro(FiltroRelModEdModAten filtro) throws GeneralException {
        try {
            RelModEdModAtenDAO DAO = new RelModEdModAtenDAO(em);
            List<SgRelModEdModAten> rels = DAO.obtenerPorFiltro(filtro);

            if (filtro.getInicializarGrados()) {
                for (SgRelModEdModAten m : rels) {
                    m.getReaGrado().size();
                }
            }
            return rels;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
