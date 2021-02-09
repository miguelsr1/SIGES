/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.enums.TipoOrigenDistribuccionMonto;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
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
@DiscriminatorValue(value = TipoOrigenDistribuccionMonto.Values.PROYECTO)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public  class ProyectoAporte extends OrigenDistribuccionMontoInsumo  implements Serializable {

    @ManyToOne
    @JoinColumn(name = "fue_proyecto")
    private Proyecto proyecto;

    @Column(name = "fue_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal monto;

    @Column(name = "fue_monto_paripassu", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoParipassu;
    @Column(name = "fue_formula_paripassu")
    private String formulaParipassu;
    
    @Column(name = "fue_temp_monto_paripassu", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoParipassuEnConstruccion;
    @Column(name = "fue_temp_formula_paripassu")
    private String formulaParipassuEnConstruccion;

    @Column(name = "fue_posicion")
    protected Integer posicion;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMontoParipassu() {
        return montoParipassu;
    }

    public void setMontoParipassu(BigDecimal montoParipassu) {
        this.montoParipassu = montoParipassu;
    }

    public String getFormulaParipassu() {
        return formulaParipassu;
    }

    public void setFormulaParipassu(String formulaParipassu) {
        this.formulaParipassu = formulaParipassu;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public BigDecimal getMontoParipassuEnConstruccion() {
        return montoParipassuEnConstruccion;
    }

    public void setMontoParipassuEnConstruccion(BigDecimal montoParipassuEnConstruccion) {
        this.montoParipassuEnConstruccion = montoParipassuEnConstruccion;
    }

    public String getFormulaParipassuEnConstruccion() {
        return formulaParipassuEnConstruccion;
    }

    public void setFormulaParipassuEnConstruccion(String formulaParipassuEnConstruccion) {
        this.formulaParipassuEnConstruccion = formulaParipassuEnConstruccion;
    }
    
    public String getUltUsuario() {
        return ultUsuario;
    }
   
    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final ProyectoAporte other = (ProyectoAporte) obj;
        if ((this.id != null) && (other.id != null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }
    

}
