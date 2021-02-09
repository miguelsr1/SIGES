package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum TipoDocumento {
    
    ci("Cédula de Identidad", 1),
    dni("Documento Nacional de Identidad", 0),
    ric("Registro de Identidad Civil", 0),
    cie("Cédula de Identidad de Extranjero", 0),
    cin("Cédula de Identidad para Nacionales", 0),
    cc("Cédula de Ciudadanía", 0),
    ti("Tarjeta de Identidad", 0),
    ce("Carné de Extranjería", 0),
    psp("Pasaporte", 2),
    otro("Otro", 0);

    private final String nombre;
    private final int valorSiias;

    TipoDocumento(String nombre, int valorSiias) {
        this.nombre = nombre;
        this.valorSiias = valorSiias;
    }

    public String getNombre() {
        return nombre;
    }

    public int getValorSiias() {
        return valorSiias;
    }

}
