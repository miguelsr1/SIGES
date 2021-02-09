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
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pro_adq_item_prov_ofer")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ProcesoAdquisicionItemProvOferta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pro_adq_item_prov_of")
    @SequenceGenerator(name = "seq_pro_adq_item_prov_of", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pro_adq_item_prov_of", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pro_adq_proveedor")
    private ProcesoAdquisicionProveedor procesoAdquisicionProveedor;

    @ManyToOne
    @JoinColumn(name = "pro_item")
    private ProcesoAdquisicionItem procesoAdquisicionItem;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ofertaItemProveedor")
    private List<ProcesoAdquisicionItemOfertaInsumo> ofertasPorInsumo;

    @Transient
    private boolean asociado;

    @Transient
    private boolean editable;

    @Transient
    private BigDecimal precioTotal;

    @Column(name = "pro_precio_unit_oferta", columnDefinition = "Decimal(20,6)")
    private BigDecimal precioUnitOferta;

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

    public ProcesoAdquisicionItem getProcesoAdquisicionItem() {
        return procesoAdquisicionItem;
    }

    public void setProcesoAdquisicionItem(ProcesoAdquisicionItem procesoAdquisicionItem) {
        this.procesoAdquisicionItem = procesoAdquisicionItem;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public BigDecimal calcularPrecioTotal() {
        precioTotal = BigDecimal.ZERO;
        if (ofertasPorInsumo != null) {
            for (ProcesoAdquisicionItemOfertaInsumo ofertaInsumo : ofertasPorInsumo) {
                if (ofertaInsumo.getMontoOferta() != null) {
                    precioTotal = precioTotal.add(ofertaInsumo.getMontoOferta());
                }
            }
        }
        return precioTotal;
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

    public ProcesoAdquisicionProveedor getProcesoAdquisicionProveedor() {
        return procesoAdquisicionProveedor;
    }

    public void setProcesoAdquisicionProveedor(ProcesoAdquisicionProveedor procesoAdquisicionProveedor) {
        this.procesoAdquisicionProveedor = procesoAdquisicionProveedor;
    }

    public boolean getAsociado() {
        return asociado;
    }

    public void setAsociado(boolean asociado) {
        this.asociado = asociado;
    }

    public void setEditable(boolean b) {
        editable = b;
    }

    public boolean getEditable() {
        return editable;
    }

    public List<ProcesoAdquisicionItemOfertaInsumo> getOfertasPorInsumo() {
        return ofertasPorInsumo;
    }

    public void setOfertasPorInsumo(List<ProcesoAdquisicionItemOfertaInsumo> ofertaPorInsumo) {
        this.ofertasPorInsumo = ofertaPorInsumo;
    }

    public BigDecimal getPrecioUnitOferta() {
        return precioUnitOferta;
    }

    public void setPrecioUnitOferta(BigDecimal precioUnitOferta) {
        this.precioUnitOferta = precioUnitOferta;
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

        final ProcesoAdquisicionItemProvOferta other = (ProcesoAdquisicionItemProvOferta) obj;
        if (this.id != null) {

            return Objects.equals(this.id, other.id);
        }

        return this == obj;
    }

}
