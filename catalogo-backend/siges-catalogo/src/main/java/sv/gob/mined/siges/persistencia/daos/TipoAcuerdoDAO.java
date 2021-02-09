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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroTipoAcuerdo;
import sv.gob.mined.siges.persistencia.entidades.SgTipoAcuerdo;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class TipoAcuerdoDAO extends HibernateJpaDAOImp<SgTipoAcuerdo, Integer> implements Serializable {

    private EntityManager em;

    public TipoAcuerdoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroTipoAcuerdo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getTipoAcuerdoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoPk", filtro.getTipoAcuerdoPk());
            criterios.add(criterio);
        }

        if (filtro.getExcluirTipoAcuerdoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NOT_EQUALS, "taoPk", filtro.getExcluirTipoAcuerdoPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "taoCodigo", filtro.getCodigo().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoCodigo", filtro.getCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "taoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getNombre()));
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        List<CriteriaTO> orTramites = new ArrayList();

        if (filtro.getTaoTramiteCambioDomicilioSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoTramiteCambioDomicilioSede", filtro.getTaoTramiteCambioDomicilioSede());
            if (BooleanUtils.isTrue(filtro.getRealizarORConTramites())) {
                orTramites.add(criterio);
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getTaoTramiteCreacionSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoTramiteCreacionSede", filtro.getTaoTramiteCreacionSede());
            if (BooleanUtils.isTrue(filtro.getRealizarORConTramites())) {
                orTramites.add(criterio);
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getTaoTramiteAmpliacionServicios() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoTramiteAmpliacionServicios", filtro.getTaoTramiteAmpliacionServicios());
            if (BooleanUtils.isTrue(filtro.getRealizarORConTramites())) {
                orTramites.add(criterio);
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getTaoTramiteDisminucionServicios() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoTramiteDisminucionServicios", filtro.getTaoTramiteDisminucionServicios());
            if (BooleanUtils.isTrue(filtro.getRealizarORConTramites())) {
                orTramites.add(criterio);
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getTaoTramiteNominacionSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoTramiteNominacionSede", filtro.getTaoTramiteNominacionSede());
            if (BooleanUtils.isTrue(filtro.getRealizarORConTramites())) {
                orTramites.add(criterio);
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getTaoTramiteRevocatoriaSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "taoTramiteRevocatoriaSede", filtro.getTaoTramiteRevocatoriaSede());
            if (BooleanUtils.isTrue(filtro.getRealizarORConTramites())) {
                orTramites.add(criterio);
            } else {
                criterios.add(criterio);
            }
        }

        if (BooleanUtils.isTrue(filtro.getRealizarORConTramites())) {
            if (orTramites.size() > 1) {
                criterios.add(CriteriaTOUtils.createORTO(orTramites.toArray(new CriteriaTO[orTramites.size()])));
            } else if (orTramites.size() == 1) {
                criterios.add(orTramites.get(0));
            }
        }

        return criterios;
    }

    public List<SgTipoAcuerdo> obtenerPorFiltro(FiltroTipoAcuerdo filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgTipoAcuerdo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroTipoAcuerdo filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgTipoAcuerdo.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
