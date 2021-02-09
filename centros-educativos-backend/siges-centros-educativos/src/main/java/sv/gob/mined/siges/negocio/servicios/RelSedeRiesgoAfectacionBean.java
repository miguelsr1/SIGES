/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroRelSedeRiesgoAfectacion;
import sv.gob.mined.siges.negocio.validaciones.RelSedeRiesgoAfectacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SedeRiesgoAfectacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelSedeRiesgoAfectacion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelSedeRiesgoAfectacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgRelSedeRiesgoAfectacion
     * @throws GeneralException
     */
    public SgRelSedeRiesgoAfectacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgRelSedeRiesgoAfectacion> codDAO = new CodigueraDAO<>(em, SgRelSedeRiesgoAfectacion.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.AUTENTICADO);
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
                CodigueraDAO<SgRelSedeRiesgoAfectacion> codDAO = new CodigueraDAO<>(em, SgRelSedeRiesgoAfectacion.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.AUTENTICADO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param rar SgRelSedeRiesgoAfectacion
     * @return SgRelSedeRiesgoAfectacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgRelSedeRiesgoAfectacion guardar(SgRelSedeRiesgoAfectacion rar) throws GeneralException {
        try {
            if (rar != null) {
                if (RelSedeRiesgoAfectacionValidacionNegocio.validar(rar)) {
                    CodigueraDAO<SgRelSedeRiesgoAfectacion> codDAO = new CodigueraDAO<>(em, SgRelSedeRiesgoAfectacion.class);                
                    return codDAO.guardar(rar, null);   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return rar;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgRelSedeRiesgoAfectacion> codDAO = new CodigueraDAO<>(em, SgRelSedeRiesgoAfectacion.class);
            return codDAO.obtenerTotalPorFiltro(filtro,ConstantesOperaciones.AUTENTICADO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelSedeRiesgoAfectacion>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgRelSedeRiesgoAfectacion> obtenerPorFiltro(FiltroRelSedeRiesgoAfectacion filtro) throws GeneralException {
        try {
            SedeRiesgoAfectacionDAO codDAO = new SedeRiesgoAfectacionDAO(em);
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
                CodigueraDAO<SgRelSedeRiesgoAfectacion> codDAO = new CodigueraDAO<>(em, SgRelSedeRiesgoAfectacion.class);
                codDAO.eliminarPorId(id,ConstantesOperaciones.AUTENTICADO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
