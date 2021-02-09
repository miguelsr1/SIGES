/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados.centros_educativos;


public enum EnumAmbito {
    MINED("MINED"),
    DEPARTAMENTAL("Departamental"),
    SEDE("Sede"),
    PARTICION_SEDE("Partición Sede"),
    MODALIDADES_FLEXIBLES("Modalidades Flexibles"),
    SECCION("Sección"),  
    PERSONA("Persona"),
    SISTEMA_INTEGRADO("Sistema Integrado"),
    UNIDAD_ADMINISTRATIVA("Unidad administrativa"),
    IMPLEMENTADORA("Implementadora");
    
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
