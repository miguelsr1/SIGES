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
import sv.gob.mined.siges.filtros.FiltroMensaje;
import sv.gob.mined.siges.negocio.validaciones.MensajeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.MensajeDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMensaje;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class MensajeBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMensaje
     * @throws GeneralException
     */
    public SgMensaje obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {       
                CodigueraDAO<SgMensaje> codDAO = new CodigueraDAO<>(em, SgMensaje.class);
                return codDAO.obtenerPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }
    
    /**
     * Devuelve los mensajes en el formato necesario para un Resource Bundle
     *
     * @return Response Object[][]
     * @throws GeneralException
     */
    public Object[][] obtenerParaResourceBundle() throws GeneralException {
        try {
            MensajeDAO menDAO = new MensajeDAO(em);
            return menDAO.obtenerParaResourceBundle();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
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
                CodigueraDAO<SgMensaje> codDAO = new CodigueraDAO<>(em, SgMensaje.class);
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
     * @param msj SgMensaje
     * @return SgMensaje
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMensaje guardar(SgMensaje msj) throws GeneralException {
        try {
            if (msj != null) {
                if (MensajeValidacionNegocio.validar(msj)) {
                    CodigueraDAO<SgMensaje> codDAO = new CodigueraDAO<>(em, SgMensaje.class);
                    boolean guardar = true;
                    if (msj.getMsjPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(msj.getClass(), msj.getMsjPk() , msj.getMsjVersion());
                        SgMensaje valorAnterior = (SgMensaje) valorAnt;
                        guardar = !MensajeValidacionNegocio.compararParaGrabar(valorAnterior, msj);
                    }
                    if (guardar) {
                        return codDAO.guardar(msj);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return msj;
    }
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMensaje filtro) throws GeneralException {
        try {
            MensajeDAO codDAO = new MensajeDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgMensaje 
     * @throws GeneralException
     */
    public List<SgMensaje> obtenerPorFiltro(FiltroMensaje filtro) throws GeneralException {
        try {
            MensajeDAO codDAO = new MensajeDAO(em);
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
                CodigueraDAO<SgMensaje> codDAO = new CodigueraDAO<>(em, SgMensaje.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
