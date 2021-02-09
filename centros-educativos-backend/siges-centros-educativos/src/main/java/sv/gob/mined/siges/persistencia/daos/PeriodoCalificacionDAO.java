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
import sv.gob.mined.siges.filtros.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalificacion;

/**
 *
 * @author Sofis Solutions
 */
public class PeriodoCalificacionDAO extends HibernateJpaDAOImp<SgPeriodoCalificacion, Integer> implements Serializable {

    public PeriodoCalificacionDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroPeriodoCalificacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaCalendario.calPk", filtro.getCalPk());
            criterios.add(criterio);
        }
        if (filtro.getPcaModalidadEducativa() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaModalidad.modPk", filtro.getPcaModalidadEducativa());
            criterios.add(criterio);
        }
        if (filtro.getPcaModalidadAtencion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaModalidadAtencion.matPk", filtro.getPcaModalidadAtencion());
            criterios.add(criterio);
        }
        if (filtro.getPcaNumero() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaNumero", filtro.getPcaNumero());
            criterios.add(criterio);
        }

        if (filtro.getPcaNumeros() != null && !filtro.getPcaNumeros().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "pcaNumero", filtro.getPcaNumeros());
            criterios.add(criterio);
        }

        if (filtro.getPcaAnioLectivo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaCalendario.calAnioLectivo.alePk", filtro.getPcaAnioLectivo());
            criterios.add(criterio);
        }
        if (filtro.getPcaTipoCalendario() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaCalendario.calTipoCalendario.tcePk", filtro.getPcaTipoCalendario());
            criterios.add(criterio);
        }
        if (filtro.getPcaSubModalidadAtencion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaSubModalidadAtencion.smoPk", filtro.getPcaSubModalidadAtencion());
            criterios.add(criterio);
        }
        if (filtro.getPcaEsPrueba() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaEsPrueba", filtro.getPcaEsPrueba());
            criterios.add(criterio);
        }

        if (filtro.getPcaTipoPeriodo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaTipoPeriodo", filtro.getPcaTipoPeriodo());
            criterios.add(criterio);
        }

        if (filtro.getPcaNumeroPeriodo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pcaNumeroPeriodo", filtro.getPcaNumeroPeriodo());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgPeriodoCalificacion> obtenerPorFiltro(FiltroPeriodoCalificacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgPeriodoCalificacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroPeriodoCalificacion filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgPeriodoCalificacion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
