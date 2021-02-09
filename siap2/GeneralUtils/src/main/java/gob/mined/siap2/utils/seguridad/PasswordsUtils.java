/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.seguridad;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

/**
 * Esta clase provee métodos para procesar contraseñas.
 *
 * @author Sofis Solutions
 */
public class PasswordsUtils {

    public static String hash(String plainPassword) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passHash = sha256.digest(plainPassword.getBytes());
            return encode(passHash);
        } catch (Exception ex) {
            Logger.getLogger(PasswordsUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Este método verifica si un texto plano genera el hash indicado.
     *
     * @param plainPassword: texto plano
     * @param pHash: hash del texto plano
     * @return true si el hash indicado corresponde al hash del texto plano.
     */
    public static boolean verify(String plainPassword, String pHash) {
        boolean ok = false; 
        String hash = hash(plainPassword);
        if (pHash != null && hash != null) {
            ok = hash.equals(pHash);
        }
        return ok;
    }

    /**
     * Codifica en base 64 como string.
     *
     * @param input
     * @return
     */
    private static String encode(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    /**
     * Decodifica un string.
     *
     * @param input
     * @return
     */
    private static byte[] decode(String input) {
        return Base64.decodeBase64(input);

    }

}
