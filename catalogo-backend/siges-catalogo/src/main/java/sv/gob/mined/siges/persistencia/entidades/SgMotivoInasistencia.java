/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_motivos_inasistencia", uniqueConstraints = {
    @UniqueConstraint(name = "min_codigo_uk", columnNames = {"min_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "minPk", scope = SgMotivoInasistencia.class)
public class SgMotivoInasistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "min_pk")
    private Long minPk;

    @Size(max = 4)
    @Column(name = "min_codigo", length = 4)
    @AtributoCodigo
    private String minCodigo;

    @Size(max = 255)
    @Column(name = "min_nombre", length = 255)
    @AtributoNormalizable
    private String minNombre;

    @Size(max = 255)
    @Column(name = "min_nombre_busqueda", length = 255)
    @AtributoNombre
    private String minNombreBusqueda;

    @Column(name = "min_habilitado")
    @AtributoHabilitado
    private Boolean minHabilitado;
            
    @Column(name = "min_motivo_justificado")
    private Boolean minMotivoJustificado;

    @Column(name = "min_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime minUltModFecha;

    @Size(max = 45)
    @Column(name = "min_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String minUltModUsuario;

    @Column(name = "min_version")
    @Version
    private Integer minVersion;

    public SgMotivoInasistencia() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.minNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.minNombre);
    }

    public String getMinNombreBusqueda() {
        return minNombreBusqueda;
    }

    public void setMinNombreBusqueda(String minNombreBusqueda) {
        this.minNombreBusqueda = minNombreBusqueda;
    }

    public Long getMinPk() {
        return minPk;
    }

    public void setMinPk(Long minPk) {
        this.minPk = minPk;
    }

    public String getMinCodigo() {
        return minCodigo;
    }

    public void setMinCodigo(String minCodigo) {
        this.minCodigo = minCodigo;
    }

    public String getMinNombre() {
        return minNombre;
    }

    public void setMinNombre(String minNombre) {
        this.minNombre = minNombre;
    }

    public Boolean getMinHabilitado() {
        return minHabilitado;
    }

    public Boolean getMinMotivoJustificado() {
        return minMotivoJustificado;
    }

    public void setMinMotivoJustificado(Boolean minMotivoJustificado) {
        this.minMotivoJustificado = minMotivoJustificado;
    }

    public void setMinHabilitado(Boolean minHabilitado) {
        this.minHabilitado = minHabilitado;
    }

    public LocalDateTime getMinUltModFecha() {
        return minUltModFecha;
    }

    public void setMinUltModFecha(LocalDateTime minUltModFecha) {
        this.minUltModFecha = minUltModFecha;
    }

    public String getMinUltModUsuario() {
        return minUltModUsuario;
    }

    public void setMinUltModUsuario(String minUltModUsuario) {
        this.minUltModUsuario = minUltModUsuario;
    }

    public Integer getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(Integer minVersion) {
        this.minVersion = minVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (minPk != null ? minPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMotivoInasistencia)) {
            return false;
        }
        SgMotivoInasistencia other = (SgMotivoInasistencia) object;
        if ((this.minPk == null && other.minPk != null) || (this.minPk != null && !this.minPk.equals(other.minPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesMotivoInasistencia[ minPk=" + minPk + " ]";
    }

}
