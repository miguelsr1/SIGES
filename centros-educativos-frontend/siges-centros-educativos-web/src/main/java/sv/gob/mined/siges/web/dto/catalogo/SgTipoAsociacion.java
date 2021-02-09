/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tasPk", scope = SgTipoAsociacion.class)
public class SgTipoAsociacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tasPk;

    private String tasCodigo;

    private String tasNombre;

    private String tasNombreBusqueda;
    
    private String tasDescripcion;

    private Boolean tasHabilitado;

    private LocalDateTime tasUltModFecha;

    private String tasUltModUsuario;

    private Integer tasVersion;

    public SgTipoAsociacion() {
        this.tasHabilitado = Boolean.TRUE;
    }

    public Long getTasPk() {
        return tasPk;
    }

    public void setTasPk(Long tasPk) {
        this.tasPk = tasPk;
    }

    public String getTasCodigo() {
        return tasCodigo;
    }

    public void setTasCodigo(String tasCodigo) {
        this.tasCodigo = tasCodigo;
    }

    public String getTasNombre() {
        return tasNombre;
    }

    public void setTasNombre(String tasNombre) {
        this.tasNombre = tasNombre;
    }

    public String getTasNombreBusqueda() {
        return tasNombreBusqueda;
    }

    public void setTasNombreBusqueda(String tasNombreBusqueda) {
        this.tasNombreBusqueda = tasNombreBusqueda;
    }

    public String getTasDescripcion() {
        return tasDescripcion;
    }

    public void setTasDescripcion(String tasDescripcion) {
        this.tasDescripcion = tasDescripcion;
    }

    public Boolean getTasHabilitado() {
        return tasHabilitado;
    }

    public void setTasHabilitado(Boolean tasHabilitado) {
        this.tasHabilitado = tasHabilitado;
    }

    public LocalDateTime getTasUltModFecha() {
        return tasUltModFecha;
    }

    public void setTasUltModFecha(LocalDateTime tasUltModFecha) {
        this.tasUltModFecha = tasUltModFecha;
    }

    public String getTasUltModUsuario() {
        return tasUltModUsuario;
    }

    public void setTasUltModUsuario(String tasUltModUsuario) {
        this.tasUltModUsuario = tasUltModUsuario;
    }

    public Integer getTasVersion() {
        return tasVersion;
    }

    public void setTasVersion(Integer tasVersion) {
        this.tasVersion = tasVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tasPk);
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
        final SgTipoAsociacion other = (SgTipoAsociacion) obj;
        if (!Objects.equals(this.tasPk, other.tasPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesTipoAsociacion{" + "tasPk=" + tasPk + ", tasCodigo=" + tasCodigo + ", tasNombre=" + tasNombre + ", tasNombreBusqueda=" + tasNombreBusqueda + ", tasHabilitado=" + tasHabilitado + ", tasUltModFecha=" + tasUltModFecha + ", tasUltModUsuario=" + tasUltModUsuario + ", tasVersion=" + tasVersion + '}';
    }

}
