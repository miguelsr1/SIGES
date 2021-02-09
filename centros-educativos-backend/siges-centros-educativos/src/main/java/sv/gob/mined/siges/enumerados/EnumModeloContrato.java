/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumModeloContrato {
    ACUERDO("Acuerdo de Nombramiento"),
    CONTRATO("Contrato"),
    OTRO("Otro");
    
    private final String text;

    private EnumModeloContrato(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
}

