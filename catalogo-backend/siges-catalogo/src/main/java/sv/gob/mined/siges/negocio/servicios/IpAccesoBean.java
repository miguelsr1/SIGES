/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroIpAcceso;
import sv.gob.mined.siges.negocio.validaciones.IpAccesoValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.IpAccesoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgIpsAcceso;

@Stateless
public class IpAccesoBean {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgIpsAcceso
     * @throws GeneralException
     */
    public SgIpsAcceso obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgIpsAcceso> codDAO = new CodigueraDAO<>(em, SgIpsAcceso.class);
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
                CodigueraDAO<SgIpsAcceso> codDAO = new CodigueraDAO<>(em, SgIpsAcceso.class);
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
     * @param etn SgIpsAcceso
     * @return SgIpsAcceso
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgIpsAcceso guardar(SgIpsAcceso etn) throws GeneralException {
        try {
            if (etn != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (IpAccesoValidacionNegocio.validar(etn)) {
                    CodigueraDAO<SgIpsAcceso> codDAO = new CodigueraDAO<>(em, SgIpsAcceso.class);
                    return codDAO.guardar(etn);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return etn;
    }

    
    
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroIpAcceso
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroIpAcceso filtro) throws GeneralException {
        try {
            IpAccesoDAO codDAO = new IpAccesoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroIpAcceso
     * @return Lista de SgIpsAcceso 
     * @throws GeneralException
     */
    public List<SgIpsAcceso> obtenerPorFiltro(FiltroIpAcceso filtro) throws GeneralException {
        try {
            IpAccesoDAO codDAO = new IpAccesoDAO(em);
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
                CodigueraDAO<SgIpsAcceso> codDAO = new CodigueraDAO<>(em, SgIpsAcceso.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}

