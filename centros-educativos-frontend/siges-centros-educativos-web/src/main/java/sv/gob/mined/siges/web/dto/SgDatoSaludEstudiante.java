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
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoApoyo;

/**
 *
 * @author bruno
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dsePk", scope = SgDatoSaludEstudiante.class)
public class SgDatoSaludEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long dsePk;
    
    private SgAnioLectivo dseAnioLectivo;
    
    private SgTipoApoyo dseTipoApoyo;
    
    private SgEstudiante dseEstudiante;
    
    private String dseDescripcion;
    
    private LocalDateTime dseUltModFecha;
    
    private String dseUltModUsuario;
    
    private Integer dseVersion;

    public Long getDsePk() {
        return dsePk;
    }

    public void setDsePk(Long dsePk) {
        this.dsePk = dsePk;
    }

    public SgAnioLectivo getDseAnioLectivo() {
        return dseAnioLectivo;
    }

    public void setDseAnioLectivo(SgAnioLectivo dseAnioLectivo) {
        this.dseAnioLectivo = dseAnioLectivo;
    }

    public SgTipoApoyo getDseTipoApoyo() {
        return dseTipoApoyo;
    }

    public void setDseTipoApoyo(SgTipoApoyo dseTipoApoyo) {
        this.dseTipoApoyo = dseTipoApoyo;
    }

    public SgEstudiante getDseEstudiante() {
        return dseEstudiante;
    }

    public void setDseEstudiante(SgEstudiante dseEstudiante) {
        this.dseEstudiante = dseEstudiante;
    }
    
    public String getDseDescripcion() {
        return dseDescripcion;
    }

    public void setDseDescripcion(String dseDescripcion) {
        this.dseDescripcion = dseDescripcion;
    }

    public LocalDateTime getDseUltModFecha() {
        return dseUltModFecha;
    }

    public void setDseUltModFecha(LocalDateTime dseUltModFecha) {
        this.dseUltModFecha = dseUltModFecha;
    }

    public String getDseUltModUsuario() {
        return dseUltModUsuario;
    }

    public void setDseUltModUsuario(String dseUltModUsuario) {
        this.dseUltModUsuario = dseUltModUsuario;
    }

    public Integer getDseVersion() {
        return dseVersion;
    }

    public void setDseVersion(Integer dseVersion) {
        this.dseVersion = dseVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.dsePk);
        hash = 67 * hash + Objects.hashCode(this.dseAnioLectivo);
        hash = 67 * hash + Objects.hashCode(this.dseTipoApoyo);
        hash = 67 * hash + Objects.hashCode(this.dseEstudiante);
        hash = 67 * hash + Objects.hashCode(this.dseDescripcion);
        hash = 67 * hash + Objects.hashCode(this.dseUltModFecha);
        hash = 67 * hash + Objects.hashCode(this.dseUltModUsuario);
        hash = 67 * hash + Objects.hashCode(this.dseVersion);
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
        final SgDatoSaludEstudiante other = (SgDatoSaludEstudiante) obj;
        if (!Objects.equals(this.dsePk, other.dsePk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgDatoSaludEstudiante{" + "dsePk=" + dsePk + '}';
    }
    
}
