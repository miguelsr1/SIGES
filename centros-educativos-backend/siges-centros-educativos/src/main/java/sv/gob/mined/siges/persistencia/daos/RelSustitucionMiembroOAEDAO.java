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
import sv.gob.mined.siges.filtros.FiltroRelSustitucionMiembroOAE;
import sv.gob.mined.siges.persistencia.entidades.SgRelSustitucionMiembroOAE;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class RelSustitucionMiembroOAEDAO extends HibernateJpaDAOImp<SgRelSustitucionMiembroOAE, Integer> implements Serializable {

    private JsonWebToken jwt;

    public RelSustitucionMiembroOAEDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroRelSustitucionMiembroOAE filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getRsmSustitucionMiembroFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rsmSustitucionMiembroFk.smoPk", filtro.getRsmSustitucionMiembroFk());
            criterios.add(criterio);
        }

        if (filtro.getRsmMiembroSustituyenteFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rsmMiembroSustituyenteFk.poaPk", filtro.getRsmMiembroSustituyenteFk());
            criterios.add(criterio);
        }
        
        if (filtro.getRsmMiembroaSustituirFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rsmMiembroaSustituirFk.poaPk", filtro.getRsmMiembroaSustituirFk());
            criterios.add(criterio);
        }
        if (filtro.getRsmEstado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rsmSustitucionMiembroFk.smoEstado", filtro.getRsmEstado());
            criterios.add(criterio);
        }
        if (filtro.getRsmMiembrosSustituirPkList()!= null && !filtro.getRsmMiembrosSustituirPkList().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rsmMiembroaSustituirFk.poaPk",filtro.getRsmMiembrosSustituirPkList());
            criterios.add(criterio);
        }
        if (filtro.getRsmSustituirMiembroOAEList()!= null && !filtro.getRsmSustituirMiembroOAEList().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "rsmSustitucionMiembroFk.smoPk",filtro.getRsmSustituirMiembroOAEList());
            criterios.add(criterio);
        }
        if(filtro.getRsmOAEPk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rsmSustitucionMiembroFk.smoOaeFk.oaePk", filtro.getRsmOAEPk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgRelSustitucionMiembroOAE> obtenerPorFiltro(FiltroRelSustitucionMiembroOAE filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgRelSustitucionMiembroOAE.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroRelSustitucionMiembroOAE filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgRelSustitucionMiembroOAE.class);
    }

    public Long cantidadTotal(FiltroRelSustitucionMiembroOAE filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgRelSustitucionMiembroOAE.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
