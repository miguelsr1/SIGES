/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_unidades_administrativas", uniqueConstraints = {
    @UniqueConstraint(name = "uad_codigo_uk", columnNames = {"uad_codigo"})
    ,
    @UniqueConstraint(name = "uad_nombre_uk", columnNames = {"uad_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uadPk", scope = SgAfUnidadesAdministrativas.class)
public class SgAfUnidadesAdministrativas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "uad_pk", nullable = false)
    private Long uadPk;
    
    @Size(max = 7)
    @Column(name = "uad_codigo", length = 7)
    @AtributoCodigo
    private String uadCodigo;
    
    @Size(max = 255)
    @Column(name = "uad_nombre", length = 255)
    @AtributoNormalizable
    private String uadNombre;
    
    @Size(max = 255)
    @AtributoNombre
    @Column(name = "uad_nombre_busqueda", length = 255)
    private String uadNombreBusqueda;
    
    @Column(name = "uad_habilitado")
    @AtributoHabilitado
    private Boolean uadHabilitado;
    
    @Size(max = 60)
    @Column(name = "uad_direccion", length = 60)
    private String uadDireccion;
    
    @Size(max = 60)
    @Column(name = "uad_nombre_director", length = 60)
    private String uadNombreDirector;
    
    @Size(max = 60)
    @Column(name = "uad_cargo_director", length = 60)
    private String uadCargoDirector;
    
    @Size(max = 60)
    @Column(name = "uad_telefono",  length = 60)
    private String uadTelefono;
    
    @Size(max = 60)
    @Column(name = "uad_responsable",  length = 60)
    private String uadResponsable;
    
    @Column(name = "uad_fecha_inventario")
    private LocalDate uadFechaInventario;
    
    @Column(name = "uad_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime uadUltModFecha;

    @Size(max = 45)
    @Column(name = "uad_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario 
    private String uadUltModUsuario;
    
    @Column(name = "uad_version")
    @Version
    private Integer uadVersion;
    
    @JoinColumn(name = "uad_unidad_activo_fijo_fk", referencedColumnName = "uaf_pk")
    @ManyToOne(optional = false)
    private SgAfUnidadesActivoFijo uadUnidadActivoFijoFk;
    
    public SgAfUnidadesAdministrativas() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.uadNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.uadNombre);
    }

    public Long getUadPk() {
        return uadPk;
    }

    public void setUadPk(Long uadPk) {
        this.uadPk = uadPk;
    }

    public String getUadCodigo() {
        return uadCodigo;
    }

    public void setUadCodigo(String uadCodigo) {
        this.uadCodigo = uadCodigo;
    }

    public String getUadNombre() {
        return uadNombre;
    }

    public void setUadNombre(String uadNombre) {
        this.uadNombre = uadNombre;
    }

    public String getUadNombreBusqueda() {
        return uadNombreBusqueda;
    }

    public void setUadNombreBusqueda(String uadNombreBusqueda) {
        this.uadNombreBusqueda = uadNombreBusqueda;
    }

    public Boolean getUadHabilitado() {
        return uadHabilitado;
    }

    public void setUadHabilitado(Boolean uadHabilitado) {
        this.uadHabilitado = uadHabilitado;
    }

    public String getUadDireccion() {
        return uadDireccion;
    }

    public void setUadDireccion(String uadDireccion) {
        this.uadDireccion = uadDireccion;
    }

    public String getUadNombreDirector() {
        return uadNombreDirector;
    }

    public void setUadNombreDirector(String uadNombreDirector) {
        this.uadNombreDirector = uadNombreDirector;
    }

    public String getUadCargoDirector() {
        return uadCargoDirector;
    }

    public void setUadCargoDirector(String uadCargoDirector) {
        this.uadCargoDirector = uadCargoDirector;
    }

    public String getUadTelefono() {
        return uadTelefono;
    }

    public void setUadTelefono(String uadTelefono) {
        this.uadTelefono = uadTelefono;
    }

    public String getUadResponsable() {
        return uadResponsable;
    }

    public void setUadResponsable(String uadResponsable) {
        this.uadResponsable = uadResponsable;
    }

    public LocalDate getUadFechaInventario() {
        return uadFechaInventario;
    }

    public void setUadFechaInventario(LocalDate uadFechaInventario) {
        this.uadFechaInventario = uadFechaInventario;
    }

    public LocalDateTime getUadUltModFecha() {
        return uadUltModFecha;
    }

    public void setUadUltModFecha(LocalDateTime uadUltModFecha) {
        this.uadUltModFecha = uadUltModFecha;
    }

    public String getUadUltModUsuario() {
        return uadUltModUsuario;
    }

    public void setUadUltModUsuario(String uadUltModUsuario) {
        this.uadUltModUsuario = uadUltModUsuario;
    }

    public Integer getUadVersion() {
        return uadVersion;
    }

    public void setUadVersion(Integer uadVersion) {
        this.uadVersion = uadVersion;
    }

    public SgAfUnidadesActivoFijo getUadUnidadActivoFijoFk() {
        return uadUnidadActivoFijoFk;
    }

    public void setUadUnidadActivoFijoFk(SgAfUnidadesActivoFijo uadUnidadActivoFijoFk) {
        this.uadUnidadActivoFijoFk = uadUnidadActivoFijoFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uadPk != null ? uadPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfUnidadesAdministrativas)) {
            return false;
        }
        SgAfUnidadesAdministrativas other = (SgAfUnidadesAdministrativas) object;
        if ((this.uadPk == null && other.uadPk != null) || (this.uadPk != null && !this.uadPk.equals(other.uadPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfUnidadesAdministrativas[ uadPk=" + uadPk + " ]";
    }
    
}
