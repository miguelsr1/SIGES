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
import javax.json.JsonNumber;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.filtros.FiltroEspecialidadesPersonalAlAplicar;
import sv.gob.mined.siges.persistencia.entidades.SgEspecialidadesPersonalAlAplicar;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class EspecialidadPersonalAlAplicarDAO extends HibernateJpaDAOImp<SgEspecialidadesPersonalAlAplicar, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public EspecialidadPersonalAlAplicarDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroEspecialidadesPersonalAlAplicar filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAplicacionPlazaFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "epaAplicacionPlazaFk.aplPk", filtro.getAplicacionPlazaFk());
            criterios.add(criterio);
        }
  
        if (filtro.getAplicacionesPlazaFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "epaAplicacionPlazaFk.aplPk", filtro.getAplicacionesPlazaFk());
            criterios.add(criterio);
        }
        if(filtro.getEpaCum()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "epaCum", filtro.getEpaCum());
            criterios.add(criterio);
        }
        if (filtro.getEpaFechaGraduacionDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "epaFechaGraduacion", filtro.getEpaFechaGraduacionDesde());
            criterios.add(criterio);
        }
        if (filtro.getEpaFechaGraduacionHasta()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "epaFechaGraduacion", filtro.getEpaFechaGraduacionHasta());
            criterios.add(criterio);
        }
        if(filtro.getEspecialidades()!=null && !filtro.getEspecialidades().isEmpty()){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "epaEspecialidad.espPk", filtro.getEspecialidades());
            criterios.add(criterio);
        }
        

        return criterios;
    }

    public List<SgEspecialidadesPersonalAlAplicar> obtenerPorFiltro(FiltroEspecialidadesPersonalAlAplicar filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgEspecialidadesPersonalAlAplicar.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEspecialidadesPersonalAlAplicar filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEspecialidadesPersonalAlAplicar.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
