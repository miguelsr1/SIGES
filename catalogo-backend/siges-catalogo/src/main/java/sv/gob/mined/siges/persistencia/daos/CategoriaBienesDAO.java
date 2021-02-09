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
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroCategoriaBienes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.entidades.SgAfCategoriaBienes;
import sv.gob.mined.siges.utils.ReflectionUtils;


public class CategoriaBienesDAO extends HibernateJpaDAOImp<SgAfCategoriaBienes, Long> implements Serializable {

    private EntityManager em;

    public CategoriaBienesDAO(EntityManager em) {
        super(em);
        this.em = em;
    }


    private List<CriteriaTO> generarCriteria(FiltroCategoriaBienes filtro) {

        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgAfCategoriaBienes.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCodigo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "cabCodigo", filtro.getCodigo());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "cabNombre", filtro.getNombre().trim());
            criterios.add(criterio);
        }

        if (filtro.getClasificacionId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cabClasificacionBienes.cbiPk", filtro.getClasificacionId());
            criterios.add(criterio);
        }
        
        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgAfCategoriaBienes> obtenerPorFiltro(FiltroCategoriaBienes filtro) throws DAOGeneralException {
        try {
            
            List<CriteriaTO> criterios = this.generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));        
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgAfCategoriaBienes.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCategoriaBienes filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfCategoriaBienes.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
