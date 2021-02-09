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
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAcuerdoSede;
import sv.gob.mined.siges.negocio.validaciones.AcuerdoSedeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.AcuerdoSedeDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSede;

@Stateless
@Traced
public class AcuerdoSedeBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @param dataSecurity
     * @return SgAcuerdoSede
     * @throws GeneralException
     *
     */
    public SgAcuerdoSede obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAcuerdoSede> codDAO = new CodigueraDAO<>(em, SgAcuerdoSede.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_ACUERDO_SEDE);
                } else {
                    return codDAO.obtenerPorId(id, null);
                }
            } catch (BusinessException be) {
                throw be;
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
     * @param dataSecurity
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAcuerdoSede> codDAO = new CodigueraDAO<>(em, SgAcuerdoSede.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_ACUERDO_SEDE);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgAcuerdoSede
     * @param dataSecurity
     * @return SgAcuerdoSede
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAcuerdoSede guardar(SgAcuerdoSede entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (AcuerdoSedeValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgAcuerdoSede> codDAO = new CodigueraDAO<>(em, SgAcuerdoSede.class);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getAsePk() == null ? ConstantesOperaciones.CREAR_ACUERDO_SEDE : ConstantesOperaciones.ACTUALIZAR_ACUERDO_SEDE);
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
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroAcuerdoSede filtro) throws GeneralException {
        try {
            AcuerdoSedeDAO DAO = new AcuerdoSedeDAO(em, seguridadBean);
            return DAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroAcuerdoSede
     * @return Lista de SgAcuerdoSede
     * @throws GeneralException
     */
    public List<SgAcuerdoSede> obtenerPorFiltro(FiltroAcuerdoSede filtro) throws GeneralException {
        try {
            AcuerdoSedeDAO DAO = new AcuerdoSedeDAO(em, seguridadBean);
            return DAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());

        } catch (BusinessException be) {
            throw be;
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
                CodigueraDAO<SgAcuerdoSede> codDAO = new CodigueraDAO<>(em, SgAcuerdoSede.class);
                codDAO.eliminarPorId(id, null);
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
