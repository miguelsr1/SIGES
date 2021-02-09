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
import sv.gob.mined.siges.filtros.FiltroTipoServicioSanitario;
import sv.gob.mined.siges.negocio.validaciones.TipoServicioSanitarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.TipoServicioSanitarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgTipoServicioSanitario;

@Stateless
public class TipoServicioSanitarioBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgTipoServicioSanitario
     * @throws GeneralException
     */
    public SgTipoServicioSanitario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgTipoServicioSanitario> codDAO = new CodigueraDAO<>(em, SgTipoServicioSanitario.class);
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
                CodigueraDAO<SgTipoServicioSanitario> codDAO = new CodigueraDAO<>(em, SgTipoServicioSanitario.class);
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
     * @param tno SgTipoServicioSanitario
     * @return SgTipoServicioSanitario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgTipoServicioSanitario guardar(SgTipoServicioSanitario tno) throws GeneralException {
        try {
            if (tno != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (TipoServicioSanitarioValidacionNegocio.validar(tno)) {
                    CodigueraDAO<SgTipoServicioSanitario> codDAO = new CodigueraDAO<>(em, SgTipoServicioSanitario.class);
                 
                        return codDAO.guardar(tno);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return tno;
    }

    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTipoServicioSanitario filtro) throws GeneralException {
        try {
            TipoServicioSanitarioDAO codDAO = new TipoServicioSanitarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgTipoServicioSanitario 
     * @throws GeneralException
     */
    public List<SgTipoServicioSanitario> obtenerPorFiltro(FiltroTipoServicioSanitario filtro) throws GeneralException {
        try {
            TipoServicioSanitarioDAO codDAO = new TipoServicioSanitarioDAO(em);
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
                CodigueraDAO<SgTipoServicioSanitario> codDAO = new CodigueraDAO<>(em, SgTipoServicioSanitario.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
