/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumCeldaDiaHora;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalendario;
import sv.gob.mined.siges.filtros.FiltroHorarioEscolar;
import sv.gob.mined.siges.filtros.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.filtros.seguridad.FiltroRol;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuarioRol;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.HorarioEscolarValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.HorarioEscolarDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalendario;
import sv.gob.mined.siges.persistencia.entidades.SgHorarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.SgHorarioEscolarLite;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativaLite;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgContexto;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgRol;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuarioRol;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class HorarioEscolarBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private UsuarioBean usuariosBean;

    @Inject
    private CalendarioBean calendarioBean;

    @Inject
    private PersonalSedeEducativaBean personalBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgHorarioEscolar
     * @throws GeneralException
     */
    public SgHorarioEscolar obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgHorarioEscolar.class);
                SgHorarioEscolar he = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_HORARIOS_ESCOLARES);
                he.getHesSeccion().getSecPk();
                return he;
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
                CodigueraDAO<SgHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgHorarioEscolar.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_HORARIOS_ESCOLARES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param hes SgHorarioEscolar
     * @return SgHorarioEscolar
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgHorarioEscolar guardar(SgHorarioEscolar hes) throws GeneralException {
        try {
            if (hes != null) {
                if (HorarioEscolarValidacionNegocio.validar(hes)) {
                    if (hes.getHesDiasLectivos() != null && hes.getHesDiasLectivos().size() > 0) {

                        //SE VALIDA QUE EL DIA QUE SE MARCA COMO NO LECTIVO NO TENGA UN CONTROL DE ASISTENCIA REALIZADO
                        List<DayOfWeek> diaSemana = hes.getHesDiasLectivos().stream().filter(d -> BooleanUtils.isFalse(d.getDlhEsLectivo())).map(c -> EnumCeldaDiaHora.diaSemana(c.getDlhDia())).collect(Collectors.toList());

                        if (!diaSemana.isEmpty()) {
                            FiltroCalendario fc = new FiltroCalendario();
                            fc.setAnioLectivoFk(hes.getHesSeccion().getSecAnioLectivo().getAlePk());
                            fc.setTipoCalendarioPk(hes.getHesSeccion().getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());
                            fc.setIncluirCampos(new String[]{"calFechaInicio", "calFechaFin"});
                            List<SgCalendario> calendarios = calendarioBean.obtenerPorFiltro(fc);
                            if (!calendarios.isEmpty()) {
                                Long numOfDaysBetween = ChronoUnit.DAYS.between(calendarios.get(0).getCalFechaInicio(), LocalDate.now().isAfter(calendarios.get(0).getCalFechaFin()) ? calendarios.get(0).getCalFechaFin() : LocalDate.now());
                                List<LocalDate> fechas = IntStream.iterate(0, i -> i + 1)
                                        .limit(numOfDaysBetween)
                                        .mapToObj(i -> calendarios.get(0).getCalFechaInicio().plusDays(i))
                                        .collect(Collectors.toList());

                                List<LocalDate> fechasDeDia = fechas.stream().filter(c -> diaSemana.contains(c.getDayOfWeek())).collect(Collectors.toList());

                                Long result = (Long) em.createQuery("SELECT count(*) from SgControlAsistenciaCabezal where cacSeccion=:seccion and cacFecha in :listaFecha ")
                                        .setParameter("seccion", hes.getHesSeccion())
                                        .setParameter("listaFecha", fechasDeDia)
                                        .setMaxResults(1)
                                        .getSingleResult();

                                if (result.compareTo(0L) != 0) {
                                    BusinessException ge = new BusinessException();
                                    ge.addError(Errores.ERROR_EXISTE_CONTROL_ASISTENCIA_PARA_DIA);
                                    throw ge;
                                }
                            }
                        }
                    }
                    CodigueraDAO<SgHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgHorarioEscolar.class);
                    return codDAO.guardar(hes, hes.getHesPk() == null ? ConstantesOperaciones.CREAR_HORARIOS_ESCOLARES : ConstantesOperaciones.ACTUALIZAR_HORARIOS_ESCOLARES);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hes;
    }

    @Interceptors({AuditInterceptor.class})
    public SgHorarioEscolarLite guardarLite(SgHorarioEscolarLite hes) throws GeneralException {
        try {
            if (hes != null) {
                if (hes.getHesUnicoDocente() && hes.getHesDocente() != null) {
                    if (hes.getHesPk() != null) {
                        crearUsuarioRol(hes.getHesPk(), hes.getHesDocente().getPsePk(), Boolean.TRUE, null);
                    }
                } else {
                    if (hes.getHesUnicoDocente()) {
                        BusinessException ge = new BusinessException();
                        ge.addError("hesDocente", Errores.ERROR_DOCENTE_VACIO);
                        throw ge;
                    }
                }
                CodigueraDAO<SgHorarioEscolarLite> codDAO = new CodigueraDAO<>(em, SgHorarioEscolarLite.class);
                eliminarComponenteDocenteAsociadoHorarioEscolar(hes);
                return codDAO.guardar(hes, hes.getHesPk() == null ? ConstantesOperaciones.CREAR_HORARIOS_ESCOLARES : ConstantesOperaciones.ACTUALIZAR_HORARIOS_ESCOLARES);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return hes;
    }

    /**
     * Devuelve true si el personal tiene horario escolar asignado para el
     * componente en la sección
     *
     * @param codigoUsuario String
     * @param seccionPk Long
     * @param componentePk Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean usuarioDictaComponenteEnSeccion(String codigoUsuario, Long seccionPk, Long componentePk) throws GeneralException {

        if (StringUtils.isBlank(codigoUsuario) || seccionPk == null || componentePk == null) {
            throw new IllegalArgumentException();
        }

        BigInteger personalPk = (BigInteger) em.createNativeQuery("select usu_personal_pk from seguridad.sg_usuarios where usu_codigo = :codigo")
                .setParameter("codigo", codigoUsuario)
                .getSingleResult();

        if (personalPk == null) {
            return Boolean.FALSE;
        }

        return this.personalDictaComponenteEnSeccion(personalPk.longValue(), seccionPk, componentePk);
    }

    /**
     * Devuelve true si el personal tiene horario escolar asignado para el
     * componente en la sección
     *
     * @param personalPk Long
     * @param seccionPk Long
     * @param componentePk Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean personalDictaComponenteEnSeccion(Long personalPk, Long seccionPk, Long componentePk) {

        if (personalPk == null || seccionPk == null || componentePk == null) {
            throw new IllegalArgumentException();
        }

        Long countUnico = ((BigInteger) em.createNativeQuery("select count(*) from centros_educativos.sg_horarios_escolares he "
                + "where he.hes_seccion_fk = :seccionPk "
                + "and he.hes_unico_docente and he.hes_docente_fk = :personalPk ")
                .setParameter("seccionPk", seccionPk)
                .setParameter("personalPk", personalPk)
                .getSingleResult()).longValue();

        if (countUnico > 0L) {
            return Boolean.TRUE;
        }

        Long countCo = ((BigInteger) em.createNativeQuery("select count(*) from centros_educativos.sg_componentes_docentes cd "
                + " INNER JOIN centros_educativos.sg_horarios_escolares he ON (cd.cdo_horario_escolar_fk = he.hes_pk) "
                + " where he.hes_seccion_fk = :seccionPk and cd.cdo_componente_fk = :componentePk and cd.cdo_docente_fk = :personalPk ")
                .setParameter("seccionPk", seccionPk)
                .setParameter("personalPk", personalPk)
                .setParameter("componentePk", componentePk)
                .getSingleResult()).longValue();

        return countCo > 0L;

    }

    public void crearUsuarioRol(Long horarioEscolarPk, Long personalPk, Boolean esUnicoDocente, Long eliminarContextoDePersonalPk) {
        FiltroHorarioEscolar fhe = new FiltroHorarioEscolar();
        fhe.setHesPk(horarioEscolarPk);
        fhe.setIncluirCampos(new String[]{"hesSeccion.secPk"});
        List<SgHorarioEscolar> listHorarioEscolar = obtenerPorFiltro(fhe);

        Long secPk = listHorarioEscolar.get(0).getHesSeccion().getSecPk();

        FiltroRol frol = new FiltroRol();
        frol.setCodigoExacto(Constantes.CODIGO_ROL_DOCENTE_AULA);
        List<SgRol> listRol = seguridadBean.obtenerRol(frol);
        SgRol rol = !listRol.isEmpty() ? listRol.get(0) : null;

        if (rol != null) {
            FiltroUsuarioRol fur = new FiltroUsuarioRol();
            fur.setRol(rol.getRolPk());
            fur.setAmbito(EnumAmbito.SECCION);
            fur.setContexto(secPk);
            fur.setIncluirCampos(new String[]{"purUsuario.usuCodigo", "purUsuario.usuPk", "purUsuario.usuPersonalPk"});
            List<SgUsuarioRol> usuRolList = seguridadBean.obtenerUsuariosRol(fur);

            FiltroPersonalSedeEducativa fpse = new FiltroPersonalSedeEducativa();
            fpse.setPsePk(personalPk);
            fpse.setIncluirCampos(new String[]{"psePk", "psePersona.perDui", "psePersona.perNip", "psePersona.perUsuarioPk",
                "psePersona.perPk", "psePersona.perPrimerNombre",
                "psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perSegundoApellido"});
            List<SgPersonalSedeEducativa> listPersonal = personalBean.obtenerPorFiltro(fpse);

            SgUsuario usu = usuariosBean.crearObtenerUsuarioPersona(listPersonal.get(0).getPsePersona());

            Boolean tieneRol = Boolean.FALSE;
            for (SgUsuarioRol usuRol : usuRolList) {
                tieneRol = Boolean.FALSE;
                if (usu.getUsuCodigo().equals(usuRol.getPurUsuario().getUsuCodigo())) {
                    tieneRol = Boolean.TRUE;
                    break;
                }

            }
            if (BooleanUtils.isFalse(tieneRol)) {
                SgContexto contexto = new SgContexto();
                contexto.setConAmbito(EnumAmbito.SECCION);
                contexto.setContextoId(secPk);

                SgUsuarioRol usuRol = new SgUsuarioRol();
                usuRol.setPurUsuario(usu);
                usuRol.setPurRol(rol);
                usuRol.setPurContexto(contexto);
                seguridadBean.guardarUsuarioRol(usuRol);
            }

            if (BooleanUtils.isTrue(esUnicoDocente)) {
                for (SgUsuarioRol usurol : usuRolList) {
                    if (!usurol.getPurUsuario().getUsuPk().equals(usu.getUsuPk())) {
                        seguridadBean.eliminarUsuarioRol(usurol.getPurPk());
                    }
                }
            }
            
            if (eliminarContextoDePersonalPk != null) {
                List<SgUsuarioRol> usuRolLi = usuRolList.stream().filter(c -> eliminarContextoDePersonalPk.equals(c.getPurUsuario().getUsuPersonalPk())).collect(Collectors.toList());
                if (!usuRolLi.isEmpty()) {
                    seguridadBean.eliminarUsuarioRol(usuRolLi.get(0).getPurPk());
                }
            }

        }
    }

    public void eliminarUsuarioRol(Long horarioEscolarPk, SgPersonalSedeEducativaLite personal) {
        FiltroHorarioEscolar fhe = new FiltroHorarioEscolar();
        fhe.setHesPk(horarioEscolarPk);
        fhe.setIncluirCampos(new String[]{"hesSeccion.secPk"});
        List<SgHorarioEscolar> listHorarioEscolar = obtenerPorFiltro(fhe);

        Long secPk = listHorarioEscolar.get(0).getHesSeccion().getSecPk();

        FiltroRol frol = new FiltroRol();
        frol.setCodigo(Constantes.CODIGO_ROL_DOCENTE_AULA);

        List<SgRol> listRol = seguridadBean.obtenerRol(frol);
        SgRol rol = !listRol.isEmpty() ? listRol.get(0) : null;

        if (rol != null) {
            FiltroUsuarioRol fur = new FiltroUsuarioRol();
            fur.setRol(rol.getRolPk());
            fur.setAmbito(EnumAmbito.SECCION);
            fur.setContexto(secPk);
            fur.setUsuarioCodigo(personal.getPsePersona().getPerDui());
            fur.setIncluirCampos(new String[]{"purVersion"});
            List<SgUsuarioRol> usuRolList = seguridadBean.obtenerUsuariosRol(fur);
            if (!usuRolList.isEmpty()) {
                seguridadBean.eliminarUsuarioRol(usuRolList.get(0).getPurPk());
            }
        }
    }

    public void eliminarComponenteDocenteAsociadoHorarioEscolar(SgHorarioEscolarLite hre) {
        if (hre.getHesPk() != null) {
            String query = "delete from SgComponenteDocente where cdoHorarioEscolar.hesPk= :hrePk";
            Query qda = em.createQuery(query).setParameter("hrePk", hre.getHesPk());
            qda.executeUpdate();
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroHorarioEscolar filtro) throws GeneralException {
        try {
            HorarioEscolarDAO codDAO = new HorarioEscolarDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_HORARIOS_ESCOLARES);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgHorarioEscolar>
     * @throws GeneralException
     */
    public List<SgHorarioEscolar> obtenerPorFiltro(FiltroHorarioEscolar filtro) throws GeneralException {
        try {
            HorarioEscolarDAO codDAO = new HorarioEscolarDAO(em, seguridadBean);
            List<SgHorarioEscolar> hes = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_HORARIOS_ESCOLARES);
            if (!hes.isEmpty()) {
                if (filtro.getHesLevantarListaDocentes() != null) {
                    if (filtro.getHesLevantarListaDocentes()) {
                        for (SgHorarioEscolar horEsc : hes) {
                            if (horEsc.getHesDocentes() != null) {
                                horEsc.getHesDocentes().size();
                            }
                        }
                    }
                }
            }
            return hes;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
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
                CodigueraDAO<SgHorarioEscolar> codDAO = new CodigueraDAO<>(em, SgHorarioEscolar.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_HORARIOS_ESCOLARES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
