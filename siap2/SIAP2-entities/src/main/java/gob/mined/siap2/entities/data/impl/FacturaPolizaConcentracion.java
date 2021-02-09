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
@DiscriminatorValue(value = TipoDestinoFactura.Values.POLIZA_CONCENTRACION)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class FacturaPolizaConcentracion extends Factura implements Serializable {
  
    @ManyToOne
    @JoinColumn(name = "fac_poliza")
    private PolizaDeConcentracion polizaConcentracion;
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public PolizaDeConcentracion getPolizaConcentracion() {
        return polizaConcentracion;
    }

    public void setPolizaConcentracion(PolizaDeConcentracion polizaConcentracion) {
        this.polizaConcentracion = polizaConcentracion;
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
        final FacturaPolizaConcentracion other = (FacturaPolizaConcentracion) obj;
        if ((this.id != null) && !Objects.equals(this.id, other.id)) {
            return false;
        }

        return (this == obj);
    }

}
