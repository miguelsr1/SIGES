/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cviPk", scope = SgCasoViolacion.class)
public class SgCasoViolacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long cviPk;
    
    private SgSede cviSede;
    
    private SgAnioLectivo cviAnio;
    
    private Boolean cviViolaciones;
    
    private LocalDateTime cviUltModFecha;
    
    private String cviUltModUsuario;
    
    private Integer cviVersion;

    public SgCasoViolacion() {
        this.cviViolaciones = Boolean.TRUE;
    }

    public Long getCviPk() {
        return cviPk;
    }

    public void setCviPk(Long cviPk) {
        this.cviPk = cviPk;
    }

    public SgSede getCviSede() {
        return cviSede;
    }

    public void setCviSede(SgSede cviSede) {
        this.cviSede = cviSede;
    }

    public SgAnioLectivo getCviAnio() {
        return cviAnio;
    }

    public void setCviAnio(SgAnioLectivo cviAnio) {
        this.cviAnio = cviAnio;
    }

    public Boolean getCviViolaciones() {
        return cviViolaciones;
    }

    public void setCviViolaciones(Boolean cviViolaciones) {
        this.cviViolaciones = cviViolaciones;
    }

    public LocalDateTime getCviUltModFecha() {
        return cviUltModFecha;
    }

    public void setCviUltModFecha(LocalDateTime cviUltModFecha) {
        this.cviUltModFecha = cviUltModFecha;
    }

    public String getCviUltModUsuario() {
        return cviUltModUsuario;
    }

    public void setCviUltModUsuario(String cviUltModUsuario) {
        this.cviUltModUsuario = cviUltModUsuario;
    }

    public Integer getCviVersion() {
        return cviVersion;
    }

    public void setCviVersion(Integer cviVersion) {
        this.cviVersion = cviVersion;
    }



    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cviPk);
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
        final SgCasoViolacion other = (SgCasoViolacion) obj;
        if (!Objects.equals(this.cviPk, other.cviPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCasoViolacion [" + "cviPk=" + cviPk +", cviUltModFecha=" + cviUltModFecha + ", cviUltModUsuario=" + cviUltModUsuario + ", cviVersion=" + cviVersion + ']';
    }
    
    

}
