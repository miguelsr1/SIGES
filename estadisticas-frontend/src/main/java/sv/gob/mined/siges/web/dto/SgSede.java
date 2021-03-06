/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
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
public abstract class SgSede implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long sedPk;

    private String sedCodigo;

    private String sedNombre;

    private String sedNombreBusqueda;
    
    private SgDireccion sedDireccion;

    private Boolean sedHabilitado;

    private String sedTelefono;

    private String sedTelefonoMovil;

    private String sedCorreoElectronico;

    private String sedCorreoElectronico2;

    private String sedSitioWeb;

    private Integer sedAnioInicioAct;

    private String sedPropietario;

    private String sedNota;

    private List<SgJornadaLaboral> sedJornadasLaborales;

    private TipoSede sedTipo;

    private LocalDateTime sedUltModFecha;

    private Boolean sedInternet;

    private String sedVelocidadInternet;

    private String sedProveedorInternet;

    private Integer sedCasosViolacion;

    private Boolean sedReligiosa;

    private String sedReligion;

    private Boolean sedConsejoConsultivoEducacion;

    private Boolean sedComitePedagogico;

    private Boolean sedComitePadresMadres;

    private Boolean sedComiteEstudiantil;

    private String sedUltModUsuario;

    private Integer sedVersion;

    private Boolean sedMigracion;

    private SgSede sedSedeAdscritaDe;

    private String sedDireccionNotificacion;


    public SgSede() {
    }

    @JsonIgnore
    public String getSedCodigoNombre() {
        return this.sedCodigo + " - " + this.sedNombre;
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

    public Boolean getSedHabilitado() {
        return sedHabilitado;
    }

    public void setSedHabilitado(Boolean sedHabilitado) {
        this.sedHabilitado = sedHabilitado;
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

    public String getSedCorreoElectronico2() {
        return sedCorreoElectronico2;
    }

    public void setSedCorreoElectronico2(String sedCorreoElectronico2) {
        this.sedCorreoElectronico2 = sedCorreoElectronico2;
    }

    public String getSedSitioWeb() {
        return sedSitioWeb;
    }

    public void setSedSitioWeb(String sedSitioWeb) {
        this.sedSitioWeb = sedSitioWeb;
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

    public LocalDateTime getSedUltModFecha() {
        return sedUltModFecha;
    }

    public void setSedUltModFecha(LocalDateTime sedUltModFecha) {
        this.sedUltModFecha = sedUltModFecha;
    }

    public String getSedUltModUsuario() {
        return sedUltModUsuario;
    }

    public void setSedUltModUsuario(String sedUltModUsuario) {
        this.sedUltModUsuario = sedUltModUsuario;
    }

    public Integer getSedVersion() {
        return sedVersion;
    }

    public void setSedVersion(Integer sedVersion) {
        this.sedVersion = sedVersion;
    }

    public Boolean getSedMigracion() {
        return sedMigracion;
    }

    public void setSedMigracion(Boolean sedMigracion) {
        this.sedMigracion = sedMigracion;
    }

    public String getSedNombreBusqueda() {
        return sedNombreBusqueda;
    }

    public void setSedNombreBusqueda(String sedNombreBusqueda) {
        this.sedNombreBusqueda = sedNombreBusqueda;
    }

    public List<SgJornadaLaboral> getSedJornadasLaborales() {
        return sedJornadasLaborales;
    }

    public void setSedJornadasLaborales(List<SgJornadaLaboral> sedJornadasLaborales) {
        this.sedJornadasLaborales = sedJornadasLaborales;
    }

    public SgSede getSedSedeAdscritaDe() {
        return sedSedeAdscritaDe;
    }

    public void setSedSedeAdscritaDe(SgSede sedSedeAdscritaDe) {
        this.sedSedeAdscritaDe = sedSedeAdscritaDe;
    }

    public String getSedDireccionNotificacion() {
        return sedDireccionNotificacion;
    }

    public void setSedDireccionNotificacion(String sedDireccionNotificacion) {
        this.sedDireccionNotificacion = sedDireccionNotificacion;
    }
    
    public Boolean getSedInternet() {
        return sedInternet;
    }

    public void setSedInternet(Boolean sedInternet) {
        this.sedInternet = sedInternet;
    }

    public String getSedVelocidadInternet() {
        return sedVelocidadInternet;
    }

    public void setSedVelocidadInternet(String sedVelocidadInternet) {
        this.sedVelocidadInternet = sedVelocidadInternet;
    }

    public SgDireccion getSedDireccion() {
        return sedDireccion;
    }

    public void setSedDireccion(SgDireccion sedDireccion) {
        this.sedDireccion = sedDireccion;
    }

    public String getSedProveedorInternet() {
        return sedProveedorInternet;
    }

    public void setSedProveedorInternet(String sedProveedorInternet) {
        this.sedProveedorInternet = sedProveedorInternet;
    }

    public Integer getSedCasosViolacion() {
        return sedCasosViolacion;
    }

    public void setSedCasosViolacion(Integer sedCasosViolacion) {
        this.sedCasosViolacion = sedCasosViolacion;
    }

    public Boolean getSedReligiosa() {
        return sedReligiosa;
    }

    public void setSedReligiosa(Boolean sedReligiosa) {
        this.sedReligiosa = sedReligiosa;
    }

    public String getSedReligion() {
        return sedReligion;
    }

    public void setSedReligion(String sedReligion) {
        this.sedReligion = sedReligion;
    }

    public Boolean getSedConsejoConsultivoEducacion() {
        return sedConsejoConsultivoEducacion;
    }

    public void setSedConsejoConsultivoEducacion(Boolean sedConsejoConsultivoEducacion) {
        this.sedConsejoConsultivoEducacion = sedConsejoConsultivoEducacion;
    }

    public Boolean getSedComitePedagogico() {
        return sedComitePedagogico;
    }

    public void setSedComitePedagogico(Boolean sedComitePedagogico) {
        this.sedComitePedagogico = sedComitePedagogico;
    }

    public Boolean getSedComitePadresMadres() {
        return sedComitePadresMadres;
    }

    public void setSedComitePadresMadres(Boolean sedComitePadresMadres) {
        this.sedComitePadresMadres = sedComitePadresMadres;
    }

    public Boolean getSedComiteEstudiantil() {
        return sedComiteEstudiantil;
    }

    public void setSedComiteEstudiantil(Boolean sedComiteEstudiantil) {
        this.sedComiteEstudiantil = sedComiteEstudiantil;
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
