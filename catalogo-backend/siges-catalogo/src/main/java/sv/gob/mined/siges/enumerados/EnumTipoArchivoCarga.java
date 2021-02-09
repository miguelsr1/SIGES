/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumTipoArchivoCarga {
    DOCUMENTOS("Documentos"),
    IMAGENES("Imagenes"),
    DOC_IMAGENES("Documentos e Imagenes");
    
    private final String text;

    private EnumTipoArchivoCarga(final String text) {
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
