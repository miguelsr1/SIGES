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
@Table(name = "sg_elementos_hogar", schema = Constantes.SCHEMA_CATALOGO, uniqueConstraints = {
    @UniqueConstraint(name = "eho_codigo_uk", columnNames = {"eho_codigo"})
    ,
    @UniqueConstraint(name = "eho_nombre_uk", columnNames = {"eho_nombre"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ehoPk", scope = SgElementoHogar.class)
@Audited
public class SgElementoHogar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eho_pk", nullable = false)
    private Long ehoPk;

    @Size(max = 45)
    @Column(name = "eho_codigo", length = 45)
    @AtributoCodigo
    private String ehoCodigo;

    @Size(max = 255)
    @Column(name = "eho_nombre", length = 255)
    @AtributoNormalizable
    private String ehoNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "eho_nombre_busqueda", length = 255)
    private String ehoNombreBusqueda;

    @Column(name = "eho_habilitado")
    @AtributoHabilitado
    private Boolean ehoHabilitado;

    @Column(name = "eho_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ehoUltModFecha;

    @Size(max = 45)
    @Column(name = "eho_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ehoUltModUsuario;

    @Column(name = "eho_version")
    @Version
    private Integer ehoVersion;

    public SgElementoHogar() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ehoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ehoNombre);
    }

    public Long getEhoPk() {
        return ehoPk;
    }

    public void setEhoPk(Long ehoPk) {
        this.ehoPk = ehoPk;
    }

    public String getEhoCodigo() {
        return ehoCodigo;
    }

    public void setEhoCodigo(String ehoCodigo) {
        this.ehoCodigo = ehoCodigo;
    }

    public String getEhoNombre() {
        return ehoNombre;
    }

    public void setEhoNombre(String ehoNombre) {
        this.ehoNombre = ehoNombre;
    }

    public String getEhoNombreBusqueda() {
        return ehoNombreBusqueda;
    }

    public void setEhoNombreBusqueda(String ehoNombreBusqueda) {
        this.ehoNombreBusqueda = ehoNombreBusqueda;
    }

    public Boolean getEhoHabilitado() {
        return ehoHabilitado;
    }

    public void setEhoHabilitado(Boolean ehoHabilitado) {
        this.ehoHabilitado = ehoHabilitado;
    }

    public LocalDateTime getEhoUltModFecha() {
        return ehoUltModFecha;
    }

    public void setEhoUltModFecha(LocalDateTime ehoUltModFecha) {
        this.ehoUltModFecha = ehoUltModFecha;
    }

    public String getEhoUltModUsuario() {
        return ehoUltModUsuario;
    }

    public void setEhoUltModUsuario(String ehoUltModUsuario) {
        this.ehoUltModUsuario = ehoUltModUsuario;
    }

    public Integer getEhoVersion() {
        return ehoVersion;
    }

    public void setEhoVersion(Integer ehoVersion) {
        this.ehoVersion = ehoVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ehoPk);
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
        final SgElementoHogar other = (SgElementoHogar) obj;
        if (!Objects.equals(this.ehoPk, other.ehoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgElementoHogar{" + "ehoPk=" + ehoPk + ", ehoCodigo=" + ehoCodigo + ", ehoNombre=" + ehoNombre + ", ehoNombreBusqueda=" + ehoNombreBusqueda + ", ehoHabilitado=" + ehoHabilitado + ", ehoUltModFecha=" + ehoUltModFecha + ", ehoUltModUsuario=" + ehoUltModUsuario + ", ehoVersion=" + ehoVersion + '}';
    }
    
    

}
