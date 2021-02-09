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
import sv.gob.mined.siges.filtros.FiltroSubModalidad;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.entidades.SgSubModalidadAtencion;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class SubModalidadDAO extends HibernateJpaDAOImp<SgSubModalidadAtencion, Long> implements Serializable {

    private EntityManager em;

    public SubModalidadDAO(EntityManager em) {
        super(em);
        this.em = em;
    }


    private List<CriteriaTO> generarCriteria(FiltroSubModalidad filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgSubModalidadAtencion.class, AtributoCodigo.class);
        String campoNombre = ReflectionUtils.obtenerNombreCampoAnotado(SgSubModalidadAtencion.class, AtributoNombre.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgSubModalidadAtencion.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();
        //Código
        if (!StringUtils.isBlank(filtro.getSmoCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getSmoCodigo().trim());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getSmoCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getSmoCodigoExacto().trim());
            criterios.add(criterio);
        }        
        //Nombre
        if (!StringUtils.isBlank(filtro.getSmoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getSmoNombre().trim()));
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getSmoNombreExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoNombre, SofisStringUtils.normalizarParaBusqueda(filtro.getSmoNombreExacto().trim()));
            criterios.add(criterio);
        }
        
        //Modalidad de atención
        if (!StringUtils.isBlank(filtro.getSmoModalidad())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "smoModalidadFk.matNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSmoModalidad().trim()));
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getSmoModalidadExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "smoModalidadFk.matNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSmoModalidadExacto().trim()));
            criterios.add(criterio);
        }


        if (filtro.getSmoHabilitado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getSmoHabilitado());
            criterios.add(criterio);
        }
        
        if (filtro.getSmoModalidadPk()!=null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "smoModalidadFk.matPk", filtro.getSmoModalidadPk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgSubModalidadAtencion> obtenerPorFiltro(FiltroSubModalidad filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgSubModalidadAtencion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroSubModalidad filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSubModalidadAtencion.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
   

}
