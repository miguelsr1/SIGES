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
import sv.gob.mined.siges.filtros.FiltroSelloFirmaTitular;
import sv.gob.mined.siges.negocio.validaciones.SelloFirmaTitularValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SelloFirmaTitularDAO;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirmaTitular;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class SelloFirmaTitularBean {

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
     * @return SgSelloFirmaTitular
     * @throws GeneralException
     */
    public SgSelloFirmaTitular obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSelloFirmaTitular> codDAO = new CodigueraDAO<>(em, SgSelloFirmaTitular.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SELLO_FIRMA_TITULAR);
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSelloFirmaTitular> codDAO = new CodigueraDAO<>(em, SgSelloFirmaTitular.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SELLO_FIRMA_TITULAR);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param sft SgSelloFirmaTitular
     * @return SgSelloFirmaTitular
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSelloFirmaTitular guardar(SgSelloFirmaTitular sft) throws GeneralException {
        try {
            if (sft != null) {
                if (SelloFirmaTitularValidacionNegocio.validar(sft)) {
                    CodigueraDAO<SgSelloFirmaTitular> codDAO = new CodigueraDAO<>(em, SgSelloFirmaTitular.class);
                    boolean guardar = true;
                    
                    return codDAO.guardar(sft, sft.getSftPk()== null ? ConstantesOperaciones.CREAR_SELLO_FIRMA_TITULAR : ConstantesOperaciones.ACTUALIZAR_SELLO_FIRMA_TITULAR);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sft;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroSelloFirmaTitular filtro) throws GeneralException {
        try {
            SelloFirmaTitularDAO codDAO = new SelloFirmaTitularDAO(em, seguridadBean);
            return codDAO.cantidadTotal(filtro, ConstantesOperaciones.BUSCAR_SELLO_FIRMA_TITULAR);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgSelloFirmaTitular>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgSelloFirmaTitular> obtenerPorFiltro(FiltroSelloFirmaTitular filtro) throws GeneralException {
        try {
            SelloFirmaTitularDAO codDAO = new SelloFirmaTitularDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SELLO_FIRMA_TITULAR);
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
                CodigueraDAO<SgSelloFirmaTitular> codDAO = new CodigueraDAO<>(em, SgSelloFirmaTitular.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SELLO_FIRMA_TITULAR);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
