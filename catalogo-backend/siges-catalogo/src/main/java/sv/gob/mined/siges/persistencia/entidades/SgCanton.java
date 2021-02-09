/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "sg_cantones", uniqueConstraints = {
    @UniqueConstraint(name = "can_codigo_nombre_uk", columnNames = {"can_codigo", "can_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "canPk", scope = SgCanton.class)
public class SgCanton implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "can_pk", nullable = false)
    private Long canPk;

    @JoinColumn(name = "can_municipio_fk", referencedColumnName = "mun_pk")
    @ManyToOne(optional = false)
    private SgMunicipio canMunicipio;

    @OneToMany(mappedBy = "casCanton")
    @JsonIgnore
    private List<SgCaserio> canCaserios;

    @Size(max = 6)
    @Column(name = "can_codigo", length = 6)
    @AtributoCodigo
    private String canCodigo;

    @Column(name = "can_habilitado")
    @AtributoHabilitado
    private Boolean canHabilitado;

    @Size(max = 255)
    @Column(name = "can_nombre", length = 255)
    @AtributoNormalizable
    private String canNombre;

    @Size(max = 255)
    @Column(name = "can_nombre_busqueda", length = 255)
    @AtributoNombre
    private String canNombreBusqueda;

    @Column(name = "can_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime canUltModFecha;

    @Size(max = 45)
    @Column(name = "can_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String canUltModUsuario;

    @Column(name = "can_version")
    @Version
    private Integer canVersion;

    public SgCanton() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.canNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.canNombre);
    }

    public Long getCanPk() {
        return canPk;
    }

    public void setCanPk(Long canPk) {
        this.canPk = canPk;
    }

    public SgMunicipio getCanMunicipio() {
        return canMunicipio;
    }

    public void setCanMunicipio(SgMunicipio canMunicipio) {
        this.canMunicipio = canMunicipio;
    }

    public List<SgCaserio> getCanCaserios() {
        return canCaserios;
    }

    public void setCanCaserios(List<SgCaserio> canCaserios) {
        this.canCaserios = canCaserios;
    }

    public String getCanCodigo() {
        return canCodigo;
    }

    public void setCanCodigo(String canCodigo) {
        this.canCodigo = canCodigo;
    }

    public Boolean getCanHabilitado() {
        return canHabilitado;
    }

    public void setCanHabilitado(Boolean canHabilitado) {
        this.canHabilitado = canHabilitado;
    }

    public String getCanNombre() {
        return canNombre;
    }

    public void setCanNombre(String canNombre) {
        this.canNombre = canNombre;
    }

    public String getCanNombreBusqueda() {
        return canNombreBusqueda;
    }

    public void setCanNombreBusqueda(String canNombreBusqueda) {
        this.canNombreBusqueda = canNombreBusqueda;
    }

    public LocalDateTime getCanUltModFecha() {
        return canUltModFecha;
    }

    public void setCanUltModFecha(LocalDateTime canUltModFecha) {
        this.canUltModFecha = canUltModFecha;
    }

    public String getCanUltModUsuario() {
        return canUltModUsuario;
    }

    public void setCanUltModUsuario(String canUltModUsuario) {
        this.canUltModUsuario = canUltModUsuario;
    }

    public Integer getCanVersion() {
        return canVersion;
    }

    public void setCanVersion(Integer canVersion) {
        this.canVersion = canVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (canPk != null ? canPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCanton)) {
            return false;
        }
        SgCanton other = (SgCanton) object;
        if ((this.canPk == null && other.canPk != null) || (this.canPk != null && !this.canPk.equals(other.canPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesCanton{" + "canPk=" + canPk + '}';
    }

}
