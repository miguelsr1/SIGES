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
import sv.gob.mined.siges.filtros.FiltroAccion;
import sv.gob.mined.siges.negocio.validaciones.AccionSolicitudTrasladoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.AccionDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAccionSolicitudTraslado;

@Stateless
public class AccionSolicitudTrasladoBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAccionSolicitudTraslado
     * @throws GeneralException
     */
    public SgAccionSolicitudTraslado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgAccionSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgAccionSolicitudTraslado.class);
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
                CodigueraDAO<SgAccionSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgAccionSolicitudTraslado.class);
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
     * @param ayu SgAccionSolicitudTraslado
     * @return SgAccionSolicitudTraslado
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgAccionSolicitudTraslado guardar(SgAccionSolicitudTraslado ayu) throws GeneralException {
        try {
            if (ayu != null) {
                if (AccionSolicitudTrasladoValidacionNegocio.validar(ayu)) {
                    CodigueraDAO<SgAccionSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgAccionSolicitudTraslado.class);
                    boolean guardar = true;
                    if (ayu.getAccPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(ayu.getClass(), ayu.getAccPk() , ayu.getAccVersion());
                        SgAccionSolicitudTraslado valorAnterior = (SgAccionSolicitudTraslado) valorAnt;
                        guardar = !AccionSolicitudTrasladoValidacionNegocio.compararParaGrabar(valorAnterior, ayu);
                    }
                    if (guardar) {
                        return codDAO.guardar(ayu);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ayu;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroAccion
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroAccion filtro) throws GeneralException {
        try {
            AccionDAO codDAO = new AccionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroAccion
     * @return Lista de SgAccionSolicitudTraslado 
     * @throws GeneralException
     */
    public List<SgAccionSolicitudTraslado> obtenerPorFiltro(FiltroAccion filtro) throws GeneralException {
        try {
            AccionDAO codDAO = new AccionDAO(em);
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
                CodigueraDAO<SgAccionSolicitudTraslado> codDAO = new CodigueraDAO<>(em, SgAccionSolicitudTraslado.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
