package gob.mined.siap2.entities.enums;



public enum EstadoDesembolsoTransferenciaComponente {
    
    REALIZADO("Realizado"),
    CERRADO("Cerrado");

    private final String label;
    
    private EstadoDesembolsoTransferenciaComponente(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public String getTexto(){
        switch(this){
            case REALIZADO:
                return "Realizado";
            case CERRADO:
                return "Cerrado";
            default: 
                return "";
        }
    }
    
}
