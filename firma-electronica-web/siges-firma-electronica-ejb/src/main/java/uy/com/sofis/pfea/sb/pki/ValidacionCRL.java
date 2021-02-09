/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.sb.pki;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.agesic.firma.datatypes.ResultadoValidacion;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extensions;

/**
 * Clase que verifica un certificado X509 contra un CRLS
 *
 * @author Sofis Solutions
 */
public class ValidacionCRL {

    private static final Logger logger = Logger.getLogger(ValidacionRootCA.class.getName());

    public static final String CERTIFICADO_REVOCADO = "CERTIFICADO_REVOCADO";

    private static final Map<String, X509CRL> crlCache = new HashMap<>();

    /**
     * Se fija el estado de revocacion del certificado contra la CRL indicada.
     * Si no existe o no es accesible, utiliza las crl definidas en el
     * certificado
     *
     * @param cert el certificado a validar
     * @param crlDefaultUrl default url de la crl
     * @throws CertificateVerificationException if the certificate fue revocado
     */
    public static ResultadoValidacion validar(X509Certificate cert, String crlDefaultUrl) throws Exception {
        if (!StringUtils.isBlank(crlDefaultUrl)) {
            try {
                X509CRL crl = downloadCRL(crlDefaultUrl);
                if (crl == null) {
                    logger.log(Level.SEVERE, "Error al validar certificado contra default CRL. La default CRL es null.");
                    return validarCRLSDelCertificado(cert);
                }
                if (crl.isRevoked(cert)) {
                    ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_REVOCADO, "El certificado está revocado", 0, null);
                    return resultadoValidacion;
                }
            } catch (Exception w) {
                logger.log(Level.SEVERE, "Error al validar certificado contra default CRL.", w);
                return validarCRLSDelCertificado(cert);
            }
        } else {
            logger.log(Level.INFO, "Default CRL no seteada.");
            return validarCRLSDelCertificado(cert);
        }

        return null;
    }

    /**
     * Extrae la url de la CRL del certificado, si este posee la extension
     * correspondiente y se fija el estado de revocacion del certificado contra
     * la CRL
     *
     * @param cert el certificado a validar
     * @throws CertificateVerificationException if the certificate fue revocado
     */
    public static ResultadoValidacion validarCRLSDelCertificado(X509Certificate cert) throws Exception {
        //para esto el certificado debe tener la extension 2.5.29.31 - CRL Distribution Points.
        //En esta seccion está la url desde donde se descargan los certificados revocados
        logger.log(Level.INFO, "Invocando validación de CRL definidos en el certificado");
        List<String> crlDistPoints = new ArrayList();
        try {
            crlDistPoints = getCrlDistributionPoints(cert);
        } catch (CertificateParsingException | IOException ex) {
            logger.log(Level.SEVERE, "No se pudo determinar la lista de proveedores de CRLs", ex);
            throw ex;
        }
        if (crlDistPoints != null && !crlDistPoints.isEmpty()) {
            for (String crlDP : crlDistPoints) {
                try {
                    X509CRL crl = downloadCRL(crlDP);
                    if (crl != null && crl.isRevoked(cert)) {
                        ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_REVOCADO, "El certificado está revocado", 0, null);
                        return resultadoValidacion;
                    }
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "No se pudo determinar la lista de proveedores de CRLs", ex);
                    ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_REVOCADO, "No se pudo obtener la URL de descarga de la lista de revocación: (" + crlDP + ")", 0, null);
                    return resultadoValidacion;
                }
            }
        } else {
            ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_REVOCADO, "No se pudo obtener la lista de crls del certificado", 0, null);
            return resultadoValidacion;
        }
        return null;
    }

    /**
     * Descarga la CRL a partir de la URL dada. Soporta http, https, ftp.
     */
    private static X509CRL downloadCRL(String crlURL) throws Exception {
        if (crlCache.containsKey(crlURL)) {
            X509CRL crl = crlCache.get(crlURL);
            Date nextUpdate = crl.getNextUpdate();
            if (nextUpdate != null && nextUpdate.after(new Date())) {
                //La CRL está vigente
                return crl;
            } else {
                //La CRL no está vigente
                crlCache.remove(crlURL);
            }
        }
        if (crlURL.startsWith("http://") || crlURL.startsWith("https://") || crlURL.startsWith("ftp://")) {
            X509CRL crl = downloadCRLFromWeb(crlURL);
            if (crl != null) {
                synchronized (crlCache) {
                    crlCache.put(crlURL, crl);
                }
                return crl;
            }
        }
        return null;
    }

    /**
     * Descarga la CRL desde la URL dada (HTTP/HTTPS/FTP)
     */
    private static X509CRL downloadCRLFromWeb(String crlURL)
            throws MalformedURLException, IOException, CertificateException,
            CRLException {

        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            // Create an ssl socket factory with our all-trusting manager
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });

        } catch (Exception ex) {

        }

        URL url = new URL(crlURL);
        InputStream crlStream = url.openStream();

        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509CRL crl = (X509CRL) cf.generateCRL(crlStream);
            return crl;
        } finally {
            crlStream.close();
        }
    }

    /**
     * Extrae del certificado todas las URL de las CRLs si el certificado posee
     * la extension 2.5.29.31 - CRL Distribution Points. Si no la posee retorna
     * la lista vacia
     */
    private static List<String> getCrlDistributionPoints(X509Certificate cert) throws CertificateParsingException, IOException {
        byte[] crldpExt = cert.getExtensionValue(X509Extensions.CRLDistributionPoints.getId());
        if (crldpExt == null) {
            List<String> emptyList = new ArrayList<>();
            return emptyList;
        }
        ASN1InputStream oAsnInStream = new ASN1InputStream(new ByteArrayInputStream(crldpExt));
        DERObject derObjCrlDP = oAsnInStream.readObject();
        DEROctetString dosCrlDP = (DEROctetString) derObjCrlDP;
        byte[] crldpExtOctets = dosCrlDP.getOctets();
        ASN1InputStream oAsnInStream2 = new ASN1InputStream(new ByteArrayInputStream(crldpExtOctets));
        DERObject derObj2 = oAsnInStream2.readObject();
        CRLDistPoint distPoint = CRLDistPoint.getInstance(derObj2);
        List<String> crlUrls = new ArrayList<>();
        for (DistributionPoint dp : distPoint.getDistributionPoints()) {
            DistributionPointName dpn = dp.getDistributionPoint();
            // Look for URIs in fullName
            if (dpn != null) {
                if (dpn.getType() == DistributionPointName.FULL_NAME) {
                    GeneralName[] genNames = GeneralNames.getInstance(dpn.getName()).getNames();
                    for (GeneralName genName : genNames) {
                        if (genName.getTagNo() == GeneralName.uniformResourceIdentifier) {
                            String url = DERIA5String.getInstance(genName.getName()).getString();
                            crlUrls.add(url);
                        }
                    }
                }
            }
        }
        return crlUrls;
    }
}
