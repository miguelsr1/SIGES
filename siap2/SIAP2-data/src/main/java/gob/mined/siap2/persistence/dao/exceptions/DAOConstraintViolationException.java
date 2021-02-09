/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.exceptions;

/**
 * Esta clase corresponde a una excepción de DAO para indicar la violación de una restricción de integridad a nivel del modelo.
 * @author Sofis
 */
public class DAOConstraintViolationException extends DAOGeneralException{

    public DAOConstraintViolationException(){

    }

    public DAOConstraintViolationException(String msg){
        super(msg);
    }

    public DAOConstraintViolationException(Exception msg){
        super(msg);
    }

    private String foreignKey = null;

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }
}
