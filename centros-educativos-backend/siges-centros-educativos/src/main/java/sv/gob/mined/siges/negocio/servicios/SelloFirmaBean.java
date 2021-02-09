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
import sv.gob.mined.siges.filtros.FiltroSelloFirma;
import sv.gob.mined.siges.negocio.validaciones.SelloFirmaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SelloFirmaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirma;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class SelloFirmaBean {

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
     * @return SgSelloFirma
     * @throws GeneralException
     */
    public SgSelloFirma obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSelloFirma> codDAO = new CodigueraDAO<>(em, SgSelloFirma.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_SELLO_FIRMA);
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
                CodigueraDAO<SgSelloFirma> codDAO = new CodigueraDAO<>(em, SgSelloFirma.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SELLO_FIRMA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param sfi SgSelloFirma
     * @return SgSelloFirma
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSelloFirma guardar(SgSelloFirma sfi) throws GeneralException {
        try {
            if (sfi != null) {
                if (SelloFirmaValidacionNegocio.validar(sfi)) {
                    CodigueraDAO<SgSelloFirma> codDAO = new CodigueraDAO<>(em, SgSelloFirma.class);
                    return codDAO.guardar(sfi, sfi.getSfiPk()== null ? ConstantesOperaciones.CREAR_SELLO_FIRMA : ConstantesOperaciones.ACTUALIZAR_SELLO_FIRMA);
                   
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sfi;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroSelloFirma filtro) throws GeneralException {
        try {
            SelloFirmaDAO codDAO = new SelloFirmaDAO(em, seguridadBean);
            return codDAO.cantidadTotal(filtro, ConstantesOperaciones.BUSCAR_SELLO_FIRMA);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgSelloFirma>
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgSelloFirma> obtenerPorFiltro(FiltroSelloFirma filtro) throws GeneralException {
        try {
            SelloFirmaDAO codDAO = new SelloFirmaDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SELLO_FIRMA);
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
                CodigueraDAO<SgSelloFirma> codDAO = new CodigueraDAO<>(em, SgSelloFirma.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SELLO_FIRMA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
