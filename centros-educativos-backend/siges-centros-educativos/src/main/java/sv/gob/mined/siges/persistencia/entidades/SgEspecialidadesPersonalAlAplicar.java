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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspecialidad;

@Entity
@Table(name = "sg_especialidades_personal_al_aplicar", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "epaPk", scope = SgEspecialidadesPersonalAlAplicar.class)
@Audited
public class SgEspecialidadesPersonalAlAplicar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "epa_pk")
    private Long epaPk;
   
    @JoinColumn(name = "epa_aplicacion_plaza_fk", referencedColumnName = "apl_pk")
    @ManyToOne
    private SgAplicacionPlaza epaAplicacionPlazaFk;
    
    @JoinColumn(name = "epa_especialidad_fk", referencedColumnName = "esp_pk")
    @ManyToOne
    private SgEspecialidad epaEspecialidad;
    
    @Column(name = "epa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime epaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "epa_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String epaUltModUsuario;
    
    @Column(name = "epa_version")
    @Version
    private Integer epaVersion;
    
    @Column(name = "epa_fecha_graduacion")
    private LocalDate epaFechaGraduacion;
    
    @Column(name = "epa_cum")
    private Double epaCum;

    public SgEspecialidadesPersonalAlAplicar() {
    }

    public Long getEpaPk() {
        return epaPk;
    }

    public void setEpaPk(Long epaPk) {
        this.epaPk = epaPk;
    }

    public SgAplicacionPlaza getEpaAplicacionPlazaFk() {
        return epaAplicacionPlazaFk;
    }

    public void setEpaAplicacionPlazaFk(SgAplicacionPlaza epaAplicacionPlazaFk) {
        this.epaAplicacionPlazaFk = epaAplicacionPlazaFk;
    }

    

    public SgEspecialidad getEpaEspecialidad() {
        return epaEspecialidad;
    }

    public void setEpaEspecialidad(SgEspecialidad epaEspecialidad) {
        this.epaEspecialidad = epaEspecialidad;
    }

    public LocalDateTime getEpaUltModFecha() {
        return epaUltModFecha;
    }

    public void setEpaUltModFecha(LocalDateTime epaUltModFecha) {
        this.epaUltModFecha = epaUltModFecha;
    }

    public String getEpaUltModUsuario() {
        return epaUltModUsuario;
    }

    public void setEpaUltModUsuario(String epaUltModUsuario) {
        this.epaUltModUsuario = epaUltModUsuario;
    }

    public Integer getEpaVersion() {
        return epaVersion;
    }

    public void setEpaVersion(Integer epaVersion) {
        this.epaVersion = epaVersion;
    }

    public LocalDate getEpaFechaGraduacion() {
        return epaFechaGraduacion;
    }

    public void setEpaFechaGraduacion(LocalDate epaFechaGraduacion) {
        this.epaFechaGraduacion = epaFechaGraduacion;
    }

    public Double getEpaCum() {
        return epaCum;
    }

    public void setEpaCum(Double epaCum) {
        this.epaCum = epaCum;
    }
    

    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (epaPk != null ? epaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEspecialidadesPersonalAlAplicar)) {
            return false;
        }
        SgEspecialidadesPersonalAlAplicar other = (SgEspecialidadesPersonalAlAplicar) object;
        if ((this.epaPk == null && other.epaPk != null) || (this.epaPk != null && !this.epaPk.equals(other.epaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad[ epaPk=" + epaPk + " ]";
    }
    
}
