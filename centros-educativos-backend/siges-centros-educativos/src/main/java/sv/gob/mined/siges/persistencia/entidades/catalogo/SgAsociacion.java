/*  SIGES
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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_asociaciones",uniqueConstraints = {
    @UniqueConstraint(name = "aso_codigo_uk", columnNames = {"aso_codigo"}),
    @UniqueConstraint(name = "aso_nombre_uk", columnNames = {"aso_nombre"})},  
        schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "asoPk", scope = SgAsociacion.class) 
public class SgAsociacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aso_pk")
    private Long asoPk;
    
    @Size(max = 4)
    @Column(name = "aso_codigo")
    @AtributoCodigo
    private String asoCodigo;
    
    @Size(max = 100)
    @Column(name = "aso_nombre")
    @AtributoNormalizable
    private String asoNombre;
    
    @Size(max = 100)
    @Column(name = "aso_nombre_busqueda")
    @AtributoNombre
    private String asoNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "aso_descripcion")
    private String asoDescripcion;
    
    @JoinColumn(name = "aso_tipo_asociacion_fk", referencedColumnName = "tas_pk")
    @ManyToOne
    private SgTipoAsociacion asoTipoAsociacion;
    
    @Column(name = "aso_extranjera")
    private Boolean asoExtranjera;
    
    @Column(name = "aso_habilitado")
    @AtributoHabilitado
    private Boolean asoHabilitado;
    
    @Column(name = "aso_ejecuta_fondos_mined")
    private Boolean asoEjecutaFondosMined;
    
    @Size(max = 20)
    @Column(name = "aso_nit")
    private String asoNit;
    
    @Size(max = 100)
    @Column(name = "aso_nombre_representante_legal")
    private String asoNombreRepresentanteLegal;
    
    
    @Column(name = "aso_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime asoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "aso_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String asoUltModUsuario;
    
    @Column(name = "aso_version")
    @Version
    private Integer asoVersion;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "telAsociaciones")
    private List<SgTelefono> asoTelefonos;   
    
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "pinAsociaciones")
    private List<SgProyectoInstitucional> asoProyectos;

    public SgAsociacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.asoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.asoNombre);
    }

    public SgAsociacion(Long asoPk) {
        this.asoPk = asoPk;
    }

    public Long getAsoPk() {
        return asoPk;
    }

    public void setAsoPk(Long asoPk) {
        this.asoPk = asoPk;
    }

    public String getAsoCodigo() {
        return asoCodigo;
    }

    public void setAsoCodigo(String asoCodigo) {
        this.asoCodigo = asoCodigo;
    }

    public String getAsoNombre() {
        return asoNombre;
    }

    public void setAsoNombre(String asoNombre) {
        this.asoNombre = asoNombre;
    }

    public String getAsoNombreBusqueda() {
        return asoNombreBusqueda;
    }

    public void setAsoNombreBusqueda(String asoNombreBusqueda) {
        this.asoNombreBusqueda = asoNombreBusqueda;
    }

    public String getAsoDescripcion() {
        return asoDescripcion;
    }

    public void setAsoDescripcion(String asoDescripcion) {
        this.asoDescripcion = asoDescripcion;
    }

    public Boolean getAsoExtranjera() {
        return asoExtranjera;
    }

    public void setAsoExtranjera(Boolean asoExtranjera) {
        this.asoExtranjera = asoExtranjera;
    }

    public Boolean getAsoHabilitado() {
        return asoHabilitado;
    }

    public void setAsoHabilitado(Boolean asoHabilitado) {
        this.asoHabilitado = asoHabilitado;
    }

    public Boolean getAsoEjecutaFondosMined() {
        return asoEjecutaFondosMined;
    }

    public void setAsoEjecutaFondosMined(Boolean asoEjecutaFondosMined) {
        this.asoEjecutaFondosMined = asoEjecutaFondosMined;
    }

    public String getAsoNit() {
        return asoNit;
    }

    public void setAsoNit(String asoNit) {
        this.asoNit = asoNit;
    }

    public String getAsoNombreRepresentanteLegal() {
        return asoNombreRepresentanteLegal;
    }

    public void setAsoNombreRepresentanteLegal(String asoNombreRepresentanteLegal) {
        this.asoNombreRepresentanteLegal = asoNombreRepresentanteLegal;
    }

    public LocalDateTime getAsoUltModFecha() {
        return asoUltModFecha;
    }

    public void setAsoUltModFecha(LocalDateTime asoUltModFecha) {
        this.asoUltModFecha = asoUltModFecha;
    }

    public String getAsoUltModUsuario() {
        return asoUltModUsuario;
    }

    public void setAsoUltModUsuario(String asoUltModUsuario) {
        this.asoUltModUsuario = asoUltModUsuario;
    }

    public Integer getAsoVersion() {
        return asoVersion;
    }

    public void setAsoVersion(Integer asoVersion) {
        this.asoVersion = asoVersion;
    }

    public List<SgTelefono> getAsoTelefonos() {
        return asoTelefonos;
    }

    public void setAsoTelefonos(List<SgTelefono> asoTelefonos) {
        this.asoTelefonos = asoTelefonos;
    }

    public SgTipoAsociacion getAsoTipoAsociacion() {
        return asoTipoAsociacion;
    }

    public void setAsoTipoAsociacion(SgTipoAsociacion asoTipoAsociacion) {
        this.asoTipoAsociacion = asoTipoAsociacion;
    }

    public List<SgProyectoInstitucional> getAsoProyectos() {
        return asoProyectos;
    }

    public void setAsoProyectos(List<SgProyectoInstitucional> asoProyectos) {
        this.asoProyectos = asoProyectos;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asoPk != null ? asoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAsociacion)) {
            return false;
        }
        SgAsociacion other = (SgAsociacion) object;
        if ((this.asoPk == null && other.asoPk != null) || (this.asoPk != null && !this.asoPk.equals(other.asoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAsociacion[ asoPk=" + asoPk + " ]";
    }
    
}
