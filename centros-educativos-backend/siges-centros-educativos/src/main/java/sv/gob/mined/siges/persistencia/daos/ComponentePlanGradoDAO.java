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
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.filtros.FiltroComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;

public class ComponentePlanGradoDAO extends HibernateJpaDAOImp<SgComponentePlanGrado, Integer> implements Serializable {

    private EntityManager em;
    
    public ComponentePlanGradoDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroComponentePlanGrado filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getCpgComponentePlanEstudioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgComponentePlanEstudio.cpePk", filtro.getCpgComponentePlanEstudioPk());
            criterios.add(criterio);
        }

        if (filtro.getCpgGradoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgGrado.graPk", filtro.getCpgGradoPk());
            criterios.add(criterio);
        }

        if (filtro.getCpgOpcionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgOpcion.opcPk", filtro.getCpgOpcionPk());
            criterios.add(criterio);
        }

        if (filtro.getCpgPlanEstudioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgPlanEstudio.pesPk", filtro.getCpgPlanEstudioPk());
            criterios.add(criterio);
        }

        if (filtro.getCpgProgramaEducativoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgProgramaEducativo.pedPk", filtro.getCpgProgramaEducativoPk());
            criterios.add(criterio);
        }
        if (filtro.getCpgSeccionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgGrado.graServicioEducativo.sduSeccion.secPk", filtro.getCpgSeccionPk());
            criterios.add(criterio);
        }
        if (filtro.getCpgObjetoPromocion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgObjetoPromocion", filtro.getCpgObjetoPromocion());
            criterios.add(criterio);
        }
        if (filtro.getCpgPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgPk", filtro.getCpgPk());
            criterios.add(criterio);
        }
        if (filtro.getCpgSeccionExclusiva() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgExclusivoSeccion.secPk", filtro.getCpgSeccionExclusiva());
            criterios.add(criterio);
        }
        if (filtro.getCpgCalificacionIngresadaCE() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgCalificacionIngresadaCE", filtro.getCpgCalificacionIngresadaCE());
            criterios.add(criterio);
        }
        
        if (!StringUtils.isBlank(filtro.getCpeNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "cpgComponentePlanEstudio.cpeNombre", filtro.getCpeNombre());
            criterios.add(criterio);
        }
        if (filtro.getCpeNombreBusquedaList()!=null && !filtro.getCpeNombreBusquedaList().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cpgComponentePlanEstudio.cpeNombreBusqueda", filtro.getCpeNombreBusquedaList());
            criterios.add(criterio);
        }
        if (filtro.getCpgAgregandoSeccionExclusiva() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "cpgExclusivoSeccion.secPk", filtro.getCpgAgregandoSeccionExclusiva());

            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NULL, "cpgExclusivoSeccion", 1);

            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));

        } else {
            if (filtro.getCpgSeccionExclusiva() == null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "cpgComponentePlanEstudio.cpeTipo", TipoComponentePlanEstudio.AESS);
                criterios.add(criterio);
            }

        }

        if (filtro.getCodigosExternos() != null && !filtro.getCodigosExternos().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Integer cod : filtro.getCodigosExternos()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgComponentePlanEstudio.cpeCodigoExterno", cod);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getCpgTipoComponentePlanEstudio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgComponentePlanEstudio.cpeTipo", filtro.getCpgTipoComponentePlanEstudio());
            criterios.add(criterio);
        }

        if (filtro.getCpgEsTipoPaes() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgComponentePlanEstudio.cpeEsPaes", filtro.getCpgEsTipoPaes());
            criterios.add(criterio);
        }
        
        if(filtro.getCpgPkList()!=null && filtro.getCpgPkList().size()>0){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cpgPk", filtro.getCpgPkList());
            criterios.add(criterio);
        }
        
        //Caso especial en que componente puede ser objeto promocion o una cpe en particular
        if(filtro.getCpgObjetoPromocionOPrueba()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgObjetoPromocion", Boolean.TRUE);
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cpgComponentePlanEstudio.cpePk", filtro.getCpgObjetoPromocionOPrueba());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }
        

        return criterios;
    }

    public List<SgComponentePlanGrado> obtenerPorFiltro(FiltroComponentePlanGrado filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgComponentePlanGrado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroComponentePlanGrado filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgComponentePlanGrado.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
