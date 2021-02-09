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
import sv.gob.mined.siges.filtros.FiltroGrado;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;

public class GradoDAO extends HibernateJpaDAOImp<SgGrado, Integer> implements Serializable {

    private EntityManager em;

    public GradoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroGrado filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getGraPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graPk", filtro.getGraPk());
            criterios.add(criterio);
        }
        
        if (filtro.getModPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getModPk());
            criterios.add(criterio);
        }

        if (filtro.getModAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getModAtencionPk());
            criterios.add(criterio);
        }

        if (filtro.getSubModAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSubModAtencionPk());
            criterios.add(criterio);
        }

        if (filtro.getSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graServicioEducativo.sduSede.sedPk", filtro.getSedePk());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getAnioLectivoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graServicioEducativo.sduSeccion.secAnioLectivo.alePk", filtro.getAnioLectivoPk());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getRelModEdModAtenPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graRelacionModalidad.reaPk", filtro.getRelModEdModAtenPk());
            criterios.add(criterio);
        }
        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "graHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }
        if (filtro.getGraDefinicionTitulo() != null) {
            if (filtro.getGraDefinicionTitulo()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EMPTY, "graDefinicionTitulo", 1);
                criterios.add(criterio);
            }

        }
        if (filtro.getListRelModEdModAtenPk()!= null && !filtro.getListRelModEdModAtenPk().isEmpty()) {
            List<CriteriaTO> periodoCriteria = new ArrayList();
            for (Long etp : filtro.getListRelModEdModAtenPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "graRelacionModalidad.reaPk", etp);
                periodoCriteria.add(criterio);
            }
            if (!periodoCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = periodoCriteria.toArray(new CriteriaTO[periodoCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        return criterios;
    }

    public List<SgGrado> obtenerPorFiltro(FiltroGrado filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgGrado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroGrado filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgGrado.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
