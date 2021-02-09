/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.UniqueConstraint;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_techo_pla_accc",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "mon_anio_fiscal", "mon_unidad_tecnica", "pla_planificacion"})
        }
)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class TechoPlanificacionAccionCentral extends TechoAccionCentral implements Serializable{
    
    @ManyToOne
    @JoinColumn(name = "pla_planificacion")
    private PlanificacionEstrategica planificacion;

    public PlanificacionEstrategica getPlanificacion() {
        return planificacion;
    }

    public void setPlanificacion(PlanificacionEstrategica planificacion) {
        this.planificacion = planificacion;
    }

 
    
    
    
    
    
    
}
