package uy.com.sofis.pfea.enums;

/**
 * @author Sofis Solutions
 */
public enum Permisos {
  INICIO("Inicio"),
  CARGAR_ARCHIVOS("Cargar archivos"),
  BANDEJA_ARCHIVOS("Bandeja de archivos"),
  CARGAR_ARCHIVO_PUBLICO("Carga Publica"),
  FIRMAR_ARCHIVO_PRIVADO("Firma Privada");

  private final String nombre;

  Permisos(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
}
