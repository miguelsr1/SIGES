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
import sv.gob.mined.siges.filtros.FiltroRangoFecha;
import sv.gob.mined.siges.persistencia.entidades.SgRangoFecha;

public class RangoFechaDAO extends HibernateJpaDAOImp<SgRangoFecha, Integer> implements Serializable {

    private EntityManager em;

    public RangoFechaDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroRangoFecha filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getPeriodoCalificacionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaPk", filtro.getPeriodoCalificacionPk());
            criterios.add(criterio);
        }

        if (filtro.getExcluirRangoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "rfePk", filtro.getExcluirRangoPk());
            criterios.add(criterio);
        }

        if (filtro.getCodigoExacto() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfeCodigo", filtro.getCodigoExacto());
            criterios.add(criterio);
        }

        if (filtro.getCantidadPeriodoCalificacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaNumero", filtro.getCantidadPeriodoCalificacion());
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfeHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        // [Inicio,Fin] superposición [Desde,Hasta] --> [x1,x2] superposición [y1,y2]
        // x1 <= y2 && y1 <= x2
        if (filtro.getDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "rfeFechaHasta", filtro.getDesde());
            criterios.add(criterio);
        }

        if (filtro.getHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "rfeFechaDesde", filtro.getHasta());
            criterios.add(criterio);
        }

        if (filtro.getModalidadEducativaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaModalidad.modPk", filtro.getModalidadEducativaPk());
            criterios.add(criterio);
        }

        if (filtro.getModalidadAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaModalidadAtencion.matPk", filtro.getModalidadAtencionPk());
            criterios.add(criterio);
        }
        if (filtro.getSubModalidadAtencionPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaSubModalidadAtencion.smoPk", filtro.getSubModalidadAtencionPk());
            criterios.add(criterio);
        }
        if (filtro.getPcaNumeros() != null && !filtro.getPcaNumeros().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rfePeriodoCalificacion.pcaNumero", filtro.getPcaNumeros());
            criterios.add(criterio);
        }
        
        if (filtro.getPcaTipoPeriodo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaTipoPeriodo", filtro.getPcaTipoPeriodo());
            criterios.add(criterio);
        }

        if (filtro.getPcaNumeroPeriodo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaNumeroPeriodo", filtro.getPcaNumeroPeriodo());
            criterios.add(criterio);
        }
        
         if (filtro.getPcaAnioLectivo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaCalendario.calAnioLectivo.alePk", filtro.getPcaAnioLectivo());
            criterios.add(criterio);
        }
        if (filtro.getPcaTipoCalendario() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaCalendario.calTipoCalendario.tcePk", filtro.getPcaTipoCalendario());
            criterios.add(criterio);
        }
        if (filtro.getPcaEsPrueba() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rfePeriodoCalificacion.pcaEsPrueba", filtro.getPcaEsPrueba());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgRangoFecha> obtenerPorFiltro(FiltroRangoFecha filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRangoFecha.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroRangoFecha filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRangoFecha.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
