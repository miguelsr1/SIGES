/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroPersona;
import sv.gob.mined.siges.filtros.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.PersonaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.PersonaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAllegado;
import sv.gob.mined.siges.persistencia.entidades.SgDatosResidencialesPersona;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteLiteEliminar;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativa;
import sv.gob.mined.siges.persistencia.entidades.SgPersonalSedeEducativaLite;
import sv.gob.mined.siges.persistencia.entidades.SgTelefono;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDiscapacidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTrastornoAprendizaje;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.utilidades.InitializeObjectUtils;

@Stateless
@Traced
public class PersonaBean {

    private static final Logger LOGGER = Logger.getLogger(PersonaBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private NieBean nieBean;

    @Inject
    private UsuarioBean usuarioBean;

    @Inject
    @ConfigProperty(name = "email-valid-pattern")
    private String emailPattern;

    @Inject
    private DireccionBean direccionBean;

    @Inject
    private DatosResidencialesPersonaBean datosResidencialesBean;
    
    @Inject
    private EstudianteBean estudianteBean;
    
    @Inject
    private AlertaBean alertaBean;
    
    @Inject
    private PersonalSedeEducativaBean personalBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPersona
     * @throws GeneralException
     */
    public SgPersona obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgPersona> dao = new CodigueraDAO<>(em, SgPersona.class);
                SgPersona ret = dao.obtenerPorId(id, null);
                InitializeObjectUtils.inicializarPersona(ret);
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
                CodigueraDAO<SgPersona> codDAO = new CodigueraDAO<>(em, SgPersona.class);
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
     * @param entity SgPersona
     * @return SgPersona
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgPersona guardar(SgPersona entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion              
                if (PersonaValidacionNegocio.validar(entity, emailPattern)) {
                    CodigueraDAO<SgPersona> codDAO = new CodigueraDAO<>(em, SgPersona.class);
                    if (BooleanUtils.isNotTrue(entity.getPerTieneDiscapacidades())) {
                        entity.setPerDiscapacidades(null);
                    }

                    if (entity.getPerPk() == null && entity.getPerDatosResidenciales() == null) {
                        entity.setPerDatosResidenciales(new SgDatosResidencialesPersona());
                    }
                    SgDatosResidencialesPersona datosResidenciales = entity.getPerDatosResidenciales();
                    entity.setPerDatosResidenciales(null);

                    //Verificar si se modificó algún dato validado
                    //Solo se puede estar validado por uno a la vez
                    if (entity.getPerPk() != null
                            && (BooleanUtils.isTrue(entity.getPerCunValidadoRNPN())
                            || BooleanUtils.isTrue(entity.getPerDuiValidadoRNPN())
                            || BooleanUtils.isTrue(entity.getPerPartidaNacValidadaRNPN()))) {

                        FiltroPersona fper = new FiltroPersona();
                        fper.setPerPk(entity.getPerPk());
                        fper.setIncluirCampos(new String[]{"perDui", "perCun", "perPrimerNombre", "perSegundoNombre", "perTercerNombre",
                            "perPrimerApellido", "perSegundoApellido", "perTercerApellido", "perFechaNacimiento", "perPartidaNacimientoAnio", "perPartidaNacimiento",
                            "perPartidaNacimientoFolio", "perPartidaNacimientoLibro", "perPartidaNacimientoComplemento"});
                        SgPersona anterior = obtenerPorFiltro(fper).get(0);

                        // TODO: validar sexo ?
                        Boolean cambioDatos = Boolean.FALSE;

                        if (BooleanUtils.isTrue(entity.getPerDuiValidadoRNPN())) {
                            if (!Objects.equals(entity.getPerDui(), anterior.getPerDui())) {
                                cambioDatos = Boolean.TRUE;
                            }
                        }

                        if (BooleanUtils.isTrue(entity.getPerCunValidadoRNPN())) {
                            if (!Objects.equals(entity.getPerCun(), anterior.getPerCun())) {
                                cambioDatos = Boolean.TRUE;
                            }
                        }

                        if (BooleanUtils.isTrue(entity.getPerPartidaNacValidadaRNPN())) {
                            if (!Objects.equals(entity.getPerPartidaNacimiento(), anterior.getPerPartidaNacimiento())
                                    || !Objects.equals(entity.getPerPartidaNacimientoAnio(), anterior.getPerPartidaNacimientoAnio())
                                    || !Objects.equals(entity.getPerPartidaNacimientoFolio(), anterior.getPerPartidaNacimientoFolio())
                                    || !Objects.equals(entity.getPerPartidaNacimientoLibro(), anterior.getPerPartidaNacimientoLibro())
                                    || !Objects.equals(entity.getPerPartidaNacimientoComplemento(), anterior.getPerPartidaNacimientoComplemento())) {
                                cambioDatos = Boolean.TRUE;
                            }
                        }

                        if (!entity.getPerNombreCompleto().equals(anterior.getPerNombreCompleto())) {
                            cambioDatos = Boolean.TRUE;
                        }

                        if (!Objects.equals(entity.getPerFechaNacimiento(), anterior.getPerFechaNacimiento())) {
                            cambioDatos = Boolean.TRUE;
                        }

                        if (cambioDatos) {
                            entity.setPerDuiValidadoRNPN(Boolean.FALSE);
                            entity.setPerCunValidadoRNPN(Boolean.FALSE);
                            entity.setPerPartidaNacValidadaRNPN(Boolean.FALSE);
                        }

                    }

                    SgPersona p = codDAO.guardar(entity, null);

                    if (datosResidenciales != null) {
                        datosResidenciales.setPerPersona(p);
                        datosResidenciales = datosResidencialesBean.guardar(datosResidenciales);
                        p.setPerDatosResidenciales(datosResidenciales);
                    }

                    return p;
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
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroPersona filtro) throws GeneralException {
        try {
            PersonaDAO DAO = new PersonaDAO(em);
            return DAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersona
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgPersona> obtenerPorFiltro(FiltroPersona filtro) throws GeneralException {
        try {
            PersonaDAO DAO = new PersonaDAO(em);
            return DAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio.
     * Utiliza hibernate search.
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltroLucene(FiltroPersona filtro) throws GeneralException {
        try {
            PersonaDAO DAO = new PersonaDAO(em);
            return DAO.obtenerTotalPorFiltroLucene(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio. Utiliza
     * hibernate search.
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersona
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgPersona> obtenerPorFiltroLucene(FiltroPersona filtro) throws GeneralException {
        try {
            PersonaDAO DAO = new PersonaDAO(em);
            List<SgPersona> plist = DAO.obtenerPorFiltroLucene(filtro);
            for (SgPersona p : plist) {
                if (p.getPerEstudiante() != null) {
                    if (p.getPerEstudiante().getEstUltimaMatricula() != null) {
                        p.getPerEstudiante().getEstUltimaMatricula().getMatPk();
                    }
                }
            }
            return plist;
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
    public void eliminar(SgPersona per) throws GeneralException {
        if (per != null && per.getPerPk() != null) {
            try {
                CodigueraDAO<SgPersona> codDAO = new CodigueraDAO<>(em, SgPersona.class);

   
                em.createQuery("DELETE FROM SgDatosResidencialesPersona where perPk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();
                //Eliminar relacion con allegados
                em.createQuery("DELETE FROM SgAllegado where allPersona.perPk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar relacion con allegados
                em.createQuery("DELETE FROM SgAllegado where allPersonaReferenciada.perPk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar identificaciones
                em.createQuery("DELETE FROM SgIdentificacion where idePersona.perPk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar discapacidades
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_discapacidades where per_pk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar trastornos aprendizaje
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_trastornos_aprendizaje where per_pk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar trastornos aprendizaje
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_trastornos_aprendizaje where per_pk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar telefonos
                em.createNativeQuery("DELETE FROM centros_educativos.sg_telefonos where tel_persona = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar terapias de persona
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_terapias where per_pk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar terapias de sg_personas_referencias_apoyo
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_referencias_apoyo where per_pk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();

                //Eliminar datos residenciales
                em.createNativeQuery("DELETE FROM centros_educativos.sg_datos_residenciales_personas where per_pk = :perPk")
                        .setParameter("perPk", per.getPerPk())
                        .executeUpdate();
                codDAO.eliminar(per, null);
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
                CodigueraDAO<SgPersona> codDAO = new CodigueraDAO<>(em, SgPersona.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve SgPersona en una determinada revision
     *
     * @param id Long
     * @param revision Long
     * @return T
     */
    public SgPersona obtenerEnRevision(Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<SgPersona> respuesta = reader.createQuery().forEntitiesAtRevision(SgPersona.class, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                SgPersona ret = respuesta.get(0);
                //TODO
                //InitializeObjectUtils.inicializarPersonaHist(ret);
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void reindexar() {
        try {
            FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
            fullTextEntityManager.createIndexer(SgPersona.class)
                    .transactionTimeout(25000)
                    .startAndWait();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void purgeIndex() {
        try {
            FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
            fullTextEntityManager.purgeAll(SgPersona.class);
            LOGGER.log(Level.INFO, "PURGE ALL...");

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public void updateIndex() {
        try {
            LOGGER.log(Level.INFO, "Updating lucene personas index...");
            //TODO: mejorar. Ver de hacer una entidad lite de persona para no cargar el estudiante

            Session session = em.unwrap(Session.class);
            FullTextSession fullTextSession = Search.getFullTextSession(session);
            ScrollableResults results = fullTextSession.createCriteria(SgPersona.class)
                    .add(Restrictions.eq("perLuceneIndexUpdated", false))
                    .setReadOnly(true)
                    .setCacheable(false)
                    .setFetchSize(50)
                    .setMaxResults(1000)
                    .scroll(ScrollMode.FORWARD_ONLY);
            fullTextSession.setFlushMode(FlushMode.MANUAL);
            fullTextSession.setCacheMode(CacheMode.IGNORE);
            int batch = 0;
            List<Long> personasPk = new ArrayList<>();
            while (results.next()) {
                SgPersona p = (SgPersona) results.get(0);
                personasPk.add(p.getPerPk());
                fullTextSession.index(p);
                if (batch % 60 == 0) {
                    fullTextSession.flushToIndexes();
                    fullTextSession.clear();
                }
                LOGGER.log(Level.INFO, "Updated personas indexes: " + batch);
                batch++;
            }
            fullTextSession.flushToIndexes();
            fullTextSession.clear();

            LOGGER.log(Level.INFO, "Updated personas indexes: " + batch);

            if (!personasPk.isEmpty()) {
                em.createQuery("UPDATE SgPersona SET perLuceneIndexUpdated = true where perPk IN (:ids)")
                        .setParameter("ids", personasPk)
                        .executeUpdate();
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public SgPersona generarNie(SgPersona entity) throws GeneralException {
        try {
            entity.setPerNie(nieBean.generarNie().getNiePk());
            return guardar(entity);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Actualiza la información de seguridad
     *
     * @param persona
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void actualizarSeguridad(SgPersona persona) throws GeneralException {
        if (persona != null) {
            try {
                SgUsuario usu = null;
                if (persona.getPerUsuarioPk() != null) {
                    usu = usuarioBean.obtenerPorId(persona.getPerUsuarioPk());
                } else {
//                    if (!StringUtils.isBlank(persona.getPerDui())) {
//                        FiltroUsuario fu = new FiltroUsuario();
//                        fu.setCodigoExacto(persona.getPerDui());
//                        List<SgUsuario> usuario = usuarioBean.obtenerPorFiltro(fu);
//                        usu = usuario.isEmpty() ? null : usuario.get(0);
//
//                    } else if (!StringUtils.isBlank(persona.getPerNip())) {
//                        FiltroUsuario fu = new FiltroUsuario();
//                        fu.setCodigoExacto(persona.getPerNip());
//                        List<SgUsuario> usuario = usuarioBean.obtenerPorFiltro(fu);
//                        usu = usuario.isEmpty() ? null : usuario.get(0);
//                    }
                }
                if (usu != null) {

                    Boolean cambioDatos = Boolean.FALSE;

                    //Consultar la persona actual
                    FiltroPersona fper = new FiltroPersona();
                    fper.setPerPk(persona.getPerPk());
                    fper.setIncluirCampos(new String[]{"perDui", "perNip", "perPrimerNombre", "perSegundoNombre", "perTercerNombre",
                        "perPrimerApellido", "perSegundoApellido", "perTercerApellido", "perEmail"});
                    SgPersona anterior = obtenerPorFiltro(fper).get(0);

//                    if (!StringUtils.isBlank(persona.getPerDui())){
//                        if (!persona.getPerDui().equals(anterior.getPerDui())) {
//                            usu.setUsuCodigo(persona.getPerDui());
//                            cambioDatos = Boolean.TRUE;
//                        }
//                    } else if (!StringUtils.isBlank(persona.getPerNip())) {
//                        if (!persona.getPerNip().equals(anterior.getPerNip())) {
//                            usu.setUsuCodigo(persona.getPerNip());
//                            cambioDatos = Boolean.TRUE;
//                        }
//                    }
                    if (!persona.getPerNombreCompleto().equals(anterior.getPerNombreCompleto())) {
                        usu.setUsuNombre(persona.getPerNombreCompleto());
                        cambioDatos = Boolean.TRUE;
                    }

                    if (!StringUtils.isBlank(persona.getPerEmail())) {
                        if (!persona.getPerEmail().equals(anterior.getPerEmail())) {
                            usu.setUsuEmail(persona.getPerEmail());
                            cambioDatos = Boolean.TRUE;
                        }
                    } else {
                        if (!StringUtils.isBlank(anterior.getPerEmail())) {
                            cambioDatos = Boolean.TRUE;
                        }
                    }
                    usu.setUsuPersonaPk(persona.getPerPk());

                    if (BooleanUtils.isTrue(cambioDatos)) {
                        usuarioBean.guardar(usu);
                    }
                }

            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void marcarPersonaParaValidacionDUIRNPN(Long perPk) throws GeneralException {
        try {
            em.createQuery("UPDATE SgPersona set perDuiPendienteValidacionRNPN = true where perPk = :perPk")
                    .setParameter("perPk", perPk)
                    .executeUpdate();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void desmarcarPersonaParaValidacionDUIRNPN(Long perPk) throws GeneralException {
        try {
            em.createQuery("UPDATE SgPersona set perDuiPendienteValidacionRNPN = false where perPk = :perPk")
                    .setParameter("perPk", perPk)
                    .executeUpdate();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void marcarPersonaParaValidacionCUNRNPN(Long perPk) throws GeneralException {
        try {
            em.createQuery("UPDATE SgPersona set perCunPendienteValidacionRNPN = true where perPk = :perPk")
                    .setParameter("perPk", perPk)
                    .executeUpdate();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void desmarcarPersonaParaValidacionCUNRNPN(Long perPk) throws GeneralException {
        try {
            em.createQuery("UPDATE SgPersona set perCunPendienteValidacionRNPN = false where perPk = :perPk")
                    .setParameter("perPk", perPk)
                    .executeUpdate();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void marcarPersonaParaValidacionPartidaNacimientoRNPN(Long perPk) throws GeneralException {
        try {
            em.createQuery("UPDATE SgPersona set perPartidaNacPendienteValidacionRNPN = true where perPk = :perPk")
                    .setParameter("perPk", perPk)
                    .executeUpdate();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void desmarcarPersonaParaValidacionPartidaNacimientoRNPN(Long perPk) throws GeneralException {
        try {
            em.createQuery("UPDATE SgPersona set perPartidaNacPendienteValidacionRNPN = false where perPk = :perPk")
                    .setParameter("perPk", perPk)
                    .executeUpdate();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgDiscapacidad> obtenerDiscapacidades(Long perPk) {
        try {
            PersonaDAO DAO = new PersonaDAO(em);
            return DAO.obtenerDiscapacidades(perPk);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgTrastornoAprendizaje> obtenerTrastornos(Long perPk) {
        if (perPk != null) {
            String query = "select tra.* from centros_educativos.sg_personas_trastornos_aprendizaje rel inner join catalogo.sg_trastornos_aprendizaje tra\n"
                    + " on tra.tra_pk=rel.tra_pk\n"
                    + " where rel.per_pk=:perPk";
            javax.persistence.Query q = em.createNativeQuery(query, SgTrastornoAprendizaje.class);
            q.setParameter("perPk", perPk);

            List<SgTrastornoAprendizaje> objs = q.getResultList();
            return objs;
        } else {
            return new ArrayList();
        }
    }
    
    //persona A es la persona que queda en el sistema
    //persona B es la persona que se borra y se le pasan sus datos a la persona A
    //En esta funcionalidad se le asignan las identificaciones de B a A y algunos datos de persona
    //El resto de las relaciones de B son eliminadas (por ejemplo estudiante, personal)
    public void unirPersona(Long personaApk, Long personaBpk)throws GeneralException {
        try{           
            if(personaApk==null || personaBpk==null){
                BusinessException be=new BusinessException();
                be.addError(Errores.ERROR_PERSONA_UNIR_VACIO);
                throw be;
            }
            if(personaApk.equals(personaBpk)){
                BusinessException be=new BusinessException();
                be.addError(Errores.ERROR_PERSONA_UNIR_MISMA_PERSONA);
                throw be;
            }
            
            SgPersona personaA =  em.find(SgPersona.class, personaApk);
            SgPersona personaB = em.find(SgPersona.class, personaBpk);    
                                
            em.createNativeQuery("update centros_educativos.sg_personas set per_dui=null, per_nie=null,per_cun=null,per_nip=null,per_nit=null,per_nup=null \n" +
                " where per_pk=:perPk")
             .setParameter("perPk", personaBpk)
             .executeUpdate();
            em.flush();
            
            //Copiar campos simples persona que no sean null
            PropertyUtils.describe(personaB).entrySet().stream()
                    .filter(e -> e.getValue() != null
                    && !e.getKey().equals("class")
                    && !e.getKey().equals("perPk")
                    && !e.getKey().equals("perEstudiante")
                    && !e.getKey().equals("perDatosResidenciales")
                    && !e.getKey().equals("perVersion")
                    && !Collection.class.isAssignableFrom(e.getValue().getClass()))
                    .forEach(e -> {
                        try {
                            if(PropertyUtils.getProperty(personaA, e.getKey())==null){
                                PropertyUtils.setProperty(personaA, e.getKey(), e.getValue());
                            }
                        } catch (Exception ex) {
                        }
                    });
            
            if(personaB.getPerIdentificaciones()!=null && !personaB.getPerIdentificaciones().isEmpty()){
                for(SgIdentificacion ident:personaB.getPerIdentificaciones()){
                    ident.setIdePersona(personaA);
                }
            }

            
            //Modificar colecciones persona
            if (personaB.getPerAllegados() != null) {               
                
                for (SgAllegado a : personaB.getPerAllegados()) {
                    a.setAllReferente(Boolean.FALSE);
                    a.setAllPersonaReferenciada(personaA);
                }
            }
            
            
           em.createNativeQuery("update centros_educativos.sg_allegados set all_persona=:perPkA where all_persona=:perPkB")
                            .setParameter("perPkA", personaApk)
                            .setParameter("perPkB", personaBpk)
                            .executeUpdate();     
           
            if (personaB.getPerDiscapacidades() != null) {
                //Guardado por guardar estudiante cascada
                if (personaA.getPerDiscapacidades() == null) {
                    personaA.setPerDiscapacidades(new ArrayList<>());
                }
                for (SgDiscapacidad d : personaB.getPerDiscapacidades()) {
                    if (!personaA.getPerDiscapacidades().contains(d)) {
                        personaA.getPerDiscapacidades().add(d);
                    }
                }
            }
            if (personaB.getPerIdentificaciones() != null) {
                //Guardado por guardar estudiante cascada
                if (personaA.getPerIdentificaciones() == null) {
                    personaA.setPerIdentificaciones(new ArrayList<>());
                }
                for (SgIdentificacion i : personaB.getPerIdentificaciones()) {
                    i.setIdePersona(personaA);
                    if (!personaA.getPerIdentificaciones().contains(i)) {
                        personaA.getPerIdentificaciones().add(i);
                    }
                }
            }
            if (personaB.getPerTelefonos() != null) {
                //Guardado por guardar estudiante cascada
                if (personaA.getPerTelefonos() == null) {
                    personaA.setPerTelefonos(new ArrayList<>());
                }
                for (SgTelefono t : personaB.getPerTelefonos()) {
                    t.setTelPersona(personaA);
                    if (!personaA.getPerTelefonos().contains(t)) {
                        personaA.getPerTelefonos().add(t);
                    }
                }
            }
            
            em.flush();
            
            //Si personaB tiene estudiante se borra con todas las relaciones
            FiltroEstudiante fe=new FiltroEstudiante();
            fe.setEstPersona(personaB.getPerPk());
            fe.setIncluirCampos(new String[]{"estVersion"});
            List<SgEstudiante> listEst=estudianteBean.obtenerPorFiltro(fe);            
            if(listEst!=null && !listEst.isEmpty()){
                
                alertaBean.eliminarAlertasTempranas(listEst.get(0).getEstPk());
                estudianteBean.eliminarRelacionesEstudiante(listEst.get(0).getEstPk());
                em.flush();
                CodigueraDAO<SgEstudianteLiteEliminar> codDAOEst = new CodigueraDAO<>(em, SgEstudianteLiteEliminar.class);
                codDAOEst.eliminarPorId(listEst.get(0).getEstPk(), null);
            }
            
            //Si personaB tiene personal se borra con todas sus relaciones
            FiltroPersonalSedeEducativa fse=new FiltroPersonalSedeEducativa();
            fse.setPerPk(personaB.getPerPk());
            fse.setIncluirCampos(new String[]{"pseVersion"});
            List<SgPersonalSedeEducativa> personalList=personalBean.obtenerPorFiltro(fse);
            
            if(personalList!=null && !personalList.isEmpty()){
                personalBean.eliminarRelacionesDePersonal(personalList.get(0).getPsePk());
                CodigueraDAO<SgPersonalSedeEducativaLite> codDAOPer = new CodigueraDAO<>(em, SgPersonalSedeEducativaLite.class);
                codDAOPer.eliminarPorId(personalList.get(0).getPsePk(), null);
            }

            //Se elimina personaB
     
                //Eliminar discapacidades
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_discapacidades where per_pk = :perPk")
                        .setParameter("perPk", personaBpk)
                        .executeUpdate();

     

                //Eliminar trastornos aprendizaje
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_trastornos_aprendizaje where per_pk = :perPk")
                        .setParameter("perPk", personaBpk)
                        .executeUpdate();

             
                //Eliminar terapias de persona
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_terapias where per_pk = :perPk")
                        .setParameter("perPk", personaBpk)
                        .executeUpdate();

                //Eliminar terapias de sg_personas_referencias_apoyo
                em.createNativeQuery("DELETE FROM centros_educativos.sg_personas_referencias_apoyo where per_pk = :perPk")
                        .setParameter("perPk", personaBpk)
                        .executeUpdate();

                //Eliminar datos residenciales
                em.createNativeQuery("DELETE FROM centros_educativos.sg_datos_residenciales_personas where per_pk = :perPk")
                        .setParameter("perPk", personaBpk)
                        .executeUpdate();
            em.flush();

            em.createNativeQuery("DELETE FROM centros_educativos.sg_personas where per_pk = :perPk")
                .setParameter("perPk", personaBpk)
                .executeUpdate();
            
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

}
