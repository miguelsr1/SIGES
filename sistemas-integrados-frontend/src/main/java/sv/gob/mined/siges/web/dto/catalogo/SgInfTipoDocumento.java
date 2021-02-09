/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "tidPk", scope = SgInfTipoDocumento.class)
public class SgInfTipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tidPk;

    private String tidCodigo;

    private String tidNombre;

    private String tidNombreBusqueda;

    private Boolean tidHabilitado;

    private LocalDateTime tidUltModFecha;

    private String tidUltModUsuario;

    private Integer tidVersion;

    public SgInfTipoDocumento() {
    }


    public Long getTidPk() {
        return tidPk;
    }

    public void setTidPk(Long tidPk) {
        this.tidPk = tidPk;
    }

    public String getTidCodigo() {
        return tidCodigo;
    }

    public void setTidCodigo(String tidCodigo) {
        this.tidCodigo = tidCodigo;
    }

    public String getTidNombre() {
        return tidNombre;
    }

    public void setTidNombre(String tidNombre) {
        this.tidNombre = tidNombre;
    }

    public String getTidNombreBusqueda() {
        return tidNombreBusqueda;
    }

    public void setTidNombreBusqueda(String tidNombreBusqueda) {
        this.tidNombreBusqueda = tidNombreBusqueda;
    }

    public Boolean getTidHabilitado() {
        return tidHabilitado;
    }

    public void setTidHabilitado(Boolean tidHabilitado) {
        this.tidHabilitado = tidHabilitado;
    }

    public LocalDateTime getTidUltModFecha() {
        return tidUltModFecha;
    }

    public void setTidUltModFecha(LocalDateTime tidUltModFecha) {
        this.tidUltModFecha = tidUltModFecha;
    }

    public String getTidUltModUsuario() {
        return tidUltModUsuario;
    }

    public void setTidUltModUsuario(String tidUltModUsuario) {
        this.tidUltModUsuario = tidUltModUsuario;
    }

    public Integer getTidVersion() {
        return tidVersion;
    }

    public void setTidVersion(Integer tidVersion) {
        this.tidVersion = tidVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tidPk);
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
        final SgInfTipoDocumento other = (SgInfTipoDocumento) obj;
        if (!Objects.equals(this.tidPk, other.tidPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfTipoDocumento{" + "tiiPk=" + tidPk + ", tidCodigo=" + tidCodigo + ", tidNombre=" + tidNombre + ", tidNombreBusqueda=" + tidNombreBusqueda + ", tidHabilitado=" + tidHabilitado + ", tidUltModFecha=" + tidUltModFecha + ", tidUltModUsuario=" + tidUltModUsuario + ", tidVersion=" + tidVersion + '}';
    }
}

