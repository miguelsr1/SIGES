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
import sv.gob.mined.siges.filtros.FiltroComponenteDocente;
import sv.gob.mined.siges.persistencia.entidades.SgComponenteDocente;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ComponenteDocenteDAO extends HibernateJpaDAOImp<SgComponenteDocente, Integer> implements Serializable {

    private JsonWebToken jwt;

    public ComponenteDocenteDAO(EntityManager em) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
    }

    private List<CriteriaTO> generarCriteria(FiltroComponenteDocente filtro) {

        List<CriteriaTO> criterios = new ArrayList();
        if (filtro.getCdoHorarioEscolar() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdoHorarioEscolar.hesPk", filtro.getCdoHorarioEscolar());
            criterios.add(criterio);
        }
        if (filtro.getCdoDocente() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdoDocente.psePk", filtro.getCdoDocente());
            criterios.add(criterio);
        }
        if (filtro.getCdoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdoPk", filtro.getCdoPk());
            criterios.add(criterio);
        }       
        if (filtro.getCdoAnio() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdoHorarioEscolar.hesSeccion.secAnioLectivo.aleAnio", filtro.getCdoAnio());
            criterios.add(criterio);
        }
        if (filtro.getCdoDocentes() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cdoDocente.psePk", filtro.getCdoDocentes());
            criterios.add(criterio);
        }
        if(filtro.getCdoSeccion()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdoHorarioEscolar.hesSeccion.secPk", filtro.getCdoSeccion());
            criterios.add(criterio);
        }
        if (filtro.getCdoSede()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdoHorarioEscolar.hesSeccion.secServicioEducativo.sduSede.sedPk", filtro.getCdoSede());
            
            if (BooleanUtils.isTrue(filtro.getBuscarEnSedeAdscrita())) {

                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cdoHorarioEscolar.hesSeccion.secServicioEducativo.sduSede.sedSedeAdscritaDe.sedPk", filtro.getCdoSede());
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

        return criterios;
    }

    public List<SgComponenteDocente> obtenerPorFiltro(FiltroComponenteDocente filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgComponenteDocente.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroComponenteDocente filtro) throws DAOGeneralException {
        return cantidadTotal(filtro, SgComponenteDocente.class);
    }

    public Long cantidadTotal(FiltroComponenteDocente filtro, Class clase) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgComponenteDocente.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
