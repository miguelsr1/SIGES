/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.ArchivoListener;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_archivos", schema = Constantes.SCHEMA_PUBLIC)
@XmlRootElement
@EntityListeners({EntidadListener.class, ArchivoListener.class})
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "achPk", scope = SgArchivo.class)
public class SgArchivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ach_pk", nullable = false)
    private Long achPk;

    @Size(max = 255)
    @Column(name = "ach_nombre", length = 255)
    private String achNombre;

    @Size(max = 1000)
    @Column(name = "ach_descripcion", length = 1000)
    private String achDescripcion;

    @Size(max = 500)
    @Column(name = "ach_path", length = 500)
    private String achTmpPath;

    @Size(max = 255)
    @Column(name = "ach_content_type", length = 255)
    private String achContentType;

    @Size(max = 50)
    @Column(name = "ach_ext", length = 50)
    private String achExt;

    @AtributoUltimaModificacion
    @Column(name = "ach_ult_mod_fecha")
    private LocalDateTime achUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "ach_ult_mod_usuario", length = 45)
    private String achUltmodUsuario;

    @Column(name = "ach_version")
    @Version
    private Integer achVersion;
    

    public Long getAchPk() {
        return achPk;
    }

    public void setAchPk(Long achPk) {
        this.achPk = achPk;
    }

    public String getAchNombre() {
        return achNombre;
    }

    public void setAchNombre(String achNombre) {
        this.achNombre = achNombre;
    }

    public String getAchDescripcion() {
        return achDescripcion;
    }

    public void setAchDescripcion(String achDescripcion) {
        this.achDescripcion = achDescripcion;
    }

    public String getAchContentType() {
        return achContentType;
    }

    public void setAchContentType(String achContentType) {
        this.achContentType = achContentType;
    }

    public String getAchExt() {
        return achExt;
    }

    public void setAchExt(String achExt) {
        this.achExt = achExt;
    }

    public LocalDateTime getAchUltmodFecha() {
        return achUltmodFecha;
    }

    public void setAchUltmodFecha(LocalDateTime achUltmodFecha) {
        this.achUltmodFecha = achUltmodFecha;
    }

    public String getAchUltmodUsuario() {
        return achUltmodUsuario;
    }

    public void setAchUltmodUsuario(String achUltmodUsuario) {
        this.achUltmodUsuario = achUltmodUsuario;
    }

    public Integer getAchVersion() {
        return achVersion;
    }

    public void setAchVersion(Integer achVersion) {
        this.achVersion = achVersion;
    }

    public String getAchTmpPath() {
        return achTmpPath;
    }

    public void setAchTmpPath(String achTmpPath) {
        this.achTmpPath = achTmpPath;
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.achPk);
        return hash;
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
        final SgArchivo other = (SgArchivo) obj;
        if (!Objects.equals(this.achPk, other.achPk)) {
            return false;
        }
        return true;
    }

}
