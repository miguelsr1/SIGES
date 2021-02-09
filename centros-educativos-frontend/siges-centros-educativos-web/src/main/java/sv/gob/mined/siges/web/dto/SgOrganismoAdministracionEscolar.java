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
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "oaePk", scope = SgOrganismoAdministracionEscolar.class)
public class SgOrganismoAdministracionEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long oaePk;
    
    private String oaeNombre;

    private SgSede oaeSede;
    
    private SgTipoOrganismoAdministrativo oaeTipoOrganismoAdministrativo;

    private String oaeActaIntegracion;

    private LocalDate oaeFechaActaIntegracion;

    private LocalDate oaeFechaVencimiento;
    
    private LocalDate oaeFechaLegalizacion;
    
    private String oaeAcuerdoCierre;
    
    private LocalDate oaeFechaCierre;
    
    private Boolean oaeMiembrosVigentes;

    private EnumEstadoOrganismoAdministrativo oaeEstado;

    private LocalDateTime oaeUltModFecha;

    private String oaeUltModUsuario;

    private Integer oaeVersion;
    
    private List<SgPersonaOrganismoAdministracionLite> oaePersonasOrganismoAdministriativoLite;
    
    private List<SgPersonaOrganismoAdministracion> oaePersonasOrganismoAdministriativo;
    
    private String oaeNumeroAcuerdo;

    private LocalDate oaeFechaAcuerdo;

    private String oaeAmpliarDatos;

    private String oaeMotivoRechazo;
    
    private List<SgItemsEvaluarOAE> itemsEvaluarOAE;
    
    private List<SgRelOAEIdentificacionOAE> oaeIdentificaciones;
    
    private SgOrganismoAdministracionEscolar oaeMiembrosRenovadoPadreFk;
    
    private Boolean oaeEsMiembroRenovado;
    
    private String oaeNumeroResolucion;
    
    private LocalDate oaeFechaResolucion;

    public SgOrganismoAdministracionEscolar() {
        oaeEsMiembroRenovado=Boolean.FALSE;
    }

    public Long getOaePk() {
        return oaePk;
    }

    public void setOaePk(Long oaePk) {
        this.oaePk = oaePk;
    }

    public SgSede getOaeSede() {
        return oaeSede;
    }

    public void setOaeSede(SgSede oaeSede) {
        this.oaeSede = oaeSede;
    }

    public String getOaeActaIntegracion() {
        return oaeActaIntegracion;
    }

    public void setOaeActaIntegracion(String oaeActaIntegracion) {
        this.oaeActaIntegracion = oaeActaIntegracion;
    }

    public LocalDate getOaeFechaActaIntegracion() {
        return oaeFechaActaIntegracion;
    }

    public void setOaeFechaActaIntegracion(LocalDate oaeFechaActaIntegracion) {
        this.oaeFechaActaIntegracion = oaeFechaActaIntegracion;
    }

    public LocalDate getOaeFechaVencimiento() {
        return oaeFechaVencimiento;
    }

    public void setOaeFechaVencimiento(LocalDate oaeFechaVencimiento) {
        this.oaeFechaVencimiento = oaeFechaVencimiento;
    }

    public EnumEstadoOrganismoAdministrativo getOaeEstado() {
        return oaeEstado;
    }

    public void setOaeEstado(EnumEstadoOrganismoAdministrativo oaeEstado) {
        this.oaeEstado = oaeEstado;
    }

    public LocalDateTime getOaeUltModFecha() {
        return oaeUltModFecha;
    }

    public void setOaeUltModFecha(LocalDateTime oaeUltModFecha) {
        this.oaeUltModFecha = oaeUltModFecha;
    }

    public String getOaeUltModUsuario() {
        return oaeUltModUsuario;
    }

    public void setOaeUltModUsuario(String oaeUltModUsuario) {
        this.oaeUltModUsuario = oaeUltModUsuario;
    }

    public Integer getOaeVersion() {
        return oaeVersion;
    }

    public void setOaeVersion(Integer oaeVersion) {
        this.oaeVersion = oaeVersion;
    }

    public List<SgPersonaOrganismoAdministracion> getOaePersonasOrganismoAdministriativo() {
        return oaePersonasOrganismoAdministriativo;
    }

    public void setOaePersonasOrganismoAdministriativo(List<SgPersonaOrganismoAdministracion> oaePersonasOrganismoAdministriativo) {
        this.oaePersonasOrganismoAdministriativo = oaePersonasOrganismoAdministriativo;
    }
    
    public List<SgPersonaOrganismoAdministracionLite> getOaePersonasOrganismoAdministriativoLite() {
        return oaePersonasOrganismoAdministriativoLite;
    }

    public void setOaePersonasOrganismoAdministriativoLite(List<SgPersonaOrganismoAdministracionLite> oaePersonasOrganismoAdministriativoLite) {
        this.oaePersonasOrganismoAdministriativoLite = oaePersonasOrganismoAdministriativoLite;
    }

    public Boolean getOaeMiembrosVigentes() {
        return oaeMiembrosVigentes;
    }

    public String getOaeNumeroAcuerdo() {
        return oaeNumeroAcuerdo;
    }

    public void setOaeNumeroAcuerdo(String oaeNumeroAcuerdo) {
        this.oaeNumeroAcuerdo = oaeNumeroAcuerdo;
    }

    public LocalDate getOaeFechaAcuerdo() {
        return oaeFechaAcuerdo;
    }

    public void setOaeFechaAcuerdo(LocalDate oaeFechaAcuerdo) {
        this.oaeFechaAcuerdo = oaeFechaAcuerdo;
    }

    public String getOaeAmpliarDatos() {
        return oaeAmpliarDatos;
    }

    public void setOaeAmpliarDatos(String oaeAmpliarDatos) {
        this.oaeAmpliarDatos = oaeAmpliarDatos;
    }

    public String getOaeMotivoRechazo() {
        return oaeMotivoRechazo;
    }

    public void setOaeMotivoRechazo(String oaeMotivoRechazo) {
        this.oaeMotivoRechazo = oaeMotivoRechazo;
    }


    public void setOaeMiembrosVigentes(Boolean oaeMiembrosVigentes) {
        this.oaeMiembrosVigentes = oaeMiembrosVigentes;
    }

    public String getOaeNombre() {
        return oaeNombre;
    }

    public void setOaeNombre(String oaeNombre) {
        this.oaeNombre = oaeNombre;
    }

    public SgTipoOrganismoAdministrativo getOaeTipoOrganismoAdministrativo() {
        return oaeTipoOrganismoAdministrativo;
    }

    public void setOaeTipoOrganismoAdministrativo(SgTipoOrganismoAdministrativo oaeTipoOrganismoAdministrativo) {
        this.oaeTipoOrganismoAdministrativo = oaeTipoOrganismoAdministrativo;
    }

    public LocalDate getOaeFechaLegalizacion() {
        return oaeFechaLegalizacion;
    }

    public void setOaeFechaLegalizacion(LocalDate oaeFechaLegalizacion) {
        this.oaeFechaLegalizacion = oaeFechaLegalizacion;
    }

    public LocalDate getOaeFechaCierre() {
        return oaeFechaCierre;
    }

    public void setOaeFechaCierre(LocalDate oaeFechaCierre) {
        this.oaeFechaCierre = oaeFechaCierre;
    }

    public String getOaeAcuerdoCierre() {
        return oaeAcuerdoCierre;
    }

    public void setOaeAcuerdoCierre(String oaeAcuerdoCierre) {
        this.oaeAcuerdoCierre = oaeAcuerdoCierre;
    }

    public List<SgItemsEvaluarOAE> getItemsEvaluarOAE() {
        return itemsEvaluarOAE;
    }

    public void setItemsEvaluarOAE(List<SgItemsEvaluarOAE> itemsEvaluarOAE) {
        this.itemsEvaluarOAE = itemsEvaluarOAE;
    }

    public List<SgRelOAEIdentificacionOAE> getOaeIdentificaciones() {
        return oaeIdentificaciones;
    }

    public void setOaeIdentificaciones(List<SgRelOAEIdentificacionOAE> oaeIdentificaciones) {
        this.oaeIdentificaciones = oaeIdentificaciones;
    }

    public SgOrganismoAdministracionEscolar getOaeMiembrosRenovadoPadreFk() {
        return oaeMiembrosRenovadoPadreFk;
    }

    public void setOaeMiembrosRenovadoPadreFk(SgOrganismoAdministracionEscolar oaeMiembrosRenovadoPadreFk) {
        this.oaeMiembrosRenovadoPadreFk = oaeMiembrosRenovadoPadreFk;
    }

    public Boolean getOaeEsMiembroRenovado() {
        return oaeEsMiembroRenovado;
    }

    public void setOaeEsMiembroRenovado(Boolean oaeEsMiembroRenovado) {
        this.oaeEsMiembroRenovado = oaeEsMiembroRenovado;
    }

    public String getOaeNumeroResolucion() {
        return oaeNumeroResolucion;
    }

    public void setOaeNumeroResolucion(String oaeNumeroResolucion) {
        this.oaeNumeroResolucion = oaeNumeroResolucion;
    }

    public LocalDate getOaeFechaResolucion() {
        return oaeFechaResolucion;
    }

    public void setOaeFechaResolucion(LocalDate oaeFechaResolucion) {
        this.oaeFechaResolucion = oaeFechaResolucion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (oaePk != null ? oaePk.hashCode() : 0);
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
        final SgOrganismoAdministracionEscolar other = (SgOrganismoAdministracionEscolar) obj;
        if (!Objects.equals(this.oaePk, other.oaePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgOrganismoAdministracionEscolar[ oaePk=" + oaePk + " ]";
    }

}
