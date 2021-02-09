/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.centros_educativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.centros_educativos.EnumAnioLectivoEstado;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "alePk", scope = SgAnioLectivo.class)
public class SgAnioLectivo implements Serializable {

    private Long alePk;
    
    private Integer aleAnio;
    
    private String aleDescripcion;
    
    private EnumAnioLectivoEstado aleEstado;
    
    private LocalDateTime aleUltModFecha;
    
    private String aleUltModUsuario;
    
    private Integer aleVersion;
    
    private List<SgSeccion> aleSeccion;
    

    public SgAnioLectivo() {
    }

    public SgAnioLectivo(Long alePk) {
        this.alePk = alePk;
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
