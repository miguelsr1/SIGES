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
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroSolvencias;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SolvenciasDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfSolvencias;

@Stateless
@Traced
public class SolvenciasBean {

    @PersistenceContext
    private EntityManager em;

    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfSolvencias
     * @throws GeneralException
     * 
     */
    public SgAfSolvencias obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfSolvencias> codDAO = new CodigueraDAO<>(em, SgAfSolvencias.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.ELIMINAR_SOLVENCIA_UNIDAD);
                } else {
                    return codDAO.obtenerPorId(id, null);
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
                CodigueraDAO<SgAfSolvencias> codDAO = new CodigueraDAO<>(em, SgAfSolvencias.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SOLVENCIAS_UNIDADES);
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
     * @param entity SgAfSolvencias
     * @return SgAfSolvencias
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfSolvencias guardar(SgAfSolvencias entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                CodigueraDAO<SgAfSolvencias> codDAO = new CodigueraDAO<>(em, SgAfSolvencias.class);
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (BooleanUtils.isTrue(dataSecurity)){
                    entity = codDAO.guardar(entity, entity.getSolPk()== null ? ConstantesOperaciones.CREAR_SOLVENCIA_UNIDAD : ConstantesOperaciones.ACTUALIZAR_SOLVENCIA_UNIDAD);
                } else {
                    entity = codDAO.guardar(entity, null);
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
     * @param filtro FiltroSolvencias
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroSolvencias filtro) throws GeneralException {
        try {
            SolvenciasDAO codDAO = new SolvenciasDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroSolvencias
     * @return Lista de <SgAfSolvencias>
     * @throws GeneralException
     */
    public List<SgAfSolvencias> obtenerPorFiltro(FiltroSolvencias filtro) throws GeneralException {
        try {
            SolvenciasDAO codDAO = new SolvenciasDAO(em);
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
                CodigueraDAO<SgAfSolvencias> codDAO = new CodigueraDAO<>(em, SgAfSolvencias.class);
                codDAO.eliminarPorId(id, null);
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
