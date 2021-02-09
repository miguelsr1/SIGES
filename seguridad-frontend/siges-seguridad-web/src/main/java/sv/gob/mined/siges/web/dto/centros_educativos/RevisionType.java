/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

/**
 *
 * @author Sofis Solutions
 */

public enum RevisionType {
    ADD("Creación"),
    MOD("Edición"),
    DEL("Eliminación");

    private final String text;

    private RevisionType(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    
}

