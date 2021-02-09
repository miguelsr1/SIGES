/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_terapias", uniqueConstraints = {
    @UniqueConstraint(name = "ter_codigo_uk", columnNames = {"ter_codigo"})
    ,
    @UniqueConstraint(name = "ter_nombre_uk", columnNames = {"ter_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "terPk", scope = SgTerapia.class)
@Audited
public class SgTerapia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ter_pk", nullable = false)
    private Long terPk;

    @Size(max = 45)
    @Column(name = "ter_codigo", length = 45)
    @AtributoCodigo
    private String terCodigo;

    @Size(max = 255)
    @Column(name = "ter_nombre", length = 255)
    @AtributoNormalizable
    private String terNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ter_nombre_busqueda", length = 255)
    private String terNombreBusqueda;

    @Column(name = "ter_habilitado")
    @AtributoHabilitado
    private Boolean terHabilitado;

    @Column(name = "ter_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime terUltModFecha;

    @Size(max = 45)
    @Column(name = "ter_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String terUltModUsuario;

    @Column(name = "ter_version")
    @Version
    private Integer terVersion;

    public SgTerapia() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.terNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.terNombre);
    }

    public Long getTerPk() {
        return terPk;
    }

    public void setTerPk(Long terPk) {
        this.terPk = terPk;
    }

    public String getTerCodigo() {
        return terCodigo;
    }

    public void setTerCodigo(String terCodigo) {
        this.terCodigo = terCodigo;
    }

    public String getTerNombre() {
        return terNombre;
    }

    public void setTerNombre(String terNombre) {
        this.terNombre = terNombre;
    }

    public String getTerNombreBusqueda() {
        return terNombreBusqueda;
    }

    public void setTerNombreBusqueda(String terNombreBusqueda) {
        this.terNombreBusqueda = terNombreBusqueda;
    }

    public Boolean getTerHabilitado() {
        return terHabilitado;
    }

    public void setTerHabilitado(Boolean terHabilitado) {
        this.terHabilitado = terHabilitado;
    }

    public LocalDateTime getTerUltModFecha() {
        return terUltModFecha;
    }

    public void setTerUltModFecha(LocalDateTime terUltModFecha) {
        this.terUltModFecha = terUltModFecha;
    }

    public String getTerUltModUsuario() {
        return terUltModUsuario;
    }

    public void setTerUltModUsuario(String terUltModUsuario) {
        this.terUltModUsuario = terUltModUsuario;
    }

    public Integer getTerVersion() {
        return terVersion;
    }

    public void setTerVersion(Integer terVersion) {
        this.terVersion = terVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.terPk);
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
        final SgTerapia other = (SgTerapia) obj;
        if (!Objects.equals(this.terPk, other.terPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTerapia{" + "terPk=" + terPk + ", terCodigo=" + terCodigo + ", terNombre=" + terNombre + ", terNombreBusqueda=" + terNombreBusqueda + ", terHabilitado=" + terHabilitado + ", terUltModFecha=" + terUltModFecha + ", terUltModUsuario=" + terUltModUsuario + ", terVersion=" + terVersion + '}';
    }
    
    

}
