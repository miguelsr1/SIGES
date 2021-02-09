
package uy.com.sofis.pfea.utilidades;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author Sofis Solutions
 */
public class UtilidadesJSF {

  private static void mostrarMensaje(FacesMessage.Severity severidad, String cliente, String titulo, String resumen) {
    FacesMessage mensaje = new FacesMessage(severidad, titulo, resumen);
    FacesContext.getCurrentInstance().addMessage(cliente, mensaje);
  }
  
  public static void mostrarMensajeInfo(String cliente, String titulo, String resumen) {
    mostrarMensaje(FacesMessage.SEVERITY_INFO, cliente, titulo, resumen);
  }

  public static void mostrarMensajeWarn(String cliente, String titulo, String resumen) {
    mostrarMensaje(FacesMessage.SEVERITY_WARN, cliente, titulo, resumen);
  }

  public static void mostrarMensajeError(String cliente, String titulo, String resumen) {
    mostrarMensaje(FacesMessage.SEVERITY_ERROR, cliente, titulo, resumen);
  }

}
