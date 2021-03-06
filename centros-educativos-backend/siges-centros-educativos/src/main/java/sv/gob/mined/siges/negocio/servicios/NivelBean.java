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
import sv.gob.mined.siges.filtros.FiltroNivel;
import sv.gob.mined.siges.negocio.validaciones.NivelValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.NivelDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCiclo;
import sv.gob.mined.siges.persistencia.entidades.SgNivel;

@Stateless
@Traced
public class NivelBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgNivel
     * @throws GeneralException
     */
    public SgNivel obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgNivel> codDAO = new CodigueraDAO<>(em, SgNivel.class);
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
                CodigueraDAO<SgNivel> codDAO = new CodigueraDAO<>(em, SgNivel.class);
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
     * @param entity SgNivel
     * @return SgNivel
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgNivel guardar(SgNivel entity) throws GeneralException {
        try {
            if (entity != null) {
                if (NivelValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgNivel> codDAO = new CodigueraDAO<>(em, SgNivel.class);
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
                CodigueraDAO<SgNivel> codDAO = new CodigueraDAO<>(em, SgNivel.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroNivel filtro) throws GeneralException {
        try {
            NivelDAO codDAO = new NivelDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgNivel
     * @throws GeneralException
     */
    public List<SgNivel> obtenerPorFiltro(FiltroNivel filtro) throws GeneralException {
        try {
            NivelDAO codDAO = new NivelDAO(em);
            List<SgNivel> listNiv = codDAO.obtenerPorFiltro(filtro);
            if (filtro.getInicializarCiclos()) {
                for (SgNivel niv : listNiv) {
                    if (niv.getNivCiclos() != null) {
                        niv.getNivCiclos().size();
                        for (SgCiclo cic : niv.getNivCiclos()) {
                            if (cic.getCicModalidades() != null) {
                                cic.getCicModalidades().size();
                            }
                        }
                    }
                }
            }
            return listNiv;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
