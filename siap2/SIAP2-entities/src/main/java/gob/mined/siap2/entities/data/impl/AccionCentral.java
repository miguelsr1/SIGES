/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue(value = TipoMontoPorAnio.Values.ACCION_CENTRAL)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class AccionCentral extends ConMontoPorAnio implements Serializable {

    @OneToMany(mappedBy = "accionCentral", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "indice")
    private List<TechoActividadAccionCentral> montosUT;
  
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public List<TechoActividadAccionCentral> getMontosUT() {
        return montosUT;
    }

    public void setMontosUT(List<TechoActividadAccionCentral> montosUT) {
        this.montosUT = montosUT;
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
        final AccionCentral other = (AccionCentral) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
