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
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroEtiqueta;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.entidades.SgEtiqueta;
import sv.gob.mined.siges.utils.ReflectionUtils;

public class EtiquetaDAO extends HibernateJpaDAOImp<SgEtiqueta, Long> implements Serializable {

    private EntityManager em;

    public EtiquetaDAO(EntityManager em) {
        super(em);
        this.em = em;
    }


    private List<CriteriaTO> generarCriteria(FiltroEtiqueta filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgEtiqueta.class, AtributoCodigo.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgEtiqueta.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getCodigo().trim());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getCodigoExacto().trim());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getValor())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "etiValor", filtro.getValor().trim());
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getHabilitado());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgEtiqueta> obtenerPorFiltro(FiltroEtiqueta filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgEtiqueta.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEtiqueta filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEtiqueta.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    public Object[][] obtenerParaResourceBundle() throws DAOGeneralException {
        try {
            String query = "Select e.eti_codigo, e.eti_valor from " + Constantes.SCHEMA_CATALOGO + ".sg_etiquetas e where e.eti_habilitado = true";
            List<Object[]> list = em.createNativeQuery(query).getResultList();
            Object[][] ret = list.toArray(new Object[][] {});
            return ret;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }


}
