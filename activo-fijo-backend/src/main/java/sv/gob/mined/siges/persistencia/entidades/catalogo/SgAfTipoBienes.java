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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_tipo_bienes", uniqueConstraints = {
    @UniqueConstraint(name = "tbi_codigo_uk", columnNames = {"tbi_codigo"})
    ,
    @UniqueConstraint(name = "tbi_nombre_uk", columnNames = {"tbi_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tbiPk", resolver = JsonIdentityResolver.class,scope = SgAfTipoBienes.class)
public class SgAfTipoBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tbi_pk", nullable = false)
    private Long tbiPk;

    @Size(max = 4)
    @Column(name = "tbi_codigo", length = 4)
    @AtributoCodigo
    private String tbiCodigo;

    @Size(max = 255)
    @Column(name = "tbi_nombre", length = 255)
    @AtributoNormalizable
    private String tbiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tbi_nombre_busqueda", length = 255)
    private String tbiNombreBusqueda;
            
    @Column(name = "tbi_habilitado")
    @AtributoHabilitado
    private Boolean tbiHabilitado;
    
    @JoinColumn(name = "tbi_categoria_bienes_fk", referencedColumnName = "cab_pk")
    @ManyToOne
    private SgAfCategoriaBienes tbiCategoriaBienes;
    
    @Column(name = "tbi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tbiUltModFecha;

    @Size(max = 45)
    @Column(name = "tbi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tbiUltModUsuario;

    @Column(name = "tbi_version")
    @Version
    private Integer tbiVersion;

    public SgAfTipoBienes() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tbiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tbiNombre);
    }

    public Long getTbiPk() {
        return tbiPk;
    }

    public void setTbiPk(Long tbiPk) {
        this.tbiPk = tbiPk;
    }

    public String getTbiCodigo() {
        return tbiCodigo;
    }

    public void setTbiCodigo(String tbiCodigo) {
        this.tbiCodigo = tbiCodigo;
    }

    public String getTbiNombre() {
        return tbiNombre;
    }

    public void setTbiNombre(String tbiNombre) {
        this.tbiNombre = tbiNombre;
    }

    public String getTbiNombreBusqueda() {
        return tbiNombreBusqueda;
    }

    public void setTbiNombreBusqueda(String tbiNombreBusqueda) {
        this.tbiNombreBusqueda = tbiNombreBusqueda;
    }

    public Boolean getTbiHabilitado() {
        return tbiHabilitado;
    }

    public void setTbiHabilitado(Boolean tbiHabilitado) {
        this.tbiHabilitado = tbiHabilitado;
    }

    public SgAfCategoriaBienes getTbiCategoriaBienes() {
        return tbiCategoriaBienes;
    }

    public void setTbiCategoriaBienes(SgAfCategoriaBienes tbiCategoriaBienes) {
        this.tbiCategoriaBienes = tbiCategoriaBienes;
    }

    public LocalDateTime getTbiUltModFecha() {
        return tbiUltModFecha;
    }

    public void setTbiUltModFecha(LocalDateTime tbiUltModFecha) {
        this.tbiUltModFecha = tbiUltModFecha;
    }

    public String getTbiUltModUsuario() {
        return tbiUltModUsuario;
    }

    public void setTbiUltModUsuario(String tbiUltModUsuario) {
        this.tbiUltModUsuario = tbiUltModUsuario;
    }

    public Integer getTbiVersion() {
        return tbiVersion;
    }

    public void setTbiVersion(Integer tbiVersion) {
        this.tbiVersion = tbiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.tbiPk);
        hash = 31 * hash + Objects.hashCode(this.tbiCodigo);
        hash = 31 * hash + Objects.hashCode(this.tbiNombre);
        hash = 31 * hash + Objects.hashCode(this.tbiNombreBusqueda);
        hash = 31 * hash + Objects.hashCode(this.tbiHabilitado);
        hash = 31 * hash + Objects.hashCode(this.tbiCategoriaBienes);
        hash = 31 * hash + Objects.hashCode(this.tbiUltModFecha);
        hash = 31 * hash + Objects.hashCode(this.tbiUltModUsuario);
        hash = 31 * hash + Objects.hashCode(this.tbiVersion);
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
        final SgAfTipoBienes other = (SgAfTipoBienes) obj;
        if (!Objects.equals(this.tbiPk, other.tbiPk)) {
            return false;
        }
        return true;
    }
 
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgTipoBienes[ tbiPk =" + tbiPk + " ]";
    }

}
