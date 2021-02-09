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
@Table(name = "sg_implementadoras", uniqueConstraints = {
    @UniqueConstraint(name = "imp_codigo_uk", columnNames = {"imp_codigo"})
    ,
    @UniqueConstraint(name = "imp_nombre_uk", columnNames = {"imp_nombre"})
    ,
    @UniqueConstraint(name = "imp_orden_uk", columnNames = {"imp_orden"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "impPk", scope = SgImplementadora.class)
@Audited
public class SgImplementadora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "imp_pk", nullable = false)
    private Long impPk;

    @Size(max = 45)
    @Column(name = "imp_codigo", length = 45)
    @AtributoCodigo
    private String impCodigo;

    @Size(max = 255)
    @Column(name = "imp_nombre", length = 255)
    @AtributoNormalizable
    private String impNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "imp_nombre_busqueda", length = 255)
    private String impNombreBusqueda;

    @Column(name = "imp_habilitado")
    @AtributoHabilitado
    private Boolean impHabilitado;
    
    @Column(name = "imp_version")
    @Version
    private Integer impVersion;

    @Column(name = "imp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime impUltModFecha;

    @Size(max = 45)
    @Column(name = "imp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String impUltModUsuario;

    @Column(name = "imp_orden")
    @Version
    private Integer impOrden;

    public SgImplementadora() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.impNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.impNombre);
    }

    public Long getImpPk() {
        return impPk;
    }

    public void setImpPk(Long impPk) {
        this.impPk = impPk;
    }

    public String getImpCodigo() {
        return impCodigo;
    }

    public void setImpCodigo(String impCodigo) {
        this.impCodigo = impCodigo;
    }

    public String getImpNombre() {
        return impNombre;
    }

    public void setImpNombre(String impNombre) {
        this.impNombre = impNombre;
    }

    public String getImpNombreBusqueda() {
        return impNombreBusqueda;
    }

    public void setImpNombreBusqueda(String impNombreBusqueda) {
        this.impNombreBusqueda = impNombreBusqueda;
    }

    public Boolean getImpHabilitado() {
        return impHabilitado;
    }

    public void setImpHabilitado(Boolean impHabilitado) {
        this.impHabilitado = impHabilitado;
    }

    public LocalDateTime getImpUltModFecha() {
        return impUltModFecha;
    }

    public void setImpUltModFecha(LocalDateTime impUltModFecha) {
        this.impUltModFecha = impUltModFecha;
    }

    public String getImpUltModUsuario() {
        return impUltModUsuario;
    }

    public void setImpUltModUsuario(String impUltModUsuario) {
        this.impUltModUsuario = impUltModUsuario;
    }

    public Integer getImpVersion() {
        return impVersion;
    }

    public void setImpVersion(Integer impVersion) {
        this.impVersion = impVersion;
    }

    public Integer getImpOrden() {
        return impOrden;
    }

    public void setImpOrden(Integer impOrden) {
        this.impOrden = impOrden;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.impPk);
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
        final SgImplementadora other = (SgImplementadora) obj;
        if (!Objects.equals(this.impPk, other.impPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgImplementadoras{" + "impPk=" + impPk + ", impCodigo=" + impCodigo + ", impNombre=" + impNombre + ", impNombreBusqueda=" + impNombreBusqueda + ", impHabilitado=" + impHabilitado + ", impUltModFecha=" + impUltModFecha + ", impUltModUsuario=" + impUltModUsuario + ", impVersion=" + impVersion + '}';
    }
    
    

}
