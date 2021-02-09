/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioBasico;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "risPk", scope = SgRelInmuebleServicioBasico.class)
public class SgRelInmuebleServicioBasico implements Serializable {

    private static final long serialVersionUID = 1L;

   private Long risPk;

    private String risUltModUsuario;

    private LocalDateTime risUltModFecha;    

    private Integer risVersion;    

    private SgInmueblesSedes risInmuebleSede;    

    private SgServicioBasico risServicio;
    
    public SgRelInmuebleServicioBasico() {
        
    }

    public Long getRisPk() {
        return risPk;
    }

    public void setRisPk(Long risPk) {
        this.risPk = risPk;
    }

    public String getRisUltModUsuario() {
        return risUltModUsuario;
    }

    public void setRisUltModUsuario(String risUltModUsuario) {
        this.risUltModUsuario = risUltModUsuario;
    }

    public LocalDateTime getRisUltModFecha() {
        return risUltModFecha;
    }

    public void setRisUltModFecha(LocalDateTime risUltModFecha) {
        this.risUltModFecha = risUltModFecha;
    }

    public Integer getRisVersion() {
        return risVersion;
    }

    public void setRisVersion(Integer risVersion) {
        this.risVersion = risVersion;
    }

    public SgInmueblesSedes getRisInmuebleSede() {
        return risInmuebleSede;
    }

    public void setRisInmuebleSede(SgInmueblesSedes risInmuebleSede) {
        this.risInmuebleSede = risInmuebleSede;
    }

    public SgServicioBasico getRisServicio() {
        return risServicio;
    }

    public void setRisServicio(SgServicioBasico risServicio) {
        this.risServicio = risServicio;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final SgRelInmuebleServicioBasico other = (SgRelInmuebleServicioBasico) obj;
        if (!Objects.equals(this.risPk, other.risPk)) {
            return false;
        }
        if (!Objects.equals(this.risInmuebleSede, other.risInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.risServicio, other.risServicio)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleServicioBasico{" + "risPk=" + risPk + ", risUltModUsuario=" + risUltModUsuario + ", risUltModFecha=" + risUltModFecha + ", risVersion=" + risVersion + ", risInmuebleSede=" + risInmuebleSede + ", risServicio=" + risServicio + '}';
    }

}