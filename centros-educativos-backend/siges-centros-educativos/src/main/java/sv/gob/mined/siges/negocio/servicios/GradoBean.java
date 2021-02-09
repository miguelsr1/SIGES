/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroGrado;
import sv.gob.mined.siges.negocio.validaciones.GradoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.GradoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;

@Stateless
@Traced
public class GradoBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgGrado
     * @throws GeneralException
     */
    public SgGrado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgGrado> codDAO = new CodigueraDAO<>(em, SgGrado.class);
                SgGrado ret = codDAO.obtenerPorId(id, null);
                if (ret.getGraDefinicionTitulo() != null){
                    ret.getGraDefinicionTitulo().size();
                }
                return ret;
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
                CodigueraDAO<SgGrado> codDAO = new CodigueraDAO<>(em, SgGrado.class);
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
     * @param entity SgGrado
     * @return SgGrado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgGrado guardar(SgGrado entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (GradoValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgGrado> codDAO = new CodigueraDAO<>(em, SgGrado.class);
                    return codDAO.guardar(entity, null);
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
    public Long obtenerTotalPorFiltro(FiltroGrado filtro) throws GeneralException {
        try {
            GradoDAO codDAO = new GradoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgGrado
     * @throws GeneralException
     */
    public List<SgGrado> obtenerPorFiltro(FiltroGrado filtro) throws GeneralException {
        try {
            GradoDAO codDAO = new GradoDAO(em);
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
                CodigueraDAO<SgGrado> codDAO = new CodigueraDAO<>(em, SgGrado.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    

}
