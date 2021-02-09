/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sftPk", scope = SgSelloFirmaTitular.class)
public class SgSelloFirmaTitular implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sftPk;

    private String sftPrimerNombre;

    private String sftSegundoNombre;

    private String sftPrimerApellido;

    private String sftSegundoApellido;

    private LocalDate sftFechaDesde;

    private LocalDate sftFechaHasta;

    private String sftObservaciones;

    private String sftNombreBusqueda;

    private Boolean sftHabilitado;

    private LocalDateTime sftUltModFecha;

    private String sftUltModUsuario;

    private Integer sftVersion;

    private SgArchivo sftFirmaSello;

    public SgSelloFirmaTitular() {
        this.sftHabilitado = Boolean.TRUE;
    }

    public String getSftNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.sftPrimerNombre != null) {
            s.append(this.sftPrimerNombre).append(" ");
        }
        if (this.sftSegundoNombre != null) {
            s.append(this.sftSegundoNombre).append(" ");
        }
        if (this.sftPrimerApellido != null) {
            s.append(this.sftPrimerApellido).append(" ");
        }
        if (this.sftSegundoApellido != null) {
            s.append(this.sftSegundoApellido).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }
   
    public Long getSftPk() {
        return sftPk;
    }

    public void setSftPk(Long sftPk) {
        this.sftPk = sftPk;
    }

    public String getSftPrimerNombre() {
        return sftPrimerNombre;
    }

    public void setSftPrimerNombre(String sftPrimerNombre) {
        this.sftPrimerNombre = sftPrimerNombre;
    }

    public String getSftSegundoNombre() {
        return sftSegundoNombre;
    }

    public void setSftSegundoNombre(String sftSegundoNombre) {
        this.sftSegundoNombre = sftSegundoNombre;
    }

    public String getSftPrimerApellido() {
        return sftPrimerApellido;
    }

    public void setSftPrimerApellido(String sftPrimerApellido) {
        this.sftPrimerApellido = sftPrimerApellido;
    }

    public String getSftSegundoApellido() {
        return sftSegundoApellido;
    }

    public void setSftSegundoApellido(String sftSegundoApellido) {
        this.sftSegundoApellido = sftSegundoApellido;
    }

    public LocalDate getSftFechaDesde() {
        return sftFechaDesde;
    }

    public void setSftFechaDesde(LocalDate sftFechaDesde) {
        this.sftFechaDesde = sftFechaDesde;
    }

    public LocalDate getSftFechaHasta() {
        return sftFechaHasta;
    }

    public void setSftFechaHasta(LocalDate sftFechaHasta) {
        this.sftFechaHasta = sftFechaHasta;
    }

    public String getSftObservaciones() {
        return sftObservaciones;
    }

    public void setSftObservaciones(String sftObservaciones) {
        this.sftObservaciones = sftObservaciones;
    }

    public String getSftNombreBusqueda() {
        return sftNombreBusqueda;
    }

    public void setSftNombreBusqueda(String sftNombreBusqueda) {
        this.sftNombreBusqueda = sftNombreBusqueda;
    }

    public SgArchivo getSftFirmaSello() {
        return sftFirmaSello;
    }

    public void setSftFirmaSello(SgArchivo sftFirmaSello) {
        this.sftFirmaSello = sftFirmaSello;
    }

    public LocalDateTime getSftUltModFecha() {
        return sftUltModFecha;
    }

    public void setSftUltModFecha(LocalDateTime sftUltModFecha) {
        this.sftUltModFecha = sftUltModFecha;
    }

    public String getSftUltModUsuario() {
        return sftUltModUsuario;
    }

    public void setSftUltModUsuario(String sftUltModUsuario) {
        this.sftUltModUsuario = sftUltModUsuario;
    }

    public Integer getSftVersion() {
        return sftVersion;
    }

    public void setSftVersion(Integer sftVersion) {
        this.sftVersion = sftVersion;
    }

    public Boolean getSftHabilitado() {
        return sftHabilitado;
    }

    public void setSftHabilitado(Boolean sftHabilitado) {
        this.sftHabilitado = sftHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sftPk != null ? sftPk.hashCode() : 0);
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
        final SgSelloFirmaTitular other = (SgSelloFirmaTitular) obj;
        if (!Objects.equals(this.sftPk, other.sftPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgSelloFirmaTitular[ sftPk=" + sftPk + " ]";
    }

}
