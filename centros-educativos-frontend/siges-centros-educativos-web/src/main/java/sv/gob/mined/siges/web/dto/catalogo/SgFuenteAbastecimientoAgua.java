/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "faaPk", scope = SgFuenteAbastecimientoAgua.class)
public class SgFuenteAbastecimientoAgua implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long faaPk;

    private String faaCodigo;

    private String faaNombre;

    private Boolean faaHabilitado;

    private LocalDateTime faaUltModFecha;

    private String faaUltModUsuario;

    private Integer faaVersion;
    
    
    public SgFuenteAbastecimientoAgua() {
        this.faaHabilitado = Boolean.TRUE;
    }

    public Long getFaaPk() {
        return faaPk;
    }

    public void setFaaPk(Long faaPk) {
        this.faaPk = faaPk;
    }

    public String getFaaCodigo() {
        return faaCodigo;
    }

    public void setFaaCodigo(String faaCodigo) {
        this.faaCodigo = faaCodigo;
    }

    public String getFaaNombre() {
        return faaNombre;
    }

    public void setFaaNombre(String faaNombre) {
        this.faaNombre = faaNombre;
    }

    public LocalDateTime getFaaUltModFecha() {
        return faaUltModFecha;
    }

    public void setFaaUltModFecha(LocalDateTime faaUltModFecha) {
        this.faaUltModFecha = faaUltModFecha;
    }

    public String getFaaUltModUsuario() {
        return faaUltModUsuario;
    }

    public void setFaaUltModUsuario(String faaUltModUsuario) {
        this.faaUltModUsuario = faaUltModUsuario;
    }

    public Integer getFaaVersion() {
        return faaVersion;
    }

    public void setFaaVersion(Integer faaVersion) {
        this.faaVersion = faaVersion;
    }

    
     public Boolean getFaaHabilitado() {
        return faaHabilitado;
    }

    public void setFaaHabilitado(Boolean faaHabilitado) {
        this.faaHabilitado = faaHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faaPk != null ? faaPk.hashCode() : 0);
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
        final SgFuenteAbastecimientoAgua other = (SgFuenteAbastecimientoAgua) obj;
        if (!Objects.equals(this.faaPk, other.faaPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgFuenteAbastecimientoAgua[ faaPk=" + faaPk + " ]";
    }
    
}
