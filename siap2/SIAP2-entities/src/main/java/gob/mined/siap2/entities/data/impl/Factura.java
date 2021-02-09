/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoDestinoFactura;
import gob.mined.siap2.entities.enums.TipoFactura;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author rgonzalez
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_factura")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "fac_tipo_destino", discriminatorType = DiscriminatorType.STRING, length = 64)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public abstract class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_factura_gen")
    @SequenceGenerator(name = "seq_factura_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_factura", allocationSize = 1)
    @Column(name = "fac_id")
    protected Integer id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "fac_tipo_destino")
    private TipoDestinoFactura destinoFactura;

    @Column(name = "fac_fecha")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date fecha;

    @Column(name = "fac_numero")
    private Integer numero;
    
    @Lob
    @Column(name = "fac_obs")
    private String observacion;

    @Column(name = "fac_importe", columnDefinition = "Decimal(20,2)")
    private BigDecimal importe;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "fac_tipo")
    private TipoFactura tipo;
    
    //Auditoria
    @Column(name = "fac_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "fac_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "fac_version")
    @Version
    private Integer version;
        
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
  
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
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

    public TipoFactura getTipo() {
        return tipo;
    }

    public void setTipo(TipoFactura tipo) {
        this.tipo = tipo;
    }

    
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public TipoDestinoFactura getDestinoFactura() {
        return destinoFactura;
    }

    public void setDestinoFactura(TipoDestinoFactura destinoFactura) {
        this.destinoFactura = destinoFactura;
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
        final Factura other = (Factura) obj;
        if (this.id!=null && other.id !=null) {
            return Objects.equals(this.id, other.id);
        }
        return this == other;
    }


}
