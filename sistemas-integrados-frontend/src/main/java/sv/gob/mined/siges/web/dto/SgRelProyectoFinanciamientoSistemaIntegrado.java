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
import sv.gob.mined.siges.web.dto.catalogo.SgProyectoFinanciamientoSistemaIntegrado;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rpsPk", scope = SgRelProyectoFinanciamientoSistemaIntegrado.class)
public class SgRelProyectoFinanciamientoSistemaIntegrado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rpsPk;

    private SgSistemaIntegrado rpsSistemaIntegrado;

    private SgProyectoFinanciamientoSistemaIntegrado rpsProyectoFinanciamiento;

    private LocalDateTime rpsUltModFecha;

    private String rpsUltModUsuario;

    private Integer rpsVersion;

    public SgRelProyectoFinanciamientoSistemaIntegrado() {
    }

    public Long getRpsPk() {
        return rpsPk;
    }

    public void setRpsPk(Long rpsPk) {
        this.rpsPk = rpsPk;
    }

    public SgSistemaIntegrado getRpsSistemaIntegrado() {
        return rpsSistemaIntegrado;
    }

    public void setRpsSistemaIntegrado(SgSistemaIntegrado rpsSistemaIntegrado) {
        this.rpsSistemaIntegrado = rpsSistemaIntegrado;
    }

    public SgProyectoFinanciamientoSistemaIntegrado getRpsProyectoFinanciamiento() {
        return rpsProyectoFinanciamiento;
    }

    public void setRpsProyectoFinanciamiento(SgProyectoFinanciamientoSistemaIntegrado rpsProyectoFinanciamiento) {
        this.rpsProyectoFinanciamiento = rpsProyectoFinanciamiento;
    }

    public LocalDateTime getRpsUltModFecha() {
        return rpsUltModFecha;
    }

    public void setRpsUltModFecha(LocalDateTime rpsUltModFecha) {
        this.rpsUltModFecha = rpsUltModFecha;
    }

    public String getRpsUltModUsuario() {
        return rpsUltModUsuario;
    }

    public void setRpsUltModUsuario(String rpsUltModUsuario) {
        this.rpsUltModUsuario = rpsUltModUsuario;
    }

    public Integer getRpsVersion() {
        return rpsVersion;
    }

    public void setRpsVersion(Integer rpsVersion) {
        this.rpsVersion = rpsVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rpsPk != null ? rpsPk.hashCode() : 0);
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
        final SgRelProyectoFinanciamientoSistemaIntegrado other = (SgRelProyectoFinanciamientoSistemaIntegrado) obj;
        if (!Objects.equals(this.rpsPk, other.rpsPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelAcuerdoPropuestaPedagogica[ rpsPk=" + rpsPk + " ]";
    }

}
