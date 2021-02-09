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
import sv.gob.mined.siges.filtros.FiltroCuenta;
import sv.gob.mined.siges.persistencia.daos.CuentaDAO;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCuenta;

/**
 * Servicio que gestiona las cuentas de SIAP2
 * @author Sofis Solutions
 */
@Stateless
public class CuentaEsBean {

    @PersistenceContext
    private EntityManager em;
    
   

    public List<SsCuenta> buscarUnidadPresupuestaria(FiltroCuenta filtro) throws GeneralException {
        try {
            CuentaDAO codDAO = new CuentaDAO(em);
            return codDAO.buscarUnidadPresupuestaria(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SsCuenta> buscarLineaPresupuestaria(FiltroCuenta filtro) throws GeneralException {
        try {
            CuentaDAO codDAO = new CuentaDAO(em);
            return codDAO.buscarLineaPresupuestaria(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
     /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCuenta filtro) throws GeneralException {
        try {
            CuentaDAO codDAO = new CuentaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
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
    public List<SsCuenta> obtenerPorFiltro(FiltroCuenta filtro) throws GeneralException {
        try {
            CuentaDAO codDAO = new CuentaDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
}
