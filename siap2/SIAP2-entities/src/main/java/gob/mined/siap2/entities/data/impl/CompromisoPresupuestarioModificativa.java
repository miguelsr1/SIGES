/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.enums.TipoCompromisoPresupuestario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@DiscriminatorValue(value = TipoCompromisoPresupuestario.Values.MODIFICATIVA)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class CompromisoPresupuestarioModificativa extends CompromisoPresupuestario implements Serializable {


    

    @OneToOne
    @JoinColumn(name = "com_modificativa")
    private ModificativaContrato modificativaContrato;    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public ModificativaContrato getModificativaContrato() {
        return modificativaContrato;
    }

    public void setModificativaContrato(ModificativaContrato modificativaContrato) {
        this.modificativaContrato = modificativaContrato;
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
        final CompromisoPresupuestarioModificativa other = (CompromisoPresupuestarioModificativa) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
