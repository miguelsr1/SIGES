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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tcoPk", scope = SgTipoConstruccion.class)
public class SgTipoConstruccion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long tcoPk;

    private String tcoCodigo;

    private String tcoNombre;

    private Boolean tcoHabilitado;

    private LocalDateTime tcoUltModFecha;

    private String tcoUltModUsuario;

    private Integer tcoVersion;
    
    
    public SgTipoConstruccion() {
        this.tcoHabilitado = Boolean.TRUE;
    }

    public Long getTcoPk() {
        return tcoPk;
    }

    public void setTcoPk(Long tcoPk) {
        this.tcoPk = tcoPk;
    }

    public String getTcoCodigo() {
        return tcoCodigo;
    }

    public void setTcoCodigo(String tcoCodigo) {
        this.tcoCodigo = tcoCodigo;
    }

    public String getTcoNombre() {
        return tcoNombre;
    }

    public void setTcoNombre(String tcoNombre) {
        this.tcoNombre = tcoNombre;
    }

    public LocalDateTime getTcoUltModFecha() {
        return tcoUltModFecha;
    }

    public void setTcoUltModFecha(LocalDateTime tcoUltModFecha) {
        this.tcoUltModFecha = tcoUltModFecha;
    }

    public String getTcoUltModUsuario() {
        return tcoUltModUsuario;
    }

    public void setTcoUltModUsuario(String tcoUltModUsuario) {
        this.tcoUltModUsuario = tcoUltModUsuario;
    }

    public Integer getTcoVersion() {
        return tcoVersion;
    }

    public void setTcoVersion(Integer tcoVersion) {
        this.tcoVersion = tcoVersion;
    }

    
     public Boolean getTcoHabilitado() {
        return tcoHabilitado;
    }

    public void setTcoHabilitado(Boolean tcoHabilitado) {
        this.tcoHabilitado = tcoHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcoPk != null ? tcoPk.hashCode() : 0);
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
        final SgTipoConstruccion other = (SgTipoConstruccion) obj;
        if (!Objects.equals(this.tcoPk, other.tcoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoConstruccion[ tcoPk=" + tcoPk + " ]";
    }
    
}
