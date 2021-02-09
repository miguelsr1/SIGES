/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.enums.EstadoCompromiso;
import gob.mined.siap2.entities.enums.TipoCompromisoPresupuestario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_compromisos_prestario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "com_tipo", discriminatorType = DiscriminatorType.STRING, length = 64)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public abstract class CompromisoPresupuestario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_comp_pres_gen")
    @SequenceGenerator(name = "seq_comp_pres_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_comp_pres", allocationSize = 1)
    @Column(name = "com_id")
    protected Integer id;  
        
    // update/insert is managed by discriminator mechanics
    @Column(name = "com_tipo", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoCompromisoPresupuestario tipo;
            
    @Enumerated(EnumType.STRING)
    @Column(name = "com_estado")
    private EstadoCompromiso estado;
        
    @Column(name = "com_fecha_solicitud")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaSolicitud;    
    
    @Column(name = "com_fecha_emision")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEmision;    
    
    @ManyToOne
    @JoinColumn(name = "com_usu_emision_id")
    private SsUsuario usuarioEmision;
    
    @Column(name = "com_numero_safi")
    private String numeroSAFI;
        
    @Column(name = "com_fecha_validacion")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaValidacion;    
    
    @ManyToOne
    @JoinColumn(name = "com_usu_validacion_id")
    private SsUsuario usuarioValidacion;
        
    @ManyToOne
    @JoinColumn(name = "com_reserva_fondos")
    private Archivo reservaFondos;
    
    //Auditoria
    @Column(name = "com_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "com_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "com_version")
    @Version
    private Integer version;

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EstadoCompromiso getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompromiso estado) {
        this.estado = estado;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
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

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public String getNumeroSAFI() {
        return numeroSAFI;
    }

    public void setNumeroSAFI(String numeroSAFI) {
        this.numeroSAFI = numeroSAFI;
    }    

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public SsUsuario getUsuarioValidacion() {
        return usuarioValidacion;
    }

    public void setUsuarioValidacion(SsUsuario usuarioValidacion) {
        this.usuarioValidacion = usuarioValidacion;
    }

    public TipoCompromisoPresupuestario getTipo() {
        return tipo;
    }

    public void setTipo(TipoCompromisoPresupuestario tipo) {
        this.tipo = tipo;
    }

    public Archivo getReservaFondos() {
        return reservaFondos;
    }

    public void setReservaFondos(Archivo reservaFondos) {
        this.reservaFondos = reservaFondos;
    }
    
    public SsUsuario getUsuarioEmision() {
        return usuarioEmision;
    }

    public void setUsuarioEmision(SsUsuario usuarioEmision) {
        this.usuarioEmision = usuarioEmision;
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
        final CompromisoPresupuestario other = (CompromisoPresupuestario) obj;
        if (this.id!=null && other.id !=null) {
            return Objects.equals(this.id, other.id);
        }
        return this == other;
    }

}
