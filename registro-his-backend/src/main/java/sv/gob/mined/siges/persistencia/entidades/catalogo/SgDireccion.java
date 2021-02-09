/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

@Entity
@Table(name = "sg_direcciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dirPk", scope = SgDireccion.class)
@Audited
public class SgDireccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dir_pk", nullable = false)
    private Long dirPk;
    
    @Size(max = 500)
    @Column(name = "dir_direccion", length = 500)
    private String dirDireccion;
    
    @JoinColumn(name = "dir_departamento")
    @ManyToOne
    private SgDepartamento dirDepartamento;
       
    @JoinColumn(name = "dir_municipio")
    @ManyToOne
    private SgMunicipio dirMunicipio;
    
    @Column(name = "dir_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dirUltModFecha;  
    
    @Size(max = 45)
    @Column(name = "dir_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dirUltModUsuario;
        
    @Column(name = "dir_version")
    @Version
    private Integer dirVersion;
      
  
    public SgDireccion() {
    }

    public Long getDirPk() {
        return dirPk;
    }

    public void setDirPk(Long dirPk) {
        this.dirPk = dirPk;
    }

    public String getDirDireccion() {
        return dirDireccion;
    }

    public void setDirDireccion(String dirDireccion) {
        this.dirDireccion = dirDireccion;
    }

    public SgDepartamento getDirDepartamento() {
        return dirDepartamento;
    }

    public void setDirDepartamento(SgDepartamento dirDepartamento) {
        this.dirDepartamento = dirDepartamento;
    }

    public LocalDateTime getDirUltModFecha() {
        return dirUltModFecha;
    }

    public void setDirUltModFecha(LocalDateTime dirUltModFecha) {
        this.dirUltModFecha = dirUltModFecha;
    }

    public String getDirUltModUsuario() {
        return dirUltModUsuario;
    }

    public void setDirUltModUsuario(String dirUltModUsuario) {
        this.dirUltModUsuario = dirUltModUsuario;
    }

    public Integer getDirVersion() {
        return dirVersion;
    }

    public void setDirVersion(Integer dirVersion) {
        this.dirVersion = dirVersion;
    }

    public SgMunicipio getDirMunicipio() {
        return dirMunicipio;
    }

    public void setDirMunicipio(SgMunicipio dirMunicipio) {
        this.dirMunicipio = dirMunicipio;
    }
     
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.dirPk);
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
        final SgDireccion other = (SgDireccion) obj;
        if (!Objects.equals(this.dirPk, other.dirPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDireccion{" + "dirPk=" + dirPk + '}';
    }

}
