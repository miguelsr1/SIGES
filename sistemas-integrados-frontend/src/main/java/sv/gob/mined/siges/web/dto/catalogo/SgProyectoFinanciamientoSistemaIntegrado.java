/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pfsPk", scope = SgProyectoFinanciamientoSistemaIntegrado.class)

public class SgProyectoFinanciamientoSistemaIntegrado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pfsPk;

    private String pfsCodigo;

    private String pfsNombre;

    private String pfsNombreBusqueda;

    private Boolean pfsHabilitado;

    private LocalDateTime pfsUltModFecha;

    private String pfsUltModUsuario;

    private Integer pfsVersion;

    public SgProyectoFinanciamientoSistemaIntegrado() {
    }

    public Long getPfsPk() {
        return pfsPk;
    }

    public void setPfsPk(Long pfsPk) {
        this.pfsPk = pfsPk;
    }

    public String getPfsCodigo() {
        return pfsCodigo;
    }

    public void setPfsCodigo(String pfsCodigo) {
        this.pfsCodigo = pfsCodigo;
    }

    public String getPfsNombre() {
        return pfsNombre;
    }

    public void setPfsNombre(String pfsNombre) {
        this.pfsNombre = pfsNombre;
    }

    public String getPfsNombreBusqueda() {
        return pfsNombreBusqueda;
    }

    public void setPfsNombreBusqueda(String pfsNombreBusqueda) {
        this.pfsNombreBusqueda = pfsNombreBusqueda;
    }

    public Boolean getPfsHabilitado() {
        return pfsHabilitado;
    }

    public void setPfsHabilitado(Boolean pfsHabilitado) {
        this.pfsHabilitado = pfsHabilitado;
    }

    public LocalDateTime getPfsUltModFecha() {
        return pfsUltModFecha;
    }

    public void setPfsUltModFecha(LocalDateTime pfsUltModFecha) {
        this.pfsUltModFecha = pfsUltModFecha;
    }

    public String getPfsUltModUsuario() {
        return pfsUltModUsuario;
    }

    public void setPfsUltModUsuario(String pfsUltModUsuario) {
        this.pfsUltModUsuario = pfsUltModUsuario;
    }

    public Integer getPfsVersion() {
        return pfsVersion;
    }

    public void setPfsVersion(Integer pfsVersion) {
        this.pfsVersion = pfsVersion;
    }

    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.pfsPk);
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
        final SgProyectoFinanciamientoSistemaIntegrado other = (SgProyectoFinanciamientoSistemaIntegrado) obj;
        if (!Objects.equals(this.pfsPk, other.pfsPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgProyectoFinanciamientoSistemaIntegrado{" + "pfsPk=" + pfsPk + ", pfsCodigo=" + pfsCodigo + ", pfsNombre=" + pfsNombre + ", pfsNombreBusqueda=" + pfsNombreBusqueda + ", pfsHabilitado=" + pfsHabilitado + ", pfsUltModFecha=" + pfsUltModFecha + ", pfsUltModUsuario=" + pfsUltModUsuario + ", pfsVersion=" + pfsVersion + '}';
    }
    
    

}
