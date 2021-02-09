/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "proPk", scope = SgAfProyectos.class)
public class SgAfProyectos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long proPk;
    private String proCodigo;
    private String proNombre;
    private String proNombreBusqueda;
    private Boolean proHabilitado;
    private Boolean proProyectoLiquidado;
    private LocalDateTime proUltModFecha;
    private String proUltModUsuario;
    private Integer proVersion;

    public SgAfProyectos() {
        proHabilitado = Boolean.TRUE;
        proProyectoLiquidado = Boolean.FALSE;
    }

    public Long getProPk() {
        return proPk;
    }

    public void setProPk(Long proPk) {
        this.proPk = proPk;
    }

    public String getProCodigo() {
        return proCodigo;
    }

    public void setProCodigo(String proCodigo) {
        this.proCodigo = proCodigo;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public String getProNombreBusqueda() {
        return proNombreBusqueda;
    }

    public void setProNombreBusqueda(String proNombreBusqueda) {
        this.proNombreBusqueda = proNombreBusqueda;
    }

    public Boolean getProHabilitado() {
        return proHabilitado;
    }

    public void setProHabilitado(Boolean proHabilitado) {
        this.proHabilitado = proHabilitado;
    }

    public LocalDateTime getProUltModFecha() {
        return proUltModFecha;
    }

    public void setProUltModFecha(LocalDateTime proUltModFecha) {
        this.proUltModFecha = proUltModFecha;
    }

    public String getProUltModUsuario() {
        return proUltModUsuario;
    }

    public void setProUltModUsuario(String proUltModUsuario) {
        this.proUltModUsuario = proUltModUsuario;
    }

    public Integer getProVersion() {
        return proVersion;
    }

    public void setProVersion(Integer proVersion) {
        this.proVersion = proVersion;
    }

    public Boolean getProProyectoLiquidado() {
        return proProyectoLiquidado;
    }

    public void setProProyectoLiquidado(Boolean proProyectoLiquidado) {
        this.proProyectoLiquidado = proProyectoLiquidado;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.proPk);
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
        final SgAfProyectos other = (SgAfProyectos) obj;
        if (!Objects.equals(this.proPk, other.proPk)) {
            return false;
        }
        return true;
    }
 
    
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgProyectos[ proPk=" + proPk + " ]";
    }

}

