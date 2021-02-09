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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroRelPeriodosHabilitacionPeriodo;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgRelPeriodosHabilitacionPeriodo;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class RelPeriodosHabilitacionPeriodoDAO extends HibernateJpaDAOImp<SgRelPeriodosHabilitacionPeriodo, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private EntityManager em;
    private JsonWebToken jwt;

    public RelPeriodosHabilitacionPeriodoDAO(EntityManager em, SeguridadBean seg) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
        this.segDAO=seg;
    }

    private List<CriteriaTO> generarCriteria(FiltroRelPeriodosHabilitacionPeriodo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getRphEstudianteFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphHabilitacionPeriodoCalificacionFk.hpcEstudianteFk.estPk", filtro.getRphEstudianteFk());
            criterios.add(criterio);
        }
        
        if (filtro.getRphHabilitacionPeriodoCalificacionFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphHabilitacionPeriodoCalificacionFk.hpcPk", filtro.getRphHabilitacionPeriodoCalificacionFk());
            criterios.add(criterio);
        }
         if (filtro.getRangoFechaFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphRangoFechaFk.rfePk", filtro.getRangoFechaFk());
            criterios.add(criterio);
        }
         
         if (filtro.getHpcEstado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphHabilitacionPeriodoCalificacionFk.hpcEstado", filtro.getHpcEstado());
            criterios.add(criterio);
        }
         
         if(filtro.getEstudiantesFk()!=null && !filtro.getEstudiantesFk().isEmpty()){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rphHabilitacionPeriodoCalificacionFk.hpcEstudianteFk.estPk", filtro.getEstudiantesFk());
             criterios.add(criterio);
         }
         
         if(filtro.getFechaHabilitada()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "rphHabilitacionPeriodoCalificacionFk.hpcFechaDesde", filtro.getFechaHabilitada());
             criterios.add(criterio);
             MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "rphHabilitacionPeriodoCalificacionFk.hpcFechaHasta", filtro.getFechaHabilitada());
             criterios.add(criterio1);    
         }
      
         if (filtro.getSeccionFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphHabilitacionPeriodoCalificacionFk.hpcEstudianteFk.estUltimaMatricula.matSeccion.secPk", filtro.getSeccionFk());
            criterios.add(criterio);
        }
         
        if (filtro.getPeriodoCalificacionFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphRangoFechaFk.rfePeriodoCalificacion.pcaPk", filtro.getPeriodoCalificacionFk());
            criterios.add(criterio);
        }
         
         if(filtro.getIgnorarRangoFechaFk()!=null && !filtro.getIgnorarRangoFechaFk().isEmpty()){
             List<CriteriaTO> cabezalesCriteria = new ArrayList();
             for(Long rangoPk:filtro.getIgnorarRangoFechaFk()){
                 MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "rphRangoFechaFk.rfePk", rangoPk);
                cabezalesCriteria.add(criterio);
             }            

            if (!cabezalesCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = cabezalesCriteria.toArray(new CriteriaTO[cabezalesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
         }
         
         if(filtro.getRangoFechaFkList()!=null && !filtro.getRangoFechaFkList().isEmpty()){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rphRangoFechaFk.rfePk", filtro.getRangoFechaFkList());
             criterios.add(criterio);
         }
       
         if (filtro.getRphTipoPeriodoCalificacion()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphTipoPeriodoCalificacion", filtro.getRphTipoPeriodoCalificacion());
            criterios.add(criterio);
        }
         
        if(filtro.getRphTipoCalendarioEscolarList()!=null && !filtro.getRphTipoCalendarioEscolarList().isEmpty()){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rphTipoCalendarioEscolar", filtro.getRphTipoCalendarioEscolarList());
             criterios.add(criterio);
         }
        
        if (filtro.getRphTipoCalendarioEscolar()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rphTipoCalendarioEscolar", filtro.getRphTipoCalendarioEscolar());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgRelPeriodosHabilitacionPeriodo> obtenerPorFiltro(FiltroRelPeriodosHabilitacionPeriodo filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelPeriodosHabilitacionPeriodo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null,filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroRelPeriodosHabilitacionPeriodo filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelPeriodosHabilitacionPeriodo.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public SgRelPeriodosHabilitacionPeriodo obtenerPorId(Long id) throws DAOGeneralException {
        try {
            return em.find(SgRelPeriodosHabilitacionPeriodo.class, id);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
