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
@Table(name = "sg_cargo_titular", uniqueConstraints = {
    @UniqueConstraint(name = "cti_codigo_uk", columnNames = {"cti_codigo"})
    ,
    @UniqueConstraint(name = "cti_nombre_uk", columnNames = {"cti_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ctiPk", scope = SgCargoTitular.class)
@Audited
public class SgCargoTitular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cti_pk", nullable = false)
    private Long ctiPk;

    @Size(max = 45)
    @Column(name = "cti_codigo", length = 45)
    @AtributoCodigo
    private String ctiCodigo;

    @Size(max = 255)
    @Column(name = "cti_nombre", length = 255)
    @AtributoNormalizable
    private String ctiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cti_nombre_busqueda", length = 255)
    private String ctiNombreBusqueda;

    @Column(name = "cti_habilitado")
    @AtributoHabilitado
    private Boolean ctiHabilitado;
    
    @Column(name = "cti_es_titular")
    private Boolean ctiEsTitular;

    @Column(name = "cti_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ctiUltModFecha;

    @Size(max = 45)
    @Column(name = "cti_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ctiUltModUsuario;

    @Column(name = "cti_version")
    @Version
    private Integer ctiVersion;

    public SgCargoTitular() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ctiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ctiNombre);
    }

    public Long getCtiPk() {
        return ctiPk;
    }

    public void setCtiPk(Long ctiPk) {
        this.ctiPk = ctiPk;
    }

    public String getCtiCodigo() {
        return ctiCodigo;
    }

    public void setCtiCodigo(String ctiCodigo) {
        this.ctiCodigo = ctiCodigo;
    }

    public String getCtiNombre() {
        return ctiNombre;
    }

    public void setCtiNombre(String ctiNombre) {
        this.ctiNombre = ctiNombre;
    }

    public String getCtiNombreBusqueda() {
        return ctiNombreBusqueda;
    }

    public void setCtiNombreBusqueda(String ctiNombreBusqueda) {
        this.ctiNombreBusqueda = ctiNombreBusqueda;
    }

    public Boolean getCtiHabilitado() {
        return ctiHabilitado;
    }

    public void setCtiHabilitado(Boolean ctiHabilitado) {
        this.ctiHabilitado = ctiHabilitado;
    }

    public LocalDateTime getCtiUltModFecha() {
        return ctiUltModFecha;
    }

    public void setCtiUltModFecha(LocalDateTime ctiUltModFecha) {
        this.ctiUltModFecha = ctiUltModFecha;
    }

    public String getCtiUltModUsuario() {
        return ctiUltModUsuario;
    }

    public void setCtiUltModUsuario(String ctiUltModUsuario) {
        this.ctiUltModUsuario = ctiUltModUsuario;
    }

    public Integer getCtiVersion() {
        return ctiVersion;
    }

    public void setCtiVersion(Integer ctiVersion) {
        this.ctiVersion = ctiVersion;
    }

    public Boolean getCtiEsTitular() {
        return ctiEsTitular;
    }

    public void setCtiEsTitular(Boolean ctiEsTitular) {
        this.ctiEsTitular = ctiEsTitular;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ctiPk);
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
        final SgCargoTitular other = (SgCargoTitular) obj;
        if (!Objects.equals(this.ctiPk, other.ctiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCargoTitular{" + "ctiPk=" + ctiPk + ", ctiCodigo=" + ctiCodigo + ", ctiNombre=" + ctiNombre + ", ctiNombreBusqueda=" + ctiNombreBusqueda + ", ctiHabilitado=" + ctiHabilitado + ", ctiUltModFecha=" + ctiUltModFecha + ", ctiUltModUsuario=" + ctiUltModUsuario + ", ctiVersion=" + ctiVersion + '}';
    }
    
    

}
