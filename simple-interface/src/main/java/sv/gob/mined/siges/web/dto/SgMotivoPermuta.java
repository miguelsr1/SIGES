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


/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mpePk", scope = SgMotivoPermuta.class)
public class SgMotivoPermuta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long mpePk;
    
    private String mpeCodigo;
    
    private Boolean mpeHabilitado;
    
    private String mpeNombre;
    
    private String mpeNombreBusqueda;
    
    private String mpeDescripcion;
    
    private LocalDateTime mpeUltModFecha;
    
    private String mpeUltModUsuario;
    
    private Integer mpeVersion;

    public SgMotivoPermuta() {
        this.mpeHabilitado = Boolean.TRUE;
    }


    public SgMotivoPermuta(Long mpePk) {
        this.mpePk = mpePk;
    }

    public Long getMpePk() {
        return mpePk;
    }

    public void setMpePk(Long mpePk) {
        this.mpePk = mpePk;
    }

    public String getMpeCodigo() {
        return mpeCodigo;
    }

    public void setMpeCodigo(String mpeCodigo) {
        this.mpeCodigo = mpeCodigo;
    }

    public Boolean getMpeHabilitado() {
        return mpeHabilitado;
    }

    public void setMpeHabilitado(Boolean mpeHabilitado) {
        this.mpeHabilitado = mpeHabilitado;
    }

    public String getMpeNombre() {
        return mpeNombre;
    }

    public void setMpeNombre(String mpeNombre) {
        this.mpeNombre = mpeNombre;
    }

    public String getMpeNombreBusqueda() {
        return mpeNombreBusqueda;
    }

    public void setMpeNombreBusqueda(String mpeNombreBusqueda) {
        this.mpeNombreBusqueda = mpeNombreBusqueda;
    }

    public String getMpeDescripcion() {
        return mpeDescripcion;
    }

    public void setMpeDescripcion(String mpeDescripcion) {
        this.mpeDescripcion = mpeDescripcion;
    }

    public LocalDateTime getMpeUltModFecha() {
        return mpeUltModFecha;
    }

    public void setMpeUltModFecha(LocalDateTime mpeUltModFecha) {
        this.mpeUltModFecha = mpeUltModFecha;
    }

    public String getMpeUltModUsuario() {
        return mpeUltModUsuario;
    }

    public void setMpeUltModUsuario(String mpeUltModUsuario) {
        this.mpeUltModUsuario = mpeUltModUsuario;
    }

    public Integer getMpeVersion() {
        return mpeVersion;
    }

    public void setMpeVersion(Integer mpeVersion) {
        this.mpeVersion = mpeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mpePk != null ? mpePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgMotivoPermuta)) {
            return false;
        }
        SgMotivoPermuta other = (SgMotivoPermuta) object;
        if ((this.mpePk == null && other.mpePk != null) || (this.mpePk != null && !this.mpePk.equals(other.mpePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMotivoPermuta[ mpePk=" + mpePk + " ]";
    }
    
}
