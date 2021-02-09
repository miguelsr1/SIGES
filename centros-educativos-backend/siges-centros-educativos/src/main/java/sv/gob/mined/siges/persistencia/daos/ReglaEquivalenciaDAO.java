/*
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
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroReglaEquivalencia;
import sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalencia;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ReglaEquivalenciaDAO extends HibernateJpaDAOImp<SgReglaEquivalencia, Integer> implements Serializable {


    public ReglaEquivalenciaDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroReglaEquivalencia filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getReglaNormativaId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "reqPk", filtro.getReglaNormativaId());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "reqNombre", filtro.getNombre());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNormativa())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "reqNormativa", SofisStringUtils.normalizarParaBusqueda(filtro.getNormativa().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getRegla())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "reqReglaEquivalencia", SofisStringUtils.normalizarParaBusqueda(filtro.getRegla().trim()));
            criterios.add(criterio);
        }
        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "reqHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }
                  
        return criterios;
    }

    public List<SgReglaEquivalencia> obtenerPorFiltro(FiltroReglaEquivalencia filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgReglaEquivalencia.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroReglaEquivalencia filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgReglaEquivalencia.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
