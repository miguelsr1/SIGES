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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.filtros.FiltroActividadCalendario;
import sv.gob.mined.siges.filtros.FiltroSedes;
import sv.gob.mined.siges.negocio.servicios.SeguridadBean;
import sv.gob.mined.siges.persistencia.entidades.SgActividadCalendario;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

public class ActividadCalendarioDAO extends HibernateJpaDAOImp<SgActividadCalendario, Integer> implements Serializable {

    private SeguridadBean segDAO;
    private SedesDAO sedeDAO;
    private JsonWebToken jwt;

    public ActividadCalendarioDAO(EntityManager em, SeguridadBean segBean) throws Exception {
        super(em);
        this.jwt = Lookup.obtenerJWT();
        this.segDAO = segBean;
        this.sedeDAO = new SedesDAO(em, segBean);
    }

    private List<CriteriaTO> generarCriteria(FiltroActividadCalendario filtro, List<OperationSecurity> ops) throws Exception {

        List<CriteriaTO> criterios = new ArrayList();

        if(filtro.getNoUsarDataSecurity()==null || BooleanUtils.isNotTrue(filtro.getNoUsarDataSecurity())){
            // CRITERIA DATA SECURITY CUSTOM
            List<CriteriaTO> criteriosAmbito = this.generarCriteriaDataSecurityCustom(ops);
            if (criteriosAmbito.size() > 1) {
                criterios.add(CriteriaTOUtils.createORTO(criteriosAmbito.toArray(new CriteriaTO[criteriosAmbito.size()])));
            } else if (criterios.size() == 1) {
                criterios.add(criteriosAmbito.get(0));
            }
            //FIN CRITERIA DATA SECURITY CUSTOM
        }
        if (filtro.getCalendarioPk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "acaCalendario.calPk", filtro.getCalendarioPk());
            criterios.add(criterio);
        }
        if (filtro.getTomarEnCuentaTodosAmbitos() == null) {
            if (filtro.getAmbitos() != null && !filtro.getAmbitos().isEmpty()) {
                List<CriteriaTO> datosAND = new ArrayList();
                for (EnumAmbito ambito : filtro.getAmbitos()) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaAmbito", ambito);
                    datosAND.add(criterio);
                }
                CriteriaTO[] arrCriterioOR = datosAND.toArray(new CriteriaTO[datosAND.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);
            }

            if (filtro.getSedePk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "acaSede.sedPk", filtro.getSedePk());
                criterios.add(criterio);
            }

