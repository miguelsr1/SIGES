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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_quedan_emitidos")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class QuedanEmitido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_quedan_emitidos_gen")
    @SequenceGenerator(name = "seq_quedan_emitidos_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_quedan_emitidos", allocationSize = 1)
    @Column(name = "que_id")
    protected Integer id;
    
        
    @OneToOne
    @JoinColumn(name = "que_archivo")
    private Archivo archivo;
    
        
    @OneToOne
    @JoinColumn(name = "que_acta")
    private ActaContrato acta;
    
    
    @ManyToOne
    @JoinColumn(name = "que_num_comp_acta")
    private NumeroComprobanteRecepcionDePAgo numeroComprobanteRecepcionPago;
    
    
    @Column(name = "que_fecha_generacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date fechaGeneracion;

    
    @Column(name = "que_monto", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoquedan;
    
    
    @Column(name = "que_monto_sin_impuestos", columnDefinition = "Decimal(20,2)")
    private BigDecimal montoSinImpuestos;
    
    @OneToMany(mappedBy = "quedan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ValoresImpuestoQuedan> valoresImpuesto;
    
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

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public NumeroComprobanteRecepcionDePAgo getNumeroComprobanteRecepcionPago() {
        return numeroComprobanteRecepcionPago;
    }

    public void setNumeroComprobanteRecepcionPago(NumeroComprobanteRecepcionDePAgo numeroComprobanteRecepcionPago) {
        this.numeroComprobanteRecepcionPago = numeroComprobanteRecepcionPago;
    }
    

    public ActaContrato getActa() {
        return acta;
    }

    public void setActa(ActaContrato acta) {
        this.acta = acta;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public BigDecimal getMontoSinImpuestos() {
        return montoSinImpuestos;
    }

    public void setMontoSinImpuestos(BigDecimal montoSinImpuestos) {
        this.montoSinImpuestos = montoSinImpuestos;
    }

    public List<ValoresImpuestoQuedan> getValoresImpuesto() {
        return valoresImpuesto;
    }

    public void setValoresImpuesto(List<ValoresImpuestoQuedan> valoresImpuesto) {
        this.valoresImpuesto = valoresImpuesto;
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
        final QuedanEmitido other = (QuedanEmitido) obj;
        if ((this.id != null) && (other.id!=null)) {
            return Objects.equals(this.id, other.id);
        }

        return (this == obj);
    }

    public BigDecimal getMontoquedan() {
        return montoquedan;
    }

    public void setMontoquedan(BigDecimal montoquedan) {
        this.montoquedan = montoquedan;
    }

}
