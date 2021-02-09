/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sofis.persistence.dao.exceptions;

/**
 *
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
