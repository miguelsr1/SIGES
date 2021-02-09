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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author eduardo
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_acta_item")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)

public class RelActaItem implements Serializable {
   
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_acta_item")
    @SequenceGenerator(name = "seq_rel_acta_item", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_acta_item", allocationSize = 1)
    @Column(name = "rel_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="rel_item")
    private ProcesoAdquisicionItem item;

    @ManyToOne
    @JoinColumn(name="rel_acta")
    private ActaContrato acta;
    
    @Lob
    @Column(name = "rel_descripcion")
    private String descripcion;
    
    @Column(name = "rel_importe", columnDefinition = "Decimal(20,2)")
    private BigDecimal importe;

    @Column(name = "rel_cantidad")
    private Integer cantRecibida;
    
    //Auditoria
    @Column(name = "rel_ult_usuario")
    @AtributoUltimoUsuario
    private String actUsuario;

    @Column(name = "rel_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date actMod;

    @Column(name = "rel_version")
    @Version
    private Integer version;
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ProcesoAdquisicionItem getItem() {
        return item;
    }

    public void setItem(ProcesoAdquisicionItem item) {
        this.item = item;
    }

    public ActaContrato getActa() {
        return acta;
    }

    public void setActa(ActaContrato acta) {
        this.acta = acta;
    }

    public String getActUsuario() {
        return actUsuario;
    }

    public void setActUsuario(String actUsuario) {
        this.actUsuario = actUsuario;
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
    
    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Integer getCantRecibida() {
        return cantRecibida;
    }

    public void setCantRecibida(Integer cantRecibida) {
        this.cantRecibida = cantRecibida;
    }
    
    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.getId());
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

        final RelActaItem other = (RelActaItem) obj;
        if (this.getId() != null) {
            return Objects.equals(this.getId(), other.getId());
        }
        return (this == obj);
    }

}
