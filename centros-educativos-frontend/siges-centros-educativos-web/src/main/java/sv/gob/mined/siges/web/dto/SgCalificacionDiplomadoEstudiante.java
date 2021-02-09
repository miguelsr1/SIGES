/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.utilidades.NumberFormatUtils;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdePk", scope = SgCalificacionDiplomadoEstudiante.class)
public class SgCalificacionDiplomadoEstudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cdePk;

    private SgEstudiante cdeEstudianteFk;

    private SgCalificacionDiplomado cdeCalificacionDiplomadoFk;    

    private String cdeCalificacionNumerica;

    private SgCalificacion cdeCalificacionConceptualFk;    

    private String cdeObservacion;

    private LocalDate cdeFechaRealizado;

    private LocalDateTime cdeUltModFecha;

    private String cdeUltModUsuario;

    private Integer cdeVersion;
    
    
    public SgCalificacionDiplomadoEstudiante() {
   
    }
    
    @JsonIgnore
    public String getCaeCalificacionNumerica(Integer precision) {
        if (!StringUtils.isBlank(cdeCalificacionNumerica)) {
            Double d = Double.parseDouble(cdeCalificacionNumerica);
            return NumberFormatUtils.formatDouble(d, precision);
        }
        return "";
    }

    public Long getCdePk() {
        return cdePk;
    }

    public void setCdePk(Long cdePk) {
        this.cdePk = cdePk;
    }

    public SgEstudiante getCdeEstudianteFk() {
        return cdeEstudianteFk;
    }

    public void setCdeEstudianteFk(SgEstudiante cdeEstudianteFk) {
        this.cdeEstudianteFk = cdeEstudianteFk;
    }

    public SgCalificacionDiplomado getCdeCalificacionDiplomadoFk() {
        return cdeCalificacionDiplomadoFk;
    }

    public void setCdeCalificacionDiplomadoFk(SgCalificacionDiplomado cdeCalificacionDiplomadoFk) {
        this.cdeCalificacionDiplomadoFk = cdeCalificacionDiplomadoFk;
    }

    public String getCdeCalificacionNumerica() {
        return cdeCalificacionNumerica;
    }

    public void setCdeCalificacionNumerica(String cdeCalificacionNumerica) {
        this.cdeCalificacionNumerica = cdeCalificacionNumerica;
    }

    public SgCalificacion getCdeCalificacionConceptualFk() {
        return cdeCalificacionConceptualFk;
    }

    public void setCdeCalificacionConceptualFk(SgCalificacion cdeCalificacionConceptualFk) {
        this.cdeCalificacionConceptualFk = cdeCalificacionConceptualFk;
    }

    public String getCdeObservacion() {
        return cdeObservacion;
    }

    public void setCdeObservacion(String cdeObservacion) {
        this.cdeObservacion = cdeObservacion;
    }

    public LocalDate getCdeFechaRealizado() {
        return cdeFechaRealizado;
    }

    public void setCdeFechaRealizado(LocalDate cdeFechaRealizado) {
        this.cdeFechaRealizado = cdeFechaRealizado;
    }

 

    public LocalDateTime getCdeUltModFecha() {
        return cdeUltModFecha;
    }

    public void setCdeUltModFecha(LocalDateTime cdeUltModFecha) {
        this.cdeUltModFecha = cdeUltModFecha;
    }

    public String getCdeUltModUsuario() {
        return cdeUltModUsuario;
    }

    public void setCdeUltModUsuario(String cdeUltModUsuario) {
        this.cdeUltModUsuario = cdeUltModUsuario;
    }

    public Integer getCdeVersion() {
        return cdeVersion;
    }

    public void setCdeVersion(Integer cdeVersion) {
        this.cdeVersion = cdeVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdePk != null ? cdePk.hashCode() : 0);
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
        final SgCalificacionDiplomadoEstudiante other = (SgCalificacionDiplomadoEstudiante) obj;
        if (!Objects.equals(this.cdePk, other.cdePk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalificacionDiplomadoEstudiante[ cdePk=" + cdePk + " ]";
    }
    
}
