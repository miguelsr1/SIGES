/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroEdificio;
import sv.gob.mined.siges.persistencia.entidades.SgEdificio;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class EdificioDAO extends HibernateJpaDAOImp<SgEdificio, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    public EdificioDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroEdificio filtro) {

        List<CriteriaTO> criterios = new ArrayList();        
     
        if(filtro.getDepartamentoId() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
            criterios.add(criterio);
        }
        if(filtro.getMunicipioId()!= null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
            criterios.add(criterio);
        }
        if(filtro.getSedeId()!= null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisSedeFk.sedPk", filtro.getSedeId());
            criterios.add(criterio);
        }
        if(filtro.getInmuebleId()!= null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisPk", filtro.getInmuebleId());
            criterios.add(criterio);
        }
        if(!StringUtils.isBlank(filtro.getCodigo())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "ediCodigo", filtro.getCodigo());
            criterios.add(criterio);
        }
        if (filtro.getUnidadAdministrativa()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediInmuebleSedeFk.tisAfUnidadAdministrativa.uadPk", filtro.getUnidadAdministrativa());
            criterios.add(criterio);
        }
        if(filtro.getRelInmuebleUnidadResFk()!=null){
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "ediRelInmuebleUnidadResp.riuPk", filtro.getRelInmuebleUnidadResFk());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgEdificio> obtenerPorFiltro(FiltroEdificio filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            Boolean distinct = filtro.getSeNecesitaDistinct()!=null?filtro.getSeNecesitaDistinct():Boolean.FALSE;
            
            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct()!=null?filtro.getSeNecesitaDistinct():Boolean.FALSE;
                            break;
                        }
                        if (SeguridadAmbitos.DEPARTAMENTAL.name().equals(op.getAmbit()) || 
                                SeguridadAmbitos.SEDE.name().equals(op.getAmbit()) || 
                                SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) ||                                
                                SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())) {
                            distinct = Boolean.TRUE;
                        }
                    }
                }
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgEdificio.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops,  filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    public Long obtenerTotalPorFiltro(FiltroEdificio filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEdificio.class, criterio,  false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }   
    
}
