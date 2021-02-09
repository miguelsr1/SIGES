/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
@Table(name = "sg_maximo_nivel_educativo_alcanzado", uniqueConstraints = {
    @UniqueConstraint(name = "mne_codigo_uk", columnNames = {"mne_codigo"})
    ,
    @UniqueConstraint(name = "mne_nombre_uk", columnNames = {"mne_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mnePk", scope = SgMaximoNivelEducativoAlcanzado.class)
@Audited
public class SgMaximoNivelEducativoAlcanzado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mne_pk", nullable = false)
    private Long mnePk;

    @Size(max = 45)
    @Column(name = "mne_codigo", length = 45)
    @AtributoCodigo
    private String mneCodigo;

    @Size(max = 255)
    @Column(name = "mne_nombre", length = 255)
    @AtributoNormalizable
    private String mneNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "mne_nombre_busqueda", length = 255)
    private String mneNombreBusqueda;

    @Column(name = "mne_habilitado")
    @AtributoHabilitado
    private Boolean mneHabilitado;

    @Column(name = "mne_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mneUltModFecha;

    @Size(max = 45)
    @Column(name = "mne_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mneUltModUsuario;

    @Column(name = "mne_version")
    @Version
    private Integer mneVersion;

    public SgMaximoNivelEducativoAlcanzado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mneNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mneNombre);
    }

    public Long getMnePk() {
        return mnePk;
    }

    public void setMnePk(Long mnePk) {
        this.mnePk = mnePk;
    }

    public String getMneCodigo() {
        return mneCodigo;
    }

    public void setMneCodigo(String mneCodigo) {
        this.mneCodigo = mneCodigo;
    }

    public String getMneNombre() {
        return mneNombre;
    }

    public void setMneNombre(String mneNombre) {
        this.mneNombre = mneNombre;
    }

    public String getMneNombreBusqueda() {
        return mneNombreBusqueda;
    }

    public void setMneNombreBusqueda(String mneNombreBusqueda) {
        this.mneNombreBusqueda = mneNombreBusqueda;
    }

    public Boolean getMneHabilitado() {
        return mneHabilitado;
    }

    public void setMneHabilitado(Boolean mneHabilitado) {
        this.mneHabilitado = mneHabilitado;
    }

    public LocalDateTime getMneUltModFecha() {
        return mneUltModFecha;
    }

    public void setMneUltModFecha(LocalDateTime mneUltModFecha) {
        this.mneUltModFecha = mneUltModFecha;
    }

    public String getMneUltModUsuario() {
        return mneUltModUsuario;
    }

    public void setMneUltModUsuario(String mneUltModUsuario) {
        this.mneUltModUsuario = mneUltModUsuario;
    }

    public Integer getMneVersion() {
        return mneVersion;
    }

    public void setMneVersion(Integer mneVersion) {
        this.mneVersion = mneVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mnePk);
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
        final SgMaximoNivelEducativoAlcanzado other = (SgMaximoNivelEducativoAlcanzado) obj;
        if (!Objects.equals(this.mnePk, other.mnePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMaximoNivelEducativoAlcanzado{" + "mnePk=" + mnePk + ", mneCodigo=" + mneCodigo + ", mneNombre=" + mneNombre + ", mneNombreBusqueda=" + mneNombreBusqueda + ", mneHabilitado=" + mneHabilitado + ", mneUltModFecha=" + mneUltModFecha + ", mneUltModUsuario=" + mneUltModUsuario + ", mneVersion=" + mneVersion + '}';
    }
    
    

}
