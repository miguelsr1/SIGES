/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dedPk", scope = SgDireccionDepartamental.class)
public class SgDireccionDepartamental implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dedPk;

    private String dedNombre;
    
    private String dedNit;
    
    private String dedTelefono;
    
    private String dedFax;
    
    private String dedIpAutorizada;

    private Boolean dedHabilitado;

    private LocalDateTime dedUltModFecha;

    private String dedUltModUsuario;

    private Integer dedVersion;

    private SgDepartamento dedDepartamentoFk;

    private String decDirectorCargo;
    private String decDirectorNombre;
    private String decDirectorDomicilio;
    private SgProfesion dedDirectorProfesionFk;
    private String decPagadorCargo;
    private String decPagadorNombre;
    private String decRefrendarioCargo;
    private String decRefrendarioNombre;

    public SgDireccionDepartamental() {
        this.dedHabilitado = Boolean.TRUE;
    }

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }

    public String getDedNombre() {
        return dedNombre;
    }

    public void setDedNombre(String dedNombre) {
        this.dedNombre = dedNombre;
    }

    public String getDedNit() {
        return dedNit;
    }

    public void setDedNit(String dedNit) {
        this.dedNit = dedNit;
    }
    
    public String getDedTelefono() {
        return dedTelefono;
    }

    public void setDedTelefono(String dedTelefono) {
        this.dedTelefono = dedTelefono;
    }

    public String getDedFax() {
        return dedFax;
    }

    public void setDedFax(String dedFax) {
        this.dedFax = dedFax;
    }

    public String getDedIpAutorizada() {
        return dedIpAutorizada;
    }

    public void setDedIpAutorizada(String dedIpAutorizada) {
        this.dedIpAutorizada = dedIpAutorizada;
    }
    
    public LocalDateTime getDedUltModFecha() {
        return dedUltModFecha;
    }

    public void setDedUltModFecha(LocalDateTime dedUltModFecha) {
        this.dedUltModFecha = dedUltModFecha;
    }

    public String getDedUltModUsuario() {
        return dedUltModUsuario;
    }

    public void setDedUltModUsuario(String dedUltModUsuario) {
        this.dedUltModUsuario = dedUltModUsuario;
    }

    public Integer getDedVersion() {
        return dedVersion;
    }

    public void setDedVersion(Integer dedVersion) {
        this.dedVersion = dedVersion;
    }

    public Boolean getDedHabilitado() {
        return dedHabilitado;
    }

    public void setDedHabilitado(Boolean dedHabilitado) {
        this.dedHabilitado = dedHabilitado;
    }

    public SgDepartamento getDedDepartamentoFk() {
        return dedDepartamentoFk;
    }

    public void setDedDepartamentoFk(SgDepartamento dedDepartamentoFk) {
        this.dedDepartamentoFk = dedDepartamentoFk;
    }

    public String getDecDirectorCargo() {
        return decDirectorCargo;
    }

    public void setDecDirectorCargo(String decDirectorCargo) {
        this.decDirectorCargo = decDirectorCargo;
    }

    public String getDecDirectorNombre() {
        return decDirectorNombre;
    }

    public void setDecDirectorNombre(String decDirectorNombre) {
        this.decDirectorNombre = decDirectorNombre;
    }

    public String getDecDirectorDomicilio() {
        return decDirectorDomicilio;
    }

    public void setDecDirectorDomicilio(String decDirectorDomicilio) {
        this.decDirectorDomicilio = decDirectorDomicilio;
    }

    public SgProfesion getDedDirectorProfesionFk() {
        return dedDirectorProfesionFk;
    }

    public void setDedDirectorProfesionFk(SgProfesion dedDirectorProfesionFk) {
        this.dedDirectorProfesionFk = dedDirectorProfesionFk;
    }
    

    public String getDecPagadorCargo() {
        return decPagadorCargo;
    }

    public void setDecPagadorCargo(String decPagadorCargo) {
        this.decPagadorCargo = decPagadorCargo;
    }

    public String getDecPagadorNombre() {
        return decPagadorNombre;
    }

    public void setDecPagadorNombre(String decPagadorNombre) {
        this.decPagadorNombre = decPagadorNombre;
    }

    public String getDecRefrendarioCargo() {
        return decRefrendarioCargo;
    }

    public void setDecRefrendarioCargo(String decRefrendarioCargo) {
        this.decRefrendarioCargo = decRefrendarioCargo;
    }

    public String getDecRefrendarioNombre() {
        return decRefrendarioNombre;
    }

    public void setDecRefrendarioNombre(String decRefrendarioNombre) {
        this.decRefrendarioNombre = decRefrendarioNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dedPk != null ? dedPk.hashCode() : 0);
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
        final SgDireccionDepartamental other = (SgDireccionDepartamental) obj;
        if (!Objects.equals(this.dedPk, other.dedPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgDireccionDepartamental[ dedPk=" + dedPk + " ]";
    }

}
