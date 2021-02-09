/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.security.Principal;

/**
 *
 * @author Sofis Solutions
 */
public class LoggingUtils {
    
    public static String getMessage(Principal user, Exception ex){
        return user.getName();
    }
    
}
