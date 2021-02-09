/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "alcPk", scope = SgAlcanceExtraccion.class)
public class SgAlcanceExtraccion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long alcPk;

    private Long alcOrganizacionPk;

    private Long alcNivelPk;

    private Long alcCicloPk;

    private Long alcModalidadPk;
    
    private Long alcModalidadAtencionPk;
    
    private Long alcSubModalidadAtencionPk;
    
    private Long alcGradoPk;

    private LocalDate alcFechaMatriculas;
    
    private SgExtraccion alcExtraccion;
   
    private LocalDateTime alcUltModFecha;

    private String alcUltModUsuario;

    private Integer alcVersion;

    public SgAlcanceExtraccion() {
    }

    public Long getAlcPk() {
        return alcPk;
    }

    public void setAlcPk(Long alcPk) {
        this.alcPk = alcPk;
    }

    public Long getAlcOrganizacionPk() {
        return alcOrganizacionPk;
    }

    public void setAlcOrganizacionPk(Long alcOrganizacionPk) {
        this.alcOrganizacionPk = alcOrganizacionPk;
    }

    public Long getAlcNivelPk() {
        return alcNivelPk;
    }

    public void setAlcNivelPk(Long alcNivelPk) {
        this.alcNivelPk = alcNivelPk;
    }

    public Long getAlcCicloPk() {
        return alcCicloPk;
    }

    public void setAlcCicloPk(Long alcCicloPk) {
        this.alcCicloPk = alcCicloPk;
    }

    public Long getAlcModalidadPk() {
        return alcModalidadPk;
    }

    public void setAlcModalidadPk(Long alcModalidadPk) {
        this.alcModalidadPk = alcModalidadPk;
    }

    public Long getAlcModalidadAtencionPk() {
        return alcModalidadAtencionPk;
    }

    public void setAlcModalidadAtencionPk(Long alcModalidadAtencionPk) {
        this.alcModalidadAtencionPk = alcModalidadAtencionPk;
    }

    public Long getAlcSubModalidadAtencionPk() {
        return alcSubModalidadAtencionPk;
    }

    public void setAlcSubModalidadAtencionPk(Long alcSubModalidadAtencionPk) {
        this.alcSubModalidadAtencionPk = alcSubModalidadAtencionPk;
    }

    public Long getAlcGradoPk() {
        return alcGradoPk;
    }

    public void setAlcGradoPk(Long alcGradoPk) {
        this.alcGradoPk = alcGradoPk;
    }
    
    public LocalDate getAlcFechaMatriculas() {
        return alcFechaMatriculas;
    }

    public void setAlcFechaMatriculas(LocalDate alcFechaMatriculas) {
        this.alcFechaMatriculas = alcFechaMatriculas;
    }

    public LocalDateTime getAlcUltModFecha() {
        return alcUltModFecha;
    }

    public void setAlcUltModFecha(LocalDateTime alcUltModFecha) {
        this.alcUltModFecha = alcUltModFecha;
    }

    public String getAlcUltModUsuario() {
        return alcUltModUsuario;
    }

    public void setAlcUltModUsuario(String alcUltModUsuario) {
        this.alcUltModUsuario = alcUltModUsuario;
    }

    public Integer getAlcVersion() {
        return alcVersion;
    }

    public void setAlcVersion(Integer alcVersion) {
        this.alcVersion = alcVersion;
    }

    public SgExtraccion getAlcExtraccion() {
        return alcExtraccion;
    }

    public void setAlcExtraccion(SgExtraccion alcExtraccion) {
        this.alcExtraccion = alcExtraccion;
    }

   
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.alcPk);
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
        final SgAlcanceExtraccion other = (SgAlcanceExtraccion) obj;
        if (!Objects.equals(this.alcPk, other.alcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAlcanceExtraccion{" + "alcPk=" + alcPk + ", alcOrganizacionPk=" + alcOrganizacionPk + ", alcNivelPk=" + alcNivelPk + ", alcCicloPk=" + alcCicloPk + ", alcModalidadPk=" + alcModalidadPk + ", alcModalidadAtencionPk=" + alcModalidadAtencionPk + ", alcSubModalidadAtencionPk=" + alcSubModalidadAtencionPk + ", alcGradoPk=" + alcGradoPk + ", alcFechaMatriculas=" + alcFechaMatriculas + '}';
    }

    
    
    
    


}
