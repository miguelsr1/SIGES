/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroTipoBienes;
import sv.gob.mined.siges.negocio.validaciones.TipoBienesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TipoBienesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfTipoBienes;

@Stateless
public class TipoBienesBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstadosActivos
     * @throws GeneralException
     */
    public SgAfTipoBienes obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgAfTipoBienes> codDAO = new CodigueraDAO<>(em, SgAfTipoBienes.class);
                SgAfTipoBienes tbien = codDAO.obtenerPorId(id);
                if(tbien != null && tbien.getTbiCategoriaBienes() != null && tbien.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList() != null) {
                    tbien.getTbiCategoriaBienes().getSgAfSeccionesCategoriaList().size();
                }
                return tbien;
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
                CodigueraDAO<SgAfTipoBienes> codDAO = new CodigueraDAO<>(em, SgAfTipoBienes.class);
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
     * @param esc SgAfTipoBienes
     * @return SgAfTipoBienes
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAfTipoBienes guardar(SgAfTipoBienes eact) throws GeneralException {
        try {
            if (eact != null) {
                if (TipoBienesValidacionNegocio.validar(eact)) {
                    CodigueraDAO<SgAfTipoBienes> codDAO = new CodigueraDAO<>(em, SgAfTipoBienes.class);
                    return codDAO.guardar(eact);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eact;
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
                CodigueraDAO<SgAfTipoBienes> codDAO = new CodigueraDAO<>(em, SgAfTipoBienes.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    
    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroTipoBienes
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTipoBienes filtro) throws GeneralException {
        try {
            TipoBienesDAO codDAO = new TipoBienesDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroTipoBienes
     * @return Lista de SgAfTipoBienes 
     * @throws GeneralException
     */
    public List<SgAfTipoBienes> obtenerPorFiltro(FiltroTipoBienes filtro) throws GeneralException {
        try {
            TipoBienesDAO codDAO = new TipoBienesDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
