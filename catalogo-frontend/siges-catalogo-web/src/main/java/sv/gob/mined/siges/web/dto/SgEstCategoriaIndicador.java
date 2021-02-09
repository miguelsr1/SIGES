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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cinPk", scope = SgEstCategoriaIndicador.class)
public class SgEstCategoriaIndicador implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cinPk;

    private String cinCodigo;

    private String cinNombre;

    private Boolean cinHabilitado;

    private LocalDateTime cinUltModFecha;

    private String cinUltModUsuario;

    private Integer cinVersion;
    
    
    public SgEstCategoriaIndicador() {
        this.cinHabilitado = Boolean.TRUE;
    }

    public Long getCinPk() {
        return cinPk;
    }

    public void setCinPk(Long cinPk) {
        this.cinPk = cinPk;
    }

    public String getCinCodigo() {
        return cinCodigo;
    }

    public void setCinCodigo(String cinCodigo) {
        this.cinCodigo = cinCodigo;
    }

    public String getCinNombre() {
        return cinNombre;
    }

    public void setCinNombre(String cinNombre) {
        this.cinNombre = cinNombre;
    }

    public LocalDateTime getCinUltModFecha() {
        return cinUltModFecha;
    }

    public void setCinUltModFecha(LocalDateTime cinUltModFecha) {
        this.cinUltModFecha = cinUltModFecha;
    }

    public String getCinUltModUsuario() {
        return cinUltModUsuario;
    }

    public void setCinUltModUsuario(String cinUltModUsuario) {
        this.cinUltModUsuario = cinUltModUsuario;
    }

    public Integer getCinVersion() {
        return cinVersion;
    }

    public void setCinVersion(Integer cinVersion) {
        this.cinVersion = cinVersion;
    }

    
     public Boolean getCinHabilitado() {
        return cinHabilitado;
    }

    public void setCinHabilitado(Boolean cinHabilitado) {
        this.cinHabilitado = cinHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cinPk != null ? cinPk.hashCode() : 0);
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
        final SgEstCategoriaIndicador other = (SgEstCategoriaIndicador) obj;
        if (!Objects.equals(this.cinPk, other.cinPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEstCategoriaIndicador[ cinPk=" + cinPk + " ]";
    }
    
}
