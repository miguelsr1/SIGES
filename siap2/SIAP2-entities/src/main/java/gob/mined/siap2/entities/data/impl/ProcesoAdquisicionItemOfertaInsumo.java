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
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pro_adq_item_ofer_ins")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProcesoAdquisicionItemOfertaInsumo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pro_adq_item_ofer_ins")
    @SequenceGenerator(name = "seq_pro_adq_item_ofer_ins", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pro_adq_item_ofer_ins", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pro_oferta_item_prov")
    private ProcesoAdquisicionItemProvOferta ofertaItemProveedor;

    @ManyToOne
    @JoinColumn(name = "pro_adq_insumo")
    private ProcesoAdquisicionInsumo procesoAdqInsumo;

    @Column(name = "pro_monto_oferta",columnDefinition = "Decimal(20,2)")
    private BigDecimal montoOferta;
    
    
    @Column(name = "pro_cantidad_oferta")
    private Integer cantidadOferta;
    
     //Auditoria
    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pro_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pro_version")
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidadOferta() {
        return cantidadOferta;
    }

    public void setCantidadOferta(Integer cantidadOferta) {
        this.cantidadOferta = cantidadOferta;
    }

    public ProcesoAdquisicionItemProvOferta getOfertaItemProveedor() {
        return ofertaItemProveedor;
    }

    public void setOfertaItemProveedor(ProcesoAdquisicionItemProvOferta ofertaItemProveedor) {
        this.ofertaItemProveedor = ofertaItemProveedor;
    }

    public ProcesoAdquisicionInsumo getProcesoAdqInsumo() {
        return procesoAdqInsumo;
    }

    public void setProcesoAdqInsumo(ProcesoAdquisicionInsumo procesoAdqInsumo) {
        this.procesoAdqInsumo = procesoAdqInsumo;
    }

    public BigDecimal getMontoOferta() {
        return montoOferta;
    }

    public void setMontoOferta(BigDecimal montoOferta) {
        this.montoOferta = montoOferta;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    
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

        final ProcesoAdquisicionItemOfertaInsumo other = (ProcesoAdquisicionItemOfertaInsumo) obj;
        if (this.id != null) {

            return Objects.equals(this.id, other.id);
        }

        return this == obj;
    }

}
