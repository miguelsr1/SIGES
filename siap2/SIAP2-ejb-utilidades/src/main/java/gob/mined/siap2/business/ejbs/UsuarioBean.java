/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs;

import gob.mined.siap2.business.datatype.DataRolUsuario;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.SsRelRolOperacion;
import gob.mined.siap2.entities.data.SsRol;
import gob.mined.siap2.entities.data.SsTokenPassword;
import gob.mined.siap2.entities.data.SsUsuOfiRoles;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.PoliticaContrasenia;
import gob.mined.siap2.entities.data.impl.SsPlantillasCorreo;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.helper.PoliticaContraseniaHelper;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.filtros.FiltroUsuario;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.utils.seguridad.PasswordsUtils;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;

/**
 * Esta clase incluye los métodos para la gestión de usuarios del sistema.
 *
 * @author SsUsuario
 */
@Stateless(name = "UsuarioBean")
@LocalBean
public class UsuarioBean {

    @Inject
    private DatosUsuario du;
    private static final Logger logger = Logger.getLogger(UsuarioBean.class.getName());
    @Inject
    private ConfiguracionBean configuracionBean;

    @Resource
    private javax.ejb.SessionContext ctx;

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;

    @Inject
    private MailBean mailBean;
    @Inject
    private LogAuditoriaBean logBean;
    @Inject
    private PlantillasCorreoBean plcBean;

    private static final String cambiarContrasenia = "cambiarContrasenia";

    /**
     * Este método guarda un elemento de tipo SsUsuario. Se aplica para la
     * creación de la entidad y para la modificación de una entidad existente.
     *
     * @param cnf
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public SsUsuario guardarUsuario(SsUsuario editando) throws GeneralException {
        logger.log(Level.INFO, "guardar");
        try {

            //Primero se verifica si el código de usuario ya está registrado
            if (editando.getUsuId() == null && usuarioDAO.existeUsuarioByCodigo(editando.getUsuCod())) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EL_CODIGO_USUARIO_YA_EXISTE);
                throw b;
            }

            editando.setUsuFechaPassword(new Date());
            editando.setUsuIntentosFallidos(0);
            if (editando.getUsuPassword() == null || editando.getUsuPassword().trim().length() == 0) {
                String password = obtenerPasswordInicial();
                String hash = PasswordsUtils.hash(password);
                editando.setUsuPassword(hash);

                mailBean.enviarNuevoUsuario(editando.getUsuCorreoElectronico(), editando.getUsuCod(), password);
            }
            if (editando.getUsuId() == null) {//usuario nuevo
                editando.setUsuCreadoPor(ctx.getCallerPrincipal().getName());
                PoliticaContrasenia pol = obtenerPoliticaContrasenia();

                if (pol.getCambiaPasswordPrimeraVez() != null && pol.getCambiaPasswordPrimeraVez()) {

                    editando.setUsuCambioPassword(Boolean.TRUE);//Marco para que pida cambio de password la primera vez que ingrese.
                }
            }

            return (SsUsuario) generalDAO.update(editando);

        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (Exception ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve el elemento configuración por el ID
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public SsUsuario obtenerSsUsuarioPorId(Integer id) throws GeneralException, DAOGeneralException {
        logger.log(Level.INFO, "obtenerSsUsuarioPorId");
        try {
            return (SsUsuario) generalDAO.findById(SsUsuario.class, id);
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Devuelve el token y lo elimina de la base para que no pueda ser utilizado
     * nuevamente
     *
     * @param token
     * @return
     */
    public SsTokenPassword obtenerToken(String token) {
        List<SsTokenPassword> lista = usuarioDAO.getTokenPassword(token);
        if (lista != null && !lista.isEmpty()) {
            SsTokenPassword tokenPasswd = lista.get(0);
            generalDAO.getEntityManager().remove(tokenPasswd);
            return tokenPasswd;
        } else {
            return null;
        }
    }

