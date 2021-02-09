/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoCurso {
    CREADO("Creado"),
    PUBLICADO("Publicado"),
    CERRADO("Cerrado");

    private final String text;

    private EnumEstadoCurso(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}