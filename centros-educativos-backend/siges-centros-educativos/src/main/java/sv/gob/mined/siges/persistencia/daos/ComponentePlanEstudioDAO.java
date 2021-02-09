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
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.filtros.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;

public class ComponentePlanEstudioDAO extends HibernateJpaDAOImp<SgComponentePlanEstudio, Integer> implements Serializable {


    public ComponentePlanEstudioDAO(EntityManager em) throws Exception {
        super(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroComponentePlanEstudio filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCpeCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cpeCodigo", filtro.getCpeCodigo().trim());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getCpeCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpeCodigo", filtro.getCpeCodigoExacto().trim());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getCpeNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "cpeNombre", filtro.getCpeNombre());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getCpeNombrePublicable())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "cpeNombrePublicable", filtro.getCpeNombrePublicable().trim());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getCpeDescripcion())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cpeDescripcion", filtro.getCpeDescripcion().trim());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getCpeContenidoTematico())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "cpeContenidoTematico", filtro.getCpeContenidoTematico().trim());
            criterios.add(criterio);
        }
        if (filtro.getCpeTipo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpeTipo", filtro.getCpeTipo());
            criterios.add(criterio);
        }
        if (filtro.getCpeGrado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpeComponentesPlanGrado.cpgGrado.graPk", filtro.getCpeGrado());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getCpePlanEstudio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpeComponentesPlanGrado.cpgPlanEstudio.pesPk", filtro.getCpePlanEstudio());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getCpeHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpeHabilitado", filtro.getCpeHabilitado());
            criterios.add(criterio);
        }
        if (filtro.getExculsivoDeSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cpeComponentesPlanGrado.cpgExclusivoSeccion.secPk", filtro.getExculsivoDeSeccion());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NULL, "cpeComponentesPlanGrado.cpgExclusivoSeccion", 1);

            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        } else {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "cpeTipo", TipoComponentePlanEstudio.AESS);
            criterios.add(criterio);
        }
        if (filtro.getCpeEsPaes() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpeEsPaes", filtro.getCpeEsPaes());
            criterios.add(criterio);
        }
        if (filtro.getCpeOmitirTipo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "cpeTipo", filtro.getCpeOmitirTipo());
            criterios.add(criterio);
        }
        if (filtro.getCpeCodigoExterno() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpeCodigoExterno", filtro.getCpeCodigoExterno());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgComponentePlanEstudio> obtenerPorFiltro(FiltroComponentePlanEstudio filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgComponentePlanEstudio.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }


    public Long cantidadTotal(FiltroComponentePlanEstudio filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgComponentePlanEstudio.class, criterio, filtro.getSeNecesitaDistinct(), null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
