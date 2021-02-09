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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroProveedor;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsProveedor;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;


/**
 * DAO correspondiente a proveedores
 *
 * @author jgiron
 */
public class ProveedorDAO extends HibernateJpaDAOImp<SsProveedor, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    
    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public ProveedorDAO(EntityManager em) throws Exception {
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
     * Genera una lista de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroProveedor filtro) {
        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getProId() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "proId", filtro.getProId());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getProNit())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "proNit", filtro.getProNit());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getProNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "proNombre", filtro.getProNombre());
            criterios.add(criterio);
        }

        if (filtro.getProveedorMined() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "proveedorMined", filtro.getProveedorMined());
            criterios.add(criterio);
        }

        if (filtro.getProNitNombre() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "proNit", filtro.getProNitNombre().trim());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "proNombre", filtro.getProNitNombre());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
        }

        return criterios;
    }
    
    /**
     * Devuelve todos los presupuestos escolares que satisfacen un determinado
     * filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SsProveedor> obtenerPorFiltro(FiltroProveedor filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SsProveedor.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    /**
     * Devuelve la cantidad de elementos que satisfacen un determinado filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroProveedor filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SsProveedor.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
