/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import sv.gob.mined.siges.filtros.FiltroPeriodoCalendario;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalendario;

/**
 *
 * @author usuario
 */
public class PeriodoCalendarioDAO extends HibernateJpaDAOImp<SgPeriodoCalendario, Integer> implements Serializable {


    public PeriodoCalendarioDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroPeriodoCalendario filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cesCalendario.calPk", filtro.getCalPk());
            criterios.add(criterio);
        }

        if (filtro.getCesModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cesNivel.nivPk", filtro.getCesNivelFk());
            criterios.add(criterio);
        }

        if (filtro.getCesNivelFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cesModalidadAtencion.matPk", filtro.getCesModalidadAtencionFk());
            criterios.add(criterio);
        }
        if (filtro.getCesTipo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cesTipo", filtro.getCesTipo());
            criterios.add(criterio);
        }
        if (filtro.getFechaCalificacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "cesFechaHasta", filtro.getFechaCalificacion());
            criterios.add(criterio);
        }
        if (filtro.getFechaCalificacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "cesFechaDesde", filtro.getFechaCalificacion());
            criterios.add(criterio);
        }
        if (filtro.getCesHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cesHabilitado", filtro.getCesHabilitado());
            criterios.add(criterio);
        }
        if (filtro.getCesSubModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cesSubModalidadAtencion.smoPk", filtro.getCesSubModalidadAtencionFk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgPeriodoCalendario> obtenerPorFiltro(FiltroPeriodoCalendario filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgPeriodoCalendario.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroPeriodoCalendario filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgPeriodoCalendario.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
