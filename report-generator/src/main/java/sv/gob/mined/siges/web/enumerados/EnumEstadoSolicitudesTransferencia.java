/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.enumerados;

/**
 *
 * @author sofis-iquezada
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
