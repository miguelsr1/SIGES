package sv.gob.mined.siges.utils;

import java.util.List;

public class ObjectUtils  {
    
    /**
      * Determina si dos objetos son iguales, esto es:
      * si los dos son nulos o si tienen el mismo contenido
      * @param <T>
      * @param e1
      * @param e2
      * @return 
      */
    public static <T extends Object> boolean sonIguales(T e1, T e2) {
        if (e1 != null) {
            if (e2 != null) {
                return e1.equals(e2);
            } else {
                return false;
            }
        } else {
            return e2 == null;
        }
    }
    
    public static <T extends Object> boolean mismosElementosEnLista(List<T> e1, List<T> e2){
        if (e1 != null && !e1.isEmpty()){
            if (e2 != null && !e2.isEmpty()){
                if (e1.containsAll(e2) && e2.size() == e1.size()){
                    return true;
                } 
                return false;         
            } else {
                return false;
            }
        } else if ( e2 != null && !e2.isEmpty()){
            return false;
        } 
        return true;
    }
    
    
}
