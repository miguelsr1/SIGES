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
import sv.gob.mined.siges.filtros.FiltroFactorRiesgoSede;
import sv.gob.mined.siges.negocio.validaciones.FactorRiesgoSedeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.FactorRiesgoSedeDAO;
import sv.gob.mined.siges.persistencia.entidades.SgFactorRiesgoSede;

@Stateless
@Traced
public class FactorRiesgoSedeBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgFactorRiesgoSede
     * @throws GeneralException
     */
    public SgFactorRiesgoSede obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgFactorRiesgoSede> codDAO = new CodigueraDAO<>(em, SgFactorRiesgoSede.class);
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
                CodigueraDAO<SgFactorRiesgoSede> codDAO = new CodigueraDAO<>(em, SgFactorRiesgoSede.class);
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
     * @param entity SgFactorRiesgoSede
     * @return SgFactorRiesgoSede
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgFactorRiesgoSede guardar(SgFactorRiesgoSede entity) throws GeneralException {
        try {
            if (entity != null) {
                if (FactorRiesgoSedeValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgFactorRiesgoSede> codDAO = new CodigueraDAO<>(em, SgFactorRiesgoSede.class);
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
                CodigueraDAO<SgFactorRiesgoSede> codDAO = new CodigueraDAO<>(em, SgFactorRiesgoSede.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroFactorRiesgoSede
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroFactorRiesgoSede filtro) throws GeneralException {
        try {
            FactorRiesgoSedeDAO codDAO = new FactorRiesgoSedeDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroFactorRiesgoSede
     * @return Lista de SgFactorRiesgoSede
     * @throws GeneralException
     */     
    public List<SgFactorRiesgoSede> obtenerPorFiltro(FiltroFactorRiesgoSede filtro) throws GeneralException {
        try {
            FactorRiesgoSedeDAO codDAO = new FactorRiesgoSedeDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
