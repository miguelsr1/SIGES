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
import sv.gob.mined.siges.filtros.FiltroCategoriaBienes;
import sv.gob.mined.siges.negocio.validaciones.CategoriaBienesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CategoriaBienesDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAfCategoriaBienes;


@Stateless
public class CategoriaBienesBean {
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfCategoriaBienes
     * @throws GeneralException
     */
    public SgAfCategoriaBienes obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfCategoriaBienes> codDAO = new CodigueraDAO<>(em, SgAfCategoriaBienes.class);
                SgAfCategoriaBienes categoria = codDAO.obtenerPorId(id);
                if(categoria != null && categoria.getSgAfSeccionesCategoriaList() != null) {
                    categoria.getSgAfSeccionesCategoriaList().size();
                }  
                return categoria;
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
                CodigueraDAO<SgAfCategoriaBienes> codDAO = new CodigueraDAO<>(em, SgAfCategoriaBienes.class);
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
     * @param etn SgAfCategoriaBienes
     * @return SgAfCategoriaBienes
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAfCategoriaBienes guardar(SgAfCategoriaBienes etn) throws GeneralException {
        try {
            if (etn != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (CategoriaBienesValidacionNegocio.validar(etn)) {
                    CodigueraDAO<SgAfCategoriaBienes> codDAO = new CodigueraDAO<>(em, SgAfCategoriaBienes.class);
                    return codDAO.guardar(etn);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return etn;
    }

    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCategoriaBienes
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCategoriaBienes filtro) throws GeneralException {
        try {
            CategoriaBienesDAO codDAO = new CategoriaBienesDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCategoriaBienes
     * @return Lista de SgEtnia 
     * @throws GeneralException
     */
    public List<SgAfCategoriaBienes> obtenerPorFiltro(FiltroCategoriaBienes filtro) throws GeneralException {
        try {
            CategoriaBienesDAO codDAO = new CategoriaBienesDAO(em);
            List<SgAfCategoriaBienes> lista = codDAO.obtenerPorFiltro(filtro);
            if(filtro.getInicializarLista() != null && filtro.getInicializarLista()) {
                for (SgAfCategoriaBienes e : lista) {
                    e.getSgAfSeccionesCategoriaList().size();
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
                CodigueraDAO<SgAfCategoriaBienes> codDAO = new CodigueraDAO<>(em, SgAfCategoriaBienes.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}

