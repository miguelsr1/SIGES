/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsFuenteFinanciamiento;

/**
 * Servicio que gestiona las transferencias
 *
 * @author Sofis Solutions
 */
@Stateless
public class FuenteFinanciamientoBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private SeguridadBean seguridadBean;
    
    @Inject
    private ConsultaHistoricoBean ch;

    
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
                CodigueraDAO<SsFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SsFuenteFinanciamiento.class);
                return codDAO.objetoExistePorId(id,ConstantesOperaciones.BUSCAR_FUENTE_FIN);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
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
            CodigueraDAO<SsFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SsFuenteFinanciamiento.class);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_FUENTE_FIN);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgTransferencia>
     * @throws GeneralException
     */
    public List<SsFuenteFinanciamiento> obtenerPorFiltro(FiltroCodiguera filtro) throws GeneralException {
        try {
            CodigueraDAO<SsFuenteFinanciamiento> codDAO = new CodigueraDAO<>(em, SsFuenteFinanciamiento.class);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_FUENTE_FIN);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
