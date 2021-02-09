/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos.catalogo;

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
import sv.gob.mined.siges.enumerados.EnumTipoFormula;
import sv.gob.mined.siges.filtros.catalogo.FiltroFormula;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFormula;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class FormulaDAO extends HibernateJpaDAOImp<SgFormula, Long> implements Serializable {

    private EntityManager em;

    public FormulaDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroFormula filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgFormula.class, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(SgFormula.class, AtributoNombre.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgFormula.class, AtributoHabilitado.class);

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
        
        if (filtro.getTipoFormula() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "fomTipoFormula", filtro.getTipoFormula());
            criterios.add(criterio);
        }
        
        if (filtro.getFormulaPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "fomPk", filtro.getFormulaPk());
            criterios.add(criterio);
        }
        
         if (filtro.getListTipoFormula()!= null && !filtro.getListTipoFormula().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (EnumTipoFormula tipo : filtro.getListTipoFormula()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fomTipoFormula", tipo);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
         
         if(filtro.getTieneSubFormula()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "fomTienSubformula", filtro.getTieneSubFormula());
            criterios.add(criterio);
         }
         
         if(filtro.getDescartarPk()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NOT_EQUALS, "fomPk", filtro.getDescartarPk());
            criterios.add(criterio);
         }

        return criterios;
    }

    public List<SgFormula> obtenerPorFiltro(FiltroFormula filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgFormula.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroFormula filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgFormula.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
