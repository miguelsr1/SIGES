
package gob.mined.siap2.entities.enums;


public enum EstadoDesembolso {
 
    P("En proceso");
    
    private final String label;
    
    
    private EstadoDesembolso(String label){
        this.label = label;
    }

    
    
    public String getLabel() {
        return label;
    }

    
}
