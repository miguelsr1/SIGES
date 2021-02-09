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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 * Entidad correspondiente a los items de liquidación
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_items_liquidacion", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "iliPk", scope = SgItemLiquidacion.class)
@Audited
public class SgItemLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ili_pk", nullable = false)
    private Long iliPk;

    @Column(name = "ili_codigo")
    @AtributoCodigo
    private Integer iliCodigo;

    @Size(max = 255)
    @Column(name = "ili_nombre", length = 255)
    @AtributoNormalizable
    private String iliNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ili_nombre_busqueda", length = 255)
    private String iliNombreBusqueda;

    @Column(name = "ili_habilitado")
    @AtributoHabilitado
    private Boolean iliHabilitado;

    @Column(name = "ili_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime iliUltModFecha;

    @Size(max = 45)
    @Column(name = "ili_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String iliUltModUsuario;

    @Column(name = "ili_version")
    @Version
    private Integer iliVersion;

    public SgItemLiquidacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.iliNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.iliNombre);
    }

    public Long getIliPk() {
        return iliPk;
    }

    public void setIliPk(Long iliPk) {
        this.iliPk = iliPk;
    }

    public Integer getIliCodigo() {
        return iliCodigo;
    }

    public void setIliCodigo(Integer iliCodigo) {
        this.iliCodigo = iliCodigo;
    }

    public String getIliNombre() {
        return iliNombre;
    }

    public void setIliNombre(String iliNombre) {
        this.iliNombre = iliNombre;
    }

    public String getIliNombreBusqueda() {
        return iliNombreBusqueda;
    }

    public void setIliNombreBusqueda(String iliNombreBusqueda) {
        this.iliNombreBusqueda = iliNombreBusqueda;
    }

    public Boolean getIliHabilitado() {
        return iliHabilitado;
    }

    public void setIliHabilitado(Boolean iliHabilitado) {
        this.iliHabilitado = iliHabilitado;
    }

    public LocalDateTime getIliUltModFecha() {
        return iliUltModFecha;
    }

    public void setIliUltModFecha(LocalDateTime iliUltModFecha) {
        this.iliUltModFecha = iliUltModFecha;
    }

    public String getIliUltModUsuario() {
        return iliUltModUsuario;
    }

    public void setIliUltModUsuario(String iliUltModUsuario) {
        this.iliUltModUsuario = iliUltModUsuario;
    }

    public Integer getIliVersion() {
        return iliVersion;
    }

    public void setIliVersion(Integer iliVersion) {
        this.iliVersion = iliVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.iliPk);
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
        final SgItemLiquidacion other = (SgItemLiquidacion) obj;
        if (!Objects.equals(this.iliPk, other.iliPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgItemLiquidacion{" + "iliPk=" + iliPk + ", iliCodigo=" + iliCodigo + ", iliNombre=" + iliNombre + ", iliNombreBusqueda=" + iliNombreBusqueda + ", iliHabilitado=" + iliHabilitado + ", iliUltModFecha=" + iliUltModFecha + ", iliUltModUsuario=" + iliUltModUsuario + ", iliVersion=" + iliVersion + '}';
    }
    
    

}
