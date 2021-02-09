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
public enum EstadoCertificadoDispPresupuestaria {
    ENVIADO("EstadoCertificadoDispPresupuestaria.ENVIADO"),
    APROBADO("EstadoCertificadoDispPresupuestaria.APROBADO"),
    RECHAZADO("EstadoCertificadoDispPresupuestaria.RECHAZADO");

    private String label;
    private String abrev;

    private EstadoCertificadoDispPresupuestaria(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static String traducirEstado(EstadoCertificadoDispPresupuestaria estado) {
        String texto = "";
        if (estado == null) {
            return texto;
        }
        if (estado.equals(ENVIADO)) {
            texto = "Enviado";
        } else if (estado.equals(APROBADO)) {
            texto = "Aprobado";
        } else if (estado.equals(RECHAZADO)) {
            texto = "Rechazado";
        }
        return texto;
    }

}
