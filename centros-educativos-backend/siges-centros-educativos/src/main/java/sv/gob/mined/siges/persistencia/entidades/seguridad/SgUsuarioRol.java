/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.seguridad;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_usuario_rol", schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "purPk", scope = SgUsuarioRol.class)
@Audited
public class SgUsuarioRol implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pur_pk")
    private Long purPk;
    
    @JoinColumn(name = "pur_usuario_fk", referencedColumnName = "usu_pk")
    @ManyToOne
    private SgUsuario purUsuario;
    
    @JoinColumn(name = "pur_rol_fk", referencedColumnName = "rol_pk")
    @ManyToOne
    private SgRol purRol;
    
    @JoinColumn(name = "pur_contexto_fk", referencedColumnName = "con_pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private SgContexto purContexto;
    
    @Column(name = "pur_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime purUltModFecha;
    
    @Size(max = 45)
    @Column(name = "pur_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String purUltModUsuario;
    
    @Column(name = "pur_version")
    @Version
    private Integer purVersion;

    public SgUsuarioRol() {
    }

    public SgUsuarioRol(Long purPk) {
        this.purPk = purPk;
    }

    public Long getPurPk() {
        return purPk;
    }

    public void setPurPk(Long purPk) {
        this.purPk = purPk;
    }

    public SgUsuario getPurUsuario() {
        return purUsuario;
    }

    public void setPurUsuario(SgUsuario purUsuario) {
        this.purUsuario = purUsuario;
    }

  

    public SgRol getPurRol() {
        return purRol;
    }

    public void setPurRol(SgRol purRol) {
        this.purRol = purRol;
    }

    

    public LocalDateTime getPurUltModFecha() {
        return purUltModFecha;
    }

    public void setPurUltModFecha(LocalDateTime purUltModFecha) {
        this.purUltModFecha = purUltModFecha;
    }

    public String getPurUltModUsuario() {
        return purUltModUsuario;
    }

    public void setPurUltModUsuario(String purUltModUsuario) {
        this.purUltModUsuario = purUltModUsuario;
    }

    public Integer getPurVersion() {
        return purVersion;
    }

    public void setPurVersion(Integer purVersion) {
        this.purVersion = purVersion;
    }

    public SgContexto getPurContexto() {
        return purContexto;
    }

    public void setPurContexto(SgContexto purContexto) {
        this.purContexto = purContexto;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purPk != null ? purPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgUsuarioRol)) {
            return false;
        }
        SgUsuarioRol other = (SgUsuarioRol) object;
        if ((this.purPk == null && other.purPk != null) || (this.purPk != null && !this.purPk.equals(other.purPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgUsuarioRol[ purPk=" + purPk + " ]";
    }
    
}
