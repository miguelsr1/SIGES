/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

/**
 * Clase auxiliar para guardar los egresos de un presupuesto desde un archivo
 * @author sofis-iquezada
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EgresoArchivoPost implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long presupuestoPk;
    private List<EgresosArchivo> egresos;

    public Long getPresupuestoPk() {
        return presupuestoPk;
    }

    public void setPresupuestoPk(Long presupuestoPk) {
        this.presupuestoPk = presupuestoPk;
    }

    public List<EgresosArchivo> getEgresos() {
        return egresos;
    }

    public void setEgresos(List<EgresosArchivo> egresos) {
        this.egresos = egresos;
    }
    
    
    
}
