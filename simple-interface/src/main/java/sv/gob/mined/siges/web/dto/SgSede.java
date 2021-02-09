/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.TipoSede;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "sedTipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SgCentroEducativoOficial.class, name = TipoSede.Codes.CENTRO_EDUCATIVO_OFICIAL)
    ,@JsonSubTypes.Type(value = SgCentroEducativoPrivado.class, name = TipoSede.Codes.CENTRO_EDUCATIVO_PRIVADO)
    ,@JsonSubTypes.Type(value = SgCirculoAlfabetizacion.class, name = TipoSede.Codes.CIRCULO_ALFABETIZACION)
    ,@JsonSubTypes.Type(value = SgCirculoInfancia.class, name = TipoSede.Codes.CIRCULO_PRIMERA_INFANCIA)
    ,@JsonSubTypes.Type(value = SgSedeEducame.class, name = TipoSede.Codes.EDUCAME)
})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "sedPk", scope = SgSede.class)
public class SgSede implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sedPk;

    private String sedCodigo;

    private String sedNombre;
    
    private Boolean sedHabilitado; 
    
    private SgDireccion sedDireccion;

    private String sedTelefono;

    private String sedTelefonoMovil;

    private String sedCorreoElectronico;

    private Integer sedAnioInicioAct;

    private String sedPropietario;

    private String sedNota;

    private SgTipoCalendario sedTipoCalendario;
    
    private List<SgJornadaLaboral> sedJornadasLaborales;
    
    private TipoSede sedTipo;
    
    private Integer sedVersion;
    
    private Boolean sedOrigenExterno;
    
    public SgSede() {
        this.sedHabilitado = Boolean.TRUE;
        this.sedOrigenExterno = Boolean.TRUE;
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
    }

    public SgDireccion getSedDireccion() {
        return sedDireccion;
    }

    public void setSedDireccion(SgDireccion sedDireccion) {
        this.sedDireccion = sedDireccion;
    }

    public String getSedTelefono() {
        return sedTelefono;
    }

    public void setSedTelefono(String sedTelefono) {
        this.sedTelefono = sedTelefono;
    }

    public TipoSede getSedTipo() {
        return sedTipo;
    }

    public void setSedTipo(TipoSede sedTipo) {
        this.sedTipo = sedTipo;
    }

    public String getSedCorreoElectronico() {
        return sedCorreoElectronico;
    }

    public void setSedCorreoElectronico(String sedCorreoElectronico) {
        this.sedCorreoElectronico = sedCorreoElectronico;
    }

    public Integer getSedAnioInicioAct() {
        return sedAnioInicioAct;
    }

    public void setSedAnioInicioAct(Integer sedAnioInicioAct) {
        this.sedAnioInicioAct = sedAnioInicioAct;
    }

    public String getSedPropietario() {
        return sedPropietario;
    }

    public void setSedPropietario(String sedPropietario) {
        this.sedPropietario = sedPropietario;
    }

    public String getSedTelefonoMovil() {
        return sedTelefonoMovil;
    }

    public void setSedTelefonoMovil(String sedTelefonoMovil) {
        this.sedTelefonoMovil = sedTelefonoMovil;
    }

    public String getSedNota() {
        return sedNota;
    }

    public void setSedNota(String sedNota) {
        this.sedNota = sedNota;
    }

    public SgTipoCalendario getSedTipoCalendario() {
        return sedTipoCalendario;
    }

    public void setSedTipoCalendario(SgTipoCalendario sedTipoCalendario) {
        this.sedTipoCalendario = sedTipoCalendario;
    }

    public List<SgJornadaLaboral> getSedJornadasLaborales() {
        return sedJornadasLaborales;
    }

    public void setSedJornadasLaborales(List<SgJornadaLaboral> sedJornadasLaborales) {
        this.sedJornadasLaborales = sedJornadasLaborales;
    }

    public Integer getSedVersion() {
        return sedVersion;
    }

    public void setSedVersion(Integer sedVersion) {
        this.sedVersion = sedVersion;
    }

    public Boolean getSedHabilitado() {
        return sedHabilitado;
    }

    public void setSedHabilitado(Boolean sedHabilitado) {
        this.sedHabilitado = sedHabilitado;
    }

    public Boolean getSedOrigenExterno() {
        return sedOrigenExterno;
    }

    public void setSedOrigenExterno(Boolean sedOrigenExterno) {
        this.sedOrigenExterno = sedOrigenExterno;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.sedPk);
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
        final SgSede other = (SgSede) obj;
        if (!Objects.equals(this.sedPk, other.sedPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.web.dto.SgSede{" + "sedPk=" + sedPk + '}';
    }

  
    

}
