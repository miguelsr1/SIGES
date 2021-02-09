/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_areas_inversion", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "adiPk", scope = SgAreaInversion.class)
/**
 * Entidad correspondiente a las áreas de inversión.
 */
public class SgAreaInversion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ai_id", nullable = false)
    private Long adiPk;

    @Size(max = 45)
    @Column(name = "ai_codigo", length = 45)
    private String adiCodigo;

    @Size(max = 255)
    @Column(name = "ai_nombre", length = 255)
    private String adiNombre;

    @Size(max = 255)
    @Column(name = "ai_descripcion", length = 255)
    private String adiDescripcion;

    @Column(name = "ai_habilitado")
    private Boolean adiHabilitado;

    @Column(name = "ai_ult_mod")
    @AtributoUltimaModificacion
    private LocalDateTime adiUltModFecha;

    @Size(max = 45)
    @Column(name = "ai_ult_usuario", length = 45)
    @AtributoUltimoUsuario
    private String adiUltModUsuario;

    @Column(name = "ai_version")
    @Version
    private Integer adiVersion;

    @JoinColumn(name = "ai_area_padre", referencedColumnName = "ai_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgAreaInversion adiPadrePk;

    @Column(name = "ai_aplica_plan_compras")
    private Boolean adiAplicaPlanCompras;

    public Long getAdiPk() {
        return adiPk;
    }

    public void setAdiPk(Long adiPk) {
        this.adiPk = adiPk;
    }

    public String getAdiCodigo() {
        return adiCodigo;
    }

    public void setAdiCodigo(String adiCodigo) {
        this.adiCodigo = adiCodigo;
    }

    public String getAdiNombre() {
        return adiNombre;
    }

    public void setAdiNombre(String adiNombre) {
        this.adiNombre = adiNombre;
    }

    public Boolean getAdiHabilitado() {
        return adiHabilitado;
    }

    public void setAdiHabilitado(Boolean adiHabilitado) {
        this.adiHabilitado = adiHabilitado;
    }

    public LocalDateTime getAdiUltModFecha() {
        return adiUltModFecha;
    }

    public void setAdiUltModFecha(LocalDateTime adiUltModFecha) {
        this.adiUltModFecha = adiUltModFecha;
    }

    public String getAdiUltModUsuario() {
        return adiUltModUsuario;
    }

    public void setAdiUltModUsuario(String adiUltModUsuario) {
        this.adiUltModUsuario = adiUltModUsuario;
    }

    public Integer getAdiVersion() {
        return adiVersion;
    }

    public void setAdiVersion(Integer adiVersion) {
        this.adiVersion = adiVersion;
    }

    public String getAdiDescripcion() {
        return adiDescripcion;
    }

    public void setAdiDescripcion(String adiDescripcion) {
        this.adiDescripcion = adiDescripcion;
    }

    public SgAreaInversion getAdiPadrePk() {
        return adiPadrePk;
    }

    public void setAdiPadrePk(SgAreaInversion adiPadrePk) {
        this.adiPadrePk = adiPadrePk;
    }

    public Boolean getAdiAplicaPlanCompras() {
        return adiAplicaPlanCompras;
    }

    public void setAdiAplicaPlanCompras(Boolean adiAplicaPlanCompras) {
        this.adiAplicaPlanCompras = adiAplicaPlanCompras;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.adiPk);
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
        final SgAreaInversion other = (SgAreaInversion) obj;
        if (!Objects.equals(this.adiPk, other.adiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAreaInversion{" + "adiPk=" + adiPk + '}';
    }

}
