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
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroProyectoInstitucional;
import sv.gob.mined.siges.negocio.validaciones.ProyectoInstitucionalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ProyectoInstitucionalDAO;
import sv.gob.mined.siges.persistencia.entidades.SgProyectoInstitucional;
import sv.gob.mined.siges.persistencia.utilidades.LoadLazyCollectionsViewInterceptor;

@Stateless
public class ProyectoInstitucionalBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgProyectoInstitucional
     * @throws GeneralException
     */   
    @Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgProyectoInstitucional obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgProyectoInstitucional> codDAO = new CodigueraDAO<>(em, SgProyectoInstitucional.class);
                SgProyectoInstitucional proyInst=codDAO.obtenerPorId(id);
                proyInst.getPinAsociaciones().size();
                return proyInst;
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
                CodigueraDAO<SgProyectoInstitucional> codDAO = new CodigueraDAO<>(em, SgProyectoInstitucional.class);
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
     * @param pin SgProyectoInstitucional
     * @return SgProyectoInstitucional
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgProyectoInstitucional guardar(SgProyectoInstitucional pin) throws GeneralException {
        try {
            if (pin != null) {
                if (ProyectoInstitucionalValidacionNegocio.validar(pin)) {
                    CodigueraDAO<SgProyectoInstitucional> codDAO = new CodigueraDAO<>(em, SgProyectoInstitucional.class);
                    return codDAO.guardar(pin);                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pin;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroProyectoInstitucional filtro) throws GeneralException {
        try {
            ProyectoInstitucionalDAO proDAO = new ProyectoInstitucionalDAO(em);
            return proDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgProyectoInstitucional 
     * @throws GeneralException
     */
    public List<SgProyectoInstitucional> obtenerPorFiltro(FiltroProyectoInstitucional filtro) throws GeneralException {
        try {
            ProyectoInstitucionalDAO proDAO = new ProyectoInstitucionalDAO(em);
            List<SgProyectoInstitucional> lista = proDAO.obtenerPorFiltro(filtro);
            if (BooleanUtils.isTrue(filtro.getInicializarBeneficios())){
                for (SgProyectoInstitucional m : lista){
                    m.getPinBeneficios().size();
                }
            }
            if (BooleanUtils.isTrue(filtro.getInicializarAsociaciones())){
                for (SgProyectoInstitucional m : lista){
                    m.getPinAsociaciones().size();
                }
            }
            return lista;
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
                CodigueraDAO<SgProyectoInstitucional> codDAO = new CodigueraDAO<>(em, SgProyectoInstitucional.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
