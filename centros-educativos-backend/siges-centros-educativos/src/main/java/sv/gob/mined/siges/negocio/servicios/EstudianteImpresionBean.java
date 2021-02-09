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
import sv.gob.mined.siges.filtros.FiltroEstudianteImpresion;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EstudianteImpresionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EstudianteImpresionBean {
    @PersistenceContext
    private EntityManager em;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    public SgEstudianteImpresion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstudianteImpresion> codDAO = new CodigueraDAO<>(em, SgEstudianteImpresion.class);
                return codDAO.obtenerPorId(id,null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

   /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEstudianteImpresion filtro) throws GeneralException {
        try {
            EstudianteImpresionDAO codDAO = new EstudianteImpresionDAO(em);
            return codDAO.cantidadTotal(filtro,null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgEstudioRealizado>
     * @throws GeneralException
     */
    public List<SgEstudianteImpresion> obtenerPorFiltro(FiltroEstudianteImpresion filtro) throws GeneralException {
        try {
            EstudianteImpresionDAO codDAO = new EstudianteImpresionDAO(em);
            return codDAO.obtenerPorFiltro(filtro,null);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
