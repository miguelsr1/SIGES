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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_rel_padqitem_padqins")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)

public class RelacionProAdqItemInsumo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rel_pro_adq_item_ins")
    @SequenceGenerator(name = "seq_rel_pro_adq_item_ins", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_rel_pro_adq_item_ins", allocationSize = 1)
    @Column(name = "rel_id")
    private Integer id;

    @Column(name = "rel_cantidad")
    private Integer cantidad;

    @ManyToOne(optional = true)
    @JoinColumn(name = "rel_pro_ins_id", nullable = true)
    private ProcesoAdquisicionInsumo insumo;

    @ManyToOne(optional = true)
    @JoinColumn(name = "rel_pro_item_id", nullable = true)
    private ProcesoAdquisicionItem item;

    @Column(name = "rel_pro_fecha_cont")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaContratacion;

    @Column(name = "rel_pro_monto_unit_adj", columnDefinition = "Decimal(20,6)")
    private BigDecimal montoUnitAdjudicado;

    @Column(name = "rel_pro_cant_adj")
    private Integer cantidadAdjudicada;

    @Column(name = "rel_pro_monto_total_adj", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoTotalAdjudicado;
    
    @Column(name = "rel_pro_monto_total_sol", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoTotalSolicitado;
    
    //los pagos que se han hecho por el insumo del item
    @OneToMany(mappedBy = "relacionItemInsumo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoInsumo> pagosInsumo;
    
    @Column(name = "rel_pro_monto_rescindido", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoRescindido;
    
    @Column(name = "rel_duracion_ins_contrato")
    private Integer duracionInsumoEnContrato;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        if(cantidad == null){
            cantidad = 0;
        }
        return cantidad;
    }

    public ProcesoAdquisicionInsumo getInsumo() {
        return insumo;
    }

    public void setInsumo(ProcesoAdquisicionInsumo insumo) {
        this.insumo = insumo;
    }

    public ProcesoAdquisicionItem getItem() {
        return item;
    }

    public void setItem(ProcesoAdquisicionItem item) {
        this.item = item;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getMontoUnitAdjudicado() {
        if (montoUnitAdjudicado == null) {
            montoUnitAdjudicado = BigDecimal.ZERO;
        }
        return montoUnitAdjudicado;
    }

    public void setMontoUnitAdjudicado(BigDecimal montoUnitAdjudicado) {
        this.montoUnitAdjudicado = montoUnitAdjudicado;
    }

    public Date getUltMod() {
        return actMod;
    }

    public void setUltMod(Date ultMod) {
        this.actMod = ultMod;
    }

    public String getUltUsuario() {
        return actUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.actUsuario = ultUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCantidadAdjudicada() {
        if (cantidadAdjudicada == null) {
            cantidadAdjudicada = 0;
        }
        return cantidadAdjudicada;
    }

    public void setCantidadAdjudicada(Integer cantidadAdjudicada) {
        this.cantidadAdjudicada = cantidadAdjudicada;
    }

    public BigDecimal getMontoTotalAdjudicado() {
        if (montoTotalAdjudicado == null) {
            montoTotalAdjudicado = BigDecimal.ZERO;
        }
        return montoTotalAdjudicado;
    }

    public void setMontoTotalAdjudicado(BigDecimal montoTotalAdjudicado) {
        this.montoTotalAdjudicado = montoTotalAdjudicado;
    }
    
    public BigDecimal getMontoTotalSolicitado() {
        if (montoTotalSolicitado == null) {
            montoTotalSolicitado = BigDecimal.ZERO;
        }
        return montoTotalSolicitado;
    }

    public void setMontoTotalSolicitado(BigDecimal montoTotalSolicitado) {
        this.montoTotalSolicitado = montoTotalSolicitado;
    }

    public List<PagoInsumo> getPagosInsumo() {
        return pagosInsumo;
    }

    public void setPagosInsumo(List<PagoInsumo> pagosInsumo) {
        this.pagosInsumo = pagosInsumo;
    }
    
    public BigDecimal getMontoRescindido() {
        return montoRescindido;
    }

    public void setMontoRescindido(BigDecimal montoRescindido) {
        this.montoRescindido = montoRescindido;
    }

    public Integer getDuracionInsumoEnContrato() {
        return duracionInsumoEnContrato;
    }

    public void setDuracionInsumoEnContrato(Integer duracionInsumoEnContrato) {
        this.duracionInsumoEnContrato = duracionInsumoEnContrato;
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

        final RelacionProAdqItemInsumo other = (RelacionProAdqItemInsumo) obj;
        if (this.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return (this == obj);
    }

}
