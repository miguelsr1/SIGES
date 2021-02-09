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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroFactura;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgFactura;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a la factura
 *
 * @author jgiron
 */
public class FacturaDAO extends HibernateJpaDAOImp<SgFactura, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    private SeguridadBean segBean;

    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public FacturaDAO(EntityManager em, SeguridadBean segBean) throws Exception {
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
     * Genera una lista de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroFactura filtro) {
        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getFacPk() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "facPk", filtro.getFacPk());
            criterios.add(criterio);
        }

        if (filtro.getFacEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                    "facEstado", filtro.getFacEstado());
            criterios.add(criterio);
        }

        if (filtro.getComponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeId",
                    filtro.getComponente());
            criterios.add(criterio);
        }

        if (filtro.getSubcomponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesId", filtro.getSubcomponente());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesId", filtro.getSubcomponente());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
        }
        if (filtro.getUnidadPresupuestaria() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuCuentaPadre.cuId",
                    filtro.getUnidadPresupuestaria());
            criterios.add(criterio);
        }

        if (filtro.getLineaPresupuestaria() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuId",
                    filtro.getLineaPresupuestaria());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getFacNumero())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "facNumero", filtro.getFacNumero());
            criterios.add(criterio);
        }

        if (filtro.getFacFechaFacturaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "facFechaFactura", filtro.getFacFechaFacturaDesde());
            criterios.add(criterio);
        }
        if (filtro.getFacFechaFacturaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "facFechaFactura", filtro.getFacFechaFacturaHasta());
            criterios.add(criterio);
        }
        if (filtro.getFacAnioFiscalId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemPlanCompra.comPresupuestoFk.pesAnioFiscalFk.aniPk", filtro.getFacAnioFiscalId());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemMovimiento.movPresupuestoFk.pesAnioFiscalFk.aniPk", filtro.getFacAnioFiscalId());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
        }

        if (filtro.getFacProveedorId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facProveedorFk.proId", filtro.getFacProveedorId());
            criterios.add(criterio);
        }
        if (filtro.getFacTipoDocumento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facTipoDocumento", filtro.getFacTipoDocumento());
            criterios.add(criterio);
        }
        if (filtro.getFacFechaNotaCredito() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facFechaNotaCredito", filtro.getFacFechaNotaCredito());
            criterios.add(criterio);
        }
        if (filtro.getFacNotaCredito() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facNotaCredito", filtro.getFacNotaCredito());
            criterios.add(criterio);
        }

        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facSedeFk.sedPk", filtro.getSedePk());
            criterios.add(criterio);
        }

        if (filtro.getMovimientoOrigen() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemMovimiento.movFuenteIngresos.movOrigen", filtro.getMovimientoOrigen());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movOrigen", filtro.getMovimientoOrigen());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
        }

        if (filtro.getAnioFiscal() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemMovimiento.movPresupuestoFk.pesAnioFiscalFk.aniAnio", filtro.getAnioFiscal());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "facItemPlanCompra.comPresupuestoFk.pesAnioFiscalFk.aniAnio", filtro.getAnioFiscal());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
        }

        if (filtro.getFacNumeroComplete() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "facNumero", filtro.getFacNumeroComplete().trim());
            criterios.add(criterio);
        }
        if (filtro.getSedesIds() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "facSedeFk.sedPk", filtro.getSedesIds());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve una lista de planes de compra a partir de un filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgFactura> obtenerPorFiltro(FiltroFactura filtro,String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgFactura.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de planes de compra que satisfacen un criterio dado.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroFactura filtro,String securityOperation) throws DAOGeneralException {
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
            return this.countByCriteria(SgFactura.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
