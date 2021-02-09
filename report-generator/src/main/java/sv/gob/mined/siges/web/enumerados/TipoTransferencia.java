/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package sv.gob.mined.siges.web.enumerados;

/**
 *
 * @author Sofis Solutions
 */
public enum TipoTransferencia {
    PRESUPUESTO_ESCOLAR("Presupuesto escolar"),
    OTRAS_TRANSFERENCIAS("Otras transferencias a centros educativos");

    private String label;

    private TipoTransferencia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
