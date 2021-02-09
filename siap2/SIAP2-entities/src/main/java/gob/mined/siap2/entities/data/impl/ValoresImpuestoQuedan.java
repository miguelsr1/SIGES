/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoImpuesto;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_quedan_val_imp")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class ValoresImpuestoQuedan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_quedan_val_imp_gen")
    @SequenceGenerator(name = "seq_quedan_val_imp_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_quedan_val_imp", allocationSize = 1)
    @Column(name = "que_id")
    protected Integer id;
        
    @OneToOne
    @JoinColumn(name = "que_quedan")
    private QuedanEmitido quedan;
    
    @OneToOne
    @JoinColumn(name = "que_impuesto")
    private Impuesto impuesto;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "que_tipo_imp")
    private TipoImpuesto tipoImpuestoEnCodiguera;

    @Column(name = "que_impuesto_valor", columnDefinition = "Decimal(20,2)")
    private BigDecimal valorImpuestoEnCodiguera;
    
    @Column(name = "que_pago_valor_impuesto", columnDefinition = "Decimal(20,2)")
    private BigDecimal valorImpuestoEnPAgo;
    
    @Column(name = "que_pago_porcentaje_anticipo", columnDefinition = "Decimal(20,2)")
    private BigDecimal porcentajeRetencionEnCodiguera;
    
    @Column(name = "que_valor_aniticpo", columnDefinition = "Decimal(20,2)")
    private BigDecimal valorRetencionEnPago;
    
    
    
    //Auditoria
    @Column(name = "que_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "que_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "que_version")
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

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public QuedanEmitido getQuedan() {
        return quedan;
    }

    public void setQuedan(QuedanEmitido quedan) {
        this.quedan = quedan;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public TipoImpuesto getTipoImpuestoEnCodiguera() {
        return tipoImpuestoEnCodiguera;
    }

    public void setTipoImpuestoEnCodiguera(TipoImpuesto tipoImpuestoEnCodiguera) {
        this.tipoImpuestoEnCodiguera = tipoImpuestoEnCodiguera;
    }

    public BigDecimal getValorImpuestoEnCodiguera() {
        return valorImpuestoEnCodiguera;
    }

    public void setValorImpuestoEnCodiguera(BigDecimal valorImpuestoEnCodiguera) {
        this.valorImpuestoEnCodiguera = valorImpuestoEnCodiguera;
    }

    public BigDecimal getValorImpuestoEnPAgo() {
        return valorImpuestoEnPAgo;
    }

    public void setValorImpuestoEnPAgo(BigDecimal valorImpuestoEnPAgo) {
        this.valorImpuestoEnPAgo = valorImpuestoEnPAgo;
    }

    public BigDecimal getPorcentajeRetencionEnCodiguera() {
        return porcentajeRetencionEnCodiguera;
    }

    public void setPorcentajeRetencionEnCodiguera(BigDecimal porcentajeRetencionEnCodiguera) {
        this.porcentajeRetencionEnCodiguera = porcentajeRetencionEnCodiguera;
    }

    public BigDecimal getValorRetencionEnPago() {
        return valorRetencionEnPago;
    }

    public void setValorRetencionEnPago(BigDecimal valorRetencionEnPago) {
        this.valorRetencionEnPago = valorRetencionEnPago;
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
        final ValoresImpuestoQuedan other = (ValoresImpuestoQuedan) obj;
        if ((this.id != null) && (other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }



}
