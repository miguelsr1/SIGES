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
import sv.gob.mined.siges.filtros.FiltroCaserio;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.SgCaserio;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class CaserioDAO extends HibernateJpaDAOImp<SgCaserio, Long> implements Serializable {

    private EntityManager em;

    public CaserioDAO(EntityManager em) {
        super(em);
        this.em = em;
    }


    private List<CriteriaTO> generarCriteria(FiltroCaserio filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgCaserio.class, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(SgCaserio.class, AtributoNombre.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgCaserio.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();
        //Código    
        if (!StringUtils.isBlank(filtro.getCasCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoCodigo, filtro.getCasCodigo().trim());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCasCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getCasCodigoExacto().trim());
            criterios.add(criterio);
        }
        //Nombre    
        if (!StringUtils.isBlank(filtro.getCasNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getCasNombre().trim()));
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCasNombreExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getCasNombreExacto().trim()));
            criterios.add(criterio);
        }
        //Cantón    
        if (!StringUtils.isBlank(filtro.getCasCanton())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "casCanton.canNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getCasCanton().trim()));
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCasCantonExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "casCanton.canNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getCasCantonExacto().trim()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCasCantonExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "casCanton.canPk", filtro.getCasCantonFk());
            criterios.add(criterio);
        }        

        if (filtro.getCasHabilitado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getCasHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgCaserio> obtenerPorFiltro(FiltroCaserio filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCaserio.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCaserio filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCaserio.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    

}
