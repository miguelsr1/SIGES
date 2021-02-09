/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.security.OperationSecurity;
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
import sv.gob.mined.siges.enumerados.EnumModoPago;
import sv.gob.mined.siges.filtros.FiltroPago;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgPago;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a los movimientos de cuenta bancaria (genèrico)
 *
 * @author jgiron
 */
public class PagoDAO extends HibernateJpaDAOImp<SgPago, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    private SeguridadBean segBean;

    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public PagoDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.em = em;
        this.segBean = segBean;
    }

    /**
     * Genera los criteria a partir de los filtros
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroPago filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getPgsPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsPk", filtro.getPgsPk());
            criterios.add(criterio);
        }

        if (filtro.getPgsFactura() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsFactura.facPk", filtro.getPgsFactura());
            criterios.add(criterio);
        }

        if (filtro.getSedesIds() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "pgsFactura.facSedeFk.sedPk", filtro.getSedesIds());
            criterios.add(criterio);
        }

        if (filtro.getPgsNumeroCheque() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsNumeroCheque", filtro.getPgsNumeroCheque());
            criterios.add(criterio);
        }

        if (filtro.getPgsFechaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "pgsFechaPago", filtro.getPgsFechaDesde());
            criterios.add(criterio);
        }

        if (filtro.getPgsFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESS, "pgsFechaPago", filtro.getPgsFechaHasta());
            criterios.add(criterio);
        }

        if (filtro.getOrigen() != null) {
            if (filtro.getOrigen().equals(1)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsModoPago", EnumModoPago.CHEQUE);
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsModoPago", EnumModoPago.EFECTIVO);
                criterios.add(criterio);
            }
        }

        if (filtro.getComponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsFactura.facItemMovimiento.movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeId",
                    filtro.getComponente());
            criterios.add(criterio);
        }

        if (filtro.getSubcomponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsFactura.facItemMovimiento.movTechoPresupuestal.subComponente.gesId",
                    filtro.getSubcomponente());
            criterios.add(criterio);
        }
        if (filtro.getUnidadPresupuestaria() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsFactura.facItemMovimiento.movTechoPresupuestal.subCuenta.cuCuentaPadre.cuId",
                    filtro.getUnidadPresupuestaria());
            criterios.add(criterio);
        }

        if (filtro.getLineaPresupuestaria() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsFactura.facItemMovimiento.movTechoPresupuestal.subCuenta.cuId",
                    filtro.getLineaPresupuestaria());
            criterios.add(criterio);
        }

        if (filtro.getMovimientoOrigenFac() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsFactura.facItemMovimiento.movFuenteIngresos.movOrigen", filtro.getMovimientoOrigenFac());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsFactura.facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movOrigen", filtro.getMovimientoOrigenFac());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
        }

        if (filtro.getPgsChequeraFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pgsChequeraFk.chePk", filtro.getPgsChequeraFk());
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
    public List<SgPago> obtenerPorFiltro(FiltroPago filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgPago.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
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
    public Long obtenerTotalPorFiltro(FiltroPago filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgPago.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
