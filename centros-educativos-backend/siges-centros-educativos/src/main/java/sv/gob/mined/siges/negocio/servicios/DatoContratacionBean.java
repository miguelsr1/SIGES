/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDatoContratacion;
import sv.gob.mined.siges.filtros.FiltroHorarioEscolar;
import sv.gob.mined.siges.filtros.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.filtros.catalogo.FiltroCargoRol;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuarioRol;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.DatoContratacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.DatoContratacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargoRoles;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgContexto;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuarioRol;

@Stateless
@Traced
public class DatoContratacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private HorarioEscolarBean horarioEscolarBean;

    @Inject
    private CatalogoBean catalogoBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private UsuarioBean usuarioBean;

    @Inject
    private PersonalSedeEducativaBean personalSedeBean;

    @Inject
    private DatoEmpleadoBean datoEmpleadoBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgDatoContratacion
     * @throws GeneralException
     */
    public SgDatoContratacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDatoContratacion> codDAO = new CodigueraDAO<>(em, SgDatoContratacion.class);
                SgDatoContratacion ret = codDAO.obtenerPorId(id, null);
                if (ret.getDcoJornadasLaborales() != null) {
                    ret.getDcoJornadasLaborales().size();
                }
                if (ret.getDcoCentroEducativo() != null) {
                    ret.getDcoCentroEducativo().getSedPk();
                }
                return ret;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDatoContratacion> codDAO = new CodigueraDAO<>(em, SgDatoContratacion.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgDatoContratacion
     * @return SgDatoContratacion
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgDatoContratacion guardar(SgDatoContratacion entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (DatoContratacionValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgDatoContratacion> codDAO = new CodigueraDAO<>(em, SgDatoContratacion.class);

                    if (entity.getDcoDatoEmpleado().getDemPk() == null || entity.getDcoDatoEmpleado().getDemPersonalSede().getPsePk() == null) {
                        throw new IllegalStateException();
                    }

                    if (entity.getDcoPk() != null) {
                        if (BooleanUtils.isFalse(entity.getDcoActividadesDocentes()) && !TipoPersonalSedeEducativa.DOC.equals(entity.getDcoTipo())) {
                            verAsignacionHorarioEscolar(entity);
                        }
                    }

                    //Si no esta vencido, se generan roles
                    if (entity.getDcoHasta() == null || entity.getDcoHasta().isAfter(LocalDate.now())) {

                        if (BooleanUtils.isTrue(entity.getDcoActividadesDocentes()) || TipoPersonalSedeEducativa.DOC.equals(entity.getDcoTipo())) {
                            datoEmpleadoBean.permitirAplicarAPlaza(entity.getDcoDatoEmpleado().getDemPk());
                        }

                        FiltroCargoRol filtroCargoRoles = new FiltroCargoRol();
                        filtroCargoRoles.setCarCargo(entity.getDcoCargo().getCarPk());
                        List<SgCargoRoles> listCargoRol = catalogoBean.obtenerPorFiltroCargoRol(filtroCargoRoles);
                        if (!listCargoRol.isEmpty()) {
                            FiltroPersonalSedeEducativa fpse = new FiltroPersonalSedeEducativa();
                            fpse.setPsePk(entity.getDcoDatoEmpleado().getDemPersonalSede().getPsePk());
                            fpse.setIncluirCampos(new String[]{"psePk", "psePersona.perDui", "psePersona.perNip", "psePersona.perUsuarioPk",
                                "psePersona.perPk", "psePersona.perPrimerNombre",
                                "psePersona.perPrimerApellido", "psePersona.perSegundoNombre", "psePersona.perSegundoApellido"});
                            List<SgPersonalSedeEducativa> listPersonal = personalSedeBean.obtenerPorFiltro(fpse);

                            if (!listPersonal.isEmpty()) {

                                SgUsuario usuario = usuarioBean.crearObtenerUsuarioPersona(listPersonal.get(0).getPsePersona());

                                for (SgCargoRoles carRol : listCargoRol) {
                                    FiltroUsuarioRol fur = new FiltroUsuarioRol();
                                    fur.setUsuario(usuario.getUsuPk());
                                    fur.setRol(carRol.getCarRol().getRolPk());
                                    fur.setAmbito(EnumAmbito.SEDE);
                                    fur.setContexto(entity.getDcoCentroEducativo().getSedPk());
                                    List<SgUsuarioRol> usuRolList = seguridadBean.obtenerUsuariosRol(fur);
                                    if (usuRolList.isEmpty()) {
                                        SgUsuarioRol usuRol = new SgUsuarioRol();
                                        SgContexto contexto = new SgContexto();
                                        contexto.setConAmbito(EnumAmbito.SEDE);
                                        contexto.setContextoId(entity.getDcoCentroEducativo().getSedPk());
                                        usuRol.setPurContexto(contexto);
                                        usuRol.setPurUsuario(usuario);
                                        usuRol.setPurRol(carRol.getCarRol());
                                        usuRol = seguridadBean.guardarUsuarioRol(usuRol);
                                    }
                                }

                            }
                        }
                    }
                    return codDAO.guardar(entity, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    public void verAsignacionHorarioEscolar(SgDatoContratacion entity) {
        FiltroDatoContratacion filtrodc = new FiltroDatoContratacion();
        filtrodc.setDcoPk(entity.getDcoPk());
        filtrodc.setIncluirCampos(new String[]{"dcoActividadesDocentes", "dcoVersion"});
        List<SgDatoContratacion> listDato = this.obtenerPorFiltro(filtrodc);
        if (!listDato.isEmpty()) {

            SgDatoContratacion datoEnBase = listDato.get(0);
            if (datoEnBase.getDcoActividadesDocentes() && !entity.getDcoActividadesDocentes()) {

                FiltroHorarioEscolar fhe = new FiltroHorarioEscolar();
                fhe.setHesPersonalSede(entity.getDcoDatoEmpleado().getDemPersonalSede().getPsePk());
                Long cantHorarios = horarioEscolarBean.obtenerTotalPorFiltro(fhe);
                if (cantHorarios > 0) {

                    BusinessException ge = new BusinessException();
                    ge.addError(Errores.ERROR_PERSONAL_ASIGNADO_HORARIO_ESCOLAR);
                    throw ge;
                }
            }
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgDatoContratacion> codDAO = new CodigueraDAO<>(em, SgDatoContratacion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroDatoContratacion filtro) throws GeneralException {
        try {
            DatoContratacionDAO codDAO = new DatoContratacionDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgDatoContratacion
     * @throws GeneralException
     */
    public List<SgDatoContratacion> obtenerPorFiltro(FiltroDatoContratacion filtro) throws GeneralException {
        try {
            DatoContratacionDAO codDAO = new DatoContratacionDAO(em);
            List<SgDatoContratacion> datosContratacionList = codDAO.obtenerPorFiltro(filtro);
            if (BooleanUtils.isTrue(filtro.getIncluirJornadasLaborales())) {

                for (SgDatoContratacion datosContratacion : datosContratacionList) {
                    if (datosContratacion.getDcoJornadasLaborales() != null) {
                        datosContratacion.getDcoJornadasLaborales().size();
                    }
                }
            }
            return datosContratacionList;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
