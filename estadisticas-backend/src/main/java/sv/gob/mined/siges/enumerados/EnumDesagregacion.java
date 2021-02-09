/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 *
 * @author USUARIO
 */
public enum EnumDesagregacion {
    
    DEPARTAMENTO("Departamento"),
    ZONA("Zona"),
    SECTOR_OFI_PRI("Sector (oficial y privado)"),
    SECTOR_PUB_PRI("Sector (público y privado)"),
    SEXO("Sexo"),
    NIVEL("Nivel"),
    GRADO("Grado"),
    CANTIDAD_ANIOS("Cantidad de años");
    
    private final String text;

    private EnumDesagregacion(final String text) {
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
