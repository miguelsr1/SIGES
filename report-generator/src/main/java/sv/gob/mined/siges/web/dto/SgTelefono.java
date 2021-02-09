/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "telPk", scope = SgTelefono.class)
public class SgTelefono implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long telPk;

    private String telTelefono;

    private LocalDateTime telUltModFecha;

    private String telUltModUsuario;

    private Integer telVersion;

    private Boolean telPkForView;

    public SgTelefono() {
    }

    public SgTelefono(Long telPk) {
        this.telPk = telPk;
    }

    public Long getTelPk() {
        return telPk;
    }

    public void setTelPk(Long telPk) {
        this.telPk = telPk;
    }

    public String getTelTelefono() {
        return telTelefono;
    }

    public void setTelTelefono(String telTelefono) {
        this.telTelefono = telTelefono;
    }

    public LocalDateTime getTelUltModFecha() {
        return telUltModFecha;
    }

    public void setTelUltModFecha(LocalDateTime telUltModFecha) {
        this.telUltModFecha = telUltModFecha;
    }

    public String getTelUltModUsuario() {
        return telUltModUsuario;
    }

    public void setTelUltModUsuario(String telUltModUsuario) {
        this.telUltModUsuario = telUltModUsuario;
    }

    public Integer getTelVersion() {
        return telVersion;
    }

    public void setTelVersion(Integer telVersion) {
        this.telVersion = telVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (telPk != null ? telPk.hashCode() : 0);
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
        final SgTelefono other = (SgTelefono) obj;
        return this.telPk != null && this.telPk.equals(other.telPk);
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesTelefono[ telPk=" + telPk + " ]";
    }

}
