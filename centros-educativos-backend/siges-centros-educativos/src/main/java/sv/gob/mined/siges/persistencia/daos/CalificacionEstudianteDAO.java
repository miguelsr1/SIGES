/*
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
import sv.gob.mined.siges.dto.SgCalificacionCompPeriodo;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgAnioLectivo;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;

/**
 *
 * @author Sofis Solutions
 */
public class CalificacionEstudianteDAO extends HibernateJpaDAOImp<SgCalificacionEstudiante, Integer> implements Serializable {


    public CalificacionEstudianteDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroCalificacionEstudiante filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCabezalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calPk", filtro.getCabezalPk());
            criterios.add(criterio);
        }

        if (filtro.getExcluirCabezalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "caeCalificacion.calPk", filtro.getExcluirCabezalPk());
            criterios.add(criterio);
        }

        if (filtro.getSeccionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calSeccion.secPk", filtro.getSeccionPk());
            criterios.add(criterio);
        }

        if (filtro.getCalificacionEstudiantePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caePk", filtro.getCalificacionEstudiantePk());
            criterios.add(criterio);
        }

        if (filtro.getSeccionActualEstudiantesPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeEstudiante.estUltimaSeccionPk", filtro.getSeccionActualEstudiantesPk());
            criterios.add(criterio);
        }

        if (filtro.getAnioLectivoPk() != null) {
            
            SgAnioLectivo anio = new SgAnioLectivo(filtro.getAnioLectivoPk(),0);
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calSeccion.secAnioLectivo",  anio);
            criterios.add(criterio);
        }

        if (filtro.getAnioLectivo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calSeccion.secAnioLectivo.aleAnio", filtro.getAnioLectivo());
            criterios.add(criterio);
        }

        if (filtro.getEstudiantePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeEstudiante.estPk", filtro.getEstudiantePk());
            criterios.add(criterio);
        }

        if (filtro.getCaeEstudiantesPk() != null && !filtro.getCaeEstudiantesPk().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "caeEstudiante.estPk", filtro.getCaeEstudiantesPk());
            criterios.add(criterio);
        }

        if (filtro.getCaeComponentePlanEstudio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calComponentePlanEstudio.cpePk", filtro.getCaeComponentePlanEstudio());
            criterios.add(criterio);
        }

        if (filtro.getCaeRangoFechaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calRangoFecha.rfePk", filtro.getCaeRangoFechaPk());
            criterios.add(criterio);
        }
        if (filtro.getCaeTipoPeriodoCalificacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calTipoPeriodoCalificacion", filtro.getCaeTipoPeriodoCalificacion());
            criterios.add(criterio);
        }
        if (filtro.getCaeTipoCalendarioEscolar() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calTipoCalendarioEscolar", filtro.getCaeTipoCalendarioEscolar());
            criterios.add(criterio);
        }
        if (filtro.getCaeNumero() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calNumero", filtro.getCaeNumero());
            criterios.add(criterio);
        }
        if (filtro.getCaeEstudiantePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeEstudiante.estPk", filtro.getCaeEstudiantePk());
            criterios.add(criterio);
        }
        if (filtro.getDescartandoSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "caeCalificacion.calSeccion.secPk", filtro.getDescartandoSeccion());
            criterios.add(criterio);
        }
        if (filtro.getCaeTiposPeriodoCalificacion() != null && !filtro.getCaeTiposPeriodoCalificacion().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "caeCalificacion.calTipoPeriodoCalificacion", filtro.getCaeTiposPeriodoCalificacion());
            criterios.add(criterio);
