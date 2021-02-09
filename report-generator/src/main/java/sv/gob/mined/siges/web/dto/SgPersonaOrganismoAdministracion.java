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

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "poaPk", scope = SgPersonaOrganismoAdministracion.class)
public class SgPersonaOrganismoAdministracion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long poaPk;

    private String poaNombre;

    private String poaNombreBusqueda;

    private String poaDui;

    private String poaOcupacion;

    private String poaDomicilio;

    private Boolean poaHabilitado;

    private LocalDateTime poaUltModFecha;

    private String poaUltModUsuario;

    private Integer poaVersion;

    private SgOrganismoAdministracionEscolar poaOrganismoAdministrativoEscolar;

    private SgCargoOAE poaCargo;
    
    private Boolean poaPrerregistro;
    
    private SgPersona poaPersona;
    
    private String poaDuiExpedidoEn;
    
    private LocalDate poaFechaExpedicionDUI;


    public SgPersonaOrganismoAdministracion() {
        this.poaHabilitado = Boolean.TRUE;
        this.poaPrerregistro = Boolean.TRUE;
    }

    public Long getPoaPk() {
        return poaPk;
    }

    public void setPoaPk(Long poaPk) {
        this.poaPk = poaPk;
    }

    public String getPoaNombreBusqueda() {
        return poaNombreBusqueda;
    }

    public void setPoaNombreBusqueda(String poaNombreBusqueda) {
        this.poaNombreBusqueda = poaNombreBusqueda;
    }

    public String getPoaDui() {
        return poaDui;
    }

    public void setPoaDui(String poaDui) {
        this.poaDui = poaDui;
    }

    public String getPoaOcupacion() {
        return poaOcupacion;
    }

    public void setPoaOcupacion(String poaOcupacion) {
        this.poaOcupacion = poaOcupacion;
    }

    public String getPoaDomicilio() {
        return poaDomicilio;
    }

    public void setPoaDomicilio(String poaDomicilio) {
        this.poaDomicilio = poaDomicilio;
    }

    public SgOrganismoAdministracionEscolar getPoaOrganismoAdministrativoEscolar() {
        return poaOrganismoAdministrativoEscolar;
    }

    public void setPoaOrganismoAdministrativoEscolar(SgOrganismoAdministracionEscolar poaOrganismoAdministrativoEscolar) {
        this.poaOrganismoAdministrativoEscolar = poaOrganismoAdministrativoEscolar;
    }

    public SgCargoOAE getPoaCargo() {
        return poaCargo;
    }

    public void setPoaCargo(SgCargoOAE poaCargo) {
        this.poaCargo = poaCargo;
    }


    public String getPoaNombre() {
        return poaNombre;
    }

    public void setPoaNombre(String poaNombre) {
        this.poaNombre = poaNombre;
    }

    public LocalDateTime getPoaUltModFecha() {
        return poaUltModFecha;
    }

    public void setPoaUltModFecha(LocalDateTime poaUltModFecha) {
        this.poaUltModFecha = poaUltModFecha;
    }

    public String getPoaUltModUsuario() {
        return poaUltModUsuario;
    }

    public void setPoaUltModUsuario(String poaUltModUsuario) {
        this.poaUltModUsuario = poaUltModUsuario;
    }

    public Integer getPoaVersion() {
        return poaVersion;
    }

    public void setPoaVersion(Integer poaVersion) {
        this.poaVersion = poaVersion;
    }

    public Boolean getPoaHabilitado() {
        return poaHabilitado;
    }

    public void setPoaHabilitado(Boolean poaHabilitado) {
        this.poaHabilitado = poaHabilitado;
    }

    public Boolean getPoaPrerregistro() {
        return poaPrerregistro;
    }

    public void setPoaPrerregistro(Boolean poaPrerregistro) {
        this.poaPrerregistro = poaPrerregistro;
    }

    public SgPersona getPoaPersona() {
        return poaPersona;
    }

    public void setPoaPersona(SgPersona poaPersona) {
        this.poaPersona = poaPersona;
    }

    public String getPoaDuiExpedidoEn() {
        return poaDuiExpedidoEn;
    }

    public void setPoaDuiExpedidoEn(String poaDuiExpedidoEn) {
        this.poaDuiExpedidoEn = poaDuiExpedidoEn;
    }

    public LocalDate getPoaFechaExpedicionDUI() {
        return poaFechaExpedicionDUI;
    }

    public void setPoaFechaExpedicionDUI(LocalDate poaFechaExpedicionDUI) {
        this.poaFechaExpedicionDUI = poaFechaExpedicionDUI;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (poaPk != null ? poaPk.hashCode() : 0);
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
        final SgPersonaOrganismoAdministracion other = (SgPersonaOrganismoAdministracion) obj;
        if (!Objects.equals(this.poaPk, other.poaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgPersonaOrganismoAdministracion[ poaPk=" + poaPk + " ]";
    }

}
