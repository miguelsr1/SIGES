/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_proyectos_inst_estudiantes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "piePk", scope = SgProyectoInstEstudiante.class)
@Audited
public class SgProyectoInstEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pie_pk")
    private Long piePk;
    
    @JoinColumn(name = "pie_proyecto_institucional_fk", referencedColumnName = "pro_pk")
    @ManyToOne
    private SgProyectoInstitucionalSede pieProyectoInstitucional;
    
    @JoinColumn(name = "pie_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante pieEstudiante;
    
    @Column(name = "pie_fecha_otorgado")
    private LocalDate pieFechaOtorgado;
    
    @Size(max = 500)
    @Column(name = "pie_observaciones", length = 500)
    private String pieObservaciones;
    
    @Column(name = "pie_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pieUltModFecha;
    
    @Size(max = 45)
    @Column(name = "pie_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pieUltModUsuario;
    
    @Column(name = "pie_version")
    @Version
    private Integer pieVersion;
    

    public SgProyectoInstEstudiante() {
    }

    public SgProyectoInstEstudiante(Long piePk) {
        this.piePk = piePk;
    }

    public Long getPiePk() {
        return piePk;
    }

    public void setPiePk(Long piePk) {
        this.piePk = piePk;
    }

    public SgProyectoInstitucionalSede getPieProyectoInstitucional() {
        return pieProyectoInstitucional;
    }

    public void setPieProyectoInstitucional(SgProyectoInstitucionalSede pieProyectoInstitucional) {
        this.pieProyectoInstitucional = pieProyectoInstitucional;
    }

    public SgEstudiante getPieEstudiante() {
        return pieEstudiante;
    }

    public void setPieEstudiante(SgEstudiante pieEstudiante) {
        this.pieEstudiante = pieEstudiante;
    }

    public LocalDate getPieFechaOtorgado() {
        return pieFechaOtorgado;
    }

    public void setPieFechaOtorgado(LocalDate pieFechaOtorgado) {
        this.pieFechaOtorgado = pieFechaOtorgado;
    }

    public String getPieObservaciones() {
        return pieObservaciones;
    }

    public void setPieObservaciones(String pieObservaciones) {
        this.pieObservaciones = pieObservaciones;
    }

    public LocalDateTime getPieUltModFecha() {
        return pieUltModFecha;
    }

    public void setPieUltModFecha(LocalDateTime pieUltModFecha) {
        this.pieUltModFecha = pieUltModFecha;
    }

    public String getPieUltModUsuario() {
        return pieUltModUsuario;
    }

    public void setPieUltModUsuario(String pieUltModUsuario) {
        this.pieUltModUsuario = pieUltModUsuario;
    }

    public Integer getPieVersion() {
        return pieVersion;
    }

    public void setPieVersion(Integer pieVersion) {
        this.pieVersion = pieVersion;
    }


    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (piePk != null ? piePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProyectoInstEstudiante)) {
            return false;
        }
        SgProyectoInstEstudiante other = (SgProyectoInstEstudiante) object;
        if ((this.piePk == null && other.piePk != null) || (this.piePk != null && !this.piePk.equals(other.piePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProyectoInstEstudiante[ piePk=" + piePk + " ]";
    }
    
}
