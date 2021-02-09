package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.EstadoTransferenciaComponente;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.eclipse.persistence.annotations.Customizer;

@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_transferencias_componente")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@Cacheable(false)
public class TransferenciasComponente implements Serializable{
    
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_transferencias_componente", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_transferencias_componente", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transferencias_componente")
    @Column(name = "tc_id")
    private Integer id;
    
    @Column(name = "tc_descripcion")
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "tc_componente")
    private CategoriaPresupuestoEscolar componente;
    
    @ManyToOne
    @JoinColumn(name = "tc_subcomponente")
    private ComponentePresupuestoEscolar subcomponente;
    
    @ManyToOne
    @JoinColumn(name = "tc_unidad_presupuestaria")
    private Cuentas unidadPresupuestaria;
    
    @ManyToOne
    @JoinColumn(name = "tc_linea_presupuestaria")
    private Cuentas lineaPresupuestaria;
    
    @ManyToOne
    @JoinColumn(name = "tc_anio_fiscal")
    private AnioFiscal anioFiscal;
    
    @Column(name = "tc_porcentaje")
    private BigDecimal porcentaje;
    
    @Column(name = "tc_importe_fijo_centro")
    private BigDecimal importeFijoCentro;
    
    @Column(name = "tc_remanente")
    private Boolean remanente;
    
    @Column(name = "tc_estado")
    @Enumerated(EnumType.ORDINAL)
    private EstadoTransferenciaComponente estado;

    @ManyToOne
    @JoinColumn(name = "tc_fuente_recursos_fk")
    private FuenteRecursos fuenteRecursos;
    
    @ManyToOne
    @JoinColumn(name = "tc_transferencia")
    private Transferencia transferencia;
    
    //Auditoria
    @Column(name = "tc_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "tc_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "tc_version")
    @Version
    private Integer version;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoriaPresupuestoEscolar getComponente() {
        return componente;
    }

    public void setComponente(CategoriaPresupuestoEscolar componente) {
        this.componente = componente;
    }

    public ComponentePresupuestoEscolar getSubcomponente() {
        return subcomponente;
    }

    public void setSubcomponente(ComponentePresupuestoEscolar subcomponente) {
        this.subcomponente = subcomponente;
    }

    public Cuentas getUnidadPresupuestaria() {
        return unidadPresupuestaria;
    }

    public void setUnidadPresupuestaria(Cuentas unidadPresupuestaria) {
        this.unidadPresupuestaria = unidadPresupuestaria;
    }

    public Cuentas getLineaPresupuestaria() {
        return lineaPresupuestaria;
    }

    public void setLineaPresupuestaria(Cuentas lineaPresupuestaria) {
        this.lineaPresupuestaria = lineaPresupuestaria;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public EstadoTransferenciaComponente getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransferenciaComponente estado) {
        this.estado = estado;
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

    @Override
    public String toString() {
        return "TransferenciasComponente{" + 
                "id=" + id + 
                ", componente=" + componente + 
                ", subcomponente=" + subcomponente + 
                ", unidadPresupuestaria=" + unidadPresupuestaria + 
                ", lineaPresupuestaria=" + lineaPresupuestaria + 
                ", anioFiscal=" + anioFiscal + 
                ", porcentaje=" + porcentaje + 
                ", estado=" + estado + 
                ", ultMod=" + ultMod + 
                ", ultUsuario=" + ultUsuario + 
                ", version=" + version + '}';
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final TransferenciasComponente other = (TransferenciasComponente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public BigDecimal getImporteFijoCentro() {
        return importeFijoCentro;
    }

    public void setImporteFijoCentro(BigDecimal importeFijoCentro) {
        this.importeFijoCentro = importeFijoCentro;
    }

    public Boolean getRemanente() {
        return remanente;
    }

    public void setRemanente(Boolean remanente) {
        this.remanente = remanente;
    }

    public FuenteRecursos getFuenteRecursos() {
        return fuenteRecursos;
    }

    public void setFuenteRecursos(FuenteRecursos fuenteRecursos) {
        this.fuenteRecursos = fuenteRecursos;
    }

    public Transferencia getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    
    
    
    
}
