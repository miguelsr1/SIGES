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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.DependenciaEconomicaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDependenciaEconomica;

@Stateless
public class DependenciaEconomicaBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDependenciaEconomica
     * @throws GeneralException
     */
    public SgDependenciaEconomica obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgDependenciaEconomica> codDAO = new CodigueraDAO<>(em, SgDependenciaEconomica.class);
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
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDependenciaEconomica> codDAO = new CodigueraDAO<>(em, SgDependenciaEconomica.class);
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
     * @param dec SgDependenciaEconomica
     * @return SgDependenciaEconomica
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgDependenciaEconomica guardar(SgDependenciaEconomica dec) throws GeneralException {
        try {
            if (dec != null) {
                if (DependenciaEconomicaValidacionNegocio.validar(dec)) {
                    CodigueraDAO<SgDependenciaEconomica> codDAO = new CodigueraDAO<>(em, SgDependenciaEconomica.class);
                    boolean guardar = true;
                    if (dec.getDecPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(dec.getClass(), dec.getDecPk() , dec.getDecVersion());
                        SgDependenciaEconomica valorAnterior = (SgDependenciaEconomica) valorAnt;
                        guardar = !DependenciaEconomicaValidacionNegocio.compararParaGrabar(valorAnterior, dec);
                    }
                    if (guardar) {
                        return codDAO.guardar(dec);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return dec;
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
                CodigueraDAO<SgDependenciaEconomica> codDAO = new CodigueraDAO<>(em, SgDependenciaEconomica.class);
                codDAO.eliminarPorId(id);
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
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgDependenciaEconomica> codDAO = new CodigueraDAO<>(em, SgDependenciaEconomica.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgDependenciaEconomica 
     * @throws GeneralException
     */
    public List<SgDependenciaEconomica> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgDependenciaEconomica> codDAO = new CodigueraDAO<>(em, SgDependenciaEconomica.class);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
