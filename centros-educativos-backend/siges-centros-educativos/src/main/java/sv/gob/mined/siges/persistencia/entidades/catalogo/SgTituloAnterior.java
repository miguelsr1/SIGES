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
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_titulo_anterior", schema=Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tanPk", scope = SgTituloAnterior.class)
@Audited
public class SgTituloAnterior implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tan_pk", nullable = false)
    private Long tanPk;

    @Size(max = 45)
    @Column(name = "tan_codigo", length = 45)
    @AtributoCodigo
    private String tanCodigo;

    @Size(max = 255)
    @Column(name = "tan_nombre", length = 255)
    @AtributoNormalizable
    private String tanNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tan_nombre_busqueda", length = 255)
    private String tanNombreBusqueda;

    @Column(name = "tan_habilitado")
    @AtributoHabilitado
    private Boolean tanHabilitado;

    @Column(name = "tan_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tanUltModFecha;

    @Size(max = 45)
    @Column(name = "tan_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tanUltModUsuario;

    @Column(name = "tan_version")
    @Version
    private Integer tanVersion;

    public SgTituloAnterior() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tanNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tanNombre);
    }

    public Long getTanPk() {
        return tanPk;
    }

    public void setTanPk(Long tanPk) {
        this.tanPk = tanPk;
    }

    public String getTanCodigo() {
        return tanCodigo;
    }

    public void setTanCodigo(String tanCodigo) {
        this.tanCodigo = tanCodigo;
    }

    public String getTanNombre() {
        return tanNombre;
    }

    public void setTanNombre(String tanNombre) {
        this.tanNombre = tanNombre;
    }

    public String getTanNombreBusqueda() {
        return tanNombreBusqueda;
    }

    public void setTanNombreBusqueda(String tanNombreBusqueda) {
        this.tanNombreBusqueda = tanNombreBusqueda;
    }

    public Boolean getTanHabilitado() {
        return tanHabilitado;
    }

    public void setTanHabilitado(Boolean tanHabilitado) {
        this.tanHabilitado = tanHabilitado;
    }

    public LocalDateTime getTanUltModFecha() {
        return tanUltModFecha;
    }

    public void setTanUltModFecha(LocalDateTime tanUltModFecha) {
        this.tanUltModFecha = tanUltModFecha;
    }

    public String getTanUltModUsuario() {
        return tanUltModUsuario;
    }

    public void setTanUltModUsuario(String tanUltModUsuario) {
        this.tanUltModUsuario = tanUltModUsuario;
    }

    public Integer getTanVersion() {
        return tanVersion;
    }

    public void setTanVersion(Integer tanVersion) {
        this.tanVersion = tanVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tanPk);
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
        final SgTituloAnterior other = (SgTituloAnterior) obj;
        if (!Objects.equals(this.tanPk, other.tanPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTituloAnterior{" + "tanPk=" + tanPk + ", tanCodigo=" + tanCodigo + ", tanNombre=" + tanNombre + ", tanNombreBusqueda=" + tanNombreBusqueda + ", tanHabilitado=" + tanHabilitado + ", tanUltModFecha=" + tanUltModFecha + ", tanUltModUsuario=" + tanUltModUsuario + ", tanVersion=" + tanVersion + '}';
    }
    
    

}
