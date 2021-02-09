package uy.com.sofis.pfea.sb.pki;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.agesic.firma.datatypes.ResultadoValidacion;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.StoreException;

import org.bouncycastle.x509.X509CertStoreSelector;
import org.bouncycastle.x509.X509Store;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

public class ValidacionRootCA{

    private static final Logger logger = Logger.getLogger(ValidacionRootCA.class.getName());
    
    public static final String CERTIFICADO_NO_CONFIABLE = "CERTIFICADO_NO_CONFIABLE";
    
    public static ResultadoValidacion validar(X509Certificate cert, String sRutaJks, String sPassJks) throws Exception {
        KeyStore trustStore = null;
        try {
            char[] passJks = sPassJks.toCharArray();
            trustStore = cargarKeystore(sRutaJks, passJks);
        } catch (Exception w) {
            throw w;
        }

        //Si el certificado es autofirmado debe estar en el truststore
        if (isSelfSigned(cert)) {
            if (trustStore.getCertificateAlias(cert) == null) {
                ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_NO_CONFIABLE, "El certificado es autofirmado y no es de confianza", 0, null);
                return resultadoValidacion;
            }
        }

        //Validar la cadena de certificación
        Set<X509Certificate> additionalCerts = new HashSet<>();
        Set<X509Certificate> trustedRootCerts = new HashSet<>();
        Set<X509Certificate> intermediateCerts = new HashSet<>();

        PKIXParameters params = new PKIXParameters(trustStore);
        for (TrustAnchor ta : params.getTrustAnchors()) {
            X509Certificate trustCert = ta.getTrustedCert();
            additionalCerts.add(trustCert);
        }
        for (X509Certificate additionalCert : additionalCerts) {
            if (isSelfSigned(additionalCert)) {
                trustedRootCerts.add(additionalCert);
            } else {
                intermediateCerts.add(additionalCert);
            }
        }
        try {
            PKIXCertPathBuilderResult verifiedCertChain = verifyCertificate(cert, trustedRootCerts, intermediateCerts);
            return null;
        } catch(CertPathBuilderException cpbEx) {
            logger.log(Level.SEVERE, "La cadena de certificación no es válida", cpbEx);
            ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_NO_CONFIABLE, "La cadena de certificación no es válida", 0, null);
            return resultadoValidacion;
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "No se pudo validar la cadena de certificación", ex);
            ResultadoValidacion resultadoValidacion = new ResultadoValidacion(CERTIFICADO_NO_CONFIABLE, "No se pudo validar la cadena de certificación", 0, null);
            return resultadoValidacion;
        }
    }

    public static boolean isSelfSigned(X509Certificate cert) throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
        try {
            PublicKey key = cert.getPublicKey();
            cert.verify(key);
            return true;
        } catch (SignatureException | InvalidKeyException sigEx) {
            return false;
        }
    }

    private static PKIXCertPathBuilderResult verifyCertificate(X509Certificate cert, Set<X509Certificate> trustedRootCerts, Set<X509Certificate> intermediateCerts) throws GeneralSecurityException {
        Set<TrustAnchor> trustAnchors = new HashSet<>();
        for (X509Certificate trustedRootCert : trustedRootCerts) {
            trustAnchors.add(new TrustAnchor(trustedRootCert, null));
        }
        
        for (X509Certificate intermediateCert : intermediateCerts) {
            trustAnchors.add(new TrustAnchor(intermediateCert, null));
        }
        
        X509CertSelector selector = new X509CertSelector();
        selector.setCertificate(cert);
        
        PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);
        //PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, null);
        
        pkixParams.setRevocationEnabled(false);
        Set<X509Certificate> allCerts = new HashSet<>(trustedRootCerts);
        allCerts.addAll(intermediateCerts);
        allCerts.add(cert);
        CertStore allCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(allCerts), "BC");
        
        pkixParams.addCertStore(allCertStore);
        CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");
        
        try {
            //Validar el certificado y su cadena de 
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(pkixParams);
            return result;
        }catch(CertPathBuilderException cpbEx) {
            
            logger.log(Level.SEVERE, cpbEx.getMessage(), cpbEx);
            
            //Obtener el certificado con el cual fue firmado
            try {
                X509Certificate issuer = descargarCertificadoEmisor(cert);
                if(issuer==null) {
                    throw cpbEx;
                }
                cert.verify(issuer.getPublicKey());
                
                return verifyCertificate(issuer, trustedRootCerts, intermediateCerts);
            }catch(Exception ex) {
                logger.log(Level.SEVERE, "Error al validar el certificado", ex);
                throw cpbEx;
            }
        }
    }

    
    private static X509Certificate descargarCertificadoEmisor(X509Certificate cert) throws CertificateException, MalformedURLException, IOException {
        byte[] extVal = cert.getExtensionValue("1.3.6.1.5.5.7.1.1");
        AuthorityInformationAccess aia = AuthorityInformationAccess.getInstance(X509ExtensionUtil.fromExtensionValue(extVal));
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        // check if there is a URL to issuer's certificate
        AccessDescription[] descriptions = aia.getAccessDescriptions();
        X509Certificate issuer = null;
        for (AccessDescription ad : descriptions) {
            // check if it's a URL to issuer's certificate
            if (ad.getAccessMethod().equals(X509ObjectIdentifiers.id_ad_caIssuers)) {
                GeneralName location = ad.getAccessLocation();
                if (location.getTagNo() == GeneralName.uniformResourceIdentifier) {
                    String issuerUrl = location.getName().toString();
                    // http URL to issuer (test in your browser to see if it's a valid certificate)
                    // you can use java.net.URL.openStream() to create a InputStream and create
                    // the certificate with your CertificateFactory
                    URL url = new URL(issuerUrl);
                    issuer = (X509Certificate) certificateFactory.generateCertificate(url.openStream());
                }
            }
        }
        return issuer;
    }
    
    protected static Collection findCertificates(X509CertStoreSelector certSelect, List certStores) throws Exception {
        Set certs = new HashSet();
        Iterator iter = certStores.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof X509Store) {
                X509Store certStore = (X509Store) obj;
                try {
                    certs.addAll(certStore.getMatches(certSelect));
                } catch (StoreException e) {
                    throw new Exception("Problem while picking certificates from X.509 store.", e);
                }
            } else {
                CertStore certStore = (CertStore) obj;
                try {
                    certs.addAll(certStore.getCertificates(certSelect));
                } catch (CertStoreException ex) {
                    throw new Exception("Problem while picking certificates from certificate store.", ex);
                }
            }
        }
        return certs;
    }

    private static KeyStore cargarKeystore(String rutaJks, char[] ksPass) throws Exception {
        KeyStore keyStore = null;
        try {
            File jks = new File(rutaJks);
            if (jks == null) {
                throw new Exception("No se pudo encontrar el keystore [" + rutaJks + "]");
            }
            Security.addProvider(new BouncyCastleProvider());
            keyStore = KeyStore.getInstance("jks");
            InputStream input = new FileInputStream(jks);
            keyStore.load(input, ksPass);
        } catch (IOException ioEx) {
            if (ioEx.getCause() instanceof UnrecoverableKeyException) {
                throw new Exception("La contraseña del keystore no es correcta");
            }
            throw new Exception("No se pudo cargar el keystore: "+ioEx.getMessage());
        }
        return keyStore;
    }
}
