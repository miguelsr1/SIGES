/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.negocio.validaciones.AutorizacionEdicionPresupuestoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.AutorizacionEdicionPresupuestoDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.persistencia.entidades.SgPresupuestoEscolar;

/**
 * Servicio que gestiona la autorización de edición del presupuesto
 * @author Sofis Solutions
 */
@Stateless
public class AutorizacionEdicionPresupuestoBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroAutorizacionEdicionPresupuesto
     * @return Lista de <SgAutorizacionEdicionPresupuesto>
     * @throws GeneralException
     */
    public List<SgAutorizacionEdicionPresupuesto> obtenerPorFiltro(FiltroAutorizacionEdicionPresupuesto filtro) throws GeneralException {
        try {
            AutorizacionEdicionPresupuestoDAO codDAO = new AutorizacionEdicionPresupuestoDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAutorizacionEdicionPresupuestos
     * @throws GeneralException
     */
    public SgAutorizacionEdicionPresupuesto obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAutorizacionEdicionPresupuesto> codDAO = new CodigueraDAO<>(em, SgAutorizacionEdicionPresupuesto.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAutorizacionEdicionPresupuesto
     * @return SgAutorizacionEdicionPresupuesto
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAutorizacionEdicionPresupuesto guardar(SgAutorizacionEdicionPresupuesto entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (AutorizacionEdicionPresupuestoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgAutorizacionEdicionPresupuesto> codDAO = new CodigueraDAO<>(em, SgAutorizacionEdicionPresupuesto.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getAutPk() == null ? ConstantesOperaciones.CREAR_AUTORIZACION_EDICION_PRESUPUESTO : ConstantesOperaciones.ACTUALIZAR_AUTORIZACION_EDICION_PRESUPUESTO);
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
     * @param filtro FiltroAutorizacionEdicionPresupuesto
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAutorizacionEdicionPresupuesto filtro) throws GeneralException {
        try {
            AutorizacionEdicionPresupuestoDAO codDAO = new AutorizacionEdicionPresupuestoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
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
                CodigueraDAO<SgAutorizacionEdicionPresupuesto> codDAO = new CodigueraDAO<>(em, SgAutorizacionEdicionPresupuesto.class);
                codDAO.eliminarPorId(id, null);
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
                CodigueraDAO<SgAutorizacionEdicionPresupuesto> codDAO = new CodigueraDAO<>(em, SgAutorizacionEdicionPresupuesto.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_AUTORIZACION_EDICION_PRESUPUESTO);
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
     * @param entity SgAutorizacionEdicionPresupuesto
     * @return SgAutorizacionEdicionPresupuesto
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPresupuestoEscolar guardarAutorizado(SgPresupuestoEscolar entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                CodigueraDAO<SgPresupuestoEscolar> codDAO = new CodigueraDAO<>(em, SgPresupuestoEscolar.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.guardar(entity, entity.getPesPk() == null ? ConstantesOperaciones.CREAR_PRESUPUESTOS_ESCOLARES : ConstantesOperaciones.ACTUALIZAR_PRESUPUESTOS_ESCOLARES);
                } else {
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
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoPresPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPresupuestoEscolar> codDAO = new CodigueraDAO<>(em, SgPresupuestoEscolar.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_MOVIMIENTO_PRESUPUESTO);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarUsuarios(Long id) throws GeneralException {
        if (id != null) {
            FiltroAutorizacionEdicionPresupuesto user = new FiltroAutorizacionEdicionPresupuesto();
            user.setAutPresupuestoFk(id);
            user.setOrderBy(new String[]{"autPk"});
            user.setIncluirCampos(new String[]{
                "autPk",
                "autPresupuestoFk.pesPk",
                "autPresupuestoFk.pesVersion",
                "autUsuarioValidadoFk.usuPk",
                "autUsuarioValidadoFk.usuVersion",
                "autVersion"
            });
            user.setAscending(new boolean[]{false});
            List<SgAutorizacionEdicionPresupuesto> usuarios = obtenerPorFiltro(user);

            if (id != null && !usuarios.isEmpty()) {
                for (SgAutorizacionEdicionPresupuesto deleteUser : usuarios) {
                    eliminar(deleteUser.getAutPk());
                }
            }
            try {
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
