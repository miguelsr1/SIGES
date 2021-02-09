/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroProyeccionPoblacion;
import sv.gob.mined.siges.persistencia.daos.ProyeccionPoblacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstProyeccionPoblacion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ProyeccionesPoblacionBean {

 
    @PersistenceContext(name = Constantes.MAIN_DATASOURCE)
    private EntityManager em;


    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroProyeccionPoblacion
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroProyeccionPoblacion filtro) throws GeneralException {
        try {
            ProyeccionPoblacionDAO codDAO = new ProyeccionPoblacionDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroProyeccionPoblacion
     * @return Lista de <SgProyeccionPoblacion>
     * @throws GeneralException
     */
    public List<SgEstProyeccionPoblacion> obtenerPorFiltro(FiltroProyeccionPoblacion filtro) throws GeneralException {
        try {
            ProyeccionPoblacionDAO codDAO = new ProyeccionPoblacionDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

  

}
