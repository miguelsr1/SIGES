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
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.filtros.FiltroIndicadorMaterializado;
import sv.gob.mined.siges.persistencia.entidades.SgIndicadorMaterializado;

/**
 *
 * @author usuario
 */
public class IndicadorMaterializadoDAO extends HibernateJpaDAOImp<SgIndicadorMaterializado, Integer> implements Serializable {

    private EntityManager em;

    public IndicadorMaterializadoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroIndicadorMaterializado filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "indAnio", filtro.getAnio());
            criterios.add(criterio);
        }
        if (filtro.getIndicadorPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "indIndicador.indPk", filtro.getIndicadorPk());
            criterios.add(criterio);
        }

        if (filtro.getDesagregacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "indDesagregacion", filtro.getDesagregacion());
            criterios.add(criterio);
        }

        if (BooleanUtils.isTrue(filtro.getSinDesagregacion())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "indDesagregacion", 1);
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgIndicadorMaterializado> obtenerPorFiltro(FiltroIndicadorMaterializado filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgIndicadorMaterializado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroIndicadorMaterializado filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgIndicadorMaterializado.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
