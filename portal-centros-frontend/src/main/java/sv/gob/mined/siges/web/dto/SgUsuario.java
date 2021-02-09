/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "usuPk", scope = SgUsuario.class)
public class SgUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long usuPk;

    private String usuCodigo;

    private String usuNombre;

    private String usuEmail;
    
    private Boolean usuHabilitado;

    private String usuPassword;

    private SgArchivo usuFoto;

    private LocalDateTime usuUltModFecha;

    private String usuUltModUsuario;

    private Integer usuVersion;
    
    private Boolean usuAceptaTerminos;

    public SgUsuario() {
    }

    public Long getUsuPk() {
        return usuPk;
    }

    public void setUsuPk(Long usuPk) {
        this.usuPk = usuPk;
    }

    public String getUsuCodigo() {
        return usuCodigo;
    }

    public void setUsuCodigo(String usuCodigo) {
        this.usuCodigo = usuCodigo;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public LocalDateTime getUsuUltModFecha() {
        return usuUltModFecha;
    }

    public void setUsuUltModFecha(LocalDateTime usuUltModFecha) {
        this.usuUltModFecha = usuUltModFecha;
    }

    public String getUsuUltModUsuario() {
        return usuUltModUsuario;
    }

    public void setUsuUltModUsuario(String usuUltModUsuario) {
        this.usuUltModUsuario = usuUltModUsuario;
    }

    public Integer getUsuVersion() {
        return usuVersion;
    }

    public void setUsuVersion(Integer usuVersion) {
        this.usuVersion = usuVersion;
    }

    public SgArchivo getUsuFoto() {
        return usuFoto;
    }

    public void setUsuFoto(SgArchivo usuFoto) {
        this.usuFoto = usuFoto;
    }

    public String getUsuEmail() {
        return usuEmail;
    }

    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }

    public Boolean getUsuAceptaTerminos() {
        return usuAceptaTerminos;
    }

    public void setUsuAceptaTerminos(Boolean usuAceptaTerminos) {
        this.usuAceptaTerminos = usuAceptaTerminos;
    }

    public String getUsuPassword() {
        return usuPassword;
    }

    public void setUsuPassword(String usuPassword) {
        this.usuPassword = usuPassword;
    }

    public Boolean getUsuHabilitado() {
        return usuHabilitado;
    }


    public void setUsuHabilitado(Boolean usuHabilitado) {
        this.usuHabilitado = usuHabilitado;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuPk != null ? usuPk.hashCode() : 0);
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
        final SgUsuario other = (SgUsuario) obj;
        if (!Objects.equals(this.usuPk, other.usuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgUsuario[ usuPk=" + usuPk + " ]";
    }

}
