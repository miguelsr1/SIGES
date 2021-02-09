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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProgramaEducativo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_planes_estudio", uniqueConstraints = {
    @UniqueConstraint(name = "pes_codigo_uk", columnNames = {"pes_codigo"})}, 
        schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "pesPk", scope = SgPlanEstudio.class)
@Audited
public class SgPlanEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pes_pk")
    private Long pesPk;
    
    @Size(max = 15)
    @Column(name = "pes_codigo",length = 15)
    @AtributoCodigo
    private String pesCodigo;
    
    @Size(max = 255)
    @Column(name = "pes_nombre",length = 255)
    @AtributoNormalizable
    private String pesNombre;
    
    @Size(max = 255)
    @Column(name = "pes_nombre_busqueda", length = 255)
    @AtributoNombre
    private String pesNombreBusqueda;
    
    @Size(max = 500)
    @Column(name = "pes_descripcion",length = 500)
    private String pesDescripcion;
    
    @Column(name = "pes_vigente")
    private Boolean pesVigente;
    
    @Column(name = "pes_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pesUltModFecha;
    
    @Size(max = 45)
    @Column(name = "pes_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String pesUltModUsuario;
    
    @Column(name = "pes_version")
    @Version
    private Integer pesVersion;
    
    @JoinColumn(name = "pes_relacion_modalidad_fk", referencedColumnName = "rea_pk")
    @ManyToOne(optional = false)
    private SgRelModEdModAten pesRelacionModalidad;
    
    @JoinColumn(name = "pes_opcion_fk", referencedColumnName = "opc_pk")
    @ManyToOne(optional = true)
    private SgOpcion pesOpcion;
    
    @JoinColumn(name = "pes_programa_educativo_fk", referencedColumnName = "ped_pk")
    @ManyToOne(optional = true)
    private SgProgramaEducativo pesProgramaEducativo;
    
    @OneToMany(mappedBy = "cpgPlanEstudio")
    @NotAudited
    private List<SgComponentePlanGrado> pesPlanGrado;


    @PrePersist
    @PreUpdate
    public void preSave() {
        this.pesNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.pesNombre);
    }

    public SgPlanEstudio(Long pesPk, Integer pesVersion) {
        this.pesPk = pesPk;
        this.pesVersion = pesVersion;
    }
     
    public SgPlanEstudio() {
    }

    public SgPlanEstudio(Long pesPk) {
        this.pesPk = pesPk;
    }

    public Long getPesPk() {
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;
    }

    public String getPesCodigo() {
        return pesCodigo;
    }

    public void setPesCodigo(String pesCodigo) {
        this.pesCodigo = pesCodigo;
    }

    public String getPesNombre() {
        return pesNombre;
    }

    public void setPesNombre(String pesNombre) {
        this.pesNombre = pesNombre;
    }

    public String getPesNombreBusqueda() {
        return pesNombreBusqueda;
    }

    public void setPesNombreBusqueda(String pesNombreBusqueda) {
        this.pesNombreBusqueda = pesNombreBusqueda;
    }    
    
    public Boolean getPesVigente() {
        return pesVigente;
    }

    public void setPesVigente(Boolean pesVigente) {
        this.pesVigente = pesVigente;
    }

    public LocalDateTime getPesUltModFecha() {
        return pesUltModFecha;
    }

    public void setPesUltModFecha(LocalDateTime pesUltModFecha) {
        this.pesUltModFecha = pesUltModFecha;
    }

    public String getPesUltModUsuario() {
        return pesUltModUsuario;
    }

    public void setPesUltModUsuario(String pesUltModUsuario) {
        this.pesUltModUsuario = pesUltModUsuario;
    }

    public Integer getPesVersion() {
        return pesVersion;
    }

    public void setPesVersion(Integer pesVersion) {
        this.pesVersion = pesVersion;
    }

    public SgRelModEdModAten getPesRelacionModalidad() {
        return pesRelacionModalidad;
    }

    public void setPesRelacionModalidad(SgRelModEdModAten pesRelacionModalidad) {
        this.pesRelacionModalidad = pesRelacionModalidad;
    }

    public String getPesDescripcion() {
        return pesDescripcion;
    }

    public void setPesDescripcion(String pesDescripcion) {
        this.pesDescripcion = pesDescripcion;
    }


    public SgOpcion getPesOpcion() {
        return pesOpcion;
    }

    public void setPesOpcion(SgOpcion pesOpcion) {
        this.pesOpcion = pesOpcion;
    }

    public SgProgramaEducativo getPesProgramaEducativo() {
        return pesProgramaEducativo;
    }

    public void setPesProgramaEducativo(SgProgramaEducativo pesProgramaEducativo) {
        this.pesProgramaEducativo = pesProgramaEducativo;
    }


    public List<SgComponentePlanGrado> getPesPlanGrado() {
        return pesPlanGrado;
    }

    public void setPesPlanGrado(List<SgComponentePlanGrado> pesPlanGrado) {
        this.pesPlanGrado = pesPlanGrado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pesPk != null ? pesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPlanEstudio)) {
            return false;
        }
        SgPlanEstudio other = (SgPlanEstudio) object;
        if ((this.pesPk == null && other.pesPk != null) || (this.pesPk != null && !this.pesPk.equals(other.pesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPlanesEstudio[ pesPk=" + pesPk + " ]";
    }
    
}
