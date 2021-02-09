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
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tiempos_comida_dia", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcdPk", scope = SgTiemposComidaDia.class)
@Audited
public class SgTiemposComidaDia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tcd_pk", nullable = false)
    private Long tcdPk;

    @Size(max = 45)
    @Column(name = "tcd_codigo", length = 45)
    @AtributoCodigo
    private String tcdCodigo;

    @Size(max = 255)
    @Column(name = "tcd_nombre", length = 255)
    @AtributoNormalizable
    private String tcdNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tcd_nombre_busqueda", length = 255)
    private String tcdNombreBusqueda;

    @Column(name = "tcd_habilitado")
    @AtributoHabilitado
    private Boolean tcdHabilitado;

    @Column(name = "tcd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tcdUltModFecha;

    @Size(max = 45)
    @Column(name = "tcd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tcdUltModUsuario;

    @Column(name = "tcd_version")
    @Version
    private Integer tcdVersion;

    public SgTiemposComidaDia() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tcdNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tcdNombre);
    }

    public Long getTcdPk() {
        return tcdPk;
    }

    public void setTcdPk(Long tcdPk) {
        this.tcdPk = tcdPk;
    }

    public String getTcdCodigo() {
        return tcdCodigo;
    }

    public void setTcdCodigo(String tcdCodigo) {
        this.tcdCodigo = tcdCodigo;
    }

    public String getTcdNombre() {
        return tcdNombre;
    }

    public void setTcdNombre(String tcdNombre) {
        this.tcdNombre = tcdNombre;
    }

    public String getTcdNombreBusqueda() {
        return tcdNombreBusqueda;
    }

    public void setTcdNombreBusqueda(String tcdNombreBusqueda) {
        this.tcdNombreBusqueda = tcdNombreBusqueda;
    }

    public Boolean getTcdHabilitado() {
        return tcdHabilitado;
    }

    public void setTcdHabilitado(Boolean tcdHabilitado) {
        this.tcdHabilitado = tcdHabilitado;
    }

    public LocalDateTime getTcdUltModFecha() {
        return tcdUltModFecha;
    }

    public void setTcdUltModFecha(LocalDateTime tcdUltModFecha) {
        this.tcdUltModFecha = tcdUltModFecha;
    }

    public String getTcdUltModUsuario() {
        return tcdUltModUsuario;
    }

    public void setTcdUltModUsuario(String tcdUltModUsuario) {
        this.tcdUltModUsuario = tcdUltModUsuario;
    }

    public Integer getTcdVersion() {
        return tcdVersion;
    }

    public void setTcdVersion(Integer tcdVersion) {
        this.tcdVersion = tcdVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tcdPk);
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
        final SgTiemposComidaDia other = (SgTiemposComidaDia) obj;
        if (!Objects.equals(this.tcdPk, other.tcdPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTiemposComidaDia{" + "tcdPk=" + tcdPk + ", tcdCodigo=" + tcdCodigo + ", tcdNombre=" + tcdNombre + ", tcdNombreBusqueda=" + tcdNombreBusqueda + ", tcdHabilitado=" + tcdHabilitado + ", tcdUltModFecha=" + tcdUltModFecha + ", tcdUltModUsuario=" + tcdUltModUsuario + ", tcdVersion=" + tcdVersion + '}';
    }
    
    

}
