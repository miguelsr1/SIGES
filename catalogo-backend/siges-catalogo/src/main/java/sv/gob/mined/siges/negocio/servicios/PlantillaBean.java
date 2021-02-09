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
import sv.gob.mined.siges.filtros.FiltroPlantilla;
import sv.gob.mined.siges.negocio.validaciones.PlantillaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PlantillaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgPlantilla;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class PlantillaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPlantilla
     * @throws GeneralException
     */
    public SgPlantilla obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPlantilla> codDAO = new CodigueraDAO<>(em, SgPlantilla.class);
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
                CodigueraDAO<SgPlantilla> codDAO = new CodigueraDAO<>(em, SgPlantilla.class);
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
     * @param pla SgPlantilla
     * @return SgPlantilla
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgPlantilla guardar(SgPlantilla pla) throws GeneralException {
        try {
            if (pla != null) {
                if (PlantillaValidacionNegocio.validar(pla)) {
                    CodigueraDAO<SgPlantilla> codDAO = new CodigueraDAO<>(em, SgPlantilla.class);
                    return codDAO.guardar(pla);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return pla;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPlantilla filtro) throws GeneralException {
        try {
            PlantillaDAO codDAO = new PlantillaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgPlantilla>
     * @throws GeneralException
     */
    public List<SgPlantilla> obtenerPorFiltro(FiltroPlantilla filtro) throws GeneralException {
        try {
            PlantillaDAO codDAO = new PlantillaDAO(em);
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
                CodigueraDAO<SgPlantilla> codDAO = new CodigueraDAO<>(em, SgPlantilla.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
