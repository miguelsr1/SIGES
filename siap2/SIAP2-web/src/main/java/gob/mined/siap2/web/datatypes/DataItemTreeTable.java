/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
//eliminar
public class DataItemTreeTable implements Serializable{
    private String tipo;
    private Object objeto;
    
    //tipo para arbol
     public static final String ROOT="ROOT";
    public static final String OBJETO="OBJETO";
    public static final String PRODUCTO="PRODUCTO";

    public DataItemTreeTable(String tipo, Object objeto) {
        this.tipo = tipo;
        this.objeto = objeto;
    }
    
    public DataItemTreeTable() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }
    
}
