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
import sv.gob.mined.siges.filtros.FiltroDiscapacidad;
import sv.gob.mined.siges.persistencia.entidades.SgDiscapacidad;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class DiscapacidadDAO extends HibernateJpaDAOImp<SgDiscapacidad, Integer> implements Serializable {

    private EntityManager em;
    
    public DiscapacidadDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroDiscapacidad filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getDisNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "disNombre", SofisStringUtils.normalizarParaBusqueda(filtro.getDisNombre()));
            criterios.add(criterio);
        }
      if (filtro.getDisHabilitado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "disHabilitado", filtro.getDisHabilitado());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgDiscapacidad> obtenerPorFiltro(FiltroDiscapacidad filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgDiscapacidad.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public List<SgDiscapacidad> obtenerDiscapacidades(Long perPk) {
        String query = "select dis.* from "
                + "centros_educativos.sg_personas_discapacidades spd\n" +
                "join catalogo.sg_discapacidades dis on spd.dis_pk = dis.dis_pk\n" +
                "where spd.per_pk = 4980422";
        javax.persistence.Query q = em.createNativeQuery(query, SgDiscapacidad.class);
        //q.setParameter("perPk", perPk);

        List<SgDiscapacidad> objs = q.getResultList();
        return objs;
    }

}
