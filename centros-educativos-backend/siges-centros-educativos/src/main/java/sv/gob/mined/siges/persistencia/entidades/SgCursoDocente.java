/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
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
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumEstadoCurso;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCategoriaCurso;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspecialidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoModulo;

@Entity
@Table(name = "sg_cursos_docentes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdsPk", scope = SgCursoDocente.class)
public class SgCursoDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cds_pk")
    private Long cdsPk;
    
    @JoinColumn(name = "cds_modulo_fk")
    @OneToOne
    private SgModuloFormacionDocente cdsModulo;
    
    @Size(max = 45)
    @Column(name = "cds_nombre", length = 45)
    private String cdsNombre;
    
    @Size(max = 255)
    @Column(name = "cds_descripcion", length = 255)
    private String cdsDescripcion;
    
    @Size(max = 100)
    @Column(name = "cds_facilitador", length = 100)
    private String cdsFacilitador;
    
    @Column(name = "cds_fecha_inicio")
    private LocalDate cdsFechaInicio;
    
    @Column(name = "cds_fecha_fin")
    private LocalDate cdsFechaFin;
    
    @JoinColumn(name = "cds_categoria_fk", referencedColumnName = "ccu_pk")
    @ManyToOne
    private SgCategoriaCurso cdsCategoria;
    
    @JoinColumn(name = "cds_tipo_modulo_fk", referencedColumnName = "tmo_pk")
    @ManyToOne
    private SgTipoModulo cdsTipoModulo;
    
    @JoinColumn(name = "cds_nivel_fk", referencedColumnName = "niv_pk")
    @ManyToOne
    private SgNivel cdsNivel;
    
    @JoinColumn(name = "cds_especialidad_fk", referencedColumnName = "esp_pk")
    @ManyToOne
    private SgEspecialidad cdsEspecialidad;
    
    @Column(name = "cds_cupo")
    private Integer cdsCupo;
    
    @JoinColumn(name = "cds_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede cdsSede;
    
    @Column(name = "cds_otra_sede")
    private Boolean cdsOtraSede;
    
    @Size(max = 100)
    @Column(name = "cds_sede_nombre", length = 100)
    private String cdsSedeNombre;
    
    @JoinColumn(name = "cds_sede_direccion_fk")
    @OneToOne(cascade = CascadeType.ALL)
    private SgDireccion cdsSedeDireccion;
    
    @Column(name = "cds_admite_inscripcion_solicitud")
    private Boolean cdsAdmiteInscripcionSolicitud;
    
    @Column(name = "cds_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoCurso cdsEstado;
    
    @Column(name = "cds_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cdsUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cds_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cdsUltModUsuario;
    
    @Column(name = "cds_version")
    @Version
    private Integer cdsVersion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cdcCurso", orphanRemoval = true)
    @NotAudited
    private List<SgCeldaDiaHoraCurso> cdsCeldasDiaHora;

    public SgCursoDocente() {
    }

    public SgCursoDocente(Long cdsPk) {
        this.cdsPk = cdsPk;
    }

    public Long getCdsPk() {
        return cdsPk;
    }

    public void setCdsPk(Long cdsPk) {
        this.cdsPk = cdsPk;
    }

    public SgModuloFormacionDocente getCdsModulo() {
        return cdsModulo;
    }

    public void setCdsModulo(SgModuloFormacionDocente cdsModulo) {
        this.cdsModulo = cdsModulo;
    }

    public String getCdsNombre() {
        return cdsNombre;
    }

    public void setCdsNombre(String cdsNombre) {
        this.cdsNombre = cdsNombre;
    }

    public String getCdsDescripcion() {
        return cdsDescripcion;
    }

    public void setCdsDescripcion(String cdsDescripcion) {
        this.cdsDescripcion = cdsDescripcion;
    }

    public String getCdsFacilitador() {
        return cdsFacilitador;
    }

    public void setCdsFacilitador(String cdsFacilitador) {
        this.cdsFacilitador = cdsFacilitador;
    }

    public LocalDate getCdsFechaInicio() {
        return cdsFechaInicio;
    }

    public void setCdsFechaInicio(LocalDate cdsFechaInicio) {
        this.cdsFechaInicio = cdsFechaInicio;
    }

    public LocalDate getCdsFechaFin() {
        return cdsFechaFin;
    }

    public void setCdsFechaFin(LocalDate cdsFechaFin) {
        this.cdsFechaFin = cdsFechaFin;
    }

    public SgCategoriaCurso getCdsCategoria() {
        return cdsCategoria;
    }

    public void setCdsCategoria(SgCategoriaCurso cdsCategoria) {
        this.cdsCategoria = cdsCategoria;
    }

    public SgTipoModulo getCdsTipoModulo() {
        return cdsTipoModulo;
    }

    public void setCdsTipoModulo(SgTipoModulo cdsTipoModulo) {
        this.cdsTipoModulo = cdsTipoModulo;
    }

    public SgNivel getCdsNivel() {
        return cdsNivel;
    }

    public void setCdsNivel(SgNivel cdsNivel) {
        this.cdsNivel = cdsNivel;
    }

    public SgEspecialidad getCdsEspecialidad() {
        return cdsEspecialidad;
    }

    public void setCdsEspecialidad(SgEspecialidad cdsEspecialidad) {
        this.cdsEspecialidad = cdsEspecialidad;
    }

    public Integer getCdsCupo() {
        return cdsCupo;
    }

    public void setCdsCupo(Integer cdsCupo) {
        this.cdsCupo = cdsCupo;
    }

    public SgSede getCdsSede() {
        return cdsSede;
    }

    public void setCdsSede(SgSede cdsSede) {
        this.cdsSede = cdsSede;
    }

    public Boolean getCdsOtraSede() {
        return cdsOtraSede;
    }

    public void setCdsOtraSede(Boolean cdsOtraSede) {
        this.cdsOtraSede = cdsOtraSede;
    }

    public String getCdsSedeNombre() {
        return cdsSedeNombre;
    }

    public void setCdsSedeNombre(String cdsSedeNombre) {
        this.cdsSedeNombre = cdsSedeNombre;
    }

    public SgDireccion getCdsSedeDireccion() {
        return cdsSedeDireccion;
    }

    public void setCdsSedeDireccion(SgDireccion cdsSedeDireccion) {
        this.cdsSedeDireccion = cdsSedeDireccion;
    }

    public Boolean getCdsAdmiteInscripcionSolicitud() {
        return cdsAdmiteInscripcionSolicitud;
    }

    public void setCdsAdmiteInscripcionSolicitud(Boolean cdsAdmiteInscripcionSolicitud) {
        this.cdsAdmiteInscripcionSolicitud = cdsAdmiteInscripcionSolicitud;
    }

    public EnumEstadoCurso getCdsEstado() {
        return cdsEstado;
    }

    public void setCdsEstado(EnumEstadoCurso cdsEstado) {
        this.cdsEstado = cdsEstado;
    }

    public LocalDateTime getCdsUltModFecha() {
        return cdsUltModFecha;
    }

    public void setCdsUltModFecha(LocalDateTime cdsUltModFecha) {
        this.cdsUltModFecha = cdsUltModFecha;
    }

    public String getCdsUltModUsuario() {
        return cdsUltModUsuario;
    }

    public void setCdsUltModUsuario(String cdsUltModUsuario) {
        this.cdsUltModUsuario = cdsUltModUsuario;
    }

    public Integer getCdsVersion() {
        return cdsVersion;
    }

    public void setCdsVersion(Integer cdsVersion) {
        this.cdsVersion = cdsVersion;
    }

    public List<SgCeldaDiaHoraCurso> getCdsCeldasDiaHora() {
        return cdsCeldasDiaHora;
    }

    public void setCdsCeldasDiaHora(List<SgCeldaDiaHoraCurso> cdsCeldasDiaHora) {
        this.cdsCeldasDiaHora = cdsCeldasDiaHora;
    }


    
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdsPk != null ? cdsPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCursoDocente)) {
            return false;
        }
        SgCursoDocente other = (SgCursoDocente) object;
        if ((this.cdsPk == null && other.cdsPk != null) || (this.cdsPk != null && !this.cdsPk.equals(other.cdsPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCursoDocente[ cdsPk=" + cdsPk + " ]";
    }
    
}
