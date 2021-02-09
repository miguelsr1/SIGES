/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEscalaCalificacion;
import sv.gob.mined.siges.negocio.validaciones.EscalaCalificacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EscalaCalificacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEscalaCalificacion;
import sv.gob.mined.siges.persistencia.utilidades.LoadLazyCollectionsViewInterceptor;

@Stateless
public class EscalaCalificacionBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgEscalaCalificacion
     * @throws GeneralException
     */
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgEscalaCalificacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgEscalaCalificacion> codDAO = new CodigueraDAO<>(em, SgEscalaCalificacion.class);
                return codDAO.obtenerPorId(id);
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEscalaCalificacion> codDAO = new CodigueraDAO<>(em, SgEscalaCalificacion.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }
        

    /**
     * Guarda el objeto indicado
     *
     * @param eca
     * @return SgEscalaCalificacion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class, LoadLazyCollectionsViewInterceptor.class})
    public SgEscalaCalificacion guardar(SgEscalaCalificacion eca) throws GeneralException {
        try {
            if (eca != null) {
                if (EscalaCalificacionValidacionNegocio.validar(eca)) {
                    CodigueraDAO<SgEscalaCalificacion> codDAO = new CodigueraDAO<>(em, SgEscalaCalificacion.class);
                    boolean guardar = true;
                    if (eca.getEcaPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(eca.getClass(), eca.getEcaPk() , eca.getEcaVersion());
                        SgEscalaCalificacion valorAnterior = (SgEscalaCalificacion) valorAnt;
                        guardar = !EscalaCalificacionValidacionNegocio.compararParaGrabar(valorAnterior, eca);
                    }
                    if (guardar) {
                        return codDAO.guardar(eca);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eca;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEscalaCalificacion filtro) throws GeneralException {
        try {
            EscalaCalificacionDAO codDAO = new EscalaCalificacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgEscalaCalificacion> obtenerPorFiltro(FiltroEscalaCalificacion filtro) throws GeneralException {
        try {
            EscalaCalificacionDAO codDAO = new EscalaCalificacionDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
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
                CodigueraDAO<SgEscalaCalificacion> codDAO = new CodigueraDAO<>(em, SgEscalaCalificacion.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
