/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.ResultadoOperacion;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "audPk", scope = SgRegistroAuditoria.class)
public class SgRegistroAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long audPk;

    private String audIp;

    private String audOperacion;

    private String audClase;
    
    private Long audEntidadId;

    private String audComentario;

    private ResultadoOperacion audResultadoOperacion;

    private LocalDateTime audFecha;

    private String audExcepcion;

    private String audCodigoUsuario;

    private String audHashMD5;

    public SgRegistroAuditoria() {
    }

    public Long getAudPk() {
        return audPk;
    }

    public void setAudPk(Long audPk) {
        this.audPk = audPk;
    }

    public String getAudIp() {
        return audIp;
    }

    public void setAudIp(String audIp) {
        this.audIp = audIp;
    }

    public String getAudOperacion() {
        return audOperacion;
    }

    public void setAudOperacion(String audOperacion) {
        this.audOperacion = audOperacion;
    }

    public String getAudClase() {
        return audClase;
    }

    public void setAudClase(String audClase) {
        this.audClase = audClase;
    }

    public ResultadoOperacion getAudResultadoOperacion() {
        return audResultadoOperacion;
    }

    public void setAudResultadoOperacion(ResultadoOperacion audResultadoOperacion) {
        this.audResultadoOperacion = audResultadoOperacion;
    }

    public Long getAudEntidadId() {
        return audEntidadId;
    }

    public void setAudEntidadId(Long audEntidadId) {
        this.audEntidadId = audEntidadId;
    }

    public String getAudComentario() {
        return audComentario;
    }

    public void setAudComentario(String audComentario) {
        this.audComentario = audComentario;
    }

    public LocalDateTime getAudFecha() {
        return audFecha;
    }

    public void setAudFecha(LocalDateTime audFecha) {
        this.audFecha = audFecha;
    }

    public String getAudExcepcion() {
        return audExcepcion;
    }

    public void setAudExcepcion(String audExcepcion) {
        this.audExcepcion = audExcepcion;
    }

    public String getAudCodigoUsuario() {
        return audCodigoUsuario;
    }

    public void setAudCodigoUsuario(String audCodigoUsuario) {
        this.audCodigoUsuario = audCodigoUsuario;
    }

    public String getAudHashMD5() {
        return audHashMD5;
    }

    public void setAudHashMD5(String audHashMD5) {
        this.audHashMD5 = audHashMD5;
    }

    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.audPk);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgRegistroAuditoria other = (SgRegistroAuditoria) obj;
        if (!Objects.equals(this.audPk, other.audPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesRegistroAuditoria{" + "audPk=" + audPk + ", audIp=" + audIp + ", audOperacion=" + audOperacion + ", audClase=" + audClase + ", audEntidadId=" + audEntidadId + ", audComentario=" + audComentario + ", audResultadoOperacion=" + audResultadoOperacion + ", audFecha=" + audFecha + ", audExcepcion=" + audExcepcion + ", audCodigoUsuario=" + audCodigoUsuario + ", audHashMD5=" + audHashMD5 + '}';
    }

    

}
