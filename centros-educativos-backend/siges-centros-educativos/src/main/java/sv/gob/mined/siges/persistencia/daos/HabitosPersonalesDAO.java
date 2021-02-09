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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroHabitosPersonales;
import sv.gob.mined.siges.persistencia.entidades.SgHabitosPersonales;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class HabitosPersonalesDAO extends HibernateJpaDAOImp<SgHabitosPersonales, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public HabitosPersonalesDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroHabitosPersonales filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAnioLectivoPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hapAnioLectivoFk.alePk", filtro.getAnioLectivoPk());
            criterios.add(criterio);
        }
      
        if (filtro.getFichaEstudianteFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hapEstudianteFk.estPk", filtro.getFichaEstudianteFk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgHabitosPersonales> obtenerPorFiltro(FiltroHabitosPersonales filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgHabitosPersonales.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroHabitosPersonales filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgHabitosPersonales.class);
    }

    public Long cantidadTotal(FiltroHabitosPersonales filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgHabitosPersonales.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
