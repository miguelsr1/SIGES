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
import sv.gob.mined.siges.filtros.FiltroGesPresEs;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsGesPresEs;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a la entidad SubComponente
 *
 * @author jgiron
 */
public class GesPresEsDAO extends HibernateJpaDAOImp<SsGesPresEs, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    
    /**
     * Constuctor de la clase
     *
     * @param em
     * @throws Exception
     */
    public GesPresEsDAO(EntityManager em) throws Exception {
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
     * Genera los criteria a partir de un filtro.
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroGesPresEs filtro) {
        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getGesId() != null) {
            MatchCriteriaTO criterio
                    = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS,
                            "gesId", filtro.getGesId());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getGesNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "gesNombre", filtro.getGesNombre());
            criterios.add(criterio);
        }
        
        if (filtro.getCpeId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "gesCategoriaComponente.cpeId", filtro.getCpeId());
            criterios.add(criterio);
        }

        return criterios;
    }
    
    /**
     * Devuelve los insumos que satisfacen un filtro dado
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SsGesPresEs> obtenerPorFiltro(FiltroGesPresEs filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SsGesPresEs.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
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
    public Long obtenerTotalPorFiltro(FiltroGesPresEs filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SsGesPresEs.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
