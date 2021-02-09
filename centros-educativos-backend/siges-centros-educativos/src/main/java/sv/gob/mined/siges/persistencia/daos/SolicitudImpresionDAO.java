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
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.filtros.FiltroSolicitudImpresion;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class SolicitudImpresionDAO extends HibernateJpaDAOImp<SgSolicitudImpresion, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public SolicitudImpresionDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroSolicitudImpresion filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (filtro.getSimUsuario() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simUsuario.usuPk", filtro.getSimUsuario());
            criterios.add(criterio);
        }
        if (filtro.getSimSeccion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secPk", filtro.getSimSeccion());
            criterios.add(criterio);
        }
        if (filtro.getSimSede() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedPk", filtro.getSimSede());
            criterios.add(criterio);
        }
        if (filtro.getSimEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simEstado", filtro.getSimEstado());
            criterios.add(criterio);
        }
        if (filtro.getSimDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", filtro.getSimDepartamento());
            criterios.add(criterio);
        }
        if (filtro.getSimMunicipio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munPk", filtro.getSimMunicipio());
            criterios.add(criterio);
        }
        if (filtro.getSimFechaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "simFechaSolicitud", filtro.getSimFechaDesde());
            criterios.add(criterio);
        }
        if (filtro.getSimFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "simFechaSolicitud", filtro.getSimFechaHasta());
            criterios.add(criterio);
        }
        if (filtro.getSimDefinicionTitulo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simDefinicionTitulo.dtiPk", filtro.getSimDefinicionTitulo());
            criterios.add(criterio);
        }
        if (filtro.getSimPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simPk", filtro.getSimPk());
            criterios.add(criterio);
        }

        if (filtro.getSimTipoSolicitudImpresion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simTipoImpresion", filtro.getSimTipoSolicitudImpresion());
            criterios.add(criterio);
        }

        if (filtro.getEstudiantesPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simEstudianteImpresion.eimEstudiante.estPk", filtro.getEstudiantesPk());
            criterios.add(criterio);
        }
        if (filtro.getSimSedeCodigo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simSeccion.secServicioEducativo.sduSede.sedCodigo", filtro.getSimSedeCodigo());
            criterios.add(criterio);
        }

        if (filtro.getSimEstados() != null && !filtro.getSimEstados().isEmpty()) {
            List<CriteriaTO> criteriosOR = new ArrayList();
            for (EnumEstadoSolicitudImpresion solImp : filtro.getSimEstados()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simEstado", solImp);
                criteriosOR.add(criterio);

            }
            CriteriaTO[] arrCriterioOR = criteriosOR.toArray(new CriteriaTO[criteriosOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getSolicitudesImpresionPk() != null && !filtro.getSolicitudesImpresionPk().isEmpty()) {
            List<CriteriaTO> criteriosOR = new ArrayList();
            for (Long solImp : filtro.getSolicitudesImpresionPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simPk", solImp);
                criteriosOR.add(criterio);

            }
            CriteriaTO[] arrCriterioOR = criteriosOR.toArray(new CriteriaTO[criteriosOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getSimTiposSolicitudImpresion() != null && !filtro.getSimTiposSolicitudImpresion().isEmpty()) {
            List<CriteriaTO> criteriosOR = new ArrayList();
            for (EnumTipoSolicitudImpresion solImp : filtro.getSimTiposSolicitudImpresion()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simTipoImpresion", solImp);
                criteriosOR.add(criterio);

            }
            CriteriaTO[] arrCriterioOR = criteriosOR.toArray(new CriteriaTO[criteriosOR.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }
        if (filtro.getSimTituloEntregadoCentroEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simTituloEntregadoCentroEducativo", filtro.getSimTituloEntregadoCentroEducativo());
            criterios.add(criterio);
        }
        if (filtro.getSimTituloEntregadoDepartamental() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "simTituloEntregadoDepartamental", filtro.getSimTituloEntregadoDepartamental());
            criterios.add(criterio);
        }

        return criterios;
    }

    public List<SgSolicitudImpresion> obtenerPorFiltro(FiltroSolicitudImpresion filtro, String securityOperation) throws DAOGeneralException {
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
            return this.findEntityByCriteria(SgSolicitudImpresion.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), filtro.getSeNecesitaDistinct(), segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()), filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroSolicitudImpresion filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSolicitudImpresion.class, criterio, filtro.getSeNecesitaDistinct(), segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject()));
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
