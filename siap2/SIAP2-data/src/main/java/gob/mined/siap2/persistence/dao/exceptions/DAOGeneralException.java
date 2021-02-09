/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.exceptions;

/**
 * Clase desarrollada por Sofis Solutions para el sistema SIAP
 * Corresponde a una excepción genérica de acceso a datos.
 * @author Sofis Solutions
 */
public class DAOGeneralException  extends Exception{

    /**
     * Constructor por defecto.
     */
    public DAOGeneralException(){

    }

    /**
     * Constructor a partir de un mensaje
     * @param msg 
     */
     public DAOGeneralException(String msg){
        super(msg);
    }

     /**
      * Constructor a partir de una excepción.
      * @param msg 
      */
     public DAOGeneralException(Exception msg){
        super(msg);
    }

}
