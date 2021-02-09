package gob.mined.siap2.entities.data.impl;


import gob.mined.siges.entities.data.impl.SgPresupuestoEscolarMovimiento;
import gob.mined.siges.entities.data.impl.SgSede;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_tope_presupestal")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class TopePresupuestal implements Serializable{
 
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_tope_presupuestal", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_tope_presupuestal", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tope_presupuestal")
    @Column(name = "tp_id")
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "tp_componente")
    private ComponentePresupuestoEscolar gesPresEs;
    
    @ManyToOne
    @JoinColumn(name = "tp_sub_cuenta")
    private Cuentas subCuenta;
    
    @ManyToOne
    @JoinColumn(name = "tp_sede")
    private SgSede sede;
    
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
    
    @ManyToOne
    @JoinColumn(name = "tp_fuente_financiamiento")
    private FuenteFinanciamiento fuenteFinanciamiento;
    
    @ManyToOne
    @JoinColumn(name = "tp_fuente_recursos")
    private FuenteRecursos fuenteRecursos;


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
    
    @ManyToOne
    @JoinColumn(name = "tp_movimiento")
    private SgPresupuestoEscolarMovimiento presupuestoEscolarMovimiento;
    
    
    @ManyToOne
    @JoinColumn(name = "tp_rel_ges_pres")
    private RelacionGesPresEsAnioFiscal relGesPres;
    
    @OneToMany(mappedBy = "topePresupuestal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopeDetalleMatriculas> detalleMatriculas;

    public TopePresupuestal() {
        this.detalleMatriculas =  new ArrayList();
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ComponentePresupuestoEscolar getGesPresEs() {
        return gesPresEs;
    }

    public void setGesPresEs(ComponentePresupuestoEscolar gesPresEs) {
        this.gesPresEs = gesPresEs;
    }

    public Cuentas getSubCuenta() {
        return subCuenta;
    }

    public void setSubCuenta(Cuentas subCuenta) {
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

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

   public String getCodigoSede(){
       return sede != null ? sede.getCodigo() : "";
   }

    public SgPresupuestoEscolarMovimiento getPresupuestoEscolarMovimiento() {
        return presupuestoEscolarMovimiento;
    }

    public void setPresupuestoEscolarMovimiento(SgPresupuestoEscolarMovimiento presupuestoEscolarMovimiento) {
        this.presupuestoEscolarMovimiento = presupuestoEscolarMovimiento;
    }

    public FuenteFinanciamiento getFuenteFinanciamiento() {
        return fuenteFinanciamiento;
    }

    public void setFuenteFinanciamiento(FuenteFinanciamiento fuenteFinanciamiento) {
        this.fuenteFinanciamiento = fuenteFinanciamiento;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
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

    public RelacionGesPresEsAnioFiscal getRelGesPres() {
        return relGesPres;
    }

    public void setRelGesPres(RelacionGesPresEsAnioFiscal relGesPres) {
        this.relGesPres = relGesPres;
    }

     public List<TopeDetalleMatriculas> getDetalleMatriculas() {
        return detalleMatriculas;
    }

    public void setDetalleMatriculas(List<TopeDetalleMatriculas> detalleMatriculas) {
        this.detalleMatriculas = detalleMatriculas;
    }
    
    @Override
    public String toString() {
        return "TopePresupuestal{" + "id=" + id + ", gesPresEs=" + gesPresEs + ", subCuenta=" + subCuenta + ", sede=" + sede + ", monto=" + monto + ", montoAprobado=" + montoAprobado + ", anioFiscal=" + anioFiscal + ", estado=" + estado + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + ", presupuestoEscolarMovimiento=" + presupuestoEscolarMovimiento + '}';
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
        final TopePresupuestal other = (TopePresupuestal) obj;
        if (this.id!= null && other.getId()!=null) {
            return Objects.equals(this.id, other.getId());
        }
        return this == other;
    }
}
