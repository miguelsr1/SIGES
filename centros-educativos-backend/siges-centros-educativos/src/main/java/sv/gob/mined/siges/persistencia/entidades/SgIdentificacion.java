/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPais;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoIdentificacion;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_identificaciones", uniqueConstraints = {
    @UniqueConstraint(name = "ide_numero_tipo_pais_uk", columnNames = {"ide_numero_documento","ide_tipo_documento","ide_pais_emisor"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "idePk", scope = SgIdentificacion.class)
@Audited
public class SgIdentificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_pk")
    private Long idePk;
    
    @Column(name = "ide_fecha_expedicion")
    private LocalDate ideFechaExpedicion;
    
    @Column(name = "ide_fecha_vencimiento")
    private LocalDate ideFechaVencimiento;
    
    @Size(max = 50)
    @Column(name = "ide_numero_documento",length = 50)
    private String ideNumeroDocumento;
    
    @Size(max = 25)
    @Column(name = "ide_folio",length = 25)
    private String ideFolio;
    
    @Size(max = 25)
    @Column(name = "ide_libro",length = 25)
    private String ideLibro;
    
    @Column(name = "ide_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ideUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ide_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String ideUltModUsuario;
    
    @Column(name = "ide_version")
    @Version
    private Integer ideVersion;
    
    @JoinColumn(name = "ide_persona")
    @ManyToOne
    private SgPersona idePersona;
    
    @JoinColumn(name = "ide_tipo_documento")
    @ManyToOne
    private SgTipoIdentificacion ideTipoDocumento;
    
    @JoinColumn(name = "ide_pais_emisor")
    @ManyToOne
    private SgPais idePaisEmisor;

    public SgIdentificacion() {
    }

    public Long getIdePk() {
        return idePk;
    }

    public void setIdePk(Long idePk) {
        this.idePk = idePk;
    }

    public LocalDate getIdeFechaExpedicion() {
        return ideFechaExpedicion;
    }

    public void setIdeFechaExpedicion(LocalDate ideFechaExpedicion) {
        this.ideFechaExpedicion = ideFechaExpedicion;
    }

    public LocalDate getIdeFechaVencimiento() {
        return ideFechaVencimiento;
    }

    public void setIdeFechaVencimiento(LocalDate ideFechaVencimiento) {
        this.ideFechaVencimiento = ideFechaVencimiento;
    }

    public String getIdeNumeroDocumento() {
        return ideNumeroDocumento;
    }

    public void setIdeNumeroDocumento(String ideNumeroDocumento) {
        this.ideNumeroDocumento = ideNumeroDocumento;
    }

    public String getIdeFolio() {
        return ideFolio;
    }

    public void setIdeFolio(String ideFolio) {
        this.ideFolio = ideFolio;
    }

    public String getIdeLibro() {
        return ideLibro;
    }

    public void setIdeLibro(String ideLibro) {
        this.ideLibro = ideLibro;
    }

    public LocalDateTime getIdeUltModFecha() {
        return ideUltModFecha;
    }

    public void setIdeUltModFecha(LocalDateTime ideUltModFecha) {
        this.ideUltModFecha = ideUltModFecha;
    }

    public String getIdeUltModUsuario() {
        return ideUltModUsuario;
    }

    public void setIdeUltModUsuario(String ideUltModUsuario) {
        this.ideUltModUsuario = ideUltModUsuario;
    }

    public Integer getIdeVersion() {
        return ideVersion;
    }

    public void setIdeVersion(Integer ideVersion) {
        this.ideVersion = ideVersion;
    }

    public SgPersona getIdePersona() {
        return idePersona;
    }

    public void setIdePersona(SgPersona idePersona) {
        this.idePersona = idePersona;
    }

    public SgTipoIdentificacion getIdeTipoDocumento() {
        return ideTipoDocumento;
    }

    public void setIdeTipoDocumento(SgTipoIdentificacion ideTipoDocumento) {
        this.ideTipoDocumento = ideTipoDocumento;
    }

    public SgPais getIdePaisEmisor() {
        return idePaisEmisor;
    }

    public void setIdePaisEmisor(SgPais idePaisEmisor) {
        this.idePaisEmisor = idePaisEmisor;
    }
    

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePk != null ? idePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgIdentificacion)) {
            return false;
        }
        SgIdentificacion other = (SgIdentificacion) object;
        if ((this.idePk == null && other.idePk != null) || (this.idePk != null && !this.idePk.equals(other.idePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgIdentificaciones[ idePk=" + idePk + " ]";
    }
    
}
