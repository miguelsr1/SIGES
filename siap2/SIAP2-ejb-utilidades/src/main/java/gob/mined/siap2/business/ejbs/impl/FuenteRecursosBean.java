
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroCodiguera;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
@Stateless(name = "FuenteRecursosBean")
@LocalBean
public class FuenteRecursosBean {
    private static final Logger logger = Logger.getLogger(FuenteRecursosBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    public List<FuenteRecursos> getFuentesRecursosFiltro(FiltroCodiguera filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if(filtro.getFuenteId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteFinanciamiento.id", filtro.getFuenteId());
                criterios.add(criterio);
            }
            if (filtro.getHabilitado() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", filtro.getHabilitado());
                criterios.add(criterio);
            }
            if (filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigo", filtro.getCodigo().trim().toLowerCase());
                criterios.add(criterio);
            }
            if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getNombre().trim().toLowerCase());
                criterios.add(criterio);
            }
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(FuenteRecursos.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
}
