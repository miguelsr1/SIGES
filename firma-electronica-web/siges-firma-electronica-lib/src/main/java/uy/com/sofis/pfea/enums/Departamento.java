package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum Departamento {
    ARTIGAS("Artigas", 2),
    CANELONES("Canelones", 3),
    CERROLARGO("Cerro Largo", 4),
    COLONIA("Colonia", 5),
    DURAZNO("Durazno", 6),
    FLORES("Flores", 7),
    FLORIDA("Florida", 8),
    LAVALLEJA("Lavalleja", 9),
    MALDONADO("Maldonado", 10),
    MONTEVIDEO("Montevideo", 1),
    PAYSANDU("Paysandú", 11),
    RIONEGRO("Río Negro", 12),
    RIVERA("Rivera", 13),
    ROCHA("Rocha", 14),
    SALTO("Salto", 15),
    SANJOSE("San José", 16),
    SORIANO("Soriano", 17),
    TACUAREMBO("Tacuarembó", 18),
    TREINTAYTRES("Treinta y Tres", 19),
    OTRO("Otro/Sin especificar", 0);

    private final String nombre;
    private final int codigoSIIAS;
    
    Departamento(String nombre, int codigoSIIAS) {
        this.nombre = nombre;
        this.codigoSIIAS = codigoSIIAS;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCodigoSIIAS() {
        return codigoSIIAS;
    }
    
    public static Departamento getByCodigoSIIAS(int codigoSiias) {
        for(Departamento depto : Departamento.values()) {
            if(depto.codigoSIIAS == codigoSiias) {
                return depto;
            }
        }
        return OTRO;
    }
}
