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
@Table(name = "sg_inf_obra_exterior", uniqueConstraints = {
    @UniqueConstraint(name = "oex_codigo_uk", columnNames = {"oex_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oexPk", scope = SgInfObraExterior.class)
@Audited
public class SgInfObraExterior implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "oex_pk", nullable = false)
    private Long oexPk;

    @Size(max = 45)
    @Column(name = "oex_codigo", length = 45)
    @AtributoCodigo
    private String oexCodigo;

    @Size(max = 255)
    @Column(name = "oex_nombre", length = 255)
    @AtributoNormalizable
    private String oexNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "oex_nombre_busqueda", length = 255)
    private String oexNombreBusqueda;

    @Column(name = "oex_habilitado")
    @AtributoHabilitado
    private Boolean oexHabilitado;

    @Column(name = "oex_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime oexUltModFecha;

    @Size(max = 45)
    @Column(name = "oex_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String oexUltModUsuario;

    @Column(name = "oex_version")
    @Version
    private Integer oexVersion;
    
    @Column(name = "oex_aplica_otro")
    private Boolean oexAplicaOtros;
    
    @Column(name = "oex_orden")
    private Integer oexOrden;

    public SgInfObraExterior() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.oexNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.oexNombre);
    }

    public Long getOexPk() {
        return oexPk;
    }

    public void setOexPk(Long oexPk) {
        this.oexPk = oexPk;
    }

    public String getOexCodigo() {
        return oexCodigo;
    }

    public void setOexCodigo(String oexCodigo) {
        this.oexCodigo = oexCodigo;
    }

    public String getOexNombre() {
        return oexNombre;
    }

    public void setOexNombre(String oexNombre) {
        this.oexNombre = oexNombre;
    }

    public String getOexNombreBusqueda() {
        return oexNombreBusqueda;
    }

    public void setOexNombreBusqueda(String oexNombreBusqueda) {
        this.oexNombreBusqueda = oexNombreBusqueda;
    }

    public Boolean getOexHabilitado() {
        return oexHabilitado;
    }

    public void setOexHabilitado(Boolean oexHabilitado) {
        this.oexHabilitado = oexHabilitado;
    }

    public LocalDateTime getOexUltModFecha() {
        return oexUltModFecha;
    }

    public void setOexUltModFecha(LocalDateTime oexUltModFecha) {
        this.oexUltModFecha = oexUltModFecha;
    }

    public String getOexUltModUsuario() {
        return oexUltModUsuario;
    }

    public void setOexUltModUsuario(String oexUltModUsuario) {
        this.oexUltModUsuario = oexUltModUsuario;
    }

    public Integer getOexVersion() {
        return oexVersion;
    }

    public Boolean getOexAplicaOtros() {
        return oexAplicaOtros;
    }

    public void setOexAplicaOtros(Boolean oexAplicaOtros) {
        this.oexAplicaOtros = oexAplicaOtros;
    }

    public void setOexVersion(Integer oexVersion) {
        this.oexVersion = oexVersion;
    }

    public Integer getOexOrden() {
        return oexOrden;
    }

    public void setOexOrden(Integer oexOrden) {
        this.oexOrden = oexOrden;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.oexPk);
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
        final SgInfObraExterior other = (SgInfObraExterior) obj;
        if (!Objects.equals(this.oexPk, other.oexPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfObraExterior{" + "oexPk=" + oexPk + ", oexCodigo=" + oexCodigo + ", oexNombre=" + oexNombre + ", oexNombreBusqueda=" + oexNombreBusqueda + ", oexHabilitado=" + oexHabilitado + ", oexUltModFecha=" + oexUltModFecha + ", oexUltModUsuario=" + oexUltModUsuario + ", oexVersion=" + oexVersion + '}';
    }
    
    

}
