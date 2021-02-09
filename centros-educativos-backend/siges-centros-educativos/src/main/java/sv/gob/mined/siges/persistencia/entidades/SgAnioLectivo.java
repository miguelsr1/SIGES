/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumAnioLectivoEstado;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_anio_lectivo",uniqueConstraints = {
    @UniqueConstraint(name = "ale_anio_uk", columnNames = {"ale_anio"})},
        schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "alePk", scope = SgAnioLectivo.class)
public class SgAnioLectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ale_pk")
    private Long alePk;
    
    @Column(name = "ale_anio")
    private Integer aleAnio;
    
    @Size(max = 255)
    @Column(name = "ale_descripcion",length = 255)
    private String aleDescripcion;
    
    @Column(name = "ale_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumAnioLectivoEstado aleEstado;
    
    @Column(name = "ale_corriente")
    private Boolean aleCorriente;
    
    @Column(name = "ale_habilitado_promedios")
    private Boolean aleHabilitadoPromedios;
    
    @Column(name = "ale_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime aleUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ale_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String aleUltModUsuario;
    
    @Column(name = "ale_version")
    @Version
    private Integer aleVersion;
    
    @OneToMany(mappedBy = "secAnioLectivo")
    @NotAudited
    private List<SgSeccion> aleSeccion;
    

    public SgAnioLectivo() {
    }

    public SgAnioLectivo(Long alePk) {
        this.alePk = alePk;
    }

    public SgAnioLectivo(Long alePk, Integer aleVersion) {
        this.alePk = alePk;
        this.aleVersion = aleVersion;
    }
    

    public Long getAlePk() {
        return alePk;
    }

    public void setAlePk(Long alePk) {
        this.alePk = alePk;
    }

    public Integer getAleAnio() {
        return aleAnio;
    }

    public void setAleAnio(Integer aleAnio) {
        this.aleAnio = aleAnio;
    }

    public String getAleDescripcion() {
        return aleDescripcion;
    }

    public void setAleDescripcion(String aleDescripcion) {
        this.aleDescripcion = aleDescripcion;
    }

    public EnumAnioLectivoEstado getAleEstado() {
        return aleEstado;
    }

    public void setAleEstado(EnumAnioLectivoEstado aleEstado) {
        this.aleEstado = aleEstado;
    }

    public LocalDateTime getAleUltModFecha() {
        return aleUltModFecha;
    }

    public void setAleUltModFecha(LocalDateTime aleUltModFecha) {
        this.aleUltModFecha = aleUltModFecha;
    }

    public String getAleUltModUsuario() {
        return aleUltModUsuario;
    }

    public void setAleUltModUsuario(String aleUltModUsuario) {
        this.aleUltModUsuario = aleUltModUsuario;
    }

    public Integer getAleVersion() {
        return aleVersion;
    }

    public void setAleVersion(Integer aleVersion) {
        this.aleVersion = aleVersion;
    }

    public List<SgSeccion> getAleSeccion() {
        return aleSeccion;
    }

    public void setAleSeccion(List<SgSeccion> aleSeccion) {
        this.aleSeccion = aleSeccion;
    }

    public Boolean getAleCorriente() {
        return aleCorriente;
    }

    public void setAleCorriente(Boolean aleCorriente) {
        this.aleCorriente = aleCorriente;
    }

    public Boolean getAleHabilitadoPromedios() {
        return aleHabilitadoPromedios;
    }

    public void setAleHabilitadoPromedios(Boolean aleHabilitadoPromedios) {
        this.aleHabilitadoPromedios = aleHabilitadoPromedios;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alePk != null ? alePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAnioLectivo)) {
            return false;
        }
        SgAnioLectivo other = (SgAnioLectivo) object;
        if ((this.alePk == null && other.alePk != null) || (this.alePk != null && !this.alePk.equals(other.alePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAnioLectivo[ alePk=" + alePk + " ]";
    }
    
}
