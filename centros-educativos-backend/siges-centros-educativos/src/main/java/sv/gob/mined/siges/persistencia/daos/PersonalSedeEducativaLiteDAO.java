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
import sv.gob.mined.siges.enumerados.SeguridadAmbitos;
import sv.gob.mined.siges.filtros.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativaLite;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.utils.SofisStringUtils;

public class PersonalSedeEducativaLiteDAO extends HibernateJpaDAOImp<SgPersonalSedeEducativaLite, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private JsonWebToken jwt;

    public PersonalSedeEducativaLiteDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
    }

    private List<CriteriaTO> generarCriteria(FiltroPersonalSedeEducativa filtro) {

        List<CriteriaTO> criterios = new ArrayList();

        if (!StringUtils.isBlank(filtro.getPerNombreCompleto())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "psePersona.perNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerNombreCompleto()));
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getPerPrimerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "psePersona.perPrimerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "psePersona.perSegundoNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "psePersona.perTercerNombreBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerNombre()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerPrimerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "psePersona.perPrimerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerPrimerApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerSegundoApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "psePersona.perSegundoApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerSegundoApellido()));
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerTercerApellido())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "psePersona.perTercerApellidoBusqueda", SofisStringUtils.normalizarParaBusqueda(filtro.getPerTercerApellido()));
            criterios.add(criterio);
        }
        if (filtro.getPerFechaNacimiento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePersona.perFechaNacimiento", filtro.getPerFechaNacimiento());
            criterios.add(criterio);
        }

        if (filtro.getPerDepartamento() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedDireccion.dirDepartamento.depPk", filtro.getPerDepartamento());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getPerMunicipio() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedDireccion.dirMunicipio.munPk", filtro.getPerMunicipio());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getPerCentroEducativo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedPk", filtro.getPerCentroEducativo());

            if (BooleanUtils.isTrue(filtro.getBuscarEnSedeAdscrita())) {

                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoCentroEducativo.sedSedeAdscritaDe.sedPk", filtro.getPerCentroEducativo());
                List<CriteriaTO> sedesCriteria = new ArrayList();
                sedesCriteria.add(criterio);
                sedesCriteria.add(criterio2);
                CriteriaTO[] arrCriterioOR = sedesCriteria.toArray(new CriteriaTO[sedesCriteria.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);

            } else {
                criterios.add(criterio);
            }

            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }

        if (filtro.getPerNip() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePersona.perNip", filtro.getPerNip());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerDui())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePersona.perDui", filtro.getPerDui());
            criterios.add(criterio);
        }
        if (!StringUtils.isBlank(filtro.getPerCodigoEmpleado())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "pseDatoEmpleado.demCodigoEmpleado", filtro.getPerCodigoEmpleado());
            criterios.add(criterio);
        }
        if (filtro.getPerTipoEmpleado() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "pseDatoEmpleado.demDatoContratacion.dcoTipo", filtro.getPerTipoEmpleado());
            criterios.add(criterio);
            filtro.setSeNecesitaDistinct(Boolean.TRUE);
        }
        if (filtro.getPerNie() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePersona.perNie", filtro.getPerNie());
            criterios.add(criterio);
        }

        if (filtro.getPerPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "psePersona.perPk", filtro.getPerPk());
            criterios.add(criterio);
        }

        if (BooleanUtils.isTrue(filtro.getPersonalActivo())) {
            LocalDate hoy = LocalDate.now();

            //Fecha hasta mayor a hoy o null
            List<CriteriaTO> periodoCriteria = new ArrayList();
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "pseDatoEmpleado.demDatoContratacion.dcoHasta", hoy);
            periodoCriteria.add(criterio);
            criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.NULL, "pseDatoEmpleado.demDatoContratacion.dcoHasta", 1);
            periodoCriteria.add(criterio);

            CriteriaTO[] arrCriterioOR = periodoCriteria.toArray(new CriteriaTO[periodoCriteria.size()]);
            CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
            criterios.add(criterioOR);
        }

        return criterios;
    }

    public List<SgPersonalSedeEducativaLite> obtenerPorFiltro(FiltroPersonalSedeEducativa filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            String[] orderBy = filtro.getOrderBy();
            boolean[] asc = filtro.getAscending();

            Boolean distinct = Boolean.TRUE; //Todos los ámbitos menos MINED necesitan distinct porque la seguridad busca en datos contratación
            List<OperationSecurity> ops = null;
            if (securityOperation == null) {
                distinct = filtro.getSeNecesitaDistinct();
            } else {
                ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
                if (ops != null) {
                    for (OperationSecurity op : ops) {
                        if (SeguridadAmbitos.MINED.name().equals(op.getAmbit())) {
                            distinct = filtro.getSeNecesitaDistinct();
                            break;
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
            return this.findEntityByCriteria(SgPersonalSedeEducativaLite.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), distinct, ops, filtro.getIncluirCampos());

        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
