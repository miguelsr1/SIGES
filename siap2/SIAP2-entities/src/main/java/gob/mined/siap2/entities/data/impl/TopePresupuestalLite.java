package gob.mined.siap2.entities.data.impl;


import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siges.entities.data.impl.SgSedeLite;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_tope_presupestal")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class TopePresupuestalLite implements Serializable{
 
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_tope_presupuestal", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_tope_presupuestal", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tope_presupuestal")
    @Column(name = "tp_id")
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "tp_componente")
    private ComponentePresupuestoEscolarLite gesPresEs;
    
    @ManyToOne
    @JoinColumn(name = "tp_sub_cuenta")
    private CuentasLite subCuenta;
    
    @ManyToOne
    @JoinColumn(name = "tp_sede")
    private SgSedeLite sede;
    
    @Column(name = "tp_monto_formulacion")
    private BigDecimal monto;
    
    @Column(name = "tp_monto_aprobado")
    private BigDecimal montoAprobado;
    
    @Column(name = "tp_cant_matricula")
    private Integer cantidadMatricula;
    
    @Column(name = "tp_cant_matricula_aprobada")
    private Integer cantidadMatriculaAprobada;
    
    @ManyToOne
    @JoinColumn(name = "tp_anio_fiscal")
    private AnioFiscal anioFiscal;
    
    @Column(name = "tp_estado")
    @Enumerated(EnumType.ORDINAL)
    private EstadoTopePresupuestal estado;

    //Auditoria
    @Column(name = "tp_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "tp_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "tp_version")
    @Version
    private Integer version;
    
    public TopePresupuestalLite() {
     
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ComponentePresupuestoEscolarLite getGesPresEs() {
        return gesPresEs;
    }

    public void setGesPresEs(ComponentePresupuestoEscolarLite gesPresEs) {
        this.gesPresEs = gesPresEs;
    }

    public CuentasLite getSubCuenta() {
        return subCuenta;
    }

    public void setSubCuenta(CuentasLite subCuenta) {
        this.subCuenta = subCuenta;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
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

    public EstadoTopePresupuestal getEstado() {
        return estado;
    }

    public void setEstado(EstadoTopePresupuestal estado) {
        this.estado = estado;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal monto_aprobado) {
        this.montoAprobado = monto_aprobado;
    }

    public SgSedeLite getSede() {
        return sede;
    }

    public void setSede(SgSedeLite sede) {
        this.sede = sede;
    }

   public String getCodigoSede(){
       return sede != null ? sede.getCodigo() : "";
   }

    public Integer getCantidadMatricula() {
        return cantidadMatricula;
    }

    public void setCantidadMatricula(Integer cantidadMatricula) {
        this.cantidadMatricula = cantidadMatricula;
    }

    public Integer getCantidadMatriculaAprobada() {
        return cantidadMatriculaAprobada;
    }

    public void setCantidadMatriculaAprobada(Integer cantidadMatriculaAprobada) {
        this.cantidadMatriculaAprobada = cantidadMatriculaAprobada;
    }

    @Override
    public String toString() {
        return "TopePresupuestalLite{" + "id=" + id + ", gesPresEs=" + gesPresEs + ", subCuenta=" + subCuenta + ", sede=" + sede + ", monto=" + monto + ", montoAprobado=" + montoAprobado + ", cantidadMatricula=" + cantidadMatricula + ", cantidadMatriculaAprobada=" + cantidadMatriculaAprobada + ", anioFiscal=" + anioFiscal + ", estado=" + estado + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final TopePresupuestalLite other = (TopePresupuestalLite) obj;
        if (this.id!= null && other.getId()!=null) {
            return Objects.equals(this.id, other.getId());
        }
        return this == other;
    }
}
