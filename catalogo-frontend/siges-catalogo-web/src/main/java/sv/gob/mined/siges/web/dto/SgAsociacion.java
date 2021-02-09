/*  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "asoPk", scope = SgAsociacion.class) 
public class SgAsociacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long asoPk;
    
    private String asoCodigo;
    
    private String asoNombre;
    
    private String asoNombreBusqueda;
    
    private String asoDescripcion;
    
    private SgTipoAsociacion asoTipoAsociacion;
    
    private Boolean asoExtranjera;
    
    private Boolean asoHabilitado;
    
    private Boolean asoEjecutaFondosMined;
    
    private String asoNit;
    
    private String asoNombreRepresentanteLegal;
    
    private Integer asoAnioFundacion;
    
    private String asoResponsableInstitucional;
    
    private String asoCorreo;
    
    private String asoCorreoAlternativo;
    
    private String asoNombreCoordiandor;
    
    private String asoTelefonoCoordiandor;
    
    private String asoCorreoCoordiandor;
    
    private String asoNombreResponsableAdministrativo;
    
    private String asoTelefonoResponsableAdministrativo;
    
    private String asoCorreoResponsableAdministrativo;
    
    private SgDireccion asoDireccionFk;
    
    private LocalDateTime asoUltModFecha;
    
    private String asoUltModUsuario;
    
    private Integer asoVersion;
    
    private List<SgTelefono> asoTelefonos;
    
    private List<SgProyectoInstitucional> asoProyectos;

    public SgAsociacion() {
        this.asoHabilitado = Boolean.TRUE;
        this.asoEjecutaFondosMined = Boolean.FALSE;
        this.asoExtranjera = Boolean.FALSE;
        this.asoDireccionFk = new SgDireccion();
        this.asoProyectos = new ArrayList<>();
        this.asoTelefonos = new ArrayList<>();
    }

    public SgAsociacion(Long asoPk) {
        this.asoPk = asoPk;
    }

    public Long getAsoPk() {
        return asoPk;
    }

    public void setAsoPk(Long asoPk) {
        this.asoPk = asoPk;
    }

    public String getAsoCodigo() {
        return asoCodigo;
    }

    public void setAsoCodigo(String asoCodigo) {
        this.asoCodigo = asoCodigo;
    }

    public String getAsoNombre() {
        return asoNombre;
    }

    public void setAsoNombre(String asoNombre) {
        this.asoNombre = asoNombre;
    }

    public String getAsoNombreBusqueda() {
        return asoNombreBusqueda;
    }

    public void setAsoNombreBusqueda(String asoNombreBusqueda) {
        this.asoNombreBusqueda = asoNombreBusqueda;
    }

    public String getAsoDescripcion() {
        return asoDescripcion;
    }

    public void setAsoDescripcion(String asoDescripcion) {
        this.asoDescripcion = asoDescripcion;
    }

    public Boolean getAsoExtranjera() {
        return asoExtranjera;
    }

    public void setAsoExtranjera(Boolean asoExtranjera) {
        this.asoExtranjera = asoExtranjera;
    }

    public Boolean getAsoHabilitado() {
        return asoHabilitado;
    }

    public void setAsoHabilitado(Boolean asoHabilitado) {
        this.asoHabilitado = asoHabilitado;
    }

    public Boolean getAsoEjecutaFondosMined() {
        return asoEjecutaFondosMined;
    }

    public void setAsoEjecutaFondosMined(Boolean asoEjecutaFondosMined) {
        this.asoEjecutaFondosMined = asoEjecutaFondosMined;
    }

    public String getAsoNit() {
        return asoNit;
    }

    public void setAsoNit(String asoNit) {
        this.asoNit = asoNit;
    }

    public String getAsoNombreRepresentanteLegal() {
        return asoNombreRepresentanteLegal;
    }

    public void setAsoNombreRepresentanteLegal(String asoNombreRepresentanteLegal) {
        this.asoNombreRepresentanteLegal = asoNombreRepresentanteLegal;
    }

    public SgDireccion getAsoDireccionFk() {
        return asoDireccionFk;
    }

    public void setAsoDireccionFk(SgDireccion asoDireccionFk) {
        this.asoDireccionFk = asoDireccionFk;
    }

    public LocalDateTime getAsoUltModFecha() {
        return asoUltModFecha;
    }

    public void setAsoUltModFecha(LocalDateTime asoUltModFecha) {
        this.asoUltModFecha = asoUltModFecha;
    }

    public String getAsoUltModUsuario() {
        return asoUltModUsuario;
    }

    public void setAsoUltModUsuario(String asoUltModUsuario) {
        this.asoUltModUsuario = asoUltModUsuario;
    }

    public Integer getAsoVersion() {
        return asoVersion;
    }

    public void setAsoVersion(Integer asoVersion) {
        this.asoVersion = asoVersion;
    }

    public List<SgTelefono> getAsoTelefonos() {
        return asoTelefonos;
    }

    public void setAsoTelefonos(List<SgTelefono> asoTelefonos) {
        this.asoTelefonos = asoTelefonos;
    }

    public SgTipoAsociacion getAsoTipoAsociacion() {
        return asoTipoAsociacion;
    }

    public void setAsoTipoAsociacion(SgTipoAsociacion asoTipoAsociacion) {
        this.asoTipoAsociacion = asoTipoAsociacion;
    }

    public List<SgProyectoInstitucional> getAsoProyectos() {
        return asoProyectos;
    }

    public void setAsoProyectos(List<SgProyectoInstitucional> asoProyectos) {
        this.asoProyectos = asoProyectos;
    }

    public Integer getAsoAnioFundacion() {
        return asoAnioFundacion;
    }

    public void setAsoAnioFundacion(Integer asoAnioFundacion) {
        this.asoAnioFundacion = asoAnioFundacion;
    }

    public String getAsoResponsableInstitucional() {
        return asoResponsableInstitucional;
    }

    public void setAsoResponsableInstitucional(String asoResponsableInstitucional) {
        this.asoResponsableInstitucional = asoResponsableInstitucional;
    }

    public String getAsoCorreo() {
        return asoCorreo;
    }

    public void setAsoCorreo(String asoCorreo) {
        this.asoCorreo = asoCorreo;
    }

    public String getAsoCorreoAlternativo() {
        return asoCorreoAlternativo;
    }

    public void setAsoCorreoAlternativo(String asoCorreoAlternativo) {
        this.asoCorreoAlternativo = asoCorreoAlternativo;
    }

    public String getAsoNombreCoordiandor() {
        return asoNombreCoordiandor;
    }

    public void setAsoNombreCoordiandor(String asoNombreCoordiandor) {
        this.asoNombreCoordiandor = asoNombreCoordiandor;
    }

    public String getAsoTelefonoCoordiandor() {
        return asoTelefonoCoordiandor;
    }

    public void setAsoTelefonoCoordiandor(String asoTelefonoCoordiandor) {
        this.asoTelefonoCoordiandor = asoTelefonoCoordiandor;
    }

    public String getAsoCorreoCoordiandor() {
        return asoCorreoCoordiandor;
    }

    public void setAsoCorreoCoordiandor(String asoCorreoCoordiandor) {
        this.asoCorreoCoordiandor = asoCorreoCoordiandor;
    }

    public String getAsoNombreResponsableAdministrativo() {
        return asoNombreResponsableAdministrativo;
    }

    public void setAsoNombreResponsableAdministrativo(String asoNombreResponsableAdministrativo) {
        this.asoNombreResponsableAdministrativo = asoNombreResponsableAdministrativo;
    }

    public String getAsoTelefonoResponsableAdministrativo() {
        return asoTelefonoResponsableAdministrativo;
    }

    public void setAsoTelefonoResponsableAdministrativo(String asoTelefonoResponsableAdministrativo) {
        this.asoTelefonoResponsableAdministrativo = asoTelefonoResponsableAdministrativo;
    }

    public String getAsoCorreoResponsableAdministrativo() {
        return asoCorreoResponsableAdministrativo;
    }

    public void setAsoCorreoResponsableAdministrativo(String asoCorreoResponsableAdministrativo) {
        this.asoCorreoResponsableAdministrativo = asoCorreoResponsableAdministrativo;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asoPk != null ? asoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAsociacion)) {
            return false;
        }
        SgAsociacion other = (SgAsociacion) object;
        if ((this.asoPk == null && other.asoPk != null) || (this.asoPk != null && !this.asoPk.equals(other.asoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAsociacion[ asoPk=" + asoPk + " ]";
    }
    
}
