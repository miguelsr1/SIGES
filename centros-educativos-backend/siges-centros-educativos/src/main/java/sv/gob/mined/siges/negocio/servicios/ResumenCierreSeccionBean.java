package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroResumenCierreSeccion;
import sv.gob.mined.siges.persistencia.daos.ResumenCierreSeccionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgResumenCierreSeccion;

/**
 *
 * @author bruno
 */
@Stateless
@Traced
public class ResumenCierreSeccionBean {

    @PersistenceContext
    private EntityManager em;


    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroResumenCierreSeccion filtro) throws GeneralException {
        try {
            ResumenCierreSeccionDAO DAO = new ResumenCierreSeccionDAO(em);
            return DAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgServicioEducativo
     * @throws GeneralException
     */
    public List<SgResumenCierreSeccion>  obtenerPorFiltro(FiltroResumenCierreSeccion filtro) throws GeneralException {
        try {
            ResumenCierreSeccionDAO DAO = new ResumenCierreSeccionDAO(em);
            List<SgResumenCierreSeccion> ca = DAO.obtenerPorFiltro(filtro);
            return ca;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
   
}
