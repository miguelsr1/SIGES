/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils;

 
import gob.mined.siap2.business.ejbs.DatosUsuarioRemote;
import gob.mined.siap2.business.ejbs.EntityManagementBeanRemote;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.TechnicalException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;



/**
 *
 * @author Sofis Solutions
 */
public class EJBUtils {
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
     private static Object lookup(String beanName, String viewClassFullName,boolean stateful) throws NamingException {
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        context.addToEnvironment("sistema", "SIAP2");
        // The app name is the application name of the deployed EJBs. This is typically the ear name
        // without the .ear suffix. However, the application name could be overridden in the application.xml of the
        // EJB deployment on the server.
        // Since we haven't deployed the application as a .ear, the app name for us will be an empty string
        final String appName = "SIAP2-4.0";
        // This is the module name of the deployed EJBs on the server. This is typically the jar name of the
        // EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
        // In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
        // jboss-as-ejb-remote-app
        final String moduleName = "SIAP2";
        // AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
        // our EJB deployment, so this is an empty string
        final String distinctName = "";
        // The EJB name which by default is the simple class name of the bean implementation class
        // let's do the lookup
        String looks="ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassFullName;
        if (stateful) {
            looks+="?stateful";
        }
        return (Object) context.lookup(looks);
    }
     
    public static EntityManagementBeanRemote getEntityManagement() throws TechnicalException {
          try {
              
              Context context = new InitialContext();
            return (EntityManagementBeanRemote) context.lookup( "java:global/SIAP2-ear-4.0/SIAP2-ejb-utilidades-4.0/EntityManagementBean!gob.mined.siap2.business.ejbs.EntityManagementBeanRemote");
                   
         } catch (NamingException ex) {
              TechnicalException te = new TechnicalException();
              te.addError(ex.getMessage());
              throw te;
         }
    }
    
    public static DatosUsuarioRemote getDatosUsuario() throws TechnicalException {
          try {
             return (DatosUsuarioRemote)lookup("DatosUsuario", DatosUsuarioRemote.class.getName(),true);
         } catch (NamingException ex) {
              TechnicalException te = new TechnicalException();
              te.addError(ex.getMessage());
              throw te;
         }
    }
    
      
     
    public static UsuarioBean getUsuarioBean() throws TechnicalException {
          try {
              Context context = new InitialContext();
              return (UsuarioBean) context.lookup( "java:global/SIAP2-ear-4.0/SIAP2-ejb-utilidades-4.0/UsuarioBean!gob.mined.siap2.business.ejbs.UsuarioBean");
                   
         } catch (NamingException ex) {
               logger.log(Level.SEVERE, ex.getMessage(), ex);
             TechnicalException te = new TechnicalException();
             te.addError(ex.getMessage());
             throw te;
         }
    }
    
    
     
     
}
