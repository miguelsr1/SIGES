/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
 package gob.mined.siap2.business.datatype;

import java.io.Serializable;

/**
 * Tipo de dato correspondiente a la plantilla de insumo
 * @author Sofis Solutions
 */
public class DataInsumoPlantilla implements Serializable {
    //para la plantilla utiliza el tipo de insumo root, categoria, insumo y dummy
    // para onu se utiliza todo menos categoria.

    public static final String TIPO_ROOT = "TIPO_ROOT";

    public static final String TIPO_SEGMENTO = "TIPO_SEGMENTO";
    public static final String TIPO_FAMILIA = "TIPO_FAMILIA";
    public static final String TIPO_CLASE = "TIPO_CLASE";
    public static final String TIPO_ARTICULO = "TIPO_ARTICULO";
    public static final String TIPO_INSUMO = "TIPO_INSUMO";
    public static final String TIPO_DUMMY = "TIPO_DUMMY";

    public static final String TIPO_CATEGORIA = "TIPO_CATEGORIA";

    private String nombre;
    private boolean usado;
    private String type;
    private Object objeto;

    
    
    public DataInsumoPlantilla() {
    }

    public DataInsumoPlantilla(String type, String nombre, boolean usado, Object objeto) {
        this.nombre = nombre;
        this.usado = usado;
        this.type = type;
        this.objeto = objeto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
