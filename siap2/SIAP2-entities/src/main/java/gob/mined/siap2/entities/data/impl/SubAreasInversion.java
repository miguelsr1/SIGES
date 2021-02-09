package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class SubAreasInversion extends AreasInversion implements Serializable{
    
    
    
    
    
    @ManyToOne
    @JoinColumn(name = "ai_area_padre") 
    private SubAreasInversion areaPadre;
    
    @OneToMany(mappedBy = "areaPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubAreasInversion> areasInversiones;

    
    
    
//    public List<SubAreasInversion> getAreasInversiones() {
//        return areasInversiones;
//    }
//
//    public void setAreasInversiones(List<SubAreasInversion> areasInversiones) {
//        this.areasInversiones = areasInversiones;
//    }
//
//    public SubAreasInversion getAreaPadre() {
//        return areaPadre;
//    }
//
//    public void setAreaPadre(SubAreasInversion areaPadre) {
//        this.areaPadre = areaPadre;
//    }
 
    
    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubAreasInversion other = (SubAreasInversion) obj;
        if (this.getId()!= null && other.getId()!=null) {
            return this.getId().equals(other.getId());
        }
        return this == other;
    }
    
}
