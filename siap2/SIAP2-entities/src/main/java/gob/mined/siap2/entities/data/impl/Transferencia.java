package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.EstadoTransferenciaComponente;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_transferencias")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Transferencia implements Serializable{
    
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_transferencias", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_transferencias", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transferencias")
    @Column(name = "tra_id")
    private Integer id;
    
    @Column(name = "tra_descripcion")
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "tra_id_anio_fiscal") 
    private AnioFiscal anioFiscal;
    
    @ManyToOne
    @JoinColumn(name = "tra_id_subcomponente") 
    private ComponentePresupuestoEscolar subcomponente;

    @ManyToOne
    @JoinColumn(name = "tra_id_linea_presupuestaria") 
    private Cuentas lineaPresupuestaria;

    @Column(name = "tra_porcentaje")
    private BigDecimal porcentaje;
    
    @Column(name = "tra_importe_fijo_centro")
    private BigDecimal importeFijoCentro;
    
    @Column(name = "tra_remanente")
    private Boolean remanente;
    
    @Column(name = "tra_estado")
    @Enumerated(EnumType.ORDINAL)
    private EstadoTransferenciaComponente estado;
    
    @ManyToOne
    @JoinColumn(name = "tra_relacion_componente_anio_fiscal")
    private RelacionGesPresEsAnioFiscal relacionGesPresEsAnioFiscal;
    
    @Column(name = "tra_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;
    
    @Column(name = "tra_ult_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "tra_version")
    @Version
    private Integer version;

    
    
    
    
    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnioFiscal getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(AnioFiscal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public ComponentePresupuestoEscolar getSubcomponente() {
        return subcomponente;
    }

    public void setSubcomponente(ComponentePresupuestoEscolar subcomponente) {
        this.subcomponente = subcomponente;
    }

    public Cuentas getLineaPresupuestaria() {
        return lineaPresupuestaria;
    }

    public void setLineaPresupuestaria(Cuentas lineaPresupuestaria) {
        this.lineaPresupuestaria = lineaPresupuestaria;
    }

    public Date getUltModFecha() {
        return ultModFecha;
    }

    public void setUltModFecha(Date ultModFecha) {
        this.ultModFecha = ultModFecha;
    }

    public String getUltModUsuario() {
        return ultModUsuario;
    }

    public void setUltModUsuario(String ultModUsuario) {
        this.ultModUsuario = ultModUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
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

    public EstadoTransferenciaComponente getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransferenciaComponente estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public RelacionGesPresEsAnioFiscal getRelacionGesPresEsAnioFiscal() {
        return relacionGesPresEsAnioFiscal;
    }

    public void setRelacionGesPresEsAnioFiscal(RelacionGesPresEsAnioFiscal relacionGesPresEsAnioFiscal) {
        this.relacionGesPresEsAnioFiscal = relacionGesPresEsAnioFiscal;
    }

    

    
}
