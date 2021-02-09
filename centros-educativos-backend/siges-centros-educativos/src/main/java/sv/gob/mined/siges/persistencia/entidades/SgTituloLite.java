/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_titulo", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "titPk", scope = SgTituloLite.class)
@Audited
public class SgTituloLite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tit_pk", nullable = false)
    private Long titPk;
    
  
    @Column(name = "tit_anulado")
    private Boolean titAnulada;
    
        @Column(name = "tit_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime titUltModFecha;

    @Size(max = 45)
    @Column(name = "tit_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String titUltModUsuario;

    @Column(name = "tit_version")
    @Version
    private Integer titVersion;
    
  

    public SgTituloLite() {
    }
    
   
    public Long getTitPk() {
        return titPk;
    }

    public void setTitPk(Long titPk) {
        this.titPk = titPk;
    }
    
    public Boolean getTitAnulada() {
        return titAnulada;
    }

    public void setTitAnulada(Boolean titAnulada) {
        this.titAnulada = titAnulada;
    }

    public LocalDateTime getTitUltModFecha() {
        return titUltModFecha;
    }

    public void setTitUltModFecha(LocalDateTime titUltModFecha) {
        this.titUltModFecha = titUltModFecha;
    }

    public String getTitUltModUsuario() {
        return titUltModUsuario;
    }

    public void setTitUltModUsuario(String titUltModUsuario) {
        this.titUltModUsuario = titUltModUsuario;
    }

    public Integer getTitVersion() {
        return titVersion;
    }

    public void setTitVersion(Integer titVersion) {
        this.titVersion = titVersion;
    }

    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.titPk);
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
        final SgTituloLite other = (SgTituloLite) obj;
        if (!Objects.equals(this.titPk, other.titPk)) {
            return false;
        }
        return true;
    }

}
