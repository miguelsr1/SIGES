
package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum ClasificacionEspectaculo {
  ATP("Apta para todo público"),
  M09("Apta para mayores de 9 años"),
  M12("Apta para mayores de 12 años"),
  M15("Apta para mayores de 15 años"),
  M18("Apta para mayores de 18 años"),
  R18("Restringida"),
  SC("Sin clasificar");
  
  private final String nombre;
  
  ClasificacionEspectaculo(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
}
