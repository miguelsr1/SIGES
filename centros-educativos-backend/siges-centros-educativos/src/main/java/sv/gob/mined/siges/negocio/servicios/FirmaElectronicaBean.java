/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.net.URL;
import java.util.List;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.wsclient.ResultadoValidacion;
import sv.gob.mined.siges.wsclient.SigesFirmaServer;
import sv.gob.mined.siges.wsclient.SigesFirmaServerWS;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class FirmaElectronicaBean {

    private static final String NOMBRE_SISTEMA = "SIGES";

    public List<ResultadoValidacion> enviarDocumentosParaFirmar(List<byte[]> documentos, String transactionReturnUrl, String fileName, String usuarioEnviaDocumento, String usuarioDebeFirmarDocumento) throws GeneralException {
        try {
            String wsdlPath = null;
            try {
                wsdlPath = System.getProperty("service.enviar-documentos-firma-wsdl.baseUrl");
            } catch (Exception ex) {
                throw new TechnicalException("Error al cargar la configuracion enviar-documentos-firma-wsdl");
            }
            
            URL url = new URL(wsdlPath);
            QName qName = new QName("http://ws.firma.siges.sv/", "SigesFirmaServerWS");
            SigesFirmaServerWS service = new SigesFirmaServerWS(url, qName);
            SigesFirmaServer port = service.getSigesFirmaServerWSPort();
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdlPath.replaceFirst("\\?wsdl", ""));
            return port.enviarDocumentoParaFirmar(fileName, NOMBRE_SISTEMA, "urlRetorno", transactionReturnUrl, documentos, usuarioEnviaDocumento, usuarioDebeFirmarDocumento);

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    public List<ResultadoValidacion> obtenerDocumentosFirmados(String idDocumento, String fileName) throws GeneralException {
        try {
            String wsdlPath = null;
            try {
                wsdlPath = System.getProperty("service.enviar-documentos-firma-wsdl.baseUrl");
            } catch (Exception ex) {
                throw new TechnicalException("Error al cargar la configuracion enviar-documentos-firma-wsdl");
            }

            URL url = new URL(wsdlPath);
            QName qName = new QName("http://ws.firma.siges.sv/", "SigesFirmaServerWS");
            SigesFirmaServerWS service = new SigesFirmaServerWS(url, qName);
            SigesFirmaServer port = service.getSigesFirmaServerWSPort();
            BindingProvider bp = (BindingProvider) port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsdlPath.replaceFirst("\\?wsdl", ""));
                        
            return port.obtenerDocumentoFirmado(fileName, NOMBRE_SISTEMA, idDocumento);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    public byte[] obtenerDocumentoFirmado(String idDocumento, String fileName) throws GeneralException {
        List<ResultadoValidacion> listaDocumentos = this.obtenerDocumentosFirmados(idDocumento, fileName);
        if (listaDocumentos == null || listaDocumentos.isEmpty() || !listaDocumentos.get(0).getCode().equalsIgnoreCase("OK")
                        || listaDocumentos.get(0).getPdf() == null || listaDocumentos.get(0).getPdf().isEmpty()) {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_AL_OBTENER_DOCUMENTO_FIRMADO);
                    throw be;
        } else {
            return listaDocumentos.get(0).getPdf().get(0);
        }
    }
}
