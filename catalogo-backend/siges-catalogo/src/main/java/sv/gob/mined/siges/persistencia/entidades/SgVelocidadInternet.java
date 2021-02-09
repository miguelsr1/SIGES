/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
@Table(name = "sg_velocidades_internet", uniqueConstraints = {
    @UniqueConstraint(name = "vin_codigo_uk", columnNames = {"vin_codigo"})
    ,
    @UniqueConstraint(name = "vin_nombre_uk", columnNames = {"vin_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "vinPk", scope = SgVelocidadInternet.class)
@Audited
public class SgVelocidadInternet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "vin_pk", nullable = false)
    private Long vinPk;

    @Size(max = 45)
    @Column(name = "vin_codigo", length = 45)
    @AtributoCodigo
    private String vinCodigo;

    @Size(max = 255)
    @Column(name = "vin_nombre", length = 255)
    @AtributoNormalizable
    private String vinNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "vin_nombre_busqueda", length = 255)
    private String vinNombreBusqueda;

    @Column(name = "vin_habilitado")
    @AtributoHabilitado
    private Boolean vinHabilitado;

    @Column(name = "vin_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime vinUltModFecha;

    @Size(max = 45)
    @Column(name = "vin_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String vinUltModUsuario;

    @Column(name = "vin_version")
    @Version
    private Integer vinVersion;

    public SgVelocidadInternet() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.vinNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.vinNombre);
    }

    public Long getVinPk() {
        return vinPk;
    }

    public void setVinPk(Long vinPk) {
        this.vinPk = vinPk;
    }

    public String getVinCodigo() {
        return vinCodigo;
    }

    public void setVinCodigo(String vinCodigo) {
        this.vinCodigo = vinCodigo;
    }

    public String getVinNombre() {
        return vinNombre;
    }

    public void setVinNombre(String vinNombre) {
        this.vinNombre = vinNombre;
    }

    public String getVinNombreBusqueda() {
        return vinNombreBusqueda;
    }

    public void setVinNombreBusqueda(String vinNombreBusqueda) {
        this.vinNombreBusqueda = vinNombreBusqueda;
    }

    public Boolean getVinHabilitado() {
        return vinHabilitado;
    }

    public void setVinHabilitado(Boolean vinHabilitado) {
        this.vinHabilitado = vinHabilitado;
    }

    public LocalDateTime getVinUltModFecha() {
        return vinUltModFecha;
    }

    public void setVinUltModFecha(LocalDateTime vinUltModFecha) {
        this.vinUltModFecha = vinUltModFecha;
    }

    public String getVinUltModUsuario() {
        return vinUltModUsuario;
    }

    public void setVinUltModUsuario(String vinUltModUsuario) {
        this.vinUltModUsuario = vinUltModUsuario;
    }

    public Integer getVinVersion() {
        return vinVersion;
    }

    public void setVinVersion(Integer vinVersion) {
        this.vinVersion = vinVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.vinPk);
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
        final SgVelocidadInternet other = (SgVelocidadInternet) obj;
        if (!Objects.equals(this.vinPk, other.vinPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgVelocidadInternet{" + "vinPk=" + vinPk + ", vinCodigo=" + vinCodigo + ", vinNombre=" + vinNombre + ", vinNombreBusqueda=" + vinNombreBusqueda + ", vinHabilitado=" + vinHabilitado + ", vinUltModFecha=" + vinUltModFecha + ", vinUltModUsuario=" + vinUltModUsuario + ", vinVersion=" + vinVersion + '}';
    }
    
    

}
