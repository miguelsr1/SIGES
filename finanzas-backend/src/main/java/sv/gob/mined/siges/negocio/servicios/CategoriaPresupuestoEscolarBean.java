/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.persistencia.daos.CategoriaPresupuestoEscolarDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCategoriaPresupuestoEscolar;

/**
 * Servicio que gestiona los componentes (antes denominados categorías del
 * presupuesto escolar)
 *
 * @author Sofis Solutions
 */
@Stateless
public class CategoriaPresupuestoEscolarBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SsCategoriaPresupuestoEscolar>
     * @throws GeneralException
     */
    public List<SsCategoriaPresupuestoEscolar> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SsCategoriaPresupuestoEscolar> codDAO = new CodigueraDAO<>(this.em, SsCategoriaPresupuestoEscolar.class);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCategoriaPresupuestoEscolar
     * @return Lista de <SsCategoriaPresupuestoEscolar>
     * @throws GeneralException
     */
    public List<SsCategoriaPresupuestoEscolar> obtenerComponentePorFiltro(FiltroCategoriaPresupuestoEscolar filtro) throws GeneralException {
        try {
            CategoriaPresupuestoEscolarDAO codDAO = new CategoriaPresupuestoEscolarDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
