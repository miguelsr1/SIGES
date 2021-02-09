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
import sv.gob.mined.siges.web.dto.catalogo.SgTipoApoyo;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoProveedor;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "asePk", scope = SgAsistenciaSede.class)
public class SgAsistenciaSede implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long asePk;
    
    private SgSede aseSede;
    
    private SgAnioLectivo aseAnio;
    
    private SgTipoApoyo aseTipoApoyo;
    
    private SgTipoProveedor aseTipoProveedor;
    
    private String aseNombreProveedor;
    
    private LocalDateTime aseUltModFecha;
    
    private String aseUltModUsuario;
    
    private Integer aseVersion;

    public SgAsistenciaSede() {
    }

    public Long getAsePk() {
        return asePk;
    }

    public void setAsePk(Long asePk) {
        this.asePk = asePk;
    }

    public SgSede getAseSede() {
        return aseSede;
    }

    public void setAseSede(SgSede aseSede) {
        this.aseSede = aseSede;
    }

    public SgAnioLectivo getAseAnio() {
        return aseAnio;
    }

    public void setAseAnio(SgAnioLectivo aseAnio) {
        this.aseAnio = aseAnio;
    }

    public SgTipoApoyo getAseTipoApoyo() {
        return aseTipoApoyo;
    }

    public void setAseTipoApoyo(SgTipoApoyo aseTipoApoyo) {
        this.aseTipoApoyo = aseTipoApoyo;
    }

    public SgTipoProveedor getAseTipoProveedor() {
        return aseTipoProveedor;
    }

    public void setAseTipoProveedor(SgTipoProveedor aseTipoProveedor) {
        this.aseTipoProveedor = aseTipoProveedor;
    }

    public String getAseNombreProveedor() {
        return aseNombreProveedor;
    }

    public void setAseNombreProveedor(String aseNombreProveedor) {
        this.aseNombreProveedor = aseNombreProveedor;
    }

    public LocalDateTime getAseUltModFecha() {
        return aseUltModFecha;
    }

    public void setAseUltModFecha(LocalDateTime aseUltModFecha) {
        this.aseUltModFecha = aseUltModFecha;
    }

    public String getAseUltModUsuario() {
        return aseUltModUsuario;
    }

    public void setAseUltModUsuario(String aseUltModUsuario) {
        this.aseUltModUsuario = aseUltModUsuario;
    }

    public Integer getAseVersion() {
        return aseVersion;
    }

    public void setAseVersion(Integer aseVersion) {
        this.aseVersion = aseVersion;
    }




    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.asePk);
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
        final SgAsistenciaSede other = (SgAsistenciaSede) obj;
        if (!Objects.equals(this.asePk, other.asePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAsistenciaSede{" + "asePk=" + asePk +", aseUltModFecha=" + aseUltModFecha + ", aseUltModUsuario=" + aseUltModUsuario + ", aseVersion=" + aseVersion + '}';
    }
    
    

}
