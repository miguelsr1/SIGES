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
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroDocumentoSistema;
import sv.gob.mined.siges.persistencia.entidades.SgDocumentoSistema;

public class DocumentoSistemaDAO extends HibernateJpaDAOImp<SgDocumentoSistema, Integer> implements Serializable {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(DocumentoSistemaDAO.class.getName());

    public DocumentoSistemaDAO(EntityManager em) {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroDocumentoSistema filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        

        if(!StringUtils.isBlank(filtro.getNombre())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "dsiNombre", filtro.getNombre());
            criterios.add(criterio);
        }

        if(!StringUtils.isBlank(filtro.getDescripcion())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "dsiDescripcion", filtro.getDescripcion());
            criterios.add(criterio);
        }
        
        if (filtro.getTipoDocumento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dsiTipoDocumento.tidPk", filtro.getTipoDocumento());
            criterios.add(criterio);
        }
        
        if (filtro.getCategoriaDocumento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dsiCategoriaDocumento", filtro.getCategoriaDocumento());
            criterios.add(criterio);
        }
        
        if (filtro.getSistemaIntegrado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "dsiSistemaIntegrado.sinPk", filtro.getSistemaIntegrado());
            criterios.add(criterio);
        }

        
        return criterios;
    }

    public List<SgDocumentoSistema> obtenerPorFiltro(FiltroDocumentoSistema filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgDocumentoSistema.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null,filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroDocumentoSistema filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgDocumentoSistema.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
