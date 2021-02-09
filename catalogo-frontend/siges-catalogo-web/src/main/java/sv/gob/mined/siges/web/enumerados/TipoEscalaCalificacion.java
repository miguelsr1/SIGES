/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum TipoEscalaCalificacion {
    NUMERICA("Numérica"),
    CONCEPTUAL("Conceptual");
    
    private final String text;

    private TipoEscalaCalificacion(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
    
    
}
