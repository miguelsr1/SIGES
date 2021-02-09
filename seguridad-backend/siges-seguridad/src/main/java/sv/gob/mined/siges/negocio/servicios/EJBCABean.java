/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.io.FileInputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import sv.gob.mined.siges.constantes.RevocationReasons;
import sv.gob.mined.siges.dto.SgCertificateRequest;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.utils.SofisStringUtils;
import sv.gob.mined.siges.ws.EjbcaWS;
import sv.gob.mined.siges.ws.EjbcaWSService;
import sv.gob.mined.siges.ws.KeyStore;
import sv.gob.mined.siges.ws.UserDataVOWS;

@Stateless
public class EJBCABean {


    private static final Logger LOGGER = Logger.getLogger(EJBCABean.class.getName());

    public static final String TOKEN_TYPE_P12 = "P12";
    public static final int STATUS_NEW = 10;  // New user

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");

            java.security.KeyStore keyStore = java.security.KeyStore.getInstance("pkcs12");
            char[] KEYSTOREPW = System.getProperty("service.ejbca.ws_key_pass").toCharArray();
            char[] KEYPW = System.getProperty("service.ejbca.ws_key_pass").toCharArray();
            keyStore.load(new FileInputStream(System.getProperty("service.ejbca.ws_key_location")), KEYSTOREPW);
            javax.net.ssl.KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, KEYPW);

            sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);

        }
    }
    
    public void revocarUsuario(String codigo) throws GeneralException {
         try {

          
            //comunicación entre seguridad-backend y ejbca es interna. Podemos desactivar ssl
            disableSslVerification();

            QName qname = new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSService");
            EjbcaWSService service = new EjbcaWSService(new URL(System.getProperty("service.ejbca.wsdl")), qname);
            EjbcaWS ws = service.getEjbcaWSPort();
            HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(ws).getConduit();

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");

            java.security.KeyStore keyStore = java.security.KeyStore.getInstance("pkcs12");
            char[] KEYSTOREPW = System.getProperty("service.ejbca.ws_key_pass").toCharArray();
            char[] KEYPW = System.getProperty("service.ejbca.ws_key_pass").toCharArray();
            keyStore.load(new FileInputStream(System.getProperty("service.ejbca.ws_key_location")), KEYSTOREPW);
            javax.net.ssl.KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, KEYPW);
            sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            TLSClientParameters tlsCP = new TLSClientParameters();
            tlsCP.setDisableCNCheck(true);
            tlsCP.setHostnameVerifier(allHostsValid);
            tlsCP.setSSLSocketFactory(sc.getSocketFactory());
            conduit.setTlsClientParameters(tlsCP);

            //Revoke todos los certificados existentes para el usuario
            try {
                ws.revokeUser(codigo, RevocationReasons.UNSPECIFIED.getDatabaseValue(), false);
            } catch (Exception ex) {
            }


        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgUsuario
     * @throws GeneralException
     */
    public byte[] generarCertificado(SgCertificateRequest req) throws GeneralException {

        try {

          
            //comunicación entre seguridad-backend y ejbca es interna. Podemos desactivar ssl
            disableSslVerification();

            QName qname = new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSService");
            EjbcaWSService service = new EjbcaWSService(new URL(System.getProperty("service.ejbca.wsdl")), qname);
            EjbcaWS ws = service.getEjbcaWSPort();
            HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(ws).getConduit();

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");

            java.security.KeyStore keyStore = java.security.KeyStore.getInstance("pkcs12");
            char[] KEYSTOREPW = System.getProperty("service.ejbca.ws_key_pass").toCharArray();
            char[] KEYPW = System.getProperty("service.ejbca.ws_key_pass").toCharArray();
            keyStore.load(new FileInputStream(System.getProperty("service.ejbca.ws_key_location")), KEYSTOREPW);
            javax.net.ssl.KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, KEYPW);
            sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            TLSClientParameters tlsCP = new TLSClientParameters();
            tlsCP.setDisableCNCheck(true);
            tlsCP.setHostnameVerifier(allHostsValid);
            tlsCP.setSSLSocketFactory(sc.getSocketFactory());
            conduit.setTlsClientParameters(tlsCP);

            //Revoke todos los certificados existentes para el usuario
            try {
                ws.revokeUser(req.getCodigo(), RevocationReasons.UNSPECIFIED.getDatabaseValue(), false);
            } catch (Exception ex) {
            }

            String cn = req.getCodigo();
            if (req.getNombre() != null) {
                cn = SofisStringUtils.convertNonAscii(SofisStringUtils.normalizarString(req.getNombre().replaceAll(",", "")));
            }

            UserDataVOWS user1 = new UserDataVOWS();
            user1.setUsername(req.getCodigo());
            user1.setPassword(req.getPassword());
            user1.setClearPwd(true);
            user1.setSubjectDN("CN=" + cn + ",O=" + System.getProperty("service.ejbca.ca.org") + ",C=SV,serialNumber=" + req.getCodigo());
            user1.setEmail(req.getEmail());
            user1.setSubjectAltName(null);
            user1.setStatus(STATUS_NEW);
            user1.setTokenType(TOKEN_TYPE_P12);
            user1.setEndEntityProfileName("EMPTY");
            user1.setCertificateProfileName("ENDUSER");
            user1.setCaName(System.getProperty("service.ejbca.ca.name"));

            ws.editUser(user1);

            //Crear nuevo certificado
            KeyStore ksenv2 = ws.pkcs12Req(req.getCodigo(), req.getPassword(), null, "1024", "RSA");
            String base64 = new String(ksenv2.getKeystoreData());
            byte[] pkc12 = org.bouncycastle.util.encoders.Base64.decode(base64);
            
            return pkc12;
                            

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }
    
}
