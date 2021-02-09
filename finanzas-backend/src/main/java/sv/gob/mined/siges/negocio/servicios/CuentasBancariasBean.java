/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCuentasBancarias;
import sv.gob.mined.siges.negocio.validaciones.CuentasBancariasValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.CuentasBancariasDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancarias;
import sv.gob.mined.siges.persistencia.entidades.SgRelCuentaTipoCuenta;
import sv.gob.mined.siges.persistencia.utilidades.InitializeObjectUtils;

/**
 * Servicio que gestiona las cuentas bancarias
 *
 * @author Sofis Solutions
 */
@Stateless
public class CuentasBancariasBean {

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
     * @return SgCuentasBancarias
     * @throws GeneralException
     */
    public SgCuentasBancarias obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCuentasBancarias> codDAO = new CodigueraDAO<>(em, SgCuentasBancarias.class);
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
                CodigueraDAO<SgCuentasBancarias> codDAO = new CodigueraDAO<>(em, SgCuentasBancarias.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CUENTAS_BANCARIAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda la cuenta bancaria y las relaciones con los tipos de cuentas seleccionados.
     * @param cbc SgCuentasBancarias
     * @return SgCuentasBancarias
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCuentasBancarias guardar(SgCuentasBancarias cbc) throws GeneralException {
        try {
            List<SgRelCuentaTipoCuenta> rels = new ArrayList<>();
            if (cbc != null) {
                if (CuentasBancariasValidacionNegocio.validar(cbc)) {
                    CodigueraDAO<SgCuentasBancarias> codDAO = new CodigueraDAO<>(em, SgCuentasBancarias.class);
                    CodigueraDAO<SgRelCuentaTipoCuenta> relDAO = new CodigueraDAO<>(em, SgRelCuentaTipoCuenta.class);
                    rels = cbc.getCbcCuentaTipoCuenta();
                    cbc.setCbcCuentaTipoCuenta(new ArrayList());
                    cbc = codDAO.guardar(cbc, ConstantesOperaciones.CREAR_CUENTAS_BANCARIAS);
                    if (rels != null && !rels.isEmpty()) {
                        for (SgRelCuentaTipoCuenta rel : rels) {
                            rel.setRelCuentaBancariaFk(cbc);
                            relDAO.guardar(rel, ConstantesOperaciones.CREAR_CUENTAS_BANCARIAS);
                        }
                    }
                    return cbc;
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
    public Long obtenerTotalPorFiltro(FiltroCuentasBancarias filtro) throws GeneralException {
        try {
            CuentasBancariasDAO codDAO = new CuentasBancariasDAO(em,seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCuentasBancarias>
     * @throws GeneralException
     */
    public List<SgCuentasBancarias> obtenerPorFiltro(FiltroCuentasBancarias filtro) throws GeneralException {
        try {
            CuentasBancariasDAO codDAO = new CuentasBancariasDAO(em,seguridadBean);
            List<SgCuentasBancarias> cuentas = codDAO.obtenerPorFiltro(filtro,filtro.getSecurityOperation());
            for (SgCuentasBancarias cuenta : cuentas) {
                if (cuenta.getCbcCuentaTipoCuenta()!=null && !cuenta.getCbcCuentaTipoCuenta().isEmpty()) {
                    cuenta.getCbcCuentaTipoCuenta().size();
                }
            }
            return cuentas;
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
                CodigueraDAO<SgCuentasBancarias> codDAO = new CodigueraDAO<>(em, SgCuentasBancarias.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CUENTAS_BANCARIAS);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve un objeto en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return SgCuentasBancarias
     */
    public SgCuentasBancarias obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgCuentasBancarias> respuesta = reader.createQuery().forEntitiesAtRevision(SgCuentasBancarias.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgCuentasBancarias ret = respuesta.get(0);
                InitializeObjectUtils.initializeHistoricRevisionRecursive(ret, new HashSet<>());

                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

}
