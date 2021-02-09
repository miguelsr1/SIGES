/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroArchivoBitMatricula;
import sv.gob.mined.siges.negocio.validaciones.ArchivoBitMatriculaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.ArchivoBitMatriculaDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoBitMatricula;

@Stateless
@Traced
public class ArchivoBitMatriculaBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgArchivoBitMatricula
     * @throws GeneralException
     */
    public SgArchivoBitMatricula obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgArchivoBitMatricula> codDAO = new CodigueraDAO<>(em, SgArchivoBitMatricula.class);
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
                CodigueraDAO<SgArchivoBitMatricula> codDAO = new CodigueraDAO<>(em, SgArchivoBitMatricula.class);
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
     * @param entity SgArchivoBitMatricula
     * @return SgArchivoBitMatricula
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgArchivoBitMatricula guardar(SgArchivoBitMatricula entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ArchivoBitMatriculaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgArchivoBitMatricula> codDAO = new CodigueraDAO(em, SgArchivoBitMatricula.class);
                    SgArchivoBitMatricula arch = codDAO.guardar(entity, null);
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroArchivoBitMatricula filtro) throws GeneralException {
        try {
            ArchivoBitMatriculaDAO codDAO = new ArchivoBitMatriculaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalendario>
     * @throws GeneralException
     */
    public List<SgArchivoBitMatricula> obtenerPorFiltro(FiltroArchivoBitMatricula filtro) throws GeneralException {
        try {
            ArchivoBitMatriculaDAO codDAO = new ArchivoBitMatriculaDAO(em);
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
                SgArchivoBitMatricula entity = em.find(SgArchivoBitMatricula.class, id);
                CodigueraDAO<SgArchivoBitMatricula> codDAO = new CodigueraDAO<>(em, SgArchivoBitMatricula.class);
                codDAO.eliminar(entity, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
