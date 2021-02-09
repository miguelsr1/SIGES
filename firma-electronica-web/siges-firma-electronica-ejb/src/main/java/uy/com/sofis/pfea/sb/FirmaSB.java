/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.sb;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import org.agesic.firma.datatypes.InfoFirma;
import org.agesic.firma.datatypes.ResultadoValidacion;
import org.apache.commons.lang3.BooleanUtils;
import uy.com.sofis.pfea.entities.Documento;
import uy.com.sofis.pfea.sb.pki.ValidacionCRL;
import uy.com.sofis.pfea.sb.pki.ValidacionRootCA;
import uy.com.sofis.pfea.sb.pki.ValidacionVigencia;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 *
 * @author sofis3
 */
@Stateless
@LocalBean
public class FirmaSB {

    private static final Logger logger = Logger.getLogger(FirmaSB.class.getName());

    public static Integer LONGITUD_TOKEN_OMISION = 20;

    private static final DateFormat FORMATO_FECHA_TRANSFRONTERIZA = new SimpleDateFormat("yyyy-MM-dd EEE HH:mm:ss Z", Locale.forLanguageTag("es"));
    
    
    @Resource
    SessionContext ctx;

    @EJB
    DocumentosSB docsSB;

    @EJB
    ConfiguracionSB confSB;

    public List<InfoFirma> obtenerInfoFirmas(Integer docId) {
        Documento doc = docsSB.obtenerDocumentoPorId(docId);
        return this.obtenerInfoFirmas(doc);
    }
    
