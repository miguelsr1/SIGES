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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.filtros.FiltroLoteBienes;
import sv.gob.mined.siges.persistencia.entidades.SgAfLoteBienes;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class LoteBienesDAO extends HibernateJpaDAOImp<SgAfLoteBienes, Long> implements Serializable {

    
    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    
    public LoteBienesDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroLoteBienes filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if(filtro.getTipoUnidad() != null) {
            switch (filtro.getTipoUnidad()) {
                case UNIDAD_ADMINISTRATIVA:
                    MatchCriteriaTO criterioNotNullAD = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "lbiUnidadesAdministrativas.uadPk", 1);
                    criterios.add(criterioNotNullAD);
                    
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "lbiUnidadesAdministrativas.uadPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                case CENTRO_ESCOLAR:
                    MatchCriteriaTO criterioNotNullCE = CriteriaTOUtils.createMatchCriteriaTO(
                            MatchCriteriaTO.types.NOT_NULL, "lbiSede.sedPk", 1);
                    criterios.add(criterioNotNullCE);
                    
                    if(filtro.getUnidadAdministrativaId()!= null) {
                        MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                                MatchCriteriaTO.types.EQUALS, "lbiSede.sedPk", filtro.getUnidadAdministrativaId());
                        criterios.add(criterio);
                    }
                    break;
                default:
                    break;
            }
        }
        
        if(filtro.getEstado() != null) {
            if(filtro.getDiferenteEstado() != null && filtro.getDiferenteEstado()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NOT_EQUALS, "lbiEstado", filtro.getEstado());
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "lbiEstado", filtro.getEstado());
                criterios.add(criterio);
            }
        }
        
        if(filtro.getEstados() != null && !filtro.getEstados().isEmpty()) {
            List<CriteriaTO> estadosCriteria = new ArrayList();
            for(EnumEstadosProceso estado : filtro.getEstados()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lbiEstado", estado);
                estadosCriteria.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = estadosCriteria.toArray(new CriteriaTO[estadosCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);    
        }
        
        if (filtro.getFechaProcesoDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "lbiFechaFinalProcesamiento", LocalDateTime.of(filtro.getFechaProcesoDesde(), LocalTime.MIN));
            criterios.add(criterio);
        }
        
        if (filtro.getFechaProcesoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "lbiFechaFinalProcesamiento", LocalDateTime.of(filtro.getFechaProcesoHasta(), LocalTime.MAX));
            criterios.add(criterio);
        }
        
        
        
        return criterios;
    }

    public List<SgAfLoteBienes> obtenerPorFiltro(FiltroLoteBienes filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAfLoteBienes.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroLoteBienes filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfLoteBienes.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
