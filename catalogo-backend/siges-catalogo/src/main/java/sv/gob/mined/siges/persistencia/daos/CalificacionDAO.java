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
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacion;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class CalificacionDAO extends HibernateJpaDAOImp<SgCalificacion, Long> implements Serializable {

    private EntityManager em;

    public CalificacionDAO(EntityManager em) {
        super(em);
        this.em = em;
    }


    private List<CriteriaTO> generarCriteria(FiltroCalificacion filtro) {

        String campoCodigo = ReflectionUtils.obtenerNombreCampoAnotado(SgCalificacion.class, AtributoCodigo.class);
        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgCalificacion.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();
        
        //Calificación
        if (filtro.getEcaCalPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS,"calEscala.ecaPk", filtro.getEcaCalPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getCalCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, campoCodigo, filtro.getCalCodigo().trim());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCalCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoCodigo, filtro.getCalCodigoExacto().trim());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCalEscala())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "calEscala.ecaNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getCalEscala().trim()));
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCalEscalaExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "calEscala.ecaNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getCalEscalaExacto().trim()));
            criterios.add(criterio);
        }
        //Valor
        if (!StringUtils.isBlank(filtro.getCalValor())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "calValor", filtro.getCalValor().trim());
            criterios.add(criterio);
        }
        //Orden 
        if (filtro.getCalOrden()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "calOrden", filtro.getCalOrden());
            criterios.add(criterio);
        }
        //Habilitado
        if (filtro.getCalHabilitado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getCalHabilitado());
            criterios.add(criterio);
        }
        if(filtro.getCalEscalaCalificacionPk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calEscala.ecaPk", filtro.getCalEscalaCalificacionPk());
            criterios.add(criterio);
        }
        

        return criterios;
    }

    public List<SgCalificacion> obtenerPorFiltro(FiltroCalificacion filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCalificacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null);    
            
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCalificacion filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCalificacion.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    public Object[][] obtenerParaResourceBundle() throws DAOGeneralException {
        try {
            String query = "Select e.cal_codigo, e.cal_valor from " + Constantes.SCHEMA_CATALOGO + ".sg_calificaciones e where e.cal_habilitado = true";
            List<Object[]> list = em.createNativeQuery(query).getResultList();
            Object[][] ret = list.toArray(new Object[][] {});
            return ret;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }


}
