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
import sv.gob.mined.siges.filtros.FiltroAsignacionPerfilPersonal;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgAsignacionPerfilPersonal;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class AsignacionPerfilPersonalDAO extends HibernateJpaDAOImp<SgAsignacionPerfilPersonal, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public AsignacionPerfilPersonalDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroAsignacionPerfilPersonal filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getApeSedeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "appAsignacionPerfilFk.apeSedeFk.sedPk", filtro.getApeSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getApeDepartamentoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "appAsignacionPerfilFk.apeSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getApeDepartamentoFk());
            criterios.add(criterio);
        }
        if (filtro.getApePersonalFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "appPersonalFk.psePk", filtro.getApePersonalFk());
            criterios.add(criterio);
        }
        if (filtro.getAppAsignacionPerfilFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "appAsignacionPerfilFk.apePk", filtro.getAppAsignacionPerfilFk());
            criterios.add(criterio);
        }
        if(filtro.getAppPk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "appPk", filtro.getAppPk());
            criterios.add(criterio);
        }
        
        if(filtro.getAppPersonaFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "appPersonalFk.psePersona.perPk", filtro.getAppPersonaFk());
            criterios.add(criterio);
        }
       
        return criterios;
    }

    public List<SgAsignacionPerfilPersonal> obtenerPorFiltro(FiltroAsignacionPerfilPersonal filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAsignacionPerfilPersonal.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAsignacionPerfilPersonal filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAsignacionPerfilPersonal.class, criterio, false,null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
