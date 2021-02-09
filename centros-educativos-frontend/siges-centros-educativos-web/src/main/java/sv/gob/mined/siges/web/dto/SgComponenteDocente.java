/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Sofis Solutions
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdoPk", scope = SgComponenteDocente.class)
public class SgComponenteDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long cdoPk;
    
    private SgHorarioEscolar cdoHorarioEscolar;
    
    private SgComponentePlanEstudio cdoComponente;
    
    private SgPersonalSedeEducativaNoJsonIdentity cdoDocente;
    
    private LocalDateTime cdoUltModFecha;
    
    private String cdoUltModUsuario;
    
    private Integer cdoVersion;   
    
    private Long eliminarContextoDePersonalPk;

    public SgComponenteDocente() {
    }

    public SgComponenteDocente(Long cdoPk) {
        this.cdoPk = cdoPk;
    }

    public Long getCdoPk() {
        return cdoPk;
    }

    public void setCdoPk(Long cdoPk) {
        this.cdoPk = cdoPk;
    }

    public SgHorarioEscolar getCdoHorarioEscolar() {
        return cdoHorarioEscolar;
    }

    public void setCdoHorarioEscolar(SgHorarioEscolar cdoHorarioEscolar) {
        this.cdoHorarioEscolar = cdoHorarioEscolar;
    }

    public SgComponentePlanEstudio getCdoComponente() {
        return cdoComponente;
    }

    public void setCdoComponente(SgComponentePlanEstudio cdoComponente) {
        this.cdoComponente = cdoComponente;
    }

    public SgPersonalSedeEducativaNoJsonIdentity getCdoDocente() {
        return cdoDocente;
    }

    public void setCdoDocente(SgPersonalSedeEducativaNoJsonIdentity cdoDocente) {
        this.cdoDocente = cdoDocente;
    }

    public LocalDateTime getCdoUltModFecha() {
        return cdoUltModFecha;
    }

    public void setCdoUltModFecha(LocalDateTime cdoUltModFecha) {
        this.cdoUltModFecha = cdoUltModFecha;
    }

    public String getCdoUltModUsuario() {
        return cdoUltModUsuario;
    }

    public void setCdoUltModUsuario(String cdoUltModUsuario) {
        this.cdoUltModUsuario = cdoUltModUsuario;
    }

    public Integer getCdoVersion() {
        return cdoVersion;
    }

    public void setCdoVersion(Integer cdoVersion) {
        this.cdoVersion = cdoVersion;
    }

    public Long getEliminarContextoDePersonalPk() {
        return eliminarContextoDePersonalPk;
    }

    public void setEliminarContextoDePersonalPk(Long eliminarContextoDePersonalPk) {
        this.eliminarContextoDePersonalPk = eliminarContextoDePersonalPk;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdoPk != null ? cdoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SgComponenteDocente)) {
            return false;
        }
        SgComponenteDocente other = (SgComponenteDocente) object;
        if ((this.cdoPk == null && other.cdoPk != null) || (this.cdoPk != null && !this.cdoPk.equals(other.cdoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgComponenteDocente[ cdoPk=" + cdoPk + " ]";
    }
    
}
