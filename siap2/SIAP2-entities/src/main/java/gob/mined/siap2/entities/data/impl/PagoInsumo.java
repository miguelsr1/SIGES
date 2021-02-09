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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pago_insumo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class PagoInsumo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pago_insumo")
    @SequenceGenerator(name = "seq_pago_insumo", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_pago_insumo", allocationSize = 1)
    @Column(name = "pag_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pag_rel_item_insumo")
    private RelacionProAdqItemInsumo relacionItemInsumo;

    @ManyToOne
    @JoinColumn(name = "pag_contrato")
    private ActaContrato contrato;

    @Column(name = "pag_importe", columnDefinition = "Decimal(20,2)")
    private BigDecimal importe;

    @Column(name = "pag_cantidad")
    private Integer cantRecibida;
    
    @Column(name = "pag_descripcion")
    private String descripcion;

    //Auditoria
    @Column(name = "pag_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pag_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pag_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public RelacionProAdqItemInsumo getRelacionItemInsumo() {
        return relacionItemInsumo;
    }

    public void setRelacionItemInsumo(RelacionProAdqItemInsumo relacionItemInsumo) {
        this.relacionItemInsumo = relacionItemInsumo;
    }

    public ActaContrato getContrato() {
        return contrato;
    }

    public void setContrato(ActaContrato contrato) {
        this.contrato = contrato;
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

    public void setCantRecibida(Integer cantRecivida) {
        this.cantRecibida = cantRecivida;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        final PagoInsumo other = (PagoInsumo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
