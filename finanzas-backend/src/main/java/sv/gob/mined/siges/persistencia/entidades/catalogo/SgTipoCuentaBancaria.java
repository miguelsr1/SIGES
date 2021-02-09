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
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tipo_cuenta_bancaria", uniqueConstraints = {
    @UniqueConstraint(name = "tcb_codigo_uk", columnNames = {"tcb_codigo"})
    ,
    @UniqueConstraint(name = "tcb_nombre_uk", columnNames = {"tcb_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcbPk", scope = SgTipoCuentaBancaria.class)
@Audited
/**
 * Entidad correspondiente a los tipos de cuenta en el sentido de para qué se
 * utiliza esa cuenta.
 */
public class SgTipoCuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tcb_pk", nullable = false)
    private Long tcbPk;

    @Size(max = 45)
    @Column(name = "tcb_codigo", length = 45)
    @AtributoCodigo
    private String tcbCodigo;

    @Size(max = 255)
    @Column(name = "tcb_nombre", length = 255)
    @AtributoNormalizable
    private String tcbNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tcb_nombre_busqueda", length = 255)
    private String tcbNombreBusqueda;

    @Column(name = "tcb_habilitado")
    @AtributoHabilitado
    private Boolean tcbHabilitado;

    @Column(name = "tcb_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tcbUltModFecha;

    @Size(max = 45)
    @Column(name = "tcb_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tcbUltModUsuario;

    @Column(name = "tcb_version")
    @Version
    private Integer tcbVersion;

    public SgTipoCuentaBancaria() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tcbNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tcbNombre);
    }

    public Long getTcbPk() {
        return tcbPk;
    }

    public void setTcbPk(Long tcbPk) {
        this.tcbPk = tcbPk;
    }

    public String getTcbCodigo() {
        return tcbCodigo;
    }

    public void setTcbCodigo(String tcbCodigo) {
        this.tcbCodigo = tcbCodigo;
    }

    public String getTcbNombre() {
        return tcbNombre;
    }

    public void setTcbNombre(String tcbNombre) {
        this.tcbNombre = tcbNombre;
    }

    public String getTcbNombreBusqueda() {
        return tcbNombreBusqueda;
    }

    public void setTcbNombreBusqueda(String tcbNombreBusqueda) {
        this.tcbNombreBusqueda = tcbNombreBusqueda;
    }

    public Boolean getTcbHabilitado() {
        return tcbHabilitado;
    }

    public void setTcbHabilitado(Boolean tcbHabilitado) {
        this.tcbHabilitado = tcbHabilitado;
    }

    public LocalDateTime getTcbUltModFecha() {
        return tcbUltModFecha;
    }

    public void setTcbUltModFecha(LocalDateTime tcbUltModFecha) {
        this.tcbUltModFecha = tcbUltModFecha;
    }

    public String getTcbUltModUsuario() {
        return tcbUltModUsuario;
    }

    public void setTcbUltModUsuario(String tcbUltModUsuario) {
        this.tcbUltModUsuario = tcbUltModUsuario;
    }

    public Integer getTcbVersion() {
        return tcbVersion;
    }

    public void setTcbVersion(Integer tcbVersion) {
        this.tcbVersion = tcbVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tcbPk);
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
        final SgTipoCuentaBancaria other = (SgTipoCuentaBancaria) obj;
        if (!Objects.equals(this.tcbPk, other.tcbPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoCuentaBancaria{" + "tcbPk=" + tcbPk + ", tcbCodigo=" + tcbCodigo + ", tcbNombre=" + tcbNombre + ", tcbNombreBusqueda=" + tcbNombreBusqueda + ", tcbHabilitado=" + tcbHabilitado + ", tcbUltModFecha=" + tcbUltModFecha + ", tcbUltModUsuario=" + tcbUltModUsuario + ", tcbVersion=" + tcbVersion + '}';
    }

}