    /**
     * Envía mail con URL para hacer al cambio de contraseña
     *
     * @param usuCod
     * @param contexto
     */
    //Envia mail con URL para hacer al cambio de contraseña
    public void recordarContrasenia(String usuCod, String contexto) {
        Long trn = logBean.loguear(null, cambiarContrasenia, usuCod, String.format("Inicio de recordar contraseña para: %s.", usuCod), null);
        SsUsuario usuario = obtenerSsUsuarioPorCodigo(usuCod);
        if (usuario == null) {
            String error = "Error: el usuario no existe.";
            logBean.loguear(trn, cambiarContrasenia, usuCod, String.format(error), null);
            return;
        }

        List<String> correos = new LinkedList();
        correos.add(usuario.getUsuCorreoElectronico());
        SsPlantillasCorreo plantilla = plcBean.obtenerCnfPorCodigo("PLANTILLA_RECORDAR_CONTRASENIA");
        if (plantilla != null) {
            String subject = "SAPI - Recuperación de contraseña";//Tomar de la configuración - Ver ejemplos de MailBean comentados
            SsTokenPassword tokenPassword = new SsTokenPassword();
            tokenPassword.setUsuario(usuCod);
            tokenPassword.setToken(getCadenaAlfanumAleatoria(32));
            generalDAO.getEntityManager().persist(tokenPassword);

            Map<String, String> variables = new HashMap<>();
            String uri = contexto + "/paginasPublicas/recordarContrasenia2.xhtml?lt=" + tokenPassword.getToken();
            variables.put("#URL#", "<a href=\"" + uri + "\">" + uri + "</a>");
            variables.put("#NOMBRE_USUARIO#", usuario.getNombreCompleto());

            String mensaje = TextUtils.reemplazarMultiplesStrings(plantilla.getPlcContenido(), variables);
            mailBean.enviarMail(subject, correos, mensaje);
            logBean.loguear(trn, "cambiarContrasenia", usuCod, String.format("Mail enviado a: %s.", usuario.getUsuCorreoElectronico()), usuario.getUsuId());
        } else {
            throw new BusinessException();
        }
    }

