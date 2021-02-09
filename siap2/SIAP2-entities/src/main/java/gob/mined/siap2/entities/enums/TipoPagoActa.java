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
public enum TipoPagoActa {

    PORCENTAJE("TipoPagoActa.PORCENTAJE"),
    MONTO_FIJO("TipoPagoActa.MONTO_FIJO");
   

    private String label;

    private TipoPagoActa(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }


}
