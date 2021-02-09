/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.cache.annotation.CacheResult;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.catalogo.FiltroCalificacionCa;
import sv.gob.mined.siges.persistencia.daos.CalificacionCatalogosDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;

@Stateless
public class CalificacionCatalogosBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgCalificacion
     * @throws GeneralException
     */
    public SgCalificacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgCalificacion> codDAO = new CodigueraDAO<>(em, SgCalificacion.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }
            
 
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCalificacionCa filtro) throws GeneralException {
        try {
            CalificacionCatalogosDAO codDAO = new CalificacionCatalogosDAO(em);
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
    @CacheResult(cacheName = Constantes.CALIFICACION_CACHE)
    public List<SgCalificacion> obtenerPorFiltro(FiltroCalificacionCa filtro) throws GeneralException {
        try {
            CalificacionCatalogosDAO codDAO = new CalificacionCatalogosDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }



}
