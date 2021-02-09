/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.eclipse.microprofile.opentracing.Traced;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.MediaType;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgAsistenciasInasistenciasEstudianteResponse;
import sv.gob.mined.siges.dto.SgEstudianteServicioSocial;
import sv.gob.mined.siges.dto.SgPrediccionDesercionRequest;
import sv.gob.mined.siges.dto.SgPrediccionDesercionResponse;
import sv.gob.mined.siges.dto.SgRegistroFichaEstudiante;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroAllegado;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.filtros.FiltroCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroPersona;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.EstudianteValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EstudianteDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionesHistoricasEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgDireccion;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteLiteEliminar;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteValoracion;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion;
import sv.gob.mined.siges.persistencia.entidades.SgManifestacionViolencia;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgOpcion;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgPlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgTelefono;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNacionalidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPais;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSexo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoIdentificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgZona;
import sv.gob.mined.siges.persistencia.utilidades.InitializeObjectUtils;

@Stateless
@Traced
public class EstudianteBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private AlertaBean alertaBean;

    @Inject
    private PersonaBean personaBean;

    @Inject
    private AllegadoBean allegadoBean;

    @Inject
    private ConfiguracionBean configuracionBean;

    @Inject
    @ConfigProperty(name = "email-valid-pattern")
    private String emailPattern;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private EscolaridadEstudianteBean escolaridadEstudianteBean;

    @Inject
    private PlanEstudioBean planEstudioBean;
    
    @Inject
    private CalificacionEstudianteBean calificacionEstudianteBean;
    
    @Inject
    private CalificacionesHistoricasEstudianteBean calificacionesHistEstudianteBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstudiante
     * @throws GeneralException
     *
     */
    public SgEstudiante obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstudiante> codDAO = new CodigueraDAO<>(em, SgEstudiante.class);
                SgEstudiante ret = null;
                if (BooleanUtils.isTrue(dataSecurity)) {
                    ret = codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_ESTUDIANTES);
                } else {
                    ret = codDAO.obtenerPorId(id, null);
                }
                if (ret != null) {
                    InitializeObjectUtils.inicializarPersona(ret.getEstPersona());
                    if (ret.getEstPersona().getPerDatosResidenciales() != null) {
                        ret.getEstPersona().getPerDatosResidenciales().getPerPk();
                        if (ret.getEstPersona().getPerDatosResidenciales().getPerElementosHogar() != null) {
                            ret.getEstPersona().getPerDatosResidenciales().getPerElementosHogar().size();
                        }
                    }
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
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstudiante> codDAO = new CodigueraDAO<>(em, SgEstudiante.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_ESTUDIANTES);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgEstudiante
     * @return SgEstudiante
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgEstudiante guardar(SgEstudiante entity, Boolean dataSecurity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (EstudianteValidacionNegocio.validar(entity, emailPattern, configuracionBean)) {
                    CodigueraDAO<SgEstudiante> codDAO = new CodigueraDAO<>(em, SgEstudiante.class);
                    SgPersona per = personaBean.guardar(entity.getEstPersona());
                    entity.setEstPersona(per);
                    if (BooleanUtils.isTrue(dataSecurity)) {
                        return codDAO.guardar(entity, entity.getEstPk() == null ? ConstantesOperaciones.CREAR_ESTUDIANTE : ConstantesOperaciones.ACTUALIZAR_ESTUDIANTE);
                    } else {
                        return codDAO.guardar(entity, null);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Copia estudiante origen a destino Datos de origen tienen precedencia
     * sobre destino. Elimina estudiante, persona y dire origen.
     *
     * @param Long estudianteOrigenPk
     * @param Long estudianteDestinoPk
     * @throws GeneralException
     */
    public void unirEstudiantes(Long estOrigenPk, Long estDestinoPk) throws GeneralException {
        try {
            SgEstudiante origen = em.find(SgEstudiante.class, estOrigenPk);
            SgEstudiante destino = em.find(SgEstudiante.class, estDestinoPk);
            SgPersona perOrigen = origen.getEstPersona();
            SgPersona perDestino = destino.getEstPersona();
            SgDireccion dirOrigen = perOrigen.getPerDireccion();
            SgDireccion dirDestino = perDestino.getPerDireccion();

            if (destino.getEstUltimaMatricula() != null && destino.getEstUltimaMatricula().getMatEstado().equals(EnumMatriculaEstado.ABIERTO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_MATRICULA_ABIERTA_ESTUDIANTE);
                throw be;
            }
            if (origen.getEstUltimaMatricula() != null && origen.getEstUltimaMatricula().getMatEstado().equals(EnumMatriculaEstado.ABIERTO)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_MATRICULA_ABIERTA_ESTUDIANTE);
                throw be;
            }

            //Copiar campos simples estudiante que no sean null
            PropertyUtils.describe(origen).entrySet().stream()
                    .filter(e -> e.getValue() != null
                    && !e.getKey().equals("class")
                    && !e.getKey().equals("estPk")
                    && !e.getKey().equals("estPersona")
                    && !e.getKey().equals("estVersion")
                    && !Collection.class.isAssignableFrom(e.getValue().getClass()))
                    .forEach(e -> {
                        try {
                            PropertyUtils.setProperty(destino, e.getKey(), e.getValue());
                        } catch (Exception ex) {
                        }
                    });

            //Copiar campos simples persona que no sean null
            PropertyUtils.describe(perOrigen).entrySet().stream()
                    .filter(e -> e.getValue() != null
                    && !e.getKey().equals("class")
                    && !e.getKey().equals("perPk")
                    && !e.getKey().equals("perEstudiante")
                    && !e.getKey().equals("perDatosResidenciales")
                    && !e.getKey().equals("perVersion")
                    && !Collection.class.isAssignableFrom(e.getValue().getClass()))
                    .forEach(e -> {
                        try {
                            PropertyUtils.setProperty(perDestino, e.getKey(), e.getValue());
                        } catch (Exception ex) {
                        }
                    });

            //Copiar campos simples direccion que no sean null
            PropertyUtils.describe(dirOrigen).entrySet().stream()
                    .filter(e -> e.getValue() != null
                    && !e.getKey().equals("class")
                    && !e.getKey().equals("dirPk")
                    && !e.getKey().equals("dirVersion")
                    && !Collection.class.isAssignableFrom(e.getValue().getClass()))
                    .forEach(e -> {
                        try {
                            PropertyUtils.setProperty(dirDestino, e.getKey(), e.getValue());
                        } catch (Exception ex) {
                        }
                    });

            //Modificar colecciones estudiante
            if (origen.getEstAsistencia() != null) {
                //Hibernate autosave
                for (SgAsistencia a : origen.getEstAsistencia()) {
                    a.setAsiEstudiante(destino);
                }
            }
            if (origen.getEstManifestacionesViolencia() != null) {
                //Hibernate autosave
                for (SgManifestacionViolencia m : origen.getEstManifestacionesViolencia()) {
                    m.setMviEstudiante(destino);
                }
            }
            if (origen.getEstValoraciones() != null) {
                //Hibernate autosave
                for (SgEstudianteValoracion v : origen.getEstValoraciones()) {
                    v.setEsvEstudiante(destino);
                }
            }
            if (origen.getEstMatriculas() != null) {
                for (SgMatricula mat : origen.getEstMatriculas()) {
                    for (SgMatricula matDestino : destino.getEstMatriculas()) {
                        if (dateRangeOverlap(mat, matDestino)) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_MATRICULAS_CON_FECHA_SOLAPADA);
                            throw be;
                        }
                    }
                }

                //Hibernate autosave
                for (SgMatricula m : origen.getEstMatriculas()) {
                    m.setMatEstudiante(destino);
                }
            }

            if (origen.getEstCalificaciones() != null) {
                //Hibernate autosave
                for (SgCalificacionEstudiante c : origen.getEstCalificaciones()) {
                    c.setCaeEstudiante(destino);
                }
            }

            if (origen.getEstPk() != null && destino.getEstPk() != null) {
                em.createNativeQuery("update acreditacion.sg_calificaciones_historicas_estudiante set che_estudiante_fk=:destinoPk,che_estudiante_nie=:destinoNie,che_estudiante_per_pk=:destinoPersona where che_estudiante_fk=:origenPk")
                        .setParameter("destinoPk", destino.getEstPk())
                        .setParameter("destinoNie", destino.getEstPersona().getPerNie())
                        .setParameter("destinoPersona", destino.getEstPersona().getPerPk())
                        .setParameter("origenPk", origen.getEstPk())
                        .executeUpdate();
            }

            //Modificar colecciones persona
            if (perOrigen.getPerAllegados() != null) {
                //Hibernate autosave
                Boolean origenTieneReferente = Boolean.FALSE;
                for (SgAllegado a : perOrigen.getPerAllegados()) {
                    if (a.getAllReferente()) {
                        origenTieneReferente = Boolean.TRUE;
                        break;
                    }
                }
                if (origenTieneReferente) {
                    for (SgAllegado a : perDestino.getPerAllegados()) {
                        a.setAllReferente(Boolean.FALSE);
                    }
                }
                for (SgAllegado a : perOrigen.getPerAllegados()) {
                    a.setAllPersonaReferenciada(perDestino);
                }
            }
            if (perOrigen.getPerDiscapacidades() != null) {
                //Guardado por guardar estudiante cascada
                if (perDestino.getPerDiscapacidades() == null) {
                    perDestino.setPerDiscapacidades(new ArrayList<>());
                }
                for (SgDiscapacidad d : perOrigen.getPerDiscapacidades()) {
                    if (!perDestino.getPerDiscapacidades().contains(d)) {
                        perDestino.getPerDiscapacidades().add(d);
                    }
                }
            }
            if (perOrigen.getPerIdentificaciones() != null) {
                //Guardado por guardar estudiante cascada
                if (perDestino.getPerIdentificaciones() == null) {
                    perDestino.setPerIdentificaciones(new ArrayList<>());
                }
                for (SgIdentificacion i : perOrigen.getPerIdentificaciones()) {
                    i.setIdePersona(perDestino);
                    if (!perDestino.getPerIdentificaciones().contains(i)) {
                        perDestino.getPerIdentificaciones().add(i);
                    }
                }
            }
            if (perOrigen.getPerTelefonos() != null) {
                //Guardado por guardar estudiante cascada
                if (perDestino.getPerTelefonos() == null) {
                    perDestino.setPerTelefonos(new ArrayList<>());
                }
                for (SgTelefono t : perOrigen.getPerTelefonos()) {
                    t.setTelPersona(perDestino);
                    if (!perDestino.getPerTelefonos().contains(t)) {
                        perDestino.getPerTelefonos().add(t);
                    }
                }
            }

            this.guardar(destino, Boolean.FALSE);
            eliminar(origen);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    private Boolean dateRangeOverlap(SgMatricula matOrigen, SgMatricula matDestino) {
        if (BooleanUtils.isTrue(matOrigen.getMatAnulada()) || BooleanUtils.isTrue(matDestino.getMatAnulada())) {
            return Boolean.FALSE;
        } else {
            if (matOrigen.getMatFechaIngreso() != null && matOrigen.getMatFechaHasta() != null && matDestino.getMatFechaIngreso() != null && matDestino.getMatFechaHasta() != null) {
                return (matOrigen.getMatFechaIngreso().isBefore(matDestino.getMatFechaHasta())) && (matOrigen.getMatFechaHasta().isAfter(matDestino.getMatFechaIngreso()));
            }
            if (matOrigen.getMatFechaIngreso() != null && matOrigen.getMatFechaHasta() == null && matDestino.getMatFechaIngreso() != null && matDestino.getMatFechaHasta() != null) {
                return (matOrigen.getMatFechaIngreso().isAfter(matDestino.getMatFechaIngreso()) && matOrigen.getMatFechaIngreso().isBefore(matDestino.getMatFechaHasta()));
            }
        }

        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroEstudiante filtro) throws GeneralException {
        try {
            EstudianteDAO DAO = new EstudianteDAO(em, seguridadBean);
            if (filtro.getEstPk() != null || filtro.getEstPersona() != null) {
                //Si filtro por pk de estudiante o persona deshabilito la seguridad
                filtro.setSecurityOperation(null);
            }
            return DAO.obtenerTotalPorFiltro(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgEstudiante
     * @throws GeneralException
     */
    public List<SgEstudiante> obtenerPorFiltro(FiltroEstudiante filtro) throws GeneralException {
        try {

            EstudianteDAO DAO = new EstudianteDAO(em, seguridadBean);
            if (filtro.getEstPk() != null || filtro.getEstPersona() != null) {
                //Si filtro por pk de estudiante o persona deshabilito la seguridad
                filtro.setSecurityOperation(null);
            }
            List<SgEstudiante> estudiantes = DAO.obtenerPorFiltro(filtro, filtro.getSecurityOperation());

            if (BooleanUtils.isTrue(filtro.getIncluirResponsableOContactoEmergencia())) {
                for (SgEstudiante e : estudiantes) {
                    FiltroAllegado filtroAll = new FiltroAllegado();
                    filtroAll.setAllPersonaReferenciadaPk(e.getEstPersona().getPerPk());
                    filtroAll.setAllEsReferenteOContactoEmergencia(Boolean.TRUE);
                    filtroAll.setIncluirCampos(new String[]{"allPersona.perNie", "allPersona.perPk",
                        "allPersona.perPrimerNombre", "allPersona.perSegundoNombre", "allPersona.perNombreBusqueda",
                        "allPersona.perPrimerApellido", "allPersona.perSegundoApellido", "allPersona.perFechaNacimiento",
                        "allVersion", "allReferente", "allContactoEmergencia"});
                    e.getEstPersona().setPerAllegados(allegadoBean.obtenerPorFiltro(filtroAll));
                }
            }
            if (BooleanUtils.isTrue(filtro.getIncluirDiscapacidades()) && estudiantes != null && !estudiantes.isEmpty()) {
                for (SgEstudiante est : estudiantes) {
                    Query discapacidades = em.createNativeQuery("select dis.* from centros_educativos.sg_estudiantes est inner join centros_educativos.sg_personas per on per.per_pk=est.est_persona\n "
                            + "inner join centros_educativos.sg_personas_discapacidades rel on rel.per_pk=per.per_pk\n "
                            + "inner join catalogo.sg_discapacidades dis on rel.dis_pk=dis.dis_pk "
                            + "where est.est_pk = :estPk", SgDiscapacidad.class)
                            .setParameter("estPk", est.getEstPk());
                    List<SgDiscapacidad> objs = discapacidades.getResultList();
                    if (est.getEstPersona() == null) {
                        est.setEstPersona(new SgPersona());
                    }
                    est.getEstPersona().setPerDiscapacidades(objs);
                }
            }

            return estudiantes;

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public void actualizarServicioSocial(List<SgEstudianteServicioSocial> estudiantes) throws GeneralException {
        try {
            CodigueraDAO<SgEstudiante> codDAO = new CodigueraDAO(em, SgEstudiante.class);
            for (SgEstudianteServicioSocial e : estudiantes) {
                SgEstudiante estg = em.find(SgEstudiante.class, e.getEstPk());
                estg.setEstRealizoServicioSocial(e.getEstRealizoServicioSocial());
                estg.setEstFechaServicioSocial(e.getEstFechaServicioSocial());
                estg.setEstDescripcionServicioSocial(e.getEstDescripcionServicioSocial());
                estg.setEstCantidadHorasServicioSocial(e.getEstCantidadHorasServicioSocial());
                EstudianteValidacionNegocio.validarServicioSocial(estg);
                codDAO.guardar(estg, null);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto indicado
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(SgEstudiante est) throws GeneralException {
        if (est != null && est.getEstPk() != null) {
            try {
                CodigueraDAO<SgEstudiante> codDAO = new CodigueraDAO<>(em, SgEstudiante.class);
                alertaBean.eliminarAlertasTempranas(est.getEstPk());
                eliminarRelacionesEstudiante(est.getEstPk());
                em.flush();
                codDAO.eliminarPorId(est.getEstPk(), null);
                em.flush();
                personaBean.eliminar(est.getEstPersona());
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Elimina el objeto indicado
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarRelacionesEstudiante(Long estPk) throws GeneralException {
        if (estPk != null) {
            try {
                if (estPk != null) {
                    CodigueraDAO<SgEstudiante> codDAO = new CodigueraDAO<>(em, SgEstudiante.class);

                    em.createNativeQuery("update centros_educativos.sg_estudiantes set est_ultima_matricula_fk=null, est_ultima_sede_fk=null, est_ultima_seccion_fk=null \n"
                            + "where est_pk=:estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    em.flush();
                    //Eliminar asistencias
                    em.createQuery("DELETE FROM SgAsistencia where asiEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();

                    //Eliminar datos de salud del estudiante
                    em.createQuery("DELETE FROM SgDatoSaludEstudiante where dseEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();

                    //Eliminar diplomados de estudiante
                    em.createQuery("DELETE FROM SgDiplomadosEstudiante where depEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();

                    //Eliminar escolaridad del estudiante
                    em.createQuery("DELETE FROM SgEscolaridadEstudiante where escEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();

                    //Eliminar estudiante valoracion
                    em.createQuery("DELETE FROM SgEstudianteValoracion where esvEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar estudiante impresion
                    em.createQuery("DELETE FROM SgEstudianteImpresion where eimEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar ficha salud
                    em.createQuery("DELETE FROM SgFichaSalud where fsaEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar manifestación violenta
                    em.createQuery("DELETE FROM SgManifestacionViolencia where mviEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar matriculas
                    em.createQuery("DELETE FROM SgMatricula where matEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar notificaciones
                    em.createQuery("DELETE FROM SgNotificacion where nfsEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar proyectos inst estudiantes
                    em.createQuery("DELETE FROM SgProyectoInstEstudiante where pieEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();

                    //Eliminar titulos
                    em.createQuery("DELETE FROM SgTitulo where titEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar solicitud traslado
                    em.createQuery("DELETE FROM SgSolicitudTraslado where sotEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar habilitacion periodo calificacion
                    em.createQuery("DELETE FROM SgHabilitacionPeriodoCalificacion where hpcEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar calificacion diplomado estudiante
                    em.createQuery("DELETE FROM SgCalificacionDiplomadoEstudiante where cdeEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar diplomas estudiante
                    em.createQuery("DELETE FROM SgDiplomaEstudiante where dieEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar medidas exame fisico
                    em.createQuery("DELETE FROM SgMedidasExamenFisico where mefEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar habitos alimentación
                    em.createQuery("DELETE FROM SgHabitosAlimentacion where halEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    //Eliminar habitos personales
                    em.createQuery("DELETE FROM SgHabitosPersonales where hapEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    em.createQuery("DELETE FROM SgCalificacionEstudiante where caeEstudiante.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    em.createQuery("DELETE FROM SgCalificacionesHistoricasEstudiante where cheEstudianteFk.estPk = :estPk")
                            .setParameter("estPk", estPk)
                            .executeUpdate();
                    em.flush();
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
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

                SgEstudianteLiteEliminar est = em.find(SgEstudianteLiteEliminar.class, id);
                
                FiltroCalificacionEstudiante fc =new FiltroCalificacionEstudiante();
                fc.setCaeEstudiantePk(est.getEstPk());
                fc.setIncluirCampos(new String[]{"caeVersion"});
                fc.setMaxResults(1L);
                List<SgCalificacionEstudiante> listCal=calificacionEstudianteBean.obtenerPorFiltro(fc);
                
                if(listCal!=null && !listCal.isEmpty()){
                    BusinessException be=new BusinessException();
                    be.addError( Errores.ERROR_EXISTEN_CALIFICACIONES_ESTUDIANTE);
                    throw be;
                }else{
                    FiltroCalificacionesHistoricasEstudiante fche=new FiltroCalificacionesHistoricasEstudiante();
                    fche.setEstPk(id);
                    fche.setIncluirCampos(new String[]{"cheVersion"});
                    fche.setMaxResults(1L);
                    List<SgCalificacionesHistoricasEstudiante> listC=calificacionesHistEstudianteBean.obtenerPorFiltro(fche);
                    if(listC!=null && !listC.isEmpty()){
                        BusinessException be=new BusinessException();
                         be.addError( Errores.ERROR_EXISTEN_CALIFICACIONES_HISTORICAS_ESTUDIANTE);
                         throw be;
                    }
                }

                CodigueraDAO<SgEstudianteLiteEliminar> codDAO = new CodigueraDAO<>(em, SgEstudianteLiteEliminar.class);
                alertaBean.eliminarAlertasTempranas(id);
                eliminarRelacionesEstudiante(id);
                codDAO.eliminar(est, null);
                
                FiltroAllegado fa=new FiltroAllegado();
                fa.setAllPersonaPk(est.getEstPersona().getPerPk());
                fa.setIncluirCampos(new String[]{"allVersion"});
                fa.setMaxResults(1L);
                List<SgAllegado> allList=allegadoBean.obtenerPorFiltro(fa);
                //Si el estudiante es familiar de alguien entonces no se borra el registro de persona
                if(allList==null || allList.isEmpty()){

                    personaBean.eliminar(est.getEstPersona());
                }
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve SgEstudiante en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return T
     */
    public SgEstudiante obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgEstudiante> respuesta = reader.createQuery().forEntitiesAtRevision(SgEstudiante.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgEstudiante ret = respuesta.get(0);
                InitializeObjectUtils.inicializarEstudianteHist(ret);
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    @Interceptors(AuditInterceptor.class)
    public void generarRegistroFichaEscolaridadEstudiante(SgRegistroFichaEstudiante registro) {
        try {

            //Validaciones
            //Validaciones de estudiante se hacen en guardarEstudiante
            //Validaciones de escolaridad se hacen en guardarEscolaridad
            BusinessException be = new BusinessException();

            if (StringUtils.isBlank(registro.getDui())
                    && StringUtils.isBlank(registro.getNumCarneResidente())
                    && StringUtils.isBlank(registro.getNumPasaporte())) {
                be.addError(Errores.ERROR_IDENTIFICACIONES_VACIO);
            }

            if (!be.getErrores().isEmpty()) {
                throw be;
            }

            SgEstudiante est = new SgEstudiante();

            SgPersona per = est.getEstPersona();

            per.setPerDui(registro.getDui());
            per.setPerPrimerNombre(registro.getPrimerNombre());
            per.setPerSegundoNombre(registro.getSegundoNombre());
            per.setPerTercerNombre(registro.getTercerNombre());
            per.setPerPrimerApellido(registro.getPrimerApellido());
            per.setPerSegundoApellido(registro.getSegundoApellido());
            per.setPerTercerApellido(registro.getTercerApellido());
            per.setPerFechaNacimiento(registro.getFechaNacimiento());

            per.setPerPartidaNacimientoPresenta(registro.getPartidaNacimiento() != null ? registro.getPartidaNacimiento() : Boolean.FALSE);
            per.setPerPartidaNacimientoAnio(registro.getPartidaNacimientoAnio());
            per.setPerPartidaNacimiento(registro.getPartidaNacimientoNumero());
            per.setPerPartidaNacimientoFolio(registro.getPartidaNacimientoFolio());
            per.setPerPartidaNacimientoLibro(registro.getPartidaNacimientoLibro());
            per.setPerPartidaNacimientoComplemento(registro.getPartidaNacimientoComplemento());

            if (registro.getPartidaNacimientoDepartamento() != null && registro.getPartidaNacimientoDepartamento() > 0L) {
                per.setPerPartidaDepartamento(em.getReference(SgDepartamento.class, registro.getPartidaNacimientoDepartamento()));
            }

            if (registro.getPartidaNacimientoMunicipio() != null && registro.getPartidaNacimientoMunicipio() > 0L) {
                per.setPerPartidaMunicipio(em.getReference(SgMunicipio.class, registro.getPartidaNacimientoMunicipio()));
            }

            if (registro.getSexoPk() != null && registro.getSexoPk() > 0L) {
                per.setPerSexo(new SgSexo(registro.getSexoPk(), 0));
            }
            if (registro.getEstadoFamiliarPk() != null && registro.getEstadoFamiliarPk() > 0L) {
                per.setPerEstadoCivil(new SgEstadoCivil(registro.getEstadoFamiliarPk(), 0));
            }
            if (registro.getNacionalidadPk() != null && registro.getNacionalidadPk() > 0L) {
                per.setPerNacionalidad(new SgNacionalidad(registro.getNacionalidadPk(), 0));
            }

            if (!StringUtils.isBlank(registro.getNumCarneResidente())) {
                SgIdentificacion iden = new SgIdentificacion();
                iden.setIdeNumeroDocumento(registro.getNumCarneResidente());
                if (registro.getPaisCarneResidentePk() != null && registro.getPaisCarneResidentePk() > 0L) {
                    iden.setIdePaisEmisor(new SgPais(registro.getPaisCarneResidentePk(), 0));
                } else {
                    //Por defecto El Salvador
                    iden.setIdePaisEmisor(new SgPais(229L, 0));
                }
                iden.setIdeTipoDocumento(new SgTipoIdentificacion(Constantes.PK_CARNE_RESIDENTE, 0));
                iden.setIdePersona(per);
                per.getPerIdentificaciones().add(iden);
            }

            if (!StringUtils.isBlank(registro.getNumPasaporte())) {
                SgIdentificacion iden = new SgIdentificacion();
                iden.setIdeNumeroDocumento(registro.getNumPasaporte());
                if (registro.getPaisDocumentoPk() != null && registro.getPaisDocumentoPk() > 0L) {
                    iden.setIdePaisEmisor(new SgPais(registro.getPaisDocumentoPk(), 0));
                }
                iden.setIdeTipoDocumento(new SgTipoIdentificacion(Constantes.PK_PASAPORTE, 0));
                iden.setIdePersona(per);
                per.getPerIdentificaciones().add(iden);
            }

            SgDireccion dir = per.getPerDireccion();

            if (registro.getZonaPk() != null && registro.getZonaPk() > 0L) {
                dir.setDirZona(em.getReference(SgZona.class, registro.getZonaPk()));
            } else {
                dir.setDirZona(em.getReference(SgZona.class, Constantes.ZONA_SIN_DATO_PK));
            }
            if (registro.getDepartamentoPk() != null && registro.getDepartamentoPk() > 0L) {
                dir.setDirDepartamento(em.getReference(SgDepartamento.class, registro.getDepartamentoPk()));
            } else {
                dir.setDirDepartamento(em.getReference(SgDepartamento.class, Constantes.DEPARTAMENTO_SIN_DATO_PK));
            }
            if (registro.getMunicipioPk() != null && registro.getMunicipioPk() > 0L) {
                dir.setDirMunicipio(em.getReference(SgMunicipio.class, registro.getMunicipioPk()));
            } else {
                dir.setDirMunicipio(em.getReference(SgMunicipio.class, Constantes.MUNICIPIO_SIN_DATO_PK));
            }
            dir.setDirDireccion(registro.getDireccion());

            //Verificar persona existente
            FiltroPersona fp = new FiltroPersona();
            fp.setDui(per.getPerDui());
            fp.setOtrasIden(per.getPerIdentificaciones());
            fp.setMaxResults(2L);
            fp.setIncluirCampos(new String[]{"perVersion"});
            List<SgPersona> listaPersonas = personaBean.obtenerPorFiltro(fp);

            if (listaPersonas.size() > 1) {
                be.addError(Errores.MULTIPLES_PERSONAS_CON_MISMA_IDENTIFICACION);
                throw be;
            } else if (listaPersonas.size() == 1) {
                SgPersona p = listaPersonas.get(0);
                //Buscamos si persona ya existe como estudiante
                FiltroEstudiante fe = new FiltroEstudiante();
                fe.setEstPersona(p.getPerPk());
                fe.setIncluirCampos(new String[]{"estVersion", "estPersona.perHabilitado"});
                List<SgEstudiante> estudiantes = this.obtenerPorFiltro(fe);
                if (!estudiantes.isEmpty()) {
                    est = estudiantes.get(0);
                } else {
                    //Seteamos persona existente a nuevo estudiante
                    SgPersona persona = personaBean.obtenerPorId(p.getPerPk());
                    est.setEstPersona(persona);
                    est.setEstEsDeTramite(Boolean.TRUE);
                    est = this.guardar(est, Boolean.FALSE);
                }
            } else {
                est.setEstEsDeTramite(Boolean.TRUE);
                est = this.guardar(est, Boolean.FALSE);
            }

            SgEscolaridadEstudiante escolaridad = new SgEscolaridadEstudiante();
            escolaridad.setEscEstudiante(est);
            escolaridad.setEscGeneradaPorEquivalencia(Boolean.TRUE);
            escolaridad.setEscCreadoCierre(Boolean.FALSE);
            escolaridad.setEscResultado(EnumPromovidoCalificacion.PROMOVIDO);
            escolaridad.setEscEqAnio(registro.getAnioLectivo());
            escolaridad.setEscEqNumeroTramite(registro.getNumTramite());
            escolaridad.setEscNombreSede(registro.getNombreSede());
            escolaridad.setEscEqFechaTramite(LocalDate.now());
            if (registro.getGradoPk() != null && registro.getGradoPk() > 0L) {
                escolaridad.setEscEqGrado(em.getReference(SgGrado.class, registro.getGradoPk()));
            }
            if (registro.getProgramaEducativoPk() != null && registro.getProgramaEducativoPk() > 0L) {
                escolaridad.setEscEqProgramaEducativo(em.getReference(SgProgramaEducativo.class, registro.getProgramaEducativoPk()));
            }
            if (registro.getOpcionPk() != null && registro.getOpcionPk() > 0L) {
                escolaridad.setEscEqOpcion(em.getReference(SgOpcion.class, registro.getOpcionPk()));
            }

            if (registro.getPlanEstudioPk() != null && registro.getPlanEstudioPk() > 0L) {
                escolaridad.setEscEqPlanEstudio(em.getReference(SgPlanEstudio.class, registro.getPlanEstudioPk()));
            }

            escolaridadEstudianteBean.guardar(escolaridad);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve cantidad de asistencias, inasistencias justificadas,
     * inasistencias injustificadas desde la vista materializada para el año
     * actual
     *
     * @param estudiantePk Long
     * @return SgAsistenciasInasistenciasEstudianteResponse
     */
    public SgAsistenciasInasistenciasEstudianteResponse obtenerCantidadAsistenciasInasistenciasAnioActual(Long estudiantePk) throws GeneralException {
        try {

            String query = " select asistencias, inasistencias_justificadas, inasistencias_no_justificadas  "
                    + "from centros_educativos.sg_estudiantes_asistencias_" + LocalDate.now().getYear() + " t "
                    + "where estudiante = :estPk ";

            Query q = em.createNativeQuery(query)
                    .setParameter("estPk", estudiantePk);

            List<Object[]> res = q.getResultList();

            if (!res.isEmpty()) {

                Object[] ob = res.get(0);

                Integer asistencias = ((Integer) ob[0]);
                Integer inasistenciasJustificadas = ((Integer) ob[1]);
                Integer inasistenciasInjustificadas = ((Integer) ob[2]);

                SgAsistenciasInasistenciasEstudianteResponse ret = new SgAsistenciasInasistenciasEstudianteResponse();
                if (asistencias != null) {
                    ret.setAsistencias(asistencias);
                }
                if (inasistenciasInjustificadas != null) {
                    ret.setInasistenciasInjustificadas(inasistenciasInjustificadas);
                }
                if (inasistenciasJustificadas != null) {
                    ret.setInasistenciasJustificadas(inasistenciasJustificadas);
                }

                return ret;
            }

            return new SgAsistenciasInasistenciasEstudianteResponse();

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve probabilidad de deserción del estudiante
     *
     * @param id Long
     * @return SgPrediccionDesercionResponse
     */
    public SgPrediccionDesercionResponse prediccionDesercion(Long estudiantePk) throws GeneralException {
        if (estudiantePk == null) {
            throw new IllegalStateException();
        }
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        int timeout = 30 * 1000; //30 segundos
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {

            SgPrediccionDesercionRequest consulta = new SgPrediccionDesercionRequest();

            FiltroEstudiante filEst = new FiltroEstudiante();
            filEst.setEstPk(estudiantePk);
            filEst.setSecurityOperation(null);
            filEst.setIncluirCampos(new String[]{"estPersona.perFechaNacimiento", "estPersona.perSexo.sexPk", "estPersona.perEmbarazo", "estPersona.perEstadoCivil.eciPk",
                "estPersona.perCantidadHijos", "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graPk",
                "estUltimaMatricula.matRepetidor", "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTipo",
                "estPersona.perDireccion.dirZona.zonPk"});
            List<SgEstudiante> ests = this.obtenerPorFiltro(filEst);

            SgEstudiante est = ests.get(0);

            SgAsistenciasInasistenciasEstudianteResponse asInas = this.obtenerCantidadAsistenciasInasistenciasAnioActual(estudiantePk);

            consulta.setEst_edad(Period.between(est.getEstPersona().getPerFechaNacimiento(), LocalDate.now()).getYears());
            consulta.setEst_embarazo(BooleanUtils.isTrue(est.getEstPersona().getPerEmbarazo()) ? 1 : 0);
            consulta.setEst_estado_civil(est.getEstPersona().getPerEstadoCivil().getEciPk());
            consulta.setEst_sexo(est.getEstPersona().getPerSexo().getSexPk());
            consulta.setEst_grado(est.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
            consulta.setEst_hijos(est.getEstPersona().getPerCantidadHijos() != null ? est.getEstPersona().getPerCantidadHijos() : 0);
            consulta.setEst_inasistencias(asInas.getInasistenciasInjustificadas() + asInas.getInasistenciasJustificadas());
            consulta.setEst_repetidor(est.getEstUltimaMatricula().getMatRepetidor() ? 1 : 0);
            consulta.setEst_tipo_sede(est.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduSede().getSedTipo());
            consulta.setEst_zona(est.getEstPersona().getPerDireccion().getDirZona().getZonPk());

            SSLContextBuilder builder = new SSLContextBuilder();

            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout).build();

            httpclient = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(
                    sslsf).build();

            URIBuilder ubuilder = new URIBuilder(System.getProperty("service.prediccion.baseUrl"));
            URI uri = ubuilder.build();
            HttpPost post = new HttpPost(uri);

            post.addHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

            StringEntity postingString = new StringEntity(om.writeValueAsString(consulta));
            post.setEntity(postingString);

            response = httpclient.execute(post);

            if ((response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
                SgPrediccionDesercionResponse respuesta = om.readValue(response.getEntity().getContent(), SgPrediccionDesercionResponse.class);
                return respuesta;
            } else {
                throw new Exception("ERROR_HTTP_STATUS_" + response.getStatusLine().getStatusCode());
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {
            }
        }
    }

}
