/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.enums;

/**
 *
 * @author Sofis Solutions
 */
public enum EnumTipoTransferencia {
    PRESUPUESTO_ESCOLAR("Presupuesto escolar"),
    OTRAS_TRANSFERENCIAS("Otras transferencias a centros educativos");

    private String label;

    private EnumTipoTransferencia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
