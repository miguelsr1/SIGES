
package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum DocPaisEmisor {
  uy("Uruguay"),
  ar("Argentina"),
  br("Brasil"),
  py("Paraguay"),
  bo("Bolivia"),
  cl("Chile"),
  co("Colombia"),
  ec("Ecuador"),
  pe("Perú"),
  ve("Venezuela"),
  otro("Otro/Sin especificar");
  
  private final String nombre;
  
  DocPaisEmisor(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
}
