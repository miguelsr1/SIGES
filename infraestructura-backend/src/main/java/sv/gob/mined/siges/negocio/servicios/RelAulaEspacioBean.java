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
import sv.gob.mined.siges.filtros.FiltroRelAulaEspacio;
import sv.gob.mined.siges.negocio.validaciones.RelAulaEspacioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelAulaEspacioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelAulaEspacio;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelAulaEspacioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelAulaEspacio
     * @throws GeneralException
     */
    public SgRelAulaEspacio obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelAulaEspacio> codDAO = new CodigueraDAO<>(em, SgRelAulaEspacio.class);
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
                CodigueraDAO<SgRelAulaEspacio> codDAO = new CodigueraDAO<>(em, SgRelAulaEspacio.class);
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
     * @param rae SgRelAulaEspacio
     * @return SgRelAulaEspacio
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelAulaEspacio guardar(SgRelAulaEspacio rae) throws GeneralException {
        try {
            if (rae != null) {
                if (RelAulaEspacioValidacionNegocio.validar(rae)) {
                    CodigueraDAO<SgRelAulaEspacio> codDAO = new CodigueraDAO<>(em, SgRelAulaEspacio.class);
                    boolean guardar = true;
                  
                        return codDAO.guardar(rae);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rae;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelAulaEspacio filtro) throws GeneralException {
        try {
            RelAulaEspacioDAO codDAO = new RelAulaEspacioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelAulaEspacio>
     * @throws GeneralException
     */
    public List<SgRelAulaEspacio> obtenerPorFiltro(FiltroRelAulaEspacio filtro) throws GeneralException {
        try {
            RelAulaEspacioDAO codDAO = new RelAulaEspacioDAO(em);
            return codDAO.obtenerPorFiltro(filtro,null);
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
                CodigueraDAO<SgRelAulaEspacio> codDAO = new CodigueraDAO<>(em, SgRelAulaEspacio.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