//            List<CriteriaTO> periodoCriteria = new ArrayList();
//            for (EnumTiposPeriodosCalificaciones etp : filtro.getCaeTiposPeriodoCalificacion()) {
//                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calTipoPeriodoCalificacion", etp);
//                periodoCriteria.add(criterio);
//            }
//            CriteriaTO[] arrCriterioOR = periodoCriteria.toArray(new CriteriaTO[periodoCriteria.size()]);
//            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
//            criterios.add(criterioOR);
        }
        if (filtro.getComponentePlanEstudioPk() != null && !filtro.getComponentePlanEstudioPk().isEmpty()) {
            List<CriteriaTO> periodoCriteria = new ArrayList();
            for (Long etp : filtro.getComponentePlanEstudioPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calComponentePlanEstudio.cpePk", etp);
                periodoCriteria.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = periodoCriteria.toArray(new CriteriaTO[periodoCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getCalficacionesPk()!= null && !filtro.getCalficacionesPk().isEmpty()) {
            List<CriteriaTO> periodoCriteria = new ArrayList();
            for (Long etp : filtro.getCalficacionesPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calPk", etp);
                periodoCriteria.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = periodoCriteria.toArray(new CriteriaTO[periodoCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getCaePromovido() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caePromovidoCalificacion", filtro.getCaePromovido());
            criterios.add(criterio);
        }
        if (filtro.getCaeEsPaes() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calComponentePlanEstudio.cpeEsPaes", filtro.getCaeEsPaes());
            criterios.add(criterio);
        }
        if (filtro.getCaeGradoFk()!= null) {
            
            SgGrado grado = new SgGrado(filtro.getCaeGradoFk(), 0);
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calSeccion.secServicioEducativo.sduGrado", grado);
            criterios.add(criterio);
        }
        
        if(filtro.getCaeTipoPeriodo()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calSeccion.secTipoPeriodo", filtro.getCaeTipoPeriodo());
            criterios.add(criterio);
        }
        
        if (filtro.getCaeNumeroPeriodo()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calSeccion.secNumeroPeriodo", filtro.getCaeNumeroPeriodo());
            criterios.add(criterio);
        }
        if (filtro.getRangoFechaHabilitado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calRangoFecha.rfeHabilitado", filtro.getRangoFechaHabilitado());
            criterios.add(criterio);
        }
        if (filtro.getCpeTipo()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calComponentePlanEstudio.cpeTipo", filtro.getCpeTipo());
            criterios.add(criterio);
        }
        
        if(filtro.getCalificacionCompPeriodoList()!=null && !filtro.getCalificacionCompPeriodoList().isEmpty()){
            List<CriteriaTO> variablesOR = new ArrayList();
            
            
            for (SgCalificacionCompPeriodo etp : filtro.getCalificacionCompPeriodoList()) {
                List<CriteriaTO> variablesAND = new ArrayList();
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calComponentePlanEstudio.cpePk", etp.getComponentePlanEstudioPk());
                variablesAND.add(criterio);
                
                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calTipoPeriodoCalificacion", etp.getTipoPeriodoCalificacion());
                variablesAND.add(criterio);
                
                if(etp.getRangoFechaPk()!=null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calRangoFecha.rfePk", etp.getRangoFechaPk());
                    variablesAND.add(criterio);
                }
                if(etp.getTipoCalendarioEscolar()!=null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calTipoCalendarioEscolar", etp.getTipoCalendarioEscolar());
                    variablesAND.add(criterio);
                }
                if(etp.getRphNumeroExtraordinario()!=null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "caeCalificacion.calNumero", etp.getRphNumeroExtraordinario());
                    variablesAND.add(criterio);
                }
                
                CriteriaTO[] arrCriterioAND = variablesAND.toArray(new CriteriaTO[variablesAND.size()]);
                CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];
                
                variablesOR.add(criterioAND);
            }
            
            if (!variablesOR.isEmpty()) {
                CriteriaTO[] arrCriterioOR = variablesOR.toArray(new CriteriaTO[variablesOR.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        return criterios;
    }

    public List<SgCalificacionEstudiante> obtenerPorFiltro(FiltroCalificacionEstudiante filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCalificacionEstudiante.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCalificacionEstudiante filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCalificacionEstudiante.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
