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
import sv.gob.mined.siges.filtros.FiltroCierreAnioLectivoSede;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgCierreAnioLectivoSede;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author bruno
 */
public class CierreAnioLectivoSedeDAO extends HibernateJpaDAOImp<SgCierreAnioLectivoSede, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    private SeguridadBean segDAO;

    public CierreAnioLectivoSedeDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.em = em;
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroCierreAnioLectivoSede filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCierreAnioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calPk", filtro.getCierreAnioPk());
            criterios.add(criterio);
        }
        
        if (filtro.getAnioLectivo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calAnioLectivo.aleAnio", filtro.getAnioLectivo());
            criterios.add(criterio);
        }
        
        if (filtro.getAnioLectivoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calAnioLectivo.alePk", filtro.getAnioLectivoId());
            criterios.add(criterio);
        }

        if (filtro.getSedeId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSede.sedPk", filtro.getSedeId());
            criterios.add(criterio);
        }
        
        if (filtro.getDepartamentoId() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
            criterios.add(criterio);
        }
        
        if (filtro.getMunicipioId() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
            criterios.add(criterio);
        }
        
        if (filtro.getFirmado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calFirmado", filtro.getFirmado());
            criterios.add(criterio);
        }
        
        if (filtro.getProcesoCierreCompleto() != null){
            if (filtro.getProcesoCierreCompleto()){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "calFechaCierre", 1);
                criterios.add(criterio);
            } else {
                //las que deben ser firmadas
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "calFechaCierre", 1);
                criterios.add(criterio);
            }
        
        }
        
        if (!StringUtils.isBlank(filtro.getFirmaTransactionId())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "calFirmaTransactionId", filtro.getFirmaTransactionId());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgCierreAnioLectivoSede> obtenerPorFiltro(FiltroCierreAnioLectivoSede filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgCierreAnioLectivoSede.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCierreAnioLectivoSede filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCierreAnioLectivoSede.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
