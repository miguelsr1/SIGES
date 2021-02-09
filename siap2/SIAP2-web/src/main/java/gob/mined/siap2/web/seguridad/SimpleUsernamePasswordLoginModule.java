/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.seguridad;

import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.web.utils.EJBUtils;
import java.io.Serializable;
import java.security.acl.Group;
import java.util.Map;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

/**
 *
 * @author Sofis Solutions
 */
public class SimpleUsernamePasswordLoginModule extends UsernamePasswordLoginModule implements Serializable {

    private static final long serialVersionUID = 1L;
    protected static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @SuppressWarnings("rawtypes")
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map sharedState,
            Map options) {

        super.initialize(subject, callbackHandler, sharedState, options);
    }

    /**
     * (required) The UsernamePasswordLoginModule modules compares the result of
     * this method with the actual password.
     */
    @Override
    protected String getUsersPassword() throws LoginException {

        String username = super.getUsername();

        return username;
    }

    /**
     * (optional) Override if you want to change how the password are compared
     * or if you need to perform some conversion on them.
     *
     * @param inputPassword
     * @param username
     * @return
     */
    @Override
    protected boolean validatePassword(String inputPassword, String username) {    
        UsuarioBean usuarioBean = EJBUtils.getUsuarioBean();
        return usuarioBean.validarUsuario(username, inputPassword);
    }

    /**
     * (required) The groups of the user, there must be at least one group
     * called "Roles" (though it likely can be empty) containing the roles the
     * user has.
     */
    @Override
    protected Group[] getRoleSets() throws LoginException {

        SimpleGroup group = new SimpleGroup("Roles");
        try {
            group.addMember(new SimplePrincipal("usuario_autenticado"));
        } catch (Exception e) {
            throw new LoginException("No fue posible crear el grupo: " + group);
        }
        return new Group[]{group};
    }
}
