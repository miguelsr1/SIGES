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
import sv.gob.mined.siges.filtros.FiltroAula;
import sv.gob.mined.siges.persistencia.entidades.SgAula;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class AulaDAO extends HibernateJpaDAOImp<SgAula, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    public AulaDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroAula filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getDepartamentoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisDireccion.dirDepartamento.depPk", filtro.getDepartamentoId());
            criterios.add(criterio);
        }
        if (filtro.getMunicipioId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisDireccion.dirMunicipio.munPk", filtro.getMunicipioId());
            criterios.add(criterio);
        }
        if (filtro.getSedeId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisSedeFk.sedPk", filtro.getSedeId());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "aulCodigo", filtro.getCodigo());
            criterios.add(criterio);
        }
        if (filtro.getInmueblePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisPk", filtro.getInmueblePk());
            criterios.add(criterio);
        }
        if (filtro.getEdificioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediPk", filtro.getEdificioPk());
            criterios.add(criterio);
        }
        if (filtro.getUnidadAdministrativa()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aulEdificioFk.ediInmuebleSedeFk.tisAfUnidadAdministrativa.uadPk", filtro.getUnidadAdministrativa());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgAula> obtenerPorFiltro(FiltroAula filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
             Boolean distinct = Boolean.FALSE;
             
             List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct  = Boolean.FALSE;
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
            return this.findEntityByCriteria(SgAula.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAula filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAula.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
