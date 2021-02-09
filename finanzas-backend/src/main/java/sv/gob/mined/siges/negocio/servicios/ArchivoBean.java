/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.negocio.validaciones.ArchivoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;

@Stateless
@Traced
/**
 * Sevicio que gestiona los archivos
 */
public class ArchivoBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgArchivo
     * @throws GeneralException
     */
    public SgArchivo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgArchivo> codDAO = new CodigueraDAO<>(em, SgArchivo.class);
                return codDAO.obtenerPorId(id, null);
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
                CodigueraDAO<SgArchivo> codDAO = new CodigueraDAO<>(em, SgArchivo.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgArchivo
     * @return SgArchivo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgArchivo guardar(SgArchivo entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ArchivoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgArchivo> codDAO = new CodigueraDAO(em, SgArchivo.class);
                    SgArchivo arch = codDAO.guardar(entity, null);
                    return arch;
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
                SgArchivo entity = em.find(SgArchivo.class, id);
                CodigueraDAO<SgArchivo> codDAO = new CodigueraDAO<>(em, SgArchivo.class);
                codDAO.eliminar(entity, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Elimina el objeto
     *
     * @param entity SgArchivo
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(SgArchivo entity) throws GeneralException {
        if (entity != null) {
            try {
                CodigueraDAO<SgArchivo> codDAO = new CodigueraDAO<>(em, SgArchivo.class);
                codDAO.eliminar(entity, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
