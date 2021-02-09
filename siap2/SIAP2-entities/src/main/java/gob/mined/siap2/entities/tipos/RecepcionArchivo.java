/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.tipos;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class RecepcionArchivo implements Serializable {
    private Integer achId;
    private Boolean achRecibida;
    private String achNotas;

    public Integer getAchId() {
        return achId;
    }

    public void setAchId(Integer achId) {
        this.achId = achId;
    }

    public Boolean getAchRecibida() {
        return achRecibida;
    }

    public void setAchRecibida(Boolean achRecibida) {
        this.achRecibida = achRecibida;
    }

    public String getAchNotas() {
        return achNotas;
    }

    public void setAchNotas(String achNotas) {
        this.achNotas = achNotas;
    }
    
    
    
}
