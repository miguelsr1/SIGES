/*
 * SIGES
* Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Estado de las solicitdues de transferencia.
 *
 * @author
 */
public enum EnumEstadoSolicitudesTransferencia {
    EN_PROCESO("En proceso"),
    ENVIADA_A_UFI("Enviada a UFI"),
    RECIBIDA_PRESUPUESTO("Recibida por Presupuesto"),
    TRAMITADO_ANTE_HACIENDA("Tramitado ante Hacienda"),
    TRANSFERIDO("Transferido");

    private final String text;

    private EnumEstadoSolicitudesTransferencia(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
