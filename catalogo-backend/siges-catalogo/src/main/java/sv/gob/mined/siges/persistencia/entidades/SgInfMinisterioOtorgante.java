/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
@Table(name = "sg_inf_ministerio_otorgante", uniqueConstraints = {
    @UniqueConstraint(name = "mio_codigo_uk", columnNames = {"mio_codigo"})
    ,
    @UniqueConstraint(name = "mio_nombre_uk", columnNames = {"mio_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mioPk", scope = SgInfMinisterioOtorgante.class)
@Audited
public class SgInfMinisterioOtorgante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mio_pk", nullable = false)
    private Long mioPk;

    @Size(max = 45)
    @Column(name = "mio_codigo", length = 45)
    @AtributoCodigo
    private String mioCodigo;

    @Size(max = 255)
    @Column(name = "mio_nombre", length = 255)
    @AtributoNormalizable
    private String mioNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "mio_nombre_busqueda", length = 255)
    private String mioNombreBusqueda;

    @Column(name = "mio_habilitado")
    @AtributoHabilitado
    private Boolean mioHabilitado;

    @Column(name = "mio_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mioUltModFecha;

    @Size(max = 45)
    @Column(name = "mio_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mioUltModUsuario;

    @Column(name = "mio_version")
    @Version
    private Integer mioVersion;

    public SgInfMinisterioOtorgante() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mioNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mioNombre);
    }

    public Long getMioPk() {
        return mioPk;
    }

    public void setMioPk(Long mioPk) {
        this.mioPk = mioPk;
    }

    public String getMioCodigo() {
        return mioCodigo;
    }

    public void setMioCodigo(String mioCodigo) {
        this.mioCodigo = mioCodigo;
    }

    public String getMioNombre() {
        return mioNombre;
    }

    public void setMioNombre(String mioNombre) {
        this.mioNombre = mioNombre;
    }

    public String getMioNombreBusqueda() {
        return mioNombreBusqueda;
    }

    public void setMioNombreBusqueda(String mioNombreBusqueda) {
        this.mioNombreBusqueda = mioNombreBusqueda;
    }

    public Boolean getMioHabilitado() {
        return mioHabilitado;
    }

    public void setMioHabilitado(Boolean mioHabilitado) {
        this.mioHabilitado = mioHabilitado;
    }

    public LocalDateTime getMioUltModFecha() {
        return mioUltModFecha;
    }

    public void setMioUltModFecha(LocalDateTime mioUltModFecha) {
        this.mioUltModFecha = mioUltModFecha;
    }

    public String getMioUltModUsuario() {
        return mioUltModUsuario;
    }

    public void setMioUltModUsuario(String mioUltModUsuario) {
        this.mioUltModUsuario = mioUltModUsuario;
    }

    public Integer getMioVersion() {
        return mioVersion;
    }

    public void setMioVersion(Integer mioVersion) {
        this.mioVersion = mioVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mioPk);
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
        final SgInfMinisterioOtorgante other = (SgInfMinisterioOtorgante) obj;
        if (!Objects.equals(this.mioPk, other.mioPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfMinisterioOtorgante{" + "mioPk=" + mioPk + ", mioCodigo=" + mioCodigo + ", mioNombre=" + mioNombre + ", mioNombreBusqueda=" + mioNombreBusqueda + ", mioHabilitado=" + mioHabilitado + ", mioUltModFecha=" + mioUltModFecha + ", mioUltModUsuario=" + mioUltModUsuario + ", mioVersion=" + mioVersion + '}';
    }
    
    

}
