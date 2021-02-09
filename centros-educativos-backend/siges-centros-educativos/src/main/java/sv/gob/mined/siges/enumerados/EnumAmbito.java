/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumAmbito {
    MINED("General"),
    DEPARTAMENTAL("Departamental"),
    IMPLEMENTADORA("Implementadora"),
    SEDE("Sede"),
    PARTICION_SEDE("Partición Sede"),
    MODALIDADES_FLEXIBLES("Modalidades Flexibles"),
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
