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
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroSelloFirma;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirma;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class SelloFirmaDAO extends HibernateJpaDAOImp<SgSelloFirma, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public SelloFirmaDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroSelloFirma filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getSedPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiSede.sedPk", filtro.getSedPk());
            criterios.add(criterio);
        }
        if (filtro.getSfiHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiHabilitado", filtro.getSfiHabilitado());
            criterios.add(criterio);
        }
        if (filtro.getSfiTipoRepresentantePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiTipoRepresentante.trePk", filtro.getSfiTipoRepresentantePk());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSfiTipoRepresentanteCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiTipoRepresentante.treCodigo", filtro.getSfiTipoRepresentanteCodigo());
            criterios.add(criterio);
        }
        if (filtro.getSfiHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiHabilitado", filtro.getSfiHabilitado());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSfiNombreCompletoPersona())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE,
                    "sfiPersona.perNombreBusqueda",
                    SofisStringUtils.normalizarParaBusqueda(filtro.getSfiNombreCompletoPersona()));
            criterios.add(criterio);
        }
        if(filtro.getSfiSedes()!=null && !filtro.getSfiSedes().isEmpty()){
            List<CriteriaTO> sedeCriteria = new ArrayList();
            for (Long sedPk : filtro.getSfiSedes()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiSede.sedPk", sedPk);
                sedeCriteria.add(criterio);
            }

            if (!sedeCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = sedeCriteria.toArray(new CriteriaTO[sedeCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }
        if(filtro.getArchivoVacio()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "sfiFirmaSello", 1);
            criterios.add(criterio);
        }
        if(filtro.getSfiPersonaPk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiPersona.perPk", filtro.getSfiPersonaPk());
            criterios.add(criterio);
        }
        if(filtro.getSfiDui()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sfiPersona.perDui", filtro.getSfiDui());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgSelloFirma> obtenerPorFiltro(FiltroSelloFirma filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgSelloFirma.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroSelloFirma filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSelloFirma.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
