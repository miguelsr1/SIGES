/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRelCuentaTipoCuenta;
import sv.gob.mined.siges.persistencia.daos.RelCuentaTipoCuentaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgRelCuentaTipoCuenta;

/**
 * Servicio que gestiona las cuentas bancarias
 *
 * @author Sofis Solutions
 */
@Stateless
public class RelCuentaTipoCuentaBean {

    private static final Logger LOGGER = Logger.getLogger(RelCuentaTipoCuentaBean.class.getName());
    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;


   
    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroRelCuentaTipoCuenta filtro) throws GeneralException {
        try {
            RelCuentaTipoCuentaDAO codDAO = new RelCuentaTipoCuentaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgRelCuentaTipoCuenta>
     * @throws GeneralException
     */
    public List<SgRelCuentaTipoCuenta> obtenerPorFiltro(FiltroRelCuentaTipoCuenta filtro) throws GeneralException {
        try {
            RelCuentaTipoCuentaDAO codDAO = new RelCuentaTipoCuentaDAO(em);
            List<SgRelCuentaTipoCuenta> cuentas = codDAO.obtenerPorFiltro(filtro);
            return cuentas;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

   

}
