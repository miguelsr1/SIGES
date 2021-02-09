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
import sv.gob.mined.siges.filtros.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;

public class EscolaridadEstudianteDAO extends HibernateJpaDAOImp<SgEscolaridadEstudiante, Integer> implements Serializable {


    public EscolaridadEstudianteDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroEscolaridadEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getEstudiantesPk() != null && !filtro.getEstudiantesPk().isEmpty()) {
            List<CriteriaTO> estPkCriteria = new ArrayList();
            for (Long pk : filtro.getEstudiantesPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "escEstudiante.estPk", pk);
                estPkCriteria.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = estPkCriteria.toArray(new CriteriaTO[estPkCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if (filtro.getServicioEducativoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "escServicioEducativo.sduPk", filtro.getServicioEducativoPk());
            criterios.add(criterio);
        }

        if (filtro.getEstudiantePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "escEstudiante.estPk", filtro.getEstudiantePk());
            criterios.add(criterio);
        }

        if (filtro.getAnioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "escAnio.alePk", filtro.getAnioPk());
            criterios.add(criterio);
        }
        if (filtro.getMatriculaPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "escMatriculaFk.matPk", filtro.getMatriculaPk());
            criterios.add(criterio);
        }
        
        if (filtro.getEscGeneradaPorEquivalencia() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "escGeneradaPorEquivalencia", filtro.getEscGeneradaPorEquivalencia());
            criterios.add(criterio);
        }
        
        if (filtro.getResultado() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "escResultado", filtro.getResultado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgEscolaridadEstudiante> obtenerPorFiltro(FiltroEscolaridadEstudiante filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgEscolaridadEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEscolaridadEstudiante filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEscolaridadEstudiante.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
