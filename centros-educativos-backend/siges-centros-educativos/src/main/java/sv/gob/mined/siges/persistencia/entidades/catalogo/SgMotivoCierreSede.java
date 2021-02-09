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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_motivos_cierre_sede", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mcsPk", scope = SgMotivoCierreSede.class)
@Audited
public class SgMotivoCierreSede implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mcs_pk", nullable = false)
    private Long mcsPk;

    @Size(max = 45)
    @Column(name = "mcs_codigo", length = 45)
    @AtributoCodigo
    private String mcsCodigo;

    @Size(max = 255)
    @Column(name = "mcs_nombre", length = 255)
    @AtributoNormalizable
    private String mcsNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "mcs_nombre_busqueda", length = 255)
    private String mcsNombreBusqueda;

    @Column(name = "mcs_habilitado")
    @AtributoHabilitado
    private Boolean mcsHabilitado;

    @Column(name = "mcs_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mcsUltModFecha;

    @Size(max = 45)
    @Column(name = "mcs_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mcsUltModUsuario;

    @Column(name = "mcs_version")
    @Version
    private Integer mcsVersion;

    public SgMotivoCierreSede() {
    }

    public Long getMcsPk() {
        return mcsPk;
    }

    public void setMcsPk(Long mcsPk) {
        this.mcsPk = mcsPk;
    }

    public String getMcsCodigo() {
        return mcsCodigo;
    }

    public void setMcsCodigo(String mcsCodigo) {
        this.mcsCodigo = mcsCodigo;
    }

    public String getMcsNombre() {
        return mcsNombre;
    }

    public void setMcsNombre(String mcsNombre) {
        this.mcsNombre = mcsNombre;
    }

    public String getMcsNombreBusqueda() {
        return mcsNombreBusqueda;
    }

    public void setMcsNombreBusqueda(String mcsNombreBusqueda) {
        this.mcsNombreBusqueda = mcsNombreBusqueda;
    }

    public Boolean getMcsHabilitado() {
        return mcsHabilitado;
    }

    public void setMcsHabilitado(Boolean mcsHabilitado) {
        this.mcsHabilitado = mcsHabilitado;
    }

    public LocalDateTime getMcsUltModFecha() {
        return mcsUltModFecha;
    }

    public void setMcsUltModFecha(LocalDateTime mcsUltModFecha) {
        this.mcsUltModFecha = mcsUltModFecha;
    }

    public String getMcsUltModUsuario() {
        return mcsUltModUsuario;
    }

    public void setMcsUltModUsuario(String mcsUltModUsuario) {
        this.mcsUltModUsuario = mcsUltModUsuario;
    }

    public Integer getMcsVersion() {
        return mcsVersion;
    }

    public void setMcsVersion(Integer mcsVersion) {
        this.mcsVersion = mcsVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mcsPk);
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
        final SgMotivoCierreSede other = (SgMotivoCierreSede) obj;
        if (!Objects.equals(this.mcsPk, other.mcsPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMotivoCierreSede{" + "mcsPk=" + mcsPk + ", mcsCodigo=" + mcsCodigo + ", mcsNombre=" + mcsNombre + ", mcsNombreBusqueda=" + mcsNombreBusqueda + ", mcsHabilitado=" + mcsHabilitado + ", mcsUltModFecha=" + mcsUltModFecha + ", mcsUltModUsuario=" + mcsUltModUsuario + ", mcsVersion=" + mcsVersion + '}';
    }
    
    

}
