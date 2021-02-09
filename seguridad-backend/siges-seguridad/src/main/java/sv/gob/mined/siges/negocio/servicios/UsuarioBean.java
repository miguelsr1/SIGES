/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.security.OperationSecurity;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgActualizarPerfil;
import sv.gob.mined.siges.dto.SgCertificateRequest;
import sv.gob.mined.siges.dto.SgNotification;
import sv.gob.mined.siges.dto.SgUsuarioContextoId;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroRol;
import sv.gob.mined.siges.filtros.FiltroUsuario;
import sv.gob.mined.siges.filtros.FiltroUsuarioRol;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.UsuarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.UsuarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgContexto;
import sv.gob.mined.siges.persistencia.entidades.SgRol;
import sv.gob.mined.siges.persistencia.entidades.SgUsuario;
import sv.gob.mined.siges.persistencia.entidades.SgUsuarioRol;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Stateless
public class UsuarioBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    @ConfigProperty(name = "users.defaultpassword", defaultValue = "BienvenidoSiges")
    private String defaultPassword;

    @Inject
    private ConfiguracionBean configBean;

    @Inject
    private RolBean rolBean;

    @Inject
    private UsuarioRolBean usuarioRolBean;

    @Inject
    private EJBCABean ejbcaBean;

    @Inject
    private Event<SgNotification> notificationEvent;

    @Inject
    private Event<SgCertificateRequest> certificateRequestEvent;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgUsuario
     * @throws GeneralException
     */
    public SgUsuario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                return codDAO.obtenerPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve los codigos de las operaciones del usuario con el codigo
     * indicado
     *
     * @param codigo String
     * @return Lista de String
     * @throws GeneralException
     */
    public List<String> obtenerOperacionesPorUsuarioCodigo(String codigo, List<Long> categoriasOperacionPk) throws GeneralException {
        if (codigo != null) {
            try {

                String query = "Select operacion_codigo from " + Constantes.SCHEMA_SEGURIDAD + ".permisos where usuario_codigo = :cod";

                if (categoriasOperacionPk != null && !categoriasOperacionPk.isEmpty()) {
                    query = query + " and operacion_categoria IN (:catoperacion)";
                }

                Query q = em.createNativeQuery(query);

                q.setParameter("cod", codigo);

                if (categoriasOperacionPk != null && !categoriasOperacionPk.isEmpty()) {
                    q.setParameter("catoperacion", categoriasOperacionPk);
                }

                return q.getResultList();
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve lista de OperationSecurity para usuario y operacion dados
     * indicado
     *
     * @param operacion String
     * @param codigousuario String
     * @return Lista de OperationSecurity
     * @throws GeneralException
     */
    public List<OperationSecurity> obtenerOperacionesSeguridad(String operacion, String codigousuario, List<Long> categoriasOperacionPk) throws GeneralException {
        try {
            String query = "Select operacion_codigo, ambito, operacion_cascada, contexto, regla from " + Constantes.SCHEMA_SEGURIDAD + ".permisos "
                    + "where usuario_codigo = :cod";

            if (!StringUtils.isEmpty(operacion)) {
                query = query + " and operacion_codigo = :opcodigo";
            }
            if (categoriasOperacionPk != null && !categoriasOperacionPk.isEmpty()) {
                query = query + " and operacion_categoria IN (:catoperacion)";
            }

            Query q = em.createNativeQuery(query);

            q.setParameter("cod", codigousuario);

            if (!StringUtils.isEmpty(operacion)) {
                q.setParameter("opcodigo", operacion);
            }
            if (categoriasOperacionPk != null && !categoriasOperacionPk.isEmpty()) {
                q.setParameter("catoperacion", categoriasOperacionPk);
            }

            List<OperationSecurity> ret = new ArrayList<>();

            List<Object[]> objs = (List<Object[]>) q.getResultList();
            for (Object[] ob : objs) {
                OperationSecurity op = new OperationSecurity(
                        (String) ob[0],
                        (String) ob[1],
                        (boolean) ob[2],
                        ob[3] != null ? ((BigInteger) ob[3]).longValue() : null,
                        (String) ob[4]
                );
                ret.add(op);
            }
            return ret;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Cambio de contraseña.
     *
     * @param codigoUsuario Código del usuario.
     * @param passwordActual Contraseña actual hasheada.
     * @param passwordNueva Nueva contraseña a asignar hasheada.
     * @return
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public boolean cambiarPassword(String codigoUsuario, String passwordActual, String passwordNueva) throws GeneralException {
        try {
            SgUsuario usu = this.obtenerPorCodigo(codigoUsuario);
            if (usu != null) {

                if (usu.getUsuPassword().equals(passwordActual)) {

                    CodigueraDAO<SgUsuario> usuDAO = new CodigueraDAO<>(em, SgUsuario.class);
                    usu.setUsuPassword(passwordNueva);
                    usu.setUsuPassExpired(0);
                    usu.setUsuUpdatePass(LocalDateTime.now());
                    usuDAO.guardar(usu);

                } else {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_CONTRASENA_ACTUAL_INCORRECTA);
                    throw be;
                }

                return true;

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return false;
    }

    /**
     * Devuelve el usuario por codigo
     *
     * @param codigo String
     * @return SgUsuario
     * @throws GeneralException
     */
    public SgUsuario obtenerPorCodigo(String codigo) throws GeneralException {
        if (codigo != null) {
            try {
                FiltroUsuario filtro = new FiltroUsuario();
                filtro.setCodigoExacto(codigo);
                List<SgUsuario> usuarios = this.obtenerPorFiltro(filtro);
                if (!usuarios.isEmpty()) {
                    return usuarios.get(0);
                }
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgUsuario
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuario guardar(SgUsuario entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (UsuarioValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                    if (entity.getUsuPk() == null) {
                        entity.setUsuPassword(SofisStringUtils.encodeSHA256Hex(defaultPassword));
                    }

                    return codDAO.guardar(entity);
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
     * Revoca certificados para usuario
     *
     * @param codigo String
     * @return
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void revocarCertificadosUsuario(String codigo) throws GeneralException {
        try {
            if (codigo != null) {
                ejbcaBean.revocarUsuario(codigo);
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Genera certificado para usuario
     *
     * @param entity SgUsuario
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public byte[] generarCertificado(SgUsuario entity, String certificatePassword) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                Boolean generacionActivada = Boolean.parseBoolean(configBean.obtenerPorCodigo(Constantes.CONFIG_GENERACION_CERTIFICADOS_ACTIVADA).getConValor());
                if (BooleanUtils.isNotTrue(generacionActivada)) {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_GENERACION_CERTIFICADOS_DESACTIVADA);
                    throw be;
                }
                String dominiosConf = configBean.obtenerPorCodigo(Constantes.CONFIG_DOMINIO_MAIL_GENERACION_CERTIFICADOS).getConValor();

                List<String> dominios = Arrays.asList(dominiosConf.split(","));

                if (entity.getUsuEmail() != null) {
                    String userEmailDomain = entity.getUsuEmail().substring(entity.getUsuEmail().indexOf("@") + 1);
                    if (!dominios.contains(userEmailDomain)) {
                        BusinessException be = new BusinessException();
                        be.addError(Errores.ERROR_GENERACION_CERTIFICADOS_DOMINIO_MAIL_NO_HABILITADO);
                        throw be;
                    }
                } else {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_GENERACION_CERTIFICADOS_DOMINIO_MAIL_NO_HABILITADO);
                    throw be;
                }

                if (entity.getUsuCodigo() != null) {

                    SgCertificateRequest req = new SgCertificateRequest();
                    req.setCodigo(entity.getUsuCodigo());
                    req.setEmail(entity.getUsuEmail());
                    req.setNombre(entity.getUsuNombre());
                    req.setPassword(certificatePassword);

                    return ejbcaBean.generarCertificado(req);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Actualiza perfil de usuario
     *
     * @param nuevoPerfil
     * @param codigoUsuario
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuario actualizarMiPerfil(SgActualizarPerfil nuevoPerfil, String codigoUsuario) throws GeneralException {
        try {
            if (codigoUsuario != null) {
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                SgUsuario usu = this.obtenerPorCodigo(codigoUsuario);
                usu.setUsuNombre(nuevoPerfil.getNombre());
                usu.setUsuEmail(nuevoPerfil.getEmail());
                return codDAO.guardar(usu);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Acepta terminos de usuario
     *
     * @param entity usuPk
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuario aceptarTerminos(String codigoUsuario) throws GeneralException {
        try {
            if (codigoUsuario != null) {
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                SgUsuario u = this.obtenerPorCodigo(codigoUsuario);
                u.setUsuAceptaTerminos(Boolean.TRUE);
                return codDAO.guardar(u);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Rechazar terminos de usuario
     *
     * @param entity usuPk
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuario rechazarTerminos(String codigoUsuario) throws GeneralException {
        try {
            if (codigoUsuario != null) {
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                SgUsuario u = this.obtenerPorCodigo(codigoUsuario);
                u.setUsuAceptaTerminos(Boolean.FALSE);
                return codDAO.guardar(u);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

    /**
     * Setea en true atributo usuEnviadoPentaho
     *
     * @param entity SgUsuario
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void enviarAPentaho(SgUsuario entity) throws GeneralException {
        try {
            if (entity.getUsuPk() != null) {
                Boolean esAdminPentaho = entity.getUsuEsAdminPentaho();
                SgUsuario usu = em.find(SgUsuario.class, entity.getUsuPk());
                usu.setUsuEnviadoPentaho(Boolean.TRUE);
                usu.setUsuEsAdminPentaho(esAdminPentaho);
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                codDAO.guardar(usu);
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Setea en false atributo usuEnviadoPentaho
     *
     * @param entity SgUsuario
     * @return SgUsuario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminarDePentaho(SgUsuario entity) throws GeneralException {
        try {
            if (entity.getUsuPk() != null) {
                SgUsuario usu = em.find(SgUsuario.class, entity.getUsuPk());
                usu.setUsuEnviadoPentaho(Boolean.FALSE);
                usu.setUsuEsAdminPentaho(Boolean.FALSE);
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                codDAO.guardar(usu);
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroUsuario filtro) throws GeneralException {
        try {
            UsuarioDAO codDAO = new UsuarioDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgUsuario
     * @throws GeneralException
     */
    public List<SgUsuario> obtenerPorFiltro(FiltroUsuario filtro) throws GeneralException {
        try {
            UsuarioDAO codDAO = new UsuarioDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgUsuario> codDAO = new CodigueraDAO<>(em, SgUsuario.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgUsuario
     * @return void
     * @throws GeneralException
     */
    public void crearUsuarioContextoId(SgUsuarioContextoId entity) throws GeneralException {
        try {
            if (entity != null) {

                FiltroRol frol = new FiltroRol();
                frol.setCodigo(entity.getUscCodigoRol());
                List<SgRol> listRol = rolBean.obtenerPorFiltro(frol);

                SgRol rol = !listRol.isEmpty() ? listRol.get(0) : null;

                if (rol != null) {

                    FiltroUsuarioRol fur = new FiltroUsuarioRol();
                    fur.setRol(rol.getRolPk());
                    fur.setAmbito(entity.getUscAmbito());
                    fur.setContexto(entity.getUscContextoId());
                    fur.setUsuarios(entity.getUscUsuarios().stream().map(u -> u.getUsuPk()).collect(Collectors.toList()));

                    List<SgUsuarioRol> usuRolList = usuarioRolBean.obtenerPorFiltroUsuarioRol(fur);
                    List<SgUsuario> usuCrearRol = entity.getUscUsuarios();

                    for (SgUsuarioRol usuRol : usuRolList) {
                        for (SgUsuario usuEntity : entity.getUscUsuarios()) {
                            if (usuEntity.getUsuCodigo().equals(usuRol.getPurUsuario().getUsuCodigo())) {
                                usuCrearRol.remove(usuEntity);
                                break;
                            }
                        }
                    }

                    for (SgUsuario usu : usuCrearRol) {
                        SgContexto contexto = new SgContexto();
                        contexto.setConAmbito(entity.getUscAmbito());
                        contexto.setContextoId(entity.getUscContextoId());

                        SgUsuarioRol usuRol = new SgUsuarioRol();
                        usuRol.setPurUsuario(usu);
                        usuRol.setPurRol(rol);
                        usuRol.setPurContexto(contexto);
                        usuarioRolBean.guardar(usuRol);
                    }

                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

}
