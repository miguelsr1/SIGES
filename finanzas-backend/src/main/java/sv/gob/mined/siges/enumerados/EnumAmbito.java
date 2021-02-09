/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Àmbito de aplicación de seguridad de un rol.
 *
 * @author jgiron
 */
public enum EnumAmbito {
    MINED("General"),
    DEPARTAMENTAL("Departamental"),
    SEDE("Sede"),
    PARTICION_SEDE("Partición Sede"),
    SECCION("Sección"),
    SISTEMA_INTEGRADO("Sistema Integrado"),
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
