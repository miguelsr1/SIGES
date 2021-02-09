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
import javax.persistence.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente al presupuesto escolar
 *
 * @author jgiron
 */
public class PresupuestoEscolarDAO extends HibernateJpaDAOImp<SgPresupuestoEscolar, Integer> implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(PresupuestoEscolarDAO.class.getName());
    
    private EntityManager em;
    private JsonWebToken jwt;
    private SeguridadBean segBean;


    /**
     * Constructor de la clase
     *
     * @param em
     * @throws Exception
     */
    public PresupuestoEscolarDAO(EntityManager em, SeguridadBean segBean) throws Exception {
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

    /**
     * Genera una lista de criteria a partir de un filtro
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroPresupuestoEscolar filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getAnioFiscal() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pesAnioFiscalFk.aniAnio", filtro.getAnioFiscal());
            criterios.add(criterio);
        }

        if (filtro.getIdSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pesSedeFk.sedPk", filtro.getIdSede());
            criterios.add(criterio);
        }
        
        if (filtro.getSedesIds() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "pesSedeFk.sedPk", filtro.getSedesIds());
            criterios.add(criterio);
        }

        if (filtro.getPesEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pesEstado", filtro.getPesEstado());
            criterios.add(criterio);
        }

        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pesHabilitado", filtro.getHabilitado());
            criterios.add(criterio);
        }

        if (filtro.getPesSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pesSedeFk.sedPk", filtro.getPesSedeFk());
            criterios.add(criterio);
        }

        if (filtro.getPesPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pesPk", filtro.getPesPk());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve todos los presupuestos escolares que satisfacen un determinado
     * filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public List<SgPresupuestoEscolar> obtenerPorFiltro(FiltroPresupuestoEscolar filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();
            
            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.SEDE.name().equals(op.getAmbit())) {
                            break;
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
            return this.findEntityByCriteria(SgPresupuestoEscolar.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un determinado filtro.
     *
     * @param filtro
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroPresupuestoEscolar filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);
            
            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            }
            
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgPresupuestoEscolar.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Busca las áreas de inversión que son áreas principales y cumplen un
     * determinado filtro
     *
     * @param filtro
     * @return lista de áreas de inversión
     * @throws DAOGeneralException
     */
    public List<SgSede> buscarFinanzas(Long filtro) throws DAOGeneralException {
        try {
            Query areasInversion = em.createNativeQuery("select\n"
                    + "	sede.*,\n"
                    + "	ced.*\n"
                    + "from\n"
                    + "	seguridad.sg_usuario_rol usurol\n"
                    + "inner join seguridad.sg_roles roles on\n"
                    + "	roles.rol_pk = usurol.pur_rol_fk\n"
                    + "inner join seguridad.sg_contextos contexto on\n"
                    + "	contexto.con_pk = usurol.pur_contexto_fk\n"
                    + "inner join centros_educativos.sg_sedes sede on\n"
                    + "	sede.sed_pk = contexto.con_contexto_id\n"
                    + "inner join centros_educativos.sg_sedes_ced ced on\n"
                    + "	ced.sed_pk = sede.sed_pk\n"
                    + "where\n"
                    + "	roles.rol_codigo like '%FIN%'\n"
                    + "	and pur_usuario_fk = " + filtro, SgSede.class);
            return areasInversion.getResultList();
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
