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
import sv.gob.mined.siges.filtros.FiltroFactura;
import sv.gob.mined.siges.negocio.validaciones.FacturaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.FacturaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgFactura;

/**
 * Servicio que gestiona facturas
 * @author Sofis Solutions
 */
@Stateless
public class FacturaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private ArchivoBean archivoBean;
    
    @Inject
    private SeguridadBean seguridadBean;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgFactura
     * @throws GeneralException
     */
    public SgFactura obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgFactura> codDAO = new CodigueraDAO<>(em, SgFactura.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_FACTURAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgFactura
     * @return SgFactura
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgFactura guardar(SgFactura entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (FacturaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgFactura> codDAO = new CodigueraDAO<>(em, SgFactura.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getFacPk() == null ? ConstantesOperaciones.CREAR_FACTURAS : ConstantesOperaciones.ACTUALIZAR_FACTURAS);
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
     * @param filtro FiltroFactura
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroFactura filtro) throws GeneralException {
        try {
            FacturaDAO codDAO = new FacturaDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroFactura
     * @return Lista de SgFactura
     * @throws GeneralException
     */
    public List<SgFactura> obtenerPorFiltro(FiltroFactura filtro) throws GeneralException {
        try {
            FacturaDAO codDAO = new FacturaDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,filtro.getSecurityOperation());
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
                CodigueraDAO<SgFactura> codDAO = new CodigueraDAO<>(em, SgFactura.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_FACTURAS);
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
                CodigueraDAO<SgFactura> codDAO = new CodigueraDAO<>(em, SgFactura.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_FACTURAS);
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
     * Anula el objeto indicado
     *
     * @param entity SgFactura
     * @return SgFactura
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgFactura anular(SgFactura entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (FacturaValidacionNegocio.anular(entity)) {
                    CodigueraDAO<SgFactura> codDAO = new CodigueraDAO<>(em, SgFactura.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getFacPk() == null ? ConstantesOperaciones.CREAR_FACTURAS : ConstantesOperaciones.ACTUALIZAR_FACTURAS);
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
     * Paga el objeto indicado
     *
     * @param entity SgFactura
     * @return SgFactura
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgFactura pagar(SgFactura entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (FacturaValidacionNegocio.pagar(entity)) {
                    CodigueraDAO<SgFactura> codDAO = new CodigueraDAO<>(em, SgFactura.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getFacPk() == null ? ConstantesOperaciones.CREAR_FACTURAS : ConstantesOperaciones.ACTUALIZAR_FACTURAS);
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
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgFactura
     * @throws GeneralException
     */
    public SgFactura obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgFactura> codDAO = new CodigueraDAO<>(em, SgFactura.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

}
