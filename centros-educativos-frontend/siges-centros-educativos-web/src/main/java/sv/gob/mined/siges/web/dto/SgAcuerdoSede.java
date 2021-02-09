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
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "asePk", scope = SgAcuerdoSede.class)
public class SgAcuerdoSede implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long asePk;
    
    private SgSede aseSede;
    
    private SgTipoAcuerdo aseTipoAcuerdo;
    
    private String aseNumeroResolucion;
    
    private LocalDate aseFechaResolucion;
    
    private String aseNumeroAcuerdo;
    
    private LocalDate aseFechaRegistro;
    
    private String aseNombreResponsable;
    
    private LocalDate aseFechaGeneracion;
    
    private String aseObservacion;
    
    private String aseNumeroSolicitud;
    
    private String aseCodigoNominacion;
    
    private Boolean aseSoloLectura;
    
    private LocalDateTime aseUltModFecha;
    
    private String aseUltModUsuario;
    
    private Integer aseVersion;

    public SgAcuerdoSede() {
        this.aseFechaRegistro = LocalDate.now();
    }

    public SgAcuerdoSede(Long asePk) {
        this.asePk = asePk;
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

    public SgTipoAcuerdo getAseTipoAcuerdo() {
        return aseTipoAcuerdo;
    }

    public void setAseTipoAcuerdo(SgTipoAcuerdo aseTipoAcuerdo) {
        this.aseTipoAcuerdo = aseTipoAcuerdo;
    }

    public String getAseNumeroResolucion() {
        return aseNumeroResolucion;
    }

    public void setAseNumeroResolucion(String aseNumeroResolucion) {
        this.aseNumeroResolucion = aseNumeroResolucion;
    }

    public LocalDate getAseFechaResolucion() {
        return aseFechaResolucion;
    }

    public void setAseFechaResolucion(LocalDate aseFechaResolucion) {
        this.aseFechaResolucion = aseFechaResolucion;
    }    

    public String getAseNumeroAcuerdo() {
        return aseNumeroAcuerdo;
    }

    public void setAseNumeroAcuerdo(String aseNumeroAcuerdo) {
        this.aseNumeroAcuerdo = aseNumeroAcuerdo;
    }

    public LocalDate getAseFechaRegistro() {
        return aseFechaRegistro;
    }

    public void setAseFechaRegistro(LocalDate aseFechaRegistro) {
        this.aseFechaRegistro = aseFechaRegistro;
    }

    public String getAseNombreResponsable() {
        return aseNombreResponsable;
    }

    public void setAseNombreResponsable(String aseNombreResponsable) {
        this.aseNombreResponsable = aseNombreResponsable;
    }

    public LocalDate getAseFechaGeneracion() {
        return aseFechaGeneracion;
    }

    public void setAseFechaGeneracion(LocalDate aseFechaGeneracion) {
        this.aseFechaGeneracion = aseFechaGeneracion;
    }

    public String getAseObservacion() {
        return aseObservacion;
    }

    public void setAseObservacion(String aseObservacion) {
        this.aseObservacion = aseObservacion;
    }

    public String getAseNumeroSolicitud() {
        return aseNumeroSolicitud;
    }

    public void setAseNumeroSolicitud(String aseNumeroSolicitud) {
        this.aseNumeroSolicitud = aseNumeroSolicitud;
    }

    public String getAseCodigoNominacion() {
        return aseCodigoNominacion;
    }

    public void setAseCodigoNominacion(String aseCodigoNominacion) {
        this.aseCodigoNominacion = aseCodigoNominacion;
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

    public Boolean getAseSoloLectura() {
        return aseSoloLectura;
    }

    public void setAseSoloLectura(Boolean aseSoloLectura) {
        this.aseSoloLectura = aseSoloLectura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asePk != null ? asePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAcuerdoSede)) {
            return false;
        }
        SgAcuerdoSede other = (SgAcuerdoSede) object;
        if ((this.asePk == null && other.asePk != null) || (this.asePk != null && !this.asePk.equals(other.asePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSede[ asePk=" + asePk + " ]";
    }
    
}
