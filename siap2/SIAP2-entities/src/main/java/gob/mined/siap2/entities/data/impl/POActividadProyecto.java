/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import com.mined.siap2.interfaces.ManejoLineaBaseTrabajo;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue("PROYECTO")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POActividadProyecto extends POActividadBase implements Serializable, ManejoLineaBaseTrabajo<POActividadBase> {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "poa_actividad_proy")
    private ActividadPOProyecto actividadCodiguera;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
   
    
    
    public ActividadPOProyecto getActividadCodiguera() {
        return actividadCodiguera;
    }

    public void setActividadCodiguera(ActividadPOProyecto actividadCodiguera) {
        this.actividadCodiguera = actividadCodiguera;
    }

    public String getNombre() {
        return actividadCodiguera.getNombre();
    }
    
    
    

    // </editor-fold>

}
