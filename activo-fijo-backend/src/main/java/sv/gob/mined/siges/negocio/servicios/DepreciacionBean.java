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
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.negocio.validaciones.DepreciacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DepreciacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacion;

@Stateless
@Traced
public class DepreciacionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfDepreciacion
     * @throws GeneralException
     * 
     */
    public SgAfDepreciacion obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfDepreciacion> codDAO = new CodigueraDAO<>(em, SgAfDepreciacion.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_DEPRECIACION_BIENES);
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
                CodigueraDAO<SgAfDepreciacion> codDAO = new CodigueraDAO<>(em, SgAfDepreciacion.class);
                if (BooleanUtils.isTrue(dataSecurity)){
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_DEPRECIACION_BIENES);
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
     * @param entity SgAfDepreciacion
     * @return SgAfDepreciacion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAfDepreciacion guardar(SgAfDepreciacion entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (DepreciacionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgAfDepreciacion> codDAO = new CodigueraDAO<>(em, SgAfDepreciacion.class);
                    if (BooleanUtils.isTrue(dataSecurity)){
                        entity = codDAO.guardar(entity, entity.getDepPk() != null? ConstantesOperaciones.ACTUALIZAR_DEPRECIACION_BIEN: ConstantesOperaciones.CREAR_DEPRECIACION_BIEN);
                    } else {
                        entity = codDAO.guardar(entity, null);
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
     * @param filtro FiltroBienesDepreciables
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            DepreciacionDAO codDAO = new DepreciacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroBienesDepreciables
     * @return Lista de <SgAfDepreciacion>
     * @throws GeneralException
     */
    public List<SgAfDepreciacion> obtenerPorFiltro(FiltroBienesDepreciables filtro) throws GeneralException {
        try {
            DepreciacionDAO codDAO = new DepreciacionDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Elimina la lista de registros de depreciacion de un bien
     *
     * @param idBien Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarPorIdBien(Long idBien) {
        try {
            FiltroBienesDepreciables filtro = new FiltroBienesDepreciables();
            filtro.setIdBien(idBien);

            List<SgAfDepreciacion> lista = obtenerPorFiltro(filtro);
            if(lista != null && !lista.isEmpty()) {
                CodigueraDAO<SgAfDepreciacion> codDAO = new CodigueraDAO<>(em, SgAfDepreciacion.class);
                for(SgAfDepreciacion dep : lista) {
                    codDAO.eliminar(dep, ConstantesOperaciones.ELIMINAR_DEPRECIACION_BIEN);
                }
            }
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
        try {
            CodigueraDAO<SgAfDepreciacion> codDAO = new CodigueraDAO<>(em, SgAfDepreciacion.class);
            codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_DEPRECIACION_BIEN);
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
    public void eliminarLista(List<Long> ids) throws GeneralException {
        try {
            em.createQuery("delete from SgAfDepreciacion d where d.depPk in :listIds")
                    .setParameter("listIds", ids)
                    .executeUpdate();
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
    public void eliminarListado(String operacion) throws GeneralException {
        try {
            em.createQuery("delete from SgAfDepreciacion d where d.depOperacion=:operacion")
                    .setParameter("operacion", operacion)
                    .executeUpdate();
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    /**
     * Actualiza los registros de depreciación que fueron actualizados previamente
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void actualizarDepreciacion(String operacion) throws GeneralException {
        try {
            em.createQuery("update SgAfDepreciacion d set d.depMontoDepreciacion=d.depUltimoMontoDepreciacion, d.depProcesado=true where d.depOperacion=:operacion and d.depProcesado=false")
                    .setParameter("operacion", operacion)
                    .executeUpdate();
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}