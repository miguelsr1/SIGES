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
import sv.gob.mined.siges.filtros.FiltroEspecialidad;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.SgEspecialidad;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class EspecialidadDAO extends HibernateJpaDAOImp<SgEspecialidad, Long> implements Serializable {

    private EntityManager em;

    public EspecialidadDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroEspecialidad filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgEspecialidad.class, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(SgEspecialidad.class, AtributoNombre.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgEspecialidad.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoCodigo, filtro.getCodigo().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getNombre().trim()));
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getHabilitado());
            criterios.add(criterio);
        }
        
        if (filtro.getEspExcluirPK() != null && !filtro.getEspExcluirPK().isEmpty()) {
            MatchCriteriaTO criterio;
            for (Long espPk : filtro.getEspExcluirPK()) {
                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "espPk", espPk);
                criterios.add(criterio);
            }
        }

        return criterios;
    }

    public List<SgEspecialidad> obtenerPorFiltro(FiltroEspecialidad filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgEspecialidad.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEspecialidad filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEspecialidad.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
