/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils.generalutils;

import java.util.List;

/**
 * Esta clase incluye métodos para procesamiento de listas.
 * @author Sofis Solutions
 */
public class ListUtils {
    
    /**
     * Este método elimina un objeto de una lista.
     * @param lista
     * @param objeto 
     */
    public static void deleteElement(List lista, Object objeto){
        int index=0;
        boolean encontro = false;
        while (index < lista.size() && !encontro){
            if (lista.get(index) == objeto){
                encontro = true;
            }else{
                index= index+1;
            }
        }
        
        if (encontro){
            lista.remove(index);
        }
    }
    

    
    
}
