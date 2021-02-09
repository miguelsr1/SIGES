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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroCalificacionDiplomado;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionDiplomado;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
public class CalificacionDiplomadoDAO extends HibernateJpaDAOImp<SgCalificacionDiplomado, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public CalificacionDiplomadoDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.segDAO = segBean;
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroCalificacionDiplomado filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCalSedeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedPk", filtro.getCalSedeFk());
            criterios.add(criterio);
        }
        
        if (filtro.getCadTipoCalificacion()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadTipoCalificacion", filtro.getCadTipoCalificacion());
            criterios.add(criterio);
        }
        
        if (filtro.getCalAnioLectivoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadAnioLectivoFk.alePk", filtro.getCalAnioLectivoFk());
            criterios.add(criterio);
        }
        
        if (filtro.getCalNumeroPeriodo()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadNumeroPeriodo", filtro.getCalNumeroPeriodo());
            criterios.add(criterio);
        }
        if(filtro.getCalDepartamentoFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getCalDepartamentoFk());
            criterios.add(criterio);
        }
    
        if (filtro.getCalModuloDiplomadoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadModuloDiplomado.mdiPk", filtro.getCalModuloDiplomadoFk());
            criterios.add(criterio);
        }
        
        if (filtro.getCalDiplomadoFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cadModuloDiplomado.mdiDiplomado.dipPk", filtro.getCalDiplomadoFk());
            criterios.add(criterio);
        }
        
        
        return criterios;
    }

    public List<SgCalificacionDiplomado> obtenerPorFiltro(FiltroCalificacionDiplomado filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

             Boolean distinct = filtro.getSeNecesitaDistinct()!=null?filtro.getSeNecesitaDistinct():Boolean.FALSE;
            List<OperationSecurity> ops = null;
            if (securityOperation != null) {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct()!=null?filtro.getSeNecesitaDistinct():Boolean.FALSE;
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) 
                                || SeguridadAmbitos.SISTEMA_INTEGRADO.name().equals(op.getAmbit()) ||
                                SeguridadAmbitos.MODALIDADES_FLEXIBLES.name().equals(op.getAmbit()) ||
                                SeguridadAmbitos.IMPLEMENTADORA.name().equals(op.getAmbit()) ) {
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
            return this.findEntityByCriteria(SgCalificacionDiplomado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct,  ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroCalificacionDiplomado filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);


            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgCalificacionDiplomado.class, criterio, false,  securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
