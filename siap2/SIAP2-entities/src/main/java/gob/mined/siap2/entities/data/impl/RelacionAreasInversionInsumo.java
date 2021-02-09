package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_areas_inversion_insumo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class RelacionAreasInversionInsumo implements Serializable{
    
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_rel_areas_inver_insumo", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_areas_inver_insumo", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_areas_inver_insumo")
    @Column(name = "id")
    private Integer id;
    
    
    @ManyToOne
    @JoinColumn(name = "id_area_inversion") 
    private AreasInversion areaInversion;
    
    @ManyToOne
    @JoinColumn(name = "id_insumo") 
    private Insumo insumo;

    
    private transient Boolean existente = true;
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AreasInversion getAreaInversion() {
        return areaInversion;
    }

    public void setAreaInversion(AreasInversion areaInversion) {
        this.areaInversion = areaInversion;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public Boolean getExistente() {
        return existente;
    }

    public void setExistente(Boolean existente) {
        this.existente = existente;
    }
    
    
    
}
