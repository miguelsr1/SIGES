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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgGradoAfectacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTiposRiesgo;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_sede_riesgo_afectacion",schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rarPk", scope = SgRelSedeRiesgoAfectacion.class)
@Audited
public class SgRelSedeRiesgoAfectacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rar_pk", nullable = false)
    private Long rarPk;

     
    @JoinColumn(name = "rar_tipo_riesgo_fk")
    @ManyToOne
    private SgTiposRiesgo rarTipoRiesgo;
     
     @JoinColumn(name = "rar_grado_afectacion_fk")
    @ManyToOne
    private SgGradoAfectacion rarGradoAfectacion;    

    @Column(name = "rar_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rarUltModFecha;

    @Size(max = 45)
    @Column(name = "rar_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rarUltModUsuario;

    @Column(name = "rar_version")
    @Version
    private Integer rarVersion;

    public SgRelSedeRiesgoAfectacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
   
    }

    public Long getRarPk() {
        return rarPk;
    }

    public void setRarPk(Long rarPk) {
        this.rarPk = rarPk;
    }

    public LocalDateTime getRarUltModFecha() {
        return rarUltModFecha;
    }

    public void setRarUltModFecha(LocalDateTime rarUltModFecha) {
        this.rarUltModFecha = rarUltModFecha;
    }

    public String getRarUltModUsuario() {
        return rarUltModUsuario;
    }

    public void setRarUltModUsuario(String rarUltModUsuario) {
        this.rarUltModUsuario = rarUltModUsuario;
    }

    public Integer getRarVersion() {
        return rarVersion;
    }

    public void setRarVersion(Integer rarVersion) {
        this.rarVersion = rarVersion;
    }

    public SgTiposRiesgo getRarTipoRiesgo() {
        return rarTipoRiesgo;
    }

    public void setRarTipoRiesgo(SgTiposRiesgo rarTipoRiesgo) {
        this.rarTipoRiesgo = rarTipoRiesgo;
    }

    public SgGradoAfectacion getRarGradoAfectacion() {
        return rarGradoAfectacion;
    }

    public void setRarGradoAfectacion(SgGradoAfectacion rarGradoAfectacion) {
        this.rarGradoAfectacion = rarGradoAfectacion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rarPk);
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
        final SgRelSedeRiesgoAfectacion other = (SgRelSedeRiesgoAfectacion) obj;
        if (!Objects.equals(this.rarPk, other.rarPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelSedeRiesgoAfectacion{" + "rarPk=" + rarPk + ", rarUltModFecha=" + rarUltModFecha + ", rarUltModUsuario=" + rarUltModUsuario + ", rarVersion=" + rarVersion + '}';
    }
    
    

}
