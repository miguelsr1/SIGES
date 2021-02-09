
package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum EstadoOferta {
  DISPONIBLE("Disponible"),
  UTILIZADA("Utilizada"),
  NOAPLICA("No aplica"),
  NOVIGENTE("No vigente");
  
  private final String nombre;
  
  EstadoOferta(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
  
}
