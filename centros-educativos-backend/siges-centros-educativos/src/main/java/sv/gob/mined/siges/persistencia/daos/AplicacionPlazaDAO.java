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
import sv.gob.mined.siges.filtros.FiltroAplicacionPlaza;
import sv.gob.mined.siges.persistencia.entidades.SgAplicacionPlaza;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivosSeleccionPLaza;

public class AplicacionPlazaDAO extends HibernateJpaDAOImp<SgAplicacionPlaza, Integer> implements Serializable {
    
    private EntityManager em;

    public AplicacionPlazaDAO(EntityManager em) {
        super(em);
        this.em = em;
    }
     
     private List<CriteriaTO> generarCriteria(FiltroAplicacionPlaza filtro) {
         
        List<CriteriaTO> criterios = new ArrayList();
        
        if(!StringUtils.isBlank(filtro.getAplCodigoUsuario())){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS_ILIKE, "aplCodigoUsuario", filtro.getAplCodigoUsuario());
            criterios.add(criterio);
        }    
        if(filtro.getAplEstado()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplEstado", filtro.getAplEstado());
            criterios.add(criterio);
        }
        if(filtro.getAplFechaAplico()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplFechaAplico", filtro.getAplFechaAplico());
            criterios.add(criterio);
        }
        if(filtro.getAplPersonal()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplPersonal.psePk", filtro.getAplPersonal());
            criterios.add(criterio);
        }
        if(filtro.getAplPlaza()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplPlaza.splPk", filtro.getAplPlaza());
            criterios.add(criterio);
        }
        if(filtro.getSeleccionadoEnMatriz()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplSeleccionadoEnMatriz", filtro.getSeleccionadoEnMatriz());
            criterios.add(criterio);
        }
        if(filtro.getAplPlazas()!=null && !filtro.getAplPlazas().isEmpty()){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "aplPlaza.splPk", filtro.getAplPlazas());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getDui())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplPersonal.psePersona.perDui", filtro.getDui());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getNip())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplPersonal.psePersona.perNip", filtro.getNip());
            criterios.add(criterio);
        }
        if(filtro.getDepartamento()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplPersonal.psePersona.perDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            criterios.add(criterio);
        }
        if(filtro.getMunicipio()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplPersonal.psePersona.perDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            criterios.add(criterio);
        }
        if(filtro.getEpaCum()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplEspecialidadesAlAplicar.epaCum", filtro.getEpaCum());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getEpaFechaGraduacionDesde()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "aplEspecialidadesAlAplicar.epaFechaGraduacion", filtro.getEpaFechaGraduacionDesde());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getEpaFechaGraduacionHasta()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "aplEspecialidadesAlAplicar.epaFechaGraduacion", filtro.getEpaFechaGraduacionHasta());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if(filtro.getEspecialidades()!=null && !filtro.getEspecialidades().isEmpty()){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "aplEspecialidadesAlAplicar.epaEspecialidad.espPk", filtro.getEspecialidades());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        
        if(filtro.getDiscapacidades() !=null && !filtro.getDiscapacidades().isEmpty()){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "aplPersonal.psePersona.perDiscapacidades.disPk", filtro.getDiscapacidades());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        
        if(filtro.getCalidadAplicante() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "aplCalidadAplicantes.ciaPk", filtro.getCalidadAplicante());
            criterios.add(criterio);
        }
        
        return criterios;
    }

    public List<SgAplicacionPlaza> obtenerPorFiltro(FiltroAplicacionPlaza filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            Boolean distinct = filtro.getSeNecesitaDistinct()!=null?filtro.getSeNecesitaDistinct():Boolean.FALSE;
            
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgAplicacionPlaza.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroAplicacionPlaza filtro) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgAplicacionPlaza.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public List<SgMotivosSeleccionPLaza> obtenerMotivosSeleccion(Long aplPk) {
        String query = "select * from centros_educativos.sg_motivo_aplicacion_plaza ma\n" +
                    "join catalogo.sg_motivos_seleccion_plaza mp on ma.msp_pk = mp.msp_pk\n" +
                    "where ma.apl_pk = :aplPk order by mp.msp_orden asc";
        javax.persistence.Query q = em.createNativeQuery(query, SgMotivosSeleccionPLaza.class);
        q.setParameter("aplPk", aplPk);

        List<SgMotivosSeleccionPLaza> objs = q.getResultList();
        return objs;
    }
    
}
