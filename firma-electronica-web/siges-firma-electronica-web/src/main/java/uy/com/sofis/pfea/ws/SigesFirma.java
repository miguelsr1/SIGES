package uy.com.sofis.pfea.ws;

import com.sofis.utils.UtilVarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.Action;
import uy.com.sofis.pfea.entities.Documento;
import uy.com.sofis.pfea.sb.DocumentosSB;
import org.agesic.firma.datatypes.ResultadoValidacion;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.enums.EstadoDocumentoBandeja;
import uy.com.sofis.pfea.exceptions.GeneralException;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 *
 * Web Services de Integración en Java. Este servicio web es invocado por el
 * applet.
 *
 * @author sofis-solutions
 */
@WebService(name = "SigesFirma", serviceName = "SigesFirmaWS", portName = "SigesFirmaWSPort", targetNamespace = "http://ws.firma.siges.sv/")
public class SigesFirma {

    private static final Logger logger = Logger.getLogger(SigesFirma.class.getName());

    @Inject
    DocumentosSB documentosSB;

    @WebMethod()
    @WebResult(name = "respuesta", targetNamespace = "http://ws.firma.siges.sv/")
    public List<byte[]> obtenerDocumentos(@WebParam(name = "id_transaccion", targetNamespace = "http://ws.firma.siges.sv/") String idTransaccion) {
        try {
            Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTransaccion, true);
            if (documento == null) {
                GeneralException g = new GeneralException("El documento solicitado no existe o ha expirado");
                throw g;
            }

            List<byte[]> docsArray = new ArrayList();
            //Si el documento está firmado se envía la versión firmada (para volver a firmarlo), sino se envía la versión original
            docsArray = documentosSB.obtenerBytesDocumento(documento.getIdentificador(), documento.getFirmado());
            return docsArray;
        } catch (GeneralException g) {
            throw g;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al obtener documentos para entregar al componente de firma (idTransaccion=" + idTransaccion + ")", ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Metodo invocado con el APPLET una vez que se firmaron los documentos de
     * forma correcta.
     *
     * @param idTransaccion id de transacción
     * @param documentos lista de documentos con la firma incorporada
     * @return 0 si se ejecutó de forma correcta, 1 si error. En caso de 1 se
     * despliega un error general al usuario del Applet.
     */
    @WebMethod()
    @WebResult(name = "respuesta", targetNamespace = "http://ws.firma.siges.sv/")
    @Action(input = "http://ws.firma.siges.sv/SigesFirmaWS/comunicarDocumentosRequest",
            output = "http://ws.firma.siges.sv/SigesFirmaWS/comunicarDocumentosResponse")
    public List<ResultadoValidacion> comunicarDocumentos(
            @WebParam(name = "id_transaccion", targetNamespace = "http://ws.firma.siges.sv/") String idTransaccion,
            @WebParam(name = "documentos", targetNamespace = "http://ws.firma.siges.sv/") List<DataHandler> documentos,
            @WebParam(name = "certificate", targetNamespace = "http://ws.firma.siges.sv/") byte[] certificate) throws IOException {

        try {
            List ret = new ArrayList();
            Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idTransaccion, true);
            if (documento == null) {
                GeneralException g = new GeneralException("El documento a firmar no existe o ha expirado");
                throw g;
            }
            List<byte[]> docBytes = UtilVarios.getBytesFromDataHandler(documentos);

            documento.setDocumentoBytes(docBytes);
            documento.setFechaExpiracion(UtilVarios.sumarDiasFecha(new Date(), 10));
            documento.setEstadoDocumento(EstadoDocumentoBandeja.FI);
            documento.setEliminado(Boolean.FALSE);
            documento.setFirmado(Boolean.TRUE);
            documento.setUltimoUsuario("COMPONENTE FIRMA");
            documento.setUltimaModificacion(new Date());
            documentosSB.guardarDocumentoFirmado(documento);
            return ret;

        } catch (GeneralException g) {
            throw g;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al recbir documentos del componente de firma (idTransaccion=" + idTransaccion + ")", ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
 
}
