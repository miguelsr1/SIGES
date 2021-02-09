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
public enum TipoDestinoFactura {

    ACTA_CONTRATO_OC("TipoDestinoFactura.ACTA_CONTRATO_OC"),
    POLIZA_CONCENTRACION("TipoDestinoFactura.POLIZA_CONCENTRACION");
   

    private String label;

    private TipoDestinoFactura(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static class Values {
        public static final String ACTA_CONTRATO_OC = "ACTA_CONTRATO_OC";
        public static final String POLIZA_CONCENTRACION = "POLIZA_CONCENTRACION";
    }


}
