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
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.filtros.FiltroEstudianteImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;

public class EstudianteImpresionDAO extends HibernateJpaDAOImp<SgEstudianteImpresion, Integer> implements Serializable {

    private SeguridadDAO segDAO;

    public EstudianteImpresionDAO(EntityManager em) throws Exception {
        super(em);
        this.segDAO = new SeguridadDAO(em);
    }

    private List<CriteriaTO> generarCriteria(FiltroEstudianteImpresion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getEimPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimPk", filtro.getEimPk());
            criterios.add(criterio);
        }

        if (filtro.getEimSolicitudImpresionPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimSolicitudImpresion.simPk", filtro.getEimSolicitudImpresionPk());
            criterios.add(criterio);
        }

        if (filtro.getEimSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimSolicitudImpresion.simSeccion.secPk", filtro.getEimSeccion());
            criterios.add(criterio);
        }
        if (filtro.getEimDefinicionTitulo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimSolicitudImpresion.simDefinicionTitulo.dtiPk", filtro.getEimDefinicionTitulo());
            criterios.add(criterio);
        }
        if (filtro.getEimAnulada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimAnulada", filtro.getEimAnulada());
            criterios.add(criterio);
        }
        if (filtro.getSimTipoSolicitudImpresion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimSolicitudImpresion.simTipoImpresion", filtro.getSimTipoSolicitudImpresion());
            criterios.add(criterio);
        }
        if (filtro.getEimNie()!= null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimEstudiante.estPersona.perNie", filtro.getEimNie());
            criterios.add(criterio);
        }
        if (filtro.getEimNoAnulada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "eimAnulada", Boolean.FALSE);

            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NULL, "eimAnulada", 1);

            criterios.add(CriteriaTOUtils.createORTO(criterio2, criterio));
        }

        if (filtro.getEimEstudiante() != null && !filtro.getEimEstudiante().isEmpty()) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "eimEstudiante.estPk", filtro.getEimEstudiante());
            criterios.add(criterio);
        }

        if (filtro.getEimEstadosSolicitud() != null && !filtro.getEimEstadosSolicitud().isEmpty()) {
            List<CriteriaTO> criteriosOR = new ArrayList();
            for (EnumEstadoSolicitudImpresion solImp : filtro.getEimEstadosSolicitud()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimSolicitudImpresion.simEstado", solImp);
                criteriosOR.add(criterio);

            }
            CriteriaTO[] arrCriterioOR = criteriosOR.toArray(new CriteriaTO[criteriosOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getEimSolicitudesImpresion()!= null && !filtro.getEimSolicitudesImpresion().isEmpty()) {    
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "eimSolicitudImpresion.simPk", filtro.getEimSolicitudesImpresion());
                criterios.add(criterio);
        }
        if (filtro.getEimEstudiantePk() != null ) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimEstudiante.estPk", filtro.getEimEstudiantePk());
            criterios.add(criterio);
        }
        if(filtro.getEimPlanEstudioFk()!=null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimSolicitudImpresion.simSeccion.secPlanEstudio.pesPk", filtro.getEimPlanEstudioFk());
            criterios.add(criterio);
        }
        if(filtro.getEimGradoFk()!=null){
             MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "eimSolicitudImpresion.simSeccion.secServicioEducativo.sduGrado.graPk", filtro.getEimGradoFk());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgEstudianteImpresion> obtenerPorFiltro(FiltroEstudianteImpresion filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgEstudianteImpresion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroEstudianteImpresion filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgEstudianteImpresion.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
