/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "sg_modalidades", uniqueConstraints = {
    @UniqueConstraint(name = "mod_codigo_uk", columnNames = {"mod_codigo"})
    ,
    @UniqueConstraint(name = "mod_nombre_ciclo_uk", columnNames = {"mod_nombre", "mod_ciclo"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "modPk", scope = SgModalidad.class)
@Audited
public class SgModalidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mod_pk")
    private Long modPk;

    @Size(max = 4)
    @Column(name = "mod_codigo", length = 4)
    @AtributoCodigo
    private String modCodigo;

    @Size(max = 500)
    @Column(name = "mod_nombre", length = 500)
    @AtributoNombre
    private String modNombre;

    @Column(name = "mod_orden")
    private Integer modOrden;
    
    @Column(name = "mod_admite_opcion")
    private Boolean modAdmiteOpcion;

    @Column(name = "mod_habilitado")
    @AtributoHabilitado
    private Boolean modHabilitado;

    @Column(name = "mod_aplica_diplomado")
    private Boolean modAplicaDiplomado;
    
    @Column(name = "mod_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime modUltModFecha;

    @Size(max = 45)
    @Column(name = "mod_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String modUltModUsuario;

    @Column(name = "mod_version")
    @Version
    private Integer modVersion;

    @JoinColumn(name = "mod_ciclo", referencedColumnName = "cic_pk")
    @ManyToOne(optional = false)
    private SgCiclo modCiclo;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "opcModalidad")
    @NotAudited
    private List<SgOpcion> modOpciones;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "reaModalidadEducativa")
    @NotAudited
    private List<SgRelModEdModAten> modRelModalidadAtencion;

    public SgModalidad() {
    }

    public Long getModPk() {
        return modPk;
    }

    public void setModPk(Long modPk) {
        this.modPk = modPk;
    }

    public String getModCodigo() {
        return modCodigo;
    }

    public void setModCodigo(String modCodigo) {
        this.modCodigo = modCodigo;
    }

    public String getModNombre() {
        return modNombre;
    }

    public void setModNombre(String modNombre) {
        this.modNombre = modNombre;
    }

    public Integer getModOrden() {
        return modOrden;
    }

    public void setModOrden(Integer modOrden) {
        this.modOrden = modOrden;
    }

    public Boolean getModAdmiteOpcion() {
        return modAdmiteOpcion;
    }

    public void setModAdmiteOpcion(Boolean modAdmiteOpcion) {
        this.modAdmiteOpcion = modAdmiteOpcion;
    }
    
    public Boolean getModHabilitado() {
        return modHabilitado;
    }

    public void setModHabilitado(Boolean modHabilitado) {
        this.modHabilitado = modHabilitado;
    }

    public LocalDateTime getModUltModFecha() {
        return modUltModFecha;
    }

    public void setModUltModFecha(LocalDateTime modUltModFecha) {
        this.modUltModFecha = modUltModFecha;
    }

    public String getModUltModUsuario() {
        return modUltModUsuario;
    }

    public void setModUltModUsuario(String modUltModUsuario) {
        this.modUltModUsuario = modUltModUsuario;
    }

    public Integer getModVersion() {
        return modVersion;
    }

    public void setModVersion(Integer modVersion) {
        this.modVersion = modVersion;
    }

    public SgCiclo getModCiclo() {
        return modCiclo;
    }

    public void setModCiclo(SgCiclo modCiclo) {
        this.modCiclo = modCiclo;
    }

    public List<SgRelModEdModAten> getModRelModalidadAtencion() {
        return modRelModalidadAtencion;
    }

    public void setModRelModalidadAtencion(List<SgRelModEdModAten> modRelModalidadAtencion) {
        this.modRelModalidadAtencion = modRelModalidadAtencion;
    }

    public List<SgOpcion> getModOpciones() {
        return modOpciones;
    }

    public void setModOpciones(List<SgOpcion> modOpciones) {
        this.modOpciones = modOpciones;
    }

    public Boolean getModAplicaDiplomado() {
        return modAplicaDiplomado;
    }

    public void setModAplicaDiplomado(Boolean modAplicaDiplomado) {
        this.modAplicaDiplomado = modAplicaDiplomado;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modPk != null ? modPk.hashCode() : 0);
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
        final SgModalidad other = (SgModalidad) obj;
        if (!Objects.equals(this.modPk, other.modPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesModalidad[ modPk=" + modPk + " ]";
    }

}
