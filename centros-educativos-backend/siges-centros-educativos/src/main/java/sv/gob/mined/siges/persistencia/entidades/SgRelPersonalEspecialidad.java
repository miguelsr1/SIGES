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
@Table(name = "sg_rel_personal_especialidades", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rpePk", scope = SgRelPersonalEspecialidad.class)
@Audited
public class SgRelPersonalEspecialidad implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rpe_pk")
    private Long rpePk;
   
    @JoinColumn(name = "rpe_personal_fk", referencedColumnName = "pse_pk")
    @ManyToOne
    private SgPersonalSedeEducativa rpePersonal;
    
    @JoinColumn(name = "rpe_especialidad_fk", referencedColumnName = "esp_pk")
    @ManyToOne
    private SgEspecialidad rpeEspecialidad;
    
    @Column(name = "rpe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rpeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rpe_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String rpeUltModUsuario;
    
    @Column(name = "rpe_version")
    @Version
    private Integer rpeVersion;
    
    @Column(name = "rpe_fecha_graduacion")
    private LocalDate rpeFechaGraduacion;
    
    @Column(name = "rpe_cum")
    private Double rpeCum;

    public SgRelPersonalEspecialidad() {
    }

    public Long getRpePk() {
        return rpePk;
    }

    public void setRpePk(Long rpePk) {
        this.rpePk = rpePk;
    }

    public SgPersonalSedeEducativa getRpePersonal() {
        return rpePersonal;
    }

    public void setRpePersonal(SgPersonalSedeEducativa rpePersonal) {
        this.rpePersonal = rpePersonal;
    }

    public SgEspecialidad getRpeEspecialidad() {
        return rpeEspecialidad;
    }

    public void setRpeEspecialidad(SgEspecialidad rpeEspecialidad) {
        this.rpeEspecialidad = rpeEspecialidad;
    }

    public LocalDateTime getRpeUltModFecha() {
        return rpeUltModFecha;
    }

    public void setRpeUltModFecha(LocalDateTime rpeUltModFecha) {
        this.rpeUltModFecha = rpeUltModFecha;
    }

    public String getRpeUltModUsuario() {
        return rpeUltModUsuario;
    }

    public void setRpeUltModUsuario(String rpeUltModUsuario) {
        this.rpeUltModUsuario = rpeUltModUsuario;
    }

    public Integer getRpeVersion() {
        return rpeVersion;
    }

    public void setRpeVersion(Integer rpeVersion) {
        this.rpeVersion = rpeVersion;
    }

    public LocalDate getRpeFechaGraduacion() {
        return rpeFechaGraduacion;
    }

    public void setRpeFechaGraduacion(LocalDate rpeFechaGraduacion) {
        this.rpeFechaGraduacion = rpeFechaGraduacion;
    }

    public Double getRpeCum() {
        return rpeCum;
    }

    public void setRpeCum(Double rpeCum) {
        this.rpeCum = rpeCum;
    }
    

    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rpePk != null ? rpePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelPersonalEspecialidad)) {
            return false;
        }
        SgRelPersonalEspecialidad other = (SgRelPersonalEspecialidad) object;
        if ((this.rpePk == null && other.rpePk != null) || (this.rpePk != null && !this.rpePk.equals(other.rpePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad[ rpePk=" + rpePk + " ]";
    }
    
}
