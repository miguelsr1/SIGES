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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.negocio.validaciones.MotivosSeleccionPLazaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgMotivosSeleccionPLaza;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class MotivosSeleccionPLazaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgMotivosSeleccionPLaza
     * @throws GeneralException
     */
    public SgMotivosSeleccionPLaza obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgMotivosSeleccionPLaza> codDAO = new CodigueraDAO<>(em, SgMotivosSeleccionPLaza.class);
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
                CodigueraDAO<SgMotivosSeleccionPLaza> codDAO = new CodigueraDAO<>(em, SgMotivosSeleccionPLaza.class);
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
     * @param msp SgMotivosSeleccionPLaza
     * @return SgMotivosSeleccionPLaza
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgMotivosSeleccionPLaza guardar(SgMotivosSeleccionPLaza msp) throws GeneralException {
        try {
            if (msp != null) {
                if (MotivosSeleccionPLazaValidacionNegocio.validar(msp)) {
                    CodigueraDAO<SgMotivosSeleccionPLaza> codDAO = new CodigueraDAO<>(em, SgMotivosSeleccionPLaza.class);
                    return codDAO.guardar(msp);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return msp;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgMotivosSeleccionPLaza> codDAO = new CodigueraDAO<>(em, SgMotivosSeleccionPLaza.class);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgMotivosSeleccionPLaza>
     * @throws GeneralException
     */
    public List<SgMotivosSeleccionPLaza> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SgMotivosSeleccionPLaza> codDAO = new CodigueraDAO<>(em, SgMotivosSeleccionPLaza.class);
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
                CodigueraDAO<SgMotivosSeleccionPLaza> codDAO = new CodigueraDAO<>(em, SgMotivosSeleccionPLaza.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
