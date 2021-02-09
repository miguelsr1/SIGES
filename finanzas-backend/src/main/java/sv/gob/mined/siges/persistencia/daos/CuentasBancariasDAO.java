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
import sv.gob.mined.siges.filtros.FiltroCuentasBancarias;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancarias;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a las cuentas bancarias de una pagaduría de una dirección
 * departamental
 *
 * @author jgiron
 */
public class CuentasBancariasDAO extends HibernateJpaDAOImp<SgCuentasBancarias, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    private SeguridadBean segBean;
    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public CuentasBancariasDAO(EntityManager em,SeguridadBean segBean) throws Exception {
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
     * Genera un conjunto de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroCuentasBancarias filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCbcSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cbcSedeFk.sedPk", filtro.getCbcSedeFk());
            criterios.add(criterio);
        }
        
        if (filtro.getSedesIds() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cbcSedeFk.sedPk", filtro.getSedesIds());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCbcNumeroCuenta())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cbcNumeroCuenta", filtro.getCbcNumeroCuenta());
            criterios.add(criterio);
        }

        if (filtro.getCbcHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cbcHabilitado", filtro.getCbcHabilitado());
            criterios.add(criterio);
        }
        
        if (filtro.getCbcOtroIngreso() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cbcOtroIngreso", filtro.getCbcOtroIngreso());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCbcTitular())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "cbcTitular", filtro.getCbcTitular());
            criterios.add(criterio);
        }

        if (filtro.getCbcBancoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cbcBancoFk.bncPk", filtro.getCbcBancoFk());
            criterios.add(criterio);
        }


        if (filtro.getCbcPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cbcPk", filtro.getCbcPk());
            criterios.add(criterio);
        }

        if (filtro.getDepartamentoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cbcSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoFk());
            criterios.add(criterio);
        }
        
        if (filtro.getCbcCuentaTipoCuenta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cbcCuentaTipoCuenta.cbcTipoCuentaBacFk.tcbPk", filtro.getCbcCuentaTipoCuenta());
            criterios.add(criterio);
        }
        
        if (filtro.getSinTipoCuenta()!= null) {
            if(filtro.getSinTipoCuenta().equals(Boolean.TRUE)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NULL, "cbcCuentaTipoCuenta.cbcTipoCuentaBacFk",1);
                criterios.add(criterio);
            }
        }

        return criterios;
    }

    /**
     * Devuelve un conjunto de cuentas bancarias que satisfacen un filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgCuentasBancarias> obtenerPorFiltro(FiltroCuentasBancarias filtro,String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCuentasBancarias.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve el total de elementos que satisfacen un filtro
     *
     * @param filtro
     * @return cantidad de elementos que satisfacen el filtro
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCuentasBancarias filtro,String securityOperation) throws DAOGeneralException {
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
            return this.countByCriteria(SgCuentasBancarias.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
