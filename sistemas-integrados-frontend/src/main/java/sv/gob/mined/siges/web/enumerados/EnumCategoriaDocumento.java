/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumCategoriaDocumento {
    GOBERNANZA("Estructura de gobernanza"),
    OAE("Actas OAE"),
    OTROS("Otros");
    
    private final String text;

    private EnumCategoriaDocumento(final String text) {
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
