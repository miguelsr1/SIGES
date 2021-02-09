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
import sv.gob.mined.siges.filtros.FiltroCursoDocente;
import sv.gob.mined.siges.persistencia.entidades.SgCursoDocente;

public class CursoDocenteDAO extends HibernateJpaDAOImp<SgCursoDocente, Integer> implements Serializable {


    public CursoDocenteDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroCursoDocente filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCdsCategoria() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdsCategoria.ccuPk", filtro.getCdsCategoria());
            criterios.add(criterio);
        }

        if (filtro.getCdsDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdsSede.sedDireccion.dirDepartamento.depPk", filtro.getCdsDepartamento());
            criterios.add(criterio);
        }

        if (filtro.getCdsEspecialidad() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdsEspecialidad.espPk", filtro.getCdsEspecialidad());
            criterios.add(criterio);
        }
        if (filtro.getCdsEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdsEstado", filtro.getCdsEstado());
            criterios.add(criterio);
        }

        if (filtro.getCdsFechaInicio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "cdsFechaInicio", filtro.getCdsFechaInicio());
            criterios.add(criterio);
        }
        if (filtro.getCdsFechaFin() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "cdsFechaFin", filtro.getCdsFechaFin());
            criterios.add(criterio);
        }

        if (filtro.getCdsModulo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdsModulo.mfdPk", filtro.getCdsModulo());
            criterios.add(criterio);
        }

        if (filtro.getCdsNivel() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdsNivel.nivPk", filtro.getCdsNivel());
            criterios.add(criterio);
        }

        if (filtro.getCdsNombreCurso() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "cdsNombre", filtro.getCdsNombreCurso());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgCursoDocente> obtenerPorFiltro(FiltroCursoDocente filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

//            List<OperationSecurity> ops = null;
//            if (securityOperation != null) {
//                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
//            }
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgCursoDocente.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCursoDocente filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

//            List<OperationSecurity> ops = null;
//            if (securityOperation != null) {
//                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
//            }
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCursoDocente.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
