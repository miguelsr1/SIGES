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
import java.util.List;
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarColeccion;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_motivos_inasistencia_personal", uniqueConstraints = {
    @UniqueConstraint(name = "mip_codigo_uk", columnNames = {"mip_codigo"})
    ,
    @UniqueConstraint(name = "mip_nombre_uk", columnNames = {"mip_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mipPk", scope = SgMotivoInasistenciaPersonal.class)
@Audited
public class SgMotivoInasistenciaPersonal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mip_pk", nullable = false)
    private Long mipPk;

    @Size(max = 45)
    @Column(name = "mip_codigo", length = 45)
    @AtributoCodigo
    private String mipCodigo;

    @Size(max = 255)
    @Column(name = "mip_nombre", length = 255)
    @AtributoNormalizable
    private String mipNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "mip_nombre_busqueda", length = 255)
    private String mipNombreBusqueda;

    @Column(name = "mip_habilitado")
    @AtributoHabilitado
    private Boolean mipHabilitado;
    
    @Column(name = "mip_motivo_justificado")
    private Boolean mipMotivoJustificado;

    @Column(name = "mip_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mipUltModFecha;

    @Size(max = 45)
    @Column(name = "mip_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mipUltModUsuario;

    @Column(name = "mip_version")
    @Version
    private Integer mipVersion;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_motivo_inasistencia_personal_cargo",
            schema = Constantes.SCHEMA_CATALOGO,
            joinColumns = @JoinColumn(name = "mip_pk"),
            inverseJoinColumns = @JoinColumn(name = "car_pk"))
    @AtributoInicializarColeccion
    private List<SgCargo> mipCargos;

    public SgMotivoInasistenciaPersonal() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mipNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mipNombre);
    }

    public Long getMipPk() {
        return mipPk;
    }

    public void setMipPk(Long mipPk) {
        this.mipPk = mipPk;
    }

    public String getMipCodigo() {
        return mipCodigo;
    }

    public void setMipCodigo(String mipCodigo) {
        this.mipCodigo = mipCodigo;
    }

    public String getMipNombre() {
        return mipNombre;
    }

    public void setMipNombre(String mipNombre) {
        this.mipNombre = mipNombre;
    }

    public String getMipNombreBusqueda() {
        return mipNombreBusqueda;
    }

    public void setMipNombreBusqueda(String mipNombreBusqueda) {
        this.mipNombreBusqueda = mipNombreBusqueda;
    }

    public Boolean getMipHabilitado() {
        return mipHabilitado;
    }

    public void setMipHabilitado(Boolean mipHabilitado) {
        this.mipHabilitado = mipHabilitado;
    }

    public Boolean getMipMotivoJustificado() {
        return mipMotivoJustificado;
    }

    public void setMipMotivoJustificado(Boolean mipMotivoJustificado) {
        this.mipMotivoJustificado = mipMotivoJustificado;
    }

    public LocalDateTime getMipUltModFecha() {
        return mipUltModFecha;
    }

    public void setMipUltModFecha(LocalDateTime mipUltModFecha) {
        this.mipUltModFecha = mipUltModFecha;
    }

    public String getMipUltModUsuario() {
        return mipUltModUsuario;
    }

    public void setMipUltModUsuario(String mipUltModUsuario) {
        this.mipUltModUsuario = mipUltModUsuario;
    }

    public Integer getMipVersion() {
        return mipVersion;
    }

    public void setMipVersion(Integer mipVersion) {
        this.mipVersion = mipVersion;
    }

    public List<SgCargo> getMipCargos() {
        return mipCargos;
    }

    public void setMipCargos(List<SgCargo> mipCargos) {
        this.mipCargos = mipCargos;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mipPk);
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
        final SgMotivoInasistenciaPersonal other = (SgMotivoInasistenciaPersonal) obj;
        if (!Objects.equals(this.mipPk, other.mipPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMotivoInasistenciaPersonal{" + "mipPk=" + mipPk + ", mipCodigo=" + mipCodigo + ", mipNombre=" + mipNombre + ", mipNombreBusqueda=" + mipNombreBusqueda + ", mipHabilitado=" + mipHabilitado + ", mipUltModFecha=" + mipUltModFecha + ", mipUltModUsuario=" + mipUltModUsuario + ", mipVersion=" + mipVersion + '}';
    }
    
    

}
