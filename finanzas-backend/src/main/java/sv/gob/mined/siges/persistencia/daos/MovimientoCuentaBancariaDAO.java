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
import sv.gob.mined.siges.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.filtros.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a los movimientos de cuenta bancaria (genèrico)
 *
 * @author jgiron
 */
public class MovimientoCuentaBancariaDAO extends HibernateJpaDAOImp<SgMovimientoCuentaBancaria, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public MovimientoCuentaBancariaDAO(EntityManager em) throws Exception {
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
    private List<CriteriaTO> generarCriteria(FiltroMovimientoCuentaBancaria filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getMcbCuentaFK() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbCuentaFK.cbcPk", filtro.getMcbCuentaFK());
            criterios.add(criterio);
        }

        if (filtro.getMcbFechaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "mcbFecha", filtro.getMcbFechaDesde());
            criterios.add(criterio);
        }

        if (filtro.getMcbFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "mcbFecha", filtro.getMcbFechaHasta());
            criterios.add(criterio);
        }

        if (filtro.getMcbTipoMovimiento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbTipoMovimiento", filtro.getMcbTipoMovimiento());
            criterios.add(criterio);
        }

        if (filtro.getComponentePk() != null) {
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbPagoFk.pgsFactura.facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente", filtro.getComponentePk());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbPagoFk.pgsFactura.facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente", filtro.getComponentePk());
            criterios.add(CriteriaTOUtils.createORTO(criterio1, criterio2));
        }

        if (filtro.getSubComponentePk() != null) {
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbPagoFk.pgsFactura.facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente", filtro.getSubComponentePk());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbPagoFk.pgsFactura.facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente", filtro.getSubComponentePk());
            criterios.add(CriteriaTOUtils.createORTO(criterio1, criterio2));
        }

        if (filtro.getMcbChequeCobrado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.
                    createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbChequeCobrado", Boolean.FALSE);
            criterios.add(criterio);
        }

        if (filtro.getMcbAplicaConciliacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.
                    createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbAplicaConciliacion", Boolean.TRUE);
            criterios.add(criterio);
        }

        if (filtro.getComponenteIngresoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbDesembolsoCedFk.dceReqFondoCedFk.rfcTransferenciaCedFk.tacTransferenciaFk.componente.cpeId", filtro.getComponenteIngresoPk());
            criterios.add(criterio);
        }

        if (filtro.getSubComponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbDesembolsoCedFk.dceReqFondoCedFk.rfcTransferenciaCedFk.tacTransferenciaFk.subComponente.gesId", filtro.getSubComponente());
            criterios.add(criterio);
        }

        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbDesembolsoCedFk.dceReqFondoCedFk.rfcTransferenciaCedFk.tacCedFk.sedPk", filtro.getSedePk());
            criterios.add(criterio);
        }

        if (filtro.getAnioFiscal() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbMovFuenteIngresosFk.movPresupuestoFk.pesAnioFiscalFk.aniAnio", filtro.getAnioFiscal());
            criterios.add(criterio);
        }

        if (filtro.getAnioFiscalId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbMovFuenteIngresosFk.movPresupuestoFk.pesAnioFiscalFk.aniPk", filtro.getAnioFiscal());
            criterios.add(criterio);
        }

        if (filtro.getSedePresupuesto() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbMovFuenteIngresosFk.movPresupuestoFk.pesSedeFk.sedPk", filtro.getSedePresupuesto());
            criterios.add(criterio);
        }

        if (filtro.getMcbChequeAnulado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbChequeAnulado", filtro.getMcbChequeAnulado());
            criterios.add(criterio);
        }

        if (filtro.getMovimientoOrigen() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbPagoFk.pgsFactura.facItemMovimiento.movFuenteIngresos.movOrigen", filtro.getMovimientoOrigen());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbPagoFk.pgsFactura.facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movOrigen", filtro.getMovimientoOrigen());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbMovFuenteIngresosFk.movOrigen", filtro.getMovimientoOrigen());

            if (filtro.getMovimientoOrigen().equals(EnumMovimientosOrigen.P)) {
                MatchCriteriaTO criterio3 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "mcbDesembolsoCedFk.dcePk", 1);
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1, criterio2, criterio3));
            } else {
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1, criterio2));
            }

        }
        if (filtro.getMcbTipoRetiro() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mcbTipoRetiro", filtro.getMcbTipoRetiro());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve la lista de movimientos de cuentas bancarias a partir de un
     * filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgMovimientoCuentaBancaria> obtenerPorFiltro(FiltroMovimientoCuentaBancaria filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgMovimientoCuentaBancaria.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen el filtro indicado
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroMovimientoCuentaBancaria filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgMovimientoCuentaBancaria.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
