/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "traPk", scope = SgInfTratamientoAgua.class)
public class SgInfTratamientoAgua implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long traPk;

    private String traCodigo;

    private String traNombre;

    private Boolean traHabilitado;

    private LocalDateTime traUltModFecha;

    private String traUltModUsuario;

    private Integer traVersion;

    private Integer traOrden;
   
    private Boolean traAplicaOtros;

    private Boolean traAplicaAbastecimientoAgua;
    
    private Boolean traAplicaAlmacenamientoAgua;
    
    private Boolean traAplicaDrenaje;
    
    
    public SgInfTratamientoAgua() {
        this.traHabilitado = Boolean.TRUE;
    }

    public Long getTraPk() {
        return traPk;
    }

    public void setTraPk(Long traPk) {
        this.traPk = traPk;
    }

    public String getTraCodigo() {
        return traCodigo;
    }

    public void setTraCodigo(String traCodigo) {
        this.traCodigo = traCodigo;
    }

    public String getTraNombre() {
        return traNombre;
    }

    public void setTraNombre(String traNombre) {
        this.traNombre = traNombre;
    }

    public LocalDateTime getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(LocalDateTime traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }

    
     public Boolean getTraHabilitado() {
        return traHabilitado;
    }

    public void setTraHabilitado(Boolean traHabilitado) {
        this.traHabilitado = traHabilitado;
    }

    public Integer getTraOrden() {
        return traOrden;
    }

    public void setTraOrden(Integer traOrden) {
        this.traOrden = traOrden;
    }

    public Boolean getTraAplicaOtros() {
        return traAplicaOtros;
    }

    public void setTraAplicaOtros(Boolean traAplicaOtros) {
        this.traAplicaOtros = traAplicaOtros;
    }

    public Boolean getTraAplicaAbastecimientoAgua() {
        return traAplicaAbastecimientoAgua;
    }

    public void setTraAplicaAbastecimientoAgua(Boolean traAplicaAbastecimientoAgua) {
        this.traAplicaAbastecimientoAgua = traAplicaAbastecimientoAgua;
    }

    public Boolean getTraAplicaAlmacenamientoAgua() {
        return traAplicaAlmacenamientoAgua;
    }

    public void setTraAplicaAlmacenamientoAgua(Boolean traAplicaAlmacenamientoAgua) {
        this.traAplicaAlmacenamientoAgua = traAplicaAlmacenamientoAgua;
    }

    public Boolean getTraAplicaDrenaje() {
        return traAplicaDrenaje;
    }

    public void setTraAplicaDrenaje(Boolean traAplicaDrenaje) {
        this.traAplicaDrenaje = traAplicaDrenaje;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traPk != null ? traPk.hashCode() : 0);
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
        final SgInfTratamientoAgua other = (SgInfTratamientoAgua) obj;
        if (!Objects.equals(this.traPk, other.traPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgInfTratamientoAgua[ traPk=" + traPk + " ]";
    }
    
}
