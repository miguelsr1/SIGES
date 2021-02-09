/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.negocio.validaciones.UsuarioRolValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgUsuarioRol;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgResponsable;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.filtros.FiltroRol;
import sv.gob.mined.siges.filtros.FiltroUsuarioRol;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.daos.UsuarioRolDAO;
import sv.gob.mined.siges.persistencia.entidades.SgConfiguracion;
import sv.gob.mined.siges.persistencia.entidades.SgContexto;
import sv.gob.mined.siges.persistencia.entidades.SgRol;
import sv.gob.mined.siges.persistencia.entidades.SgUsuario;

@Stateless
public class UsuarioRolBean {

    private static final Logger LOGGER = Logger.getLogger(UsuarioRolBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SimpleBean simpleBean;

    @Inject
    private RolBean rolBean;

    @Inject
    private UsuarioBean usuarioBean;
    
    @Inject
    private ConfiguracionBean configuracionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgPersonaUsuariaRol
     * @throws GeneralException
     */
    public SgUsuarioRol obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgUsuarioRol> codDAO = new CodigueraDAO<>(em, SgUsuarioRol.class);
                return codDAO.obtenerPorId(id);
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
                CodigueraDAO<SgUsuarioRol> codDAO = new CodigueraDAO<>(em, SgUsuarioRol.class);
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
     * @param entity SgPersonaUsuariaRol
     * @return SgPersonaUsuariaRol
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuarioRol guardar(SgUsuarioRol entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (UsuarioRolValidacionNegocio.validar(entity)) {

                    //Validar roles de ambito persona
                    if (EnumAmbito.PERSONA.equals(entity.getPurContexto().getConAmbito())) {
          
                        SgConfiguracion conf = configuracionBean.obtenerPorCodigo(Constantes.CONFIG_ROLES_AMBITO_PERSONA);  
                        List<String> roles = new ArrayList<>();
                        if (conf.getConValor() != null){
                            roles = Arrays.asList(conf.getConValor().split("\\|"));
                        }
                        
                        if (!roles.contains(entity.getPurRol().getRolCodigo())) {
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_AMBITO_PERSONA_NO_ADMITITO_PARA_ROL_SELECCIONADO);
                            throw be;
                        }

                    }

                    CodigueraDAO<SgUsuarioRol> codDAO = new CodigueraDAO<>(em, SgUsuarioRol.class);
                    SgUsuarioRol usuRol = codDAO.guardar(entity);


                    return usuRol;
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
    public Long obtenerTotalPorFiltro(FiltroUsuarioRol filtro) throws GeneralException {
        try {
            UsuarioRolDAO codDAO = new UsuarioRolDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersonaUsuariaRol
     * @throws GeneralException
     */
    public List<SgUsuarioRol> obtenerPorFiltro(FiltroUsuarioRol filtro) throws GeneralException {
        try {
            UsuarioRolDAO codDAO = new UsuarioRolDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgPersonaUsuariaRol
     * @throws GeneralException
     */
    public List<SgUsuarioRol> obtenerPorFiltroUsuarioRol(FiltroUsuarioRol filtro) throws GeneralException {
        try {

            UsuarioRolDAO codDAO = new UsuarioRolDAO(em);
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
                CodigueraDAO<SgUsuarioRol> codDAO = new CodigueraDAO<>(em, SgUsuarioRol.class);
                SgUsuarioRol usuRol = codDAO.obtenerPorId(id);
                SgUsuario usu = usuRol.getPurUsuario();
                usu.getPusPersonaUsuarioRol().remove(usuRol);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Crea el usuario y roles para el responsable del estudiante
     *
     * @param responsable
     * @throws BusinessException
     * @throws Exception
     */
    @Interceptors(AuditInterceptor.class)
    public SgUsuarioRol crearUsuarioRolResponsable(SgResponsable responsable) throws BusinessException, Exception {
        SgUsuarioRol usuarioRol = new SgUsuarioRol();
        try {
            if (responsable.getUsuPk() != null || StringUtils.isNotBlank(responsable.getDocumento())) {
                FiltroRol filtroRoles = new FiltroRol();
                filtroRoles.setCodigo(Constantes.CODIGO_ROL_PADRE);
                filtroRoles.setHabilitado(Boolean.TRUE);
                List<SgRol> roles = rolBean.obtenerPorFiltro(filtroRoles);

                if (roles != null && !roles.isEmpty()) {
                    SgUsuario userFound = null;
                    if (responsable.getUsuPk() != null){
                       userFound = em.find(SgUsuario.class, responsable.getUsuPk());
                    } else {
                       userFound = usuarioBean.obtenerPorCodigo(responsable.getDocumento());
                    }
                    
                    if (userFound == null) {//Si no existe, se crea usuario y rol
                        SgUsuario usuario = new SgUsuario();
                        usuario.setUsuCodigo(responsable.getDocumento());
                        usuario.setUsuNombre(responsable.getNombreCompleto());
                        usuario.setUsuEmail(responsable.getEmail());
                        usuario.setUsuPersonaPk(responsable.getPersonaResponsableId());
                        usuario.setUsuHabilitado(Boolean.TRUE);
                        usuario.setUsuAceptaTerminos(Boolean.TRUE);
                        usuario.setUsuPassExpired(0);
                        userFound = usuarioBean.guardar(usuario);//Se crear Usuario
                        if (userFound != null) {
                            usuarioRol.setPurUsuario(userFound);
                            usuarioRol.setPurRol(roles.get(0));

                            SgContexto contexto = new SgContexto();
                            contexto.setConAmbito(EnumAmbito.PERSONA);
                            contexto.setContextoId(responsable.getPersonaEstudianteId());
                            usuarioRol.setPurContexto(contexto);
                            usuarioRol = guardar(usuarioRol);//Se agrega ROL "PADRE" al usuario
                        }
                    } else {
                        //Si existe, se verifica que tenga asignado el rol padre, que tenga al estudiante coomo contexto
                        FiltroUsuarioRol filtroUsuarioRol = new FiltroUsuarioRol();
                        filtroUsuarioRol.setUsuario(userFound.getUsuPk());
                        filtroUsuarioRol.setRol(roles.get(0).getRolPk());
                        filtroUsuarioRol.setAmbito(EnumAmbito.PERSONA);
                        filtroUsuarioRol.setContexto(responsable.getPersonaEstudianteId());
                        List<SgUsuarioRol> listaRolesUsuario = obtenerPorFiltroUsuarioRol(filtroUsuarioRol);
                        if (listaRolesUsuario == null || listaRolesUsuario.isEmpty()) {
                            usuarioRol.setPurUsuario(userFound);
                            usuarioRol.setPurRol(roles.get(0));

                            SgContexto contexto = new SgContexto();
                            contexto.setConAmbito(EnumAmbito.PERSONA);
                            contexto.setContextoId(responsable.getPersonaEstudianteId());
                            usuarioRol.setPurContexto(contexto);
                            usuarioRol = guardar(usuarioRol);//Se agrega ROL "PADRE" al usuario
                        }

                    }
                } else {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_NO_EXISTE_ROL_PADRE);
                    throw be;
                }
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CREACION_USUARIO_ALLEGADO);
                throw be;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return usuarioRol;
    }
}
