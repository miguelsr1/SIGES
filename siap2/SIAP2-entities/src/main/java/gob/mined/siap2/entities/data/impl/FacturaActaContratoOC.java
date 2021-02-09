/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.enums.TipoDestinoFactura;
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
@DiscriminatorValue(value = TipoDestinoFactura.Values.ACTA_CONTRATO_OC)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class FacturaActaContratoOC extends Factura implements Serializable {
  
    @ManyToOne
    @JoinColumn(name = "fac_acta")
    private ActaContrato actaContrato;
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public ActaContrato getActaContrato() {
        return actaContrato;
    }

    public void setActaContrato(ActaContrato actaContrato) {
        this.actaContrato = actaContrato;
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
        final FacturaActaContratoOC other = (FacturaActaContratoOC) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
