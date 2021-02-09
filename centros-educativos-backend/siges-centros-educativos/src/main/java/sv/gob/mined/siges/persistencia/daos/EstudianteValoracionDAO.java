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
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroEstudianteValoracion;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteValoracion;

public class EstudianteValoracionDAO extends HibernateJpaDAOImp<SgEstudianteValoracion, Integer> implements Serializable {

    private EntityManager em;

    public EstudianteValoracionDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroEstudianteValoracion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getEsvEstudiante() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "esvEstudiante.estPk", filtro.getEsvEstudiante());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getEsvUltModUsuario())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "esvUltModUsuario", filtro.getEsvUltModUsuario());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgEstudianteValoracion> obtenerPorFiltro(FiltroEstudianteValoracion filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            if (criterios.size() > 1) {
                CriteriaTO criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
                return this.findEntityByCriteria(SgEstudianteValoracion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null,filtro.getIncluirCampos());
            } else if (criterios.size() == 1) {
                CriteriaTO criterio = criterios.get(0);
                return this.findEntityByCriteria(SgEstudianteValoracion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null,filtro.getIncluirCampos());
            } else {
                return this.findEntityByCriteria(SgEstudianteValoracion.class, null, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,null, filtro.getIncluirCampos());
            }
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEstudianteValoracion filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEstudianteValoracion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
