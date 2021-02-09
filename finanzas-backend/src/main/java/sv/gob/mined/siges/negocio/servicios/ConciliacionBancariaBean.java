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
import sv.gob.mined.siges.filtros.FiltroConciliacionBancaria;
import sv.gob.mined.siges.negocio.validaciones.ConciliacionBancariaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ConciliacionBancariaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgConciliacionesBancarias;

/**
 * Servicio que gestiona las conciliaciones bancarias
 * @author Sofis Solutions
 */
@Stateless
public class ConciliacionBancariaBean {

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
     * @return SgConciliacionesBancarias
     * @throws GeneralException
     */
    public SgConciliacionesBancarias obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgConciliacionesBancarias> codDAO = new CodigueraDAO<>(em, SgConciliacionesBancarias.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CONCILIACION_BANCARIA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgConciliacionesBancarias
     * @return SgConciliacionesBancarias
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgConciliacionesBancarias guardar(SgConciliacionesBancarias entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ConciliacionBancariaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgConciliacionesBancarias> codDAO = new CodigueraDAO<>(em, SgConciliacionesBancarias.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getConPk() == null ? ConstantesOperaciones.CREAR_CONCILIACION_BANCARIA : ConstantesOperaciones.ACTUALIZAR_CONCILIACION_BANCARIA);
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
     * @param filtro FiltroConciliacionBancaria
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroConciliacionBancaria filtro) throws GeneralException {
        try {
            ConciliacionBancariaDAO codDAO = new ConciliacionBancariaDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroConciliacionBancaria
     * @return Lista de SgConciliacionesBancarias
     * @throws GeneralException
     */
    public List<SgConciliacionesBancarias> obtenerPorFiltro(FiltroConciliacionBancaria filtro) throws GeneralException {
        try {
            ConciliacionBancariaDAO codDAO = new ConciliacionBancariaDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
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
                CodigueraDAO<SgConciliacionesBancarias> codDAO = new CodigueraDAO<>(em, SgConciliacionesBancarias.class);
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
                CodigueraDAO<SgConciliacionesBancarias> codDAO = new CodigueraDAO<>(em, SgConciliacionesBancarias.class);
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
}
