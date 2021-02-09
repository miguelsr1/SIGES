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
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroCuenta;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCuenta;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a la entidad cuenta
 * @author jgiron
 */
public class CuentaDAO extends HibernateJpaDAOImp<SsCuenta, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    
    /**
     * Constructor
     *
     * @param em
     * @throws Exception
     */
    public CuentaDAO(EntityManager em) throws Exception {
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
     * Genera un criteria a partir del filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroCuenta filtro) {
        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getCuId() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "cuId", filtro.getCuId());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCuNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cuNombre", filtro.getCuNombre());
            criterios.add(criterio);
        }
        
        if (filtro.getCuCuentaPadre()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,"cuCuentaPadre", filtro.getCuCuentaPadre());
            criterios.add(criterio);
        }

        return criterios;
    }
    
    
    /**
     * Devuelve los elementos que satisfacen un filtro determinado.
     *
     * @param filtro
     * @return lista de las áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SsCuenta> obtenerPorFiltro(FiltroCuenta filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SsCuenta.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    /**
     * Devuelve el total de elementos que satisfacen el filtro determinado.
     *
     * @param filtro
     * @return cantidad de elementos
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCuenta filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SsCuenta.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen un filtro determinado.
     *
     * @param filtro
     * @return lista de las áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SsCuenta> buscarUnidadPresupuestaria(FiltroCuenta filtro) throws DAOGeneralException {
        try {
            Query areasInversion = em.createNativeQuery(" select * from siap2.ss_cuentas where cu_cuenta_padre is null order by cu_nombre", SsCuenta.class);
            return areasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    /**
     * Devuelve los elementos que satisfacen un filtro determinado.
     *
     * @param filtro
     * @return lista de las áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SsCuenta> buscarLineaPresupuestaria(FiltroCuenta filtro) throws DAOGeneralException {
        try {
            Query areasInversion = em.createNativeQuery(" select * from siap2.ss_cuentas where cu_cuenta_padre is not null order by cu_nombre", SsCuenta.class);
            return areasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
