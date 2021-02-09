/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;




import gob.mined.siap2.entities.enums.TipoMetaIndicador;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Objects;
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
@DiscriminatorValue(value = TipoMetaIndicador.Values.META_INDICADOR)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class MetaIndicadorProyecto extends MetaIndicador implements Serializable {


    @ManyToOne
    @JoinColumn(name = "met_indicador")
    private Indicador indicador;
    
    @ManyToOne
    @JoinColumn(name = "met_proy")
    private Proyecto proyecto;
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
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
        final MetaIndicadorProyecto other = (MetaIndicadorProyecto) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
