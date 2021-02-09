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
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.filtros.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.persistencia.entidades.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class PersonaOrganismoAdministrativoDAO extends HibernateJpaDAOImp<SgPersonaOrganismoAdministracion, Integer> implements Serializable {

    private EntityManager em;

    public PersonaOrganismoAdministrativoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroPersonaOrganismoAdministrativo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getPoaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaPk", filtro.getPoaPk());
            criterios.add(criterio);
        }
        if (filtro.getPoaOrganismoAdministrativoEscolar() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaOrganismoAdministrativoEscolar.oaePk", filtro.getPoaOrganismoAdministrativoEscolar());
            criterios.add(criterio);
        }
        
        if (BooleanUtils.isTrue(filtro.getPoaHabilitado())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaHabilitado", filtro.getPoaHabilitado());
            criterios.add(criterio);
        }else{
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaHabilitado", BooleanUtils.toBoolean(filtro.getPoaHabilitado()));
            criterios.add(criterio);
        }
        
        if (filtro.getPoaCargoOAEPk() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaCargo.coaPk", filtro.getPoaCargoOAEPk());
            criterios.add(criterio);
        }
        
        
        if (filtro.getPerPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaPersona.perPk", filtro.getPerPk());
            criterios.add(criterio);
        }
        
         if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "poaPersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "poaNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }
   

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "poaPersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "poaPersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "poaPersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "poaPersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "poaPersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.START_WITH, "poaPersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }
        
         //IDENTIFICACIONES
        List<CriteriaTO> indentificacionesOR = new ArrayList();
        if (filtro.getNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaPersona.perNie", filtro.getNie());
            indentificacionesOR.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getDui())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaPersona.perDui", filtro.getDui());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaDui", SofisStringUtils.normalizarParaBusqueda(filtro.getDui()));
            indentificacionesOR.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }

        if (!StringUtils.isBlank(filtro.getNit())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaPersona.perNit", filtro.getNit());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaNit", SofisStringUtils.normalizarParaBusqueda(filtro.getNit()));
            indentificacionesOR.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }
     
        if (filtro.getNip() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaPersona.perNip", filtro.getNip());
            indentificacionesOR.add(criterio);
        }

        if (filtro.getCun() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaPersona.perCun", filtro.getCun());
            indentificacionesOR.add(criterio);
        }
      


        if (!indentificacionesOR.isEmpty()) {
            CriteriaTO[] arrCriterioOR = indentificacionesOR.toArray(new CriteriaTO[indentificacionesOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if(filtro.getSedeId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poaOrganismoAdministrativoEscolar.oaeSede.sedPk", filtro.getSedeId());
            criterios.add(criterio);
        }
        
        if(filtro.getCodigosTOA() != null && !filtro.getCodigosTOA().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "poaCargo.coaTiposOrganismoAdministrativo.toaCodigo", filtro.getCodigosTOA());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgPersonaOrganismoAdministracion> obtenerPorFiltro(FiltroPersonaOrganismoAdministrativo filtro) throws DAOGeneralException {
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
          return this.findEntityByCriteria(SgPersonaOrganismoAdministracion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());

        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroPersonaOrganismoAdministrativo filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgPersonaOrganismoAdministracion.class);
    }

    public Long cantidadTotal(FiltroPersonaOrganismoAdministrativo filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgPersonaOrganismoAdministracion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
