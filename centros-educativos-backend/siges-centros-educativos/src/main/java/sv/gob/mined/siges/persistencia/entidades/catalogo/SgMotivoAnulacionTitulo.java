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
@Table(name = "sg_motivo_anulacion_titulo", uniqueConstraints = {
    @UniqueConstraint(name = "mat_codigo_motivo_anulacion_titulo_uk", columnNames = {"mat_codigo"})
    ,
    @UniqueConstraint(name = "mat_nombre_motivo_anulacion_titulo_uk", columnNames = {"mat_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "matPk", scope = SgMotivoAnulacionTitulo.class)
@Audited
public class SgMotivoAnulacionTitulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mat_pk", nullable = false)
    private Long matPk;

    @Size(max = 45)
    @Column(name = "mat_codigo", length = 45)
    @AtributoCodigo
    private String matCodigo;

    @Size(max = 255)
    @Column(name = "mat_nombre", length = 255)
    @AtributoNormalizable
    private String matNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "mat_nombre_busqueda", length = 255)
    private String matNombreBusqueda;

    @Column(name = "mat_habilitado")
    @AtributoHabilitado
    private Boolean matHabilitado;

    @Column(name = "mat_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime matUltModFecha;

    @Size(max = 45)
    @Column(name = "mat_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String matUltModUsuario;

    @Column(name = "mat_version")
    @Version
    private Integer matVersion;

    public SgMotivoAnulacionTitulo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.matNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.matNombre);
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public String getMatCodigo() {
        return matCodigo;
    }

    public void setMatCodigo(String matCodigo) {
        this.matCodigo = matCodigo;
    }

    public String getMatNombre() {
        return matNombre;
    }

    public void setMatNombre(String matNombre) {
        this.matNombre = matNombre;
    }

    public String getMatNombreBusqueda() {
        return matNombreBusqueda;
    }

    public void setMatNombreBusqueda(String matNombreBusqueda) {
        this.matNombreBusqueda = matNombreBusqueda;
    }

    public Boolean getMatHabilitado() {
        return matHabilitado;
    }

    public void setMatHabilitado(Boolean matHabilitado) {
        this.matHabilitado = matHabilitado;
    }

    public LocalDateTime getMatUltModFecha() {
        return matUltModFecha;
    }

    public void setMatUltModFecha(LocalDateTime matUltModFecha) {
        this.matUltModFecha = matUltModFecha;
    }

    public String getMatUltModUsuario() {
        return matUltModUsuario;
    }

    public void setMatUltModUsuario(String matUltModUsuario) {
        this.matUltModUsuario = matUltModUsuario;
    }

    public Integer getMatVersion() {
        return matVersion;
    }

    public void setMatVersion(Integer matVersion) {
        this.matVersion = matVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.matPk);
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
        final SgMotivoAnulacionTitulo other = (SgMotivoAnulacionTitulo) obj;
        if (!Objects.equals(this.matPk, other.matPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMotivoAnulacionTitulo{" + "matPk=" + matPk + ", matCodigo=" + matCodigo + ", matNombre=" + matNombre + ", matNombreBusqueda=" + matNombreBusqueda + ", matHabilitado=" + matHabilitado + ", matUltModFecha=" + matUltModFecha + ", matUltModUsuario=" + matUltModUsuario + ", matVersion=" + matVersion + '}';
    }
    
    

}
