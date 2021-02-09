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
import sv.gob.mined.siges.filtros.FiltroRelEdificioEspacio;
import sv.gob.mined.siges.negocio.validaciones.RelEdificioEspacioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.RelEdificioEspacioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioEspacio;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelEdificioEspacioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelEdificioEspacio
     * @throws GeneralException
     */
    public SgRelEdificioEspacio obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelEdificioEspacio> codDAO = new CodigueraDAO<>(em, SgRelEdificioEspacio.class);
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
                CodigueraDAO<SgRelEdificioEspacio> codDAO = new CodigueraDAO<>(em, SgRelEdificioEspacio.class);
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
     * @param ree SgRelEdificioEspacio
     * @return SgRelEdificioEspacio
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelEdificioEspacio guardar(SgRelEdificioEspacio ree) throws GeneralException {
        try {
            if (ree != null) {
                if (RelEdificioEspacioValidacionNegocio.validar(ree)) {
                    CodigueraDAO<SgRelEdificioEspacio> codDAO = new CodigueraDAO<>(em, SgRelEdificioEspacio.class);
                    boolean guardar = true;
                 
                        return codDAO.guardar(ree);
                
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ree;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelEdificioEspacio filtro) throws GeneralException {
        try {
            RelEdificioEspacioDAO codDAO = new RelEdificioEspacioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelEdificioEspacio>
     * @throws GeneralException
     */
    public List<SgRelEdificioEspacio> obtenerPorFiltro(FiltroRelEdificioEspacio filtro) throws GeneralException {
        try {
            RelEdificioEspacioDAO codDAO = new RelEdificioEspacioDAO(em);
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
                CodigueraDAO<SgRelEdificioEspacio> codDAO = new CodigueraDAO<>(em, SgRelEdificioEspacio.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
