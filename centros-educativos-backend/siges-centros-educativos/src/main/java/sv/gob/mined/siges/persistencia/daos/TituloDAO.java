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
import sv.gob.mined.siges.filtros.FiltroTitulo;
import sv.gob.mined.siges.persistencia.entidades.SgTitulo;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class TituloDAO extends HibernateJpaDAOImp<SgTitulo, Integer> implements Serializable {

    private EntityManager em;

    public TituloDAO(EntityManager em) throws Exception {
        super(em);
        this.em = em;
    }

    private List<CriteriaTO> generarCriteria(FiltroTitulo filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getTitHash())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "titHash", filtro.getTitHash());
            criterios.add(criterio);
        }
        if (filtro.getTitNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titEstudianteFk.estPersona.perNie", filtro.getTitNie());
            criterios.add(criterio);
        }
        if (filtro.getTitNoAnulada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "titAnulada", Boolean.TRUE);
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "titAnulada", 1);

            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
        }
        if(filtro.getTitAnulada()!=null){
            if(BooleanUtils.isTrue(filtro.getTitAnulada())){
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titAnulada", Boolean.TRUE);
                 criterios.add(criterio);
            }else{
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titAnulada", Boolean.FALSE);
                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "titAnulada", 1);
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
            }
            
        }
        if (filtro.getTitEstudiante() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titEstudianteFk.estPk", filtro.getTitEstudiante());
            criterios.add(criterio);
        }
        if (filtro.getTitSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSedeFk.sedPk", filtro.getTitSede());
            criterios.add(criterio);
        }
        if (filtro.getTitAnio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titAnio", filtro.getTitAnio());
            criterios.add(criterio);
        }
        if (filtro.getTitDefinicionTitulo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSolicitudImpresionFk.simDefinicionTitulo.dtiPk", filtro.getTitDefinicionTitulo());
            criterios.add(criterio);
        }
        if (filtro.getTitEstadoSolicitudImp() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSolicitudImpresionFk.simEstado", filtro.getTitEstadoSolicitudImp());
            criterios.add(criterio);
        }
        if (filtro.getTitSolicitudImpresion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSolicitudImpresionFk.simPk", filtro.getTitSolicitudImpresion());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getTitNombreEstudiante())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "titNombreEstudianteBusqueda",SofisStringUtils.normalizarParaBusqueda(filtro.getTitNombreEstudiante().trim()));
            criterios.add(criterio);
        }
        if(filtro.getTitPk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titPk", filtro.getTitPk());
            criterios.add(criterio);
        }
        if(filtro.getTitSeccionFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSolicitudImpresionFk.simSeccion.secPk", filtro.getTitSeccionFk());
            criterios.add(criterio);
        }
        
        if(filtro.getDepartamento()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSedeFk.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            criterios.add(criterio);
        }
        if(filtro.getMunicipio()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSedeFk.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            criterios.add(criterio);
        }
        if(filtro.getDui()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titDuiEstudiante", filtro.getDui());
            criterios.add(criterio);
        }
        if(filtro.getTitPlanEstudioFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSeccionFk.secPlanEstudio.pesPk", filtro.getTitPlanEstudioFk());
            criterios.add(criterio);
        }
        if(filtro.getTitGradoFk()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "titSeccionFk.secServicioEducativo.sduGrado.graPk", filtro.getTitGradoFk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgTitulo> obtenerPorFiltro(FiltroTitulo filtro) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgTitulo.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroTitulo filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgTitulo.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public SgTitulo obtenerPorId(Long id) throws DAOGeneralException {
        try {
            return em.find(SgTitulo.class, id);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
