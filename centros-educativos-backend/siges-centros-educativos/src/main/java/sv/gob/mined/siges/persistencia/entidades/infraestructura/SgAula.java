/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.infraestructura;

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
import java.util.Objects;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aulPk", scope = SgAula.class)
@Audited
public class SgAula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aul_pk", nullable = false)
    private Long aulPk;

    @Size(max = 45)
    @Column(name = "aul_codigo", length = 45)
    @AtributoCodigo
    private String aulCodigo;

    @Size(max = 255)
    @Column(name = "aul_nombre", length = 255)
    @AtributoNormalizable
    private String aulNombre;
    

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


    public SgAula() {
    }

    public SgAula(Long aulPk, Integer aulVersion) {
        this.aulPk = aulPk;
        this.aulVersion = aulVersion;
    }   
    

    public Long getAulPk() {
        return aulPk;
    }

    public void setAulPk(Long aulPk) {
        this.aulPk = aulPk;
    }

    public String getAulCodigo() {
        return aulCodigo;
    }

    public void setAulCodigo(String aulCodigo) {
        this.aulCodigo = aulCodigo;
    }

    public String getAulNombre() {
        return aulNombre;
    }

    public void setAulNombre(String aulNombre) {
        this.aulNombre = aulNombre;
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
        final SgAula other = (SgAula) obj;
        if (!Objects.equals(this.aulPk, other.aulPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAula{" + "aulPk=" + aulPk + ", aulCodigo=" + aulCodigo + ", aulNombre=" + aulNombre + ", aulUltModFecha=" + aulUltModFecha + ", aulUltModUsuario=" + aulUltModUsuario + ", aulVersion=" + aulVersion + '}';
    }

 
    
  
    

}
