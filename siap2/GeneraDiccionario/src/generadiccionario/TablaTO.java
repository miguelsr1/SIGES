/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generadiccionario;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sofis
 */
public class TablaTO {
    
    private String nombre;
    private String comentario;
    private String esquema;
    List<ColumnaTO> columnas = new ArrayList<ColumnaTO>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }

    public List<ColumnaTO> getColumnas() {
        return columnas;
    }

    public void setColumnas(List<ColumnaTO> columnas) {
        this.columnas = columnas;
    }
    
    
    
}
