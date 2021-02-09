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
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoDocumento;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ridPk", scope = SgRelInmuebleDocumento.class)
public class SgRelInmuebleDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ridPk;

    private SgInmueblesSedes ridInmuebleSede;

    private SgArchivo ridDocumento;

    private SgInfTipoDocumento ridTipoDocumento;

    private String ridNombre;
    
    private String ridDescripcion;

    private LocalDateTime ridUltModFecha;

    private String ridUltModUsuario;

    private Integer ridVersion;

    public SgRelInmuebleDocumento() {
    }

    public Long getRidPk() {
        return ridPk;
    }

    public void setRidPk(Long ridPk) {
        this.ridPk = ridPk;
    }

    public String getRidNombre() {
        return ridNombre;
    }

    public void setRidNombre(String ridNombre) {
        this.ridNombre = ridNombre;
    }

    public SgInmueblesSedes getRidInmuebleSede() {
        return ridInmuebleSede;
    }

    public void setRidInmuebleSede(SgInmueblesSedes ridInmuebleSede) {
        this.ridInmuebleSede = ridInmuebleSede;
    }

    public SgArchivo getRidDocumento() {
        return ridDocumento;
    }

    public void setRidDocumento(SgArchivo ridDocumento) {
        this.ridDocumento = ridDocumento;
    }

    public SgInfTipoDocumento getRidTipoDocumento() {
        return ridTipoDocumento;
    }

    public void setRidTipoDocumento(SgInfTipoDocumento ridTipoDocumento) {
        this.ridTipoDocumento = ridTipoDocumento;
    }

    public String getRidDescripcion() {
        return ridDescripcion;
    }

    public void setRidDescripcion(String ridDescripcion) {
        this.ridDescripcion = ridDescripcion;
    }

    public LocalDateTime getRidUltModFecha() {
        return ridUltModFecha;
    }

    public void setRidUltModFecha(LocalDateTime ridUltModFecha) {
        this.ridUltModFecha = ridUltModFecha;
    }

    public String getRidUltModUsuario() {
        return ridUltModUsuario;
    }

    public void setRidUltModUsuario(String ridUltModUsuario) {
        this.ridUltModUsuario = ridUltModUsuario;
    }

    public Integer getRidVersion() {
        return ridVersion;
    }

    public void setRidVersion(Integer ridVersion) {
        this.ridVersion = ridVersion;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ridPk != null ? ridPk.hashCode() : 0);
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
        final SgRelInmuebleDocumento other = (SgRelInmuebleDocumento) obj;
        if (!Objects.equals(this.ridPk, other.ridPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgRelInmuebleDocumento[ ridPk=" + ridPk + " ]";
    }

}
