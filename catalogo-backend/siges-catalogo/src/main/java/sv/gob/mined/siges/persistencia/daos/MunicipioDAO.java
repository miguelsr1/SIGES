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
import sv.gob.mined.siges.filtros.FiltroMunicipio;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.SgMunicipio;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class MunicipioDAO extends HibernateJpaDAOImp<SgMunicipio, Long> implements Serializable {

    private EntityManager em;

    public MunicipioDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroMunicipio filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgMunicipio.class, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(SgMunicipio.class, AtributoNombre.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgMunicipio.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getMunCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getMunCodigo().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getMunCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getMunCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getMunNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getMunNombre().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getMunNombreExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getMunNombreExacto().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getMunDepartamento())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "munDepartamento.depNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getMunDepartamento().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getMunDepartamentoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "munDepartamento.depNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getMunDepartamentoExacto().trim()));
            criterios.add(criterio);
        }

        if (filtro.getMunDepartamentoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "munDepartamento.depPk", filtro.getMunDepartamentoFk());
            criterios.add(criterio);
        }

        if (filtro.getMunHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getMunHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgMunicipio> obtenerPorFiltro(FiltroMunicipio filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgMunicipio.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroMunicipio filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);    
            }
            return this.countByCriteria(SgMunicipio.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }



}
