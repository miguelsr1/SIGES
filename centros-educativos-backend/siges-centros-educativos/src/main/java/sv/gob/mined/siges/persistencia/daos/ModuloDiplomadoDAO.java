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
import sv.gob.mined.siges.filtros.FiltroModuloDiplomado;
import sv.gob.mined.siges.persistencia.entidades.SgModuloDiplomado;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ModuloDiplomadoDAO extends HibernateJpaDAOImp<SgModuloDiplomado, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;

    public ModuloDiplomadoDAO(EntityManager em) throws Exception {
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

    private List<CriteriaTO> generarCriteria(FiltroModuloDiplomado filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getDiplomadoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mdiDiplomado.dipPk", filtro.getDiplomadoFk());
            criterios.add(criterio);
        }
        if (filtro.getMdiPk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "mdiPk", filtro.getMdiPk());
            criterios.add(criterio);
        }
       

        return criterios;
    }

    public List<SgModuloDiplomado> obtenerPorFiltro(FiltroModuloDiplomado filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgModuloDiplomado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroModuloDiplomado filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgModuloDiplomado.class);
    }

    public Long cantidadTotal(FiltroModuloDiplomado filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgModuloDiplomado.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
