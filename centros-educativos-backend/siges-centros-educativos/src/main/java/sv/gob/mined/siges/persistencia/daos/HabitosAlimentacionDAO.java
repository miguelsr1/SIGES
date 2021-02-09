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
import sv.gob.mined.siges.filtros.FiltroHabitosAlimentacion;
import sv.gob.mined.siges.persistencia.entidades.SgHabitosAlimentacion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class HabitosAlimentacionDAO extends HibernateJpaDAOImp<SgHabitosAlimentacion, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public HabitosAlimentacionDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroHabitosAlimentacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAnioLectivoPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "halAnioLectivoFk.alePk", filtro.getAnioLectivoPk());
            criterios.add(criterio);
        }
      
        if (filtro.getFichaEstudianteFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "halEstudianteFk.estPk", filtro.getFichaEstudianteFk());
            criterios.add(criterio);
        }
      

        return criterios;
    }

    public List<SgHabitosAlimentacion> obtenerPorFiltro(FiltroHabitosAlimentacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgHabitosAlimentacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroHabitosAlimentacion filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgHabitosAlimentacion.class);
    }

    public Long cantidadTotal(FiltroHabitosAlimentacion filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgHabitosAlimentacion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
