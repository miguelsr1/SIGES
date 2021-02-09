/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.negocio.validaciones.SedeEducameValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgSedeEducame;

@Stateless
@Traced
public class SedeEducameBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSedes
     * @throws GeneralException
     */
    public SgSedeEducame obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSedeEducame> dao = new CodigueraDAO<>(em, SgSedeEducame.class);
                return dao.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SEDES);
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
                CodigueraDAO<SgSedeEducame> dao = new CodigueraDAO<>(em, SgSedeEducame.class);
                return dao.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SEDES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgSedes
     * @return SgSedes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgSedeEducame guardar(SgSedeEducame entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (SedeEducameValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgSedeEducame> dao = new CodigueraDAO<>(em, SgSedeEducame.class);
                    return dao.guardar(entity, entity.getSedPk() == null ? ConstantesOperaciones.CREAR_EDUCAME : ConstantesOperaciones.ACTUALIZAR_EDUCAME);
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
                CodigueraDAO<SgSedeEducame> dao = new CodigueraDAO<>(em, SgSedeEducame.class);
                dao.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_EDUCAME);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}