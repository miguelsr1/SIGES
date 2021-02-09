/*
 *  Sistema de Gestión
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.graficos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Escala implements Serializable {
    
    private List<Tick> ticks;
    private String label; 

    public Escala() {
        this.ticks = new ArrayList<>();
    }

    public List<Tick> getTicks() {
        return ticks;
    }

    public void setTicks(List<Tick> ticks) {
        this.ticks = ticks;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    

   

    

    


}
