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
import sv.gob.mined.siges.filtros.FiltroProveedor;
import sv.gob.mined.siges.negocio.validaciones.ProveedorValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ProveedorDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsProveedor;

/**
 * Servicio que gestina los proveedores
 *
 * @author Sofis Solutions
 */
@Stateless
public class ProveedorBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroProveedor
     * @return Lista de <SsProveedor>
     * @throws GeneralException
     */
    public List<SsProveedor> obtenerPorFiltro(FiltroProveedor filtro) throws GeneralException {
        try {
            ProveedorDAO codDAO = new ProveedorDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SsProveedors
     * @throws GeneralException
     */
    public SsProveedor obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SsProveedor> codDAO = new CodigueraDAO<>(em, SsProveedor.class);
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
     * @param entity SsProveedor
     * @return SsProveedor
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SsProveedor guardar(SsProveedor entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ProveedorValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SsProveedor> codDAO = new CodigueraDAO<>(em, SsProveedor.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getProId() == null ? ConstantesOperaciones.CREAR_PROVEEDOR : ConstantesOperaciones.ACTUALIZAR_PROVEEDOR);
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
     * @param filtro FiltroProveedor
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroProveedor filtro) throws GeneralException {
        try {
            ProveedorDAO codDAO = new ProveedorDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
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
                CodigueraDAO<SsProveedor> codDAO = new CodigueraDAO<>(em, SsProveedor.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_PROVEEDOR);
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
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SsProveedor> codDAO = new CodigueraDAO<>(em, SsProveedor.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_PROVEEDOR);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
