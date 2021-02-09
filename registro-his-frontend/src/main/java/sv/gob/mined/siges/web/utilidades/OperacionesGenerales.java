/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class OperacionesGenerales {
    
    public static Boolean existeEnArray(String[] array, String value) {
        for(int i=0; i < array.length; i++) {
            if(array[i] != null && StringUtils.isNotEmpty(array[i].trim())) {
                if(array[i].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
