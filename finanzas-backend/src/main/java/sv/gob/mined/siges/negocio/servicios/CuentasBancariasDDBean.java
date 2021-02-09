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
import sv.gob.mined.siges.filtros.FiltroCuentasBancariasDD;
import sv.gob.mined.siges.negocio.validaciones.CuentasBancariasDDValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.CuentasBancariasDDDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancariasDD;

/**
 * Servicio que gestiona las cuentas bancarias de las Direcciones
 * Departamentales
 *
 * @author Sofis Solutions
 */
@Stateless
public class CuentasBancariasDDBean {

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
     * @return SgCuentasBancariasDD
     * @throws GeneralException
     */
    public SgCuentasBancariasDD obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCuentasBancariasDD> codDAO = new CodigueraDAO<>(em, SgCuentasBancariasDD.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CUENTAS_BANCARIAS);
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
                CodigueraDAO<SgCuentasBancariasDD> codDAO = new CodigueraDAO<>(em, SgCuentasBancariasDD.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CUENTAS_BANCARIAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param cbc SgCuentasBancariasDD
     * @return SgCuentasBancariasDD
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCuentasBancariasDD guardar(SgCuentasBancariasDD cbc) throws GeneralException {
        try {
            if (cbc != null) {
                if (CuentasBancariasDDValidacionNegocio.validar(cbc)) {
                    CodigueraDAO<SgCuentasBancariasDD> codDAO = new CodigueraDAO<>(em, SgCuentasBancariasDD.class);
                    return codDAO.guardar(cbc, ConstantesOperaciones.CREAR_CUENTAS_BANCARIAS);
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
    public Long obtenerTotalPorFiltro(FiltroCuentasBancariasDD filtro) throws GeneralException {
        try {
            CuentasBancariasDDDAO codDAO = new CuentasBancariasDDDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCuentasBancariasDD>
     * @throws GeneralException
     */
    public List<SgCuentasBancariasDD> obtenerPorFiltro(FiltroCuentasBancariasDD filtro) throws GeneralException {
        try {
            CuentasBancariasDDDAO codDAO = new CuentasBancariasDDDAO(em,seguridadBean);
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
                CodigueraDAO<SgCuentasBancariasDD> codDAO = new CodigueraDAO<>(em, SgCuentasBancariasDD.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CUENTAS_BANCARIAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
