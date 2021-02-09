/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

/**
 *
 * @author Sofis Solutions
 */
public class DataFCMGrupo {
        
    public final static String ROOT = "ROOT";
    public final static String GRUPO = "GRUPO";
    public final static String INSUMO = "INSUMO";
    
    
    private String tipo;
    private Object objeto;

    private String nombre;
      
    public DataFCMGrupo(String tipo, Object objeto) {
        this.tipo = tipo;
        this.objeto = objeto;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
