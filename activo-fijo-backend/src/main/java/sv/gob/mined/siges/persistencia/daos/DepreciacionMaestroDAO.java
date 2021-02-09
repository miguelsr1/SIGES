/*
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
import sv.gob.mined.siges.enumerados.EnumEstadosProceso;
import sv.gob.mined.siges.filtros.FiltroBienesDepreciables;
import sv.gob.mined.siges.persistencia.entidades.SgAfDepreciacionMaestro;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
public class DepreciacionMaestroDAO extends HibernateJpaDAOImp<SgAfDepreciacionMaestro, Long> implements Serializable {
    private SeguridadDAO segDAO;
    private JsonWebToken jwt;
    public DepreciacionMaestroDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroBienesDepreciables filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        
        if(StringUtils.isNotBlank(filtro.getCodigoInventario())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dmaCodigoInventario", filtro.getCodigoInventario().trim());
            criterios.add(criterio);
        }
        
       if(filtro.getAnio()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dmaAnioProceso", filtro.getAnio().intValue());
            criterios.add(criterio);
        }

        if(filtro.getMes()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dmaMesProceso", filtro.getMes().intValue());
            criterios.add(criterio);
        }
        
        if(filtro.getFuenteId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dmaFuenteFinanciamientoFk.ffiPk", filtro.getFuenteId());
            criterios.add(criterio);
        }
        
        if(filtro.getProyectoId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dmaProyectoFk.proPk", filtro.getProyectoId());
            criterios.add(criterio);
        }

        if(filtro.getEstado() != null) {
            if(filtro.getDiferenteEstado() != null && filtro.getDiferenteEstado()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "dmaEstado", filtro.getEstado());
                criterios.add(criterio);
            } else {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dmaEstado", filtro.getEstado());
                criterios.add(criterio);
            }
        }
        
        if(filtro.getEstados() != null && !filtro.getEstados().isEmpty()) {
            List<CriteriaTO> estadosCriteria = new ArrayList();
            for(EnumEstadosProceso estado : filtro.getEstados()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dmaEstado", estado);
                estadosCriteria.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = estadosCriteria.toArray(new CriteriaTO[estadosCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);    
        }
        return criterios;
    }

    public List<SgAfDepreciacionMaestro> obtenerPorFiltro(FiltroBienesDepreciables filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = this.generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
             return this.findEntityByCriteria(SgAfDepreciacionMaestro.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    public Long obtenerTotalPorFiltro(FiltroBienesDepreciables filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAfDepreciacionMaestro.class, criterio, filtro.getSeNecesitaDistinct(), securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}