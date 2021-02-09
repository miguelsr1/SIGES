/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import javax.servlet.http.HttpServletRequest;

public class JSFUtils {
  

    public static String getRemoteAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress != null) {
            // cares only about the first IP if there is a list
            ipAddress = ipAddress.replaceFirst(",.*", "");
        } else {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
