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
import sv.gob.mined.siges.negocio.validaciones.NivelIdiomaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgNivelIdioma;

@Stateless
public class NivelIdiomaBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgNivelIdioma
     * @throws GeneralException
     */
    public SgNivelIdioma obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgNivelIdioma> codDAO = new CodigueraDAO<>(em, SgNivelIdioma.class);
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
                CodigueraDAO<SgNivelIdioma> codDAO = new CodigueraDAO<>(em, SgNivelIdioma.class);
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
     * @param nid SgNivelIdioma
     * @return SgNivelIdioma
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgNivelIdioma guardar(SgNivelIdioma nid) throws GeneralException {
        try {
            if (nid != null) {
                if (NivelIdiomaValidacionNegocio.validar(nid)) {
                    CodigueraDAO<SgNivelIdioma> codDAO = new CodigueraDAO<>(em, SgNivelIdioma.class);
                    boolean guardar = true;
                    if (nid.getNidPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(nid.getClass(), nid.getNidPk() , nid.getNidVersion());
                        SgNivelIdioma valorAnterior = (SgNivelIdioma) valorAnt;
                        guardar = !NivelIdiomaValidacionNegocio.compararParaGrabar(valorAnterior, nid);
                    }
                    if (guardar) {
                        return codDAO.guardar(nid);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return nid;
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
            CodigueraDAO<SgNivelIdioma> codDAO = new CodigueraDAO<>(em, SgNivelIdioma.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgNivelIdioma 
     * @throws GeneralException
     */
    public List<SgNivelIdioma> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgNivelIdioma> codDAO = new CodigueraDAO<>(em, SgNivelIdioma.class);
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
                CodigueraDAO<SgNivelIdioma> codDAO = new CodigueraDAO<>(em, SgNivelIdioma.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
