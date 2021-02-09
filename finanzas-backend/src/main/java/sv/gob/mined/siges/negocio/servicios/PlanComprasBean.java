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
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEncabezadoPlanCompra;
import sv.gob.mined.siges.filtros.FiltroPlanCompras;
import sv.gob.mined.siges.negocio.validaciones.PlanComprasValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EncabezadoPlanComprasDAO;
import sv.gob.mined.siges.persistencia.daos.PlanComprasDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEncabezadoPlanCompras;
import sv.gob.mined.siges.persistencia.entidades.SgPlanCompras;

/**
 * Servicio que gestiona el plan de compras
 *
 * @author Sofis Solutions
 */
@Stateless
public class PlanComprasBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private ArchivoBean archivoBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPlanCompras
     * @throws GeneralException
     */
    public SgPlanCompras obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPlanCompras> codDAO = new CodigueraDAO<>(em, SgPlanCompras.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_PLAN_DE_COMPRAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgPlanCompras
     * @return SgPlanCompras
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPlanCompras guardar(SgPlanCompras entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (PlanComprasValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgPlanCompras> codDAO = new CodigueraDAO<>(em, SgPlanCompras.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getComPk() == null ? ConstantesOperaciones.CREAR_PLAN_DE_COMPRAS : ConstantesOperaciones.ACTUALIZAR_PLAN_DE_COMPRAS);
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
     * @param filtro FiltroPlanCompras
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPlanCompras filtro) throws GeneralException {
        try {
            PlanComprasDAO codDAO = new PlanComprasDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroPlanCompras
     * @return Lista de <SgPlanCompras>
     * @throws GeneralException
     */
    public List<SgPlanCompras> obtenerPorFiltro(FiltroPlanCompras filtro) throws GeneralException {
        try {
            PlanComprasDAO codDAO = new PlanComprasDAO(em);
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
                CodigueraDAO<SgPlanCompras> codDAO = new CodigueraDAO<>(em, SgPlanCompras.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_PLAN_DE_COMPRAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
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
                CodigueraDAO<SgPlanCompras> codDAO = new CodigueraDAO<>(em, SgPlanCompras.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_PLAN_DE_COMPRAS);
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
     * @param entity SgEncabezadoPlanCompras
     * @return SgEncabezadoPlanCompras
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgEncabezadoPlanCompras guardar(SgEncabezadoPlanCompras entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (PlanComprasValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgEncabezadoPlanCompras> codDAO = new CodigueraDAO<>(em, SgEncabezadoPlanCompras.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getPlaPk() == null ? ConstantesOperaciones.CREAR_PLAN_DE_COMPRAS : ConstantesOperaciones.ACTUALIZAR_PLAN_DE_COMPRAS);
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
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoPlanExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEncabezadoPlanCompras> codDAO = new CodigueraDAO<>(em, SgEncabezadoPlanCompras.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_PLAN_DE_COMPRAS);
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
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroEncabezadoPlanCompra
     * @return Lista de <SgEncabezadoPlanCompras>
     * @throws GeneralException
     */
    public List<SgEncabezadoPlanCompras> obtenerPlanPorFiltro(FiltroEncabezadoPlanCompra filtro) throws GeneralException {
        try {
            EncabezadoPlanComprasDAO codDAO = new EncabezadoPlanComprasDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
