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
public enum TipoFactura {

    FACTURA("TipoFactura.FACTURA"),
    RECIBO("TipoFactura.RECIBO");
   

    private String label;

    private TipoFactura(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getTexto(){
        String texto = "";
        if(this == FACTURA){
            texto = "FACTURA";
        } else {
            texto = "RECIBO";
        }
        return texto;
    }


}
