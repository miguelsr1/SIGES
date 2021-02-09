/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.business.datatypes.DataVerValoresValor;
import java.util.List;

/**
 *
 * @author Sofis Solutions
 */
public class DataTreeValoresIndicadores {
    private String nombre;
    List<DataVerValoresValor> valores;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DataVerValoresValor> getValores() {
        return valores;
    }

    public void setValores(List<DataVerValoresValor> valores) {
        this.valores = valores;
    }
    
    
    
}
