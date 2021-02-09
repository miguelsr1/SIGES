package uy.com.sofis.pfea.ws;

import com.sofis.utils.UtilVarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.naming.NamingException;
import javax.xml.ws.Action;
import uy.com.sofis.pfea.entities.Documento;
import uy.com.sofis.pfea.sb.DocumentosSB;
import org.agesic.firma.datatypes.ResultadoValidacion;
import org.apache.commons.lang3.StringUtils;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.enums.EstadoDocumentoBandeja;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 *
 * Web Services de Integración en Java. Este servicio web es invocado por el backend de SIGES
 *
 * @author sofis-solutions
 */
@WebService(name = "SigesFirmaServer", serviceName = "SigesFirmaServerWS", portName = "SigesFirmaServerWSPort", targetNamespace = "http://ws.firma.siges.sv/")
public class SigesFirmaServer {

    private static final Logger logger = Logger.getLogger(SigesFirmaServer.class.getName());

    @Inject
    DocumentosSB documentosSB;

  
    /**
     * Operación para permitir a aplicaciones externas enviar documentos a
     * usuarios.
     *
     * @param documentos lista de documentos con la firma incorporada
     * @return 0 si se ejecutó de forma correcta, 1 si error. En caso de 1 se
     * despliega un error general al usuario del Applet.
     */
    @WebMethod()
    @WebResult(name = "respuesta", targetNamespace = "http://ws.firma.siges.sv/")
    @Action(input = "http://ws.firma.siges.sv/SigesFirmaWS/enviarDocumentoParaFirmarRequest",
            output = "http://ws.firma.siges.sv/SigesFirmaWS/enviarDocumentoParaFirmarResponse")
    public List<ResultadoValidacion> enviarDocumentoParaFirmar(
            @WebParam(name = "nombreArchivo", targetNamespace = "http://ws.firma.siges.sv/") String nombreArchivo,
            @WebParam(name = "nombreSistema", targetNamespace = "http://ws.firma.siges.sv/") String nombreSistema,
            @WebParam(name = "urlRetorno", targetNamespace = "http://ws.firma.siges.sv/") String urlRetorno,
            @WebParam(name = "urlRetornoNavegacion", targetNamespace = "http://ws.firma.siges.sv/") String urlRetornoNavegacion,
            @WebParam(name = "documentos", targetNamespace = "http://ws.firma.siges.sv/") List<DataHandler> documentos,
            @WebParam(name = "enviadoPorUsuario", targetNamespace = "http://ws.firma.siges.sv/") String enviadoPorUsuario,
            @WebParam(name = "debeFirmarUsuario", targetNamespace = "http://ws.firma.siges.sv/") String debeFirmarUsuario)
            throws IOException, NamingException {

        List ret = new ArrayList();

        //Verificar que se ha enviado un documento
        List<byte[]> docBytes = UtilVarios.getBytesFromDataHandler(documentos);
        if (docBytes.size() == 0) {
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_NO_PROPORCIONADO.name(),
                    ResultadosSistemasExternos.DOCUMENTO_NO_PROPORCIONADO.getDescripcion(), 0, null));
        }
        //Verificar que se ha identificado a la aplicación externa
        if (StringUtils.isBlank(nombreSistema)) {
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.SISTEMA_NO_IDENTIFICADO.name(),
                    ResultadosSistemasExternos.SISTEMA_NO_IDENTIFICADO.getDescripcion(), 0, null));
        }
        
        if (StringUtils.isBlank(enviadoPorUsuario)){
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.USUARIO_NO_IDENTIFICADO.name(),
                    ResultadosSistemasExternos.USUARIO_NO_IDENTIFICADO.getDescripcion(), 0, null));
        }

        if (StringUtils.isBlank(debeFirmarUsuario)){
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.USUARIO_NO_IDENTIFICADO.name(),
                    ResultadosSistemasExternos.USUARIO_NO_IDENTIFICADO.getDescripcion(), 0, null));
        }
        
        //Si falló alguna de las validaciones no se permite continuar
        if (!ret.isEmpty()) {
            return ret;
        }

        try {
            Documento docTransaccion = new Documento();
            docTransaccion.setNombreOriginal(nombreArchivo);
            docTransaccion.setFechaCreacion(new Date());
            docTransaccion.setFechaExpiracion(UtilVarios.sumarDiasFecha(new Date(), 10));
            docTransaccion.setIdentificador(UUID.randomUUID().toString());
            docTransaccion.setNombreArchivo(docTransaccion.getIdentificador() + ".pdf");
            docTransaccion.setEstadoDocumento(EstadoDocumentoBandeja.PF);
            docTransaccion.setFirmado(Boolean.FALSE);
            docTransaccion.setEliminado(Boolean.FALSE);
            docTransaccion.setUltimoUsuario("SERVICIO WEB");
            docTransaccion.setDebeFirmarUsuario(debeFirmarUsuario);
            docTransaccion.setEnviadoPorUsuario(enviadoPorUsuario);
            docTransaccion.setUltimaModificacion(new Date());
            docTransaccion.setDocumentoBytes(docBytes);
            docTransaccion.setDescripcion("Documento: " + nombreArchivo + "; Enviado por: " + nombreSistema);
            docTransaccion.setOrigen(nombreSistema);
            docTransaccion.setRetorno(urlRetorno);
            docTransaccion.setRetornoNavegacion(urlRetornoNavegacion);

            docTransaccion = documentosSB.crearDocumento(docTransaccion);
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.OK.name(), docTransaccion.getIdentificador(), 0, null));
            return ret;
        } catch (Exception e) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(e.getMessage());
            throw ge;

        }
    }

    /**
     * Operación para obtener un documento firmado
     *
     * @return 0 si se ejecutó de forma correcta, 1 si error. En caso de 1 se
     * despliega un error general al usuario del Applet.
     */
    @WebMethod()
    @WebResult(name = "respuesta", targetNamespace = "http://ws.firma.siges.sv/")
    @Action(input = "http://ws.firma.siges.sv/SigesFirmaWS/obtenerDocumentoFirmadoRequest",
            output = "http://ws.firma.siges.sv/SigesFirmaWS/obtenerDocumentoFirmadoResponse")
    public List<ResultadoValidacion> obtenerDocumentoFirmado(
            @WebParam(name = "nombreArchivo", targetNamespace = "http://ws.firma.siges.sv/") String nombreArchivo,
            @WebParam(name = "nombreSistema", targetNamespace = "http://ws.firma.siges.sv/") String nombreSistema,
            @WebParam(name = "idDocumento", targetNamespace = "http://ws.firma.siges.sv/") String idDocumento) throws IOException {
        List ret = new ArrayList();

        //Verificar que se ha identificado a la aplicación externa
        if (StringUtils.isBlank(nombreSistema)) {
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.SISTEMA_NO_IDENTIFICADO.name(),
                    ResultadosSistemasExternos.SISTEMA_NO_IDENTIFICADO.getDescripcion(), 0, null));
        }
        //Verificar que se ha identificado al documento
        if (StringUtils.isBlank(idDocumento)) {
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_NO_IDENTIFICADO.name(),
                    ResultadosSistemasExternos.DOCUMENTO_NO_IDENTIFICADO.getDescripcion(), 0, null));
        }
        //Si no pasó alguna validación no se permite continuar
        if (!ret.isEmpty()) {
            return ret;
        }
        List<byte[]> docsArray = new ArrayList();
        try {
            Documento documento = documentosSB.obtenerDocumentoPorIdentificador(idDocumento, true);
            if (documento == null) {
                ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_NO_ENCONTRADO.name(),
                        ResultadosSistemasExternos.DOCUMENTO_NO_ENCONTRADO.getDescripcion(), 0, null));
                return ret;
            }
            //Verificar que coincida con los datos del documento original
            if (!nombreArchivo.equals(documento.getNombreOriginal()) || !nombreSistema.equals(documento.getOrigen())) {
                ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_NO_ENCONTRADO.name(),
                        ResultadosSistemasExternos.DOCUMENTO_NO_ENCONTRADO.getDescripcion(), 0, null));
                return ret;
            }
            //No se devuelven documentos expirados
            if (documento.getEstadoDocumento().equals(EstadoDocumentoBandeja.EX)) {
                ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_EXPIRADO.name(),
                        ResultadosSistemasExternos.DOCUMENTO_EXPIRADO.getDescripcion(), 0, null));
                return ret;
            }
            //No se devuelven documentos eliminados
            if (documento.getEstadoDocumento().equals(EstadoDocumentoBandeja.EL)) {
                ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_ELIMINADO.name(),
                        ResultadosSistemasExternos.DOCUMENTO_ELIMINADO.getDescripcion(), 0, null));
                return ret;
            }
            //No se devuelven documentos rechazados
            if (documento.getEstadoDocumento().equals(EstadoDocumentoBandeja.EL)) {
                ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_RECHAZADO.name(),
                        ResultadosSistemasExternos.DOCUMENTO_RECHAZADO.getDescripcion(), 0, null));
                return ret;
            }
            //No se devuelven documentos que no estén firmados
            if (!documento.getEstadoDocumento().equals(EstadoDocumentoBandeja.FI)) {
                ret.add(new ResultadoValidacion(ResultadosSistemasExternos.DOCUMENTO_NO_FIRMADO.name(),
                        ResultadosSistemasExternos.DOCUMENTO_NO_FIRMADO.getDescripcion(), 0, null));
                return ret;
            }
            //Si llegó hasta acá es porque está todo OK
            docsArray = documentosSB.obtenerBytesDocumento(documento.getIdentificador(), true);
            ret.add(new ResultadoValidacion(ResultadosSistemasExternos.OK.name(), ResultadosSistemasExternos.OK.getDescripcion(), 0, docsArray));
            return ret;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
