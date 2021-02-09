/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDefinicionTitulo;

@Entity
@Table(name = "sg_grados", uniqueConstraints = {
    @UniqueConstraint(name = "gra_codigo_uk", columnNames = {"gra_codigo"})},
        schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "graPk", scope = SgGrado.class)
@Audited
public class SgGrado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "gra_pk")
    private Long graPk;

    @Size(max = 10)
    @Column(name = "gra_codigo", length = 10)
    @AtributoCodigo
    private String graCodigo;

    @Size(max = 255)
    @Column(name = "gra_nombre", length = 255)
    private String graNombre;

    @Size(max = 500)
    @Column(name = "gra_descripcion", length = 500)
    private String graDescripcion;

    @JoinColumn(name = "gra_relacion_modalidad_fk", referencedColumnName = "rea_pk")
    @ManyToOne(optional = false)
    private SgRelModEdModAten graRelacionModalidad;

    @Column(name = "gra_orden")
    private Integer graOrden;

    @Column(name = "gra_habilitado")
    @AtributoHabilitado
    private Boolean graHabilitado;

    @Column(name = "gra_requiere_nie")
    private Boolean graRequiereNIE;
        
    @Column(name = "gra_edad_minima")
    private Integer graEdadMinima;
    
    @Column(name = "gra_edad_maxima")
    private Integer graEdadMaxima;

    @Column(name = "gra_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime graUltModFecha;

    @Size(max = 45)
    @Column(name = "gra_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String graUltModUsuario;

    @Column(name = "gra_version")
    @Version
    private Integer graVersion;

    @OneToMany(mappedBy = "sduGrado")
    @NotAudited
    private List<SgServicioEducativo> graServicioEducativo;

    @OneToMany(mappedBy = "cpgGrado")
    @NotAudited
    private List<SgComponentePlanGrado> graCompPlanGrado;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_grados_definicion_titulo",
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            joinColumns = @JoinColumn(name = "gra_pk"),
            inverseJoinColumns = @JoinColumn(name = "dti_pk"))
    @NotAudited
    private List<SgDefinicionTitulo> graDefinicionTitulo;

    @OneToMany(mappedBy = "rgpGradoFk")
    @NotAudited
    private List<SgRelGradoPlanEstudio> graRelGradoPlanEstudio;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rgpGradoDestinoFk")
    @NotAudited
    private List<SgRelGradoPrecedencia> graPrecedentes;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rgpGradoOrigenFk")
    @NotAudited
    private List<SgRelGradoPrecedencia> graSiguientes;

    public SgGrado() {
    }

    public SgGrado(Long graPk, Integer graVersion) {
        this.graPk = graPk;
        this.graVersion = graVersion;
    }    

    
    public Long getGraPk() {
        return graPk;
    }

    public void setGraPk(Long graPk) {
        this.graPk = graPk;
    }

    public String getGraCodigo() {
        return graCodigo;
    }

    public void setGraCodigo(String graCodigo) {
        this.graCodigo = graCodigo;
    }

    public String getGraNombre() {
        return graNombre;
    }

    public void setGraNombre(String graNombre) {
        this.graNombre = graNombre;
    }

    public String getGraDescripcion() {
        return graDescripcion;
    }

    public void setGraDescripcion(String graDescripcion) {
        this.graDescripcion = graDescripcion;
    }

    public SgRelModEdModAten getGraRelacionModalidad() {
        return graRelacionModalidad;
    }

    public void setGraRelacionModalidad(SgRelModEdModAten graRelacionModalidad) {
        this.graRelacionModalidad = graRelacionModalidad;
    }

    public Integer getGraOrden() {
        return graOrden;
    }

    public void setGraOrden(Integer graOrden) {
        this.graOrden = graOrden;
    }

    public Boolean getGraHabilitado() {
        return graHabilitado;
    }

    public void setGraHabilitado(Boolean graHabilitado) {
        this.graHabilitado = graHabilitado;
    }

    public Boolean getGraRequiereNIE() {
        return graRequiereNIE;
    }

    public void setGraRequiereNIE(Boolean graRequiereNIE) {
        this.graRequiereNIE = graRequiereNIE;
    }

    public LocalDateTime getGraUltModFecha() {
        return graUltModFecha;
    }

    public void setGraUltModFecha(LocalDateTime graUltModFecha) {
        this.graUltModFecha = graUltModFecha;
    }

    public String getGraUltModUsuario() {
        return graUltModUsuario;
    }

    public void setGraUltModUsuario(String graUltModUsuario) {
        this.graUltModUsuario = graUltModUsuario;
    }

    public Integer getGraVersion() {
        return graVersion;
    }

    public void setGraVersion(Integer graVersion) {
        this.graVersion = graVersion;
    }

    public List<SgComponentePlanGrado> getGraCompPlanGrado() {
        return graCompPlanGrado;
    }

    public void setGraCompPlanGrado(List<SgComponentePlanGrado> graCompPlanGrado) {
        this.graCompPlanGrado = graCompPlanGrado;
    }

    public List<SgServicioEducativo> getGraServicioEducativo() {
        return graServicioEducativo;
    }

    public void setGraServicioEducativo(List<SgServicioEducativo> graServicioEducativo) {
        this.graServicioEducativo = graServicioEducativo;
    }

    public List<SgDefinicionTitulo> getGraDefinicionTitulo() {
        return graDefinicionTitulo;
    }

    public void setGraDefinicionTitulo(List<SgDefinicionTitulo> graDefinicionTitulo) {
        this.graDefinicionTitulo = graDefinicionTitulo;
    }

    public Integer getGraEdadMinima() {
        return graEdadMinima;
    }

    public void setGraEdadMinima(Integer graEdadMinima) {
        this.graEdadMinima = graEdadMinima;
    }

    public Integer getGraEdadMaxima() {
        return graEdadMaxima;
    }

    public void setGraEdadMaxima(Integer graEdadMaxima) {
        this.graEdadMaxima = graEdadMaxima;
    }

    public List<SgRelGradoPlanEstudio> getGraRelGradoPlanEstudio() {
        return graRelGradoPlanEstudio;
    }

    public void setGraRelGradoPlanEstudio(List<SgRelGradoPlanEstudio> graRelGradoPlanEstudio) {
        this.graRelGradoPlanEstudio = graRelGradoPlanEstudio;
    }

    public List<SgRelGradoPrecedencia> getGraPrecedentes() {
        return graPrecedentes;
    }

    public void setGraPrecedentes(List<SgRelGradoPrecedencia> graPrecedentes) {
        this.graPrecedentes = graPrecedentes;
    }

    public List<SgRelGradoPrecedencia> getGraSiguientes() {
        return graSiguientes;
    }

    public void setGraSiguientes(List<SgRelGradoPrecedencia> graSiguientes) {
        this.graSiguientes = graSiguientes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (graPk != null ? graPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgGrado)) {
            return false;
        }
        SgGrado other = (SgGrado) object;
        if ((this.graPk == null && other.graPk != null) || (this.graPk != null && !this.graPk.equals(other.graPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgGrados[ graPk=" + graPk + " ]";
    }

}
