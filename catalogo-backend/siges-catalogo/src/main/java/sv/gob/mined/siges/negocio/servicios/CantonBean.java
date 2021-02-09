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
import sv.gob.mined.siges.filtros.FiltroCanton;
import sv.gob.mined.siges.negocio.validaciones.CantonValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CantonDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCanton;

@Stateless
public class CantonBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCanton
     * @throws GeneralException
     */
    public SgCanton obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgCanton> codDAO = new CodigueraDAO<>(em, SgCanton.class);
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
                CodigueraDAO<SgCanton> codDAO = new CodigueraDAO<>(em, SgCanton.class);
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
     * @param can SgCanton
     * @return SgCanton
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCanton guardar(SgCanton can) throws GeneralException {
        try {
            if (can != null) {
                if (CantonValidacionNegocio.validar(can)) {
                    CodigueraDAO<SgCanton> codDAO = new CodigueraDAO<>(em, SgCanton.class);
                    boolean guardar = true;
                    if (can.getCanPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(can.getClass(), can.getCanPk() , can.getCanVersion());
                        SgCanton valorAnterior = (SgCanton) valorAnt;
                        guardar = !CantonValidacionNegocio.compararParaGrabar(valorAnterior, can);
                    }
                    if (guardar) {
                        return codDAO.guardar(can);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return can;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCanton
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCanton filtro) throws GeneralException {
        try {
            CantonDAO codDAO = new CantonDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgCanton 
     * @throws GeneralException
     */
    public List<SgCanton> obtenerPorFiltro(FiltroCanton filtro) throws GeneralException {
        try {
            CantonDAO codDAO = new CantonDAO(em);
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
                CodigueraDAO<SgCanton> codDAO = new CodigueraDAO<>(em, SgCanton.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}