    /**
     * Devuelve el elemento de configuración según el código Si no hay ningún
     * elemento con ese código devuelve null
     *
     * @param codigo
     * @return
     * @throws GeneralException
     */
    public SsUsuario obtenerSsUsuarioPorCodigo(String codigo) throws GeneralException {
        String codigoLog = "obtenerSsUsuarioPorCodigo: " + codigo;
        logger.log(Level.INFO, codigoLog);
        try {
            List<SsUsuario> resultado = generalDAO.findByOneProperty(SsUsuario.class, "usuCod", codigo);
            if (resultado.size() == 1) {
                String resultadoLog = "el usuario tiene fecha: " + resultado.get(0).getUsuDesde();
                logger.log(Level.INFO, resultadoLog);
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }


    /**
     * Devuelve los elementos que satisfacen el criterio ingresado
     *
     * @param cto
     * @param orderBy
     * @param ascending
     * @param startPosition
     * @param cantidad
     * @return
     * @throws GeneralException
     */
    public List<SsUsuario> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        logger.log(Level.INFO, "obtenerPorCriteria");
        try {
            return generalDAO.findEntityByCriteria(SsUsuario.class, cto, orderBy, ascending, startPosition, cantidad);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Cambio de contraseña por olvido
     *
     * @param codigoSsUsuario
     * @param nuevaContrasenia
     * @throws GeneralException
     */
    public void cambiarContrasenia(String codigoSsUsuario, String nuevaContrasenia) throws GeneralException {
        try {
            SsUsuario usu = obtenerSsUsuarioPorCodigo(codigoSsUsuario);
            PoliticaContrasenia pol = obtenerPoliticaContrasenia();
            verificaUsuCodEnPassword(pol, codigoSsUsuario, nuevaContrasenia);
            verificarExpresionRegularContrasenia(pol, nuevaContrasenia);
            usu.setUsuPassword(PasswordsUtils.hash(nuevaContrasenia));
            usu.setUsuFechaPassword(new Date());
            if (pol.getCambiaPasswordDespuesOlvido() != null && pol.getCambiaPasswordDespuesOlvido()) {
                usu.setUsuCambioPassword(Boolean.TRUE);
            }
            generalDAO.merge(usu);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite cambiar la contraseña de un usuario
     *
     * @param codigoSsUsuario
     * @param nuevaContrasenia
     * @param viejaContrasenia
     */
    public void cambiarContrasenia(String codigoSsUsuario, String nuevaContrasenia, String viejaContrasenia) {
        Long trn = logBean.loguear(null, "cambiarContrasenia", null, String.format("Cambio de contraseña: %s.", codigoSsUsuario), null);
        SsUsuario usu = obtenerSsUsuarioPorCodigo(codigoSsUsuario);
        if (PasswordsUtils.verify(viejaContrasenia, usu.getUsuPassword())) {
            PoliticaContrasenia pol = obtenerPoliticaContrasenia();
            if (pol != null) {
                verificaUsuCodEnPassword(pol, codigoSsUsuario, nuevaContrasenia);
                verificarExpresionRegularContrasenia(pol, nuevaContrasenia);
            }
            usu.setUsuPassword(PasswordsUtils.hash(nuevaContrasenia));
            usu.setUsuCambioPassword(Boolean.FALSE);
            usu.setUsuFechaPassword(new Date());
            generalDAO.merge(usu);
            String formato = "Cambio realizado correctamente.";
            logBean.loguear(trn, "cambiarContrasenia", usu.getUsuCod(), String.format(formato), null);
        } else {
            String formato = "Error: No validan las contraseñas.";
            logBean.loguear(trn, "cambiarContrasenia", usu.getUsuCod(), String.format(formato), null);
            BusinessException be = new BusinessException();
            be.addError(ConstantesErrores.ERROR_CONTRASENIAS_NO_COINCIDEN);
            be.setCodigo(ConstantesErrores.CODIGO_ERROR_CONTRASENIAS_NO_COINCIDEN);
            throw be;
        }
    }

    /**
     * Este método permite verificar si la contraseña de un usuario es correcta,
     * según una política de contraseña. Actualiza la cantidad de intentos
     * consecutivos fallidos y puede bloquear la cuenta.
     *
     * @param pol
     * @param codigoSsUsuario
     * @param nuevaContrasenia
     */
    private void verificaUsuCodEnPassword(PoliticaContrasenia pol, String codigoSsUsuario, String nuevaContrasenia) {
        if (pol.getPermiteUsuCodEnPassword() != null && !pol.getPermiteUsuCodEnPassword() && nuevaContrasenia.contains(codigoSsUsuario)) {
            BusinessException be = new BusinessException();
            be.addError(ConstantesErrores.ERROR_CONTRASENIA_NO_PUEDE_INCLUIR_CODIGO_USUARIO);
            be.setCodigo(ConstantesErrores.CODIGO_ERROR_CONTRASENIA_NO_PUEDE_INCLUIR_CODIGO_USUARIO);
            throw be;
        }
    }

    /**
     * Este método permite obtener la política de contraseña
     *
     * @return
     */
    public PoliticaContrasenia obtenerPoliticaContrasenia() {
        PoliticaContrasenia pol = generalDAO.getEntityManager().find(PoliticaContrasenia.class, 1);
        if (pol == null) {//Seteos por defecto
            pol = PoliticaContraseniaHelper.obtenerPoliticaContraseniaPorDefecto();
        }
        return pol;
    }

    /**
     * Verifica que la contraseña cumpla con los parámetros establecidos. Caso
     * contrario lanza excepción
     */
    private void verificarExpresionRegularContrasenia(PoliticaContrasenia pol, String contrasenia) {

        String largo = pol.getLargoMinimo().toString();
        String especiales = pol.getCantidadMinimaCaracteresEspeciales().toString();
        String mayusculas = pol.getCantidadMinimaCaracteresMayuscula().toString();
        String minusculas = pol.getCantidadMinimaCaracteresMinusculas().toString();
        String digitos = pol.getCantidadMinimaDigitos().toString();

        String pattern = "^(?=.{LARGO,}$)(?=(.*[@#$%^&+=]){ESPECIALES})(?=(.*[A-Z]){MAYUSCULAS})(?=(.*[a-z]){MINUSCULAS})(?=(.*[0-9]){DIGITOS})(?=\\S+$).*$";
        pattern = pattern.replaceFirst("LARGO", largo).replaceFirst("ESPECIALES", especiales).replaceFirst("MAYUSCULAS", mayusculas).
                replaceFirst("MINUSCULAS", minusculas).replaceFirst("DIGITOS", digitos);

        if (!Pattern.matches(pattern, contrasenia)) {
            BusinessException be = new BusinessException();
            String[] params = {largo, especiales, mayusculas, minusculas, digitos};
            be.addError(ConstantesErrores.ERROR_CONTRASENIA_NO_CUMPLE_EXPRESION_REGULAR, params);
            be.setCodigo(ConstantesErrores.CODIGO_ERROR_CONTRASENIA_NO_CUMPLE_EXPRESION_REGULAR);
            throw be;
        }
    }

    /**
     * Este método devuelve el conjunto de operaciones de un usuario.
     *
     * @param codigoUsuario
     * @return
     */
    public List<String> obtenerOperaciones(String codigoUsuario) {
        SsUsuario usu = obtenerSsUsuarioPorCodigo(codigoUsuario);
        List<String> respuesta = new ArrayList<>();
        if (usu != null) {
            List<SsRelRolOperacion> resultado;
            for (SsUsuOfiRoles or : usu.getSsUsuOfiRolesCollection()) {

                resultado = usuarioDAO.getSsRelRolOperacion(or.getUsuOfiRolesRol().getRolId());

                for (SsRelRolOperacion rs : resultado) {
                    respuesta.add(rs.getRelRolOperacionOperacionId().getOpeCodigo());
                }
            }
        }

        return respuesta;
    }

    /**
     * Este método devuelve el conjunto de operaciones de un usuario agrupadas
     * por rol y UT.
     *
     * @param codigoUsuario
     * @return
     */
    public List<DataRolUsuario> obtenerOperacionesAgrupadasPorUT(String codigoUsuario) {
        SsUsuario usu = obtenerSsUsuarioPorCodigo(codigoUsuario);
        List<DataRolUsuario> respuesta = new ArrayList<>();
        if (usu != null) {
            for (SsUsuOfiRoles or : usu.getSsUsuOfiRolesCollection()) {
                DataRolUsuario iter = new DataRolUsuario();
                if (or.getUsuOfiRolesRol() != null) {
                    iter.setIdSsRol(or.getUsuOfiRolesRol().getRolId());
                }
                if (or.getUsuOfiRolesUnidadTecnica() != null) {
                    iter.setIdUnidadTecnica(or.getUsuOfiRolesUnidadTecnica().getId());
                }

                iter.setCodigoOperaciones(new LinkedList<String>());
                List<SsRelRolOperacion> resultado = usuarioDAO.getSsRelRolOperacion(or.getUsuOfiRolesRol().getRolId());
                for (SsRelRolOperacion rs : resultado) {
                    iter.getCodigoOperaciones().add(rs.getRelRolOperacionOperacionId().getOpeCodigo());
                }

                respuesta.add(iter);
            }
        }

        return respuesta;
    }

    /**
     * Este método permite obtener los roles de un usuario.
     *
     * @param idUsuario
     * @return
     */
    public List<SsUsuOfiRoles> getUsuOfiRoles(int idUsuario) {
        List<SsUsuOfiRoles> lista = new ArrayList<>();
        try {
            CriteriaTO toUsuarioId = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "usuOfiRolesUsuario.usuId", idUsuario);
            String[] orderBy = {"usuOfiRolesRol.rolNombre"};
            boolean[] ascending = {true};
            lista = generalDAO.findEntityByCriteria(SsUsuOfiRoles.class, toUsuarioId, orderBy, ascending, null, null);
        } catch (Exception e) {
            TechnicalException te = new TechnicalException();
            te.setCodigo(e.getMessage());
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw te;
        }
        return lista;

    }

    /**
     * Este método permite obtener la contraseña inicial para asignar a los
     * usuarios.
     *
     * @return
     */
    public String obtenerPasswordInicial() {
        Configuracion cnf = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.CODIGO_CONTRASENIA_INICIAL);
        return (cnf.getCnfValor());
    }

    /**
     * Este método permite un rol por su código.
     *
     * @param codigo
     * @return
     */
    public SsRol obtenerRolPorCodigo(String codigo) {
        try {
            List<SsRol> resultado = generalDAO.findByOneProperty(SsRol.class, "rolCod", codigo);
            if (!resultado.isEmpty()) {
                return resultado.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            TechnicalException te = new TechnicalException();
            te.setCodigo(e.getMessage());
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw te;
        }
    }

    /**
     * Este método permite determinar si la combinación usuario-contraseña es
     * correcta.
     *
     * @param username
     * @param password
     * @return
     */
    public boolean validarUsuario(String username, String password) {
        String validarUsuario = "validarUsuario";
        Long trn = logBean.loguear(null, validarUsuario, null, String.format("Intento de ingreso al sistema %s.", username), null);
        SsUsuario usuario = obtenerSsUsuarioPorCodigo(username);     
        if (usuario == null || TextUtils.isEmpty(password)) {
            return false;
        }

        if (usuario.getUsuCuentaBloqueada() != null && usuario.getUsuCuentaBloqueada()) {

            return false;
        }

        PoliticaContrasenia pol = obtenerPoliticaContrasenia();

        boolean usuarioYPasswdOK = PasswordsUtils.verify(password, usuario.getUsuPassword());
        if (usuarioYPasswdOK) {
            //VALIDACION DE QUE ESTÁ HABILITADO EN EL RANGO DE FECHAS

            GregorianCalendar gc = new GregorianCalendar();
            if (usuario.getUsuDesde() != null && gc.before(usuario.getUsuDesde())) {
                logBean.loguear(trn, validarUsuario, null, String.format("El usuario está habilitado a partir de %s.", usuario.getUsuDesde()), usuario.getUsuId());
                usuarioYPasswdOK = false;
            }

            if (usuario.getUsuHasta() != null && gc.after(usuario.getUsuHasta())) {
                logBean.loguear(trn, validarUsuario, null, String.format("El usuario está habilitado hasta %s.", usuario.getUsuHasta()), usuario.getUsuId());
                usuarioYPasswdOK = false;
            }
        }
        if (pol.getCantidadIntentos() != null && !pol.getCantidadIntentos().equals(0)) {//Controlo intentos fallidos
            if (usuarioYPasswdOK) {
                usuario.setUsuIntentosFallidos(0);
            } else {
                usuario.setUsuIntentosFallidos(usuario.getUsuIntentosFallidos() + 1);
            }
            if (usuario.getUsuIntentosFallidos().intValue() >= pol.getCantidadIntentos().intValue()) {
                String logIntentosFallidos = "El usuario está bloqueado.";
                logBean.loguear(trn, validarUsuario, null, String.format(logIntentosFallidos), usuario.getUsuId());

                usuario.setUsuCuentaBloqueada(Boolean.TRUE);
                usuarioYPasswdOK = false;
            }

            generalDAO.getEntityManager().merge(usuario);
        }
        String logValidacionUsuario = "Intento de ingreso al sistema: %s con resultado %b.";
        logBean.loguear(trn, validarUsuario, null, String.format(logValidacionUsuario, new Object[]{username, usuarioYPasswdOK}), usuario.getUsuId());
        return usuarioYPasswdOK;

    }

    /**
     * Este método actualiza los permisos de un usuario.
     *
     * @param editando
     * @param relRolToDelate
     * @param relRol
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void actualizarPermisos(SsRol editando, Set<Integer> relRolToDelate, List<SsRelRolOperacion> relRol) {
        try {
            /**
             * chequea que no exista otro rol con el mismo código *
             */
            CriteriaTO condicion = null;
            MatchCriteriaTO igualNombre = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "rolCod", editando.getRolCod());
            if (editando.getRolId() != null) {
                MatchCriteriaTO distintoId = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "rolId", editando.getRolId());
                condicion = CriteriaTOUtils.createANDTO(igualNombre, distintoId);
            } else {
                condicion = igualNombre;
            }
            String[] propiedades = {"rolId"};
            List existeIgualNombre = generalDAO.findEntityReferenceByCriteria(SsRol.class, condicion, null, null, null, null, propiedades);
            if (!existeIgualNombre.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_CODIGO_ROL_REPETIDO);
                throw b;
            }

            editando = (SsRol) generalDAO.update(editando, du.getCodigoUsuario(), du.getCodigoUsuario());

            for (Integer IdrRol : relRolToDelate) {
                Object objBorrar = generalDAO.find(SsRelRolOperacion.class, IdrRol);
                generalDAO.delete(objBorrar, du.getCodigoUsuario(), du.getCodigoUsuario());
            }

            for (SsRelRolOperacion rRol : relRol) {
                rRol.setRelRolOperacionRolId(editando);
                generalDAO.update(rRol, du.getCodigoUsuario(), du.getCodigoUsuario());
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método permite obtener las unidades técnicas de un usuario.
     *
     * @param usuId
     * @return
     */
    public List<UnidadTecnica> getUnidadesTecniasDeUsuario(Integer usuId) {
        try {
            List<UnidadTecnica> l = new LinkedList();
            SsUsuario usuario = (SsUsuario) generalDAO.find(SsUsuario.class, usuId);
            for (SsUsuOfiRoles rol : usuario.getSsUsuOfiRolesCollection()) {
                if (rol.getUsuOfiRolesUnidadTecnica() != null && !l.contains(rol.getUsuOfiRolesUnidadTecnica())) {
                    l.add(rol.getUsuOfiRolesUnidadTecnica());
                }
            }
            return l;

        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener todos los usuarios de una unidad técnica.
     *
     * @param unidadTecnicaId
     * @return
     */
    public List<SsUsuario> getUsusariosPorUnidadTecnica(Integer unidadTecnicaId) {
        List<SsUsuario> usuarios = new ArrayList<>();
        try {
            List<SsUsuOfiRoles> usuRoles = (List<SsUsuOfiRoles>) generalDAO.findByOneProperty(SsUsuOfiRoles.class, "usuOfiRolesUnidadTecnica.id", unidadTecnicaId);
            for (SsUsuOfiRoles usuRole : usuRoles) {
                usuarios.add(usuRole.getUsuOfiRolesUsuario());
            }
            return usuarios;
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método devuelve la lista de usuarios cuya unidadTecnica sea la seleccionada
     *
     * @param unidadTecnicaId
     * @return
     */
    public List<SsUsuario> getUsuariosPorIdUnidadTenica(Integer unidadTecnicaId) {
        try {
            return usuarioDAO.getUsuariosPorIdUnidadTenica(unidadTecnicaId);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método permite obtener las UT de un usuario.
     *
     * @param idUsuario
     * @param opeCodigo
     * @return
     */
    public List<UnidadTecnica> getUTDeUsuarioConOperacion(Integer idUsuario, String opeCodigo) {
        try {
            List l = usuarioDAO.getUTDeUsuarioConOperacion(idUsuario, opeCodigo);
            l.isEmpty();
            return l;
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método permite obtener las UT de un usuario.
     *
     * @param idUsuario
     * @param opeCodigo
     * @return
     */
    public List<UnidadTecnica> getUTDeUsuarioConOperacionByNombre(FiltroUnidadTecnica filtro) {

        List<Integer> listaIds = new ArrayList();
        try {
            List<UnidadTecnica> listadoRaiz = usuarioDAO.getUTDeUsuarioConOperacionByNombre(filtro);
            //unidadesRetornar.addAll(listadoRaiz);
            
            for(UnidadTecnica ut:listadoRaiz) {
                if(!listaIds.contains(ut.getId())) {
                    listaIds.add(ut.getId());
                }
                if(ut.getPadre() != null && ut.getPadre().getId() != null) {
                    if(!listaIds.contains(ut.getPadre().getId())) {
                        listaIds.add(ut.getPadre().getId());
                    }
                }
            }
            if(filtro.getMaxResults() == null) {
                filtro.setMaxResults(11L);
            }
            
            List<UnidadTecnica> unidades = usuarioDAO.getUTDeUsuarioConOperacionByNombre(listaIds, filtro.getNombre(),filtro.getMaxResults().intValue());
           
            
            unidades.sort(new Comparator<UnidadTecnica>() {
                @Override
                public int compare(UnidadTecnica a1, UnidadTecnica a2) {
                    int respuesta = 1;
                    
                    
                    if (a1 != null && a2 != null) {
                        if (a1 instanceof UnidadTecnica && a2 instanceof UnidadTecnica) {
                            
                            respuesta = a1.getNombre().compareTo(a2.getNombre());
                        }
                    }
                    return respuesta;
                 }
            });
            return unidades;
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener todos los usuarios que tienen una determinada
     * operación.
     *
     * @param operCD
     * @return
     */
    public List<SsUsuario> getUsuariosConOperacion(String operCD) {
        try {
            List l = usuarioDAO.getUsuariosConOperacion(operCD, null);
            l.isEmpty();
            return l;
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    static final String CARACTERES_DISPONIBLES_EN_TOKEN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static String getCadenaAlfanumAleatoria(int longitud) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            sb.append(CARACTERES_DISPONIBLES_EN_TOKEN.charAt(rnd.nextInt(CARACTERES_DISPONIBLES_EN_TOKEN.length())));
        }
        return sb.toString();

    }

    /**
     * Este método permite obtener los usuarios que tienen código como parte del
     * nombre
     *
     * @param idUT unidad técnica
     * @param codigo substring a buscar en el nombre.
     * @return
     */
    public List<SsUsuario> getUsuariosConNombreEnUT(Integer idUT, String codigo) {
        try {
            List<SsUsuario> l = usuarioDAO.getUsuariosConNombreEnUT(idUT, codigo);
            l.isEmpty();
            return l;
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método permite verificar si el usuario tiene un permiso especifico en una operación
     * @param idUT id de la unidad técnica
     * @param opeCodigo
     * @return lista de usuarios
     */
    public boolean getUsuarioTienePermisoEnUTConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        try {
            return usuarioDAO.getUsuarioTienePermisoEnUTConOperacion(idUT, opeCodigo, codUsuario);
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este método permite verificar si el usuario tiene un permiso especifico en una operación
     * @param idUT id de la unidad técnica
     * @param opeCodigo
     * @return lista de usuarios
     */
    public boolean getUsuarioTienePermisoEnUTPadreConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        try {
            return usuarioDAO.getUsuarioTienePermisoEnUTPadreConOperacion(idUT, opeCodigo, codUsuario);
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método permite verificar si el usuario tiene un permiso especifico en una operación
     * @param idUT id de la unidad técnica
     * @param opeCodigo
     * @return lista de usuarios
     */
    public boolean getUsuarioTienePermisoEnUTOperativaConOperacion(Integer idUT, String opeCodigo, String codUsuario) {
        try {
            return usuarioDAO.getUsuarioTienePermisoEnUTOperativaConOperacion(idUT, opeCodigo, codUsuario);
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    /**
     * retorna las UT que tiene asignada el usuario
     *
     * @return
     */
    public List<UnidadTecnica> getUTDelUsuario() {
        String codigoUsuario = du.getCodigoUsuario();
        SsUsuario usuario = (SsUsuario) obtenerSsUsuarioPorCodigo(codigoUsuario);

        List<UnidadTecnica> utDelUsuario = new LinkedList();
        for (SsUsuOfiRoles rol : usuario.getSsUsuOfiRolesCollection()) {
            utDelUsuario.add(rol.getUsuOfiRolesUnidadTecnica());
        }
        return utDelUsuario;
    }

    /**
     * Retorna true si el usuario actual posee determinada operación
     *
     * @return
     */
    public Boolean usuarioActualTieneOperacion(String operacion) {
        try {
            String codigoUsuario = du.getCodigoUsuario();
            SsUsuario usuarioActual = usuarioDAO.getUsuarioByCodigo(codigoUsuario);

            return (this.getUsuariosConOperacion(operacion)).contains(usuarioActual);
        } catch (GeneralException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    public List<SsUsuario> getUsuariosByFiltro(FiltroUsuario filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (!StringUtils.isBlank(filtro.getCodigo())) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "usuCod", filtro.getCodigo());
                criterios.add(criterio);
            }
            
            if (filtro.getIdUnidadTecnica() != null && filtro.getIdUnidadTecnica() != 0) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "unidadTecnicas.id", filtro.getIdUnidadTecnica());
                criterios.add(criterio);
            }


            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(SsUsuario.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

}
