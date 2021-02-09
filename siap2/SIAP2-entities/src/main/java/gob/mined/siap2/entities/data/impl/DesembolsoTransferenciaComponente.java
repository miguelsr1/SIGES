package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.EstadoDesembolsoTransferenciaComponente;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_desembolso_transferencia_componente")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class DesembolsoTransferenciaComponente implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_desembolso_transferencia", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_desembolso_transferencia", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_desembolso_transferencia")
    @Column(name = "des_id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "des_transferencia_desembolso")
    private TransferenciasComponente transferenciasComponente;
    
    @Column(name = "des_porcentaje")
    private BigDecimal porcentaje;
    
    @Column(name = "des_estado")
    @Enumerated(EnumType.STRING)
    private EstadoDesembolsoTransferenciaComponente estado;

    @Column(name = "des_fecha_desembolso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesembolso;
    
    @Column(name = "des_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "des_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "des_version")
    @Version
    private Integer version;
    
    
    
    
    
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer pk) {
        this.id = pk;
    }

    public TransferenciasComponente getTransferenciasComponente() {
        return transferenciasComponente;
    }

    public void setTransferenciasComponente(TransferenciasComponente transferenciasComponente) {
        this.transferenciasComponente = transferenciasComponente;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public EstadoDesembolsoTransferenciaComponente getEstado() {
        return estado;
    }

    public void setEstado(EstadoDesembolsoTransferenciaComponente estado) {
        this.estado = estado;
    }

    public Date getFechaDesembolso() {
        return fechaDesembolso;
    }

    public void setFechaDesembolso(Date fechaDesembolso) {
        this.fechaDesembolso = fechaDesembolso;
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

    
    
    
    
}
