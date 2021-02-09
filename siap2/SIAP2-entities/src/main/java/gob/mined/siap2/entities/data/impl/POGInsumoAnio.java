/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_po_insumo_anio")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POGInsumoAnio  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pog_ins_a_gen")
    @SequenceGenerator(name = "seq_pog_ins_a_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pog_ins_a", allocationSize = 1)
    @Column(name = "poi_id")
    private Integer id;
    
    @Column(name = "poi_anio")
    private Integer anio;
    
    @Column(name = "poi_cantidad")
    private Integer cantidadInsumo;

    
    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "posicion")
    private List<POGMontoFuenteInsumo> montosFuentes;
    
    
    
    
    @ManyToOne
    @JoinColumn(name = "poi_insumo")
    private POGInsumo insumo;
    

    //Auditoria
    @Column(name = "poi_ult_usuario")
    @AtributoUltimoUsuario
    private String actUsuario;

    @Column(name = "poi_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date actMod;

    @Column(name = "poi_version")
    @Version
    private Integer version;

    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
   
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getCantidadInsumo() {
        return cantidadInsumo;
    }

    public POGInsumo getInsumo() {
        return insumo;
    }

    public void setInsumo(POGInsumo insumo) {
        this.insumo = insumo;
    }

    public void setCantidadInsumo(Integer cantidadInsumo) {
        this.cantidadInsumo = cantidadInsumo;
    }

    public String getActUsuario() {
        return actUsuario;
    }

    public void setActUsuario(String actUsuario) {
        this.actUsuario = actUsuario;
    }

    public List<POGMontoFuenteInsumo> getMontosFuentes() {
        return montosFuentes;
    }

    public void setMontosFuentes(List<POGMontoFuenteInsumo> montosFuentes) {
        this.montosFuentes = montosFuentes;
    }
    

    public Date getActMod() {
        return actMod;
    }

    public void setActMod(Date actMod) {
        this.actMod = actMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    
    

    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final POGInsumoAnio other = (POGInsumoAnio) obj;
        if (this.id!=null && other.id!=null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == other);
    }
    
    
}
