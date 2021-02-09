/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAreaInversion;
import sv.gob.mined.siges.persistencia.daos.AreaInversionDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SgAreaInversion;

/**
 * Servicio que gestina las áreas de inversión
 *
 * @author Sofis Solutions
 */
@Stateless
public class AreaInversionBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMovimientos
     * @throws GeneralException
     *
     */
    //@Interceptors(LoadLazyCollectionsViewInterceptor.class)
    public SgAreaInversion obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAreaInversion> codDAO = new CodigueraDAO<>(em, SgAreaInversion.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_AREA_INVERSION);
                } else {
                    return codDAO.obtenerPorId(id, null);
                }
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
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAreaInversion> codDAO = new CodigueraDAO<>(em, SgAreaInversion.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_AREA_INVERSION);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Devuelve las áreas de inversión que satisfacen el filtro
     *
     * @param filtro
     * @return un conjunto de áreas de inversión
     * @throws GeneralException
     */
    public List<SgAreaInversion> obtenerPorFiltro(FiltroAreaInversion filtro) throws GeneralException {
        try {
            AreaInversionDAO codDAO = new AreaInversionDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve las áreas principales (no tienen padres)
     *
     * @param filtro
     * @return un conjunto de áreas de inversión
     * @throws GeneralException
     */
    public List<SgAreaInversion> buscarAreaPrincipal(FiltroAreaInversion filtro) throws GeneralException {
        try {
            AreaInversionDAO codDAO = new AreaInversionDAO(em);
            return codDAO.buscarAreaPrincipal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve un conjunto de áreas de inversión que satisfacen un criterio
     *
     * @param filtro
     * @param subarea
     * @return
     * @throws GeneralException
     */
    public List<SgAreaInversion> buscarSubAreaPrincipal(FiltroAreaInversion filtro, Long subarea) throws GeneralException {
        try {
            AreaInversionDAO codDAO = new AreaInversionDAO(em);
            return codDAO.buscarSubAreaPrincipal(filtro, subarea);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve las subáreas de una transferencia
     *
     * @param filtro
     * @return
     * @throws GeneralException
     */
    public List<SgAreaInversion> buscarSubAreaTransferencia(Long filtro) throws GeneralException {
        try {

            AreaInversionDAO codDAO = new AreaInversionDAO(em);
            return codDAO.buscarSubAreaTransferencia(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAreaInversions
     * @throws GeneralException
     */
    public SgAreaInversion obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAreaInversion> codDAO = new CodigueraDAO<>(em, SgAreaInversion.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve las subáreas de una transferencia
     *
     * @param filtro
     * @return
     * @throws GeneralException
     */
    public List<SgAreaInversion> buscarAreaTransferencia(Long filtro) throws GeneralException {
        try {

            AreaInversionDAO codDAO = new AreaInversionDAO(em);
            return codDAO.buscarAreaTransferencia(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
