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
 * @author Gustavo
 */
public enum EstadoReprogramacion {

    FORMULACION("EstadoReprogramacion.FORMULACION"),
    PENDIENTE_PAC("EstadoReprogramacion.PENDIENTE_PAC"),
    PENDIENTE_PRESUPUESTO("EstadoReprogramacion.PENDIENTE_PRESUPUESTO"),
    APROBADO("EstadoReprogramacion.APROBADO"),
    RECHAZADO("EstadoReprogramacion.RECHAZADO"),
    APLICADA("EstadoReprogramacion.APLICADA");

    private String label;
    private String labelCorto;

    private EstadoReprogramacion(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getLabelCorto() {
        labelCorto = toString();
        switch (label) {
            case "EstadoReprogramacion.FORMULACION":
                labelCorto = "FORMULACION";
                break;
            case "EstadoReprogramacion.PENDIENTE_PAC":
                labelCorto = "PENDIENTE_PAC";
                break;
            case "EstadoReprogramacion.PENDIENTE_PRESUPUESTO":
                labelCorto = "PENDIENTE_PRESUPUESTO";
                break;
            case "EstadoReprogramacion.APROBADO":
                labelCorto = "APROBADO";
                break;
            case "EstadoReprogramacion.RECHAZADO":
                labelCorto = "RECHAZADO";
                break;
        }
        labelCorto = toString();
        return labelCorto;
    }

    public String toString() {
        if (label != null) {
            return label.substring(label.indexOf("[.]") + 1);
        }
        return "";
    }
    
    public String getTexto() {
        String texto = "";
        if(label!=null){
            if(label.equals(EstadoReprogramacion.APLICADA.getLabel())){
                texto = "APLICADA";
            } else if(label.equals(EstadoReprogramacion.APROBADO.getLabel())){
                texto = "APROBADO";
            } else if(label.equals(EstadoReprogramacion.FORMULACION.getLabel())){
                texto = "FORMULACION";
            } else if(label.equals(EstadoReprogramacion.PENDIENTE_PAC.getLabel())){
                texto = "PENDIENTE_PAC";
            } else if(label.equals(EstadoReprogramacion.PENDIENTE_PRESUPUESTO.getLabel())){
                texto = "PENDIENTE_PRESUPUESTO";
            } else if(label.equals(EstadoReprogramacion.RECHAZADO.getLabel())){
                texto = "RECHAZADO";
            } 
        }
        return texto;
    }
}
