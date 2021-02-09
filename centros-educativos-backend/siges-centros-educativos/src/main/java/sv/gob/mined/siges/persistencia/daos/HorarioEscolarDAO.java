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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.filtros.FiltroHorarioEscolar;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgHorarioEscolar;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class HorarioEscolarDAO extends HibernateJpaDAOImp<SgHorarioEscolar, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public HorarioEscolarDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroHorarioEscolar filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getHesPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesPk", filtro.getHesPk());
            criterios.add(criterio);
        }
        if (filtro.getHesCentroEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secServicioEducativo.sduSede.sedPk", filtro.getHesCentroEducativo());
            
            if (BooleanUtils.isTrue(filtro.getBuscarEnSedeAdscrita())) {

                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", filtro.getHesCentroEducativo());
                List<CriteriaTO> sedesCriteria = new ArrayList();
                sedesCriteria.add(criterio);
                sedesCriteria.add(criterio2);
                CriteriaTO[] arrCriterioOR = sedesCriteria.toArray(new CriteriaTO[sedesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);

            } else {
                criterios.add(criterio);
            }
            
            
        }
        if (filtro.getHesOrganizacionCurricular() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrganizacionCurricular.ocuPk", filtro.getHesOrganizacionCurricular());
            criterios.add(criterio);
        }
        if (filtro.getHesNivel() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getHesNivel());
            criterios.add(criterio);
        }
        if (filtro.getHesSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secPk", filtro.getHesSeccion());
            criterios.add(criterio);
        }
        if (filtro.getHesPlanEstudio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secPlanEstudio.pesPk", filtro.getHesPlanEstudio());
            criterios.add(criterio);
        }
        if (filtro.getHesGrado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesCeldasDiaHora.cdhComponentePlanGrado.cpgGrado.graPk", filtro.getHesGrado());
            criterios.add(criterio);
        }
        
        if (filtro.getHesAnio() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesSeccion.secAnioLectivo.aleAnio", filtro.getHesAnio());
            criterios.add(criterio);
        }
        
        if (filtro.getHesUnicoDocentePks() != null && !filtro.getHesUnicoDocentePks().isEmpty()){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "hesDocente.psePk", filtro.getHesUnicoDocentePks());
            criterios.add(criterio);
        }
             
        if (filtro.getHesPersonalSede() != null) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesDocente.psePk", filtro.getHesPersonalSede());
            serviciosCriteria.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesDocentes.cdoDocente.psePk", filtro.getHesPersonalSede());
            serviciosCriteria.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);

            CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);

        }
        if (filtro.getHesUnicoDocente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "hesDocente.psePk", filtro.getHesUnicoDocente());
            criterios.add(criterio);
        }
        return criterios;
    }

    public List<SgHorarioEscolar> obtenerPorFiltro(FiltroHorarioEscolar filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgHorarioEscolar.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false,securityOperation!=null? segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()):null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroHorarioEscolar filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgHorarioEscolar.class, criterio, false, segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
