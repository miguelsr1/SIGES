/**
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
import java.util.logging.Logger;
import javax.json.JsonNumber;
import javax.persistence.EntityManager;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.filtros.FiltroSolicitudesOAE;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSolDeshabilitarPerJur;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class SolicitudOAEDAO extends HibernateJpaDAOImp<SgSolDeshabilitarPerJur, Integer> implements Serializable {

    private EntityManager em;
    private JsonWebToken jwt;
    private SeguridadBean segBean;
    private static final Logger LOGGER = Logger.getLogger(SolicitudOAEDAO.class.getName());

    public SolicitudOAEDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.em = em;
        this.segBean = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroSolicitudesOAE filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getSedeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedPk", filtro.getSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getDpjOaeFk()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaePk", filtro.getDpjOaeFk());
            criterios.add(criterio);
        }
        if (filtro.getDepartamento()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            criterios.add(criterio);
        }
        if (filtro.getMunicipio()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            criterios.add(criterio);
        }
        if (filtro.getEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjEstado", filtro.getEstado());
            criterios.add(criterio);
        }
        if (filtro.getTipoSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedTipo", filtro.getTipoSede());
            criterios.add(criterio);
        }else{
            List<CriteriaTO> datos = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedTipo", TipoSede.CED_OFI);
            datos.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dpjOaeFk.oaeSede.sedTipo", TipoSede.CED_PRI);
            datos.add(criterio);
            
            CriteriaTO[] arrCriterioOR = datos.toArray(new CriteriaTO[datos.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getFechaDesdeTime()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "dpjUltModFecha", filtro.getFechaDesdeTime());
            criterios.add(criterio);
        }
        if (filtro.getFechaHastaTime()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "dpjUltModFecha", filtro.getFechaHastaTime());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgSolDeshabilitarPerJur> obtenerPorFiltro(FiltroSolicitudesOAE filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            
            List<OperationSecurity> ops = null;
            Boolean distinct = Boolean.FALSE;
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = Boolean.FALSE;
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) || SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())
                                || SeguridadAmbitos.MODALIDADES_FLEXIBLES.name().equals(op.getAmbit()) || SeguridadAmbitos.IMPLEMENTADORA.name().equals(op.getAmbit())) {
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
            return this.findEntityByCriteria(SgSolDeshabilitarPerJur.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroSolicitudesOAE filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);
            
            List<OperationSecurity> ops = null;
            Boolean distinct = Boolean.FALSE;
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = Boolean.FALSE;
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) || SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())
                                || SeguridadAmbitos.MODALIDADES_FLEXIBLES.name().equals(op.getAmbit()) || SeguridadAmbitos.IMPLEMENTADORA.name().equals(op.getAmbit())) {
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
            return this.countByCriteria(SgSolDeshabilitarPerJur.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
