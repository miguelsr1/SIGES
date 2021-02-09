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
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_vulnerabilidades", uniqueConstraints = {
    @UniqueConstraint(name = "vul_codigo_uk", columnNames = {"vul_codigo"})
    ,
    @UniqueConstraint(name = "vul_nombre_uk", columnNames = {"vul_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "vulPk", scope = SgVulnerabilidades.class)
public class SgVulnerabilidades implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "vul_pk", nullable = false)
    private Long vulPk;

    @Size(max = 4)
    @Column(name = "vul_codigo", length = 4)
    @AtributoCodigo
    private String vulCodigo;

    @Size(max = 255)
    @Column(name = "vul_nombre", length = 255)
    @AtributoNormalizable
    private String vulNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "vul_nombre_busqueda", length = 255)
    private String vulNombreBusqueda;

    @Column(name = "vul_habilitado")
    @AtributoHabilitado
    private Boolean vulHabilitado;

    @Column(name = "vul_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime vulUltModFecha;

    @Size(max = 45)
    @Column(name = "vul_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String vulUltModUsuario;

    @Column(name = "vul_version")
    @Version
    private Integer vulVersion;

    public SgVulnerabilidades() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.vulNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.vulNombre);
    }

    public Long getVulPk() {
        return vulPk;
    }

    public void setVulPk(Long vulPk) {
        this.vulPk = vulPk;
    }

    public String getVulCodigo() {
        return vulCodigo;
    }

    public void setVulCodigo(String vulCodigo) {
        this.vulCodigo = vulCodigo;
    }

    public String getVulNombre() {
        return vulNombre;
    }

    public void setVulNombre(String vulNombre) {
        this.vulNombre = vulNombre;
    }

    public String getVulNombreBusqueda() {
        return vulNombreBusqueda;
    }

    public void setVulNombreBusqueda(String vulNombreBusqueda) {
        this.vulNombreBusqueda = vulNombreBusqueda;
    }

    public Boolean getVulHabilitado() {
        return vulHabilitado;
    }

    public void setVulHabilitado(Boolean vulHabilitado) {
        this.vulHabilitado = vulHabilitado;
    }

    public LocalDateTime getVulUltModFecha() {
        return vulUltModFecha;
    }

    public void setVulUltModFecha(LocalDateTime vulUltModFecha) {
        this.vulUltModFecha = vulUltModFecha;
    }

    public String getVulUltModUsuario() {
        return vulUltModUsuario;
    }

    public void setVulUltModUsuario(String vulUltModUsuario) {
        this.vulUltModUsuario = vulUltModUsuario;
    }

    public Integer getVulVersion() {
        return vulVersion;
    }

    public void setVulVersion(Integer vulVersion) {
        this.vulVersion = vulVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.vulPk);
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
        final SgVulnerabilidades other = (SgVulnerabilidades) obj;
        if (!Objects.equals(this.vulPk, other.vulPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgVulnerabilidades[ vulPk=" + vulPk + " ]";
    }

}