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
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumServiciosRest;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "prnPk", scope = SgProcesoNocturno.class)
public class SgProcesoNocturno implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long prnPk;

    private EnumServiciosRest prnServicio;

    private String prnUrl;
 
    private String prnNombre;
    
    private String prnNombreBusqueda;

    private LocalDateTime prnUltModFecha;

    private String prnUltModUsuario;

    private Integer prnVersion;
    
    private List<SgEjecucionProcesoNocturno> ejecucionesProcesoNocturno;
    
    
    public SgProcesoNocturno() {
    }

    public Long getPrnPk() {
        return prnPk;
    }

    public void setPrnPk(Long prnPk) {
        this.prnPk = prnPk;
    }

    public EnumServiciosRest getPrnServicio() {
        return prnServicio;
    }

    public void setPrnServicio(EnumServiciosRest prnServicio) {
        this.prnServicio = prnServicio;
    }

    public String getPrnUrl() {
        return prnUrl;
    }

    public void setPrnUrl(String prnUrl) {
        this.prnUrl = prnUrl;
    }


    public String getPrnNombre() {
        return prnNombre;
    }

    public void setPrnNombre(String prnNombre) {
        this.prnNombre = prnNombre;
    }

    public LocalDateTime getPrnUltModFecha() {
        return prnUltModFecha;
    }

    public void setPrnUltModFecha(LocalDateTime prnUltModFecha) {
        this.prnUltModFecha = prnUltModFecha;
    }

    public String getPrnUltModUsuario() {
        return prnUltModUsuario;
    }

    public void setPrnUltModUsuario(String prnUltModUsuario) {
        this.prnUltModUsuario = prnUltModUsuario;
    }

    public Integer getPrnVersion() {
        return prnVersion;
    }

    public void setPrnVersion(Integer prnVersion) {
        this.prnVersion = prnVersion;
    }

    public List<SgEjecucionProcesoNocturno> getEjecucionesProcesoNocturno() {
        return ejecucionesProcesoNocturno;
    }

    public void setEjecucionesProcesoNocturno(List<SgEjecucionProcesoNocturno> ejecucionesProcesoNocturno) {
        this.ejecucionesProcesoNocturno = ejecucionesProcesoNocturno;
    }

    public String getPrnNombreBusqueda() {
        return prnNombreBusqueda;
    }

    public void setPrnNombreBusqueda(String prnNombreBusqueda) {
        this.prnNombreBusqueda = prnNombreBusqueda;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prnPk != null ? prnPk.hashCode() : 0);
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
        final SgProcesoNocturno other = (SgProcesoNocturno) obj;
        if (!Objects.equals(this.prnPk, other.prnPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgProcesoNocturno[ prnPk=" + prnPk + " ]";
    }
    
}
