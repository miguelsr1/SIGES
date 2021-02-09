/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumRiesgoAlerta {
    MUY_ALTO("Muy alto", 3),
    ALTO("Alto", 2),
    MEDIO("Medio", 1),
    SIN_RIESGO("No aplica", 0);
        
    private final String text;
    private final int valor;

    private EnumRiesgoAlerta(final String text, final int valor) {
        this.text = text;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }

    public int getValor() {
        return valor;
    }
    
    
}
