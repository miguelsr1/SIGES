/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import java.util.HashMap;

/**
 *
 * @author Sofis Solutions
 */
public class TemplatesMails {
    
    
    public static String instanciar(String plantilla, String correoElectronico, String rut, String razonSocial, String estado) {
        String resultado = plantilla;
        resultado = resultado.replaceAll("#CORREO_ELECTRONICO#",correoElectronico);
        resultado = resultado.replaceAll("#RUT#", rut);
        resultado = resultado.replaceAll("#RAZON_SOCIAL#", razonSocial);
        resultado = resultado.replaceAll("#ESTADO#", estado);
        return resultado;
    }
     public static String instanciarConObservaciones(String plantilla, String correoElectronico, String rut, String razonSocial, String observaciones, String estado) {
        String resultado = plantilla;
        resultado = resultado.replaceAll("#CORREO_ELECTRONICO#",correoElectronico);
        resultado = resultado.replaceAll("#RUT#", rut);
        resultado = resultado.replaceAll("#RAZON_SOCIAL#", razonSocial);
        resultado = resultado.replaceAll("#ESTADO#", estado);
        resultado = resultado.replaceAll("#OBSERVACIONES#", estado);
        return resultado;
    }
     
     public static String instanciarConHashMap(String plantilla, HashMap<String,String> valores) {
         String resultado =plantilla;
         if (plantilla!=null) {
             for(String s: valores.keySet()) {
                 resultado = resultado.replaceAll(s, valores.get(s));
             }
         }
         return resultado;
     }
}
