/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.negocio.validaciones.DatosResidencialesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDatosResidencialesPersona;

@Stateless
@Traced
public class DatosResidencialesPersonaBean {

    private static final Logger LOGGER = Logger.getLogger(DatosResidencialesPersonaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    @ConfigProperty(name = "email-valid-pattern")
    private String emailPattern;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPersona
     * @throws GeneralException
     */
    public SgDatosResidencialesPersona obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDatosResidencialesPersona> dao = new CodigueraDAO<>(em, SgDatosResidencialesPersona.class);
                SgDatosResidencialesPersona ret = dao.obtenerPorId(id, null);
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
                CodigueraDAO<SgDatosResidencialesPersona> codDAO = new CodigueraDAO<>(em, SgDatosResidencialesPersona.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    public SgDatosResidencialesPersona guardar(SgDatosResidencialesPersona entity) throws GeneralException {
        try {
            if (entity != null) {
                if (DatosResidencialesValidacionNegocio.validar(entity)) {

                    entity.setPerPk(entity.getPerPersona().getPerPk()); //@mapsid

                    if (entity.getPerVersion() == null) {
                        em.persist(entity);
                    } else {
                        entity = em.merge(entity);
                    }
                    
                    return entity;
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
     * Elimina el objeto indicado
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(SgDatosResidencialesPersona per) throws GeneralException {
        if (per != null) {
            try {
                CodigueraDAO<SgDatosResidencialesPersona> codDAO = new CodigueraDAO<>(em, SgDatosResidencialesPersona.class);
                codDAO.eliminar(per, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
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
                CodigueraDAO<SgDatosResidencialesPersona> codDAO = new CodigueraDAO<>(em, SgDatosResidencialesPersona.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve SgPersona en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return T
     */
    public SgDatosResidencialesPersona obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgDatosResidencialesPersona> respuesta = reader.createQuery().forEntitiesAtRevision(SgDatosResidencialesPersona.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgDatosResidencialesPersona ret = respuesta.get(0);
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

}
