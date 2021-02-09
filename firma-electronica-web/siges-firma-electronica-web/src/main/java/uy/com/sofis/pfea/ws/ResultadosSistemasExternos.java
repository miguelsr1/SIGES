
package uy.com.sofis.pfea.ws;

/**
 * @author Sofis Solutions
 */
public enum ResultadosSistemasExternos {

    OK("OK"),
    USUARIO_NO_IDENTIFICADO("No se ha identificado al usuario destinatario del documento"),
    DOCUMENTO_NO_PROPORCIONADO("No se ha proporcionado el documento"),
    SISTEMA_NO_IDENTIFICADO("No se ha identificado al sistema original"),
    DOCUMENTO_NO_IDENTIFICADO("No se ha identificado al documento"),
    DOCUMENTO_NO_ENCONTRADO("No se ha encontrado el documento especificado"),
    DOCUMENTO_EXPIRADO("El documento está expirado"),
    DOCUMENTO_ELIMINADO("El documento fue eliminado"),
    DOCUMENTO_RECHAZADO("El documento fue rechazado"),
    DOCUMENTO_NO_FIRMADO("El documento no está firmado aún");
    
    private final String descripcion;

    private ResultadosSistemasExternos(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    
    
}
