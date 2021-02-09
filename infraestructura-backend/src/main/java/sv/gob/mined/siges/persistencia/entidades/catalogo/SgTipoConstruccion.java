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
@Table(name = "sg_tipo_construccion", uniqueConstraints = {
    @UniqueConstraint(name = "tco_codigo_uk", columnNames = {"tco_codigo"})
    ,
    @UniqueConstraint(name = "tco_nombre_uk", columnNames = {"tco_nombre"})}, schema=Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcoPk", scope = SgTipoConstruccion.class)
@Audited
public class SgTipoConstruccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tco_pk", nullable = false)
    private Long tcoPk;

    @Size(max = 45)
    @Column(name = "tco_codigo", length = 45)
    @AtributoCodigo
    private String tcoCodigo;

    @Size(max = 255)
    @Column(name = "tco_nombre", length = 255)
    @AtributoNormalizable
    private String tcoNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tco_nombre_busqueda", length = 255)
    private String tcoNombreBusqueda;

    @Column(name = "tco_habilitado")
    @AtributoHabilitado
    private Boolean tcoHabilitado;

    @Column(name = "tco_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tcoUltModFecha;

    @Size(max = 45)
    @Column(name = "tco_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tcoUltModUsuario;

    @Column(name = "tco_version")
    @Version
    private Integer tcoVersion;

    public SgTipoConstruccion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tcoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tcoNombre);
    }

    public Long getTcoPk() {
        return tcoPk;
    }

    public void setTcoPk(Long tcoPk) {
        this.tcoPk = tcoPk;
    }

    public String getTcoCodigo() {
        return tcoCodigo;
    }

    public void setTcoCodigo(String tcoCodigo) {
        this.tcoCodigo = tcoCodigo;
    }

    public String getTcoNombre() {
        return tcoNombre;
    }

    public void setTcoNombre(String tcoNombre) {
        this.tcoNombre = tcoNombre;
    }

    public String getTcoNombreBusqueda() {
        return tcoNombreBusqueda;
    }

    public void setTcoNombreBusqueda(String tcoNombreBusqueda) {
        this.tcoNombreBusqueda = tcoNombreBusqueda;
    }

    public Boolean getTcoHabilitado() {
        return tcoHabilitado;
    }

    public void setTcoHabilitado(Boolean tcoHabilitado) {
        this.tcoHabilitado = tcoHabilitado;
    }

    public LocalDateTime getTcoUltModFecha() {
        return tcoUltModFecha;
    }

    public void setTcoUltModFecha(LocalDateTime tcoUltModFecha) {
        this.tcoUltModFecha = tcoUltModFecha;
    }

    public String getTcoUltModUsuario() {
        return tcoUltModUsuario;
    }

    public void setTcoUltModUsuario(String tcoUltModUsuario) {
        this.tcoUltModUsuario = tcoUltModUsuario;
    }

    public Integer getTcoVersion() {
        return tcoVersion;
    }

    public void setTcoVersion(Integer tcoVersion) {
        this.tcoVersion = tcoVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tcoPk);
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
        final SgTipoConstruccion other = (SgTipoConstruccion) obj;
        if (!Objects.equals(this.tcoPk, other.tcoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoConstruccion{" + "tcoPk=" + tcoPk + ", tcoCodigo=" + tcoCodigo + ", tcoNombre=" + tcoNombre + ", tcoNombreBusqueda=" + tcoNombreBusqueda + ", tcoHabilitado=" + tcoHabilitado + ", tcoUltModFecha=" + tcoUltModFecha + ", tcoUltModUsuario=" + tcoUltModUsuario + ", tcoVersion=" + tcoVersion + '}';
    }
    
    

}
