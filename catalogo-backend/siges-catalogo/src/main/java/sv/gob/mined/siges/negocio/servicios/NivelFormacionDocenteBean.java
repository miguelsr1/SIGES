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
import sv.gob.mined.siges.negocio.validaciones.NivelFormacionDocenteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgNivelFormacionDocente;

@Stateless
public class NivelFormacionDocenteBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgNivelFormacionDocente
     * @throws GeneralException
     */
    public SgNivelFormacionDocente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgNivelFormacionDocente> codDAO = new CodigueraDAO<>(em, SgNivelFormacionDocente.class);
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
                CodigueraDAO<SgNivelFormacionDocente> codDAO = new CodigueraDAO<>(em, SgNivelFormacionDocente.class);
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
     * @param nfd SgNivelFormacionDocente
     * @return SgNivelFormacionDocente
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgNivelFormacionDocente guardar(SgNivelFormacionDocente nfd) throws GeneralException {
        try {
            if (nfd != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (NivelFormacionDocenteValidacionNegocio.validar(nfd)) {
                    CodigueraDAO<SgNivelFormacionDocente> codDAO = new CodigueraDAO<>(em, SgNivelFormacionDocente.class);
                    boolean guardar = true;
                    if (nfd.getNfdPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(nfd.getClass(), nfd.getNfdPk(), nfd.getNfdVersion());
                        SgNivelFormacionDocente valorAnterior = (SgNivelFormacionDocente) valorAnt;
                        guardar = !NivelFormacionDocenteValidacionNegocio.compararParaGrabar(valorAnterior, nfd);
                    }
                    if (guardar) {
                        return codDAO.guardar(nfd);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return nfd;
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
            CodigueraDAO<SgNivelFormacionDocente> codDAO = new CodigueraDAO<>(em, SgNivelFormacionDocente.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgNivelFormacionDocente 
     * @throws GeneralException
     */
    public List<SgNivelFormacionDocente> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgNivelFormacionDocente> codDAO = new CodigueraDAO<>(em, SgNivelFormacionDocente.class);
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
                CodigueraDAO<SgNivelFormacionDocente> codDAO = new CodigueraDAO<>(em, SgNivelFormacionDocente.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
