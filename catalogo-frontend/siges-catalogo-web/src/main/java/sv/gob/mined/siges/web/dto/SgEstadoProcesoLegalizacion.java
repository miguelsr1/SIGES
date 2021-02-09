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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eplPk", scope = SgEstadoProcesoLegalizacion.class)
public class SgEstadoProcesoLegalizacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long eplPk;

    private String eplCodigo;

    private String eplNombre;

    private Boolean eplHabilitado;
    
    private Boolean eplAplicaDatoPresentacion;
    
    private Boolean eplAplicaEstadoProceso;

    private LocalDateTime eplUltModFecha;

    private String eplUltModUsuario;

    private Integer eplVersion;
    
    
    public SgEstadoProcesoLegalizacion() {
        this.eplHabilitado = Boolean.TRUE;
        this.eplAplicaDatoPresentacion=Boolean.FALSE;
        this.eplAplicaEstadoProceso=Boolean.FALSE;
    }

    public Long getEplPk() {
        return eplPk;
    }

    public void setEplPk(Long eplPk) {
        this.eplPk = eplPk;
    }

    public String getEplCodigo() {
        return eplCodigo;
    }

    public void setEplCodigo(String eplCodigo) {
        this.eplCodigo = eplCodigo;
    }

    public String getEplNombre() {
        return eplNombre;
    }

    public void setEplNombre(String eplNombre) {
        this.eplNombre = eplNombre;
    }

    public LocalDateTime getEplUltModFecha() {
        return eplUltModFecha;
    }

    public void setEplUltModFecha(LocalDateTime eplUltModFecha) {
        this.eplUltModFecha = eplUltModFecha;
    }

    public String getEplUltModUsuario() {
        return eplUltModUsuario;
    }

    public void setEplUltModUsuario(String eplUltModUsuario) {
        this.eplUltModUsuario = eplUltModUsuario;
    }

    public Integer getEplVersion() {
        return eplVersion;
    }

    public void setEplVersion(Integer eplVersion) {
        this.eplVersion = eplVersion;
    }

    public Boolean getEplAplicaDatoPresentacion() {
        return eplAplicaDatoPresentacion;
    }

    public void setEplAplicaDatoPresentacion(Boolean eplAplicaDatoPresentacion) {
        this.eplAplicaDatoPresentacion = eplAplicaDatoPresentacion;
    }

    public Boolean getEplAplicaEstadoProceso() {
        return eplAplicaEstadoProceso;
    }

    public void setEplAplicaEstadoProceso(Boolean eplAplicaEstadoProceso) {
        this.eplAplicaEstadoProceso = eplAplicaEstadoProceso;
    }

    
     public Boolean getEplHabilitado() {
        return eplHabilitado;
    }

    public void setEplHabilitado(Boolean eplHabilitado) {
        this.eplHabilitado = eplHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eplPk != null ? eplPk.hashCode() : 0);
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
        final SgEstadoProcesoLegalizacion other = (SgEstadoProcesoLegalizacion) obj;
        if (!Objects.equals(this.eplPk, other.eplPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgEstadoProcesoLegalizacion[ eplPk=" + eplPk + " ]";
    }
    
}
