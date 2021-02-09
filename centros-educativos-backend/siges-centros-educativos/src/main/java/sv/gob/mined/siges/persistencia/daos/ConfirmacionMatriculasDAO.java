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
import sv.gob.mined.siges.filtros.FiltroConfirmacionMatriculas;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionMatriculas;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ConfirmacionMatriculasDAO extends HibernateJpaDAOImp<SgConfirmacionMatriculas, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public ConfirmacionMatriculasDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroConfirmacionMatriculas filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getDepartamentoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoFk());
            criterios.add(criterio);
        }

        if (filtro.getMunicipioFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioFk());
            criterios.add(criterio);
        }

        if (filtro.getAnioLectivoFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaAnioLectivo.alePk", filtro.getAnioLectivoFk());
            criterios.add(criterio);
        }
        
        if (filtro.getCmaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaPk", filtro.getCmaPk());
            criterios.add(criterio);
        }

        if (filtro.getSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaSede.sedPk", filtro.getSedeFk());
            criterios.add(criterio);
        }
        
        if (filtro.getFirmada()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaFirmada", filtro.getFirmada());
            criterios.add(criterio);
        }
        
        if (filtro.getConfirmada() != null){
            if (filtro.getConfirmada()){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "cmaFechaConfirmacion", 1);
                criterios.add(criterio);
            } else {
                //las que deben ser firmadas
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "cmaFechaConfirmacion", 1);
                criterios.add(criterio);
            }
        
        }
        
        if (!StringUtils.isBlank(filtro.getFirmaTransactionId())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cmaFirmaTransactionId", filtro.getFirmaTransactionId());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgConfirmacionMatriculas> obtenerPorFiltro(FiltroConfirmacionMatriculas filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgConfirmacionMatriculas.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroConfirmacionMatriculas filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgConfirmacionMatriculas.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
