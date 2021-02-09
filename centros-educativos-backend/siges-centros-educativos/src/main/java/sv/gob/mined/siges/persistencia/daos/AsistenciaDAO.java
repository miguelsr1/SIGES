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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroAsistencia;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class AsistenciaDAO extends HibernateJpaDAOImp<SgAsistencia, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public AsistenciaDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroAsistencia filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAsiPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiPk", filtro.getAsiPk());
            criterios.add(criterio);
        }
        if (filtro.getAsiSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiControl.cacSeccion.secPk", filtro.getAsiSeccion());
            criterios.add(criterio);
        }
        if (filtro.getAsiAnioLectivo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiControl.cacSeccion.secAnioLectivo.alePk", filtro.getAsiAnioLectivo());
            criterios.add(criterio);
        }
        if (filtro.getAsiEstudiante() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiEstudiante.estPk", filtro.getAsiEstudiante());
            criterios.add(criterio);
        }
        if (filtro.getAsiMotivoInasistencia() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiMotivoInasistencia.minPk", filtro.getAsiMotivoInasistencia());
            criterios.add(criterio);
        }
        if (filtro.getAsiFecha() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiFecha", filtro.getAsiFecha());
            criterios.add(criterio);
        }
        if (filtro.getAsiCabezalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiControl.cacPk", filtro.getAsiCabezalPk());
            criterios.add(criterio);
        }
        if (filtro.getAsiInasistencia() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiInasistencia", filtro.getAsiInasistencia());
            criterios.add(criterio);
        }
        if (filtro.getAsiFechaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "asiControl.cacFecha", filtro.getAsiFechaDesde());
            criterios.add(criterio);
        }
        if (filtro.getAsiFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "asiControl.cacFecha", filtro.getAsiFechaHasta());
            criterios.add(criterio);
        }

        if (filtro.getAsiCabezalesPk() != null && !filtro.getAsiCabezalesPk().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "asiControl.cacPk", filtro.getAsiCabezalesPk());
            criterios.add(criterio);
        }

        if (filtro.getAsiFechas() != null && !filtro.getAsiFechas().isEmpty()) {
            List<CriteriaTO> cabezalesCriteria = new ArrayList();
            for (LocalDate cabPk : filtro.getAsiFechas()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiControl.cacFecha", cabPk);
                cabezalesCriteria.add(criterio);
            }

            if (!cabezalesCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = cabezalesCriteria.toArray(new CriteriaTO[cabezalesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getCaeEstudiantesPk() != null && !filtro.getCaeEstudiantesPk().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "asiEstudiante.estPk", filtro.getCaeEstudiantesPk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgAsistencia> obtenerPorFiltro(FiltroAsistencia filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAsistencia.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAsistencia filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAsistencia.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public SgAsistencia obtenerPorId(Long id) throws DAOGeneralException {
        try {
            return em.find(SgAsistencia.class, id);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
