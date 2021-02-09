/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
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
import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;

/**
 *
 * Entidad correspondiente a los movimientos (lìnea de detalle) del presupuesto
 * escolar anual.
 */
@Entity
@Table(schema = Constantes.SCHEMA_FINANZAS, name = "sg_presupuesto_escolar_edicion_movimiento")
@EntityListeners({DecoratorEntityListener.class})
@Cacheable(false)
public class SgMovimientosEdicion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_presupuesto_escolar_edicion_movimiento_pk_seq", sequenceName = Constantes.SCHEMA_FINANZAS + ".sg_presupuesto_escolar_edicion_movimiento_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_presupuesto_escolar_edicion_movimiento_pk_seq")
    @Column(name = "mov_pk", nullable = false)
    private Integer id;

    @Size(max = 45)
    @Column(name = "mov_codigo", length = 45)
    private String codigo;

    @Column(name = "mov_num_movimiento")
    private Integer numMoviento;
    
    @JoinColumn(name = "mov_presupuesto_fk", referencedColumnName = "pes_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgPresupuestoEscolar presupuestoFk;

    @Column(name = "mov_tipo")
    private String tipo;

    @Column(name = "mov_monto")
    private BigDecimal monto;

    @Column(name = "mov_monto_aprobado")
    private BigDecimal montoAprobado;
    
    @Size(max = 20)
    @Column(name = "mov_origen", length = 20)
    private String origen;

    @JoinColumn(name = "mov_original_editado", referencedColumnName = "mov_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgPresupuestoEscolarMovimiento originalEditado;

    @JoinColumn(name = "mov_techo_presupuestal", referencedColumnName = "tp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TopePresupuestal techoPresupuestal;

    @Column(name = "mov_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "mov_ult_mod_usuario", length = 45)
    private String ultmodUsuario;

    @Column(name = "mov_version")
    @Version
    private Integer version;
    
    public SgMovimientosEdicion() {

    }

    public String getMovCodigoTipo() {
        return this.numMoviento + " - " + this.origen;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getNumMoviento() {
        return numMoviento;
    }

    public void setNumMoviento(Integer numMoviento) {
        this.numMoviento = numMoviento;
    }

    public SgPresupuestoEscolar getPresupuestoFk() {
        return presupuestoFk;
    }

    public void setPresupuestoFk(SgPresupuestoEscolar presupuestoFk) {
        this.presupuestoFk = presupuestoFk;
    }

    public SgPresupuestoEscolarMovimiento getOriginalEditado() {
        return originalEditado;
    }

    public void setOriginalEditado(SgPresupuestoEscolarMovimiento originalEditado) {
        this.originalEditado = originalEditado;
    }

    public TopePresupuestal getTechoPresupuestal() {
        return techoPresupuestal;
    }

    public void setTechoPresupuestal(TopePresupuestal techoPresupuestal) {
        this.techoPresupuestal = techoPresupuestal;
    }

    public Date getUltmodFecha() {
        return ultmodFecha;
    }

    public void setUltmodFecha(Date ultmodFecha) {
        this.ultmodFecha = ultmodFecha;
    }

    public String getUltmodUsuario() {
        return ultmodUsuario;
    }

    public void setUltmodUsuario(String ultmodUsuario) {
        this.ultmodUsuario = ultmodUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
    public String toString() {
        return "SgMovimientosEdicion{" + "id=" + id + ", version=" + version + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    
    // </editor-fold>

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
        final SgMovimientosEdicion other = (SgMovimientosEdicion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
