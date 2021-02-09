/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAsistenciaSede;
import sv.gob.mined.siges.negocio.validaciones.AsistenciaSedeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.AsistenciaSedeDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAsistenciaSede;

@Stateless
@Traced
public class AsistenciaSedeBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAsistenciaSede
     * @throws GeneralException
     */
    public SgAsistenciaSede obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgAsistenciaSede> codDAO = new CodigueraDAO<>(em, SgAsistenciaSede.class);
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
                CodigueraDAO<SgAsistenciaSede> codDAO = new CodigueraDAO<>(em, SgAsistenciaSede.class);
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
     * @param entity SgAsistenciaSede
     * @return SgAsistenciaSede
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgAsistenciaSede guardar(SgAsistenciaSede entity) throws GeneralException {
        try {
            if (entity != null) {
                if (AsistenciaSedeValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgAsistenciaSede> codDAO = new CodigueraDAO<>(em, SgAsistenciaSede.class);
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
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAsistenciaSede> codDAO = new CodigueraDAO<>(em, SgAsistenciaSede.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroAsistenciaSede
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAsistenciaSede filtro) throws GeneralException {
        try {
            AsistenciaSedeDAO codDAO = new AsistenciaSedeDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroAsistenciaSede
     * @return Lista de SgAsistenciaSede
     * @throws GeneralException
     */     
    public List<SgAsistenciaSede> obtenerPorFiltro(FiltroAsistenciaSede filtro) throws GeneralException {
        try {
            AsistenciaSedeDAO codDAO = new AsistenciaSedeDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
