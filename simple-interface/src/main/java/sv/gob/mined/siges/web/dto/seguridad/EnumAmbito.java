/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.seguridad;


public enum EnumAmbito {
    MINED("MINED"),
    DEPARTAMENTAL("Departamental"),
    SEDE("Sede"),
    SECCION("Sección"),
    PERSONA("Persona"),;
    
    private final String text;

    private EnumAmbito(final String text) {
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
