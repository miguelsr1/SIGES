package uy.com.sofis.pfea.binarios;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author sofis
 */
public class PfeaFileUtils {

    static final Logger logger = Logger.getLogger(PfeaFileUtils.class.getName());
    
    public static String PfeaArchiveMaker(String idTransaccion, String urlWs, String urlUpdate, String metodoFirma, String urlCRL, Boolean enabledCRL, String userData, String trustoreCAName, Boolean trustoreCAValidationEnabled) throws IOException {
        if (idTransaccion == null) {
            return null;
        }
        String pathDocumentosBandeja = "/binarios/documentos_bandeja/";
        String basePath = System.getProperty("jboss.server.base.dir");
        String nombreArchivo = idTransaccion + ".firmasiges";
        String ruta = basePath + pathDocumentosBandeja;
        File file = new File(ruta + nombreArchivo);
        FileWriter fr = new FileWriter(file);
        BufferedWriter br = new BufferedWriter(fr);
        br.write("-ID_TRANSACCION=" + idTransaccion + System.getProperty("line.separator"));
        br.write("-TIPO_DOCUMENTO=PDF" + System.getProperty("line.separator"));
        br.write("-FIRMA_WS=" + urlWs + System.getProperty("line.separator"));
        br.write("-WS_REQUEST_TIMEOUT=30000" + System.getProperty("line.separator"));
        br.write("-WS_CONNECT_TIMEOUT=30000" + System.getProperty("line.separator"));
        br.write("-METODO_FIRMA=" + (StringUtils.isBlank(metodoFirma) ? "" : metodoFirma) + System.getProperty("line.separator"));
        br.write("-UPDATE_URL=" + urlUpdate + System.getProperty("line.separator"));
        br.write("-KEYSTORE_TRUSTORE_NAME=" + trustoreCAName + System.getProperty("line.separator"));
        br.write("-CA_ROOT_VALIDATION_ENABLE=" + trustoreCAValidationEnabled + System.getProperty("line.separator"));
        br.write("-USER_DATA="+userData + System.getProperty("line.separator"));
        br.write("-CRL_URL=" + urlCRL + System.getProperty("line.separator"));
        br.write("-CRL_ENABLED="+enabledCRL+ System.getProperty("line.separator"));
        String textoHash = "ID_TRANSACCION="+idTransaccion + "+TIPO_DOCUMENTO=PDF+FIRMA_WS=" + urlWs + "+UPDATE_URL="+urlUpdate+"CRL_URL=" + urlCRL+"CRL_ENABLED="+enabledCRL+"USER_DATA="+userData+"CA_ROOT_VALIDATION_ENABLE=" + trustoreCAValidationEnabled;        
        String firma = encriptar(textoHash);
        br.write("-CODIGO_SEGURIDAD=" + firma + System.getProperty("line.separator"));
        br.close();
        fr.close();
        return ruta + nombreArchivo;
    }

    @SuppressWarnings("UseSpecificCatch")
    private static String encriptar(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256Texto = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            Context initContext = new InitialContext();
            String keystorePath = (String)initContext.lookup("java:global/pfea/jks");
            String keystorePass = (String)initContext.lookup("java:global/pfea/password");
            String certAlias = (String)initContext.lookup("java:global/pfea/alias");
            initContext.close();
            FileInputStream is = new FileInputStream(keystorePath);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] passwd = keystorePass.toCharArray();
            keystore.load(is, passwd);
            Key privateKey = keystore.getKey(certAlias, passwd);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            Key privKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey.getEncoded()));
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            byte[] bytes = cipher.doFinal(sha256Texto);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al encriptar los parámetros del archivo", ex);
            return null;
        }
    }

}
