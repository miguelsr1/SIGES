/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.daos;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.persistence.dao.imp.hibernate.HibernateJpaDAOImp;
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
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.filtros.FiltroSedes;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class SedesDAO extends HibernateJpaDAOImp<SgSede, Integer> implements Serializable {

    private SeguridadBean segBean;
    private JsonWebToken jwt;

    public SedesDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segBean = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroSedes filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (BooleanUtils.isFalse(filtro.getIncluirAdscritas())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "sedSedeAdscritaDe", 1);
            criterios.add(criterio);
        }

        if (filtro.getSedPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", filtro.getSedPk());
            criterios.add(criterio);
        }

        if (filtro.getSedTipos() != null && !filtro.getSedTipos().isEmpty()) {
            List<CriteriaTO> tiposSedes = new ArrayList();
            for (TipoSede tipo : filtro.getSedTipos()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedTipo", tipo);
                if (filtro.getPriSubvencionada() != null && TipoSede.CED_PRI.equals(tipo)) {
                    MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "priSubvencionada", filtro.getPriSubvencionada());
                    tiposSedes.add(CriteriaTOUtils.createANDTO(criterio, criterio1));
                } else {
                    tiposSedes.add(criterio);
                }
            }
            CriteriaTO[] arrCriterioOR = tiposSedes.toArray(new CriteriaTO[tiposSedes.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        } else {
            if (filtro.getSedTipo() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedTipo", filtro.getSedTipo());
                criterios.add(criterio);
            }

            if (filtro.getPriSubvencionada() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "priSubvencionada", filtro.getPriSubvencionada());
                if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                    MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.priSubvencionada", filtro.getPriSubvencionada());
                    criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
                } else {
                    criterios.add(criterio);
                }
            }
        }

        if (!StringUtils.isBlank(filtro.getSedNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedNombre()));

            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedNombre()));
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }
        if (!StringUtils.isBlank(filtro.getSedCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedCodigo", filtro.getSedCodigo().trim());

            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedSedeAdscritaDe.sedCodigo", filtro.getSedCodigo().trim());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }

        if (!StringUtils.isBlank(filtro.getSedCodigoExacto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedCodigo", filtro.getSedCodigoExacto().trim());

            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedCodigo", filtro.getSedCodigoExacto().trim());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }

        if (!StringUtils.isBlank(filtro.getSedCodigoONombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedCodigo", filtro.getSedCodigoONombre().trim());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedCodigoONombre()));

            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedSedeAdscritaDe.sedCodigo", filtro.getSedCodigoONombre().trim());
                MatchCriteriaTO criterio3 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedSedeAdscritaDe.sedNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedCodigoONombre()));
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1, criterio2, criterio3));
            } else {
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            }

        }

        if (filtro.getSedDepartamentoId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDireccion.dirDepartamento.depPk", filtro.getSedDepartamentoId());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedDireccion.dirDepartamento.depPk", filtro.getSedDepartamentoId());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getSedMunicipioId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDireccion.dirMunicipio.munPk", filtro.getSedMunicipioId());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedDireccion.dirMunicipio.munPk", filtro.getSedMunicipioId());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getSedZonaId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDireccion.dirZona.zonPk", filtro.getSedZonaId());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedDireccion.dirZona.zonPk", filtro.getSedZonaId());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getSedLegalizada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cedLegalizado", filtro.getSedLegalizada());

            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.cedLegalizado", filtro.getSedLegalizada());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getSedServicioEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduPk", filtro.getSedServicioEducativo());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduPk", filtro.getSedServicioEducativo());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedNivel() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSedNivel());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk", filtro.getSedNivel());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedCiclo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSedCiclo());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicPk", filtro.getSedCiclo());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedModalidadEducativa() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSedModalidadEducativa());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk", filtro.getSedModalidadEducativa());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedOpcion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduOpcion.opcPk", filtro.getSedOpcion());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sduOpcion.opcPk", filtro.getSedOpcion());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedGrado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graPk", filtro.getSedGrado());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduGrado.graPk", filtro.getSedGrado());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedProgramaEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduProgramaEducativo.pedPk", filtro.getSedProgramaEducativo());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduProgramaEducativo.pedPk", filtro.getSedProgramaEducativo());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedModalidadAtencion() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSedModalidadAtencion());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk", filtro.getSedModalidadAtencion());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (BooleanUtils.isTrue(filtro.getSoloModalidadesFlexibles())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencionFlexible", true);
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedSubModalidad() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSedSubModalidad());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk", filtro.getSedSubModalidad());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getSedTipoCalendario() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedTipoCalendario.tcePk", filtro.getSedTipoCalendario());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedTipoCalendario.tcePk", filtro.getSedTipoCalendario());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }

        if (filtro.getCedFineDeLucro() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cedFinesDeLucro", filtro.getCedFineDeLucro());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.cedFinesDeLucro", filtro.getCedFineDeLucro());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }
        if (filtro.getCedTipoOrganismoAdministrativoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cedTipoOrganismoAdministrativo.toaPk", filtro.getCedTipoOrganismoAdministrativoPk());
            if (BooleanUtils.isTrue(filtro.getUtilizarFiltrosEnSedePadre())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.cedTipoOrganismoAdministrativo.toaPk", filtro.getCedTipoOrganismoAdministrativoPk());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            } else {
                criterios.add(criterio);
            }
        }
        if (filtro.getSedHabilitado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedHabilitado", filtro.getSedHabilitado());
            criterios.add(criterio);
        }

        if (filtro.getSedAdscritaPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSedeAdscritaDe.sedPk", filtro.getSedAdscritaPk());
            criterios.add(criterio);
        }

        if (filtro.getSedRevisado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "sedReporteDirector.rdiPk", 1);
            criterios.add(criterio);
        }

        if (filtro.getSedesPk() != null && !filtro.getSedesPk().isEmpty()) {
            List<CriteriaTO> serviciosCriteria = new ArrayList();
            for (Long sedPk : filtro.getSedesPk()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedPk", sedPk);
                serviciosCriteria.add(criterio);
            }

            if (!serviciosCriteria.isEmpty()) {
                CriteriaTO[] arrCriterioOR = serviciosCriteria.toArray(new CriteriaTO[serviciosCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }
        }

        if (filtro.getSedHabilitadaOLegalizada() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedHabilitado", filtro.getSedHabilitadaOLegalizada());
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cedLegalizado", filtro.getSedHabilitadaOLegalizada());
            criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));

        }

        if (filtro.getSedImplementadora() != null) {
            MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedImplementadora.impPk", filtro.getSedImplementadora());
            criterios.add(criterio1);
        }

        if (filtro.getSedSistemaIntegradoPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSistemas.sinPk.sinPk", filtro.getSedSistemaIntegradoPk());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getSedSistemaIntegradoCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedSistemas.sinPk.sinCodigo", SofisStringUtils.normalizarParaBusqueda(filtro.getSedSistemaIntegradoCodigo()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getSedSistemaIntegradoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedSistemas.sinPk.sinNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedSistemaIntegradoNombre()));
            criterios.add(criterio);
        }
        if (filtro.getSedSiPromotor() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedSistemas.sinPk.sinPromotor.proPk", filtro.getSedSiPromotor());
            criterios.add(criterio);
        }
        if (filtro.getSedTieneSistemaIntegrado() != null) {
            if (BooleanUtils.isTrue(filtro.getSedTieneSistemaIntegrado())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "sedSistemas.sinPk", 1);
                criterios.add(criterio);
                filtro.setSeNecesitaDistinct(Boolean.TRUE);
            }
        }

        if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombre()) || (filtro.getSedAlfAlfabetizadorNombreCompleto() != null && !filtro.getSedAlfAlfabetizadorNombreCompleto().esVacio()) || !StringUtils.isBlank(filtro.getSedAlfPromotorNombre()) || (filtro.getSedAlfPromotorNombreCompleto() != null && !filtro.getSedAlfPromotorNombreCompleto().esVacio())) {

            if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombre()) || (filtro.getSedAlfAlfabetizadorNombreCompleto() != null && !filtro.getSedAlfAlfabetizadorNombreCompleto().esVacio())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDatoContratacion.dcoCargo.carCodigo", Constantes.CARGO_ALFABETIZADOR);
                criterios.add(criterio1);
            }

            if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombre()) || (filtro.getSedAlfPromotorNombreCompleto() != null && !filtro.getSedAlfPromotorNombreCompleto().esVacio())) {
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "sedDatoContratacion.dcoCargo.carCodigo", Constantes.CARGO_PROMOTOR);
                criterios.add(criterio1);
            }

            MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "sedDatoContratacion.dcoDesde", LocalDate.now());
            MatchCriteriaTO criterio3 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "sedDatoContratacion.dcoHasta", LocalDate.now());
            MatchCriteriaTO criterio4 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "sedDatoContratacion.dcoHasta", 1);

            criterios.add(criterio2);

            criterios.add(CriteriaTOUtils.createORTO(criterio3, criterio4));

            if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfAlfabetizadorNombre()));
                criterios.add(criterio);
            }

            if (filtro.getSedAlfAlfabetizadorNombreCompleto() != null) {
                if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerPrimerNombre())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerPrimerNombre()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerSegundoNombre())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerSegundoNombre()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerTercerNombre())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerTercerNombre()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerPrimerApellido())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerPrimerApellido()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerSegundoApellido())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerSegundoApellido()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerTercerApellido())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfAlfabetizadorNombreCompleto().getPerTercerApellido()));
                    criterios.add(criterioNom);
                }

            }

            if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfPromotorNombre()));
                criterios.add(criterio);
            }

            if (filtro.getSedAlfPromotorNombreCompleto() != null) {
                if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombreCompleto().getPerPrimerNombre())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfPromotorNombreCompleto().getPerPrimerNombre()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombreCompleto().getPerSegundoNombre())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfPromotorNombreCompleto().getPerSegundoNombre()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombreCompleto().getPerTercerNombre())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfPromotorNombreCompleto().getPerTercerNombre()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombreCompleto().getPerPrimerApellido())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfPromotorNombreCompleto().getPerPrimerApellido()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombreCompleto().getPerSegundoApellido())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfPromotorNombreCompleto().getPerSegundoApellido()));
                    criterios.add(criterioNom);
                }
                if (!StringUtils.isBlank(filtro.getSedAlfPromotorNombreCompleto().getPerTercerApellido())) {
                    MatchCriteriaTO criterioNom = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "sedDatoContratacion.dcoDatoEmpleado.demPersonalSede.psePersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getSedAlfPromotorNombreCompleto().getPerTercerApellido()));
                    criterios.add(criterioNom);
                }

            }

            filtro.setSeNecesitaDistinct(Boolean.TRUE);

        }

        return criterios;
    }

    public List<SgSede> obtenerPorFiltro(FiltroSedes filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) || SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())
                                || SeguridadAmbitos.MODALIDADES_FLEXIBLES.name().equals(op.getAmbit()) || SeguridadAmbitos.IMPLEMENTADORA.name().equals(op.getAmbit())) {
                            distinct = Boolean.TRUE;
                        }
                    }
                }
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.findEntityByCriteria(SgSede.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long cantidadTotal(FiltroSedes filtro, String securityOperation) throws DAOGeneralException {
        try {

            List<CriteriaTO> criterios = generarCriteria(filtro);

            List<OperationSecurity> ops = null;
            Boolean distinct = filtro.getSeNecesitaDistinct();
            if (securityOperation != null) {
                ops = segBean.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject(), filtro.getAmbito());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
                        }
                        if (SeguridadAmbitos.SECCION.name().equals(op.getAmbit()) || SeguridadAmbitos.PERSONA.name().equals(op.getAmbit())
                                || SeguridadAmbitos.MODALIDADES_FLEXIBLES.name().equals(op.getAmbit()) || SeguridadAmbitos.IMPLEMENTADORA.name().equals(op.getAmbit())) {
                            distinct = Boolean.TRUE;
                        }
                    }
                }
            }

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgSede.class, criterio, distinct, ops);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
