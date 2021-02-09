/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_nie", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "niePk", scope = SgNie.class)
@Audited
public class SgNie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nie_pk", nullable = false)
    private Long niePk;

    @Basic(optional = false)
    @Column(name = "nie_fecha")
    private LocalDate nieFecha;
    

    @Column(name = "nie_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nieUltModFecha;

    @Size(max = 45)
    @Column(name = "nie_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String nieUltModUsuario;

    @Column(name = "nie_version")
    @Version
    private Integer nieVersion;

    public SgNie() {
    }


    public Long getNiePk() {
        return niePk;
    }

    public void setNiePk(Long niePk) {
        this.niePk = niePk;
    }

    public LocalDate getNieFecha() {
        return nieFecha;
    }

    public void setNieFecha(LocalDate nieFecha) {
        this.nieFecha = nieFecha;
    }
    

    public LocalDateTime getNieUltModFecha() {
        return nieUltModFecha;
    }

    public void setNieUltModFecha(LocalDateTime nieUltModFecha) {
        this.nieUltModFecha = nieUltModFecha;
    }

    public String getNieUltModUsuario() {
        return nieUltModUsuario;
    }

    public void setNieUltModUsuario(String nieUltModUsuario) {
        this.nieUltModUsuario = nieUltModUsuario;
    }

    public Integer getNieVersion() {
        return nieVersion;
    }

    public void setNieVersion(Integer nieVersion) {
        this.nieVersion = nieVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.niePk);
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
        final SgNie other = (SgNie) obj;
        if (!Objects.equals(this.niePk, other.niePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgNie{" + "niePk=" + niePk +  ", nieUltModFecha=" + nieUltModFecha + ", nieUltModUsuario=" + nieUltModUsuario + ", nieVersion=" + nieVersion + '}';
    }
    
    

}
