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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class DepreciacionDAO extends HibernateJpaDAOImp<SgAfDepreciacion, Long> implements Serializable {
    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    public DepreciacionDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroBienesDepreciables filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if(filtro.getIdBien() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depBienesDepreciablesFk.bdePk", filtro.getIdBien());
            criterios.add(criterio);
        }
        
        if(StringUtils.isNotBlank(filtro.getCodigoInventario())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depBienesDepreciablesFk.bdeCodigoInventario", filtro.getCodigoInventario().trim());
            criterios.add(criterio);
        }
        
       if(filtro.getAnio()!= null) {
            if(filtro.getMenorIgualAnio() != null && filtro.getMenorIgualAnio()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "depAnio", filtro.getAnio().intValue());
                criterios.add(criterio);
            } else  if(filtro.getMayorIgualAnio() != null && filtro.getMayorIgualAnio()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "depAnio", filtro.getAnio().intValue());
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depAnio", filtro.getAnio().intValue());
                criterios.add(criterio);
            }
        }

        if(filtro.getMes()!= null) {
            if(filtro.getMenorIgualMes() != null && filtro.getMenorIgualMes()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "depMes", filtro.getMes().intValue());
                criterios.add(criterio);
            } else  if(filtro.getMayorIgualMes() != null && filtro.getMayorIgualMes()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "depMes", filtro.getMes().intValue());
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depMes", filtro.getMes().intValue());
                criterios.add(criterio);
            }
        }
        
        if(filtro.getFuenteId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depFuenteFinanciamientoFk.ffiPk", filtro.getFuenteId());
            criterios.add(criterio);
        }
        
        if(filtro.getProyectoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "depProyectoFk.proPk", filtro.getProyectoId());
            criterios.add(criterio);
        }
        
        
        if (filtro.getFechaCalculoDepreciacionDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "depFechaCalculo", filtro.getFechaCalculoDepreciacionDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaCalculoDepreciacionHasta()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "depFechaCalculo", filtro.getFechaCalculoDepreciacionHasta());
            criterios.add(criterio);
        }

        if(filtro.getOperacion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "depOperacion", filtro.getOperacion());
            criterios.add(criterio);
        }
        
        if(filtro.getCompletado() != null) {
           MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "depProcesado", filtro.getCompletado());
           criterios.add(criterio); 
        }
        
        return criterios;
    }

    public List<SgAfDepreciacion> obtenerPorFiltro(FiltroBienesDepreciables filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAfDepreciacion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), filtro.getSecurityOperation() != null ? segDAO.obtenerOperacionesSeguridad(filtro.getSecurityOperation(), jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroBienesDepreciables filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfDepreciacion.class, criterio, filtro.getSeNecesitaDistinct(), filtro.getSecurityOperation() != null ? segDAO.obtenerOperacionesSeguridad(filtro.getSecurityOperation(), jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}