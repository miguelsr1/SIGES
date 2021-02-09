/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_municipios", uniqueConstraints = {
    @UniqueConstraint(name = "mun_codigo_nombre_uk", columnNames = {"mun_codigo", "mun_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "munPk", scope = SgMunicipio.class)
public class SgMunicipio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mun_pk", nullable = false)
    private Long munPk;

    @JoinColumn(name = "mun_departamento_fk", referencedColumnName = "dep_pk")
    @ManyToOne(optional = false)
    private SgDepartamento munDepartamento;

    @OneToMany(mappedBy = "canMunicipioFk")
    @JsonIgnore
    private List<SgCanton> munCantones;

    @Size(max = 4)
    @Column(name = "mun_codigo", length = 4)
    @AtributoCodigo
    private String munCodigo;

    @Column(name = "mun_habilitado")
    @AtributoHabilitado
    private Boolean munHabilitado;

    @Size(max = 255)
    @Column(name = "mun_nombre", length = 255)
    @AtributoNormalizable
    private String munNombre;

    @Size(max = 255)
    @Column(name = "mun_nombre_busqueda", length = 255)
    @AtributoNombre
    private String munNombreBusqueda;

    @Column(name = "mun_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime munUltModFecha;

    @Size(max = 45)
    @Column(name = "mun_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String munUltModUsuario;

    @Column(name = "mun_version")
    @Version
    private Integer munVersion;

    public SgMunicipio() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.munNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.munNombre);
    }

    public Long getMunPk() {
        return munPk;
    }

    public void setMunPk(Long munPk) {
        this.munPk = munPk;
    }

    public SgDepartamento getMunDepartamento() {
        return munDepartamento;
    }

    public void setMunDepartamento(SgDepartamento munDepartamento) {
        this.munDepartamento = munDepartamento;
    }


    public String getMunCodigo() {
        return munCodigo;
    }

    public void setMunCodigo(String munCodigo) {
        this.munCodigo = munCodigo;
    }

    public Boolean getMunHabilitado() {
        return munHabilitado;
    }

    public void setMunHabilitado(Boolean munHabilitado) {
        this.munHabilitado = munHabilitado;
    }

    public String getMunNombre() {
        return munNombre;
    }

    public void setMunNombre(String munNombre) {
        this.munNombre = munNombre;
    }

    public String getMunNombreBusqueda() {
        return munNombreBusqueda;
    }

    public void setMunNombreBusqueda(String munNombreBusqueda) {
        this.munNombreBusqueda = munNombreBusqueda;
    }

    public LocalDateTime getMunUltModFecha() {
        return munUltModFecha;
    }

    public void setMunUltModFecha(LocalDateTime munUltModFecha) {
        this.munUltModFecha = munUltModFecha;
    }

    public String getMunUltModUsuario() {
        return munUltModUsuario;
    }

    public void setMunUltModUsuario(String munUltModUsuario) {
        this.munUltModUsuario = munUltModUsuario;
    }

    public Integer getMunVersion() {
        return munVersion;
    }

    public void setMunVersion(Integer munVersion) {
        this.munVersion = munVersion;
    }

    public List<SgCanton> getMunCantones() {
        return munCantones;
    }

    public void setMunCantones(List<SgCanton> munCantones) {
        this.munCantones = munCantones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (munPk != null ? munPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMunicipio)) {
            return false;
        }
        SgMunicipio other = (SgMunicipio) object;
        if ((this.munPk == null && other.munPk != null) || (this.munPk != null && !this.munPk.equals(other.munPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesMunicipios[ munPk=" + munPk + " ]";
    }
}
