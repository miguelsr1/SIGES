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
import sv.gob.mined.siges.filtros.FiltroSolicitudPlaza;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudPlaza;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class SolicitudPlazaDAO extends HibernateJpaDAOImp<SgSolicitudPlaza, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public SolicitudPlazaDAO(EntityManager em, SeguridadBean segBean)  throws Exception {
        super(em);
        this.segDAO = segBean;
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroSolicitudPlaza filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getDepartamento()!=null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splSedeSolicitante.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            criterios.add(criterio);
        }

        if (filtro.getMunicipio()!=null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splSedeSolicitante.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            criterios.add(criterio);
        }

        if (filtro.getTipo()!=null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splTipoPlaza", filtro.getTipo());
            criterios.add(criterio);
        }

        if (filtro.getNivel() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splNivel.nivPk", filtro.getNivel());
            criterios.add(criterio);
        }
        
        if (filtro.getJornada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splJornada.jlaPk", filtro.getJornada());
            criterios.add(criterio);
        }
        
        if (filtro.getModalidadAtencion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splModalidadAtencion.matPk", filtro.getModalidadAtencion());
            criterios.add(criterio);
        }
        
        if (filtro.getModalidadEducativa() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splModalidadEducativa.modPk", filtro.getModalidadEducativa());
            criterios.add(criterio);
        }
        
        if (filtro.getOpcion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splOpcion.opcPk", filtro.getOpcion());
            criterios.add(criterio);
        }
        
        if (filtro.getEspecialidad() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splEspecialidad.espPk", filtro.getEspecialidad());
            criterios.add(criterio);
        }
        
        if (filtro.getEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splEstado", filtro.getEstado());
            criterios.add(criterio);
        }
        
        if (filtro.getCodigo()!=null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "splCodigo", filtro.getCodigo());
            criterios.add(criterio);
        }
        
        if (filtro.getSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splSedeSolicitante.sedPk", filtro.getSede());
            criterios.add(criterio);
        }
                
        if (filtro.getPostulacionInicio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "splPostulacionFin", filtro.getPostulacionInicio()); 
            criterios.add(criterio);
        }

        if (filtro.getPostulacionFin()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "splPostulacionInicio", filtro.getPostulacionFin());
            criterios.add(criterio);
        }
        if(!StringUtils.isBlank(filtro.getAplicacionCodigoUsuario())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "splAplicacionesPlaza.aplPersonal.psePersona.perDui", filtro.getAplicacionCodigoUsuario());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        
        return criterios;
    }

    public List<SgSolicitudPlaza> obtenerPorFiltro(FiltroSolicitudPlaza filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            
            Boolean distinct = filtro.getSeNecesitaDistinct()!=null?filtro.getSeNecesitaDistinct():Boolean.FALSE;

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgSolicitudPlaza.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroSolicitudPlaza filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSolicitudPlaza.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
