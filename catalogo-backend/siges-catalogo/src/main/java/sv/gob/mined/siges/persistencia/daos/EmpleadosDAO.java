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
import sv.gob.mined.siges.filtros.FiltroEmpleados;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.entidades.SgAfEmpleados;
import sv.gob.mined.siges.utils.ReflectionUtils;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class EmpleadosDAO extends HibernateJpaDAOImp<SgAfEmpleados, Long> implements Serializable {

    public EmpleadosDAO(EntityManager em) {
        super(em);
    }


    private List<CriteriaTO> generarCriteria(FiltroEmpleados filtro) {

        String campoHabilitado = ReflectionUtils.obtenerNombreCampoAnotado(SgAfEmpleados.class, AtributoHabilitado.class);

        List<CriteriaTO> criterios = new ArrayList();

        if(StringUtils.isNotBlank(filtro.getDui())) {
           MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "empPersonaFk.perDui", filtro.getDui().trim());
            criterios.add(criterio); 
        }
        
        if (filtro.getPerPk()!=null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "empPersonaFk.perPk", filtro.getPerPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "empPersonaFk.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "empPersonaFk.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "empPersonaFk.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "empPersonaFk.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "empPersonaFk.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "empPersonaFk.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "empPersonaFk.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }
        
        if(filtro.getUnidadAdministrativaId()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "empUnidadAdministrativaFk.uadPk", filtro.getUnidadAdministrativaId());
            criterios.add(criterio);
        }
        
        if (filtro.getHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, campoHabilitado, filtro.getHabilitado());
            criterios.add(criterio);
        }
        

        return criterios;
    }

    public List<SgAfEmpleados> obtenerPorFiltro(FiltroEmpleados filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgAfEmpleados.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), true, null,filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroEmpleados filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0); 
            }
            return this.countByCriteria(SgAfEmpleados.class, criterio, true, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    

}
