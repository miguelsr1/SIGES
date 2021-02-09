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
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroConfirmacionAprobacion;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionAprobacion;

public class ConfirmacionAprobacionDAO extends HibernateJpaDAOImp<SgConfirmacionAprobacion, Integer> implements Serializable {


    public ConfirmacionAprobacionDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroConfirmacionAprobacion filtro) {

        List<CriteriaTO> criterios = new ArrayList();


        if (filtro.getCprPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cprPk", filtro.getCprPk());
            criterios.add(criterio);
        }

        if (filtro.getSeccionFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cprSeccion.secPk", filtro.getSeccionFk());
            criterios.add(criterio);
        }
        
        if (filtro.getComponentePlanEstudioFk() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cprComponentePlanEstudio.cpePk", filtro.getComponentePlanEstudioFk());
            criterios.add(criterio);
        }
        
        if (filtro.getFirmada()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cprFirmada", filtro.getFirmada());
            criterios.add(criterio);
        }
        
        if (filtro.getConfirmada() != null){
            if (filtro.getConfirmada()){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "cprFechaConfirmacion", 1);
                criterios.add(criterio);
            } else {
                //las que deben ser firmadas
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "cprFechaConfirmacion", 1);
                criterios.add(criterio);
            }
        
        }
        
        if (!StringUtils.isBlank(filtro.getFirmaTransactionId())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cprFirmaTransactionId", filtro.getFirmaTransactionId());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgConfirmacionAprobacion> obtenerPorFiltro(FiltroConfirmacionAprobacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgConfirmacionAprobacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroConfirmacionAprobacion filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgConfirmacionAprobacion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