            if (filtro.getDepartamentoPk() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "acaDepartamento.depPk", filtro.getDepartamentoPk());

                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "acaSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoPk());

                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio2));
            }
        } else {
            if (filtro.getAmbitos() != null && !filtro.getAmbitos().isEmpty()) {

                List<CriteriaTO> criterioGeneral = new ArrayList();
                for (EnumAmbito ambito : filtro.getAmbitos()) {
                    List<CriteriaTO> datosAND = new ArrayList();
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaAmbito", ambito);
                    datosAND.add(criterio);
                    switch (ambito) {
                        case SEDE:
                            if (filtro.getSedePk() != null) {
                                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(
                                        MatchCriteriaTO.types.EQUALS, "acaSede.sedPk", filtro.getSedePk());
                                datosAND.add(criterio2);

                            }
                            break;
                        case DEPARTAMENTAL:
                            if (filtro.getDepartamentoPk() != null) {
                                MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(
                                        MatchCriteriaTO.types.EQUALS, "acaDepartamento.depPk", filtro.getDepartamentoPk());

                                MatchCriteriaTO criterio3 = CriteriaTOUtils.createMatchCriteriaTO(
                                        MatchCriteriaTO.types.EQUALS, "acaSede.sedDireccion.dirDepartamento.depPk", filtro.getDepartamentoPk());

                                datosAND.add(CriteriaTOUtils.createORTO(criterio2, criterio3));
                            }
                            break;
                        default:
                            break;
                    }
                    CriteriaTO[] arrCriterioOR = datosAND.toArray(new CriteriaTO[datosAND.size()]);
                    CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createANDTO(arrCriterioOR) : arrCriterioOR[0];
                    criterioGeneral.add(criterioOR);
                }
                CriteriaTO[] arrCriterioOR = criterioGeneral.toArray(new CriteriaTO[criterioGeneral.size()]);
                CriteriaTO criterioOR = arrCriterioOR.length > 1 ? CriteriaTOUtils.createORTO(arrCriterioOR) : arrCriterioOR[0];
                criterios.add(criterioOR);

            }
        }
        // [Inicio,Fin] superposición [Desde,Hasta] --> [x1,x2] superposición [y1,y2]
        // x1 <= y2 && y1 <= x2

        if (filtro.getFechaDesde() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.GREATEREQUAL, "acaFechaHasta", filtro.getFechaDesde());
            criterios.add(criterio);
        }

        if (filtro.getFechaHasta() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.LESSEQUAL, "acaFechaDesde", filtro.getFechaHasta());
            criterios.add(criterio);
        }

        if (!StringUtils.isBlank(filtro.getNombre())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS_ILIKE, "acaNombre", filtro.getNombre());
            criterios.add(criterio);
        }

        if (filtro.getEsLectivo() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "acaDiaLectivo", filtro.getEsLectivo());
            criterios.add(criterio);
        }

        if (filtro.getAnioFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "acaCalendario.calAnioLectivo.alePk", filtro.getAnioFk());
            criterios.add(criterio);
        }

        if (filtro.getTipoCalendarioFk() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.EQUALS, "acaCalendario.calTipoCalendario.tcePk", filtro.getTipoCalendarioFk());
            criterios.add(criterio);
        }

        return criterios;
    }

    private List<CriteriaTO> generarCriteriaDataSecurityCustom(List<OperationSecurity> ops) throws Exception {
        List<CriteriaTO> criteriosAmbito = new ArrayList();
        criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaAmbito", EnumAmbito.MINED));
        if (ops != null && ops.isEmpty()) {
            throw new DataSecurityException();
        }
        for (OperationSecurity o : ops) {
            if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
                criteriosAmbito = new ArrayList<>();
                break;
            }
            if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
                //Puede ver actividades departamentales de su departamento, y actividades de su sede.
                criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaSede.sedPk", o.getContext()));
                FiltroSedes filSede = new FiltroSedes();
                filSede.setSedPk((Long) o.getContext());
                filSede.setIncluirCampos(new String[]{"sedDireccion.dirDepartamento.depPk"});
                List<SgSede> sedes = sedeDAO.obtenerPorFiltro(filSede, ConstantesOperaciones.BUSCAR_SEDES);
                if (!sedes.isEmpty()) {
                    SgSede sede = sedes.get(0);
                    criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaDepartamento.depPk", sede.getSedDireccion().getDirDepartamento().getDepPk()));
                }
            } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
                //Departamental puede ver actividades departamentales, y actividades de sede de su departamento
                criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaSede.sedDireccion.dirDepartamento.depPk", o.getContext()));
                criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaDepartamento.depPk", o.getContext()));
            } else {
                //Sin acceso
                criteriosAmbito.add(CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "acaPk", -1L));
            }
        }
        return criteriosAmbito;
    }

    public List<SgActividadCalendario> obtenerPorFiltro(FiltroActividadCalendario filtro, String securityOperation) throws DAOGeneralException {
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

            return this.findEntityByCriteria(SgActividadCalendario.class, criterio, orderBy, asc, filtro.getFirst(), filtro.getMaxResults(), false, null, filtro.getIncluirCampos());
        } catch (DAOGeneralException e) {
            throw e;
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

    public Long obtenerTotalPorFiltro(FiltroActividadCalendario filtro, String securityOperation) throws DAOGeneralException {
        try {
            List<OperationSecurity> ops = segDAO.obtenerOperacionesSeguridad(securityOperation, jwt.getSubject());
            List<CriteriaTO> criterios = generarCriteria(filtro, ops);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return this.countByCriteria(SgActividadCalendario.class, criterio, false, null);
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }

}
