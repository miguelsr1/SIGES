/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.UnidadesActivoFijoDAO;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesActivoFijo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class UnidadesActivoFijoBean {
    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgAfUnidadesActivoFijo
     * @throws GeneralException
     */
    public SgAfUnidadesActivoFijo obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgAfUnidadesActivoFijo> codDAO = new CodigueraDAO<>(em, SgAfUnidadesActivoFijo.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_UNIDADES_ACTIVO_FIJO);
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
                CodigueraDAO<SgAfUnidadesActivoFijo> codDAO = new CodigueraDAO<>(em, SgAfUnidadesActivoFijo.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_UNIDADES_ACTIVO_FIJO);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroUnidadesActivoFijo
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroUnidadesActivoFijo filtro) throws GeneralException {
        try {
            UnidadesActivoFijoDAO codDAO = new UnidadesActivoFijoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_UNIDADES_ACTIVO_FIJO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroUnidadesActivoFijo
     * @return Lista de <SgAfUnidadesActivoFijo>
     * @throws GeneralException
     */
    public List<SgAfUnidadesActivoFijo> obtenerPorFiltro(FiltroUnidadesActivoFijo filtro) throws GeneralException {
        try {
            UnidadesActivoFijoDAO codDAO = new UnidadesActivoFijoDAO(em);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_UNIDADES_ACTIVO_FIJO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
  

}
