/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
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
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_medios_transporte", uniqueConstraints = {
    @UniqueConstraint(name = "mtr_codigo_uk", columnNames = {"mtr_codigo"})
    ,
    @UniqueConstraint(name = "mtr_nombre_uk", columnNames = {"mtr_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mtrPk", scope = SgMedioTransporte.class)
public class SgMedioTransporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mtr_pk")
    private Long mtrPk;

    @Size(max = 4)
    @Column(name = "mtr_codigo", length = 4)
    @AtributoCodigo
    private String mtrCodigo;

    @Size(max = 255)
    @Column(name = "mtr_nombre", length = 255)
    @AtributoNormalizable
    private String mtrNombre;

    @Size(max = 255)
    @Column(name = "mtr_nombre_busqueda", length = 255)
    @AtributoNombre
    private String mtrNombreBusqueda;

    @Column(name = "mtr_habilitado")
    @AtributoHabilitado
    private Boolean mtrHabilitado;

    @Column(name = "mtr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mtrUltModFecha;

    @Size(max = 45)
    @Column(name = "mtr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mtrUltModUsuario;

    @Column(name = "mtr_version")
    @Version
    private Integer mtrVersion;

    public SgMedioTransporte() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mtrNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mtrNombre);
    }

    public String getMtrNombreBusqueda() {
        return mtrNombreBusqueda;
    }

    public void setMtrNombreBusqueda(String mtrNombreBusqueda) {
        this.mtrNombreBusqueda = mtrNombreBusqueda;
    }

    public Long getMtrPk() {
        return mtrPk;
    }

    public void setMtrPk(Long mtrPk) {
        this.mtrPk = mtrPk;
    }

    public String getMtrCodigo() {
        return mtrCodigo;
    }

    public void setMtrCodigo(String mtrCodigo) {
        this.mtrCodigo = mtrCodigo;
    }

    public String getMtrNombre() {
        return mtrNombre;
    }

    public void setMtrNombre(String mtrNombre) {
        this.mtrNombre = mtrNombre;
    }

    public Boolean getMtrHabilitado() {
        return mtrHabilitado;
    }

    public void setMtrHabilitado(Boolean mtrHabilitado) {
        this.mtrHabilitado = mtrHabilitado;
    }

    public LocalDateTime getMtrUltModFecha() {
        return mtrUltModFecha;
    }

    public void setMtrUltModFecha(LocalDateTime mtrUltModFecha) {
        this.mtrUltModFecha = mtrUltModFecha;
    }

    public String getMtrUltModUsuario() {
        return mtrUltModUsuario;
    }

    public void setMtrUltModUsuario(String mtrUltModUsuario) {
        this.mtrUltModUsuario = mtrUltModUsuario;
    }

    public Integer getMtrVersion() {
        return mtrVersion;
    }

    public void setMtrVersion(Integer mtrVersion) {
        this.mtrVersion = mtrVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.mtrPk);
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
        final SgMedioTransporte other = (SgMedioTransporte) obj;
        if (!Objects.equals(this.mtrPk, other.mtrPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesMedioTransporte[ mtrPk=" + mtrPk + " ]";
    }

}
