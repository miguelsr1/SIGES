/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ClipherHelper {

    private static final String ALGORITHM = "DES";
    private static final String HEX = "0123456789ABCDEF";

    /**
     * Encrypt data
     *
     * @param secretKey - a secret key used for encryption
     * @param data - data to encrypt
     * @return Encrypted data
     * @throws Exception
     */
    public static String cipher(String secretKey, String data) throws Exception {
        // Key has to be of length 8
        if (secretKey == null || secretKey.length() != 8) {
            throw new Exception("Invalid key length - 8 bytes key needed!");
        }

        SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return toHex(cipher.doFinal(data.getBytes()));
    }

    /**
     * Decrypt data
     *
     * @param secretKey - a secret key used for decryption
     * @param data - data to decrypt
     * @return Decrypted data
     * @throws Exception
     */
    public static String decipher(String secretKey, String data) throws Exception {
        // Key has to be of length 8
        if (secretKey == null || secretKey.length() != 8) {
            throw new Exception("Invalid key length - 8 bytes key needed!");
        }

        SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return new String(cipher.doFinal(toByte(data)));
    }

    // Helper methods
    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;

        byte[] result = new byte[len];

        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] stringBytes) {
        StringBuilder result = new StringBuilder(2 * stringBytes.length);

        for (int i = 0; i < stringBytes.length; i++) {
            result.append(HEX.charAt((stringBytes[i] >> 4) & 0x0f)).append(HEX.charAt(stringBytes[i] & 0x0f));
        }

        return result.toString();
    }

    public static String obtenerKey(int largo) {
        String cadenaAleatoria = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while (i < largo) {
            char c = (char) r.nextInt(255);
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                {
                    cadenaAleatoria += c;
                    i++;
                }
            }
        }
        return cadenaAleatoria;
    }
}
