/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
import com.sofis.security.DataSecurityException;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.filtros.FiltroSolicitudTraslado;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudTraslado;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class SolicitudTrasladoDAO extends HibernateJpaDAOImp<SgSolicitudTraslado, Integer> implements Serializable {

    private JsonWebToken jwt;
    private SeguridadBean segDAO;

    public SolicitudTrasladoDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroSolicitudTraslado filtro, List<OperationSecurity> ops) throws Exception {

        List<CriteriaTO> criterios = new ArrayList();

        // CRITERIA DATA SECURITY CUSTOM
        List<CriteriaTO> criteriosAmbito = this.generarCriteriaDataSecurityCustom(ops, filtro);

        if (criteriosAmbito.size() > 1) {
            criterios.add(CriteriaTOUtils.createORTO(criteriosAmbito.toArray(new CriteriaTO[criteriosAmbito.size()])));
        } else if (criteriosAmbito.size() == 1) {
            criterios.add(criteriosAmbito.get(0));
        }
        //FIN CRITERIA DATA SECURITY CUSTOM

        if (filtro.getPerNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotEstudiante.estPersona.perNie", filtro.getPerNie());
            criterios.add(criterio);
        }
        
        if (filtro.getSotPk() != null){
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotPk", filtro.getSotPk());
            criterios.add(criterio);
        }

        if (filtro.getPerPersonaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotEstudiante.estPersona.perPk", filtro.getPerPersonaPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sotEstudiante.estPersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sotEstudiante.estPersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sotEstudiante.estPersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTerceroNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sotEstudiante.estPersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTerceroNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sotEstudiante.estPersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sotEstudiante.estPersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sotEstudiante.estPersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }
        if (filtro.getDepartamento() != null) {
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeSolicitante.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeOrigen.sedDireccion.dirDepartamento.depPk", filtro.getDepartamento());

            CriteriaTO criterioOR = CriteriaTOUtils.createORTO(criterio1, criterio2);
            criterios.add(criterioOR);
        }
        if (filtro.getMunicipio() != null) {
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeSolicitante.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());
            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeOrigen.sedDireccion.dirMunicipio.munPk", filtro.getMunicipio());

            CriteriaTO criterioOR = CriteriaTOUtils.createORTO(criterio1, criterio2);
            criterios.add(criterioOR);
        }

        if (filtro.getSotEstado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotEstado", filtro.getSotEstado());
            criterios.add(criterio);
        }
        if (filtro.getSotMotivoTraslado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotMotivoTraslado.motPk", filtro.getSotMotivoTraslado());
            criterios.add(criterio);
        }

        if (filtro.getSolicitudEnProceso() != null) {
            for (EnumEstadoSolicitudTraslado solicitudEnProceso : filtro.getSolicitudEnProceso()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "sotEstado", solicitudEnProceso);
                criterios.add(criterio);
            }
        }

        if (filtro.getSotEstados() != null && !filtro.getSotEstados().isEmpty()) {
            List<CriteriaTO> datosAND = new ArrayList();
            for (EnumEstadoSolicitudTraslado estado : filtro.getSotEstados()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotEstado", estado);
                datosAND.add(criterio);
            }
            CriteriaTO[] arrCriterioOR = datosAND.toArray(new CriteriaTO[datosAND.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        if (filtro.getCentroEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeOrigen.sedPk", filtro.getCentroEducativo());

            if (filtro.getBuscarMismaSede() != null && filtro.getBuscarMismaSede()) {
                List<CriteriaTO> datosAND = new ArrayList();
                datosAND.add(criterio);

                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeSolicitante.sedPk", filtro.getCentroEducativo());
                datosAND.add(criterio);

                CriteriaTO[] arrCriterioAND = datosAND.toArray(new CriteriaTO[datosAND.size()]);
                CriteriaTO criterioAND = CriteriaTOUtils.createORTO(arrCriterioAND);
                criterios.add(criterioAND);
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getBuscarMismaSede() != null && !filtro.getBuscarMismaSede()) {
            if (filtro.getCentroEducativoDestino() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeSolicitante.sedPk", filtro.getCentroEducativoDestino());
                criterios.add(criterio);
            }
        }

        if (!StringUtils.isBlank(filtro.getSexo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotEstudiante.estPersona.perSexo.sexCodigo", filtro.getSexo());
            criterios.add(criterio);
        }

        if (filtro.getAnioLectivo() != null) {

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "sotFechaSolicitud", LocalDate.of(filtro.getAnioLectivo(), 1, 1));
            criterios.add(criterio);

            criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "sotFechaSolicitud", LocalDate.of(filtro.getAnioLectivo(), 12, 31));
            criterios.add(criterio);

        }

        return criterios;
    }

    private List<CriteriaTO> generarCriteriaDataSecurityCustom(List<OperationSecurity> ops, FiltroSolicitudTraslado filtro) throws Exception {
        List<CriteriaTO> criteriosAmbito = new ArrayList();
        if (ops != null && ops.isEmpty()) {
            throw new DataSecurityException();
        }
        for (OperationSecurity o : ops) {
            if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
                criteriosAmbito = new ArrayList<>();
                break;
            }
            if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
                //Puede ver solicitudes cuya sede sea origen o destino
                if (BooleanUtils.isTrue(filtro.getVerOrigen())) {
                    criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeOrigen.sedPk", o.getContext()));
                }
                if (BooleanUtils.isTrue(filtro.getVerDestino())) {
                    criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeSolicitante.sedPk", o.getContext()));
                }
            } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
                if (BooleanUtils.isTrue(filtro.getVerOrigen())) {
                    criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeOrigen.sedDireccion.dirDepartamento.depPk", o.getContext()));
                }
                if (BooleanUtils.isTrue(filtro.getVerDestino())) {
                    criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sotSedeSolicitante.sedDireccion.dirDepartamento.depPk", o.getContext()));
                }
            } else {
                //Sin acceso
                criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaPk", -1L));
            }
        }
        return criteriosAmbito;
    }

    public List<SgSolicitudTraslado> obtenerPorFiltro(FiltroSolicitudTraslado filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<OperationSecurity> ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            List<CriteriaTO> criterios = generarCriteria(filtro, ops);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgSolicitudTraslado.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroSolicitudTraslado filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<OperationSecurity> ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            List<CriteriaTO> criterios = generarCriteria(filtro, ops);
            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSolicitudTraslado.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
