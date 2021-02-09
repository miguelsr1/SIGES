/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.negocio.validaciones.PresupuestoEscolarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PresupuestoEscolarDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.InitializeObjectUtils;
import sv.gob.mined.siges.rest.PresupuestoEscolarRecurso;

/**
 * Servicio que gestiona el presupuesto escolar
 *
 * @author Sofis Solutions
 */
@Stateless
public class PresupuestoEscolarBean {

    private static final Logger LOGGER = Logger.getLogger(PresupuestoEscolarRecurso.class.getName());

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPresupuestoEscolar
     * @throws GeneralException
     *
     */
    //@Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgPresupuestoEscolar obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPresupuestoEscolar> codDAO = new CodigueraDAO<>(em, SgPresupuestoEscolar.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    SgPresupuestoEscolar presupuesto = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_PRESUPUESTOS_ESCOLARES);
                    if (presupuesto.getPesMovimientos() != null) {
                        presupuesto.getPesMovimientos().size();
                    }

                    return presupuesto;
                } else {
                    SgPresupuestoEscolar presupuesto = codDAO.obtenerPorId(id, null);
                    if (presupuesto.getPesMovimientos() != null) {
                        presupuesto.getPesMovimientos().size();
                    }

                    return presupuesto;
                }
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
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPresupuestoEscolar> codDAO = new CodigueraDAO<>(em, SgPresupuestoEscolar.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_PRESUPUESTOS_ESCOLARES);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgPresupuestoEscolar
     * @return SgPresupuestoEscolar
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPresupuestoEscolar guardar(SgPresupuestoEscolar entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (PresupuestoEscolarValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgPresupuestoEscolar> codDAO = new CodigueraDAO<>(em, SgPresupuestoEscolar.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getPesPk() == null ? ConstantesOperaciones.CREAR_PRESUPUESTOS_ESCOLARES : ConstantesOperaciones.ACTUALIZAR_PRESUPUESTOS_ESCOLARES);
                    } else {
                        return codDAO.guardar(entity, null);
                    }
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroPresupuestoEscolar
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPresupuestoEscolar filtro) throws GeneralException {
        try {
            PresupuestoEscolarDAO codDAO = new PresupuestoEscolarDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroPresupuestoEscolar
     * @return Lista de <SgPresupuestoEscolar>
     * @throws GeneralException
     */
    public List<SgPresupuestoEscolar> obtenerPorFiltro(FiltroPresupuestoEscolar filtro) throws GeneralException {
        try {
            PresupuestoEscolarDAO codDAO = new PresupuestoEscolarDAO(em,seguridadBean);
            List<SgPresupuestoEscolar> listPres = codDAO.obtenerPorFiltro(filtro,filtro.getSecurityOperation());

            for (SgPresupuestoEscolar t : listPres) {
                if (t.getPesMovimientos() != null && !t.getPesMovimientos().isEmpty()) {
                    t.getPesMovimientos().size();
                }

                if (t.getPesDocumentos() != null && !t.getPesDocumentos().isEmpty()) {
                    t.getPesDocumentos().size();
                }
            }
            return listPres;
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
                CodigueraDAO<SgPresupuestoEscolar> codDAO = new CodigueraDAO<>(em, SgPresupuestoEscolar.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Cambia el campo anulacion de los movimientos asociados al presupuesto.
     *
     * @param entity SgPresupuestoEscolar
     * @return SgPresupuestoEscolar
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPresupuestoEscolar prepararAnulacion(SgPresupuestoEscolar entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                if (entity.getPesPk() != null) {
                    Query est = em.createNativeQuery("UPDATE finanzas.sg_presupuesto_escolar_movimiento SET  mov_anulacion= true where mov_presupuesto_fk = :PresupuestoFk");
                    est.setParameter("PresupuestoFk", entity.getPesPk());
                    est.executeUpdate();
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
     * Cambia el campo anulacion de los movimientos asociados al presupuesto.
     *
     * @param entity SgPresupuestoEscolar
     * @return SgPresupuestoEscolar
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPresupuestoEscolar eliminandoMovEditados(SgPresupuestoEscolar entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                if (entity.getPesPk() != null) {
                    Query deleteEgresos = em.createNativeQuery("DELETE FROM finanzas.sg_presupuesto_escolar_edicion_movimiento where  mov_tipo = 'EM' and  mov_presupuesto_fk = :PresupuestoFk");
                    deleteEgresos.setParameter("PresupuestoFk", entity.getPesPk());
                    deleteEgresos.executeUpdate();

                    Query deleteIngresos = em.createNativeQuery("DELETE FROM finanzas.sg_presupuesto_escolar_edicion_movimiento WHERE  mov_presupuesto_fk = :PresupuestoFk");
                    deleteIngresos.setParameter("PresupuestoFk", entity.getPesPk());
                    deleteIngresos.executeUpdate();

                    Query UpdateEstado = em.createNativeQuery("UPDATE finanzas.sg_presupuesto_escolar SET pes_estado='APROBADO' WHERE pes_pk = :PresupuestoFk");
                    UpdateEstado.setParameter("PresupuestoFk", entity.getPesPk());
                    UpdateEstado.executeUpdate();
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
     * Devuelve SgEstudiante en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return T
     */
    public SgPresupuestoEscolar obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgPresupuestoEscolar> respuesta = reader.createQuery().forEntitiesAtRevision(SgPresupuestoEscolar.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgPresupuestoEscolar ret = respuesta.get(0);
                InitializeObjectUtils.initializeHistoricRevisionRecursive(ret, new HashSet<>());
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Devuelve las áreas principales (no tienen padres)
     *
     * @param filtro
     * @return un conjunto de áreas de inversión
     * @throws GeneralException
     */
    public List<SgSede> buscarFinanzas(Long filtro) throws GeneralException {
        try {
            PresupuestoEscolarDAO codDAO = new PresupuestoEscolarDAO(em,seguridadBean);
            return codDAO.buscarFinanzas(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
