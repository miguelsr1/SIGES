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
import sv.gob.mined.siges.negocio.validaciones.ModuloFormacionDocenteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgModuloFormacionDocente;

@Stateless
public class ModuloFormacionDocenteBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgModuloFormacionDocente
     * @throws GeneralException
     */
    public SgModuloFormacionDocente obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
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
                CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
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
     * @param mfd SgModuloFormacionDocente
     * @return SgModuloFormacionDocente
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgModuloFormacionDocente guardar(SgModuloFormacionDocente mfd) throws GeneralException {
        try {
            if (mfd != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ModuloFormacionDocenteValidacionNegocio.validar(mfd)) {
                    CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
                    boolean guardar = true;
                    if (mfd.getMfdPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(mfd.getClass(), mfd.getMfdPk(), mfd.getMfdVersion());
                        SgModuloFormacionDocente valorAnterior = (SgModuloFormacionDocente) valorAnt;
                        guardar = !ModuloFormacionDocenteValidacionNegocio.compararParaGrabar(valorAnterior, mfd);
                    }
                    if (guardar) {
                        return codDAO.guardar(mfd);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return mfd;
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
            CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgModuloFormacionDocente 
     * @throws GeneralException
     */
    public List<SgModuloFormacionDocente> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
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
                CodigueraDAO<SgModuloFormacionDocente> codDAO = new CodigueraDAO<>(em, SgModuloFormacionDocente.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
