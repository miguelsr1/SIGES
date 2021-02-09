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
import sv.gob.mined.siges.filtros.FiltroCargoOAE;
import sv.gob.mined.siges.persistencia.entidades.SgCargoOAE;
import sv.gob.mined.siges.persistencia.entidades.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class CargoOAEDAO extends HibernateJpaDAOImp<SgCargoOAE, Long> implements Serializable {

    private EntityManager em;

    public CargoOAEDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroCargoOAE filtro) {


        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "coaCodigo", filtro.getCodigo().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "coaCodigo", filtro.getCodigo().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "coaNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getNombre().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombreMasculino())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "coaNombreBusquedaMasculino", SofisStringUtils.normalizarParaBusqueda(filtro.getNombreMasculino().trim()));
            criterios.add(criterio);
        }


        if (filtro.getTipoOrganismoAdministrativoPk() != null) {
            
            SgTipoOrganismoAdministrativo t = new SgTipoOrganismoAdministrativo(filtro.getTipoOrganismoAdministrativoPk(), 0);
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.MEMBER_OF, "coaTiposOrganismoAdministrativo", t);
            criterios.add(criterio);
        }
        

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "coaHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgCargoOAE> obtenerPorFiltro(FiltroCargoOAE filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCargoOAE.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCargoOAE filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCargoOAE.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
