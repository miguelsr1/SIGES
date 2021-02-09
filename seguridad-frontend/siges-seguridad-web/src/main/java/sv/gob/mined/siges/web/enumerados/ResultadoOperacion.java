/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum ResultadoOperacion {
    
    OK("OK"),
    ERROR("ERROR");

    private final String text;

    private ResultadoOperacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
