/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.enums;

/**
 *
 * @author Gustavo  
 */
public enum  TipoReprogramacion {
    ACCION_CENTRAL("TipoReprogramacion.ACCION_CENTRAL"),
    ASIGNACION_NO_PROGRAMABLE("TipoReprogramacion.ASIGNACION_NO_PROGRAMABLE"),
    PROYECTO("TipoReprogramacion.PROYECTO") ;
    
    
    
    private String label;
    
    private TipoReprogramacion(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

   
    public String toString() {
        if (label!=null) {
            return label.substring(label.indexOf("[.]")+1);
        }
        return "";
    }

    public String getTexto() {
        String texto = "";
        if(label!=null){
            if(label.equals(TipoReprogramacion.ACCION_CENTRAL.getLabel())){
                texto = "ACCION_CENTRAL";
            } else if(label.equals(TipoReprogramacion.ASIGNACION_NO_PROGRAMABLE.getLabel())){
                texto = "ASIGNACION_NO_PROGRAMABLE";
            } else if(label.equals(TipoReprogramacion.PROYECTO.getLabel())){
                texto = "PROYECTO";
            } 
        }
        return texto;
    }
}
