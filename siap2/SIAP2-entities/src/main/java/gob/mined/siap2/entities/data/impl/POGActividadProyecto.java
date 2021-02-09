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
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue("POG_PROYECTO")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POGActividadProyecto extends POActividadProyecto implements Serializable, ManejoLineaBaseTrabajo<POActividadBase> {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "poa_anio_inico")
    private Integer anioInicio;
    
    @Column(name = "poa_anio_fin")
    private Integer anioFin;
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
   
    public Integer getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Integer anioInicio) {
        this.anioInicio = anioInicio;
    }

    public Integer getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Integer anioFin) {
        this.anioFin = anioFin;
    }
    
    // </editor-fold>


}
