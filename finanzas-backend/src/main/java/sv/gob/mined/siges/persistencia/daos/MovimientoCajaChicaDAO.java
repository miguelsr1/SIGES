/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonNumber;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroMovimientoCajaChica;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCajaChica;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a los movimientos de caja chica
 *
 * @author jgiron
 */
public class MovimientoCajaChicaDAO extends HibernateJpaDAOImp<SgMovimientoCajaChica, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public MovimientoCajaChicaDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.em = em;
    }

    /**
     * Genera los criteria a partir de los filtros
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroMovimientoCajaChica filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getMccCuentaFK() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mccCuentaFK.bccPk", filtro.getMccCuentaFK());
            criterios.add(criterio);
        }
        
        if (filtro.getSedePk() != null) {   
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mccCuentaFK.bccSedeFk.sedPk", filtro.getSedePk());
            criterios.add(criterio);
        }
        
        if (filtro.getMccTipoMovimiento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mccTipoMovimiento", filtro.getMccTipoMovimiento());
            criterios.add(criterio);
        }

        if (filtro.getMccFechaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "mccFecha", filtro.getMccFechaDesde());
            criterios.add(criterio);
        }

        if (filtro.getMccFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "mccFecha", filtro.getMccFechaHasta());
            criterios.add(criterio);
        }

        if (filtro.getComponentePk() != null) {
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mccPagoFk.pgsFactura.facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente", filtro.getComponentePk());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mccPagoFk.pgsFactura.facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente", filtro.getComponentePk());
            criterios.add(CriteriaTOUtils.createORTO(criterio1, criterio2));
        }

        if (filtro.getSubComponentePk() != null) {
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mccPagoFk.pgsFactura.facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente", filtro.getSubComponentePk());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mccPagoFk.pgsFactura.facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente", filtro.getSubComponentePk());
            criterios.add(CriteriaTOUtils.createORTO(criterio1, criterio2));
        }

        return criterios;
    }

    /**
     * Devuelve los movimientos de caja chica a partir de un filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgMovimientoCajaChica> obtenerPorFiltro(FiltroMovimientoCajaChica filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgMovimientoCajaChica.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos a partir de un filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMovimientoCajaChica filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgMovimientoCajaChica.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
