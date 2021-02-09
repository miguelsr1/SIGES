/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tmePk", scope = SgTipoMejora.class)
public class SgTipoMejora implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tmePk;

    private String tmeCodigo;

    private String tmeNombre;

    private Boolean tmeHabilitado;

    private LocalDateTime tmeUltModFecha;

    private String tmeUltModUsuario;

    private Integer tmeVersion;
    
    private Boolean tmeAplicaInmueble;    
    
    private Boolean tmeAplicaEdificio;
    
    
    public SgTipoMejora() {
        this.tmeHabilitado = Boolean.TRUE;
        tmeAplicaInmueble=Boolean.FALSE;
        tmeAplicaEdificio=Boolean.FALSE;
    }

    public Long getTmePk() {
        return tmePk;
    }

    public void setTmePk(Long tmePk) {
        this.tmePk = tmePk;
    }

    public String getTmeCodigo() {
        return tmeCodigo;
    }

    public void setTmeCodigo(String tmeCodigo) {
        this.tmeCodigo = tmeCodigo;
    }

    public String getTmeNombre() {
        return tmeNombre;
    }

    public void setTmeNombre(String tmeNombre) {
        this.tmeNombre = tmeNombre;
    }

    public LocalDateTime getTmeUltModFecha() {
        return tmeUltModFecha;
    }

    public void setTmeUltModFecha(LocalDateTime tmeUltModFecha) {
        this.tmeUltModFecha = tmeUltModFecha;
    }

    public String getTmeUltModUsuario() {
        return tmeUltModUsuario;
    }

    public void setTmeUltModUsuario(String tmeUltModUsuario) {
        this.tmeUltModUsuario = tmeUltModUsuario;
    }

    public Integer getTmeVersion() {
        return tmeVersion;
    }

    public void setTmeVersion(Integer tmeVersion) {
        this.tmeVersion = tmeVersion;
    }

    
     public Boolean getTmeHabilitado() {
        return tmeHabilitado;
    }

    public void setTmeHabilitado(Boolean tmeHabilitado) {
        this.tmeHabilitado = tmeHabilitado;
    }

    public Boolean getTmeAplicaInmueble() {
        return tmeAplicaInmueble;
    }

    public void setTmeAplicaInmueble(Boolean tmeAplicaInmueble) {
        this.tmeAplicaInmueble = tmeAplicaInmueble;
    }

    public Boolean getTmeAplicaEdificio() {
        return tmeAplicaEdificio;
    }

    public void setTmeAplicaEdificio(Boolean tmeAplicaEdificio) {
        this.tmeAplicaEdificio = tmeAplicaEdificio;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tmePk != null ? tmePk.hashCode() : 0);
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
        final SgTipoMejora other = (SgTipoMejora) obj;
        if (!Objects.equals(this.tmePk, other.tmePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoMejora[ tmePk=" + tmePk + " ]";
    }
    
}
