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
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import sv.gob.mined.siges.filtros.FiltroAcuerdoSistema;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSistema;

public class AcuerdoSistemaDAO extends HibernateJpaDAOImp<SgAcuerdoSistema, Integer> implements Serializable {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(AcuerdoSistemaDAO.class.getName());

    public AcuerdoSistemaDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroAcuerdoSistema filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        

        if(filtro.getAsiSistemaIntegrado()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiSistemaIntegrado.sinPk", filtro.getAsiSistemaIntegrado());
            criterios.add(criterio);
        }
        
        if (filtro.getAsiTipoAcuerdo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiTipoAcuerdo.taoPk", filtro.getAsiTipoAcuerdo());
            criterios.add(criterio);
        }
        
        if (filtro.getAsiEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiEstado", filtro.getAsiEstado());
            criterios.add(criterio);
        }
        
        if (filtro.getAsiNumeroAcuerdo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiNumeroAcuerdo", filtro.getAsiNumeroAcuerdo());
            criterios.add(criterio);
        }
        
        if (filtro.getAsiFechaCreacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "asiFechaCreacion", filtro.getAsiFechaCreacion());
            criterios.add(criterio);
        }

        if (filtro.getAsiFechaCreacionInicio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "asiFechaCreacion", filtro.getAsiFechaCreacionInicio()); 
            criterios.add(criterio);
        }

        if (filtro.getAsiFechaCreacionFin() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "asiFechaCreacion", filtro.getAsiFechaCreacionFin());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgAcuerdoSistema> obtenerPorFiltro(FiltroAcuerdoSistema filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAcuerdoSistema.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null,filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAcuerdoSistema filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAcuerdoSistema.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
