/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.utils;

import java.io.Serializable;

public class BooleanUtils implements Serializable{
    
    
    public static Boolean sonIguales(Boolean val1, Boolean val2) {
        Boolean respuesta = false;
        if (val1!=null) {
            if (val2!=null) {
                respuesta=val1.equals(val2);
            } else{
               respuesta=false;
            }
        } else {
            respuesta=val2==null;
        }
        return respuesta;
    }
}
