package sv.gob.mined.siges.persistencia.entidades;


/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_textos", schema = Constantes.SCHEMA_PFEA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "texPk", scope = SgTextoPfea.class)
public class SgTextoPfea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tex_id", nullable = false)
    private Long texPk;

    @Column(name = "tex_codigo")
    @AtributoCodigo
    private String texCodigo;
    
    @Column(name = "tex_valor")
    private String texValor;

    @Column(name = "tex_ultima_modificacion")
    @AtributoUltimaModificacion
    private LocalDateTime texUltModFecha;

    @Size(max = 45)
    @Column(name = "tex_ultimo_usuario", length = 45)
    @AtributoUltimoUsuario
    private String texUltModUsuario;

    @Column(name = "tex_version")
    @Version
    private Integer texVersion;
    


    public SgTextoPfea() {
    }

    public Long getTexPk() {
        return texPk;
    }

    public void setTexPk(Long texPk) {
        this.texPk = texPk;
    }

    public String getTexCodigo() {
        return texCodigo;
    }

    public void setTexCodigo(String texCodigo) {
        this.texCodigo = texCodigo;
    }

    public String getTexValor() {
        return texValor;
    }

    public void setTexValor(String texValor) {
        this.texValor = texValor;
    }

    public LocalDateTime getTexUltModFecha() {
        return texUltModFecha;
    }

    public void setTexUltModFecha(LocalDateTime texUltModFecha) {
        this.texUltModFecha = texUltModFecha;
    }

    public String getTexUltModUsuario() {
        return texUltModUsuario;
    }

    public void setTexUltModUsuario(String texUltModUsuario) {
        this.texUltModUsuario = texUltModUsuario;
    }

    public Integer getTexVersion() {
        return texVersion;
    }

    public void setTexVersion(Integer texVersion) {
        this.texVersion = texVersion;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.texPk);
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
        final SgTextoPfea other = (SgTextoPfea) obj;
        if (!Objects.equals(this.texPk, other.texPk)) {
            return false;
        }
        return true;
    }


   
    
    

}

