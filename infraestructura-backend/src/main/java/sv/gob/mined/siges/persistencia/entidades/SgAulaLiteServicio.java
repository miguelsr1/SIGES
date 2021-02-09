/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_aulas", schema=Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aulPk", scope = SgAulaLiteServicio.class)
@Audited
public class SgAulaLiteServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aul_pk", nullable = false)
    private Long aulPk;

    @Column(name = "aul_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime aulUltModFecha;

    @Size(max = 45)
    @Column(name = "aul_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String aulUltModUsuario;

    @Column(name = "aul_version")
    @Version
    private Integer aulVersion;

    
    @OneToMany(mappedBy = "rasAula", cascade = CascadeType.ALL, orphanRemoval = true)  
    private List<SgRelAulaServicio> aulRelAulaServicio;

    public SgAulaLiteServicio() {
    }


    public Long getAulPk() {
        return aulPk;
    }

    public void setAulPk(Long aulPk) {
        this.aulPk = aulPk;
    }
  
    public LocalDateTime getAulUltModFecha() {
        return aulUltModFecha;
    }

    public void setAulUltModFecha(LocalDateTime aulUltModFecha) {
        this.aulUltModFecha = aulUltModFecha;
    }

    public String getAulUltModUsuario() {
        return aulUltModUsuario;
    }

    public void setAulUltModUsuario(String aulUltModUsuario) {
        this.aulUltModUsuario = aulUltModUsuario;
    }

    public Integer getAulVersion() {
        return aulVersion;
    }

    public void setAulVersion(Integer aulVersion) {
        this.aulVersion = aulVersion;
    }


    public List<SgRelAulaServicio> getAulRelAulaServicio() {
        return aulRelAulaServicio;
    }

    public void setAulRelAulaServicio(List<SgRelAulaServicio> aulRelAulaServicio) {
        this.aulRelAulaServicio = aulRelAulaServicio;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.aulPk);
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
        final SgAulaLiteServicio other = (SgAulaLiteServicio) obj;
        if (!Objects.equals(this.aulPk, other.aulPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAula{" + "aulPk=" + aulPk + ", aulUltModFecha=" + aulUltModFecha + ", aulUltModUsuario=" + aulUltModUsuario + ", aulVersion=" + aulVersion + '}';
    }
    
    

}
