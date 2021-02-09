package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


@Entity
@Table(schema = Constantes.SCHEMA_FINANZAS, name = "sg_presupuesto_escolar_movimiento")
@EntityListeners({DecoratorEntityListener.class})
@Cacheable(false)
public class SgPresupuestoEscolarMovimiento implements Serializable{
    
     private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_presupuesto_escolar_movimiento_pk_seq", sequenceName = Constantes.SCHEMA_FINANZAS + ".sg_presupuesto_escolar_movimiento_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_presupuesto_escolar_movimiento_pk_seq")
    @Column(name = "mov_pk")
    private Integer id;
    
    @Column(name = "mov_origen")
    private String origen;
    
    @Column(name = "mov_tipo")
    private String tipo;
    
    @Column(name = "mov_num_movimiento")
    private Integer numMovimiento;
    
    @Column(name = "mov_monto")
    private BigDecimal monto;
    
    @Column(name = "mov_monto_aprobado")
    private BigDecimal montoAprobado;
    
    @Column(name = "mov_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "mov_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "mov_version")
    @Version
    private Integer version;
    
    @ManyToOne
    //@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "mov_techo_presupuestal")
    private TopePresupuestal topePresupuestal;
    
    @ManyToOne
    //@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "mov_presupuesto_fk")
    private SgPresupuestoEscolar presupuestoEscolar;
    
    
    @Column(name = "mov_anulacion", nullable = true)
    private Boolean anulacion;

    @Column(name = "mov_editado", nullable = true)
    private Boolean eEditado;
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Integer getNumMovimiento() {
        return numMovimiento;
    }

    public void setNumMovimiento(Integer numMovimiento) {
        this.numMovimiento = numMovimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public TopePresupuestal getTopePresupuestal() {
        return topePresupuestal;
    }

    public void setTopePresupuestal(TopePresupuestal topePresupuestal) {
        this.topePresupuestal = topePresupuestal;
    }

    public SgPresupuestoEscolar getPresupuestoEscolar() {
        return presupuestoEscolar;
    }

    public void setPresupuestoEscolar(SgPresupuestoEscolar presupuestoEscolar) {
        this.presupuestoEscolar = presupuestoEscolar;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public Boolean getAnulacion() {
        return anulacion;
    }

    public void setAnulacion(Boolean anulacion) {
        this.anulacion = anulacion;
    }

    public Boolean geteEditado() {
        return eEditado;
    }

    public void seteEditado(Boolean eEditado) {
        this.eEditado = eEditado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final SgPresupuestoEscolarMovimiento other = (SgPresupuestoEscolarMovimiento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPresupuestoEscolarMovimiento{" + "id=" + id + ", version=" + version + '}';
    }

   
}
