/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.sb.pki;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.agesic.firma.datatypes.ResultadoValidacion;

/**
 *
 * @author sofis solutions
 */
public class ValidacionVigencia {

    public static final String CERTIFICADO_EXPIRADO = "CERTIFICADO_EXPIRADO";
    public static final String CERTIFICADO_NO_VALIDO_AUN = "CERTIFICADO_NO_VALIDO_AUN";
    
    private static final DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public static ResultadoValidacion validar(X509Certificate cert, Calendar cal) {
        try {
            if (cal == null){
                cert.checkValidity();
            }else{
                cert.checkValidity(cal.getTime());
            }
            return null;
        } catch (CertificateExpiredException w) {
            ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_EXPIRADO, "El certificado ya no es válido ("+formatoFecha.format(cert.getNotAfter())+")", 0, null);
            return resultadoValidacion;
        } catch (CertificateNotYetValidException w) {
            ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_NO_VALIDO_AUN, "El certificado no es válido aún ("+formatoFecha.format(cert.getNotBefore())+")", 0, null);
            return resultadoValidacion;
        }
    }
}
