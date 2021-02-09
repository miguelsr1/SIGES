/*
 * SIGES
 * Desarrollado por: Sofis Solutions
 *
 */
package sv.gob.mined.siges.enumerados;

/**
 * Tipo de transferencia
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
