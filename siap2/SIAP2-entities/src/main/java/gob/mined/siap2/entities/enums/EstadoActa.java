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
public enum EstadoActa {
    EN_DIGITACION("EstadoActa.EN_DIGITACION", "DIG"),
    APROBADA("EstadoActa.APROBADA", "APR"),
    REVERTIDA("EstadoActa.REVERTIDA", "REV");

    private String label;
    private String abrev;

    private EstadoActa(String label, String abrev) {
        this.label = label;
        this.abrev = abrev;
    }

    public String getLabel() {
        return label;
    }
    
    public String getAbrev() {
        return abrev;
    }

    public static String traducirEstado(EstadoActa estado) {
        String texto = "";
        if (estado == null){
            return texto;
        }
        if (estado.equals(EN_DIGITACION)) {
            texto = "En Digitación";
        } else if (estado.equals(APROBADA)) {
            texto = "Aprobada";
        } else if (estado.equals(REVERTIDA)) {
            texto = "Revertida";
        }
        return texto;
    }

}
