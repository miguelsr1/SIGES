/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
@Table(name = "sg_trastornos_aprendizaje", uniqueConstraints = {
    @UniqueConstraint(name = "tra_codigo_uk", columnNames = {"tra_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "traPk", scope = SgTrastornoAprendizaje.class)
public class SgTrastornoAprendizaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tra_pk")
    private Long traPk;

    @Size(max = 4)
    @Column(name = "tra_codigo", length = 4)
    @AtributoCodigo
    private String traCodigo;

    @Size(max = 255)
    @Column(name = "tra_nombre", length = 255)
    @AtributoNormalizable
    private String traNombre;

    @Size(max = 255)
    @Column(name = "tra_nombre_busqueda", length = 255)
    @AtributoNombre
    private String traNombreBusqueda;
            
    @Column(name = "tra_habilitado")
    @AtributoHabilitado
    private Boolean traHabilitado;

    @Column(name = "tra_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime traUltModFecha;

    @Size(max = 45)
    @Column(name = "tra_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String traUltModUsuario;

    @Column(name = "tra_version")
    @Version
    private Integer traVersion;

    public SgTrastornoAprendizaje() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.traNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.traNombre);
    }

    public String getTraNombreBusqueda() {
        return traNombreBusqueda;
    }

    public void setTraNombreBusqueda(String traNombreBusqueda) {
        this.traNombreBusqueda = traNombreBusqueda;
    }

    public Long getTraPk() {
        return traPk;
    }

    public void setTraPk(Long traPk) {
        this.traPk = traPk;
    }

    public String getTraCodigo() {
        return traCodigo;
    }

    public void setTraCodigo(String traCodigo) {
        this.traCodigo = traCodigo;
    }

    public String getTraNombre() {
        return traNombre;
    }

    public void setTraNombre(String traNombre) {
        this.traNombre = traNombre;
    }

    public Boolean getTraHabilitado() {
        return traHabilitado;
    }

    public void setTraHabilitado(Boolean traHabilitado) {
        this.traHabilitado = traHabilitado;
    }

    public LocalDateTime getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(LocalDateTime traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traPk != null ? traPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTrastornoAprendizaje)) {
            return false;
        }
        SgTrastornoAprendizaje other = (SgTrastornoAprendizaje) object;
        if ((this.traPk == null && other.traPk != null) || (this.traPk != null && !this.traPk.equals(other.traPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTrastornoAprendizaje[ traPk=" + traPk + " ]";
    }

}
