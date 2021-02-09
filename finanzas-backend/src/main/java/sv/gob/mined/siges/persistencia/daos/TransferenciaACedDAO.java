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
import sv.gob.mined.siges.filtros.FiltroTransferenciaACed;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgTransferenciaACed;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 * DAO correspondiente a las transferencias a los centros educativos
 *
 * @author jgiron
 */
public class TransferenciaACedDAO extends HibernateJpaDAOImp<SgTransferenciaACed, Integer> implements Serializable {


    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    /**
     * *
     * Constructor de la clase
     *
     * @param em
     * @param segBean
     * @throws Exception
     */
    public TransferenciaACedDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        if (jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS) != null) {
            this.setMaxResultadosPermitidos(((JsonNumber) jwt.getClaim(Constantes.JWT_CLAIM_MAX_RESULTADOS_PERMITIDOS)).longValue());
        }
        if (jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO) != null) {
            this.setIncluirCamposRequerido(jwt.getClaim(Constantes.JWT_CLAIM_INCLUIR_CAMPOS_REQUERIDO));
        }
        this.segDAO = segBean;
    }

    /**
     * Genera una lista de criteria a partir un filtro.
     *
     * @param filtro
     * @return
     */
    private List<CriteriaTO> generarCriteria(FiltroTransferenciaACed filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if (filtro.getTacPk()!= null && filtro.getTacPk()>0) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacPk", filtro.getTacPk());
            criterios.add(criterio);
        }
        
        if (filtro.getTacEstado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacEstado", filtro.getTacEstado());
            criterios.add(criterio);
        }

        if (filtro.getDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacCedFk.sedDireccion.dirDepartamento", filtro.getDepartamento());
            criterios.add(criterio);
        }

        if (filtro.getTacSedePk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacCedFk.sedPk", filtro.getTacSedePk());
            criterios.add(criterio);
        }
        
        if(filtro.getTacTransferenciaFk()!=null && filtro.getTacTransferenciaFk()>0){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacTransferenciaFk.tcId", filtro.getTacTransferenciaFk());
            criterios.add(criterio);
        }
        
        if (filtro.getComponente()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacTransferenciaFk.componente.cpeId", filtro.getComponente());
            criterios.add(criterio);
        }

        if (filtro.getSubComponente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacTransferenciaFk.subComponente.gesId", filtro.getSubComponente());
            criterios.add(criterio);
        }


        if (filtro.getEstadoOae() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacCedFk.oaes.oaeEstado", filtro.getEstadoOae());
            criterios.add(criterio);
        }

        if (filtro.getTacTransferenciaDireccionDepFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacTransferenciaDireccionDepFk.tddPk", filtro.getTacTransferenciaDireccionDepFk());
            criterios.add(criterio);
        }

        if (filtro.getAnioFiscal() != null && filtro.getAnioFiscal() > 0) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacCedFk.sedPresupuestos.pesAnioFiscalFk.aniAnio", filtro.getAnioFiscal());
            criterios.add(criterio);
        }
        
        if(filtro.getSsTransferencia()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tacTransferenciaFk.tcTransferencia", filtro.getSsTransferencia());
            criterios.add(criterio);
        }

        return criterios;
    }

    /**
     * Devuelve una lista de transferencias a Centros educativos que satisfacen
     * un filtro en un contexto de seguridad dado.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public List<SgTransferenciaACed> obtenerPorFiltro(FiltroTransferenciaACed filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgTransferenciaACed.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    /**
     * Devuelve la cantidad de elementos que satisfacen un filtro dado en un
     * contexto de seguridad.
     *
     * @param filtro
     * @param securityOperation
     * @return
     * @throws DAOGeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroTransferenciaACed filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgTransferenciaACed.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
}
