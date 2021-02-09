/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mapPk", scope = SgMaterialPiso.class)
public class SgMaterialPiso implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long mapPk;

    private String mapCodigo;

    private String mapNombre;

    private Boolean mapHabilitado;

    private LocalDateTime mapUltModFecha;

    private String mapUltModUsuario;

    private Integer mapVersion;
    
    
    public SgMaterialPiso() {
        this.mapHabilitado = Boolean.TRUE;
    }

    public Long getMapPk() {
        return mapPk;
    }

    public void setMapPk(Long mapPk) {
        this.mapPk = mapPk;
    }

    public String getMapCodigo() {
        return mapCodigo;
    }

    public void setMapCodigo(String mapCodigo) {
        this.mapCodigo = mapCodigo;
    }

    public String getMapNombre() {
        return mapNombre;
    }

    public void setMapNombre(String mapNombre) {
        this.mapNombre = mapNombre;
    }

    public LocalDateTime getMapUltModFecha() {
        return mapUltModFecha;
    }

    public void setMapUltModFecha(LocalDateTime mapUltModFecha) {
        this.mapUltModFecha = mapUltModFecha;
    }

    public String getMapUltModUsuario() {
        return mapUltModUsuario;
    }

    public void setMapUltModUsuario(String mapUltModUsuario) {
        this.mapUltModUsuario = mapUltModUsuario;
    }

    public Integer getMapVersion() {
        return mapVersion;
    }

    public void setMapVersion(Integer mapVersion) {
        this.mapVersion = mapVersion;
    }

    
     public Boolean getMapHabilitado() {
        return mapHabilitado;
    }

    public void setMapHabilitado(Boolean mapHabilitado) {
        this.mapHabilitado = mapHabilitado;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mapPk != null ? mapPk.hashCode() : 0);
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
        final SgMaterialPiso other = (SgMaterialPiso) obj;
        if (!Objects.equals(this.mapPk, other.mapPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgMaterialPiso[ mapPk=" + mapPk + " ]";
    }
    
}
