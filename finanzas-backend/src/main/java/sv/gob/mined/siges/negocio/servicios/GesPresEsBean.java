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
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroGesPresEs;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.GesPresEsDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsGesPresEs;

/**
 * Servicio que gestiona el presupuesto escolar desde el lado de MINED Central
 *
 * @author Sofis Solutions
 */
@Stateless
public class GesPresEsBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SsGesPresEs>
     * @throws GeneralException
     */
    public List<SsGesPresEs> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SsGesPresEs> codDAO = new CodigueraDAO<>(this.em, SsGesPresEs.class);
            return codDAO.obtenerPorFiltro(filtro, null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroGesPresEs
     * @return Lista de <SsGesPresEs>
     * @throws GeneralException
     */
    public List<SsGesPresEs> buscarSubcomponente(FiltroGesPresEs filtro) throws GeneralException {
        try {
            GesPresEsDAO codDAO = new GesPresEsDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
