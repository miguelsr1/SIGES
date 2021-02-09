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
import sv.gob.mined.siges.filtros.FiltroAnioFiscal;
import sv.gob.mined.siges.persistencia.entidades.SgAnioFiscal;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a la entidad año fiscal.
 *
 * @author jgiron
 */
public class AnioFiscalDAO extends HibernateJpaDAOImp<SgAnioFiscal, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    /**
     * Constructor
     *
     * @param em
     * @throws Exception
     */
    public AnioFiscalDAO(EntityManager em) throws Exception {
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
     * Genera un elemento de tipo Criteria a partir del filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroAnioFiscal filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAniPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aniPk", filtro.getAniPk());
            criterios.add(criterio);
        }

        if (filtro.getAniAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aniAnio", filtro.getAniAnio());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve todos los años que satisfacen el filtro
     *
     * @param filtro
     * @return lista de años fiscales que satisfacen el filtro
     * @throws DAOGeneralException
     */
    public List<SgAnioFiscal> obtenerPorFiltro(FiltroAnioFiscal filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAnioFiscal.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un filtro
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long cantidadTotal(FiltroAnioFiscal filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgAnioFiscal.class);
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un filtro de una clase.
     *
     * @param filtro
     * @param clase
     * @return
     * @throws DAOGeneralException
     */
    public Long cantidadTotal(FiltroAnioFiscal filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAnioFiscal.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
