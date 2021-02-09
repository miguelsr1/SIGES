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
import sv.gob.mined.siges.negocio.validaciones.CirculoInfanciaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCirculoInfancia;

@Stateless
@Traced
public class CirculoInfanciaBean {

    @PersistenceContext
    private EntityManager em;


    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SigesSedes
     * @throws GeneralException
     */
    public SgCirculoInfancia obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCirculoInfancia> codDAO = new CodigueraDAO<>(em, SgCirculoInfancia.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SEDES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCirculoInfancia> dao = new CodigueraDAO<>(em, SgCirculoInfancia.class);
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
     * @param entity
     * @return SigesSedes
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgCirculoInfancia guardar(SgCirculoInfancia entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (CirculoInfanciaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgCirculoInfancia> dao = new CodigueraDAO<>(em, SgCirculoInfancia.class);
                    return dao.guardar(entity, entity.getSedPk() == null ? ConstantesOperaciones.CREAR_SEDE_CIRCULO_INFANCIA : ConstantesOperaciones.ACTUALIZAR_SEDE_CIRCULO_INFANCIA);
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
     * @param id
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCirculoInfancia> dao = new CodigueraDAO<>(em, SgCirculoInfancia.class);
                dao.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SEDE_CIRCULO_INFANCIA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
