/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.exceptions;

import javax.ejb.ApplicationException;

/**
 * Clase desarrollada por Sofis Solutions para el sistema SIAP
 * Esta excepción corresponde a la edición de un objeto que no es editable.
 * @author Sofis Solutions
 */
@ApplicationException(rollback = true)
public class DAOObjetoNoEditableException  extends RuntimeException{

    /**
     * Constructor por defecto
     */
    public DAOObjetoNoEditableException(){

    }

    /**
     * Constructor a partir de un mensaje.
     * @param msg 
     */
     public DAOObjetoNoEditableException(String msg){
        super(msg);
    }

     /**
      * Constructor a partir de una excepción.
      * @param msg 
      */
     public DAOObjetoNoEditableException(Exception msg){
        super(msg);
    }

}
