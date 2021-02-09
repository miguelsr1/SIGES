/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */

@Entity
@DiscriminatorValue(value = TipoMontoPorAnio.Values.ASIGN_NO_PROGRAMABLE)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class AsignacionNoProgramable extends ConMontoPorAnio implements Serializable {
      

    @ManyToMany
    @JoinTable(
      schema = Constantes.SCHEMA_SIAP2,
      name="ss_rel_asig_activnp",
      joinColumns={@JoinColumn(name="rel_asi_id", referencedColumnName="con_id")},
      inverseJoinColumns={@JoinColumn(name="rel_act_id", referencedColumnName="act_id")})
    private List<ActividadAsignacionNP> actividadesNP;

    
    
    
    @OneToMany(mappedBy = "asignacionNoProgramable", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "indice")
    private List<TechoAsignacionNoProgramable> techosPlanificacion;
  
    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public List<ActividadAsignacionNP> getActividadesNP() {
        return actividadesNP;
    }

    public void setActividadesNP(List<ActividadAsignacionNP> actividadesNP) {
        this.actividadesNP = actividadesNP;
    }

    public List<TechoAsignacionNoProgramable> getTechosPlanificacion() {
        return techosPlanificacion;
    }

    public void setTechosPlanificacion(List<TechoAsignacionNoProgramable> techosPlanificacion) {
        this.techosPlanificacion = techosPlanificacion;
    }

    
    
    

    // </editor-fold>


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AsignacionNoProgramable other = (AsignacionNoProgramable) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
