/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_rel_mod_ed_mod_aten", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "reaPk", scope = SgRelModEdModAten.class)
@Audited
public class SgRelModEdModAten implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rea_pk")
    private Long reaPk;
    
    @JoinColumn(name = "rea_modalidad_educativa_fk", referencedColumnName = "mod_pk")
    @ManyToOne(optional = false)
    private SgModalidad reaModalidadEducativa;
    
    @JoinColumn(name = "rea_modalidad_atencion_fk", referencedColumnName = "mat_pk")
    @ManyToOne(optional = false)
    private SgModalidadAtencion reaModalidadAtencion;
           
    @JoinColumn(name = "rea_sub_modalidad_atencion_fk", referencedColumnName = "smo_pk")
    @ManyToOne
    private SgSubModalidadAtencion reaSubModalidadAtencion;
    
    @Column(name = "rea_modalidad_atencion_flexible")
    private Boolean reaModalidadAtencionFlexible;
    
    @Column(name = "rea_habilitado")
    @AtributoHabilitado
    private Boolean reaHabilitado;
    
    @Column(name = "rea_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime reaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rea_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String reaUltModUsuario;
    
    @Column(name = "rea_version")
    @Version
    private Integer reaVersion;
    
    @OneToMany(mappedBy = "graRelacionModalidad")
    @NotAudited
    private List<SgGrado> reaGrado;
    
    @OneToMany(mappedBy = "pesRelacionModalidad")
    @NotAudited
    private List<SgPlanEstudio> reaPlanEstudio;

    public SgRelModEdModAten() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.reaModalidadAtencionFlexible = (reaSubModalidadAtencion != null); //Si tiene submodalidad, quiere decir que la modalidad es flexible
    }

    public SgRelModEdModAten(Long reaPk) {
        this.reaPk = reaPk;
    }

    public Long getReaPk() {
        return reaPk;
    }

    public void setReaPk(Long reaPk) {
        this.reaPk = reaPk;
    }

    public SgModalidad getReaModalidadEducativa() {
        return reaModalidadEducativa;
    }

    public void setReaModalidadEducativa(SgModalidad reaModalidadEducativa) {
        this.reaModalidadEducativa = reaModalidadEducativa;
    }

    public SgModalidadAtencion getReaModalidadAtencion() {
        return reaModalidadAtencion;
    }

    public void setReaModalidadAtencion(SgModalidadAtencion reaModalidadAtencion) {
        this.reaModalidadAtencion = reaModalidadAtencion;
    }

    public SgSubModalidadAtencion getReaSubModalidadAtencion() {
        return reaSubModalidadAtencion;
    }

    public void setReaSubModalidadAtencion(SgSubModalidadAtencion reaSubModalidadAtencion) {
        this.reaSubModalidadAtencion = reaSubModalidadAtencion;
    }


    public Boolean getReaHabilitado() {
        return reaHabilitado;
    }

    public void setReaHabilitado(Boolean reaHabilitado) {
        this.reaHabilitado = reaHabilitado;
    }

    public LocalDateTime getReaUltModFecha() {
        return reaUltModFecha;
    }

    public void setReaUltModFecha(LocalDateTime reaUltModFecha) {
        this.reaUltModFecha = reaUltModFecha;
    }

    public String getReaUltModUsuario() {
        return reaUltModUsuario;
    }

    public void setReaUltModUsuario(String reaUltModUsuario) {
        this.reaUltModUsuario = reaUltModUsuario;
    }

    public Integer getReaVersion() {
        return reaVersion;
    }

    public void setReaVersion(Integer reaVersion) {
        this.reaVersion = reaVersion;
    }

    public List<SgGrado> getReaGrado() {
        return reaGrado;
    }

    public void setReaGrado(List<SgGrado> reaGrado) {
        this.reaGrado = reaGrado;
    }

    public List<SgPlanEstudio> getReaPlanEstudio() {
        return reaPlanEstudio;
    }

    public void setReaPlanEstudio(List<SgPlanEstudio> reaPlanEstudio) {
        this.reaPlanEstudio = reaPlanEstudio;
    }

    public Boolean getReaModalidadAtencionFlexible() {
        return reaModalidadAtencionFlexible;
    }

    public void setReaModalidadAtencionFlexible(Boolean reaModalidadAtencionFlexible) {
        this.reaModalidadAtencionFlexible = reaModalidadAtencionFlexible;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reaPk != null ? reaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelModEdModAten)) {
            return false;
        }
        SgRelModEdModAten other = (SgRelModEdModAten) object;
        if ((this.reaPk == null && other.reaPk != null) || (this.reaPk != null && !this.reaPk.equals(other.reaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten[ reaPk=" + reaPk + " ]";
    }
    
}
