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
import sv.gob.mined.siges.filtros.FiltroMetodoAdq;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsMetodoAdq;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a los métodos de adquisición
 *
 * @author jgiron
 */
public class MetodoAdqDAO extends HibernateJpaDAOImp<SsMetodoAdq, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public MetodoAdqDAO(EntityManager em) throws Exception {
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
     * Genera los criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroMetodoAdq filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getMetId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "metId", filtro.getMetId());
            criterios.add(criterio);
        }
        if (filtro.getMetNombre() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.
                    createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "metNombre", filtro.getMetNombre());
            criterios.add(criterio);
        }
        if (filtro.getMetHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.
                    createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "metHabilitado", Boolean.TRUE);
            criterios.add(criterio);
        }
        return criterios;
    }

    /**
     * Devuelve los métodos de adquisición a partir de un filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SsMetodoAdq> obtenerPorFiltro(FiltroMetodoAdq filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SsMetodoAdq.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long cantidadTotal(FiltroMetodoAdq filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SsMetodoAdq.class);
    }

    /**
     * Devuelve la cantidad de elemntos que satisfacen el filtro.
     *
     * @param filtro
     * @param clase
     * @return
     * @throws DAOGeneralException
     */
    public Long cantidadTotal(FiltroMetodoAdq filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SsMetodoAdq.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
