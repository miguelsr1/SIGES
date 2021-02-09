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
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue("ASIGNACION_NO_PROGRAMABLE")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POActividadAsignacionNoProgramable extends POActividadBase implements Serializable, ManejoLineaBaseTrabajo<POActividadBase> {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "poa_actividad_np")
    private ActividadAsignacionNP actividadNP;
    
    @Transient
    private String nombre;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public ActividadAsignacionNP getActividadNP() {
        return actividadNP;
    }

    public void setActividadNP(ActividadAsignacionNP actividadNP) {
        this.actividadNP = actividadNP;
    }

    
    
    // </editor-fold>

    public String getNombre() {
        nombre=actividadNP.getNombre();
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
