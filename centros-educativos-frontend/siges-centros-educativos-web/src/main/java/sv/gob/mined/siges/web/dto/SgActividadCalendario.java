/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.enumerados.EnumActividadCalendario;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "acaPk", scope = SgActividadCalendario.class)
public class SgActividadCalendario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long acaPk;

    private String acaNombre;

    private String acaNombreBusqueda;

    private String acaDescripcion;
    
    private SgSede acaSede;
    
    private SgDepartamento acaDepartamento;
    
    private EnumAmbito acaAmbito;

    private LocalDate acaFechaDesde;

    private LocalDate acaFechaHasta;

    private EnumActividadCalendario acaTipo;

    private LocalDateTime acaUltModFecha;

    private String acaUltModUsuario;

    private Integer acaVersion;

    private SgCalendario acaCalendario;
    
    private Boolean acaDiaLectivo;

    public SgActividadCalendario() {
        this.acaDiaLectivo = Boolean.FALSE;
    }

    public Long getAcaPk() {
        return acaPk;
    }

    public void setAcaPk(Long acaPk) {
        this.acaPk = acaPk;
    }

    public String getAcaNombre() {
        return acaNombre;
    }

    public void setAcaNombre(String acaNombre) {
        this.acaNombre = acaNombre;
    }

    public String getAcaNombreBusqueda() {
        return acaNombreBusqueda;
    }

    public void setAcaNombreBusqueda(String acaNombreBusqueda) {
        this.acaNombreBusqueda = acaNombreBusqueda;
    }

    public String getAcaDescripcion() {
        return acaDescripcion;
    }

    public void setAcaDescripcion(String acaDescripcion) {
        this.acaDescripcion = acaDescripcion;
    }

    public LocalDate getAcaFechaDesde() {
        return acaFechaDesde;
    }

    public void setAcaFechaDesde(LocalDate acaFechaDesde) {
        this.acaFechaDesde = acaFechaDesde;
    }

    public LocalDate getAcaFechaHasta() {
        return acaFechaHasta;
    }

    public void setAcaFechaHasta(LocalDate acaFechaHasta) {
        this.acaFechaHasta = acaFechaHasta;
    }

    public EnumActividadCalendario getAcaTipo() {
        return acaTipo;
    }

    public void setAcaTipo(EnumActividadCalendario acaTipo) {
        this.acaTipo = acaTipo;
    }

    public LocalDateTime getAcaUltModFecha() {
        return acaUltModFecha;
    }

    public void setAcaUltModFecha(LocalDateTime acaUltModFecha) {
        this.acaUltModFecha = acaUltModFecha;
    }

    public String getAcaUltModUsuario() {
        return acaUltModUsuario;
    }

    public void setAcaUltModUsuario(String acaUltModUsuario) {
        this.acaUltModUsuario = acaUltModUsuario;
    }

    public Integer getAcaVersion() {
        return acaVersion;
    }

    public void setAcaVersion(Integer acaVersion) {
        this.acaVersion = acaVersion;
    }

    public SgCalendario getAcaCalendario() {
        return acaCalendario;
    }

    public void setAcaCalendario(SgCalendario acaCalendario) {
        this.acaCalendario = acaCalendario;
    }

    public String obtenerClase() {
        switch (acaTipo) {
            case ACADEMICA:
                return "tipo-actividad-verde";
            case NO_ACADEMICA:
                return "tipo-actividad-azul";
            default:
                return "tipo-actividad-negro";
        }
    }

    public SgSede getAcaSede() {
        return acaSede;
    }

    public void setAcaSede(SgSede acaSede) {
        this.acaSede = acaSede;
    }

    public SgDepartamento getAcaDepartamento() {
        return acaDepartamento;
    }

    public void setAcaDepartamento(SgDepartamento acaDepartamento) {
        this.acaDepartamento = acaDepartamento;
    }

    public EnumAmbito getAcaAmbito() {
        return acaAmbito;
    }

    public void setAcaAmbito(EnumAmbito acaAmbito) {
        this.acaAmbito = acaAmbito;
    }

    public Boolean getAcaDiaLectivo() {
        return acaDiaLectivo;
    }

    public void setAcaDiaLectivo(Boolean acaDiaLectivo) {
        this.acaDiaLectivo = acaDiaLectivo;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acaPk != null ? acaPk.hashCode() : 0);
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
        final SgActividadCalendario other = (SgActividadCalendario) obj;
        if (!Objects.equals(this.acaPk, other.acaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgActividadCalendario[ acaPk=" + acaPk + " ]";
    }

}
