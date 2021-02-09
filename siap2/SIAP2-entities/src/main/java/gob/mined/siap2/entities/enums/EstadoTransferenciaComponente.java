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
public enum EstadoTransferenciaComponente {
    EN_PROCESO("En proceso", 1),
    AUTORIZADA("Autorizada", 2),
    REALIZADA("Realizada", 3),
    CERRADA("Cerrada", 4);

    private final String label;
    private final Integer valor;

     private EstadoTransferenciaComponente(String label, Integer valor) {
        this.label = label;
        this.valor = valor;
    }


    public Integer getValor() {
        return valor;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getTexto(){
        switch(this){
            case EN_PROCESO:
                return "En proceso";
            case AUTORIZADA:
                return "Autorizada";
            case REALIZADA:
                return "Realizada";
            case CERRADA:
                return "Cerrada";
            default: 
                break;
        }
        return "";
    }


}
