/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.dto.SgCalificacionCompPeriodo;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
public class CalificacionDAO extends HibernateJpaDAOImp<SgCalificacionCE, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public CalificacionDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.segDAO = segBean;
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroCalificacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCalPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calPk", filtro.getCalPk());
            criterios.add(criterio);
        }
        if (filtro.getCalComponentePlanEstudio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calComponentePlanEstudio.cpePk", filtro.getCalComponentePlanEstudio());
            criterios.add(criterio);
        }
        if (filtro.getCalFecha() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calFecha", filtro.getCalFecha());
            criterios.add(criterio);
        }
        if (filtro.getCalRangoFecha() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calRangoFecha.rfePk", filtro.getCalRangoFecha());
            criterios.add(criterio);
        }
        if (filtro.getSecSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduSede.sedPk", filtro.getSecSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getSecNivelFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSecNivelFk());
            criterios.add(criterio);
        }
        if (filtro.getSecCicloFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSecCicloFk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadEducativaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSecModalidadEducativaPk());
            criterios.add(criterio);
        }
        if (filtro.getSecModalidadAtencionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSecModalidadAtencionPk());
            criterios.add(criterio);
        }
        if (filtro.getSecSubModalidadAtencionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSecSubModalidadAtencionFk());
            criterios.add(criterio);
        }
        if (filtro.getSecGradoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secServicioEducativo.sduGrado.graPk", filtro.getSecGradoFk());
            criterios.add(criterio);
        }
        if (filtro.getSecPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secPk", filtro.getSecPk());
            criterios.add(criterio);
        }
        if (filtro.getSecAnioLectivoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secAnioLectivo.alePk", filtro.getSecAnioLectivoFk());
            criterios.add(criterio);
        }
        if (filtro.getCalNumero() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calNumero", filtro.getCalNumero());
            criterios.add(criterio);
        }
        if (filtro.getCalTipoCalendarioEscolar() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calTipoCalendarioEscolar", filtro.getCalTipoCalendarioEscolar());
            criterios.add(criterio);
        }
        if (filtro.getCalTipoPeriodoCalificacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calTipoPeriodoCalificacion", filtro.getCalTipoPeriodoCalificacion());
            criterios.add(criterio);
        }
        if(filtro.getCalPeriodoCalificacion()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calRangoFecha.rfePeriodoCalificacion", filtro.getCalPeriodoCalificacion());
            criterios.add(criterio);
        }
        if(!StringUtils.isBlank(filtro.getEndOfUsuario())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.END_WITH, "calUltModUsuario", filtro.getEndOfUsuario());
            criterios.add(criterio);
        }
        if (filtro.getCalTiposPeriodoCalificacion() != null && !filtro.getCalTiposPeriodoCalificacion().isEmpty()) {
            List<CriteriaTO> periodoCriteria = new ArrayList();
            for (EnumTiposPeriodosCalificaciones etp : filtro.getCalTiposPeriodoCalificacion()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calTipoPeriodoCalificacion", etp);
                periodoCriteria.add(criterio);
            }
            if (!periodoCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = periodoCriteria.toArray(new CriteriaTO[periodoCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
        if (filtro.getCalSecciones()!= null && !filtro.getCalSecciones().isEmpty()) {
            List<CriteriaTO> periodoCriteria = new ArrayList();
            for (Long etp : filtro.getCalSecciones()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secPk", etp);
                periodoCriteria.add(criterio);
            }
            if (!periodoCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = periodoCriteria.toArray(new CriteriaTO[periodoCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }       
        if(filtro.getCalEstadoProcesamientoCal()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calEstadoProcesamientoPromocion", filtro.getCalEstadoProcesamientoCal());
            criterios.add(criterio);
        }
        if(filtro.getSecPlanEstudioFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secPlanEstudio.pesPk", filtro.getSecPlanEstudioFk());
            criterios.add(criterio);
        }
        if(filtro.getCalComponentePlanEstudioList()!=null && !filtro.getCalComponentePlanEstudioList().isEmpty()){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "calComponentePlanEstudio.cpePk", filtro.getCalComponentePlanEstudioList());
            criterios.add(criterio);
        }
        if(filtro.getCaeTipoPeriodo()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secTipoPeriodo", filtro.getCaeTipoPeriodo());
            criterios.add(criterio);
        }
        
        if (filtro.getCaeNumeroPeriodo()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secNumeroPeriodo", filtro.getCaeNumeroPeriodo());
            criterios.add(criterio);
        }
        
        if(filtro.getCalificacionCompPeriodoList()!=null && !filtro.getCalificacionCompPeriodoList().isEmpty()){
            List<CriteriaTO> variablesOR = new ArrayList();
            
            
            for (SgCalificacionCompPeriodo etp : filtro.getCalificacionCompPeriodoList()) {
                List<CriteriaTO> variablesAND = new ArrayList();
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calComponentePlanEstudio.cpePk", etp.getComponentePlanEstudioPk());
                variablesAND.add(criterio);
                
                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calTipoPeriodoCalificacion", etp.getTipoPeriodoCalificacion());
                variablesAND.add(criterio);
                
                if(etp.getRangoFechaPk()!=null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calRangoFecha.rfePk", etp.getRangoFechaPk());
                    variablesAND.add(criterio);
                }
                if(etp.getTipoCalendarioEscolar()!=null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calTipoCalendarioEscolar", etp.getTipoCalendarioEscolar());
                    variablesAND.add(criterio);
                }
                if(etp.getRphNumeroExtraordinario()!=null){
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calNumero", etp.getRphNumeroExtraordinario());
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
        
        if (filtro.getEstadoSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSeccion.secEstado", filtro.getEstadoSeccion());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgCalificacionCE> obtenerPorFiltro(FiltroCalificacion filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgCalificacionCE.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCalificacion filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCalificacionCE.class, criterio, false, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
