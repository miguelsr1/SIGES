/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

@Entity
@Table(name = "sg_reglas_contextos", schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "recPk", scope = SgReglaContexto.class)
@Audited
public class SgReglaContexto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rec_pk")
    private Long recPk;

    @Column(name = "rec_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime recUltModFecha;

    @Size(max = 45)
    @Column(name = "rec_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String recUltModUsuario;

    @Column(name = "rec_version")
    @Version
    private Integer recVersion;

    @Column(name = "rec_nombre")
    private String recNombre;
    
    @Column(name = "rec_regla")
    private String recRegla;

    public SgReglaContexto() {
    }

    public SgReglaContexto(Long recPk) {
        this.recPk = recPk;
    }

    public Long getRecPk() {
        return recPk;
    }

    public void setRecPk(Long recPk) {
        this.recPk = recPk;
    }

    public LocalDateTime getRecUltModFecha() {
        return recUltModFecha;
    }

    public void setRecUltModFecha(LocalDateTime recUltModFecha) {
        this.recUltModFecha = recUltModFecha;
    }

    public String getRecUltModUsuario() {
        return recUltModUsuario;
    }

    public void setRecUltModUsuario(String recUltModUsuario) {
        this.recUltModUsuario = recUltModUsuario;
    }

    public Integer getRecVersion() {
        return recVersion;
    }

    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public String getRecNombre() {
        return recNombre;
    }

    public void setRecNombre(String recNombre) {
        this.recNombre = recNombre;
    }

    public String getRecRegla() {
        return recRegla;
    }

    public void setRecRegla(String recRegla) {
        this.recRegla = recRegla;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.recPk);
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
        final SgReglaContexto other = (SgReglaContexto) obj;
        if (!Objects.equals(this.recPk, other.recPk)) {
            return false;
        }
        return true;
    }


    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgReglaContexto[ recPk=" + recPk + " ]";
    }

}
