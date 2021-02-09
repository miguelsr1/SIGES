/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCajaChica;
import sv.gob.mined.siges.negocio.validaciones.CajaChicaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.CajaChicaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCajaChica;

/**
 * Servicio que gestiona las cuentas bancarias del centro educativo
 *
 * @author Sofis Solutions
 */
@Stateless
public class CajaChicaBean {

    private static final Logger LOGGER = Logger.getLogger(CuentasBancariasBean.class.getName());
    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;
    
    @Inject
    private SeguridadBean seguridadBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgCajaChica
     * @throws GeneralException
     */
    public SgCajaChica obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCajaChica> codDAO = new CodigueraDAO<>(em, SgCajaChica.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CUENTASBANCARIASCC);
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
                CodigueraDAO<SgCajaChica> codDAO = new CodigueraDAO<>(em, SgCajaChica.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CUENTASBANCARIASCC);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param cbc SgCajaChica
     * @return SgCajaChica
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCajaChica guardar(SgCajaChica cbc) throws GeneralException {
        try {
            if (cbc != null) {
                if (CajaChicaValidacionNegocio.validar(cbc)) {
                    CodigueraDAO<SgCajaChica> codDAO = new CodigueraDAO<>(em, SgCajaChica.class);
                    return codDAO.guardar(cbc, ConstantesOperaciones.CREAR_CUENTASBANCARIASCC);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return cbc;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCajaChica filtro) throws GeneralException {
        try {
            CajaChicaDAO codDAO = new CajaChicaDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCuentasBancariasCC>
     * @throws GeneralException
     */
    public List<SgCajaChica> obtenerPorFiltro(FiltroCajaChica filtro) throws GeneralException {
        try {
            CajaChicaDAO codDAO = new CajaChicaDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,filtro.getSecurityOperation());
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
                CodigueraDAO<SgCajaChica> codDAO = new CodigueraDAO<>(em, SgCajaChica.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CUENTASBANCARIASCC);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
