/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAccion;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "apePk", scope = SgAccionPrevenirEmbarazo.class)
public class SgAccionPrevenirEmbarazo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long apePk;
    
    private SgSede apeSede;
    
    private SgAnioLectivo apeAnio;
    
    private String apeInstitucion;
    
    private SgTipoAccion apeTipoAccion;
    
    private String apeDescripcion;
    
    private LocalDateTime apeUltModFecha;
    
    private String apeUltModUsuario;
    
    private Integer apeVersion;

    public SgAccionPrevenirEmbarazo() {
    }

    public Long getApePk() {
        return apePk;
    }

    public void setApePk(Long apePk) {
        this.apePk = apePk;
    }

    public SgSede getApeSede() {
        return apeSede;
    }

    public void setApeSede(SgSede apeSede) {
        this.apeSede = apeSede;
    }

    public SgAnioLectivo getApeAnio() {
        return apeAnio;
    }

    public void setApeAnio(SgAnioLectivo apeAnio) {
        this.apeAnio = apeAnio;
    }

    public String getApeInstitucion() {
        return apeInstitucion;
    }

    public void setApeInstitucion(String apeInstitucion) {
        this.apeInstitucion = apeInstitucion;
    }

    public SgTipoAccion getApeTipoAccion() {
        return apeTipoAccion;
    }

    public void setApeTipoAccion(SgTipoAccion apeTipoAccion) {
        this.apeTipoAccion = apeTipoAccion;
    }

    public String getApeDescripcion() {
        return apeDescripcion;
    }

    public void setApeDescripcion(String apeDescripcion) {
        this.apeDescripcion = apeDescripcion;
    }

    public LocalDateTime getApeUltModFecha() {
        return apeUltModFecha;
    }

    public void setApeUltModFecha(LocalDateTime apeUltModFecha) {
        this.apeUltModFecha = apeUltModFecha;
    }

    public String getApeUltModUsuario() {
        return apeUltModUsuario;
    }

    public void setApeUltModUsuario(String apeUltModUsuario) {
        this.apeUltModUsuario = apeUltModUsuario;
    }

    public Integer getApeVersion() {
        return apeVersion;
    }

    public void setApeVersion(Integer apeVersion) {
        this.apeVersion = apeVersion;
    }




    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.apePk);
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
        final SgAccionPrevenirEmbarazo other = (SgAccionPrevenirEmbarazo) obj;
        if (!Objects.equals(this.apePk, other.apePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAccionPrevenirEmbarazo{" + "apePk=" + apePk +", apeUltModFecha=" + apeUltModFecha + ", apeUltModUsuario=" + apeUltModUsuario + ", apeVersion=" + apeVersion + '}';
    }
    
    

}
