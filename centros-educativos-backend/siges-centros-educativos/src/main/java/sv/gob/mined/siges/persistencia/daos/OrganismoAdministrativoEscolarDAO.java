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
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.filtros.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolar;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class OrganismoAdministrativoEscolarDAO extends HibernateJpaDAOImp<SgOrganismoAdministracionEscolar, Integer> implements Serializable {

    private SeguridadDAO segDAO;
    private JsonWebToken jwt;

    public OrganismoAdministrativoEscolarDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = new SeguridadDAO(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroOrganismoAdministrativoEscolar filtro) {

        List<CriteriaTO> criterios = new ArrayList();


        if (filtro.getOaeSedeFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedPk", filtro.getOaeSedeFk());
            criterios.add(criterio);
        }
        if (filtro.getOaeDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedDireccion.dirDepartamento.depPk", filtro.getOaeDepartamento());
            criterios.add(criterio);
        }
        if (filtro.getOaeMunicipio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedDireccion.dirMunicipio.munPk", filtro.getOaeMunicipio());
            criterios.add(criterio);
        }
        if (filtro.getOaeEstados() != null && !filtro.getOaeEstados().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "oaeEstado", filtro.getOaeEstados());
            criterios.add(criterio);
        }
        if (filtro.getOaeEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeEstado", filtro.getOaeEstado());
            criterios.add(criterio);
        }
        if (filtro.getOaeSinEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "oaeEstado", filtro.getOaeSinEstado());
            criterios.add(criterio);
        }
        if (filtro.getOaeTipoSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedTipo", filtro.getOaeTipoSede());
            criterios.add(criterio);
        }else{
            List<CriteriaTO> datos = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedTipo", TipoSede.CED_OFI);
            datos.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.sedTipo", TipoSede.CED_PRI);
            datos.add(criterio);
            
            CriteriaTO[] arrCriterioOR = datos.toArray(new CriteriaTO[datos.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getOaeFechaVencimientoDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "oaeFechaVencimiento", filtro.getOaeFechaVencimientoDesde());
            criterios.add(criterio);
        }
        if (filtro.getOaeFechaVencimientoHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "oaeFechaVencimiento", filtro.getOaeFechaVencimientoHasta());
            criterios.add(criterio);
        }

        if (filtro.getOaeSubvencionado()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.priSubvencionada", filtro.getOaeSubvencionado());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getOaeNumeroAcuerdo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "oaeNumeroAcuerdo", filtro.getOaeNumeroAcuerdo());
            criterios.add(criterio);
        }
        
        if (filtro.getOaeTipoOAE() != null) {
            MatchCriteriaTO criterio =CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeSede.cedTipoOrganismoAdministrativo.toaPk", filtro.getOaeTipoOAE());
            criterios.add(criterio);
        }
        if(filtro.getOaeRenovarMiembroPadre()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaeMiembrosRenovadoPadreFk.oaePk", filtro.getOaeRenovarMiembroPadre());
            criterios.add(criterio);
        }
        if(filtro.getOaePk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "oaePk", filtro.getOaePk());
            criterios.add(criterio);
        }
     
        return criterios;
    }


    
    public List<SgOrganismoAdministracionEscolar> obtenerPorFiltro(FiltroOrganismoAdministrativoEscolar filtro, String securityOperation) throws DAOGeneralException {
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
          return this.findEntityByCriteria(SgOrganismoAdministracionEscolar.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroOrganismoAdministrativoEscolar filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            } 
            return this.countByCriteria(SgOrganismoAdministracionEscolar.class, criterio, false, securityOperation != null ? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()) : null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
