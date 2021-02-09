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
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroOrganizacionCurricular;
import sv.gob.mined.siges.negocio.validaciones.OrganizacionCurricularValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.OrganizacionCurricularDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCiclo;
import sv.gob.mined.siges.persistencia.entidades.SgNivel;
import sv.gob.mined.siges.persistencia.entidades.SgOrganizacionCurricular;

@Stateless
@Traced
public class OrganizacionCurricularBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgOrganizacionCurricular
     * @throws GeneralException
     */
    public SgOrganizacionCurricular obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOrganizacionCurricular> codDAO = new CodigueraDAO<>(em, SgOrganizacionCurricular.class);
                SgOrganizacionCurricular c = codDAO.obtenerPorId(id, null);
                for (SgNivel niv : c.getOcuNiveles()) {
                    for (SgCiclo cic : niv.getNivCiclos()) {
                        cic.getCicModalidades().size();
                    }
                }
                if (c.getOcuPlantillas() != null){
                    c.getOcuPlantillas().size();
                }
                return c;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgOrganizacionCurricular
     * @throws GeneralException
     */
    public SgOrganizacionCurricular obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgOrganizacionCurricular> codDAO = new CodigueraDAO<>(em, SgOrganizacionCurricular.class);
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
                CodigueraDAO<SgOrganizacionCurricular> codDAO = new CodigueraDAO<>(em, SgOrganizacionCurricular.class);
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
     * @param entity SgOrganizacionCurricular
     * @return SgOrganizacionCurricular
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgOrganizacionCurricular guardar(SgOrganizacionCurricular entity) throws GeneralException {
        try {
            if (entity != null) {
                if (OrganizacionCurricularValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgOrganizacionCurricular> codDAO = new CodigueraDAO<>(em, SgOrganizacionCurricular.class);
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
                CodigueraDAO<SgOrganizacionCurricular> codDAO = new CodigueraDAO<>(em, SgOrganizacionCurricular.class);
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
    public Long obtenerTotalPorFiltro(FiltroOrganizacionCurricular filtro) throws GeneralException {
        try {
            OrganizacionCurricularDAO codDAO = new OrganizacionCurricularDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgOrganizacionCurricular
     * @throws GeneralException
     */
    public List<SgOrganizacionCurricular> obtenerPorFiltro(FiltroOrganizacionCurricular filtro) throws GeneralException {
        try {
            OrganizacionCurricularDAO codDAO = new OrganizacionCurricularDAO(em);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
