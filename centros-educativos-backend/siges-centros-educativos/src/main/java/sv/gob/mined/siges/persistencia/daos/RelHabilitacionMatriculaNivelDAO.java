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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgRelHabilitacionMatriculaNivel;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class RelHabilitacionMatriculaNivelDAO extends HibernateJpaDAOImp<SgRelHabilitacionMatriculaNivel, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private EntityManager em;
    private JsonWebToken jwt;

    public RelHabilitacionMatriculaNivelDAO(EntityManager em, SeguridadBean seg) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
        this.segDAO = seg;
    }

    private List<CriteriaTO> generarCriteria(FiltroRelHabilitacionMatriculaNivel filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getNivelAndNullOrNivelAndModAtSubMod() == null) {
            if (filtro.getRhnNivelFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnNivelFk.nivPk", filtro.getRhnNivelFk());
                criterios.add(criterio);
            }

            if (filtro.getRhnModalidadAtencionFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnModalidadAtencionFk.matPk", filtro.getRhnModalidadAtencionFk());
                criterios.add(criterio);
            }
            if (filtro.getRhnSubmodalidadFk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnSubmodaliadadFk.smoPk", filtro.getRhnSubmodalidadFk());
                criterios.add(criterio);
            }
        } else {
            if (filtro.getRhnNivelFk() != null) {
                List<CriteriaTO> variablesOR = new ArrayList();

                List<CriteriaTO> variablesAND = new ArrayList();
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnNivelFk.nivPk", filtro.getRhnNivelFk());
                variablesAND.add(criterio);
                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "rhnModalidadAtencionFk", 1);
                variablesAND.add(criterio);
                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "rhnSubmodaliadadFk", 1);
                variablesAND.add(criterio);

                CriteriaTO[] arrCriterioAND = variablesAND.toArray(new CriteriaTO[variablesAND.size()]);
                CriteriaTO criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];

                variablesOR.add(criterioAND);
         
                variablesAND = new ArrayList();
                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnNivelFk.nivPk", filtro.getRhnNivelFk());
                variablesAND.add(criterio);
                if (filtro.getRhnModalidadAtencionFk() != null) {
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnModalidadAtencionFk.matPk", filtro.getRhnModalidadAtencionFk());
                    variablesAND.add(criterio);
                }
                if (filtro.getRhnSubmodalidadFk() != null) {
                    criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnSubmodaliadadFk.smoPk", filtro.getRhnSubmodalidadFk());
                    variablesAND.add(criterio);
                }

                arrCriterioAND = variablesAND.toArray(new CriteriaTO[variablesAND.size()]);
                criterioAND = arrCriterioAND.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioAND) : arrCriterioAND[0];

                variablesOR.add(criterioAND);
                
                CriteriaTO[] arrCriterioOR = variablesOR.toArray(new CriteriaTO[variablesOR.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }

        }
        
        if (filtro.getRhnSedeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnHabilitacionPeriodoMatriculaFk.hmpSedeFk.sedPk", filtro.getRhnSedeFk());
            criterios.add(criterio);
        }

        if (filtro.getRhnFecha() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "rhnHabilitacionPeriodoMatriculaFk.hmpFechaHasta", filtro.getRhnFecha());
            criterios.add(criterio);

            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "rhnHabilitacionPeriodoMatriculaFk.hmpFechaDesde", filtro.getRhnFecha());
            criterios.add(criterio);
        }
        
        if (filtro.getRhnEstadoHabilitacion()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rhnHabilitacionPeriodoMatriculaFk.hmpEstado", filtro.getRhnEstadoHabilitacion());
            criterios.add(criterio);
        }
        if(filtro.getRhnHabilitacionPeriodosPkList()!=null && filtro.getRhnHabilitacionPeriodosPkList().size()>0){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rhnHabilitacionPeriodoMatriculaFk.hmpPk", filtro.getRhnHabilitacionPeriodosPkList());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgRelHabilitacionMatriculaNivel> obtenerPorFiltro(FiltroRelHabilitacionMatriculaNivel filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelHabilitacionMatriculaNivel.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroRelHabilitacionMatriculaNivel filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelHabilitacionMatriculaNivel.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public SgRelHabilitacionMatriculaNivel obtenerPorId(Long id) throws DAOGeneralException {
        try {
            return em.find(SgRelHabilitacionMatriculaNivel.class, id);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
