/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroPropuestaPedagogica;
import sv.gob.mined.siges.negocio.validaciones.PropuestaPedagogicaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PropuestaPedagogicaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPropuestaPedagogica;

@Stateless
@Traced
public class PropuestaPedagogicaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPropuestaPedagogica
     * @throws GeneralException
     */
    public SgPropuestaPedagogica obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPropuestaPedagogica> codDAO = new CodigueraDAO<>(em, SgPropuestaPedagogica.class);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPropuestaPedagogica> codDAO = new CodigueraDAO<>(em, SgPropuestaPedagogica.class);
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
     * @param entity SgPropuestaPedagogica
     * @return SgPropuestaPedagogica
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPropuestaPedagogica guardar(SgPropuestaPedagogica entity) throws GeneralException {
        try {
            if (entity != null) {
                if (PropuestaPedagogicaValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgPropuestaPedagogica> codDAO = new CodigueraDAO<>(em, SgPropuestaPedagogica.class);
                    return codDAO.guardar(entity, entity.getPpePk() == null ? ConstantesOperaciones.CREAR_PROPUESTA_PEDAGOGICA : ConstantesOperaciones.ACTUALIZAR_PROPUESTA_PEDAGOGICA);
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
                CodigueraDAO<SgPropuestaPedagogica> codDAO = new CodigueraDAO<>(em, SgPropuestaPedagogica.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_PROPUESTA_PEDAGOGICA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPropuestaPedagogica filtro) throws GeneralException {
        try {
            PropuestaPedagogicaDAO DAO = new PropuestaPedagogicaDAO(em, seguridadBean);
            return DAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_PROPUESTA_PEDAGOGICA);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPropuestaPedagogica
     * @throws GeneralException
     */
    public List<SgPropuestaPedagogica> obtenerPorFiltro(FiltroPropuestaPedagogica filtro) throws GeneralException {
        try {
            PropuestaPedagogicaDAO DAO = new PropuestaPedagogicaDAO(em, seguridadBean);
            return DAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
