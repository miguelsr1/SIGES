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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.filtros.FiltroLiquidacionProyectos;
import sv.gob.mined.siges.persistencia.entidades.SgAfLiquidacionProyecto;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class LiquidacionProyectoDAO extends HibernateJpaDAOImp<SgAfLiquidacionProyecto, Long> implements Serializable {
    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    public LiquidacionProyectoDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroLiquidacionProyectos filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if(filtro.getFuenteId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lprFuenteFinanciamientoOrigenFk.ffiPk", filtro.getFuenteId());
            criterios.add(criterio);
        }
        
        if(filtro.getProyectoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lprProyectoFk.proPk", filtro.getProyectoId());
            criterios.add(criterio);
        }
        
        if (filtro.getFechaLiquidacionDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "lprFechaLiquidacion", filtro.getFechaLiquidacionDesde());
            criterios.add(criterio);
        }
        if (filtro.getFechaLiquidacionHasta()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "lprFechaLiquidacion", filtro.getFechaLiquidacionHasta());
            criterios.add(criterio);
        }

        if(filtro.getEstado() != null) {
            if(filtro.getDiferenteEstado() != null && filtro.getDiferenteEstado()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "lprEstado", filtro.getEstado());
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lprEstado", filtro.getEstado());
                criterios.add(criterio);
            }
        }
        if(filtro.getEstados() != null && !filtro.getEstados().isEmpty()) {
            List<CriteriaTO> estadosCriteria = new ArrayList();
            for(EnumEstadosProceso estado : filtro.getEstados()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lprEstado", estado);
                estadosCriteria.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = estadosCriteria.toArray(new CriteriaTO[estadosCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);    
        }
        return criterios;
    }

    public List<SgAfLiquidacionProyecto> obtenerPorFiltro(FiltroLiquidacionProyectos filtro) throws DAOGeneralException {
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
            //return this.findEntityByCriteria(SgAfLiquidacionProyecto.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
            return this.findEntityByCriteria(SgAfLiquidacionProyecto.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroLiquidacionProyectos filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfLiquidacionProyecto.class, criterio, false, null);
            //return this.countByCriteria(SgAfLiquidacionProyecto.class, criterio, filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}