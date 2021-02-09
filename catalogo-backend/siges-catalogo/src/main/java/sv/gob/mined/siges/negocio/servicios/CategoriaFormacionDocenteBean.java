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
import sv.gob.mined.siges.negocio.validaciones.CategoriaFormacionDocenteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCategoriaFormacionDocente;

@Stateless
public class CategoriaFormacionDocenteBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCategoriaFormacionDocente
     * @throws GeneralException
     */
    public SgCategoriaFormacionDocente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCategoriaFormacionDocente> codDAO = new CodigueraDAO<>(em, SgCategoriaFormacionDocente.class);
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
                CodigueraDAO<SgCategoriaFormacionDocente> codDAO = new CodigueraDAO<>(em, SgCategoriaFormacionDocente.class);
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
     * @param cfd SgCategoriaFormacionDocente
     * @return SgCategoriaFormacionDocente
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCategoriaFormacionDocente guardar(SgCategoriaFormacionDocente cfd) throws GeneralException {
        try {
            if (cfd != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (CategoriaFormacionDocenteValidacionNegocio.validar(cfd)) {
                    CodigueraDAO<SgCategoriaFormacionDocente> codDAO = new CodigueraDAO<>(em, SgCategoriaFormacionDocente.class);
                    boolean guardar = true;
                    if (cfd.getCfdPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(cfd.getClass(), cfd.getCfdPk(), cfd.getCfdVersion());
                        SgCategoriaFormacionDocente valorAnterior = (SgCategoriaFormacionDocente) valorAnt;
                        guardar = !CategoriaFormacionDocenteValidacionNegocio.compararParaGrabar(valorAnterior, cfd);
                    }
                    if (guardar) {
                        return codDAO.guardar(cfd);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cfd;
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
            CodigueraDAO<SgCategoriaFormacionDocente> codDAO = new CodigueraDAO<>(em, SgCategoriaFormacionDocente.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgCategoriaFormacionDocente 
     * @throws GeneralException
     */
    public List<SgCategoriaFormacionDocente> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgCategoriaFormacionDocente> codDAO = new CodigueraDAO<>(em, SgCategoriaFormacionDocente.class);
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
                CodigueraDAO<SgCategoriaFormacionDocente> codDAO = new CodigueraDAO<>(em, SgCategoriaFormacionDocente.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
