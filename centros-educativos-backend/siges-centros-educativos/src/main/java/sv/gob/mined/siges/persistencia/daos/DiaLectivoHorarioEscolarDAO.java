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
import javax.persistence.EntityManager;
import sv.gob.mined.siges.filtros.FiltroDiaLectivoHorarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgDiaLectivoHorarioEscolar;

public class DiaLectivoHorarioEscolarDAO extends HibernateJpaDAOImp<SgDiaLectivoHorarioEscolar, Integer> implements Serializable {

    private EntityManager em;

    public DiaLectivoHorarioEscolarDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroDiaLectivoHorarioEscolar filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getHorarioEscolarFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "dlhHorarioEscolarFk.hesPk", filtro.getHorarioEscolarFk());
            criterios.add(criterio);
        }

        if (filtro.getSeccionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "dlhHorarioEscolarFk.hesSeccion.secPk", filtro.getSeccionFk());
            criterios.add(criterio);
        }

        if (filtro.getCeldaDiaHora() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "dlhDia", filtro.getCeldaDiaHora());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgDiaLectivoHorarioEscolar> obtenerPorFiltro(FiltroDiaLectivoHorarioEscolar filtro) throws DAOGeneralException {
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

            return this.findEntityByCriteria(SgDiaLectivoHorarioEscolar.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDiaLectivoHorarioEscolar filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgDiaLectivoHorarioEscolar.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
