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
import sv.gob.mined.siges.filtros.FiltroServicioBasico;
import sv.gob.mined.siges.negocio.validaciones.ServicioBasicoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ServicioBasicoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgServicioBasico;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ServicioBasicoBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgServicioBasico
     * @throws GeneralException
     */
    public SgServicioBasico obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgServicioBasico> codDAO = new CodigueraDAO<>(em, SgServicioBasico.class);
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
                CodigueraDAO<SgServicioBasico> codDAO = new CodigueraDAO<>(em, SgServicioBasico.class);
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
     * @param sba SgServicioBasico
     * @return SgServicioBasico
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgServicioBasico guardar(SgServicioBasico sba) throws GeneralException {
        try {
            if (sba != null) {
                if (ServicioBasicoValidacionNegocio.validar(sba)) {
                    CodigueraDAO<SgServicioBasico> codDAO = new CodigueraDAO<>(em, SgServicioBasico.class);
         
                 
                        return codDAO.guardar(sba);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sba;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroServicioBasico filtro) throws GeneralException {
        try {
            ServicioBasicoDAO codDAO = new ServicioBasicoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgServicioBasico>
     * @throws GeneralException
     */
    public List<SgServicioBasico> obtenerPorFiltro(FiltroServicioBasico filtro) throws GeneralException {
        try {
            ServicioBasicoDAO codDAO = new ServicioBasicoDAO(em);
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
                CodigueraDAO<SgServicioBasico> codDAO = new CodigueraDAO<>(em, SgServicioBasico.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
