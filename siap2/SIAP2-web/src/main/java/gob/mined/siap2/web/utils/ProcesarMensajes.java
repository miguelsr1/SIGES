/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils;

import gob.mined.siap2.web.mb.ErroresMB;
import gob.mined.siap2.web.presentacion.tipos.MensajePresentacion;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;

/**
 *
 * @author Sofis Solutions
 */
public class ProcesarMensajes {
    
     public static List<FacesMessage> obtenerMensajes(List<String> msgs) {
        List<FacesMessage> respuesta = new ArrayList();
        for(String s: msgs) {
            respuesta.add(new FacesMessage(FacesMessage.SEVERITY_ERROR,ErroresMB.obtenerDescripcion(s),ErroresMB.obtenerDescripcion(s)));
        }
        return respuesta;
    }
     
     public  static FacesMessage generarError(String mensaje) {
         return new FacesMessage(FacesMessage.SEVERITY_ERROR,mensaje,mensaje);
     }
     
     public  static MensajePresentacion generarError(String para, String mensaje) {
         return new MensajePresentacion(para,mensaje);
     }
}
