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
import sv.gob.mined.siges.filtros.FiltroModalidad;
import sv.gob.mined.siges.negocio.validaciones.ModalidadValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ModalidadDAO;
import sv.gob.mined.siges.persistencia.entidades.SgModalidad;
import sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten;

@Stateless
@Traced
public class ModalidadBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado. Inicializa las
     * colecciones.
     *
     * @param id Long
     * @return SgModalidad
     * @throws GeneralException
     */
    public SgModalidad obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgModalidad> codDAO = new CodigueraDAO<>(em, SgModalidad.class);
                SgModalidad m = codDAO.obtenerPorId(id, null);
                return m;
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
     * @return SgModalidad
     * @throws GeneralException
     */
    public SgModalidad obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgModalidad> codDAO = new CodigueraDAO<>(em, SgModalidad.class);
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
                CodigueraDAO<SgModalidad> codDAO = new CodigueraDAO<>(em, SgModalidad.class);
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
     * @param entity SgModalidad
     * @return SgModalidad
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgModalidad guardar(SgModalidad entity) throws GeneralException {
        try {
            if (entity != null) {
                if (ModalidadValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgModalidad> codDAO = new CodigueraDAO<>(em, SgModalidad.class);
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
                CodigueraDAO<SgModalidad> codDAO = new CodigueraDAO<>(em, SgModalidad.class);
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
    public Long obtenerTotalPorFiltro(FiltroModalidad filtro) throws GeneralException {
        try {
            ModalidadDAO codDAO = new ModalidadDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgModalidad
     * @throws GeneralException
     */
    public List<SgModalidad> obtenerPorFiltro(FiltroModalidad filtro) throws GeneralException {
        try {
            ModalidadDAO codDAO = new ModalidadDAO(em);
            List<SgModalidad> mods = codDAO.obtenerPorFiltro(filtro);
            if (filtro.getInicializarRel()) {
                for (SgModalidad m : mods) {                 
                        m.setModRelModalidadAtencion(obtenerRelModalidad(m.getModPk()));                    
                    
                }
            }
            return mods;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    public List<SgRelModEdModAten> obtenerRelModalidad(Long modPk) {
        String query = "select * from centros_educativos.sg_rel_mod_ed_mod_aten rel inner join catalogo.sg_modalidades_atencion mat\n" +
            "on rel.rea_modalidad_atencion_fk=mat.mat_pk\n" +
            "where rea_modalidad_educativa_fk= :perPk";
        javax.persistence.Query q = em.createNativeQuery(query, SgRelModEdModAten.class);
        q.setParameter("perPk", modPk);

        List<SgRelModEdModAten> objs = q.getResultList();
        return objs;
    }

}