    public List<InfoFirma> obtenerInfoFirmas(Documento doc) {
        
        boolean validarConfianza = false;
        String pathTrustore = null;
        String passTrustore = null;
        boolean validarCrl = false;
        String crlUrl = null;

        try {

            Boolean bValidarConfianza = BooleanUtils.toBooleanObject(confSB.obtenerConfiguracionPorCodigos("FIRMA_VALIDAR_CONFIANZA"));
            if (bValidarConfianza != null) {
                validarConfianza = bValidarConfianza;
                if (validarConfianza) {
                    pathTrustore = confSB.obtenerConfiguracionPorCodigos("FIRMA_VALIDAR_CONFIANZA_TRUSTORE_PATH");
                    passTrustore = confSB.obtenerConfiguracionPorCodigos("FIRMA_VALIDAR_CONFIANZA_TRUSTORE_PASS");
                }
            }
            Boolean bValidarCrl = BooleanUtils.toBooleanObject(confSB.obtenerConfiguracionPorCodigos("FIRMA_VALIDAR_CRL"));
            if (bValidarCrl != null) {
                validarCrl = bValidarCrl;
                if (validarCrl) {
                    crlUrl = confSB.obtenerConfiguracionPorCodigos("FIRMA_VALIDAR_CRL_URL_INTERNA");
                }
            }

            List<byte[]> bytesList = null;   
            if (doc.getDocumentoBytes() != null){       
                bytesList = doc.getDocumentoBytes();
            } else {
                bytesList = docsSB.obtenerBytesDocumento(doc.getIdentificador(), true);
            }
            
            byte[] bytes = null;
            if (bytesList.size() > 0) {
                bytes = bytesList.get(0);
            }

            if (bytes == null) {
                bytesList = docsSB.obtenerBytesDocumento(doc.getIdentificador(), false);
                if (bytesList.size() > 0) {
                    bytes = bytesList.get(0);
                }
            }

            if (bytes == null) {
                logger.log(Level.SEVERE, "No se pudo obtener el documento");
                return null;
            }

            // Esto es necesario porque las versiones más recientes de
            // JDK no implementan algunos algoritmos o le cambian el nombre
            Provider provider;
            try {
                Class c = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
                Security.insertProviderAt((java.security.Provider) c.newInstance(), 2000);
                provider = (Provider) c.newInstance();
            } catch (Exception ex) {
                provider = null;
            }

            PdfReader reader = new PdfReader(bytes);
            AcroFields af = reader.getAcroFields();
            List<String> names = af.getSignatureNames();
            List<InfoFirma> infos = new ArrayList();
            if (names != null) {
                for (int k = 0; k < names.size(); ++k) {
                    boolean valida = true;
                    String mensaje = "";
                    String name = names.get(k);
                    PdfPKCS7 pkcs7;
                    if (provider != null) {
                        pkcs7 = af.verifySignature(name, provider.getName());
                    } else {
                        pkcs7 = af.verifySignature(name);
                    }
                    Calendar cal = pkcs7.getSignDate();
                    Certificate pkc[] = pkcs7.getSignCertificateChain();
                    if (!pkcs7.verify()) {
                        valida = false;
                        mensaje = "El documento ha sido modificado luego de firmado.";
                    } else {
                       
                        for (Certificate x509Certificate : pkc) {
                            try {
                                ResultadoValidacion resultadoValidacion = ValidacionVigencia.validar((X509Certificate) x509Certificate, cal);
                                if (resultadoValidacion != null) {
                                    valida = false;
                                    mensaje = resultadoValidacion.getMessage();
                                }
                            } catch (Exception ex) {
                                logger.log(Level.SEVERE, "No se pudo verificar la vigencia del certificado.", ex);
                                valida = false;
                                mensaje = "No se pudo verificar la vigencia del certificado.";
                            }
                            if (valida && validarConfianza) {
                                try {
                                    if (pathTrustore != null && !pathTrustore.equalsIgnoreCase("") && passTrustore != null && !passTrustore.equalsIgnoreCase("")) {
                                        ResultadoValidacion resultadoValidacion = ValidacionRootCA.validar((X509Certificate) x509Certificate, pathTrustore, passTrustore);
                                        if (resultadoValidacion != null) {
                                            valida = false;
                                            mensaje = resultadoValidacion.getMessage();
                                        }
                                    } else {
                                        logger.log(Level.SEVERE, "No se pudo verificar la confianza del certificado: no está configurado el truststore");
                                        valida = false;
                                        mensaje = "No se pudo verificar la confianza del certificado.";
                                    }
                                } catch (Exception ex) {
                                    logger.log(Level.SEVERE, "No se pudo verificar la confianza del certificado", ex);
                                    valida = false;
                                    mensaje = "No se pudo verificar la confianza del certificado.";
                                }
                            }
                            if (valida && validarCrl) {
                                try {
                                    ResultadoValidacion resultadoValidacion = ValidacionCRL.validar((X509Certificate) x509Certificate, crlUrl);
                                    if (resultadoValidacion != null) {
                                        valida = false;
                                        mensaje = resultadoValidacion.getMessage();
                                    }
                                } catch (Exception ex) {
                                    logger.log(Level.SEVERE, "No se pudo verificar si el certificado está revocado", ex);
                                    valida = false;
                                    mensaje = "No se pudo verificar si el certificado está revocado.";
                                }
                            }
                        }
                    }

                    X509Certificate cert = (X509Certificate) pkcs7.getSigningCertificate();
                    Date fecha = pkcs7.getSignDate() != null ? pkcs7.getSignDate().getTime() : null;
                    String sdn = cert.getSubjectDN() != null ? cert.getSubjectDN().getName() : null;
                    String sname = determinarSubject(pkcs7);
                    String emisor = determinarIssuer(pkcs7);
                    infos.add(new InfoFirma(names.size() - k, sname, fecha, sdn, valida, mensaje, emisor, cert.getEncoded(), FirmaSB.getCertificateIdentificacion(sdn)));
                }
            }     
            return infos;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "No se pudo obtener las firmas del documento", ex);
            return null;
        }
    
    }
    
    private static String getCertificateIdentificacion(String sdn) {
        StringTokenizer stk = new StringTokenizer(sdn, ",");
        String token;
        String respuesta = null;
        while (stk.hasMoreTokens()) {
            token = stk.nextToken();
            String lowerToken = token.toLowerCase();
            if (lowerToken.contains("serialnumber")) {
                String[] partes = token.split("=");
                respuesta = partes[1];
                break;
            }
        }
        return respuesta;
    }
    
    private String determinarSubject(PdfPKCS7 pkcs7) {
        try {
            if (pkcs7.getSignName() != null) {
                return pkcs7.getSignName();
            }
            String cn = null;
            String email = null;
            X509Certificate cert = (X509Certificate) pkcs7.getSigningCertificate();
            LdapName ln = new LdapName(cert.getSubjectDN().getName());
            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    cn = rdn.getValue().toString();
                    break;
                }
                if (rdn.getType().equalsIgnoreCase("E") || rdn.getType().equalsIgnoreCase("EMAIL") || rdn.getType().equalsIgnoreCase("EMAILADDRESS")) {
                    email = rdn.getValue().toString();
                    break;
                }
            }
            return cn != null ? cn : email;
        } catch (Exception ex) {
            Logger.getLogger(FirmaSB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String determinarIssuer(PdfPKCS7 pkcs7) {
        try {
            X509Certificate cert = (X509Certificate) pkcs7.getSigningCertificate();
            LdapName ln = new LdapName(cert.getIssuerDN().getName());
            String cn = null;
            String email = null;
            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    cn = rdn.getValue().toString();
                    break;
                }
                if (rdn.getType().equalsIgnoreCase("E") || rdn.getType().equalsIgnoreCase("EMAIL") || rdn.getType().equalsIgnoreCase("EMAILADDRESS")) {
                    email = rdn.getValue().toString();
                    break;
                }
            }
            return cn != null ? cn : email != null ? email : cert.getIssuerX500Principal().getName();
        } catch (Exception ex) {
            Logger.getLogger(FirmaSB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
