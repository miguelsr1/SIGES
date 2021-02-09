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
import sv.gob.mined.siges.filtros.FiltroHabitosPersonales;
import sv.gob.mined.siges.negocio.validaciones.HabitosPersonalesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.HabitosPersonalesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgHabitosPersonales;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class HabitosPersonalesBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgHabitosPersonales
     * @throws GeneralException
     */
    public SgHabitosPersonales obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgHabitosPersonales> codDAO = new CodigueraDAO<>(em, SgHabitosPersonales.class);
                return codDAO.obtenerPorId(id,null);
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
                CodigueraDAO<SgHabitosPersonales> codDAO = new CodigueraDAO<>(em, SgHabitosPersonales.class);
                return codDAO.objetoExistePorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Guarda el objeto indicado
     *
     * @param hap SgHabitosPersonales
     * @return SgHabitosPersonales
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabitosPersonales guardar(SgHabitosPersonales hap) throws GeneralException {
        try {
            if (hap != null) {
                if (HabitosPersonalesValidacionNegocio.validar(hap)) {
                    CodigueraDAO<SgHabitosPersonales> codDAO = new CodigueraDAO<>(em, SgHabitosPersonales.class);              
                    return codDAO.guardar(hap,null);               
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hap;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroHabitosPersonales filtro) throws GeneralException {
        try {
            HabitosPersonalesDAO codDAO = new HabitosPersonalesDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgHabitosPersonales>
     * @throws GeneralException
     */
    public List<SgHabitosPersonales> obtenerPorFiltro(FiltroHabitosPersonales filtro) throws GeneralException {
        try {
            HabitosPersonalesDAO codDAO = new HabitosPersonalesDAO(em);
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
                CodigueraDAO<SgHabitosPersonales> codDAO = new CodigueraDAO<>(em, SgHabitosPersonales.class);
                codDAO.eliminarPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
