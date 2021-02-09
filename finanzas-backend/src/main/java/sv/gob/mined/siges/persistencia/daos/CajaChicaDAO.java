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
import sv.gob.mined.siges.filtros.FiltroCajaChica;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgCajaChica;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a las cajas chicas de los centros educativos
 *
 * @author jgiron
 */
public class CajaChicaDAO extends HibernateJpaDAOImp<SgCajaChica, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    private SeguridadBean segBean;
    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public CajaChicaDAO(EntityManager em, SeguridadBean segBean) throws Exception {
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
     * Construye un conjunto de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroCajaChica filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getBccSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bccSedeFk.sedPk", filtro.getBccSedeFk());
            criterios.add(criterio);
        }
        
        if (filtro.getSedesIds() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "bccSedeFk.sedPk", filtro.getSedesIds());
            criterios.add(criterio);
        }
        
        if (filtro.getBccPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "bccPk", filtro.getBccPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getBccNumeroCuenta())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "bccNumeroCuenta", filtro.getBccNumeroCuenta());
            criterios.add(criterio);
        }

        if (filtro.getBccHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bccHabilitado", filtro.getBccHabilitado());
            criterios.add(criterio);
        }
        
        if (filtro.getOtrosIngresos() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bccOtroIngreso", filtro.getOtrosIngresos());
            criterios.add(criterio);
        }
        
        if (filtro.getSubComponenteFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "bccSubcomponenteFk.gesId", filtro.getSubComponenteFk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getBccTitular())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "bccTitular", filtro.getBccTitular());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve un conjunto de cuentas bancarias de centro educativo que
     * satisfacen los criterios de un filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgCajaChica> obtenerPorFiltro(FiltroCajaChica filtro,String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCajaChica.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve el total de elementos que satisfacen un determinado filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCajaChica filtro,String securityOperation) throws DAOGeneralException {
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
            return this.countByCriteria(SgCajaChica.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
